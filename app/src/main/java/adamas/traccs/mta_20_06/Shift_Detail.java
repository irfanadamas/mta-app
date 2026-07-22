
package adamas.traccs.mta_20_06;

        import android.Manifest;

        import android.content.IntentFilter;
        import android.content.res.Configuration;
        import android.location.Location;
        import android.location.LocationManager;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.Handler;

        import android.text.Html;
        import android.util.Base64;
        import android.view.View;

        import android.view.Menu;
        import android.view.MenuItem;
        import android.net.wifi.WifiConfiguration;

        import java.io.BufferedReader;
        import java.io.BufferedWriter;
        import java.io.ByteArrayOutputStream;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.FileReader;
        import java.io.FileWriter;
        import java.io.IOException;
        import java.nio.ByteBuffer;

        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.List;
        import java.util.Locale;
        import java.util.Timer;


        import javax.xml.parsers.DocumentBuilder;
        import javax.xml.parsers.DocumentBuilderFactory;
        import javax.xml.parsers.ParserConfigurationException;



        import org.json.JSONObject;
        import org.ksoap2.SoapEnvelope;
        import org.ksoap2.serialization.PropertyInfo;
        import org.ksoap2.serialization.SoapObject;
        import org.ksoap2.serialization.SoapPrimitive;
        import org.ksoap2.serialization.SoapSerializationEnvelope;
        import org.ksoap2.transport.HttpTransportSE;
        import org.w3c.dom.Document;
        import org.w3c.dom.Element;
        import org.w3c.dom.Node;
        import org.w3c.dom.NodeList;
        import org.xml.sax.SAXException;
        import org.xmlpull.v1.XmlSerializer;

        import androidx.appcompat.app.ActionBar;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.app.ActivityCompat;
        import androidx.core.text.HtmlCompat;
        import androidx.recyclerview.widget.DefaultItemAnimator;
        import androidx.recyclerview.widget.DividerItemDecoration;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import timesheet.NetworkStateReceiver;

        import android.accounts.Account;
        import android.accounts.AccountManager;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.app.TimePickerDialog;
        import android.content.ContentResolver;
        import android.content.Context;
        import android.content.ContextWrapper;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.pm.PackageManager;
        import android.content.pm.ResolveInfo;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Path;
        import android.graphics.RectF;

        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.net.Uri;

        import android.os.AsyncTask;
        import android.os.Environment;
        import android.os.StrictMode;
        import android.util.AttributeSet;
        import android.util.Log;
        import android.util.Xml;
        import android.view.Gravity;
        import android.view.MotionEvent;
        import android.view.ViewGroup;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.TimePicker;
        import android.widget.Toast;

        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
       // import com.android.volley.VolleyError;
        //import com.android.volley.toolbox.StringRequest;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;
        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.GooglePlayServicesUtil;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.common.api.PendingResult;
        import com.google.android.gms.common.api.Status;
        import com.google.android.gms.location.LocationListener;
        import com.google.android.gms.location.LocationRequest;
        import com.google.android.gms.location.LocationServices;
        import com.google.android.material.navigation.NavigationView;

        import android.location.Address;
        import android.location.Geocoder;


public class Shift_Detail extends AppCompatActivity
        implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        NavigationView.OnNavigationItemSelectedListener
        , NetworkStateReceiver.NetworkStateReceiverListener {

    public String root = "https://58.162.142.150/timesheet";
    private final String NAMESPACE = "https://tempuri.org/";
    private String URL = root + "/TimeSheet.asmx?op=StartJob";
    private final String SOAP_ACTION = "https://tempuri.org/StartJob";
    private final String METHOD_NAME = "StartJob";

    private String URL2 = root + "/TimeSheet.asmx?op=EndJob";
    private final String SOAP_ACTION2 = "https://tempuri.org/EndJob";
    private final String METHOD_NAME2 = "EndJob";

    private String URL3 = root + "/TimeSheet.asmx?op=AcceptTimes";
    private final String SOAP_ACTION3 = "https://tempuri.org/AcceptTimes";
    private final String METHOD_NAME3 = "AcceptTimes";

    private String URL4 = root + "/TimeSheet.asmx?op=Add_client_Note";
    private final String SOAP_ACTION4 = "https://tempuri.org/Add_client_Note";
    private final String METHOD_NAME4 = "Add_client_Note";


    private String URL6 = root + "/CaseManagement.asmx?op=GetDomain_with_Criteria";
    private final String SOAP_ACTION6 = "https://tempuri.org/GetDomain_with_Criteria";
    private final String METHOD_NAME6 = "GetDomain_with_Criteria";


    // public DefaultHttpClient httpsClient;
    int loading_Recipient=0;
    String urlString = root + "/traccs_error_log.txt";
    String ErroString = "";
    float bandwidth = 0;
    Location_Address loc_address = new Location_Address();
    boolean View_only = false;
    boolean ExcludeFromAppLogging=false;
    int screen_width = 0;
    Button btn_exit;
    Button btn_extra_info;
    Button btn_time;
    Button btn_start_job;
    Button btn_End_job;
    Button btn_client_note;
    Button btn_ack;
    Button btn_Pic;
    Button btnSign;
    Button btnIncident;
    Button btnLeave;
    boolean Job_end_done = false;
    boolean update = false;
    String RecordNo;
    Button btn;
    TextView txtAddress;
    Button btn_travel;
    TextView lblTime;
    TextView lblTime2;
    Email_Settings email_seting = null;
    boolean email_seting_call = false;
   private boolean menu_displayed=false;
    TextView txtServiceType;
    List<String> lst_shiftgoals;
    String tmp_cor_email = "";
    String Recipient_PinCode = "0";
    String Mobility = "";
    String duration = "";
    String Client_address="";
    String Careplanchange = "";
    String specialConsideration = "";
    String notes_from_roster="";
    String RunsheetAlerts = "-";
    String RosterNotes = "";

    String AccountNo = "";
    String OperatorID = "";
    String address = "";
    String Phone = "";
    String TMMode = "0";
    String MobileFutureLimit = "15";
    String Program = "";
    String Service_Setting = "";
    String RecipientDocFolder = "";
    int roster_type;
    boolean time_set_done = false;
    boolean Server_Available = true;
    String StaffCode = "";
    String ServiceType = "";
    DateFormat fmtDateAndTime = DateFormat.getDateTimeInstance();
    Calendar myCalendar = Calendar.getInstance(fmtDateAndTime.getTimeZone());
    File froot = null;
    Context context;
    TextView txtAcknowledge;
    ListView gridView;
    Boolean Task_exists = false;
    // TextView txtAcknowledge;
    String msg;
    Task[] items;
    private ArrayList<Task> lst_task = null;
    private int task_elements = 0;
    String TimesheetReights;
    String Personid = "";
    boolean ForceShiftReport, HasServiceNotes;
    String MyOwnCarStatus = "0";
    String MTAServiceType = "";
    String barCode = "";
    String KMAgainstTravelOnly;
    String Button_Pressed = "";
    String AllowSetTime = "";
    String TAMode = "";
    String QR = "";
    String AllowPicUpload = "false";
    String RosterDate = "";
    String Started_Job_No = "0";
    String start_time = "";
    String EndTime = "";
    String StartTime = "";
    String Job_Message = "";
    String Time_String = "";
    String UserId = "";
    boolean task_checked = false;
    boolean task_checked_do_Job = false;
    boolean Process_Sleep_Over;
    int Time_count = -2;
    Timer timer;
    String Latitude = "0";
    String Longitude = "0";
    String Current_Address = "";
    String PinCode = "0";
    String Simple_Address = "";
    String MobileIncident = "false";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    //private GoogleApiClient mGoogleApiClient;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int STATIC_INTEGER_VALUE = 1;
    private static final int STATIC_INTEGER_VALUE2 = 2;

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 10;

    LocationRequest mLocationRequest;
    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    double last_lat, last_lon;

    long cntr = 0;
    int location_interval = 0;
    String OperatorId = "";
    String Security_Token = "";
    String GPS_Status = "";
    String UserSessionLimit = "10";
    String email_msg = "";

    double Distance = 0;

    String Actual_Client_Code = "";
    LinearLayout mContent;
    Shift_Detail.Signature mSignature = null;
    View mView;
    Button mClear, mGetSign, mCancel, getsign_login;
    byte[] sign_buffer = null;
    String picturePath = "";
    File mypath;
    public static String tempDir;
    boolean Signature_Mode = true;
    String mobilegeocodelimit = "1";
    String UniqueId = "";

    LocationManager locationManager = null;
    String Apply_Goe_Location_Setting = "true";
    User_Settings user_settings = null;

    String TA_TRAVELDEFAULT = "-";
    String Exclude_Goe_Location_Setting = "False";
    String Email_Subject = "";

    private String UseOPNoteAsShiftReport = "0";
    private String UseServiceNoteAsShiftReport = "0";

    Email email;
    String FirstName = "";
    String lastName = "";
    String dateOfBirth = "";
    String age = "";
    String Title = "";
    String PreferredName = "";
    Bulk_Data bulk_data;




    boolean loction_flag = false;
    boolean Settings_Done = false;
    String Shift_Status = "";
    final String PREFS_NAME = "MTAPrefs";
    SharedPreferences settings = null;
    ArrayList<GroupAlerts> lst_groups = null;
    ArrayList<String> lst_leave = null;


    String Receipient_Email = "";
    String Cordinator_Email = "";
    String Staff_Coordinator_Email = "";
    String RECIPIENT_COORDINATOR = "";
    static String possibleEmail = "";
    static String packageName = "";
    static String email_name = "";
    static int MinimumInternetSpeedForOnline = 10;
    XmlData xml ;
    NavigationView navigationView = null;
    String MinorGroup = "";
    TextView txtClient;
    NetworkStateReceiver networkStateReceiver;
    boolean flagDecoration;

    String[] permissions = {
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.READ_INTERNAL_STORAGE",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_NETWORK_STATE",
            "android.permission.ACCESS_WIFI_STATE"


    };

    protected boolean shouldAskPermissions() {
        try {
            return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
        } catch (Exception ex) {
        }

        return false;
    }


    protected void askPermissions() {
        int requestCode = 200;
        try {
            requestPermissions(permissions, requestCode);
        } catch (Exception ex) {
        }
    }


    TimePickerDialog.OnTimeSetListener d = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            duration = hour + ":" + minute;

            if (time_set_done == false) {
                lblTime.setText("Shift/Job No. " + RecordNo + " Job Start Time: " + start_time + " Duration: " + duration);
                Set_Job_Time();
            }
        }
    };

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            start_time = hour + ":" + minute;
            lblTime.setText("Shift/Job No." + RecordNo + " Job Start Time: " + start_time + " Duration: " + duration);
            // if (view.callOnClick())

        }
    };

    private String set_leading_zero(int val, int size) {
        String new_val = String.valueOf(val);
        size = size - (new_val.length());
        for (int i = 0; i < size; i++) {
            new_val = "0" + new_val;
        }
        return new_val;
    }


   /* @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // onSaveInstanceState(newBundy);
          //  Resolution_Setting();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // onSaveInstanceState(newBundy);

          //  Resolution_Setting();
        }

    }*/

    private void handleUncaughtException(Thread thread, Throwable e) {

        // The following shows what I'd like, though it won't work like this.
        //Intent intent = new Intent (getApplicationContext(),MainActivity.class);
        // startActivity(intent);
        Tost_Message( " Unable to load the activity");

        finish();
        // Add some code logic if needed based on your requirement
    }

    @Override
    public void networkAvailable() {

        if (Server_Available == false) {
            Tost_Message("Online Connection becomes available\nRe-login the App in online mode");
            finish();
        }

        Server_Available = true;
        /* TODO: Your connection-oriented stuff here */
    }

    @Override
    public void networkUnavailable() {
        Server_Available = false;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);

        Exit_Shift();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift__detail);
        context=this.getApplicationContext();
        //  setContentView(R.layout.content_shift__detail__view);
        //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
        setupActionBar();

        flagDecoration=true;
        xml = new XmlData(context);
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

/*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);*/
        loading_Recipient=0;

        Bundle bundle = getIntent().getExtras();

        settings = getSharedPreferences(PREFS_NAME, 0);
        txtServiceType = findViewById(R.id.txtServiceType);
        txtAcknowledge = ((TextView) findViewById(R.id.txtAcknowledge));
        lblTime = (TextView) findViewById(R.id.txtTime_Actual);

        final TextView tvaddress = (TextView) findViewById(R.id.txtAddress);
        txtAcknowledge = ((TextView) findViewById(R.id.txtAcknowledge));
        txtClient = findViewById(R.id.txtClient);

        try {

            root = settings.getString("root", "");

            set_server_Ip(root);

            urlString = root + "/timesheet.asmx";

            Server_Available = isOnline(getApplicationContext());


/*
            if (lblTime2.getText().toString().equalsIgnoreCase("time")){
                try{
                    new MyAsyncClass4_Recipient().execute();
                } catch(Exception ex){return;}
            }
*/

            try {

                if (bundle.get("RecordNo") == null)
                    RecordNo = "0";
                else
                    RecordNo = bundle.getString("RecordNo");

                TMMode = bundle.getString("TMMode");

                if (bundle.get("RosterDate") == null) {
                    DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
                    Date date = new Date();
                    RosterDate = dateFormat2.format(date);
                } else
                    RosterDate = bundle.get("RosterDate").toString();

                try {
                    notes_from_roster = bundle.get("notes").toString();
                }catch(Exception ex){}

                try {
                    Client_address = bundle.getString("Address").replace(",","<br>");
                } catch (Exception ex) {
                }
                // Started_Job_No= bundle.getString("Started_Job_No");

            } catch (Exception ex) {
                Tost_Message(ex.toString());
            }
            if (bundle.get("AccountNo") == null)
                AccountNo = "ADMINISTRATION";
            else
                AccountNo = bundle.getString("AccountNo");


            try {
                Actual_Client_Code = bundle.getString("Actual_Client_Code");
            } catch (Exception ex) {
            }
            if (bundle.get("Roster_type") == null)
                roster_type = 0;
            else
                roster_type = Integer.parseInt(bundle.get("Roster_type").toString());

            if (bundle.get("ServiceType") == null)
                ServiceType = "Nothing";
            else
                ServiceType = bundle.get("ServiceType").toString();

            try {

                RunsheetAlerts = "-";
                if (bundle.get("RunsheetAlerts") != null) {

                    RunsheetAlerts = bundle.get("RunsheetAlerts").toString();
                }
            } catch (Exception ex) {
            }
            if (bundle.get("Program") == null)
                Program = "";
            else
                Program = bundle.get("Program").toString();

            if (bundle.get("StartTime") == null)
                StartTime = "";
            else
                StartTime = bundle.get("StartTime").toString();

            if (bundle.get("EndTime") == null)
                EndTime = "";
            else
                EndTime = bundle.get("EndTime").toString();

            try {
                if (bundle.get("MinorGroup") == null)
                    MinorGroup = "";
                else
                    MinorGroup = bundle.get("MinorGroup").toString();
            } catch (Exception ex) {
            }

            try {
                if (bundle.get("MTAServiceType") == null)
                    MTAServiceType = "";
                else
                    MTAServiceType = bundle.get("MTAServiceType").toString();
            } catch (Exception ex) {
            }

            try {
                if (bundle.get("MyOwnCarStatus") == null)
                    MyOwnCarStatus = "0";
                else
                    MyOwnCarStatus = bundle.get("MyOwnCarStatus").toString();
            } catch (Exception ex) {
            }


            try {
                if (bundle.get("Service_Setting") == null)
                    Service_Setting = "-";
                else
                    Service_Setting = bundle.get("Service_Setting").toString();
            } catch (Exception ex) {
            }
            try {
                if (bundle.get("Shift_Status") == null)
                    Shift_Status = "";
                else
                    Shift_Status = bundle.get("Shift_Status").toString();
            } catch (Exception ex) {
            }

            try {
                View_only = bundle.getBoolean("View_only");
            } catch (Exception ex) {
            }
            try {
                ExcludeFromAppLogging =Boolean.parseBoolean(bundle.getString("ExcludeFromAppLogging"));
            } catch (Exception ex) {
            }

            try {
                ForceShiftReport =Boolean.parseBoolean(bundle.getString("ForceShiftReport"));
            } catch (Exception ex) {
            }

            try {
                HasServiceNotes = settings.getBoolean("HasServiceNotes",false);
            } catch (Exception ex) {
            }

            MobileFutureLimit = settings.getString("MobileFutureLimit", "10");
            OperatorID = settings.getString("OperatorId", "0"); //bundle.getString("User");
            Security_Token = settings.getString("Security_Token", "-");


            RecipientDocFolder = settings.getString("RecipientDocFolder", "0");//bundle.get("RecipientDocFolder").toString();
            AllowSetTime = settings.getString("AllowSetTime", "false");//bundle.get("RecipientDocFolder").toString();
            TAMode = settings.getString("TAMode", "false");//bundle.get("RecipientDocFolder").toString();
            AllowPicUpload = settings.getString("AllowPicUpload", "false");//bundle.get("RecipientDocFolder").toString();
            StaffCode = settings.getString("StaffCode", "ABC");
            Security_Token = settings.getString("Security_Token", "");
            OperatorId = settings.getString("OperatorId", "0");
            Apply_Goe_Location_Setting = settings.getString("Apply_Goe_Location_Setting", "False");
            Exclude_Goe_Location_Setting = settings.getString("Exclude_Goe_Location_Setting", "False");
            Process_Sleep_Over = false;


            String  rosterDate = bundle.getString("RosterDate","");




            try {
                //new MyAsyncClass().execute();
                Job_Status2(RecordNo);
            } catch (Exception ex) {
            }

            try {
                getRoster_Recipient2(RecordNo);
                // new MyAsyncClass4_Recipient_locally().execute();
            } catch (Exception ex) {
            }

            getRecipient_Address(Actual_Client_Code);

            Get_User_Settings2();

            if (txtClient.getText().toString().equalsIgnoreCase("MTA Mullar A")){
                try{

                    //bulk_data= new Bulk_Data(root,StaffCode,OperatorId,Security_Token,Server_Available,rosterDate,context);

                    txtClient.setText("");
                    if (Server_Available){
                        new MyAsyncClass4_Recipient().execute();
                        //MyAsyncClass5_Recipient
                       // new MyAsyncClass5_Recipient().execute();
                    }

                } catch(Exception ex){return;}
            }


        } catch (Exception ex) {
            txtAcknowledge.setText("Operation not done due to some server error\n" + ex.toString());
        }

        try {

            //  if(Server_Available && (Apply_Goe_Location_Setting.equalsIgnoreCase("true"))) {
            if (isGooglePlayServicesAvailable()) {

                createLocationRequest();
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .enableAutoManage(this, 0, this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
            }
        } catch (Exception ex) {
        }

        try {


            msg = "";

            if (Actual_Client_Code.equalsIgnoreCase("!MULTIPLE") || Actual_Client_Code.equalsIgnoreCase("!INTERNAL")) {
                msg="";
            } else {
               // msg = "\n" + (address == null ? "-" : address);
                //  msg = msg + "\nPhone : " + Phone + "\n";
                msg=Client_address;
            }


            tvaddress.setText(HtmlCompat.fromHtml(msg,0));
            tvaddress.setTag(address);
            tvaddress.setVisibility(View.VISIBLE);
            String[] strTime;
            Time_String = bundle.getString("Time_String");
            msg = Time_String;
            strTime = msg.split(",");
            start_time = strTime[0];
            duration = strTime[1];
            //    lblTime.setText(  "Shift/Job No." +RecordNo + " Job Start Time: " + start_time + " Duration: " + duration );


        } catch (Exception ex) {

            Tost_Message("Operation not done due to some server error\n" + ex.toString());
        }
        set_Permission();

        //View rl_task=findViewById(R.id.cardView4) ;
        TextView txtTasks = findViewById(R.id.txtTasks);
        ListView lstViewTask = findViewById(R.id.lstViewTask);

        if (TaskList_checked_status()) {
            Task_exists = true;
            txtTasks.setVisibility(View.VISIBLE);
            lstViewTask.setVisibility(View.VISIBLE);


         //   lstViewTask.setAdapter(new ListAdapter(this, items, root, Server_Available, OperatorId, Security_Token, settings));
           try {
               getTskList2();
           }catch(Exception ex){}
            Task t = null;
            items = new Task[task_elements];
            for (int i = 0; i < task_elements; i++) {
                t = lst_task.get(i);
                //  tv.setText(tv.getText() + "\n" + t.getRecordNo() + " " + t.TaskCOmpleteget() + " " + t.getTaskDetail());
                items[i] = t;
            }
            if (task_elements > 0) {
                lstViewTask.setAdapter(new ListAdapter(this, items, root, Server_Available, OperatorId, Security_Token, settings));
                setListViewHeightBasedOnChildren(lstViewTask);
            } else {

                lstViewTask.setVisibility(View.GONE);
            }


        } else {
            txtTasks.setVisibility(View.GONE);
            lstViewTask.setVisibility(View.GONE);
        }

        txtTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog_Task(v);
            }
        });

        com.mikhaellopez.circularimageview.CircularImageView profile_image = findViewById(R.id.profile_image);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Actual_Client_Code.equalsIgnoreCase("!INTERNAL") || Actual_Client_Code.equalsIgnoreCase("!MULTIPLE") )
                    Tost_Message("No profile for group booking");
               else
                   show_profile(v.getContext());
            }
        });

        txtClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Actual_Client_Code.equalsIgnoreCase("!INTERNAL") || Actual_Client_Code.equalsIgnoreCase("!MULTIPLE") )
                    Tost_Message("No profile for group booking");
                else
                    show_profile(v.getContext());
            }
        });
        TextView txtAddress = findViewById(R.id.txtAddress);
        txtAddress.setText(Html.fromHtml("" +  Client_address+ ""));
        txtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                show_Map();
            }
        });

        ImageView imageView2 = findViewById(R.id.imageView2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_Map();
            }
        });
        btn_start_job = (Button) findViewById(R.id.btnstartjob);
        btn_start_job.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                long recNo=0;
                try {

                    //   recNo = Job_already_started2(RecordNo);
                    recNo = Long.parseLong(settings.getString("Started_Job_No", "0"));

                } catch (Exception e) {
                }

                if (recNo > 0 && recNo != Integer.parseInt(RecordNo) && (!MinorGroup.equalsIgnoreCase("BREAK"))) {
                    //((TextView) findViewById(R.id.txtAcknowledge)).setText("Job already has been completed");
                    ((TextView) findViewById(R.id.txtAcknowledge)).setText("Another Job already has been started");
                    return;
                }


                if (settings.getString("ApplyAcceptShift","false").equalsIgnoreCase("true")){

                    Boolean acceptedbyStaff = false;

                    try {
                        acceptedbyStaff = Boolean.parseBoolean(bundle.getString("acceptedbyStaff"));
                    } catch (Exception ex) {
                    }

                    String newAcceptDate = bundle.getString("NewAcceptDate");
                    boolean result = isDateGreaterOrEqual(newAcceptDate);

                    if (result)
                    {
                       acceptRosters();
                        return;
                    }
                }

                if (btn_start_job.getText().toString().startsWith("START")) {
                    ShowDialog_for_Job_Start(v, "Are you sure you want to Start Job");
                } else if (btn_start_job.getText().toString().startsWith("END")) {

                   // ShowDialog_for_End_Job(v, "Are you sure you want to End Job\nEnsure you add km travel if needed \n Ensure you added shift report");
                    ShowDialog_for_End_Job(v, " \n Do you need to add Travel Claim \n \n Ensure you have added a Client Note \n \n Are you sure you want to End Job");
                } else {
                    ShowDialog_for_Job_Start(v, "Are you sure you want to cancel Start Job");

                    //ShowDialog_for_End_Job(v, "Are you sure you want to cancel End Job");
                }


            }
        });
        btn_time = (Button) findViewById(R.id.btnsetTime);
        btn_time.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (AllowSetTime.equalsIgnoreCase("true")){

                    if (settings.getString("ApplyAcceptShift","false").equalsIgnoreCase("true")){

                        Boolean acceptedbyStaff = false;

                        try {
                            acceptedbyStaff = Boolean.parseBoolean(bundle.getString("acceptedbyStaff"));
                        } catch (Exception ex) {
                        }

                        String newAcceptDate = bundle.getString("NewAcceptDate");
                        boolean result = isDateGreaterOrEqual(newAcceptDate);
                        if (result)
                        {
                           acceptRosters();
                            return;
                        }
                    }

                    ShowDialog_for_Set_Job(btn_time, "Are You Sure You want to set Job Timing");
                }else
                {
                    Tost_Message("You do not have permission to perform this operation");
                }
            }

        });

        btn_ack = (Button) findViewById(R.id.btnAcknowledge);
      /*  final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(1000); // duration - 1000 for 1 second and 500 for half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
      btn_ack.startAnimation(animation);
*/

        btn_ack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                btn_start_job.setVisibility(View.VISIBLE);
                btn_time.setVisibility(View.VISIBLE);
                txtAcknowledge.setVisibility(View.VISIBLE);
                if (AllowSetTime.equalsIgnoreCase("true")) {
                    btn_time.setBackground(getDrawable(R.drawable.btn_dark_blue_simple));
                    btn_time.setTextColor( getResources().getColor(R.color.background_white));
                }else {
                    btn_time.setBackground(getDrawable(R.drawable.disabled_button));
                    btn_time.setTextColor( getResources().getColor(R.color.text_color));
                }
                btn_ack.setVisibility(View.INVISIBLE);
                // btn_ack.startAnimation(null);

            }
        });



    }

    void acceptRosters(){
        // Create the AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the title and message
        builder.setTitle("Notification");
        builder.setMessage("Please accept the rosters. The shift can not be started before accepting the roster");



        // Optional: Add a neutral button (can be used for a third option)
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle "Neutral" button click
            }
        });

        // Show the dialog
        builder.show();
    }

    public String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public boolean isDateGreaterOrEqual(String newAcceptDate) {
        String dateFormat = "dd/MM/yyyy";  // Adjust this format based on your actual NewAcceptDate string format

        SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);

        try {
            Date newAcceptDateObj           = dateFormatter.parse(newAcceptDate);
            Date currentDate                = dateFormatter.parse(getCurrentDate());

            // Compare the dates
            return currentDate.compareTo(newAcceptDateObj) >= 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    void show_profile(Context context) {

        try {

            Intent it = new Intent(context, Client_Profile.class);
            Bundle b = new Bundle();
            address = address.replace("\n", "");
            b.putString("address", address);
            b.putString("FirstName", FirstName);
            b.putString("PreferredName", PreferredName);
            b.putString("lastName", lastName);
            b.putString("dateOfBirth", dateOfBirth);
            b.putString("age", age);
            b.putString("Cordinator", RECIPIENT_COORDINATOR);
            b.putString("Personid", Personid);
            b.putString("Phone", Phone);
            b.putString("Actual_Client_Code", Actual_Client_Code);
            b.putString("ShowClientPhoneInApp", user_settings.getShowClientPhoneInApp());

            it.putExtras(b);
            startActivity(it);

        } catch (Exception ex) {
        }

    }

    public void Exit_Shift() {


        try {

           // if (Security_Token == null || Server_Available == false) finish();
          //  if (Security_Token.equalsIgnoreCase("")) finish();


            if (Server_Available == true && settings.getBoolean("Update", false) == true && !Security_Token.equals("")) {
                try {

                    new Shift_Detail.MyAsyncClassUpdates().execute();
                   // finish();
                } catch (Exception ex) {
                }

            }

        } catch (Exception ex) {

        } finally {

        }

        //  Shift_Detail.super.onBackPressed();
    }


    /* private void hide_Navigation_Items()
     {
         navigationView = (NavigationView) findViewById(R.id.nav_view);
         Menu nav_Menu = navigationView.getMenu();


         try {
             if( !user_settings.getAllowPicUpload().equalsIgnoreCase("true")) {
                 // Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                 nav_Menu.findItem(R.id.nav_Picture).setVisible(false);
             }else
                 nav_Menu.findItem(R.id.nav_Picture).setVisible(true);
         } catch (Exception ex) {}

         try {
             if (user_settings.getAllowTravelEntry().equalsIgnoreCase("true")){
                 if (!View_only || (View_only && user_settings.get_RestrictTravelSameDay().equalsIgnoreCase("false"))) {
                     nav_Menu.findItem(R.id.nav_Travel).setVisible(true);
                 }else
                     nav_Menu.findItem(R.id.nav_Travel).setVisible(false);

             }else
                 nav_Menu.findItem(R.id.nav_Travel).setVisible(false);
         } catch (Exception ex) {}


         try {
             if ((user_settings.getAllowOPNote().equalsIgnoreCase("true") || user_settings.getAllowCaseNote().equalsIgnoreCase("true") )
                     && !(Actual_Client_Code.equalsIgnoreCase("!MULTIPLE") || Actual_Client_Code.equalsIgnoreCase("!INTERNAL")))
             {
                 nav_Menu.findItem(R.id.nav_Client_Note).setVisible(true);
             } else
                 //if(!user_settings.getAllowClientNoteEntry().equalsIgnoreCase("true"))
                 nav_Menu.findItem(R.id.nav_Client_Note).setVisible(false);


         } catch (Exception ex) {}

         try {
             if(!user_settings.getAllowRosterNoteEntry().equalsIgnoreCase("true"))
                 nav_Menu.findItem(R.id.nav_Roster_Note).setVisible(false);
             else
                 nav_Menu.findItem(R.id.nav_Roster_Note).setVisible(true);
         } catch (Exception ex) {}

         if( !user_settings.getAllowIncidentEntry().equalsIgnoreCase("true"))
             nav_Menu.findItem(R.id.nav_Incident).setVisible(false);
         else
             nav_Menu.findItem(R.id.nav_Incident).setVisible(true);


         try {
             if( !user_settings.getAllowRegisterSign().equalsIgnoreCase("true"))
                 nav_Menu.findItem(R.id.nav_Sign).setVisible(false);
             else
                 nav_Menu.findItem(R.id.nav_Sign).setVisible(true);
         } catch (Exception ex) {}

         if (View_only) {

             nav_Menu.findItem(R.id.nav_Start).setVisible(false);
         }

         if (View_only ) {
             nav_Menu.findItem(R.id.nav_End).setVisible(false);


         }
         if (View_only || !user_settings.getAllowSetTime().equalsIgnoreCase("true") || btn_End_job.getText().toString().contains("Cancel"))
             nav_Menu.findItem(R.id.nav_Set_Time).setVisible(false);



         if (!user_settings.getAllowViewGoalPlans().equalsIgnoreCase("true"))
             nav_Menu.findItem(R.id.nav_Plans).setVisible(false);

         else {
             nav_Menu.findItem(R.id.nav_Plans).setVisible(true);
         }

         if ( user_settings.get_ViewClientCareplans().equalsIgnoreCase("true") ||user_settings.get_ViewClientDocuments().equalsIgnoreCase("true")
                 || user_settings.get_ViewClientCareplans().equalsIgnoreCase("1") ||user_settings.get_ViewClientDocuments().equalsIgnoreCase("1"))
         {
             nav_Menu.findItem(R.id.nav_document).setVisible(true);
         }else {
             nav_Menu.findItem(R.id.nav_document).setVisible(false);
         }

     }*/
    public void setTitle(String title) {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

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
               final View imageMenu= actionBar.getCustomView().findViewById(R.id.imageMenu_view);
                //ImageView imageMenu=(ImageView) actionBar.getCustomView().findViewById(R.id.imageMenu);
                // imageMenu.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // imageMenu.setEnabled(false);
                     //   set_main_menu(v.getContext());
                      //  imageMenu.setEnabled(true);
                    }
                });
                imageMenu.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        int action = event.getAction();
                        if (action == MotionEvent.ACTION_DOWN){
                           // imageMenu.setEnabled(false);
                           // set_main_menu(v.getContext());
                          //  imageMenu.setEnabled(true);
                        }else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL){
                          //  imageMenu.setEnabled(false);
                            if (menu_displayed==false) {
                                menu_displayed=true;
                                set_main_menu(v.getContext());
                                imageMenu.setEnabled(true);
                            }
                        }

                        return true;
                    }
                });

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

                textviewTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      //  onBackPressed();
                    }
                });

            } catch (Exception ex) {
            }
        }
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

    void call_finish() {
        try {

            Handler handler3 = new Handler(getMainLooper());
            handler3.post(new Runnable() {
                @Override
                public void run() {
                    delay(1);
                }
            });

        } catch (Exception e) {
        }


        finish();
    }

    void delay(int second) {
        final Context context = getApplicationContext();
        final long min = (second * 360000);
        for (long i = 0; i < min; i++)
            i = i;
        Handler handler = new Handler(context.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                //  for(long i=0; i<min; i++)
                //  i=i;
            }
        });
    }

    void set_time() {
        try {

            try {

              //  settings.edit().putBoolean("Update", true).commit();
            } catch (Exception ex) {
            }
/*
            String strTime[];
            String strTime2[];
            strTime = duration.split(":");
            strTime2 = start_time.split(":");


            String stime[] = start_time.split(":");
            int m = Integer.parseInt(strTime2[1]);
            int h = Integer.parseInt(strTime2[0]);

            int hour = Integer.parseInt(strTime[0]);
            minute = Integer.parseInt(strTime[1]);

            minute = ((minute + m) % 60);
            hour = h + hour + ((minute + m) / 60);
            duration = hour + ":" + minute;
            strTime = duration.split(":");*/

            // start_time="";
            //duration="";


            TimePickerDialog tdp_dur = new TimePickerDialog(Shift_Detail.this, R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    duration =set_leading_zero(hourOfDay, 2) + ":" + set_leading_zero(minute, 2);

                    // dtpStartTime.setText(set_leading_zero(hourOfDay, 2) + ":" + set_leading_zero(minute, 2));

                   time_set_done = false;
                   try {
                       Set_Job_Time();
                       //new MyAsyncClass_Set_Time().execute();
                   }catch(Exception ex){}

                }
            }, 12, 00, false);


            //TimePickerDialog tdp = new TimePickerDialog(Roster_Availability_Main.this, td, 12, 00, true);

            tdp_dur.setTitle("Set End Time of Shift");
            tdp_dur.setCancelable(true);
            tdp_dur.setCanceledOnTouchOutside(false);
            tdp_dur.setIcon(R.drawable.ic_time_24dp);

            tdp_dur.getActionBar();
            tdp_dur.show();

            TimePickerDialog tdp = new TimePickerDialog(Shift_Detail.this, R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    // start_time + "," + duration
                    start_time =set_leading_zero(hourOfDay, 2) + ":" + set_leading_zero(minute, 2);

                    // dtpStartTime.setText(set_leading_zero(hourOfDay, 2) + ":" + set_leading_zero(minute, 2));
                    //
                }
            }, 12, 00, false);


            //TimePickerDialog tdp = new TimePickerDialog(Roster_Availability_Main.this, td, 12, 00, true);

            tdp.setTitle("Set Start Time of Shift");
            tdp.setCancelable(true);
            tdp.setCanceledOnTouchOutside(false);
            tdp.setIcon(R.drawable.ic_time_24dp);

            tdp.getActionBar();
            tdp.show();
