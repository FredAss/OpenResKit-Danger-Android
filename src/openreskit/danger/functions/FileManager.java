package openreskit.danger.functions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import android.content.Context;

public class FileManager 
{
	public static void CreateFileWithValues(String fileName, String fileContent, Context context)
	{
		try
		{
			FileOutputStream opt = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			opt.write(fileContent.getBytes());
			opt.close();
		}
		catch(Exception ex)
		{			
		}
	}
	
	public static String ReadFromFile(String fileName, Context context)
	{
		File file = context.getFileStreamPath(fileName);
		String line = "";
		StringBuilder total = new StringBuilder();
		
		if(file.exists())
		{
			try
	    	{
	    		FileInputStream ips = context.openFileInput(fileName);
	    		BufferedReader r = new BufferedReader(new InputStreamReader(ips)); 		
	    		
	    		while((line = r.readLine()) != null)
	    		{
	    			total.append(line);
	    		}
	    		r.close();
	    		ips.close();	    		
	    	}
	    	catch(Exception e)
	    	{
	    		
	    	}
			return total.toString();
		}
		else
		{
			return total.append("").toString();
		}	
	}
}
