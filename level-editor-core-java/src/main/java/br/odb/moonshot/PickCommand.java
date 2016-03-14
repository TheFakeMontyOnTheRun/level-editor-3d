package br.odb.moonshot;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.libscene.SpaceRegion;
import br.odb.gameutils.math.Vec3;
import br.odb.moonshot.parameterdefinitions.AbsolutePositionInSpaceParameterDefinition;

public class PickCommand extends UserCommandLineAction {

	@Override
	public String getDescription() {
		return "";
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

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[]{ new AbsolutePositionInSpaceParameterDefinition()};
	}
}
