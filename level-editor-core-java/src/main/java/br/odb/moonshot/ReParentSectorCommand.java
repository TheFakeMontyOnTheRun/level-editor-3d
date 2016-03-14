package br.odb.moonshot;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;
import br.odb.moonshot.parameterdefinitions.NodeIdParameterDefinition;

public class ReParentSectorCommand extends UserCommandLineAction {

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public void run(ConsoleApplication app, String operand) throws Exception {
		LevelEditor editor = (LevelEditor) app;
		String[] operands;
		
		operands = operand.split( "[ ]+" );
		SceneNode subject = editor.world.masterSector.getChild( operands[ 0 ] );
		SceneNode newParent = editor.world.masterSector.getChild( operands[ 1 ] );
		
		if ( subject == null || newParent == null ) {
			app.getClient().alert( "invalid operands" );
		}
		
		if ( newParent instanceof GroupSector ) {
			( ( GroupSector) newParent ).addChild( subject );
		}
		
		subject.parent = newParent;
	}

	@Override
	public String toString() {
		return "reparent-sector";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[]{ new NodeIdParameterDefinition(), new NodeIdParameterDefinition()};
	}
}
