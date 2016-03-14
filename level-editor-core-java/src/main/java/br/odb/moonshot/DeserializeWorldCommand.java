/**
 * 
 */
package br.odb.moonshot;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.libscene.World;
import br.odb.moonshot.parameterdefinitions.DestinationFilenameParameterDefinition;

/**
 * @author monty
 *
 */
public class DeserializeWorldCommand extends UserCommandLineAction {

	/**
	 * 
	 */
	public DeserializeWorldCommand() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDescription() {
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.odb.gameapp.UserCommandLineAction#run(br.odb.gameapp.ConsoleApplication
	 * , java.lang.String)
	 */
	@Override
	public void run(ConsoleApplication app, String operand) throws Exception {
		World world;
		LevelEditor editor = (LevelEditor) app;
		InputStream file = editor.openAsInputStream(operand);
		InputStream buffer = new BufferedInputStream(file);
		ObjectInput input = new ObjectInputStream(buffer);
		world = (World) input.readObject();

		((LevelEditor) app).world = world;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.odb.gameapp.UserCommandLineAction#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "deserialize";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[]{ new DestinationFilenameParameterDefinition()};
	}
}
