package br.odb.moonshot.parameterdefinitions;

import br.odb.gameapp.command.ApplicationData;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameutils.math.Vec3;

/**
 * Created by monty on 12/03/16.
 */
public class DimensionParameterDefinition extends CommandParameterDefinition<Vec3> {
	public DimensionParameterDefinition() {
		super("dimensions", "Volumetric dimensions");
	}

	@Override
	public Vec3 obtainFromArguments(String s, ApplicationData applicationData) {
		return null;
	}
}
