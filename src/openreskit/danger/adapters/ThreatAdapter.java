package openreskit.danger.adapters;

import java.util.List;
import openreskit.danger.R;
import openreskit.danger.models.Threat;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ThreatAdapter extends ArrayAdapter<Threat> 
{
	private List<Threat> threats;
	
	public ThreatAdapter(Context context, int textViewResourceId, List<Threat> gfactors) 
	{
		super(context, textViewResourceId, gfactors);

		this.threats = gfactors;
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
		
		Threat threat = threats.get(position);
		
		if(threat != null)
		{
			TextView Name = (TextView)v.findViewById(R.id.gfactorName);
			TextView Number = (TextView)v.findViewById(R.id.gactorNumber);
			
			if(Number != null)
			{
				Number.setText(threat.getGFactor().getNumber());
			}
			if(Name != null)
			{
				Name.setText(threat.getGFactor().getName());
			}
			
			/*if(threat.getStatus().equals("bearbeitet") && threat.getRiskDimension() != 0 && threat.getRiskPossibility() != 0);
			{
				Name.setTextColor(Color.parseColor("#FF0000"));
			}
			if(threat.getStatus().equals("bearbeitet") && threat.getRiskDimension() == 0 && threat.getRiskPossibility() == 0)
			{
				Name.setTextColor(Color.parseColor("#04B404"));
			}
			if(threat.getStatus().equals("nicht bearbeitet"))
			{
				Name.setTextColor(Color.parseColor("#000000"));
			}*/
		}
		return v;
	}
}
