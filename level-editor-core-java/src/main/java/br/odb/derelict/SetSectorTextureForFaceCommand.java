package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;
import br.odb.libstrip.Material;
import br.odb.utils.Direction;

public class SetSectorTextureForFaceCommand extends UserCommandLineAction {

	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public int requiredOperands() {
		return 2;
	}

	@Override
	public void run(ConsoleApplication app, String args) throws Exception {
		String[] arg;
		SceneNode sr;
		GroupSector gs;
		LevelEditor editor = (LevelEditor) app;
		arg = args.split( "[ ]+" );
		
		sr = editor.world.masterSector.getChild( arg[ 0 ] );
		
		if ( sr instanceof GroupSector ) {
			gs = (GroupSector)sr;
			Direction d = Direction.valueOf( arg[ 1 ] );
			gs.shades.put( d, new Material( "mat_" + d.simpleName + "_" + arg[ 2 ], null, arg[ 2 ], null, null  ) );
		} else {
			app.getClient().alert( "Not a GroupSector" );
		}
	}

	@Override
	public String toString() {
		return "set-texture";
	}
}
