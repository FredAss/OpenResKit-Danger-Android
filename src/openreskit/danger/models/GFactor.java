package openreskit.danger.models;

import java.io.Serializable;
import java.util.Collection;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@SuppressWarnings("serial")
@DatabaseTable(tableName = "gFactors")
@JsonIgnoreProperties(ignoreUnknown=true)
public class GFactor implements Serializable
{
	@DatabaseField (id = true)
	@JsonProperty("Id")
	private int Id;
		
	@DatabaseField
	@JsonProperty("Number")
	private String Number = "";
	
	@DatabaseField
	@JsonProperty("Name")
	private String Name = "";
	
	@DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = "category_id")
	@JsonIgnore
	private Category Category = null;
	
	@ForeignCollectionField (eager=false, columnName = "question_id")
	@JsonProperty("Questions")
	private Collection<Question> Question = null;
	
	@ForeignCollectionField (eager=false, columnName = "dangerpoints_id")
	@JsonProperty("Dangerpoints")
	private Collection<Dangerpoint> Dangerpoints = null;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getNumber() {
		return Number;
	}

	public void setNumber(String number) {
		Number = number;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Category getCategory() {
		return Category;
	}

	public void setCategory(Category category) {
		Category = category;
	}

	public Collection<Question> getQuestion() {
		return Question;
	}

	public void setQuestion(Collection<Question> question) {
		Question = question;
	}

	public Collection<Dangerpoint> getDangerpoints() {
		return Dangerpoints;
	}

	public void setDangerpoints(Collection<Dangerpoint> dangerpoints) {
		Dangerpoints = dangerpoints;
	}

	public GFactor(int id, String number, String name,
			openreskit.danger.models.Category category,
			Collection<openreskit.danger.models.Question> question,
			Collection<openreskit.danger.models.Dangerpoint> dangerpoints) {
		super();
		Id = id;
		Number = number;
		Name = name;
		Category = category;
		Question = question;
		Dangerpoints = dangerpoints;
	}

	public GFactor() {
		super();
	}

	

}