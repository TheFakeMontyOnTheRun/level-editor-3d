// MainWindow.cs created with MonoDevelop
// User: daniel at 9:03 PM 7/31/2009
//
// To change standard headers go to Edit->Preferences->Coding->Standard Headers
//
using System;
using Gtk;
using MonoBED2;
//-----------------------------------------------------------------------------
/*
 * @author Daniel "Monty" Monteiro
 * Level editor main window
 * */
public partial class MainWindow: Gtk.Window {	

	/*
	 * The mouse button is pressed. This differentiates the gestures, acording to the edit mode.
	 * */
	bool buttonpressed;
	/*
	 * 
	 * */
	int lastx;
	/*
	 * 
	 * */
	int lasty;
	/*
	 * 
	 * */
	int cursorx;
	/*
	 * 
	 * */
	int cursory;
	/*
	 * 
	 * */
	int cursorz;
	/*
	 * World geometry and properties
	 * */
	World iWorld;
	/*
	 * Information on the world characters and dynamic entities
	 * */
	ActorManager iActors;
	/*
	 * View context collection
	 * */
	WorldView[] iView;
//-----------------------------------------------------------------------------	
	/*
	 * Constructor. Just allocates the data 
	 * */
	public MainWindow (): base (Gtk.WindowType.Toplevel) {
		Build ();
		
		iWorld = new World();
		iActors = new ActorManager();		
		cursorx = cursory = cursorz = 0;
		cmbEditMode.Active = ( int ) WorldView.InteractMode.PLACE_CURSOR;
		iView = new WorldView[ 2 ];
		
		for ( int c = 0; c < 2; c++ ) {
				iView[ c ] = new WorldView( ( WorldView.ViewMode )  c );
				iView[ c ].iCurrentViewMode = WorldView.ViewMode.XZ;
		}
		
		
		resetworld();
	}
//-----------------------------------------------------------------------------	
	/*
	 * 
	 * */
	protected void OnDeleteEvent (object sender, DeleteEventArgs a) {
		Application.Quit();
		a.RetVal = true;
	}
//-----------------------------------------------------------------------------
	/*
	 * View 1 need to be refreshed 
	 * */
	protected virtual void OnDA1ExposeEvent (object o, Gtk.ExposeEventArgs args) {		
				
		int width = 0;
		int height = 0;
		args.Event.Window.GetSize( out width, out height );

		if ( width > 255 || height > 255 ) {
		//	iView[ 1 ].zoom = Math.Min( width / 255, height / 255 );
		}		
		
		hsda2.SetRange( 0, Math.Max( 1, ( 255 * iView[ 1 ].zoom ) /*- ( width * iView[ 1 ].zoom ) */) );
		vsda2.SetRange( 0, Math.Max( 1, ( 255 * iView[ 1 ].zoom ) /*- ( height * iView[ 1 ].zoom ) */) );
		
		vsda2.Value = iView[ 1 ].pany * iView[ 1 ].zoom;
		hsda2.Value = iView[ 1 ].panx * iView[ 1 ].zoom;
		
		iView[ 1 ].iCurrentViewMode = ( WorldView.ViewMode ) CBView1.Active;
		paintViews( 1, args );
	}
//-----------------------------------------------------------------------------
	/*
	 * View 2 need to be refreshed
	 * */
	protected virtual void OnDA3ExposeEvent (object o, Gtk.ExposeEventArgs args) {
		int width = 0;
		int height = 0;
		args.Event.Window.GetSize( out width, out height );
		
		if ( width > 255 || height > 255 ) {
		//	iView[ 0 ].zoom = Math.Min( width / 255, height / 255 );
		}
		
		hsda3.SetRange( 0, Math.Max( 1, ( 255 * iView[ 0 ].zoom ) /*- ( width * iView[ 0 ].zoom )*/ ) );
		vsda3.SetRange( 0, Math.Max( 1, ( 255 * iView[ 0 ].zoom ) /*- ( height * iView[ 0 ].zoom )*/ ) );
		vsda3.Value = iView[ 0 ].pany * iView[ 0 ].zoom;
		hsda3.Value = iView[ 0 ].panx * iView[ 0 ].zoom;
		                  
		iView[ 0 ].iCurrentViewMode = ( WorldView.ViewMode ) CBView3.Active;
		paintViews( 0, args );
	}
//-----------------------------------------------------------------------------
	/*
	 * Redraw the world view
	 * */
	private void paintViews( int d, Gtk.ExposeEventArgs args ) {
		int selected = ( int ) spnSectorSelector.Value;
		
			iView[ d ].fillbg( args, Style,StateType.Active, StateType.Prelight );
			
			for ( int c = 1; c < iWorld.Count(); c++ )			
				if ( !iWorld.isParent( c ) && spnSectorSelector.Value != c )			
					iView[ d ].ExposeSector( args, Style, iWorld.GetSector(c), StateType.Insensitive );

			if ( selected != SectorConstants.NO_SECTOR_LINK )
				iView[ d ].ExposeSector( args, Style, iWorld.GetSector( selected ), StateType.Active );		
		
			iView[ d ].drawcursorat( cursorx, cursory, cursorz, args, Style, StateType.Selected );
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	protected virtual void onOpenMapBtnClicked( object sender, System.EventArgs e ) {
		
		FileChooserDialog chooser = new FileChooserDialog( "Please select a map to open ...",
													        this,
													        FileChooserAction.Open,
													        "Cancel", ResponseType.Cancel,
													        "Open", ResponseType.Accept );
												        
        if( chooser.Run() == ( int ) ResponseType.Accept ) {
	        Title = "MonoBED " + chooser.Filename.ToString();
			iWorld.LoadMap( chooser.Filename );
	     	chooser.Destroy();
		
     	}		
		
		spnSectorSelector.SetRange( 0.0, ( double ) iWorld.Count() );
		QueueDraw();
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	public void onViewScrollWheelRolled( int viewId, Gtk.ScrollEventArgs args ) {
		
		int delta = 0;
		
		if ( args.Event.Direction == Gdk.ScrollDirection.Up )
			delta = 1;
		
		if ( args.Event.Direction == Gdk.ScrollDirection.Down )
			delta =- 1;
		
		iView[ viewId ].zoom += delta;
		
		if ( iView[ viewId ].zoom <= 1 ) 
			iView[ viewId ].zoom = 1;
		
		if (iView[ viewId ].zoom == 1) {
			iView[ viewId ].panx = 0;
			iView[ viewId ].pany = 0;
		}
		
		System.Console.WriteLine("zoom:" + iView[ viewId ].zoom.ToString() );
		QueueDraw();
		
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	protected virtual void OnDA1ScrollEvent ( object o, Gtk.ScrollEventArgs args ) {
		onViewScrollWheelRolled( 1, args );
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	protected virtual void OnDA3ScrollEvent ( object o, Gtk.ScrollEventArgs args ) {
		onViewScrollWheelRolled( 0, args );
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	void SelectSector( int aSector ) {
		
		System.Console.Write( "selecionei " );
		System.Console.WriteLine( aSector );
		spnSectorSelector.Value = ( double ) aSector;
		clrSelection.Color = iWorld.GetSector( ( int ) spnSectorSelector.Value ).GetColor( cmbFaceSelection.Active );
		QueueDraw();
		
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	public void onViewPointerPressed( int viewId, Gtk.ButtonPressEventArgs args ) {
		
		buttonpressed = true;
		System.Console.WriteLine( "click" );
		System.Console.WriteLine( iView[ viewId ].iCurrentViewMode );
		System.Console.WriteLine( cmbEditMode.Active );

		switch( iView[ viewId ].iCurrentViewMode ) {
			
			case WorldView.ViewMode.XZ:	{
			
					switch ( ( WorldView.InteractMode ) cmbEditMode.Active ) {
				
						case WorldView.InteractMode.MOVE_MAP: {
					
							lastx = ( int ) args.Event.X;
							lasty = ( int ) args.Event.Y;
					
						}				
						break;
							
						case WorldView.InteractMode.PLACE_CURSOR: {
					
							System.Console.WriteLine( "cursor posicionado" );
							lastx = ( int ) args.Event.X;
							lasty = ( int ) args.Event.Y;				
							cursorx = iView[ viewId ].ScreenToMapX( args.Event.Window, lastx );
							cursory = iView[ viewId ].ScreenToMapY( args.Event.Window, lasty );
							System.Console.WriteLine( "cursorx:" + cursorx.ToString() );
							System.Console.WriteLine( "cursory:" + cursory.ToString() );
					
						}
						break;
					
						case WorldView.InteractMode.MOVE_SECTOR: {
					
							System.Console.WriteLine( "movendo setor" );
							lastx = ( int ) args.Event.X;
							lasty = ( int ) args.Event.Y;				
							cursorx = iView[ viewId ].ScreenToMapX( args.Event.Window, lastx );
							cursory = iView[ viewId ].ScreenToMapY( args.Event.Window, lasty );
							System.Console.WriteLine( "cursorx:" + cursorx.ToString() );
							System.Console.WriteLine( "cursory:" + cursory.ToString() );
							iWorld.GetSector( ( int ) spnSectorSelector.Value ).MoveXZTo( cursorx, cursory );
					
						}			
						break;				
					
						case WorldView.InteractMode.SELECT_SECTOR: {
					
							lastx = ( int ) args.Event.X;
							lasty = ( int ) args.Event.Y;				
							for ( int c = 1; c < iWorld.Count(); c++ )		
								if ( iWorld.GetSector( c ).WhereBSC(
						                                 iView[ viewId ].ScreenToMapX( args.Event.Window, lastx ),
						                                 iView[ viewId ].ScreenToMapY( args.Event.Window, lasty ),
						                                 0 ) == SectorConstants.DIRECTIONS.INSIDE )
									SelectSector( c );
					
						}
						break;				
					}
				}
		break;
		}
		QueueDraw();		
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	protected virtual void OnDA1ButtonPressEvent ( object o, Gtk.ButtonPressEventArgs args ) {
		onViewPointerPressed( 1, args );
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	protected virtual void OnDA1ButtonReleaseEvent ( object o, Gtk.ButtonReleaseEventArgs args) {
		buttonpressed = false;
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	protected virtual void OnDA3ButtonPressEvent ( object o, Gtk.ButtonPressEventArgs args ) {
		onViewPointerPressed( 0, args );
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	protected virtual void OnDA3ButtonReleaseEvent ( object o, Gtk.ButtonReleaseEventArgs args ) {
		buttonpressed = false;
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	protected virtual void OnExposeEvent ( object o, Gtk.ExposeEventArgs args ) {
		
		lblCursorX.Text = System.Convert.ToString( cursorx );
		lblCursorY.Text = System.Convert.ToString( cursory );
		lblCursorZ.Text = System.Convert.ToString( cursorz );	
		
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	protected virtual void onNewSectorBtnClicked ( object sender, System.EventArgs e ) {
		
		createSectorAtCursor();
		QueueDraw();
	}
//-----------------------------------------------------------------------------
	/*
	 * Initializes the data structures to a empty world
	 * @see Constructor
	 * */
	public void resetworld() {
		
		iWorld.Clear();
		Sector sector = new Sector();
		sector.SetX0( 0 );
		sector.SetY0( 0 );
		sector.SetHeight0( 0 );
		sector.SetX1( 255 );
		sector.SetY1( 255 );
		sector.SetHeight1( 255 );		
		iWorld.Add( sector );
		spnSectorSelector.SetRange( 0.0, ( double ) iWorld.Count() );
		spnSectorSelector.Value = SectorConstants.NO_SECTOR_LINK;
		cursorx = 128;
		cursory = 128;
		cursorz = 128;
		QueueDraw();
		
	}
	public void createSectorAtCursor( ) {
		createSectorAtCursor( false );
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	public void createSectorAtCursor( bool solid ) {
		
		Sector sector = new Sector();
		sector.solid = solid;
		sector.SetX0( cursorx );
		sector.SetY0( cursory );
		sector.SetHeight0( 0 );
		sector.SetX1( 10 );
		sector.SetY1( 10 );
		sector.SetHeight1( 10 );
		sector.SetParent( SectorConstants.NO_PARENT );
		iWorld.Add( sector );
		System.Console.WriteLine( iWorld.Count().ToString() + 
		                          ":setor criado @" + 
		                          cursorx.ToString() + 
		                          "," + 
		                          cursory.ToString() + 
		                          "," + 
		                          cursorz.ToString() );
		spnSectorSelector.SetRange( 0.0, ( double ) iWorld.Count() );		
		SelectSector( iWorld.Count() -1 );
	}
//-----------------------------------------------------------------------------
	/*
	 * New Map button clicked 
	 * */
	protected virtual void onNewMapBtnClicked ( object sender, System.EventArgs e ) {
		System.Console.WriteLine( "New Map clicked" );
		resetworld();
	}
//-----------------------------------------------------------------------------
	/*
	 * Color Selection Button Clicked
	 * */
	protected virtual void onClrSelectionClicked (object sender, System.EventArgs e) {
		
		System.Console.WriteLine( "color pick:" + clrSelection.Color.ToString() );
		iWorld.GetSector( ( int ) spnSectorSelector.Value ).SetColor( cmbFaceSelection.Active,clrSelection.Color );
		QueueDraw();
		
	}
//-----------------------------------------------------------------------------
	/*
	 * Face Selection Combo Box Changed
	 * */
	protected virtual void onCmbFaceSelectionChanged (object sender, System.EventArgs e) {
		
		clrSelection.Color = iWorld.GetSector( ( int ) spnSectorSelector.Value ).GetColor( cmbFaceSelection.Active );
		
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	private void onViewPointerDrag( int viewId, Gtk.MotionNotifyEventArgs args ) {
		
		if ( buttonpressed ) {
			
			switch ( ( WorldView.InteractMode ) cmbEditMode.Active ) {
				
				case WorldView.InteractMode.RESIZE_SECTOR:		
					switch ( iView[ viewId ].iCurrentViewMode ) {
						case WorldView.ViewMode.XZ:
							iWorld.GetSector( ( int ) spnSectorSelector.Value ).StretchXZTo(
							                                                                iView[ viewId ].ScreenToMapX( args.Event.Window, ( int ) args.Event.X ),
							                                                                iView[ viewId ].ScreenToMapY( args.Event.Window, ( int ) args.Event.Y ) );
						break;
						case WorldView.ViewMode.YZ:
							iWorld.GetSector( ( int ) spnSectorSelector.Value ).StretchXYTo(
							                                                                iView[ viewId ].ScreenToMapX( args.Event.Window, ( int ) args.Event.X ),
							                                                                iView[ viewId ].ScreenToMapY( args.Event.Window, ( int ) args.Event.Y ) );
						break;
						case WorldView.ViewMode.XY:
							iWorld.GetSector( ( int ) spnSectorSelector.Value ).StretchYZTo(
							                                                                iView[ viewId ].ScreenToMapX( args.Event.Window, ( int ) args.Event.X ),
							                                                                iView[ viewId ].ScreenToMapY( args.Event.Window, ( int ) args.Event.Y ) );
						break;
						case WorldView.ViewMode.ISO:
						break;
						
					}
					break;
				case WorldView.InteractMode.MOVE_SECTOR:
					switch ( iView[ viewId ].iCurrentViewMode ) {	
						case WorldView.ViewMode.XZ:
							iWorld.GetSector( ( int ) spnSectorSelector.Value ).MoveXZTo(
								                                                    iView[ viewId ].ScreenToMapX( args.Event.Window, ( int ) args.Event.X ),
								                                                    iView[ viewId ].ScreenToMapY( args.Event.Window, ( int ) args.Event.Y )
								                                                      );						
						break;
						case WorldView.ViewMode.YZ:
							iWorld.GetSector( ( int ) spnSectorSelector.Value ).MoveXYTo(
								                                                    iView[ viewId ].ScreenToMapX( args.Event.Window, ( int ) args.Event.X ),
								                                                    iView[ viewId ].ScreenToMapY( args.Event.Window, ( int ) args.Event.Y )
								                                                      );				
						break;
						case WorldView.ViewMode.XY:
							iWorld.GetSector( ( int ) spnSectorSelector.Value ).MoveYZTo(
								                                                    iView[ viewId ].ScreenToMapX( args.Event.Window, ( int ) args.Event.X ),
								                                                    iView[ viewId ].ScreenToMapY( args.Event.Window, ( int ) args.Event.Y )
								                                                      );					
						break;
						case WorldView.ViewMode.ISO:
						break;
					
					}
					break;
					
				case WorldView.InteractMode.MOVE_MAP:
					iView[ viewId ].panx += ( lastx - ( int ) args.Event.X ) / ( iView[ viewId ].zoom );
					iView[ viewId ].pany += ( lasty - ( int ) args.Event.Y ) / ( iView[ viewId ].zoom );
					
					if ( iView[ viewId ].panx < 0 )
						iView[ viewId ].panx = 0;
					
					if ( iView[ viewId ].pany < 0 )
						iView[ viewId ].pany = 0;

						vsda2.Value = iView[ viewId ].pany;
						hsda2.Value = iView[ viewId ].panx;
					break;
			}
		} else {
		//	cursorx = lastx;
		//	cursory = lasty;
		}
		
		QueueDraw();		
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	protected virtual void OnDA1MotionNotifyEvent (object o, Gtk.MotionNotifyEventArgs args) {
		onViewPointerDrag( 1, args );
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	protected virtual void OnDA3MotionNotifyEvent (object o, Gtk.MotionNotifyEventArgs args) {
		 onViewPointerDrag( 0, args );
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	protected virtual void onSaveAsBtnClicked (object sender, System.EventArgs e) {
		Gtk.FileChooserDialog fc = new Gtk.FileChooserDialog( "save?",
		                                                      this,
		                                                      FileChooserAction.Save,
		                                                      "Cancel",
		                                                      ResponseType.Cancel,
		                                                      "Save",
		                                                      ResponseType.Accept );	 	

        if ( fc.Run() == ( int )ResponseType.Accept ) {
	       	iWorld.SaveMap(fc.Filename.ToString(),iActors);
			fc.Destroy();
		}
	}
//-----------------------------------------------------------------------------	
	/*
	 * 
	 * */
	protected virtual void onAddLampBtnClicked (object sender, System.EventArgs e)	{
		Actor actor = new Actor();
		actor.SetLight( 128 );
		actor.SetLocation( ( int ) spnSectorSelector.Value );
		actor.SetKind( 0xFFFF );
		iActors.AddActor( actor );
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	protected virtual void OnCmdAddDoorClicked ( object sender, System.EventArgs e ) {
		iWorld.GetSector( ( int ) spnSectorSelector.Value ).SetDoor( cmbFaceSelection.Active );
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	protected virtual void OnNewActionActivated ( object sender, System.EventArgs e ) {
		resetworld();
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	protected virtual void OnCBView3Changed ( object sender, System.EventArgs e ) {
		QueueDraw();
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	protected virtual void OnCBView1Changed ( object sender, System.EventArgs e ) {
		QueueDraw();
	}
//-----------------------------------------------------------------------------
	/*
	 * 
	 * */
	protected virtual void OnExecuteActionActivated ( object sender, System.EventArgs e ) {
		Gtk.FileChooserDialog fc = new Gtk.FileChooserDialog( "export?",
		                                                      this,
		                                                      FileChooserAction.Save,
		                                                      "Cancel",
		                                                      ResponseType.Cancel,
		                                                      "Save",
		                                                      ResponseType.Accept );	 	

        if( fc.Run() == ( int ) ResponseType.Accept ) {
	       	iWorld.ExportToMobile( fc.Filename.ToString(), iActors );
			fc.Destroy();
		}
	}
	protected virtual void OnVscrollbar2ValueChanged (object sender, System.EventArgs e) {
		System.Console.WriteLine("Valor:" + vsda2.Value );
		iView[ 1 ].pany = vsda2.Value / iView[ 1 ].zoom;
		QueueDraw();
	}
	
	protected virtual void OnHsda2ValueChanged (object sender, System.EventArgs e) {
		System.Console.WriteLine("Valor:" + hsda2.Value );
		iView[ 1 ].panx = hsda2.Value / iView[ 1 ].zoom;
		QueueDraw();
	}
	
	
	protected virtual void OnVsda3ValueChanged (object sender, System.EventArgs e) 	{
		System.Console.WriteLine("Valor:" + vsda3.Value );
		iView[ 0 ].pany = vsda3.Value / iView[ 0 ].zoom;
		QueueDraw();
	}
	
	
	protected virtual void OnHsda3ValueChanged (object sender, System.EventArgs e) 	{
		System.Console.WriteLine("Valor:" + hsda3.Value );
		iView[ 0 ].panx = hsda3.Value / iView[ 0 ].zoom;
		QueueDraw();
	}
	
	protected virtual void OnBtnExportClicked (object sender, System.EventArgs e) {
		/*
				Gtk.FileChooserDialog fc = new Gtk.FileChooserDialog( "Export?",
		                                                      this,
		                                                      FileChooserAction.Save,
		                                                      "Cancel",
		                                                      ResponseType.Cancel,
		                                                      "Export",
		                                                      ResponseType.Accept );	 	

        if ( fc.Run() == ( int )ResponseType.Accept ) {
			
			fc.Destroy();
		}
		*/
		iWorld.exportMap( /*fc.Filename.ToString()*/ "", iActors );
		
	}
	
	
	protected virtual void OnBtnImportLevelClicked (object sender, System.EventArgs e)	{
		iWorld.importLEVEL( "/home/monty/prison.level", iActors );
	}

	
	protected virtual void OnBtnAddSolidClicked (object sender, System.EventArgs e)	{
		createSectorAtCursor( true );
		spnSectorSelector.SetRange( 0.0, ( double ) iWorld.Count() );		
		SelectSector( iWorld.Count() -1 );
		QueueDraw();		
		Console.WriteLine("solid created");
	}
	
	void setCurrentInteractMode( WorldView.InteractMode mode ) {
		cmbEditMode.Active = ( int ) mode;
		QueueDraw();
	}
	
	protected virtual void OnDA2KeyPressEvent (object o, Gtk.KeyPressEventArgs args) {
		handleKeyboard( args.Event.Key );
	}
	
	protected virtual void OnDA3KeyPressEvent (object o, Gtk.KeyPressEventArgs args) {
		handleKeyboard( args.Event.Key );
	}
	
	protected virtual void OnVbox1KeyPressEvent (object o, Gtk.KeyPressEventArgs args) {
		handleKeyboard( args.Event.Key );
	}
	
	protected virtual void OnKeyPressEvent (object o, Gtk.KeyPressEventArgs args) {
		handleKeyboard( args.Event.Key );
	}
	
	void handleKeyboard( Gdk.Key key ) {
		
		WorldView.InteractMode newMode = ( WorldView.InteractMode ) cmbEditMode.Active;
		
		switch ( key ) {
			case Gdk.Key.R:
			case Gdk.Key.r:			
				newMode = WorldView.InteractMode.RESIZE_SECTOR;
				break;
			case Gdk.Key.T:
			case Gdk.Key.t:			
				newMode = WorldView.InteractMode.MOVE_SECTOR;
				break;
			case Gdk.Key.S:
			case Gdk.Key.s:			
				newMode = WorldView.InteractMode.SELECT_SECTOR;
				break;
			case Gdk.Key.A:
			case Gdk.Key.a:			
				newMode = WorldView.InteractMode.MOVE_MAP;
				break;
			case Gdk.Key.C:
			case Gdk.Key.c:			
				newMode = WorldView.InteractMode.PLACE_CURSOR;
				break;
			///edit action
			case Gdk.Key.D:
			case Gdk.Key.d:
				createSectorAtCursor( true );
				break;
			case Gdk.Key.N:
			case Gdk.Key.n:
				createSectorAtCursor();
				break;
		}
		
		setCurrentInteractMode( newMode );
	}
	
	
//-----------------------------------------------------------------------------
}