package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.gameapp.UserMetaCommandLineAction;

public class NewFileCommand extends UserMetaCommandLineAction {

	public NewFileCommand(ConsoleApplication app) {
		super( app );
	}

	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public int requiredOperands() {
		return 1;
	}

	@Override
	public void run(ConsoleApplication app, String operand ) throws Exception {
		
	}

	@Override
	public String toString() {
		return "new-file";
	}

}
