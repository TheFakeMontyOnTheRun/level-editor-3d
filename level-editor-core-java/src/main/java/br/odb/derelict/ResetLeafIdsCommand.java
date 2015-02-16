package br.odb.derelict;

import br.odb.gameapp.ApplicationClient;
import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;
import br.odb.libscene.Sector;

public class ResetLeafIdsCommand extends UserCommandLineAction {

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
		resetIds( app.getClient(), editor.world.masterSector, ( "parents".equalsIgnoreCase( arg1 ) ) );

	}

	private void resetIds(ApplicationClient client, GroupSector sector, boolean parentsToo ) {
		
		int counter = 0;
		
		for ( SceneNode sr : sector.getSons() ) {
			
			if ( sr instanceof GroupSector ) {
				resetIds( client, (GroupSector) sr, parentsToo );
			} 
			
			if ( sr instanceof Sector || parentsToo ) {
				sr.id = "" + counter++;
			}
		}		
		
		client.alert( "resetting Ids from sector " + sector.id + " sons." );

	}

	@Override
	public String toString() {
		return "reset-leaf-ids";
	}
}
