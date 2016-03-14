/**
 * 
 */
package br.odb.moonshot;

import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.moonshot.parameterdefinitions.DestinationFilenameParameterDefinition;

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
		LevelEditor editor = (LevelEditor) app;
		OutputStream os = editor.openAsOutputStream(operand);
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

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[]{ new DestinationFilenameParameterDefinition()};
	}
}
