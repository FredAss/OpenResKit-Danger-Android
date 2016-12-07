package openreskit.danger.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import openreskit.danger.R;
import openreskit.danger.adapters.CompanyAdapter;
import openreskit.danger.dummy.LoadDummy;
import openreskit.danger.functions.FileManager;
import openreskit.danger.models.Company;
import openreskit.danger.persistence.ORMLiteDBHelper;
import openreskit.danger.sync.GetSurvey;
import openreskit.danger.sync.Synchronization;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SelectCompanyActivity extends Activity
{	
	private ListView companyList;
	List<Company> companiesFromDB;

	ORMLiteDBHelper helper;
	
	@Override	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_company_layout);

		helper = new ORMLiteDBHelper(getApplication());

		FillListWithCompanies();		
		
		companyList.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) 
			{
				Intent createTask = new Intent(SelectCompanyActivity.this, EditWorkplaceFragmentActivity.class);
				createTask.putExtra("Company", (Company) companyList.getAdapter().getItem(position));
				startActivity(createTask);			
			}
		});	
		
		companyList.setOnItemLongClickListener(new OnItemLongClickListener() 
		{
		      @Override
		      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) 
		      {
		    	  Intent intent = new Intent(SelectCompanyActivity.this, OverviewCompanyActivity.class);
		    	  intent.putExtra("Company", (Company) companyList.getAdapter().getItem(position));
		    	  startActivity(intent);

		    	  return true;
		      }
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		String fileContent = FileManager.ReadFromFile("DangerSettings.txt", this);
	    switch (item.getItemId()) 
	    {


	        case R.id.company_abort:
	        	return true;
	        case R.id.company_snyc:
	        	
	        	
	        	
	        	if(!fileContent.equals(""))
	        	{
	        	 
	        		new GetSurvey(fileContent, this).execute();
	        		
	        		new Synchronization().new GetHubData(fileContent, this).execute();

	        		
	        		
	        	}
	        	else
	        	{
	        		Context context = getApplicationContext();
	    			CharSequence text = "Bitte tragen Sie in den Einstellungen die IP des Servers und den Port ein.";
	    	

	    			Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
	    			toast.show();
	        	}	
	        	return true;
	        	
	        case R.id.company_send:
	          	if(!fileContent.equals(""))
	        	{
	        	new Synchronization().new SendHubData(fileContent, this).execute();
	        	}
	        	else
	        	{
	        		Context context = getApplicationContext();
	    			CharSequence text = "Bitte tragen Sie in den Einstellungen die IP des Servers und den Port ein.";
	    		

	    			Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
	    			toast.show();
	        	}	
	        	return true;
	        
	        case R.id.company_settings:
	        	Intent inten = new Intent(SelectCompanyActivity.this, SettingsActivity.class);
	        	startActivity(inten);
	        
	        default:
	            return super.onContextItemSelected(item);
	    }
	}

	private String ReadUrlPortFromFile()
    {   	
		String line = "";
		
    	try
    	{
    		FileInputStream ips = openFileInput("DangerSettings.txt");
    		BufferedReader r = new BufferedReader(new InputStreamReader(ips));
    		StringBuilder total = new StringBuilder();
    		
    		
    		while((line = r.readLine()) != null)
    		{
    			total.append(line);
    		}
    		r.close();
    		ips.close();
    		
    		return total.toString();
    	}
    	catch(Exception e)
    	{
    		
    	}
    	return line;	
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.company_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	

	
	public void FillListWithCompanies()
	{
		companiesFromDB = new ArrayList<Company>();
		try {
			companiesFromDB = helper.getCompanyDao().queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ArrayAdapter<Company> companyListAdapter = new CompanyAdapter(this, R.layout.listitem_layout_company, companiesFromDB);
		companyList = (ListView)findViewById(R.id.company_list);
		companyList.setAdapter(companyListAdapter);
	}
}	
	
