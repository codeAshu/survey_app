package iiitb.surveyapp;

import iiitb.surveyapp.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private int participantID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    public boolean checkLogin(String login,String password)
    {
    	//I need to get the references for the login and password first
    	
    	EditText log=(EditText) findViewById(R.id.editText1);
    	EditText pass=(EditText) findViewById(R.id.editText2);
    	String l=log.getText().toString();
    	String p=pass.getText().toString();
    	
    	System.out.println("now we are matching with"+l+" "+p);
    	if ( (l.equals(login)) && (p.equals(password)) )
    		return true;
    	else
    		return false;
    	 }

    
    public void onclick(View view)
    {
    	if (view.getId()==R.id.button1)
    	{
    		if (checkLogin("s","1"))  
    			//login from server  
    			//save this id as participatnt ID
    		{
    			 //set the participant id
    		    			 
    			Intent i=new Intent(getApplicationContext(),ViewSurveyActivity.class); //display questions
    			i.putExtra("paricipantID", 1);
    			startActivity(i);
    			 
    			
    		}
    		else
    			Toast.makeText(this, "wrong ID or Password!!", Toast.LENGTH_LONG).show();
   		
    	}
    	
    	
    }
    
    
}
