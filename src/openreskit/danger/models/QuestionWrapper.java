package openreskit.danger.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class QuestionWrapper implements Serializable 
{
	private int Id = 0;
	private String QuestionStatus = "";
	private Question Question = null;
	private Threat Threat = null;
	private boolean isNegative = false;
	private boolean isPositive = false;
	
	public QuestionWrapper(int id, String questionStatus, openreskit.danger.models.Question question, Threat threat) 
	{
		super();
		Id = id;
		QuestionStatus = questionStatus;
		Question = question;
		Threat = threat;
	}

	public QuestionWrapper() 
	{
		super();
	}

	public int getId() 
	{
		return Id;
	}

	public void setId(int id) 
	{
		Id = id;
	}

	public String getQuestionStatus() 
	{
		return QuestionStatus;
	}

	public void setQuestionStatus(String questionStatus) 
	{
		QuestionStatus = questionStatus;
	}

	public Question getQuestion() 
	{
		return Question;
	}

	public void setQuestion(Question question) 
	{
		Question = question;
	}

	public Threat getThreat() 
	{
		return Threat;
	}

	public void setThreat(Threat threat) 
	{
		Threat = threat;
	}

	public boolean isNegative() 
	{
		return isNegative;
	}

	public void setNegative(boolean isNegative) 
	{
		this.isNegative = isNegative;
		this.isPositive = false;
		if(isNegative == true)
		{
			setQuestionStatus("IsNotOkay");
		}
		else if(isPositive == false && isNegative == false)
		{
			setQuestionStatus("");
		}
	}

	public boolean isPositive() 
	{
		return isPositive;
	}

	public void setPositive(boolean isPositive) 
	{
		this.isPositive = isPositive;
		this.isNegative = false;
		if(isPositive == true)
		{
			setQuestionStatus("IsOkay");
		}
		else if(isPositive == false && isNegative == false)
		{
			setQuestionStatus("");
		}
	}
}
