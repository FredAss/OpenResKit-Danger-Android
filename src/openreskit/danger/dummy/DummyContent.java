package openreskit.danger.dummy;

import java.util.ArrayList;
import java.util.List;
import openreskit.danger.models.Assessment;
import openreskit.danger.models.Category;
import openreskit.danger.models.Company;
import openreskit.danger.models.GFactor;
import openreskit.danger.models.Question;
import openreskit.danger.models.Surveytype;
import openreskit.danger.models.Workplace;


public class DummyContent {

	public static List<Company> DUMMY_COMPANYS = new ArrayList<Company>();
//	public static List<Workplace> DUMMY_WORKPLACES = new ArrayList<Workplace>();
//	public static List<Assessment> DUMMY_ASSESSMENT = new ArrayList<Assessment>();
//	public static List<Category> DUMMY_CATEGORY = new ArrayList<Category>();
//	public static List<Surveytype> DUMMY_SURVEYTYPE = new ArrayList<Surveytype>();
//	public static List<GFatctor> DUMMY_GFACTOR = new ArrayList<GFatctor>();
//	public static List<Question> DUMMY_QUESTION = new ArrayList<Question>();
//	
	// Dummy-Data for Company-Table
	private static Company  c1 = new Company(1,"GfBU", "Hönow", "+49 123 456 789",null,null,null);
	private static Company  c2 = new Company(2,"Musterunternehmen", "Berlin", "+49 1337",null,null,null);
	private static Company  c3 = new Company(3,"HTW gmbh", "Schöneweide", "+49 192837465",null,null,null);
//	
//	// Dummy-Data for Workplace-Table
//	private static Workplace w1 = new Workplace(1, "CAP1", "CAP1U", "Computerarbeitsplatz 1", "F214", c1, null);
//	private static Workplace w2 = new Workplace(2, "CAP2", "CAP2U", "Computerarbeitsplatz 2", "F214", c1, null);
//	private static Workplace w3 = new Workplace(3, "DRB1", "DRB1U", "Drehbank", "Werkstatt", c1, null);	
//	
//	// Dummy-Data for Assessment-Table
//	private static Assessment a1 = new Assessment(1, w1);
//	private static Assessment a2 = new Assessment(2, w1);
//	private static Assessment a3 = new Assessment(3, w2);
//	
//	//Dummy-Data for Category-Table
////	private static Category cd1 = new Category(1, "Mechanische Gefährdung");
////	private static Category cd2 = new Category(2, "Elektrische Gefährdung");
////	private static Category cd3 = new Category(3, "Gefahrstoffe");
////	private static Category cd4 = new Category(4, "Biologische Gefährdungen");
////	private static Category cd5 = new Category(5, "Brand- und Explosionsgefährdung");
////	private static Category cd6 = new Category(6, "Thermische Gefährdung");
////	private static Category cd7 = new Category(7, "Gefährdung durch spezielle physikalische Einwirkungen");
////	private static Category cd8 = new Category(8, "Gefährdung durch Arbeitsumgebungsbedingungen");
////	private static Category cd9 = new Category(9, "Physikalische Belastung/Arbeitsschwere");
////	private static Category cd10 = new Category(10, "Wahrnehmung und Handhabbarkeit");
////	private static Category cd11 = new Category(11, "Sonstige Gefährdungen");
////	private static Category cd12 = new Category(12, "Psychische Belastung");
////	private static Category cd13 = new Category(13, "Organisation");
//	
//	private static Surveytype st1 = new Surveytype(1, "Allgemeiner Fragebogen");
//	private static Surveytype st2 = new Surveytype(2, "Besonderer Fragebogen");
//
//	
//	private static GFatctor gf1 = new GFatctor(1, "1.1", "Ungeschützte bewegliche Maschinenteile", cd1);
//	private static GFatctor gf2 = new GFatctor(2, "1.2", "Teile mit gefährlichen Oberflächen", cd1);
//	private static GFatctor gf3 = new GFatctor(3, "2.1", "gefährliche Körperströme", cd2);
//	private static GFatctor gf4 = new GFatctor(4, "2.2", "Lichtbögen", cd2);	
//	private static GFatctor gf5 = new GFatctor(5, "3.1", "Gase", cd3);
//	private static GFatctor gf6 = new GFatctor(6, "3.2", "Dämpfe", cd3);
//	private static GFatctor gf7 = new GFatctor(7, "4.1", "Infektionsgefahr durch Mikroorganismen, Viren oder biologische Arbeitsstoffe", cd4);
//	private static GFatctor gf8 = new GFatctor(8, "4.2", "gentechnisch veränderte Organismen (GVO)", cd4);
//	private static GFatctor gf9 = new GFatctor(9, "8.1", "Klima", cd8);
//	private static GFatctor gf10 = new GFatctor(10, "8.2", "Beleuchtung", cd8);
//	private static GFatctor gf11 = new GFatctor(11, "9.1", "Schwere dynamische Arbeit (dynamische Ganzkörperarbeit", cd9);
//	private static GFatctor gf12 = new GFatctor(12, "9.2", "einseitige dynamische Arbeit", cd9);
//	private static GFatctor gf13 = new GFatctor(13, "10.1", "Informationsaufnahme", cd10);
//	private static GFatctor gf14 = new GFatctor(14, "10.2", "Wahrnehmungsumfang", cd10);
//	
//	private static Question q1 = new Question(1, "Sind die Sicherheitsabstände eingehalten?", gf1);
//	private static Question q2 = new Question(2, "Sind die Gefahrstellen ausreichend gesichert?", gf1);
//	private static Question q3 = new Question(3, "Ist der Kontakt zu scharfkantigen, spitzen oder rauen Teilen verhindert (durch Nutzung technischer Hilfsmittel, trennende Schutzeinrichtungen, ausreichende Bewegungsräume am Arbeitsplatz, ausreichende Wahrnehmbarkeit, PSA)?", gf2);
//	private static Question q4 = new Question(4, "Bestehen lichtdurchlässige Flächen von türen aus bruchsicherem Werkstoff?", gf2);
//	private static Question q5 = new Question(5, "Sind die Betriebsmittel entsprechend den Betriebsbedingungen und den äußeren Einflüssen ausgewählt (z. B. IP-Schutzarten, mechanischer Schutz)?", gf3);
//	private static Question q6 = new Question(6, "Werden die elektrischen Betriebsmittel bestimmungsgemäß verwendet?", gf3);
//	private static Question q7 = new Question(7, "siehe Ziffer 2.1", gf4);
//	private static Question q8 = new Question(8, "Werden bei Schalthandlungunter Last PSA benutzt?", gf4);
//	private static Question q9 = new Question(9, "Wurde geprüft, ob mit gefährlichen Stoffen, Zubereitungen oder Erzeugnissen umgegangen wird?", gf5);
//	private static Question q10 = new Question(10, "Wurde geprüft, ob verfahrensbedingt Gefahrstoffe entstehen können?", gf5);
//	private static Question q11 = new Question(11, "Wurde geprüft, ob mit gefährlichen Stoffen, Zubereitungen oder Erzeugnissen umgegangen wird?", gf6);
//	private static Question q12 = new Question(12, "Wurde geprüft, ob verfahrensbedingt Gefahrstoffe entstehen können?", gf6);
//	private static Question q13 = new Question(13, "Können Beschäftigte bei ihrer Tätigkeit beabsichtigt oder unbeabsichtigt mit krankheitserregenden biologischen Arbeitsstoffen in Berührung kommen (z. B. bei Tätigkeiten im Bereich Biotechnologie, in Krankenhäusern und Arztpraxen, in der Landwirtschaft, in der Abwasser- oder Abfallwirtschaft)?", gf7);
//	private static Question q14 = new Question(14, "Wurden Betriebsanweisungen erstellt und werden die Beschäftigten unterwiesen?", gf7);
//	
//	private static Question q15 = new Question(15, "Entspricht der Messwert dem geforderten Mindestwert der ArbStättV (in Abhängigkeit von der Arbeitsschwere), siehe folgende Tabelle?", gf9);
//	private static Question q16 = new Question(16, "Werden Raumtemperaturen > 26°C vermieden?", gf9);
//	private static Question q17 = new Question(17, "Haben Beschäftigte an ständigen Arbeitsplätzen eine Sichtverbindung nach außen?", gf10);
//	private static Question q18 = new Question(18, "Liegen die Messwerte über der vorgegebenen Nennbeleuchtungsstärke nach DIN 5035-2 am Arbeitsplatz (in Abhängigkeit von der Sehaufgabe)?", gf10);
//	private static Question q19 = new Question(19, "Bewegungen des ganzen Körpers (> 1/7 der gesamten Skelettmuskelmasse)", gf11);
//	private static Question q20 = new Question(20, "Werden die Richtwerte für den Arbeitsenergieumsatz (AEU) für Frauen 12 kJ/min und für Männer 16 kJ/min unterschritten?", gf11);
//	private static Question q21 = new Question(21, "Einsatz kleiner Muskelgruppen (< 1/7 der gesamten Muskelmasse, z. B. ein Fuß, ein Arm, ein Bein, Finder unter Bewegung der Unterarme)", gf12);
//	private static Question q22 = new Question(22, "Werden kraftaufwändige Findertätigkeiten mit hoher Bewegungsfrequenz vermieden?", gf12);
//	private static Question q23 = new Question(23, "Sind die optischen Signalgeber ausreichend wahrnehmbar und ist deren Informationsgehalt verständlich?", gf13);
//	private static Question q24 = new Question(24, "Sind dir Informationselemente nach Funktion und Bedeutung gruppiert?", gf13);
//	private static Question q25 = new Question(25, "Wird verhindert, dass sehr viele Informationen auf einmal aufgenommen werden müssen?", gf14);
//	private static Question q26 = new Question(26, "Werden reine Überwachungstätigkeiten vermieden (z. B. Unterbrechung durch aktives Handeln)?", gf14);
//	
	static 
	{
		DUMMY_COMPANYS.add(c1);
		DUMMY_COMPANYS.add(c2);
		DUMMY_COMPANYS.add(c3);
//		
//		DUMMY_WORKPLACES.add(w1);
//		DUMMY_WORKPLACES.add(w2);
//		DUMMY_WORKPLACES.add(w3);
//		
//		DUMMY_ASSESSMENT.add(a1);
//		DUMMY_ASSESSMENT.add(a2);
//		DUMMY_ASSESSMENT.add(a3);
//		
//		DUMMY_CATEGORY.add(cd1);
//		DUMMY_CATEGORY.add(cd2);
//		DUMMY_CATEGORY.add(cd3);
//		DUMMY_CATEGORY.add(cd4);
//		DUMMY_CATEGORY.add(cd5);
//		DUMMY_CATEGORY.add(cd6);
//		DUMMY_CATEGORY.add(cd7);
//		DUMMY_CATEGORY.add(cd8);
//		DUMMY_CATEGORY.add(cd9);
//		DUMMY_CATEGORY.add(cd10);
//		DUMMY_CATEGORY.add(cd11);
//		DUMMY_CATEGORY.add(cd12);
//		DUMMY_CATEGORY.add(cd13);
//		
//		DUMMY_SURVEYTYPE.add(st1);
//		DUMMY_SURVEYTYPE.add(st2);
//		
//		
//		DUMMY_GFACTOR.add(gf1);
//		DUMMY_GFACTOR.add(gf2);
//		DUMMY_GFACTOR.add(gf3);
//		DUMMY_GFACTOR.add(gf4);
//		DUMMY_GFACTOR.add(gf5);
//		DUMMY_GFACTOR.add(gf6);
//		DUMMY_GFACTOR.add(gf7);
//		DUMMY_GFACTOR.add(gf8);
//		DUMMY_GFACTOR.add(gf9);
//		DUMMY_GFACTOR.add(gf10);
//		DUMMY_GFACTOR.add(gf11);
//		DUMMY_GFACTOR.add(gf12);
//		DUMMY_GFACTOR.add(gf13);
//		DUMMY_GFACTOR.add(gf14);
//		
//		DUMMY_QUESTION.add(q1);
//		DUMMY_QUESTION.add(q2);
//		DUMMY_QUESTION.add(q3);
//		DUMMY_QUESTION.add(q4);
//		DUMMY_QUESTION.add(q5);
//		DUMMY_QUESTION.add(q6);
//		DUMMY_QUESTION.add(q7);
//		DUMMY_QUESTION.add(q8);
//		DUMMY_QUESTION.add(q9);
//		DUMMY_QUESTION.add(q10);
//		DUMMY_QUESTION.add(q11);
//		DUMMY_QUESTION.add(q12);
//		DUMMY_QUESTION.add(q13);
//		DUMMY_QUESTION.add(q14);
//		DUMMY_QUESTION.add(q15);
//		DUMMY_QUESTION.add(q16);
//		DUMMY_QUESTION.add(q17);
//		DUMMY_QUESTION.add(q18);
//		DUMMY_QUESTION.add(q19);
//		DUMMY_QUESTION.add(q20);
//		DUMMY_QUESTION.add(q21);
//		DUMMY_QUESTION.add(q22);
//		DUMMY_QUESTION.add(q23);
//		DUMMY_QUESTION.add(q24);
//		DUMMY_QUESTION.add(q25);
//		DUMMY_QUESTION.add(q26);
	}
}
