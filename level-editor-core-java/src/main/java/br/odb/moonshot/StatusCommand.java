package br.odb.moonshot;


import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserMetaCommandLineAction;
import br.odb.libscene.builders.WorldLoader;

public class StatusCommand extends UserMetaCommandLineAction {

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public void run(ConsoleApplication app, String arg1) throws Exception {
		LevelEditor editor = (LevelEditor) app;
		
		app.getClient().printNormal( WorldLoader.toXML( editor.world ) );
		
	}

	@Override
	public String toString() {
		return "status";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[0];
	}

}
