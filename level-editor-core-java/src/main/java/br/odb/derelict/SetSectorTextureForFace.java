package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SpaceRegion;
import br.odb.utils.Direction;

public class SetSectorTextureForFace extends UserCommandLineAction {

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int requiredOperands() {
		return 2;
	}

	@Override
	public void run(ConsoleApplication app, String args) throws Exception {
		String[] arg;
		GroupSector gs;
		SpaceRegion sr;
		Direction d;
		LevelEditor editor = (LevelEditor) app;
		arg = args.split( "[ ]+" );
		
		sr = editor.world.masterSector.getChild( arg[ 0 ] );
		if ( sr instanceof GroupSector ) {
		
		} else {
			app.getClient().alert( "Not a GroupSector" );
		}
	}

	@Override
	public String toString() {
		return "set-texture";
	}
}
