package openreskit.danger.persistence;
import java.sql.SQLException;
import java.util.UUID;

import openreskit.danger.R;
import openreskit.danger.models.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class ORMLiteDBHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "Danger.db";
	private static final int DATABASE_VERSION = 1;

	// the DAO object we use to access the SimpleData table
	private Dao<Action, UUID> actionDao = null;
	private Dao<Activity, UUID> activityDao = null;
	private Dao<Assessment, UUID> assessmentDao = null;
	private Dao<Category, Integer> categoryDao = null;
	private Dao<Company, Integer> companyDao = null;
	private Dao<Dangerpoint, UUID> dangerpointsDao = null;
	private Dao<GFactor, UUID> gfactorDao = null;
	private Dao<Person, UUID> personDao = null;
	private Dao<Picture, UUID> pictureDao = null;
	private Dao<ProtectionGoal, UUID> protectionGoalDao = null;
	private Dao<Question, Integer> questionDao = null;
	private Dao<Surveytype, Integer> surveytypeDao = null;
	private Dao<Threat, UUID> threatDao = null;
	private Dao<Workplace, UUID> workplaceDao = null;

	


	public ORMLiteDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
	}

	/**
	 * This is called when the database is first created. Usually you should call createTable statements here to create
	 * the tables that will store your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(ORMLiteDBHelper.class.getName(), "onCreate");
			long millisbefore = System.currentTimeMillis();
			TableUtils.createTable(connectionSource, Company.class);
			Log.i(ORMLiteDBHelper.class.getName(), "Created Company");

			TableUtils.createTable(connectionSource, Person.class);
			TableUtils.createTable(connectionSource, Surveytype.class);
			TableUtils.createTable(connectionSource, Category.class);
			Log.i(ORMLiteDBHelper.class.getName(), "Created Categroy");
			TableUtils.createTable(connectionSource, GFactor.class);
			TableUtils.createTable(connectionSource, Question.class);
			TableUtils.createTable(connectionSource, Workplace.class);
			Log.i(ORMLiteDBHelper.class.getName(), "Created Assessment");

			TableUtils.createTable(connectionSource, Assessment.class);
			
			TableUtils.createTable(connectionSource, Threat.class);


			TableUtils.createTable(connectionSource, Picture.class);
			TableUtils.createTable(connectionSource, ProtectionGoal.class);
			TableUtils.createTable(connectionSource, Action.class);
			Log.i(ORMLiteDBHelper.class.getName(), "Created Action");

			
			TableUtils.createTable(connectionSource, Dangerpoint.class);
			Log.i(ORMLiteDBHelper.class.getName(), "Created Dangerpoints");
			
			TableUtils.createTable(connectionSource, Activity.class);
			Log.i(ORMLiteDBHelper.class.getName(), "Created Activity");
			
			long millisafter = System.currentTimeMillis();
			long millis = millisafter-millisbefore;
			Log.i(ORMLiteDBHelper.class.getName(), "created new entries in onCreate: "+millis);
		} catch (Exception e) {
			Log.e(ORMLiteDBHelper.class.getName(), "Kann keine Datenbank erstellen", e);
		}

	}



	//TODO Datenbankupgrade
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		
			Log.i(ORMLiteDBHelper.class.getName(), "onUpgrade");
		
	}

	/**
	 * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
	 * value.
	 */
	public Dao<Action, UUID> getActionDao() throws SQLException {
		if (actionDao == null) {
			actionDao = getDao(Action.class);
		}
		return actionDao;
	}
	
	public Dao<Assessment, UUID> getAssessmentDao() throws SQLException {
		if (assessmentDao == null) {
			assessmentDao = getDao(Assessment.class);
		}
		return assessmentDao;
	}

	public Dao<Category, Integer> getCategoryDao() throws SQLException {
		
		if (categoryDao == null) {
			categoryDao = getDao(Category.class);
		}return categoryDao;
	}

	public Dao<Company, Integer> getCompanyDao() throws SQLException{
		if (companyDao == null) {
			companyDao = getDao(Company.class);
		}return companyDao;
	}

	public Dao<GFactor, UUID> getGfactorDao() throws SQLException{
		if (gfactorDao == null) {
			gfactorDao = getDao(GFactor.class);
		}return gfactorDao;
	}

	public Dao<Person, UUID> getPersonDao() throws SQLException{
		if (personDao == null) {
			personDao = getDao(Person.class);
		}return personDao;
	}

	public Dao<Picture, UUID> getPictureDao() throws SQLException{
		if (pictureDao == null) {
			pictureDao = getDao(Picture.class);
		}return pictureDao;
	}

	public Dao<ProtectionGoal,UUID> getProtectionGoalDao() throws SQLException{
		if (protectionGoalDao == null) {
			protectionGoalDao = getDao(ProtectionGoal.class);
		}return protectionGoalDao;
	}

	public Dao<Question, Integer> getQuestionDao() throws SQLException{
		if (questionDao == null) {
			questionDao = getDao(Question.class);
		}return questionDao;
	}

	public Dao<Surveytype, Integer> getSurveytypeDao() throws SQLException{
		if (surveytypeDao == null) {
			surveytypeDao = getDao(Surveytype.class);
		}return surveytypeDao;
	}

	public Dao<Threat, UUID> getThreatDao() throws SQLException{
		if (threatDao == null) {
			threatDao = getDao(Threat.class);
		}return threatDao;
	}

	public Dao<Workplace, UUID> getWorkplaceDao() throws SQLException{
		if (workplaceDao == null) {
			workplaceDao = getDao(Workplace.class);
		}return workplaceDao;
	}

	
	
	public Dao<Dangerpoint, UUID> getDangerpointDao() throws SQLException
	{
		if(dangerpointsDao == null) {
			dangerpointsDao = getDao(Dangerpoint.class);
		}return dangerpointsDao;
	}
	
	public Dao<Activity, UUID> getActivityDao() throws SQLException
	{
		if(activityDao == null){
			activityDao = getDao(Activity.class);
		}return activityDao;
	}
	
	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		actionDao = null;
		assessmentDao = null;
		categoryDao = null;
		companyDao = null;
		gfactorDao = null;
		personDao = null;
		pictureDao = null;
		protectionGoalDao = null;
		questionDao = null;
		surveytypeDao = null;
		threatDao = null;
		workplaceDao = null;

		dangerpointsDao = null;
		activityDao = null;
		Log.i("Database Closed", super.getClass().getName().toString());
		
	}
}


