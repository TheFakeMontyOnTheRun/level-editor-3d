package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;

public class NewSectorCommand extends UserCommandLineAction {

	public NewSectorCommand(ConsoleApplication app) {
		super( );
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
		return "new-sector";
	}

}
