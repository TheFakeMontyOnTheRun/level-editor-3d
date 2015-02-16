package br.odb.derelict;

import java.io.OutputStream;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.SceneNode;
import br.odb.libscene.SpaceRegion;
import br.odb.libscene.util.SceneTesselator;
import br.odb.libstrip.GeneralTriangleFactory;
import br.odb.utils.Color;
import br.odb.utils.math.Vec3;

public class GenerateSVGCommand extends UserCommandLineAction {

	@Override
	public String getHelp() {
		return "generates SVG view for map";
	}

	@Override
	public int requiredOperands() {
		return 0;
	}

	@Override
	public void run(ConsoleApplication app, String arg1) throws Exception {
		OutputStream os;
		LevelEditor editor = (LevelEditor) app;
		os = editor.openAsOutputStream("/Users/monty/view.svg");
		os.write( SVGRenderer.renderXZ( editor.world ).getBytes() );
		SceneTesselator tesselator = new SceneTesselator( new GeneralTriangleFactory() );
		os = editor.openAsOutputStream("/Users/monty/view.opt.svg");
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
				c = tesselator.getColorForFace( ((SpaceRegion)sr) );

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
}
