package openreskit.danger.models;

import java.io.Serializable;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@SuppressWarnings("serial")
@DatabaseTable(tableName = "questions")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Question implements Serializable
{
	@DatabaseField (id = true)
	@JsonProperty("Id")
	private int Id = 0;
	
	@DatabaseField
	@JsonProperty("QuestionName")
	private String questionName = "";
	
	@DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = "gfactor_id")
	@JsonIgnore
	private GFactor Gfactor = null;

	public Question(int id, String questionName, GFactor gfactor) 
	{
		super();
		Id = id;
		this.questionName = questionName;
		Gfactor = gfactor;
	}

	public Question() 
	{
		super();
	}
	@JsonIgnore
	public int getId() 
	{
		return Id;
	}
	@JsonIgnore
	public void setId(int id) 
	{
		Id = id;
	}
	@JsonIgnore
	public String getQuestionName() 
	{
		return questionName;
	}
	@JsonIgnore
	public void setQuestionName(String questionName) 
	{
		this.questionName = questionName;
	}
	@JsonIgnore
	public GFactor getGfactor() 
	{
		return Gfactor;
	}
	@JsonIgnore
	public void setGfactor(GFactor gfactor) 
	{
		Gfactor = gfactor;
	}		
}
