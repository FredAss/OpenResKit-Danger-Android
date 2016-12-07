package openreskit.danger.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@SuppressWarnings("serial")
@DatabaseTable(tableName = "dangerpoints")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Dangerpoint implements Serializable{

	@DatabaseField (id = true)
	@JsonProperty("Id")
	private int Id;
	
	@DatabaseField
	@JsonProperty("Name")
	private String Name = "";
	
	@DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = "gfactor_id")
	@JsonIgnore
	private GFactor Gfactor = null;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}


	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public GFactor getGfactor() {
		return Gfactor;
	}

	public void setGfactor(GFactor gfactor) {
		Gfactor = gfactor;
	}

	public Dangerpoint(int id,  String name, GFactor gfactor) {
		super();
		Id = id;
		
		Name = name;
		Gfactor = gfactor;
	}

	public Dangerpoint() {
		super();
	}

	
	
	
}
