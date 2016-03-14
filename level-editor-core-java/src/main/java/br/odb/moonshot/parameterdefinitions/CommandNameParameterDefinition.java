package br.odb.moonshot.parameterdefinitions;

import br.odb.gameapp.command.ApplicationData;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;

/**
 * Created by monty on 12/03/16.
 */
public class CommandNameParameterDefinition extends CommandParameterDefinition<UserCommandLineAction> {
	public CommandNameParameterDefinition() {
		super("command-name", "The short name for command needing explanation");
	}

	@Override
	public UserCommandLineAction obtainFromArguments(String s, ApplicationData applicationData) {
		return null;
	}
}
