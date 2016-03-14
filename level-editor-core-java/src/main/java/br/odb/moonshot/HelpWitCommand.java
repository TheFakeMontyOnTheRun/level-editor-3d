package br.odb.moonshot;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserMetaCommandLineAction;
import br.odb.moonshot.parameterdefinitions.CommandNameParameterDefinition;

public class HelpWitCommand extends UserMetaCommandLineAction {

	@Override
	public String getDescription() {
		return "Get help";
	}

	@Override
	public void run(ConsoleApplication app, String arg1) throws Exception {
		LevelEditor editor = (LevelEditor) app; 
		editor.showHelpFor( arg1 );
	}

	@Override
	public String toString() {
		return "help-with";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[]{ new CommandNameParameterDefinition()};
	}

}
