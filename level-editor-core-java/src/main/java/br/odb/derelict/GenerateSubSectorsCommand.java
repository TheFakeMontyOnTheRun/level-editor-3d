/**
 * 
 */
package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.World;
import br.odb.worldprocessing.GeometryCompiler;

/**
 * @author monty
 *
 */
public class GenerateSubSectorsCommand extends UserCommandLineAction {

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
	public void run(ConsoleApplication app, String arg1) throws Exception {
		LevelEditor editor = (LevelEditor) app;
		World world = editor.world;
		GeometryCompiler compiler = new GenericTreeGeometryCompiler( world );
		compiler.setClient( editor.getClient() );
		compiler.run();
	}

	/* (non-Javadoc)
	 * @see br.odb.gameapp.UserCommandLineAction#toString()
	 */
	@Override
	public String toString() {
		return "compile";
	}
}
