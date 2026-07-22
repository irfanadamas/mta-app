package adamas.traccs.mta_20_06;

import android.os.Bundle;

import android.view.View;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MyAlert extends AppCompatActivity {


    public String root="https://58.162.142.150/" ; //https://10.0.2.2:49884
    private final String NAMESPACE = "https://tempuri.org/";

    Alert_Message msg= null;
    boolean Server_Available=false;
    String OperatorId ="";
    String Security_Token="";
    String Personid="";
    String recordNo="";
    String AccountNo="";
    String Message_Content="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_alert);
        set_server_Ip();

        Button btn_exit=(Button)findViewById(R.id.btnExit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int mNotificationId = 001;
// Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
                mNotifyMgr.cancel (mNotificationId);
                finish();
            }
        });

        Button btnOK=(Button)findViewById(R.id.btnAckn);
        btnOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Update_Status();

                int mNotificationId = 001;
// Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
                mNotifyMgr.cancel (mNotificationId);
                finish();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        //  show_Dialog();
    }
    void Update_Status(){
        String Acknowledge_Query="Update messages set WorkerAcknowledged=1,WorkerAcknowledgedDate=getDate() where Recordno=" + recordNo;
        if (isNetworkAvailable(getApplicationContext()))
            Server_Available=true;

        Make_update(Acknowledge_Query);
    }


    void set_server_Ip()
    {
        try {
            Bundle bundle = getIntent().getExtras();
            root = bundle.get("root").toString();
            recordNo = bundle.get("recordNo").toString();


            //  URL = root  + "/TimeSheet.asmx?op=Add_Travel_Roster";

            Server_Available = bundle.get("Server").toString().equalsIgnoreCase("true");


            if (bundle.get("Security_Token") == null)
                Security_Token = "";
            else
                Security_Token = bundle.get("Security_Token").toString();


            if (bundle.get("OperatorId") == null)
                OperatorId = "";
            else
                OperatorId = bundle.get("OperatorId").toString();

            if (bundle.get("Message_Content") == null)
                Message_Content = "";
            else
                Message_Content = bundle.get("Message_Content").toString();

            msg = new Alert_Message();
            msg.setRecordNo(recordNo);
            msg.setMessage_Content(Message_Content);

            TextView txtMsg = (TextView) findViewById(R.id.txtMsg);
            txtMsg.setText(Message_Content);

        }catch(Exception ex){
            Toast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_LONG).show();}
    }

    String getSecurityToken(){

        String Val= this.OperatorId  + "$" + this.Security_Token + "$";
        return Val;
    }

    public void Make_update(String command)
    {
        TextView txtMsg = (TextView) findViewById(R.id.txtMsg);

        //  txtMsg.setText("Server_Available" + Server_Available);

        try
        {
            if (Server_Available==false){
                return;
            }


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


                //  txtMsg.setText(URL5 + "\n" +result.toString());

            }
        }catch(Exception ex){

            txtMsg.setText(ex.toString());

        }

    }
    public static boolean isNetworkAvailable(Context context)
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