package br.odb.derelict;

import br.odb.gameapp.ApplicationClient;
import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;

public class ClearMeshCommand extends UserCommandLineAction {

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int requiredOperands() {
		return 0;
	}

	@Override
	public void run(ConsoleApplication app, String arg1) throws Exception {
		LevelEditor editor = (LevelEditor) app; 
		clearMesh( app.getClient(), editor.world.masterSector );

	}

	private void clearMesh(ApplicationClient client, GroupSector sector) {
		for ( SceneNode sr : sector.getSons() ) {
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
