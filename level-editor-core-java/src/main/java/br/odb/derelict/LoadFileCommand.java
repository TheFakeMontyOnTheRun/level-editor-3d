package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.World;
import br.odb.libscene.builder.WorldLoader;

public class LoadFileCommand extends UserCommandLineAction {

	
	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public int requiredOperands() {
		return 1;
	}

	@Override
	public void run(ConsoleApplication app, String operand ) throws Exception {
			World world;
			world = WorldLoader.build( app.openAsInputStream( operand ) );
			((LevelEditor)app).world = world;
	}

	@Override
	public String toString() {
		return "load";
	}

}
