package iiitb.surveyapp;

import iiitb.surveyapp.model.QuestionModel;
import iiitb.surveyapp.model.Survey;
 

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.os.StrictMode;

public class SurveyApplication extends Application {


	ArrayList<Survey> currentTasks ;
	String parsedString = "";
	private int participantid;
	public static ArrayList<QuestionModel>  qsList = new ArrayList<QuestionModel>(); 

	@Override
	public void onCreate() {  
		super.onCreate();

		}

	public int getParticipantid() {
		return participantid;
	}

	public void setParticipantid(int participantid) {
		this.participantid = participantid;
	}

	public static String convertinputStreamToString(InputStream ists)
			throws IOException {
		if (ists != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader r1 = new BufferedReader(new InputStreamReader(
						ists, "UTF-8"));
				while ((line = r1.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} finally {
				ists.close();
			}
			return sb.toString();
		} else {
			return "";
			
		}
	}

	public void loadTasks(int participantId) {

		currentTasks = new ArrayList<Survey>();
		System.out.println(" load task!!");

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 


		try {
			System.out.println(participantId);
			String path = "http://192.16.13.30/get_surveys.php?p_id="+Integer.toString(participantId);

			URL url = new URL(path);
			URLConnection conn = url.openConnection();

			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			InputStream is = httpConn.getInputStream();
			parsedString = convertinputStreamToString(is);

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(parsedString);

		try {


			JSONObject jsonObj = new JSONObject(parsedString);

			// Getting JSON Array node
			JSONArray surveys = jsonObj.getJSONArray("survey");

			// looping through All surveys for this participant id
			for (int i = 0; i < surveys.length(); i++) {
				JSONObject object = surveys.getJSONObject(i);


				//create a survey object and assign the values id,name,complete
				Survey s1 = new Survey(object.getString("survey_name"));
				s1.setId(Integer.parseInt(object.getString("survey_id")));
				currentTasks.add(s1);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

	public ArrayList<Survey> getCurrentTasks() {
		return currentTasks;
	}

	public void setCurrentTask(ArrayList<Survey> currentTask) {
		this.currentTasks = currentTask;
	}


	public String loadSchema(int surveyid) {

		System.out.println(" getting schema!!");
		//give permission
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 

		try {
			System.out.println(surveyid);
			String path = "http://192.168.13.30/get_questions.php?s_id="+Integer.toString(surveyid);

			URL url = new URL(path);
			URLConnection conn = url.openConnection();

			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			InputStream is = httpConn.getInputStream();
			parsedString = convertinputStreamToString(is);
		//	is.close();

		}catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(parsedString);

		try {

			JSONObject jsonObj = new JSONObject(parsedString);

			// Getting JSON Array node
			JSONArray questions = jsonObj.getJSONArray("question");

			// looping through All surveys for this participant id
			for (int i = 0; i < questions.length(); i++) {
				JSONObject object = questions.getJSONObject(i);

				//create a Question object and assign the values id,type and text
				QuestionModel ques = new QuestionModel(Integer.parseInt(object.getString("id")),object.getString("type"),object.getString("text"),null);
				qsList.add(ques);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 


		StringBuilder sb = new StringBuilder();
		String content = null;

		content = "{\"meta\": {\"type\": \"meta\",\"name\": \"Survey App\"}";
		sb.append(content);
		for (QuestionModel qm : qsList) {

			switch (qm.getType()) {

			case "binary":
				sb.append(",\n");
				content = "\""+qm.getQuesText()+"\": { \"type\" : \"integer\", \"id\" :\""+
						qm.getQuesID()+"\", \"default\" : \"0\", \"priority\" : \"" +
						qm.getQuesID() +"\", \"options\": {\"0\" : \"Yes\",\"1\" : \"No\" } }";
				sb.append(content);
				break;

			case "open":
				sb.append(",\n");
				content = "\""+qm.getQuesText()+"\": { \"type\" : \"string\", \"id\" :\""+
						qm.getQuesID()+"\", \"default\" : \"\", \"priority\" : \"" +
						qm.getQuesID()+"\", \"hint\":\"write here..\"}";
				sb.append(content);
				break;

			case "ls5":
				sb.append(",\n");
				content = "\""+qm.getQuesText()+"\": { \"type\" : \"integer\", \"id\" :\""+
						qm.getQuesID()+"\", \"default\" : \"0\", \"priority\" : \"" +
						qm.getQuesID()+"\", \"options\": " +
						"{ \"0\" : \"Strongly Disagree\"," +
						" \"1\" : \"Disagree\"," +
						" \"2\" : \"Neutral\"," +
						" \"3\" : \"Agree\"," +
						" \"4\" : \"Strongly Agree\" } }";
				sb.append(content);
				break;

			case "ls7":
				sb.append(",\n");
				content = "\""+qm.getQuesText()+"\": { \"type\" : \"integer\", \"id\" :\""+
						qm.getQuesID()+"\", \"default\" : \"0\", \"priority\" : \"" +
						qm.getQuesID()+"\", \"options\": " +
						"{ \"0\" : \"Strongly Disagree\"," +
						" \"1\" : \"Disagree\"," +
						" \"2\" : \"Somewhat Disagree\"," +
						" \"3\" : \"Netural\"," +
						" \"4\" : \"Somewhat Agree\"," +
						" \"5\" : \"Agree\"," +
						" \"6\" : \"Strongly Agree\" } }";
				sb.append(content);
				break;

			default:
				break;
			} 
		}
		content = "\n}";
		sb.append(content);
		qsList.clear();
		return sb.toString();
	}

}
