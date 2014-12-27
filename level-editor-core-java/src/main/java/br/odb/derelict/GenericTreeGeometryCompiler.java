package br.odb.derelict;

import br.odb.libscene.World;
import br.odb.worldprocessing.DegenerateSectorCuller;
import br.odb.worldprocessing.GeometryCompiler;
import br.odb.worldprocessing.RemoveCoincidantSectors;
import br.odb.worldprocessing.RemoveLeafSectors;
import br.odb.worldprocessing.WorldLocalPartitioner;

public class GenericTreeGeometryCompiler extends GeometryCompiler {

	public GenericTreeGeometryCompiler(World world) {
		super();
		
		this.world = world;
		
		this.processingPipeline.add( new RemoveLeafSectors() );
		this.processingPipeline.add( new DegenerateSectorCuller() );
		this.processingPipeline.add( new WorldLocalPartitioner() );
		this.processingPipeline.add( new RemoveCoincidantSectors() );
		this.processingPipeline.add( new DegenerateSectorCuller() );
	}
	
	@Override
	public void log(String arg0, String arg1) {
		getClient().printVerbose( arg0 + ": " + arg1 );

	}

	@Override
	protected void doQuit() {

	}
}
