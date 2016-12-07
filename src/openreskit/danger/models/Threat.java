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
@DatabaseTable(tableName = "threats")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Threat implements Serializable 
{
	@DatabaseField (id = true)
	private String Id;
	
	@DatabaseField
	@JsonProperty("Id")
	private int ServerId;
	
	@DatabaseField
	@JsonProperty("Description")
	private String Description = "";
	
	@DatabaseField
	@JsonProperty("RiskDimension")
	private int RiskDimension = 0;
	
	@DatabaseField
	@JsonProperty("RiskPossibility")
	private int RiskPossibility = 0;
	
	@DatabaseField
	@JsonProperty("Status")
	private int Status = 0; 
	
	@DatabaseField
	@JsonProperty("Actionneed")
	private boolean Actionneed = false; 
	
	@DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = "assessment_id")
	private Assessment Assessment = null;
	
	@DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = "gfactor_id")
	@JsonProperty("GFactor")
	private GFactor GFactor = null;
	
	@DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = "dangerpoint_id")
	@JsonProperty("Dangerpoint")
	private Dangerpoint Dangerpoint = null;
	
	@DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = "activity_id")
	@JsonProperty("Activity")
	private Activity Activity = null;

	@ForeignCollectionField (eager=false, columnName = "picture_id")
	@JsonProperty("Pictures")
	private Collection<Picture> Pictures = null;

	@ForeignCollectionField (eager=false, columnName = "protectiongoal_id")
	@JsonProperty("ProtectionGoals")
	private Collection<ProtectionGoal> ProtectionGoals = null;

	@ForeignCollectionField (eager=false, columnName = "action_id")
	@JsonProperty("Actions")
	private Collection<Action> Actions = null;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public int getServerId() {
		return ServerId;
	}

	public void setServerId(int serverId) {
		ServerId = serverId;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public int getRiskDimension() {
		return RiskDimension;
	}

	public void setRiskDimension(int riskDimension) {
		RiskDimension = riskDimension;
	}

	public int getRiskPossibility() {
		return RiskPossibility;
	}

	public void setRiskPossibility(int riskPossibility) {
		RiskPossibility = riskPossibility;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public boolean getActionneed() {
		return Actionneed;
	}

	public void setActionneed(boolean actionneed) {
		Actionneed = actionneed;
	}

	public Assessment getAssessment() {
		return Assessment;
	}

	public void setAssessment(Assessment assessment) {
		Assessment = assessment;
	}

	public GFactor getGFactor() {
		return GFactor;
	}

	public void setGFactor(GFactor gFactor) {
		GFactor = gFactor;
	}

	public Dangerpoint getDangerpoint() {
		return Dangerpoint;
	}

	public void setDangerpoint(Dangerpoint dangerpoint) {
		Dangerpoint = dangerpoint;
	}

	public Activity getActivity() {
		return Activity;
	}

	public void setActivity(Activity activity) {
		Activity = activity;
	}

	public Collection<Picture> getPictures() {
		return Pictures;
	}

	public void setPictures(Collection<Picture> pictures) {
		Pictures = pictures;
	}

	public Collection<ProtectionGoal> getProtectionGoals() {
		return ProtectionGoals;
	}

	public void setProtectionGoals(Collection<ProtectionGoal> protectionGoals) {
		ProtectionGoals = protectionGoals;
	}

	public Collection<Action> getActions() {
		return Actions;
	}

	public void setActions(Collection<Action> actions) {
		Actions = actions;
	}

	public Threat(String id, int serverId, String description,
			int riskDimension, int riskPossibility, int status, boolean actionneed,
			openreskit.danger.models.Assessment assessment,
			openreskit.danger.models.GFactor gFactor,
			openreskit.danger.models.Dangerpoint dangerpoint,
			openreskit.danger.models.Activity activity,
			Collection<Picture> pictures,
			Collection<ProtectionGoal> protectionGoals,
			Collection<Action> actions) {
		super();
		Id = id;
		ServerId = serverId;
		Description = description;
		RiskDimension = riskDimension;
		RiskPossibility = riskPossibility;
		Status = status;
		Actionneed = actionneed;
		Assessment = assessment;
		GFactor = gFactor;
		Dangerpoint = dangerpoint;
		Activity = activity;
		Pictures = pictures;
		ProtectionGoals = protectionGoals;
		Actions = actions;
	}

	public Threat() {
		super();
	}

	


}
