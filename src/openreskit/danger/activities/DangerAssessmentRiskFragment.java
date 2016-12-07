package openreskit.danger.activities;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.j256.ormlite.dao.ForeignCollection;

import openreskit.danger.R;
import openreskit.danger.functions.RiskGroupCalculator;
import openreskit.danger.functions.Serialization;
import openreskit.danger.interfaces.OnButtonSaveClickListener;
import openreskit.danger.models.Assessment;
import openreskit.danger.models.Dangerpoint;
import openreskit.danger.models.Picture;
import openreskit.danger.models.QuestionWrapper;
import openreskit.danger.models.Threat;
import openreskit.danger.models.Workplace;
import openreskit.danger.persistence.ORMLiteDBHelper;
import openreskit.danger.ui.HorizontialListView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DangerAssessmentRiskFragment extends Fragment 
{
	ViewGroup view;


	
	Assessment assessmentToWork;
	Threat selectedThreat;
	long possibilityNumber;
	long dimensionNumber;
	List<Picture> photos;
	List<QuestionWrapper> question;
	String description = "";
	List<Dangerpoint> dangerpointsOfGfactorList;
	List<openreskit.danger.models.Activity> activitiesOfWorkplaceList;	
	
	OnButtonSaveClickListener saveListener;
	BaseAdapter mAdapter; 

	HorizontialListView imageList;
	Spinner threatPossibility;
	Spinner threatDimension;
	Spinner dangerpointsOfGfactor;
	Spinner activitiesOfWorkplace;
	TextView riskGroup;
	ImageButton takePhoto;
	EditText threatDescription;
	TextView imageDefaultText;
	ORMLiteDBHelper helper;
	boolean canSave = true;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		this.setHasOptionsMenu(true);
		view = (ViewGroup) inflater.inflate(R.layout.danger_assessment_risk, container, false);
			
		threatPossibility = (Spinner)view.findViewById(R.id.ThreatPossibility);
		threatDimension = (Spinner)view.findViewById(R.id.ThreatDimension);
		dangerpointsOfGfactor = (Spinner)view.findViewById(R.id.DangerpointsOfGfactor);
		activitiesOfWorkplace = (Spinner)view.findViewById(R.id.ActivitiesOfWorkplace);
		riskGroup = (TextView)view.findViewById(R.id.RiskGroup);
		takePhoto = (ImageButton)view.findViewById(R.id.TakeImage);
		imageList = (HorizontialListView)view.findViewById(R.id.testView);
		threatDescription = (EditText)view.findViewById(R.id.ThreatDescription);
		imageDefaultText = (TextView)view.findViewById(R.id.TakeImageDefaultText);

		helper = new ORMLiteDBHelper(getActivity());
		selectedThreat = DangerAssessmentFragmentActivity.threat;
		
		dangerpointsOfGfactorList = new ArrayList<Dangerpoint>();
		activitiesOfWorkplaceList = new ArrayList<openreskit.danger.models.Activity>();

		//dangerpointsOfGfactorList = (List<Dangerpoint>) selectedThreat.getGFactor().getDangerpoints();
		
		try {
			dangerpointsOfGfactorList = helper.getDangerpointDao().queryForEq("gfactor_id", selectedThreat.getGFactor().getId());
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			activitiesOfWorkplaceList = helper.getActivityDao().queryForEq("workplace_id", DangerAssessmentFragmentActivity.receivedWorkplace.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		
		photos = new ArrayList<Picture>();
		question = new ArrayList<QuestionWrapper>();
			
		InitializeControls();
		LoadDataFromThreatInControls();
		
		return view;
	}

	@Override
	public void onPause() 
	{
		super.onPause();
		
		
		
		if(canSave == true)
			SaveThreat();
	}

	

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
		inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.risk_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		
		switch(item.getItemId())
		{
		case R.id.risk_save:
			SaveThreat();
			Toast.makeText(getActivity(), "Die Risikobeschreibung wurde gespeichert.", Toast.LENGTH_LONG).show();
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		
		try 
		{
			saveListener = (OnButtonSaveClickListener)getActivity();
		} 
		catch (Exception e) 
		{
			
		}
	}
	
	private void InitializeControls()
	{	
		FillRiskDimensionSpinner();
		FillRiskPossibilitySpinner();
		FillDangerpointsOfGfactorSpinner();
		FillActivitiesOfWorkplaceSpinner();
		
		threatPossibility.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) 
			{
				possibilityNumber = arg0.getItemIdAtPosition(position) ;
				
				if(possibilityNumber < 5 &&  dimensionNumber < 5)
				{
					CalculateRiskGroup(possibilityNumber, dimensionNumber);
				}
				else if(possibilityNumber == 5 | dimensionNumber == 5)
				{
					riskGroup.setText("Risikogruppe: ");
					riskGroup.setTextColor(android.graphics.Color.BLACK);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) 
			{
				riskGroup.setText("");
			}		
		});
		
		threatDimension.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) 
			{
				dimensionNumber = arg0.getItemIdAtPosition(position) ;
				
				if(possibilityNumber < 5  && dimensionNumber < 5)
				{
					CalculateRiskGroup(possibilityNumber, dimensionNumber);
				}	
				else if(possibilityNumber == 5 | dimensionNumber == 5)
				{
					riskGroup.setText("Risikogruppe: ");
					riskGroup.setTextColor(android.graphics.Color.BLACK);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) 
			{
				riskGroup.setText("");		
			}	
		});
		
		takePhoto.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				canSave = false;
				Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, 1337);
			}
		});
		
		imageList.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) 
			{
				byte[] decodedByte = Base64.decode(((Picture) photos.get(position)).getPic(), 0);
			    takePhoto.setImageBitmap(BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length));
				//takePhoto.setImageBitmap(photos.get(position).getPic());		
			}
		});		
	}
	

	

	private void SaveThreat()
	{
			selectedThreat.setDescription(threatDescription.getText().toString());
			selectedThreat.setDangerpoint(dangerpointsOfGfactorList.get(dangerpointsOfGfactor.getSelectedItemPosition()));
			
			if(threatDimension.getSelectedItemPosition() == 5)
				selectedThreat.setRiskDimension(5);
			else
				selectedThreat.setRiskDimension(threatDimension.getSelectedItemPosition());
			
			if(threatPossibility.getSelectedItemPosition() == 5)
				selectedThreat.setRiskPossibility(5);
			else
				selectedThreat.setRiskPossibility(threatPossibility.getSelectedItemPosition());
						
			selectedThreat.setActivity(activitiesOfWorkplaceList.get(activitiesOfWorkplace.getSelectedItemPosition()));
			//selectedThreat.setDangerpoint(dangerpointsOfGfactorList.get(dangerpointsOfGfactor.getSelectedItemPosition()));;
			
			
			try
			{
				//ds.Open();
				
				List<Picture> newPicturesOfThreat = new ArrayList<Picture>();
				
				for (Picture picture : photos) {		
					//if(ds.IsPictureAlreadyExistend(picture) == false)
					picture.setThreat(selectedThreat);
						newPicturesOfThreat.add(picture);
				}
				helper.getThreatDao().update(selectedThreat);
				//ds.UpdateThreatInDb(selectedThreat);
				
				if(newPicturesOfThreat.size() != 0)
					for (Picture pic : newPicturesOfThreat) {
						helper.getPictureDao().createOrUpdate(pic);
					}
					
					//ds.InsertPictureInDb(newPicturesOfThreat, selectedThreat);
				
				//ds.Close();
			}
			catch(Exception ex)
			{
			
			}
		
			DangerAssessmentFragmentActivity.threat = selectedThreat;
			saveListener.OnSaveClickListener();
		
	}
	
	private void FillRiskPossibilitySpinner() 
	{
		String[] threatPossibilityValues = new String[]{"A - häufig", "B - gelegentlich", "C - selten", "D - unwahrscheinlich", "E - praktisch unmöglich", "Wahrscheinlichkeit wählen"};
		
		ArrayAdapter<CharSequence> spinnerAdapter1 = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, threatPossibilityValues);
		spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
		threatPossibility.setAdapter(spinnerAdapter1);
		
		threatPossibility.setSelection(5);
	}

	private void FillRiskDimensionSpinner() 
	{
		String[] threatDimensionsValues = new String[]{"V - ohne Arbeitsausfall", "IV - mit Arbeitsausfall", "III - leichter bleibender Gesundheitsschaden", "II - schwerer bleibender Gesundheitsschaden", "I - Tod", "Schadensausmaß wählen"};	
		
		ArrayAdapter<CharSequence> dimensionAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, threatDimensionsValues);
		dimensionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		threatDimension.setAdapter(dimensionAdapter);
		
		threatDimension.setSelection(5);
	}
	
	private void FillDangerpointsOfGfactorSpinner() 
	{
		String[] dangerpointsOfGfactorsArray = new String[dangerpointsOfGfactorList.size()];
		
		int position = 0;
		
		for (Dangerpoint dangerpoint : dangerpointsOfGfactorList) 
		{
			dangerpointsOfGfactorsArray[position] = dangerpoint.getName();
			position++;
		}
		
		ArrayAdapter<CharSequence> dangerpointAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, dangerpointsOfGfactorsArray);
		dangerpointAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		dangerpointsOfGfactor.setAdapter(dangerpointAdapter);
	}
	
	private void FillActivitiesOfWorkplaceSpinner() 
	{
		String[] activitiesOfWorkplace = new String[activitiesOfWorkplaceList.size()];
		
		int position = 0;
		
		for(openreskit.danger.models.Activity activity : activitiesOfWorkplaceList)
		{
			activitiesOfWorkplace[position] = activity.getName();
			position++;
		}
		
		ArrayAdapter<CharSequence> activityAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, activitiesOfWorkplace);
		activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		this.activitiesOfWorkplace.setAdapter(activityAdapter);
	}


	private void LoadDataFromThreatInControls()
	{
		Threat checkThreat = DangerAssessmentFragmentActivity.threat;
		
		if(checkThreat.getDescription() == null || checkThreat.getRiskDimension() == 0 || checkThreat.getRiskPossibility() == 0)
		{
			threatDescription.setHint(description);
			selectedThreat = checkThreat;
		}
		else
			threatDescription.setText(DangerAssessmentFragmentActivity.threat.getDescription());
		
		if(checkThreat.getDangerpoint() != null)
		{
			Dangerpoint da = checkThreat.getDangerpoint();
			
			for(int i = 0; i < dangerpointsOfGfactorList.size(); i++)
			{
				Dangerpoint daList = dangerpointsOfGfactorList.get(i);
				
				if(da.getId()==(daList.getId()))
				{
					dangerpointsOfGfactor.setSelection(i);
					break;
				}
			}
		}
		
		if(checkThreat.getActivity() != null)
		{
			openreskit.danger.models.Activity ac = checkThreat.getActivity();
			
			for(int i = 0; i < activitiesOfWorkplaceList.size(); i++)
			{
				openreskit.danger.models.Activity acList = activitiesOfWorkplaceList.get(i);
				
				if(ac.getId().equals(acList.getId()))
				{
					activitiesOfWorkplace.setSelection(i);
					break;
				}
			}
		}
		threatDimension.setSelection(checkThreat.getRiskDimension());
		threatPossibility.setSelection(checkThreat.getRiskPossibility());
		
		
		
		try
		{
			//ds.Open();
			photos = helper.getPictureDao().queryForEq("threat_id", checkThreat.getId());
//			photos = ds.GetAllPicturesFromThreat(checkThreat);
//			ds.Close();
		}
		catch(Exception ex)
		{
			
		}
		
		FillImageList();
		
		if(photos.size() != 0)
		{
			byte[] decodedByte = Base64.decode(((Picture) photos.get(0)).getPic(), 0);
		    takePhoto.setImageBitmap(ResizeBitmap(BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length)));
			//takePhoto.setImageBitmap(photos.get(0).getPic());
			CheckIfPicturesExist();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if(requestCode == 1337)
		{			
			Picture picture = new Picture();
			
			try
			{
				Bitmap a = (Bitmap)data.getExtras().get("data");
				
				picture.setId(UUID.randomUUID().toString());
				
	            ByteArrayOutputStream stream = new ByteArrayOutputStream();
	            a.compress(Bitmap.CompressFormat.PNG, 100, stream);
	            picture.setPic(Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT));
				
			
				takePhoto.setImageBitmap(ResizeBitmap(a));
								
				photos.add(picture);
										
				CheckIfPicturesExist();
				
				FillImageList();
			}
			catch(Exception ex)
			{
				
			}
		}
		else
		{
			
		}
		
		canSave = true;
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private Bitmap ResizeBitmap(Bitmap image)
	{
		
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		
		Bitmap resizedImage = Bitmap.createScaledBitmap(image, imageWidth, imageHeight, true);
		return resizedImage;
	}
	
	private void CheckIfPicturesExist()
	{
		if(photos.size() == 0)
		{
			imageDefaultText.setText("Klick zum Bild aufnehmen");
		}
		else
		{
			imageDefaultText.setText("");
		}
	}
	
	/**
	 * Creates an adapter for filling listview with data
	 */
	private void FillImageList()
	{
		CreateAdapter();
		imageList.setAdapter(mAdapter);
	}
	
	public void PrepareValuesFromThreat(Threat gfactor, List<QuestionWrapper> questionWrapper, Assessment assessment)
	{
		selectedThreat = gfactor;
		question = questionWrapper;
		assessmentToWork = assessment;
		
		for(int i = 0; i < question.size(); i++)
		{
			if((question.get(i).getQuestionStatus()).equals("IsNotOkay"))
				description += question.get(i).getQuestion().getQuestionName();
		}
	}
	
	private void CreateAdapter()
	{
		mAdapter = new BaseAdapter() 
		{
			@Override
			public View getView(int position, View convertView, ViewGroup parent) 
			{
				View retval = LayoutInflater.from(getActivity()).inflate(R.layout.listitem_layout_image, null);
				ImageView photo = (ImageView)retval.findViewById(R.id.PicturesTaken);
				
				byte[] decodedByte = Base64.decode(((Picture) photos.get(position)).getPic(), 0);
				photo.setImageBitmap(BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length));
				
				//photo.setImageBitmap(photos.get(position).getPic());
				return retval;
			}
			
			@Override
			public long getItemId(int arg0) 
			{
				return 0;
			}
			
			@Override
			public Object getItem(int arg0) 
			{
				return null;
			}
			
			@Override
			public int getCount() 
			{
				return photos.size();
			}
		};
	}

	/**
	 * Determines the risk-group regarding the selected possibility and dimension of the threat
	 * @param possibility
	 * @param dimension
	 */
	private void CalculateRiskGroup(long possibility, long dimension)
	{	
		int[][] riskMatrix = RiskGroupCalculator.CalculateRiskGroupe();
				
		// Receive the risk-group from the matrix with the assigned parameters
		int riskNumber = riskMatrix[(int) possibility][(int) dimension];
		String risk = "Risikogruppe: " + String.valueOf(riskNumber);
		
		// Changes the color of the text regarding the risk-group
		/*switch (riskNumber) 
		{
			case 1:
				riskGroup.setTextColor(android.graphics.Color.RED);
				break;
			case 2:
				riskGroup.setTextColor(android.graphics.Color.YELLOW);
				break;
			case 3:
				riskGroup.setTextColor(android.graphics.Color.GREEN);	
				break;
			default:
				riskGroup.setTextColor(android.graphics.Color.BLACK);
				break;
		}*/
		riskGroup.setText(risk);		
	}
}
