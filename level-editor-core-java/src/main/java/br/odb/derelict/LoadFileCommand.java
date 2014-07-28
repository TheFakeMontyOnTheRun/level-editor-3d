package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.gameapp.UserMetaCommandLineAction;
import br.odb.libscene.World;

public class LoadFileCommand extends UserCommandLineAction {

	
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
			World world;
			world= new World();
            
            world.internalize( operand, operand, app, meshFactory );
            
            ((LevelEditor)app).world = world;
        }

	@Override
	public String toString() {
		return "load";
	}

}
