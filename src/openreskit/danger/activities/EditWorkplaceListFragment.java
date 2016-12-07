package openreskit.danger.activities;

import java.util.ArrayList;
import java.util.List;
import openreskit.danger.R;
import openreskit.danger.adapters.WorkplaceAdapter;
import openreskit.danger.interfaces.OnButtonAddPressListener;
import openreskit.danger.interfaces.OnButtonUpdatePressListener;
import openreskit.danger.interfaces.OnListClickListener;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import openreskit.danger.models.Company;
import openreskit.danger.models.Workplace;

import openreskit.danger.persistence.ORMLiteDBHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ListView;

public class EditWorkplaceListFragment extends Fragment 
{
	ViewGroup view;
	
	//DataSource ds;
	Company receivedCompany;
	List<Workplace> workplacesFromDb;
	OnListClickListener listListener;
	OnButtonAddPressListener addButtonListener;
	OnButtonUpdatePressListener updateButtonListener;
	WorkplaceAdapter workplaceListAdapter;
	
	ListView WorkplaceList;
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		
		receivedCompany = EditWorkplaceFragmentActivity.selectedCompany;
		view = (ViewGroup) inflater.inflate(R.layout.edit_workplace_list, container, false);
		WorkplaceList = (ListView)view.findViewById(R.id.WorkplaceList);
		WorkplaceList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		//ds = new DataSource(getActivity());
		
		init(view);
		LoadDataForWorkplaceList();
	
		return view;
	}
	
	private void init(View view)
	{
		WorkplaceList.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) 
			{	
				WorkplaceList.setSelector(R.drawable.background);
				WorkplaceList.setItemChecked(position, true);
				listListener.OnListClicked(workplacesFromDb.get(position), position);
				WorkplaceList.setSelection(position);
						
				v.setSelected(true);
			}			
		}); 
	}
	
	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		
		try
		{
			listListener = (OnListClickListener) getActivity();
		}
		catch(ClassCastException ex)
		{
			throw new ClassCastException(activity.toString() + " must implement onListClicked");
		}
	}

	private void LoadDataForWorkplaceList()
	{
		WorkplaceList.setAdapter(null);
		workplacesFromDb = new ArrayList<Workplace>();
		
		try
		{
			
			ORMLiteDBHelper help = new ORMLiteDBHelper(getActivity());
			workplacesFromDb = help.getWorkplaceDao().queryForEq("company_id", receivedCompany);
		
			
//			ds.Open();
//			workplacesFromDb = ds.GetAllWorkplacesFromCompanyFromDb(receivedCompany);
//			ds.Close();
		}
		catch(Exception ex)
		{
		
		}
		
		workplaceListAdapter = new WorkplaceAdapter(getActivity(), R.layout.listitem_layout_workplace, workplacesFromDb);
		WorkplaceList.setAdapter(workplaceListAdapter);
	}
	
	void AddWorkplaceInList(Workplace workplace)
	{
		workplacesFromDb.add(workplace);
		workplaceListAdapter.notifyDataSetChanged();
	}
	
	void UpdateWorkplaceInList(Workplace workplace, int position)
	{
		workplacesFromDb.add(position, workplace);
		workplacesFromDb.remove(position+1);
		workplaceListAdapter.insert(workplace, position);	
		
		WorkplaceList.setItemChecked(position, false);
	}
	
	void DeleteWorkplaceInList( int position)
	{
		workplacesFromDb.remove(position);
		workplaceListAdapter.notifyDataSetChanged();
	}
}
