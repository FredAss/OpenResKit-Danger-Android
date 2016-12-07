package openreskit.danger.adapters;

import java.util.List;
import openreskit.danger.R;
import openreskit.danger.models.GFactor;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GFactorAdapter extends ArrayAdapter<GFactor> 
{
	private List<GFactor> gfactors;
	private boolean[] isMarked;
	
	public GFactorAdapter(Context context, int textViewResourceId, List<GFactor> gfactors, boolean[] isMarked) 
	{
		super(context, textViewResourceId, gfactors);

		this.gfactors = gfactors;
		this.isMarked = isMarked;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{		
		View v = convertView;
		
		if(v == null)
		{
			LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.listitem_layout_gfactor, null);
		}
		
		GFactor gfactor = gfactors.get(position);
		
		if(gfactor != null)
		{
			TextView Name = (TextView)v.findViewById(R.id.gfactorName);
			TextView Number = (TextView)v.findViewById(R.id.gactorNumber);
			
			if(Number != null)
			{
				Number.setText(gfactor.getNumber());
			}
			if(Name != null)
			{
				Name.setText(gfactor.getName());
			}
			if(isMarked[position] == false)
			{
				Name.setTextColor(Color.parseColor("#21610B"));
			}
			else
			{
				Name.setTextColor(android.graphics.Color.BLACK);
			}
		}
		return v;
	}
}
