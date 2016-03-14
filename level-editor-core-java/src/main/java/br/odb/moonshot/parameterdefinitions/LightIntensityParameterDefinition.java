package br.odb.moonshot.parameterdefinitions;

import br.odb.gameapp.command.ApplicationData;
import br.odb.gameapp.command.CommandParameterDefinition;

/**
 * Created by monty on 12/03/16.
 */
public class LightIntensityParameterDefinition extends CommandParameterDefinition<Float> {
	@Override
	public Float obtainFromArguments(String s, ApplicationData applicationData) {
		return null;
	}

	public LightIntensityParameterDefinition() {
		super("light-intensity", "A scalar value for light intensity");
	}
}
