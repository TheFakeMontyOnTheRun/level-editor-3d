package br.odb.derelict;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;

public class SetSectorTextureForFace extends UserCommandLineAction {

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
		LevelEditor editor = (LevelEditor) app;
		arg = args.split( "[ ]+" );
		
		sr = editor.world.masterSector.getChild( arg[ 0 ] );
		if ( sr instanceof GroupSector ) {
			throw new Exception( "Fat ass has not tried his fat fingers into this functionality");
		} else {
			app.getClient().alert( "Not a GroupSector" );
		}
	}

	@Override
	public String toString() {
		return "set-texture";
	}
}
