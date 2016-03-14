/**
 *
 */
package br.odb.moonshot;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.libscene.CameraNode;
import br.odb.libscene.GroupSector;
import br.odb.libscene.LightNode;
import br.odb.libscene.SceneNode;
import br.odb.libscene.World;
import br.odb.libstrip.Material;
import br.odb.gameutils.Color;
import br.odb.gameutils.Direction;
import br.odb.moonshot.parameterdefinitions.DestinationFilenameParameterDefinition;

/**
 * @author monty
 */
public class ImportGEOCommand extends UserCommandLineAction {

	/**
	 *
	 */
	public ImportGEOCommand() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDescription() {
		return "";
	}

	/* (non-Javadoc)
	 * @see br.odb.gameapp.UserCommandLineAction#run(br.odb.gameapp.ConsoleApplication, java.lang.String)
	 */
	@Override
	public void run(ConsoleApplication app, String operand) throws Exception {
		GroupSector master = new GroupSector("_root");
		LevelEditor editor = (LevelEditor) app;
		InputStream openFile = editor.openAsInputStream(operand);
		List<GroupSector> sectors = new ArrayList<>();
		Scanner in = new Scanner(openFile);
		String line;
		GroupSector gs = null;
		int id = 0;
		String[] parms;
		Color c;
		while (in.hasNextLine()) {

			line = in.nextLine();

			if (line.charAt(0) == 's') {

				if (gs != null) {
					master.addChild(gs);
					sectors.add(gs);
				}
				++id;
				gs = new GroupSector("" + id);

				parms = line.split("[ ]+");
				gs.localPosition.set(Float.parseFloat(parms[1]), Float.parseFloat(parms[3]), Float.parseFloat(parms[5]));
				gs.size.set(Float.parseFloat(parms[2]), Float.parseFloat(parms[4]), Float.parseFloat(parms[6]));

			} else if (line.charAt(0) == 'c') {
				parms = line.split("[ ]+");
				Direction index = Direction.values()[Integer.parseInt(parms[1])];
				c = new Color(Integer.parseInt(parms[2]), Integer.parseInt(parms[3]), Integer.parseInt(parms[4]));
				gs.shades.put(index, Material.makeWithColor(c));
			} else if (line.charAt(0) == 'a') {
				parms = line.split("[ ]+");
				int sector = Integer.parseInt(parms[1]);
				int intensity = Integer.parseInt(parms[2]);
				SceneNode node;

				if (intensity == 0) {
					node = new CameraNode("camera");
				} else {
					node = new LightNode("light");
					((LightNode) node).intensity = intensity / 255.0f;
				}

				sectors.get(sector).addChild(node);
				node.localPosition.set(0, 0, 0);
			}


		}

		((LevelEditor) app).world = new World(master);

	}

	/* (non-Javadoc)
	 * @see br.odb.gameapp.UserCommandLineAction#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "import-geo";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[]{new DestinationFilenameParameterDefinition()};
	}

}
