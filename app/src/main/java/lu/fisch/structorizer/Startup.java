package lu.fisch.structorizer;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class Startup extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startup);
		
		Button button = (Button) findViewById(R.id.button_new);
		button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		        Intent intent = new Intent(Startup.this,Editor.class);
		        startActivity(intent);
		    }
		});		

		button = (Button) findViewById(R.id.button_load);
		button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		        Intent intent = new Intent(Startup.this,Load.class);
		        startActivity(intent);
		    }
		});		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.startup, menu);
		return true;
	}

}
