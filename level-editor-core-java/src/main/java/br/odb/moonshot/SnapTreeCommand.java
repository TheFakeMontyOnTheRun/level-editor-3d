package br.odb.moonshot;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;
import br.odb.libscene.SpaceRegion;

public class SnapTreeCommand extends UserCommandLineAction {

	LevelEditor editor;

	@Override
	public String getDescription() {
		return "";
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
		
		target.localPosition.x = (float) Math.round( target.localPosition.x );
		target.localPosition.y = (float) Math.round( target.localPosition.y );
		target.localPosition.z = (float) Math.round( target.localPosition.z );
		
		editor.getClient().printVerbose( "snapping " + target.id );
		
		if ( target instanceof SpaceRegion ) {
			snapRegion( (SpaceRegion) target );
		}
	}

	
	private void snapRegion( SpaceRegion region ) {
		
		region.size.x = (float) Math.round( region.size.x );
		region.size.x = (float) Math.round( region.size.y );
		region.size.x = (float) Math.round( region.size.z );
		
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

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[0];
	}
}
