package br.odb.moonshot;


import br.odb.gameapp.ConsoleApplication;

public class LevelEditorCliApp {


	public static void main(String[] args) {
		final LevelEditor levelEditor = new LevelEditor();

		levelEditor.setAppName("Moonshot Level Editor")
				.setAuthorName("Daniel 'MontyOnTheRun' Monteiro")
				.setLicenseName("3-Clause BSD").setReleaseYear(2016);
		levelEditor.setApplicationClient(new ConsoleApplication.ConsoleClient(levelEditor));
		levelEditor.waitForInput();
	}
}