/*


            TimePickerDialog dur = new TimePickerDialog(Shift_Detail.this, d, Integer.parseInt(strTime[0]), Integer.parseInt(strTime[1]), true);
            dur.setTitle("Set End Time of Activity");
            dur.setCancelable(true);
            dur.setIcon(R.drawable.duration);
            dur.getActionBar();
            dur.show();

            TimePickerDialog t1 = new TimePickerDialog(Shift_Detail.this, t, Integer.parseInt(strTime2[0]), Integer.parseInt(strTime2[1]), true);
            t1.setTitle("Set Start Time of Activity");
            t1.setCancelable(true);
            t1.setIcon(R.drawable.start_job2);
            t1.show();
*/

        } catch (Exception ex) {
            ((TextView) findViewById(R.id.txtAcknowledge)).setText("Operation not done due to some server error\n" + ex.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shift__detail__view, menu);
        //menu.clear();
        return false;
//


    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            menu.clear();
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            case R.id.btnClinetNote2:
                try {
                    if (!(this.Actual_Client_Code.equalsIgnoreCase("!MULTIPLE") || this.Actual_Client_Code.equalsIgnoreCase("!INTERNAL")))
                        Set_Client_Note(txtAcknowledge);
                    else
                        Tost_Message("Permission denied to add notes for group shift");

                } catch (Exception ex) {
                }
                break;
            case R.id.btnTravel2:
                try {
                    Call_Travel(txtAcknowledge);
                } catch (Exception ex) {
                }
                break;
            case R.id.btnPic2:
                try {
                    call_pic_upload(txtAcknowledge);
                } catch (Exception ex) {
                }
                break;

            case R.id.btnRosterNote2:
                try {
                    if (!(this.Actual_Client_Code.equalsIgnoreCase("!MULTIPLE") || this.Actual_Client_Code.equalsIgnoreCase("!INTERNAL")))
                        Add_roster_Note(txtAcknowledge.getContext());
                    else
                        Tost_Message("Permission denied to add notes for group shift");

                } catch (Exception ex) {
                }
                break;

            case R.id.btnSignature2:
                try {
                    Register_Signature(txtAcknowledge.getContext());
                } catch (Exception ex) {
                }
                break;
            case R.id.btnIncident2:
                try {

                    settings.edit().putBoolean("Update", true).commit();
                } catch (Exception ex) {
                }
                txtAcknowledge = findViewById(R.id.txtAcknowledge);
                Set_Incident(txtAcknowledge.getContext());

                break;
            case R.id.btnLeave2:
                try {
                    //Set_Leave(txtAcknowledge.getContext());
                } catch (Exception ex) {
                }
                break;
            case R.id.btnPlans2:
                try {
                    Load_Plan();
                } catch (Exception ex) {
                }
                break;
            case R.id.btnDcoument2:
                try {
                    View_Documents();
                } catch (Exception ex) {
                }
                break;


            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        if (1 == 1) return false;
        int id = item.getItemId();
        if (Actual_Client_Code.equalsIgnoreCase("!MULTIPLE") || Actual_Client_Code.equalsIgnoreCase("!INTERNAL")) {

            //return true;
        }

        if (id == R.id.nav_Picture) {
            try {
                if (!user_settings.getAllowPicUpload().equalsIgnoreCase("true"))
                    Tost_Message( "Permission denied");
                else
                    call_pic_upload(txtAcknowledge);
            } catch (Exception ex) {
            }
        } else if (id == R.id.nav_Travel) {
            try {
                if (user_settings.getAllowTravelEntry().equalsIgnoreCase("true")) {
                    if (!View_only || (View_only && user_settings.get_RestrictTravelSameDay().equalsIgnoreCase("false"))) {
                        Call_Travel(txtAcknowledge);
                    } else
                        Tost_Message( "Permission denied");
                } else
                    Tost_Message( "Permission denied");
            } catch (Exception ex) {
            }
        } else if (id == R.id.nav_Client_Note) {
            try {
                if ((user_settings.getAllowOPNote().equalsIgnoreCase("true") || user_settings.getAllowCaseNote().equalsIgnoreCase("true"))
                        && !(Actual_Client_Code.equalsIgnoreCase("!MULTIPLE") || Actual_Client_Code.equalsIgnoreCase("!INTERNAL"))) {
                    Set_Client_Note(txtAcknowledge);
                } else
                    //if(!user_settings.getAllowClientNoteEntry().equalsIgnoreCase("true"))
                  Tost_Message( "Permission denied");


            } catch (Exception ex) {
            }
        } else if (id == R.id.nav_Roster_Note) {
            try {
                if (!user_settings.getAllowRosterNoteEntry().equalsIgnoreCase("true"))
                  Tost_Message( "Permission denied");
                else
                    Add_roster_Note(txtAcknowledge.getContext());
            } catch (Exception ex) {
            }
        } else if (id == R.id.nav_Incident) {
            txtAcknowledge = findViewById(R.id.txtAcknowledge);
            if (!user_settings.getAllowIncidentEntry().equalsIgnoreCase("true"))
              Tost_Message( "Permission denied");
            else
                Set_Incident(txtAcknowledge.getContext());

        } else if (id == R.id.nav_Sign) {

            try {
                if (!user_settings.getAllowRegisterSign().equalsIgnoreCase("true"))
                  Tost_Message( "Permission denied");
                else
                    Register_Signature(txtAcknowledge.getContext());
            } catch (Exception ex) {
            }

        } else if (!View_only && id == R.id.nav_Start) {

            btn_start_job = (Button) findViewById(R.id.btnstartjob);
            if (btn_start_job.isEnabled()) {
                if (btn_start_job.getText().toString().startsWith("START")) {
                    ShowDialog_for_Job_Start(btn_start_job, "Are you sure you want to Start Job");
                } else if (user_settings.getHIdeCancelButton().equalsIgnoreCase("False")) {
                    ShowDialog_for_Job_Start(btn_start_job, "Are you sure you want to cancel Start Job");
                }
            }
        } else if (!View_only && id == R.id.nav_End) {

            btn_End_job = (Button) findViewById(R.id.btnstartjob);

            if (btn_End_job.isEnabled() && btn_End_job.getVisibility() == View.VISIBLE) {
                if (btn_End_job.getText().toString().startsWith("END")) {

                    //ShowDialog_for_End_Job(txtAcknowledge, "Are you sure you want to End Job\nEnsure you add km travel if needed \n Ensure you added shift report");
                    ShowDialog_for_End_Job(txtAcknowledge, " \n Do you need to add Travel Claim \n \n Ensure you have added a Client Note \n \n Are you sure you want to End Job");
                } else {
                    // ShowDialog_for_End_Job(txtAcknowledge, "Are you sure you want to cancel End Job");
                }
            }

        } else if (!View_only && id == R.id.nav_Set_Time) {
            Button btnsetTime = (Button) findViewById(R.id.btnsetTime);
            btn_End_job = (Button) findViewById(R.id.btnstartjob);
            if (btnsetTime.isEnabled()) {
                if (!user_settings.getAllowSetTime().equalsIgnoreCase("true")
                        || btn_End_job.getText().toString().contains("Cancel")
                        || btn_start_job.getText().toString().contains("Cancel"))
                  Tost_Message( "Permission denied");
                else
                    ShowDialog_for_Set_Job(btnsetTime, "Are You Sure You want to set Job Timing");
            }
        } else if (id == R.id.nav_Plans) {
            if (!user_settings.getAllowViewGoalPlans().equalsIgnoreCase("true"))
              Tost_Message( "Permission denied");
            else {
                try {
                    Load_Plan();
                } catch (Exception ex) {
                }
            }
        } else if (id == R.id.nav_document) {
            if (user_settings.get_ViewClientCareplans().equalsIgnoreCase("true") || user_settings.get_ViewClientDocuments().equalsIgnoreCase("true")
                    || user_settings.get_ViewClientCareplans().equalsIgnoreCase("1") || user_settings.get_ViewClientDocuments().equalsIgnoreCase("1")) {
                try {
                    View_Documents();
                } catch (Exception ex) {
                }

            } else {
              Tost_Message( "Permission denied");
            }


        }


        return true;


    }

    public void Load_Plan() {
        try {
            //Intent i= new Intent(getApplicationContext(),PlanGoalStrat.class);
            Intent i = new Intent(getApplicationContext(), Goal_Plan_Tree.class);
            Bundle b = new Bundle();

            b.putString("root", root);
            b.putString("Recipient", Actual_Client_Code);
            b.putString("Security_Token", Security_Token);
            b.putString("OperatorId", OperatorId);
            b.putString("PersonId", Personid);

            i.putExtras(b);
            startActivity(i);

        } catch (Exception ex) {
        }

    }

    public void View_Care_Plan_Note() {
        try {
            Intent i = new Intent(getApplicationContext(), Care_Plane_Notes.class);
            Bundle b = new Bundle();

            b.putString("root", root);
            b.putString("RecordNo", RecordNo);
            b.putString("AccountNo", AccountNo);
            b.putString("Roster_Date", RosterDate);

            b.putString("Recipient", Actual_Client_Code);
            b.putString("Security_Token", Security_Token);
            b.putString("OperatorId", OperatorId);
            b.putString("PersonId", Personid);
            b.putBoolean("Server_Available", Server_Available);

            i.putExtras(b);
            startActivity(i);

        } catch (Exception ex) {
        }

    }

    public void View_Documents() {
        try {
            Intent i = new Intent(getApplicationContext(), Client_Documents.class);
            Bundle b = new Bundle();

            b.putString("root", root);
            b.putString("Recipient", Actual_Client_Code);
            b.putString("Security_Token", Security_Token);
            b.putString("OperatorId", OperatorId);
            b.putString("PersonId", Personid);
            b.putBoolean("Server_Available", Server_Available);

            i.putExtras(b);
            startActivity(i);

        } catch (Exception ex) {
        }

    }

    public void Process_SleepOver2() {
        try {


            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getApplicationContext());

            Date date = new Date();

            String strDate_current = dateFormat.format(date);
            String strTime = timeFormat.format(date) ;
           // strDate_current = strDate_current + " " + strTime;

            String strDate = RosterDate + " " + strTime;

            String command = "";

            command = "\nP" + "`" + RecordNo + "`" + strDate;

            set_Updates(command);
            xml.set_Job_Completed_Status(RecordNo);
            ((TextView) findViewById(R.id.txtAcknowledge)).setText("Job completed successfully");
            txtAcknowledge.setText(txtAcknowledge.getText() + " (" + RecordNo + " )");
            Job_end_done = true;
            MainActivity.form_resumed=false;
            btn_start_job.setVisibility(View.GONE);
            btn_time.setVisibility(View.GONE);

            if (xml.check_MultipleShift(RecordNo,"23:59").equalsIgnoreCase("Multiple") && !MinorGroup.equalsIgnoreCase("BREAK")) {
                xml.Process_MultipleShift(RecordNo, AccountNo, RosterDate, StartTime, EndTime,"23:59");

            }


            Roster_Info rost=getRoster(strDate_current,"00:00");
            String NRecordNo=rost.getRecordNo();

            xml.set_Job_Completed_Status(NRecordNo);

            if (xml.check_MultipleShift(NRecordNo,"").equalsIgnoreCase("Multiple") && !MinorGroup.equalsIgnoreCase("BREAK")) {
                xml.Process_MultipleShift(NRecordNo, AccountNo, rost.getRoster_Date(), rost.getStart_Time(), rost.get_End_Time(),"");

            }
        } catch (Exception ex) {
        }

    }
    Roster_Info getRoster(String rosterDate, String startTime) {
        Roster_Info rst = null;

        try {

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();
            File fileDir = null;
            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = null;

            fXmlFile = new File(fileDir, "traccs.xml");

            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Roster_Info");

                String stime = "";
                String sDate= "";

                for (int tmp = 0; tmp < nList.getLength(); tmp++) {
                    try {

                        Node nNode = nList.item(tmp);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            stime = eElement.getElementsByTagName("Start_Time").item(0).getTextContent();
                            sDate = eElement.getElementsByTagName("Roster_Date").item(0).getTextContent();


                            if (sDate.equals(rosterDate) && stime.equals(startTime)) {
                                rst = new Roster_Info();
                                rst.setRecordNo(eElement.getElementsByTagName("RecordNo").item(0).getTextContent());

                                rst.setRoster_Date(eElement.getElementsByTagName("Roster_Date").item(0).getTextContent());
                                rst.setCarer_code(eElement.getElementsByTagName("Carer_code").item(0).getTextContent());
                                rst.setServiceType(eElement.getElementsByTagName("ServiceType").item(0).getTextContent());

                                if (eElement.getElementsByTagName("Service_Detail").item(0).getTextContent() != null)
                                    rst.setService_Detail(eElement.getElementsByTagName("Service_Detail").item(0).getTextContent());
                                else
                                    rst.setService_Detail("-");


                                rst.setStart_Time(eElement.getElementsByTagName("Start_Time").item(0).getTextContent());
                                rst.setDuration(eElement.getElementsByTagName("Duration").item(0).getTextContent());

                                rst.setClient_code(eElement.getElementsByTagName("Client_code").item(0).getTextContent());
                                rst.setProgram(eElement.getElementsByTagName("Program").item(0).getTextContent());

                                rst.setDayNo(Integer.parseInt(eElement.getElementsByTagName("DayNo").item(0).getTextContent()));
                                rst.setMonthNo(Integer.parseInt(eElement.getElementsByTagName("Monthno").item(0).getTextContent()));
                                rst.setYearNo(Integer.parseInt(eElement.getElementsByTagName("YearNo").item(0).getTextContent()));
                                rst.setBlockNo(Integer.parseInt(eElement.getElementsByTagName("blockNo").item(0).getTextContent()));

                                if (eElement.getElementsByTagName("Notes").item(0).getTextContent() != null) {
                                    rst.setNotes(eElement.getElementsByTagName("Notes").item(0).getTextContent());
                                } else {
                                    rst.setNotes("-");
                                }

                                rst.setRoster_type(eElement.getElementsByTagName("Roster_Type").item(0).getTextContent());
                                rst.setStarted(eElement.getElementsByTagName("Started").item(0).getTextContent());

                                if (eElement.getElementsByTagName("Completed").item(0).getTextContent() != null) {
                                    rst.setCompleted(eElement.getElementsByTagName("Completed").item(0).getTextContent());
                                } else {
                                    rst.setCompleted("0");
                                }

                                rst.setActual_Client_Code(eElement.getElementsByTagName("Actual_Client_Code").item(0).getTextContent());

                                try {
                                    rst.setServiceSetting(eElement.getElementsByTagName("servicesetting").item(0).getTextContent());

                                } catch (Exception ex) {
                                }

                                try {
                                    rst.setTA_LOGINMODE(eElement.getElementsByTagName("TA_LOGINMODE").item(0).getTextContent());
                                    rst.setTA_EXCLUDEGEOLOCATION(eElement.getElementsByTagName("TA_EXCLUDEGEOLOCATION").item(0).getTextContent());
                                    rst.setGroup_Alerts(eElement.getElementsByTagName("Group_Alerts").item(0).getTextContent());

                                    rst.setKM(eElement.getElementsByTagName("KM").item(0).getTextContent());
                                } catch (Exception ex) {
                                }

                                try {
                                    rst.setInfoOnly(eElement.getElementsByTagName("InfoOnly").item(0).getTextContent());
                                } catch (Exception ex) {
                                }

                                try {
                                    rst.setAddress(eElement.getElementsByTagName("Address").item(0).getTextContent());
                                } catch (Exception ex) {
                                }

                                try {
                                    rst.setDebtor(eElement.getElementsByTagName("Debtor").item(0).getTextContent());
                                } catch (Exception ex) {
                                }

                                try {
                                    rst.setFee(eElement.getElementsByTagName("Fee").item(0).getTextContent());
                                } catch (Exception ex) {
                                }

                                try {
                                    rst.setDisplayFeeInApp(eElement.getElementsByTagName("DisplayFeeInApp").item(0).getTextContent());
                                } catch (Exception ex) {
                                }

                                try {
                                    rst.setDisplayDebtorInApp(eElement.getElementsByTagName("DisplayDebtorInApp").item(0).getTextContent());
                                } catch (Exception ex) {
                                }


                                try {

                                    rst.set_ACCOUNTINGIDENTIFIER(eElement.getElementsByTagName("ACCOUNTINGIDENTIFIER").item(0).getTextContent());
                                } catch (Exception ex) {
                                }

                                try {

                                    rst.setMinorGroup(eElement.getElementsByTagName("MinorGroup").item(0).getTextContent());
                                } catch (Exception ex) {
                                }

                                try {

                                    rst.setMTAServiceType(eElement.getElementsByTagName("MTAServiceType").item(0).getTextContent());
                                } catch (Exception ex) {
                                }

                                try {

                                    rst.setMyOwnCarStatus(eElement.getElementsByTagName("MyOwnCarStatus").item(0).getTextContent());
                                } catch (Exception ex) {
                                }

                                try {

                                    rst.setDisable_Shift_Start_Alarm(eElement.getElementsByTagName("Disable_Shift_Start_Alarm").item(0).getTextContent());
                                } catch (Exception ex) {
                                }

                                try {

                                    rst.setDisable_Shift_End_Alarm(eElement.getElementsByTagName("Disable_Shift_End_Alarm").item(0).getTextContent());
                                } catch (Exception ex) {
                                }

                                try {

                                    rst.setTA_MultiShift(eElement.getElementsByTagName("TA_MultiShift").item(0).getTextContent());
                                } catch (Exception ex) {
                                }

                                try {
                                    if (eElement.getElementsByTagName("Group_Recipients") == null)
                                        continue;
                                    List<Group_Recipient> lst = new ArrayList<Group_Recipient>();
                                    Group_Recipient rg = null;
                                    NodeList nlst = eElement.getElementsByTagName("Group_Recipient");
                                    for (int ind = 0; ind < nlst.getLength(); ind++) {
                                        Node nNod = nlst.item(ind);
                                        Element eElm = (Element) nNod;
                                        rg = new Group_Recipient();

                                        String val = "";
                                        val = eElm.getElementsByTagName("Name").item(0).getTextContent();
                                        rg.setName(val);
                                        val = eElm.getElementsByTagName("AccountNo").item(0).getTextContent();
                                        rg.setAccountNo(val);
                                        val = eElm.getElementsByTagName("RecordNo").item(0).getTextContent();
                                        rg.setRecordNo(val);
                                        val = eElm.getElementsByTagName("PickUpAddress1").item(0).getTextContent();
                                        rg.setPickUpAddress1(val);
                                        try {
                                            val = eElm.getElementsByTagName("Status").item(0).getTextContent();
                                            rg.setStatus(val);
                                        }catch(Exception ex){ rg.setStatus("1");}
                                        lst.add(rg);
                                        val = eElm.getElementsByTagName("Coordinator_Email").item(0).getTextContent();
                                        rg.setCoordinator_Email(val);

                                        val = eElm.getElementsByTagName("RECIPIENT_CoOrdinator").item(0).getTextContent();
                                        rg.setRECIPIENT_CoOrdinator(val);

                                        try {
                                            val = eElm.getElementsByTagName("Personid").item(0).getTextContent();
                                            rg.set_Personid(val);
                                        }catch(Exception ex){ rg.set_Personid("0");}

                                        lst.add(rg);

                                    }
                                    rst.set_group_Recipients(lst);

                                } catch (Exception ex) {
                                }

                                break;
                            }
                        }
                    } catch (Exception exF) {
                    }
                }
            }

        } catch (Exception ex) {
        }

        return rst;
    }
    public void Process_SleepOver() {

        if (!Server_Available) {
            Process_SleepOver2();
            return;
        }

        // Button  tvaddress2=(Button)findViewById(R.id.txtAddress);

        String URL6 = root + "/Timesheet.asmx?op=Process_SleepOver_Jobs";
        String SOAP_ACTION6 = "https://tempuri.org/Process_SleepOver_Jobs";
        String METHOD_NAME6 = "Process_SleepOver_Jobs";

        try {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME6);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug = true;

            PropertyInfo pi = new PropertyInfo();
            pi.setName("Recordno");
            pi.setValue(getSecurityToken() + RecordNo);
            request.addProperty(pi);

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("cancel");
            pi2.setValue(false);
            request.addProperty(pi2);

            String value = "12";
            try {

                //disabled this code because it was not working
               /*  value = Settings.System.getString(getApplicationContext().getContentResolver(),
                        Settings.System.TIME_12_24);
*/
                // boolean is24hour = DateFormat.is24HourFormat(activityContext);
            } catch (Exception ex) {
            }

            String am_pm = "";
            Calendar datetime = Calendar.getInstance();

            if (Integer.parseInt(value) == 24) {

                if (datetime.getTime().getHours() < 13)
                    am_pm = " AM";
                else
                    am_pm = " PM";

            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getApplicationContext());

            Date date = new Date();

            String strDate_current = dateFormat.format(date);
            String strTime = timeFormat.format(date) + am_pm;
            strDate_current = strDate_current + " " + strTime;

            String strDate = RosterDate + " " + strTime;

            // String strDate=dateFormat.format(RosterDate + " 11:59") + am_pm;
            // String strDate= RosterDate + " 11:59 PM" ;
            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("timeStamp");
            pi3.setValue(strDate);
            request.addProperty(pi3);

            if (Latitude == null) Latitude = "0";

            PropertyInfo pi4 = new PropertyInfo();
            pi4.setName("Latitude");
            pi4.setValue(Latitude);
            request.addProperty(pi4);

            if (Longitude == null) Longitude = "0";

            PropertyInfo pi5 = new PropertyInfo();
            pi5.setName("Longitude");
            pi5.setValue(Longitude);
            request.addProperty(pi5);
            if (Current_Address == null) Current_Address = "-";
            PropertyInfo pi6 = new PropertyInfo();
            pi6.setName("Location");
            pi6.setValue(Current_Address);
            request.addProperty(pi6);




            // tvaddress2.setText("RecordNo=" + RecordNo + "\n RosterDate=" + strDate + "\nLatitude=" + Latitude + " Longitude="+ Longitude + "\n" + Current_Address);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            androidHttpTransport.call(SOAP_ACTION6, envelope);

            SoapPrimitive result = (SoapPrimitive) envelope.getResponse();

            try {
                if (result.toString().equalsIgnoreCase("true")) {
                    settings.edit().putBoolean("Process_SleepOver", true).commit();
                    ((TextView) findViewById(R.id.txtAcknowledge)).setText("Job completed successfully");
                    txtAcknowledge.setText(txtAcknowledge.getText() + " (" + RecordNo + " )");
                    btn_start_job.setVisibility(View.GONE);
                    btn_time.setVisibility(View.GONE);
                    MainActivity.form_resumed=false;
                    MainActivity.enforce_refresh=true;
                    try {
                       // new MyAsyncClass5().execute();
                    } catch (Exception ex) {
                    }
                }
            } catch (Exception ex) {
            }


            //finish();

        } catch (Exception e) {
             //tvaddress2.setText(e.toString());

        }


    }

    void call_pic_upload(View v) {
        try {

            Intent intent2 = new Intent(v.getContext(), Upload_Photo.class);

            Bundle bundle = new Bundle();

            bundle.putString("root", root);
            bundle.putString("Personid", Personid);
            bundle.putString("RecipientDocFolder", RecipientDocFolder);
            bundle.putString("AccountNo", AccountNo);
            bundle.putString("RecordNo", RecordNo);
            bundle.putString("UserId", UserId);
            bundle.putString("OperatorId", OperatorId);
            bundle.putString("Security_Token", Security_Token);

            if (Server_Available == true)
                bundle.putString("Server", "True");
            else
                bundle.putString("Server", "False");

            bundle.putString("StaffCode", StaffCode);
            bundle.putString("Cordinator_Email", Cordinator_Email);


            //  bundle.putString("Timesheet",TimesheetReights);
            intent2.putExtras(bundle);
            // startActivityForResult(intent2);
            startActivityForResult(intent2, STATIC_INTEGER_VALUE2);

        } catch (Exception ex) {
          //  lblTime.setText("Operation not done due to some server error\n" + ex.toString());
        }
    }

    void Call_Travel(View v) {
        try {

            Intent intent2 = new Intent(v.getContext(), Travel.class);

            Bundle bundle = new Bundle();
            bundle.putString("root", root);
            bundle.putString("Personid", Personid);
            bundle.putString("recordNo", RecordNo);
            bundle.putString("AccountNo", Actual_Client_Code);
            bundle.putString("RosterDate", RosterDate);
            bundle.putString("EndTime", EndTime);
            bundle.putString("StaffCode", StaffCode);

            if (Server_Available == true)
                bundle.putString("Server", "True");
            else
                bundle.putString("Server", "False");
            //  bundle.putString("Timesheet",TimesheetReights);

            bundle.putString("OperatorId", OperatorId);
            bundle.putString("Security_Token", Security_Token);

            bundle.putString("TA_TRAVELDEFAULT", TA_TRAVELDEFAULT);
            bundle.putString("MyOwnCarStatus", MyOwnCarStatus);
            bundle.putString("RestrictTravelSameDay", user_settings.get_RestrictTravelSameDay());

            intent2.putExtras(bundle);
            startActivity(intent2);
        } catch (Exception ex) {
          //  lblTime.setText("Operation not done due to some server error\n" + ex.toString());
        }
    }
/*
    TextView    mTextStatus = (TextView) findViewById(R.id.txtRosterAlert);
    ScrollView   mScrollView = (ScrollView) findViewById(R.id.scrollView1);

    private void scrollToBottom()
    {
        mScrollView.post(new Runnable()
        {
            public void run()
            {
                mScrollView.smoothScrollTo(0, mTextStatus.getBottom());
            }
        });
    }
*/

    @Override
    public void onUserInteraction() {

        super.onUserInteraction();
        MainActivity.idle_time = 0;
        getApp().touch();
        Log.d(TAG2, "User interaction to "+this.toString());
    }

    private static final String TAG2=ControlActivity.class.getName();
    public ControlApplication getApp()
    {
        return (ControlApplication )this.getApplication();
    }

    void Call_Set_Time_Job(View v) {

        btn_start_job.setEnabled(false);
        if (Server_Available) {

            //getLocation(getApplicationContext());
            //   Save_Current_Location();
        }

        String strCaption = btn_start_job.getText().toString().substring(0, 5);
        if (TMMode.equals("1") || TMMode.equals("True") || Boolean.parseBoolean(TMMode) == true) {
            try {
                Button_Pressed = "Set";
                show_QRCodeScan();
            } catch (Exception e) {
                txtAcknowledge.setText(e.toString());
            }
        } else if (TMMode.equals("2")) {
            get_sign_login(v.getContext(), "Set");

        } else if (TMMode.equals("3") && !PinCode.equalsIgnoreCase("0")) {
            get_Authenticate_Pin(v.getContext(), "Set");
        } else {
            set_time();
        }

    }

    void Call_Start_Job(View v) {

        btn_start_job.setEnabled(false);
        btn_time.setEnabled(false);
        // btn_time.setVisibility(View.GONE);

        if (Server_Available) {

            //getLocation(getApplicationContext());
            //   Save_Current_Location();
        }

        String strCaption = btn_start_job.getText().toString().substring(0, 5);
        if (TMMode.equals("1") || TMMode.equals("True") || Boolean.parseBoolean(TMMode) == true) {
            try {
                Button_Pressed = "start";
                show_QRCodeScan();
            } catch (Exception e) {
                txtAcknowledge.setText(e.toString());
            }
        } else if (TMMode.equals("2")) {
            get_sign_login(v.getContext(), "start");
            // Register_Signature(btn_End_job.getContext());
            //Set_Job();

        } else if (TMMode.equals("3") && !PinCode.equalsIgnoreCase("0")) {
            get_Authenticate_Pin(v.getContext(), "start");
        } else {
            Set_Job();
        }

    }

    public void call_End_Job(View v) {
        try {

            // settings.edit().putBoolean("Update", true).commit();
        } catch (Exception ex) {
        }
        btn_End_job = findViewById(R.id.btnstartjob);
        btn_End_job.setEnabled(false);
        btn_End_job.setVisibility(View.GONE);
        if (Server_Available && (Current_Address.equals("") || Current_Address == null)) {
            //getLocation(getApplicationContext());
            //find_Location3(getApplicationContext());


        }

        if (Server_Available && (Current_Address.equals("") || Current_Address == null)) {
            Current_Address = "-";
            Latitude = "0";
            Longitude = "0";
        }


        if (TaskList_checked_status()) {

            ShowDialog_for_Job_End_With_Task_check(v, "ALL TASKS HAVE NOT BEEN COMPLETED - DO YOU WANT TO CONTINUE");

        } else {
            Perform_End_Job(v);
        }
    }

    void Register_Signature(Context c) {
        try {

            if (Server_Available == false) {

                Tost_Message( "The feature of Signature Authentication does not work in offline mode");
                return;
            }
             Intent intent = new Intent(Shift_Detail.this, CaptureSignature_Recipient.class);
            Bundle b = new Bundle();

            b.putString("AccountNo", AccountNo);
            b.putString("root", root);
            b.putString("OperatorId", OperatorId);
            b.putString("Security_Token", Security_Token);
            intent.putExtras(b);

            //CaptureActivity ca= new CaptureActivity();
            startActivity(intent);


        } catch (Exception ex) {
            Tost_Message(  ex.toString());
        }
    }

    final void Perform_End_Job(View v) {

        // String strCaption = btn_start_job.getText().toString().substring(0, 5);
        btn_End_job = (Button) findViewById(R.id.btnstartjob);
        if (TMMode.equals("1") || TMMode.equals("True") || Boolean.parseBoolean(TMMode) == true) {
            try {
                show_QRCodeScan();
                // txtAcknowledge.setText(barCode);
                Button_Pressed = "end";
            } catch (Exception e) {
                txtAcknowledge.setText(e.toString());
            }
        } else if (TMMode.equals("2")) {
            get_sign_login(btn_End_job.getContext(), "end");
            // Register_Signature(btn_End_job.getContext());
            //End_Job();
        } else if (TMMode.equals("3") && !PinCode.equalsIgnoreCase("0")) {
            get_Authenticate_Pin(btn_End_job.getContext(), "end");
        } else {
            End_Job();
        }
    }

    void show_QRCodeScan() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (shouldAskPermissions()) {
                askPermissions();
            }
            //return;
        }
        try {


            Intent intent = new Intent(this, SimpleScannerActivity.class);
            // Intent intent = new Intent(this, QRCode.class);
            //CaptureActivity ca= new CaptureActivity();
            startActivityForResult(intent, STATIC_INTEGER_VALUE);


        } catch (Exception ex) {
            Tost_Message(ex.toString());
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // txtAcknowledge=  ((TextView) findViewById(R.id.txtAcknowledge));
        //String newText2 = data.getStringExtra("barcode");
        //txtAcknowledge.setText(newText2);
       // Tost_Message(barCode + "\n " + Personid);
        switch (requestCode) {
            case (STATIC_INTEGER_VALUE): {
                if (resultCode == Activity.RESULT_OK) {
                    barCode = data.getStringExtra("barcode");
                    barCode = barCode.toUpperCase();
                    barCode.replace("HTTP://", "");

                    if (Actual_Client_Code.equalsIgnoreCase("!MULTIPLE") || Actual_Client_Code.equalsIgnoreCase("!INTERNAL")){
                        if (barCode.equals(Service_Setting))

                            if (Button_Pressed.equals("Set")) {
                                set_time();
                            } else if (Button_Pressed.equals("start")) {
                                Set_Job();
                            } else {
                                End_Job();
                            }
                        else {
                            if (Service_Setting.equalsIgnoreCase("-"))
                                ShowDialog("QR code not found in local database, please refresh data ");
                            else
                                ShowDialog("Incorrect/Invalid QR Code " + barCode + "\n (" + Service_Setting + ")");
                            //txtAcknowledge.setText("Invalid QR Code " + barCode);
                            // TODO Update your TextView.
                        }
                    }else {
                        if (barCode.equals(Personid))

                            if (Button_Pressed.equals("Set")) {
                                set_time();
                            } else if (Button_Pressed.equals("start")) {
                                Set_Job();
                            } else {
                                End_Job();
                            }
                        else {
                            if (Personid.equalsIgnoreCase("0"))
                                ShowDialog("QR code not found in local database, please refresh data ");
                            else
                                ShowDialog("Incorrect/Invalid QR Code " + barCode + "\n (" + Personid + ")");
                            //txtAcknowledge.setText("Invalid QR Code " + barCode);
                            // TODO Update your TextView.
                        }
                    }

                }
                break;
            }
            case STATIC_INTEGER_VALUE2: {
                //  Photo_Alert();
                break;
            }
        }
    }

    void Photo_Alert() {
        try {
            String messgas = "Picture of client \"" + AccountNo + " is updated \" by \"" + StaffCode + "\" :\n\n";

            String title = "Client Picture Updated by " + StaffCode + "\n\n";
            send_email_alert(messgas, title);
        } catch (Exception ex) {
        }
    }

    String getSecurityToken() {

        String Val = OperatorId + "$" + Security_Token + "$";
        return Val;
    }

    String get_Lant_Long(String Adress) {
        String result = "";
        String API_Key = user_settings.get_GoogleAPI_Key();
        String url = "https://maps.google.com/maps/api/geocode/json?key=" + API_Key + "&sensor=true&address=" + Adress.replace(" ", "%20");
        // String url= Uri.parse(link).toString() ;// String.format(Locale.ENGLISH, link);

        // Tost_Message( Uri.parse(link).toString());

   /*       String tag[] = {"text"};
          JSONObject jsonObj = null;
        HttpResponse response = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            response = httpClient.execute(httpPost, localContext);

            try {
                String resp_body = EntityUtils.toString(response.getEntity());
                // Log.v("resp_body", resp_body.toString());
                jsonObj = new JSONObject(resp_body);

                //  txtAddress.setText("jsonObj\n"+ jsonObj.toString());

            } catch (Exception ex) {

            }
            try {


                JSONArray obj1 = (JSONArray) jsonObj.getJSONArray("results");
                // txtAddress.setText("obj1\n"+ obj1.toString());

                JSONObject obj2 = (JSONObject) obj1.getJSONObject(0);
                // txtAddress.setText("obj2\n"+ obj2.toString());

                JSONObject obj3 = (JSONObject) obj2.getJSONObject("geometry");
                 JSONObject obj4  =(JSONObject)obj3.getJSONObject("location");

                //for (int i = 0; i < obj4.length(); i++)
                    result = obj4.getString("lat").toString();
                result = result + ", " + obj4.getString("lng").toString();


            } catch (Exception e) {

            }

        } catch (Exception e) {

        }
*/

        return result;
    }

    void show_Map() {
        try {
            if (!Server_Available) return;
            if (Actual_Client_Code.equalsIgnoreCase("!MULTIPLE") || Actual_Client_Code.equalsIgnoreCase("!INTERNAL")) {
                return;
            }
            String lat_long = "";
            String lat_long_current = "";

            String API_Key = "7fstSK1HMsg37X5xV6rRKsDzbj4=";// user_settings.get_GoogleAPI_Key();

            lat_long_current = loc.Longitude + "," + loc.Latitude;
            lat_long = "";// get_Lant_Long(Simple_Address);


            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + lat_long_current + "&daddr=" + lat_long));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            //startActivity(intent);

            //String  link="https://maps.googleapis.com/maps/api/directions/json?origin=Your Location&destination=" + Simple_Address + "&key="+API_Key;
