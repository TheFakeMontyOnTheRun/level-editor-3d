package br.odb.derelict;

import java.io.InputStream;
import java.util.List;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.liboldfart.WavefrontMaterialLoader;
import br.odb.liboldfart.WavefrontOBJLoader;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;
import br.odb.libstrip.Material;
import br.odb.libstrip.builders.GeneralTriangleFactory;

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
			WavefrontOBJLoader loader = new WavefrontOBJLoader( new GeneralTriangleFactory() );
			InputStream meshData	 = app.openAsInputStream( operands[ 1 ] );
			InputStream materialData = app.openAsInputStream( operands[ 2 ] );
			List< Material > materials = WavefrontMaterialLoader.parseMaterials( materialData );
			
			(( GroupSector) sr ).mesh.clear();
			(( GroupSector) sr ).mesh.faces.addAll( loader.loadMeshes( meshData, materials ).get( 0 ).faces );
		}
	}

	@Override
	public String toString() {
		return "set-mesh-sector";
	}
}
