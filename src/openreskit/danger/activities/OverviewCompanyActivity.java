package openreskit.danger.activities;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.kobjects.mime.Decoder;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import openreskit.danger.R;
import openreskit.danger.adapters.ActionAdapter;
import openreskit.danger.adapters.ImageAdapter;
import openreskit.danger.adapters.ProtectionGoalAdapter;
import openreskit.danger.adapters.WorkplaceThreatExpandableAdapter;
import openreskit.danger.functions.RiskGroupCalculator;
import openreskit.danger.functions.Status;
import openreskit.danger.models.Action;
import openreskit.danger.models.Assessment;
import openreskit.danger.models.Company;
import openreskit.danger.models.Picture;
import openreskit.danger.models.ProtectionGoal;
import openreskit.danger.models.Threat;
import openreskit.danger.models.Workplace;

import openreskit.danger.persistence.ORMLiteDBHelper;
import openreskit.danger.ui.HorizontialListView;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class OverviewCompanyActivity extends OrmLiteBaseActivity<ORMLiteDBHelper>
{
	
	Context c;
	
	ArrayAdapter<Picture> adPicture;
	ArrayAdapter<ProtectionGoal> adProtectionGoal;
	ArrayAdapter<Action> adAction;

	ExpandableListView WorkplaceThreatListView;
	TextView companyName;
	TextView threatDescription;
	TextView threatRiskgroup;
	ImageView threatPic;
	
	HashMap<Workplace, List<Threat>> Child;
	List<Workplace> Parent;

	HorizontialListView threatPicListView;
	ListView threatProtectionListView;
	ListView threatActionListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.overview_company_layout);
		
		c = this;
		WorkplaceThreatListView = (ExpandableListView)findViewById(R.id.overviewExpWorkplaceThreatListView);
		companyName = (TextView)findViewById(R.id.overviewCompanyName);
		threatDescription = (TextView)findViewById(R.id.overviewDescription);
		threatRiskgroup = (TextView)findViewById(R.id.overviewRiskgroup);
		threatPic = (ImageView)findViewById(R.id.overviewImage);
		threatPicListView = (HorizontialListView)findViewById(R.id.overviewPicList);
		threatProtectionListView = (ListView)findViewById(R.id.overviewProtections);
		threatActionListView = (ListView)findViewById(R.id.overviewActions);
		
		//ds = new DataSource(this);
		
		Company company = (Company) getIntent().getExtras().getSerializable("Company");
		
		companyName.setText(company.getName());
		
		FillExpandableList(company);
		
		InitializeControls();
	}

	private void InitializeControls() 
	{
		WorkplaceThreatListView.setOnChildClickListener(new OnChildClickListener() 
		{
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) 
			{
				Threat selectedThreat = (Threat)((WorkplaceThreatExpandableAdapter)WorkplaceThreatListView.getExpandableListAdapter()).getChild(groupPosition, childPosition);

				LoadDataOfThreat(selectedThreat);
				
				int index  = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
				parent.setItemChecked(index, true);
				
				return true;
			}
		});
		
		threatPicListView.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) 
			{

			byte[] decodedByte = Base64.decode(((Picture) threatPicListView.getAdapter().getItem(position)).getPic(), 0);
		    threatPic.setImageBitmap(BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length));
			//threatPic.setImageBitmap(((Picture) threatPicListView.getAdapter().getItem(position)).getPic());				
			}
		});		
	}

	protected void LoadDataOfThreat(Threat selectedThreat)
	{
		LoadDescriptionsOfThreat(selectedThreat);
		LoadPicturesOfThreat(selectedThreat);
		LoadProtectionsOfThreat(selectedThreat);
		LoadActionsOfThreat(selectedThreat);
	}

	private void LoadDescriptionsOfThreat(Threat selectedThreat) 
	{
		if(!selectedThreat.getDescription().equals(""))
			threatDescription.setText(selectedThreat.getDescription());
		else
			threatDescription.setText("Keine Beschreibung");
		
		if(!(selectedThreat.getRiskDimension() == 5) | !(selectedThreat.getRiskPossibility() == 5))
			threatRiskgroup.setText("Risikogruppe: " + String.valueOf(RiskGroupCalculator.CalculateRiskGroupe()[selectedThreat.getRiskPossibility()][selectedThreat.getRiskDimension()]));
		else
			threatRiskgroup.setText("Risikogruppe: Keine Risikogruppe");
	}
	
	private void LoadPicturesOfThreat(Threat selectedThreat) 
	{
		List<Picture> pictureList = new ArrayList<Picture>();
		
		try
		{
			//ds.Open();
			pictureList = getHelper().getPictureDao().queryForEq("threat_id", selectedThreat.getId());
					//ds.GetAllPicturesFromThreat(selectedThreat);
			//ds.Close();
		}
		catch(Exception ex)
		{
			
		}
		
		if(pictureList.size() != 0)
		{
			
			byte[] decodedByte = Base64.decode(pictureList.get(0).getPic(), 0);
		    threatPic.setImageBitmap(BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length));
			
			//threatPic.setImageBitmap(pictureList.get(0).getPic());
			
			adPicture = new ImageAdapter(c, R.layout.listitem_layout_image, pictureList);
			threatPicListView.setAdapter(adPicture);
		}
		else
		{
			adPicture = new ImageAdapter(c, R.layout.listitem_layout_image, pictureList);
			threatPicListView.setAdapter(adPicture);
			threatPic.setImageBitmap(null);
		}
	}
	

	private void LoadProtectionsOfThreat(Threat selectedThreat) 
	{
		if(!(selectedThreat.getRiskDimension() == 0) && !(selectedThreat.getRiskPossibility() == 0))
		{
			List<ProtectionGoal> listOfprotectiongoals = new ArrayList<ProtectionGoal>();
			
			try
			{
				listOfprotectiongoals = getHelper().getProtectionGoalDao().queryForEq("threat_id", selectedThreat.getId());
				//listOfprotectiongoals = ds.GetAllProtectionGoalsFromDb(selectedThreat);
				
			}
			catch(Exception ex)
			{
				
			}
			
			adProtectionGoal = new ProtectionGoalAdapter(c, R.layout.listitem_layout_protectiongoal, listOfprotectiongoals);
			threatProtectionListView.setAdapter(adProtectionGoal);
		}
		else
		{
			List<ProtectionGoal> listOfprotectiongoals = new ArrayList<ProtectionGoal>();
			adProtectionGoal = new ProtectionGoalAdapter(c, R.layout.listitem_layout_protectiongoal, listOfprotectiongoals);
			threatProtectionListView.setAdapter(adProtectionGoal);
		}
	}
		
	private void LoadActionsOfThreat(Threat selectedThreat) 
	{
		if(!(selectedThreat.getRiskDimension() == 0) && !(selectedThreat.getRiskPossibility() == 0))
		{
			List<Action> listOfactions = new ArrayList<Action>();
				
			try
			{
				listOfactions = getHelper().getActionDao().queryForEq("threat_id", selectedThreat.getId());
				//ds.Open();
				//listOfactions = ds.GetAllActionGoalsFromDb(selectedThreat);
				//ds.Close();
			}
			catch(Exception ex)
			{
					
			}
			
			adAction = new ActionAdapter(c, R.layout.listitem_layout_action, listOfactions);
			threatActionListView.setAdapter(adAction);
		}	
		else
		{
			List<Action> listOfactions = new ArrayList<Action>();
			adAction = new ActionAdapter(c, R.layout.listitem_layout_action, listOfactions);
			threatActionListView.setAdapter(adAction);
		}
	}

	private void FillExpandableList(Company company) 
	{
		try
		{
			Parent = getHelper().getWorkplaceDao().queryForEq("company_id", company.getId());
			//ds.Open();
			//Parent = ds.GetAllWorkplacesFromCompanyFromDb(company);
			//ds.Close();
		}
		catch(Exception ex)
		{
			
		}
		
		List<Assessment> assessmentList = new ArrayList<Assessment>();
		Child = new HashMap<Workplace, List<Threat>>();
		
		for (Workplace workplace2 : Parent) {
			try
			{
				assessmentList.addAll((getHelper().getAssessmentDao().queryForEq("workplace_id", workplace2.getId())));
				//ds.Open();
				//assessmentList.add(ds.GetAssessmentFromWorkplace(workplace2));
				//ds.Close();
			}
			catch(Exception ex)
			{
				
			}
		}
		
		for (Assessment assessment : assessmentList) 
		{
			List<Threat> threatOfAssessment = new ArrayList<Threat>();
			
			Workplace w = new Workplace();
			
			for(int i = 0; i < Parent.size(); i++)
			{
				String a = Parent.get(i).getId();
				
				if(assessment.getWorkplace().getId().equals(Parent.get(i).getId()))
				{
					w = Parent.get(i);
					break;
				}
			}
			
			List<Threat> alThr = new ArrayList<Threat>();
			
			try
			{
				//Cursor cursor = database.rawQuery("SELECT * FROM Threat WHERE Threat_Assessment_FK = " + assassment.GetId() + " AND Threat_Status != 'nicht relevant' AND Threat_Status != 'nicht bearbeitet' GROUP BY Threat_Gfactor_FK, THREAT_ID;", null);
				String a = "";
				
				String b = a + "";
				//List<Threat> allThreats = getHelper().getThreatDao().queryForEq("assessment_id", assessment.getId());
				
				//alThr = getHelper().getThreatDao().queryBuilder().where().eq("assassment_id", assessment.getId()).and().eq("status", Status.Threat.getNumericType()).query();
				Where where = getHelper().getThreatDao().queryBuilder().where();
				
				alThr = where.and(where.eq("assessment_id", assessment.getId()), where.eq("Status", Status.Threat.getNumericType())).query();
				
				
				
				/*for(Threat obj : allThreats)
					   if (obj.getStatus() == Status.Threat.getNumericType()){
						   threatOfAssessment.add(obj);
					   }*/
				
//				ds.Open();
//				threatOfAssessment = ds.GetAllThreatRegardingStatusFromDb(assessment);
//				ds.Close();
			}
			catch(SQLException ex)
			{
				Log.i("DB-Abfrage", ex.toString());
			}
			
			Child.put(w, alThr);
		}
		
		ExpandableListAdapter adWorkplaceThreat = new WorkplaceThreatExpandableAdapter(c, Parent, Child);
		WorkplaceThreatListView.setAdapter(adWorkplaceThreat);	
	}
}
