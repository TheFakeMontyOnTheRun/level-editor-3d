package br.odb.derelict;

import java.io.OutputStream;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneTesselator;
import br.odb.libscene.Sector;
import br.odb.libscene.SpaceRegion;
import br.odb.utils.Color;
import br.odb.utils.Direction;
import br.odb.utils.math.Vec3;
import br.odb.worldprocessing.Utils;

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
		os.write( SVGRenderer.renderXZ( SceneTesselator.generateSubSectorQuadsForWorld( editor.world ) ).getBytes() );
		
		os = editor.openAsOutputStream("/Users/monty/view.opt.svg");
		StringBuilder sb = new StringBuilder(
				"<?xml version='1.0' encoding='UTF-8' standalone='no'?>\n<svg xmlns='http://www.w3.org/2000/svg'>\n");
		Color c;
		Vec3 v;
		
		for (SpaceRegion sr : Utils
				.getAllRegionsAsList(editor.world.masterSector)) {

			if (sr instanceof Sector || sr instanceof GroupSector ) {
				
				v = sr.getAbsolutePosition();
				
				sb.append("<rect ");
				sb.append(" id = '" + sr.id + "_" + System.currentTimeMillis() + "' ");
				sb.append(" x = '" + v.x + "' ");
				sb.append(" y = '" + v.z + "' ");
				sb.append(" width = '" + sr.size.x + "' ");
				sb.append(" height = '" + sr.size.z + "' ");

				if (sr.colorForDirection.get(Direction.FLOOR) != null) {
					c = sr.colorForDirection.get(Direction.FLOOR);
				} else {
					c = new Color(64, 64, 64, 64);
				}

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
