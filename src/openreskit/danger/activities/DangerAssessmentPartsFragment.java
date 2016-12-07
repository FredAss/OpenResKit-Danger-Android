package openreskit.danger.activities;

import openreskit.danger.R;

import openreskit.danger.interfaces.OnButtonPressListener;
import openreskit.danger.models.Workplace;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DangerAssessmentPartsFragment extends Fragment 
{
	private ViewGroup view;
	Workplace workplace;
	OnButtonPressListener buttonListener;
	
	TextView workplaceName;
	Button dangerDescription;
	Button dangerRisk;
	Button dangerProtection;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{	
		view = (ViewGroup) inflater.inflate(R.layout.danger_assessment_parts, container, false);
		
		dangerDescription = (Button)view.findViewById(R.id.DangerDescriptionButton);
		dangerRisk = (Button)view.findViewById(R.id.DangerRiskButton);
		dangerProtection = (Button)view.findViewById(R.id.DangerProtectionButton);
		workplaceName = (TextView)view.findViewById(R.id.DangerWorkplaceName);
		
		SetEnabledStatusOfButtons(false, true, true);
		
		workplace = DangerAssessmentFragmentActivity.receivedWorkplace;
		
		workplaceName.setText(workplace.getName());
		
		InitializeControls();
		
		return view;
	}

	private void InitializeControls()
	{
		dangerDescription.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				buttonListener.OnButtonPressed(view.findViewById(R.id.DangerDescriptionButton));
				SetEnabledStatusOfButtons(false, true, true);
			}
		});
		
		dangerRisk.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				buttonListener.OnButtonPressed(view.findViewById(R.id.DangerRiskButton));
				SetEnabledStatusOfButtons(true, false, true);
			}
		});
		
		dangerProtection.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				buttonListener.OnButtonPressed(view.findViewById(R.id.DangerProtectionButton));
				SetEnabledStatusOfButtons(true, true, false);
			}
		});
	}
	
	public void SetEnabledStatusOfButtons(boolean description, boolean risk, boolean protection)
	{
		dangerDescription.setEnabled(description);
		dangerRisk.setEnabled(risk);
		dangerProtection.setEnabled(protection);
		
		if(description == false)
		{
			dangerDescription.setTypeface(Typeface.DEFAULT_BOLD);
			dangerDescription.setBackgroundColor(Color.parseColor("#33B5E5"));
			dangerProtection.setTypeface(Typeface.DEFAULT);
			dangerProtection.setBackgroundColor(Color.parseColor("#222222"));
			dangerRisk.setTypeface(Typeface.DEFAULT);
			dangerRisk.setBackgroundColor(Color.parseColor("#222222"));
		}
		else if(risk == false)
		{
			dangerDescription.setTypeface(Typeface.DEFAULT);
			dangerDescription.setBackgroundColor(Color.parseColor("#222222"));
			dangerProtection.setTypeface(Typeface.DEFAULT);
			dangerProtection.setBackgroundColor(Color.parseColor("#222222"));
			dangerRisk.setTypeface(Typeface.DEFAULT_BOLD);
			dangerRisk.setBackgroundColor(Color.parseColor("#33B5E5"));
		}
		else if(protection == false)
		{
			dangerDescription.setTypeface(Typeface.DEFAULT);
			dangerDescription.setBackgroundColor(Color.parseColor("#222222"));
			dangerProtection.setTypeface(Typeface.DEFAULT_BOLD);
			dangerProtection.setBackgroundColor(Color.parseColor("#33B5E5"));
			dangerRisk.setTypeface(Typeface.DEFAULT);
			dangerRisk.setBackgroundColor(Color.parseColor("#222222"));
		}
	}
	
	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);		
		try 
		{
			buttonListener = (OnButtonPressListener) getActivity();
		}
		catch (ClassCastException ex) 
		{
			throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
		}
	}
}
