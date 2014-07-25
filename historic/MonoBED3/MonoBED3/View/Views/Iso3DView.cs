using System;
using Gtk;

namespace MonoBED3
{
	[System.ComponentModel.ToolboxItem(true)]
	public class Iso3DView : Gtk.DrawingArea, ContextAware, ContextListener, Zoomable
	{
		//-----------------------------------------------------------------------------		
		private Vector3 myCursorRef;
		private bool shouldDrawGrid = false;
		private Vector3 dragBeginPoint;
		private int panX;
		private int panY;
		private ZoomLevelListener listener;
		private byte zoom; // 1 - mais afastado, 255 - mais próximo.
		bool shouldDrawAsSolids = true;
		private bool drawExtendingLines;
		
		
		public	void setDrawExtendingLines( bool shouldDrawLines ) {
			
			drawExtendingLines = shouldDrawLines;
		}
		
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
		
		public void contextModified() {
		}
		
		public Iso3DView ( ZoomLevelListener listener)
		{
			this.listener = listener;
			zoom = 10;
			listener.zoomLevelChanged( 10 );
			AddEvents((int)Gdk.EventMask.AllEventsMask);
		}
		
		private int getScreenX( int mapX, int mapZ ) {
			return panX + ( mapX - mapZ ) * zoom;
		}
		
		private int getScreenY( int mapY, int mapZ ) {
			return 255 - ( - panY + ( mapY - mapZ ) * zoom );
		}		
		
		private int getScreenX( float mapX, float mapZ ) {
			return getScreenX( ( int )mapX, (int) mapZ );
		}
		
		private int getScreenY( float mapY, float mapZ ) {
			return getScreenY( ( int )mapY, (int) mapZ );
		}				
		
		private float convertToUniversalX( float x, float z )
		{
			return ( x - panX  ) / zoom;
		}
		
		private float convertToUniversalY( float y, float z )
		{
			return ( 255 - ( y - panY ) ) / zoom;
		}
		
		private float convertToUniversalZ( float z )
		{
			return z;
		}
		
		protected override bool OnMotionNotifyEvent (Gdk.EventMotion evnt)
		{
			
			if ( dragBeginPoint != null ) {
				panX += ( int ) ( evnt.X - dragBeginPoint.getX() );
				panY += ( int ) ( evnt.Y - dragBeginPoint.getY() );
				dragBeginPoint.setVec( ( float )evnt.X, ( float )evnt.Y, 0.0f );
				QueueDraw();
			}
			
			
			return base.OnMotionNotifyEvent (evnt);
		}
		
		
		
		protected override bool OnButtonReleaseEvent (Gdk.EventButton evnt)
		{
			dragBeginPoint = null;
			
			return base.OnButtonReleaseEvent (evnt);
		}
		
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
		
		
		private void updateCursor( float x, float y, float z ) {
			
			myCursorRef.setX( convertToUniversalX( x, z ) );
			myCursorRef.setY( convertToUniversalY( y, z ) );	
		}
		
		public void setDrawAsSolids( bool shouldDrawAsSolids ) {
			
			this.shouldDrawAsSolids = shouldDrawAsSolids;
		}
		
