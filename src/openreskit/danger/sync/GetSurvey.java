package openreskit.danger.sync;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import openreskit.danger.models.Action;
import openreskit.danger.models.Assessment;
import openreskit.danger.models.Category;
import openreskit.danger.models.Company;
import openreskit.danger.models.Dangerpoint;
import openreskit.danger.models.GFactor;
import openreskit.danger.models.Person;
import openreskit.danger.models.Picture;
import openreskit.danger.models.ProtectionGoal;
import openreskit.danger.models.Question;
import openreskit.danger.models.Surveytype;
import openreskit.danger.models.Threat;
import openreskit.danger.models.Workplace;

import openreskit.danger.persistence.ORMLiteDBHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class GetSurvey extends AsyncTask <Void,Void,Boolean> 
{
	
		private ProgressDialog progress;
		
		private List<Surveytype> surveytypes = new ArrayList<Surveytype>();
        private List<Category> categories = new ArrayList<Category>();
        private List<GFactor> gfactors = new ArrayList<GFactor>();
        private List<Question> questions = new ArrayList<Question>();
        private List<Dangerpoint> dangerpoints = new ArrayList<Dangerpoint>();

        
        private String url;
        private String port;
        private String username;
        private String password;
        
        private Activity mContext;
 
        public GetSurvey(String urlPort, Activity context) 


        {
			super();
			
			
			String[] charsForTxtfile = urlPort.split(";");
			url = charsForTxtfile[0];
			port = charsForTxtfile[1];
			username = charsForTxtfile[2];
			password = charsForTxtfile[3];
			
			progress = new ProgressDialog(context);
			
			mContext = context;
		}
        
        protected void onPreExecute() {
            this.progress.setMessage("Lade Fragebögen herunter");
            this.progress.show();
        }

		protected Boolean doInBackground(Void... arg0) 
		{
			
			String defaultIP = url;

			try {
				

				String expandSurvey = "Categories,Categories/GFactors,Categories/GFactors/Questions,Categories/GFactors/Dangerpoints";
				JSONArray surveyArray = getJSONArrayFromOdata(defaultIP, port, username, password, "OpenResKitHub", "Surveytypes", expandSurvey, null);
				insertSurvey(surveyArray);

				writeOnDb();
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
		
		private void writeOnDb() {
			ORMLiteDBHelper ds = new ORMLiteDBHelper(mContext);
						
			for (Surveytype st: surveytypes) {
				try {
					ds.getSurveytypeDao().createOrUpdate(st);
					}
				catch (Exception ex) 
				{
						
				}
			}
	        
			for(Category cd : categories)
			{
			
				try
				{		
					ds.getCategoryDao().createOrUpdate(cd);
				} 
				catch (Exception ex) 
				{
						
				}
			}
			for(GFactor gf : gfactors)
			{
				try
				{
					ds.getGfactorDao().createOrUpdate(gf);
				} 
				catch (Exception ex) 
				{
						
				}
			}
			for (Question question : questions) 
			{
				try {
					ds.getQuestionDao().createOrUpdate(question);
				} catch (Exception e) {

				}
			}
			for (Dangerpoint dangerpoint : dangerpoints) 
			{
				try {
					ds.getDangerpointDao().createOrUpdate(dangerpoint);
				} catch (Exception e) {

				}
			}
			
			
			ds.close();
            
			
		}

		private void insertSurvey (JSONArray data) {
	        JSONObject oneSurvey = null;
	        JSONObject oneCategory = null;
	        JSONObject oneGFactor = null;
	        JSONObject oneQuestion = null;
	        JSONObject oneDangerpoint = null;
	        
	        ObjectMapper mapper = new ObjectMapper();
	        try {	        
	        for (int i = 0; i < data.length(); i++) {
	            
	                oneSurvey = data.getJSONObject(i);
	                Surveytype surveytype = mapper.readValue(oneSurvey.toString(), Surveytype.class);
		            surveytypes.add(surveytype);
		            Log.i("JSON Parse", "Surveytype hinzugefügt "+ surveytype.getName());
		            JSONArray categoriesArray = oneSurvey.getJSONArray("Categories");
        	        for(int j = 0; j < categoriesArray.length(); j++){
        	        	oneCategory = categoriesArray.getJSONObject(j);
        	        	Category category = mapper.readValue(oneCategory.toString(),Category.class);
        	        	category.setSurveytype(surveytype);
		                categories.add(category);
		                Log.i("JSON Parse", "Category hinzugefügt "+ category.getName() + category.getSurveytype().getName());
		                JSONArray gfactorsArray = oneCategory.getJSONArray("GFactors");
		                for(int k = 0; k< gfactorsArray.length();k++) {
		                	oneGFactor = gfactorsArray.getJSONObject(k);
			            	GFactor gfactor = mapper.readValue(oneGFactor.toString(),GFactor.class);
				            gfactor.setCategory(category);
			            	gfactors.add(gfactor);	
			            	Log.i("JSON Parse", "GFactor hinzugefügt "+ gfactor.getName() + gfactor.getCategory().getName());
			            	
			            	JSONArray questionArray = oneGFactor.getJSONArray("Questions");
				            JSONArray dangerpointArray = oneGFactor.getJSONArray("Dangerpoints");
				            for(int l = 0; l< questionArray.length();l++) {
				            	oneQuestion = questionArray.getJSONObject(l);
				            	Question question = mapper.readValue(oneQuestion.toString(),Question.class);
					            question.setGfactor(gfactor);
				            	questions.add(question);
				            	Log.i("JSON Parse", "Question hinzugefügt " + question.getQuestionName() + question.getGfactor().getName());
				            }
				            for(int l = 0; l< dangerpointArray.length();l++) {
				            	oneDangerpoint = dangerpointArray.getJSONObject(l);
				            	Dangerpoint dangerpoint = mapper.readValue(oneDangerpoint.toString(),Dangerpoint.class);
				            	dangerpoint.setGfactor(gfactor);
				            	dangerpoints.add(dangerpoint);
				            	Log.i("JSON Parse", "Dangerpoint hinzugefügt " + dangerpoint.getName() + dangerpoint.getGfactor().getName());
				            }
		                } 
	       	        }
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
		}
		


		private JSONArray getJSONArrayFromOdata(String ip, String port, String username, String password, String endpoint, String collection, String expand, String filter) throws Exception
		{
			JSONArray returnJSONArray = null;
			String jsonText = null;
			String uriString = null;
			try {
				HttpParams httpParams = new BasicHttpParams();
				HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
				httpParams.setBooleanParameter("http.protocol.expect-continue", false);
				HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
				if (filter == null) {
					uriString = "http://"+ip+":"+ port +"/" + endpoint +"/"+ collection +"/?$format=json&$expand=" + expand;
				} 
				else
				{
					uriString = "http://"+ ip +":"+ port +"/" + endpoint +"/"+ collection +"/?$format=json&$expand=" + expand + "&$filter="+ filter;
				}
				HttpGet request = new HttpGet(uriString);
				request.setHeader("Accept", "application/json");
				request.setHeader("Content-type", "application/json");
				request.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(username, password), "UTF-8", false));
				HttpClient httpClient = new DefaultHttpClient(httpParams);
				
				HttpResponse response = httpClient.execute(request);
		        if(response.getStatusLine().getStatusCode() == 200){
		            HttpEntity entity = response.getEntity();
		            if (entity != null) {
		                //InputStream instream = entity.getContent();
		                
		                jsonText = EntityUtils.toString(entity, HTTP.UTF_8);

		                //instream.close();
		            }
		            returnJSONArray  = new JSONObject(jsonText).getJSONArray("value");
		        }
		        else if (response.getStatusLine().getStatusCode() == 403) 
		        {
		        	Exception e1 = new AuthenticationException("Der Benutzername oder das Passwort für die Authentifizierung am OData Service sind nicht korrekt");
		        	throw e1; 
		        }
			}
			catch (Exception e) 
			{
				throw e;
			}
	        return returnJSONArray;
		}

		
		}

