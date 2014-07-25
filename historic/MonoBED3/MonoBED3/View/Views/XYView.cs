using System;
using Gtk;
using br.odb.utils.math;
using br.odb.libscene;
using System.Diagnostics;

namespace MonoBED3
{
	[System.ComponentModel.ToolboxItem(true)]
	public class XYView : Gtk.DrawingArea, ContextAware, ContextListener, Zoomable, Drawable
	{
//-----------------------------------------------------------------------------		
		private Cursor myCursorRef;
		private bool shouldDrawGrid = false;
		private Vec3 dragBeginPoint;
		private int panX;
		private int panY;
		Stopwatch sw;
		private ZoomLevelListener listener;
		private byte zoom; // 1 - mais afastado, 255 - mais próximo.
		bool shouldDrawAsSolids = true;
		private bool drawExtendingLines;
//-----------------------------------------------------------------------------		
		public void setZoomLevel( byte zoom ) {
			this.zoom =  zoom;
			QueueDraw();
		}
//-----------------------------------------------------------------------------		
		public void setContext(EditorContext instance)
		{
			myCursorRef = getContext().getCursor();			
		}
//-----------------------------------------------------------------------------		
		public EditorContext getContext()
		{
			return MainWindow.editContext;	
		}
//-----------------------------------------------------------------------------
		public void contextModified() {
		}
//-----------------------------------------------------------------------------		
		public void setDrawExtendingLines( bool shouldDrawLines ) {
			
			drawExtendingLines = shouldDrawLines;
		}
//-----------------------------------------------------------------------------		
		public XYView ( ZoomLevelListener listener)
		{
			this.listener = listener;
			zoom = 10;
			listener.zoomLevelChanged( 10 );
			AddEvents((int)Gdk.EventMask.AllEventsMask);
		}
//-----------------------------------------------------------------------------
		private int getScreenX( int mapX ) {
			return panX + ( mapX ) * zoom;
		}
//-----------------------------------------------------------------------------
		private int getScreenY( int mapY ) {
			return 255 - ( - panY + ( mapY ) * zoom );
		}		
//-----------------------------------------------------------------------------		
		private int getScreenX( float mapX ) {
			return getScreenX( ( int )mapX );
		}
//-----------------------------------------------------------------------------		
		private int getScreenY( float mapY ) {
			return getScreenY( ( int )mapY );
		}				
//-----------------------------------------------------------------------------
		private float convertToUniversalX( float x)
		{
			return ( x - panX  ) / zoom;
		}
//-----------------------------------------------------------------------------
		private float convertToUniversalY( float y)
		{
			return ( 255 - ( y - panY ) ) / zoom;
		}
//-----------------------------------------------------------------------------
		private float convertToUniversalZ( float z)
		{
			return z;
		}
//-----------------------------------------------------------------------------
		protected override bool OnMotionNotifyEvent (Gdk.EventMotion evnt)
		{
			
			if ( dragBeginPoint != null ) {
				panX += ( int ) ( evnt.X - dragBeginPoint.getX() );
				panY += ( int ) ( evnt.Y - dragBeginPoint.getY() );
				dragBeginPoint.set( ( float )evnt.X, ( float )evnt.Y, 0.0f );
				QueueDraw();
			}
			
			
			return base.OnMotionNotifyEvent (evnt);
		}
//-----------------------------------------------------------------------------
		protected override bool OnButtonReleaseEvent (Gdk.EventButton evnt)
		{

			QueueDraw();
			if (evnt.Button != 1)
				return true;

			if ( sw == null || sw.ElapsedMilliseconds < 200 ) {

				updateCursor( ( float ) evnt.X, ( float ) evnt.Y );
				dragBeginPoint = new Vec3( ( float ) evnt.X, ( float )evnt.Y, 0.0f );
				getContext().setCurrentSectorFromCursor();	
				getContext ().notifyChange ();
			}

			if ( sw != null )
				sw.Stop ();

			dragBeginPoint = null;
			QueueDraw();
			return base.OnButtonReleaseEvent (evnt);
		}
//-----------------------------------------------------------------------------
		protected override bool OnScrollEvent (Gdk.EventScroll evnt)
		{
			if ( evnt.Direction == Gdk.ScrollDirection.Up ) {
				++zoom;
			} else {
				--zoom;
			}
			
			if ( zoom <= 0 )
				zoom = 1;
			
			if ( zoom >= 254 )
				zoom = 254;
				
			listener.zoomLevelChanged( zoom );
			
			return base.OnScrollEvent (evnt);
		}
//-----------------------------------------------------------------------------
		private void updateCursor( float x, float y ) {

			myCursorRef.setX( convertToUniversalX( x ) );
			myCursorRef.setY( convertToUniversalY( y ) );	
		}
//-----------------------------------------------------------------------------		
		public void setDrawAsSolids( bool shouldDrawAsSolids ) {
			
			this.shouldDrawAsSolids = shouldDrawAsSolids;
		}
//-----------------------------------------------------------------------------
		protected override bool OnButtonPressEvent (Gdk.EventButton ev)
		{
			switch (ev.Button)
			{
			case 1:				

				if ( dragBeginPoint != null )
					return true ;

				sw = Stopwatch.StartNew();

				dragBeginPoint = new Vec3( ( float ) ev.X,( float )ev.Y,  0.0f );				
				break;
				
			case 2:
				
				{
				Sector obj = getContext().getCurrentSector();
				
				if ( obj == null )
					break;
				
				updateCursor( ( float ) ev.X, ( float ) ev.Y );
					
				if ( MainWindow.altButtonBehaviour != 0 ) {
					obj.moveTo( myCursorRef );
				} else {
					obj.setDX( myCursorRef.getX() - obj.getX0());
					obj.setDY( myCursorRef.getY() + obj.getY0());					
				}
				
				
				}

				break;
			case 3:		
				{
				Sector obj = getContext().getCurrentSector();
				
				if ( obj == null )
					break;
				
				updateCursor( ( float ) ev.X, ( float ) ev.Y );

				if ( MainWindow.altButtonBehaviour != 0 ) {

					obj.setDX(myCursorRef.getX() - obj.getX0());
					obj.setDY(- myCursorRef.getY() + obj.getY0());					
				} else {
					obj.moveTo( myCursorRef );
				}						
			}
			break;				
			}
			
			getContext().askForScreenRefresh();			
			return base.OnButtonPressEvent (ev);
		}
//-----------------------------------------------------------------------------
		private void drawGrid()
		{		
			int sizeX, sizeY, strideX, strideY;
			Gdk.GC gc;

			gc = new Gdk.GC( base.GdkWindow );
			
			sizeX = 512;
			sizeY = 512;
			strideX = zoom + 1;
			strideY = zoom + 1;
			
			byte color = ( byte )( 255 - zoom );
			gc.RgbFgColor = new Gdk.Color( color, color, color );
			
			for (int x=0; x < sizeX * strideX; x+= strideX )		
				GdkWindow.DrawLine(gc, panX + x,0, panX + x, sizeY);				
			
			
			for (int y=0; y < sizeY * strideY; y+= strideY )		
				GdkWindow.DrawLine(gc,0, panY + y,sizeX, panY + y);		
		}
//-----------------------------------------------------------------------------
		private void drawWorldLimits()
		{		
			
			Gdk.GC gc;

			gc = new Gdk.GC( base.GdkWindow );
			
			gc.RgbFgColor = new Gdk.Color( 0, 0, 0 );
			
			GdkWindow.DrawLine(gc, getScreenX( 0 ), getScreenY( 0 ), getScreenX( 255 ), getScreenY( 0 ) );
			GdkWindow.DrawLine(gc, getScreenX( 0 ), getScreenY( 0 ), getScreenX( 0 ), getScreenY( 255 ) );
			GdkWindow.DrawLine(gc, getScreenX( 0 ), getScreenY( 255 ), getScreenX( 255 ), getScreenY( 255 ) );
			GdkWindow.DrawLine(gc, getScreenX( 255 ), getScreenY( 0 ), getScreenX( 255 ), getScreenY( 255 ) );
			
		}
//-----------------------------------------------------------------------------
		private void drawSector( Sector sf, Gdk.GC gc ) {
		
			if (sf == null)
				return;

				bool selected = getContext ().getCurrentSector () == sf;
				gc.RgbFgColor = Utils.getGtkColor (sf.getColor( 0 ), 0); //sf.getEmissiveLightningIntensity() );
				
				int x0 = getScreenX( sf.getX0() );
				int x1 = getScreenX( sf.getX1() );
				int z0 = getScreenY( sf.getY0() );
				int z1 = getScreenY( sf.getY1() );
				
				if ( shouldDrawAsSolids || selected ) {
					GdkWindow.DrawRectangle(gc, true, x0, z1, x1 - x0, z0 - z1 );				
				}
			
				if ( this.drawExtendingLines ) {
					
					int width;
					int height;
					this.GdkWindow.GetSize( out width, out height );
				
					GdkWindow.DrawLine( gc, x0, 0, x0, height );
					GdkWindow.DrawLine( gc, x1, 0, x1, height );
					GdkWindow.DrawLine( gc, 0, z1, width, z1 );
					GdkWindow.DrawLine( gc, 0, z0, width, z0 );
				}
				
				for ( int c = 0; c < 6; ++c ) {
				
					if ( sf.getDoor( c ) != null ) 
						gc.RgbFgColor = new Gdk.Color( 255, 0, 0 );
					else if ( selected )
						gc.RgbFgColor = new Gdk.Color( 0, 0, 255 );
					else if ( sf.getLink( c ) == 0 )
						gc.RgbFgColor = new Gdk.Color( 0, 0, 0 );
					else
						continue;

						
						switch( c ) {
							case br.odb.libscene.Constants.FACE_N:
								GdkWindow.DrawLine( gc, x0, z0, x1, z0 );
								break;
							case br.odb.libscene.Constants.FACE_E:
								GdkWindow.DrawLine( gc, x1, z0, x1, z1 );
								break;
							case br.odb.libscene.Constants.FACE_S:
								GdkWindow.DrawLine( gc, x0, z1, x1, z1 );
								break;							
							case br.odb.libscene.Constants.FACE_W:
								GdkWindow.DrawLine( gc, x0, z0, x0, z1 );
								break;
							case br.odb.libscene.Constants.FACE_FLOOR:
								GdkWindow.DrawLine( gc, x0, z0, x1, z1 );
								break;
							case br.odb.libscene.Constants.FACE_CEILING:
								GdkWindow.DrawLine( gc, x1, z0, x0, z1 );
								break;							
						}
					
				}
		}
//-----------------------------------------------------------------------------				
		protected override bool OnExposeEvent (Gdk.EventExpose ev)
		{
			base.OnExposeEvent (ev);
			
			if ( getContext().isLocked() ) {
				return true;
			}
			
			Gdk.GC gc;
			int current;			
			int width;
			int height;
			ev.Window.GetSize( out width, out height );

			gc = new Gdk.GC( base.GdkWindow );
			gc.RgbFgColor = new Gdk.Color( 255, 255, 255 );
			GdkWindow.DrawRectangle( gc, true, 0, 0, 512, 512 );

			drawWorldLimits();
			
			if ( shouldDrawGrid ) {
				drawGrid();
			}
			
			

			gc = new Gdk.GC( base.GdkWindow );

			
			current = 1;
			Sector sf;
			sf = getContext().getSector(current);
			
			while (sf != null)
			{
				drawSector( sf, gc );				
				current++;
				if ( getContext().getTotalSectors() >= current )
					sf = getContext().getSector(current);
			}	
			
			if (  getContext().getCurrentSector() != getContext().getSector( 0 ) ) {
				
				sf = getContext ().getCurrentSector ();
				drawSector( sf, gc );				
			}
			
			gc.RgbFgColor = new Gdk.Color( 0, 255, 0 );
			

			if (myCursorRef == null)
				return true; 
			
			gc.RgbFgColor = new Gdk.Color( 0,0,255);
			GdkWindow.DrawLine( gc, 0, getScreenY( myCursorRef.getY() ) + 2, width, getScreenY( myCursorRef.getY() ) + 2);
			GdkWindow.DrawLine( gc, getScreenX( myCursorRef.getX() ) + 2, 0, getScreenX( myCursorRef.getX() ) + 2, height );
			GdkWindow.DrawRectangle(gc, true, getScreenX( myCursorRef.getX() ), getScreenY( myCursorRef.getY() ),5,5);
			
			return true;
		}
//-----------------------------------------------------------------------------
		protected override void OnSizeAllocated (Gdk.Rectangle allocation)
		{
			base.OnSizeAllocated (allocation);
		}
//-----------------------------------------------------------------------------
		protected override void OnSizeRequested (ref Gtk.Requisition requisition)
		{
			requisition.Height = 255;
			requisition.Width = 512;
		}
//-----------------------------------------------------------------------------		
		public void reset() {
			this.zoom = 10;
			this.panX = 0;
			this.panY = 0;
			this.QueueDraw();
		}
//-----------------------------------------------------------------------------		
		public void setDrawGrid( bool shouldDrawGrid ) {
			this.shouldDrawGrid = shouldDrawGrid;
		}
//-----------------------------------------------------------------------------
	}
//-----------------------------------------------------------------------------
}
//-----------------------------------------------------------------------------
