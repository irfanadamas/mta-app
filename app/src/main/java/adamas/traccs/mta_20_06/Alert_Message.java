package adamas.traccs.mta_20_06;

public class Alert_Message {

	   private String Recordno;
		
		public String getRecordNo() { return this.Recordno; }
		public void setRecordNo(String value) { this.Recordno = value; }

		private String Type;
		public String getType() { return this.Type; }
		public void setType(String value) { this.Type = value; }

		private String Recipient;
		public String getRecipient() {return this.Recipient; }
		public void setRecipient(String value) { this.Recipient = value; }
	        
	    private String Message_Content;
		public String getMessage_Content() {return this.Message_Content; }
		public void setMessage_Content(String value) { this.Message_Content = value; }
	                  
	    private String Sent;
		public String getSent() { return this.Sent; }
		public void setSent(String value) { this.Sent = value; }
		
		private String Acknowledged;
		public String getAcknowledged() { return this.Acknowledged; }
		public void setAcknowledged(String value) { this.Acknowledged = value; }
		
		private String StaffId;
		public String getStaffId() { return this.StaffId; }
		public void setStaffId(String value) { this.StaffId = value; }
		
		private String WorkerAcknowledged;
		public String getWorkerAcknowledged() { return this.WorkerAcknowledged; }
		public void setWorkerAcknowledged(String value) { this.WorkerAcknowledged = value; }
		
		private String WorkerAcknowledgedDate;
		public String getWorkerAcknowledgedDate() { return this.WorkerAcknowledgedDate; }
		public void setWorkerAcknowledgedDate(String value) { this.WorkerAcknowledgedDate = value; }
		
			
}
