package br.odb.moonshot;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.libscene.LightNode;
import br.odb.libscene.SceneNode;
import br.odb.gameutils.math.Vec3;
import br.odb.moonshot.parameterdefinitions.AbsolutePositionInSpaceParameterDefinition;
import br.odb.moonshot.parameterdefinitions.LightIntensityParameterDefinition;
import br.odb.moonshot.parameterdefinitions.NodeIdParameterDefinition;

public class SetLight extends UserCommandLineAction {

	@Override
	public String getDescription() {
		return "";
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

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[]{ new NodeIdParameterDefinition(), new AbsolutePositionInSpaceParameterDefinition(), new LightIntensityParameterDefinition()};
	}
}
