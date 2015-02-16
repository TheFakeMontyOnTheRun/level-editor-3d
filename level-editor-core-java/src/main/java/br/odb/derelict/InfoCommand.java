package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.builders.GroupSectorBuilder;

public class InfoCommand extends UserCommandLineAction {

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
		LevelEditor editor = (LevelEditor) app;
		

		app.getClient().printNormal( GroupSectorBuilder.gsb.toXML( (GroupSector)editor.world.masterSector.getChild( operand ) ) );
	}

	@Override
	public String toString() {
		return "info";
	}
}
