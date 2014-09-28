package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.SpaceRegion;
import br.odb.utils.math.Vec3;

public class PickCommand extends UserCommandLineAction {

	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public int requiredOperands() {
		return 3;
	}

	@Override
	public void run( ConsoleApplication app, String operand ) throws Exception {
		LevelEditor editor = (LevelEditor) app;
		String[] operands = operand.split( "[ ]+" );
		SpaceRegion picked = null;
		Vec3 v = new Vec3( Float.parseFloat( operands[ 0 ] ), Float.parseFloat( operands[ 1 ] ), Float.parseFloat( operands[ 2 ] ) );
		picked = editor.world.masterSector.pick( v );
		
		if ( picked != null ) {
			
			app.getClient().printNormal( picked.id );
		}
	}

	@Override
	public String toString() {
		return "pick";
	}

}
