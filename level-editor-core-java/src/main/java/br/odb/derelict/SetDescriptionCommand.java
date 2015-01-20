package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;

public class SetDescriptionCommand extends UserCommandLineAction {

	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public int requiredOperands() {
		return 1;
	}

	@Override
	public void run(ConsoleApplication app, String operand) throws Exception {
		( (LevelEditor) app).world.masterSector.getChild( operand );

	}

	@Override
	public String toString() {
		return "set-description";
	}
}
