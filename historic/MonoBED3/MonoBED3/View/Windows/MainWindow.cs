using System;
using Gtk;
using MonoBED3;
using br.odb.portalizer;
using br.odb.libscene;

//-----------------------------------------------------------------------------
public partial class MainWindow : Gtk.Window, ContextListener, br.odb.utils.FileServerDelegate
{
	public static EditorContext editContext;
	public static int altButtonBehaviour;
	private static MainWindow instance;
//-----------------------------------------------------------------------------
	public static MainWindow getInstance() {
		return instance;
	}	
//-----------------------------------------------------------------------------	
	public MainWindow () : base(Gtk.WindowType.Toplevel)
	{
		Build ();
		editContext = null;
		instance = this;
		createNewContext();
		
		br.odb.libscene.World.snapLevel = 100;
	}
//-----------------------------------------------------------------------------
	protected void OnDeleteEvent (object sender, DeleteEventArgs a)
	{
		if ( onQuitApp() ) {
			
			Application.Quit ();
			a.RetVal = true;
		}
	}
//-----------------------------------------------------------------------------	
	protected virtual void OnExposeEvent (object o, Gtk.ExposeEventArgs args)
	{
		this.tblViewsLayout.ShowAll();
	}
//-----------------------------------------------------------------------------	
	protected virtual void OnNewActionActivated (object sender, System.EventArgs e)
	{
		if (editContext != null && editContext.isCommitNeeded())
		{
			editContext.commit();
		}
		
		createNewContext();
	}	
//-----------------------------------------------------------------------------
	private void broadcastContext( EditorContext context ) {		
		
		this.view1.setContext(context);
		this.view2.setContext(context);
		this.view3.setContext(context);
		this.view4.setContext(context);				
	}
//-----------------------------------------------------------------------------
	private void createNewContext()
	{

		editContext = new EditorContext(this);	
		editContext.setBroadcaster( this );
		broadcastContext( editContext );
		updateWindowTitle();
	}	
//-----------------------------------------------------------------------------	
	protected virtual void OnSaveAsActionActivated (object sender, System.EventArgs e) {
		onSaveMapWithDialogChooser();		
	}
//-----------------------------------------------------------------------------	
	public EditorContext getContext() {
		
		return editContext;
	}
//-----------------------------------------------------------------------------	
	private void updateWindowTitle() {
		
		String newTitle = "MonoBED - ";
		
		if ( editContext.isCommitNeeded() ) {
			newTitle += "[modified] ";
		}
		
		if ( editContext.getFilename() != null )
			newTitle += editContext.getFilename();
		else
			newTitle += "new file";
		
		Title = newTitle;		
	}
//-----------------------------------------------------------------------------
	public void contextModified() {
		broadcastContextModified();
		updateWindowTitle();
	}
//-----------------------------------------------------------------------------	
	public void broadcastContextModified() {
		Logger.log("broadcasting modified");
		( ( ContextListener )view1 ).contextModified();
		( ( ContextListener )view2 ).contextModified();
		( ( ContextListener )view3 ).contextModified();
		( ( ContextListener )view4 ).contextModified();
	}
//-----------------------------------------------------------------------------
	//import OBJ as subtractive space
	protected virtual void OnPropertiesAction1Activated (object sender, System.EventArgs e) {
		
		Logger.log( "importando de OBJ" );

		FileChooserDialog chooser = new FileChooserDialog( "Please select a mesh to import ...",
													        this,
													        FileChooserAction.Open,
													        "Cancel", ResponseType.Cancel,
													        "Open", ResponseType.Accept );
		
		FileFilter filterOBJ  = new FileFilter();
		filterOBJ.Name = "WaveFront OBJ files";
		filterOBJ.AddPattern("*.obj");
		chooser.AddFilter(filterOBJ);
												        
        if( chooser.Run() == ( int ) ResponseType.Accept ) {

			String inputPath = chooser.Filename;
			br.odb.libscene.World world = null;
			chooser.Hide();
			//finally start processing
			java.util.ArrayList mesh = null;				
			br.odb.liboldfart.parser.FileFormatParser loader = null;		
			world = new br.odb.libscene.World();
			loader = new br.odb.liboldfart.wavefront_obj.WavefrontOBJLoader();
			((br.odb.liboldfart.wavefront_obj.WavefrontOBJLoader)loader).currentPath = inputPath.Substring( 0, inputPath.LastIndexOf('/') + 1 );
			loader.preBuffer( this.openAsInputStream( chooser.Filename ) );
			loader.parseDocument( this );		
			mesh = loader.getGeometry();	
			br.odb.libscene.WorldPartitioner.buildConvexHulls( world, mesh );
			createNewContext();
			editContext.setBroadcaster( this );
			editContext.internalize( world, false );
			Title = "MonoBED - " + chooser.Filename.ToString();
			editContext.askForScreenRefresh();				
     	}	
		
		chooser.Destroy();	
	}
//-----------------------------------------------------------------------------
	protected virtual void OnGenerateOptimizedBZK3 (object sender, System.EventArgs e) {
		generateBZK3();
	}
//-----------------------------------------------------------------------------	
	private void generateBZK3() {
		
		Logger.log( "exportando level" );

		EditorWorld worldBefore =  new EditorWorld();
		worldBefore.copyFrom (editContext.getWorld());

		ExportMap dialog = new ExportMap( worldBefore );
		dialog.ShowNow();		
	}
//-----------------------------------------------------------------------------
	protected virtual void OnSaveActionActivated (object sender, System.EventArgs e) {
		onSave();
	}
//-----------------------------------------------------------------------------	
	private void onSave() {

		if ( editContext.getFilename() == null ||  editContext.getFilename() == "" )
			onSaveMapWithDialogChooser();
		else
			editContext.saveCurrent();
		
		updateWindowTitle();
	}
//-----------------------------------------------------------------------------	
	private void onSaveMapWithDialogChooser() {
		Gtk.FileChooserDialog fc = new Gtk.FileChooserDialog( "Please name the file to save",
		                                                      this,
		                                                      FileChooserAction.Save,
		                                                      "Cancel",
		                                                      ResponseType.Cancel,
		                                                      "Save",
		                                                      ResponseType.Accept );	 
		
					
			FileFilter filterBZK3  = new FileFilter();
			filterBZK3.Name = "BZK3 map files";
			filterBZK3.AddPattern("*.level");
			fc.AddFilter(filterBZK3);
			fc.Name = "untitled.level";
			fc.DoOverwriteConfirmation = true;

        if ( fc.Run() == ( int )ResponseType.Accept ) {
	       	editContext.save( fc.Filename.ToString() );	
			updateWindowTitle();
		}		
		fc.Destroy();
	}
//-----------------------------------------------------------------------------
	protected virtual void OnOpenAction1Activated (object sender, System.EventArgs e)
	{
		onOpenMapWithDialogChooser();
	}
//-----------------------------------------------------------------------------	
	protected virtual void OnOpenActionActivated (object sender, System.EventArgs e) {
		onOpenMapWithDialogChooser();
	}
//-----------------------------------------------------------------------------
	private void onOpenMapWithDialogChooser() {
		onOpenMapWithDialogChooser( true );
	}
//-----------------------------------------------------------------------------		
	private void onOpenMapWithDialogChooser( bool openMasters ) {
	
		FileChooserDialog chooser = new FileChooserDialog( "Please select a map to open ...",
													        this,
													        FileChooserAction.Open,
													        "Cancel", ResponseType.Cancel,
													        "Open", ResponseType.Accept );
		
			FileFilter filterBZK3  = new FileFilter();
			filterBZK3.Name = "BZK3 map files";
			filterBZK3.AddPattern("*.level");
			chooser.AddFilter(filterBZK3);


												        
        if( chooser.Run() == ( int ) ResponseType.Accept ) {

			createNewContext();
			editContext.setBroadcaster( this );
			chooser.Hide();
			editContext.loadSnapshot( chooser.Filename, openMasters );
			Title = "MonoBED - " + chooser.Filename.ToString();
     	}	
		
		chooser.Destroy();
	}
//-----------------------------------------------------------------------------
	protected virtual void OnImportBZK2WithDialogChooser (object sender, System.EventArgs e)
	{
		FileChooserDialog chooser = new FileChooserDialog( "Please select a map to open ...",
													        this,
													        FileChooserAction.Open,
													        "Cancel", ResponseType.Cancel,
													        "Open", ResponseType.Accept );
												        
		FileFilter filterBZK2  = new FileFilter();
		filterBZK2.Name = "Legacy BZK2 map files";
		filterBZK2.AddPattern("*.bzk2");
		chooser.AddFilter(filterBZK2);

        if( chooser.Run() == ( int ) ResponseType.Accept ) {

			createNewContext();
			editContext.setBroadcaster( this );
			chooser.Hide();
			editContext.importBZK2( chooser.Filename, true );
			Title = "MonoBED - " + chooser.Filename.ToString();
     	}	
		
		chooser.Destroy();		
	}
//-----------------------------------------------------------------------------
	protected virtual void OnImportCompiledMapActivated (object sender, System.EventArgs e)
	{
		onOpenMapWithDialogChooser( false );
	}
//-----------------------------------------------------------------------------	
	public java.io.InputStream openAsInputStream( string filename) {
		
		return new java.io.FileInputStream( filename );
	}
//-----------------------------------------------------------------------------
	public java.io.OutputStream openAsOutputStream( string filename) {
		return new java.io.FileOutputStream( filename );
	}	
//-----------------------------------------------------------------------------
	protected void OnQuitActionActivated (object sender, System.EventArgs e) {
		onQuitApp();
	}
//-----------------------------------------------------------------------------		
	private bool onQuitApp() {
		
		if ( editContext != null && editContext.isCommitNeeded() ) {

			MessageDialog md = new MessageDialog ( this, 
                                      DialogFlags.DestroyWithParent,
	                              MessageType.Question, 
                                      ButtonsType.YesNo, "You have unsaved changed. Do you want to save?");
				
			ResponseType result = (ResponseType)md.Run ();
			
			if (result == ResponseType.Yes) {
				this.onSave();	
			}

			md.Destroy();
		}
		this.Destroy();
		return true;
	}
//-----------------------------------------------------------------------------
	protected void OnDestroyEvent (object o, Gtk.DestroyEventArgs args)
	{
		onQuitApp();
	}
//-----------------------------------------------------------------------------
	protected void OnExportOBJFile (object sender, System.EventArgs e)
	{
		Gtk.FileChooserDialog fc = new Gtk.FileChooserDialog( "Please name the file to save",
		                                                     this,
		                                                     FileChooserAction.Save,
		                                                     "Cancel",
		                                                     ResponseType.Cancel,
		                                                     "Save",
		                                                     ResponseType.Accept );	 	
		
			FileFilter filterOBJ  = new FileFilter();
			filterOBJ.Name = "WaveFront OBJ files";
			filterOBJ.AddPattern("*.obj");
			fc.AddFilter(filterOBJ);
			fc.DoOverwriteConfirmation = true;

		
		if ( fc.Run() == ( int )ResponseType.Accept ) {
			editContext.save( fc.Filename.ToString() );	
			updateWindowTitle();
		}	
		fc.Destroy();
	}
//-----------------------------------------------------------------------------
	protected void OnPropertiesActionActivated (object sender, System.EventArgs e)
	{
		MapProperties mapProperties = new MapProperties();
		mapProperties.Show();
	}
//-----------------------------------------------------------------------------
	protected void OnAboutMonoBed3Activated (object sender, System.EventArgs e)
	{
		AboutDlg about = new AboutDlg();
		about.Show();
	}
//-----------------------------------------------------------------------------
	public void removeEntity() {

		editContext.removeEntity( editContext.getCurrentSector() );
		editContext.setCurrent( editContext.getSector( 0 ) );
	}
//-----------------------------------------------------------------------------
	protected void OnGenerateOptimizedBZK3ToolbarActivated (object sender, System.EventArgs e)
	{
		generateBZK3();
	}
//-----------------------------------------------------------------------------
	protected void OnAboutActionActivated (object sender, System.EventArgs e)
	{
		AboutDlg about = new AboutDlg();
		about.Show();
	}
//-----------------------------------------------------------------------------
	//Action: Search for errors and warnings
	protected void OnFindActionActivated (object sender, System.EventArgs e)
	{
		MapProperties diagDlg = new MapProperties();
		diagDlg.Show();
	}
//-----------------------------------------------------------------------------
	public java.io.InputStream openAsset(String filename) {
		// TODO Auto-generated method stub
		return null;
	}
//-----------------------------------------------------------------------------	
	public java.io.InputStream openAsset(int resId) {
		// TODO Auto-generated method stub
		return null;
	}
//-----------------------------------------------------------------------------
}
//-----------------------------------------------------------------------------