/**
 * 
 */
package br.odb.moonshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.odb.gameapp.ApplicationClient;
import br.odb.libscene.World;
import br.odb.worldprocessing.WorldProcessor;

/**
 * @author monty
 * 
 */
public abstract class GeometryCompiler extends WorldProcessor {
	
	public GeometryCompiler(ApplicationClient client, World worldToProcess) {
		super(client, worldToProcess);
	}

	protected final List<WorldProcessor> processingPipeline = new ArrayList<>();
	
	@Override
	public void run() {

		for (WorldProcessor p : processingPipeline) {

			if (client != null) {

				client.printVerbose("Executing step:" + p + " at "
						+ Calendar.getInstance().getTime().toString());
			}

			p.run();
		}

		if (client != null) {

			client.printVerbose("finished running " + this + " at " + Calendar.getInstance().getTime().toString() );
		}
	}

	@Override
	public String toString() {

		return "Geometry Compilation";
	}
}
