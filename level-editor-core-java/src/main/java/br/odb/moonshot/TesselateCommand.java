package br.odb.moonshot;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.CommandParameterDefinition;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.libscene.util.SceneTesselator;
import br.odb.libstrip.builders.GeneralTriangleFactory;

public class TesselateCommand extends UserCommandLineAction {

	public TesselateCommand() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public void run(ConsoleApplication app, String arg1) throws Exception {
		LevelEditor editor = (LevelEditor) app; 
		new SceneTesselator( new GeneralTriangleFactory() ).generateSubSectorQuadsForWorld( editor.world );
	}

	@Override
	public String toString() {
		return "tesselate";
	}

	@Override
	public CommandParameterDefinition[] requiredOperands() {
		return new CommandParameterDefinition[0];
	}

}
