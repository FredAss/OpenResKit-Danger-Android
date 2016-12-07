package openreskit.danger.functions;

public enum Status 
{
	Not_Relevant(0),
	Not_Finished(1),
	No_Threat(2),
	In_Process(3),
	Threat(4);
	
	private int type;
	
	Status(int enumInt)
	{
		this.type = enumInt;
	}
	
	public int getNumericType()
	{
		return type;
	}
	
}
