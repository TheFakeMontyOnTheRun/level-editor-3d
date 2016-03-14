package br.odb.moonshot;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserMetaCommandLineAction;

public class QuitCommand extends UserMetaCommandLineAction {

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public void run(ConsoleApplication arg0, String arg1) throws Exception {

	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "quit";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[0];
	}

}
