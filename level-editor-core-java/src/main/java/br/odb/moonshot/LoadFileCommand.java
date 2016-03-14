package br.odb.moonshot;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.libscene.World;
import br.odb.libscene.builders.WorldLoader;
import br.odb.moonshot.parameterdefinitions.SourceFilenameParameterDefinition;

public class LoadFileCommand extends UserCommandLineAction {

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public void run(ConsoleApplication app, String operand ) throws Exception {
			World world;
			LevelEditor editor = (LevelEditor) app;
			world = WorldLoader.build( editor.openAsInputStream( operand ) );
			((LevelEditor)app).world = world;
	}

	@Override
	public String toString() {
		return "load";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[]{ new SourceFilenameParameterDefinition()};
	}

}
