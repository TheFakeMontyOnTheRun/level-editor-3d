package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.LightNode;
import br.odb.libscene.SceneNode;
import br.odb.utils.math.Vec3;

public class SetLight extends UserCommandLineAction {

	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public int requiredOperands() {
		return 5;
	}

	@Override
	public void run(ConsoleApplication app, String operand) throws Exception {
		String[] operands = operand.split( "[ ]+" );
		LevelEditor editor = (LevelEditor) app;
		Vec3 v = new Vec3( Float.parseFloat( operands[ 1 ]), Float.parseFloat( operands[ 2 ]), Float.parseFloat( operands[ 3 ]));
		float light = Float.parseFloat( operands[ 4 ]);
		
		SceneNode subject = editor.world.masterSector.getChild( operands[ 0 ] );
		
		if ( subject instanceof LightNode ) {
			LightNode node = ((LightNode)subject);
			node.intensity = light;
			node.setPositionFromGlobal( v );
		} else {
			app.getClient().alert( "Not a light node." );
		}
	}

	@Override
	public String toString() {
		return "set-light";
	}
}
