package br.odb.derelict;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.libscene.World;

public class LevelEditor extends ConsoleApplication {

	String filename;
	World world;
	
	
    public LevelEditor() {
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

    @Override
    protected void doQuit() {
        this.continueRunning = false;
    }

    @Override
    public ConsoleApplication init() {
        continueRunning = true;

        for (UserCommandLineAction cmd : new UserCommandLineAction[]{
            new QuitCommand(this), new NewFileCommand( this ), new NewSectorCommand( this ), new MoveCommand(), new SaveCommand(this), new ResizeCommand( this ),
            new StatusCommand(this)}) {

            this.registerCommand(cmd);
        }

        return super.init();
    }
}
