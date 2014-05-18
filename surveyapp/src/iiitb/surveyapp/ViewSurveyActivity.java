package iiitb.surveyapp;
 
import iiitb.surveyapp.adaptor.SurveyListAdapter;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ViewSurveyActivity extends ListActivity {

     
	private SurveyApplication app;
	private SurveyListAdapter adapter;
	private Button removeButton;
	int participantId;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_survey);
        Bundle bundle = getIntent().getExtras();
        participantId = bundle.getInt("paricipantID");
        setUpViews();
        
        app = (SurveyApplication)getApplication();
        app.setParticipantid(participantId);
        app.loadTasks(participantId);
        adapter = new SurveyListAdapter(app.getCurrentTasks(),this);
        setListAdapter(adapter);
	
	}
	
    @Override
	protected void onResume() {
		super.onResume();
		adapter.forceReload();
	
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Intent i=new Intent( getApplicationContext(),DisplayQuestions.class); //display questions
		i.putExtra("paricipantID",participantId );
		i.putExtra("surveyId", position+1);
		startActivity(i);
	}

	private void removeCompletedTasks() {
		Long[] ids = adapter.removeCompletedTasks();
	//	app.deleteTasks(ids);
	}
	
	
	private void setUpViews() {
		 
		removeButton = (Button)findViewById(R.id.remove_button);
		 
		removeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				removeCompletedTasks();
			}

		});
	}

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_surveys, menu);
        return true;
    }
    
}
