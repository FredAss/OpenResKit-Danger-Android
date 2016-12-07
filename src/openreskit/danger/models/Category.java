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
@DatabaseTable(tableName = "categorys")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Category implements Serializable
{
	@DatabaseField (id = true)
	@JsonProperty("Id")
	private int Id;
		
	@DatabaseField 
	@JsonProperty("Name")
	private String Name = "";
	
	@DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = "surveytype_id")
	@JsonIgnore
	private Surveytype Surveytype = null;

	@ForeignCollectionField (eager=false, columnName = "gfactor_id")
	@JsonProperty("GFactors")
	private Collection<GFactor> GFactor = null;



	
	
	public Category(int id, String name,
			openreskit.danger.models.Surveytype surveytype,
			Collection<GFactor> gFactor) {
		super();
		Id = id;
		Name = name;
		Surveytype = surveytype;
		GFactor = gFactor;
	}



	public Category() {
		super();
		// TODO Auto-generated constructor stub
	}


	@JsonIgnore
	public int getId() {
		return Id;
	}


	@JsonIgnore
	public void setId(int id) {
		Id = id;
	}


	@JsonIgnore
	public String getName() {
		return Name;
	}


	@JsonIgnore
	public void setName(String name) {
		Name = name;
	}


	@JsonIgnore
	public Surveytype getSurveytype() {
		return Surveytype;
	}


	@JsonIgnore
	public void setSurveytype(Surveytype surveytype) {
		Surveytype = surveytype;
	}


	@JsonIgnore
	public Collection<GFactor> getGFactor() {
		return GFactor;
	}


	@JsonIgnore
	public void setGFactor(Collection<GFactor> gFactor) {
		GFactor = gFactor;
	}
	
	
	





	
}
