/**
 * 
 */
package br.odb.derelict;

import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;

/**
 * @author monty
 *
 */
public class SerializeWorldCommand extends UserCommandLineAction {

	/**
	 * 
	 */
	public SerializeWorldCommand() {
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
		LevelEditor editor = (LevelEditor) app;
		OutputStream os = app.openAsOutputStream(operand);
		ObjectOutput output = new ObjectOutputStream(os);
		output.writeObject( editor.world );

		os.close();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.odb.gameapp.UserCommandLineAction#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "serialize";
	}

}
