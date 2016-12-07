package openreskit.danger.adapters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import openreskit.danger.R;
import openreskit.danger.models.Action;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ActionAdapter extends ArrayAdapter<Action>
{
	private List<Action> actions;
	
	public ActionAdapter(Context context, int textViewResourceId, List<Action> actions) 
	{
		super(context, textViewResourceId, actions);
		this.actions = actions;		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View v = convertView;
		
		if(v == null)
		{
			LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.listitem_layout_action, null);
		}
		
		Action a = actions.get(position);
		
		if(a != null)
		{
			TextView Name = (TextView)v.findViewById(R.id.actionNameItem);
			TextView Responsible = (TextView)v.findViewById(R.id.actionResponsibleItem);
			TextView DueDate = (TextView)v.findViewById(R.id.actionDueDateItem);
			
			if(Name != null)
			{
				Name.setText(a.getDescription());
			}
			if(Responsible != null)
			{
				Responsible.setText("Verantwortlicher: " + a.getPerson().getName());
			}
			if(DueDate != null)
			{
				
//				DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
//				String ab = df.format(a.getDueDate()); 
				
				
				Calendar c = Calendar.getInstance();
				c.setTime(a.getDueDate());
			
				String ab = c.get(Calendar.DATE) + "." + (c.get(Calendar.MONTH)+1) + "." + c.get(Calendar.YEAR);
				
				DueDate.setText("zum " + ab);
			}	
		}	
		return v;
	}
}
