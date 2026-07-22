package adamas.traccs.mta_20_06;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import static android.os.Looper.getMainLooper;

public class Upload_Updates {
    final String PREFS_NAME = "MTAPrefs";
    SharedPreferences settings = null;
   // Context contxt=null;

    File froot = null;
    Context context;
	    private String root="https://58.162.142.150/timesheet" ; //https://10.0.2.2:49884
	    private final String NAMESPACE = "https://tempuri.org/";
	    String line;
	    String OperatorId;
	      String Security_Token;
	      
	    public  Upload_Updates(String root, String OperatorId, String Security_Token, Context contxt){
	    	this.root=root;
	    	this.OperatorId=OperatorId;
	    	this.Security_Token=Security_Token;
            this.context=contxt;
	    	
	    }
	    
	    String getSecurityToken(){
			   
		   	String Val= this.OperatorId  + "$" + this.Security_Token + "$";
		   	return Val;
		   }
	    
	 public void  Upload_Updates_on_server() throws IOException{
	       // btnOKK = (Button) findViewById(R.id.btnOk);
	       
	      try{
	            // check for SDcard   
	          
	      
	       froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);  
	       File filein=null;
	       File fileDir=null;
	       String state = Environment.getExternalStorageState();
	      
	       // txtServer.setText(state);
	       if (Environment.MEDIA_MOUNTED.equals(state)) {
	          //check sdcard permission 
	            
	    	    fileDir = new File(froot.getAbsolutePath()+"/.server/"); 
	            filein= new File(fileDir, "updates.txt");
	            
	                    if (filein.exists())
	                        {
	                           try{
	                            FileReader fileReader = new FileReader(filein);
	                            BufferedReader  buf= new BufferedReader(fileReader);
	                          
	                             
	                         while ((line = buf.readLine()) != null ){
                                 if (!line.equalsIgnoreCase(""))
                                    // post_command(line);

                                     prepare_update(line);
                                final Thread tUpdate = new Thread() {
                                public void run() {

                                        try{

                                            Handler handler3 = new Handler(getMainLooper());
                                            handler3.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try{

                                                        sleep(100);
                                                    }catch(Exception ex){}
                                                }
                                            });
                                        } catch (Exception e) {  }
                                    } };
                                tUpdate.start();
	             }
	                               
	                         fileReader.close();
	                       
	                     // fileDir = new File(froot.getAbsolutePath()); 
	                         froot.setWritable(true);
	                          File file = new File(fileDir, "updates.txt");  
	                            FileWriter filewriter = new FileWriter(file);  
	                            BufferedWriter out = new BufferedWriter(filewriter);  
	                            out.write("");  
	                            out.close();
	                            
	                             File fileJ = new File(fileDir, "jobs.txt");  
	                            FileWriter filewriterJ = new FileWriter(fileJ);  
	                            BufferedWriter outJ = new BufferedWriter(filewriterJ);  
	                            outJ.write("");  
	                            outJ.close();                         
	                           } catch (Exception e) {
	                         //  textMsg.setText( "Upload_Updates : " + e.toString());
	                             //Log.e("Exception","error occurred while creating xml file");
	                            }
	                         
	                                                      
	                         
	                        } 

	                 }   
	         clear_Jobs_status();
	     } catch (Exception e) {
	     
	     //              Log.e("Exception","error occurred while creating xml file");
	      }
	    
	}
	 public void  clear_Jobs_status() throws IOException{
	       
         String[] values;
        // textMsg=  ((TextView) findViewById(R.id.textMsg));
      try{
            // check for SDcard   
          
      // textMsg.setText("Clearing Job status");
       froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);  
       File filein=null;
       File fileDir=null;
       String state = Environment.getExternalStorageState();
      // txtServer.setText(state);
       if (Environment.MEDIA_MOUNTED.equals(state)) {
          //check sdcard permission 
            fileDir = new File(froot.getAbsolutePath()+"/.server/"); 
            filein= new File(fileDir, "jobs.txt");  
                    if (filein.exists())
                        {
                           try{
                            FileReader fileReader = new FileReader(filein);
                            BufferedReader  buf= new BufferedReader(fileReader);
                          if (buf==null){
                               fileReader.close();
                              return;
                          }
                             
                         line = buf.readLine();

                         if (line.equals(""))
                         line = buf.readLine();
                         values= line.split(",");
                               
                         fileReader.close();
                         
                        DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
                        Date date = new Date();
                        String currentDateString = dateFormat2.format(date);
               
          
                       if (!values[2].equals(currentDateString)){
                     // fileDir = new File(froot.getAbsolutePath()); 
                         froot.setWritable(true);
                          File file = new File(fileDir, "jobs.txt");  
                            FileWriter filewriter = new FileWriter(file);  
                            BufferedWriter out = new BufferedWriter(filewriter);  
                            out.write("");  
                            out.close();
                       }                            
                           } catch (Exception e) {
                          // textMsg.setText( "clearing Job status : " + e.toString());
                             //Log.e("Exception","error occurred while creating xml file");
                            }
                           
                        } 

                 }                  
     } catch (Exception e) {
     // textMsg.setText(e.toString());
     //              Log.e("Exception","error occurred while creating xml file");
      }
}

    private void prepare_update(String command_i)
    {
        final String command=command_i;
        final String[] cmd;
       //  textMsg=  ((TextView) findViewById(R.id.textMsg));
       try{
            cmd=command.split("`");

        String RecordNo;
        String timestamp;
        char op_case=cmd[0].charAt(0);
        switch(op_case)
        {
            case 'S' :
            {
             // textMsg.setText("Make Update : Start Job");
              RecordNo=cmd[1];
              timestamp=cmd[2];
              Set_Job (op_case,RecordNo,timestamp);
             break;           
            }
           case 'C' :
           {
             //  textMsg.setText("Make Update : Cancel Job");
               RecordNo=cmd[1];
              timestamp=cmd[2];
             Set_Job (op_case,RecordNo,timestamp);
             break;           
           }
            case 'E' :
            {
                //  textMsg.setText("Make Update : Cancel Job");
                RecordNo=cmd[1];
                timestamp=cmd[2];
                End_Job (op_case,RecordNo,timestamp);
                break;
            }
            case 'F' :
            {
                //  textMsg.setText("Make Update : Cancel Job");

                RecordNo=cmd[1];
                timestamp=cmd[2];
                End_Job (op_case,RecordNo,timestamp);
                break;
            }

            case 'T' :
           {   
              // textMsg.setText("Make Update : Set Time");
               RecordNo=cmd[1];
              timestamp=cmd[2];
               Set_Time( RecordNo, timestamp);
             break;           
           }
           case 'V' :
           {   
            
              try{
            	  Add_Travel_Roster( cmd[1], cmd[2],cmd[3],cmd[4],cmd[5],cmd[6],cmd[7],cmd[8],cmd[9]);
              }catch(Exception ex){}
             break;           
           }
           case 'N' :
           { 
              try{
              Add_Incident( cmd[1], cmd[2],cmd[3],cmd[4],cmd[5],cmd[6],Boolean.parseBoolean(cmd[7]),cmd[8],cmd[9],cmd[10],cmd[11],cmd[12]);
              }catch(Exception ex){}
             break;           
           }
           case 'L' :
           { 
              try{
              Add_Leave(cmd[1], cmd[2],cmd[3],cmd[4],cmd[5],cmd[6]);
              }catch(Exception ex){}
             break;           
           }
           case 'P' :
           { 
              try{
                  Process_SleepOver(cmd[1], cmd[2]);
              /*   Handler handler = new Handler();
                  Runnable r = new Runnable() {
                      public void run() {

                      }
                  };
                  handler.postDelayed(r, 1000);*/


              }catch(Exception ex){}
             break;           
           }
            case 'R' :
            {
                try{


                    add_Roster_Availability(cmd[1], cmd[2],cmd[3],cmd[4],cmd[5],cmd[6],cmd[7]);


                }catch(Exception ex){}
                break;
            }
            case 'G' :
            {
                try{


                    add_New_Attendee(cmd[1], cmd[2]);


                }catch(Exception ex){}
                break;
            }
           default:
           {
               Make_update (command);

            /*   Handler handler = new Handler();
               Runnable r = new Runnable() {
                   public void run() {
                   }
               };
               handler.postDelayed(r, 1000);*/
              //  textMsg.setText("Updating values");

           }    
        }
         } catch (Exception e) {
    //  textMsg.setText("Prepare_update: " + e.toString());
    
      }
    }

    void add_New_Attendee(String rosterRecordNo,String accountNo){
       boolean isResponseSuccess;
        try
        {
            String URL5 = root + "/TimeSheet.asmx?op=Add_Attendee";
            String SOAP_ACTION5 =  "https://tempuri.org/Add_Attendee";
            String METHOD_NAME5 = "Add_Attendee";

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME5);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
            androidHttpTransport.debug =true;

            PropertyInfo pi=new PropertyInfo();
            pi.setName("RecordNo");

            pi.setValue( getSecurityToken() + rosterRecordNo);
            request.addProperty(pi);

            PropertyInfo pi2=new PropertyInfo();
            pi2.setName("AccountNo");

            pi2.setValue(accountNo);
            request.addProperty(pi2);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            SoapPrimitive result = null;
            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION5, envelope);
            result = (SoapPrimitive) envelope.getResponse();

            isResponseSuccess = Integer.parseInt(result.toString()) > 0;
            // txtServer.setText(androidHttpTransport.requestDump);
        }catch(Exception ex){
            isResponseSuccess = false;
        }
    }
    void add_Roster_Availability(String StaffCode, String r_Date, String Start_Time, String End_Time, String Note,String Cycle,String Week_Days){

        String URL6 = root  + "/TimeSheet.asmx?op=Add_Roster_Availability";
        String SOAP_ACTION6 =  "https://tempuri.org/Add_Roster_Availability";
        String METHOD_NAME6 = "Add_Roster_Availability";


        try{



            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME6);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug =true;

            PropertyInfo pi1=new PropertyInfo();
            pi1.setName("Carer_Code");
            pi1.setValue( getSecurityToken() + StaffCode);
            request.addProperty(pi1);

            PropertyInfo pi2=new PropertyInfo();
            pi2.setName("Roster_Date");
            pi2.setValue( r_Date);
            request.addProperty(pi2);

            PropertyInfo pi3=new PropertyInfo();
            pi3.setName("Start_Time");
            pi3.setValue( Start_Time);
            request.addProperty(pi3);

            PropertyInfo pi4=new PropertyInfo();
            pi4.setName("End_Time");
            pi4.setValue( End_Time);
            request.addProperty(pi4);

            PropertyInfo pi5=new PropertyInfo();
            pi5.setName("Note");
            pi5.setValue( Note);
            request.addProperty(pi5);

            PropertyInfo pi6 = new PropertyInfo();
            pi6.setName("Cycle");
            pi6.setValue(Cycle);
            request.addProperty(pi6);

            PropertyInfo pi7 = new PropertyInfo();
            pi7.setName("Week_Days");
            pi7.setValue(Week_Days);
            request.addProperty(pi7);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION6, envelope);

            SoapPrimitive   result =(SoapPrimitive)envelope.getResponse();
           // RecordNo=result.toString();

        }catch (Exception e){
            //Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
        }


    }

    public void Set_Job(char op_case,String RecordNo,String timestamp)
    {
          String URL5 = root + "/TimeSheet.asmx?op=StartJob";
          String SOAP_ACTION5 =  "https://tempuri.org/StartJob";
          String METHOD_NAME5 = "StartJob"; 
           //  textMsg=  ((TextView) findViewById(R.id.textMsg));         
        try
        {


            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME5);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
            androidHttpTransport.debug =true;
            
            PropertyInfo pi=new PropertyInfo(); 
            pi.setName("Recordno"); 
          
            pi.setValue(getSecurityToken () + RecordNo); 
            request.addProperty(pi);
       
             PropertyInfo pi2=new PropertyInfo(); 
              pi2.setName("cancel");

            pi2.setValue(op_case != 'S');
            request.addProperty(pi2);
       
            PropertyInfo pi3=new PropertyInfo(); 
            pi3.setName("timeStamp");           
            pi3.setValue(timestamp); 
            request.addProperty(pi3);
       
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION5, envelope);     

            SoapPrimitive  result =(SoapPrimitive)envelope.getResponse();
                  
        }catch(Exception ex){
         //  textMsg.setText(ex.toString());
        }
           
    }
    public void End_Job(char op_case,String RecordNo,String timestamp)
    {
        String URL5 = root + "/TimeSheet.asmx?op=EndJob";
        String SOAP_ACTION5 =  "https://tempuri.org/EndJob";
        String METHOD_NAME5 = "EndJob";
        //  textMsg=  ((TextView) findViewById(R.id.textMsg));
        try
        {


            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME5);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
            androidHttpTransport.debug =true;

            PropertyInfo pi = new PropertyInfo();
            pi.setName("Recordno");
            pi.setValue(getSecurityToken() + RecordNo);
            request.addProperty(pi);

            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("cancel");

            pi3.setValue(op_case != 'E');
            request.addProperty(pi3);

            PropertyInfo pi4 = new PropertyInfo();
            pi4.setName("Latitude");
            pi4.setValue("0");
            request.addProperty(pi4);

            PropertyInfo pi5 = new PropertyInfo();
            pi5.setName("Longitude");
            pi5.setValue("0");
            request.addProperty(pi5);

            PropertyInfo pi6 = new PropertyInfo();
            pi6.setName("Location");
            pi6.setValue("-");
            request.addProperty(pi6);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            DateFormat tFormat = new SimpleDateFormat("HH:mm");


            Date date = new Date();
            String strDate = dateFormat.format(date);
            String strTime = tFormat.format(date);
            strDate = strDate + " " + strTime;


            PropertyInfo pi7 = new PropertyInfo();
            pi7.setName("timeStamp");
            pi7.setValue(timestamp);
            request.addProperty(pi7);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION5, envelope);

            SoapPrimitive  result =(SoapPrimitive)envelope.getResponse();

        }catch(Exception ex){
            //  textMsg.setText(ex.toString());
        }

    }
       public void Set_Time(String RecordNo, String Time_String)
    {
          //  textMsg=  ((TextView) findViewById(R.id.textMsg));         
        try
        {
             String URL5 = root + "/TimeSheet.asmx?op=AcceptTimes";
            String SOAP_ACTION5 =  "https://tempuri.org/AcceptTimes";
             String METHOD_NAME5 = "AcceptTimes";
    
            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME5);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
            androidHttpTransport.debug =true;
            
            PropertyInfo pi=new PropertyInfo(); 
            pi.setName("Recordno"); 
          
            pi.setValue(getSecurityToken() + RecordNo); 
            request.addProperty(pi);
            
            PropertyInfo pi2=new PropertyInfo(); 
            pi2.setName("Time_String");     
            pi2.setValue( Time_String); 
            request.addProperty(pi2);
             // start_time="";  duration="";
          
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION5, envelope);     

            SoapPrimitive  result =(SoapPrimitive)envelope.getResponse();
             
        }catch(Exception ex){
         //textMsg.setText(ex.toString());
        }
           
    }
         public int Make_update(String command)
    {
        try
        {
             String URL5 = root + "/TimeSheet.asmx?op=Make_update";
             String SOAP_ACTION5 =  "https://tempuri.org/Make_update";
             String METHOD_NAME5 = "Make_update";
      if ((!command.equals("")) && command!=null ){
            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME5);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
            androidHttpTransport.debug =true;
            
            PropertyInfo pi=new PropertyInfo(); 
            pi.setName("command"); 
          
            pi.setValue(command.replace("~","\n"));
            request.addProperty(pi);
            
            PropertyInfo pi2=new PropertyInfo(); 
            pi2.setName("Fontra");           
            pi2.setValue(getSecurityToken() + "99"); 
            request.addProperty(pi2);
            
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION5, envelope);     
            SoapPrimitive  result =(SoapPrimitive)envelope.getResponse();
            
        }
        }catch(Exception ex){
        // textMsg.setText(ex.toString());
         return 0;
        }
        return 5;   
    }
         
         public void Add_Travel_Roster(String RecordNo, String distance,String Travel_Type, String Charge_Type,String startKM,String EndKM,String notes,String MyCar ,String Mobile_Device )
         {
            
        	  String NAMESPACE = "https://tempuri.org/";
        	   String URL = root  + "/TimeSheet.asmx?op=Add_Travel_Roster";
        	  String SOAP_ACTION =  "https://tempuri.org/Add_Travel_Roster";
        	  String METHOD_NAME = "Add_Travel_Roster"; 
               
                 
              
              try{
                 SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
                 HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                 androidHttpTransport.debug =true;
                 
                 PropertyInfo pi=new PropertyInfo(); 
                 pi.setName("RecordNo"); 
               
                 pi.setValue( getSecurityToken() + RecordNo); 
                 request.addProperty(pi);
                
                 PropertyInfo pi2=new PropertyInfo(); 
                 pi2.setName("distance");
                 pi2.setValue(""+distance); 
                 request.addProperty(pi2);
                 
                 PropertyInfo pi3=new PropertyInfo(); 
                 pi3.setName("Travel_Type");
                 pi3.setValue(Travel_Type); 
                 request.addProperty(pi3);
                 
                 PropertyInfo pi4=new PropertyInfo(); 
                 pi4.setName("Charge_Type");
                 pi4.setValue(Charge_Type); 
                 request.addProperty(pi4);
                 
                 PropertyInfo pi5=new PropertyInfo(); 
                 pi5.setName("StartKm");
                 pi5.setValue(""+startKM); 
                 request.addProperty(pi5);
                 
                 PropertyInfo pi6=new PropertyInfo(); 
                 pi6.setName("EndKM");
                 pi6.setValue(""+EndKM); 
                 request.addProperty(pi6);
                 
                 PropertyInfo pi7=new PropertyInfo(); 
                 pi7.setName("Notes");
                 pi7.setValue(notes.replace("~","\n"));
                 request.addProperty(pi7);

                  PropertyInfo pi8=new PropertyInfo();
                  pi8.setName("OwnVehicle");
                  pi8.setValue(MyCar);
                  request.addProperty(pi8);

                  PropertyInfo pi9=new PropertyInfo();
                  pi9.setName("Mobile_Device");
                  pi9.setValue(Mobile_Device);
                  request.addProperty(pi9);

                  SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                 envelope.dotNet=true;
                 envelope.setOutputSoapObject(request);
                 // Make the soap call.
                 androidHttpTransport.call(SOAP_ACTION, envelope);     
                 SoapPrimitive  result =(SoapPrimitive)envelope.getResponse();
                 
                 //Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
                 
                 if (Double.parseDouble(result.toString())>0)
                 {
                     //Toast.makeText(getApplicationContext(), "Travel Added Successfully", Toast.LENGTH_LONG).show();
                	
                 }else
                 {
                     // Toast.makeText(getApplicationContext(), "Roster Record is not addedd properly due to some missing default values", Toast.LENGTH_LONG).show();
                	
                 }
             
             }catch(Exception ex){
             
             }
             
         }
         public void Add_Incident(String Personid,String Incident_Type,String Service,String Incident_Severity,String Location,String Note, boolean No_Recipient,String StaffCode,String OperatorID,String AccountNo,String Program ,String IncidentSummary)
         {
      	   
      	   
      	     String URL41 = root + "/TimeSheet.asmx?op=Add_Incident";
      	     String SOAP_ACTION41 =  "https://tempuri.org/Add_Incident";
      	     String METHOD_NAME41 = "Add_Incident";
      	    
             // TextView txtmsg=  ((TextView) findViewById(R.id.textMsg));
          //   txtmsg.setVisibility(View.VISIBLE);
            // txtmsg.setText("Calling add note  " + URL4);
             
           
           
             try            
             {             
                  SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME41);
                  HttpTransportSE androidHttpTransport2 = new HttpTransportSE(URL41);
                  androidHttpTransport2.debug =true;
                
                  PropertyInfo pi=new PropertyInfo(); 
                  pi.setName("PersonId"); 
                  pi.setValue( getSecurityToken() + Personid); 
                  request.addProperty(pi);
                  
                  PropertyInfo pi2=new PropertyInfo(); 
                  pi2.setName("Incident_Type"); 
                  pi2.setValue(Incident_Type); 
                  request.addProperty(pi2);
                  
                  
                  PropertyInfo pi3=new PropertyInfo(); 
                  pi3.setName("Service"); 
                  pi3.setValue(Service); 
                  request.addProperty(pi3);
                  
                  PropertyInfo pi4=new PropertyInfo(); 
                  pi4.setName("Incident_Severity");     
                  pi4.setValue(Incident_Severity); 
                  request.addProperty(pi4);
                  
                  PropertyInfo pi5=new PropertyInfo(); 
                  pi5.setName("Location");     
                  pi5.setValue(Location); 
                  request.addProperty(pi5);
                             
                  PropertyInfo pi6=new PropertyInfo(); 
                  pi6.setName("Note");   
                //  Note=Note.replace("\'", "\'\'");
                  Note=Note;
                  pi6.setValue(Note.replace("~","\n"));
                  request.addProperty(pi6);
                  
                  PropertyInfo pi7=new PropertyInfo(); 
                  pi7.setName("staff");     
                  pi7.setValue(StaffCode); 
                  request.addProperty(pi7);
                  
                  PropertyInfo pi8=new PropertyInfo(); 
                  pi8.setName("OperatorID");     
                  pi8.setValue(OperatorID); 
                  request.addProperty(pi8);
                  
                  PropertyInfo pi9=new PropertyInfo(); 
                  pi9.setName("RecipientCode");     
                  pi9.setValue(AccountNo); 
                  request.addProperty(pi9);
                  
                  PropertyInfo pi10=new PropertyInfo(); 
                  pi10.setName("Program");     
                  pi10.setValue(Program); 
                  request.addProperty(pi10);
                  
                 
                  PropertyInfo pi11=new PropertyInfo(); 
                  pi11.setName("No_Receipient");     
                  pi11.setValue(No_Recipient); 
                  request.addProperty(pi11);

                 PropertyInfo pi12 = new PropertyInfo();
                 pi12.setName("IncidentSummary");
                 pi12.setValue(IncidentSummary);
                 request.addProperty(pi12);
                  
                  SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                  envelope.dotNet=true;
                  envelope.setOutputSoapObject(request);
                  
                  SoapPrimitive  result=null;
                             
                  // Make the soap call.
                  androidHttpTransport2.call(SOAP_ACTION41, envelope);     
                 result= (SoapPrimitive) envelope.getResponse();
                           
                 if (Integer.parseInt(result.toString())>0)
                     
                 ;//	Toast.makeText(getApplicationContext(), "Cleint Incident added Successfully", Toast.LENGTH_LONG).show();
                       
                 else
                 	;//Toast.makeText(getApplicationContext(), "Client Incident not added due to some problem - " + AccountNo + " Result=" + result.toString(), Toast.LENGTH_LONG).show();
                   
                 try{
                   //    sendEmail("Client Note",Note,"arshadblouch81@yahoo.com");
                 }catch(Exception ex){
                 	
                 	//Toast.makeText(getApplicationContext(), "Operation not done due to some server error\n" + ex.toString(), Toast.LENGTH_LONG).show();
      	              
                 }
                
                  
              }catch(Exception ex){
              //	Toast.makeText(getApplicationContext(), "Operation not done due to some server error\n" + ex.toString(), Toast.LENGTH_LONG).show();
      	           
              }
                 
         
         }
         
         public void Add_Leave(String Personid,String Leave_Type,String Start_Date, String End_Date,String Note, String Address1 )
         {
      	   
      	     String URL41 = root + "/TimeSheet.asmx?op=Add_LeaveEntry";
      	     String SOAP_ACTION41 =  "https://tempuri.org/Add_LeaveEntry";
      	     String METHOD_NAME41 = "Add_LeaveEntry";
      	    
           //   TextView txtmsg=  ((TextView) findViewById(R.id.textMsg));
          //   txtmsg.setVisibility(View.VISIBLE);
            // txtmsg.setText("Calling add note  " + URL4);
             
           
           
             try            
             {             
                  SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME41);
                  HttpTransportSE androidHttpTransport2 = new HttpTransportSE(URL41);
                  androidHttpTransport2.debug =true;
                
                  PropertyInfo pi=new PropertyInfo(); 
                  pi.setName("StaffCode");
                  pi.setValue( getSecurityToken() + Personid); 
                  request.addProperty(pi);
                  
                  PropertyInfo pi2=new PropertyInfo(); 
                  pi2.setName("Leave_Type"); 
                  pi2.setValue(Leave_Type); 
                  request.addProperty(pi2);
                  
                  
                  PropertyInfo pi3=new PropertyInfo(); 
                  pi3.setName("Start_Date"); 
                  pi3.setValue(Start_Date); 
                  request.addProperty(pi3);
                  
                  PropertyInfo pi4=new PropertyInfo(); 
                  pi4.setName("End_Date");     
                  pi4.setValue(End_Date); 
                  request.addProperty(pi4);
                  
                  PropertyInfo pi5=new PropertyInfo(); 
                  pi5.setName("Note");     
                  pi5.setValue(Note.replace("~","\n"));
                  request.addProperty(pi5);
                             
                  PropertyInfo pi6=new PropertyInfo(); 
                  pi6.setName("Address1");   
                  pi6.setValue(Address1); 
                  request.addProperty(pi6);
                  
                 
                  
                  SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                  envelope.dotNet=true;
                  envelope.setOutputSoapObject(request);
                  
                  SoapPrimitive  result=null;
                             
                  // Make the soap call.
                  androidHttpTransport2.call(SOAP_ACTION41, envelope);     
                 result= (SoapPrimitive) envelope.getResponse();
                           
                 if (Integer.parseInt(result.toString())>0)
                     
                 	;//Toast.makeText(getApplicationContext(), "Cleint Incident added Successfully", Toast.LENGTH_LONG).show();
                       
                 else
                 	;//Toast.makeText(getApplicationContext(), "Client Incident not added due to some problem - " + AccountNo + " Result=" + result.toString(), Toast.LENGTH_LONG).show();
                   
                 try{
                   //    sendEmail("Client Note",Note,"arshadblouch81@yahoo.com");
                 }catch(Exception ex){
                 	
                 	//;Toast.makeText(getApplicationContext(), "Operation not done due to some server error\n" + ex.toString(), Toast.LENGTH_LONG).show();
      	              
                 }
                
                  
              }catch(Exception ex){
              	//;Toast.makeText(getApplicationContext(), "Operation not done due to some server error\n" + ex.toString(), Toast.LENGTH_LONG).show();
      	           
              }
                 
         
         }
         
         public   void Process_SleepOver(String RecordNo, String RosterDate) 
         {
         	
         	
         	 // Button  tvaddress2=(Button)findViewById(R.id.txtAddress2); 
         	  
         	  String URL6 = root  + "/Timesheet.asmx?op=Process_SleepOver_Jobs";
         	  String SOAP_ACTION6 =  "https://tempuri.org/Process_SleepOver_Jobs";
         	  String METHOD_NAME6 = "Process_SleepOver_Jobs"; 
         	    
         	 
             try{
             	
                  SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME6);
                  HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
                  androidHttpTransport.debug =true;
                                   
                  PropertyInfo pi=new PropertyInfo(); 
                  pi.setName("Recordno"); 
                  pi.setValue(getSecurityToken () + RecordNo); 
                  request.addProperty(pi);
             
                  PropertyInfo pi2=new PropertyInfo(); 
                  pi2.setName("cancel");     
                  pi2.setValue(false);                 
                  request.addProperty(pi2);

                  PropertyInfo pi3=new PropertyInfo(); 
                  pi3.setName("timeStamp");           
                  pi3.setValue(RosterDate);
                  request.addProperty(pi3);
                  
                  String Latitude="0";
                  String Longitude="0";
                  String Location_Address="Offline Sleep over shift";
                  
                  PropertyInfo pi4=new PropertyInfo(); 
                  pi4.setName("Latitude");           
                  pi4.setValue(Latitude); 
                  request.addProperty(pi4);
                  
                  if(Longitude==null) Longitude="0";
                  
                  PropertyInfo pi5=new PropertyInfo(); 
                  pi5.setName("Longitude");           
                  pi5.setValue(Longitude); 
                  request.addProperty(pi5);
                  if(Location_Address==null) Location_Address="-";
                  PropertyInfo pi6=new PropertyInfo(); 
                  pi6.setName("Location");           
                  pi6.setValue(Location_Address); 
                  request.addProperty(pi6);
                //  Toast.makeText(getApplicationContext(), "Calling sleepover\n" + Location_Address + "\n"  + Latitude + ", " + Longitude, Toast.LENGTH_SHORT).show();
    	           
                 
                 // tvaddress2.setText("RecordNo=" + RecordNo + "\n RosterDate=" + strDate + "\nLatitude=" + Latitude + " Longitude="+ Longitude + "\n" + Location_Address);
                  SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                  envelope.dotNet=true;
                  envelope.setOutputSoapObject(request);

                  androidHttpTransport.call(SOAP_ACTION6, envelope);     

                  SoapPrimitive  result =(SoapPrimitive)envelope.getResponse();
                  
                   
                   //finish();
                 
             }catch (Exception e){
             	//Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
                 //tvaddress2.setText(e.toString());
                 
             }
              

         }

    /*void post_command(String command){
        final Context context=this.contxt;
        final String cmd=command;
        Handler handler =  new Handler(context.getMainLooper());
        handler.post( new Runnable(){
            public void run(){


                prepare_update(cmd);

            }
        });
    }*/
}
