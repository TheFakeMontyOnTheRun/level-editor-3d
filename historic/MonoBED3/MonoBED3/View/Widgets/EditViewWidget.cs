using System;
using Gtk;
using System.Collections;
//-----------------------------------------------------------------------------
namespace MonoBED3
{
//-----------------------------------------------------------------------------
	[System.ComponentModel.ToolboxItem(true)]
//-----------------------------------------------------------------------------	
	public partial class EditViewWidget : Gtk.Bin, ContextAware, ContextListener, ZoomLevelListener
	{
		private ContextAware currentView;
		private ArrayList views;
//-----------------------------------------------------------------------------
		protected virtual void OnCmbSelectViewChanged (object sender, System.EventArgs e)
		{
			setView(this.cmbSelectView.Active);
		}
//-----------------------------------------------------------------------------		
		public void setView(int id)
		{
			this.vboxViewSelectorWidget.Remove(getCurrentGtkView());
			currentView = (ContextAware)views[id];
			this.vboxViewSelectorWidget.Add(getCurrentGtkView());
			currentView.setContext( getContext() );
			redraw();
		}
//-----------------------------------------------------------------------------		
		public void zoomLevelChanged( byte zoom ) {
			spnZoom.Value = zoom;
		}
//-----------------------------------------------------------------------------		
		public EditViewWidget ()
		{			
			this.Build ();			
			
			//editContext = null;
			
			views = new ArrayList();
			views.Add( new XZView( this ));
			views.Add( new XYView( this ));
			views.Add( new PaletteWidget());
			currentView = (ContextAware)views[0];
			this.vboxViewSelectorWidget.Add((Gtk.Widget)currentView);
			redraw();
		}
//-----------------------------------------------------------------------------
		public void setContext(EditorContext context) {
			
			for ( int c = 0; c < views.Count; ++c ) {
				
				if ( views[ c ] is ContextAware ) {
					
					Logger.log( "views[ " + c + " ] = " + views[ c ] + "<-" + context );
					( ( ContextAware ) views[ c ] ).setContext(context);
				}
			}
		}
//-----------------------------------------------------------------------------		
		public EditorContext getContext()
		{
			return MainWindow.editContext;
		}
//-----------------------------------------------------------------------------		
		protected virtual void OnExposeEvent (object o, Gtk.ExposeEventArgs args)
		{
			redraw();
		}
//-----------------------------------------------------------------------------
		private void redraw()
		{
			Show();
			if (currentView != null)
				getCurrentGtkView().Show();			
		}		
//-----------------------------------------------------------------------------		
		private Gtk.Widget getCurrentGtkView()
		{
			if (currentView != null)
				return (Gtk.Widget)currentView;
			
			return null;
		}
//-----------------------------------------------------------------------------
		public void contextReset() {
		}
//-----------------------------------------------------------------------------		
		public void contextModified() {
			foreach ( object cl in views ) {
				if ( cl != null && cl is ContextListener )
					( ( ContextListener ) cl ).contextModified();
			}
		}
//-----------------------------------------------------------------------------
		protected void OnKeyPressEvent (object o, Gtk.KeyPressEventArgs args)
		{
			int diff = 0;
			
			switch ( args.Event.Key ) {
				case Gdk.Key.A:
				case Gdk.Key.a:
					diff = + 1;
					break;
				
				case Gdk.Key.z:
				case Gdk.Key.Z:
					diff = - 1;
					break;
				
			}
			
			spnZoom.Value += diff;
			
			broadcastZoomLevel();
			getContext().askForScreenRefresh();
		}
//-----------------------------------------------------------------------------
		private void broadcastZoomLevel() {

			foreach ( object cl in views ) {
				if ( cl != null && cl is Zoomable )
					( ( Zoomable ) cl ).setZoomLevel( ( byte ) spnZoom.Value );
			}
		}
//-----------------------------------------------------------------------------
		protected void OnSpnZoomValueChanged (object sender, System.EventArgs e)
		{
			broadcastZoomLevel();
		}
//-----------------------------------------------------------------------------
		protected void OnChkDrawGridToggled (object sender, System.EventArgs e)
		{
			foreach ( object cl in views ) {
				if ( cl != null && cl is Drawable )
					( ( Drawable ) cl ).setDrawGrid( chkDrawGrid.Active );
			}
		}
//-----------------------------------------------------------------------------
		protected void OnBtnResetClicked (object sender, System.EventArgs e)
		{	
			this.spnZoom.Value = 10;
			
			foreach ( object cl in views ) {
				if ( cl != null && cl is Drawable )
					( ( Zoomable ) cl ).reset();
			}
		}
//-----------------------------------------------------------------------------
		protected void OnChkDrawSolidsToggled (object sender, System.EventArgs e)
		{
			foreach ( object cl in views ) {
				if ( cl != null && cl is Drawable )
					( ( Drawable ) cl ).setDrawAsSolids( chkDrawAsSolids.Active );
			}
			
			getContext().askForScreenRefresh();
		}
//-----------------------------------------------------------------------------
		protected void OnChkDrawExtendingLinesToggled (object sender, System.EventArgs e)
		{
			foreach ( object cl in views ) {
				if ( cl != null && cl is Drawable )
					( ( Drawable ) cl ).setDrawExtendingLines( this.chkDrawExtendingLines.Active );
			}
			
			getContext().askForScreenRefresh();
		}
//-----------------------------------------------------------------------------
	}
//-----------------------------------------------------------------------------
}
//-----------------------------------------------------------------------------