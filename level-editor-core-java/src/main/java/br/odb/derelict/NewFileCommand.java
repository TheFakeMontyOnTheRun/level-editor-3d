package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserMetaCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.World;

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
		return 0;
	}

	@Override
	public void run(ConsoleApplication app, String operand ) throws Exception {
		LevelEditor editor = (LevelEditor) app;
		editor.world = new World( new GroupSector( "root" ) );
	}

	@Override
	public String toString() {
		return "new-file";
	}

}
