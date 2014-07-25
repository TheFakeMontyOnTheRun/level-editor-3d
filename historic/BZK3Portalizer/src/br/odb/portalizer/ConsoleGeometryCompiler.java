/**
 * 
 */
package br.odb.portalizer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import br.odb.gamelib.gameapp.ConsoleApplication;
import br.odb.gamelib.gameapp.UserCommandLineAction;
import br.odb.libscene.World;
import br.odb.worldprocessing.AddFirstMasterSector;
import br.odb.worldprocessing.DegenerateSectorCuller;
import br.odb.worldprocessing.GeometryCompiler;
import br.odb.worldprocessing.RemoveCoincidantSectors;
import br.odb.worldprocessing.RemoveFirstMasterSector;
import br.odb.worldprocessing.RemoveLeafSectors;
import br.odb.worldprocessing.SectorLinker;
import br.odb.worldprocessing.SectorSnapper;
import br.odb.worldprocessing.SequencialSectorIdSetter;
import br.odb.worldprocessing.StartSectorLocator;
import br.odb.worldprocessing.WorldLocalPartitioner;

/**
 * @author monty
 *
 */
public class ConsoleGeometryCompiler extends GeometryCompiler {
	

	/**
	 * 
	 */
	public ConsoleGeometryCompiler() {
	
	}

	/* (non-Javadoc)
	 * @see br.odb.utils.FileServerDelegate#openAsInputStream(java.lang.String)
	 */
	@Override
	public InputStream openAsInputStream(String filename) throws IOException {

		return new FileInputStream( filename );
	}

	/* (non-Javadoc)
	 * @see br.odb.utils.FileServerDelegate#openAsset(java.lang.String)
	 */
	@Override
	public InputStream openAsset(String filename) throws IOException {

		return new FileInputStream( filename );
	}

	/* (non-Javadoc)
	 * @see br.odb.utils.FileServerDelegate#openAsset(int)
	 */
	@Override
	public InputStream openAsset(int resId) throws IOException {

		return null;
	}

	/* (non-Javadoc)
	 * @see br.odb.utils.FileServerDelegate#openAsOutputStream(java.lang.String)
	 */
	@Override
	public OutputStream openAsOutputStream(String filename) throws IOException {

		return new FileOutputStream( filename );
	}

	/* (non-Javadoc)
	 * @see br.odb.ConsoleApplication#printPreamble()
	 */
	public void printPreambleXX() {

		if ( client != null ) {
			client.printVerbose( "Portalizer - make portals out of coinciding hollow spaces. Copyright ODB ( main author: Daniel Monteiro )" );
			client.printVerbose( "Special thanks for all the folks at Nano Studio ( http://www.nanostudio.com.br )" );
			client.printVerbose("");
			client.printVerbose( "USAGE: <JVM execution command> Portaller<.extension?> [ -verbose ] < -inputobj | -inputgeo > [ -dumpsvg ] -input <file input> -output <file output>" );
			client.printVerbose("");
			client.printVerbose( "-verbose shows all the silly messages I use for debugging" );
			client.printVerbose( "-inputobj indicates the input file is a Wavefront OBJ. The rotation on X axis is already taken into account" );
			client.printVerbose( "-inputgeo indicates the input file is a GEO file, the same as the output file." );
			client.printVerbose( "-snap enables bouding boxes axis snapping." );		
			client.printVerbose( "-input path-to-file" );
			client.printVerbose( "-output path-to-file" );
		}
	}

	/* (non-Javadoc)
	 * @see br.odb.ConsoleApplication#serArgs(java.lang.String[])
	 */
        public void setArgs(String[] args) {
		
		HashMap< String, String > configs = new UnixStyleParameterParser().parse( args );
		buildProcessPipelineFromConfigs( configs );
	}

	private void buildProcessPipelineFromConfigs(HashMap<String, String> configs) {
		processingPipeline.add( new RemoveLeafSectors() );
		processingPipeline.add( new SequencialSectorIdSetter() );
		processingPipeline.add( new SectorSnapper() );
		processingPipeline.add( new RemoveFirstMasterSector() );
		processingPipeline.add( new DegenerateSectorCuller() );
		processingPipeline.add( new WorldLocalPartitioner() );
		processingPipeline.add( new DegenerateSectorCuller() );
		processingPipeline.add( new RemoveCoincidantSectors() );
		processingPipeline.add( new AddFirstMasterSector() );
		processingPipeline.add( new SequencialSectorIdSetter() );
		processingPipeline.add( new SectorLinker() );
		processingPipeline.add( new StartSectorLocator( 1 ) );
	}

    @Override
    public UserCommandLineAction[] getAvailableCommands() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void log(String tag, String string) {
        System.out.println( tag + "-" + string ); 
    }

    @Override
    protected void onQuit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
