package br.odb.moonshot;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.libscene.CameraNode;
import br.odb.moonshot.parameterdefinitions.AbsolutePositionInSpaceParameterDefinition;

public class NewCamera extends UserCommandLineAction {

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public void run(ConsoleApplication app, String operand) throws Exception {
		LevelEditor editor = (LevelEditor) app;
		editor.world.masterSector.addChild( new CameraNode( operand.trim() ) );
	}

	@Override
	public String toString() {
		return "new-camera";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[]{ new AbsolutePositionInSpaceParameterDefinition()};
	}
}
