package lu.fisch.structorizer;

import java.util.ArrayList;
import java.util.List;

import lu.fisch.structorizer.helpers.Item;
import lu.fisch.structorizer.helpers.MyListAdapter;
import lu.fisch.structorizer.helpers.OnCustomClickListener;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class Load extends Activity implements OnCustomClickListener {

	private static final int TIMEOUT_MILLISEC = 500;
	
	private ArrayList<Item> items = new ArrayList<Item>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load);
		
		catchData();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
	  setContentView(R.layout.activity_load);
	}
	
	@Override
	public void OnCustomClick(View aView, int position) {
		Log.d("bob",position+""); 
		Intent intent = new Intent(this, Editor.class);
		intent.putExtra("lu.fisch.structorizer.item", items.get(position));
		startActivity(intent);
		catchData();
	}

	
	public void catchData()
	{
		ArrayList<Item> empty = new ArrayList<Item>();
		empty.add(new Item("", "Error", "No connexion to the server ...", ""));
		ListView yourListView = (ListView) findViewById(R.id.list);
		yourListView.setClickable(true);
		MyListAdapter customAdapter = new MyListAdapter(Load.this,R.layout.list_item,empty);
		yourListView.setAdapter(customAdapter);

		// getting data from the network may not be done in the main thread
		final Runnable r = new Runnable() {
			public void run() {
				items.clear();
				
				HttpParams httpParams = new BasicHttpParams();
		        HttpConnectionParams.setConnectionTimeout(httpParams,
		                TIMEOUT_MILLISEC);
		        HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
		        //
		        HttpParams p = new BasicHttpParams();
		        // p.setParameter("name", pvo.getName());
		        p.setParameter("user", "1");

		        HttpClient httpclient = new DefaultHttpClient(p);
		        String url = "http://structorizer.fisch.lu/Php/diagrams.php";
		        HttpPost httppost = new HttpPost(url);	
		        
		        try 
		        {
		            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		            nameValuePairs.add(new BasicNameValuePair("user", "1"));
		            
		            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		            ResponseHandler<String> responseHandler = new BasicResponseHandler();
		            String responseBody = httpclient.execute(httppost,
		                    responseHandler);
		            // Parse
		            JSONObject json = new JSONObject(responseBody);
		            JSONArray jArray = json.getJSONArray("diagrams");

		            for (int i = 0; i < jArray.length(); i++) 
		            {
		                JSONObject diagram = jArray.getJSONObject(i);
		                Log.d("bob",diagram.getString("name"));
		                
		                items.add(new Item(diagram.getString("id"),
		                				   diagram.getString("name"),
		                				   diagram.getString("desc"),
		                				   diagram.getString("xml")));	
		            }

		        } 
		        catch (Exception e) 
		        {
		        	Log.w("bob",e.toString());
		            e.printStackTrace();
		        }
		        finally 
		        {
		        	final ArrayList<Item> finalItems = items;
		        	// updating UI must be done in the main thread,
		        	Runnable r2 = new Runnable() {
						
						@Override
						public void run() 
						{
							ListView yourListView = (ListView) findViewById(R.id.list);
							yourListView.setClickable(true);
				    		MyListAdapter customAdapter = new MyListAdapter(Load.this,R.layout.list_item,finalItems);
				    		yourListView.setAdapter(customAdapter);
						}
					};
					// so tell it to do so.
					runOnUiThread(r2);
		        }
				
			}
		};
		
		(new Thread(r)).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.load, menu);
		return true;
	}



	

}
