package br.odb.derelict;


import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.gameapp.UserMetaCommandLineAction;

public class StatusCommand extends UserMetaCommandLineAction {

	public StatusCommand( ConsoleApplication app ) {
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
		return 0;
	}

	@Override
	public void run(ConsoleApplication app, String arg1) throws Exception {
	}
        

	@Override
	public String toString() {
		return "status";
	}

}
