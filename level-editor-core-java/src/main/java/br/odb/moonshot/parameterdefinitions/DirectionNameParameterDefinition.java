package br.odb.moonshot.parameterdefinitions;

import br.odb.gameapp.command.ApplicationData;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameutils.Direction;

/**
 * Created by monty on 12/03/16.
 */
public class DirectionNameParameterDefinition extends CommandParameterDefinition<Direction> {
	public DirectionNameParameterDefinition() {
		super("direction", "Direction name (one capital leter)");
	}

	@Override
	public Direction obtainFromArguments(String s, ApplicationData applicationData) {
		return null;
	}
}
