package br.odb.moonshot;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserMetaCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SpaceRegion;
import br.odb.gameutils.math.Vec3;
import br.odb.moonshot.parameterdefinitions.DimensionParameterDefinition;
import br.odb.moonshot.parameterdefinitions.NodeIdParameterDefinition;

public class ResizeCommand extends UserMetaCommandLineAction {
	@Override
	public String getDescription() {
		return "";
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
				target.size.set( pos );
			}
		}
	}

	@Override
	public String toString() {		
		return "resize";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[]{ new NodeIdParameterDefinition(), new DimensionParameterDefinition()};
	}
}
