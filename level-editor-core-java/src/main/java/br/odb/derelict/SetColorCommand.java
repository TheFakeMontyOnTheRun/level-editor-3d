package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libstrip.Material;
import br.odb.utils.Color;
import br.odb.utils.Direction;

public class SetColorCommand extends UserCommandLineAction {

	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public int requiredOperands() {
		return 5;
	}

	@Override
	public void run(ConsoleApplication app, String operands) throws Exception {
		LevelEditor editor = (LevelEditor) app;

		String[] parms;
		parms = operands.split("[ ]+");

		GroupSector target = null;
		Color color;

		target = (GroupSector) editor.world.masterSector.getChild(parms[0]
				.trim());
		
		while ( !( target instanceof GroupSector ) ) {
			
			if ( target.parent != null ) {				
				target = (GroupSector) target.parent;
			}
		}
		
		if ( !( target instanceof GroupSector ) ) {
			return;
		}
		
		Direction d = Direction.getDirectionForSimpleName( parms[ 1 ] );
		
		color = new Color(Float.parseFloat(parms[2]),
				Float.parseFloat(parms[3]), Float.parseFloat(parms[4]));

		Material m = new Material( null, color, null, null );
		target.shades.put( d,  m );

	}

	@Override
	public String toString() {
		return "set-color";
	}
}
