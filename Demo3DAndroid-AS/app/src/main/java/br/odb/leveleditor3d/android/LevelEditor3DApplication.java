package br.odb.leveleditor3d.android;

import br.odb.derelict.LevelEditor;
import android.app.Application;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LevelEditor3DApplication extends Application {
	public LevelEditor levelEditor;

	public LevelEditor3DApplication() {
//		levelEditor = new LevelEditor();
//
//		levelEditor.setAppName("Derelict3D Level Editor")
//        .setAuthorName("Daniel 'MontyOnTheRun' Monteiro")
//        .setLicenseName("3-Clause BSD").setReleaseYear(2014);
//		levelEditor.createDefaultClient();
//		levelEditor.start();
	}


	public static String readFully(InputStream inputStream, String encoding)
			throws IOException {
		return new String(readFully(inputStream), encoding);
	}

	private static byte[] readFully(InputStream inputStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, length);
		}
		return baos.toByteArray();
	}
}
