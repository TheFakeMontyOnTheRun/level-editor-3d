package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserMetaCommandLineAction;

public class SaveCommand extends UserMetaCommandLineAction {
	


	public SaveCommand( ConsoleApplication app ) {
		super( app );

	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int requiredOperands() {
		// TODO Auto-generated method stub
		return 1;
	}
	;

	@Override
	public void run(ConsoleApplication app, String operand) throws Exception {
	
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "save";
	}

}
