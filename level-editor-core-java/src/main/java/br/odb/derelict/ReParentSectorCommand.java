package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;

public class ReParentSectorCommand extends UserCommandLineAction {

	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public int requiredOperands() {
		return 2;
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

}
