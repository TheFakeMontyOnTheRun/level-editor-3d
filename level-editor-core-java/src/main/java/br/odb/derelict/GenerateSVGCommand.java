package br.odb.derelict;

import java.io.OutputStream;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.SceneTesselator;

public class GenerateSVGCommand extends UserCommandLineAction {

	@Override
	public String getHelp() {
		return "generates SVG view for map";
	}

	@Override
	public int requiredOperands() {
		return 0;
	}

	@Override
	public void run(ConsoleApplication app, String arg1) throws Exception {
		OutputStream os;
		LevelEditor editor = (LevelEditor) app;
		os = editor.openAsOutputStream( "/Users/monty/view.svg" );
		os.write( SVGRenderer.renderXZ( SceneTesselator.generateQuadsForWorld( editor.world ) ).getBytes() );
	}

	@Override
	public String toString() {
		return "svg";
	}
}
