package app;
/**
 * 
 */


import br.odb.gamelib.gameapp.ApplicationClient;
import java.io.IOException;


import br.odb.libscene.World;
import br.odb.portalizer.ConsoleGeometryCompiler;
import br.odb.utils.FileServerDelegate;


//-----------------------------------------------------------------------------
//=============================================================================
/**
 * @author Daniel "Monty" Monteiro
 *
 */
public class Portalizer implements ApplicationClient {
	
	//-----------------------------------------------------------------------------	
	/** 
	 * @param args
	 */
	public static void main(String[] args) {
		
		World.snapLevel = 2;
		ConsoleGeometryCompiler compiler = new ConsoleGeometryCompiler();
		Portalizer portalizer = new Portalizer();
		portalizer.setFileServer( compiler );
		compiler.setClient( new Portalizer() );
		compiler.setArgs( args );
		World world = new World();
		world.internalize( System.getProperty("user.home") + "/totautis_floor1.level", false, compiler, null );
		compiler.prepareFor( world );
		compiler.run();
		try {
			world.saveToDiskAsLevel( compiler.openAsOutputStream( System.getProperty("user.home") + "/totautis_floor1.opt.level" ) );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
        
        public void playMedia( String s1, String s2 ) {
            
        }

	private FileServerDelegate fsd;

	public void setFileServer( FileServerDelegate fsd ) {
		this.fsd = fsd;		
	}

	@Override
	public void printWarning(String msg) {
		System.out.println( "*WARNING: " + msg + " *");
		
	}

	@Override
	public void printError(String msg) {
		System.out.println( "**ERROR: " + msg + " **" );		
	}

	@Override
	public void printVerbose(String msg) {
		System.out.println( msg );		
	}

	@Override
	public int chooseOption(String question, String[] options) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String requestFilenameForSave() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String requestFilenameForOpen() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInput(String msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileServerDelegate getFileServer() {
		return fsd;
	}
	
//	
//	public static class PartitionerConsoleListener implements PartitionerListener {
//		
//		public void onStatusUpdate( int pass, float state ) {
//			System.out.println("pass " + pass + " status: " + state );
//		}
//		
//		public void debugMessage( String msg ) {
//			
//		}
//		
//		public void warnMessage( String msg ) {
//			
//		}
//		
//		public void errorMessage( String msg ) {
//			
//		}
//	}
//	
//	
//	public static final int NO_CHOSEN_FORMAT = 0;
//	public static final int OBJ = 1;
//	public static final int GEO = 2;
//	public static final int VRML = 3;
//	private static boolean shouldOptimizeAndCompile = true;
//	private static boolean shouldCheckConsistency = true;
//	private static boolean shouldGuessDoors = true;
//	private static Portalizer instance;
////-----------------------------------------------------------------------------	
//	/** 
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		
//		WorldPartitioner.listener = new PartitionerConsoleListener();
//		
//		int pos;
//		int chosenFormat;
//		boolean shouldDumpSVG;
//		ArrayList<String> params;
//		String outputPath;
//		String inputPath;
//		String svgPath = "";
//		World worldBefore = null;
//		World world = null;
//		outputPath = null;
//		inputPath = null;
//		shouldDumpSVG = false;
//		chosenFormat = NO_CHOSEN_FORMAT;
//		params = new ArrayList<String>();
//		ArrayList<Actor> people = new ArrayList<Actor>();
//		
//		for ( int c = 0; c < args.length; ++c ) {
//			params.add( args[ c ] );
//		}
//		
//		System.out.println( "t0:" + Calendar.getInstance().getTime().toString() );
//		
//		/// enable this block for debug
////		params.add( "-startsector" );
////		params.add( "22" );
////		params.add( "-inputgeo" );
////		params.add( "-snap" );
////		params.add( "100" );
////		params.add( "-input" );
////		params.add( "/Users/monty/test.level" );
////		params.add( "-output" );		
////		params.add( "/Users/monty/test.opt.level" );
////		params.add( "-dumpsvg" );
////		params.add( "/Users/monty/svg" );
//		//////
//
//		if ( params.size() == 0 ) {
//			printHelp();
//			System.exit( 0 );
//		}
//
//
//		///parsing -verbose
//		pos = params.indexOf( "-verbose" );
//		
//		if ( pos != -1 ) {
//			VerbosityLevel.level = VerbosityLevel.VERY;
//		}
//		
//		//parsing -dumpsvg
//		pos = params.indexOf( "-dumpsvg" );
//		
//		if ( pos != -1 ) {
//			shouldDumpSVG = true;
//			
//			svgPath = params.get( pos + 1 );
//		}
//		
//		//parsing -nocompile
//		pos = params.indexOf( "-nocompile" );
//		
//		if ( pos != -1 ) {
//			shouldOptimizeAndCompile = false;
//		}
//		
//		//parsing -inputobj
//		pos = params.indexOf( "-inputobj" );
//		
//		if ( pos != -1 ) {
//			chosenFormat = OBJ;
//		}
//		
//		//parsing -inputobj
//		pos = params.indexOf( "-hollow" );
//		
//		if ( pos != -1 ) {
//			WorldPartitioner.hollow = true;
//		} 		
//		
//		//parsing -inputgeo
//		pos = params.indexOf( "-inputgeo" );
//		
//		if ( pos != -1 ) {
//			chosenFormat = GEO;
//		}		
//
//		pos = params.indexOf( "-nocheck" );
//		
//		if ( pos != -1 ) {
//			shouldCheckConsistency = false;
//		}	
//		
//		pos = params.indexOf( "-noguessdoors" );
//		
//		if ( pos != -1 ) {
//			shouldGuessDoors = false;
//			System.out.println("ignoring doors...");
//		}	
//		
//		
//		//parsing -input
//		pos = params.indexOf( "-input" );
//		
//		if ( pos != -1 ) {
//			inputPath = params.get( pos + 1 );
//		}		
//		
//		//parsing -output
//		pos = params.indexOf( "-output" );
//		
//		if ( pos != -1 ) {
//			outputPath = params.get( pos + 1 );
//		}
//		
//		//parsing -output
//		pos = params.indexOf( "-snap" );
//		
//		if ( pos != -1 ) {
//			World.snapLevel = Integer.parseInt( params.get( pos + 1 ) );
//		}
//		
//		//parsing -output
//		pos = params.indexOf( "-startsector" );
//		
//		if ( pos != -1 ) {
//			WorldPartitioner.transientStartSector = Integer.parseInt( params.get( pos + 1 ) );
//		}		
//		
//
//		pos = params.indexOf( "-multipass" );
//		
//		if ( pos != -1 ) {
//			WorldPartitioner.multipass = true;
//		}
//		
//
//		//final error checks
//		
//		if ( inputPath == null ) {
//			System.out.println( "ERROR: no input file specified" );
//			System.exit( 0 );
//		}
//		
//		if ( chosenFormat == NO_CHOSEN_FORMAT ) {
//			System.out.println( "ERROR: no input file format specified" );
//			System.exit( 0 );
//		}
//
//		if ( outputPath == null ) {
//			System.out.println( "WARNING: no output file specified. Will just process and stop" );
//		}
//		
//		//finally start processing
//		ArrayList mesh = null;		
//		
//		FileFormatParser loader = null;		
//		world = new World();
//		
//		switch ( chosenFormat ) {
//		case OBJ:
//			loader = new WavefrontOBJLoader();
//			break;
//		case GEO:
//			loader = new GEOLoader();
//			break;
////very soon!			
////		case VRML:
////			DOMVRMLLoader loader = new DOMVRMLLoader();
////			break;
//		}
//
//		if ( loader == null ) {
//			System.out.println( "WARNING: no output file format specified. Stopping." );
//			System.exit( 0 );			
//		}
//		
//		try {
//		
//			if ( loader instanceof WavefrontOBJLoader )
//				((WavefrontOBJLoader)loader).currentPath = inputPath.substring( 0, inputPath.lastIndexOf('/') + 1 );
//			
//			loader.setFileServer( getInstance() );
//			loader.prepareForPath( inputPath );
//			loader.preBuffer( getInstance().openAsInputStream( inputPath ) );
//		} catch (FileNotFoundException e) {
//			System.out.println("input file not found");
//			System.exit( 0 );
//		}
//		loader.parseDocument( getInstance() );
//		
//		if ( loader instanceof WavefrontOBJLoader ) {
//			mesh = loader.getGeometry();
//			
//			if ( shouldDumpSVG )
//				try {
//					br.odb.libscene.Mesh.dumpToViewSVG( mesh, getInstance().openAsOutputStream( svgPath + "-geometry.svg" ) );
//				} catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}		
//			
//			//adds master sector.
//			WorldPartitioner.buildConvexHulls( world, mesh );
//		}
//		
//		
//		if ( loader instanceof GEOLoader ) {
//			world = ( ( GEOLoader ) loader ).getWorld();			
//		}
//		
//
//		if ( shouldOptimizeAndCompile ) {
//			
//			//so is the dance of optimizations:
//			//first, create a empty world.
//			//then, copy the current sectors into it, as masters
//			//optimize the current world
//			//and then, add back the reserved masters into the optimized world.
//			worldBefore = new World();
//			WorldPartitioner.snapConnections( world );
//			worldBefore.addMasters( world );
//			world = WorldPartitioner.optimize( world );
//			
//			//ja adiciona setor raiz...
//			world.addMastersBefore( worldBefore );
//			
//			WorldPartitioner.guessLinks( world, shouldGuessDoors  );
//			
//			if ( WorldPartitioner.hollow ) {
////				WorldPartitioner.deleteHollowSectors( world );
////				WorldPartitioner.guessLinks( world, shouldGuessDoors  );				
//			}
//				
//			
//			//Engine already does that on preload stage
////			if ( shouldGuessDoors )
////				WorldPartitioner.guessDoors( world );
//		}
//		
////		if ( shouldDumpSVG )
////			WorldPartitioner.dumpTopViewToSVG( world, svgPath + "-after.svg");
//		
//		if ( outputPath != null ) {
//			
//			if ( WorldPartitioner.transientStartSector != -1 ) {
//				Actor actor = new Actor();
//				actor.setCurrentSector( WorldPartitioner.transientStartSector );
//				people.add( actor );
//			}
//			
//			try {
//				world.saveToDiskAsLevel( getInstance().openAsOutputStream( outputPath ), people, false );
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				System.out.println( "could not save file!");
//			}
//		}
//		
//		if ( shouldCheckConsistency && !world.checkConsistency() ) {
//			System.out.println( "Consistency error. Please try again" );			
//			System.exit( 0 );
//		}		
//		System.out.println( "t1:" + Calendar.getInstance().getTime().toString() );
//	}
////-----------------------------------------------------------------------------
//
//	private static Portalizer getInstance() {
//		
//		if ( instance == null )
//			instance = new Portalizer();
//		
//		return instance;
//	}
//
//	/**
//	 * 
//	 */
//	private static void printHelp() {
//		System.out.println( "Portalizer - make portals out of coinciding hollow spaces. Copyright ODB ( main author: Daniel Monteiro )" );
//		System.out.println( "Special thanks for all the folks at Nano Studio ( http://www.nanostudio.com.br )" );
//		System.out.println();
//		System.out.println( "USAGE: <JVM execution command> Portaller<.extension?> [ -verbose ] < -inputobj | -inputgeo > [ -dumpsvg ] -input <file input> -output <file output>" );
//		System.out.println();
//		System.out.println( "-verbose shows all the silly messages I use for debugging" );
//		System.out.println( "-inputobj indicates the input file is a Wavefront OBJ. The rotation on X axis is already taken into account" );
//		System.out.println( "-inputgeo indicates the input file is a GEO file, the same as the output file." );
//		System.out.println( "-snap enables bouding boxes axis snapping." );		
//		System.out.println( "-dumpsvg dumps 3 SVG files: one for the loaded geometry, another for the created bouding boxes and another, after the partitioning, with the final sectors." );
//		System.out.println( "-input path-to-file" );
//		System.out.println( "-output path-to-file" );
//	}
//
//	@Override
//	public InputStream openAsInputStream(String filename) throws FileNotFoundException {
//		
//		return new FileInputStream( filename );
//	}
//
//	@Override
//	public OutputStream openAsOutputStream(String filename) throws FileNotFoundException {
//		return new FileOutputStream( filename );
//	}
//
//	@Override
//	public InputStream openAsset(String filename) throws FileNotFoundException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public InputStream openAsset(int resId) throws FileNotFoundException {
//		// TODO Auto-generated method stub
//		return null;
//	}

    @Override
    public void setClientId(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void printNormal(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void alert(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendQuit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isConnected() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
//=============================================================================