package openreskit.danger.adapters;

import java.util.List;
import openreskit.danger.R;
import openreskit.danger.models.Workplace;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WorkplaceAdapter extends ArrayAdapter<Workplace> 
{
	private List<Workplace> workplaces;
	
	public WorkplaceAdapter(Context context, int textViewResourceId, List<Workplace> workplaces) 
	{
		super(context, textViewResourceId, workplaces);
		this.workplaces = workplaces;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View v = convertView;

		if (v == null) 
		{
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.listitem_layout_workplace, null);
		}

		Workplace w = workplaces.get(position);

		if (w != null) 
		{
			TextView Name = (TextView)v.findViewById(R.id.workplaceName);
			TextView NameCompany = (TextView)v.findViewById(R.id.workplaceNameCompany);
			
					
			if(Name != null)
			{
				Name.setText(w.getName());
			}
			if(NameCompany != null)
			{
				NameCompany.setText(w.getNameCompany());
			}
					
		}
		
		return v;
	}

	@Override
	public void clear() 
	{
		super.clear();
		notifyDataSetChanged();
	}

	@Override
	public void add(Workplace workplace) 
	{
		super.add(workplace);
		notifyDataSetChanged();
	}

	@Override
	public void insert(Workplace workplace, int position) 
	{
		super.insert(workplace, position);
		workplaces.remove(position+1);
		notifyDataSetChanged();
	}	
}
