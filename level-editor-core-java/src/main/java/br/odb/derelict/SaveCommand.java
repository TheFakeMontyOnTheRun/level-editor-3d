package br.odb.derelict;

import java.io.OutputStream;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserMetaCommandLineAction;
import br.odb.libscene.builders.WorldLoader;

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
		LevelEditor editor = (LevelEditor) app;
		OutputStream os = app.openAsOutputStream( operand );
		
		byte[] bytes = WorldLoader.toXML( editor.world ).getBytes();
		
		os.write( bytes );
		
		os.close();		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "save";
	}

}
