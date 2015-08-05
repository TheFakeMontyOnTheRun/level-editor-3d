package br.odb.derelict;

import java.util.ArrayList;
import java.util.List;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.MeshNode;
import br.odb.libscene.SceneNode;

public class ClearMeshCommand extends UserCommandLineAction {

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int requiredOperands() {
		return 0;
	}

	@Override
	public void run(ConsoleApplication app, String arg1) throws Exception {
		LevelEditor editor = (LevelEditor) app; 
		
		clearMeshesOn( editor.world.masterSector );
	}

	private void clearMeshesOn(GroupSector sector ) {
		
		List< MeshNode > nodes = new ArrayList<MeshNode>();
		
		for ( SceneNode sn : sector.getSons() ) {
			if ( sn instanceof MeshNode ) {
				nodes.add( (MeshNode) sn );
			} else if ( sn instanceof GroupSector ) {
				clearMeshesOn( (GroupSector) sn );
			}
		}
		
		for ( MeshNode node : nodes ) {
			sector.removeChild( node );
		}
	}

	@Override
	public String toString() {
		return "clear-meshes";
	}

}
