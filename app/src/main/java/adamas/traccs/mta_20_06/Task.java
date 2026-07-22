/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adamas.traccs.mta_20_06;

/**
 *
 * @author arshad
 */
public class Task {
        private String Recordno;
	
	public String getRecordNo() { return this.Recordno; }
	public void setRecordNo(String value) { this.Recordno = value; }
      

	private String TaskCOmplete;
	public String getTaskCOmplete() { return this.TaskCOmplete; }
	public void setTaskCOmplete(String value) { this.TaskCOmplete = value; }
	

	private String TaskDetail;
	public String getTaskDetail() {return this.TaskDetail; }
	public void setTaskDetail(String value) { this.TaskDetail = value; }
        
        private String client_code;
	public String getclient_code() {return this.client_code; }
	public void setclient_code(String value) { this.client_code = value; }
                  
         private String rosterRecordno;
	public String getRosterRecordNo() { return this.rosterRecordno; }
	public void setRosterRecordNo(String value) { this.rosterRecordno = value; }
	
}
