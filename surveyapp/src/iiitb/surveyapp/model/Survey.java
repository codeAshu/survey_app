package iiitb.surveyapp.model;

public class Survey {
	
	
	private long id;
	private String name;
	private boolean complete;
	


	public Survey(String surveyName){
		name = surveyName;
	}
	
	public void setId(long id) {
		this.id = id;
		
	}

	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString(){
		return name;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public void toggleComplete() {
		complete = !complete;
		
	}
	
}
