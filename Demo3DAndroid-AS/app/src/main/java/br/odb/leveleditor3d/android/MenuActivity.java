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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[] { "prison.opt.ser", "prison.opt.xml" } );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLevels.setAdapter(adapter);

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
            intent.putExtra( "level", (String)spnLevels.getSelectedItem() );
			startActivity(intent);
			break;
		}
	}
}
