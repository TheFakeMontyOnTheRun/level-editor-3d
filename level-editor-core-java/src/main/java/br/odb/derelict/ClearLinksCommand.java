package br.odb.derelict;

import br.odb.gameapp.ApplicationClient;
import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.Sector;
import br.odb.libscene.SpaceRegion;
import br.odb.utils.Direction;

public class ClearLinksCommand extends UserCommandLineAction {

	public ClearLinksCommand() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int requiredOperands() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void run(ConsoleApplication app, String arg1) throws Exception {
		LevelEditor editor = (LevelEditor) app; 
		clearMesh( app.getClient(), editor.world.masterSector );

	}

	private void clearMesh(ApplicationClient client, GroupSector sector) {
		for ( SpaceRegion sr : sector.getSons() ) {
			if ( sr instanceof GroupSector ) {
				clearMesh( client, (GroupSector) sr );
			} else if ( sr instanceof Sector ) {
				
				for ( int c = 0; c < Direction.values().length; ++c ) {
					((Sector) sr ).links[ c ] = null;	
				}				
			}
		}		
		
		client.alert( "removing links from sector " + sector.id + " sons." );

	}

	@Override
	public String toString() {
		return "clear-meshes";
	}

}
