package adamas.traccs.mta_20_06;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;



        import java.util.ArrayList;
        import java.util.Timer;
        import java.util.TimerTask;

        import org.ksoap2.SoapEnvelope;
        import org.ksoap2.serialization.PropertyInfo;
        import org.ksoap2.serialization.SoapObject;
        import org.ksoap2.serialization.SoapPrimitive;
        import org.ksoap2.serialization.SoapSerializationEnvelope;
        import org.ksoap2.transport.HttpTransportSE;

        import android.R;
        import android.app.AlertDialog;
        import android.app.IntentService;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.app.Service;

        import android.content.ComponentName;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.graphics.Color;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.IBinder;

        import android.view.Gravity;
import android.view.Menu;
import android.view.View;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class Alert_Service extends Service{
    public String root="https://58.162.142.150/" ; //https://10.0.2.2:49884
    private final String NAMESPACE = "https://tempuri.org/";

    private  String URL = root  + "/TimeSheet.asmx?op=getStaffAlert_Messages";
    private final String SOAP_ACTION =  "https://tempuri.org/getStaffAlert_Messages";
    private final String METHOD_NAME = "getStaffAlert_Messages";

    String Staff="";
    String Roster_Date="";
    boolean Server_Available=false;
    String OperatorId ="";
    String Security_Token="";

    ArrayList<Alert_Message> lst_task=null;

    private final int UPDATE_INTERVAL = 60 * 2000;
    private final Timer timer = new Timer();
    //  private static final int NOTIFICATION_EX = 1;
    //  private NotificationManager notificationManager;

    public Alert_Service() {
        super();

        // super(Alert_Service.class.getName());
        //   setIntentRedelivery(true);
    }
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(getApplicationContext(),  "Service is running", Toast.LENGTH_LONG).show();


        return null;
    }



    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        // Toast.makeText(getApplicationContext(),  "Service is started", Toast.LENGTH_LONG).show();

        // Toast.makeText(getApplicationContext(), "Service onStart", Toast.LENGTH_LONG).show();


    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // Toast.makeText(getApplicationContext(),  "Service is stopped", Toast.LENGTH_LONG).show();
    }



    @Override
    public void onCreate() {


    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {

        this.root=intent.getStringExtra("root");
        this.Staff=intent.getStringExtra("Staff");
        this.Roster_Date=intent.getStringExtra("Roster_Date");
        this.Server_Available= Boolean.parseBoolean(intent.getStringExtra("Server_Available"));
        this.OperatorId= intent.getStringExtra("OperatorId");
        this.Security_Token= intent.getStringExtra("Security_Token");
        URL = this.root  + "/TimeSheet.asmx?op=getStaffAlert_Messages";

        final Handler handler = new Handler();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //  getMessageList();
                handler.post(new Runnable() {
                    public void run() {
                        // Toast.makeText(getApplicationContext(), "Service isNetworkAvailable = " + isNetworkAvailable(getApplicationContext()) + ", " + getSecurityToken() , Toast.LENGTH_LONG).show();
                        if   (!isNetworkAvailable(getApplicationContext())){
                            return;
                        }
                        lst_task= new ArrayList<Alert_Message>();
                        Alert_Message tsk=null;
                        try {

                            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
                            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                            androidHttpTransport.debug =true;

                            PropertyInfo pi=new PropertyInfo();
                            pi.setName("Staff");

                            String message =  getSecurityToken() + Staff;
                            pi.setValue(message);
                            request.addProperty(pi);

                            PropertyInfo pi2=new PropertyInfo();
                            pi2.setName("roster_date");
                            pi2.setValue(Roster_Date);
                            request.addProperty(pi2);

                            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                            envelope.dotNet=true;
                            envelope.setOutputSoapObject(request);

                            // Make the soap call.
                            androidHttpTransport.call(SOAP_ACTION, envelope);

                            SoapObject  result =(SoapObject)envelope.getResponse();

                            String obj;
                            // for (int j=0; j<result.getPropertyCount(); j++)       {
                            for (int j=0; j<1; j++)       {
                                SoapObject   result2 =(SoapObject)result.getProperty(j);
                                tsk= new Alert_Message();
                                for(int i=0 ; i<result2.getPropertyCount(); i++)
                                {
                                    obj=result2.getProperty("Recordno").toString();
                                    tsk.setRecordNo(obj);

                                    obj=result2.getProperty("Type").toString();
                                    tsk.setType(obj);

                                    obj=result2.getProperty("Recipient").toString();
                                    tsk.setRecipient(obj);

                                    obj=result2.getProperty("Message_Content").toString();
                                    tsk.setMessage_Content(obj);

                                    obj=result2.getProperty("Sent").toString();
                                    tsk.setSent(obj);

                                    obj=result2.getProperty("Acknowledged").toString();
                                    tsk.setAcknowledged(obj);

                                    obj=result2.getProperty("StaffID").toString();
                                    tsk.setStaffId(obj);

                                }

                                lst_task.add(tsk);

                            }

                        } catch (Exception aE) {

                        }
                        show_Messages();

                    }
                });

                // Check if there are updates here and notify if true

            }
        }, 2000, UPDATE_INTERVAL);
        return START_STICKY;
    }

    private void stopService() {
        if (timer != null) timer.cancel();
    }


    String getSecurityToken(){

        String Val= this.OperatorId  + "$" + this.Security_Token + "$";
        return Val;
    }

    public void getMessageList(){

        if (!isNetworkAvailable(getApplicationContext()))
        {

            return;

        }
        lst_task= new ArrayList<Alert_Message>();
        Alert_Message tsk=null;
        try {

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug =true;

            PropertyInfo pi=new PropertyInfo();
            pi.setName("Recipient");

            String message =  getSecurityToken() + Staff;
            pi.setValue(message);
            request.addProperty(pi);

            PropertyInfo pi2=new PropertyInfo();
            pi2.setName("roster_date");
            pi2.setValue(Roster_Date);
            request.addProperty(pi2);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION, envelope);

            SoapObject  result =(SoapObject)envelope.getResponse();

            String obj;
            for (int j=0; j<result.getPropertyCount(); j++)       {
                SoapObject   result2 =(SoapObject)result.getProperty(j);
                tsk= new Alert_Message();
                for(int i=0 ; i<result2.getPropertyCount(); i++)
                {
                    obj=result2.getProperty("Recordno").toString();
                    tsk.setRecordNo(obj);

                    obj=result2.getProperty("Type").toString();
                    tsk.setType(obj);

                    obj=result2.getProperty("Recipient").toString();
                    tsk.setRecipient(obj);

                    obj=result2.getProperty("Message_Content").toString();
                    tsk.setMessage_Content(obj);

                    obj=result2.getProperty("Sent").toString();
                    tsk.setSent(obj);

                    obj=result2.getProperty("Acknowledged").toString();
                    tsk.setAcknowledged(obj);

                    obj=result2.getProperty("StaffID").toString();
                    tsk.setStaffId(obj);
               /*
                obj=result2.getProperty("WorkerAcknowledged").toString();
                tsk.setWorkerAcknowledged(obj.toString());

                obj=result2.getProperty("WorkerAcknowledgedDate").toString();
                tsk.setWorkerAcknowledgedDate(obj.toString());
                */
                }


                lst_task.add(tsk);

            }

        } catch (Exception a) {
            Toast.makeText(getApplicationContext(),a.toString(),Toast.LENGTH_LONG).show();
        }

    }

    void show_Messages(){

        Alert_Message msg=null;
        // Toast.makeText(getApplicationContext(), "Service running msg", Toast.LENGTH_LONG).show();

        if (lst_task==null) return;

        try{
            for(int i=0; i<lst_task.size(); i++){
                msg= lst_task.get(i);

                Toast toast = Toast.makeText(getApplicationContext(),"Alert!\n" + msg.getMessage_Content(), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.getView().setBackgroundColor(Color.GRAY);
                toast.show();

                ShowDialog(getApplication().getApplicationContext(), msg);

                //ShowDialog (App.getContext(),msg);

            }
        } catch (Exception ax) {}

    }

    public void ShowDialog(Context context, Alert_Message msg ) {
        try{

        	/*	 try{
             	Intent shareIntent = new Intent(Intent.ACTION_SEND);
                  shareIntent.setType("text/plain");
                  shareIntent.putExtra(Intent.EXTRA_TEXT, msg.getMessage_Content());
                 Intent new_intent = Intent.createChooser(shareIntent, "Share via");
                 // Intent new_intent = new Intent(context,MyAlert.class);
                  new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  getApplicationContext().startActivity(new_intent);

             	}catch(Exception ex){
                 	Toast.makeText(getApplicationContext(), "Try-I:\n"+ ex.toString(), Toast.LENGTH_LONG).show();
                 	}


        	Intent intent;
            intent = new Intent(this, MyAlert.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


            Intent intent = new Intent(context,MyAlert.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);

            intent.setFlags(Intent.FLAG_FROM_BACKGROUND);
            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

			intent.putExtra("Staff", Staff);
			intent.putExtra("Roster_Date",  Roster_Date);
			intent.putExtra("Server_Available", Server_Available);
			intent.putExtra("OperatorId", OperatorId);
			intent.putExtra("Security_Token", Security_Token);
			intent.putExtra("recordNo", msg.getRecordNo());
			intent.putExtra("root", root);
			intent.putExtra("Message_Content", msg.getMessage_Content());

			getApplicationContext().startActivity(intent);
			*/


            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_popup_reminder)
                            .setContentTitle("MTA Alert")
                            .setContentText(msg.getMessage_Content());

            Intent resultIntent = new Intent(this, MyAlert.class);
            Bundle bundle = new Bundle();

            bundle.putString("root", root);
            bundle.putString("recordNo", msg.getRecordNo());
            bundle.putString("Staff", Staff);
            bundle.putString("Roster_Date", Roster_Date);
            bundle.putString("Server",""+ Server_Available);
            bundle.putString("OperatorId", OperatorId);
            bundle.putString("Security_Token", Security_Token);
            bundle.putString("Message_Content", msg.getMessage_Content());

            resultIntent.putExtras(bundle);


            PendingIntent resultPendingIntent = PendingIntent.getActivity(this,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT	);

            mBuilder.setContentIntent(resultPendingIntent);


// Sets an ID for the notification
            int mNotificationId = 001;
// Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());


        }catch(Exception ex){
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }

    }
    public void Make_update(String command)
    {
        try
        {
            if (Server_Available==false){
                return;
            }

            String URL5 = root + "/TimeSheet.asmx?op=Make_update";
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

    public  boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            if (info != null)
            {
                for (int i = 0; i < info.length; i++)
                {
                    //  Log.i("Class", info[i].getState().toString());
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
