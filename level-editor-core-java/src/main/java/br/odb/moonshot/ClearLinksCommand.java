package br.odb.moonshot;

import br.odb.gameapp.ApplicationClient;
import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;
import br.odb.libscene.Sector;
import br.odb.gameutils.Direction;

import java.util.ArrayList;
import java.util.List;

public class ClearLinksCommand extends UserCommandLineAction {

	@Override
	public String getDescription() {
		return "";
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
		return "clear-links";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[0];
	}

}
