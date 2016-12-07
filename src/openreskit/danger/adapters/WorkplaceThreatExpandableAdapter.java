package openreskit.danger.adapters;

import java.util.HashMap;
import java.util.List;
import openreskit.danger.R;
import openreskit.danger.functions.Status;
import openreskit.danger.models.Threat;
import openreskit.danger.models.Workplace;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class WorkplaceThreatExpandableAdapter extends BaseExpandableListAdapter
{
	private Context context;
	private List<Workplace> GroupCategory;
	private HashMap<Workplace, List<Threat>> ChildThreat;

	public WorkplaceThreatExpandableAdapter(Context context, List<Workplace> groupCategory, HashMap<Workplace, List<Threat>> childThreat) 
	{
		this.context = context;
		this.GroupCategory = groupCategory;
		this.ChildThreat = childThreat;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) 
	{
		return ChildThreat.get(GroupCategory.get(groupPosition)).get(childPosition);
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition) 
	{
		return childPosition;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) 
	{
		Threat threat = (Threat)getChild(groupPosition, childPosition);
		
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.listitem_layout_category_threat_child, null);
		}
		
		if(threat != null)
		{
			TextView Number = (TextView)convertView.findViewById(R.id.threatNumber);
			TextView Name = (TextView)convertView.findViewById(R.id.threatName);
			
			if(Number != null)
			{
				Number.setText(threat.getGFactor().getNumber());
			}
			if(Name != null)
			{
				Name.setText(threat.getGFactor().getName());
			}
			
			if(threat.getStatus() == Status.Threat.getNumericType());
			{
				Name.setTextColor(Color.parseColor("#FF0000"));
			}
			/*if(threat.getStatus() == Status.No_Threat.getNumericType())
			{
				Name.setTextColor(Color.parseColor("#04B404"));
			}
			if(threat.getStatus() == Status.Not_Finished.getNumericType())
			{
				Name.setTextColor(Color.parseColor("#000000"));
			}*/
		}	
		return convertView;
	}
	
	@Override
	public int getChildrenCount(int groupPosition) 
	{
		return ChildThreat.get(GroupCategory.get(groupPosition)).size();
	}
	
	@Override
	public Object getGroup(int groupPosition) 
	{
		return GroupCategory.get(groupPosition);
	}
	
	@Override
	public int getGroupCount() 
	{
		return GroupCategory.size();
	}
	
	@Override
	public long getGroupId(int groupPosition) 
	{
		return groupPosition;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) 
	{
		Workplace workplace = (Workplace)getGroup(groupPosition);
		
		if(convertView == null)
		{
			LayoutInflater infalInfater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInfater.inflate(R.layout.listitem_layout_category_threat_parent,  null);
		}
		
		if(workplace != null)
		{
			TextView group = (TextView)convertView.findViewById(R.id.groupName);
			
			if(group != null)
			{
				group.setText(workplace.getName());
			}
		}
		return convertView;
	}
	
	@Override
	public boolean hasStableIds() 
	{
		return false;
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) 
	{
		return true;
	}
}
