package iiitb.surveyapp.view;
import iiitb.surveyapp.model.Survey;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

public class SurveyListItem extends LinearLayout {
	
	private Survey survey;
	private CheckedTextView checkbox;
	
	public SurveyListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		checkbox = (CheckedTextView)findViewById(android.R.id.text1);
	}



	public Survey getTask() {
		return survey;
	}

	public void setTask(Survey survey) {
		this.survey = survey;
		checkbox.setText(survey.getName());
		checkbox.setChecked(survey.isComplete());
	}

}
