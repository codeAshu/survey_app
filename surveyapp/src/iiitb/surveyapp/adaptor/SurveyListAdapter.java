package iiitb.surveyapp.adaptor;

import iiitb.surveyapp.model.Survey;
import iiitb.surveyapp.view.SurveyListItem;

import java.util.ArrayList;

import iiitb.surveyapp.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SurveyListAdapter extends BaseAdapter {

	private ArrayList<Survey> tasks;
	private Context context;
	
	public SurveyListAdapter(ArrayList<Survey> tasks, Context context) {
		super();
		this.tasks = tasks;
		this.context = context;
	}

	@Override
	public int getCount() {
		return tasks.size();
	}

	@Override
	public Survey getItem(int position) {
		return (null == tasks)? null : tasks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SurveyListItem tli;
		if(null == convertView){
			tli = (SurveyListItem)View.inflate(context, R.layout.task_list_item, null);
		}
		else{
			tli = (SurveyListItem)convertView;
		}
		tli.setTask(tasks.get(position));
		return tli;
	}

	public void forceReload() {
		notifyDataSetChanged();
	}

	public void toggleTaskCompleteAtPosition(int position) {
		Survey t = tasks.get(position);
		t.toggleComplete();
		notifyDataSetChanged();
		
	}

	public Long[] removeCompletedTasks() {
		ArrayList<Survey> completedTasks = new ArrayList<Survey>();
		ArrayList<Long> ids = new ArrayList<Long>();
		for(Survey task : tasks){
			if(task.isComplete()){
				completedTasks.add(task);
				ids.add(task.getId());
			}
		}
		tasks.removeAll(completedTasks);
		notifyDataSetChanged();
		
		return ids.toArray(new Long[]{});
		
	}


}
