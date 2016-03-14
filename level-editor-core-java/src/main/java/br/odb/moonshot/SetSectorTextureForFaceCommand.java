package br.odb.moonshot;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;
import br.odb.libstrip.Material;
import br.odb.gameutils.Direction;
import br.odb.moonshot.parameterdefinitions.NodeIdParameterDefinition;
import br.odb.moonshot.parameterdefinitions.SourceFilenameParameterDefinition;

public class SetSectorTextureForFaceCommand extends UserCommandLineAction {

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public void run(ConsoleApplication app, String args) throws Exception {
		String[] arg;
		SceneNode sr;
		GroupSector gs;
		LevelEditor editor = (LevelEditor) app;
		arg = args.split( "[ ]+" );
		
		sr = editor.world.masterSector.getChild( arg[ 0 ] );
		
		if ( sr instanceof GroupSector ) {
			gs = (GroupSector)sr;
			Direction d = Direction.valueOf( arg[ 1 ] );
			gs.shades.put( d, Material.makeWithTexture( arg[ 2 ] ) );
		} else {
			app.getClient().alert( "Not a GroupSector" );
		}
	}

	@Override
	public String toString() {
		return "set-texture";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[]{ new NodeIdParameterDefinition(), new SourceFilenameParameterDefinition()};
	}
}
