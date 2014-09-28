package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SpaceRegion;
import br.odb.utils.Color;
import br.odb.utils.Direction;

public class SetColorCommand extends UserCommandLineAction {

	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public int requiredOperands() {
		return 4;
	}

	@Override
	public void run(ConsoleApplication app, String operands) throws Exception {
		LevelEditor editor = (LevelEditor) app;

		String[] parms;
		Direction d;
		parms = operands.split("[ ]+");

		SpaceRegion target = null;
		Color color;

		d = Direction.getDirectionForPrettyName(parms[1]);

		if (d == null) {

			d = Direction.getDirectionForSimpleName(parms[1]);
		}

		target = (GroupSector) editor.world.masterSector.getChild(parms[0]
				.trim());
		color = new Color(Float.parseFloat(parms[2]),
				Float.parseFloat(parms[3]), Float.parseFloat(parms[4]));

		target.colorForDirection.put(d, color);

	}

	@Override
	public String toString() {
		return "set-color";
	}
}
