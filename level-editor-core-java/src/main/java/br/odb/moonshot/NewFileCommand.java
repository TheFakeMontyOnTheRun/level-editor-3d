package br.odb.moonshot;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserMetaCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.World;

public class NewFileCommand extends UserMetaCommandLineAction {
	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public void run(ConsoleApplication app, String operand ) throws Exception {
		LevelEditor editor = (LevelEditor) app;
		editor.world = new World( new GroupSector( "root" ) );
	}

	@Override
	public String toString() {
		return "new-file";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[0];
	}
}
