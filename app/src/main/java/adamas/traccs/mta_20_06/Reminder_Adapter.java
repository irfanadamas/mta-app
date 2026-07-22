
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adamas.traccs.mta_20_06;

/**** @author arshad*/
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;

import  adamas.traccs.mta_20_06.R;
public class Reminder_Adapter extends BaseAdapter 
{
	 private String root="https://58.162.142.150/timesheet" ; //https://10.0.2.2:49884
	 private final String NAMESPACE = "https://tempuri.org/";
	    
	 private final String URL4 = root  + "/TimeSheet.asmx?op=getDevice_Active_Reminders";
	 private final String SOAP_ACTION4 =  "https://tempuri.org/getDevice_Active_Reminders";
	 private final String METHOD_NAME4 = "getDevice_Active_Reminders"; 
	    
	private final Context context;
	List<DeviceReminders> Reminders=null;
	private DeviceReminders remind =null;
	LinearLayout listView=null;
	LinearLayout ll=null;
	  String OperatorId;
      String Security_Token;
      
	private int width;
	public Reminder_Adapter(Context context, List<DeviceReminders>   reminders,String OperatorId, String Security_Token, String roott)  {
	        this.context = context;               
	        this.Reminders = reminders;
	        this.OperatorId=OperatorId;
            this.Security_Token=Security_Token;
          	this.root=roott;
	}
 
	public View getView(int position, View convertView, ViewGroup parent){
		final TextView tv1;
//		final   Button btn1;
	//	final   Button btn2;


        View gridView=null;


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            gridView = inflater.inflate(R.layout.reminder_row, null);
        }else{
            gridView=convertView;
        }
        // set image based on selected text
       /* mView=(View) gridView.findViewById(R.id.rl_main);
        LView=(View) mView.findViewById(R.id.rl_body1);
        RView=(View) mView.findViewById(R.id.rl_body2);*/

        //btn1 =  (Button) gridView.findViewById(R.id.btnAck);
      //  btn2 =  (Button) gridView.findViewById(R.id.btnRemind);
        tv1  =  (TextView) gridView.findViewById(R.id.txtDetail);



				remind=Reminders.get(position);
              //  tv1.setText(remind.getActivationDateTime() + "\n" + remind.getDetail());
                tv1.setText( remind.getDetail() + "\n" + remind.getMessageGroup() );
                tv1.setTag(remind.getRecordnumber());

             /*   btn1.setTag(remind);
		if (remind.getMessageGroup().toString().equalsIgnoreCase("BOOKING")){
			btn1.setText("ACT");
			btn2.setText("REJ");
		}else{
			btn1.setText("ACK");
			btn2.setText("REM");
		}



			btn1.setOnClickListener(new OnClickListener()
   		     {
   		      @Override
   		       public void onClick(View v)
   		       {
   		    	   String command="Update DeviceReminders set status=0 where Recordnumber=" + tv1.getTag().toString() ;
   		    	  // Make_update(command);
                   try{
                       new MyAsyncClass().execute(command,"","");
                   }catch(Exception ex){}
   		    	 tv1.setBackgroundColor(Color.parseColor("#e5e996"));


                   try {

                       remind=(DeviceReminders) btn1.getTag();
                       //Toast.makeText(v.getContext(),remind.getDetail(),Toast.LENGTH_LONG).show();
                       if (remind.getMessageGroup().toString().equalsIgnoreCase("BOOKING"))
                           Accept_Roster_Booking(btn1, remind);
                   }catch(Exception ex){}

   		    	//Toast.makeText(v.getContext(), "Will be Remind later" , Toast.LENGTH_LONG).show();
   		       }
   		     });   
   		             		        
                btn2.setOnClickListener(new OnClickListener()
      		     {
   		      @Override
      		       public void onClick(View v)
      		       {
      		    	 String command="Update DeviceReminders set status=1 where Recordnumber=" + tv1.getTag().toString() ;
		    	  // Make_update(command);
                       try{
                           new MyAsyncClass().execute(command,"","");
                       }catch(Exception ex){}
		    	 //  Toast.makeText(v.getContext(), "Reminder Dismissed\n"+command , Toast.LENGTH_LONG).show();
		    	   tv1.setBackgroundColor(Color.parseColor("#996677"));


      		       }
      		     });   
*/

           
         return gridView;
	}
    
      
     
	@Override
	public int getCount() {
		return Reminders.size();
	}
 
	@Override
	public Object getItem(int position) {
		return Reminders.get(position);
	}
 
	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	 String getSecurityToken(){
		   
		   	String Val= this.OperatorId  + "$" + this.Security_Token + "$";
		   	return Val;
		   }
	 public void Make_update(String command)
	    {
	        try{
	        	
	        	String URL5 = this.root + "/TimeSheet.asmx?op=Make_update";
	             String SOAP_ACTION5 =  "https://tempuri.org/Make_update";
	             String METHOD_NAME5 = "Make_update";
	      if ((!command.equals("")) && command!=null ){
	            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME5);
	            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
	            androidHttpTransport.debug =true;
	            
	            PropertyInfo pi=new PropertyInfo(); 
	            pi.setName("command"); 
	          
	            pi.setValue(command); 
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
	        
	        }
	        
	    }

	public void Accept_Roster_Booking(View v,DeviceReminders r  ) {
		try{

			final String SqlStmt="Update Roster set [Carer Code]='" + r.getStaff() + "'" + " where RecordNo=" + r.getExternalID() +
                    ";Update Roster set [Carer Code]='" + r.getStaff() + "'" +
					" where [Batch#] is not null and [Batch#] in ( select isnull([Batch#],-1) from roster where RecordNo=" + r.getExternalID() + ")";
          //  Toast.makeText(v.getContext(),SqlStmt,Toast.LENGTH_LONG).show();

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

			alertDialogBuilder.setTitle("Take/Accept Booking");
			alertDialogBuilder
					.setMessage("Are you sure, you want to Accept/Take  " + r.getDetail())
					.setCancelable(false)
					.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
                            try{
                                new MyAsyncClass().execute(SqlStmt,"","");
                            }catch(Exception ex){}
							//Make_update(SqlStmt);

							dialog.cancel();

						}
					})
					.setNegativeButton("No",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
							dialog.cancel();
						}
					});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
			alertDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

			// show it
			alertDialog.show();

		}catch(Exception ex) {}
	}

    class MyAsyncClass extends AsyncTask<String, String, String> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... mApi) {
            try {

               Make_update(mApi[0]);
            }
            catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
            return "True";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
           // pDialog.cancel();
            //  Toast.makeText(getApplicationContext(), "Email send", 100).show();
        }
    }
}