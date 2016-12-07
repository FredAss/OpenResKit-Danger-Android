package openreskit.danger.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import openreskit.danger.R;
import openreskit.danger.adapters.CategoryThreatExpandableAdapter;
import openreskit.danger.interfaces.OnGFactorListClickListener;
import openreskit.danger.models.Assessment;
import openreskit.danger.models.Category;
import openreskit.danger.models.Threat;
import openreskit.danger.models.Workplace;
import openreskit.danger.persistence.ORMLiteDBHelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class DangerAssessmentListFragment extends Fragment 
{
	ViewGroup view;

	
	Assessment assessment;
	Workplace receivedWorkplace;
	OnGFactorListClickListener listClickListener;
	HashMap<Category, List<Threat>> Child;
	List<Category> parent;
	List<Threat> listOfThreats;
	int positionOfGroup;
	int positionOfChild;
	
	ExpandableListView expandableList;
	ExpandableListAdapter expandableAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{		
		view = (ViewGroup) inflater.inflate(R.layout.danger_assessment_list, container, false);		
		Initialize();
		
		expandableList = (ExpandableListView)view.findViewById(R.id.dangerAssessmentList);
		expandableAdapter = new CategoryThreatExpandableAdapter(getActivity(), parent, Child);
		expandableList.setAdapter(expandableAdapter);
				
		expandableList.setItemChecked(1, true);
		
		DangerAssessmentFragmentActivity.threat = (Threat) expandableList.getExpandableListAdapter().getChild(0, 0);
		
		InitializeComponents();
		
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		
		try
		{
			listClickListener = (OnGFactorListClickListener)getActivity();
		}
		catch(ClassCastException ex)
		{
			throw new ClassCastException(activity.toString() + " must implement onListClicked");
		}
	}
	
	public void Initialize()
	{

		
		receivedWorkplace = DangerAssessmentFragmentActivity.receivedWorkplace;
		assessment = DangerAssessmentFragmentActivity.assassment;
		
		Child = new HashMap<Category, List<Threat>>();
		parent = new ArrayList<Category>();
		
		listOfThreats = new ArrayList<Threat>();
		
		try
		{
			ORMLiteDBHelper helper = new ORMLiteDBHelper(getActivity());
			listOfThreats = helper.getThreatDao().queryForEq("assessment_id", assessment.getId());
			parent = helper.getCategoryDao().queryForEq("surveytype_id", receivedWorkplace.getSurveyType().getId());
//			ds.Open();
//			listOfThreats = ds.GetAllThreatFromDb(assessment);
//			parent = ds.GetAllCategoriesFromSurveytype(receivedWorkplace.getSurveyType());
//			ds.Close();
		}
		catch(Exception ex)
		{
			Log.i("DBException", ex.toString());
		}
		
		for(int i = 0; i < parent.size(); i++)
		{
			List<Threat> test = new ArrayList<Threat>();
			
			for(int j = 0; j < listOfThreats.size(); j++)
			{
				if(listOfThreats.get(j).getGFactor().getCategory().getId() == parent.get(i).getId())
				{
					test.add(listOfThreats.get(j));
				}
			}
			
			Child.put(parent.get(i), test);
		}
	}
	
	public void InitializeComponents()
	{
		expandableList.setOnChildClickListener(new OnChildClickListener() 
		{
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) 
			{
				positionOfGroup = groupPosition;
				positionOfChild = childPosition;
				
				Threat selectedThreat = (Threat)((CategoryThreatExpandableAdapter)expandableList.getExpandableListAdapter()).getChild(groupPosition, childPosition);
				DangerAssessmentFragmentActivity.threat = selectedThreat;
				listClickListener.OnFactorListClicked(selectedThreat);
				
				int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
				parent.setItemChecked(index, true);
				
				return true;
			}
		});
	}
	
	public void AddThreatInDB(Threat newThreat)
	{
		Category cat = new Category();
		
		for(int i = 0; i < parent.size(); i++)
		{
			if(parent.get(i).getId() == newThreat.getGFactor().getCategory().getId())
			{
				cat = parent.get(i);
			}
		}
		
		Child.get(cat).add(positionOfChild + 1, newThreat);
		
		((CategoryThreatExpandableAdapter) expandableAdapter).notifyDataSetChanged();
	}
	
	public void UpdateThreatInDB(Threat doneThreat)
	{
		for(List<Threat> child : Child.values())
		{
			for (Threat threat : child) {
				if(threat.getId() == doneThreat.getId())
				{
					threat = doneThreat;
				}
			}	
		}
		
		((CategoryThreatExpandableAdapter) expandableAdapter).notifyDataSetChanged();
	}
	
	public void DisableExpandableListView()
	{
		expandableList.setEnabled(false);
	}
	
	public void EnableExpandableListView()
	{
		expandableList.setEnabled(true);
	}
}
