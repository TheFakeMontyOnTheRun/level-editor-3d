package br.odb.moonshot;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libstrip.Material;
import br.odb.gameutils.Color;
import br.odb.gameutils.Direction;
import br.odb.moonshot.parameterdefinitions.ColorRGBComponentsParameterDefinition;
import br.odb.moonshot.parameterdefinitions.DirectionNameParameterDefinition;
import br.odb.moonshot.parameterdefinitions.NodeIdParameterDefinition;

public class SetColorCommand extends UserCommandLineAction {

	@Override
	public String getDescription() {
		return "";
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

		Material m = Material.makeWithColor(color);
		target.shades.put( d,  m );

	}

	@Override
	public String toString() {
		return "set-color";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[]{ new NodeIdParameterDefinition(), new DirectionNameParameterDefinition(), new ColorRGBComponentsParameterDefinition()};
	}
}
