package br.odb.moonshot;

import java.io.OutputStream;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserMetaCommandLineAction;
import br.odb.libscene.builders.WorldLoader;
import br.odb.moonshot.parameterdefinitions.SourceFilenameParameterDefinition;

public class SaveCommand extends UserMetaCommandLineAction {

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public void run(ConsoleApplication app, String operand) throws Exception {
		LevelEditor editor = (LevelEditor) app;
		OutputStream os = editor.openAsOutputStream( operand );
		
		byte[] bytes = WorldLoader.toXML( editor.world ).getBytes();
		
		os.write( bytes );
		
		os.close();		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "save";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[]{ new SourceFilenameParameterDefinition()};
	}
}
