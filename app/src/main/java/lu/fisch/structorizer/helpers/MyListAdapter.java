package lu.fisch.structorizer.helpers;

import java.util.List;

import lu.fisch.structorizer.R;
import lu.fisch.structorizer.R.id;
import lu.fisch.structorizer.R.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyListAdapter extends ArrayAdapter<Item> {
	
	private List<Item> items;
	private OnCustomClickListener callback;
	
	public MyListAdapter(Context context, int resource, List<Item> items) {
	
	    super(context, resource, items);

	    this.callback=(OnCustomClickListener)context;
	    this.items = items;
	
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    View v = convertView;
	
	    if (v == null) {
	
	        LayoutInflater vi;
	        vi = LayoutInflater.from(getContext());
	        v = vi.inflate(R.layout.list_item, null);
	
	    }
	
	    Item p = items.get(position);
	
	    if (p != null) {
	
	        TextView tt = (TextView) v.findViewById(R.id.name);
	        TextView tt1 = (TextView) v.findViewById(R.id.description);
	        TextView tt3 = (TextView) v.findViewById(R.id.id);
	
	        if (tt != null) {
	            tt.setText(p.getName());
	        }
	        if (tt1 != null) {
	
	            tt1.setText(p.getDescription());
	        }
	        if (tt3 != null) {
	
	            tt3.setText(p.getId());
	        }
	    }
	    
	    v.setOnClickListener(new CustomOnClickListener(callback, position));
	    
	    return v;
	
	}
}