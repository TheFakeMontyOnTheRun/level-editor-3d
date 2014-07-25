using System;
using System.Collections.Generic;
using System.Runtime.CompilerServices;
using br.odb.liboldfart.wavefront_obj;
using br.odb.libscene;
using br.odb.utils.math;
using Gtk;
//-----------------------------------------------------------------------------
namespace MonoBED3
{
//-----------------------------------------------------------------------------
	[System.ComponentModel.ToolboxItem(true)]
	public partial class PaletteWidget : Gtk.Bin, ContextAware, ContextListener
	{
//-----------------------------------------------------------------------------		
		public void setContext(EditorContext instance)
		{
			this.updateWidgets();
		}
//-----------------------------------------------------------------------------		
		public EditorContext getContext()
		{
			return MainWindow.editContext;	
		}
//-----------------------------------------------------------------------------		
		public PaletteWidget ()
		{
			this.Build ();
		}
//-----------------------------------------------------------------------------				
		protected virtual void OnBtnCreateEntityClicked (object sender, System.EventArgs e)
		{
			if (getContext () != null) {

				Sector s = getContext ().addNewSector( true );
				s.moveTo (getContext().getCursor());
				s.setDX (10);
				s.setDY (10);
				s.setDZ (10);
				getContext ().notifyChange ();
			} else 
				Logger.log("**ERROR** No editor context available.");
			
		}	
//-----------------------------------------------------------------------------
		protected virtual void OnLblDuplicateEntityClicked (object sender, System.EventArgs e) {
			duplicateEntity();

		}
//-----------------------------------------------------------------------------		
		private void duplicateEntity() {

			Sector duplicated = new Sector ( getContext().getCurrentSector() );
			getContext().addSector( duplicated );
			getContext().setCurrent(  duplicated );
			getContext().notifyChange();
		}
//-----------------------------------------------------------------------------	
		protected virtual void OnBtnDeleteEntityClicked (object sender, System.EventArgs e) {
			deleteCurrentEntity();
		}
//-----------------------------------------------------------------------------		
		[MethodImpl(MethodImplOptions.Synchronized)]
		public void deleteCurrentEntity() {
			
			if ( getContext() != null && getContext().getCurrentSector() != null ) {
				
//				getContext().lockContext();
				Sector sector = ( ( Sector ) getContext().getCurrentSector() );
				int id = sector.getId();
				
				if ( id == 0 ) {
//					getContext().unlockContext();
					return;
				}
				
				getContext().removeEntity( sector );
				getContext().setCurrent( getContext().getSector( 0 ) );
//				getContext().unlockContext();
				getContext().notifyChange();
			}
		}
//-----------------------------------------------------------------------------
		protected virtual void OnBtnDeleteFaceLinkClicked (object sender, System.EventArgs e) {
	
			int face = this.cmbSelectFace.Active; //currentFace;
			if ( getContext() != null && getContext().getCurrentSector() != null ) {
				
				( ( Sector ) getContext().getCurrentSector() ).setLink( face, 0 );
				getContext().notifyChange();
			}
		}
//-----------------------------------------------------------------------------				
		protected virtual void OnFaceColorBtnColorSet (object sender, System.EventArgs e)
		{
			Sector s = ( ( Sector ) getContext().getCurrentSector() );
			s.setColor( Utils.getColor( FaceColorBtn.Color ), this.cmbSelectFace.Active );
			getContext().askForScreenRefresh();
		}
//-----------------------------------------------------------------------------		
		protected virtual void OnCboEntitiesChanged (object sender, System.EventArgs e)
		{
			if ( getContext().getTotalSectors() > 0 && cboEntities.Active >= 0 && cboEntities.Active < getContext().getTotalSectors() ) {
				getContext().setCurrent( getContext().getSector( cboEntities.Active ) );	
			}
		}
//-----------------------------------------------------------------------------
		private void updateCombo() {
			
	        CellRendererText cell;
			ListStore store;
			
			cboEntities.Clear();
			cboLink.Clear();
			cell = new CellRendererText();
	        cboEntities.PackStart(cell, false );
	        cboEntities.AddAttribute(cell, "text", 0);
	        store = new ListStore(typeof (string));
			cboEntities.Model = store;
			
			cell = new CellRendererText();
	        cboLink.PackStart(cell, false );
	        cboLink.AddAttribute(cell, "text", 0);
			cboLink.Model = store;
			
			store.AppendValues ("-" );
			
			if ( getContext() != null ) {

				for ( int c = 1; c < getContext().getTotalSectors(); ++c ) {

		       		store.AppendValues ("Sector " + c );
				}
			}
			
			if ( getContext() != null && getContext().getCurrentSector() != null )
				cboEntities.Active = getContext().getCurrentSector().getId();
		}		
//-----------------------------------------------------------------------------
		[MethodImpl(MethodImplOptions.Synchronized)]
		private void updateWidgets() {
			
			int currentFace = this.cmbSelectFace.Active;
			updateCombo();
			
			if ( getContext() == null ) {
				chkIsDoor.Active = false;
				chkIsDoor.Sensitive = false;
				return;
			}
			
			spnCursorX.Value = getContext().getCursor().getX();
			spnCursorY.Value = getContext().getCursor().getY();
			spnCursorZ.Value = getContext().getCursor().getZ();
			
			if ( getContext() != null && getContext().getCurrentSector() != null && getContext().getCurrentSector().getId() > 0 ) {

				Sector s = getContext ().getCurrentSector ();

				if ( s == null  )
					return;

				cboLink.Active = s.getLink( currentFace );
				
				//aqui entra atualização de widgets para um dado sector
				if (s.getExtraInformation () == null)
					entExtraData.Text = "";
				else
					entExtraData.Text = s.getExtraInformation ();


				if ( s.getMapLink() != null ) {
					this.edtLinkPath.Text = s.getMapLink();
				} else {
					this.edtLinkPath.Text = "-";
				}


				if ( s.getDecalAt( cmbSelectFace.Active ) != null ) {
					this.edtDecalPath.Text = s.getDecalAt( cmbSelectFace.Active );
				} else {
					this.edtDecalPath.Text = "-";
				}

				this.chkIsDoor.Active = ( ( ( Sector ) getContext().getCurrentSector() ).getDoor( currentFace ) != null );


				Color color = s.getColor( currentFace );
				byte r = Utils.clamp( ( byte )( color.getR() ), 0, 255 );
				byte g = Utils.clamp( ( byte )( color.getG() ), 0, 255 );
				byte b = Utils.clamp( ( byte )( color.getB() ), 0, 255 );
				FaceColorBtn.Color = new Gdk.Color( r,g,b );
				
				if ( s.getLink( currentFace ) != 0 ) {
					
					chkIsDoor.Active = ( s.getDoor( currentFace ) != null );
					chkIsDoor.State = StateType.Active;					
				} else {
					chkIsDoor.Active = false;
					chkIsDoor.State = StateType.Insensitive;
				}
				
				this.edtDecalPath.Text = ( s.getDecalAt( currentFace ) );
				spnFloorHeight.Value = s.getY0();
				spnCeilingHeight.Value = s.getY1();
				cboLink.Active = s.getLink( currentFace );
			}
			
			if ( getContext().getCurrentSector() != null ) {
			
				cboEntities.Active = getContext().getCurrentSector().getId();
			}
			
			if ( getContext().getCurrentSector() != null ) {
				
				chkIsDoor.Sensitive = true;				

				
				if ( getContext().getCurrentSector() is Sector ) {
					chkIsDoor.Active = ( getContext().getCurrentSector().getDoor( currentFace ) != null );
				}						
				
			} else {
				chkIsDoor.Active = false;
				chkIsDoor.Sensitive = false;
				
			}
		}
//-----------------------------------------------------------------------------
		protected virtual void OnChkIsDoorToggled (object sender, System.EventArgs e) {
		
			int currentFace = this.cmbSelectFace.Active;
			if ( ( getContext().getCurrentSector() is Sector ) ) {
				if (chkIsDoor.Active)
					getContext ().getCurrentSector ().setDoorAt (currentFace, getContext ().getCurrentSector ().getLink (currentFace));
				else
					getContext ().getCurrentSector ().removeDoorAt (currentFace);
			}
		}
//-----------------------------------------------------------------------------		
		public void contextModified() {
			updateWidgets();
		}
//-----------------------------------------------------------------------------		
		protected virtual void OnCboLinkChanged (object sender, System.EventArgs e) {
			
			int face = this.cmbSelectFace.Active;
			
			if ( getContext() != null && getContext().getCurrentSector() != null ) {
				
				( ( Sector ) getContext().getCurrentSector() ).setLink( face, cboLink.Active );
				getContext().notifyChange();
			}
		}
//-----------------------------------------------------------------------------
		protected virtual void OnCmbSelectFaceChanged (object sender, System.EventArgs e)
		{
			updateWidgets();
		}
//-----------------------------------------------------------------------------		
		protected void OnCmbEditModeChanged (object sender, System.EventArgs e)
		{
			MainWindow.altButtonBehaviour = cmbEditMode.Active;
		}
//-----------------------------------------------------------------------------
		protected void OnChkIsDoorClicked (object sender, System.EventArgs e)
		{


		}
//-----------------------------------------------------------------------------
		protected void OnSpnCeilingHeightChangeValue (object o, Gtk.ChangeValueArgs args)
		{

		}
//-----------------------------------------------------------------------------
		protected void OnSpnFloorHeightChangeValue (object o, Gtk.ChangeValueArgs args)
		{
		}
//-----------------------------------------------------------------------------		
		protected virtual void OnLblRemoveDecalClicked (object sender, System.EventArgs e) {

			Sector s = getContext().getCurrentSector();
			s.setDecalAt( cmbSelectFace.Active, null );
			getContext().askForScreenRefresh();
			updateWidgets();
		}	
//-----------------------------------------------------------------------------
		protected void OnSpnCeilingHeightValueChanged (object sender, System.EventArgs e)
		{

			Sector s = getContext ().getCurrentSector ();
			s.setY1( (float)this.spnCeilingHeight.Value );
			getContext().askForScreenRefresh();
		}
//-----------------------------------------------------------------------------
		protected void OnSpnFloorHeightValueChanged (object sender, System.EventArgs e)
		{
			Sector s = getContext ().getCurrentSector ();
			s.setY0( (float)this.spnFloorHeight.Value );
			getContext().askForScreenRefresh();
		}
//-----------------------------------------------------------------------------
		void reset() {

		}
//-----------------------------------------------------------------------------		
		void setDrawGrid( bool shouldDrawGrid ) {

		}		
//-----------------------------------------------------------------------------
		protected void OnEntCursorXEditingDone (object sender, System.EventArgs e)
		{
		//	getContext().getCursor().setX( ( float ) System.Convert.ToDouble( entCursorX.Text ) );
			getContext().askForScreenRefresh();
		}
//-----------------------------------------------------------------------------
		protected void OnEntCursorYEditingDone (object sender, System.EventArgs e)
		{
//getContext().getCursor().setY( ( float ) System.Convert.ToDouble( entCursorY.Text ) );
			getContext().askForScreenRefresh();
		}
//-----------------------------------------------------------------------------
		protected void OnEntCursorZEditingDone (object sender, System.EventArgs e)
		{
		//	getContext().getCursor().setZ( ( float ) System.Convert.ToDouble( entCursorZ.Text ) );
			getContext().askForScreenRefresh();
		}
//-----------------------------------------------------------------------------
		protected void OnSpnCursorZChangeValue (object o, Gtk.ChangeValueArgs args)
		{
			throw new System.NotImplementedException ();
		}
//-----------------------------------------------------------------------------
		protected void OnSpnCursorZValueChanged (object sender, System.EventArgs e)
		{
			getContext().getCursor().setZ( ( float ) spnCursorZ.Value );
			getContext().askForScreenRefresh();
		}
//-----------------------------------------------------------------------------		
		protected void OnSpnCursorYChangeValue (object o, Gtk.ChangeValueArgs args)
		{
			throw new System.NotImplementedException ();
		}
//-----------------------------------------------------------------------------
		protected void OnSpnCursorYValueChanged (object sender, System.EventArgs e)
		{
			getContext().getCursor().setY( ( float ) spnCursorY.Value );
			getContext().askForScreenRefresh();
		}
//-----------------------------------------------------------------------------
		protected void OnSpnCursorXChangeValue (object o, Gtk.ChangeValueArgs args)
		{
			throw new System.NotImplementedException ();
		}
//-----------------------------------------------------------------------------
		protected void OnSpnCursorXValueChanged (object sender, System.EventArgs e)
		{
			getContext().getCursor().setX( ( float ) spnCursorX.Value );
			getContext().askForScreenRefresh();
		}
//-----------------------------------------------------------------------------
		protected void OnSpnInternalLightningChangeValue (object o, Gtk.ChangeValueArgs args)
		{
			throw new System.NotImplementedException ();
		}
//-----------------------------------------------------------------------------
		protected void OnSpnInternalLightningValueChanged (object sender, System.EventArgs e)
		{
		//	getContext().getCurrentSector().setEmissiveLightningIntensity( ( int ) this.spnInternalLightning.Value );
			getContext().askForScreenRefresh();
		}
//-----------------------------------------------------------------------------
		protected void OnBtnDeleteMapLinkClicked (object sender, EventArgs e)
		{
			Sector s = getContext().getCurrentSector();
			s.setMapLink( null );
			updateWidgets();
		}
//-----------------------------------------------------------------------------
		protected void OnBtnBrowseForDecalsClicked (object sender, System.EventArgs e)
		{

			
			FileChooserDialog chooser = new FileChooserDialog( "Select decal to apply",
			                                                  MainWindow.getInstance(),
			                                                  FileChooserAction.Open,
			                                                  "Cancel", ResponseType.Cancel,
			                                                  "Open", ResponseType.Accept );
			
			FileFilter filterBZK3;

			filterBZK3 = new FileFilter();
			filterBZK3.Name = "Decal files";
			filterBZK3.AddPattern("*.bin");

			chooser.AddFilter(filterBZK3);

			filterBZK3 = new FileFilter();
			filterBZK3.Name = "SVG graphic files";
			filterBZK3.AddPattern("*.svg");

			chooser.AddFilter(filterBZK3);


			
			
			
			if( chooser.Run() == ( int ) ResponseType.Accept ) {
				
				
				String path = chooser.Filename;
				path = path.Substring( path.LastIndexOf("/") + 1 );
				Sector s = getContext().getCurrentSector();

				String decalUri = path;
				s.setDecalAt( cmbSelectFace.Active, decalUri );
				getContext().askForScreenRefresh();	

			}	
			
			chooser.Destroy();
			updateWidgets();
		}
//-----------------------------------------------------------------------------
		protected void OnBtnBrowseForMapsToLinkClicked (object sender, System.EventArgs e)
		{


			FileChooserDialog chooser = new FileChooserDialog( "Please select a map to link",
			                                                  MainWindow.getInstance(),
			                                                  FileChooserAction.Open,
			                                                  "Cancel", ResponseType.Cancel,
			                                                  "Open", ResponseType.Accept );
			
			FileFilter filterBZK3  = new FileFilter();
			filterBZK3.Name = "BZK3 map files";
			filterBZK3.AddPattern("*.level");
			chooser.AddFilter(filterBZK3);
			
			
			
			if( chooser.Run() == ( int ) ResponseType.Accept ) {


				String path = chooser.Filename;

				path = path.Substring( path.LastIndexOf("/") + 1 );
				
				Sector s = getContext().getCurrentSector();

				s.setMapLink( path );
				chooser.Hide();
			}	
			
			chooser.Destroy();
			updateWidgets();
		}
//-----------------------------------------------------------------------------

		protected void OnEntExtraDataChanged (object sender, EventArgs e)
		{
			getContext ().getCurrentSector ().setExtraInformation (entExtraData.Text);
		}
	}
//-----------------------------------------------------------------------------	
}

