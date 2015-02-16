package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.GroupSector;

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
		
		LevelEditor editor = (LevelEditor) app;
		if ( operand.trim().length() > 0 ) {
			editor.world.masterSector.addChild( new GroupSector( operand.trim() ) );	
		} else {
			app.getClient().alert( "Provide a id" );
		}
		
	}

	@Override
	public String toString() {
		return "new-sector";
	}

}
