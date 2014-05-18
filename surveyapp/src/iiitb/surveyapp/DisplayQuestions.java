package iiitb.surveyapp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;






import iiitb.surveyapp.view.FormActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class DisplayQuestions extends FormActivity
{
	public static final int OPTION_SAVE = 1;
	public static final int OPTION_SUBMIT = 0;
	public static final int OPTION_CANCEL = 2;
	private SurveyApplication app;
	private int surveyId;
	private int participantId;
	private String schema;
	private AlertDialog unsavedChangesDialog;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate( savedInstanceState );
		
		Bundle bundle = getIntent().getExtras();
		 participantId = bundle.getInt("paricipantID");
		 surveyId = bundle.getInt("surveyId");
		
		app = (SurveyApplication)getApplication();
		app.setParticipantid(this.participantId);
		
		schema = app.loadSchema(this.surveyId);
		System.out.println(schema);
	    generateForm(schema);

	    Toast.makeText(this, Integer.toString(this.surveyId), Toast.LENGTH_LONG).show(); 
	    
		//check for the file of this form if available then
		Context context = getApplicationContext();
		String fileName = Integer.toString(surveyId)+Integer.toString(participantId)+".json";
		
		File file = context.getFileStreamPath(fileName);
		System.out.println(file.getAbsolutePath());
		
		//if file found then show the contents
		if(file.exists())  
			populate( FormActivity.parseFileToString( this, fileName ) );
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) 
	{
		menu.add( 0, OPTION_SUBMIT, 0, "Submit" );
		menu.add( 0, OPTION_SAVE, 0, "Save" );
		menu.add( 0, OPTION_CANCEL, 0, "Cancel" );
		return true;
	}

	@Override
	public boolean onMenuItemSelected( int id, MenuItem item )
	{

		switch( item.getItemId() )
		{ 
		case OPTION_SAVE:

			//save this into a file.
			JSONObject temp = new JSONObject();
			temp = save();
			System.out.println(temp.toString());
			
			//file is saved as "survey_id.json"
			String fileName = Integer.toString(surveyId)+Integer.toString(participantId)+".json";
			Context context = getApplicationContext();
			File file = context.getFileStreamPath(fileName);
			
			//if already exist then delete the previous one!!
			if(file.exists())
				context.deleteFile(fileName);
			
			try {
				FileWriter out = new FileWriter(new File(context.getFilesDir(), fileName));
				out.write(temp.toString());
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			//show confirmation dialouge
			unsavedChangesDialog = new AlertDialog.Builder(this)
			.setTitle("You Have Saved Your Answers")
			.setMessage("Would you like to continue taking this survey or want to go back?")
			.setPositiveButton("Continue", new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					 dialog.cancel();
				}
				})
			.setNegativeButton("Go back", new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				        finish();
				}
				})
				.create();
			unsavedChangesDialog.show();

			
			break;

		case OPTION_SUBMIT:
			
			//show confirmation dialouge
			unsavedChangesDialog = new AlertDialog.Builder(this)
			.setTitle(R.string.unsaved_changes_title)
			.setMessage(R.string.unsaved_changes_message)
			.setPositiveButton("Submit", new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
					JSONObject data = new JSONObject();					 
					data = submit(surveyId,participantId);
							
					//remove the saved file also
					String delFileName = Integer.toString(surveyId)+Integer.toString(participantId)+".json";
					Context con = getApplicationContext();
					con.deleteFile(delFileName);
				
					//do the connection and put the data and send it to server for update
					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			    	StrictMode.setThreadPolicy(policy); 
			    	
			    	//connection-------
			    	HttpParams httpParams = new BasicHttpParams();
		            HttpConnectionParams.setConnectionTimeout(httpParams,10000);
		            HttpConnectionParams.setSoTimeout(httpParams, 10000);
		            HttpClient client = new DefaultHttpClient(httpParams);
		            String url = "http://192.168.13.30/updates.php";
		            HttpPost request = new HttpPost(url);

		            
		            //send object------
		            try {
						request.setEntity(new ByteArrayEntity(data.toString().getBytes(
						        "UTF8")));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            request.setHeader("json", data.toString());
		            HttpResponse response = null;
					try {
						response = client.execute(request);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            HttpEntity entity = response.getEntity();
		           
		            
		            // If the response does not enclose an entity, there is no need
		            if (entity != null) {
		               
		                //Toast.makeText(this,  "damn",
		                     //   Toast.LENGTH_LONG).show();
		            }
		            else
		            {
		            	//Toast.makeText(this, "no luck", Toast.LENGTH_LONG).show();
		            }
		        
		    
					
					finish();
				}
				})
			
				.setNegativeButton(android.R.string.cancel, new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				        dialog.cancel();
				}
				})
				.create();
			unsavedChangesDialog.show();
			
			break;

		case OPTION_CANCEL:
			//remove the saved file also
			String delFileName = Integer.toString(surveyId)+Integer.toString(participantId)+".json";
			Context con = getApplicationContext();
			con.deleteFile(delFileName);
			finish();
			break;
		}

		return super.onMenuItemSelected( id, item );
	}
}