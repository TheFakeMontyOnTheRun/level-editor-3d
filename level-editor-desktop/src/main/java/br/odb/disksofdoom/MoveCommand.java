package br.odb.disksofdoom;

import br.odb.disksofdoom.DisksOfDoomMainApp.Disk;
import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.gameapp.UserMetaCommandLineAction;

public class MoveCommand extends UserCommandLineAction {

	
	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public int requiredOperands() {
		return 2;
	}

	@Override
	public void run(ConsoleApplication app, String operands) throws Exception {
            
            int p0;
            int p1;
            Disk d0;
            Disk d1;
            
            DisksOfDoomMainApp game = (DisksOfDoomMainApp) app;            
            String[] data = operands.split( "\\ ");
            
            p0 = Integer.parseInt( data[ 0 ] );
            p1 = Integer.parseInt( data[ 1 ] );

            d0 = game.pole[ p0 ].peek();
            d1 = game.pole[ p1 ].peek();
            
            if ( d0 == null || ( d1 != null && d1.size < d0.size ) ) {
                return;
            }
            
            ++game.move;
            
            game.getClient().alert("move " + game.move );
            
            game.pole[ p1 ].push( game.pole[ p0 ].pop() );
            
            if ( game.pole[ 0 ].isEmpty() && game.pole[ 1 ].isEmpty() ) {
                game.getClient().alert("you won!");
                game.doQuit();
            }
	}

	@Override
	public String toString() {
		return "move";
	}

}
