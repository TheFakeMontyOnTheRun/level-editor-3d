package br.odb.disksofdoom;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;

import java.util.LinkedList;

public class DisksOfDoomMainApp extends ConsoleApplication {


    public static class Disk {

        public int size;

        public Disk(int size) {
            this.size = size;
        }
    }

    LinkedList< Disk>[] pole;
    int move;
    
    public DisksOfDoomMainApp() {
        this.pole = new LinkedList[3];
        pole[ 0] = new LinkedList<Disk>();
        pole[ 1] = new LinkedList<Disk>();
        pole[ 2] = new LinkedList<Disk>();

        pole[ 0].push(new Disk(3));
        pole[ 0].push(new Disk(2));
        pole[ 0].push(new Disk(1));
    }

    public static void main(String[] args) {

        // This will probably never change.
        DisksOfDoomMainApp doom = (DisksOfDoomMainApp) new DisksOfDoomMainApp()
                .setAppName("Disks Of Doom")
                .setAuthorName("Daniel 'MontyOnTheRun' Monteiro")
                .setLicenseName("3-Clause BSD").setReleaseYear(2014);
        doom.createDefaultClient();
        doom.start();
    }

    @Override
    public void log(String tag, String message) {
        getClient().printVerbose(tag + ":" + message);

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        super.run();
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
            new QuitCommand(this), new NewGameCommand( this ), new MoveCommand(), new SolveCommand(this),
            new StatusCommand(this)}) {

            this.registerCommand(cmd);
        }

        return super.init();
    }

	public void updateVisuals(DisksOfDoomMainApp game) {
		try {
			new StatusCommand( game).run(game, "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
