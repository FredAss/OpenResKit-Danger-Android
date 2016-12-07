package openreskit.danger.sync;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import openreskit.danger.activities.SelectCompanyActivity;
import openreskit.danger.models.Action;
import openreskit.danger.models.Assessment;
import openreskit.danger.models.Category;
import openreskit.danger.models.Company;
import openreskit.danger.models.GFactor;
import openreskit.danger.models.Person;
import openreskit.danger.models.Picture;
import openreskit.danger.models.ProtectionGoal;
import openreskit.danger.models.Question;
import openreskit.danger.models.Surveytype;
import openreskit.danger.models.Threat;
import openreskit.danger.models.Workplace;

import openreskit.danger.persistence.ORMLiteDBHelper;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class Synchronization  {

	private Activity mContext;
	public List<Action> mActions = new ArrayList<Action>();
	public List<Assessment> mAssessments = new ArrayList<Assessment>();
	public List<Company> mCompanys = new ArrayList<Company>();
	public List<Person> mPersons = new ArrayList<Person>();
	public List<Picture> mPictures = new ArrayList<Picture>();
	public List<ProtectionGoal> mProtectionGoals = new ArrayList<ProtectionGoal>();
	public List<Threat> mThreats = new ArrayList<Threat>();
	public List<Workplace> mWorkplaces = new ArrayList<Workplace>();


	
	public List<openreskit.danger.models.Activity> mActivitys = new ArrayList<openreskit.danger.models.Activity>();
	

	
	final DateFormat dfs = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.GERMANY);

	;

	private String url;
	private String port;

	public class SendHubData extends AsyncTask<Void, Void, Boolean> {
		ORMLiteDBHelper helper;
		private ProgressDialog progress;
		public SendHubData(String urlPort, Activity ctx) {
			super();

			String[] a = urlPort.split(";");
			url = a[0];
			port = a[1];
			mContext = ctx;
			helper = new ORMLiteDBHelper(mContext);
			
			progress = new ProgressDialog(mContext);

		}

		 protected void onPreExecute() {
	            this.progress.setMessage("Schreibe Daten auf Server");
	            this.progress.show();
	        }
		
		protected Boolean doInBackground(Void... params) {
			String ip = url;
			String username = "root";
			String password = "ork123";

				try {
					mCompanys = helper.getCompanyDao().queryForAll();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			for (Company company : mCompanys) {
				
				try {
					
					
			
					writeODataFromJson(url, port,
							"root", "ork123", "OpenResKitHub",
							"Company",company);
	
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			for (Company cp : mCompanys) {
				try {
					helper.getCompanyDao().createOrUpdate(cp);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			return true;
		}
		
protected void onPostExecute(final Boolean success) {
    
			

            if (progress.isShowing()) {
            	progress.dismiss();
        }

        if (success) {
            Toast.makeText(mContext, "OK", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext, "Error", Toast.LENGTH_LONG).show();
        }
    }
		
		private void writeODataFromJson(String defaultIP, String port,
				String username, String password, String endpoint,
				String collection, Company cp) throws Exception  {
			
			HttpResponse response;
			HttpParams httpParams = new BasicHttpParams();
			HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
			httpParams.setBooleanParameter("http.protocol.expect-continue", false);
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
			
			ObjectMapper mapper = new ObjectMapper();
			mActivitys = helper.getActivityDao().queryForAll();
			mPersons = helper.getPersonDao().queryForAll();
			mPictures = helper.getPictureDao().queryForAll();
			mProtectionGoals = helper.getProtectionGoalDao().queryForAll();
		
			
			
				for (Person pe : mPersons) {
					HttpPost request = NewUpdatePersonRequest(defaultIP,port,
							username, password, endpoint,
							"Person",pe);
					
					response = httpClient.execute(request);
					HttpEntity responseEntity = response.getEntity();
				
					if(responseEntity != null) 
					{
						String jsonText = EntityUtils.toString(responseEntity, HTTP.UTF_8);
						JSONObject answer = new JSONObject(jsonText);
						Log.i("JSON Answer Person",answer.toString() );
						Person newPerson = mapper.readValue(answer.toString(), Person.class);	
						pe.setServerId(newPerson.getServerId());
						helper.getPersonDao().createOrUpdate(pe);
						
					
				  	}
					
				}
			
			
			
			for (openreskit.danger.models.Activity ac : mActivitys) {
				HttpPost request = NewUpdateActivityRequest(defaultIP,port,
						username, password, endpoint,
						"Activity",ac);
				
				response = httpClient.execute(request);
				HttpEntity responseEntity = response.getEntity();
			
				if(responseEntity != null) 
				{
					String jsonText = EntityUtils.toString(responseEntity, HTTP.UTF_8);
					JSONObject answer = new JSONObject(jsonText);
					Log.i("JSON Answer Activity",answer.toString() );
					openreskit.danger.models.Activity newActivity = mapper.readValue(answer.toString(), openreskit.danger.models.Activity.class);	    	
				
					ac.setServerId(newActivity.getServerId());
					helper.getActivityDao().createOrUpdate(ac);
			
			  	}
				
			}
			
			for (ProtectionGoal pg : mProtectionGoals) {
				
				
				HttpPost request = NewUpdateProtectionGoalRequest(defaultIP,port,
						username, password, endpoint,
						"ProtectionGoal",pg);
				
				response = httpClient.execute(request);
				HttpEntity responseEntity = response.getEntity();
			
				if(responseEntity != null) 
				{
					String jsonText = EntityUtils.toString(responseEntity, HTTP.UTF_8);
					JSONObject answer = new JSONObject(jsonText);
					Log.i("JSON Answer ProtectionGoal",answer.toString() );
					ProtectionGoal newPG = mapper.readValue(answer.toString(), ProtectionGoal.class);	    	
					pg.setServerId(newPG.getServerId());
					helper.getProtectionGoalDao().createOrUpdate(pg);
				
			  	}
				
			}
			
			
			mActions = helper.getActionDao().queryForAll();
			for (Action ac : mActions) {
				
				
				HttpPost request = NewUpdateActionRequest(defaultIP,port,
						username, password, endpoint,
						"Action",ac);
				
				response = httpClient.execute(request);
				HttpEntity responseEntity = response.getEntity();
			
				if(responseEntity != null) 
				{
					String jsonText = EntityUtils.toString(responseEntity, HTTP.UTF_8);
					JSONObject answer = new JSONObject(jsonText);
					Log.i("JSON Answer Action",answer.toString() );
					Action newAction = mapper.readValue(answer.toString(), Action.class);	    	
					ac.setServerId(newAction.getServerId());
					helper.getActionDao().createOrUpdate(ac);
			
					
			  	}
				
			}
			
			for (Picture pic : mPictures) {
				
				
				HttpPost request = NewUpdatePictureRequest(defaultIP,port,
						username, password, endpoint,
						"Picture",pic);
				
				response = httpClient.execute(request);
				HttpEntity responseEntity = response.getEntity();
			
				if(responseEntity != null) 
				{
					String jsonText = EntityUtils.toString(responseEntity, HTTP.UTF_8);
					JSONObject answer = new JSONObject(jsonText);
					Log.i("JSON Answer Picture",answer.toString() );
					Picture newPicture = mapper.readValue(answer.toString(), Picture.class);	    	
					pic.setServerId(newPicture.getServerId());
					helper.getPictureDao().createOrUpdate(pic);
		
			  	}
				else {
					Log.i("JSON Answer Picture","Keine Antwort" );
				}
				
			}

			
			//Ebene 2
		

			mThreats = helper.getThreatDao().queryForAll();
				for (Threat th : mThreats) {
					

					HttpPost request = NewUpdateThreatRequest(defaultIP,port,
							username, password, endpoint,
							"Threat",th);
					
					response = httpClient.execute(request);
					HttpEntity responseEntity = response.getEntity();
				
					if(responseEntity != null) 
					{
						String jsonText = EntityUtils.toString(responseEntity, HTTP.UTF_8);
						JSONObject answer = new JSONObject(jsonText);
						Log.i("JSON Answer Threat",answer.toString() );
						Threat newThreat = mapper.readValue(answer.toString(), Threat.class);	    	
						th.setServerId(newThreat.getServerId());
						helper.getThreatDao().createOrUpdate(th);
		
				  	}
					
				}
			
			//Ebene 3
				mAssessments = helper.getAssessmentDao().queryForAll();
				for (Assessment as : mAssessments) {
					
					HttpPost request = NewUpdateAssessmentRequest(defaultIP,port,
							username, password, endpoint,
							"Assessment",as);
					
					response = httpClient.execute(request);
					HttpEntity responseEntity = response.getEntity();
				
					if(responseEntity != null) 
					{
						String jsonText = EntityUtils.toString(responseEntity, HTTP.UTF_8);
						JSONObject answer = new JSONObject(jsonText);
						Log.i("JSON Answer Assessment",answer.toString() );
						Assessment newAssessment = mapper.readValue(answer.toString(), Assessment.class);	    	
						as.setServerId(newAssessment.getServerId());
						helper.getAssessmentDao().createOrUpdate(as);
				
				  	}
					
				}
			
			
			//Ebene 4
				mWorkplaces = helper.getWorkplaceDao().queryForAll();
				for (Workplace wp : mWorkplaces) {
					
					HttpPost request = NewUpdateWorkplaceRequest(defaultIP,port,
							username, password, endpoint,
							"Workplace",wp);
					response = httpClient.execute(request);
					HttpEntity responseEntity = response.getEntity();
				
					if(responseEntity != null) 
					{
						String jsonText = EntityUtils.toString(responseEntity, HTTP.UTF_8);
						JSONObject answer = new JSONObject(jsonText);
						Log.i("JSON Answer Workplace",answer.toString() );
						Workplace newWorkplace = mapper.readValue(answer.toString(), Workplace.class);	    	
						wp.setServerId(newWorkplace.getServerId());
						helper.getWorkplaceDao().createOrUpdate(wp);
					
					}
				}
			
			
			HttpPost request = new HttpPost("http://" + defaultIP + ":" + port + "/" + endpoint + "/" + "Companies(" + cp.getId()+")");
			request.setHeader("X-HTTP-Method", "PUT");
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json;odata=verbose");
			request.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(username, password), "UTF-8", false));
			
			String companyJSONString = "{\"odata.type\":\"OpenResKit.DomainModel.Company\",";
			Log.i("JSON String","Gesetzte Company String "+cp.getId() );
			companyJSONString += "\"Id\":\"" +cp.getId()+ "\",";
			companyJSONString += "\"Name\":\"" +cp.getName()+ "\",";
			companyJSONString += "\"Adress\":\"" +cp.getAdress()+ "\",";
			companyJSONString += "\"Telephone\":\"" +cp.getTelephone()+ "\",";
			companyJSONString += "\"TypeOfBusiness\":\"" +cp.getTypeOfBusiness()+ "\",";
			
			String workplaceIDJSONString = "[";
			int counter = 1;
			for (Workplace wpId: cp.getWorkplace()) 
			{
				workplaceIDJSONString += "{\"__metadata\":{\"uri\": \"http://" + defaultIP + ":" + port + "/" + endpoint + "/Workplaces("+ wpId.getServerId() +")\"}}";
				if (counter < cp.getWorkplace().size()) 
				{
					workplaceIDJSONString += ",";
				}
				counter++;
			}

			workplaceIDJSONString += "]";
			
			companyJSONString += "\"Workplaces\":"+ workplaceIDJSONString +",";

			
			
			String personIDJSONString = "[";
			counter = 1;
			for (Person peId: cp.getPerson()) 
			{
				personIDJSONString += "{\"__metadata\":{\"uri\": \"http://" + defaultIP + ":" + port + "/" + endpoint + "/People("+ peId.getServerId() +")\"}}";
				if (counter < cp.getPerson().size()) 
				{
					personIDJSONString += ",";
				}
				counter++;
			}

			personIDJSONString += "]";
			
			companyJSONString += "\"Persons\":"+ personIDJSONString +"}";
			
			 Log.i("JSON Company Request"," Company Request "+companyJSONString );
			
			StringEntity stringEntity = new StringEntity(companyJSONString,HTTP.UTF_8);
			stringEntity.setContentType("application/json");
			request.setEntity(stringEntity);
			response = httpClient.execute(request);
			HttpEntity responseEntity = response.getEntity();
			if(responseEntity != null) 
			{
				String jsonText = EntityUtils.toString(responseEntity, HTTP.UTF_8);
				JSONObject answer = new JSONObject(jsonText);
				 Log.i("JSON Answer","Antwort nach Company Request "+answer.toString() );
			}
			
		}

	

		
		
		private HttpPost NewUpdateWorkplaceRequest(String defaultIP, String port,
				String username, String password, String endpoint,
				String collection,Workplace wp) throws Exception {
			HttpPost request = null;
			
			if (wp.getServerId()!=0) {
				request = new HttpPost("http://" + defaultIP + ":" + port + "/" + endpoint + "/" + "Workplaces("+ wp.getServerId()+ ")");
				request.setHeader("X-HTTP-Method", "PUT");
			}else {
				request = new HttpPost("http://" + defaultIP + ":" + port + "/" + endpoint + "/" + "Workplaces");
				request.setHeader("X-HTTP-Method-Override", "PUT");
			}
			
			
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json;odata=verbose");
			request.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(username, password), "UTF-8", false));

			String workplaceJSONString = "{\"odata.type\":\"OpenResKit.DomainModel.Workplace\",";
			
			workplaceJSONString += "\"SurveyType\":{\"__metadata\":{\"uri\": \"http://" + defaultIP + ":" + port + "/" + endpoint + "/Surveytypes("+ wp.getSurveyType().getId() +")\"}},";
			workplaceJSONString += "\"Description\":\"" +wp.getDescription()+ "\",";
			if (wp.getServerId()!=0) {
				workplaceJSONString += "\"Id\":\"" +wp.getServerId()+ "\",";
			}
			workplaceJSONString += "\"NameCompany\":\"" +wp.getNameCompany()+ "\",";
			workplaceJSONString += "\"Name\":\""+ wp.getName() +"\",";
					
			
			String assessmentIDJSONString = "[";
			int counter = 1;
			for (Assessment asId: wp.getAssessments()) 
			{
				assessmentIDJSONString += "{\"__metadata\":{\"uri\": \"http://" + defaultIP + ":" + port + "/" + endpoint + "/Assessments("+ asId.getServerId() +")\"}}";
				if (counter < wp.getAssessments().size()) 
				{
					assessmentIDJSONString += ",";
				}
				counter++;
			}

			assessmentIDJSONString += "]";
		
			workplaceJSONString += "\"Assessments\":"+ assessmentIDJSONString +",";
			
			
			String activityIDJSONString = "[";
			counter = 1;
			for (openreskit.danger.models.Activity asId: wp.getActivities()) 
			{
				activityIDJSONString += "{\"__metadata\":{\"uri\": \"http://" + defaultIP + ":" + port + "/" + endpoint + "/Activities("+ asId.getServerId() +")\"}}";
				if (counter < wp.getActivities().size()) 
				{
					activityIDJSONString += ",";
				}
				counter++;
			}

			activityIDJSONString += "]";

			workplaceJSONString += "\"Activities\":"+ activityIDJSONString +"}";
			
			

			request.setEntity(new StringEntity(workplaceJSONString,HTTP.UTF_8));
			  Log.i("JSON String",workplaceJSONString );
			return request;
		}
		
		private HttpPost NewUpdateAssessmentRequest(String defaultIP, String port,
				String username, String password, String endpoint,
				String collection,Assessment as) throws Exception {
			HttpPost request = null;
		
			if (as.getServerId()!=0) {
				request = new HttpPost("http://" + defaultIP + ":" + port + "/" + endpoint + "/" + "Assessments("+ as.getServerId()+ ")");
				request.setHeader("X-HTTP-Method", "PUT");
			}else {
				request = new HttpPost("http://" + defaultIP + ":" + port + "/" + endpoint + "/" + "Assessments");
				request.setHeader("X-HTTP-Method-Override", "PUT");
			}
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json;odata=verbose");
			request.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(username, password), "UTF-8", false));

			String assessmentJSONString = "{\"odata.type\":\"OpenResKit.DomainModel.Assessment\",";
			if (as.getServerId()!=0) {
				assessmentJSONString += "\"Id\":\"" +as.getServerId()+ "\",";
			}
			if (as.getEvaluatingPerson()!=null) {
			assessmentJSONString += "\"EvaluatingPerson\":{\"__metadata\":{\"uri\": \"http://" + defaultIP + ":" + port + "/" + endpoint + "/People("+ as.getEvaluatingPerson().getServerId() +")\"}},";
			}
			assessmentJSONString += "\"Status\":\"" +as.getStatus()+ "\",";
			assessmentJSONString += "\"AssessmentDate\":\"" +dfs.format(as.getAssessmentDate())+ "\",";
		
			
			String threatIDJSONString = "[";
			int counter = 1;
			for (Threat thId: as.getThreats()) 
			{
				threatIDJSONString += "{\"__metadata\":{\"uri\": \"http://" + defaultIP + ":" + port + "/" + endpoint + "/Threats("+ thId.getServerId() +")\"}}";
				if (counter < as.getThreats().size()) 
				{
					threatIDJSONString += ",";
				}
				counter++;
			}

			threatIDJSONString += "]";
		
			assessmentJSONString += "\"Threats\":"+ threatIDJSONString +"}";
			Log.i("JSON Request Assessment",assessmentJSONString );
			request.setEntity(new StringEntity(assessmentJSONString,HTTP.UTF_8));
		
		return request;
		}
		
		private HttpPost NewUpdateThreatRequest(String defaultIP, String port,
				String username, String password, String endpoint,
				String collection,Threat th) throws Exception {
			HttpPost request = null;
		
			if (th.getServerId()!=0) {
				request = new HttpPost("http://" + defaultIP + ":" + port + "/" + endpoint + "/" + "Threats("+ th.getServerId()+ ")");
				request.setHeader("X-HTTP-Method", "PUT");
			}else {
				request = new HttpPost("http://" + defaultIP + ":" + port + "/" + endpoint + "/" + "Threats");
				request.setHeader("X-HTTP-Method-Override", "PUT");
			}
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json;odata=verbose");
			request.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(username, password), "UTF-8", false));

			String threatJSONString = "{\"odata.type\":\"OpenResKit.DomainModel.Threat\",";
			if (th.getServerId()!=0) {
				threatJSONString += "\"Id\":\"" +th.getServerId()+ "\",";
			}
			threatJSONString += "\"GFactor\":{\"__metadata\":{\"uri\": \"http://" + defaultIP + ":" + port + "/" + endpoint + "/GFactors("+ th.getGFactor().getId() +")\"}},";
			
			if (th.getActivity()!=null) {
			threatJSONString += "\"Activity\":{\"__metadata\":{\"uri\": \"http://" + defaultIP + ":" + port + "/" + endpoint + "/Activities("+ th.getActivity().getServerId() +")\"}},";
			}
			if (th.getDangerpoint()!=null) {
				threatJSONString += "\"Dangerpoint\":{\"__metadata\":{\"uri\": \"http://" + defaultIP + ":" + port + "/" + endpoint + "/Dangerpoints("+ th.getDangerpoint().getId() +")\"}},";
			}
			
			threatJSONString += "\"Description\":\"" +th.getDescription()+ "\",";
			threatJSONString += "\"RiskDimension\":\"" +th.getRiskDimension()+ "\",";
			threatJSONString += "\"RiskPossibility\":\"" +th.getRiskPossibility()+ "\",";
			threatJSONString += "\"Status\":\"" +th.getStatus()+ "\",";
			threatJSONString += "\"Actionneed\":\"" +th.getActionneed()+ "\",";
			
		
			
			String pictureIDJSONString = "[";
			int counter = 1;
			for (Picture picId: th.getPictures()) 
			{
				pictureIDJSONString += "{\"__metadata\":{\"uri\": \"http://" + defaultIP + ":" + port + "/" + endpoint + "/Pictures("+ picId.getServerId() +")\"}}";
				if (counter < th.getPictures().size()) 
				{
					pictureIDJSONString += ",";
				}
				counter++;
			}

			pictureIDJSONString += "]";
			
			threatJSONString += "\"Pictures\":"+ pictureIDJSONString +",";
			
			
			String protectionGoalsIDJSONString = "[";
			counter = 1;
			for (ProtectionGoal pgId: th.getProtectionGoals()) 
			{
				protectionGoalsIDJSONString += "{\"__metadata\":{\"uri\": \"http://" + defaultIP + ":" + port + "/" + endpoint + "/ProtectionGoals("+ pgId.getServerId() +")\"}}";
				if (counter < th.getProtectionGoals().size()) 
				{
					protectionGoalsIDJSONString += ",";
				}
				counter++;
			}

			protectionGoalsIDJSONString += "]";
			
			threatJSONString += "\"ProtectionGoals\":"+ protectionGoalsIDJSONString +",";
			
			
			
			String actionsIDJSONString = "[";
			counter = 1;
			for (Action acId: th.getActions()) 
			{
				actionsIDJSONString += "{\"__metadata\":{\"uri\": \"http://" + defaultIP + ":" + port + "/" + endpoint + "/Actions("+ acId.getServerId() +")\"}}";
				if (counter < th.getActions().size()) 
				{
					actionsIDJSONString += ",";
				}
				counter++;
			}

			actionsIDJSONString += "]";
	
			threatJSONString += "\"Actions\":"+ actionsIDJSONString +"}";
			Log.i("JSON Request Threat ",threatJSONString );
			request.setEntity(new StringEntity(threatJSONString,HTTP.UTF_8));
		
		return request;
		}

		private HttpPost NewUpdateActivityRequest(String defaultIP, String port,
				String username, String password, String endpoint,
				String collection,openreskit.danger.models.Activity ac) throws Exception {
			HttpPost request = null;
		
			if (ac.getServerId()!=0) {
				request = new HttpPost("http://" + defaultIP + ":" + port + "/" + endpoint + "/" + "Activities("+ ac.getServerId()+ ")");
				request.setHeader("X-HTTP-Method", "PUT");
			}else {
				request = new HttpPost("http://" + defaultIP + ":" + port + "/" + endpoint + "/" + "Activities");
				request.setHeader("X-HTTP-Method-Override", "PUT");
			}
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json;odata=verbose");
			request.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(username, password), "UTF-8", false));

			String activityJSONString = "{\"odata.type\":\"OpenResKit.DomainModel.Workplace\",";
			if (ac.getServerId()!=0) {
				activityJSONString += "\"Id\":\"" +ac.getServerId()+ "\",";
			}
			activityJSONString += "\"Name\":\"" +ac.getName()+ "\"}";
			Log.i("JSON Request Activity ",activityJSONString );
			request.setEntity(new StringEntity(activityJSONString,HTTP.UTF_8));
		
		return request;
		}

		private HttpPost NewUpdateProtectionGoalRequest(String defaultIP, String port,
				String username, String password, String endpoint,
				String collection,ProtectionGoal pg) throws Exception {
			HttpPost request = null;
		
			if (pg.getServerId()!=0) {
				request = new HttpPost("http://" + defaultIP + ":" + port + "/" + endpoint + "/" + "ProtectionGoals("+ pg.getServerId()+ ")");
				request.setHeader("X-HTTP-Method", "PUT");
			}else {
				request = new HttpPost("http://" + defaultIP + ":" + port + "/" + endpoint + "/" + "ProtectionGoals");
				request.setHeader("X-HTTP-Method-Override", "PUT");
			}
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json;odata=verbose");
			request.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(username, password), "UTF-8", false));

			String pgJSONString = "{\"odata.type\":\"OpenResKit.DomainModel.ProtectionGoal\",";
			if (pg.getServerId()!=0) {
				pgJSONString += "\"Id\":\"" +pg.getServerId()+ "\",";
			}
			
			pgJSONString += "\"Description\":\"" +pg.getDescription()+ "\"}";
			Log.i("JSON Request ProtectionGoal ",pgJSONString );		
			request.setEntity(new StringEntity(pgJSONString,HTTP.UTF_8));
		return request;
		}
		
	
		private HttpPost NewUpdateActionRequest(String defaultIP, String port,
				String username, String password, String endpoint,
				String collection,Action ac) throws Exception {
			HttpPost request = null;
		
			if (ac.getServerId()!=0) {
				request = new HttpPost("http://" + defaultIP + ":" + port + "/" + endpoint + "/" + "Actions("+ ac.getServerId()+ ")");
				request.setHeader("X-HTTP-Method", "PUT");
			}else {
				request = new HttpPost("http://" + defaultIP + ":" + port + "/" + endpoint + "/" + "Actions");
				request.setHeader("X-HTTP-Method-Override", "PUT");
			}
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json;odata=verbose");
			request.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(username, password), "UTF-8", false));

			String actionJSONString = "{\"odata.type\":\"OpenResKit.DomainModel.Action\",";
			if (ac.getServerId()!=0) {
				actionJSONString += "\"Id\":\"" +ac.getServerId()+ "\",";
			}
			actionJSONString += "\"Person\":{\"__metadata\":{\"uri\": \"http://" + defaultIP + ":" + port + "/" + endpoint + "/People("+ ac.getPerson().getServerId() +")\"}},";
			actionJSONString += "\"Description\":\"" +ac.getDescription()+ "\",";
			actionJSONString += "\"Effect\":\"" +ac.getEffect()+ "\",";
			actionJSONString += "\"Execution\":\"" +ac.getExecution()+ "\",";
			actionJSONString += "\"DueDate\":\"" +dfs.format(ac.getDueDate())+ "\"";
		
			
			
			
			actionJSONString += "}";
			Log.i("JSON Request Action ",actionJSONString );	
			request.setEntity(new StringEntity(actionJSONString,HTTP.UTF_8));
		
		return request;
		}

		
		private HttpPost NewUpdatePictureRequest(String defaultIP, String port,
				String username, String password, String endpoint,
				String collection,Picture pic) throws Exception {
			HttpPost request = null;
			if (pic.getServerId()!=0) {
				request = new HttpPost("http://" + defaultIP + ":" + port + "/" + endpoint + "/" + "Pictures("+ pic.getServerId()+ ")");
				request.setHeader("X-HTTP-Method", "PUT");
			}else {
				request = new HttpPost("http://" + defaultIP + ":" + port + "/" + endpoint + "/" + "Pictures");
				request.setHeader("X-HTTP-Method-Override", "PUT");
			}

	
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json;odata=verbose");
			request.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(username, password), "UTF-8", false));

			String pictureJSONString = "{\"odata.type\":\"OpenResKit.DomainModel.Picture\",";
			if (pic.getServerId()!=0) {
				pictureJSONString += "\"Id\":\"" +pic.getServerId()+ "\",";
			}
			pictureJSONString += "\"Pic\":\"" +pic.getPic()+ "\"";

			pictureJSONString +="}";
			
			Log.i("JSON Request Picture ",pictureJSONString );	
			request.setEntity(new StringEntity(pictureJSONString,HTTP.UTF_8));
		return request;
		}
		
		
		private HttpPost NewUpdatePersonRequest(String defaultIP, String port,
				String username, String password, String endpoint,
				String collection,Person pe) throws Exception {
			HttpPost request = null;
		
			if (pe.getServerId()!=0) {
				request = new HttpPost("http://" + defaultIP + ":" + port + "/" + endpoint + "/" + "People("+ pe.getServerId()+ ")");
				request.setHeader("X-HTTP-Method", "PUT");
			}else {
				request = new HttpPost("http://" + defaultIP + ":" + port + "/" + endpoint + "/" + "People");
				request.setHeader("X-HTTP-Method-Override", "PUT");
			}
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json;odata=verbose");
			request.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(username, password), "UTF-8", false));

			String personJSONString = "{\"odata.type\":\"OpenResKit.DomainModel.Person\",";
			if (pe.getServerId()!=0) {
				personJSONString += "\"Id\":\"" +pe.getServerId()+ "\",";
			}
			personJSONString += "\"Name\":\"" +pe.getName()+ "\"";

			personJSONString +="}";
			Log.i("JSON Request Person ",personJSONString );	
			request.setEntity(new StringEntity(personJSONString,HTTP.UTF_8));
		return request;
		}
		
		
		
	}

	
	public class GetHubData extends AsyncTask<Void, Void, Boolean>{
		ORMLiteDBHelper ds; 
		private ProgressDialog progress;
		private SelectCompanyActivity activity;
		
		public GetHubData(String urlPort, Activity ctx) {
			super();

			String[] a = urlPort.split(";");
			url = a[0];
			port = a[1];
			mContext = ctx;
			ds = new ORMLiteDBHelper(mContext);
			this.activity = (SelectCompanyActivity) ctx;
			
			progress = new ProgressDialog(mContext);
		}
		
        
        protected void onPreExecute() {
            this.progress.setMessage("Empfange Daten vom Server");
            this.progress.show();
        }
		
		
		@Override
		protected Boolean doInBackground(Void... arg0) {

			String defaultIP = url;
			String username = "root";
			String password = "ork123";

			String expandFullData = "Workplaces,Persons,Workplaces/SurveyType,Workplaces/Assessments,Workplaces/Activities,Workplaces/Assessments/Threats,Workplaces/Assessments/EvaluatingPerson,Workplaces/Assessments/AssessmentDate,Workplaces/Assessments/Threats/Actions,Workplaces/Assessments/Threats/Actions/Person,Workplaces/Assessments/Threats/Actions/DueDate,Workplaces/Assessments/Threats/ProtectionGoals,Workplaces/Assessments/Threats/Pictures,Workplaces/Assessments/Threats/GFactor,Workplaces/Assessments/Threats/Activity,Workplaces/Assessments/Threats/Dangerpoint";
		
			JSONArray dataArray;
			try {
				dataArray = getJSONArrayFromOdata(defaultIP, port, username,
						password, "OpenResKitHub", "Companies", expandFullData,
						null);

				insertData(dataArray);
			
				writeOnDb();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
			

			return true;
		}
			
protected void onPostExecute(final Boolean success) {
    
			

            if (progress.isShowing()) {
            	progress.dismiss();
        }

        if (success) {
        	
        	activity.FillListWithCompanies();
            Toast.makeText(mContext, "OK", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext, "Error", Toast.LENGTH_LONG).show();
        }
    }
		
		
		
		private void insertData(JSONArray data) {
			JSONObject oneCompany = null;
			JSONObject onePerson = null;
			JSONObject oneWorkplace = null;
			JSONObject oneAssessment = null;
			JSONObject oneThreat = null;
			JSONObject onePicture = null;
			JSONObject oneProtectionGoal = null;
			JSONObject oneAction = null;
			JSONObject oneActivity = null;
			Map<Integer, Person> personMap = new HashMap<Integer, Person>();
			Map<Integer, openreskit.danger.models.Activity> activityMap = new HashMap<Integer, openreskit.danger.models.Activity>();
			ObjectMapper mapper = new ObjectMapper();

			try {
				for (int i = 0; i < data.length(); i++) {
	                oneCompany = data.getJSONObject(i);
	                Company company = mapper.readValue(oneCompany.toString(), Company.class);
	                mCompanys.add(company);
		            Log.i("JSON Parse", "Company hinzugefügt "+ company.getName());
		            JSONArray workplacesArray = oneCompany.getJSONArray("Workplaces");
		            JSONArray personsArray = oneCompany.getJSONArray("Persons");
		            for (int j = 0; j < personsArray.length(); j++ ) {
		            	onePerson = personsArray.getJSONObject(j);
		            	Person person = mapper.readValue(onePerson.toString(), Person.class);
		            	person.setId(UUID.randomUUID().toString());
		            	person.setCompany(company);
		            	mPersons.add(person);
		            	personMap.put(person.getServerId(), person);
		            	Log.i("JSON Parse", "Person hinzugefügt "+ person.getName());
		            }
		            for (int j = 0; j < workplacesArray.length(); j++ ) {
		            	oneWorkplace = workplacesArray.getJSONObject(j);
		            	Workplace workplace = mapper.readValue(oneWorkplace.toString(), Workplace.class);
		            	workplace.setId(UUID.randomUUID().toString());
		            	workplace.setCompany(company);
		            	mWorkplaces.add(workplace);
		            	Log.i("JSON Parse", "Workplace hinzugefügt "+ workplace.getName()+ " CompanyName "+ workplace.getCompany().getName() + "ServerID" + workplace.getServerId());
		             	JSONArray assessmentArray = oneWorkplace.getJSONArray("Assessments");
		            	JSONArray activityArray = oneWorkplace.getJSONArray("Activities");
		            	for (int k = 0; k < activityArray.length(); k++ ) {
		            		oneActivity = activityArray.getJSONObject(k);
		            		openreskit.danger.models.Activity activity = mapper.readValue(oneActivity.toString(), openreskit.danger.models.Activity.class);
		            		activity.setId(UUID.randomUUID().toString());
		            		activity.setWorkplace(workplace);
		            		mActivitys.add(activity);
		            		activityMap.put(activity.getServerId(), activity);
		            		Log.i("JSON Parse", "Activity hinzugefügt "+ activity.getName().toString());
		            	}
		            	for (int k = 0; k < assessmentArray.length(); k++ ) {
		            		oneAssessment = assessmentArray.getJSONObject(k);
		            		Assessment assessment = mapper.readValue(oneAssessment.toString(), Assessment.class);
		            		assessment.setId(UUID.randomUUID().toString());
		            		assessment.setWorkplace(workplace);
		            		if (assessment.getEvaluatingPerson() != null) {
		            			assessment.setEvaluatingPerson(personMap.get(assessment.getEvaluatingPerson().getServerId()));
		            		}		            		
		            		mAssessments.add(assessment);
		            		Log.i("JSON Parse", "Assessment hinzugefügt "+ assessment.getAssessmentDate().toString());
		
		            		JSONArray threatArray = oneAssessment.getJSONArray("Threats");
		            		for (int l = 0; l < threatArray.length(); l++) {
		            			oneThreat = threatArray.getJSONObject(l);
		            			Threat threat = mapper.readValue(oneThreat.toString(), Threat.class);
		            			threat.setId(UUID.randomUUID().toString());
		            			threat.setAssessment(assessment);
		            			if (threat.getActivity() != null) {	            				
		            				threat.setActivity(activityMap.get(threat.getActivity().getServerId()));
		            			}
		            			
		            			
		            			mThreats.add(threat);            		
		            			
		            			JSONArray pictureArray = oneThreat.getJSONArray("Pictures");
		            			JSONArray protectionGoalArray = oneThreat.getJSONArray("ProtectionGoals");
		            			JSONArray actionArray = oneThreat.getJSONArray("Actions");
		            			for (int m = 0; m<pictureArray.length(); m++) {
		            				onePicture = pictureArray.getJSONObject(m);
		            				Picture picture = mapper.readValue(onePicture.toString(), Picture.class);
		            				picture.setId(UUID.randomUUID().toString());
		            				picture.setThreat(threat);
		            				mPictures.add(picture);
		            				Log.i("JSON Parse", "Picture hinzugefügt ");
		            			}
		            			for (int m = 0; m<protectionGoalArray.length(); m++) {
		            				oneProtectionGoal = protectionGoalArray.getJSONObject(m);
		            				ProtectionGoal protectionGoal = mapper.readValue(oneProtectionGoal.toString(), ProtectionGoal.class);
		            				protectionGoal.setId(UUID.randomUUID().toString());
		            				protectionGoal.setThreat(threat);
		            				mProtectionGoals.add(protectionGoal);
		            				Log.i("JSON Parse", "ProtectionGoal hinzugefügt "+ protectionGoal.getDescription());
		            			}
		            			for (int m = 0; m<actionArray.length(); m++) {
		            				oneAction = actionArray.getJSONObject(m);
		            				Action action = mapper.readValue(oneAction.toString(), Action.class);
		            				action.setId(UUID.randomUUID().toString());
		            				action.setThreat(threat);
		            				if (action.getPerson() != null) {
				            		action.setPerson(personMap.get(action.getPerson().getServerId()));
				            		
		            				}

		            				mActions.add(action);
		            				Log.i("JSON Parse", "Action hinzugefügt "+ action.getDescription());
		            			}
		            			threat.setActions(mActions);
		            			threat.setProtectionGoals(mProtectionGoals);
		            			threat.setPictures(mPictures);
		            		}
		            		assessment.setThreats(mThreats);
		            	}
		            	workplace.setAssessments(mAssessments);
		            	workplace.setActivities(mActivitys);
		            }
		            company.setWorkplace(mWorkplaces);
		            company.setPerson(mPersons);
		            }
				

			} catch (JsonParseException e) {
				// TODO Auto-generated catch bl
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
			personMap.clear();
			activityMap.clear();
		}

		private void writeOnDb() {
		
			try {
				for (Company cp : mCompanys) {
					ds.getCompanyDao().createOrUpdate(cp);
				}
				for (Person person : mPersons) {
					ds.getPersonDao().createOrUpdate(person);
				}
				for (Workplace wp : mWorkplaces) {
					ds.getWorkplaceDao().createOrUpdate(wp);
				}
				for (openreskit.danger.models.Activity act : mActivitys) {
					ds.getActivityDao().createOrUpdate(act);
				}
				
				for (Assessment as : mAssessments) {
					ds.getAssessmentDao().createOrUpdate(as);
				}
				for (Threat threat : mThreats) {
					ds.getThreatDao().createOrUpdate(threat);
				}
				for (Picture pic : mPictures) {
					ds.getPictureDao().createOrUpdate(pic);
				}
				for (ProtectionGoal pg : mProtectionGoals) {
					ds.getProtectionGoalDao().createOrUpdate(pg);
				}
				for (Action act : mActions) {
					ds.getActionDao().createOrUpdate(act);
				}
			} catch (Exception ex) {

			}

		}

		private JSONArray getJSONArrayFromOdata(String ip, String port,
				String username, String password, String endpoint,
				String collection, String expand, String filter) throws Exception {
			JSONArray returnJSONArray = null;
			String jsonText = null;
			String uriString = null;
			try {
				HttpParams httpParams = new BasicHttpParams();
				HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
				httpParams.setBooleanParameter("http.protocol.expect-continue",
						false);
				HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
				if (filter == null) {
					uriString = "http://" + ip + ":" + port + "/" + endpoint + "/"
							+ collection + "/?$format=json&$expand=" + expand;
				} else {
					uriString = "http://" + ip + ":" + port + "/" + endpoint + "/"
							+ collection + "/?$format=json&$expand=" + expand
							+ "&$filter=" + filter;
				}
				HttpGet request = new HttpGet(uriString);
				request.setHeader("Accept", "application/json");
				request.setHeader("Content-type", "application/json");
				request.addHeader(BasicScheme.authenticate(
						new UsernamePasswordCredentials(username, password),
						"UTF-8", false));
				HttpClient httpClient = new DefaultHttpClient(httpParams);

				HttpResponse response = httpClient.execute(request);
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						// InputStream instream = entity.getContent();

						jsonText = EntityUtils.toString(entity, HTTP.UTF_8);

						// instream.close();
					}
					returnJSONArray = new JSONObject(jsonText)
							.getJSONArray("value");
				} else if (response.getStatusLine().getStatusCode() == 403) {
					Exception e1 = new AuthenticationException(
							"Der Benutzername oder das Passwort für die Authentifizierung am OData Service sind nicht korrekt");
					throw e1;
				}
			} catch (Exception e) {
				throw e;
			}
			return returnJSONArray;
		}

	}


}
