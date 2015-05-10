package br.odb.derelict;



public class LevelEditorCliApp {
	

	public static void main( String[] args ) {
		LevelEditor levelEditor = new LevelEditor();
		
		levelEditor.setAppName("Derelict3D Level Editor")
        .setAuthorName("Daniel 'MontyOnTheRun' Monteiro")
        .setLicenseName("3-Clause BSD").setReleaseYear(2014);
		levelEditor.createDefaultClient();
		new Thread( levelEditor ).start();
	}
}
