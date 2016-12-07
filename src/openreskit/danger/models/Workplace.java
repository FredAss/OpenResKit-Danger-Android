package openreskit.danger.models;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@SuppressWarnings("serial")
@DatabaseTable(tableName = "workplaces")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Workplace implements Serializable 
{
	@DatabaseField (id = true)
	private String Id;
	
	@DatabaseField
	@JsonProperty("Id")
	private int ServerId;
	
	@DatabaseField 
	@JsonProperty("Name")
	private String Name = "";
	
	@DatabaseField 
	@JsonProperty("NameCompany")
	private String NameCompany = "";
	
	@DatabaseField 
	@JsonProperty("Description")
	private String Description = "";
	
	@DatabaseField (foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = "company_id")
	@JsonProperty("Company")
	private Company Company = null;
	
	@DatabaseField (foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = "surveytype_id")
	@JsonProperty("SurveyType")
	private Surveytype SurveyType = null;
	
	@ForeignCollectionField (eager=false, columnName = "assessment_id")
	@JsonIgnore
	private Collection<Assessment> Assessments = null;
	
	@ForeignCollectionField (eager=false, columnName = "activity_id")
	@JsonIgnore
	private Collection<Activity> Activities = null;

	@JsonIgnore
	public String getId() {
		return Id;
	}
	
	@JsonIgnore
	public void setId(String id) {
		Id = id;
	}

	@JsonIgnore
	public int getServerId() {
		return ServerId;
	}

	@JsonIgnore
	public void setServerId(int serverId) {
		ServerId = serverId;
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
	public String getNameCompany() {
		return NameCompany;
	}

	@JsonIgnore
	public void setNameCompany(String nameCompany) {
		NameCompany = nameCompany;
	}

	@JsonIgnore
	public String getDescription() {
		return Description;
	}

	@JsonIgnore
	public void setDescription(String description) {
		Description = description;
	}

	@JsonIgnore
	public Company getCompany() {
		return Company;
	}

	@JsonIgnore
	public void setCompany(Company company) {
		Company = company;
	}

	@JsonIgnore
	public Surveytype getSurveyType() {
		return SurveyType;
	}

	@JsonIgnore
	public void setSurveyType(Surveytype surveyType) {
		SurveyType = surveyType;
	}

	@JsonIgnore
	public Collection<Assessment> getAssessments() {
		return Assessments;
	}

	@JsonIgnore
	public void setAssessments(Collection<Assessment> assessments) {
		Assessments = assessments;
	}

	@JsonIgnore
	public Collection<Activity> getActivities() {
		return Activities;
	}

	@JsonIgnore
	public void setActivities(Collection<Activity> activities) {
		Activities = activities;
	}


	public Workplace(String id, int serverId, String name, String nameCompany,
			String description, String position,
			openreskit.danger.models.Company company, Surveytype surveyType,
			Collection<Assessment> assessments, Collection<Activity> activities) {
		super();
		Id = id;
		ServerId = serverId;
		Name = name;
		NameCompany = nameCompany;
		Description = description;
		Company = company;
		SurveyType = surveyType;
		Assessments = assessments;
		Activities = activities;
	}

	
	public Workplace() {
		super();
	}

	

	

}
