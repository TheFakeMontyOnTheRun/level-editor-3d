package br.odb.moonshot;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.libscene.SceneNode;
import br.odb.gameutils.math.Vec3;
import br.odb.moonshot.parameterdefinitions.AbsolutePositionInSpaceParameterDefinition;
import br.odb.moonshot.parameterdefinitions.NodeIdParameterDefinition;

public class MoveCommand extends UserCommandLineAction {

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public void run(ConsoleApplication app, String operands) throws Exception {
		LevelEditor editor = (LevelEditor) app;
		String[] parms;
		
		parms = operands.split( "[ ]+" );
		
		SceneNode target = null;
		Vec3 pos;
		
		if ( parms.length >= 4 ) {
			
			target = editor.world.masterSector.getChild( parms[ 0 ].trim() );
			pos = new Vec3( Float.parseFloat( parms[ 1 ]), Float.parseFloat( parms[ 2 ]), Float.parseFloat( parms[ 3 ]) );
			
			if ( target != null && pos != null ) {
				target.setPositionFromGlobal( pos );
			}
		}
	}

	@Override
	public String toString() {
		return "move";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[]{ new NodeIdParameterDefinition(), new AbsolutePositionInSpaceParameterDefinition()};
	}

}
