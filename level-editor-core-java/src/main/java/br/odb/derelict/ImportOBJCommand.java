package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.World;
import br.odb.libscene.builder.WorldLoader;

public class ImportOBJCommand extends UserCommandLineAction {

	@Override
	public String getHelp() {
		return "import obj";
	}

	@Override
	public int requiredOperands() {
		return 1;
	}

	@Override
	public void run(ConsoleApplication app, String operands) throws Exception {
		World world;
		world = WorldLoader.build( app.openAsInputStream( operands ) );
		((LevelEditor)app).world = world;
		
	}

	@Override
	public String toString() {
		return "importobj";
	}
}
