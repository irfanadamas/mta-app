package adamas.traccs.mta_20_06;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;


import android.os.ResultReceiver;
import android.util.Xml;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class Load_Data_Service2 extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "adamas.traccs.mta.action.FOO";
    private static final String ACTION_BAZ = "adamas.traccs.mta.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "adamas.traccs.mta.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "adamas.traccs.mta.extra.PARAM2";

    public String root="https://58.162.142.150/timesheet" ; //https://10.0.2.2:49884
    private final String NAMESPACE = "https://tempuri.org/";
    String StaffCode="";
    String OperatorId="";
    String Security_Token="";
    boolean server_available=false;
    File froot = null;
    int progress=0;
    Context context;

    public Load_Data_Service2() {
        super("load_data_service");
    }
    @Override
    public void onStart(Intent intent, int startId) {

        onHandleIntent(intent);
        context=this.getApplicationContext();
    }
    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {

        Intent intent = new Intent(context, Load_Data_Service2.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);

    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, Load_Data_Service2.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }

            try{
                Bundle b =intent.getExtras();
                root=b.getString("root");
                OperatorId=b.getString("OperatorId");
                Security_Token=b.getString("Security_Token");
                StaffCode=b.getString("StaffCode");
                server_available=  b.getBoolean("Server_Available");


                load_data();


           /* ResultReceiver receiver = intent.getParcelableExtra("receiver");
            root=intent.getStringExtra("root");
            OperatorId=intent.getStringExtra("OperatorId");
            Security_Token=intent.getStringExtra("Security_Token");
            StaffCode=intent.getStringExtra("StaffCode");
            server_available=  intent.getBooleanExtra("Server_Available",false);



                // data that will be send into ResultReceiver
                Bundle data = new Bundle();
                data.putInt("progress", progress);

                // here you are sending progress into ResultReceiver located in your Activity
                receiver.send(MainActivity.NEW_PROGRESS, data);
                load_Receipient_Detail();*/
              /*  try{

                    Handler handler3 = new Handler(getMainLooper());
                    handler3.post(new Runnable() {
                        @Override

                        public void run() {

                             load_Receipient_Detail();

                        }
                    });
                } catch (Exception e) {  }

                progress=progress+20;
                //  load_paid_hours();
                //  getRoster_Recipient();
                data.putInt("progress",progress);
                receiver.send(MainActivity.NEW_PROGRESS, data);

                try{

                    Handler handler3 = new Handler(getMainLooper());
                    handler3.post(new Runnable() {
                        @Override

                        public void run() {

                            load_Task_Detail();

                        }
                    });
                } catch (Exception e) {  }


                progress=progress+20;
                data.putInt("progress",progress);
                receiver.send(MainActivity.NEW_PROGRESS, data);

                try{

                    Handler handler3 = new Handler(getMainLooper());
                    handler3.post(new Runnable() {
                        @Override

                        public void run() {

                            load_Group_Alerts_Detail();

                        }
                    });
                } catch (Exception e) {  }



                progress=progress+10;
                data.putInt("progress",progress);
                receiver.send(MainActivity.NEW_PROGRESS, data);

                try{

                    Handler handler3 = new Handler(getMainLooper());
                    handler3.post(new Runnable() {
                        @Override

                        public void run() {

                            load_incident_Locations();

                        }
                    });
                } catch (Exception e) {  }

                progress=progress+10;
                data.putInt("progress",progress);
                receiver.send(MainActivity.NEW_PROGRESS, data);

                try{

                    Handler handler3 = new Handler(getMainLooper());
                    handler3.post(new Runnable() {
                        @Override

                        public void run() {

                            load_LeaveTypes();

                        }
                    });
                } catch (Exception e) {  }


                progress=progress+10;
                data.putInt("progress",progress);
                receiver.send(MainActivity.NEW_PROGRESS, data);

                try{

                    Handler handler3 = new Handler(getMainLooper());
                    handler3.post(new Runnable() {
                        @Override

                        public void run() {

                            get_OP_Case_Notes();

                        }
                    });
                } catch (Exception e) {  }

                progress=progress+20;
                data.putInt("progress",progress);
                receiver.send(MainActivity.NEW_PROGRESS, data);

                try{

                    Handler handler3 = new Handler(getMainLooper());
                    handler3.post(new Runnable() {
                        @Override

                        public void run() {

                            load_Transport_Detail();

                        }
                    });
                } catch (Exception e) {  }


                progress=progress+10;
                data.putInt("progress",progress);
                receiver.send(MainActivity.NEW_PROGRESS, data);
*/


            } catch (Exception e) {  }



        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void load_data(){


        try{


            load_Task_Detail();

            load_Group_Alerts_Detail();

            load_incident_Locations();


          /*  load_LeaveTypes();

            get_OP_Case_Notes();


            load_Transport_Detail();*/




        } catch (Exception e) {  }
    }
    String getSecurityToken(){

        String Val= OperatorId  + "$" + Security_Token + "$";
        return Val;
    }

    public void load_Transport_Detail(){

        String URL6 = root  + "/TimeSheet.asmx?op=getTransport_Detail";
        String SOAP_ACTION6 =  "https://tempuri.org/getTransport_Detail";
        String METHOD_NAME6 = "getTransport_Detail";


        try{
            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME6);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug =true;

            PropertyInfo pi1=new PropertyInfo();
            pi1.setName("Carer_Code");
            pi1.setValue( getSecurityToken() + StaffCode);
            request.addProperty(pi1);



            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION6, envelope);

            String xml= androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir=null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath()+"/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir,"Transport.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try{
                    newxmlfile.createNewFile();
                }catch(IOException e){

                }
                FileOutputStream fileos = null;
                try{
                    fileos = new FileOutputStream(newxmlfile );
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                }catch (Exception ex){ }
                finally{   fileos.close();}
            }

        }catch (Exception e){
            //Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
        }
    }
    public void load_incident_Locations(){

        String URL6 = root  + "/TimeSheet.asmx?op=GetDomain_with_Criteria";
        String SOAP_ACTION6 =  "https://tempuri.org/GetDomain_with_Criteria";
        String METHOD_NAME6 = "GetDomain_with_Criteria";


        String Criteria="domain='IMLocation'";
        boolean b_NoBlank=false;
        String s_Default="";

        try{
            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME6);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug =true;

            PropertyInfo pi1=new PropertyInfo();
            pi1.setName("Criteria");
            pi1.setValue( getSecurityToken() + Criteria);
            request.addProperty(pi1);

            PropertyInfo pi2=new PropertyInfo();
            pi2.setName("b_NoBlank");
            pi2.setValue(b_NoBlank);
            request.addProperty(pi2);

            PropertyInfo pi3=new PropertyInfo();
            pi3.setName("s_Default");
            pi3.setValue(s_Default);
            request.addProperty(pi3);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION6, envelope);

            String xml= androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir=null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath()+"/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir,"Incident_Locations.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try{
                    newxmlfile.createNewFile();
                }catch(IOException e){

                }
                FileOutputStream fileos = null;
                try{
                    fileos = new FileOutputStream(newxmlfile );
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                }catch (Exception ex){ }
                finally{   fileos.close();}
            }

        }catch (Exception e){
            //Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
        }
    }
    public void getRoster_Recipient()
    {

        try
        {
            String URL5 = root + "/TimeSheet.asmx?op=getRoster_RecipientAll";
            String SOAP_ACTION5 =  "https://tempuri.org/getRoster_RecipientAll";
            String METHOD_NAME5 = "getRoster_RecipientAll";

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME5);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
            androidHttpTransport.debug =true;

            PropertyInfo pi=new PropertyInfo();
            pi.setName("StaffCode");

            pi.setValue( getSecurityToken() + StaffCode);
            request.addProperty(pi);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION5, envelope);

            String xml= androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir=null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath()+"/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir,"Roster_Recipient.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try{
                    newxmlfile.createNewFile();
                }catch(IOException e){

                }
                FileOutputStream fileos = null;
                try{
                    fileos = new FileOutputStream(newxmlfile );
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                }catch (Exception ex){ }
                finally{   fileos.close();}
            }

        }catch(Exception ex){}


    }
    public void load_Receipient_Detail(){
        String URL2 = root  + "/TimeSheet.asmx?op=getStaff_Recipient_Detail";
        String SOAP_ACTION2 =  "https://tempuri.org/getStaff_Recipient_Detail";
        String METHOD_NAME2 = "getStaff_Recipient_Detail";

        String buff="";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME2);
        try{
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL2);
            androidHttpTransport.debug =true;

            PropertyInfo pi3=new PropertyInfo();
            pi3.setName("AccountNo");
            pi3.setValue(getSecurityToken() +StaffCode);
            request.addProperty(pi3);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;

            envelope.setOutputSoapObject(request);
            androidHttpTransport.call(SOAP_ACTION2, envelope);
            // SoapObject obj=(SoapObject) envelope.getResponse();
            String xml= androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir=null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            fileDir = new File(froot.getAbsolutePath()+"/.server/");
            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir,"Recipient.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try{
                    newxmlfile.createNewFile();

                }catch(IOException e){
                    // textMsg.setText("bb "+ e.toString());
                }
                FileOutputStream fileos = null;




                try{
                    fileos = new FileOutputStream(newxmlfile );
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                }catch (Exception ex){}
                finally{   fileos.close();}
            }
        }catch (Exception ex){ }

        //    textMsg.setText("Done successfully");

    }

    public void load_Task_Detail(){
        String URL3 = root  + "/TimeSheet.asmx?op=getAllTaskList";
        String SOAP_ACTION3 =  "https://tempuri.org/getAllTaskList";
        String METHOD_NAME3 = "getAllTaskList";

        String buff="";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME3);
        try{
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL3);
            androidHttpTransport.debug =true;
            PropertyInfo pi=new PropertyInfo();
            pi.setName("carer_code");
            pi.setValue(getSecurityToken() +StaffCode);
            request.addProperty(pi);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;

            envelope.setOutputSoapObject(request);
            androidHttpTransport.call(SOAP_ACTION3, envelope);
            // SoapObject obj=(SoapObject) envelope.getResponse();
            String xml= androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir=null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            fileDir = new File(froot.getAbsolutePath()+"/.server/");
            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir,"task.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try{
                    newxmlfile.createNewFile();

                }catch(IOException e){

                }
                FileOutputStream fileos = null;

                try{
                    fileos = new FileOutputStream(newxmlfile );
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                }catch (Exception ex){}
                finally{   fileos.close();}
            }
        }catch (Exception ex){ }
        //    textMsg.setText("Done successfully");

    }
    public void load_Group_Alerts_Detail(){
        String URL6 = root  + "/TimeSheet.asmx?op=getAllAlertGroups";
        String SOAP_ACTION6 =  "https://tempuri.org/getAllAlertGroups";
        String METHOD_NAME6 = "getAllAlertGroups";

        String buff="";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME6);
        try{
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug =true;
            PropertyInfo pi2=new PropertyInfo();
            pi2.setName("Fontra");
            pi2.setValue(getSecurityToken() + "99");
            request.addProperty(pi2);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;

            envelope.setOutputSoapObject(request);
            androidHttpTransport.call(SOAP_ACTION6, envelope);
            // SoapObject obj=(SoapObject) envelope.getResponse();
            String xml= androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir=null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            fileDir = new File(froot.getAbsolutePath()+"/.server/");
            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir,"group_alerts.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try{
                    newxmlfile.createNewFile();

                }catch(IOException e){
                    //textMsg.setText("bb "+ e.toString());
                }
                FileOutputStream fileos = null;

                try{
                    fileos = new FileOutputStream(newxmlfile );
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                }catch (Exception ex){ }
                finally{   fileos.close();}
            }
        }catch (Exception ex){}
        //    textMsg.setText("Done successfully");

    }

    public void load_LeaveTypes(){

        String URL6 = root  + "/TimeSheet.asmx?op=GetDomain_with_Criteria";
        String SOAP_ACTION6 =  "https://tempuri.org/GetDomain_with_Criteria";
        String METHOD_NAME6 = "GetDomain_with_Criteria";

        String Criteria=" DOMAIN = 'LEAVEAPP'";



        try{
            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME6);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug =true;

            PropertyInfo pi1=new PropertyInfo();
            pi1.setName("Criteria");
            pi1.setValue( getSecurityToken() + Criteria);
            request.addProperty(pi1);

            PropertyInfo pi2=new PropertyInfo();
            pi2.setName("b_NoBlank");
            pi2.setValue("true");
            request.addProperty(pi2);

            PropertyInfo pi3=new PropertyInfo();
            pi3.setName("s_Default");
            pi3.setValue("-");
            request.addProperty(pi3);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION6, envelope);

            String xml= androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir=null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath()+"/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir,"LeaveTypes.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try{
                    newxmlfile.createNewFile();
                }catch(IOException e){

                }
                FileOutputStream fileos = null;
                try{
                    fileos = new FileOutputStream(newxmlfile );
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                }catch (Exception ex){ }
                finally{   fileos.close();}
            }

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
        }
    }
    public void load_paid_hours(){

        String URL6 = root  + "/TimeSheet.asmx?op=get_Paid_Hours";
        String SOAP_ACTION6 =  "https://tempuri.org/get_Paid_Hours";
        String METHOD_NAME6 = "get_Paid_Hours";


        String format="yyyy/MM/dd";

        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        Calendar c = Calendar.getInstance();
        Date dt=c.getTime();

        String curr_Date =sdf.format(dt);

        // Toast.makeText(getApplicationContext(),  "StaffCode=" + StaffCode, Toast.LENGTH_LONG).show();
        try{
            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME6);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug =true;

            PropertyInfo pi1=new PropertyInfo();
            pi1.setName("AccountNo");
            pi1.setValue( getSecurityToken() + StaffCode);
            request.addProperty(pi1);

            PropertyInfo pi2=new PropertyInfo();
            pi2.setName("Roster_Date");
            pi2.setValue(curr_Date);
            request.addProperty(pi2);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION6, envelope);

            String xml= androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir=null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath()+"/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir,"Paid_Hours.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try{
                    newxmlfile.createNewFile();
                }catch(IOException e){

                }
                FileOutputStream fileos = null;
                try{
                    fileos = new FileOutputStream(newxmlfile );
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                }catch (Exception ex){ }
                finally{   fileos.close();}
            }

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
        }
    }

    public void get_OP_Case_Notes(){


        try
        {
            String URL5 = root + "/TimeSheet.asmx?op=getOP_Case_NoteAll";
            String SOAP_ACTION5 =  "https://tempuri.org/getOP_Case_NoteAll";
            String METHOD_NAME5 = "getOP_Case_NoteAll";

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME5);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
            androidHttpTransport.debug =true;

            PropertyInfo pi=new PropertyInfo();
            pi.setName("StaffCode");

            pi.setValue( getSecurityToken() + StaffCode);
            request.addProperty(pi);

            PropertyInfo pi2=new PropertyInfo();
            pi2.setName("RecipientsCodes");

            pi2.setValue( getRecipientCodes());
            request.addProperty(pi2);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION5, envelope);

            String xml= androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir=null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath()+"/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir,"op_case_note.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try{
                    newxmlfile.createNewFile();
                }catch(IOException e){

                }
                FileOutputStream fileos = null;
                try{
                    fileos = new FileOutputStream(newxmlfile );
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                }catch (Exception ex){ }
                finally{   fileos.close();}
            }

        }catch(Exception ex){}


    }


    public String  getRecipientCodes() {

        String client_codes = "";

        try {
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();
            File fileDir = null;
            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = new File(fileDir, "Recipient.xml");
            //  rosters=new ArrayList<Roster_Info>();
            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Recipient");
                if (nList == null) return "";

                String client_code = "";


                for (int temp = 0; temp < nList.getLength(); temp++) {

                    try {
                        Node nNode = nList.item(temp);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            client_code= eElement.getElementsByTagName("AccountNo").item(0).getTextContent();

                            if ((client_codes.indexOf(client_code))>-1) continue;

                            if (client_codes.equalsIgnoreCase(""))
                                client_codes =  client_code;
                            else
                                client_codes = client_codes + "," + client_code;

                        }

                    } catch (Exception ex) {
                    }

                }
            }
        } catch (Exception ex) {
        }

        return client_codes;
    }

}