//          String  link="https://www.google.com/maps/dir/Your Location/" + Simple_Address.replace(" ", "%20")+"";
//
            //String  link="https://www.google.com/maps/dir/Your Location/" + Simple_Address + "/@" + lat_long + "&key="+API_Key;

            String link = "http://maps.google.com/maps?f=q&hl=en&geocode=&time=&date=&ttype=&saddr=Your Location&daddr=" + Simple_Address;
            String uri = String.format(Locale.ENGLISH, link);
            Intent intent2 = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse(uri));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

            startActivity(intent2);

            if (true) return;
/*

            Intent intent2 = new Intent(getApplicationContext(), Web_View.class);
            Bundle bundle = new Bundle();
            bundle.putString("URL","http://maps.googleapis.com/maps/api/directions/json?origin=Lahore&destination=Multan&sensor=true");
            bundle.putString("Current_Address",Current_Address);
            bundle.putString("Dest_Address",Simple_Address);
            intent2.putExtras(bundle);
            startActivity(intent2);
*/


            if (Simple_Address.equalsIgnoreCase("") || Simple_Address.equalsIgnoreCase("-"))
                Simple_Address = address;

            Save_Current_Location();
            //  ((TextView) findViewById(R.id.txtAcknowledge)).setText(address);
            //Intent intent3 = new Intent(getApplicationContext(), MapsActivity.class);
            Intent intent3 = new Intent(getApplicationContext(), MapsActivity.class);
            Bundle bundle2 = new Bundle();
            bundle2.putString("Current_Address", Current_Address);
            bundle2.putString("Dest_Address", Simple_Address);
            intent3.putExtras(bundle2);
            startActivity(intent3);
        } catch (Exception ex) {
            // ((TextView) findViewById(R.id.txtAcknowledge)).setText("Operation not done due to some server error\n" + ex.toString());
        }
    }

    void set_server_Ip(String str_root) {
        URL = str_root + "/TimeSheet.asmx?op=StartJob";
        URL2 = str_root + "/TimeSheet.asmx?op=EndJob";
        URL3 = str_root + "/TimeSheet.asmx?op=AcceptTimes";
        URL4 = str_root + "/TimeSheet.asmx?op=Add_client_Note";
        // URL5 = str_root + "/TimeSheet.asmx?op=getTaskList";
        URL6 = root + "/CaseManagement.asmx?op=GetDomain_with_Criteria";
    }


    public void Set_Job() {
        try {

            //  settings.edit().putBoolean("Update", true).commit();
        } catch (Exception ex) {
        }
        long recNo = 0;
        btn_start_job = (Button) findViewById(R.id.btnstartjob);


        try {

            //   recNo = Job_already_started2(RecordNo);
            recNo = Long.parseLong(settings.getString("Started_Job_No", "0"));

        } catch (Exception e) {
        }

        if (recNo > 0 && recNo != Integer.parseInt(RecordNo) && (!MinorGroup.equalsIgnoreCase("BREAK"))) {
            //((TextView) findViewById(R.id.txtAcknowledge)).setText("Job already has been completed");
            ((TextView) findViewById(R.id.txtAcknowledge)).setText("Another Job already has been started");
            return;
        }


        if (Server_Available == false) {
            ((TextView) findViewById(R.id.txtAcknowledge)).setText("calling Set Job2");
            Set_Job2();
            return;
        }


        try {
            // new MyAsyncClass4().execute();
            //   return;

        } catch (Exception ex) {
        }
        boolean result_status = false;
        try {

            try {
                result_status = Start_Job();

            } catch (Exception ex) {
            }

            if (result_status == true) {

                if (btn_start_job.getText().toString().charAt(0) == 'C') {

                    try {
                        settings.edit().putString("Started_Job_No", RecordNo).commit();
                        Started_Job_No = settings.getString("Started_Job_No", "0");
                        Shift_Status = "Started";
                        //  set_Job_Status_In_Local_file(RecordNo + ",Job Cancelled," + currentDateString );
                    } catch (Exception ex) {
                    }
                    ((TextView) findViewById(R.id.txtAcknowledge)).setText("Job cancelled successfully");
                    txtAcknowledge.setText(txtAcknowledge.getText() + " (" + RecordNo + " )");
                    // btn_End_job.setEnabled(false);
                    //   btn_End_job.setVisibility(View.GONE);
                    xml.Update_Roster_Node(RecordNo, "Started", "0");
                    MainActivity.form_resumed = false;
                } else {

                    try {
                        Shift_Status = "Started";
                        settings.edit().putString("Started_Job_No", RecordNo).commit();
                        Started_Job_No = RecordNo;

                        // set_Job_Status_In_Local_file(RecordNo + ",JOB STARTED," + currentDateString );
                    } catch (Exception ex) {
                    }
                    ((TextView) findViewById(R.id.txtAcknowledge)).setText("Job started successfully");
                    txtAcknowledge.setText(txtAcknowledge.getText() + " (" + RecordNo + " )");
                    btn_start_job.setText("END JOB");
                    btn_start_job.setEnabled(true);
                    //  btn_End_job.setVisibility(View.VISIBLE);
                    xml.Update_Roster_Node(RecordNo, "Started", "1");

                    String curr_time = "";
                    try {

                        curr_time = getCurrentTime();
                    } catch (Exception ex) {
                    }
                    xml.Update_Roster_Node(RecordNo, "Actual_Start", curr_time);
                    //gridView = (ListView) findViewById(R.id.gridViewTask);
                    // gridView.setEnabled(false);
                    if (user_settings.getHIdeCancelButton().equalsIgnoreCase("true")) {
                      //  btn_start_job.setVisibility(View.GONE);
                    }

                    MainActivity.form_resumed = false;
                }
            } else
                ((TextView) findViewById(R.id.txtAcknowledge)).setText("Operation not done due to some server problem " + result_status);

        } catch (Exception ex) {
            ((TextView) findViewById(R.id.txtAcknowledge)).setText("Operation not done due to some server error\n" + ex.toString());
        }
    }

    private boolean Start_Job() {

        boolean status = false;

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug = true;

            PropertyInfo pi = new PropertyInfo();
            pi.setName("Recordno");
            pi.setValue(getSecurityToken() + RecordNo);
            request.addProperty(pi);

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("cancel");
            pi2.setValue(false);
            request.addProperty(pi2);

            /*btn=(Button)findViewById(R.id.btnstartjob);
            if (btn.getText().toString().charAt(0) =='S')
            {
                pi2.setValue(false);
                btn.setText("Cancel\nStart");
                btn.setTag("Cancel\nStart");
            }
            else
            {
                pi2.setValue(true);
                btn.setText("START\nJOB");
                btn.setTag("START\nJOB");
            }*/

   /*
        String value = Settings.System.getString(getApplicationContext().getContentResolver(),
				 Settings.System.TIME_12_24);

		 String am_pm="";
   	 Calendar datetime = Calendar.getInstance();
   	  if  (Integer.parseInt(value)==24 &&  datetime.getTime().getHours()<13){
   	    if (datetime.get(Calendar.AM_PM) == Calendar.AM)
   	        am_pm = " AM";
   	    else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
   	        am_pm = " PM";
   	  }
      //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss" );
   	  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd" );
      java.text.DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getApplicationContext());

       Date date = new Date();
       String strDate=dateFormat.format(date);
       String strTime=timeFormat.format(date)+ am_pm;
       strDate =strDate + " " + strTime;
       */
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            DateFormat tFormat = new SimpleDateFormat("HH:mm");

            Date date = new Date();
            String strDate = dateFormat.format(date);
            String strTime = tFormat.format(date);
            strDate = strDate + " " + strTime;

            strDate = "";
            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("timeStamp");
            pi3.setValue(strDate);
            request.addProperty(pi3);

            PropertyInfo pi4 = new PropertyInfo();
            pi4.setName("Latitude");
            pi4.setValue(Latitude);
            request.addProperty(pi4);

            PropertyInfo pi5 = new PropertyInfo();
            pi5.setName("Longitude");
            pi5.setValue(Longitude);
            request.addProperty(pi5);

            PropertyInfo pi6 = new PropertyInfo();
            pi6.setName("Location");
            pi6.setValue(Current_Address);
            request.addProperty(pi6);



            //  ((TextView) findViewById(R.id.txtAcknowledge)).setText("Location_Address" + Location_Address + ", " + Latitude +", "+ Longitude);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION, envelope);
            //   DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
            // Date date = new Date();
            //   String currentDateString = dateFormat2.format(date);

            //if (btn.getTag().toString().charAt(0) =='S')
            //  return true;


            SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
            if (result == null)
                status = false;
            else
                status = Boolean.parseBoolean(result.toString());
        } catch (Exception ex) {
            status = false;
        }

        return status;
    }

    public void Set_Job2() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            DateFormat tFormat = new SimpleDateFormat("HH:mm");


            Date date = new Date();
            String strDate = dateFormat.format(date);
            String strTime = tFormat.format(date);
            strDate = strDate + " " + strTime;

            btn = (Button) findViewById(R.id.btnstartjob);
            String command = "";
            if (btn.getText().toString().charAt(0) == 'S')
                command = "\nS" + "`" + RecordNo + "`" + strDate;

            else
                command = "\nC" + "`" + RecordNo + "`" + strDate;

            set_Updates(command);

            if (((TextView) findViewById(R.id.txtAcknowledge)).getText().toString().equals("Operation done successfully")) {
                btn = (Button) findViewById(R.id.btnstartjob);

                DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
                String currentDateString = dateFormat2.format(date);
                // if (Boolean.valueOf(result.toString())==true)
                    ((TextView) findViewById(R.id.txtAcknowledge)).setText("Job started successfully");
                    txtAcknowledge.setText(txtAcknowledge.getText() + " (" + RecordNo + " )");

                btn.setText("END JOB");
                   // btn_End_job.setVisibility(View.GONE);
                    xml.Update_Roster_Node(RecordNo, "Started", "1");
                    MainActivity.form_resumed=false;
                btn.setEnabled(true);
                Shift_Status="Started";
                Started_Job_No=RecordNo;
                settings.edit().putString("Started_Job_No",RecordNo).commit();
                    //gridView = (ListView) findViewById(R.id.gridViewTask);
                    // gridView.setEnabled(false);
               /* } else {

                    try {
                        // set_Job_Status_In_Local_file(RecordNo + ",JOB STARTED," + currentDateString );
                    } catch (Exception ex) {
                    }
                    ((TextView) findViewById(R.id.txtAcknowledge)).setText("Job Cancelled successfully");
                    txtAcknowledge.setText(txtAcknowledge.getText() + " (" + RecordNo + " )");
                    //btn_End_job.setEnabled(true);
                   // btn_End_job.setVisibility(View.VISIBLE);
                    xml.Update_Roster_Node(RecordNo, "Started", "0");

                    if (user_settings.getHIdeCancelButton().equalsIgnoreCase("true")) {
                        btn_start_job.setVisibility(View.GONE);
                    }
                }*/
            } else
                ((TextView) findViewById(R.id.txtAcknowledge)).setText("Operation not done due to some system problem");

        } catch (Exception ex) {
            ((TextView) findViewById(R.id.txtAcknowledge)).setText("Operation not done successfully due to some server problem");
        }

    }


    boolean Process_end_Job() {
        boolean status = false;
        try {
            if (Process_Sleep_Over) {
                Process_SleepOver();
                return true;
            }
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL2);
            androidHttpTransport.debug = true;

            PropertyInfo pi = new PropertyInfo();
            pi.setName("Recordno");
            pi.setValue(getSecurityToken() + RecordNo);
            request.addProperty(pi);

            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("cancel");
            pi3.setValue(false);

            request.addProperty(pi3);

            PropertyInfo pi4 = new PropertyInfo();
            pi4.setName("Latitude");
            pi4.setValue(Latitude);
            request.addProperty(pi4);

            PropertyInfo pi5 = new PropertyInfo();
            pi5.setName("Longitude");
            pi5.setValue(Longitude);
            request.addProperty(pi5);

            PropertyInfo pi6 = new PropertyInfo();
            pi6.setName("Location");
            pi6.setValue(Current_Address);
            request.addProperty(pi6);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            DateFormat tFormat = new SimpleDateFormat("HH:mm");


            Date date = new Date();
            String strDate = dateFormat.format(date);
            String strTime = tFormat.format(date);
            strDate = strDate + " " + strTime;

            strDate = "";
            PropertyInfo pi7 = new PropertyInfo();
            pi7.setName("timeStamp");
            pi7.setValue(strDate);
            request.addProperty(pi7);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION2, envelope);

            SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
            if (result == null)
                status = false;
            else {
                status = Boolean.parseBoolean(result.toString());
            }

        } catch (Exception ex) {
            ((TextView) findViewById(R.id.txtAcknowledge)).setText("Operation not done due to some server error ");
            status = false;
        }

        return status;
    }

    public void End_Job2() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            DateFormat tFormat = new SimpleDateFormat("HH:mm");


            Date date = new Date();
            String strDate = dateFormat.format(date);
            String strTime = tFormat.format(date);
            strDate = strDate + " " + strTime;

            String command = "";
            btn = (Button) findViewById(R.id.btnstartjob);

           /* if (btn.getTag().toString().charAt(0) == 'E')

                command = "\n update eziTracker_log set LODateTime = '" + strDate + "', WorkDuration = Convert(Decimal(15, 4), DATEDIFF(n, DateTime, '" + strDate + "'))/60  WHERE JobNo = " + RecordNo;
            else
                command = "\n Update eziTracker_log set LODateTime = Null,  WorkDuration = Null WHERE JobNo = " + RecordNo;
*/

            if (btn.getText().toString().charAt(0) == 'E')
                command = "\nE" + "`" + RecordNo + "`" + strDate;

            else
                command = "\nF" + "`" + RecordNo + "`" + strDate;

            set_Updates(command);

                DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
                String currentDateString = dateFormat2.format(date);

                    ((TextView) findViewById(R.id.txtAcknowledge)).setText("Job completed successfully");
                    txtAcknowledge.setText(txtAcknowledge.getText() + " (" + RecordNo + " )");
                    btn_start_job.setEnabled(false);
                    btn_start_job.setVisibility(View.GONE);
                    btn_time.setVisibility(View.GONE);
                    Job_end_done = true;

                    xml.Update_Roster_Node(RecordNo, "Started", "0");
                    xml.Update_Roster_Node(RecordNo, "Completed", RecordNo);

                    //gridView = (ListView) findViewById(R.id.gridViewTask);
                    //  gridView.setEnabled(false);

                        MainActivity.form_resumed=false;

                 Shift_Status="Completed";
                    if (xml.check_MultipleShift(RecordNo,"").equalsIgnoreCase("Multiple") && !MinorGroup.equalsIgnoreCase("BREAK")) {
                        xml.Process_MultipleShift(RecordNo, AccountNo, RosterDate, StartTime, EndTime,"");

                    }

        } catch (Exception ex) {
            ((TextView) findViewById(R.id.txtAcknowledge)).setText("Operation not done due to some server error \n" + ex.toString());
        }

    }

    public void Set_Job_Time() {
        if (Server_Available == false) {
            Set_Time2();
            return;
        }
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME3);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL3);
            androidHttpTransport.debug = true;

            PropertyInfo pi = new PropertyInfo();
            pi.setName("Recordno");

            pi.setValue(getSecurityToken() + RecordNo);
            request.addProperty(pi);

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("Time_String");
            pi2.setValue(start_time + "," + duration);
            request.addProperty(pi2);
            // start_time="";  duration="";
            time_set_done = true;
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION3, envelope);

            SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
            if (Boolean.valueOf(result.toString()) == true) {

                btn_time.setVisibility(View.GONE);
                btn_start_job.setVisibility(View.GONE);
                ((TextView) findViewById(R.id.txtAcknowledge)).setText("Time setting done successfully");
                try{
                   // new MyAsyncClass_Set_Time().execute();
                    xml.set_Job_Completed_Status(RecordNo);


                }catch(Exception ex){}
            } else
                ((TextView) findViewById(R.id.txtAcknowledge)).setText("Operation not done");

        } catch (Exception ex) {
            ((TextView) findViewById(R.id.txtAcknowledge)).setText("Operation not done due to some server error\n" + ex.toString());
        }

    }

    public void Set_Time2() {
        try {
            String command;
            String Time_String = (start_time + "," + duration);
            command = "\nT" + "`" + RecordNo + "`" + Time_String;
            set_Updates(command);

        } catch (Exception ex) {
            ((TextView) findViewById(R.id.txtAcknowledge)).setText("Operation not done due to some server error\n" + ex.toString());
        }

    }

    public List<String> get_ShiftGoals() {


        File froot = null;
        File fileDir = null;
        List<String> lst_goals = null;

        try {

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();

            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = new File(fileDir, "ShiftGoals.xml");

            lst_goals = new ArrayList<String>();


            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("DataDomain");



                if (nList == null) return null;


                for (int tmp = 0; tmp < nList.getLength(); tmp++) {
                    try {

                        Node nNode = nList.item(tmp);


                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;

                            String val = eElement.getElementsByTagName("Description").item(0).getTextContent();
                            lst_goals.add(val);
                        }
                    } catch (Exception e) {

                    }


                }//loop
            }
        } catch (Exception e) {
        }

        return lst_goals;
    }

    public void Set_Client_Note(View view) {

        try {
            Bundle bundle = new Bundle();

            bundle.putString("RecordNo", RecordNo);
            bundle.putString("Roster_Date", RosterDate);
            bundle.putString("Recipient", AccountNo);
            bundle.putString("AccountNo", Actual_Client_Code);
            bundle.putString("PersonId", Personid);
            bundle.putString("Main_Op_Note", "OPNOTE");
            bundle.putString("Job_Time", StartTime);
            bundle.putString("Enforce_Note", "false");

        /*    bundle.putString("RecordNo",rst.getRecordNo());
            bundle.putString("Roster_Date",rst.getRoster_Date() + " " + rst.getStart_Time() + "-" + rst.get_End_Time());
            bundle.putString("Recipient",rst.getClient_code());
            bundle.putString("AccountNo",rst.getActual_Client_Code());
            bundle.putString("PersonId",rst.getPersonId());
            bundle.putString("Job_Time",rst.getStart_Time() + "-" + rst.get_End_Time());*/


            Intent intent2 = new Intent(Shift_Detail.this, OP_Case_Note_Activity.class);
            intent2.putExtras(bundle);
            Shift_Detail.this.startActivity(intent2);

        } catch (Exception ex) {
            Tost_Message(  ex.toString());
        }


        /*
        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.client_note);
        dialog.setTitle("Add/Change Client Note");
        dialog.setCanceledOnTouchOutside(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;



        TextView txt1 = (TextView) dialog.findViewById(R.id.txt1);
        txt1.setText("Add / Change Client Note for \n" + AccountNo);
        final Spinner spinner1 = (Spinner) dialog.findViewById(R.id.spShiftGoals);
        final TextView txtShiftPurpose = (TextView) dialog.findViewById(R.id.txtShiftPurpose);
        final TextView txtAction = dialog.findViewById(R.id.txtAction);
        final TextView txtOutCome = dialog.findViewById(R.id.txtOutCome);


        final CheckBox chkOPNote = (CheckBox) dialog.findViewById(R.id.chkOPNote);
        final CheckBox chkCaseNote = (CheckBox) dialog.findViewById(R.id.chkCaseNote);
        final CheckBox chkIncidentNote = (CheckBox) dialog.findViewById(R.id.chkIncidentNote);
        final CheckBox chkServiceNote = (CheckBox) dialog.findViewById(R.id.chkServiceNote);
        final CheckBox chkClinicalNotes = (CheckBox) dialog.findViewById(R.id.chkClinicalNotes);

        chkOPNote.setVisibility(View.GONE);
        chkCaseNote.setVisibility(View.GONE);
        chkIncidentNote.setVisibility(View.GONE);
        chkServiceNote.setVisibility(View.GONE);
        chkClinicalNotes.setVisibility(View.GONE);

        if (user_settings.getAllowOPNote().equalsIgnoreCase("true")) {
            chkOPNote.setVisibility(View.VISIBLE);
        }
        if (user_settings.getAllowCaseNote().equalsIgnoreCase("true")) {
            chkCaseNote.setVisibility(View.VISIBLE);
        }
        if (user_settings.getAllowIncidentNote().equalsIgnoreCase("true")) {
            //chkIncidentNote.setVisibility(View.VISIBLE);
        }

        if (user_settings.getAllowOPNote().equalsIgnoreCase("true") && user_settings.getAllowCaseNote().equalsIgnoreCase("False"))// && user_settings.getAllowIncidentNote().equalsIgnoreCase("False"))
            chkOPNote.setChecked(true);

        if (user_settings.getAllowOPNote().equalsIgnoreCase("False") && user_settings.getAllowCaseNote().equalsIgnoreCase("true")) //&& user_settings.getAllowIncidentNote().equalsIgnoreCase("False"))
            chkCaseNote.setChecked(true);

        if (user_settings.getAllowOPNote().equalsIgnoreCase("False") && user_settings.getAllowCaseNote().equalsIgnoreCase("False") && user_settings.getAllowIncidentNote().equalsIgnoreCase("true")) {
            chkIncidentNote.setChecked(true);
        }


        final View rl_Shift_Note = (View) dialog.findViewById(R.id.rl_Shift_Note);
        final View rl_Shift_Note2 = (View) dialog.findViewById(R.id.rl_Shift_Note2);

        try {
            rl_Shift_Note.setVisibility(View.GONE);
            rl_Shift_Note2.setVisibility(View.GONE);

            if ((UseOPNoteAsShiftReport.equalsIgnoreCase("true") || UseOPNoteAsShiftReport.equals("1"))
                    && chkOPNote.isChecked() )
            {
                rl_Shift_Note.setVisibility(View.VISIBLE);
                rl_Shift_Note2.setVisibility(View.VISIBLE);

                TextView txtShiftDate = dialog.findViewById(R.id.txtShiftDate);
                //txtShiftDate.setText(RosterDate.substring(8,2) + "/" + RosterDate.substring(5,2) + "/" + RosterDate.substring(0,4));
                txtShiftDate.setText(RosterDate.substring(8,10) + "/" + RosterDate.substring(5,7) + "/" + RosterDate.substring(0,4) + " " + lblTime2.getText());

                lst_shiftgoals = get_ShiftGoals();
                if (lst_shiftgoals.size() > 0) {

                    ArrayAdapter adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lst_shiftgoals);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinner1.setAdapter(adapter1);
                }
            }
        }catch(Exception ex){Tost_Message(ex.toString());}


        // Tost_Message("Opening Dialog Form");


        EditText txtRosterNote = (EditText) dialog.findViewById(R.id.txtRosterNote);

        txtRosterNote.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MainActivity.idle_time=0;
                return false;
            }


        });



        Button dialogButton = (Button) dialog.findViewById(R.id.btnExit);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button dialogSave = (Button) dialog.findViewById(R.id.btnOK);
        // if button is clicked, close the custom dialog

        dialogSave.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String Note_Type = "OPNOTE";

                EditText txtRosterNote = (EditText) dialog.findViewById(R.id.txtRosterNote);
                if (chkOPNote.isChecked()) Note_Type = "OPNOTE";
                if (chkCaseNote.isChecked()) Note_Type = "CASENOTE";
                if (chkIncidentNote.isChecked()) Note_Type = "RECIMNOTE";

                String notes="";

                if ((UseOPNoteAsShiftReport.equalsIgnoreCase("true") || UseOPNoteAsShiftReport.equals("1"))
                        && chkOPNote.isChecked() )
                {


                    notes = "SHIFT REPORT FOR : " +  RosterDate + " " + lblTime2.getText() + "\n\n"
                            + "SHIFT GOAL/S: \n" +spinner1.getSelectedItem() + "\n\n"
                            + "SHIFT PURPOSE: \n" + txtShiftPurpose.getText()  + "\n\n"
                            + "ACTIONS: \n" + txtAction.getText() + "\n\n"
                            + "OUTCOME: \n" + txtOutCome.getText() + "\n\n"
                            + txtRosterNote.getText().toString();


                }else {

                    notes = txtRosterNote.getText().toString();
                }


                if (check_valid_note(notes)) {
                    notes = SQLSafe(notes); //notes=notes.replace("\'", "\'\'");
                    Update_client_Note(notes, Note_Type);
                    dialog.dismiss();
                } else

            }
        });
        chkOPNote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    chkIncidentNote.setChecked(false);
                    // chkIncidentNote.setEnabled(false);
                    chkCaseNote.setChecked(false);
                    //  chkCaseNote.setEnabled(false);
                    rl_Shift_Note.setVisibility(View.VISIBLE);
                    rl_Shift_Note2.setVisibility(View.VISIBLE);

                }


            }
        });
        chkCaseNote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    chkIncidentNote.setChecked(false);
                    // chkIncidentNote.setEnabled(false);
                    chkOPNote.setChecked(false);
                    // chkOPNote.setEnabled(false);
                    rl_Shift_Note.setVisibility(View.GONE);
                    rl_Shift_Note2.setVisibility(View.GONE);


                }


            }
        });

        chkIncidentNote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    chkCaseNote.setChecked(false);
                    //   chkCaseNote.setEnabled(false);
                    chkOPNote.setChecked(false);
                    //    chkOPNote.setEnabled(false);
                    rl_Shift_Note.setVisibility(View.GONE);
                    rl_Shift_Note2.setVisibility(View.GONE);
                    Set_Incident(dialog.getContext());
                    // dialog.dismiss();

                }


            }
        });





        dialog.show();
*/
    }

    public String SQLSafe(String sValue) {

        String sAns = null;
        sAns = sValue.replace("'", "''");
        sAns = sAns.replace("\"", "''");
        return (sAns);

    }

    public void Update_client_Note(String Note, String Note_Type) {
        TextView txtmsg = ((TextView) findViewById(R.id.txtAcknowledge));
        txtmsg.setVisibility(View.VISIBLE);
        // txtmsg.setText("Calling add note  " + URL4);

        if (Server_Available == false) {
            if (!isOnline(getApplicationContext()))
                Note = Note.replace("\n", "~");
            Update_client_Note2(Note, Note_Type);


            return;
        }

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME4);
            HttpTransportSE androidHttpTransport2 = new HttpTransportSE(URL4);
            androidHttpTransport2.debug = true;
            String values = AccountNo;// + "$" + Personid + "$" + OperatorID;
            PropertyInfo pi = new PropertyInfo();
            pi.setName("RecipientCode");
            pi.setValue(getSecurityToken() + values);
            request.addProperty(pi);


            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("PersonId");
            pi2.setValue(Personid);
            request.addProperty(pi2);

            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("OperatorID");
            pi3.setValue(OperatorID);
            request.addProperty(pi3);

            if (!check_valid_note(Note)) {
                Tost_Message(  "Please enter valid Note, You have used some invalid characters");
                return;
            }
            PropertyInfo pi4 = new PropertyInfo();
            pi4.setName("Note");
            pi4.setValue(Note);
            request.addProperty(pi4);

            PropertyInfo pi5 = new PropertyInfo();
            pi5.setName("Note_Type");
            pi5.setValue(Note_Type);
            request.addProperty(pi5);

            PropertyInfo pi6 = new PropertyInfo();
            pi6.setName("WhoCode");
            pi6.setValue(AccountNo);
            request.addProperty(pi6);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            SoapPrimitive result = null;

            // Make the soap call.
            androidHttpTransport2.call(SOAP_ACTION4, envelope);
            result = (SoapPrimitive) envelope.getResponse();

            if (Boolean.valueOf(result.toString()) == true) {
                Tost_Message(  "Client Note added Successfully");


            } else
                Tost_Message( "Operation not done - " + AccountNo + " Result=" + result.toString());

            try {
                String messgas = "The following op/case note has been added for client \"" + AccountNo + "\" by \"" + StaffCode + "\" :\n\n" + Note;
                String title = "  TRACCS Client Note Added for : " + AccountNo + "\n";

                send_email_alert(messgas, title);

            } catch (Exception ex) {

                Tost_Message("Operation not done due to some server error\n" + ex.toString());

            }


        } catch (Exception ex) {
            Tost_Message("Operation not done due to some server error\n" + ex.toString());

        }


    }

    public void Set_Incident(Context view) {
        Intent i = new Intent(Shift_Detail.this, Record_Incident.class);
        Bundle b = new Bundle();
        b.putString("RecordNo", RecordNo);
        b.putInt("Index", 0);
        b.putString("PersonId", Personid);
        b.putString("AccountNo", AccountNo);
        b.putString("Service_Setting", Service_Setting);
        b.putString("ServiceType", ServiceType);
        b.putString("Program", Program);
        b.putString("Cordinator_Email", Cordinator_Email);


        i.putExtras(b);

        startActivity(i);
    }

    public void Add_Incident2(String Incident_Type, String Service, String Incident_Severity, String Location, String Note, boolean No_Recipient, String StaffCode, String OperatorID, String AccountNo, String Program, String IncidentSummary) {
        try {
            String command;
            // command= "\n update roster set notes='" + Note.replace("'", "''") + "' WHERE recordno = " + RecordNo;
            //command= command+ "\n update Recipients set notes='" + Note.replace("'", "''") + "' WHERE AccountNo in (select [client code] from roster where recordno = " + RecordNo + ")";

            Note = SQLSafe(Note);
            Note = Note.replace("\n", "~");
            IncidentSummary = SQLSafe(IncidentSummary);
            Note = Note.replace("\n", "~");
            command = "\nN`" + Personid + "`" + Incident_Type + "`" + Service + "`" + Incident_Severity + "`" + Location + "`" + Note + "`" + No_Recipient + "`" + StaffCode + "`" + OperatorID + "`" + AccountNo + "`" + Program + "`" + IncidentSummary;


            set_Updates(command);
            try {
                String messgas = "Client Incident added by  \"" + StaffCode + "\" :\n\n" + "Incident Type : " + Incident_Type + "\nSeverity : " + Incident_Severity + " \nLocation : " + Location + " \nService : " + Service + "\n" + IncidentSummary + "\n" + Note;
                String title = "Client Incident for \"" + AccountNo + "\"\n";

                send_email_alert(messgas, title);

            } catch (Exception ex) {

            }
            Tost_Message("Client Incident added Successfully");
        } catch (Exception ex) {
            Tost_Message("Operation not done due to some error\n" + ex.toString());
        }
    }

    public void Add_Incident(String Incident_Type, String Service, String Incident_Severity, String Location, String Note, boolean No_Recipient, String IncidentSummary) {

        if (Server_Available == false) {
            Add_Incident2(Incident_Type, Service, Incident_Severity, Location, Note, No_Recipient, StaffCode, OperatorID, AccountNo, Program, IncidentSummary);
            return;
        }

        String URL41 = root + "/TimeSheet.asmx?op=Add_Incident";
        String SOAP_ACTION41 = "https://tempuri.org/Add_Incident";
        String METHOD_NAME41 = "Add_Incident";

        TextView txtmsg = ((TextView) findViewById(R.id.txtAcknowledge));
        txtmsg.setVisibility(View.VISIBLE);
        // txtmsg.setText("Calling add note  " + URL4);


        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME41);
            HttpTransportSE androidHttpTransport2 = new HttpTransportSE(URL41);
            androidHttpTransport2.debug = true;

            PropertyInfo pi = new PropertyInfo();
            pi.setName("PersonId");
            pi.setValue(getSecurityToken() + Personid);
            request.addProperty(pi);

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("Incident_Type");
            pi2.setValue(Incident_Type);
            request.addProperty(pi2);


            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("Service");
            pi3.setValue(Service);
            request.addProperty(pi3);

            PropertyInfo pi4 = new PropertyInfo();
            pi4.setName("Incident_Severity");
            pi4.setValue(Incident_Severity);
            request.addProperty(pi4);

            PropertyInfo pi5 = new PropertyInfo();
            pi5.setName("Location");
            pi5.setValue(Location);
            request.addProperty(pi5);

            PropertyInfo pi6 = new PropertyInfo();
            pi6.setName("Note");
            //  Note=Note.replace("\'", "\'\'");
            Note = SQLSafe(Note);
            pi6.setValue(Note);
            request.addProperty(pi6);

            PropertyInfo pi7 = new PropertyInfo();
            pi7.setName("staff");
            pi7.setValue(StaffCode);
            request.addProperty(pi7);

            PropertyInfo pi8 = new PropertyInfo();
            pi8.setName("OperatorID");
            pi8.setValue(OperatorID);
            request.addProperty(pi8);

            PropertyInfo pi9 = new PropertyInfo();
            pi9.setName("RecipientCode");
            pi9.setValue(AccountNo);
            request.addProperty(pi9);

            PropertyInfo pi10 = new PropertyInfo();
            pi10.setName("Program");
            pi10.setValue(Program);
            request.addProperty(pi10);


            PropertyInfo pi11 = new PropertyInfo();
            pi11.setName("No_Receipient");
            pi11.setValue(No_Recipient);
            request.addProperty(pi11);

            PropertyInfo pi12 = new PropertyInfo();
            pi12.setName("IncidentSummary");
            pi12.setValue(IncidentSummary);
            request.addProperty(pi12);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            SoapPrimitive result = null;

            // Make the soap call.
            androidHttpTransport2.call(SOAP_ACTION41, envelope);
            result = (SoapPrimitive) envelope.getResponse();

            if (Integer.parseInt(result.toString()) > 0) {

                Tost_Message( "Client Incident added Successfully");
                try {
                    String messgas = "Client Incident added by  \"" + StaffCode + "\" :\n\n" + "Incident Type : " + Incident_Type + "\nServity : " + Incident_Severity + " \nLocation : " + Location + " \nService : " + Service + "\n" + IncidentSummary + "\n" + Note;
                    String title = "Client Incident for \"" + AccountNo + "\"\n";


                    if (No_Recipient || Cordinator_Email.equals("")) {
                        tmp_cor_email = Cordinator_Email;
                        Cordinator_Email = Staff_Coordinator_Email;
                    }

                    send_email_alert(messgas, title);


                } catch (Exception ex) {
                    Tost_Message("Operation not done due to some server error\n" + ex.toString());
                }


            } else
                Tost_Message(  "Client Incident not added due to some problem - " + AccountNo + " Result=" + result.toString());


        } catch (Exception ex) {
            Tost_Message("Operation not done due to some server error\n" + ex.toString());
        }


    }

    public long Add_Leave2(String Leave_Type, String Start_Date, String End_Date, String Note, String Address1) {
        try {
            String command;
            // command= "\n update roster set notes='" + Note.replace("'", "''") + "' WHERE recordno = " + RecordNo;
            //command= command+ "\n update Recipients set notes='" + Note.replace("'", "''") + "' WHERE AccountNo in (select [client code] from roster where recordno = " + RecordNo + ")";

            Note = SQLSafe(Note);

            command = "\nL`" + StaffCode + "`" + Leave_Type + "`" + Start_Date + "`" + End_Date + "`" + Note + "`" + Address1;


            set_Updates(command);
            Tost_Message( "Leave Application added Successfully");
            return 1;

        } catch (Exception ex) {
            Tost_Message( "Operation not done due to some error\n" + ex.toString());
            return 0;
        }
    }

    long Add_Leave(String Leave_Type, String Start_Date, String End_Date, String Note, String Address1) {

        long r_val = 0;

        if (Server_Available == false) {
            r_val = Add_Leave2(Leave_Type, Start_Date, End_Date, Note, Address1);
            return r_val;
        }

        String URL41 = root + "/TimeSheet.asmx?op=Add_LeaveEntry";
        String SOAP_ACTION41 = "https://tempuri.org/Add_LeaveEntry";
        String METHOD_NAME41 = "Add_LeaveEntry";

        //  TextView txtmsg=  ((TextView) findViewById(R.id.txtAcknowledge));
        //txtmsg.setVisibility(View.VISIBLE);
        // txtmsg.setText("Calling add note  " + URL4);


        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME41);
            HttpTransportSE androidHttpTransport2 = new HttpTransportSE(URL41);
            androidHttpTransport2.debug = true;

            PropertyInfo pi = new PropertyInfo();
            pi.setName("StaffCode");
            pi.setValue(getSecurityToken() + StaffCode);
            request.addProperty(pi);

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("Leave_Type");
            pi2.setValue(Leave_Type);
            request.addProperty(pi2);


            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("Start_Date");
            pi3.setValue(Start_Date);
            request.addProperty(pi3);

            PropertyInfo pi4 = new PropertyInfo();
            pi4.setName("End_Date");
            pi4.setValue(End_Date);
            request.addProperty(pi4);

            PropertyInfo pi5 = new PropertyInfo();
            pi5.setName("Note");
            pi5.setValue(Note);
            request.addProperty(pi5);

            PropertyInfo pi6 = new PropertyInfo();
            pi6.setName("Address1");
            pi6.setValue(Address1);
            request.addProperty(pi6);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            SoapPrimitive result = null;

            // Make the soap call.
            androidHttpTransport2.call(SOAP_ACTION41, envelope);
            result = (SoapPrimitive) envelope.getResponse();
            r_val = Integer.parseInt(result.toString());
            if (r_val > 0) {

                  /*  try
                    {
                        String messgas="Leave Application submitted by  \"" + StaffCode + "\" :\n\n" + "Leave_Type : " + Leave_Type + "\nFrom : " + Start_Date + " to "+ End_Date + "\n" + Note;
                        String title="Leave Application for \"" + StaffCode+ "\"\n" ;
                        send_email_alert(messgas,title);
                    }catch(Exception ex){}*/


            } else

                try {
                    //    sendEmail("Client Note",Note,"arshadblouch81@yahoo.com");
                } catch (Exception ex) {


                }


        } catch (Exception ex) {

        }

        return r_val;

    }

    public void Update_client_Note2(String Note, String Note_Type) {
        try {
            String command;
            // command= "\n update roster set notes='" + Note.replace("'", "''") + "' WHERE recordno = " + RecordNo;
            //command= command+ "\n update Recipients set notes='" + Note.replace("'", "''") + "' WHERE AccountNo in (select [client code] from roster where recordno = " + RecordNo + ")";

            Note = SQLSafe(Note);
            Note = Note.replace("\n", "~");

            command = "\n" + "insert into history(detaildate,PersonId,creator,Detail,PrivateFlag,ExtraDetail1,ExtraDetail2,whocode,PublishToApp) " +
                    "values(getDate(),'" + Personid + "','" + OperatorID + "','" + SQLSafe(Note) + "',0,'" + Note_Type + "','" + Note_Type + "','" + AccountNo + "',1)";


            set_Updates(command);

            try {
                String messgas = "The following op/case note has been added for client \"" + AccountNo + "\" by \"" + StaffCode + "\" :\n\n" + Note;
                String title = "  TRACCS Client Note Added for : " + AccountNo + "\n";

                send_email_alert(messgas, title);

            } catch (Exception ex) {

            }
            ((TextView) findViewById(R.id.txtAcknowledge)).setText("Client Note added Successfully");
        } catch (Exception ex) {
            ((TextView) findViewById(R.id.txtAcknowledge)).setText("Operation not done due to some server error\n" + ex.toString());
        }

    }

    public Date getDateFromDatePicket(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }


    class MyAsyncClass_Leave extends AsyncTask<String, String, Long> {

        LoadingDialog pDialog;
        String messgas = "";
        long r_val = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Shift_Detail.this);
            pDialog.setMessage("Please wait while Processing Leave....");
            pDialog.show();

        }

        @Override
        protected Long doInBackground(String... mApi) {


            try {

                String parm1 = mApi[0];
                String parm2 = mApi[1];

                String[] vals1 = parm1.split("`");
                String[] vals2 = parm2.split("`");

                messgas = "Leave Application submitted by  \"" + StaffCode + "\" :\n\n" + "Leave_Type : " + vals1[0] + "\nFrom : " + vals2[0] + " to " + vals2[1] + "\n" + vals1[1];
                r_val = Add_Leave(vals1[0], vals2[0], vals2[1], vals1[1], "");

                return r_val;


            } catch (Exception ex) {

            }
            return r_val;
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            pDialog.cancel();


            if (result > 0) {
                Tost_Message(  "Leave Application added Successfully");
                try {
                    String title = "Leave Application for \"" + StaffCode + "\n\n";
                    send_email_alert(messgas, title);

                } catch (Exception ex) {
                    Tost_Message(  ex.toString());
                }
            } else
                Tost_Message( "Unable to add Leave Application ");
        }
    }

    public void set_Updates(String command) throws IOException {
        // File froot = null;
        try {
            // check for SDcard
            ((TextView) findViewById(R.id.txtAcknowledge)).setText("making update");
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File filein = null;
            File fileDir = null;
            String Command2 = "";

            if (command.indexOf("\n", 1) > 0) {
                Command2 = command.replace("\n", "%%");
                Command2 = "\n" + Command2.substring(2);
            } else
                Command2 = command;


            String state = Environment.getExternalStorageState();
            //  txtAcknowledge.setText("No Server found - " + state);
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
                        out.write(Command2);
                        out.close();

                    } catch (Exception e) {
                        txtAcknowledge.setText(e.toString());
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
                    out.write(Command2);
                    out.close();
                }
            }
        } catch (Exception e) {
            //  Button ts = (Button) findViewById(R.id.txtAddress);
            // ts.setText(e.toString());
        }
        ((TextView) findViewById(R.id.txtAcknowledge)).setText("Operation done successfully");
    }

    public void set_Job_Status_In_Local_file(String command) throws IOException {
        // File froot = null;
        try {
            // check for SDcard

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File filein = null;
            File fileDir = null;
            String state = Environment.getExternalStorageState();
            //  txtAcknowledge.setText("No Server found - " + state);
            if (Environment.MEDIA_MOUNTED.equals(state)) {

                //check sdcard permission
                fileDir = new File(froot.getAbsolutePath() + "/.server/");

                filein = new File(fileDir, "jobs.txt");

                if (filein.exists()) {
                    try {
                        froot.setWritable(true);
                        //if (froot.canWrite()) {

                        File file = new File(fileDir, "jobs.txt");
                        FileWriter filewriter = new FileWriter(file, true);
                        BufferedWriter out = new BufferedWriter(filewriter);
                        out.write("\n" + command);
                        out.close();

                    } catch (Exception e) {
                        txtAcknowledge.setText(e.toString());
                        //Log.e("Exception","error occurred while creating xml file");
                    }
                } else {
                    froot.setWritable(true);

                    //if (froot.canWrite()) {

                    if (!fileDir.exists())
                        fileDir.mkdirs();

                    File file = new File(fileDir, "jobs.txt");
                    FileWriter filewriter = new FileWriter(file, true);
                    BufferedWriter out = new BufferedWriter(filewriter);
                    out.write("\n" + command);
                    out.close();
                }
            }
        } catch (Exception e) {
            //  Button ts = (Button) findViewById(R.id.txtAddress);
            // ts.setText(e.toString());
        }

    }

   /* private void show_TaskList() {
        // TextView tv = (TextView) findViewById(R.id.txtRunsheetAlert);
        try {


            getTskList2();
            Task t = null;
            txtAcknowledge = ((TextView) findViewById(R.id.txtAcknowledge));
            items = new Task[task_elements];
            for (int i = 0; i < task_elements; i++) {
                t = lst_task.get(i);
                //  tv.setText(tv.getText() + "\n" + t.getRecordNo() + " " + t.TaskCOmpleteget() + " " + t.getTaskDetail());
                items[i] = t;
            }
            if (task_elements > 0) {
                //gridView = (ListView) findViewById(R.id.gridViewTask);

                //  gridView.setMinimumHeight(400);
              //  gridView.setVisibility(View.VISIBLE);
              //  gridView.setAdapter(new ListAdapter(this, items, root, Server_Available, OperatorId, Security_Token, settings));
                //  tv.setText("");
                //  tv.setVisibility(View.GONE);

                setListViewHeightBasedOnChildren(gridView);
                txtAcknowledge.setVisibility(View.VISIBLE);
            } else {
                txtAcknowledge.setVisibility(View.INVISIBLE);
                gridView.setVisibility(View.INVISIBLE);
            }


        } catch (Exception ex) {
            //tv.setHeight(100);
            //   tv.setText("Operation not done due to some server error\n" + ex.toString());
        }
    }*/

    private boolean TaskList_checked_status() {
        boolean status = false;
        try {
            getTskList2();

            try {
                if (lst_task.size() > 0) ;
                //    settings.edit().putBoolean("Update", true).commit();
            } catch (Exception ex) {
            }
            Task t = null;
            for (int i = 0; i < task_elements; i++) {
                t = lst_task.get(i);

                if (t.getTaskCOmplete().equalsIgnoreCase("-")) {
                    status = true;
                    break;
                }

            }

        } catch (Exception ex) {
        }
        return status;
    }

    public void getTskList() {
        String URL5 = root + "/TimeSheet.asmx?op=getTaskList";
        String SOAP_ACTION5 = "https://tempuri.org/getTaskList";
        String METHOD_NAME5 = "getTaskList";

        if (Server_Available == false) {
            try {
                getTskList2();
                return;
            } catch (Exception ex) {
            }
        }
        lst_task = new ArrayList<Task>();
        Task tsk = null;
        try {


            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME5);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
            androidHttpTransport.debug = true;

            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("RecordNo");

            String message = getSecurityToken() + RecordNo;
            pi3.setValue(message);
            request.addProperty(pi3);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION5, envelope);

            SoapObject result = (SoapObject) envelope.getResponse();

            String obj;
            task_elements = 0;
            for (int j = 0; j < result.getPropertyCount(); j++) {
                SoapObject result2 = (SoapObject) result.getProperty(j);
                tsk = new Task();
                for (int i = 0; i < result2.getPropertyCount(); i++) {
                    obj = result2.getProperty(i).toString();

                    switch (i) {
                        case 0:
                            tsk.setRosterRecordNo(obj);
                            break;
                        case 1:
                            tsk.setRecordNo(obj);
                            break;
                        case 2:
                            tsk.setTaskCOmplete(obj);
                            break;
                        case 3:
                            tsk.setTaskDetail(obj);
                            break;

                    }

                }
                task_elements = task_elements + 1;
                lst_task.add(tsk);

            }

        } catch (Exception aE) {
            //((TextView) findViewById(R.id.txtRosterAlert)).setText(aE.toString());
        }

    }

    public void getTskList2() throws ParserConfigurationException, SAXException {


        lst_task = new ArrayList<Task>();
        Task tsk = null;
        try {

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            File fileDir = null;
            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = new File(fileDir, "task.xml");

            TextView txtAcknowledge = findViewById(R.id.txtAddress);

            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Task");

                if (nList == null) return;
                task_elements = 0;
                for (int temp = 0; temp < nList.getLength(); temp++) {

                    try {
                        Node nNode = nList.item(temp);

                        tsk = new Task();
                        String value = "";
                        //  txtAcknowledge.setText("\nCurrent Element :" + nNode.getNodeName());
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            value = eElement.getElementsByTagName("RosterRecordNo").item(0).getTextContent();
                            //   txtAcknowledge.setText( txtAcknowledge.getText() +"\n" + value + " = " +RecordNo );
                            if (!value.equals(RecordNo)) continue;

                            value = eElement.getElementsByTagName("RecordNo").item(0).getTextContent();
                            tsk.setRecordNo(value);
                            value = eElement.getElementsByTagName("TaskCOmplete").item(0).getTextContent();
                            tsk.setTaskCOmplete(value);
                            value = eElement.getElementsByTagName("TaskDetail").item(0).getTextContent();
                            tsk.setTaskDetail(value);
                            task_elements = task_elements + 1;
                            lst_task.add(tsk);
                        }


                    } catch (Exception aE) {
                        // ((Button) findViewById(R.id.txtAddress)).setText(aE.toString());
                    }
                }
            } else {
                txtAcknowledge.setText("Xml for Task List file not found");
            }
        } catch (Exception aE) {
            //  ((Button) findViewById(R.id.txtAddress)).setText(aE.toString());
        }
    }

    public void getLeaves() throws ParserConfigurationException, SAXException {


        lst_leave = new ArrayList<String>();


        try {

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            File fileDir = null;
            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = new File(fileDir, "LeaveTypes.xml");

            //   Button txtAcknowledge = findViewById(R.id.txtAddress));

            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("DataDomain");

                if (nList == null) return;

                for (int temp = 0; temp < nList.getLength(); temp++) {

                    try {
                        Node nNode = nList.item(temp);


                        String value = "";
                        //  txtAcknowledge.setText("\nCurrent Element :" + nNode.getNodeName());
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;

                            value = eElement.getElementsByTagName("Description").item(0).getTextContent();


                            lst_leave.add(value);
                        }


                    } catch (Exception aE) {
                        // ((Button) findViewById(R.id.txtAddress)).setText(aE.toString());
                    }
                }
            } else {
                txtAcknowledge.setText("Xml for Task List file not found");
            }
        } catch (Exception aE) {

        }
    }


    public void Make_update(String command) {
        try {
            if (Server_Available == false) {
                try {
                    set_Updates("\n" + command);
                    return;
                } catch (Exception ex) {
                    //txtMsg.setText(ex.toString());
                }
            }
            String URL5 = root + "/TimeSheet.asmx?op=Make_update";
            String SOAP_ACTION5 = "https://tempuri.org/Make_update";
            String METHOD_NAME5 = "Make_update";
            int timeout_in_millsecond = 6000;

            if ((!command.equals("")) && command != null) {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME5);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5, timeout_in_millsecond);
                androidHttpTransport.debug = true;

                PropertyInfo pi = new PropertyInfo();
                pi.setName("command");

                pi.setValue(command);
                request.addProperty(pi);


                PropertyInfo pi2 = new PropertyInfo();
                pi2.setName("Fontra");
                pi2.setValue(getSecurityToken() + "99");
                request.addProperty(pi2);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                // Make the soap call.

                androidHttpTransport.call(SOAP_ACTION5, envelope);

                SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
            }
        } catch (Exception ex) {
            txtAcknowledge.setText("Operation not done due to some server error\n" + ex.toString());
        }

    }

    public long Job_already_started2(String RecordNo) throws IOException {


        long rec_no = xml.get_Job_Started(RecordNo, RosterDate);

        return rec_no;

    }

    public long Job_already_started(String RecordNo) {
        long sts = 0;
        try {
            if (Server_Available == false) {
                long REC = Job_already_started2(RecordNo);
                return REC;
            }
            String URL5 = root + "/TimeSheet.asmx?op=Job_Already_Started";
            String SOAP_ACTION5 = "https://tempuri.org/Job_Already_Started";
            String METHOD_NAME5 = "Job_Already_Started";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME5);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
            androidHttpTransport.debug = true;

            PropertyInfo pi = new PropertyInfo();
            pi.setName("Recordno");

            pi.setValue(getSecurityToken() + RecordNo);
            request.addProperty(pi);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION5, envelope);

            SoapPrimitive result = (SoapPrimitive) envelope.getResponse();

            sts = Integer.parseInt(result.toString());
           /* if (result.toString().toLowerCase().equals("true"))
                 sts= true;
            else
                sts= false;
           */

        } catch (Exception ex) {

        }
        return sts;
    }


    public void Job_Status2(String RecordNo) throws IOException {

        btn_start_job = findViewById(R.id.btnstartjob);
        btn_time = findViewById(R.id.btnsetTime);
        btn_ack = findViewById(R.id.btnAcknowledge);
        TextView txtAcknowledge = findViewById(R.id.txtAcknowledge);

        String sts = "No Job has been started";
        if (Shift_Status.equalsIgnoreCase("Started")) {
            sts = "JOB STARTED";
            btn_start_job.setText("END JOB");
            btn_start_job.setTag("E");
        } else if (Shift_Status.equalsIgnoreCase("Completed")) {
            sts = "JOB COMPLETED";


        } else {
            sts = "JOB NOT STARTED";

        }

        if (View_only || ExcludeFromAppLogging){
            btn_start_job.setVisibility(View.GONE);
            btn_time.setVisibility(View.GONE);
            btn_ack.setVisibility(View.GONE);

            txtAcknowledge.setText("View Only Job");
            if(ExcludeFromAppLogging) {
                Tost_Message("This shift does not require log on/off");
                txtAcknowledge.setText("This shift does not require log on/off");
                //View_only=true;
            }
            return;
        }


        txtAcknowledge.setTag(sts);
        txtAcknowledge.setText(sts + " (" + RecordNo + " )");


        String curr_Date = "";
        try {
            Calendar c = Calendar.getInstance();
            Date dt = c.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            curr_Date = sdf.format(dt);



        } catch (Exception ex) {
        }


        //   if (!user_settings.getToDate().equals(RosterDate) ) {
        if (!curr_Date.equals(RosterDate)) {
            btn_ack.setVisibility(View.INVISIBLE);
            btn_start_job.setEnabled(false);
            btn_time.setEnabled(false);
            btn_start_job.setVisibility(View.GONE);
            btn_time.setVisibility(View.GONE);
            txtAcknowledge.setVisibility(View.GONE);
            Settings_Done = true;
            // long days = calculateDays(RosterDate, user_settings.getToDate());
            long days = calculateDays(RosterDate, curr_Date);

            //  result = xml.get_Job_Started(RecordNo, RosterDate);
            // Roster_Info last_shift=   xml.Get_MultipleGroup_Last_Shift(RecordNo);
            //EndTime=last_shift.get_End_Time();

            View_only = true;
            //Toast.makeText(getApplicationContext()," result=" + result + ",days=" + days, Toast.LENGTH_LONG).show();

            if (Shift_Status.equalsIgnoreCase("Started") && (days == 1) && (EndTime.equals("00:00") || EndTime.equals("24:00"))) {
                btn_start_job.setEnabled(true);
                btn_start_job.setVisibility(View.VISIBLE);
                txtAcknowledge.setVisibility(View.VISIBLE);
                Process_Sleep_Over = true;
                View_only = false;
                btn_start_job.setText("END JOB");
            } else if (Shift_Status.equalsIgnoreCase("Started") && days == 1) {
                btn_start_job.setEnabled(true);
                btn_start_job.setVisibility(View.VISIBLE);
                txtAcknowledge.setVisibility(View.VISIBLE);
                btn_start_job.setText("END JOB");
                Process_Sleep_Over = true;
                View_only = false;
            }
        }

        try {
            GPS_Status = "Location settings disabled";
            if (isGPSEnabled()) {
                GPS_Status = "Location settings enabled";
            } else {
                GPS_Status = "Location settings disabled";
            }
        } catch (Exception ex) {
        }

        if (TMMode.equals("1") || TMMode.equals("True") || Boolean.parseBoolean(TMMode) == true) {
            btn_start_job.setText(btn_start_job.getText());
        }



    }

    public Transport_Detail getTransportDetail(String RecordNo) {

        Transport_Detail trns = null;
        String RecordNo2 = "";
        String val = "";
        try {
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();
            File fileDir = null;
            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = new File(fileDir, "Transport.xml");
            //  rosters=new ArrayList<Roster_Info>();
            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Transport_Detail");
                if (nList == null) return null;

                for (int temp = 0; temp < nList.getLength(); temp++) {

                    try {
                        Node nNode = nList.item(temp);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;

                            RecordNo2 = eElement.getElementsByTagName("RecordNo").item(0).getTextContent();

                            if (RecordNo2.equals(RecordNo)) {
                                trns = new Transport_Detail();
                                try {
                                    trns.setRecordNo(RecordNo2);
                                    if (eElement.getElementsByTagName("PickUpAddress1").item(0) == null)
                                        val = "";
                                    else
                                        val = eElement.getElementsByTagName("PickUpAddress1").item(0).getTextContent();

                                } catch (Exception ex) {
                                }
                                trns.setPickUpAddress1(val);

                                try {

                                    if (eElement.getElementsByTagName("DropOffAddress1").item(0) == null)
                                        val = "";
                                    else
                                        val = eElement.getElementsByTagName("DropOffAddress1").item(0).getTextContent();

                                } catch (Exception ex) {
                                }
                                trns.setDropOffAddress1(val);

                                try {

                                    if (eElement.getElementsByTagName("VehicleCode").item(0) == null)
                                        val = "";
                                    else
                                        val = eElement.getElementsByTagName("VehicleCode").item(0).getTextContent();

                                } catch (Exception ex) {
                                }
                                trns.setVehicleCode(val);


                                try {

                                    if (eElement.getElementsByTagName("StartODO").item(0) == null)
                                        val = "";
                                    else
                                        val = eElement.getElementsByTagName("StartODO").item(0).getTextContent();

                                } catch (Exception ex) {
                                }
                                trns.setStartODO(val);

                                try {
                                    if (eElement.getElementsByTagName("EndODO").item(0) == null)
                                        val = "";
                                    else
                                        val = eElement.getElementsByTagName("EndODO").item(0).getTextContent();

                                } catch (Exception ex) {
                                }
                                trns.setEndODO(val);

                                try {
                                    if (eElement.getElementsByTagName("Roster_Date").item(0) == null)
                                        val = "";
                                    else
                                        val = eElement.getElementsByTagName("Roster_Date").item(0).getTextContent();

                                } catch (Exception ex) {
                                }
                                trns.setRoster_Date(val);

                                try {
                                    if (eElement.getElementsByTagName("appointmentTime").item(0) == null)
                                        val = "";
                                    else
                                        val = eElement.getElementsByTagName("appointmentTime").item(0).getTextContent();

                                } catch (Exception ex) {
                                }
                                trns.setAppointmentTime(val);

                                try {
                                    if (eElement.getElementsByTagName("Client_Code").item(0) == null)
                                        val = "";
                                    else
                                        val = eElement.getElementsByTagName("Client_Code").item(0).getTextContent();

                                } catch (Exception ex) {
                                }
                                trns.setClient_Code(val);

                                try {
                                    if (eElement.getElementsByTagName("Carer_Code").item(0) == null)
                                        val = "";
                                    else
                                        val = eElement.getElementsByTagName("Carer_Code").item(0).getTextContent();

                                } catch (Exception ex) {
                                }
                                trns.setCarer_Code(val);
                                try {
                                    if (eElement.getElementsByTagName("Mobility").item(0) == null)
                                        val = "";
                                    else
                                        val = eElement.getElementsByTagName("Mobility").item(0).getTextContent();

                                } catch (Exception ex) {
                                }
                                trns.setMobility(val);

                                return trns;

                            }
                        }
                    } catch (Exception ex) {
                    }
                }
            }
        } catch (Exception ex) {
        }

        return trns;
    }

    public void getRoster_Recipient2(String RecordNo) {
        boolean load_again = false;
        try {
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();
            File fileDir = null;
            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = new File(fileDir, "Roster_Recipient.xml");
            //  rosters=new ArrayList<Roster_Info>();
            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Roster_Recipient");


                String RecordNo2 = "";

               // String ServiceType = "";
                String job_timing = "";

                boolean Recipient_Found = false;

                for (int temp = 0; temp < nList.getLength(); temp++) {

                    try {
                        Node nNode = nList.item(temp);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;

                            RecordNo2 = eElement.getElementsByTagName("RecordNo").item(0).getTextContent();

                            if (RecordNo2.equals(RecordNo)) {
                                try {
                                    Recipient_Found = true;
                                    if (eElement.getElementsByTagName("Title").item(0) == null)
                                        Title = "";
                                    else
                                        Title = eElement.getElementsByTagName("Title").item(0).getTextContent();

                                } catch (Exception ex) {
                                }

                                try {
                                    FirstName = eElement.getElementsByTagName("FirstName").item(0).getTextContent();
                                    lastName = eElement.getElementsByTagName("LastName").item(0).getTextContent();
                                    job_timing = eElement.getElementsByTagName("Job_Timing").item(0).getTextContent();
                                  //  ServiceType = eElement.getElementsByTagName("ServiceType").item(0).getTextContent();
                                    PreferredName = eElement.getElementsByTagName("PreferredName").item(0).getTextContent();
                                    dateOfBirth = eElement.getElementsByTagName("DateOfBirth").item(0).getTextContent();
                                    age = eElement.getElementsByTagName("Age").item(0).getTextContent();
                                } catch (Exception ex) {
                                }

                                try {

                                    Cordinator_Email = "";
                                    if (eElement.getElementsByTagName("Cordinator_Email").item(0).getTextContent() != null) {

                                        Cordinator_Email = eElement.getElementsByTagName("Cordinator_Email").item(0).getTextContent();
                                    }
                                } catch (Exception ex) {
                                }


                                try {

                                    RECIPIENT_COORDINATOR = "";
                                    if (eElement.getElementsByTagName("RECIPIENT_COORDINATOR").item(0).getTextContent() != null) {

                                        RECIPIENT_COORDINATOR = eElement.getElementsByTagName("RECIPIENT_COORDINATOR").item(0).getTextContent();
                                    }
                                } catch (Exception ex) {
                                }


                                try {

                                    Receipient_Email = "";
                                    if (eElement.getElementsByTagName("Email").item(0).getTextContent() != null) {

                                        Receipient_Email = eElement.getElementsByTagName("Email").item(0).getTextContent();
                                    }

                                } catch (Exception ex) {
                                }
                                try {

                                    Mobility = "";
                                    if (eElement.getElementsByTagName("Mobility").item(0).getTextContent() != null) {

                                        Mobility = eElement.getElementsByTagName("Mobility").item(0).getTextContent();
                                    }

                                } catch (Exception ex) {
                                }

                                try {


                                    if (eElement.getElementsByTagName("PinCode").item(0).getTextContent() != null) {

                                        Recipient_PinCode = eElement.getElementsByTagName("PinCode").item(0).getTextContent();
                                    }

                                } catch (Exception ex) {
                                }


                                TextView txtBooking = findViewById(R.id.txtBooking);

                                String ltime_detail = "";
                                if (FirstName.equalsIgnoreCase(lastName))
                                    ltime_detail = FirstName;//+ " - " + job_timing;
                                else
                                    ltime_detail =  FirstName + " " + lastName;//+ " - " + job_timing;

                               // if (!PreferredName.equalsIgnoreCase("-") && !PreferredName.equalsIgnoreCase(""))
                                 //   ltime_detail = ltime_detail + " (" + PreferredName + ")";

                                txtClient.setText(ltime_detail);

                                //   ltime_detail=spcace + ltime_detail + spcace;
                                String DOB_Detail = "";
                               /* if (dateOfBirth.equals("01/01/1900") || Actual_Client_Code.equalsIgnoreCase("!MULTIPLE") || Actual_Client_Code.equalsIgnoreCase("!INTERNAL")) {
                                    DOB_Detail = "";
                                }else{
                                    DOB_Detail="\nDOB : " + dateOfBirth + " (" + age + ")";
                                }
*/
                                if (!MTAServiceType.equalsIgnoreCase("")) {
                                    txtServiceType.setText(ServiceType + "\nJOB MTA Service : " + MTAServiceType + "");
                                 //   txtBooking.setText(ltime_detail + "\n" + MTAServiceType );
                                }else {
                                    txtServiceType.setText(ServiceType + "");
                                   // txtBooking.setText(ltime_detail  );
                                }

                                if (!RECIPIENT_COORDINATOR.equals("")) {
                                    txtBooking.setText(ltime_detail + "\nCoordinator : " + RECIPIENT_COORDINATOR + DOB_Detail);

                                }



                                lblTime.setText(job_timing);
                            //    StartTime = job_timing.substring(0, 5);

                                try {
                                    //   this.setTitle(ltime_detail);
                                    //  getSupportActionBar().setTitle(ltime_detail);
                                    //   getActionBar().setTitle(ltime_detail);

                                } catch (Exception ex) {
                                }
                                try {


                                    if (eElement.getElementsByTagName("Careplanchange").item(0).getTextContent() != null) {

                                        Careplanchange = eElement.getElementsByTagName("Careplanchange").item(0).getTextContent();
                                    }
                                    if (eElement.getElementsByTagName("RosterNotes").item(0).getTextContent() != null) {

                                        RosterNotes = eElement.getElementsByTagName("RosterNotes").item(0).getTextContent();

                                    }
                                    if (eElement.getElementsByTagName("SpecialConsiderations").item(0).getTextContent() != null) {

                                        specialConsideration = eElement.getElementsByTagName("SpecialConsiderations").item(0).getTextContent();
                                    }

                                    TextView txtRunsheetAlert_Label = (TextView) findViewById(R.id.txtRunsheetAlert_Label);
                                    TextView txtExtraInfo_Label = (TextView) findViewById(R.id.txtExtraInfo_Label);
                                    TextView txtExtraInfo = (TextView) findViewById(R.id.txtExtraInfo);

                                    TextView txtCarePlanAlert = (TextView) findViewById(R.id.txtCarePlanAlert);
                                    //    TextView txtCarePlanAlert_title = (TextView) findViewById(R.id.txtCarePlanAlert_title);
                                    TextView tvalert = (TextView) findViewById(R.id.txtRunsheetAlert);
                                    View rl_CarePlanAlert = (View) findViewById(R.id.cardView5);

                                    rl_CarePlanAlert.setVisibility(View.GONE);

                                    //   txtCarePlanAlert_title.setVisibility(View.VISIBLE);
                                    txtCarePlanAlert.setVisibility(View.GONE);
                                    tvalert.setVisibility(View.GONE);

                                    txtRunsheetAlert_Label.setVisibility(View.GONE);
                                    txtExtraInfo_Label.setVisibility(View.GONE);
                                    txtExtraInfo.setVisibility(View.GONE);

                                    txtCarePlanAlert.setText("");
                                    RosterNotes = notes_from_roster ;//+ "\n" + RosterNotes  ;

                                    btn_time = findViewById(R.id.btnsetTime);
                                    RosterNotes = RosterNotes.replaceAll("\\s+"," ");

                                   // if (View_only) return;

                                    if (Shift_Status.equalsIgnoreCase("Not Started") && !View_only) {
                                        btn_ack.setVisibility(View.INVISIBLE);
                                        btn_start_job.setVisibility(View.VISIBLE);
                                        Button btn_time = (Button) findViewById(R.id.btnsetTime);
                                        btn_time.setVisibility(View.VISIBLE);
                                        if (AllowSetTime.equalsIgnoreCase("true")) {
                                            btn_time.setBackground(getDrawable(R.drawable.btn_dark_blue_simple));
                                            btn_time.setTextColor( getResources().getColor(R.color.background_white));
                                        }else {
                                            btn_time.setBackground(getDrawable(R.drawable.disabled_button));
                                            btn_time.setTextColor( getResources().getColor(R.color.text_color));
                                        }

                                        txtAcknowledge.setVisibility(View.VISIBLE);
                                    }
                                    try {
                                        if (Careplanchange.equalsIgnoreCase("true") || Careplanchange.equalsIgnoreCase("1")) {
                                            btn_ack.setVisibility(View.VISIBLE);
                                            btn_start_job.setVisibility(View.INVISIBLE);
                                            btn_time.setVisibility(View.INVISIBLE);
                                            txtAcknowledge.setVisibility(View.INVISIBLE);


                                            if (!specialConsideration.equals("") && !specialConsideration.equals("-")) {
                                                txtCarePlanAlert.setVisibility(View.VISIBLE);
                                                txtRunsheetAlert_Label.setVisibility(View.VISIBLE);
                                                txtCarePlanAlert.setText(("** CAREPLAN CHANGE – Please review careplan **\n" +
                                                        specialConsideration + "\n" +  ""));
                                                //txtCarePlanAlert.setText(txtCarePlanAlert.getText()+"");
                                                if (!RosterNotes.equals("") && !RosterNotes.equals("-")) {
                                                    txtExtraInfo_Label.setVisibility(View.VISIBLE);
                                                    txtExtraInfo.setVisibility(View.VISIBLE);
                                                    txtExtraInfo.setText(RosterNotes);
                                                }
                                            } else if (!RosterNotes.equals("") && !RosterNotes.equals("-")) {
                                              //  txtCarePlanAlert.setText(("** CAREPLAN CHANGE – Please review careplan **" + "\n" + "" + RosterNotes + ""));
                                                txtExtraInfo.setText(RosterNotes);
                                                txtExtraInfo_Label.setVisibility(View.VISIBLE);
                                                txtExtraInfo.setVisibility(View.VISIBLE);
                                            } else {
                                                //txtCarePlanAlert_title.setVisibility(View.GONE);
                                                txtCarePlanAlert.setVisibility(View.GONE);
                                                rl_CarePlanAlert.setVisibility(View.GONE);
                                                txtRunsheetAlert_Label.setVisibility(View.GONE);


                                            }
                                        } else if ((!specialConsideration.equals("") && !specialConsideration.equals("-"))) {
                                            btn_ack.setVisibility(View.VISIBLE);
                                            btn_start_job.setVisibility(View.INVISIBLE);
                                            btn_time.setVisibility(View.INVISIBLE);
                                              txtAcknowledge.setVisibility(View.INVISIBLE);
                                            txtCarePlanAlert.setVisibility(View.VISIBLE);
                                            txtRunsheetAlert_Label.setVisibility(View.VISIBLE);

                                            txtCarePlanAlert.setText((specialConsideration + "\n" +  ""));
                                            txtCarePlanAlert.setVisibility(View.VISIBLE);
                                            txtRunsheetAlert_Label.setVisibility(View.VISIBLE);
                                            rl_CarePlanAlert.setVisibility(View.VISIBLE);
                                            if (!RosterNotes.equals("") && !RosterNotes.equals("-")) {
                                                txtExtraInfo_Label.setVisibility(View.VISIBLE);
                                                txtExtraInfo.setVisibility(View.VISIBLE);
                                                txtExtraInfo.setText(RosterNotes);
                                            }

                                        } else if ((!RosterNotes.equals("") && !RosterNotes.equals("-"))) {
                                            btn_ack.setVisibility(View.VISIBLE);
                                            btn_start_job.setVisibility(View.INVISIBLE);
                                            btn_time.setVisibility(View.INVISIBLE);
                                            txtAcknowledge.setVisibility(View.INVISIBLE);
                                           // txtCarePlanAlert.setText(("" + RosterNotes + ""));
                                          //  txtCarePlanAlert.setVisibility(View.VISIBLE);
                                            rl_CarePlanAlert.setVisibility(View.VISIBLE);
                                            //timer	txtCarePlanAlert.setText(txtCarePlanAlert.getText()+"")

                                                txtExtraInfo_Label.setVisibility(View.VISIBLE);
                                                txtExtraInfo.setVisibility(View.VISIBLE);
                                                txtExtraInfo.setText(RosterNotes);


                                        } else {
                                            //txtCarePlanAlert_title.setVisibility(View.GONE);
                                            txtCarePlanAlert.setVisibility(View.GONE);
                                            rl_CarePlanAlert.setVisibility(View.GONE);
                                            txtRunsheetAlert_Label.setVisibility(View.GONE);
                                            btn_ack.setVisibility(View.INVISIBLE);
                                        }
                                        if (!RunsheetAlerts.equals("-") && !RunsheetAlerts.equals("")) {
                                            txtRunsheetAlert_Label.setVisibility(View.VISIBLE);
                                            tvalert.setText(RunsheetAlerts);
                                            tvalert.setVisibility(View.VISIBLE);
                                            if (txtCarePlanAlert.getText().toString().equalsIgnoreCase(""))
                                                txtCarePlanAlert.setVisibility(View.GONE);
                                            else
                                                txtCarePlanAlert.setVisibility(View.VISIBLE);

                                            rl_CarePlanAlert.setVisibility(View.VISIBLE);

                                            btn_ack.setVisibility(View.VISIBLE);
                                            btn_start_job.setVisibility(View.INVISIBLE);
                                            btn_time.setVisibility(View.INVISIBLE);
                                              txtAcknowledge.setVisibility(View.INVISIBLE);
                                            //    rl_CarePlanAlert.setVisibility(View.VISIBLE);
                                        }
                                        if (View_only || ExcludeFromAppLogging) {
                                            btn_ack.setVisibility(View.GONE);
                                            btn_start_job.setVisibility(View.GONE);
                                            btn_time.setVisibility(View.GONE);
                                        }
                                    } catch (Exception ex) {
                                    }


                                } catch (Exception ex) {
                                }

                                break;
                            }
                        }
                    } catch (Exception ex) {
                    }

                }
                if (!Recipient_Found) {
                    try {
                        btn_ack.setVisibility(View.INVISIBLE);
                        btn_start_job.setVisibility(View.INVISIBLE);
                        btn_time.setVisibility(View.INVISIBLE);
                        txtAcknowledge.setVisibility(View.INVISIBLE);
                        if (loading_Recipient >= 2) {

                            new MyAsyncClass4_Recipient().execute();
                        }
                    } catch (Exception ex) {
                        return;
                    }
                    return;
                }
                if (Shift_Status.equalsIgnoreCase("Started")) {
                    btn_ack.setVisibility(View.INVISIBLE);
                    btn_start_job.setVisibility(View.VISIBLE);
                    btn_time.setVisibility(View.VISIBLE);
                    if (AllowSetTime.equalsIgnoreCase("true")) {
                        btn_time.setBackground(getDrawable(R.drawable.btn_dark_blue_simple));
                        btn_time.setTextColor( getResources().getColor(R.color.background_white));
                    }else {
                        btn_time.setBackground(getDrawable(R.drawable.disabled_button));
                        btn_time.setTextColor( getResources().getColor(R.color.text_color));
                    }

                    txtAcknowledge.setVisibility(View.VISIBLE);

                } else if (Shift_Status.equalsIgnoreCase("Completed")) {
                    btn_ack.setVisibility(View.INVISIBLE);
                    btn_start_job.setVisibility(View.GONE);
                    btn_time.setVisibility(View.GONE);
                    txtAcknowledge.setVisibility(View.VISIBLE);

                }

            } else {
                try {
                    if (loading_Recipient<2) {
                        new MyAsyncClass4_Recipient().execute();
                    }
                } catch (Exception ex) {
                    return;
                }
            }
        } catch (Exception ex) {
        }


    }



    public void getRoster_Recipient() {

        try {
            String URL5 = root + "/TimeSheet.asmx?op=getRoster_RecipientAll";
            String SOAP_ACTION5 = "https://tempuri.org/getRoster_RecipientAll";
            String METHOD_NAME5 = "getRoster_RecipientAll";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME5);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
            androidHttpTransport.debug = true;

            PropertyInfo pi = new PropertyInfo();
            pi.setName("StaffCode");

            pi.setValue(getSecurityToken() + StaffCode);
            request.addProperty(pi);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION5, envelope);

            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath() + "/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "Roster_Recipient.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();
                } catch (IOException e) {

                }
                FileOutputStream fileos = null;
                try {
                    fileos = new FileOutputStream(newxmlfile);
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                } catch (Exception ex) {
                } finally {
                    fileos.close();
                }
            }

        } catch (Exception ex) {
        }

        String URL2 = root + "/TimeSheet.asmx?op=getStaff_Recipient_Detail";
        String SOAP_ACTION2 = "https://tempuri.org/getStaff_Recipient_Detail";
        String METHOD_NAME2 = "getStaff_Recipient_Detail";

        String buff = "";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL2);
            androidHttpTransport.debug = true;

            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("AccountNo");
            pi3.setValue(getSecurityToken() + StaffCode);
            request.addProperty(pi3);

            PropertyInfo pi4 = new PropertyInfo();
            pi4.setName("MobileFutureLimit");
            pi4.setValue(MobileFutureLimit);
            request.addProperty(pi4);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            androidHttpTransport.call(SOAP_ACTION2, envelope);
            // SoapObject obj=(SoapObject) envelope.getResponse();
            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "Recipient.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();

                } catch (IOException e) {
                    // txtAcknowledge.setText("bb "+ e.toString());
                }
                FileOutputStream fileos = null;


                try {
                    fileos = new FileOutputStream(newxmlfile);
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                } catch (Exception ex) {
                } finally {
                    fileos.close();
                }
            }
        } catch (Exception ex) {
        }

    }

    public void ShowDialog(String commandText) {
        try {
            Button btn_time = (Button) findViewById(R.id.btnsetTime);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(btn_time.getContext());
            alertDialogBuilder.setTitle("");
            alertDialogBuilder
                    .setMessage(commandText)
                    .setCancelable(false)
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.getWindow().setGravity(10);
            // show it
            alertDialog.show();

        } catch (Exception ex) {
        }
    }

    void Add_roster_Note(Context context) {

        try {
            Bundle bundle = new Bundle();

            bundle.putString("RecordNo", RecordNo);
            bundle.putString("Roster_Date", RosterDate);
            bundle.putString("Recipient", AccountNo);
            bundle.putString("AccountNo", Actual_Client_Code);
            bundle.putString("PersonId", Personid);
            bundle.putString("Job_Time", StartTime);
            bundle.putString("Main_Op_Note", "SVCNOTE");
            bundle.putString("ServiceType", ServiceType);

            Intent intent2 = new Intent(Shift_Detail.this, Add_Service_Note.class);
            intent2.putExtras(bundle);
            Shift_Detail.this.startActivity(intent2);
            // Toast.makeText(v.getContext(),  "No Issue", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Tost_Message(ex.toString());
        }

    }

    public boolean isGPSEnabled() {
        boolean status = false;
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            status = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        return status;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isOnline(Context context) {

        ConnectivityManager connectivityManager;


        boolean connected = false;
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();

            if (Server_Available == false && connected) {

            Tost_Message("Online Connection becomes available\nRe-login the App in online mode");

                finish();
            }

            return connected;


        } catch (Exception e) {
            ///System.out.println("CheckConnectivity Exception: " + e.getMessage());
            // Log.v("connectivity", e.toString());
        }
        return connected;
    }


    private String getLocation_From_Lat_long(double lat, double longt) {
        String result = "";
        String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + longt + "&sensor=true";
        // String  url ="http://maps.googleapis.com/maps/api/directions/json?origin=Lahore%20Pakistan&destination=Sargodha%20Pakistan&sensor=false";

        TextView txtAddress = (TextView) findViewById(R.id.txtAddress);
        //  txtAddress.setText(url);

        String[] tag = {"text"};
        JSONObject jsonObj = null;
     /*   HttpResponse response = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            response = httpClient.execute(httpPost, localContext);

            try {
                String resp_body = EntityUtils.toString(response.getEntity());
                // Log.v("resp_body", resp_body.toString());
                jsonObj = new JSONObject(resp_body);

                //  txtAddress.setText("jsonObj\n"+ jsonObj.toString());

            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
            try {


                JSONArray obj1 = (JSONArray) jsonObj.getJSONArray("results");
                // txtAddress.setText("obj1\n"+ obj1.toString());

                JSONObject obj2 = (JSONObject) obj1.getJSONObject(0);
                // txtAddress.setText("obj2\n"+ obj2.toString());
                result = "";
                JSONArray obj3 = (JSONArray) obj2.getJSONArray("address_components");

                for (int i = 0; i < obj3.length(); i++)
                    result = result + ", " + obj3.getJSONObject(i).getString("long_name");

                // JSONObject obj4  =(JSONObject)obj2.getJSONObject("long_name");
                //  txtAddress.setText(txtAddress.getText() + "\nobj4\n"+ obj4.toString());

                // txtAddress.setText( result);


            } catch (Exception e) { // txtAddress.setText(e.toString());

            }

            //  Toast.makeText(getApplicationContext(), result_in_kms , Toast.LENGTH_LONG).show();
            // txtAddress.setText(result_in_kms);

        } catch (Exception e) {
        }
*/

        return result;
    }

    /* public Location_Address find_Location3(Context con) {
        //  Log.d("Find Location", "in find_location");

        double lat;
        double longt;
        GPSTracker mGPS = new GPSTracker(con);
        lat  = mGPS.getLatitude();
        longt= mGPS.getLongitude();


        loc_address.Latitude=lat;
        loc_address.Longitude=longt;

        String add="";

        String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + longt + "&sensor=true";
        String tag[] = { "text" };
        JSONObject jsonObj=null;
        HttpResponse response = null;

        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            response = httpClient.execute(httpPost, localContext);

            try
            {
                String resp_body = EntityUtils.toString(response.getEntity());
                // Log.v("resp_body", resp_body.toString());
                jsonObj = new JSONObject(resp_body);

                //  txtAddress.setText("jsonObj\n"+ jsonObj.toString());

            }catch(Exception ex){Toast.makeText(getApplicationContext(), ex.toString() , Toast.LENGTH_LONG).show();}
            try{



                JSONArray obj1= (JSONArray)jsonObj.getJSONArray("results");
                // txtAddress.setText("obj1\n"+ obj1.toString());

                JSONObject obj2=(JSONObject)obj1.getJSONObject(0);
                //  ooo=( obj2.toString());
                // Toast.makeText(getApplicationContext(), ooo , Toast.LENGTH_LONG).show();
                JSONArray obj3= (JSONArray)obj2.getJSONArray("address_components");
                //  JSONArray obj31= (JSONArray)obj2.getJSONArray("geometry");
                JSONObject obj4;

                for(int i=0; i<obj3.length(); i++){
                    obj4  =(JSONObject)obj3.getJSONObject(i);

                    add= add + " " + obj4.getString("long_name") ;
                }
                loc_address.Address=add;

                Button btnOptions=(Button)findViewById(R.id.btnOptions);
                // Toast.makeText(getApplicationContext(), add , Toast.LENGTH_LONG).show();

                btnOptions.setText(add);

            }catch(Exception e){
                Toast.makeText(getApplicationContext(), e.toString() , Toast.LENGTH_LONG).show();
            }


        }catch(Exception ex){ Toast.makeText(getApplicationContext(), ex.toString() , Toast.LENGTH_LONG).show();}

        return loc_address;
    }*/
    private String getDistanceOnRoad(String current_location, String Desination) {

        Tost_Message( "=================\n" + current_location + "\n" + Desination);
        String result_in_kms = "";
        String url = "http://maps.googleapis.com/maps/api/directions/json?origin=" + current_location.replaceAll(" ", "%20") + "&destination=" + Desination.replaceAll(" ", "%20") + "&sensor=false";
        // String  url ="http://maps.googleapis.com/maps/api/directions/json?origin=Lahore%20Pakistan&destination=Sargodha%20Pakistan&sensor=false";

        // TextView txtAddress = (TextView)findViewById(R.id.txtAddress);
        // txtAddress.setText(url);

       /* String tag[] = {"text"};
        JSONObject jsonObj = null;
        HttpResponse response = null;
        try {

            try{
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(final String hostname, final SSLSession session) {
                    HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                    return hv.verify(hostname, session);
                }
            });
            }catch(Exception ex){}
           HttpClient httpClient = new DefaultHttpClient();

         *//*    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(final String hostname, final SSLSession session) {
                    if (new NullHostNameVerifier().verify(hostname,session))
                        return true;
                    else
                        return false;
                }
            });*//*

            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            response = httpClient.execute(httpPost, localContext);

            try {
                String resp_body = EntityUtils.toString(response.getEntity());
                // Log.v("resp_body", resp_body.toString());
                jsonObj = new JSONObject(resp_body);

                //  txtAddress.setText("jsonObj\n"+ jsonObj.toString());

            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
            try{


                JSONArray obj1 = (JSONArray) jsonObj.getJSONArray("routes");
                // txtAddress.setText("obj1\n"+ obj1.toString());

                JSONObject obj2 = (JSONObject) obj1.getJSONObject(0);
                // txtAddress.setText("obj2\n"+ obj2.toString());

                JSONArray obj3 = (JSONArray) obj2.getJSONArray("legs");
                //  txtAddress.setText("obj3\n"+ obj3.toString());

                JSONObject obj4 = (JSONObject) obj3.get(0);

                // txtAddress.setText("obj4\n"+ obj4.toString());

                // txtAddress.setText("Final Value\n"+ obj4.getJSONObject("distance").getString("text"));

        	  *//*String val[]=obj4.getJSONObject("distance").getString("text").split(" ");

        		if (val[1].equalsIgnoreCase("m")){
        			double l_val=Double.parseDouble(val[0])/1000;
        			result_in_kms = "" + l_val ;
        		}else
        			result_in_kms =val[0];
                *//*

                String val = obj4.getJSONObject("distance").getString("value");
                //  double l_val=Double.parseDouble(val)/1000;
                result_in_kms = "" + val;

            } catch (Exception e) {  //txtAddress.setText(e.toString());

            }

            // Toast.makeText(getApplicationContext(), "result_in_kms" + result_in_kms , Toast.LENGTH_LONG).show();
            //  txtAddress.setText(result_in_kms);

        } catch (Exception e) {
        }
*/
        return result_in_kms;

    }

    Location_Address loc = null;

    public void getLocation_and_do_Job_Setting(Context v) {
        String distance = "";
        Distance = 8000;

        // Toast.makeText(getApplicationContext(), "Apply_Goe_Location_Setting = " + Apply_Goe_Location_Setting  , Toast.LENGTH_LONG).show();

        if (Apply_Goe_Location_Setting.equalsIgnoreCase("False")
                || Exclude_Goe_Location_Setting.equalsIgnoreCase("True")
                || Actual_Client_Code.equalsIgnoreCase("!INTERNAL")) return;


        btn_ack = (Button) findViewById(R.id.btnAcknowledge);
        btn_start_job = (Button) findViewById(R.id.btnstartjob);
        btn_End_job = (Button) findViewById(R.id.btnstartjob);
        btn_time = (Button) findViewById(R.id.btnsetTime);

        TextView lblMsg = (TextView) findViewById(R.id.txtAcknowledge);
        //Toast.makeText(getApplicationContext(), "Distance 2 = " + distance, Toast.LENGTH_LONG).show();
        btn_start_job.setEnabled(false);
        btn_End_job.setEnabled(false);
        btn_End_job.setVisibility(View.GONE);
        btn_time.setEnabled(false);
        btn_ack.setVisibility(View.INVISIBLE);


        Geocoder geocoder = null;
        List<Address> addresses;
        Location_values Current = new Location_values();
        Location_values Destination = new Location_values();

        Address location_dest = null;

        if (!isGPSEnabled()) {
            Tost_Message("Location Manager is disabled");
            return;
        }


        try {
            loc_address = loc;//get_Current_Location();

            if (loc_address != null) {
                // Location_Address loc= find_Location3(v);
                Current.latitude = loc_address.Latitude;
                Current.latitude = loc_address.Longitude;

                Current_Address = loc_address.Address;
                Latitude = "" + loc_address.Latitude;
                Longitude = "" + loc_address.Longitude;

            }
        } catch (Exception ex) {
        }

        //if (Current==null ) Toast.makeText(this.getBaseContext(), " Current location not found *********", Toast.LENGTH_LONG).show();

        //	Toast.makeText(getApplicationContext(), Current_Address, Toast.LENGTH_LONG).show();
        TextView txtAddress = (TextView) findViewById(R.id.txtAddress);
        String addr = txtAddress.getTag().toString();
        try {

            if (geocoder == null)
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());


            if (addr.equalsIgnoreCase("") || addr.equalsIgnoreCase("-")) {
                addr = "Sydney Australia";
            }
            try {
              /*  Current_Address="62/64 Hambledon Circuit, Harrington Park NSW 2567, Australia";
                distance = getDistanceOnRoad(Current_Address, Simple_Address);
                distance.replace("KM", "");*/
            } catch (Exception ex) {
            }


            if (distance != null && !distance.isEmpty() && !distance.equals("")) {
                mobilegeocodelimit = settings.getString("mobilegeocodelimit", "10");
                Tost_Message("Current Location =" + Current_Address + " \nDestination = " + addr);
                Tost_Message( "Distance =" + distance + " Limit=" + Double.parseDouble(mobilegeocodelimit) * 1000 + " M ");

                Distance = Double.parseDouble(distance);
                if (Distance <= Double.parseDouble(mobilegeocodelimit) * 1000 && Distance >= 0) {

                    btn_time.setEnabled(true);
                    btn_start_job.setEnabled(true);
                    btn_End_job.setEnabled(true);
                    //// btn_End_job.setVisibility(View.VISIBLE);
                    btn_ack.setVisibility(View.INVISIBLE);
                    //  Toast.makeText(getApplicationContext(), "Job Processing enabled due to valid distance " + distance + " from Destination", Toast.LENGTH_SHORT).show();
                    //  lblMsg.setText("Job Processing enabled due to valid distance " + Distance + " from Destination");
                    // lblMsg.setText("");


                } else {
                    btn_start_job.setEnabled(false);
                    btn_End_job.setEnabled(false);
                    btn_End_job.setVisibility(View.GONE);

                    btn_time.setEnabled(false);
                    btn_ack.setVisibility(View.INVISIBLE);
                    try {
                        GPS_Status = "Location settings disabled";
                        if (isGPSEnabled()) {
                            GPS_Status = "Location settings enabled";
                        } else {
                            GPS_Status = "Location settings disabled";
                        }
                    } catch (Exception ex) {
                    }

                    //Tost_Message( "Job processing disabled due to invalid distance " + Distance + "  from Destination");

                    Tost_Message("Job processing disabled due to invalid distance " + Distance + " m from Destination\n" +

                            "\nCurrent Location =" + Current_Address + " \n\nDestination = " + addr);


                }

                return;
            }

            try {
                //Toast.makeText(getApplicationContext(),  "Dest=" +addresses.get(0) +" Size=" + addresses.size(), Toast.LENGTH_LONG).show();

                addresses = geocoder.getFromLocationName(addr, 5);
                // Toast.makeText(getApplicationContext(),  "Dest=" +addresses.get(0) +" Size=" + addresses.size(), Toast.LENGTH_LONG).show();

                location_dest = addresses.get(0);
                // Toast.makeText(getApplicationContext(),  "Values == " + location_dest.getLatitude() + " , " + location_dest.getLongitude() , Toast.LENGTH_LONG).show();

                Destination.latitude = location_dest.getLatitude();
                Destination.longitude = location_dest.getLongitude();
            } catch (Exception ex) {
            }


        } catch (NullPointerException e) {
            // e.printStackTrace();
            Tost_Message("Error in distance finding" + e.toString());
            return;
        }

        // Toast.makeText(getApplicationContext(),  "Exclude_Goe_Location_Setting = " + Exclude_Goe_Location_Setting + " Apply_Goe_Location_Setting= " + Apply_Goe_Location_Setting , Toast.LENGTH_LONG).show();

        //-------------------Calculate distance of current and destination location------------------


        try {
            int earthRadius = 6371;

            double lat1 = Current.latitude, lon1 = Current.longitude;

            double lat2 = Destination.latitude, lon2 = location_dest.getLongitude();

            double dLat = (double) Math.toRadians(lat2 - lat1);
            double dLon = (double) Math.toRadians(lon2 - lon1);
            double a =
                    (double) (Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
                            * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2));
            double c = (double) (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
            double d = earthRadius * c;

            Distance = Math.round(d);

        } catch (Exception ex) {

            Tost_Message( "Correct distance from Recipient Destination not found " + location_dest + "\n" + Current.latitude);
            return;
        }

        //  Toast.makeText(getApplicationContext(),  "Distance from Destination = " + Distance+ " > " + mobilegeocodelimit  , Toast.LENGTH_LONG).show();


        try {
            Tost_Message("Current Location =" + Current_Address + " \nDestination = " + addr);
            Tost_Message("Distance =" + Distance + " Limit=" + Double.parseDouble(mobilegeocodelimit) * 1000 + " M ");

            if (Distance <= Double.parseDouble(mobilegeocodelimit) && Distance >= 0) {

                btn_start_job.setEnabled(true);
                btn_End_job.setEnabled(true);
                btn_End_job.setVisibility(View.GONE);
                btn_ack.setVisibility(View.VISIBLE);
                //  Toast.makeText(getApplicationContext(), "Job Processing enabled due to valid distance " + distance + " from Destination", Toast.LENGTH_SHORT).show();
                //  lblMsg.setText("Job Processing enabled due to valid distance " + Distance + " from Destination");
                // lblMsg.setText("");


            } else {
                btn_start_job.setEnabled(false);
                btn_End_job.setEnabled(false);
                btn_End_job.setVisibility(View.GONE);
                btn_ack.setVisibility(View.INVISIBLE);

                try {
                    GPS_Status = "Location settings disabled";
                    if (isGPSEnabled()) {
                        GPS_Status = "Location settings enabled";
                    } else {
                        GPS_Status = "Location settings disabled";
                    }
                } catch (Exception ex) {
                }

               // Toast.makeText(getApplicationContext(), "Job processing disabled due to invalid distance " + Distance + " from Destination", Toast.LENGTH_SHORT).show();

               Tost_Message("Job processing disabled due to invalid distance " + Distance + " from Destination\n" +

                        "\nCurrent Location =" + Current_Address + " \n\nDestination = " + addr);

            }

        } catch (Exception ex) {
            //	Toast.makeText(getApplicationContext(), "Error in distance finding" +  ex.toString(), Toast.LENGTH_LONG).show();
        }


    }

    public JSONObject getLocationInfo(String Address) {

       /* //  String  sQuery = "http://maps.google.com/maps/api/geocode/xml?address=" + Address.replaceAll(" ","%20") + "&sensor=true";
        String sQuery = "http://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=" + Address.replaceAll(" ", "%20") + "&sensor=false";

        String theXml = "";
        try {
            theXml = URLEncoder.encode(sQuery, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {

            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(final String hostname, final SSLSession session) {
                    HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                    return hv.verify(hostname, session);
                }
            });
        }catch(Exception ex){}
        HttpGet httpGet = new HttpGet(theXml);
        HttpClient client = new DefaultHttpClient();

        HttpResponse response;
        // StringBuilder stringBuilder = new StringBuilder();
        String stringBuilder = "";
        try {
            response = client.execute(httpGet);
            //  HttpEntity entity = response.getEntity();
            // InputStream stream = entity.getContent();

            stringBuilder = getJSONString(response);


        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }*/
        return null;//`jsonObject;
    }

  /*  public String getJSONString(HttpResponse response) {

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(response.getEntity().getContent());
            NodeList nl = doc.getElementsByTagName("string");
            Node n = nl.item(0);
            String str = n.getFirstChild().getNodeValue();
            System.out.println("Node value : " + str);
            return str;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }*/

    void get_distance() {
	/*String  sQuery = "http://maps.google.com/maps/api/geocode/xml?address=" & N2S(!Address) & "&sensor=false&region=$region";
	            xhrRequest.Open "GET", sQuery, False
	            xhrRequest.sEnd
	            domResponse.LoadXML xhrRequest.responseText
	            Set ixnStatus = domResponse.selectSingleNode("//status")
	            Set ixnLat = domResponse.selectSingleNode("//geometry/location/lat")
	            Set ixnLon = domResponse.selectSingleNode("//geometry/location/lng")
*/
    }


    void set_Permission() {

        screen_width = 240;
        //  hide_Navigation_Items();


        try {


            TextView tvaddress = findViewById(R.id.txtAddress);

            if (user_settings.getShowClientPhoneInApp().equalsIgnoreCase("0") || user_settings.getShowClientPhoneInApp().equalsIgnoreCase("false")) {

                msg = Client_address;//"\n" + (address == null ? "-" : address);
                //  msg = msg + "\n";
                tvaddress.setText(HtmlCompat.fromHtml(msg,0));
            }

            if ( Actual_Client_Code.equalsIgnoreCase("!MULTIPLE") || Actual_Client_Code.equalsIgnoreCase("!INTERNAL") ) {


                tvaddress.setText("");
                ImageView imageView2= findViewById(R.id.imageView2);
                imageView2.setVisibility(View.INVISIBLE);
            }

            if (settings.getString("HideAddress","0").equalsIgnoreCase("true")) {
                tvaddress.setVisibility(View.INVISIBLE);
            }else{
                tvaddress.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
        }


        try {
            //	Toast.makeText(getApplicationContext(), email_seting.getPOP3User() + "\n" + email_seting.getPOP3Password() + "\nSending Email from SMTP Server :" + email_seting.getPOP3Server(), Toast.LENGTH_LONG).show();
            //	 sender = new     GMailSender(email_seting.getPOP3User() , email_seting.getPOP3Password(),email_seting.getPOP3Server(),"465");
            get_GroupAlerts2();


            if (email_seting != null) {
                //email= new Email ("traccs.sprt.mta@gmail.com","arshadabbas","smtp.gmail.com","465");
                email = new Email(email_seting.getFromEmail(), email_seting.getSMTPPassword(), email_seting.getSMTPServer(), email_seting.getSMTP_Port());
                //Toast.makeText(getApplicationContext(), email_seting.getPOP3User() + "\n" + email_seting.getPOP3Password() + "\nSending Email from SMTP Server :" + email_seting.getPOP3Server(), Toast.LENGTH_LONG).show();
            }

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.
                    Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

        } catch (Exception ex) {
        }

        /// sleep over rosters processing


//        set_Permission();

        getEmail_Address();

        try {
            if (!Actual_Client_Code.equalsIgnoreCase("!INTERNAL") && !Actual_Client_Code.equalsIgnoreCase("!MULTIPLLE"))
                new MyAsyncClass4_Recipient_Photo().execute();
            //  Read_Recipient_Photo();

        } catch (Exception ex) {
        }


        Transport_Detail transport = getTransportDetail(RecordNo);
        TextView txtTransport = findViewById(R.id.txtTransport);
        View rl_t = findViewById(R.id.cardView7);
        rl_t.setVisibility(View.GONE);
        String Transport = "";
        if (transport != null) {
            if (transport.getAppointmentTime() != null) {

                rl_t.setVisibility(View.VISIBLE);
                txtTransport.setVisibility(View.VISIBLE);



                TextView txtPicUp = (TextView) findViewById(R.id.txtPickUp_From);
                TextView txtTakeTo = (TextView) findViewById(R.id.txtDropTo);
                TextView txtAppointmentTime = (TextView) findViewById(R.id.txtAppointmentTime);
                TextView txtMobility = (TextView) findViewById(R.id.txtMobility);

                txtAppointmentTime.setText( transport.getAppointmentTime() );
                txtMobility.setText(Mobility );
                txtPicUp.setText(transport.getPickUpAddress1());
                txtTakeTo.setText(transport.getDropOffAddress1());
                txtMobility.setText(transport.getMobility());

 
            }
        }

    }
String getHMTLSpaces(int sp){
        String spaces="";
        for (int i=0; i<sp; i++)
            spaces=spaces + "&#8287;";

        return  spaces;
}
    boolean Get_User_Settings2() {
        boolean status = true;
        user_settings = new User_Settings();
        try {
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();
            File fileDir = null;
            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = new File(fileDir, "User_Settings.xml");


            //  rosters=new ArrayList<Roster_Info>();
            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("getUser_SettingsResult");

                String current_element = "";

                if (nList == null) return false;

                try {
                    Node nNode = nList.item(0);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        try {
                            current_element = eElement.getElementsByTagName("AllowSetTime").item(0).getTextContent();

                            user_settings.setAllowSetTime(current_element);

                        } catch (Exception e) {
                        }
                        try {
                            user_settings.setTMMode(eElement.getElementsByTagName("TMMode").item(0).getTextContent());
                        } catch (Exception e) {
                        }

                        try {
                            user_settings.setMobileFutureLimit(eElement.getElementsByTagName("MobileFutureLimit").item(0).getTextContent());
                        } catch (Exception e) {
                        }

                        try {
                            user_settings.setAllowPicUpload(eElement.getElementsByTagName("AllowPicUpload").item(0).getTextContent());
                        } catch (Exception e) {
                        }
                        try {
                            current_element = eElement.getElementsByTagName("Apply_Goe_Location_Setting").item(0).getTextContent();
                            user_settings.setApply_Goe_Location_Setting(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            Apply_Goe_Location_Setting = user_settings.getApply_Goe_Location_Setting();
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("KMAgainstTravelOnly").item(0).getTextContent();
                            KMAgainstTravelOnly = current_element;
                            user_settings.setKMAgainstTravelOnly(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("mobilegeocodelimit").item(0).getTextContent();
                            user_settings.setMobilegeocodelimit(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("StaffLocationUpdateInterval").item(0).getTextContent();
                            user_settings.setStaffLocationUpdateInterval(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("AllowIncidentEntry").item(0).getTextContent();
                            user_settings.setAllowIncidentEntry(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("AllowTravelEntry").item(0).getTextContent();
                            user_settings.setAllowTravelEntry(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("AllowClientNoteEntry").item(0).getTextContent();
                            user_settings.setAllowClientNoteEntry(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("AllowRosterNoteEntry").item(0).getTextContent();
                            user_settings.setAllowRosterNoteEntry(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("StaffCode").item(0).getTextContent();
                            user_settings.setStaffCode(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("AllowRegisterSign").item(0).getTextContent();
                            user_settings.setAllowRegisterSign(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("ToDate").item(0).getTextContent();
                            user_settings.setToDate(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("Time").item(0).getTextContent();
                            user_settings.setTime(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("UserSessionLimit").item(0).getTextContent();
                            UserSessionLimit = (current_element);
                        } catch (Exception e) {
                        }

                        try {
                            user_settings.setUserSessionLimit(UserSessionLimit);

                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("ShowClientPhoneInApp").item(0).getTextContent();
                            user_settings.setShowClientPhoneInApp(current_element);

                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("TA_TRAVELDEFAULT").item(0).getTextContent();
                            TA_TRAVELDEFAULT = current_element;
                            user_settings.setTA_TRAVELDEFAULT(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("CheckAlertInterval").item(0).getTextContent();
                            user_settings.setCheckAlertInterval(current_element);

                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("AllowOPNote").item(0).getTextContent();
                            user_settings.setAllowOPNote(current_element);

                        } catch (Exception e) {
                        }


                        try {
                            current_element = eElement.getElementsByTagName("AllowClinicalNoteEntry").item(0).getTextContent();
                            user_settings.setAllowClinicalNoteEntry(current_element);

                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("AllowCaseNote").item(0).getTextContent();
                            user_settings.setAllowCaseNote(current_element);

                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("EnableViewNoteCases").item(0).getTextContent();
                            user_settings.set_EnableViewNoteCases(current_element);

                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("AllowIncidentNote").item(0).getTextContent();
                            user_settings.setAllowIncidentNote(current_element);

                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("AppUsesSMTP").item(0).getTextContent();
                            user_settings.setAppUsesSMTP(current_element);

                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("AllowLeaveEntry").item(0).getTextContent();
                            user_settings.setAllowLeaveEntry(current_element);

                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("EnableRosterDelivery").item(0).getTextContent();
                            user_settings.setEnableRosterDelivery(current_element);

                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("MinimumInternetSpeedForOnline").item(0).getTextContent();
                            user_settings.setMinimumInternetSpeedForOnline(current_element);
                            MinimumInternetSpeedForOnline = Integer.parseInt(current_element);
                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("GeolocateEnabled").item(0).getTextContent();
                            user_settings.setGeolocateEnabled(current_element);

                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("HideGeolocation").item(0).getTextContent();
                            user_settings.setHideGeolocation(current_element);

                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("Enable_Shift_End_Alarm").item(0).getTextContent();
                            user_settings.setEnable_Shift_End_Alarm(current_element);

                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("LeaveLeadTime").item(0).getTextContent();
                            user_settings.setLeaveLeadTim(current_element);

                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("AllowViewGoalPlans").item(0).getTextContent();
                            user_settings.setAllowViewGoalPlans(current_element);

                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("Coordinator_Email").item(0).getTextContent();
                            user_settings.setCoordinator_Email(current_element);
                            Staff_Coordinator_Email = current_element;

                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("AllowViewGoalPlans").item(0).getTextContent();
                            user_settings.setAllowViewGoalPlans(current_element);


                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("ViewClientCareplans").item(0).getTextContent();
                            user_settings.set_ViewClientCareplans(current_element);


                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("ViewClientDocuments").item(0).getTextContent();
                            user_settings.set_ViewClientDocuments(current_element);


                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("RestrictTravelSameDay").item(0).getTextContent();
                            user_settings.set_RestrictTravelSameDay(current_element);


                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("HIdeCancelButton").item(0).getTextContent();
                            user_settings.setHIdeCancelButton(current_element);

                            try {

                                current_element = eElement.getElementsByTagName("EnableRosterAvailability").item(0).getTextContent();
                                user_settings.setEnableRosterAvailability(current_element);


                            } catch (Exception e) {
                            }

                            try {

                                current_element = eElement.getElementsByTagName("SuppressEmailOnRosterNote").item(0).getTextContent();
                                user_settings.setSuppressEmailOnRosterNote(current_element);


                            } catch (Exception e) {
                            }

                            try {

                                current_element = eElement.getElementsByTagName("GoogleAPI_Key").item(0).getTextContent();
                                user_settings.set_GoogleAPI_Key(current_element);


                            } catch (Exception e) {
                            }

                            try {

                                current_element = eElement.getElementsByTagName("AllowMTASaveUserPass").item(0).getTextContent();
                                user_settings.setAllowMTASaveUserPass(current_element);


                            } catch (Exception e) {
                            }

                            try {

                                current_element = eElement.getElementsByTagName("UseOPNoteAsShiftReport").item(0).getTextContent();
                                user_settings.setUseOPNoteAsShiftReport(current_element);
                                UseOPNoteAsShiftReport = current_element;

                            } catch (Exception e) {
                            }

                            try {

                                current_element = eElement.getElementsByTagName("UseServiceNoteAsShiftReport").item(0).getTextContent();
                                user_settings.setUseServiceNoteAsShiftReport(current_element);
                                UseServiceNoteAsShiftReport = current_element;

                            } catch (Exception e) {
                            }
                            try {
                                current_element = eElement.getElementsByTagName("EmailUnavailabilityNotification").item(0).getTextContent();
                                user_settings.setEmailUnavailabilityNotification(current_element);
                            } catch (Exception e) {
                            }

                            try {

                                current_element = eElement.getElementsByTagName("HideAddress").item(0).getTextContent();
                                user_settings.setHideAddress(current_element);
                                // HideAddress=current_element;

                            } catch (Exception e) {
                            }

                            try {
                                current_element = eElement.getElementsByTagName("UserOverrideIncidentEmail").item(0).getTextContent();
                                user_settings.setUserOverrideIncidentEmail(current_element);
                            } catch (Exception e) {
                            }
                            try {
                                current_element = eElement.getElementsByTagName("OverrideIncientEmail").item(0).getTextContent();
                                user_settings.setOverrideIncientEmail(current_element);
                            } catch (Exception e) {
                            }



                            try {
                                current_element = eElement.getElementsByTagName("UserOverrideIncidentEmail").item(0).getTextContent();
                                user_settings.setUserOverrideIncidentEmail(current_element);

                            } catch (Exception e) {
                            }
                            try {
                                current_element = eElement.getElementsByTagName("OverrideIncientEmail").item(0).getTextContent();
                                user_settings.setOverrideIncientEmail(current_element);

                            } catch (Exception e) {
                            }


                        } catch (Exception e) {
                        }

                    }
                } catch (Exception aE) {
                } finally {
                }

            }

        } catch (Exception e) {
            status = false;
        }


        return status;
    }

    void Read_Recipient_Photo() {
        String URL2 = root + "/TimeSheet.asmx?op=get_RecipientPhoto";
        String SOAP_ACTION2 = "https://tempuri.org/get_RecipientPhoto";
        String METHOD_NAME2 = "get_RecipientPhoto";
        byte[] buffer2 = null;
        ImageView imageView = (ImageView) findViewById(R.id.profile_image);

//        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_shift__detail__view);


        //      ImageView imageView2 =(ImageView)headerView.findViewById(R.id.profile_image_nav);


        settings = getSharedPreferences(PREFS_NAME, 0);
        try {
            String text = settings.getString(Personid, "Nothing");

            if (!text.equalsIgnoreCase("Nothing")) {
                buffer2 = Base64.decode(text, 0);
                Bitmap photo = BitmapFactory.decodeByteArray(buffer2, 0, buffer2.length);
                imageView.setImageBitmap(photo);
                //            imageView2.setImageBitmap(photo);
                return;
            }


        } catch (Exception ex) {
            // Toast.makeText(this, Personid+ "\n"+ ex.toString(), Toast.LENGTH_SHORT).show();
        }


        if (Server_Available == true) {

            SoapPrimitive result = null;
            try {

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL2);
                androidHttpTransport.debug = true;

                PropertyInfo pi2 = new PropertyInfo();
                pi2.setName("PersonId");
                pi2.setValue(getSecurityToken() + Personid + "");
                request.addProperty(pi2);


                try {
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.encodingStyle = SoapEnvelope.ENC;
                    envelope.setOutputSoapObject(request);
                    // Make the soap call.
                    androidHttpTransport.call(SOAP_ACTION2, envelope);
                    result = (SoapPrimitive) envelope.getResponse();
                    String text = result.toString();
                    //  Toast.makeText(this,Personid + "\n"+ text, Toast.LENGTH_SHORT).show();

                    try {
                        settings.edit().putString(Personid, text).commit();
                    } catch (Exception ex) {
                        Tost_Message(text.length() + "\n" + ex.toString());
                    }

                    if (!text.equalsIgnoreCase("Nothing")) {
                        buffer2 = Base64.decode(text, 0);
                        Bitmap photo = BitmapFactory.decodeByteArray(buffer2, 0, buffer2.length);
                        imageView.setImageBitmap(photo);
                        //   imageView2.setImageBitmap(photo);
                    }
                    //   scaleImage(imageView, 100); // in dp

                } catch (Exception ex) {   //Toast.makeText(this,  " 0001 \n"+ ex.toString(), Toast.LENGTH_SHORT).show();
                }

            } catch (Exception ex) {   //Toast.makeText(this,  "0002\n"+ ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }


    }

    boolean Get_User_Settings() {

        boolean status = false;
        String URL5 = root + "/TimeSheet.asmx?op=getUser_Settings";
        String SOAP_ACTION5 = "https://tempuri.org/getUser_Settings";
        String METHOD_NAME5 = "getUser_Settings";

        SoapPrimitive result = null;
        SoapObject obj = null;
        TextView lblMsg = (TextView) findViewById(R.id.txtAcknowledge);

        user_settings = new User_Settings();

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME5);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
        androidHttpTransport.debug = true;


        try {

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("UserName");
            pi2.setValue(getSecurityToken() + OperatorId);
            request.addProperty(pi2);


        } catch (Exception e) {
            Tost_Message(e.toString());
        }

        try {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            // envelope.encodingStyle = SoapEnvelope.ENC;
            envelope.setOutputSoapObject(request);
            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION5, envelope);
            obj = (SoapObject) envelope.getResponse();

            if (obj.getPropertyCount() <= 0) return false;

            try {
                result = (SoapPrimitive) obj.getProperty("AllowSetTime");
                user_settings.setAllowSetTime(result.toString());

                result = (SoapPrimitive) obj.getProperty("TMMode");
                user_settings.setTMMode(result.toString());

                result = (SoapPrimitive) obj.getProperty("MobileFutureLimit");
                user_settings.setMobileFutureLimit(result.toString());

                result = (SoapPrimitive) obj.getProperty("AllowPicUpload");
                user_settings.setAllowPicUpload(result.toString());

                result = (SoapPrimitive) obj.getProperty("Apply_Goe_Location_Setting");
                user_settings.setApply_Goe_Location_Setting(result.toString());
                Apply_Goe_Location_Setting = user_settings.getApply_Goe_Location_Setting();

                result = (SoapPrimitive) obj.getProperty("KMAgainstTravelOnly");
                user_settings.setKMAgainstTravelOnly(result.toString());

                result = (SoapPrimitive) obj.getProperty("mobilegeocodelimit");
                user_settings.setMobilegeocodelimit(result.toString());

                result = (SoapPrimitive) obj.getProperty("StaffLocationUpdateInterval");
                user_settings.setStaffLocationUpdateInterval(result.toString());

                result = (SoapPrimitive) obj.getProperty("AllowIncidentEntry");
                user_settings.setAllowIncidentEntry(result.toString());

                result = (SoapPrimitive) obj.getProperty("AllowTravelEntry");
                user_settings.setAllowTravelEntry(result.toString());

                result = (SoapPrimitive) obj.getProperty("AllowClientNoteEntry");
                user_settings.setAllowClientNoteEntry(result.toString());

                result = (SoapPrimitive) obj.getProperty("AllowRosterNoteEntry");
                user_settings.setAllowRosterNoteEntry(result.toString());

                result = (SoapPrimitive) obj.getProperty("StaffCode");
                user_settings.setStaffCode(result.toString());

                result = (SoapPrimitive) obj.getProperty("AllowRegisterSign");
                user_settings.setAllowRegisterSign(result.toString());

                result = (SoapPrimitive) obj.getProperty("ToDate");
                user_settings.setToDate(result.toString());

                result = (SoapPrimitive) obj.getProperty("Time");
                user_settings.setTime(result.toString());

                result = (SoapPrimitive) obj.getProperty("UserSessionLimit");
                UserSessionLimit = (result.toString());

                user_settings.setUserSessionLimit(UserSessionLimit);

                try {
                    result = (SoapPrimitive) obj.getProperty("Enable_Shift_End_Alarm");
                    user_settings.setEnable_Shift_End_Alarm(result.toString());

                } catch (Exception e) {
                }

            } catch (Exception e) {
            }
            try {
                result = (SoapPrimitive) obj.getProperty("ShowClientPhoneInApp");
                String ShowClientPhoneInApp = (result.toString());
                user_settings.setShowClientPhoneInApp(ShowClientPhoneInApp);

            } catch (Exception e) {
            }

            try {
                result = (SoapPrimitive) obj.getProperty("TA_TRAVELDEFAULT");
                TA_TRAVELDEFAULT = (result.toString());

                user_settings.setTA_TRAVELDEFAULT(TA_TRAVELDEFAULT);


            } catch (Exception e) {
            }

            try {
                result = (SoapPrimitive) obj.getProperty("CheckAlertInterval");
                user_settings.setCheckAlertInterval(result.toString());

            } catch (Exception e) {
            }

            try {
                result = (SoapPrimitive) obj.getProperty("AllowOPNote");
                user_settings.setAllowOPNote(result.toString());

            } catch (Exception e) {
            }


            try {
                result = (SoapPrimitive) obj.getProperty("AllowCaseNote");
                user_settings.setAllowCaseNote(result.toString());

            } catch (Exception e) {
            }


            try {
                result = (SoapPrimitive) obj.getProperty("AllowIncidentNote");
                user_settings.setAllowIncidentNote(result.toString());

            } catch (Exception e) {
            }

            try {
                result = (SoapPrimitive) obj.getProperty("AppUsesSMTP");
                user_settings.setAppUsesSMTP(result.toString());

            } catch (Exception e) {
            }


            // Toast.makeText(getApplicationContext(),user_settings.getToDate() , Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Tost_Message( e.toString());
        }


        return status;
    }

//========================================================================================================================
//=============================================Authentication with digital Signature======================================

    boolean Authenticate_Recipient_Signature() {

        boolean status = true;
        String URL5 = root + "/TimeSheet.asmx?op=Login_Receipient_Signature";
        String SOAP_ACTION5 = "https://tempuri.org/Login_Receipient_Signature";
        String METHOD_NAME5 = "Login_Receipient_Signature";

        SoapPrimitive result = null;
        SoapObject obj = null;
        TextView lblMsg = (TextView) findViewById(R.id.txtAcknowledge);

        try {
           Tost_Message( "Saving Signature");


            //  if (sign_buffer == null) sign_buffer = getByteArrayFromImage(mypath.getPath().toString());

            if (sign_buffer == null) {
                Tost_Message( "Incorrect Signature recording");
                return false;
            }

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME5);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
            androidHttpTransport.debug = true;


            try {

                PropertyInfo pi = new PropertyInfo();
                String base64String = Base64.encodeToString(sign_buffer, 0);
                pi.type = Base64.DEFAULT;
                pi.setName("Signature");
                pi.setValue(base64String);
                request.addProperty(pi);

                // Tost_Message("base64String=" + base64String.length() + "buffer="+buffer.length);

                PropertyInfo pi2 = new PropertyInfo();
                pi2.setName("AccountNo");
                pi2.setValue(getSecurityToken() + AccountNo);
                request.addProperty(pi2);

                PropertyInfo pi3 = new PropertyInfo();
                pi3.setName("RecordNo");
                pi3.setValue(RecordNo);
                request.addProperty(pi3);

            } catch (Exception e) {
                Tost_Message(e.toString());
            }

            try {
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.encodingStyle = SoapEnvelope.ENC;
                envelope.setOutputSoapObject(request);
                // Make the soap call.
                androidHttpTransport.call(SOAP_ACTION5, envelope);
                result = (SoapPrimitive) envelope.getResponse();


                if (result == null) {
                    status = true;
                    //txtAcknowledge.setText("Invalid User");
                    //Toast.makeText(getApplicationContext(), "No ignature Found, Invalid Signature", Toast.LENGTH_LONG).show();

                } else if (result.toString().equalsIgnoreCase("true")) {
                    status = true;
                }
            } catch (Exception e) {
                Tost_Message(e.toString());
            }


        } catch (Exception e) {
            Tost_Message( e.toString());
        }
        return status;
    }

    private String getTodaysDate() {

        final Calendar c = Calendar.getInstance();
        int todaysDate = (c.get(Calendar.YEAR) * 10000) +
                ((c.get(Calendar.MONTH) + 1) * 100) +
                (c.get(Calendar.DAY_OF_MONTH));
        Log.w("DATE:", String.valueOf(todaysDate));
        return (String.valueOf(todaysDate));

    }

    private String getCurrentTime() {

        final Calendar c = Calendar.getInstance();
        int currentTime = (c.get(Calendar.HOUR_OF_DAY) * 10000) +
                (c.get(Calendar.MINUTE) * 100) +
                (c.get(Calendar.SECOND));
        Log.w("TIME:", String.valueOf(currentTime));
        // return (String.valueOf(currentTime));
        return (c.get(Calendar.HOUR_OF_DAY)<10 ? "0" + c.get(Calendar.HOUR_OF_DAY) : c.get(Calendar.HOUR_OF_DAY)) + ":" +
                (c.get(Calendar.MINUTE)<10?"0"+c.get(Calendar.MINUTE) :c.get(Calendar.MINUTE));


    }


    private boolean prepareDirectory() {
        try {
            return makedirs();
        } catch (Exception e) {
            e.printStackTrace();
            Tost_Message("Could not initiate File System.. Is Sdcard mounted properly?");
            return false;
        }
    }

    private boolean makedirs() {
        File tempdir = new File(tempDir);
        if (!tempdir.exists())
            tempdir.mkdirs();

        if (tempdir.isDirectory()) {
            File[] files = tempdir.listFiles();
            for (File file : files) {
                if (!file.delete()) {
                    System.out.println("Failed to delete " + file);
                }
            }
        }
        return (tempdir.isDirectory());
    }

    public void get_sign_login(Context context, String JobType) {

        try {


            final String job_type = JobType;
            if (Server_Available == false) {

                Tost_Message("The feature of Signature Authentication does not work in offline mode");
                return;
            }

            /*Register_Signature(context);
            if (job_type.equalsIgnoreCase("set")) {

                set_time();
            }
            if (job_type.equalsIgnoreCase("start")) {
                Set_Job();
            }
            if (job_type.equalsIgnoreCase("end")) {
                End_Job();
            }

           if (true) return;
           */

            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.signature2);
            dialog.setTitle("Sign to Authenticate");
            dialog.setCanceledOnTouchOutside(false);
            TextView txt1 = dialog.findViewById(R.id.txt1);


            try {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity= Gravity.TOP | Gravity.RIGHT;
                dialog.getWindow().setAttributes(lp);


                txt1.setText("Recipient\n" + AccountNo);
                tempDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + getResources().getString(R.string.external_dir) + "/";
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File directory = cw.getDir(getResources().getString(R.string.external_dir), Context.MODE_PRIVATE);

                prepareDirectory();
                String uniqueId = getTodaysDate() + "_" + getCurrentTime() + "_" + Math.random();
                String current = uniqueId + ".jpg";
                mypath = new File(directory, current);
                mypath.deleteOnExit();

            } catch (Exception ex) {
                Tost_Message( ex.toString());
            }


            mContent = (LinearLayout) dialog.findViewById(R.id.linearLayout_sign2);
            mSignature = new Shift_Detail.Signature(Shift_Detail.this, null);
            mSignature.setBackgroundColor(Color.WHITE);
            mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, 700);

            ImageView imgBack;
            imgBack =  dialog.findViewById(R.id.imgBack2);
            mCancel = (Button) dialog.findViewById(R.id.cancel);
            mClear = (Button) dialog.findViewById(R.id.clear);
            mGetSign = (Button) dialog.findViewById(R.id.getsign);
            mGetSign.setEnabled(false);
            imgBack.setVisibility(View.VISIBLE);
            mView = mContent;
            mGetSign.setText("Save");
            mClear.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.v("log_tag", "Panel Cleared");
                    mSignature.clear();
                    mGetSign.setEnabled(false);
                }
            });

            mGetSign.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        //.v("log_tag", "Panel Saved");
                        boolean error = false; //captureSignature();
                        if (!error) {
                            mView.setDrawingCacheEnabled(true);
                            mSignature.save(mView);


                            Authenticate_Recipient_Signature();

                            if (true) {
                                if (job_type.equalsIgnoreCase("set")) {
                                    dialog.dismiss();
                                    set_time();
                                }
                                if (job_type.equalsIgnoreCase("start")) {
                                    dialog.dismiss();
                                    Set_Job();
                                }
                                if (job_type.equalsIgnoreCase("end")) {
                                    dialog.dismiss();
                                    End_Job();
                                }


                            } else {
                                Tost_Message( "Invalid Signature, No Operation performed");
                            }
                            Bundle b = new Bundle();
                            b.putString("status", "done");
                            Intent intent = new Intent();
                            intent.putExtras(b);
                            setResult(RESULT_OK, intent);
                            //finish();
                           /* try {

                                if(mypath.exists()) mypath.delete();
                                File file = new File(picturePath);
                                file.delete();

                            }catch(Exception ex){ }*/

                            dialog.dismiss();
                        }

                    } catch (Exception ex) {
                        Tost_Message( ex.toString());
                    }
                }
            });

            imgBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //Log.v("log_tag", "Panel Canceled");
                    try {
                        Bundle b = new Bundle();
                        b.putString("status", "cancel");
                        Intent intent = new Intent();
                        intent.putExtras(b);
                        setResult(RESULT_OK, intent);
                        // finish();

                        dialog.dismiss();
                    } catch (Exception ex) {
                        Tost_Message( ex.toString());
                    }

                }

            });

            dialog.show();
            // yourName = (EditText) findViewById(R.id.yourName);
        } catch (Exception ex) {
            Tost_Message( ex.toString());
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPlaceDetectionApi();
                }
                break;
        }
    }

    String resultStr = "";
    String best_result = "";

    private void callPlaceDetectionApi() throws SecurityException {
/*
        // Toast.makeText(Shift_Detail.this,"Checking Current Location", Toast.LENGTH_SHORT).show();
        final PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(mGoogleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                int i=0;
                resultStr="";
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    i=i+1;
                    resultStr=resultStr + "\n" + i + " - " + String.format("Place '%s' with " +
                                    "likelihood: %g",
                            placeLikelihood.getPlace().getName() + "\n " + placeLikelihood.getPlace().getAddress(),
                            placeLikelihood.getLikelihood());

                    if(i==1) {
                        best_result = String.format("Place '%s' with " +
                                        "likelihood: %g",
                                placeLikelihood.getPlace().getName() + "\n " + placeLikelihood.getPlace().getAddress(),
                                placeLikelihood.getLikelihood());

                    }

                  */
/*  Toast.makeText(Shift_Detail.this, String.format("Place '%s' with " +
                                    "likelihood: %g",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()), Toast.LENGTH_SHORT).show();
*//*


                }
                likelyPlaces.release();
            }
        });

*/

    }


    private Location_Address guessCurrentPlace() {
        final Location_Address loc = new Location_Address();
        loc.Address = "";
        loc.Latitude = 0;
        loc.Longitude = 0;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return loc;
        }
       /* PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);
        result.setResultCallback( new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult( PlaceLikelihoodBuffer likelyPlaces ) {

                PlaceLikelihood placeLikelihood = likelyPlaces.get( 0 );
                String content = "";
                if( placeLikelihood != null && placeLikelihood.getPlace() != null && !TextUtils.isEmpty( placeLikelihood.getPlace().getName() ) )
                    content = "Most likely place: " + placeLikelihood.getPlace().getName() + "\n" + placeLikelihood.getPlace().getAddress() +"\n";
                if( placeLikelihood != null )
                    content += "Percent change of being there: " + (int) ( placeLikelihood.getLikelihood() * 100 ) + "%";

                Current_Address=placeLikelihood.getPlace().getAddress().toString();

                loc.Latitude=placeLikelihood.getPlace().getLatLng().latitude;
                loc.Longitude=placeLikelihood.getPlace().getLatLng().longitude;
                loc.Address=Current_Address;

                likelyPlaces.release();
            }
        });*/
        //  Toast.makeText(getApplicationContext(), Current_Address, Toast.LENGTH_SHORT).show();
        //   Button btnOptions=(Button)findViewById(R.id.btnOptions);
        // btnOptions.setText(Current_Address);
        return loc;
    }

    void Save_Current_Location() {

        try {

            if (1==1) return;
            //     callPlaceDetectionApi();
            //   Location_Address loc = guessCurrentPlace();
           if (settings==null) settings = getSharedPreferences(PREFS_NAME, 0);
            settings.edit().putString("Address", loc.Address).commit();
            settings.edit().putString("Latitude", "" + loc.Latitude).commit();
            settings.edit().putString("Longitude", "" + loc.Longitude).commit();
        } catch (Exception ex) {
        }
    }

    Location_Address get_Current_Location() {
        Location_Address loc = new Location_Address();
        try {
            // callPlaceDetectionApi();
            updateUI();
            settings = getSharedPreferences(PREFS_NAME, 0);
            loc.Address = settings.getString("Address", "");
            loc.Latitude = Double.parseDouble(settings.getString("Latitude", "0"));
            loc.Longitude = Double.parseDouble(settings.getString("Longitude", "0"));

            // Toast.makeText(getApplicationContext(), loc.Latitude +"\n" +  loc.Longitude, Toast.LENGTH_SHORT).show();

            Current_Address = loc.Address;
            Latitude = "" + loc_address.Latitude;
            Longitude = "" + loc_address.Longitude;

            // Button btnOptions=(Button)findViewById(R.id.btnOptions);
            //btnOptions.setText(Current_Address);

        } catch (Exception ex) {
        }
        return loc;
    }

    public class Signature extends View {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private final Paint paint = new Paint();
        private final Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public Signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        Bitmap mBitmap = null;

        public void save(View v) {
            Log.v("log_tag", "Width: " + v.getWidth());
            Log.v("log_tag", "Height: " + v.getHeight());
            if (mBitmap == null) {
                mBitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(mBitmap);
            // buffer= bitmapToByteArray(mBitmap);
            scaleDown(mBitmap, 15, true);
            try {

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                v.draw(canvas);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

                sign_buffer = bos.toByteArray();

                // FileOutputStream mFileOutStream = new FileOutputStream(mypath);
                //picturePath=mypath.getPath().toString() ;

                //  mFileOutStream.flush();
                //  mFileOutStream.close();


                // String url = Images.Media.insertImage(getContentResolver(), mBitmap, "title", null);
                //Log.v("log_tag","url: " + url);


//                sign_buffer=bitmapToByteArray(BitmapFactory.decodeFile(picturePath));
                //In case you want to delete the file
//                boolean deleted = mypath.delete();
                //Log.v("log_tag","deleted: " + mypath.toString() + deleted);
                //If you want to convert the image to string use base64 converter


            } catch (Exception e) {
                Log.v("log_tag", e.toString());
            }
        }

        public byte[] bitmapToByteArray(Bitmap bm) {
            // Create the buffer with the correct size
            int iBytes = bm.getWidth() * bm.getHeight() * 4;
            ByteBuffer buffer = ByteBuffer.allocate(iBytes);

            // Log.e("DBG", buffer.remaining()+""); -- Returns a correct number based on dimensions
            // Copy to buffer and then into byte array
            bm.copyPixelsToBuffer(buffer);
            // Log.e("DBG", buffer.remaining()+""); -- Returns 0

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            // bm.compress(Bitmap.CompressFormat.WEBP, 90, stream);

            byte[] image = stream.toByteArray();

            return image;


        }

        public Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                boolean filter) {
            float ratio = Math.min(
                    (float) maxImageSize / realImage.getWidth(),
                    (float) maxImageSize / realImage.getHeight());
            int width = Math.round((float) ratio * realImage.getWidth());
            int height = Math.round((float) ratio * realImage.getHeight());

            Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                    height, filter);
            return newBitmap;
        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }

    public void get_Authenticate_Pin(Context context, String JobType) {
        final String job_type = JobType;
        try {

            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.pin);
            dialog.setTitle("Please Enter Pin");
            dialog.setCanceledOnTouchOutside(false);

            mCancel = (Button) dialog.findViewById(R.id.cancel);
            mClear = (Button) dialog.findViewById(R.id.clear);
            mGetSign = (Button) dialog.findViewById(R.id.getsign);
            // mGetSign.setEnabled(false);
            final EditText txtReceipient_pin = (EditText) dialog.findViewById(R.id.txtReceipient_pin);

            mClear.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //  Log.v("log_tag", "Panel Cleared");
                    txtReceipient_pin.setText("");
                    // mGetSign.setEnabled(false);
                }
            });

            mGetSign.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {

                        if (Authenticate_Recipient_Pin(txtReceipient_pin.getText().toString())) {
                            if (job_type.equalsIgnoreCase("set")) {
                                dialog.dismiss();
                                set_time();
                            }
                            if (job_type.equalsIgnoreCase("start")) {
                                dialog.dismiss();
                                Set_Job();
                            }
                            if (job_type.equalsIgnoreCase("end")) {
                                dialog.dismiss();
                                End_Job();
                            }


                        } else {
                            Tost_Message("Invalid Pin Code, No Operation performed");
                            //dialog.dismiss();
                        }

                    } catch (Exception ex) {
                        Tost_Message(ex.toString());
                        // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            mCancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    dialog.dismiss();

                }

            });

            dialog.show();
            // yourName = (EditText) findViewById(R.id.yourName);
        } catch (Exception ex) {
            Tost_Message( ex.toString());
        }

    }

    public byte[] getByteArrayFromImage(String filePath) throws IOException {

        File file = new File(filePath);
        System.out.println(file.exists() + "!!");

        FileInputStream fis = new FileInputStream(file);
        //create FileInputStream which obtains input bytes from a file in a file system
        //FileInputStream is meant for reading streams of raw bytes such as image data. For reading streams of characters, consider using FileReader.

        //InputStream in = resource.openStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                bos.write(buf, 0, readNum);
                //no doubt here is 0
                /*Writes len bytes from the specified byte array starting at offset
                off to this byte array output stream.*/
                System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
            Log.d("error", "error");
        }
        byte[] bytes = bos.toByteArray();
        return bytes;
    }

    boolean Authenticate_Recipient_Pin(String Pin) {


        return Recipient_PinCode.equalsIgnoreCase(Pin) && !Recipient_PinCode.equalsIgnoreCase("0");
    }

    public List<String> Get_Incident_Recipients(String Roster_Date, String ServiceSettings) {

        List<String> lst_recpients = new ArrayList<String>();


        try {

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();
            File fileDir = null;
            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = new File(fileDir, "traccs.xml");


            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Roster_Info");


                for (int tmp = 0; tmp < nList.getLength(); tmp++) {
                    try {

                        Node nNode = nList.item(tmp);
                        //  txtAcknowledge.setText(nNode.getTextContent());
                        //  txtAcknowledge.setText("\nCurrent Element :" + nNode.getNodeName());
                        String str_date = "";
                        String str_date2 = "";
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;

                            str_date = eElement.getElementsByTagName("Roster_Date").item(0).getTextContent();
                            String code = eElement.getElementsByTagName("Actual_Client_Code").item(0).getTextContent();
                            String service_settings = eElement.getElementsByTagName("servicesetting").item(0).getTextContent();


                            DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");

                            Date date2 = new Date(str_date);
                            str_date = dateFormat2.format(date2);
                            //str_date2= dateFormat2.format(Roster_Date);

                            if (str_date.equals(Roster_Date) && service_settings.equals(ServiceSettings))
                                lst_recpients.add(code);


                        }

                    } catch (Exception aE) {
                        Tost_Message( "Error in Incident getting Recipients ");
                    }


                }
            }

        } catch (Exception aE) {
            Tost_Message( "Error in Incident getting Recipients ");
        }

        return lst_recpients;
    }


    public List<String> Get_Incident_Types() {
        List<DataDomain> lst;
        List<String> lst_inscdt = new ArrayList<String>();

        try {

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();
            File fileDir = null;
            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = new File(fileDir, "Incident_Types.xml");


            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("DataDomain2");


                for (int tmp = 0; tmp < nList.getLength(); tmp++) {
                    try {

                        Node nNode = nList.item(tmp);
                        //  txtAcknowledge.setText(nNode.getTextContent());
                        //  txtAcknowledge.setText("\nCurrent Element :" + nNode.getNodeName());

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            String code = eElement.getElementsByTagName("Description").item(0).getTextContent();

                            lst_inscdt.add(code);


                        }

                    } catch (Exception aE) {
                        Tost_Message("Error in Incident getting Locations ");
                        return lst_inscdt;
                    }


                }
            }

        } catch (Exception aE) {
            Tost_Message( "Error in Incident getting Locations ");
        }

        return lst_inscdt;
    }

    public List<String> Get_Incident_Locations() {
        List<DataDomain> lst;
        List<String> lst_inscdt = new ArrayList<String>();

        try {

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();
            File fileDir = null;
            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = new File(fileDir, "Incident_Locations.xml");


            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("DataDomain2");


                for (int tmp = 0; tmp < nList.getLength(); tmp++) {
                    try {

                        Node nNode = nList.item(tmp);
                        //  txtAcknowledge.setText(nNode.getTextContent());
                        //  txtAcknowledge.setText("\nCurrent Element :" + nNode.getNodeName());

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            String code = eElement.getElementsByTagName("Description").item(0).getTextContent();

                            lst_inscdt.add(code);


                        }

                    } catch (Exception aE) {
                        Tost_Message("Error in Incident getting Locations ");
                    }


                }
            }

        } catch (Exception aE) {
            Tost_Message("Error in Incident getting Locations ");
        }

        return lst_inscdt;
    }

    public List<DataDomain> get_DataDomain(String Criteria, boolean b_NoBlank, String s_Default) {
        List<DataDomain> lst = null;
        lst = new ArrayList<DataDomain>();
        int indx = 0;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME6);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug = true;

            PropertyInfo pi1 = new PropertyInfo();
            pi1.setName("Criteria");
            pi1.setValue(getSecurityToken() + Criteria);
            request.addProperty(pi1);

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("b_NoBlank");
            pi2.setValue(b_NoBlank);
            request.addProperty(pi2);

            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("s_Default");
            pi3.setValue(s_Default);
            request.addProperty(pi3);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION6, envelope);

            SoapObject result = (SoapObject) envelope.getResponse();
            SoapObject result2;
            SoapPrimitive obj;
            int n = result.getPropertyCount();
            DataDomain dom;
            for (int j = 0; j < result.getPropertyCount(); j++) {
                result2 = (SoapObject) result.getProperty(j);
                dom = new DataDomain();
                for (int i = 0; i < result2.getPropertyCount(); i++) {
                    obj = (SoapPrimitive) result2.getProperty(i);

                    if (i == 0) dom.setDatadomain(obj.toString());
                    if (i == 1) dom.setDescription(obj.toString());
                    lst.add(dom);
                }
            }

        } catch (Exception e) {
            Tost_Message( e.toString());
        }
        return lst;

    }

    boolean check_valid_note(String value) {
        boolean valid = false;
        int i = 0;

        for (i = 0; i < value.length(); i++) {
            if (AllowableChar(value.charAt(i), 1, ";'$.\n\t ")) {
                valid = true;
            } else {
                valid = false;
                break;
            }
        }


        return valid;

    }

    public boolean AllowableChar(char s_Char, int i_BaseFilter, String s_ExtraChar) {
        boolean AllowableChar2 = false;
        // Function To Enforce Desired Character Limitations for Text box or string parsing
        // s_Char - Character to be checked
        // i_BaseFilter
        //    1 = Loose Alphanumeric                  %*!,-_()/\0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz
        //    2 = Tight Alphanumeric                  0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz
        //    3 = Strict Numeric                      0123456789.
        //    4 = Numeric With Dollar SIgn            $0123456789.
        //    5 = Numeric With Percentage Sign        %0123456789
        //    6 = Numeric + Percentage & Dollar Sign  %$0123456789
        // s_ExtraChar - any additional characters allowed to be entered
        // Allow backspace
        if ((int) s_Char == 8) {
            return true;
        }
        // TODO: Exit Function: Warning!!! Need to return the value
        // Toast.makeText(getApplicationContext(),"s_Char=" + s_Char , Toast.LENGTH_LONG).show();

        switch (i_BaseFilter) {
            case 1:
                if (((("%*!,-_()/\\0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz:@#&;?'$.\n\t\" " + s_ExtraChar).indexOf(s_Char) + 1)
                        > 0)) {
                    AllowableChar2 = true;
                }
                break;
            case 2:
                if (((("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" + s_ExtraChar).indexOf(s_Char) + 1)
                        > 0)) {
                    AllowableChar2 = true;
                }
                break;
            case 3:
                if (((("0123456789." + s_ExtraChar).indexOf(s_Char) + 1)
                        > 0)) {
                    AllowableChar2 = true;
                }
                break;
            case 4:
                if (((("$0123456789" + s_ExtraChar).indexOf(s_Char) + 1)
                        > 0)) {
                    AllowableChar2 = true;
                }
                break;
            case 5:
                if (((("%0123456789" + s_ExtraChar).indexOf(s_Char) + 1)
                        > 0)) {
                    AllowableChar2 = true;
                }
                break;
            case 6:
                if (((("%$0123456789" + s_ExtraChar).indexOf(s_Char) + 1)
                        > 0)) {
                    AllowableChar2 = true;
                }

                break;
        }

        return AllowableChar2;
    }

    public void ShowDialog_for_Set_Job(View v, String commandText) {
        try {

            final View final_view = v;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

            alertDialogBuilder.setTitle(" Job Processing Confirmation ");
            alertDialogBuilder
                    .setMessage(commandText)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing

                            Call_Set_Time_Job(final_view);
                            dialog.cancel();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
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

        } catch (Exception ex) {
        }
    }

    public void ShowDialog_for_Job_Start(View v, String commandText) {
        try {

            final View final_view = v;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

            alertDialogBuilder.setTitle(" Job Processing Confirmation ");
            
            alertDialogBuilder
                    .setMessage(commandText)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing

                            Call_Start_Job(final_view);
                            dialog.cancel();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
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

        } catch (Exception ex) {
        }
    }

    public void ShowDialog_for_Job_End_With_Task_check(View v, String commandText) {
        try {

            final View final_view = v;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

            alertDialogBuilder.setTitle("--------- JOB STATUS ----------");
            alertDialogBuilder
                    .setMessage(commandText)
                    .setCancelable(false)
                    .setPositiveButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            btn_start_job.setVisibility(View.VISIBLE);
                            btn_start_job.setEnabled(true);
                            dialog.cancel();
                            ShowDialog_Task(final_view);
                            //Perform_End_Job(final_view);


                        }
                    })
                    .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            Perform_End_Job(final_view);
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

    public void ShowDialog_Task(View v) {
        try {

            final View final_view = v;
            final Dialog dialog = new Dialog(v.getContext());
            dialog.setContentView(R.layout.task);
            dialog.setTitle("Tasks List");
            dialog.setCanceledOnTouchOutside(false);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setAttributes(lp);
            getTskList2();
            Task t = null;

            items = new Task[task_elements];
            for (int i = 0; i < task_elements; i++) {
                t = lst_task.get(i);
                //  tv.setText(tv.getText() + "\n" + t.getRecordNo() + " " + t.TaskCOmpleteget() + " " + t.getTaskDetail());
                items[i] = t;
            }
            if (task_elements > 0) {
                gridView = (ListView) dialog.findViewById(R.id.gridViewTask);

                //  gridView.setMinimumHeight(400);
                //  gridView.setVisibility(View.VISIBLE);
                gridView.setAdapter(new ListAdapter(this, items, root, Server_Available, OperatorId, Security_Token, settings));
                //  tv.setText("");
                //  tv.setVisibility(View.GONE);

                //setListViewHeightBasedOnChildren(gridView);
                // txtAcknowledge.setVisibility(View.VISIBLE);
            } else {
                ///  txtAcknowledge.setVisibility(View.INVISIBLE);
                gridView.setVisibility(View.INVISIBLE);
            }


            Button dialogButton = (Button) dialog.findViewById(R.id.btnExit);
            // button is clicked, close the custom dialog

            dialogButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    dialog.dismiss();
                    try {
                        if (Server_Available)
                            new Shift_Detail.MyAsyncClassUpdates().execute();

                    } catch (Exception ex) {
                    }
                }
            });
            /*Button dialogSave = (Button) dialog.findViewById(R.id.btnOK);
            // if button is clicked, close the custom dialog
            dialogSave.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {


                        dialog.dismiss();

                }
            });
           */
            dialog.show();
        } catch (Exception ex) {
        }
    }

    public void ShowDialog_for_End_Job(View v, String commandText) {
        try {

            final View final_view = v;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

            alertDialogBuilder.setTitle(" Job Processing Confirmation ");
            alertDialogBuilder
                    .setMessage(commandText)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing

                            try {
                                HasServiceNotes = settings.getBoolean("HasServiceNotes",false);
                            } catch (Exception ex) {
                            }
                            if (ForceShiftReport && !HasServiceNotes){

                                Tost_Message("Please Add Service Note for the Shift");
                                return;
                            }
                            call_End_Job(final_view);
                            dialog.dismiss();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
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

        } catch (Exception ex) {
        }
    }

    void Remove_session() {

        if (Server_Available == false) return;


        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = root + "/index.aspx?logout=1&user=" + OperatorId; //"http://www.google.com";

// Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //  textView.setText("Response is: "+ response.substring(0,500));
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

// Add the request to the RequestQueue.
            queue.add(stringRequest);
        } catch (Exception ex) {
            //Tost_Message(ex.toString());
        }


    }

    void Tost_Message(String message) {
        final Context context = getApplicationContext();
        final String msg = message;
        Handler handler = new Handler(context.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void get_GroupAlerts() {

        if (!Server_Available) {
            get_GroupAlerts2();
            return;
        }

        String URL6 = root + "/Timesheet.asmx?op=getAlertGroups";
        String SOAP_ACTION6 = "https://tempuri.org/getAlertGroups";
        String METHOD_NAME6 = "getAlertGroups";

        TextView txtAlertGroup_Label = (TextView) findViewById(R.id.txtGroupAlerts);
        //  txtGroupAlerts.setTypeface(null, Typeface.BOLD);

        GroupAlerts grp = null;
        int indx = 0;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME6);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug = true;

            PropertyInfo pi1 = new PropertyInfo();
            pi1.setName("PersonID");
            pi1.setValue(getSecurityToken() + Personid);
            request.addProperty(pi1);

            //  Toast.makeText(getApplicationContext(),Personid , Toast.LENGTH_LONG).show();

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION6, envelope);

            SoapObject result = (SoapObject) envelope.getResponse();
            SoapObject result2;
            SoapPrimitive obj;
            int n = result.getPropertyCount();
            DataDomain dom;
            lst_groups = new ArrayList<GroupAlerts>();

            for (int j = 0; j < result.getPropertyCount(); j++) {
                result2 = (SoapObject) result.getProperty(j);
                grp = new GroupAlerts();

                obj = (SoapPrimitive) result2.getProperty("RecordNo");
                grp.setRecordNo(obj.toString());

                obj = (SoapPrimitive) result2.getProperty("Group");
                grp.setGroup(obj.toString());

                obj = (SoapPrimitive) result2.getProperty("Notes");
                grp.setNotes(obj.toString());

                lst_groups.add(grp);

            }
            lst_groups.add(grp);

          /*  ListView lst_view_GroupAlerts =  findViewById(R.id.gridAlerts);
            if (lst_groups.size() > 0) {
                txtAlertGroup_Label.setText("Alert Group");
                txtAlertGroup_Label.setVisibility(View.VISIBLE);
                lst_view_GroupAlerts.setVisibility(View.VISIBLE);

              *//*  Group_Alert_Adapter_Recyler mAdapter = new Group_Alert_Adapter_Recyler(this, lst_groups );
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                lst_view_GroupAlerts.setLayoutManager(mLayoutManager);
                if(flagDecoration) {
                    lst_view_GroupAlerts.setItemAnimator(new DefaultItemAnimator());
                    lst_view_GroupAlerts.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
                    flagDecoration=false;
                }*//*
            } else {
                txtAlertGroup_Label.setVisibility(View.GONE);
                lst_view_GroupAlerts.setVisibility(View.GONE);
            }
*/

        } catch (Exception e) {
            Tost_Message( e.toString());
        }


    }

    public void get_GroupAlerts2() {

        ArrayList<GroupAlerts> lst_groups = null;
        GroupAlerts grp = null;
        GroupAlerts grp2;
        try {

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();
            File fileDir = null;
            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = new File(fileDir, "group_alerts.xml");


            int indx = 0;

            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Alert_Group");



                if (nList == null) return;

                int n = nList.getLength();
                lst_groups = new ArrayList<GroupAlerts>();

                for (int tmp = 0; tmp < nList.getLength(); tmp++) {
                    try {

                        String s_PersonId = "";
                        Node nNode = nList.item(tmp);
                        //  txtAcknowledge.setText(nNode.getTextContent());
                        //  txtAcknowledge.setText("\nCurrent Element :" + nNode.getNodeName());

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            s_PersonId = eElement.getElementsByTagName("PersonId").item(0).getTextContent();

                            // txtAcknowledge.setText(txtAcknowledge.getText() + "\n" + str_date + " = " + selected_date + " " +  selection.getText().equals(code));

                            if (s_PersonId.equals(Personid)) {
                                grp = new GroupAlerts();

                                 grp.setRecordNo(eElement.getElementsByTagName("RecordNo").item(0).getTextContent());
                                grp.setGroup(eElement.getElementsByTagName("Group").item(0).getTextContent());
                                grp.setNotes(eElement.getElementsByTagName("Notes").item(0).getTextContent());
                                grp.setPersonId(eElement.getElementsByTagName("PersonId").item(0).getTextContent());
                                lst_groups.add(grp);


                            }
                        } else {
                           // txtAcknowledge.setText("Group Alerts - File does not exist");
                        }
                    } catch (Exception aE) {
                       // txtAcknowledge.setText("Error in Group Alerts " + aE.toString());
                    }


                }



            } else {
                // txtAcknowledge.setText("Group Alerts Xml file not found");
            }
        } catch (Exception aE) {
            //  ((TextView) findViewById(R.id.TextDate)).setText("get_GroupAlerts2" + aE.toString());
        }


        TextView txtGroupAlerts = (TextView) findViewById(R.id.txtGroupAlerts);
        //  txtGroupAlerts.setTypeface(null, Typeface.BOLD);
        View rl_groupAlert = (View) findViewById(R.id.cardView6);
        rl_groupAlert.setVisibility(View.GONE);


        try {
            if (lst_groups == null) {

                return;
            }
            if (lst_groups.size() <= 0) {

                return;
            }

          //  ListView lst_view_GroupAlerts =  findViewById(R.id.gridAlerts);
            ExpandableHeightListView listView = new ExpandableHeightListView(this);
            ExpandableHeightListView   listview=(ExpandableHeightListView)findViewById(R.id.expandableHeightListView);



            if (lst_groups.size() > 0) {
                rl_groupAlert.setVisibility(View.VISIBLE);
                txtGroupAlerts.setText("Alerts Group");
                txtGroupAlerts.setVisibility(View.VISIBLE);
                listView.setVisibility(View.VISIBLE);
                Group_Alert_Adapter listAdapter = new Group_Alert_Adapter(this, lst_groups);
              //  lst_view_GroupAlerts.setAdapter(listAdapter);
               // setListViewHeightBasedOnChildren2(lst_view_GroupAlerts, lst_groups);

                listview.setAdapter(listAdapter); //set your adaper
                listview.setExpanded(true);

             //   setListViewHeightBasedOnChildren_2(lst_view_GroupAlerts);
               /* Group_Alert_Adapter_Recyler mAdapter = new Group_Alert_Adapter_Recyler(this, lst_groups );
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                lst_view_GroupAlerts.setLayoutManager(mLayoutManager);
                if(flagDecoration) {
                    lst_view_GroupAlerts.setItemAnimator(new DefaultItemAnimator());
                    lst_view_GroupAlerts.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
                    flagDecoration=false;
                }
               // setListViewHeightBasedOnChildren_2(lst_view_GroupAlerts, lst_groups);*/
            } else {
                txtGroupAlerts.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            Tost_Message( e.toString());
        }


    }
    public static void setListViewHeightBasedOnChildren_2(RecyclerView listView, ArrayList<GroupAlerts> lst) {

        try {

            int h=0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            int totalHeight = 0;
            int l=0;
            View view = null;
            for (int i = 0; i < lst.size(); i++) {

                totalHeight +=  lst.get(i).getNotes().length() ;
                l=i;
            }
          /*  int l_siz=lst.get(l).getNotes().length();
            if (lst.size()>1) {
                if (l_siz < 100)
                    totalHeight += 100;
                else if (l_siz < 150)
                    totalHeight += l_siz;
                else if (l_siz < 500)
                    totalHeight += 50;
                else if (l_siz > 1000)
                    totalHeight -= l_siz / 3;
            }*/
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();



        }catch(Exception ex){}
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = (ListAdapter) listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    public static void setListViewHeightBasedOnChildren2(ListView listView, ArrayList<GroupAlerts> lst) {
        try {

            Group_Alert_Adapter listAdapter = (Group_Alert_Adapter) listView.getAdapter();
            if (listAdapter == null)
                return;

            int h=0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            int totalHeight = 0;
            int l=0;
            View view = null;
            for (int i = 0; i < listAdapter.getCount(); i++) {

                totalHeight +=  lst.get(i).getNotes().length() ;
                l=i;
            }
            int l_siz=lst.get(l).getNotes().length();
          if (listAdapter.getCount()>1) {
              if (l_siz < 100)
                  totalHeight += 100;
              else if (l_siz < 150)
                  totalHeight += l_siz;
              else if (l_siz < 500)
                  totalHeight += 50;
              else if (l_siz > 1000)
                  totalHeight -= l_siz / 3;
          }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()))+50;
            listView.setLayoutParams(params);
            listView.requestLayout();



        }catch(Exception ex){}

    }

