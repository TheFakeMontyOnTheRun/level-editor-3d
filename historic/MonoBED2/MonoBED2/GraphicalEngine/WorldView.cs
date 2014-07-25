// WorldView.cs created with MonoDevelop
// User: daniel at 1:02 AM 8/1/2009
//
// To change standard headers go to Edit->Preferences->Coding->Standard Headers
//

using System;
using Gtk;

namespace MonoBED2
{	
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	public class WorldView
	{
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */		
		public enum ViewMode{XZ,YZ,XY,ISO};
		public enum InteractMode{ SELECT_SECTOR, RESIZE_SECTOR, MOVE_SECTOR, MOVE_MAP, PLACE_CURSOR };
		
		public ViewMode iCurrentViewMode;
		public double zoom;
		public double panx,pany;
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */		
		public WorldView( ViewMode aViewMode) {
			panx=0;
			pany=0;
			zoom=1;
			iCurrentViewMode=aViewMode;
		}
		
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */		
		public void ExposeSector( Gtk.ExposeEventArgs aArgs, Gtk.Style aStyle,Sector aSector,Gtk.StateType aState ) {		
		Gdk.Window win = aArgs.Event.Window;
        Gdk.Rectangle area = new Gdk.Rectangle();
        Gdk.GC SecGC=aStyle.BaseGC (aState);	
			Gdk.Point[] point=new Gdk.Point[4];
			
			switch( ( int ) iCurrentViewMode )
			{
			case ( int ) ViewMode.XZ:
				if (aSector.GetLink(5)!= SectorConstants.NO_SECTOR_LINK && aSector.GetLink(4)!= SectorConstants.NO_SECTOR_LINK ) return;
				area.X=MapToScreenX(win,aSector.GetX0());
				area.Y=MapToScreenY(win,aSector.GetY0());
				area.Width=MapToScreenX(win,aSector.GetX1());
				area.Height=MapToScreenY(win,aSector.GetY1());
			
				if (aSector.GetLink(5)== SectorConstants.NO_SECTOR_LINK )
					SecGC.RgbFgColor=aSector.GetColor(5);
				else
					SecGC.RgbFgColor=aSector.GetColor(4);					
				
				break;
			case ( int ) ViewMode.YZ:
				if (aSector.GetLink(3)!= SectorConstants.NO_SECTOR_LINK && aSector.GetLink(1)!= SectorConstants.NO_SECTOR_LINK ) return;
				area.X=MapToScreenX(win,aSector.GetY0());
				area.Y= 128 -MapToScreenY(win,aSector.GetHeight0());
				area.Width=MapToScreenX(win,aSector.GetY1());
				area.Height= - MapToScreenY(win,aSector.GetHeight1());
				if (aSector.GetLink(3)== SectorConstants.NO_SECTOR_LINK )
				SecGC.RgbFgColor=aSector.GetColor(3);
				else
				SecGC.RgbFgColor=aSector.GetColor(1);					

				break;
			case ( int ) ViewMode.XY:
				if (aSector.GetLink( 0 )!= SectorConstants.NO_SECTOR_LINK && aSector.GetLink(2)!= SectorConstants.NO_SECTOR_LINK ) return;
				area.X=MapToScreenX(win,aSector.GetX0());
				area.Y= 128 - MapToScreenY(win,aSector.GetHeight0());
				area.Width=MapToScreenX(win,aSector.GetX1());
				area.Height= -MapToScreenY(win,aSector.GetHeight1());
				if (aSector.GetLink( 2 )== SectorConstants.NO_SECTOR_LINK )
				SecGC.RgbFgColor=aSector.GetColor(2);
				else
				SecGC.RgbFgColor=aSector.GetColor(0);					

				break;
			case ( int ) ViewMode.ISO:
//////////////
					
				if ( aState == StateType.Insensitive )
					SecGC.RgbFgColor = new Gdk.Color( 0, 0, 0 );
				else
					SecGC.RgbFgColor = new Gdk.Color( 255, 0, 0 );
				
					area.X = MapToScreenX( win, aSector.GetX0() );
					area.Y = MapToScreenY( win, aSector.GetHeight0() );
					area.Width = MapToScreenX(win,aSector.GetX1());
					area.Height = - MapToScreenY(win,aSector.GetHeight1());
					int z0 = aSector.GetY0();
					int z1 = aSector.GetY1();
				
					point[0].X= - MapToScreenX( win, panx ) + area.X + z0;						
					point[0].Y= - MapToScreenY( win, pany ) + area.Y - z0;			
					point[1].X= - MapToScreenX( win, panx ) + area.X + area.Width + z0;			
					point[1].Y= - MapToScreenY( win, pany ) + area.Y - z0;			
					point[2].X= - MapToScreenX( win, panx ) + area.X + area.Width + z0;
					point[2].Y= - MapToScreenY( win, pany ) + area.Y - area.Height - z0;
					point[3].X= - MapToScreenX( win, panx ) + area.X + z0;
					point[3].Y= - MapToScreenY( win, pany ) + area.Y - area.Height - z0;
				 	
					win.DrawPolygon(SecGC,false,point);
				
					point[0].X= - MapToScreenX( win, panx ) + area.X + z0 + z1;						
					point[0].Y= - MapToScreenY( win, pany ) + area.Y - z1 - z0;			
					point[1].X= - MapToScreenX( win, panx ) + area.X + area.Width + z0 + z1;			
					point[1].Y= - MapToScreenY( win, pany ) + area.Y - z1 - z0;			
					point[2].X= - MapToScreenX( win, panx ) + area.X + area.Width + z0 + z1;
					point[2].Y= - MapToScreenY( win, pany ) + area.Y - area.Height - z1 - z0;
					point[3].X= - MapToScreenX( win, panx ) + area.X + z0 + z1;
					point[3].Y= - MapToScreenY( win, pany ) + area.Y - area.Height - z1 - z0;	
				
					win.DrawPolygon(SecGC,false,point);
				
					point[0].X= - MapToScreenX( win, panx ) + area.X + z0;						
					point[0].Y= - MapToScreenY( win, pany ) + area.Y - z0;			
					
					point[1].X= - MapToScreenX( win, panx ) + area.X + aSector.GetY0() + z1;						
					point[1].Y= - MapToScreenY( win, pany ) + area.Y - aSector.GetY1() - z0;			
				
					win.DrawLine( SecGC,point[ 0 ].X, point[ 0 ].Y, point[ 1 ].X, point[ 1 ].Y );
				
					point[ 0 ].X= - MapToScreenX( win, panx ) + area.X + area.Width + z0 + z1;			
					point[ 0 ].Y= - MapToScreenY( win, pany ) + area.Y - z1 - z0;			
					
					point[ 1 ].X= - MapToScreenX( win, panx ) + area.X + area.Width + z0;			
					point[ 1 ].Y= - MapToScreenY( win, pany ) + area.Y - z0;			
				
					win.DrawLine( SecGC,point[ 0 ].X, point[ 0 ].Y, point[ 1 ].X, point[ 1 ].Y );
				
					point[ 0 ].X= - MapToScreenX( win, panx ) + area.X + area.Width + z0;
					point[ 0 ].Y= - MapToScreenY( win, pany ) + area.Y - area.Height - z0;
					
					point[ 1 ].X= - MapToScreenX( win, panx ) + area.X + area.Width + z0 + z1;
					point[ 1 ].Y= - MapToScreenY( win, pany ) + area.Y - area.Height - z1 - z0;
				
					win.DrawLine( SecGC,point[ 0 ].X, point[ 0 ].Y, point[ 1 ].X, point[ 1 ].Y );

					point[0].X= - MapToScreenX( win, panx ) + area.X + z0;
					point[0].Y= - MapToScreenY( win, pany ) + area.Y - area.Height - z0;
					
					point[1].X= - MapToScreenX( win, panx ) + area.X + z0 + z1;
					point[1].Y= - MapToScreenY( win, pany ) + area.Y - area.Height - z1 - z0;	
				
					win.DrawLine( SecGC,point[ 0 ].X, point[ 0 ].Y, point[ 1 ].X, point[ 1 ].Y );
					
				
				return;
////////////////////////								
			}
			
			point[0].X=-MapToScreenX(win,panx)+area.X;			
			point[0].Y=-MapToScreenY(win,pany)+area.Y;
			point[1].X=-MapToScreenX(win,panx)+area.X+area.Width;
			point[1].Y=-MapToScreenY(win,pany)+area.Y;
			point[2].X=-MapToScreenX(win,panx)+area.X+area.Width;
			point[2].Y=-MapToScreenY(win,pany)+area.Y+area.Height;
			point[3].X=-MapToScreenX(win,panx)+area.X;
			point[3].Y=-MapToScreenY(win,pany)+area.Y+area.Height;
	
			win.DrawPolygon(SecGC,true,point);
			
			if ( aSector.solid )			
				SecGC.RgbFgColor = new Gdk.Color(0,0,0);
			else
				SecGC.RgbFgColor = new Gdk.Color( 255, 255, 255 );
			
			win.DrawPolygon(SecGC,false,point);
			
			if ( aState == StateType.Insensitive )
				SecGC.RgbFgColor = new Gdk.Color( 255, 255, 255 );
			else
				SecGC.RgbFgColor = new Gdk.Color( 255, 0, 0 );
			
					for (int c=0;c<4;c++)
				
					if (aSector.GetDoor(c))
					{
						win.DrawLine(SecGC,point[c].X,point[c].Y,point[(c+1)%4].X,point[(c+1)%4].Y);
					}
		
		}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */		
		public  int ScreenToMapX(Gdk.Window aWindow,double aX) {

			int width,height;
			width=0;
			height=0;		
			aWindow.GetSize(out width,out height);        
			double w,h;
			w = width;
			h = height;
	
				
			double factorW;
				
			if ( width > height )
	        	factorW= 255.0 / w;
			else
				factorW = 255.0 / ( w * ( h / w ) );
				
				
			return (int)( ( aX * factorW / zoom )+ panx );
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	public  int ScreenToMapY(Gdk.Window aWindow,double aY) { 
		int width,height;
		width=0;
		height=0;		
		aWindow.GetSize(out width,out height);
		double w,h;
		w = width;
		h = height;
		double factorH;
			
		if ( height > width )
        	factorH = 255.0 / h;
		else
			factorH = 255.0 / ( h * ( w / h ) );
			
			
		return (int)( ( aY * factorH / zoom ) + pany );
	}		
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	public int MapToScreenX(Gdk.Window aWindow,double aX) { 
		int width,height;
		width=0;
		height=0;		
		aWindow.GetSize(out width,out height);  
		double w,h;
		w = width;
		h = height;
			
		double factorW;
			
		if ( width > height )
        	factorW= w / 255.0;
		else
			factorW = ( w * ( h / w ) ) / 255.0;
			
		return (int)((double)aX*factorW*zoom);
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	public int MapToScreenY(Gdk.Window aWindow, double aY) { 
		int width,height;
		width=0;
		height=0;		
		aWindow.GetSize(out width,out height);
		double w,h;
		w = width;
		h = height;
        
		double factorH;
			
		if ( width > height )        	
			factorH = ( h * ( w / h ) ) / 255.0;
		else
			factorH = h / 255.0;
			
		return (int)((double)aY*factorH*zoom);
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	public void fillbg(Gtk.ExposeEventArgs aArgs, Gtk.Style aStyle,Gtk.StateType active,Gtk.StateType inactive) {
		Gdk.Window win = aArgs.Event.Window;
        Gdk.Rectangle area = new Gdk.Rectangle();
        Gdk.GC SecGC;
			
		SecGC =aStyle.BaseGC(inactive);	
		int width,height;
		width=0;
		height=0;		
		win.GetSize(out width,out height);  			
		area.X=0;
		area.Y=0;
		area.Width=width;
		area.Height=height;
		win.DrawRectangle (SecGC, true, area);	
		win.DrawRectangle (SecGC, false, area);
		
		SecGC = aStyle.DarkGC( inactive );	
		Gdk.Color dark = new Gdk.Color( 100, 100, 100 );
		Gdk.Color light = new Gdk.Color( 200, 200, 200 );
		int diff;

		diff = ( MapToScreenY( win, 2 ) - MapToScreenY( win, 1 ) );
			
		for ( int y = MapToScreenY( win, 0 ); y < MapToScreenY( win, 255 ); y += diff ) {
			SecGC.RgbFgColor =  light;
			win.DrawLine( SecGC, MapToScreenX( win, 0 ),( int ) ( y * zoom ), MapToScreenX( win, 255 ), ( int ) ( y * zoom ) );
		}

			
		diff = ( MapToScreenX( win, 2 ) - MapToScreenX( win, 1 ) );
			
		for ( int x = MapToScreenX( win, 0 ); x < MapToScreenX( win, 255 ); x += diff ) {
			SecGC.RgbFgColor =  dark;
			win.DrawLine( SecGC,( int ) ( x * zoom ),  MapToScreenY( win, 0 ),( int ) ( x * zoom ), MapToScreenY( win, 255 ) );
		}

		SecGC.RgbFgColor =  new Gdk.Color( 255, 0, 0 );
			
		win.DrawLine( SecGC, MapToScreenX( win, 0 ), MapToScreenY( win, 0 ), MapToScreenX( win, 255 ), MapToScreenY( win, 0 ) );
					
		SecGC.RgbFgColor =  new Gdk.Color( 0, 255, 0 );
			
		win.DrawLine( SecGC, MapToScreenX( win, 255 ), MapToScreenY( win, 0 ), MapToScreenX( win, 255 ), MapToScreenY( win, 255 ) );			
			
		SecGC.RgbFgColor =  new Gdk.Color( 0, 0, 255 );
			
		win.DrawLine( SecGC, MapToScreenX( win, 0 ),  MapToScreenY( win, 255 ), MapToScreenX( win, 255 ), MapToScreenY( win, 255 ) );
			
		SecGC.RgbFgColor =  new Gdk.Color( 255, 255, 255 );
			
		win.DrawLine( SecGC, MapToScreenX( win, 0 ),  MapToScreenY( win, 0 ), MapToScreenX( win, 0 ), MapToScreenY( win, 255 ) );			
		
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */		
	public void drawcursorat(int ax,int ay,int az,Gtk.ExposeEventArgs aArgs, Gtk.Style aStyle,Gtk.StateType aState) {
		
		Gdk.Window win = aArgs.Event.Window;
        Gdk.Rectangle area = new Gdk.Rectangle();
        Gdk.GC SecGC=aStyle.BaseGC(aState);

		int width, height;
			
		win.GetSize( out width, out height );
			
		SecGC.RgbFgColor =  new Gdk.Color( 255, 0, 0 );
			
		win.DrawLine( SecGC, MapToScreenX( win, ax ), 0, MapToScreenX( win, ax ), height );
		win.DrawLine( SecGC, 0,  MapToScreenY( win, ay ), width, MapToScreenY( win, ay ) );			
			
        SecGC=aStyle.BaseGC(aState);			
			
		switch( iCurrentViewMode ) {
				case ViewMode.XZ: {
					area.X=this.MapToScreenX(win,ax)-5;
					area.Y=this.MapToScreenY(win,ay)-5;
					area.Width=10;
					area.Height=10;				
				}
				break;
					
				case ViewMode.YZ: {
					area.X=this.MapToScreenX(win,ay)-5;
					area.Y=128-this.MapToScreenY(win,az)-5;
					area.Width=10;
					area.Height=10;
				}
				break;
					
				case ViewMode.XY: {
					area.X=this.MapToScreenX(win,ax)-5;
					area.Y=128-this.MapToScreenY(win,az)-5;
					area.Width=10;
					area.Height=10;				
				}
				break;
			}
			
			area.X -= ( int ) ( panx * zoom );
			area.Y -= ( int ) ( pany * zoom );
			
			win.DrawRectangle (SecGC, true, area);
		}
	}
}