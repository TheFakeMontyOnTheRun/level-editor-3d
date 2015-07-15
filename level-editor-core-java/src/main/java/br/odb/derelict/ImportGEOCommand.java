/**
 * 
 */
package br.odb.derelict;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.CameraNode;
import br.odb.libscene.GroupSector;
import br.odb.libscene.LightNode;
import br.odb.libscene.SceneNode;
import br.odb.libscene.Sector;
import br.odb.libscene.World;
import br.odb.libstrip.Material;
import br.odb.utils.Color;
import br.odb.utils.Direction;

/**
 * @author monty
 *
 */
public class ImportGEOCommand extends UserCommandLineAction {

	/**
	 * 
	 */
	public ImportGEOCommand() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see br.odb.gameapp.UserCommandLineAction#getHelp()
	 */
	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see br.odb.gameapp.UserCommandLineAction#requiredOperands()
	 */
	@Override
	public int requiredOperands() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see br.odb.gameapp.UserCommandLineAction#run(br.odb.gameapp.ConsoleApplication, java.lang.String)
	 */
	@Override
	public void run(ConsoleApplication app, String operand) throws Exception {
		GroupSector master = new GroupSector( "_root" );
		InputStream openFile = app.openAsInputStream( operand );
		List< GroupSector > sectors = new ArrayList<>();
		DataInputStream dis = new DataInputStream( openFile );
		String line;
		GroupSector gs = null;
		int id = 0;
		String[] parms;
		Color c;
		while( ( line = dis.readLine() ) != null ) {
			
			if ( line.charAt( 0 ) == 's' ) {

				if ( gs != null ) {
					master.addChild( gs );
					sectors.add( gs );
				}
				++id;
				gs = new GroupSector( "" + id );
				
				parms = line.split( "[ ]+" );
				gs.localPosition.set( Float.parseFloat( parms[ 1 ]), Float.parseFloat( parms[ 3 ] ), Float.parseFloat( parms[ 5 ] ) );
				gs.size.set( Float.parseFloat( parms[ 2 ]), Float.parseFloat( parms[ 4 ] ), Float.parseFloat( parms[ 6 ] ) );
				
			} else if ( line.charAt( 0 ) == 'c' ) {
				parms = line.split( "[ ]+" );
				Direction index = Direction.values()[ Integer.parseInt( parms[ 1 ] ) ];
				c = new Color( Integer.parseInt( parms[ 2 ]), Integer.parseInt( parms[ 3 ] ), Integer.parseInt( parms[ 4 ] ) );
				gs.shades.put( index, new Material( c.getHTMLColor(), c, null, null ) );
			} else if ( line.charAt( 0 ) == 'a' ) {
				parms = line.split( "[ ]+" );
				int sector = Integer.parseInt( parms[ 1 ] );
				int intensity = Integer.parseInt( parms[ 2 ] );
				SceneNode node;
				
				if( intensity == 0 ) {
					node = new CameraNode( "camera" );
				} else {
					node = new LightNode( "light" );
					((LightNode )node).intensity = intensity / 255.0f; 
				}
				
				sectors.get( sector ).addChild( node );
				node.localPosition.set( 0, 0, 0 );
			}

			
		}
		
		((LevelEditor)app).world = new World( master );

	}

	/* (non-Javadoc)
	 * @see br.odb.gameapp.UserCommandLineAction#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "import-geo";
	}

}
