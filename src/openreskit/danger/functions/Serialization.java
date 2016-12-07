package openreskit.danger.functions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class Serialization 
{
	public static byte[] SerializeObject(Object object)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	
		try 
		{
			ObjectOutput out = new ObjectOutputStream(baos);
			out.writeObject(object);
			out.close();
			baos.close();
	
			return baos.toByteArray();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static Object DeserilizeObject(byte[] deserilizeObject)
	{
		try 
		{
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(deserilizeObject));
			Object object = in.readObject();
			in.close();
			return object;
		} 
		catch (Exception ex) 
		{
			return null;
		}
	}
	
	public static byte[] ConvertBitmapToByteArray(Bitmap image)
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		image.compress(CompressFormat.PNG, 0, outputStream);
		return outputStream.toByteArray();
	}
	
	public static Bitmap ConvertByteArrayToBitmap(byte[] sPhoto)
	{
		Bitmap photo = BitmapFactory.decodeByteArray(sPhoto, 0, sPhoto.length);
		return photo;
	}
}
