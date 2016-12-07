package openreskit.danger.adapters;

import java.util.List;
import openreskit.danger.R;
import openreskit.danger.models.QuestionWrapper;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class QuestionAdapter extends ArrayAdapter<QuestionWrapper> 
{
	private List<QuestionWrapper> questions;
	
	public QuestionAdapter(Context context, int textViewResourceId, List<QuestionWrapper> questions) 
	{
		super(context, textViewResourceId, questions);
		this.questions = questions;
	}
	
	public List<QuestionWrapper> getList()
	{
		return questions;
	}
	
	private class ViewHolder
	{
		TextView questionPlace;
		CheckBox positive;
		CheckBox negative;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		final ViewHolder viewHolder;

		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.listitem_layout_question, null);
			
			viewHolder = new ViewHolder();
			viewHolder.questionPlace = (TextView)convertView.findViewById(R.id.questionItem);
			viewHolder.positive = (CheckBox)convertView.findViewById(R.id.positiveAnswer);
			viewHolder.negative = (CheckBox)convertView.findViewById(R.id.negativeAnswer);

			convertView.setTag(viewHolder);
			convertView.setTag(R.id.questionItem, viewHolder.questionPlace);
			convertView.setTag(R.id.positiveAnswer, viewHolder.positive);
			convertView.setTag(R.id.negativeAnswer, viewHolder.negative);
		}
		else
		{
			viewHolder = (ViewHolder)convertView.getTag();	
		}
		
		viewHolder.positive.setTag(position);
		viewHolder.negative.setTag(position);
		viewHolder.questionPlace.setTag(position);
		
		viewHolder.questionPlace.setText(questions.get(position).getQuestion().getQuestionName());
		viewHolder.negative.setChecked(questions.get(position).isNegative());
		viewHolder.positive.setChecked(questions.get(position).isPositive());
		
		viewHolder.positive.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				int getPosition = (Integer)buttonView.getTag();
				
				if(buttonView.isChecked() == true)
				{
					questions.get(getPosition).setPositive(buttonView.isChecked());	
					viewHolder.negative.setChecked(false);
				}
				else
					questions.get(getPosition).setPositive(buttonView.isChecked());	
			}
		});
		
		convertView.setTag(R.id.positiveAnswer, viewHolder.positive);
		
		viewHolder.negative.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				int getPosition = (Integer)buttonView.getTag();
				
				if(buttonView.isChecked() == true)
				{
					questions.get(getPosition).setNegative(buttonView.isChecked());		
					viewHolder.positive.setChecked(false);
				}
				else
					questions.get(getPosition).setNegative(buttonView.isChecked());	
			}
		});
		
		convertView.setTag(R.id.negativeAnswer, viewHolder.negative);
				
		return convertView;
	}
}
