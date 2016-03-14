package br.odb.moonshot;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.moonshot.parameterdefinitions.VacantNodeIdParameterDefinition;

public class NewSectorCommand extends UserCommandLineAction {

	@Override
	public String getDescription() {
		return "";
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

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[]{ new VacantNodeIdParameterDefinition()};
	}
}
