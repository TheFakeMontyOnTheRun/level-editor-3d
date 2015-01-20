/**
 * 
 */
package br.odb.derelict;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.World;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.odb.gameapp.UserCommandLineAction#getHelp()
	 */
	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.odb.gameapp.UserCommandLineAction#requiredOperands()
	 */
	@Override
	public int requiredOperands() {
		// TODO Auto-generated method stub
		return 1;
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

		InputStream file = app.openAsInputStream(operand);
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

}
