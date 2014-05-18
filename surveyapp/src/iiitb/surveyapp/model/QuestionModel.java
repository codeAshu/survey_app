package iiitb.surveyapp.model;
/***************************************************************
 * This is the model by which question will be extracted!!
 * 
 ***************************************************************/
public class QuestionModel  {
	
	 
	private long quesID;
	private String quesText;
	private String type; 				// <"open","binary","ls5","ls7">
	@SuppressWarnings("unused")
	private String answer; 				
	
	/* Question type 
	 * open   :	 "Text" of the answer as a string
	 * binary :	  'Y/N'
	 * ls5    :   '1-5'
	 * ls5    :   '1-7'
	*/
	
	public QuestionModel(  long quesID,  String type,String quesText,String answer) {
		super();
		 
		this.quesID = quesID;
		this.quesText = quesText;
		this.type = type;
		this.answer =answer;
	}
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	 
	 
	public long getQuesID() {
		return quesID;
	}
	public String getQuesText() {
		return quesText;
	}
	
	
	

}