		protected override bool OnButtonPressEvent (Gdk.EventButton ev)
		{

			float currentZ =  this.getContext().getCursor().getZ();
			switch (ev.Button)
			{
			case 1:				
				updateCursor( ( float ) ev.X, ( float ) ev.Y, currentZ );
				dragBeginPoint = new Vector3( ( float ) ev.X, ( float )ev.Y, 0.0f );
				getContext().setCurrentEntityFromCursor();		
				
				break;
				
			case 2:
				
			{
				GameObjectFacade proxy = getContext().getCurrentEntityFacade();
				GameObject obj = (GameObject)proxy.getObject();
				updateCursor( ( float ) ev.X, ( float ) ev.Y, currentZ );
				
				if ( MainWindow.altButtonBehaviour != 0 ) {
					obj.moveTo( myCursorRef.getX(), myCursorRef.getY() , myCursorRef.getZ() );
				} else {
					if (obj is Sector) {
						((Sector)obj).setDX( myCursorRef.getX() - obj.getX());
						((Sector)obj).setDY( myCursorRef.getY() + obj.getY());					
					}
				}
				
				
			}
				
				break;
			case 3:		
			{
				GameObjectFacade proxy = getContext().getCurrentEntityFacade();
				GameObject obj = (GameObject)proxy.getObject();
				updateCursor( ( float ) ev.X, ( float ) ev.Y, currentZ );
				
				if ( MainWindow.altButtonBehaviour != 0 ) {
					if (obj is Sector) {
						((Sector)obj).setDX(myCursorRef.getX() - obj.getX());
						((Sector)obj).setDY(- myCursorRef.getY() + obj.getY());					
					}
				} else {
					obj.moveTo( myCursorRef.getX(), myCursorRef.getY() , myCursorRef.getZ() );
				}						
			}
				break;				
			}
			
			getContext().askForScreenRefresh();			
			return base.OnButtonPressEvent (ev);
		}
		
