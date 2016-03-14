/**
 * 
 */
package br.odb.moonshot;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.libscene.World;

/**
 * @author monty
 *
 */
public class GenerateSubSectorsCommand extends UserCommandLineAction {

	@Override
	public String getDescription() {
		return "";
	}

	/* (non-Javadoc)
	 * @see br.odb.gameapp.UserCommandLineAction#run(br.odb.gameapp.ConsoleApplication, java.lang.String)
	 */
	@Override
	public void run(ConsoleApplication app, String arg1) throws Exception {
		LevelEditor editor = (LevelEditor) app;
		World world = editor.world;
		GeometryCompiler compiler = new GenericTreeGeometryCompiler( editor.getClient(), world );
		compiler.run();
	}

	/* (non-Javadoc)
	 * @see br.odb.gameapp.UserCommandLineAction#toString()
	 */
	@Override
	public String toString() {
		return "compile";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[0];
	}
}
