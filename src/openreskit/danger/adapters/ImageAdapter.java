package openreskit.danger.adapters;

import java.util.List;

import openreskit.danger.R;
import openreskit.danger.models.Picture;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class ImageAdapter extends ArrayAdapter<Picture> 
{
	List<Picture> images;
	
	public ImageAdapter(Context context, int textViewResourceId, List<Picture> images) 
	{
		super(context, textViewResourceId, images);
		this.images = images;		
	}

	private class ViewHolder
	{
		ImageView photo;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder viewHolder;
		
		if (convertView == null) 
		{
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.listitem_layout_image, null);
			
			viewHolder = new ViewHolder();
			viewHolder.photo = (ImageView)convertView.findViewById(R.id.PicturesTaken);
			
			convertView.setTag(viewHolder);
			convertView.setTag(R.id.PicturesTaken, viewHolder.photo);
		}
		else
		{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		viewHolder.photo.setTag(position);

		byte[] decodedByte = Base64.decode(images.get(position).getPic(),0);
		
		
		viewHolder.photo.setImageBitmap(BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length));

		return convertView;
	}
}
