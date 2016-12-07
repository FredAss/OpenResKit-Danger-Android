package openreskit.danger.activities;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import openreskit.danger.R;
import openreskit.danger.adapters.QuestionAdapter;
import openreskit.danger.functions.Status;
import openreskit.danger.interfaces.OnNeedForActionButtonClickListener;
import openreskit.danger.models.Category;
import openreskit.danger.models.Question;
import openreskit.danger.models.QuestionWrapper;
import openreskit.danger.models.Threat;
import openreskit.danger.models.Workplace;

import openreskit.danger.persistence.ORMLiteDBHelper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class DangerAssessmentQuestionsFragment extends Fragment 
{
	ViewGroup view;
	
	
	Threat selectedGfactor;
	Workplace receivedWorkplace;
	List<String> TestErgebnisse;
	List<String> questionsTest;
	List<QuestionWrapper> questionWrapperList;
	List<QuestionWrapper> partOfQuestionWrapperList;
	List<Threat> listOfAllThreat;
	ArrayAdapter<QuestionWrapper> TestAdapter;
	boolean isAlreadyExistend;
	
	OnNeedForActionButtonClickListener needForActionListener;
	
	ORMLiteDBHelper helper;
	
	ListView questionList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		this.setHasOptionsMenu(true);
		view = (ViewGroup) inflater.inflate(R.layout.danger_assessment_questions, container, false);
		
		questionList = (ListView)view.findViewById(R.id.QuestionsList);
		
		helper = new ORMLiteDBHelper(getActivity());
		
		receivedWorkplace = DangerAssessmentFragmentActivity.receivedWorkplace;
		
		CheckIfAlreadyExistend();
		
		selectedGfactor = DangerAssessmentFragmentActivity.threat;
					
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		
		try
		{
			needForActionListener = (OnNeedForActionButtonClickListener)getActivity();
		}
		catch(Exception ex)
		{
			
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
		inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.question_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		switch(item.getItemId())
		{
		case R.id.question_new_threat:
			CreateNewThreat();
			Toast.makeText(getActivity(), "Es wurde eine neue Gefährdung für den GFaktor " + selectedGfactor.getGFactor().getNumber() + " " + selectedGfactor.getGFactor().getName() + " angelegt und gespeichert.", Toast.LENGTH_LONG).show();
			return true;
			
		case R.id.question_noneed:
			NoNeedForAction();
			return true;
			
		case R.id.question_need:
			NeedForAction();
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void NoNeedForAction()
	{
		boolean notDone = false;
		
		for(int i = 0; i < DangerAssessmentFragmentActivity.questions.size(); i++)
		{
			if(DangerAssessmentFragmentActivity.questions.get(i).getQuestionStatus().equals("IsNotOkay"))
			{
				notDone = true;
				break;
			}
		}
		
		if(notDone == true && !(selectedGfactor.getStatus() == Status.No_Threat.getNumericType()))
		{
			Context context = getActivity().getApplicationContext();
			CharSequence text = "Ein Risiko wurde erkannt. Bitte beschreiben und bewerten Sie das Risiko";
			int duration = Toast.LENGTH_LONG;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			NeedForAction();
		}
		else if(selectedGfactor.getStatus() == Status.No_Threat.getNumericType())
		{
			// Information that a new Threat should be added
			Context context = getActivity().getApplicationContext();
			CharSequence text = "Dieser Gefährdung wurde bereits ein Handlungsbedarf festgestellt. Erzeugen Sie eine Kopie dieser Gefährdung, um einen weiteren Handlungsbedarf festzustellen.";
			int duration = Toast.LENGTH_LONG;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		else
		{
			Threat doneThreat = selectedGfactor;
			doneThreat.setStatus(Status.No_Threat.getNumericType());
			
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
			
			DangerAssessmentFragmentActivity.allQuestions = questionWrapperList;
			DangerAssessmentFragmentActivity.questions = partOfQuestionWrapperList;
			TranslateListQuestionsToAdapterValues(doneThreat);
			DangerAssessmentFragmentActivity.dalf.UpdateThreatInDB(doneThreat);		
		}
	}
	
	private void NeedForAction()
	{
	
		
		DangerAssessmentFragmentActivity.dalf.DisableExpandableListView();
		
		DangerAssessmentFragmentActivity.allQuestions = questionWrapperList;
		DangerAssessmentFragmentActivity.questions = partOfQuestionWrapperList;
		
		TranslateListQuestionsToAdapterValues(selectedGfactor);
		needForActionListener.OnNeedForActionClicked(selectedGfactor, partOfQuestionWrapperList);
	}
	
	private void CreateNewThreat()
	{
		Threat newThreat = new Threat();
		newThreat.setId(UUID.randomUUID().toString());
		newThreat.setDescription("");
		newThreat.setRiskDimension(0);
		newThreat.setRiskPossibility(0);
		newThreat.setStatus(Status.Not_Finished.getNumericType());
		newThreat.setAssessment(DangerAssessmentFragmentActivity.assassment);
		newThreat.setGFactor(selectedGfactor.getGFactor());
		
		try
		{
			helper.getThreatDao().create(newThreat);
//			ds.Open();
//			newThreat = ds.InsertThreatInDb(newThreat);
//			ds.Close();
		}
		catch(Exception ex)
		{
			
		}
		
		UpdateQuestionWrapperList(newThreat, selectedGfactor);
		DangerAssessmentFragmentActivity.dalf.AddThreatInDB(newThreat);
		DangerAssessmentFragmentActivity.dalf.EnableExpandableListView();
	}
		

	
	private void CheckIfAlreadyExistend()
	{
		if(DangerAssessmentFragmentActivity.allQuestions == null && isAlreadyExistend == false)
		{
			isAlreadyExistend = true;
			listOfAllThreat = new ArrayList<Threat>();
			partOfQuestionWrapperList = new ArrayList<QuestionWrapper>();
			
			listOfAllThreat = DangerAssessmentFragmentActivity.dalf.listOfThreats;
			questionWrapperList = new ArrayList<QuestionWrapper>();
			try
			{
				for (Threat threat : listOfAllThreat) 
				{
					List<Question> questionsList = new ArrayList<Question>();
					questionsList.addAll(helper.getQuestionDao().queryForEq("gfactor_id", threat.getGFactor().getId()));

					for (Question question: questionsList) {
						QuestionWrapper wrapper = new QuestionWrapper(question.getId(), "", question, threat);
						questionWrapperList.add(wrapper);
					}
				}
			
//				
//				questionWrapperList = ds.GetAllQuestionFromDb(listOfAllThreat);
//				ds.Close();
			}
			catch(Exception ex)
			{
				
			}
			
			List<QuestionWrapper> pqwl = new ArrayList<QuestionWrapper>();
			
			for (QuestionWrapper questionWrapper : questionWrapperList) {
				if(DangerAssessmentFragmentActivity.threat.getId() == questionWrapper.getThreat().getId())
					pqwl.add(questionWrapper);
			}
			
			partOfQuestionWrapperList = pqwl;
			
			DangerAssessmentFragmentActivity.allQuestions = questionWrapperList;
			DangerAssessmentFragmentActivity.questions = pqwl;
		}
		else if(DangerAssessmentFragmentActivity.allQuestions != null)
		{
			questionWrapperList = DangerAssessmentFragmentActivity.allQuestions;
			partOfQuestionWrapperList = DangerAssessmentFragmentActivity.questions;
			ReloadQuestionList(selectedGfactor);
		}
		
		TestAdapter = new QuestionAdapter(getActivity(), R.layout.listitem_layout_question, partOfQuestionWrapperList);
		questionList.setAdapter(TestAdapter);
	}
	
	void ReloadQuestionList(Threat gfacFatctor)
	{
		TranslateListQuestionsToAdapterValues(selectedGfactor);
		
		List<QuestionWrapper> pqwl = new ArrayList<QuestionWrapper>();
		
		for (QuestionWrapper questionWrapper : questionWrapperList) 
		{
			QuestionWrapper qwTest = questionWrapper;
			Threat threatTest = gfacFatctor;
						
			if(gfacFatctor.getId() == questionWrapper.getThreat().getId())
				pqwl.add(questionWrapper);
		}
		
		selectedGfactor = gfacFatctor;
				
		questionList.setAdapter(null);
		Context c = getActivity();
		TestAdapter = new QuestionAdapter(c, R.layout.listitem_layout_question, pqwl);
		questionList.setAdapter(TestAdapter);		
		
		DangerAssessmentFragmentActivity.questions = pqwl;
	}
		
	private void TranslateListQuestionsToAdapterValues(Threat selectedThreat)
	{
		List<QuestionWrapper> listFromAdapter = ((QuestionAdapter)TestAdapter).getList();
		
		for(int i = 0; i < listFromAdapter.size(); i++)
		{
			for(int j = 0; j < questionWrapperList.size(); j++)
			{
				if(listFromAdapter.get(i).getThreat().getId() == questionWrapperList.get(j).getThreat().getId() && listFromAdapter.get(i).getQuestion().getId() == questionWrapperList.get(j).getQuestion().getId())
				{
					questionWrapperList.get(j).setQuestionStatus(listFromAdapter.get(i).getQuestionStatus());
				}
			}
		}

		partOfQuestionWrapperList = listFromAdapter;
		DangerAssessmentFragmentActivity.questions = partOfQuestionWrapperList;
	}
	
	public void UpdateQuestionWrapperList(Threat newThreat, Threat copiedThreat)
	{
		List<QuestionWrapper> newQuestionWrappers = new ArrayList<QuestionWrapper>();
		List<Threat> gfactorList = new ArrayList<Threat>();
		gfactorList.add(newThreat);
		List<Question> questionsList = new ArrayList<Question>();
		try
		{
			for (Threat threat : gfactorList) {
				
				questionsList.addAll(helper.getQuestionDao().queryForEq("gfactor_id", threat.getGFactor().getId()));
				for (Question question: questionsList) {
					QuestionWrapper wrapper = new QuestionWrapper(question.getId(), "", question, threat);
					questionWrapperList.add(wrapper);
				}
			}
			
			
//			ds.Open();
//			newQuestionWrappers = ds.GetAllQuestionFromDb(gfactorList);
//			ds.Close();
		}
		catch(Exception ex)
		{
			
		}
		
		List<Integer> qwPosition = new ArrayList<Integer>();
		
		for(int i = 0; i < questionWrapperList.size(); i++)
		{
			if(questionWrapperList.get(i).getThreat().getId() == copiedThreat.getId())
			{
				qwPosition.add(i);
			}
		}
		
		int position = qwPosition.get(qwPosition.size() -1);
		
		for(int i = 0; i < newQuestionWrappers.size(); i++)
		{
			questionWrapperList.add(position + 1, newQuestionWrappers.get(i));
			position += 1;
		}
		
		DangerAssessmentFragmentActivity.allQuestions = questionWrapperList;
	}
}
