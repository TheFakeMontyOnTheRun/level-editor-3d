package br.odb.moonshot;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.libscene.World;
import br.odb.moonshot.parameterdefinitions.SourceFilenameParameterDefinition;

public class ImportOBJCommand extends UserCommandLineAction {

	@Override
	public String getDescription() {
		return "import obj";
	}

	@Override
	public void run(ConsoleApplication app, String operands) throws Exception {
		World world;
		LevelEditor editor = (LevelEditor) app;
		String[] operand = operands.split( "[ ]+" );
		world = WavefrontOBJImporter.build( editor.openAsInputStream( operand[ 0 ] ), editor.openAsInputStream( operand[ 1 ] ) );
		((LevelEditor)app).world = world;
	}

	@Override
	public String toString() {
		return "import-obj";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[]{ new SourceFilenameParameterDefinition()};
	}
}
