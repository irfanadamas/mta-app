package adamas.traccs.mta_20_06;

public class GroupAlerts {
	  private String RecordNo  ; 
	  public String getRecordNo(){ return this.RecordNo ;}
	  public void setRecordNo(String val   ){  this.RecordNo=val ;}
	  
	  private String Group  ; 
	  public String getGroup(){ return this.Group ;}
	  public void setGroup(String val   ){  this.Group=val ;}

	  private String Notes  ; 
	  public String getNotes(){ return this.Notes ;}
	  public void setNotes(String val   ){  this.Notes=val ;}

	  private String _PersonId;
	  public String getPersonId(){ return this._PersonId ;}
	  public void setPersonId(String val   ){  this._PersonId=val ;}
	  private String SpecialConsiderations;


	public String getSpecialConsiderations() {
		return SpecialConsiderations;
	}

	public void setSpecialConsiderations(String specialConsiderations) {
		SpecialConsiderations = specialConsiderations;
	}
}
