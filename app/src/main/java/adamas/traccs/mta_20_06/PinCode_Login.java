package adamas.traccs.mta_20_06;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;

import android.os.Bundle;
import android.text.format.Time;
import android.util.Xml;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.material.navigation.NavigationView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import timesheet.NetworkStateReceiver;

public class PinCode_Login extends AppCompatActivity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        NavigationView.OnNavigationItemSelectedListener
        , NetworkStateReceiver.NetworkStateReceiverListener
{


      String root = "https://58.162.142.150/timesheet"; //https://10.0.2.2:49884
      String NAMESPACE = "https://tempuri.org/";
      String URL7 = root + "/TimeSheet.asmx?op=getRoster_Datewise";
      String SOAP_ACTION7 = "https://tempuri.org/getRoster_Datewise";
      String METHOD_NAME7 = "getRoster_Datewise";


    Roster_Info Current_roster;
    private ArrayList<Roster_Info> rosters = null;
    boolean start_Job=true;
    String StaffCode,Security_Token,OperatorId;
    boolean server_available=false;
    XmlData xml ;
    String Latitude="0",Longitude="0",Location_Address="-";
    Location mCurrentLocation;
    File froot;
    Context context;
    EditText txtPinCode;
    final String PREFS_NAME = "MTAPrefs";
    SharedPreferences settings = null;
    boolean PinCode_Mode;
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(false);
            try {
                String Title = actionBar.getTitle().toString();
                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                actionBar.setCustomView(getLayoutInflater().inflate(R.layout.action_bar_home, null),
                        new ActionBar.LayoutParams(
                                ActionBar.LayoutParams.MATCH_PARENT,
                                ActionBar.LayoutParams.MATCH_PARENT,
                                Gravity.CENTER
                        )
                );
                TextView textviewTitle = (TextView) actionBar.getCustomView().findViewById(R.id.actionbar_textview);
                textviewTitle.setGravity(Gravity.CENTER);
                textviewTitle.setText(Title);
                ImageView imageMenu=(ImageView) actionBar.getCustomView().findViewById(R.id.imageMenu);
                imageMenu.setVisibility(View.INVISIBLE);

                ImageView imageBack=(ImageView) actionBar.getCustomView().findViewById(R.id.imageBack);
                // imageMenu.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageBack.setVisibility(View.GONE);
                actionBar.setDisplayHomeAsUpEnabled(true);

                imageBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

            } catch (Exception ex) {
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //if(1==1) return false;

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        super.onOptionsItemSelected(item);

        switch(item.getItemId())
        {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file, menu);
        //if(1==1) return false;
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code__login);
        setupActionBar();
        context= this.getApplicationContext();
        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        root= settings.getString("root","");
        xml=new XmlData(this.getApplicationContext());
        server_available=isOnline(getApplicationContext());

        Button btnClear = findViewById(R.id.btnClear);
         txtPinCode = findViewById(R.id.txtPinCode);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtPinCode.setText("");
            }
        });

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                   // Tost_Message(txtPinCode.getText().toString());
                    if(txtPinCode.getText().toString().equals("")){
                        Tost_Message("Please Enter Valid Pin Code");
                        return;
                    }
                    start_Job=true;
                    new MyAsyncClass_PinCodeLogin().execute();

                }catch(Exception ex){
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();}

            }
        });

        Button btnLogOut = findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(txtPinCode.getText().toString().equals("")){
                        Tost_Message("Please Enter Valid Pin Code");
                        return;
                    }
                    start_Job=false;
                    new MyAsyncClass_PinCodeLogin().execute();

                }catch(Exception ex){
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();}

            }
        });
    }


    @Override
    public void networkAvailable() {
        server_available = true;
    }

    @Override
    public void networkUnavailable() {
        server_available = false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        //  Log.d(TAG, "Firing onLocationChanged..............................................");
        if(!server_available) return;
        // Toast.makeText(this, "Getting Location.........", Toast.LENGTH_SHORT).show();
         mCurrentLocation= location;
        //mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }
    private void updateUI() {
        if(!server_available) return;
        // Log.d(TAG, "UI update initiated .............");
        Geocoder coder=null;
        List<Address> add=null;
        List<Address> add2=null;

        try {
            coder = new Geocoder(this, Locale.getDefault());
            add = coder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 5);
        }catch(Exception ex){}

        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());
            try {
                //Current_Address = add.get(0).getAddressLine(0) + ", " + add.get(0).getAddressLine(1) + ", " + add.get(0).getAddressLine(2);

                String Line1=add.get(0).getAddressLine(0);
                String Line2=add.get(0).getAddressLine(1);
                String Line3=add.get(0).getAddressLine(2);

                // Current_Address = add.get(0).getAddressLine(0) + ", " + add.get(0).getAddressLine(1) + ", " + add.get(0).getAddressLine(2);

                if (Line1!=null && !Line1.equalsIgnoreCase("NULL"))
                    Location_Address=Line1;

                if (Line2!=null && !Line2.equalsIgnoreCase("NULL"))
                    Location_Address=Location_Address + ", " +Line2;

                if (Line3!=null && !Line3.equalsIgnoreCase("NULL"))
                    Location_Address=Location_Address + ", " +Line3;


            }catch(Exception ex){}


        } else {
            // Log.d(TAG, "location is null ...............");
            // Toast.makeText(getApplicationContext(), "Location is null .....................",Toast.LENGTH_LONG).show();
        }
    }

    class MyAsyncClass_PinCodeLogin extends AsyncTask<Void, Void, String> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(PinCode_Login.this);
            pDialog.setMessage("Logging User ....");

            pDialog.show();

        }

        @Override
        protected String doInBackground(Void... mApi) {


            String res="0";
            try {

                res= Login_User_With_PinCode();

            } catch (Exception ex) {//Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }

            return res;

        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            int obj=Integer.parseInt(result);
            if (obj==0 ){

                Tost_Message("No User found with this Pin Code, Invalid User");
                //  Toast.makeText(getApplicationContext(), "No User found with this Pin Code, Invalid User", Toast.LENGTH_LONG).show();
                // textMsg.setText("No User found with this Pin Code, Invalid User");
                return;
            }else if (obj>0){

                settings.edit().putBoolean("pin_code_mode", true).commit();
                set_Login_Mode_status_In_DB("Pincode");
                Login.PinCode_Mode=true;
                new MyAsyncClass_Load_Roster().execute();


            }else{
                //  Toast.makeText(getApplicationContext(), "No User found with this Pin Code, Invalid User", Toast.LENGTH_LONG).show();
                //textMsg.setText("No User found with this Pin Code, Invalid User");
                Tost_Message("No User found with this Pin Code, Invalid User");
                return;
            }
        }
    }
    class MyAsyncClass_Load_Roster extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(PinCode_Login.this);
            pDialog.setMessage("Loading data ....");

            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {



            try {

             // doService();
                load_Roster_data();
            } catch (Exception ex) {//Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }

            return null;

        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (start_Job)
                Get_Job_Processing();
            else
                Process_End_Job();
        }
    }
    String Login_User_With_PinCode() {

        String URL5 = root + "/TimeSheet.asmx?op=Login_PinCode_With_Processing_Job";
        String SOAP_ACTION5 = "https://tempuri.org/Login_PinCode_With_Processing_Job";
        String METHOD_NAME5 = "Login_PinCode_With_Processing_Job";



        SoapPrimitive result = null;
        SoapObject obj = null;
        TextView lblMsg = (TextView) findViewById(R.id.textMsg);


        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME5);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
        androidHttpTransport.debug = true;

        try {

            PropertyInfo pi = new PropertyInfo();
            pi.setName("PinCode");
            String PinCode = txtPinCode.getText().toString();
            pi.setValue(PinCode);
            request.addProperty(pi);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION5, envelope);
            obj = (SoapObject) envelope.getResponse();

            if (obj == null) return "0";

            else {

                StaffCode = obj.getProperty("Staffcode").toString();
                Security_Token = obj.getProperty("Security_Token").toString().substring(3);
                OperatorId = obj.getProperty("UserName").toString();

                return "1";
            }


        } catch (Exception e) {
            //  Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
        }
        return "0";
    }
    private  class MyAsyncClass_start extends AsyncTask<Void , Void, Void> {

        String msg="";
        ProgressDialog pDialog;
        SoapObject  obj=null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            String msg="";
            pDialog = new ProgressDialog(PinCode_Login.this);
            pDialog.setMessage("Please wait while processing ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                if (start_Job){
                    Set_Job(Current_roster.getRecordNo());

                    //   msg= "Your Job has been started successfull\n"
                    //          + " From " + Current_roster.getStart_Time() + " to " + Current_roster.get_End_Time();
                }else{
                    End_Job(Current_roster.getRecordNo());

                    //   msg= "Your Job has been stopped successfully \n"
                    //        + " From " + Current_roster.getStart_Time() + " to " + Current_roster.get_End_Time();

                }
            }catch(Exception ex){}

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();


        }
    }

    public void Get_Job_Processing() {
    String msg="";

        Current_roster=get_current_Item();
        if (Current_roster == null) {
            Tost_Message("No current Job found to process");
            // Toast.makeText(getApplicationContext(),"No current Job found to process", Toast.LENGTH_LONG).show();
            // textMsg.setText("No current Job found to process");
            return;
        }
        Button btnok = (Button) findViewById(R.id.btnLogin);

            try {
                if (start_Job) {

                    msg = "Start Job\n\nDo you want to start Job : " + Current_roster.getServiceType()
                            + "\n\n Job Time :  " + Current_roster.getStart_Time() + " to " + Current_roster.get_End_Time();
                    msg = msg + "\n\nRecord No. " + Current_roster.getRecordNo() + "\nRoster Date: " + Current_roster.getRoster_Date() +
                            "\nClient Code : " + Current_roster.getClient_code() + "\nStart Time : " + Current_roster.getStart_Time() + ", Duration :" + Current_roster.get_Calculated_Duration() + "\nNotes : " + Current_roster.getNotes();

                    ShowDialog(btnok, msg);
                } else {
                    msg = "Job Stared\n\nDo you want to End Job : " + Current_roster.getServiceType()
                            + "\n\n Job Timing : " + Current_roster.getStart_Time() + " to " + Current_roster.get_End_Time();

                    msg = msg + "\n\nRecord No. " + Current_roster.getRecordNo() + "\nRoster Date: " + Current_roster.getRoster_Date() +
                            "\nClient Code : " + Current_roster.getClient_code() + "\nStart Time : " + Current_roster.getStart_Time() + ", Duration :" + Current_roster.get_Calculated_Duration() + "\nNotes : " + Current_roster.getNotes();

                    ShowDialog(btnok, msg);
                }



            } catch (Exception e) {  //Toast.makeText(getApplicationContext(),e.toString()  , Toast.LENGTH_LONG).show();
            }
    }
    public void Process_End_Job() {
        String msg="";

        Current_roster=get_started_job();

        if (Current_roster == null) {
            Tost_Message("No current Job found to process");
            // Toast.makeText(getApplicationContext(),"No current Job found to process", Toast.LENGTH_LONG).show();
            // textMsg.setText("No current Job found to process");
            return;
        }
        Button btnok = (Button) findViewById(R.id.btnLogin);

        try {
                msg = "End Job\n\nDo you want to End Job : " + Current_roster.getServiceType()
                        + "\n\n Job Timing :  " + Current_roster.getStart_Time() + " to " + Current_roster.get_End_Time();

                msg = msg + "\n\nRecord No. " + Current_roster.getRecordNo() + "\nRoster Date: " + Current_roster.getRoster_Date() +
                        "\nClient Code : " + Current_roster.getClient_code() + "\nStart Time : " + Current_roster.getStart_Time() + ", Duration :" + Current_roster.get_Calculated_Duration() + "\nNotes : " + Current_roster.getNotes();

                ShowDialog(btnok, msg);



        } catch (Exception e) {  //Toast.makeText(getApplicationContext(),e.toString()  , Toast.LENGTH_LONG).show();
        }
    }
    public void ShowDialog(View v, String commandText) {
        try {


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

            alertDialogBuilder.setIcon(R.drawable.ic_set_time2);
            //alertDialogBuilder.setCustomTitle(customTitleView)

            alertDialogBuilder.setTitle("Currently Selected Roster");
            alertDialogBuilder
                    .setMessage(commandText)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            try{
                                new MyAsyncClass_start().execute();
                            }catch(Exception ex){}

                            dialog.cancel();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
            alertDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            // show it
            alertDialog.show();

        } catch (Exception ex) {
        }
    }
    public void load_Roster_data(){

        String URL = root  + "/TimeSheet.asmx?op=getRoster_Datewise";
        String SOAP_ACTION =  "https://tempuri.org/getRoster_Datewise";
        String METHOD_NAME = "getRoster_Datewise";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        try{
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug =true;

            PropertyInfo pi=new PropertyInfo();

            pi.setName("Client_code");
            pi.setValue( getSecurityToken() +  StaffCode);
            request.addProperty(pi);
            // TrustManagerManipulator.allowAllSSL();
            // Toast.makeText(getApplicationContext(),"getSecurityToken=" + getSecurityToken() + " "+  client_code , Toast.LENGTH_LONG).show();

            String format="yyyy/MM/dd";

            SimpleDateFormat    sdf = new SimpleDateFormat(format,Locale.getDefault());
            Calendar c = Calendar.getInstance();
            Date dt=c.getTime();

            String rosterDate = sdf.format(dt);



            PropertyInfo pi2=new PropertyInfo();
            pi2.setName("rosterDate");
            pi2.setValue(rosterDate);
            request.addProperty(pi2);

            // Toast.makeText(getApplicationContext(),"rosterDate=" +   rosterDate , Toast.LENGTH_LONG).show();

            PropertyInfo pi3=new PropertyInfo();
            pi3.setName("client");
            pi3.setValue(false);
            request.addProperty(pi3);


            // if (MobileFutureLimit.equals("0") || MobileFutureLimit.equals("") || MobileFutureLimit==null) MobileFutureLimit="10";
            PropertyInfo pi4=new PropertyInfo();
            pi4.setName("MobileFutureLimit");
            pi4.setValue("10");
            request.addProperty(pi4);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;

            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION, envelope);

            SoapObject   result =(SoapObject)envelope.getResponse();

            rosters=new ArrayList<Roster_Info>();


            Roster_Info rst;

            if (result==null){
                Tost_Message(" No Job Exists for this Date");
                return;
            }

            if (result.getPropertyCount()<=0){
                Tost_Message(" No Job exists in this Date");
                return;
            }

            SoapObject   result2=null;
            int prop=0;
            for (int j=0; j<result.getPropertyCount(); j++)
            {
                result2 =(SoapObject)result.getProperty(j);
                prop=result2.getPropertyCount();
                // Toast.makeText(getApplicationContext(), "properties count : " +  prop, Toast.LENGTH_LONG).show();

                rst= new Roster_Info();
                try{

                    rst.setRecordNo(result2.getProperty("RecordNo").toString()); // roster No
                    rst.setRoster_Date(result2.getProperty("Roster_Date").toString()); // roster date
                    rst.setServiceType(result2.getProperty("ServiceType").toString()); // ServiceType
                    rst.setService_Detail(result2.getProperty("Service_Detail").toString()); // Service_Detail
                    rst.setStart_Time(result2.getProperty("Start_Time").toString()); // Start_Time
                    rst.setDuration(result2.getProperty("Duration").toString()); // Duration
                    rst.setCarer_code(result2.getProperty("Carer_code").toString()); // Carer_code
                    rst.setClient_code(result2.getProperty("Client_code").toString()); // Client_code
                    rst.setProgram(result2.getProperty("Program").toString()); // Client_code
                    rst.setDayNo(Integer.parseInt(result2.getProperty("DayNo").toString())); // DayNo
                    rst.setMonthNo(Integer.parseInt(result2.getProperty("Monthno").toString())); // Monthno
                    rst.setYearNo(Integer.parseInt(result2.getProperty("YearNo").toString())); // YearNo
                    rst.setBlockNo(Integer.parseInt(result2.getProperty("blockNo").toString())); // blockNo
                    rst.setNotes(result2.getProperty("Notes").toString()); // Notes
                    rst.setRoster_type(result2.getProperty("Roster_Type").toString()); // Roster_Type
                    rst.setStarted(result2.getProperty("Started").toString()); // Started
                    rst.setCompleted(result2.getProperty("Completed").toString()); // Completed
                    rst.setActual_Client_Code(result2.getProperty("Actual_Client_Code").toString()); // Actual_Client_Code
                    rst.setServiceSetting(result2.getProperty("servicesetting").toString());

                } catch(Exception ex){Toast.makeText(getApplicationContext(), "Some data not fetched properly", Toast.LENGTH_LONG).show();}

                rosters.add(rst);


            }

            if (rosters==null) return;
/*

            String xml= androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir=null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            fileDir = new File(froot.getAbsolutePath()+"/.server/");
            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir,"traccs.xml");
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
                }catch (Exception ex){}// textMsg.setText("cc: " + ex.toString());}

            }*/
        }catch (Exception ex){ }//textMsg.setText("cc: " + ex.toString());}
        //    textMsg.setText("Done successfully");



    }
    Roster_Info get_current_Item()
    {
        int indx=0;
        if (rosters==null){
       //     TextView txtPinMsg= (TextView)findViewById(R.id.txtPassword);
            Tost_Message("No Job exists to start ");
           // txtPinMsg.setText("No Job exists to start ");
        }
        int total_item= rosters.size();
        Roster_Info rst=null;
        Current_roster=null;
        while(indx<total_item)
        {
            rst=rosters.get(indx);
            //Integer.parseInt(rst.getStarted())<=0


            if (Integer.parseInt(rst.getCompleted())<=0  && Integer.parseInt(rst.getStarted())<=0 && Check_valid_Duration(rst.getStart_Time(), rst.get_End_Time())==true   )
            {

                Current_roster=rst;

                break;
            }
            indx=indx+1;

        }
        return Current_roster;
    }
    Roster_Info get_started_job()
    {
        int indx=0;
        if (rosters==null){
            Tost_Message("No Job exists to start ");
            return null;
        }
        int total_item= rosters.size();
        Roster_Info rst=null;
        Current_roster=null;
        while(indx<total_item)
        {
            rst=rosters.get(indx);
            //Integer.parseInt(rst.getStarted())<=0

            if (rst.getRecordNo().equalsIgnoreCase("1071358")){
                Current_roster=rst;
            }

            if (Integer.parseInt(rst.getCompleted())<=0  && Integer.parseInt(rst.getStarted())>0 && Check_End_Duration(rst.getStart_Time(),rst.get_End_Time())  )
            {

                Current_roster=rst;


                break;
            }
            indx=indx+1;

        }
        return Current_roster;
    }
    boolean Check_End_Duration(String start_time, String end_time){
        boolean sts=false;

        String format="yyyy/MM/dd HH:mm";
        String curr_time="";
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        SimpleDateFormat    sdf = new SimpleDateFormat(format,Locale.getDefault());
        Calendar c = Calendar.getInstance();
        Date dt=c.getTime();

        try{

            curr_time =sdf.format(dt);

            Date date1=sdf.parse(curr_time);
            Date start = sdf.parse (curr_time.substring(0, 10) + " " + start_time);
            Date end =sdf.parse(curr_time.substring(0, 10) + " " + end_time);

            c.setTime(end);
            c.add(Calendar.MINUTE, 5);  // 5 minutes margin
            end = c.getTime();


            if ( date1.compareTo(start)>=0 && end.compareTo(date1)>=0)
                sts=true;

        }catch (Exception ex){

        }

        return sts;
    }

    boolean Check_valid_Duration(String start_time, String end_time){
        boolean sts=false;

        String format="yyyy/MM/dd HH:mm";
        String curr_time="";
        SimpleDateFormat    sdf = new SimpleDateFormat(format,Locale.getDefault());
        Calendar c = Calendar.getInstance();
        Date dt=c.getTime();

        try{

            Time today = new Time(Time.getCurrentTimezone());
            today.setToNow();
            curr_time =sdf.format(dt);

            Date date1=sdf.parse(curr_time);
            Date start = sdf.parse (curr_time.substring(0, 10) + " " + start_time);
            Date end =sdf.parse(curr_time.substring(0, 10) + " " + end_time);
            Calendar cal =Calendar.getInstance();
            Date curr= cal.getTime();


            // Toast.makeText(getApplicationContext(),"curr_time " + date1 + "\n start=" + start + "\n end=" + end , Toast.LENGTH_LONG).show();
            long diff = date1.getTime() - start.getTime();
            long diff2 = date1.getTime() - end.getTime();

            long duration = end.getTime() - start.getTime();


            if (curr.getTime()>=start.getTime() && curr.getTime()<=end.getTime())
            {
                sts=true;
                // Toast.makeText(getApplicationContext(),"Condition True\nCurrent " + date1.getTime() + "\n Start=" + start.getTime() + "\n end=" + end.getTime() , Toast.LENGTH_LONG).show();

            }

        }catch (Exception ex){
            // Toast.makeText(getApplicationContext(),ex.toString() , Toast.LENGTH_LONG).show();
        }

        return sts;
    }

    public void Set_Job(String RecordNo)
    {


        if (Current_roster.getStarted().equals("1")) {
            //  ((TextView) findViewById(R.id.textMsg)).setText("Job already started");
            Toast.makeText(getApplicationContext(),"Job already started", Toast.LENGTH_LONG).show();
            return;}
        if (Integer.parseInt(Current_roster.getCompleted())==Integer.parseInt( RecordNo)) {
            // ((TextView) findViewById(R.id.textMsg)).setText("Job already has been completed");
            Toast.makeText(getApplicationContext(),"Job already has been completed", Toast.LENGTH_LONG).show();
            return;}
              /*  if (Job_already_started(RecordNo)>0) {
               //((TextView) findViewById(R.id.textMsg)).setText("An other Job already has been started");
               Toast.makeText(getApplicationContext(),"Job already has been completed", Toast.LENGTH_LONG).show();
               return;}*/



        if (server_available==false)
        {
            //  ((TextView) findViewById(R.id.textMsg)).setText("calling Set_Job2");
            Set_Job2(Current_roster.getRecordNo());
            return;
        }

        try
        {
            String URL = root  + "/TimeSheet.asmx?op=StartJob";
            String SOAP_ACTION =  "https://tempuri.org/StartJob";
            String METHOD_NAME = "StartJob";


            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
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
            pi3.setValue("");
            request.addProperty(pi3);

            PropertyInfo pi4=new PropertyInfo();
            pi4.setName("Latitude");
            pi4.setValue(Latitude);
            request.addProperty(pi4);

            PropertyInfo pi5=new PropertyInfo();
            pi5.setName("Longitude");
            pi5.setValue(Longitude);
            request.addProperty(pi5);

            PropertyInfo pi6=new PropertyInfo();
            pi6.setName("Location");
            pi6.setValue(Location_Address);
            request.addProperty(pi6);

            //  Toast.makeText(getApplicationContext(), "Location_Address" + Location_Address + ", " + Latitude +", "+ Longitude, Toast.LENGTH_LONG).show ();
            //  ((TextView) findViewById(R.id.textMsg)).setText("Location_Address" + Location_Address + ", " + Latitude +", "+ Longitude);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION, envelope);
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            String currentDateString = dateFormat2.format(date);

            SoapPrimitive  result =(SoapPrimitive)envelope.getResponse();
            if (Boolean.valueOf(result.toString())==true){

                //  ((TextView) findViewById(R.id.textMsg)).setText("Job cancelled successfully");
                //Toast.makeText(getApplicationContext(),"Job started successfully", Toast.LENGTH_LONG).show();
               String msg="";
                    // Set_Job(Current_roster.getRecordNo());
                    msg= "Your Job has been started successfull\n"
                            + " From " + Current_roster.getStart_Time() + " to " + Current_roster.get_End_Time();

                Tost_Message(msg);

                xml.Update_Roster_Node(RecordNo,"Started","1");

            }

            else
                // ((TextView) findViewById(R.id.textMsg)).setText("Operation not done due to some server problem " );
                Toast.makeText(getApplicationContext(),"Operation not done due to some server problem", Toast.LENGTH_LONG).show();

        }catch(Exception ex){Toast.makeText(getApplicationContext(),ex.toString(), Toast.LENGTH_LONG).show();}

    }
    public void Set_Job2(String RecordNo)
    {
        try
        {

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            Date date = new Date();
            String strDate=dateFormat.format(date);


            String command="" ;

            command="\nC" + "`"+ RecordNo + "`" + strDate ;

            set_Updates(command);


            DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
            String currentDateString = dateFormat2.format(date);


            // Toast.makeText(getApplicationContext(),"Job started successfully", Toast.LENGTH_LONG).show();

            xml.Update_Roster_Node(RecordNo,"Started","1");

            String msg="";
            // Set_Job(Current_roster.getRecordNo());
            msg= "Your Job has been started successfull\n"
                    + " From " + Current_roster.getStart_Time() + " to " + Current_roster.get_End_Time();

            Tost_Message(msg);

        }catch(Exception ex){
            ((TextView) findViewById(R.id.textMsg)).setText("Operation not done successfully due to some server problem");
        }

    }
    public void End_Job(String RecordNo)
    {

        if (server_available==false)
        {
            End_Job2(Current_roster.getRecordNo());
            return;
        }
        String URL2 = root  + "/TimeSheet.asmx?op=EndJob";
        String SOAP_ACTION2 =  "https://tempuri.org/EndJob";
        String METHOD_NAME2 = "EndJob";

        try
        {
            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME2);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL2);
            androidHttpTransport.debug =true;

            PropertyInfo pi=new PropertyInfo();
            pi.setName("Recordno");

            pi.setValue(getSecurityToken () +  RecordNo);
            request.addProperty(pi);
            PropertyInfo pi3=new PropertyInfo();
            pi3.setName("cancel");
            pi3.setValue(false);
            request.addProperty(pi3);



            PropertyInfo pi4=new PropertyInfo();
            pi4.setName("Latitude");
            pi4.setValue(Latitude);
            request.addProperty(pi4);

            PropertyInfo pi5=new PropertyInfo();
            pi5.setName("Longitude");
            pi5.setValue(Longitude);
            request.addProperty(pi5);

            PropertyInfo pi6=new PropertyInfo();
            pi6.setName("Location");
            pi6.setValue(Location_Address);
            request.addProperty(pi6);

            Date date= new Date();
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
            String currentDateString = dateFormat2.format(date);

            PropertyInfo pi7=new PropertyInfo();
            pi7.setName("timeStamp");
            pi7.setValue("");
            request.addProperty(pi7);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION2, envelope);

            SoapPrimitive  result =(SoapPrimitive)envelope.getResponse();

            if (Boolean.valueOf(result.toString())==true){

                String msg="";

                msg= "Your Job has been completed successfully \n"
                        + " From " + Current_roster.getStart_Time() + " to " + Current_roster.get_End_Time();
                Tost_Message(msg);
                try{
                    xml.Update_Roster_Node(RecordNo,"Started","0");
                    //	   Toast.makeText(getApplicationContext(),"Job stopped successfully", Toast.LENGTH_LONG).show();

                }catch (Exception ex){}

            }
            else
               Tost_Message("Operation not done due to some server error");

        }catch(Exception ex){

            Tost_Message("Operation not done due to some server error\n" +  ex.toString());
        }

    }
    public void End_Job2(String RecordNo)
    {
        try
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            Date date = new Date();
            String strDate=dateFormat.format(date);
            String command="" ;


            command = "\n update eziTracker_log set LODateTime = '" + strDate + "', " +
                    " WorkDuration = Convert(Decimal(15, 4), DATEDIFF(n, DateTime, '" + strDate + "'))/60 " +
                    " WHERE JobNo = " + RecordNo;

            set_Updates(command);
            //  Toast.makeText(getApplicationContext(),"Operation done successfully", Toast.LENGTH_LONG).show();


            DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
            String currentDateString = dateFormat2.format(date);


            try{
                xml.Update_Roster_Node(RecordNo,"Completed",RecordNo);
                String msg="";

                msg= "Your Job has been completed successfully \n"
                        + " From " + Current_roster.getStart_Time() + " to " + Current_roster.get_End_Time();
                Tost_Message(msg);

            }catch (Exception ex){}
          //  ((TextView) findViewById(R.id.textMsg)).setText("Job stopped successfully");




        }catch(Exception ex){
            // ((TextView) findViewById(R.id.textMsg)).setText("Operation not done due to some server error \n" + ex.toString());
            Toast.makeText(getApplicationContext(),"Operation not done due to some server error", Toast.LENGTH_LONG).show();
        }
    }
    public void set_Updates(String command ) throws IOException {
        // File froot = null;
        try {
            // check for SDcard
           // ((TextView) findViewById(R.id.textMsg)).setText("making update");
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File filein = null;
            File fileDir = null;
            String state = Environment.getExternalStorageState();
//  textMsg.setText("No Server found - " + state);
            if (Environment.MEDIA_MOUNTED.equals(state)) {

                //check sdcard permission
                fileDir = new File(froot.getAbsolutePath() + "/.server/");

                filein = new File(fileDir, "Updates.txt");

                if (filein.exists()) {
                    try {
                        froot.setWritable(true);
                        //if (froot.canWrite()) {

                        File file = new File(fileDir, "Updates.txt");
                        FileWriter filewriter = new FileWriter(file, true);
                        BufferedWriter out = new BufferedWriter(filewriter);
                        out.write(command);
                        out.close();

                    } catch (Exception e) {

                        //Log.e("Exception","error occurred while creating xml file");
                    }
                } else {
                    froot.setWritable(true);

                    //if (froot.canWrite()) {

                    if (!fileDir.exists())
                        fileDir.mkdirs();

                    File file = new File(fileDir, "Updates.txt");
                    FileWriter filewriter = new FileWriter(file, true);
                    BufferedWriter out = new BufferedWriter(filewriter);
                    out.write(command);
                    out.close();
                }
            }
        } catch (Exception e) {

     //       ((TextView) findViewById(R.id.textMsg)).setText("Operation done successfully");
        }
    }


    void Tost_Message(String message){
        final Context context=getApplicationContext();
        final String msg=message;
        Handler handler =  new Handler(context.getMainLooper());
        handler.post( new Runnable(){
            public void run(){
                Toast.makeText(context, msg,Toast.LENGTH_LONG).show();
            }
        });
    }
    public boolean isOnline(Context context) {

        ConnectivityManager connectivityManager;
        NetworkInfo wifiInfo, mobileInfo;
        boolean connected = false;
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            ///System.out.println("CheckConnectivity Exception: " + e.getMessage());
            // Log.v("connectivity", e.toString());

        }
        return connected;
    }
    String getSecurityToken(){

        String Val= OperatorId  + "$" + Security_Token + "$";
        return Val;
    }
    public void set_Login_Mode_status_In_DB(String mode) {

        try {
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);


            File filein = null;
            File fileDir = null;
            PinCode_Mode = false;
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {

                //check sdcard permission
                fileDir = new File(froot.getAbsolutePath() + "/.server/");
                filein = new File(fileDir, "PP.txt");
                if (!filein.exists()) {
                    try {
                        if (!fileDir.exists())
                            fileDir.mkdirs();
                    } catch (Exception e) {
                    }
                }

                try {
                    froot.setWritable(true);
                    //if (froot.canWrite()) {

                    File file = new File(fileDir, "PP.txt");
                    FileWriter filewriter = new FileWriter(file);
                    BufferedWriter out = new BufferedWriter(filewriter);
                    out.write(mode);
                    out.close();
                } catch (Exception e) {
                }


            }

        } catch (Exception e) {

            //              Log.e("Exception","error occurred while creating xml file");
        }
    }
}
