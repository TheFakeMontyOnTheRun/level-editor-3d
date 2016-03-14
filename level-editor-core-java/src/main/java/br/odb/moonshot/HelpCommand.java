package br.odb.moonshot;

import br.odb.gameapp.ApplicationClient;
import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.gameapp.command.UserMetaCommandLineAction;
import br.odb.gameutils.Direction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;
import br.odb.libscene.Sector;

public class HelpCommand extends UserMetaCommandLineAction {

	@Override
	public String getDescription() {
		return "Get help";
	}

	@Override
	public void run(ConsoleApplication app, String arg1) throws Exception {
		LevelEditor editor = (LevelEditor) app; 
		editor.showHelp();
	}

	@Override
	public String toString() {
		return "help";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[0];
	}

}
