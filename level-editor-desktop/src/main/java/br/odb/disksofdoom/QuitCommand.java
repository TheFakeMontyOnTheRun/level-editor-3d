package br.odb.disksofdoom;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.gameapp.UserMetaCommandLineAction;

public class QuitCommand extends UserMetaCommandLineAction {

	public QuitCommand( ConsoleApplication app) {
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
	public void run(ConsoleApplication arg0, String arg1) throws Exception {
		((DisksOfDoomMainApp)getApplication()).doQuit();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "quit";
	}

}
