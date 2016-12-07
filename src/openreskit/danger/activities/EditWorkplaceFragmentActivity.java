package openreskit.danger.activities;

import openreskit.danger.R;
import openreskit.danger.interfaces.OnListClickListener;
import openreskit.danger.models.Company;
import openreskit.danger.models.Workplace;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class EditWorkplaceFragmentActivity extends FragmentActivity implements OnListClickListener
{
	 AddWorkplaceDetailFragment addFragment;
	 static EditWorkplaceListFragment listFragment;
	 EditWorkplaceDetailFragment editFragment;
	
	//DataSource ds;
    static Company selectedCompany;
    
    static Workplace selectedWorkplace;
    static int positionOfWorkplace;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_workplace);
		
		selectedCompany = (Company) getIntent().getSerializableExtra("Company");		
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		
		addFragment = new AddWorkplaceDetailFragment();
		editFragment = new EditWorkplaceDetailFragment();
		listFragment = new EditWorkplaceListFragment();

		transaction.replace(R.id.EditWorkplaceDetail, addFragment);
		transaction.replace(R.id.EditWorkplaceList, listFragment);
		transaction.commit();	
		
		//ds = new DataSource(this);
	}

	@Override
	public void OnListClicked(Object obj, int position) 
	{	
		EditWorkplaceDetailFragment newFragment = new EditWorkplaceDetailFragment();
		newFragment.setMessageFromList(obj, position);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.EditWorkplaceDetail, newFragment);
		transaction.commit();
	}
}
