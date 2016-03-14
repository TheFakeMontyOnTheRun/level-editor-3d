package br.odb.moonshot;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.libscene.LightNode;
import br.odb.moonshot.parameterdefinitions.VacantNodeIdParameterDefinition;

public class NewLight extends UserCommandLineAction {

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public void run(ConsoleApplication app, String operand) throws Exception {
		LevelEditor editor = (LevelEditor) app;
		editor.world.masterSector.addChild( new LightNode( operand.trim() ) );
	}

	@Override
	public String toString() {
		return "new-light";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[]{ new VacantNodeIdParameterDefinition()};
	}
}
