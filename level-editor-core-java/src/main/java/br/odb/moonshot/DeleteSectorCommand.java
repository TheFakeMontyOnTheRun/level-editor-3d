package br.odb.moonshot;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;
import br.odb.moonshot.parameterdefinitions.NodeIdParameterDefinition;

public class DeleteSectorCommand extends UserCommandLineAction {

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public void run(ConsoleApplication app, String operand) throws Exception {
		LevelEditor editor = (LevelEditor) app;
		GroupSector masterSector = editor.world.masterSector;
		SceneNode subject = masterSector.getChild( operand );
		
		if ( subject.parent  instanceof GroupSector ) {
			( ( GroupSector ) subject.parent ).removeChild( subject );
		}
		
		subject.parent = null;
	}

	@Override
	public String toString() {
		return "delete-sector";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[]{new NodeIdParameterDefinition()};
	}
}
