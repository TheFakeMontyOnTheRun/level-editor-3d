package br.odb.disksofdoom;


import br.odb.disksofdoom.DisksOfDoomMainApp.Disk;
import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.gameapp.UserMetaCommandLineAction;
import java.util.LinkedList;

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
            
            DisksOfDoomMainApp game = (DisksOfDoomMainApp) app;
            
            for ( int c = 0; c < game.pole.length; ++c ) {
                app.getClient().printNormal( c + ">" + poleToString( game.pole[ c ] ) );
            }           
	}
        
        String poleToString( LinkedList<Disk> pole ) {
            
            boolean first;
            first = true;
            String line = "";
            
            for ( Disk d : pole ) {
                
                if ( !first ) {
                    line = "-" + line;
                }
                
                line = d.size + line;
                
                first = false;
            }
            
            return line;
        }

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "status";
	}

}
