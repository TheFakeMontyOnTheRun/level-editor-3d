/**
 * 
 */
package br.odb.derelict;

import java.io.DataInputStream;
import java.io.InputStream;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.GroupSector;
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
		DataInputStream dis = new DataInputStream( openFile );
		String line;
		GroupSector gs = null;
		GroupSector wall = null;
		int id = 0;
		String[] parms;
		Color c;
		while( ( line = dis.readLine() ) != null ) {
			
			if ( line.charAt( 0 ) == 's' ) {

				if ( gs != null ) {
					if ( gs.material == null ) {
					//	gs.material = new Material( "mat" + id, new Color( 128, 128, 128 ), null, null, null );
					}
					master.addChild( gs );
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
				wall = extractWallSector( gs, index, "wall_" + index + "_" + id );
				wall.material = new Material( "mat" + id, c, null, null, null );
				master.addChild( wall );
			}
		}
		
		((LevelEditor)app).world = new World( master );

	}

	private GroupSector extractWallSector(GroupSector original, Direction index,
			String id) {
		
		GroupSector gs = new GroupSector( id );

		switch( index ) {
		case N:
			gs.localPosition.set( original.localPosition );
			gs.size.set(original.size.x, original.size.y, 0.1f );
			break;
		case S:
			gs.localPosition.set( original.localPosition );
			gs.localPosition.z += original.size.z;
			gs.size.set(original.size.x, original.size.y, 0.1f );			
			break;
		case W:
			gs.localPosition.set( original.localPosition );
			gs.size.set( 0.1f, original.size.y, original.size.z );			
			break;
		case E:
			gs.localPosition.set( original.localPosition );
			gs.localPosition.x += original.size.x;			
			gs.size.set( 0.1f, original.size.y, original.size.z );			
			break;
		case FLOOR:
			gs.localPosition.set( original.localPosition );
			gs.size.set(original.size.x, 0.1f, original.size.z );			
			break;
		case CEILING:
			gs.localPosition.set( original.localPosition );
			gs.localPosition.y += original.size.y;			
			gs.size.set(original.size.x, 0.1f, original.size.z );			
			break;
		}
		
		return gs;
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
