package openreskit.danger.dummy;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.provider.SyncStateContract.Helpers;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.android.apptools.OrmLiteBaseService;
import com.j256.ormlite.dao.Dao;

import openreskit.danger.models.Assessment;
import openreskit.danger.models.Category;
import openreskit.danger.models.Company;
import openreskit.danger.models.GFactor;
import openreskit.danger.models.Question;
import openreskit.danger.models.Surveytype;
import openreskit.danger.models.Workplace;

import openreskit.danger.persistence.ORMLiteDBHelper;

public class LoadDummy  {

	ORMLiteDBHelper helper;
	/**
	 * Loads all Dummy-Data into the database = new ArrayList<Company>();
	 * @param ds
	 */
	public void LoadDummyDataInDb(Activity context)
	{
		helper = new ORMLiteDBHelper(context);
		
		List<Company> companyListDummy = DummyContent.DUMMY_COMPANYS;
//		List<Workplace> workplaceDummy = DummyContent.DUMMY_WORKPLACES;
//		List<Assessment> assessmentDummy = DummyContent.DUMMY_ASSESSMENT;
//		List<Category> categoryDummy = DummyContent.DUMMY_CATEGORY;
//		List<Surveytype> surveytypeDummy = DummyContent.DUMMY_SURVEYTYPE;
//		List<GFatctor> gFactorDummy = DummyContent.DUMMY_GFACTOR;
//		List<Question> questionDummy = DummyContent.DUMMY_QUESTION;
		
			
			for (Company c : companyListDummy) {
				try {
					helper.getCompanyDao().create(c);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
			}
				
//			ds.Open();
//			for (Workplace w : workplaceDummy) 
//			{	
//				try
//				{						
//					ds.InsertWorkplaceInDb1(w);
//				}
//				catch(Exception ex)
//				{
//						
//				}
//					
//			}
//			ds.Close();
//				
//			ds.Open();
//			for(Assessment a : assessmentDummy)
//			{
//				try
//				{
//					ds.InsertAssessmentInDb(a);
//				} 
//				catch (Exception ex) 
//				{
//						
//				}
//			}
//			ds.Close();
//			
//			ds.Open();
//			for(Category cd : categoryDummy)
//			{
//				try
//				{
//					ds.InsertCategoryInDb(cd);
//				} 
//				catch (Exception ex) 
//				{
//						
//				}
//			}
//			ds.Close();
//		
//			ds.Open();
//			for(Surveytype st : surveytypeDummy)
//			{
//				try
//				{
//					ds.InsertSurveytypeInDb(st);
//				} 
//				catch (Exception ex) 
//				{
//						
//				}
//			}
//			ds.Close();
//			
//			ds.Open();
//			
//				try
//				{
//					ds.InsertSurveytypeCategoryInDb(surveytypeDummy.get(0), categoryDummy.get(0));
//					ds.InsertSurveytypeCategoryInDb(surveytypeDummy.get(0), categoryDummy.get(1));
//					ds.InsertSurveytypeCategoryInDb(surveytypeDummy.get(0), categoryDummy.get(2));
//					ds.InsertSurveytypeCategoryInDb(surveytypeDummy.get(0), categoryDummy.get(3));
//					//ds.InsertSurveytypeCategoryInDb(surveytypeDummy.get(0), categoryDummy.get(4));
//					
//					ds.InsertSurveytypeCategoryInDb(surveytypeDummy.get(1), categoryDummy.get(7));
//					ds.InsertSurveytypeCategoryInDb(surveytypeDummy.get(1), categoryDummy.get(8));
//					ds.InsertSurveytypeCategoryInDb(surveytypeDummy.get(1), categoryDummy.get(9));
//					ds.InsertSurveytypeCategoryInDb(surveytypeDummy.get(1), categoryDummy.get(10));
//					ds.InsertSurveytypeCategoryInDb(surveytypeDummy.get(1), categoryDummy.get(11));
//				} 
//				catch (Exception ex) 
//				{
//						
//				}
//			ds.Close();
//			
//			ds.Open();
//			for(GFatctor gf : gFactorDummy)
//			{
//				try
//				{
//					ds.InsertGFactorInDb(gf);
//				}
//				catch(Exception ex)
//				{
//					
//				}
//			}
//			ds.Close();
//			
//			ds.Open();
//			for (Question question : questionDummy) 
//			{
//				try {
//					ds.InsertQuestionInDb(question);
//				} catch (Exception e) {
//
//				}
//			}
//			ds.Close();
		}	
}
