using System;
//-----------------------------------------------------------------------------
namespace MonoBED3
{
//-----------------------------------------------------------------------------
	public partial class MapProperties : Gtk.Dialog
	{
//-----------------------------------------------------------------------------
		public MapProperties ()
		{
			this.Build ();
		}
//-----------------------------------------------------------------------------
		protected void OnButtonOkClicked (object sender, System.EventArgs e)
		{
			this.Destroy();
		}
//-----------------------------------------------------------------------------
		protected void OnBtnRunDiagnosticsActivated (object sender, System.EventArgs e)
		{
		}
//-----------------------------------------------------------------------------
		protected void OnBtnRunDiagnosticsClicked (object sender, System.EventArgs e)
		{
			this.txtDiagnostics.Buffer.Text = "Not implemented yet";
			QueueDraw();
		}
	}
//-----------------------------------------------------------------------------
}
//-----------------------------------------------------------------------------