package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SpaceRegion;

public class DeleteSectorCommand extends UserCommandLineAction {

	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public int requiredOperands() {
		return 1;
	}

	@Override
	public void run(ConsoleApplication app, String operand) throws Exception {
		LevelEditor editor = (LevelEditor) app;
		GroupSector masterSector = editor.world.masterSector;
		SpaceRegion subject = masterSector.getChild( operand );
		
		if ( subject.parent  instanceof GroupSector ) {
			( ( GroupSector ) subject.parent ).removeChild( subject );
		}
		
		subject.parent = null;
	}

	@Override
	public String toString() {
		return "delete-sector";
	}
}
