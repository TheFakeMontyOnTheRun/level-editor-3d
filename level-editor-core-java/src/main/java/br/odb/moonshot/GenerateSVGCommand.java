package br.odb.moonshot;

import java.io.OutputStream;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.libscene.SceneNode;
import br.odb.libscene.SpaceRegion;
import br.odb.libscene.util.SceneTesselator;
import br.odb.libstrip.builders.GeneralTriangleFactory;
import br.odb.gameutils.Color;
import br.odb.gameutils.Direction;
import br.odb.gameutils.math.Vec3;

public class GenerateSVGCommand extends UserCommandLineAction {

	@Override
	public String getDescription() {
		return "generates SVG view for map";
	}

	@Override
	public void run(ConsoleApplication app, String arg1) throws Exception {
		OutputStream os;
		LevelEditor editor = (LevelEditor) app;
		os = editor.openAsOutputStream("view.svg");
		os.write( SVGRenderer.renderXZ( editor.world ).getBytes() );
		SceneTesselator tesselator = new SceneTesselator( new GeneralTriangleFactory() );
		os = editor.openAsOutputStream("view.opt.svg");
		StringBuilder sb = new StringBuilder(
				"<?xml version='1.0' encoding='UTF-8' standalone='no'?>\n<svg xmlns='http://www.w3.org/2000/svg'>\n");
		Color c;
		Vec3 v;
		
		for (SceneNode sr : editor.world.getAllRegionsAsList()) {

			if (sr instanceof SpaceRegion ) {
				
				v = sr.getAbsolutePosition();
				
				sb.append("<rect ");
				sb.append(" id = '" + sr.id + "_" + System.currentTimeMillis() + "' ");
				sb.append(" x = '" + v.x + "' ");
				sb.append(" y = '" + v.z + "' ");
				sb.append(" width = '" + ((SpaceRegion)sr).size.x + "' ");
				sb.append(" height = '" + ((SpaceRegion)sr).size.z + "' ");
				c = tesselator.getColorForFace( Direction.FLOOR, ((SpaceRegion)sr) ).mainColor;

				sb.append(" style = 'fill: " + c.getHTMLColor() + ";' ");

				sb.append(" />\n");
			}

		}

		sb.append("\n</svg>");

		os.write(sb.toString().getBytes());
	}

	@Override
	public String toString() {
		return "svg";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[0];
	}
}
