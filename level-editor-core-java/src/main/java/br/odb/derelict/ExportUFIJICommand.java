package br.odb.derelict;

import java.io.OutputStream;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserMetaCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;
import br.odb.libscene.SpaceRegion;
import br.odb.libscene.builders.WorldLoader;
import br.odb.utils.math.Vec3;

public class ExportUFIJICommand extends UserMetaCommandLineAction {
	


	public ExportUFIJICommand( ConsoleApplication app ) {
		super( app );

	}

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
	public void run(ConsoleApplication app, String operand) throws Exception {
		LevelEditor editor = (LevelEditor) app;
		SpaceRegion sr;
		int count = 1;
		Vec3 v;
		for ( SceneNode sn : editor.world.getAllRegionsAsList() ) {
			if ( sn instanceof GroupSector ) {
				sr = (( SpaceRegion ) sn );
				
				editor.getClient().printNormal( "plane = new Plane();" );
				v = new Vec3( sr.getAbsolutePosition() );
				editor.getClient().printNormal( "plane->p0.set( " + v.x + ", " + v.y + ", " + v.z + " );" );
				v.addTo( sr.size );
				editor.getClient().printNormal( "plane->p1.set( " + v.x + ", " + v.y + ", " + v.z + " );" );
				editor.getClient().printNormal( "plane->r = 255;" );
				editor.getClient().printNormal( "plane->g = 255;" );
				editor.getClient().printNormal( "plane->b = 255;" );
				editor.getClient().printNormal( "plane->id = " + count + ";" );
				editor.getClient().printNormal( "level.planes.push_back( plane );" );
				count++;
			}
		}
	}

	@Override
	public String toString() {
		return "export-ufiji";
	}

}
