package openreskit.danger.models;

import java.io.Serializable;



import android.graphics.Bitmap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
@SuppressWarnings("serial")
@DatabaseTable(tableName = "pictures")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Picture implements Serializable
{
	@DatabaseField (id = true)
	private String Id;
	
	@DatabaseField
	@JsonProperty("Id")
	private int ServerId;
	
	@DatabaseField()
	@JsonProperty("Pic")
	private String Pic = null;
	
	@DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = "threat_id")
	@JsonProperty("Threat")
	private Threat Threat = null;



	public Picture(String id, int serverId, String pic,
			openreskit.danger.models.Threat threat) {
		super();
		Id = id;
		ServerId = serverId;
		Pic = pic;
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



	public Picture() 
	{
		super();
	}

	

	public String getPic() 
	{
		return Pic;
	}

	public void setPic(String pic) 
	{
		Pic = pic;
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
