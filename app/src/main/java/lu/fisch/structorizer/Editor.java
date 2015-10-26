package lu.fisch.structorizer;

import lu.fisch.structorizer.elements.Alternative;
import lu.fisch.structorizer.elements.Call;
import lu.fisch.structorizer.elements.Case;
import lu.fisch.structorizer.elements.For;
import lu.fisch.structorizer.elements.Forever;
import lu.fisch.structorizer.elements.Instruction;
import lu.fisch.structorizer.elements.Jump;
import lu.fisch.structorizer.elements.Parallel;
import lu.fisch.structorizer.elements.Repeat;
import lu.fisch.structorizer.elements.While;
import lu.fisch.structorizer.helpers.DrawSurface;
import lu.fisch.structorizer.helpers.Item;
import lu.fisch.structorizer.parsers.NSDParser;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;


public class Editor extends Activity {

	protected DrawSurface drawSurface = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);
		
        // get handles to the LunarView from XML, and its LunarThread
		drawSurface = (DrawSurface) findViewById(R.id.drawsurface);
		if (savedInstanceState != null) 
		{
			drawSurface.restore(savedInstanceState);
        } 

		ImageButton button;
		
		// button delete
		button = (ImageButton) findViewById(R.id.button_delete);
		button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	drawSurface.getRoot().removeElement(drawSurface.getSelected());
		    }
		});		

		// button instruction
		button = (ImageButton) findViewById(R.id.button_int_after);
		button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	drawSurface.addNewElement(new Instruction(),"Add new instruction ...","",true);
		    }
		});		

		// button alt
		button = (ImageButton) findViewById(R.id.button_alt_after);
		button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	drawSurface.addNewElement(new Alternative(),"Add new IF statement ...","",true);
		    }
		});		

		// button case
		button = (ImageButton) findViewById(R.id.button_case_after);
		button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	drawSurface.addNewElement(new Case(),"Add new CASE statement ...","",true);
		    }
		});		

		// button case
		button = (ImageButton) findViewById(R.id.button_for_after);
		button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	drawSurface.addNewElement(new For(),"Add new FOR loop ...","",true);
		    }
		});		

		// button case
		button = (ImageButton) findViewById(R.id.button_while_after);
		button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	drawSurface.addNewElement(new While(),"Add new WHILE loop ...","",true);
		    }
		});		

		// button case
		button = (ImageButton) findViewById(R.id.button_repeat_after);
		button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	drawSurface.addNewElement(new Repeat(),"Add new REPEAT loop ...","",true);
		    }
		});		

		// button case
		button = (ImageButton) findViewById(R.id.button_endless_after);
		button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	drawSurface.addNewElement(new Forever(),"Add new ENDLESS loop ...","",true);
		    }
		});		

		// button case
		button = (ImageButton) findViewById(R.id.button_call_after);
		button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	drawSurface.addNewElement(new Call(),"Add new call ...","",true);
		    }
		});		

		// button case
		button = (ImageButton) findViewById(R.id.button_jump_after);
		button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	drawSurface.addNewElement(new Jump(),"Add new jump ...","",true);
		    }
		});		

		// button case
		button = (ImageButton) findViewById(R.id.button_para_after);
		button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	drawSurface.addNewElement(new Parallel(),"Add new parallel ...","",true);
		    }
		});		

		
		// load a diagram if the intent carried one
		Intent intent = getIntent();
		Item item = (Item) intent.getSerializableExtra("lu.fisch.structorizer.item");
		if(item!=null)
		{
			this.setTitle("Structorizer :: "+item.getName());
			NSDParser parser = new NSDParser();
			Log.d("bob",item.getXml());
			drawSurface.setRoot(parser.parse(item.getXml()));
		}
	}
	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // just have the View's thread save its state into our Bundle
        super.onSaveInstanceState(outState);
        drawSurface.saveState(outState);
    }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
