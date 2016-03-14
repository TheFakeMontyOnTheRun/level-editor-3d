package br.odb.moonshot.parameterdefinitions;

import br.odb.gameapp.command.ApplicationData;
import br.odb.gameapp.command.CommandParameterDefinition;

/**
 * Created by monty on 12/03/16.
 */
public class VacantNodeIdParameterDefinition extends CommandParameterDefinition<String> {
	public VacantNodeIdParameterDefinition() {
		super("node-id", "A vacant node Id");
	}

	@Override
	public String obtainFromArguments(String s, ApplicationData applicationData) {
		return null;
	}
}
