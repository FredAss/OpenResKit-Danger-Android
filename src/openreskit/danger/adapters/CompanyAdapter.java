package openreskit.danger.adapters;

import java.util.List;
import openreskit.danger.R;
import openreskit.danger.models.Company;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CompanyAdapter extends ArrayAdapter<Company> 
{
	private List<Company> companys;
	
	public CompanyAdapter(Context context, int textViewResourceId, List<Company> companys) 
	{
		super(context, textViewResourceId, companys);
		this.companys = companys;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View v = convertView;

		if (v == null) 
		{
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.listitem_layout_company, null);
		}

		Company c = companys.get(position);

		if (c != null) 
		{

			TextView Adress = (TextView) v.findViewById(R.id.companyAdress);
			TextView Name = (TextView) v.findViewById(R.id.companyName);
			

			if (Adress != null)
			{
				Adress.setText(c.getAdress());
			}
			if (Name != null)
			{
				Name.setText(c.getName());
			}
							
		}
		return v;
	}
}

	
