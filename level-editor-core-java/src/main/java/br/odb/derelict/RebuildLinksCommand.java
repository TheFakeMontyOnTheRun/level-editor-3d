package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;

public class RebuildLinksCommand extends UserCommandLineAction {

	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public int requiredOperands() {
		return 0;
	}

	@Override
	public void run(ConsoleApplication app, String arg1) throws Exception {
		LevelEditor editor = (LevelEditor) app;
		editor.world.checkForHardLinks();
	}

	@Override
	public String toString() {
		return "rebuild-links";
	}
}
