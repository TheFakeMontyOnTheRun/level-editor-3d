package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SpaceRegion;
import br.odb.utils.math.Vec3;

public class MoveCommand extends UserCommandLineAction {

	
	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public int requiredOperands() {
		return 4;
	}

	@Override
	public void run(ConsoleApplication app, String operands) throws Exception {
		LevelEditor editor = (LevelEditor) app;
		String[] parms;
		
		parms = operands.split( "[ ]+" );
		
		SpaceRegion target = null;
		Vec3 pos;
		
		if ( parms.length >= 4 ) {
			
			target = (GroupSector) editor.world.masterSector.getChild( parms[ 0 ].trim() );
			pos = new Vec3( Float.parseFloat( parms[ 1 ]), Float.parseFloat( parms[ 2 ]), Float.parseFloat( parms[ 3 ]) );
			
			if ( target != null && pos != null ) {
				target.position.set( pos );
			}
		}
	}

	@Override
	public String toString() {
		return "move";
	}

}