		private void drawGrid()
		{		
			int sizeX, sizeY, strideX, strideY;
			StateType state;
			Gdk.GC gc;
			
			state = StateType.Active;
			gc = Style.BaseGC(state);
			
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
		
		private void drawWorldLimits()
		{		
			
			StateType state;
			Gdk.GC gc;
			
			state = StateType.Active;
			gc = Style.BaseGC(state);
			
			gc.RgbFgColor = new Gdk.Color( 0, 0, 0 );
			
			GdkWindow.DrawLine(gc, getScreenX( 0, 0 ), getScreenY( 0, 0 ), getScreenX( 255, 0 ), getScreenY( 0, 0 ) );
			GdkWindow.DrawLine(gc, getScreenX( 0, 0 ), getScreenY( 0, 0 ), getScreenX( 0, 0 ), getScreenY( 255, 0 ) );
			GdkWindow.DrawLine(gc, getScreenX( 0, 0 ), getScreenY( 255, 0 ), getScreenX( 255, 0 ), getScreenY( 255, 0 ) );
			GdkWindow.DrawLine(gc, getScreenX( 255, 0 ), getScreenY( 0, 0 ), getScreenX( 255, 0 ), getScreenY( 255, 0 ) );


			GdkWindow.DrawLine(gc, getScreenX( 0, 255 ), getScreenY( 0, 255 ), getScreenX( 255, 255 ), getScreenY( 0, 255 ) );
			GdkWindow.DrawLine(gc, getScreenX( 0, 255 ), getScreenY( 0, 255 ), getScreenX( 0, 255 ), getScreenY( 255, 255 ) );
			GdkWindow.DrawLine(gc, getScreenX( 0, 255 ), getScreenY( 255, 255 ), getScreenX( 255, 255 ), getScreenY( 255, 255 ) );
			GdkWindow.DrawLine(gc, getScreenX( 255, 255 ), getScreenY( 0, 255 ), getScreenX( 255, 255 ), getScreenY( 255, 255 ) );

//			GdkWindow.DrawLine(gc, getScreenX( 0 ), getScreenY( 0 ), getScreenX( 255 ), getScreenY( 0 ) );
//			GdkWindow.DrawLine(gc, getScreenX( 0 ), getScreenY( 0 ), getScreenX( 0 ), getScreenY( 255 ) );
//			GdkWindow.DrawLine(gc, getScreenX( 0 ), getScreenY( 255 ), getScreenX( 255 ), getScreenY( 255 ) );
//			GdkWindow.DrawLine(gc, getScreenX( 255 ), getScreenY( 0 ), getScreenX( 255 ), getScreenY( 255 ) );


		}
		
		private void drawSector( SectorFacade sf, Gdk.GC gc ) {
			
			gc.RgbFgColor = sf.getGtkColor( 0 );


			float cz0;
			float cz1;

			cz0 = sf.getZ();
			cz1 = cz0 + sf.getDZ();

			int x0 = getScreenX( sf.getX(), cz0 );
			int x1 = getScreenX( sf.getX() + sf.getDX(), cz0 );
			int z0 = getScreenY( sf.getY(), cz0 );
			int z1 = getScreenY( sf.getY() + sf.getDY(), cz0 );
			
			if ( shouldDrawAsSolids) {
				GdkWindow.DrawRectangle(gc, true, x0, z1, x1 - x0, z0 - z1 );				
			}
			
			for ( int c = 0; c < 6; ++c ) {
				
				if ( ( ( Sector )sf.getObject() ).IsDoorAt( c ) ) 
					gc.RgbFgColor = new Gdk.Color( 255, 0, 0 );
				else if ( getContext().getCurrentSectorId() == sf.getId() )
					gc.RgbFgColor = new Gdk.Color( 0, 0, 255 );
				else if ( ( ( Sector ) sf.getObject() ).getLink( c ) == 0 )
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
		
		
		protected override bool OnExposeEvent (Gdk.EventExpose ev)
		{
			base.OnExposeEvent (ev);
			
			if ( getContext().isLocked() ) {
				return true;
			}
			
			StateType state;
			Gdk.GC gc;
			GameObjectFacade go = null;
			int current;			
			int width;
			int height;
			ev.Window.GetSize( out width, out height );
			state = StateType.Normal;
			gc = Style.BaseGC(state);
			GdkWindow.DrawRectangle(gc,true,0,0,512,255);
			drawWorldLimits();
			
			if ( shouldDrawGrid ) {
				drawGrid();
			}
			
			
			state = StateType.Selected;
			gc = Style.BaseGC(state);
			
			
			current = 1;
			SectorFacade sf;
			sf = getContext().getSectorFacade(current);
			
			while (sf != null)
			{
				drawSector( sf, gc );				
				current++;
				sf = getContext().getSectorFacade(current);
			}	
			
			if (  getContext().getCurrentSectorId() != 0 ) {
				
				sf = getContext().getSectorFacade( getContext().getCurrentSectorId() );
				drawSector( sf, gc );				
			}
			
			gc.RgbFgColor = new Gdk.Color( 0, 255, 0 );
			
			current = 0;
			go = getContext().getActorFacade(current);
			
			while (go != null)
			{
				GdkWindow.DrawRectangle(gc, true, ( int ) go.getX(), ( int ) go.getY(),5,5);
				current++;
				go = getContext().getActorFacade(current);
			}	
			
			if (myCursorRef == null)
				return true; 
			
			gc.RgbFgColor = new Gdk.Color( 0,0,255);

			GdkWindow.DrawLine( gc, 0, getScreenY( myCursorRef.getY(), myCursorRef.getZ() ) + 2, width, getScreenY( myCursorRef.getY(), myCursorRef.getZ() ) + 2);
			GdkWindow.DrawLine( gc, getScreenX( myCursorRef.getX(), myCursorRef.getZ() ) + 2, 0, getScreenX( myCursorRef.getX(), myCursorRef.getZ() ) + 2, height );

			GdkWindow.DrawRectangle(gc, true, getScreenX( myCursorRef.getX(), myCursorRef.getZ() ), getScreenY( myCursorRef.getY(), myCursorRef.getZ() ),5,5);
			
			return true;
		}
		
		
		protected override void OnSizeAllocated (Gdk.Rectangle allocation)
		{
			base.OnSizeAllocated (allocation);
		}
		protected override void OnSizeRequested (ref Gtk.Requisition requisition)
		{
			requisition.Height = 255;
			requisition.Width = 512;
		}
		
		public void reset() {
			this.zoom = 10;
			this.panX = 0;
			this.panY = 0;
			this.QueueDraw();
		}
		
		public void setDrawGrid( bool shouldDrawGrid ) {
			this.shouldDrawGrid = shouldDrawGrid;
		}
	}
}