//Send Email

    public void send_email_alert(String msg, String subject) {


        email_msg = subject + msg;
        Email_Subject = subject;
        //Toast.makeText(getApplicationContext(), email_seting.getSMTPServer() + "\n" + email_seting.getSMTPUser() + "\nPassword :" + email_seting.getSMTPPassword(), Toast.LENGTH_LONG).show();

        //Toast.makeText(getApplicationContext(), "Sending Email from SMTP Server :" + email_seting.getSMTPServer(), Toast.LENGTH_LONG).show();


        //Toast.makeText(getApplicationContext(),"Cordinator_Email : " + Cordinator_Email, Toast.LENGTH_LONG).show();
        //  Toast.makeText(getApplicationContext(),Cordinator_Email + "\n" + email_msg , Toast.LENGTH_LONG).show();

        try {
            if ( !Cordinator_Email.equals("") && settings.getString("AppUsesSMTP","false").equalsIgnoreCase("true"))
            {
                try{
                    //Cordinator_Email="arshadblouch81@yahoo.com";
                    //  email.sendMail(email_seting.getFromDisplayName(), email_msg, email_seting.getFromEmail(),To_Emails);
                    email.sendMail(Email_Subject, email_msg, email_seting.getFromEmail(),Cordinator_Email);

                } catch (Exception e) {
                    //Toast.makeText(getApplicationContext(),"SMTP Server not working" , Toast.LENGTH_LONG).show();
                    send_local_email(subject,msg);
                }

            }else {
                send_local_email(subject,msg);}

        } catch (Exception ex) {send_local_email(subject,msg);}



    }

    void send_local_email(String subject, String email_msg){

        try{

            String[] TO = {Cordinator_Email};
            Uri uri = Uri.parse("mailto:" + Cordinator_Email)
                    .buildUpon()
                    .appendQueryParameter("subject", subject)
                    .appendQueryParameter("body", email_msg)
                    .build();
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            try{
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }catch (Exception ex) {}

            /*
                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, email_msg);
                intent.setData(Uri.parse("mailto:"+Cordinator_Email)); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);*/


        }catch (Exception ex) {
            // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public boolean Get_SMTP_Server_Setting() {

        boolean status = false;

        try {
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File filein = null;
            File fileDir = null;
            String state = Environment.getExternalStorageState();


            if (Environment.MEDIA_MOUNTED.equals(state)) {

                //check sdcard permission
                fileDir = new File(froot.getAbsolutePath() + "/.server/");

                filein = new File(fileDir, "server_setting.txt");
                if (filein.exists()) {

                    if (filein.length() <= 0) return false;
                    try {
                        email_seting = new Email_Settings();

                        FileReader fileReader = new FileReader(filein);
                        BufferedReader buf = new BufferedReader(fileReader);

                        String value = buf.readLine();
                        email_seting.setSMTPServer(value);

                        value = buf.readLine();
                        email_seting.setSMTPUser(value);

                        value = buf.readLine();
                        email_seting.setSMTPPassword(value);

                        value = buf.readLine();
                        email_seting.setSMTP_Port(value);

                        value = buf.readLine();
                        email_seting.setFromEmail(value);

                        value = buf.readLine();
                        email_seting.setFromDisplayName("  TRACCS Client Note Added for : " + AccountNo);

                        status = true;

                    } catch (Exception e) {
                    }


                } else {
                    try {
                        filein.createNewFile();
                    } catch (Exception ex) {
                    }
                    status = false;
                }
            }

        } catch (Exception e) {
            status = false;
        }

        return status;
    }

    public void check_SMTP_Server_Setting() {


        String smtp = settings.getString("SMTP", "Nothing");

        if (smtp.equalsIgnoreCase("Nothing")) {
            try {
                Intent intnt = new Intent(this, SMTP_Settings.class);

                intnt.putExtra("root", root);
                intnt.putExtra("Server_Available", Server_Available);
                intnt.putExtra("OperatorId", OperatorId);
                intnt.putExtra("Security_Token", Security_Token);
                startActivity(intnt);

            } catch (Exception ex) {
                Tost_Message( ex.getMessage());
            }
        }

    }

    public void get_Email_Settings() {

        check_SMTP_Server_Setting();

        String smtp = settings.getString("SMTP", "Nothing");
        if (smtp.equalsIgnoreCase("True")) {
            if (Get_SMTP_Server_Setting()) return;
        }

        if (true) return;

        String SOAP_ACTION55 = "https://tempuri.org/GetEmailSettings";
        String METHOD_NAME55 = "GetEmailSettings";
        String URL55 = root + "/TimeSheet.asmx?op=GetEmailSettings";

        if (Server_Available == false) {
            return;
        }

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME55);
            HttpTransportSE androidHttpTransport2 = new HttpTransportSE(URL55);
            androidHttpTransport2.debug = true;

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("Fontra");
            pi2.setValue(getSecurityToken() + "99");
            request.addProperty(pi2);



            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            SoapObject result = null;
            SoapPrimitive obj;
            // Make the soap call.
            androidHttpTransport2.call(SOAP_ACTION55, envelope);
            result = (SoapObject) envelope.getResponse();

            email_seting = new Email_Settings();

            for (int i = 0; i < result.getPropertyCount(); i++) {
                try {
                    obj = (SoapPrimitive) result.getProperty("POP3Server");
                    email_seting.setPOP3Server(obj.toString());

                    obj = (SoapPrimitive) result.getProperty("POP3User");
                    email_seting.setPOP3User(obj.toString());

                    obj = (SoapPrimitive) result.getProperty("POP3Password");
                    email_seting.setPOP3Password(obj.toString());

                    obj = (SoapPrimitive) result.getProperty("SMTPServer");
                    email_seting.setSMTPServer(obj.toString());

                    obj = (SoapPrimitive) result.getProperty("SMTPUser");
                    email_seting.setSMTPUser(obj.toString());

                    obj = (SoapPrimitive) result.getProperty("SMTPPassword");
                    email_seting.setSMTPPassword(obj.toString());

                    obj = (SoapPrimitive) result.getProperty("FromEmail");
                    email_seting.setFromEmail(obj.toString());

                    obj = (SoapPrimitive) result.getProperty("FromDisplayName");
                    email_seting.setFromDisplayName(obj.toString() + " TRACCS Client Note Added for : " + AccountNo);

                    obj = (SoapPrimitive) result.getProperty("SMTP_Port");
                    email_seting.setSMTP_Port(obj.toString());


                } catch (Exception ex) {
                }

            }
        } catch (Exception ex) {
            Tost_Message("Operation not done due to some server error\n" + ex.toString());

        }
        try {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            if (email_seting.getSMTPServer() == null || email_seting.getSMTPServer().equals("")) {

                email_seting.setSMTPServer(settings.getString("SMTP_Server", "mail.adamas.net.au"));
                email_seting.setSMTP_Port(settings.getString("SMTP_Port", "143"));
                email_seting.setSMTPUser(settings.getString("SMTP_User", "timwatts@adamas.net.au"));
                email_seting.setSMTPPassword(settings.getString("SMTP_Password", "samada2002"));
                email_seting.setFromDisplayName(settings.getString("Email_Subject", " TRACCS Client Note Added for : " + AccountNo));
                email_seting.setFromEmail(settings.getString("From_Address", "support@adamas.net.au"));


            }
        } catch (Exception ex) {
        }


    }
    public void set_main_menu(Context view) {
        final Dialog dialog = new Dialog(view, R.style.CustomDialog);
        dialog.setContentView(R.layout.shift_detail_menu);
        //dialog.setTitle("My Dashboard");
        dialog.setCanceledOnTouchOutside(true);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                menu_displayed=false;
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity= Gravity.TOP | Gravity.RIGHT;
        dialog.getWindow().setAttributes(lp);

        Window window = dialog.getWindow();
        if (window != null) {
            window.getAttributes().x = 0;//(int) TextDate.getTop();
            window.getAttributes().y = 0;
            window.setBackgroundDrawableResource(R.color.transparent);

            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;


            params.horizontalMargin = 0;
            params.verticalMargin = 0;
           // params.gravity = Gravity.RIGHT;
            params.gravity= Gravity.TOP | Gravity.RIGHT;
            params.dimAmount = 0;
            params.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(params);
        }


        TextView txtRecipient_menu = dialog.findViewById(R.id.txtRecipient_menu);
        TextView txtRecipient_address_menu = dialog.findViewById(R.id.txtRecipient_address_menu);

        String add = "";
        try{
        String[] a = Client_address.split("<br>");
        add=a[1];
    }catch(Exception ex){}
        txtRecipient_menu.setText (HtmlCompat.fromHtml(FirstName + " " + lastName + "<br><small>" + add + "</small>",0 ));
        //txtRecipient_address_menu.setText(address);
        txtRecipient_address_menu.setVisibility(View.INVISIBLE);
        TextView txtStartJob = dialog.findViewById(R.id.txtStartJob);
        if (btn_start_job.isEnabled() && btn_start_job.getVisibility() == View.VISIBLE
                && btn_start_job.getText().toString().startsWith("START") &&  btn_ack.getVisibility() != View.VISIBLE){
            txtStartJob.setEnabled(true);
            txtStartJob.setVisibility(View.VISIBLE);
        }else{
            txtStartJob.setVisibility(View.GONE);
        }
        txtStartJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    if (btn_start_job.getText().toString().startsWith("START")) {
                        ShowDialog_for_Job_Start(v, "Are you sure you want to Start Job");
                    } else if (btn_start_job.getText().toString().startsWith("END")) {

                        ShowDialog_for_End_Job(v, "Are you sure you want to End Job\nEnsure you add km travel if needed \n Ensure you added shift report");
                       // ShowDialog_for_End_Job(v, " \n Do you need to add Travel Claim \n \n Ensure you have added a Client Note \n \n Are you sure you want to End Job");

                    } else {
                        ShowDialog_for_Job_Start(v, "Are you sure you want to cancel Start Job");

                        //ShowDialog_for_End_Job(v, "Are you sure you want to cancel End Job");
                    }

                    dialog.dismiss();
                    menu_displayed=false;

                }catch(Exception ex){}
            }
        });
        txtStartJob.setVisibility(View.GONE);  // set always invisible to hide option

        TextView txtEndJob = dialog.findViewById(R.id.txtEndJob);
        if (btn_start_job.isEnabled() && Shift_Status.equalsIgnoreCase("Started")
        &&  btn_start_job.getVisibility() == View.VISIBLE &&  btn_ack.getVisibility() != View.VISIBLE){
            txtEndJob.setEnabled(true);
            txtEndJob.setVisibility(View.VISIBLE);

        }else{
            txtEndJob.setVisibility(View.GONE);
        }
        txtEndJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if (btn_start_job.getText().toString().startsWith("START")) {
                        ShowDialog_for_Job_Start(v, "Are you sure you want to Start Job");
                    } else if (btn_start_job.getText().toString().startsWith("END")) {

                        ShowDialog_for_End_Job(v, "Are you sure you want to End Job\nEnsure you add km travel if needed \n Ensure you added shift report");
                       // ShowDialog_for_End_Job(v, " \n Do you need to add Travel Claim \n \n Ensure you have added a Client Note \n \n Are you sure you want to End Job");
                    } else {
                        ShowDialog_for_Job_Start(v, "Are you sure you want to cancel Start Job");

                        //ShowDialog_for_End_Job(v, "Are you sure you want to cancel End Job");
                    }

                    dialog.dismiss();
                    menu_displayed=false;
                }catch(Exception ex){}
            }
        });
                txtEndJob.setVisibility(View.GONE); // set always invisible to hide option

        TextView txtSetTime = dialog.findViewById(R.id.txtSetTime);
        txtSetTime.setEnabled(false);
        txtSetTime.setEnabled(user_settings.getAllowSetTime().equalsIgnoreCase("true") && View_only == false);
        if (btn_time.isEnabled()  && btn_start_job.getText().toString().startsWith("START")
                &&  btn_time.getVisibility() == View.VISIBLE &&  btn_ack.getVisibility() != View.VISIBLE){
            txtSetTime.setVisibility(View.VISIBLE);

        }else{
            txtSetTime.setVisibility(View.GONE);
        }

        txtSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    ShowDialog_for_Set_Job(btn_time, "Are You Sure You want to set Job Timing");
                    dialog.dismiss();
                    menu_displayed=false;
                }catch(Exception ex){}
            }
        });
        txtSetTime.setVisibility(View.GONE);// set always invisible to hide option

        TextView txtUploadPhoto = dialog.findViewById(R.id.txtUploadPhoto);
        txtUploadPhoto.setVisibility(View.GONE);
        if (user_settings.getAllowPicUpload().equalsIgnoreCase("true")  && View_only==false) {
            txtUploadPhoto.setVisibility(View.VISIBLE);
        }
        txtUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    call_pic_upload(v);
                    dialog.dismiss();
                    menu_displayed=false;
                }catch(Exception ex){}
            }
        });
        TextView txtTravelClaim = dialog.findViewById(R.id.txtTravelClaim);
        txtTravelClaim.setVisibility(View.GONE);
        if (user_settings.getAllowTravelEntry().equalsIgnoreCase("true")) {
            if (!View_only ) {
                txtTravelClaim.setVisibility(View.VISIBLE);
            }else if (View_only && user_settings.get_RestrictTravelSameDay().equalsIgnoreCase("false")) {
                txtTravelClaim.setVisibility(View.VISIBLE);
            }
        }

        if (Actual_Client_Code.equalsIgnoreCase("!MULTIPLE") ) txtTravelClaim.setVisibility(View.GONE);

        txtTravelClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Call_Travel(v);
                    dialog.dismiss();
                    menu_displayed=false;

                }catch(Exception ex){}
            }
        });

        TextView txtClientNote = dialog.findViewById(R.id.txtClientNote);
        //View_only==true
        if ( Actual_Client_Code.equalsIgnoreCase("!MULTIPLE") ) txtClientNote.setVisibility(View.GONE);

        txtClientNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    Set_Client_Note(v);
                    dialog.dismiss();
                    menu_displayed=false;

                }catch(Exception ex){}
            }
        });
        TextView txtServiceNote = dialog.findViewById(R.id.txtServiceNote);
       // if (Actual_Client_Code.equalsIgnoreCase("!INTERNAL") || View_only==true) txtServiceNote.setVisibility(View.GONE);
       // if ( View_only==true) txtServiceNote.setVisibility(View.GONE);
       // if (Actual_Client_Code.equalsIgnoreCase("!INTERNAL") || Actual_Client_Code.equalsIgnoreCase("!MULTIPLE") ) txtServiceNote.setVisibility(View.GONE);

        if (UseServiceNoteAsShiftReport.equalsIgnoreCase("false") || UseServiceNoteAsShiftReport.equalsIgnoreCase("0"))
        {
            txtServiceNote.setText("Service Note");
        }

        if (user_settings.getAllowRosterNoteEntry().equalsIgnoreCase("false")) txtServiceNote.setVisibility(View.GONE);
        txtServiceNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    try {
                        Add_roster_Note(v.getContext());
                    } catch (Exception ex) {
                    }
                    dialog.dismiss();
                    menu_displayed=false;

                }catch(Exception ex){}
            }
        });


        TextView txtRecordIncident = dialog.findViewById(R.id.txtRecordIncident);
       // if (Actual_Client_Code.equalsIgnoreCase("!INTERNAL") || View_only==true) txtRecordIncident.setVisibility(View.GONE);
        if ( Actual_Client_Code.equalsIgnoreCase("!MULTIPLE") ) txtRecordIncident.setVisibility(View.GONE);
        if (user_settings.getAllowIncidentEntry().equalsIgnoreCase("false")) txtRecordIncident.setVisibility(View.GONE);

        txtRecordIncident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Set_Incident(v.getContext());

                }catch(Exception ex){}
                dialog.dismiss();
                menu_displayed=false;
            }
        });
        TextView txtSignature = dialog.findViewById(R.id.txtSignature);
        if ( View_only==true) txtSignature.setVisibility(View.GONE);
        if (user_settings.getAllowRegisterSign().equalsIgnoreCase("false")) txtSignature.setVisibility(View.GONE);

        txtSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    Register_Signature(v.getContext());

                    dialog.dismiss();
                    menu_displayed=false;
                }catch(Exception ex){}
            }
        });
        TextView txtViewPlanGoals = dialog.findViewById(R.id.txtViewPlanGoals);
        if (user_settings.getAllowViewGoalPlans().equalsIgnoreCase("false") || user_settings.getAllowViewGoalPlans().equalsIgnoreCase("0")) txtViewPlanGoals.setVisibility(View.GONE);
        txtViewPlanGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Load_Plan();
                    dialog.dismiss();
                    menu_displayed=false;
                }catch(Exception ex){}
            }
        });
        TextView txtViewClientDocuments = dialog.findViewById(R.id.txtViewClientDocuments);
        if (user_settings.get_ViewClientDocuments().equalsIgnoreCase("false") || user_settings.get_ViewClientDocuments().equalsIgnoreCase("0")  ) txtViewClientDocuments.setVisibility(View.GONE);

        txtViewClientDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    View_Documents();
                    dialog.dismiss();
                    menu_displayed=false;
                }catch(Exception ex){}
            }
        });
        ImageView ImageExit=(ImageView) dialog.findViewById(R.id.ImageExit);
        ImageExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                menu_displayed=false;
            }
        });

        //TextView txtRecipient_menu = dialog.findViewById(R.id.txtRecipient_menu);
        txtRecipient_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    dialog.dismiss();
                    menu_displayed=false;

                }catch(Exception ex){}
            }
        });
        dialog.show();

    }
    private void setNeverSleepPolicy() {
        try {
            ContentResolver cr = getApplicationContext().getContentResolver();
            int set = android.provider.Settings.System.WIFI_SLEEP_POLICY_NEVER;
            android.provider.Settings.System.putInt(cr, android.provider.Settings.System.WIFI_SLEEP_POLICY, set);
            WifiApManager wifi = new WifiApManager(getApplicationContext());

            WifiConfiguration config = wifi.getWifiApConfiguration();

            wifi.setWifiApState(config, true);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
//            WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
//                    "MyWakelockTag");
//            wakeLock.acquire();

        } catch (Exception ex) {
        }
    }

    public void load_Roster_data() {

        String URL = root + "/TimeSheet.asmx?op=getMonth_Rosters";
        String SOAP_ACTION = "https://tempuri.org/getMonth_Rosters";
        String METHOD_NAME = "getMonth_Rosters";

        String buff = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        Calendar myCalendar = Calendar.getInstance();
        Date date = myCalendar.getTime();
        String strDate = dateFormat.format(date);
        String[] strDate2 = strDate.split("/");

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        FileOutputStream fileos = null;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug = true;

            PropertyInfo pi = new PropertyInfo();
            pi.setName("client_code");
            pi.setValue(getSecurityToken() + StaffCode);
            request.addProperty(pi);

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("MonthNo");
            pi2.setValue(strDate2[1]);
            request.addProperty(pi2);

            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("YearNo");
            pi3.setValue(strDate2[0]);
            request.addProperty(pi3);


            PropertyInfo pi4 = new PropertyInfo();
            pi4.setName("client");
            pi4.setValue(false);
            request.addProperty(pi4);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            // SoapObject obj=(SoapObject) envelope.getResponse();
            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "traccs.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();

                } catch (IOException e) {
                    txtAcknowledge.setText("bb " + e.toString());
                }


                try {
                    fileos = new FileOutputStream(newxmlfile);
                    fileos.write(xml.getBytes());

                    serializer.setOutput(fileos, "UTF-8");
                    MainActivity.form_resumed = false;
                } catch (Exception ex) {
                    txtAcknowledge.setText("cc: " + ex.toString());
                }

                fileos.close();
            }
        } catch (Exception ex) {
        } finally {

        }

    }


    class MyAsyncClass extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Shift_Detail.this);
            pDialog.setMessage("Please wait while Processing  ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
// Add subject, Body, your mail Id, and receiver mail Id.
                // sender.sendMail("Subject", " body", "from Mail", "to mail");

                Job_Status2(RecordNo);
                // Get_User_Settings2();


            } catch (Exception ex) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //  set_Permission();
            pDialog.cancel();

        }
    }

    class MyAsyncClass2 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Shift_Detail.this);
            pDialog.setMessage("Please wait while Processing  ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
// Add subject, Body, your mail Id, and receiver mail Id.
                // sender.sendMail("Subject", " body", "from Mail", "to mail");

                getRoster_Recipient();
                // get_Email_Settings();

                if (Server_Available) {
                    //getLocation(getApplicationContext());

                    getLocation_and_do_Job_Setting(getApplicationContext());

                }

            } catch (Exception ex) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();
            //show_Alerts_Grid();

        }
    }



    String getEmail_Address() {

        try {
            Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            final PackageManager pm = this.getPackageManager();
            final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
            ResolveInfo best = null;
            for (final ResolveInfo info : matches)
                //if (info.activityInfo.packageName.endsWith(".gm") ||
                //info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;

            if (best != null) {
                //intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                packageName = best.activityInfo.packageName;
                email_name = best.activityInfo.name;
            }

            AccountManager accManager = AccountManager.get(this);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return "false";
            }
            Account[] acc = accManager.getAccounts();
            int accCount = acc.length;

            for (int i = 0; i < accCount; i++) {
                //Do your task here...
                possibleEmail = acc[0].name;

            }



        } catch (Exception e) {

        }

        return possibleEmail;
    }

    class MyAsyncClass3 extends AsyncTask<Void, Void, Void> {

        private final Activity mActivity;

        public MyAsyncClass3(final Activity mActivity) {
            this.mActivity = mActivity;
        }

        LoadingDialog pDialog;
        boolean local = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Shift_Detail.this);
            pDialog.setMessage("Please wait while Processing  Email Alert....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {


            // Cordinator_Email="arshadblouch81@yahoo.com";

            // subject = "  TRACCS Client Note Added for : " + AccountNo;

            try {


                if (!Cordinator_Email.equals("") & user_settings.getAppUsesSMTP().equalsIgnoreCase("true")) {

                    try {

                        //  email.sendMail(email_seting.getFromDisplayName(), email_msg, email_seting.getFromEmail(),To_Emails);
                        email.sendMail(Email_Subject, email_msg, email_seting.getFromEmail(), Cordinator_Email);

                    } catch (Exception e) {
                        //   send_local_email();
                        local = true;
                    }
                } else {
                    // send_local_email();

                    local = true;

                }


            } catch (Exception ex) {

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();
            if (local == true)
                send_local_email(mActivity);

            if (!tmp_cor_email.equals("")) {
                Cordinator_Email = tmp_cor_email;
                tmp_cor_email = "";
            }

        }
    }

    class MyAsyncClass_Remove_Session extends AsyncTask<Void, Void, Void> {


        LoadingDialog pDialog;
        boolean local = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Shift_Detail.this);
            pDialog.setMessage("User Session expired, logging out session....");
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... mApi) {

            try {

                Remove_session();


            } catch (Exception ex) {

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();
            finish();
            System.exit(0);

        }
    }

    void send_local_email(Context mActivity) {

        String subject = Email_Subject;
        try {


            Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
//            intent.setType("text/plain");
//            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
//            intent.putExtra(Intent.EXTRA_TEXT, email_msg);
//            intent.setData(Uri.parse("mailto:"+Cordinator_Email)); // or just "mailto:" for blank
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
            // startActivity(intent);

//            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{Cordinator_Email});
//            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
//            intent.putExtra(Intent.EXTRA_TEXT, email_msg);
//
//            //intent.setType("message/rfc822");
//            intent.setType("*/*");
//            startActivity(Intent.createChooser(intent, "Select Email Sending App :"));

            String[] TO = {Cordinator_Email};
            Uri uri = Uri.parse("mailto:" + Cordinator_Email)
                    .buildUpon()
                    .appendQueryParameter("subject", subject)
                    .appendQueryParameter("body", email_msg)
                    .build();
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));

            if (!tmp_cor_email.equals("")) {
                Cordinator_Email = tmp_cor_email;
                tmp_cor_email = "";
            }

        } catch (Exception ex) {

        }

    }

    class MyAsyncClass4 extends AsyncTask<Void, Void, String> {

        LoadingDialog pDialog;
        boolean result_status = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Shift_Detail.this);
            pDialog.setMessage("Please wait while Processing ....");
            pDialog.show();

        }

        @Override
        protected String doInBackground(Void... mApi) {

            try {

                result_status = Start_Job();

            } catch (Exception ex) {

            }
            return "" + result_status;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {

                if (result.equalsIgnoreCase("true"))
                    if (btn.getTag().toString().charAt(0) == 'S') {


                        try {
                            //  set_Job_Status_In_Local_file(RecordNo + ",Job Cancelled," + currentDateString );
                        } catch (Exception ex) {
                        }
                        ((TextView) findViewById(R.id.txtAcknowledge)).setText("Job cancelled successfully");
                        txtAcknowledge.setText(txtAcknowledge.getText() + " (" + RecordNo + " )");
                        btn_End_job.setEnabled(false);
                        btn_End_job.setVisibility(View.GONE);
                        xml.Update_Roster_Node(RecordNo, "Started", "0");
                    } else {

                        try {
                            // set_Job_Status_In_Local_file(RecordNo + ",JOB STARTED," + currentDateString );
                        } catch (Exception ex) {
                        }
                        ((TextView) findViewById(R.id.txtAcknowledge)).setText("Job started successfully");
                        txtAcknowledge.setText(txtAcknowledge.getText() + " (" + RecordNo + " )");
                        btn_End_job.setEnabled(true);
                        btn_End_job.setVisibility(View.VISIBLE);
                        xml.Update_Roster_Node(RecordNo, "Started", "1");

                        //gridView = (ListView) findViewById(R.id.gridViewTask);
                        //  gridView.setEnabled(false);

                        if (user_settings.getHIdeCancelButton().equalsIgnoreCase("true")) {
                            btn_start_job.setVisibility(View.GONE);
                        }

                    }
                else
                    ((TextView) findViewById(R.id.txtAcknowledge)).setText("Operation not done due to some server problem ");

            } catch (Exception ex) {
                ((TextView) findViewById(R.id.txtAcknowledge)).setText("Operation not done due to some server error\n" + ex.toString());
            }

            pDialog.cancel();

        }
    }

    class MyAsyncClass5 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Shift_Detail.this);
            pDialog.setMessage("Processing Sleep Over Jobs ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
// Add subject, Body, your mail Id, and receiver mail Id.
                // sender.sendMail("Subject", " body", "from Mail", "to mail");
                if (Server_Available) {

                    //	Process_SleepOver();
                    load_Roster_data();

                }

            } catch (Exception ex) {

            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            pDialog.cancel();
            finish();

        }
    }

    class MyAsyncClass6 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Shift_Detail.this);
            pDialog.setMessage("Please wait while loading data ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
// Add subject, Body, your mail Id, and receiver mail Id.
                // sender.sendMail("Subject", " body", "from Mail", "to mail");
                if (Server_Available) {
                    load_Roster_data();
                }

            } catch (Exception ex) {

            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();
            call_finish();

        }
    }

    class MyAsyncClass7 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Shift_Detail.this);
            pDialog.setMessage("Please wait while getting Location ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
// Add subject, Body, your mail Id, and receiver mail Id.
                // sender.sendMail("Subject", " body", "from Mail", "to mail");
                //show_Map();

                //getLocation(getApplicationContext());
                Save_Current_Location();
                // End_Job222();
            } catch (Exception ex) {

            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();
            //finish();

        }
    }

    class MyAsyncClass8 extends AsyncTask<Void, Void, String> {

        LoadingDialog pDialog;
        boolean result_status2 = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Shift_Detail.this);
            pDialog.setMessage("Please wait while processing Job ....");
            pDialog.show();

        }

        @Override
        protected String doInBackground(Void... mApi) {
            try {
// Add subject, Body, your mail Id, and receiver mail Id.
                // sender.sendMail("Subject", " body", "from Mail", "to mail");
                //show_Map();
                result_status2 = Process_end_Job();

                //  result_status2=Start_Job();
            } catch (Exception ex) {

            }
            return "" + result_status2;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            btn = (Button) findViewById(R.id.btnstartjob);
            if (result.equalsIgnoreCase("true")) {
                if (btn.getTag().toString().charAt(0) == 'E') {

                    try {
                        //  set_Job_Status_In_Local_file(RecordNo + ",Job Cancel End," + currentDateString );
                    } catch (Exception ex) {
                    }
                    ((TextView) findViewById(R.id.txtAcknowledge)).setText("Job Cancel End done");
                    txtAcknowledge.setText(txtAcknowledge.getText() + " (" + RecordNo + " )");
                    btn_start_job.setEnabled(true);
                    Job_Message = "Job Cancel End done";
                    xml.Update_Roster_Node(RecordNo, "Started", "1");
                    xml.Update_Roster_Node(RecordNo, "Completed", "0");
                    // xml.remove_Roster_Element(RecordNo);

                } else {

                    try {
                        //set_Job_Status_In_Local_file(RecordNo + ",JOB COMPLETED," + currentDateString );
                    } catch (Exception ex) {
                    }
                    ((TextView) findViewById(R.id.txtAcknowledge)).setText("Job completed successfully");
                    txtAcknowledge.setText(txtAcknowledge.getText() + " (" + RecordNo + " )");
                    btn_start_job.setEnabled(false);
                    btn_start_job.setVisibility(View.GONE);
                    btn_time.setVisibility(View.GONE);
                    Job_Message = "Job completed successfully";
                    Job_end_done = true;
                    xml.Update_Roster_Node(RecordNo, "Started", "0");
                    xml.Update_Roster_Node(RecordNo, "Completed", RecordNo);
                    MainActivity.form_resumed=false;
                    MainActivity.enforce_refresh=true;



                    // xml.add_Roster_Element(RecordNo);
                    //gridView = (ListView) findViewById(R.id.gridViewTask);
                    // gridView.setEnabled(false);
                }
            } else {
                Job_Message = "Operation not done due to some server error ";
                ((TextView) findViewById(R.id.txtAcknowledge)).setText("SS: Operation not done due to some server error " + result_status2);
            }

            pDialog.cancel();


        }
    }

    class MyAsyncClass_Set_Time extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Shift_Detail.this);
            pDialog.setMessage("Please wait while processing shift ....");
