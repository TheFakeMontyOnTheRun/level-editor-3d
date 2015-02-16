package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.CameraNode;
import br.odb.libscene.SceneNode;
import br.odb.utils.math.Vec3;

public class SetCamera extends UserCommandLineAction {

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
		float angleXZ = Float.parseFloat( operands[ 4 ]);
		
		SceneNode subject = editor.world.masterSector.getChild( operands[ 0 ] );
		
		if ( subject instanceof CameraNode ) {
			CameraNode node = ((CameraNode)subject);
			node.angleXZ = angleXZ;
			node.setPositionFromGlobal( v );
		} else {
			app.getClient().alert( "Not a camera node." );
		}
	}

	@Override
	public String toString() {
		return "set-camera";
	}
}
