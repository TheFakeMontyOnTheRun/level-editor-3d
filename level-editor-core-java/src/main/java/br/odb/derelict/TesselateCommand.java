package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.util.SceneTesselator;
import br.odb.libstrip.GeneralTriangleFactory;

public class TesselateCommand extends UserCommandLineAction {

	public TesselateCommand() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int requiredOperands() {
		// TODO Auto-generated method stub
		return 0;
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

}
