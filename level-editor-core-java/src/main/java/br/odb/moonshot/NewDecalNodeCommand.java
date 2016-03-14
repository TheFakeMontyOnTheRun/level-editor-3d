package br.odb.moonshot;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.libscene.DecalNode;
import br.odb.gameutils.Direction;
import br.odb.moonshot.parameterdefinitions.DirectionNameParameterDefinition;
import br.odb.moonshot.parameterdefinitions.SourceFilenameParameterDefinition;
import br.odb.moonshot.parameterdefinitions.VacantNodeIdParameterDefinition;

public class NewDecalNodeCommand extends UserCommandLineAction {

	public NewDecalNodeCommand() {
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public void run(ConsoleApplication app, String operand) throws Exception {
		String[] operands = operand.split( "[ ]+" );
		LevelEditor editor = (LevelEditor) app;
		if ( operand.trim().length() > 1 ) {
			editor.world.masterSector.addChild( new DecalNode( operands[ 0 ], Direction.valueOf( operands[ 1 ] ), operands[ 2 ] ) );	
		} else {
			app.getClient().alert( "Provide a id and a decal identifier" );
		}

	}

	@Override
	public String toString() {
		return "new-decal";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[]{ new VacantNodeIdParameterDefinition(), new DirectionNameParameterDefinition(), new SourceFilenameParameterDefinition() };
	}
}
