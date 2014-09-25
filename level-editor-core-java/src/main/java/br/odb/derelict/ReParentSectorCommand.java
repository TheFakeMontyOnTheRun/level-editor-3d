package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SpaceRegion;

public class ReParentSectorCommand extends UserCommandLineAction {

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
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
		SpaceRegion subject = editor.world.masterSector.getChild( operands[ 0 ] );
		SpaceRegion newParent = editor.world.masterSector.getChild( operands[ 1 ] );
		
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
