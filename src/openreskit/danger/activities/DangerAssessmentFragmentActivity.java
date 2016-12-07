package openreskit.danger.activities;

import java.util.List;
import openreskit.danger.R;
import openreskit.danger.functions.Serialization;
import openreskit.danger.functions.Status;
import openreskit.danger.interfaces.OnButtonPressListener;
import openreskit.danger.interfaces.OnButtonSaveClickListener;
import openreskit.danger.interfaces.OnGFactorListClickListener;
import openreskit.danger.interfaces.OnNeedForActionButtonClickListener;
import openreskit.danger.models.Assessment;
import openreskit.danger.models.QuestionWrapper;
import openreskit.danger.models.Threat;
import openreskit.danger.models.Workplace;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Toast;

public class DangerAssessmentFragmentActivity extends FragmentActivity implements OnButtonPressListener, OnGFactorListClickListener, OnNeedForActionButtonClickListener, OnButtonSaveClickListener
{
	static DangerAssessmentPartsFragment dap;
	static DangerAssessmentListFragment dalf;
	static DangerAssessmentQuestionsFragment daq;
		
	static Workplace receivedWorkplace;
	static Assessment assassment;
	static Threat threat;
	static List<QuestionWrapper> questions;
	static List<QuestionWrapper> allQuestions;
	
	FragmentManager fm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.danger_assessment);
		
		receivedWorkplace = (Workplace)Serialization.DeserilizeObject(getIntent().getByteArrayExtra("SelectedWorkplace")); 
		assassment = (Assessment)Serialization.DeserilizeObject(getIntent().getByteArrayExtra("Assessment"));
		
		threat = new Threat();
		
		fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();

		dalf = new DangerAssessmentListFragment();
		daq = new DangerAssessmentQuestionsFragment();
		dap = new DangerAssessmentPartsFragment();
		
		transaction.replace(R.id.dangerCategories, dalf);
		transaction.replace(R.id.dangerWorkspace, daq);
		transaction.replace(R.id.dangerAssessmentsParts, dap);
		transaction.commit();
	}

	@Override
	protected void onPause() 
	{
		super.onPause();
		
		allQuestions = null;
		questions = null;
	}

	@Override
	public void OnButtonPressed(View view) 
	{
		Fragment newFragment;
		if(view == findViewById(R.id.DangerDescriptionButton))
		{
			dalf.EnableExpandableListView();
			newFragment = daq;	
		}
		else if(view == findViewById(R.id.DangerRiskButton))
		{
			dalf.DisableExpandableListView();
			newFragment = new DangerAssessmentRiskFragment();
		}
		else if(view == findViewById(R.id.DangerProtectionButton))
		{
			dalf.DisableExpandableListView();
			newFragment = new DangerAssessmentProtectionFragment();
		}
		else
		{
			newFragment = null;
		}
		
		//CreateKeyDownMethode(newFragment);
		
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.dangerWorkspace, newFragment, "FRAGMENT_TAG");
		transaction.commit();
	}
	
	
	
	
	/*private void CreateKeyDownMethode(final Fragment newFragment)
	{
		if(newFragment instanceof DangerAssessmentRiskFragment)
		{
		
		newFragment.getView().setOnKeyListener(new OnKeyListener() 
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) 
			{
				if(keyCode == KeyEvent.KEYCODE_BACK)
				{

						((DangerAssessmentRiskFragment) newFragment).SaveThreat();
						return true;
					
				}
				return false;
			}
		});
		}
	}*/
	
	
	/*@Override
	public void onBackPressed() 
	{
		Fragment a = fm.findFragmentByTag("FRAGMENT_TAG");
		
		if(a instanceof DangerAssessmentRiskFragment)
		{
			((DangerAssessmentRiskFragment) a).SaveThreat();
		}
		
		super.onBackPressed();
	}*/

	@Override
	public void OnFactorListClicked(Threat threat) 
	{
		daq.ReloadQuestionList(threat);
	}

	@Override
	public void OnNeedForActionClicked(Threat gfactor, List<QuestionWrapper> question)
	{
		// Checks the stuatus of the selected Threat
		if(gfactor.getStatus() == Status.No_Threat.getNumericType() || gfactor.getStatus() == Status.Threat.getNumericType())
		{
			// Information that a new Threat should be added
			Context context = this.getApplicationContext();
			CharSequence text = "Dieser Gefährdung wurde bereits ein Handlungsbedarf festgestellt. Erzeugen Sie eine Kopie dieser Gefährdung, um einen weiteren Handlungsbedarf festzustellen.";
			int duration = Toast.LENGTH_LONG;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		else if(gfactor.getStatus() == Status.Not_Finished.getNumericType() | gfactor.getStatus() == Status.Not_Relevant.getNumericType())
		{				
			// Start DangerAssessmentRiskFragment and passing the selected Threat
			Fragment dar = new DangerAssessmentRiskFragment();
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			//CreateKeyDownMethode(dar);
			transaction.replace(R.id.dangerWorkspace, dar, "FRAGMENT_TAG");
			transaction.commit();
			
			dap.SetEnabledStatusOfButtons(true, false, true);
			
			((DangerAssessmentRiskFragment) dar).PrepareValuesFromThreat(gfactor, question, assassment);
		}
	}

	@Override
	public void OnSaveClickListener() 
	{
		Fragment dapo = new DangerAssessmentProtectionFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.dangerWorkspace, dapo);
		transaction.commit();
		
		dap.SetEnabledStatusOfButtons(true, true, false);
	}
}
