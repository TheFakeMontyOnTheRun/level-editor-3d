package br.odb.moonshot;

import java.io.*;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.gameapp.FileServerDelegate;
import br.odb.libscene.World;

public class LevelEditor extends ConsoleApplication implements FileServerDelegate {

	String filename;
	public World world;

	final UserCommandLineAction[] myCommands = new UserCommandLineAction[]{
			new QuitCommand(), new NewLight(), new ImportGEOCommand(), new NewCamera(), new SetCamera(), new SetLight(), new RebuildLinksCommand(), new ReParentSectorCommand(), new TesselateCommand(), new ResetLeafIdsCommand(), new SerializeWorldCommand(), new DeserializeWorldCommand(), new ClearLinksCommand(), new ClearMeshCommand(), new GenerateSubSectorsCommand(), new PickCommand(), new InfoCommand(), new SetDescriptionCommand(), new SetColorCommand(), new DeleteSectorCommand(), new LoadFileCommand(), new NewFileCommand(), new NewSectorCommand(), new MoveCommand(), new SaveCommand(), new ImportOBJCommand(), new GenerateSVGCommand(), new ResizeCommand(),
			new StatusCommand(), new HelpCommand(), new SetSectorTextureForFaceCommand(), new NewDecalNodeCommand(), new SnapTreeCommand(), new HelpWitCommand()};

	public LevelEditor() {
		init();
	}


	@Override
	public InputStream openAsInputStream(String filename) throws IOException {
		FileInputStream fis = new FileInputStream(filename);
		return fis;
	}

	@Override
	public InputStream openAsset(String s) throws IOException {
		return null;
	}

	@Override
	public InputStream openAsset(int i) throws IOException {
		return null;
	}

	@Override
	public OutputStream openAsOutputStream(String s) throws IOException {
		return new FileOutputStream(s);
	}


	@Override
	public void log(String tag, String message) {
		getClient().printVerbose(tag + ":" + message);
	}


	@Override
	public void onDataEntered(String entry) {

		if (entry == null || entry.length() == 0) {
			return;
		}

		super.onDataEntered(entry);

		try {
			runCmd(entry);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}


	public void showHelp() {

		StringBuilder buffer = new StringBuilder("");

		for (UserCommandLineAction cmd : myCommands) {
			buffer.append(cmd.getHelp());
			buffer.append("\n");
		}

		getClient().printNormal(buffer.toString());
	}

	@Override
	public ConsoleApplication init() {
		continueRunning = true;

		for (UserCommandLineAction cmd : myCommands) {

			this.registerCommand(cmd);
		}

		return super.init();
	}

	public void showHelpFor(String cmd ) {
		getClient().printNormal( getCommand( cmd ).getHelp() );
	}
}
