package openreskit.danger.adapters;

import java.util.List;

import openreskit.danger.R;
import openreskit.danger.models.ProtectionGoal;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ProtectionGoalAdapter extends ArrayAdapter<ProtectionGoal>
{
	private List<ProtectionGoal> protectiongoals;
	
	public ProtectionGoalAdapter(Context context, int textViewResourceId, List<ProtectionGoal> protectiongoals) 
	{
		super(context, textViewResourceId, protectiongoals);
		this.protectiongoals = protectiongoals;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View v = convertView;
		
		if(v == null)
		{
			LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.listitem_layout_protectiongoal, null);
		}
		
		ProtectionGoal pg = protectiongoals.get(position);
		
		if(pg != null)
		{
			TextView Name = (TextView)v.findViewById(R.id.protectionName);
			
			if(Name != null)
			{
				Name.setText(pg.getDescription());
			}
		}		
		return v;
	}
}
