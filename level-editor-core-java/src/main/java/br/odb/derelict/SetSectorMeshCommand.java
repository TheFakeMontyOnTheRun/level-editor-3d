package br.odb.derelict;

import java.io.InputStream;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.liboldfart.WavefrontOBJLoader;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;

public class SetSectorMeshCommand extends UserCommandLineAction {

	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public int requiredOperands() {
		return 2;
	}

	@Override
	public void run(ConsoleApplication app, String operand) throws Exception {
		LevelEditor editor = (LevelEditor) app;
		String [] operands = operand.split( "[ ]+" );
		SceneNode sr = editor.world.masterSector.getChild( operands[ 0 ] );

		if ( sr instanceof GroupSector ) {
			
			WavefrontOBJLoader loader = new WavefrontOBJLoader();
			InputStream meshData	 = app.openAsInputStream( operands[ 1 ] );
			InputStream materialData = app.openAsInputStream( operands[ 2 ] );
			
			(( GroupSector) sr ).mesh.clear();
			(( GroupSector) sr ).mesh.addFacesFrom( loader.loadMeshes( "sectorMesh", meshData, materialData ).get( 0 ) );
		}
	}

	@Override
	public String toString() {
		return "set-mesh-sector";
	}
}
