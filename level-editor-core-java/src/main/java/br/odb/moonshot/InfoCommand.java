package br.odb.moonshot;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.builders.GroupSectorBuilder;
import br.odb.moonshot.parameterdefinitions.NodeIdParameterDefinition;

public class InfoCommand extends UserCommandLineAction {

	@Override
	public String getDescription() {
		return "";
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

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[]{ new NodeIdParameterDefinition()};
	}
}
