package adamas.traccs.mta_20_06;

public class OP_Case_Note {

	
	 private String Recipient  ; 
	  public String getRecipient(){ return this.Recipient ;}
	  public void setRecipient(String val   ){  this.Recipient=val ;}
	  
	  private String Note_Date  ; 
	  public String getNote_Date(){ return this.Note_Date ;}
	  public void setNote_Date(String val   ){  this.Note_Date=val ;}
	  
	  private String Detail  ; 
	  public String getDetail(){ return this.Detail ;}
	  public void setDetail(String val   ){  this.Detail=val ;}
	  
	  private String Creator  ; 
	  public String getCreator(){ return this.Creator ;}
	  public void setCreator(String val   ){  this.Creator=val ;}

	  private String NoteType;


	public String getNoteType() {
		return NoteType;
	}

	public void setNoteType(String noteType) {
		NoteType = noteType;
	}

	private String RecordNo ;

	public String getRecordNo() {
		return RecordNo;
	}

	public void setRecordNo(String recordNo) {
		RecordNo = recordNo;
	}
	private String AccountNo;

	public String getAccountNo() {
		return AccountNo;
	}

	public void setAccountNo(String accountNo) {
		AccountNo = accountNo;
	}

	private String Roster_Date;

	public String getRoster_Date() {
		return Roster_Date;
	}

	public void setRoster_Date(String roster_Date) {
		Roster_Date = roster_Date;
	}

	private String Job_Time;

	public String getJob_Time() {
		return Job_Time;
	}

	public void setJob_Time(String job_Time) {
		Job_Time = job_Time;
	}

	private String PersonId;

	public String getPersonId() {
		return PersonId;
	}

	public void setPersonId(String personId) {
		PersonId = personId;
	}
}
