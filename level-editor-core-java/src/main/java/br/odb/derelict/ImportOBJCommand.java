package br.odb.derelict;

import src.old.ArrayList;
import src.old.Mesh;
import src.old.Sector;
import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.World;
import br.odb.libscene.builder.WorldLoader;

public class ImportOBJCommand extends UserCommandLineAction {

	@Override
	public String getHelp() {
		return "import obj";
	}

	@Override
	public int requiredOperands() {
		return 1;
	}

	public static void buildConvexHulls(final World world,
			final ArrayList<Mesh> mesh2) {
		Mesh mesh;
		Sector sector;

		for (int c = 0; c < mesh2.size(); ++c) {

			mesh = mesh2.get(c);

			if (mesh.faces.size() > 0) {

				sector = Sector.getConvexHull( World.snapLevel, mesh );

				if (!sector.isDegenerate()) {
					world.addSector(sector);
				}
			}
		}
	}
	
	@Override
	public void run(ConsoleApplication app, String operands) throws Exception {
		World world;
		world = WorldLoader.build( app.openAsInputStream( operands ) );
		((LevelEditor)app).world = world;
		
	}

	@Override
	public String toString() {
		return "importobj";
	}
}
