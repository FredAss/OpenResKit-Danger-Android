package openreskit.danger.models;

import java.io.Serializable;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
@SuppressWarnings("serial")
@DatabaseTable(tableName = "protectionGoals")
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProtectionGoal implements Serializable
{
	@DatabaseField (id = true)
	private String Id;
	
	@DatabaseField
	@JsonProperty("Id")
	private int ServerId;
	
	@DatabaseField
	@JsonProperty("Description")
	private String Description = "";
	
	@DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = "threat_id")
	@JsonProperty("Threat")
	private Threat Threat = null;



	public ProtectionGoal() 
	{
		super();
	}



	public ProtectionGoal(String id, int serverId, String description,
			openreskit.danger.models.Threat threat) {
		super();
		Id = id;
		ServerId = serverId;
		Description = description;
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



	public String getDescription() 
	{
		return Description;
	}

	public void setDescription(String description) 
	{
		Description = description;
	}

	public Threat getThreat() 
	{
		return Threat;
	}

	public void setThreat(Threat threat) 
	{
		Threat = threat;
	}
}
