package br.odb.disksofdoom;

import br.odb.disksofdoom.DisksOfDoomMainApp.Disk;
import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;

public class NewGameCommand extends UserCommandLineAction {

	public NewGameCommand(DisksOfDoomMainApp app) {
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
		DisksOfDoomMainApp game = (DisksOfDoomMainApp) app;
		game.pole[ 0 ].clear();
		game.pole[ 1 ].clear();
		game.pole[ 2 ].clear();
		 
		int disks = Integer.parseInt( operand );
		
		if ( disks > 0 && disks <= 10 ) {
			for ( ; disks > 0; --disks) {
				game.pole[ 0 ].push( new DisksOfDoomMainApp.Disk( disks ) );
			}
		} else {
			app.getClient().alert( "use with a number between 1 and 10" );
		}
	}

	@Override
	public String toString() {
		return "new-game";
	}

}
