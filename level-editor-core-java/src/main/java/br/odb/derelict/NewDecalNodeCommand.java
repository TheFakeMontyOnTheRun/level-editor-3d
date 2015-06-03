package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.DecalNode;
import br.odb.utils.Direction;

public class NewDecalNodeCommand extends UserCommandLineAction {

	public NewDecalNodeCommand() {
	}

	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public int requiredOperands() {
		return 3;
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
}