//            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
// Add subject, Body, your mail Id, and receiver mail Id.
                // sender.sendMail("Subject", " body", "from Mail", "to mail");
                Set_Job_Time();
              //  xml.Update_Roster_Node(RecordNo, "Started", "1");
                xml.set_Job_Completed_Status(RecordNo);


            } catch (Exception ex) {

            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();



//            btn_time=findViewById(R.id.btnsetTime);
  //          btn_start_job=findViewById(R.id.btnstartjob);
        }
    }

    public void End_Job() {
        long RecNo = 0;
        btn_End_job = findViewById(R.id.btnstartjob);
        // get_Current_Location();

        try {
            // RecNo= Job_already_started2(RecordNo);
            RecNo = Long.parseLong(settings.getString("Started_Job_No", "0"));
        } catch (Exception ex) {
        }
        if (RecNo == 0) {
            ((TextView) findViewById(R.id.txtAcknowledge)).setText("No Job has been started");
            return;
        }


        if (Process_Sleep_Over) {

            Process_SleepOver();
            return;
        }

        if (Server_Available == false) {
            End_Job2();
            return;
        }

        try {
            //  new MyAsyncClass8().execute();
            //  return;
        } catch (Exception ex) {
        }
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL2);
            androidHttpTransport.debug = true;

            PropertyInfo pi = new PropertyInfo();
            pi.setName("Recordno");

            pi.setValue(getSecurityToken() + RecordNo);
            request.addProperty(pi);
            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("cancel");
            pi3.setValue(false);
            request.addProperty(pi3);

            /*btn=(Button)findViewById(R.id.btnstartjob);
            btn_start_job=(Button)findViewById(R.id.btnstartjob);

            if (btn.getTag().toString().charAt(0) =='E')
            {
                pi3.setValue(false);
                btn.setText("Cancel\nEnd");
                btn.setTag("Cancel\nEnd");


                btn_start_job.setSelected(false);
                btn_start_job.setBackgroundResource(R.drawable.button);
                btn_start_job.setText("START JOB" + " " + QR);
                btn_start_job.setTag("START JOB" );
            }
            else
            {
                pi3.setValue(true);
                btn.setText("END\nJOB");
                btn.setTag("END\nJOB");


                btn_start_job.setSelected(true);
                btn_start_job.setBackgroundResource(R.drawable.btn_main_pressed);
                btn_start_job.setText("CANCEL START" + " " + QR);
                btn_start_job.setTag("CANCEL START");


            }*/


            PropertyInfo pi4 = new PropertyInfo();
            pi4.setName("Latitude");
            pi4.setValue(Latitude);
            request.addProperty(pi4);

            PropertyInfo pi5 = new PropertyInfo();
            pi5.setName("Longitude");
            pi5.setValue(Longitude);
            request.addProperty(pi5);

            PropertyInfo pi6 = new PropertyInfo();
            pi6.setName("Location");
            pi6.setValue(Current_Address);
            request.addProperty(pi6);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            DateFormat tFormat = new SimpleDateFormat("HH:mm");


            Date date = new Date();
            String strDate = dateFormat.format(date);
            String strTime = tFormat.format(date);
            strDate = strDate + " " + strTime;

            strDate = "";

            PropertyInfo pi7 = new PropertyInfo();
            pi7.setName("timeStamp");
            pi7.setValue(strDate);
            request.addProperty(pi7);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION2, envelope);

            SoapPrimitive result = (SoapPrimitive) envelope.getResponse();

            //Date date= new Date();
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
            String currentDateString = dateFormat2.format(date);
            if (result == null) {
                ((TextView) findViewById(R.id.txtAcknowledge)).setText("End2: Operation not done due to some server error ");
                return;
            }

            if (Boolean.valueOf(result.toString()) == true) {
                MainActivity.form_resumed = false;

                try {
                    set_Job_Status_In_Local_file(RecordNo + ",JOB COMPLETED," + currentDateString);
                } catch (Exception ex) {
                }

                ((TextView) findViewById(R.id.txtAcknowledge)).setText("Job completed successfully");
                txtAcknowledge.setText(txtAcknowledge.getText() + " (" + RecordNo + " )");
                //btn_start_job.setVisibility(View.GONE);
                Job_Message = "Job completed successfully";
                Job_end_done = true;
                // xml.Update_Roster_Node(RecordNo,"Started","0");
                xml.set_Job_Completed_Status(RecordNo);
                // xml.add_Roster_Element(RecordNo);
                //gridView = (ListView) findViewById(R.id.gridViewTask);
                // gridView.setEnabled(false);

                btn_start_job.setVisibility(View.GONE);
                btn_time.setVisibility(View.GONE);
                MainActivity.form_resumed = false;


                if (xml.check_MultipleShift(RecordNo,"").equalsIgnoreCase("Multiple") && !MinorGroup.equalsIgnoreCase("BREAK")) {
                    xml.Process_MultipleShift(RecordNo, AccountNo, RosterDate, StartTime, EndTime,"");
                    MainActivity.enforce_refresh=true;
                }
            }


        } catch (Exception ex) {
            ((TextView) findViewById(R.id.txtAcknowledge)).setText("End2: Operation not done due to some server error ");
        }

    }

    class MyAsyncClassUpdates extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Shift_Detail.this);
            // pDialog.setMessage("Please wait while updating server updates  ....");
            // pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
