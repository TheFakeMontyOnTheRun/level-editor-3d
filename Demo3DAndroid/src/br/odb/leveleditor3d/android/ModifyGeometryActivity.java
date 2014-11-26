package br.odb.leveleditor3d.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import br.odb.derelict.LevelEditor;
import br.odb.gameapp.ApplicationClient;
import br.odb.gamelib.android.GameView;
import br.odb.libscene.SceneTesselator;
import br.odb.libsvg.ColoredPolygon;
import br.odb.utils.FileServerDelegate;

public class ModifyGeometryActivity extends Activity implements OnClickListener, ApplicationClient {

	LevelEditor levelEditor;
	GameView gvMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_geometry);
		
		levelEditor = ((LevelEditor3DApplication) getApplication()).levelEditor;
		levelEditor.setApplicationClient( this );
		findViewById(R.id.btnCreateSector).setOnClickListener(this);
		findViewById(R.id.btnDelete).setOnClickListener(this);
		findViewById(R.id.btnResize).setOnClickListener(this);
		findViewById(R.id.btnMove).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnCreateSector:
			levelEditor.sendData( "new-sector" );
			break;
		case R.id.btnDelete:
			levelEditor.sendData( "deleteSector" );
			break;
		case R.id.btnResize:
			levelEditor.sendData( "new-sector" );
			break;
		case R.id.btnMove:
			levelEditor.sendData( "new-sector" );
			break;
		}
		
		update();
	}
	
	public void update() {
//		ColoredPolygon[] shapes = ShapesRenderer.renderXZ( SceneTesselator.generateQuadsForWorld( levelEditor.world.masterSector ) );
	}


	@Override
	public int chooseOption(String arg0, String[] arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FileServerDelegate getFileServer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInput(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String openHTTP(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void playMedia(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printError(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printNormal(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printVerbose(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printWarning(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String requestFilenameForOpen() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String requestFilenameForSave() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendQuit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClientId(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shortPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void alert(String arg0) {
		// TODO Auto-generated method stub
		
	}
}
