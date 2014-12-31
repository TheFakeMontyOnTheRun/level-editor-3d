package br.odb.derelict;

import br.odb.gameapp.ApplicationClient;
import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SpaceRegion;

public class ClearMeshCommand extends UserCommandLineAction {

	public ClearMeshCommand() {
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
			}
		}		
		
		client.alert( "removing meshes from sector " + sector.id );
		sector.mesh.clear();
	}

	@Override
	public String toString() {
		return "clear-meshes";
	}

}