// Add subject, Body, your mail Id, and receiver mail Id.
                // sender.sendMail("Subject", " body", "from Mail", "to mail");
                Upload_Updates up = new Upload_Updates(root, OperatorId, Security_Token,context);
                up.Upload_Updates_on_server();
            } catch (Exception ex) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();
            try {

                settings.edit().putBoolean("Update", false).commit();

                // finish();
            } catch (Exception ex) {
            }


        }
    }

    class MyAsyncClass4_Recipient_locally extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Shift_Detail.this);
            //pDialog.setMessage("Please wait while loading Recipient data ....");
            //pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
// Add subject, Body, your mail Id, and receiver mail Id.
                try {
                    getRoster_Recipient2(RecordNo);

                } catch (Exception ex) {
                    ErroString = ex.toString();
                }
            } catch (Exception ex) {
                // ErroString=ex.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();


        }
    }


    class MyAsyncClass4_Recipient extends AsyncTask<Void, Void, Void> {

        LoadingDialog loading_Dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading_Recipient++;
            loading_Dialog = new LoadingDialog(Shift_Detail.this);
            loading_Dialog.setMessage("Please wait while loading Recipient data ....");
            loading_Dialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
// Add subject, Body, your mail Id, and receiver mail Id.
                try {
                    getRoster_Recipient();


                } catch (Exception ex) {
                    ErroString = ex.toString();
                }
            } catch (Exception ex) {
                // ErroString=ex.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            loading_Dialog.cancel();
            try {
                getRoster_Recipient2(RecordNo);
            } catch (Exception ex) {
            }

            try {
                getRecipient_Address(Actual_Client_Code);
            } catch (Exception ex) {
            }



        }
    }

    class MyAsyncClass5_Recipient extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading_Recipient++;

        }
        @Override
        protected Void doInBackground(Void... mApi) {
            try {
// Add subject, Body, your mail Id, and receiver mail Id.
                try {
                    bulk_data.load_Receipient_Detail(MobileFutureLimit);
                } catch (Exception ex) {
                    ErroString = ex.toString();
                }
            } catch (Exception ex) {
                // ErroString=ex.toString();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try {
                getRecipient_Address(Actual_Client_Code);
            } catch (Exception ex) {
            }
        }
    }

    class MyAsyncClass4_Recipient_Photo extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Shift_Detail.this);
            pDialog.setMessage("Please wait while loading Recipient Photo ....");
            //  pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
