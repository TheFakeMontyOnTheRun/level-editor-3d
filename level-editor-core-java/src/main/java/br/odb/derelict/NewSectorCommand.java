package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.GroupSector;

public class NewSectorCommand extends UserCommandLineAction {

	public NewSectorCommand(ConsoleApplication app) {
		super( );
	}

	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public int requiredOperands() {
		return 1;
	}

	@Override
	public void run(ConsoleApplication app, String operand ) throws Exception {
		
		LevelEditor editor = (LevelEditor) app;
		String[] parms;
		
		parms = operand.split( "[ ]+" );
		
		GroupSector parent = null;
		
		if ( parms.length > 1 && parms[ 1 ] != null ) {
			parent = (GroupSector) editor.world.masterSector.getChild( parms[ 1 ].trim() );
		}
		
		if ( parent == null ) {
			parent = editor.world.masterSector;
		}
		
		parent.addChild( new GroupSector( parms[ 0 ].trim() ) );
	}

	@Override
	public String toString() {
		return "new-sector";
	}

}
