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
@JsonIgnoreProperties(ignoreUnknown=true)
@DatabaseTable(tableName = "surveytypes")
public class Surveytype implements Serializable 
{
	@DatabaseField (id = true)
	private int Id = 0;
	
	@DatabaseField
	private String Name = "";
	
	@ForeignCollectionField (eager=false, columnName = "category_id")
	private Collection<Category> Category = null;



	public Surveytype(int id, String name,
			Collection<openreskit.danger.models.Category> category) {
		super();
		Id = id;
		Name = name;
		Category = category;
	}
	@JsonIgnore
	public Collection<Category> getCategory() {
		return Category;
	}
	@JsonProperty("Categories")
	public void setCategory(Collection<Category> category) {
		Category = category;
	}

	public Surveytype() 
	{
		super();
	}
	@JsonProperty("Id")
	public int getId() 
	{
		return Id;
	}
	@JsonProperty("Id")
	public void setId(int id) 
	{
		Id = id;
	}
	@JsonIgnore
	public String getName() 
	{
		return Name;
	}
	@JsonProperty("Name")
	public void setName(String name) 
	{
		Name = name;
	}
}
