package openreskit.danger.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@SuppressWarnings("serial")
@DatabaseTable(tableName = "assessments")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Assessment implements Serializable
{
	@DatabaseField (id = true)
	private String Id;
	
	@DatabaseField
	@JsonProperty("Id")
	private int ServerId;
	
	@DatabaseField (foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = "workplace_id")
	@JsonProperty("Workplace")
	private Workplace Workplace = null;
	
	@DatabaseField (columnName = "AssessmentDate", dataType = DataType.DATE)
	@JsonProperty("AssessmentDate")
	private Date AssessmentDate = null;
	
	@ForeignCollectionField (eager=false, columnName = "threat_id")
	@JsonIgnore
	private Collection<Threat> Threats = null;
	
	@DatabaseField (foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = "person_id")
	@JsonProperty("EvaluatingPerson")
	private Person EvaluatingPerson = null;
	
	@DatabaseField
	@JsonProperty("Status")
	private int Status = 0;
	
	private String SetCurrentDate()
	{
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
		return df.format(c.getTime());
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

	public Workplace getWorkplace() {
		return Workplace;
	}

	public void setWorkplace(Workplace workplace) {
		Workplace = workplace;
	}

	public Date getAssessmentDate() {
		return AssessmentDate;
	}

	public void setAssessmentDate(Date assessmentDate) {
		AssessmentDate = assessmentDate;
	}

	public Collection<Threat> getThreats() {
		return Threats;
	}

	public void setThreats(Collection<Threat> threats) {
		Threats = threats;
	}

	public Person getEvaluatingPerson() {
		return EvaluatingPerson;
	}

	public void setEvaluatingPerson(Person evaluatingPerson) {
		EvaluatingPerson = evaluatingPerson;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public Assessment(String id, int serverId,
			openreskit.danger.models.Workplace workplace, Date assessmentDate,
			Collection<Threat> threats, Person evaluatingPerson, int status) {
		super();
		Id = id;
		ServerId = serverId;
		Workplace = workplace;
		AssessmentDate = assessmentDate;
		Threats = threats;
		EvaluatingPerson = evaluatingPerson;
		Status = status;
	}

	public Assessment() {
		super();
	}
	
	
	
	
}
