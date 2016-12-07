package openreskit.danger.models;

import java.io.Serializable;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@SuppressWarnings("serial")
@DatabaseTable(tableName = "persons")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Person implements Serializable
{
	@DatabaseField (id = true)
	private String Id;
	
	@DatabaseField
	@JsonProperty("Id")
	private int ServerId;
	
	@DatabaseField
	@JsonProperty("Name")
	private String Name = "";
	
	@DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = "company_id")
	@JsonProperty("Company")
	private Company Company = null;

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

	public Company getCompany() {
		return Company;
	}

	public void setCompany(Company company) {
		Company = company;
	}

	public Person(String id, int serverId, String name,
			openreskit.danger.models.Company company) {
		super();
		Id = id;
		ServerId = serverId;
		Name = name;
		Company = company;
	}

	public Person() {
		super();
	}
	
	

	
}
