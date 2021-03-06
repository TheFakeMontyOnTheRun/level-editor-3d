package br.odb.leveleditor3d.android;

import br.odb.derelict.GenericTreeGeometryCompiler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MenuActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		findViewById(R.id.btnCompile).setOnClickListener(this);
		findViewById(R.id.btnNewProject).setOnClickListener(this);
		findViewById(R.id.btnModifyGeometry).setOnClickListener(this);
		findViewById(R.id.btnViewIn3D).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btnCompile:
			GenericTreeGeometryCompiler g;
			break;
		
		case R.id.btnViewIn3D:
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			break;
		}
	}
}
