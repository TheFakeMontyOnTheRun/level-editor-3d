package br.odb.moonshot.parameterdefinitions;

import br.odb.gameapp.command.ApplicationData;
import br.odb.gameapp.command.CommandParameterDefinition;

/**
 * Created by monty on 12/03/16.
 */
public class DestinationFilenameParameterDefinition extends CommandParameterDefinition<String> {
	public DestinationFilenameParameterDefinition() {
		super("filename", "Destination filename");
	}

	@Override
	public String obtainFromArguments(String s, ApplicationData applicationData) {
		return null;
	}
}
