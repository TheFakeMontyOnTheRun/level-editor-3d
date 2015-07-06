package br.odb.leveleditor3d.android;

import br.odb.derelict.GenericTreeGeometryCompiler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MenuActivity extends Activity implements OnClickListener {


    Spinner spnLevels;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

        spnLevels = (Spinner)findViewById( R.id.spnLevels );

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[] { "floor1.opt.ser", "prison.opt.ser" } );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLevels.setAdapter(adapter);

		findViewById(R.id.btnModifyGeometry).setOnClickListener(this);
		findViewById(R.id.btnViewIn3D).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		Intent intent;

		switch (v.getId()) {
		
		case R.id.btnModifyGeometry:
			intent = new Intent(this, UseCardboardActivity.class);
			intent.putExtra( "level", (String)spnLevels.getSelectedItem() );
			startActivity(intent);
			break;
		
		case R.id.btnViewIn3D:
			intent = new Intent(this, MainActivity.class);
            intent.putExtra( "level", (String)spnLevels.getSelectedItem() );
			startActivity(intent);
			break;
		}
	}
}
