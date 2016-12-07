package openreskit.danger.activities;

import openreskit.danger.R;
import openreskit.danger.functions.FileManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;

public class SettingsActivity extends Activity
{
	EditText urlText;
	EditText portText;
	EditText usernameText;
	EditText passwordText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		urlText = (EditText)findViewById(R.id.settingsURL);
		portText = (EditText)findViewById(R.id.settingsPort);
		usernameText = (EditText)findViewById(R.id.username);
		passwordText = (EditText)findViewById(R.id.password);

		String fileContent = FileManager.ReadFromFile("DangerSettings.txt", this);
		
		if(!fileContent.equals(""))
		{
			String[] urlPort = fileContent.split(";");
			urlText.setHint(urlPort[0]);
			portText.setHint(urlPort[1]);
			usernameText.setHint(urlPort[2]);
			passwordText.setHint(urlPort[3]);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.settings_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		
		switch(item.getItemId())
		{
			case R.id.setting_save_port_url:
				
				String user;
				String pw;
				
				if (usernameText.getText().toString().equals("") | passwordText.getText().toString().equals(""))
				{
	            	user="root";
	            	pw="ork123";
	            }  
				else
				{
					user = usernameText.getText().toString();
					pw = passwordText.getText().toString();
				}
				
				String filestring = urlText.getText().toString() + ";"
							+ portText.getText().toString() + ";"
							+ user + ";"
							+ pw;
				
				FileManager.CreateFileWithValues("DangerSettings.txt", filestring, this);
				GoBackToStartScreen();
				return true;
			case R.id.setting_cancel:
				GoBackToStartScreen();	
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}	
	
	private void GoBackToStartScreen()
	{
		Intent intent = new Intent(SettingsActivity.this, SelectCompanyActivity.class);
		startActivity(intent);
	}
}
