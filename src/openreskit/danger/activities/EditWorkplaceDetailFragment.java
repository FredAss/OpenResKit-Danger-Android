package openreskit.danger.activities;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.deser.std.DateDeserializers.SqlDateDeserializer;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import openreskit.danger.R;

import openreskit.danger.functions.Serialization;
import openreskit.danger.functions.Status;
import openreskit.danger.models.Activity;
import openreskit.danger.models.Assessment;
import openreskit.danger.models.Category;
import openreskit.danger.models.GFactor;
import openreskit.danger.models.Threat;
import openreskit.danger.models.Workplace;

import openreskit.danger.persistence.ORMLiteDBHelper;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.TextView;

public class EditWorkplaceDetailFragment extends Fragment 
{
	ViewGroup view;
	
	
	Workplace editedWorkplace;
	int editedWorkplacePosition;

	List<Category> categoryFromDb;
	List<Integer> categoryFromeditingWorkplace;
	List<Category> selectedCategoriesList;

	List<String> activityList;
	List<Activity> activitiesList;
	List<Activity> newActivitiesList;
	ArrayAdapter<String> activityAdapter;
	
	TextView surveytypeText;
	
	ListView activityListView;
	EditText workplaceName;
	EditText workplaceNameCompany;
	EditText workplaceDescription;
	EditText activityName;
	Button addActivity;
	TextView companyName;
	
