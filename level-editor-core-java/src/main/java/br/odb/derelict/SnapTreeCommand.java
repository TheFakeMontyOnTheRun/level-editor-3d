package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;
import br.odb.libscene.SpaceRegion;

public class SnapTreeCommand extends UserCommandLineAction {

	LevelEditor editor;
	
	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public int requiredOperands() {
		return 0;
	}

	@Override
	public void run(ConsoleApplication app, String arg1) throws Exception {
		
		editor = (LevelEditor) app; 
		SceneNode target;
		
		if ( arg1 == null || arg1.length() == 0 ) {
			target = editor.world.masterSector; 
		} else {		
			target = editor.world.masterSector.getChild( arg1 );
		}
		
		snap( target );
	}
	
	private void snap( SceneNode target ) {
		
		target.localPosition.x = (float) Math.floor( target.localPosition.x );
		target.localPosition.y = (float) Math.floor( target.localPosition.y );
		target.localPosition.z = (float) Math.floor( target.localPosition.z );
		
		editor.getClient().printVerbose( "snapping " + target.id );
		
		if ( target instanceof SpaceRegion ) {
			snapRegion( (SpaceRegion) target );
		}
	}

	
	private void snapRegion( SpaceRegion region ) {
		
		region.size.x = (float) Math.floor( region.size.x );
		region.size.x = (float) Math.floor( region.size.y );
		region.size.x = (float) Math.floor( region.size.z );
		
		if ( region instanceof GroupSector ) {
			
			GroupSector gs = (GroupSector) region;
			
			for ( SceneNode sn : gs.getSons() ) {
				snap( sn );
			}
		}	
	}

	@Override
	public String toString() {
		return "snap-tree";
	}
}
