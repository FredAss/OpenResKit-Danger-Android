package openreskit.danger.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import openreskit.danger.R;
import openreskit.danger.adapters.ActionAdapter;
import openreskit.danger.adapters.ProtectionGoalAdapter;
import openreskit.danger.functions.MyDatePickerDialog;
import openreskit.danger.functions.Status;
import openreskit.danger.models.Action;
import openreskit.danger.models.Person;
import openreskit.danger.models.ProtectionGoal;
import openreskit.danger.models.Threat;
import openreskit.danger.models.Workplace;
import openreskit.danger.persistence.ORMLiteDBHelper;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class DangerAssessmentProtectionFragment extends Fragment 
{
	ViewGroup view;
	static final int DATE_DIALOG_ID = 0;
	private int currentYear;
	private int currentMonth;
	private int currentDay;
	Calendar c = Calendar.getInstance();
	
	Workplace workplace;

	ORMLiteDBHelper helper;
	
	Threat threat;
	
	EditText protection;
	EditText action;
	TextView protectionCount;
	TextView actionCount;
	TextView actionAppointment;
	Date actionAppointmentDate;
	AutoCompleteTextView actionResponsible;
	ImageButton actionAppointmentButton;
	Button showProtection;
	Button showActions;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		this.setHasOptionsMenu(true);
		view = (ViewGroup) inflater.inflate(R.layout.danger_assessment_protection, container, false);

		protection = (EditText)view.findViewById(R.id.protection);
		action = (EditText)view.findViewById(R.id.action);
		actionResponsible = (AutoCompleteTextView)view.findViewById(R.id.actionResponsible);
		protectionCount = (TextView)view.findViewById(R.id.protectionCount);
		actionCount = (TextView)view.findViewById(R.id.actionCount);
		actionAppointmentButton = (ImageButton)view.findViewById(R.id.actionAppointmentButton);
		actionAppointment = (TextView)view.findViewById(R.id.actionAppointment);
		showProtection = (Button)view.findViewById(R.id.btnShowProtections);
		showActions = (Button)view.findViewById(R.id.btnShowActions);
		
		helper = new ORMLiteDBHelper(getActivity());
		
		workplace = DangerAssessmentFragmentActivity.receivedWorkplace;			
		threat = DangerAssessmentFragmentActivity.threat;
			
		InitializeControls();
		
		GetCountOfProtection();
		GetCountOfAction();
		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
		inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.protection_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		
		switch(item.getItemId())
		{
			case R.id.protection_protection_action_save:
				SaveProtectionAndAction();
				Toast.makeText(getActivity(), "Die Daten wurden gespeichert.", Toast.LENGTH_LONG).show();
				return true;
				
			case R.id.protection_action_done:
				ProtectionActionDone();
				Toast.makeText(getActivity(), "Gef‰hrdungsbeurteilung f¸r " + threat.getGFactor().getNumber() + " " + threat.getGFactor().getName() + " wurde gespeichert und beendet.", Toast.LENGTH_LONG).show();
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onPause() 
	{
		SaveProtectionAndAction();	
		super.onPause();
	}

	private void SaveProtectionAndAction()
	{
		SaveProtection();
		SaveAction();
	}
	
	
	private void SaveAction() 
	{	
		if(!action.getText().toString().equals("") & !actionResponsible.getText().toString().equals("") & !actionAppointment.getText().toString().equals(""))
		{
			Person person = new Person();
			person.setId(UUID.randomUUID().toString());
			person.setName(actionResponsible.getText().toString());
			person.setCompany(workplace.getCompany());
			try
			{
				helper.getPersonDao().createIfNotExists(person);
//				ds.Open();
//				if(ds.IsPersonExistend(person) == null)
//				{
//					person = ds.InsertPersonInDb(person);
//				}
//				else
//				{
//					person = ds.IsPersonExistend(person);
//				}
//				ds.Close();
				
				Action actionForThreat = new Action();
				actionForThreat.setId(UUID.randomUUID().toString());
				actionForThreat.setDescription(action.getText().toString());
				actionForThreat.setPerson(person);
				actionForThreat.setDueDate(dTest);
				actionForThreat.setThreat(threat);
				
				try
				{
					helper.getActionDao().createIfNotExists(actionForThreat);
//					ds.Open();
//					ds.InsertActionInDb(actionForThreat);
//					ds.Close();
				}
				catch(Exception ex)
				{
					
				}
				
				GetCountOfAction();
				
				action.setText("");
				actionAppointment.setText("");
				actionResponsible.setText("");	
				
			}
			catch(Exception ex)
			{
				
			}
		}
	}

	private void SaveProtection() 
	{
		if(!protection.getText().toString().equals(""))
		{
			ProtectionGoal protectionFromThreat = new ProtectionGoal();
			protectionFromThreat.setId(UUID.randomUUID().toString());
			protectionFromThreat.setDescription(protection.getText().toString());
			protectionFromThreat.setThreat(threat);
			
			try 
			{
				helper.getProtectionGoalDao().create(protectionFromThreat);
//				ds.Open();
//				ds.InsertProtectionGoalInDb(protectionFromThreat);
//				ds.Close();
			} 
			catch (Exception ex) 
			{
				
			}
			
			protection.setText("");
			GetCountOfProtection();
		}
	}

	private void ProtectionActionDone()
	{
		Threat doneThreat = DangerAssessmentFragmentActivity.threat;
		boolean isDone = true;
		
//		try
//		{
//			
////			ds.Open();
////			isDone = ds.IsActionAndProtectionAlreadyExistend(doneThreat);
////			ds.Close();
//		}
//		catch(Exception ex)
//		{
//			
//		}
		
		if(isDone == true)
		{
			if(doneThreat.getProtectionGoals().size() == 0 | doneThreat.getActions().size() == 0 | doneThreat.getRiskDimension() == 0 | doneThreat.getRiskPossibility() == 0 | doneThreat.getDescription().equals(""))
			{
				doneThreat.setStatus(Status.In_Process.getNumericType());
			}
			else
			{
				doneThreat.setStatus(Status.Threat.getNumericType());
			}
			
			try
			{
				helper.getThreatDao().update(doneThreat);
//				ds.Open();
//				ds.UpdateThreatInDb(doneThreat);
//				ds.Close();
			}
			catch(Exception ex)
			{
				
			}		
			
			DangerAssessmentFragmentActivity.dalf.EnableExpandableListView();
			DangerAssessmentFragmentActivity.threat = doneThreat;
			DangerAssessmentFragmentActivity.dalf.UpdateThreatInDB(doneThreat);
			
			FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.dangerWorkspace, DangerAssessmentFragmentActivity.daq);
			transaction.commit();	
			
			DangerAssessmentFragmentActivity.dap.SetEnabledStatusOfButtons(false, true, true);
		}
		else
		{
			Context context = getActivity().getApplicationContext();
			CharSequence text = "Bitte tragen Sie mindestens ein Schutzziel und mindestens eine Maﬂnahme ein.";
			int duration = Toast.LENGTH_SHORT;
			
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}

	private void InitializeControls()
	{		
		showProtection.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{			
				AlertDialog.Builder builder = new Builder(getActivity());
				builder.setTitle("Schutzziele");
				
				List<ProtectionGoal> pg = new ArrayList<ProtectionGoal>();
				
				try
				{
					pg = helper.getProtectionGoalDao().queryForEq("threat_id", threat.getId());
				}
				catch(Exception ex)
				{
					
				}
				
				ListView t = new ListView(getActivity());
				ArrayAdapter<ProtectionGoal> protectionGoal = new ProtectionGoalAdapter(getActivity(),R.layout.listitem_layout_protectiongoal , pg);
				t.setAdapter(protectionGoal);
				builder.setView(t);
				final Dialog dialog = builder.create();
				dialog.show();
			}
		});
		
		showActions.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				AlertDialog.Builder builder = new Builder(getActivity());
				builder.setTitle("Maﬂnahmen");
				
				List<Action> a = new ArrayList<Action>();
				
				try
				{
					a = helper.getActionDao().queryForEq("threat_id", threat.getId());
				}
				catch(Exception ex)
				{
					
				}
				
				ListView t = new ListView(getActivity());
				ArrayAdapter<Action> actionAdapter = new ActionAdapter(getActivity(), R.layout.listitem_layout_action, a);
				t.setAdapter(actionAdapter);
				builder.setView(t);
				final Dialog dialog = builder.create();
				dialog.show();
			}
		});
		
		actionAppointmentButton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				onCreateDialog(DATE_DIALOG_ID);	
			}
		});
		
		/*actionResponsible.addTextChangedListener(new TextWatcher() 
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				String searchText = actionResponsible.getText().toString();
				
				if(!searchText.equals(""))
				{
					List<Person> resultingPerson = new ArrayList<Person>();
					
					try
					{
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("Name", searchText);
						map.put("company_id", workplace.getCompany().getId());
						resultingPerson = helper.getPersonDao().queryForFieldValues(map);
//						ds.Open();
//						resultingPerson = ds.GetAllPersonFromDb(searchText, workplace.getCompany());
//						ds.Close();	
					}
					catch(Exception ex)
					{
						
					}
					
					List<String> searchString = new ArrayList<String>();
					
					for (Person person : resultingPerson) 
					{
						searchString.add(person.getName());
					}
					
					ArrayAdapter<String> searchAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, searchString);
					actionResponsible.setAdapter(searchAdapter);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) 
			{
				
			}
			
			@Override
			public void afterTextChanged(Editable s) 
			{
				
			}
		});*/
	}
	
	private void GetCountOfProtection()
	{
		int count = 0;
		
		try
		{
			count = helper.getProtectionGoalDao().queryForEq("threat_id", threat.getId()).size();
//			ds.Open();
//			count = ds.GetCountOfProtectionGoalsOfThreat(threat);
//			ds.Close();
		}
		catch(Exception ex)
		{
			
		}
		
		protectionCount.setText("Schutzziele (Eingetragen: " + count + ")");
	}
	
	private void GetCountOfAction()
	{
		int count = 0;
		
		try
		{
			count = helper.getActionDao().queryForEq("threat_id", threat.getId()).size();
//			ds.Open();
//			count = ds.GetCountOfActionOfThreat(threat);
//			ds.Close();
		}
		catch(Exception ex)
		{
			
		}
		
		actionCount.setText("Maﬂnahme (Eingetragen: " + count + ")");
	}
	
	protected void onCreateDialog(int id)
	{
		switch (id) 
		{
			case DATE_DIALOG_ID:
				MyDatePickerDialog mdpd = new MyDatePickerDialog(getActivity(), ActionDate, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				mdpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Fertig", mdpd); 
				mdpd.setPermantentTitle("Terminauswahl");
				mdpd.show();
		}
	}
	
	Date dTest = new Date();
	
	
	private DatePickerDialog.OnDateSetListener ActionDate = new DatePickerDialog.OnDateSetListener() 
	{
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
		{	
			
			dTest.setDate(dayOfMonth);
			dTest.setMonth(monthOfYear + 1);
			dTest.setYear(year-1900);
			
			currentYear = year;
			currentMonth = monthOfYear + 1;
			currentDay = dayOfMonth;
			actionAppointment.setText(currentDay + "." + currentMonth + "." + currentYear);
		}
	};
}
