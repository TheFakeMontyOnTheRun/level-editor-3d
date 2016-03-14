package br.odb.moonshot;

import br.odb.gameapp.ApplicationClient;
import br.odb.libscene.World;
import br.odb.worldprocessing.DegenerateSectorCuller;
import br.odb.worldprocessing.GroupSectorSnapper;
import br.odb.worldprocessing.RemoveCoincidantSectors;
import br.odb.worldprocessing.RemoveLeafSectors;
import br.odb.worldprocessing.SectorLinker;
import br.odb.worldprocessing.WorldLocalPartitioner;

public class GenericTreeGeometryCompiler extends GeometryCompiler {

	public GenericTreeGeometryCompiler(ApplicationClient client,
			World worldToProcess) {
		super(client, worldToProcess);
		this.processingPipeline.add( new GroupSectorSnapper(client, worldToProcess) );
		this.processingPipeline.add( new RemoveLeafSectors(client, worldToProcess) );
		this.processingPipeline.add( new DegenerateSectorCuller(client, worldToProcess) );
		this.processingPipeline.add( new WorldLocalPartitioner(client, worldToProcess) );
		this.processingPipeline.add( new RemoveCoincidantSectors(client, worldToProcess) );
		this.processingPipeline.add( new DegenerateSectorCuller(client, worldToProcess));
		this.processingPipeline.add( new SectorLinker(client, worldToProcess));
	}
}
