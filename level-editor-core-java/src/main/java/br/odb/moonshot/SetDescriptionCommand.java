package br.odb.moonshot;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.moonshot.parameterdefinitions.LiteralStringParameterDefinition;
import br.odb.moonshot.parameterdefinitions.NodeIdParameterDefinition;

public class SetDescriptionCommand extends UserCommandLineAction {

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public void run(ConsoleApplication app, String operand) throws Exception {
		( (LevelEditor) app).world.masterSector.getChild( operand );
	}

	@Override
	public String toString() {
		return "set-description";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[]{ new NodeIdParameterDefinition(), new LiteralStringParameterDefinition()};
	}
}
