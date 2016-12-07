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
@DatabaseTable(tableName = "companys")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Company implements Serializable 
{

	@DatabaseField (id = true)
	@JsonProperty("Id")
	private int Id = 0;
	
	@DatabaseField 
	@JsonProperty("Name")
	private String Name = "";
	
	@DatabaseField 
	@JsonProperty("Adress")
	private String Adress = "";
	
	@DatabaseField 
	@JsonProperty("Telephone")
	private String Telephone = "";

	@DatabaseField 
	@JsonProperty("TypeOfBusiness")
	private String TypeOfBusiness = "";
	
	@ForeignCollectionField (eager=false, columnName = "workplace_id")
	@JsonIgnore
	private Collection<Workplace> Workplace = null;
	
	@ForeignCollectionField (eager=false, columnName = "person_id")
	@JsonIgnore
	private Collection<Person> Person = null;
	
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
	public String getAdress() {
		return Adress;
	}
	@JsonIgnore
	public void setAdress(String adress) {
		Adress = adress;
	}
	@JsonIgnore
	public String getTelephone() {
		return Telephone;
	}
	@JsonIgnore
	public void setTelephone(String telephone) {
		Telephone = telephone;
	}
	@JsonIgnore
	public String getTypeOfBusiness() {
		return TypeOfBusiness;
	}
	@JsonIgnore
	public void setTypeOfBusiness(String typeOfBusiness) {
		TypeOfBusiness = typeOfBusiness;
	}
	@JsonIgnore
	public Collection<Workplace> getWorkplace() {
		return Workplace;
	}
	@JsonIgnore
	public void setWorkplace(Collection<Workplace> workplace) {
		Workplace = workplace;
	}
	@JsonIgnore
	public Collection<Person> getPerson() {
		return Person;
	}
	@JsonIgnore
	public void setPerson(Collection<Person> person) {
		Person = person;
	}
	
	public Company(int id, String name, String adress, String telephone,
			String typeOfBusiness,
			Collection<openreskit.danger.models.Workplace> workplace,
			Collection<openreskit.danger.models.Person> person) {
		super();
		Id = id;
		Name = name;
		Adress = adress;
		Telephone = telephone;
		TypeOfBusiness = typeOfBusiness;
		Workplace = workplace;
		Person = person;
	}

	public Company() {
		super();
	}

	
	
}
