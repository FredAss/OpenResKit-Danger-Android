package openreskit.danger.activities;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.j256.ormlite.dao.ForeignCollection;

import openreskit.danger.R;

import openreskit.danger.functions.Status;
import openreskit.danger.models.Activity;
import openreskit.danger.models.Assessment;
import openreskit.danger.models.Category;
import openreskit.danger.models.Company;
import openreskit.danger.models.GFactor;
import openreskit.danger.models.Person;
import openreskit.danger.models.Surveytype;
import openreskit.danger.models.Threat;
import openreskit.danger.models.Workplace;

import openreskit.danger.persistence.ORMLiteDBHelper;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddWorkplaceDetailFragment extends Fragment 
{
	ViewGroup view;
	//DataSource ds;
	
	Company selectedCompany;
	Surveytype selectedSurveytype;

	List<Surveytype> surveytpyeFromDb;
	List<String> activityList;
	List<Activity> activitiesList;
	ArrayAdapter<String> activityAdapter;
		

	ListView activityListView;
	Spinner selectSurveytype;
	EditText addWorkplaceName;
	EditText addWorkplaceNameCompany;
	EditText addWorkplaceDescription;
	EditText activityName;
	Button addActivity;
	TextView companyName;
	ORMLiteDBHelper helper;
		
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		this.setHasOptionsMenu(true);
		view = (ViewGroup) inflater.inflate(R.layout.add_workplace_detail, container, false);
		
		companyName = (TextView)view.findViewById(R.id.companyName);
		
		activityListView = (ListView)view.findViewById(R.id.ActivityList);
		selectSurveytype = (Spinner)view.findViewById(R.id.SurveytypeAddSelection);
		addWorkplaceName = (EditText)view.findViewById(R.id.addWorkplaceName);
		addWorkplaceNameCompany = (EditText)view.findViewById(R.id.addWorkplaceNameCompany);
		addWorkplaceDescription = (EditText)view.findViewById(R.id.addWorkplaceDescribtion);
		activityName = (EditText)view.findViewById(R.id.ActivityName);
		addActivity = (Button)view.findViewById(R.id.AddActivity);
		
		selectedCompany = EditWorkplaceFragmentActivity.selectedCompany;
		companyName.setText(selectedCompany.getName());
		
		helper = new ORMLiteDBHelper(getActivity());
		
		//ds = new DataSource(getActivity());
		
		try
		{
			surveytpyeFromDb = helper.getSurveytypeDao().queryForAll();
//			ds.Open();
//			surveytpyeFromDb = ds.GetAllSurveytypesFromDb();
//			ds.Close();
		}
		catch(Exception ex)
		{
			
		}
		
		init(view, selectedCompany);
		
		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
		inflater = getActivity().getMenuInflater();
	    inflater.inflate(R.menu.workplace_add_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    switch (item.getItemId()) 
	    {
	        case R.id.workplace_add_save: 
	        	Workplace workplace = InsertWorkplace();
	        	
	        	if(workplace != null)
	        	{
	        		InsertAssessmentAndThreats(workplace);
	        		Fragment newFragment;
					newFragment = new AddWorkplaceDetailFragment();
					EditWorkplaceFragmentActivity.listFragment.AddWorkplaceInList(workplace);
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.EditWorkplaceDetail, newFragment);
					transaction.commit();
					
					Toast.makeText(getActivity(), "Arbeitsplatz wurde gespeichert.", Toast.LENGTH_LONG).show();
					
					return true;
	        	}
	        default:
	            return super.onContextItemSelected(item);
	    }			
	}
	
	private Workplace InsertWorkplace()
	{
		// Creating a new Workplace and pass the values of controls to variables
		Workplace workplace = null;
		String name = addWorkplaceName.getText().toString();
		String nameCompany = addWorkplaceNameCompany.getText().toString();
		String description = addWorkplaceDescription.getText().toString();
		
		// This checks if the controls are filled with values, which can be passed to the new Workplace
		// Controls not filled (if): returns a Toast with information
		// Controls are filled (else): ends up with inserting the new Workplace in the database
		if (name.isEmpty() | nameCompany.isEmpty() | description.isEmpty()) {
			Context context = getActivity().getApplicationContext();
			CharSequence text = "Bitte alle Felder ausfüllen";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		else 
		{
			workplace = new Workplace();
			
						
			// Set all properties of Workplace with values
			workplace.setName(name);
			workplace.setId(UUID.randomUUID().toString());
			workplace.setNameCompany(nameCompany);
			workplace.setDescription(description);
			workplace.setCompany(selectedCompany);
			workplace.setSurveyType(selectedSurveytype);
			
			for (Activity activity : activitiesList) 
			{
				activity.setId(UUID.randomUUID().toString());
				activity.setWorkplace(workplace);
				try 
				{
					helper.getActivityDao().create(activity);
				} catch (SQLException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			// This insert the Workplace and his selected categories in the database
			try
			{

				helper.getWorkplaceDao().createOrUpdate(workplace);
				Log.i("DBOperation", "Einfügen Arbeitsplatz erfolgreich" + workplace.getName());
//				ds.Open();
//				workplace = ds.InsertWorkplaceInDb(workplace, selectedCategory);
//				ds.Close();
			}
			catch(Exception ex)
			{
				Log.i("DBOperation", "Einfügen Arbeitsplatz fehlgeschlagen" + ex.toString());
			}
		}
		return workplace;
	}
	
	private void InsertAssessmentAndThreats(Workplace workplace)
	{
		// Creates an Assessment for a Workplace and save it into the database
		Assessment assessment = new Assessment();
		assessment.setWorkplace(workplace);
		assessment.setId(UUID.randomUUID().toString());
		assessment.setThreats(new ArrayList<Threat>());
		assessment.setAssessmentDate(Calendar.getInstance().getTime());

		
		
		try 
		{
			helper.getAssessmentDao().create(assessment);
			Log.i("DBOperation", "Einfügen Assessment erfolgreich" + assessment.getWorkplace().getName());
//			ds.Open();
//			assessment = ds.InsertAssessmentInDb(assessment);
//			ds.Close();
		} 
		catch (Exception ex) 
		{
			Log.i("DBOperation", "Einfügen Assessment fehlgeschlagen" + ex);
		}	
		
		// Contains all categories of one Surveytype
		List<Category> surveyTypeCategories = new ArrayList<Category>();
		// Contains all categories of one Workplace, which has been selected
	
		// Contains all Gfactors of all categories of one Surveytype
		List<GFactor> gfactorsFromSurveytype = new ArrayList<GFactor>();
		
		// Filling the lists with data
		try
		{
	
					
			surveyTypeCategories = helper.getCategoryDao().queryForEq("surveytype_id", workplace.getSurveyType().getId());
		
			for (Category stc: surveyTypeCategories) {
				gfactorsFromSurveytype.addAll(helper.getGfactorDao().queryForEq("category_id", stc.getId()));
			}
			Log.i("DBOperation", "Anzahl der Kategorien des Surveys: " + surveyTypeCategories.size());
		
			Log.i("DBOperation", "Anzahl der GFaktoren des Surveys: " + gfactorsFromSurveytype.size());
			Log.i("DBOperation", "Anzahl der GFaktoren gesamt: " + helper.getGfactorDao().queryForAll().size());
			//surveyTypeCategories = ds.GetAllCategoriesFromSurveytype(workplace.getSurveyType());
			//workplaceCategories = ds.GetAllCategoriesFromWorkplaceFromDb(workplace);
			//gfactorsFromSurveytype = ds.GetAllGFactorFromDb(surveyTypeCategories);
		//	ds.Close();
		}
		catch(Exception ex)
		{
			Log.i("DBOperation", "Listen mit Gfaktoren füllen fehlgeschlagen" + ex);
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
				Log.i("DBOperation", "Gefährdung angelegt " + threat.getStatus() + " Beschreibung: " + threat.getGFactor().getName());

			}
			catch(Exception ex)
			{
				Log.i("DBOperation", "Einfügen Gefährdung fehlgeschlagen" + ex);
			}
		}
	}
		
	void init(final ViewGroup view, final Company selectedCompany)
	{		
		List<String> surveyNames = new ArrayList<String>();
		for (Surveytype st : surveytpyeFromDb) {
			surveyNames.add(st.getName());
		}
		
		// Filling the Spinner with the names of the Surveytypes
		String[] surveyArray = surveyNames.toArray(new String[surveyNames.size()]);
		ArrayAdapter<CharSequence> spinnerAdapter1 = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, surveyArray);
		spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
		selectSurveytype.setAdapter(spinnerAdapter1);
				
		selectSurveytype.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) 
			{
				// The Categories of the selected Surveytype are loaded into the ListView
				selectedSurveytype = surveytpyeFromDb.get(position);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) 
			{
				
			}
		});
		
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
		
		FillActivityInList();
	}
	
	private void FillActivityInList()
	{
		activitiesList = new ArrayList<Activity>();
		activityList = new ArrayList<String>();
		activityAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, activityList);
		activityListView.setAdapter(activityAdapter);
	}
	
	private void AddActivityToList(Activity activity)
	{
		activitiesList.add(activity);
		activityList.add(activity.getName());
		activityAdapter.notifyDataSetChanged();
	}
}