	ORMLiteDBHelper helper;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		helper = new ORMLiteDBHelper(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		this.setHasOptionsMenu(true);
		view = (ViewGroup) inflater.inflate(R.layout.edit_workplace_detail, container, false);
		
		surveytypeText = (TextView)view.findViewById(R.id.SurveytypeSelected);

		activityListView = (ListView)view.findViewById(R.id.ActivityListEdit);
		workplaceName = (EditText)view.findViewById(R.id.WorkplaceName);
		workplaceNameCompany = (EditText)view.findViewById(R.id.WorkplaceNameCompany);
		workplaceDescription = (EditText)view.findViewById(R.id.WorkplaceDescribtion);
		activityName = (EditText)view.findViewById(R.id.ActivityNameEdit);
		addActivity = (Button)view.findViewById(R.id.AddActivityEdit);
		companyName = (TextView)view.findViewById(R.id.companyNameEdit);
		selectedCategoriesList = new ArrayList<Category>();
		
		companyName.setText(EditWorkplaceFragmentActivity.selectedCompany.getName());
		

		//ds = new DataSource(getActivity());
		
		try
		{
			categoryFromDb = helper.getCategoryDao().queryForEq("surveytype_id", editedWorkplace.getSurveyType().getId());
			
			
			//ds.Open();
			//categoryFromDb = ds.GetAllCategoriesFromSurveytype(editedWorkplace.getSurveyType());
			
			List<Assessment> ExistendThreat = helper.getAssessmentDao().queryForEq("workplace_id", editedWorkplace.getId());
			Assessment ass = ExistendThreat.get(0);
			if (helper.getThreatDao().queryForEq("assessment_id", ass).get(0).getStatus() == Status.No_Threat.getNumericType()) {
				
			}
			
			//isGoodToChange = ds.IsThreatIsWorkedExistend(ds.GetAssessmentFromWorkplace(editedWorkplace));
			//ds.Close();
		}
		catch(Exception ex)
		{
			
		}

		LoadDataFromWorkplaceInBoxes();

		addActivity.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				String nameOfActivity = activityName.getText().toString();
				
				Activity activity = new Activity();
				activity.setName(nameOfActivity);
				
				AddActivityToList(activity);
			}
		});
		
		
		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
		inflater = getActivity().getMenuInflater();
	    inflater.inflate(R.menu.workplace_edit_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    
		switch (item.getItemId()) 
	    {
	        case R.id.workplace_edit_edit:  
	        	
	        	OptionsForEditSelectedWorkplace();
	        	//EditSelectedWorkplace();   		        	
				return true;
				
	        case R.id.workplace_edit_delete:
				DeleteEditedWorkplace();
				Toast.makeText(getActivity(), "Arbeitsplatz wurde gelöscht.", Toast.LENGTH_LONG).show();
	            return true;
	            
	        case R.id.workplace_edit_copy:
	        	CreateCopyOfEditedWorkplace();
				Toast.makeText(getActivity(), "Arbeitsplatz wurde kopiert und gespeichert.", Toast.LENGTH_LONG).show();
	        	return true;
	        	
	        case R.id.workplace_edit_save:
	        	SaveChangeOfEditedWorkplace();  
				Toast.makeText(getActivity(), "Änderungen des Arbeitsplatzes wurden gespeichert.", Toast.LENGTH_LONG).show();
	        	return true;
	        	
	        case R.id.workplace_edit_cancel:
	        	CancelWorkplaceEdit();
	    		return true;
	    		
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	public void EditSelectedWorkplace()
	{
		Assessment assessment = new Assessment();
		
		try
		{
			//assessment = helper.getAssessmentDao().queryForEq("workplace_id", editedWorkplace.getId()).get(0);
			List<Assessment> result2 = helper.getAssessmentDao().queryForEq("workplace_id", editedWorkplace.getId());
			
			assessment = result2.get(result2.size()-1);
		}
		catch(Exception ex)
		{
			
		}
		
		//OptionsForEditSelectedWorkplace();
		
		Bundle bundle = new Bundle();
		bundle.putByteArray("SelectedWorkplace", Serialization.SerializeObject(editedWorkplace));
		bundle.putByteArray("Assessment", Serialization.SerializeObject(assessment));
		
		Intent newIntent = new Intent(getActivity(), DangerAssessmentFragmentActivity.class);
		newIntent.putExtras(bundle);
		startActivity(newIntent);
	}
	
	private void OptionsForEditSelectedWorkplace()
	{
		LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_edit_item, null);
				
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setTitle("Optionen");
		Dialog dialog = new Dialog(getActivity());
		
		Button buttonEditAssessment = (Button)layout.findViewById(R.id.actualAssessment);
		Button buttonNewAssessment = (Button)layout.findViewById(R.id.newAssessment);	
		
		buttonEditAssessment.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				EditSelectedWorkplace();
			}
		});
		
		buttonNewAssessment.setOnClickListener(new View.OnClickListener()
		{	
			@Override
			public void onClick(View v) 
			{
				Assessment assessment = new Assessment();	
				assessment.setId(UUID.randomUUID().toString());
				assessment.setWorkplace(editedWorkplace);
				assessment.setAssessmentDate(Calendar.getInstance().getTime());
							
				try 
				{
					helper.getAssessmentDao().create(assessment);
				} 
				catch (SQLException e)
				{
					e.printStackTrace();
				}
				
				// Contains all categories of one Surveytype
				List<Category> surveyTypeCategories = new ArrayList<Category>();
				// Contains all categories of one Workplace, which has been selected
		
				// Contains all Gfactors of all categories of one Surveytype
				List<GFactor> gfactorsFromSurveytype = new ArrayList<GFactor>();
						
				// Filling the lists with data
				try
				{
					surveyTypeCategories = helper.getCategoryDao().queryForEq("surveytype_id", editedWorkplace.getSurveyType().getId());
					
					//workplaceCategories = helper.getCategoryDao().queryForEq("workplace_id", workplace.getId());
					for (Category stc: surveyTypeCategories) 
					{
						gfactorsFromSurveytype.addAll(helper.getGfactorDao().queryForEq("category_id", stc.getId()));
					}
				}
				catch(Exception ex)
				{
							
				}
						
				// The first loop goes through the list with the Gfactors
				// The default-Threat is constructed here
				for(int i = 0; i < gfactorsFromSurveytype.size(); i++)
				{
					// Creates a new Threat
					Threat threat = new Threat();
					threat.setId(UUID.randomUUID().toString());
					threat.setDescription("");
					threat.setRiskDimension(5);
					threat.setRiskPossibility(5);
					threat.setAssessment(assessment);
					threat.setGFactor(gfactorsFromSurveytype.get(i));
					threat.setStatus(Status.Not_Finished.getNumericType());
						
					

					try
					{
						helper.getThreatDao().create(threat);
						Log.i("DBOperation", "Gefährdung angelegt " + threat.getDescription() + " " + threat.getGFactor().getName());
					}
					catch(Exception ex)
					{
						Log.i("DBOperation", "Einfügen Gefährdung fehlgeschlagen" + ex);
					}
				}
				
				EditSelectedWorkplace();
			}
		});
		
		
		
		builder.setView(layout);
		dialog = builder.create();
		dialog.show();
	}
	
	private void DeleteEditedWorkplace()
	{
		try
		{
			helper.getWorkplaceDao().delete(editedWorkplace);			
		}
		catch(Exception ex)
		{
			
		}
		
		EditWorkplaceFragmentActivity.listFragment.DeleteWorkplaceInList(editedWorkplacePosition);
	
		CancelWorkplaceEdit();
	}
	
	private void SaveChangeOfEditedWorkplace()
	{
		String name = workplaceName.getText().toString();
		String nameCompany = workplaceNameCompany.getText().toString();
		String description = workplaceDescription.getText().toString();
		
		if (name.isEmpty() || nameCompany.isEmpty() || description.isEmpty()) {
			Context context = getActivity().getApplicationContext();
			CharSequence text = "Bitte alle Felder ausfüllen";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		else
		{
			editedWorkplace.setName(name);
			editedWorkplace.setNameCompany(nameCompany);
			editedWorkplace.setDescription(description);
				
			try
			{
				helper.getWorkplaceDao().update(editedWorkplace);
//				ds.Open();
//				ds.UpdateWorkplaceInDbWithoutCategories(editedWorkplace);
//				ds.Close();
			}
			catch(Exception ex)
			{
					
			}
			
			for (Activity activity : newActivitiesList) 
			{
				activity.setId(UUID.randomUUID().toString());
				activity.setWorkplace(editedWorkplace);
				
				try {
					helper.getActivityDao().create(activity);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

				try
				{
					
					helper.getWorkplaceDao().update(editedWorkplace);
				
					Log.i("DBOperation", "Ändern Arbeitsplatz erfolgreich" + editedWorkplace.getName());
					

				}
				catch(Exception ex)
				{
						
				}
	
			Fragment newFragment;
	    		
	    	newFragment = new AddWorkplaceDetailFragment();
	    	EditWorkplaceFragmentActivity.listFragment.UpdateWorkplaceInList(editedWorkplace, editedWorkplacePosition);
    		FragmentTransaction transaction = getFragmentManager().beginTransaction();
    		transaction.replace(R.id.EditWorkplaceDetail, newFragment);
    		transaction.commit();
		}
	}
	
	private void CancelWorkplaceEdit()
	{
		Fragment newFragment;
    	newFragment = new AddWorkplaceDetailFragment();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.EditWorkplaceDetail, newFragment);
		transaction.commit();
	}
	
	private void ChangeStatusOfThreats(List<Category> selectedCategory) 
	{
		Assessment changingAssessment = new Assessment();
		List<Threat> changingThreats = new ArrayList<Threat>();
		
		try
		{
			changingAssessment = (Assessment) helper.getAssessmentDao().queryForEq("workplace_id", editedWorkplace.getId());
			changingThreats = helper.getThreatDao().queryForEq("assessment_id", changingAssessment.getId());
			
//			ds.Open();
//			changingAssessment = ds.GetAssessmentFromWorkplace(editedWorkplace);
//			changingThreats = ds.GetAllThreatFromDb(changingAssessment);
//			ds.Close();
		}
		catch(Exception ex)
		{
			
		}
		
		for (Threat threat : changingThreats) 
		{
			for (Category category : selectedCategory) 
			{
				if(threat.getGFactor().getCategory().getId() != category.getId())
					threat.setStatus(Status.Not_Relevant.getNumericType());
				else if(threat.getGFactor().getCategory().getId() == category.getId())
				{
					threat.setStatus(Status.Not_Finished.getNumericType());
					break;
				}
			}
			
			try
			{
				helper.getThreatDao().update(threat);
//				ds.Open();
//				ds.UpdateThreatInDb(threat);
//				ds.Close();
			}
			catch(Exception ex)
			{
				
			}
		}
	}

	private void CreateCopyOfEditedWorkplace()
	{
		//List<Integer> categoriesOfEditedWorkplace = new ArrayList<Integer>();
		List<Category> categoriesOfSurveytype = new ArrayList<Category>();

		
		Workplace copyOfEditedWorkplace = new Workplace();
		
		copyOfEditedWorkplace.setName("Kopie von " + editedWorkplace.getName());
		copyOfEditedWorkplace.setId(UUID.randomUUID().toString());
		copyOfEditedWorkplace.setNameCompany(editedWorkplace.getNameCompany());
		copyOfEditedWorkplace.setDescription(editedWorkplace.getDescription());
		copyOfEditedWorkplace.setCompany(editedWorkplace.getCompany());
		copyOfEditedWorkplace.setSurveyType(editedWorkplace.getSurveyType());
		
		for (Activity activity : editedWorkplace.getActivities()) 
		{
			Activity copyOfActivity = new Activity();
			copyOfActivity.setId(UUID.randomUUID().toString());
			copyOfActivity.setName(activity.getName());
			copyOfActivity.setWorkplace(copyOfEditedWorkplace);
			
			try {
				helper.getActivityDao().create(copyOfActivity);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try
		{

			helper.getWorkplaceDao().createOrUpdate(copyOfEditedWorkplace);
			Log.i("DBOperation", "Kopieren Arbeitsplatz erfolgreich" + copyOfEditedWorkplace.getName());

		}
		catch(Exception ex)
		{
			
		}

		Assessment assessment = new Assessment();
		assessment.setId(UUID.randomUUID().toString());
		assessment.setWorkplace(copyOfEditedWorkplace);
		assessment.setAssessmentDate(Calendar.getInstance().getTime());

		
			try {
				helper.getAssessmentDao().create(assessment);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
	
		// Contains all categories of one Surveytype
		List<Category> surveyTypeCategories = new ArrayList<Category>();

		// Contains all Gfactors of all categories of one Surveytype
		List<GFactor> gfactorsFromSurveytype = new ArrayList<GFactor>();
				
		// Filling the lists with data
		try
		{
			surveyTypeCategories = helper.getCategoryDao().queryForEq("surveytype_id", copyOfEditedWorkplace.getSurveyType().getId());
			//workplaceCategories = helper.getCategoryDao().queryForEq("workplace_id", workplace.getId());
			for (Category stc: surveyTypeCategories) {
				gfactorsFromSurveytype.addAll(helper.getGfactorDao().queryForEq("category_id", stc.getId()));
			}
		}
		catch(Exception ex)
		{
					
		}
				
		// The first loop goes through the list with the Gfactors
		// The default-Threat is constructed here
		for(int i = 0; i < gfactorsFromSurveytype.size(); i++)
		{
			// Creates a new Threat
			Threat threat = new Threat();
			threat.setId(UUID.randomUUID().toString());
			threat.setDescription("");
			threat.setRiskDimension(5);
			threat.setRiskPossibility(5);
			threat.setAssessment(assessment);
			threat.setGFactor(gfactorsFromSurveytype.get(i));
			threat.setStatus(Status.Not_Finished.getNumericType());
				
			
			

			
			// At the end of iteration the new Threat is saved in the database
			try
			{
				helper.getThreatDao().create(threat);
				Log.i("DBOperation", "Gefährdung angelegt " + threat.getDescription() + " " + threat.getGFactor().getName());
//				ds.Open();
//				ds.InsertThreatInDb(threat);
//				ds.Close();
			}
			catch(Exception ex)
			{
				Log.i("DBOperation", "Einfügen Gefährdung fehlgeschlagen" + ex);
			}
		
			
			
			
		
		}
		
		Fragment newFragment;
    	newFragment = new AddWorkplaceDetailFragment();
		EditWorkplaceFragmentActivity.listFragment.AddWorkplaceInList(copyOfEditedWorkplace);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.EditWorkplaceDetail, newFragment);
		transaction.commit();
	}
			
	void setMessageFromList(Object obj, int position)
	{
		editedWorkplace = (Workplace) obj;
		editedWorkplacePosition = position;
	}
	
	public void LoadDataFromWorkplaceInBoxes()
	{


		surveytypeText.setText("Ausgewählter Fragebogen: " + editedWorkplace.getSurveyType().getName());
	

		
		workplaceName.setText(editedWorkplace.getName());
		workplaceNameCompany.setText(editedWorkplace.getNameCompany());
		workplaceDescription.setText(editedWorkplace.getDescription());
		
		
		FillActivityInList();
	}

	private void FillActivityInList() 
	{
		activitiesList = new ArrayList<Activity>();
		activityList = new ArrayList<String>();
		newActivitiesList = new ArrayList<Activity>();

		try {
			activitiesList = helper.getActivityDao().queryForEq("workplace_id", editedWorkplace.getId());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (Activity activity : activitiesList) 
		{
			activityList.add(activity.getName());
		}
		
		activityAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, activityList);
		activityListView.setAdapter(activityAdapter);
	}
	
	private void AddActivityToList(Activity activity)
	{
		newActivitiesList.add(activity);
		activityList.add(activity.getName());
		activityAdapter.notifyDataSetChanged();
	}
}
