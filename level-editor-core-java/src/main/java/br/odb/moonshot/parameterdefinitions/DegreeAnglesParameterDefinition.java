package br.odb.moonshot.parameterdefinitions;

import br.odb.gameapp.command.ApplicationData;
import br.odb.gameapp.command.CommandParameterDefinition;

/**
 * Created by monty on 12/03/16.
 */
public class DegreeAnglesParameterDefinition extends CommandParameterDefinition<Float> {
	public DegreeAnglesParameterDefinition() {
		super("angle", "Angle in degrees");
	}

	@Override
	public Float obtainFromArguments(String s, ApplicationData applicationData) {
		return null;
	}
}
