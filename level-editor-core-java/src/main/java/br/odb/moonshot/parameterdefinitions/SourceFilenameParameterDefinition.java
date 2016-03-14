package br.odb.moonshot.parameterdefinitions;

import br.odb.gameapp.command.ApplicationData;
import br.odb.gameapp.command.CommandParameterDefinition;

/**
 * Created by monty on 12/03/16.
 */
public class SourceFilenameParameterDefinition extends CommandParameterDefinition<String> {
	@Override
	public String obtainFromArguments(String s, ApplicationData applicationData) {
		return null;
	}

	public SourceFilenameParameterDefinition() {
		super("source-filename", "A source file name");
	}
}
