package openreskit.danger.models;

import java.io.Serializable;
import java.util.Date;




import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@SuppressWarnings("serial")
@DatabaseTable(tableName = "actions")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Action implements Serializable
{
	@DatabaseField (id = true)
	private String Id;
	
	@DatabaseField
	@JsonProperty("Id")
	private int ServerId;
	
	@DatabaseField
	@JsonProperty("Description")
	private String Description = "";
	
	@DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = "person_id")
	@JsonProperty("Person")
	private Person Person = null;
	
	@DatabaseField
	@JsonProperty("DueDate")
	private Date DueDate = null;
	
	@DatabaseField
	@JsonProperty("Effect")
	private Boolean Effect = false;
	
	@DatabaseField
	@JsonProperty("Execution")
	private String Execution = "";
	
	@DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = "threat_id")
	@JsonProperty("Threat")
	private Threat Threat = null;

	public Threat getThreat() {
		return Threat;
	}

	public void setThreat(Threat threat) {
		Threat = threat;
	}

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

	public Person getPerson() {
		return Person;
	}

	public void setPerson(Person person) {
		Person = person;
	}

	public Date getDueDate() {
		return DueDate;
	}

	public void setDueDate(Date dueDate) {
		DueDate = dueDate;
	}

	public Boolean getEffect() {
		return Effect;
	}

	public void setEffect(Boolean effect) {
		Effect = effect;
	}

	public String getExecution() {
		return Execution;
	}

	public void setExecution(String execution) {
		Execution = execution;
	}

	

	public Action(String id, int serverId, String description,
			openreskit.danger.models.Person person, Date dueDate,
			Boolean effect, String execution,
			Threat threat) {
		super();
		Id = id;
		ServerId = serverId;
		Description = description;
		Person = person;
		DueDate = dueDate;
		Effect = effect;
		Execution = execution;
		Threat = threat;
	}

	public Action() {
		super();
	}
	
	


}