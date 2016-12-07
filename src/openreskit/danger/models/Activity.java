package openreskit.danger.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@SuppressWarnings("serial")
@DatabaseTable(tableName = "activitys")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Activity implements Serializable{

	@DatabaseField (id = true)
	private String Id;
	
	@DatabaseField
	@JsonProperty("Id")
	private int ServerId;
	
	@DatabaseField
	@JsonProperty("Name")
	private String Name = "";
	
	@DatabaseField (foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = "workplace_id")
	@JsonProperty("Workplace")
	private Workplace Workplace = null;

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

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Workplace getWorkplace() {
		return Workplace;
	}

	public void setWorkplace(Workplace workplace) {
		Workplace = workplace;
	}

	public Activity(String id, int serverId, String name,
			openreskit.danger.models.Workplace workplace) {
		super();
		Id = id;
		ServerId = serverId;
		Name = name;
		Workplace = workplace;
	}

	public Activity() {
		super();
	}
	
	
	

	
	
	
}
