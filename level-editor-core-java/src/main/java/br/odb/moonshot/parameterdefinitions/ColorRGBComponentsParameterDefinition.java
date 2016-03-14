package br.odb.moonshot.parameterdefinitions;

import br.odb.gameapp.command.ApplicationData;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameutils.Color;

/**
 * Created by monty on 12/03/16.
 */
public class ColorRGBComponentsParameterDefinition extends CommandParameterDefinition<Color> {
	public ColorRGBComponentsParameterDefinition() {
		super("color", "RGB 0..255 components");
	}

	@Override
	public Color obtainFromArguments(String s, ApplicationData applicationData) {
		return null;
	}
}
