using System;
using Gtk;
using br.odb.libscene;
using br.odb.portalizer;

namespace MonoBED3
{
	public partial class ExportMap : Gtk.Dialog
	{
		br.odb.libscene.World worldBefore;

		public ExportMap ( br.odb.libscene.World worldBefore )
		{
			this.worldBefore = worldBefore;
			this.Build ();
			updateCombo();
		}

		protected void OnButtonOkClicked (object sender, EventArgs e)
		{
			this.Destroy();
		}
		
		private void updateCombo() {
			
	        CellRendererText cell;
			ListStore store;
			
			cmbSectorsToPlaceCamera.Clear();
			
			cell = new CellRendererText();
	        cmbSectorsToPlaceCamera.PackStart(cell, false );
	        cmbSectorsToPlaceCamera.AddAttribute(cell, "text", 0);
	        store = new ListStore(typeof (string));
			cmbSectorsToPlaceCamera.Model = store;
			
		
			store.AppendValues ("-" );
			
			

			for ( int c = 1; c < worldBefore.getTotalSectors(); ++c ) {

		       		store.AppendValues ("Sector " + c );
			}
			
			EditorContext context = MainWindow.getInstance().getContext();


				
				
			
			this.cmbSectorsToPlaceCamera.Active = ( worldBefore.getTotalSectors() > 1) ? context.getCurrentSector().getId() : 0;
			
		}
	
		protected void OnBtnGenerateClicked (object sender, System.EventArgs e)
		{
			World.snapLevel = 2;
			WorldPartitioner.transientStartSector = cmbSectorsToPlaceCamera.Active;

			ConsoleGeometryCompiler compiler = new ConsoleGeometryCompiler();
			Portalizer portalizer = new Portalizer();
			portalizer.setFileServer( compiler );
			compiler.setClient( new Portalizer() );
			compiler.setArgs( new string[]{ "" } );
			compiler.prepareFor( worldBefore );
			compiler.run();

			br.odb.libscene.Actor actor = new br.odb.libscene.Actor();
			actor.setCurrentSector( WorldPartitioner.transientStartSector );

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
				String filename =  fc.Filename.ToString();
				fc.Destroy();


				worldBefore.saveToDiskAsLevel( compiler.openAsOutputStream( filename ) );

				MessageDialog md = new MessageDialog ( this, DialogFlags.Modal, MessageType.Info, ButtonsType.Ok, "Export suceeded.");
				md.Run ();
				md.Destroy();
			}	
			fc.Destroy();			
		}
	}
}