// Add subject, Body, your mail Id, and receiver mail Id.
                try {
                    Read_Recipient_Photo();

                } catch (Exception ex) {
                    ErroString = ex.toString();
                }
            } catch (Exception ex) {
                // ErroString=ex.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //  pDialog.cancel();



        }
    }

    public long calculateDays(String startDate, String endDate) {
        Date sDate = new Date(startDate);
        Date eDate = new Date(endDate);
        Calendar cal3 = Calendar.getInstance();
        cal3.setTime(sDate);
        Calendar cal4 = Calendar.getInstance();
        cal4.setTime(eDate);
        return daysBetween(cal3, cal4);
    }

    public long daysBetween(Calendar startDate, Calendar endDate) {
        Calendar date = (Calendar) startDate.clone();
        long daysBetween = 0;
        while (date.before(endDate)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    public int calculateDifference(Date a, Date b) {
        int tempDifference = 0;
        int difference = 0;
        Calendar earlier = Calendar.getInstance();
        Calendar later = Calendar.getInstance();

        if (a.compareTo(b) < 0) {
            earlier.setTime(a);
            later.setTime(b);
        } else {
            earlier.setTime(b);
            later.setTime(a);
        }

        while (earlier.get(Calendar.YEAR) != later.get(Calendar.YEAR)) {
            tempDifference = 365 * (later.get(Calendar.YEAR) - earlier.get(Calendar.YEAR));
            difference += tempDifference;

            earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
        }

        if (earlier.get(Calendar.DAY_OF_YEAR) != later.get(Calendar.DAY_OF_YEAR)) {
            tempDifference = later.get(Calendar.DAY_OF_YEAR) - earlier.get(Calendar.DAY_OF_YEAR);
            difference += tempDifference;

            earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
        }


        return difference;
    }

    /////================================== Getting Location Code======================

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onStart() {

        super.onStart();
        if (!isOnline(this)) return;
        //Log.d(TAG, "onStart fired ..............");

        try {
            mGoogleApiClient.connect();


        } catch (Exception ex) {

            Tost_Message(ex.toString());

        }
    }


    @Override
    public void onStop() {

        super.onStop();
      //Log.d(TAG, "onStop fired ..............");
        try {
            mGoogleApiClient.disconnect();
        } catch (Exception ex) {
        }

      //  Exit_Shift();

        // Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());

    }

    private boolean isGooglePlayServicesAvailable() {
        try {
            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
            if (ConnectionResult.SUCCESS == status) {
                return true;
            } else {
                GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
                return false;
            }
        } catch (Exception ex) {

            Tost_Message(ex.toString());
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (!Server_Available) return;

        //Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (!Server_Available) return;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (shouldAskPermissions()) {
                askPermissions();
            }
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

       /* mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(100); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            last_lat = (mLastLocation.getLatitude());
            last_lon = (mLastLocation.getLongitude());

        }*/
        updateUI();

        // Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Log.d(TAG, "Connection failed: " + connectionResult.toString());

        Tost_Message("Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        //  Log.d(TAG, "Firing onLocationChanged..............................................");
        if (!Server_Available) return;

        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI() {
        if (!Server_Available) return;
        // Log.d(TAG, "UI update initiated .............");
        Geocoder coder = null;
        List<Address> add = null;
        List<Address> add2 = null;

        try {
            coder = new Geocoder(this, Locale.getDefault());
            add = coder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 5);
        } catch (Exception ex) {
        }

        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());
            try {
                //Current_Address = add.get(0).getAddressLine(0) + ", " + add.get(0).getAddressLine(1) + ", " + add.get(0).getAddressLine(2);

                String Line1 = add.get(0).getAddressLine(0);
                String Line2 = add.get(0).getAddressLine(1);
                String Line3 = add.get(0).getAddressLine(2);

                // Current_Address = add.get(0).getAddressLine(0) + ", " + add.get(0).getAddressLine(1) + ", " + add.get(0).getAddressLine(2);

                if (Line1 != null && !Line1.equalsIgnoreCase("NULL"))
                    Current_Address = Line1;

                if (Line2 != null && !Line2.equalsIgnoreCase("NULL"))
                    Current_Address = Current_Address + ", " + Line2;

                if (Line3 != null && !Line3.equalsIgnoreCase("NULL"))
                    Current_Address = Current_Address + ", " + Line3;

                loc = new Location_Address();
                loc.Latitude = mCurrentLocation.getLatitude();
                loc.Longitude = mCurrentLocation.getLongitude();
                loc.Address = Current_Address;


            } catch (Exception ex) {
            }

          //  Save_Current_Location();


            if (Settings_Done) return;

            String curr_Date = "";
            try {
                Calendar c = Calendar.getInstance();
                Date dt = c.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                curr_Date = sdf.format(dt);




            } catch (Exception ex) {
            }


            //   if (!user_settings.getToDate().equals(RosterDate) ) {
            if (!curr_Date.equals(RosterDate)) {
                btn_ack.setVisibility(View.INVISIBLE);
                btn_start_job.setVisibility(View.GONE);
                txtAcknowledge.setVisibility(View.GONE);
              //  btn_End_job.setEnabled(false);
                // btn_End_job.setVisibility(View.GONE);
                 btn_time.setEnabled(false);
                btn_time.setVisibility(View.GONE);
                Settings_Done = true;
                // long days = calculateDays(RosterDate, user_settings.getToDate());
                long days = calculateDays(RosterDate, curr_Date);

                long result = xml.get_Job_Started(RecordNo, RosterDate);
                View_only = true;


                if (result == 1 && (days == 1) && (EndTime.equals("00:00") || EndTime.equals("24:00"))) {
                    btn_start_job.setVisibility(View.VISIBLE);
                    View_only = false;
                    Process_Sleep_Over = true;

                } else if (result == 1 && days == 1) {
                    btn_start_job.setVisibility(View.VISIBLE);
                    txtAcknowledge.setVisibility(View.VISIBLE);
                    Process_Sleep_Over = true;
                    View_only = false;
                }
            } else if (loction_flag) {

                getLocation_and_do_Job_Setting(getApplicationContext());
                loction_flag = false;

            }

        } else {
            // Log.d(TAG, "location is null ...............");

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!Server_Available) return;
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        if (!Server_Available) return;
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        // Log.d(TAG, "Location update stopped .......................");

    }

    @Override
    public void onResume() {
        super.onResume();
      /*  if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (shouldAskPermissions()) {
                askPermissions();
            }
            //return;
        }


        if(!isOnline(this)) return;
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();

        }
*/


    }


    public String getRecipient_Address(String ClientCode) {

        String Address_value = "";


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
                if (nList == null) return null;

                String AccountNo = "";
                String current_element = "Total elements :" + nList.getLength();
                //  txtAcknowledge.setText("RecordNo : " + Current_roster.getRecordNo());

                for (int temp = 0; temp < nList.getLength(); temp++) {

                    try {
                        Node nNode = nList.item(temp);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            current_element = eElement.getNodeValue();
                            AccountNo = eElement.getElementsByTagName("AccountNo").item(0).getTextContent();


                            if (AccountNo.equals(ClientCode)) {
                                if (Actual_Client_Code.equalsIgnoreCase("!MULTIPLE") || Actual_Client_Code.equalsIgnoreCase("!INTERNAL"))
                                    Personid = Service_Setting;
                                else
                                    Personid = eElement.getElementsByTagName("Personid").item(0).getTextContent();

                                address = eElement.getElementsByTagName("Addresses").item(0).getTextContent();

                                if (AccountNo.equals("!MULTIPLE") && roster_type == 11 && !address.equals("-")) {
                                    Address_value = "-";
                                    Simple_Address = "";
                                    ImageView imageView2=findViewById(R.id.imageView2);
                                    imageView2.setVisibility(View.GONE);
                                } else {
                                    Address_value = eElement.getElementsByTagName("Addresses").item(0).getTextContent();
                                    Simple_Address = eElement.getElementsByTagName("Simple_Address").item(0).getTextContent();
                                }

                                Phone = eElement.getElementsByTagName("Phone_Numbers").item(0).getTextContent();
                                PinCode = eElement.getElementsByTagName("PinCode").item(0).getTextContent();
                                /*
                                roster_alerts=eElement.getElementsByTagName("Roster_Alerts").item(0).getTextContent();
                                runsheet_alerts=eElement.getElementsByTagName("Runsheet_Alerts").item(0).getTextContent();
                                Notes=eElement.getElementsByTagName("Notes").item(0).getTextContent();
                              */


                                break;
                            }


                        }


                    } catch (Exception aE) {
                        //  txtAcknowledge.setText("Error 2" + current_element + " " +  aE.toString());
                    }

                }
            }


        } catch (Exception aE) {
            //((TextView) findViewById(R.id.TextDate)).setText("Error 3 : " + aE.getMessage());
        }

        return Address_value;

    }
}
