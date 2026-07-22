package adamas.traccs.mta_20_06;

        import android.annotation.SuppressLint;
        import android.app.Activity;
        import android.app.ActivityManager;
        import android.app.AlertDialog;
        import android.app.DatePickerDialog;
        import android.app.Dialog;
        import android.app.Notification;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.app.ProgressDialog;
        import android.content.ContentResolver;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.content.SharedPreferences;
        import android.content.pm.PackageManager;
        import android.content.res.Configuration;
        import android.graphics.Color;
        import android.location.LocationManager;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.net.Uri;
        import android.net.wifi.WifiConfiguration;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.Environment;
        import android.os.Handler;
        import android.os.PowerManager;
        import android.os.ResultReceiver;
        import android.os.StrictMode;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.ActionBar;
        import androidx.appcompat.app.AppCompatActivity;
        import android.text.format.Time;
        import android.util.DisplayMetrics;
        import android.util.Log;
        import android.util.Xml;
        import android.view.ContextMenu;
        import android.view.ContextThemeWrapper;
        import android.view.Display;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.MenuInflater;
        import android.view.MotionEvent;
        import android.view.View;

        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.ViewGroup;
        import android.view.ViewParent;
        import android.view.Window;
        import android.view.WindowManager;

        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.BaseAdapter;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.PopupMenu;
        import android.widget.RelativeLayout;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;


        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.material.navigation.NavigationView;
        import com.sun.mail.pop3.POP3SSLStore;
/*
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;*/

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

        import java.io.BufferedReader;
        import java.io.BufferedWriter;
        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.FileReader;
        import java.io.FileWriter;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        //import java.time.Year;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.List;
        import java.util.Locale;
        import java.util.Timer;
        import java.util.TimerTask;

        import javax.xml.parsers.DocumentBuilder;
        import javax.xml.parsers.DocumentBuilderFactory;
        import javax.xml.parsers.ParserConfigurationException;

        import androidx.core.app.ActivityCompat;
        import androidx.core.content.PermissionChecker;
        import androidx.core.graphics.Insets;
        import androidx.core.view.ViewCompat;
        import androidx.core.view.WindowInsetsCompat;
        import androidx.recyclerview.widget.DefaultItemAnimator;
        import androidx.recyclerview.widget.DividerItemDecoration;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import adamas.traccs.mta_20_06.msalandroidapp.MainActivity2Factor;
        import email.BackgroundMail;
        import io.grpc.Server;
        import timesheet.NetworkStateReceiver;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, NetworkStateReceiver.NetworkStateReceiverListener
{

    public String root = "https://58.162.142.150/timesheet"; //http://10.0.2.2:49884
    private final String NAMESPACE = "https://tempuri.org/";

    private String URL = root + "/TimeSheet.asmx?op=getRoster_Datewise";
    private final String SOAP_ACTION = "https://tempuri.org/getRoster_Datewise";
    private final String METHOD_NAME = "getRoster_Datewise";

    private String URL2 = root + "/TimeSheet.asmx?op=getRecipients";
    private final String SOAP_ACTION2 = "https://tempuri.org/getRecipients";
    private final String METHOD_NAME2 = "getRecipients";

    private String URL3 = root + "/TimeSheet.asmx?op=getRecipient_Detail";
    private final String SOAP_ACTION3 = "https://tempuri.org/getRecipient_Detail";
    private final String METHOD_NAME3 = "getRecipient_Detail";

    private String URL4 = root + "/TimeSheet.asmx?op=getDevice_Active_Reminders";
    private final String SOAP_ACTION4 = "https://tempuri.org/getDevice_Active_Reminders";
    private final String METHOD_NAME4 = "getDevice_Active_Reminders";


    String[] permissions = {
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_NETWORK_STATE",
            "android.permission.ACCESS_WIFI_STATE",
            "android.permission.GET_ACCOUNTS",
            "com.google.android.providers.gsf.permission.READ_GSERVICES",
            "adamas.traccs.mta.permission.MAPS_RECEIVE"
    };


    private final int next_rep = 0;
    private boolean menu_displayed=false;
    private boolean dashboard_displayed=false;

    private static final int GOOGLE_API_CLIENT_ID = 182;
    private GoogleApiClient mGoogleApiClient;
    private static final int PERMISSION_REQUEST_CODE = 100;


    static boolean form_resumed = false;
    static boolean booking_loaded=false;
    boolean flagDecoration=true;
    boolean View_only=false;
    String urlString = root + "/traccs_error_log.txt";
    String ErroString = "";
    float bandwidth = 0;
    int temp = 0;
    TextView rosterview;
    TextView selection;
    TextView txtRepEvent;
    TextView txtRecipient;

    Button btnDeliver;
    Button btnShowAll;
    boolean Show_all_roster = true;
    boolean Process_Sleep_Over = false;
    //    public DefaultHttpClient httpsClient;
    User_Settings user_settings = null;
    SpeedTest spt;
    int speed_limit=512;
    boolean InfoOnly=false;
    ArrayList<String> lst_leave = null;
    boolean Booking_view = false;
    boolean Accept_Booking = false;
    Boolean display_alert = true;
    Boolean login = false;
    private ArrayList<Roster_Info> rosters = null;
    static int total_item = 96;
    String[] items = new String[total_item];
    boolean exiting_form = false;
    String MTAAutRefreshOnLogin = "0";
    private List<DeviceReminders> Reminders = null;
    DeviceReminders deviceReminder;
    Location_Address loc = null;

    int[] Accounts = new int[total_item];
    int[] durationImages = new int[total_item];
    int[] started = new int[total_item];
    int[] completed = new int[total_item];
    static int block_divider = 3;
    static int cell_size = 15;
    private List<String> recipients;
    private Roster_Info Current_roster = null;
    String DATE_FORMAT_1="EEE, MMM d, yyyy";

    private boolean Group_shift=false;
    XmlData xml ;

    TextView textviewOrientation;
    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private final Handler progressBarHandler = new Handler();


    public static int last_indx = 0;
    boolean buton_clicked = false;
    int clicked = 0;
    int clicked_indx = 0;
    RecyclerView listView;
    File froot = null;
    Context context;
    TextView textMsg;
    Boolean Server_Available = true;
    String StaffCode = "";
    String UserName = "";
    String Timesheet = "0";
    String Max_RecordNo = "0";
    boolean already_alarm_shown=false;
    int height = 600;
    int wwidth = 400;
    boolean showDate = true;
    String TMMode = "0";
    String RecipientDocFolder = "";
    String KMAgainstTravelOnly = "false";
    String AllowSetTime = "";
    String TAMode = "";
    String MobileFutureLimit = "0";
    String AllowPicUpload = "0";
    String UserId = "";
    String MobileIncident = "false";
    String mobilegeocodelimit = "1";
    String OperatorId = "";
    String Security_Token = "";
    String autologin = "false";
    long Alarm_RosterNo = 0;
    String Enable_Shift_End_Alarm = "0";
    String Enable_Shift_Start_Alarm = "0";
    String Exclude_Goe_Location_Setting = "False";
    String RosterRequested = "0";
    String EnableRosterDelivery = "0";
    static int MinimumInternetSpeedForOnline = 10;
    String AllowViewBookings = "false";
    String EnableViewNoteCases = "00000";
    String RECIPIENT_COORDINATOR="";
    static boolean enforce_refresh=false ;
    int time_count = 0;
    Email email;

    Timer timer;
    Timer timer2;
    Email_Settings email_seting = null;
    String UserSessionLimit = "10";
    String Apply_Goe_Location_Setting = "false";
    String output = "";
    String StaffLocationUpdateInterval = "10";
    String CheckAlertInterval = "10";
    String Current_Address = "";
    static boolean show_again = false;
    long time_counter = 0;
    String RecipientID = "TH100004316";
    int Total_Rosters=0;

    String Staff_Coordinator_Email="";
    String email_msg = "";
    String Email_Subject = "";
    String Receipient_Email = "";
    String Cordinator_Email = "";
    static String possibleEmail = "";
    public static int NEW_PROGRESS=1000;
    int r_code=200;
    int Attendee_code=300;
    int Current_index=0;
    String Existing_Attendees="";
    Group_Recipient Current_Group_recipient;
    static boolean  Group_Recipient_Rocord_Update=false;
    public static int idle_time = 0;
    final String PREFS_NAME = "MTAPrefs";
    SharedPreferences settings = null;
    String RosterDate="";

    NavigationView navigationView=null;

    int Daily_Paid_Hours=0;
    double Daily_Paid_KM=0.0;
    int Monthly_Paid_Hours=0;
    double Monthly_Paid_KM= 0.0;

    private String UseOPNoteAsShiftReport="0";
    private String UseServiceNoteAsShiftReport="0";
    private String EmailUnavailabilityNotification="0";

    private String Allow_OverWrite_Existing_Availability="1" ;

    private static final int STATIC_INTEGER_VALUE = 1;
    DateFormat fmtDateAndTime = DateFormat.getDateInstance();//  DateFormat.getDateTimeInstance();
    private final String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    TextView TextDate;
    Calendar myCalendar = Calendar.getInstance();
    // private Main_Dairy_Adapter.RecyclerViewClickListener mListener;

    private MainActivity.DownReceiver mReceiver;
    ImageButton btnDayNext;
    ImageButton btnDayPrev;
    ProgressDialog pd;
    Bulk_Data bulk_data;
    LoadingDialog pDialog_refresh;
    NetworkStateReceiver networkStateReceiver;
    String KEY="MTASamada2002";
    byte[] myIV = KEY.getBytes(); //{(byte)50,(byte)51,(byte)52,(byte)53,(byte)54,(byte)55,(byte)56,(byte)57};
    byte[] tdesKeyData = KEY.getBytes(); /*{
            (byte)0xA2, (byte)0x15, (byte)0x37, (byte)0x07, (byte)0xCB, (byte)0x62,
            (byte)0xC1, (byte)0xD3, (byte)0xF8, (byte)0xF1, (byte)0x97, (byte)0xDF,
            (byte)0xD0, (byte)0x13, (byte)0x4F, (byte)0x79, (byte)0x01, (byte)0x67,
            (byte)0x7A, (byte)0x85, (byte)0x94, (byte)0x16, (byte)0x31, (byte)0x92 }*/

/*
    DatePickerDialog dCalander =new DatePickerDialog(MainActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            int day2 = view.getDayOfMonth();
            int month2 = view.getMonth() + 1;
            int year2 = view.getYear();
            RosterDate =(year2 + "/" + set_leading_zero(month2, 2) + "/" + set_leading_zero(day2, 2));

            doService2();
        }
    }, 2015, 02, 26);
*/

    private static final String TAG=ControlActivity.class.getName();
    public ControlApplication getApp()
    {
        return (ControlApplication )this.getApplication();
    }



    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            int day2 = view.getDayOfMonth();
            int month2 = view.getMonth() + 1;
            int year2 = view.getYear();
            RosterDate =(year2 + "/" + set_leading_zero(month2, 2) + "/" + set_leading_zero(day2, 2));

            doService2();

            // if (last_indx>0){

            //}
        }
    };


    private void set_array() {

        //  Toast.makeText(getApplicationContext()," in array setting total_item= " + total_item , Toast.LENGTH_LONG).show();
        items = null;
        Accounts = null;
        durationImages = null;
        started = null;
        completed = null;

        items = new String[total_item];
        Accounts = new int[total_item];
        durationImages = new int[total_item];
        started = new int[total_item];
        completed = new int[total_item];
    }

    private void updateLabel() {
       /* TextDate.setText(selection.getText().toString().trim() + ", " + fmtDateAndTime.format(myCalendar.getTime()));

        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
        //  DateFormat dateFormat = new SimpleDateFormat("MMM d,yyyy");
        Date date = new Date(TextDate.getTag().toString());

        String currentDateString = dateFormat.format(date);
        TextDate.setText(selection.getText().toString().trim() + ", " + currentDateString);*/

        try {


            txtRecipient = ((TextView) findViewById(R.id.txtRecipient));
            txtRecipient.setText(StaffCode + "\n Dashboard");
            TextDate = ((TextView) findViewById(R.id.TextDate));
            TextDate.setText(RosterDate);

            android.text.format.DateFormat df = new android.text.format.DateFormat();
            android.text.format.DateFormat.format(DATE_FORMAT_1, new Date(RosterDate));

            String dt = android.text.format.DateFormat.format(DATE_FORMAT_1, new Date(RosterDate)).toString();
            TextDate.setText(dt);


        }catch(Exception ex){}

    }

    private void set_default_date() {

        // DateFormat dateFormat = new SimpleDateFormat("MMM d,yyyy");

        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat dateFormat3 = new SimpleDateFormat("yyyy/MM/dd");
        Calendar myCalendar = Calendar.getInstance();
        Date date = new Date();//myCalendar.getTime();

        String currentDateString = dateFormat.format(date);
        String strfinalDate = dateFormat2.format(date);

        RosterDate=dateFormat3.format(date);

    }

    private String set_leading_zero(int val, int size) {
        String new_val = String.valueOf(val);
        size = size - (new_val.length());
        for (int i = 0; i < size; i++) {
            new_val = "0" + new_val;
        }
        return new_val;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
          //  doService2();
            //  onSaveInstanceState(newBundy);

            // Resolution_Setting();
            //  listView.setAdapter(new ImageAdapter(this, rosters, wwidth, StaffCode, getSecurityToken(), root));
            //   bntLeave.setText("Leave");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
           // doService2();
            // onSaveInstanceState(newBundy);
            // Resolution_Setting();

            //  bntLeave.setText("");
            // listView.setAdapter(new ImageAdapter(this, rosters, wwidth, StaffCode, getSecurityToken(), root));
        }
    }

    private void handleUncaughtException(Thread thread, Throwable e) {

        Tost_Message(e.getMessage());

        finish();

        // Add some code logic if needed based on your requirement
    }
    @Override
    public void networkAvailable() {

        if (Server_Available==false) {
            Tost_Message( "Online Connection becomes available\nRe-login the App in online mode");
            finish();

        }

        Server_Available = true;
        /* TODO: Your connection-oriented stuff here */
    }

    @Override
    public void networkUnavailable() {
        Server_Available = false;
        // textMsg.setText(output + "\t Offline Mode \n Location settings disabled");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this.getApplicationContext();

        xml = new XmlData(context);
        //setContentView(R.layout.content_main_activity__navigation);
      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);
        setupActionBar();

        flagDecoration=true;

        Server_Available = isOnline(getApplicationContext());

       //OP_Case_Note_Activity.Refresh_OP_Note_data=true;

        if (Server_Available == true && !enforce_refresh ) {
            try {
                //new MainActivity.MyAsyncClass6().execute();
            } catch (Exception ex) {
            }
        }
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                handleUncaughtException(thread, ex);
            }
        });

        if (shouldAskPermissions()) {
            askPermissions();
        }

        txtRecipient=findViewById(R.id.txtRecipient);
        txtRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dashboard_displayed==false) {
                    dashboard_displayed=true;
                    set_Dash_board_detail(v.getContext());
                }
            }
        });
        btnDayNext= findViewById(R.id.btnDayNext);
        btnDayNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    TextDate= findViewById(R.id.TextDate);
                    String dt = RosterDate;  // Start date
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    Calendar c = Calendar.getInstance();
                    c.setTime(sdf.parse(dt));
                    c.add(Calendar.DATE, 1);  // number of days to add
                    dt = sdf.format(c.getTime());
                    RosterDate = dt;
                    android.text.format.DateFormat df = new android.text.format.DateFormat();
                    android.text.format.DateFormat.format(DATE_FORMAT_1, new Date(RosterDate));
                    TextDate.setText(android.text.format.DateFormat.format(DATE_FORMAT_1, new Date(RosterDate)).toString());

                    doService2();


                } catch (Exception ex) {}

            }
        });

        btnDayPrev= findViewById(R.id.btnDay_prev);
        btnDayPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    TextDate= findViewById(R.id.TextDate);
                    String dt = RosterDate;  // Start date
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    Calendar c = Calendar.getInstance();
                    c.setTime(sdf.parse(dt));
                    c.add(Calendar.DATE, -1);  // number of days to add
                    dt = sdf.format(c.getTime());
                    RosterDate = dt;
                    android.text.format.DateFormat df = new android.text.format.DateFormat();
                    android.text.format.DateFormat.format(DATE_FORMAT_1, new Date(RosterDate));
                    TextDate.setText(android.text.format.DateFormat.format(DATE_FORMAT_1, new Date(RosterDate)).toString());

                    doService2();


                } catch (Exception ex) {}

            }
        });

        TextDate= findViewById(R.id.TextDate);
        TextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int y,m,d;
                y=Integer.parseInt(RosterDate.substring(0,4));
                m=Integer.parseInt(RosterDate.substring(5,7));
                d=Integer.parseInt(RosterDate.substring(8,10));
               // if (m>1) m--;
                DatePickerDialog dpt=  new DatePickerDialog(MainActivity.this,R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    //  makeWrapContent(view);
                        int day2 = view.getDayOfMonth();
                        int month2 = view.getMonth() +1;
                        int year2 = view.getYear();
                        RosterDate =(year2 + "/" + set_leading_zero(month2, 2) + "/" + set_leading_zero(day2, 2));

                        doService2();

                    }
                }, y, m-1, d);
                try {
                    TextView txt= ((TextView) ((LinearLayout) ((LinearLayout) ((LinearLayout) ((DatePicker)
                            dpt.getDatePicker()).getChildAt(0)).getChildAt(0)).getChildAt(0)).getChildAt(0));

                    txt.setText("Start Date\n" +y );
                    txt.setLineSpacing(1,1.5f);

                    TextView txt2=    ((TextView) ((LinearLayout) ((LinearLayout) ((LinearLayout) ((DatePicker)
                            dpt.getDatePicker()).getChildAt(0)).getChildAt(0)).getChildAt(0)).getChildAt(1));

                    txt2.setText(TextDate.getText().toString().substring(0,11));
                    //  txt.setLineSpacing(2,2);
                }catch(Exception ex){}

                dpt.show();




                /*new DatePickerDialog(MainActivity.this, d, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
*/


            }
        });

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));


        try {


            //mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
            //      .addApi(Places.PLACE_DETECTION_API)
            //    .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
            //  .build();

        } catch (Exception ex) {
            Tost_Message( "Location Error\n" + ex.toString());
        }

        try {
            // callPlaceDetectionApi();
            //   loc=guessCurrentPlace();
            //  new MyAsyncClass5().execute();
        } catch (Exception ex) {
            Tost_Message( "Location Error\n" + ex.toString());
        }



        try {
            settings = getSharedPreferences(PREFS_NAME, 0);
            // setNeverSleepPolicy();
        } catch (Exception ex) {
        }


        showDate = true;
        set_server_Ip();
        recipients = new ArrayList<String>();
        recipients.add(StaffCode);

        try {


            set_default_date();
            listView= findViewById(R.id.listViewMain);
            try {
                buton_clicked = true;
                //doService2();
            } catch (Exception ex) {
            }


          /*  listView.addOnItemTouchListener(new RecyclerTouchListener(this, listView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Current_roster = rosters.get(position);
                    // Shift_Detail2();
                }

                @Override
                public void onLongClick(View view, int position) {
                    Current_roster = rosters.get(position);
                }
            }));*/



        } catch (Exception ex) {
        }


        try {
            // new MyAsyncClass4_bandwidth().execute();
        } catch (Exception ex) {
        }


        Get_User_Settings2();

        Save_Server_Settings();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean syncAcceptedRosters = sharedPreferences.getBoolean("SyncAcceptedRosters", false);
        if(syncAcceptedRosters){
            syncAcceptRostersWebService(user_settings.getStaffCode());
        }

        //hide_Navigation_Items();
        //Get_User_Settings2();

      /*  Button btnLeave = (Button) findViewById(R.id.btnLeave);
        if (user_settings.getAllowLeaveEntry().equalsIgnoreCase("true")) {
            // btnLeave.setVisibility(View.VISIBLE);
            // view.setVisibility(View.VISIBLE);

        } else {

            btnLeave.setVisibility(View.GONE);

        }*/



        if (!isGPSEnabled() && Apply_Goe_Location_Setting.equalsIgnoreCase("true")) {
            buildAlertMessageNoGps();

        }


/*

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {


                // TODO Auto-generated method stub
                runOnUiThread(new Runnable() {
                    public void run() {

                        if (exiting_form) {
                            timer.cancel();
                            return;
                        }

                        idle_time = idle_time + 1;
                        // if (idle_time%3==0 )  Tost_Message(" idle_time = " + idle_time);
                        if (idle_time >= 60 * Integer.parseInt(UserSessionLimit)) {
                            // Tost_Message(" idle_time = " + idle_time);
                            try{
                              //  new MainActivity.MyAsyncClass_Remove_Session().execute();
                            }catch(Exception ex){}

                        }

                        if (Current_roster==null)
                            InfoOnly=true;
                        else{
                            InfoOnly=false;

                            try {
                                int type = Integer.parseInt(Current_roster.getRoster_type());

                                String date = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());

                                if (Current_roster.getInfoOnly().equalsIgnoreCase("true")  || !Current_roster.getRoster_Date().equals (date)
                                        || type == 9 || type == 13 || type == 14 || type == 15) {
                                    InfoOnly = true;
                                }
                            }catch(Exception ex){}

                        }

                        if (Server_Available)
                            InfoOnly=true;

                        if (time_count%20==0  && InfoOnly==false){
                            if (Enable_Shift_End_Alarm.equalsIgnoreCase("1") || Enable_Shift_End_Alarm.equalsIgnoreCase("true"))
                                Check_End_of_shift();

                            if (Enable_Shift_Start_Alarm.equalsIgnoreCase("1") || Enable_Shift_Start_Alarm.equalsIgnoreCase("true"))
                                Check_Start_of_shift();
                        }

                        if (time_counter >= Integer.parseInt(CheckAlertInterval) * 60) {
                            if (Server_Available == true) {

                                try {
                                  //  new MainActivity.MyAsyncClass6().execute();
                                } catch (Exception ex) {
                                }


                                time_counter = 0;
                            }
                        }


                        time_count = time_count + 1;
                        time_counter = time_counter + 1;
                        if (time_count >= 60) {
                            Alarm_RosterNo = 0;
                            time_count = 0;
                        }


                    }
                });

            }
        }, 1000, 1000);
            */




    }

    protected void makeWrapContent(View v) {

        View current = v;
        do {
            // Get the parent
            ViewParent parent = current.getParent();

            // Check if the parent exists
            if (parent != null) {
                // Get the view
                try {
                    current = (View) parent;
                } catch (ClassCastException e) {
                    break;
                }

                // Modify the layout
                current.getLayoutParams().width = WindowManager.LayoutParams.WRAP_CONTENT;
            }
        } while (current.getParent() != null);

        // Request a layout to be re-done
        current.requestLayout();
    }

    @SuppressLint("GestureBackNavigation")
    @Override
    public void onBackPressed() {
     //   DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            super.onBackPressed();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
      //  menu.clear();
        //if(1==1) return false;
        return false;
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


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(1==1) return false;



      //  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
      //  drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(false);

            try {
                String Title = actionBar.getTitle().toString();
                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                actionBar.setCustomView(getLayoutInflater().inflate(R.layout.action_bar_home2, null),
                        new ActionBar.LayoutParams(
                                ActionBar.LayoutParams.MATCH_PARENT,
                                ActionBar.LayoutParams.MATCH_PARENT,
                                Gravity.CENTER
                        )
                );
                TextView textviewTitle = (TextView) actionBar.getCustomView().findViewById(R.id.actionbar_textview);
                textviewTitle.setGravity(Gravity.CENTER);
                textviewTitle.setText(Title);
               final View imageMenu=actionBar.getCustomView().findViewById(R.id.imageMenu_view);
               // imageMenu.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(menu_displayed==false) {
                            menu_displayed=true;
                            set_main_menu(v.getContext());
                        }
                    }
                });

                TextView txtExit= actionBar.getCustomView().findViewById(R.id.txtExit);
               // txtExit.setVisibility(View.GONE);
              //  actionBar.setDisplayHomeAsUpEnabled(true);
                // imageMenu.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                txtExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            ViewGroup viewGroup = findViewById(android.R.id.content);
                            View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.rl_exit_confirmation, viewGroup, false);
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setView(dialogView);
                            final AlertDialog alertDialog = builder.create();
                            Button dialogButtonNo =  dialogView.findViewById(R.id.btnNo);
                            // if button is clicked, close the custom dialog
                            dialogButtonNo.setOnClickListener(new View.OnClickListener() {

                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });

                            Button dialogButtonYes =  dialogView.findViewById(R.id.btnYes);
                            // if button is clicked, close the custom dialog
                            dialogButtonYes.setOnClickListener(new View.OnClickListener() {

                                public void onClick(View v) {
                                    try{
                                        alertDialog.dismiss();
                                        if (settings.getBoolean("mslogin", false)) {
                                            Intent it = new Intent(getApplicationContext(), MainActivity2Factor.class);
                                            startActivity(it);
                                        }

                                        new MyAsyncClass_RemoveSession().execute(UserName);
                                      //  kill_all_processes();
                                    }catch(Exception ex){}
                                }
                            });
                            alertDialog.show();

                    }
                });

                textviewTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      //  onBackPressed();
                    }
                });


            } catch (Exception ex) { }
        }
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        if (1==1) return;
        if (v.getId()==R.id.listViewMain) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle("Select Option");

            if (EnableViewNoteCases.equalsIgnoreCase("00000") || Current_roster.getActual_Client_Code().equalsIgnoreCase("!MULTIPLE")) {
                menu.getItem(0).setVisible(false);
            }

            menu.getItem(1).setVisible(Current_roster.getActual_Client_Code().equalsIgnoreCase("!MULTIPLE"));

        }

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (1==1) return false;
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        Current_roster = rosters.get(info.position);
        Group_shift = Current_roster.getActual_Client_Code().equalsIgnoreCase("!MULTIPLE") ;
        Current_index = info.position;
        //   Toast.makeText(this, item.getItemId() + "" + item.getTitle(), Toast.LENGTH_SHORT).show();
       /* if ( !Current_roster.getActual_Client_Code().equalsIgnoreCase("!MULTIPLE") && item.getItemId()==R.id.mnu_recipients) {
            item.setVisible(false);
        }else{
            item.setVisible(true);

        }*/

        if(listView==null)
            listView =  findViewById(R.id.listViewMain);


        int itemId = item.getItemId();

        if (itemId == R.id.mnu_OpCase) {
            if (!EnableViewNoteCases.equalsIgnoreCase("0000") && !EnableViewNoteCases.equalsIgnoreCase("00000")) {
                String Params = Current_roster.getRecordNo() + ",-,0";
                if (Server_Available)
                    new MainActivity.MyAsyncClass_load_Op_Case_Note().execute(Params);
                show_OP_Case_Note(listView, Current_roster.getRecordNo(), "-", 0);
            }
            return true;
        } else if (itemId == R.id.mnu_recipients) {
            show_Group_Recipients(listView);
            return true;
        } else if (itemId == R.id.mnu_exit) {
            // edit stuff here
            return true;
        } else {
            return super.onContextItemSelected(item);
        }


//        switch(item.getItemId()) {
//            case R.id.mnu_OpCase:
//                if (!EnableViewNoteCases.equalsIgnoreCase("0000") && !EnableViewNoteCases.equalsIgnoreCase("00000")) {
//
//
//                    String Params= Current_roster.getRecordNo()+",-,0";
//                    if (Server_Available)
//                        new MainActivity.MyAsyncClass_load_Op_Case_Note().execute(Params);
//
//                    show_OP_Case_Note(listView, Current_roster.getRecordNo(),"-",0);
//
//
//                }
//                return true;
//            case R.id.mnu_recipients:
//                show_Group_Recipients(listView);
//
//
//                return true;
//            case R.id. mnu_exit:
//                // edit stuff here
//                return true;
//
//            default:
//                return super.onContextItemSelected(item);
//        }
    }
    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        MainActivity.idle_time  = 0;
        getApp().touch();
        Log.d(TAG, "User interaction to "+this.toString());
    }

    public void PopUpMenu(View v)
    {
        ArrayList<String> limits = new ArrayList<String>();


        Context wrapper = new ContextThemeWrapper(v.getContext(), R.style.PopupMenu);

        PopupMenu menu = new PopupMenu(wrapper, v, View.TEXT_ALIGNMENT_CENTER);
        //menu.getMenu().add("Select Item");
        menu.getMenuInflater().inflate(R.menu.menu_list,menu.getMenu());


        if (EnableViewNoteCases.equalsIgnoreCase("00000") || Group_shift ){
            menu.getMenu().getItem(0).setVisible(false);
        }

        //if ( !Current_roster.getActual_Client_Code().equalsIgnoreCase("!MULTIPLE")) {
        menu.getMenu().getItem(1).setVisible(Group_shift);

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {


            @Override
            public boolean onMenuItemClick(
                    android.view.MenuItem item) {

                int itemId = item.getItemId();
                switch(itemId) {
                    case R.id.mnu_OpCase:
                        if (!EnableViewNoteCases.equalsIgnoreCase("0000") && !EnableViewNoteCases.equalsIgnoreCase("00000")) {


                            String Params= ""; //Current_roster.getRecordNo()+",-,0";
                            if (Server_Available)
                                new MainActivity.MyAsyncClass_load_Op_Case_Note().execute(Params);

                            show_OP_Case_Note(listView,Current_roster.getRecordNo(),"-",0);

                        }
                        return true;
                    case R.id.mnu_recipients:
                        show_Group_Recipients(listView);

                        return true;
                    case R.id.mnu_exit:
                        // edit stuff here
                        return true;

                }

                return false;
            }
        });
        menu.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Server_Available && Group_Recipient_Rocord_Update){
            try{
                Group_Recipient_Rocord_Update=false;
                new MyAsyncClass2().execute();
            }catch(Exception ex){}
        }

        if (form_resumed == true) return;
        form_resumed = true;

        if (Server_Available == true && !enforce_refresh ) {
            try {
                new MainActivity.MyAsyncClass6().execute();
            } catch (Exception ex) {
            }
        }

        if(listView==null)
            listView =  findViewById(R.id.listViewMain);

        if (Server_Available && enforce_refresh){
            enforce_refresh=false;
            new MyAsyncClass3().execute();
        }
        try {
            buton_clicked = true;

                doService2();
        } catch (Exception ex) {
        }



    }

    void show_Group_Recipients(View view){

        if (Current_roster.get_group_Recipients()==null)
        {
            Tost_Message("Group Recipient data not found");
            return;
        }

//        List<String> lst=null;
//        // try {
//        final List<Group_Recipient> lst_main = Current_roster.get_group_Recipients();
//        lst= new ArrayList<String>();
//        Existing_Attendees="";
//        for (Group_Recipient g : lst_main) {
//            lst.add(g.getName());
//            if (Existing_Attendees.equals(""))
//                Existing_Attendees=g.getAccountNo();
//            else
//                Existing_Attendees= Existing_Attendees + "," +g.getAccountNo();
//        }
//        // }catch (Exception ex){}
//
//
//
//
//
//        final Dialog dialog = new Dialog(view.getContext());
//        dialog.setContentView(R.layout.recipient_group_view);
//        dialog.setTitle("Group Recipeints");
//        dialog.setCanceledOnTouchOutside(false);
//
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//
//      /*  WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width  = displayMetrics.widthPixels; ;
//        lp.height = WindowManager.LayoutParams.FLAG_FULLSCREEN;
//       */
//
//        dialog.getWindow().setAttributes(lp);
//
//        TextView  txtPickUp=(TextView)dialog.findViewById(R.id.txtPickUp) ;
//        int rType=Integer.parseInt(Current_roster.getRoster_type());
//        //  if ( rType==11 || rType==12  ) txtPickUp.setText("                          ATTENDED");
//        //  else txtPickUp.setText("                          PICKED UP");
//
//        ListView listview=(ListView)dialog.findViewById(R.id.listView1) ;
//        listview.setAdapter(null);
//        if (lst_main==null){
//
//            Tost_Message("No Group Recipients");
//            return;
//        }
//
//        MainActivity.ListArrayAdapter adapter = new MainActivity.ListArrayAdapter(MainActivity.this, lst_main);
//        listview.setAdapter(adapter);
//
//        //listview.smoothScrollToPosition(adapter.getCount());
//
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                try {
//
//                    Group_Recipient rg= lst_main.get(position);
//                    Current_Group_recipient=rg;
//
//                    //  Tost_Message(rg.getAccountNo() +"\n" + rg.getName()+ "\n" + rg.getRecordNo());
//
//                    //  Current_roster= getRoster(rg.getRecordNo());
//                    View_only=true;
//                    // Shift_Detail2();
//
//                }catch(Exception ex) {
//                    Tost_Message( ex.toString());
//                }
//            }
//        });
//
//
//        Button dialogButton = (Button) dialog.findViewById(R.id.btnCancell);
//        // if button is clicked, close the custom dialog
//        dialogButton.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//
//
//                try{
//
//                    Current_roster.setActual_Client_Code("!MULTIPLE");
//                    if (Group_Recipient_Rocord_Update) {
//                        new MainActivity.MyAsyncClass2().execute();
//                        Group_Recipient_Rocord_Update=false;
//                    }
//                }catch(Exception ex){}
//
//                dialog.dismiss();
//            }
//
//        });
//
//        Button btn_AddNewAttendees = (Button) dialog.findViewById(R.id.btn_AddNewAttendees);
//        btn_AddNewAttendees.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                try {
//                    Intent addNewAttendees = new Intent(MainActivity.this, AddNewAttendees.class);
//                    addNewAttendees.putExtra("RecordNo", Current_roster.getRecordNo());
//                    addNewAttendees.putExtra("StaffCode", StaffCode);
//                    addNewAttendees.putExtra("root", root);
//                    addNewAttendees.putExtra("Security_Token", Security_Token);
//                    addNewAttendees.putExtra("User", OperatorId);
//                    addNewAttendees.putExtra("Existing_Attendees", Existing_Attendees);
//                    addNewAttendees.putExtra("ServiceType", Current_roster.getServiceType());
//                    addNewAttendees.putExtra("MobileFutureLimit", MobileFutureLimit);
//
//
//                    //Tost_Message(Current_roster.getServiceType());
//                    Bundle bundle = getShift_Bundle();
//                    addNewAttendees.putExtras(bundle);
//                    //startActivity(addNewAttendees);
//
//                    startActivityForResult(addNewAttendees,Attendee_code);
//                    dialog.dismiss();
//
//                } catch (Exception e) {
//
//                }
//
//            }
//        });

//        dialog.show();
    }
    public void set_main_menu(Context view) {
        final Dialog dialog = new Dialog(view, R.style.CustomDialog);
        dialog.setContentView(R.layout.main_dairy_menu);
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

        ImageView ImageExit=(ImageView) dialog.findViewById(R.id.ImageExit);
        ImageExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                menu_displayed=false;
            }
        });
        TextView txtRefresh = dialog.findViewById(R.id.txtRefresh);
        txtRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    String format2 = "MMM dd, yyyy hh:mm:ss a"; //"dd/MM/yyyy hh:mm";
                    SimpleDateFormat sdf2 = new SimpleDateFormat(format2, Locale.getDefault());
                    Calendar c = Calendar.getInstance();
                    Date dt = c.getTime();

                    String str_time=sdf2.format(dt.getTime());
                    settings.edit().putString("Data_Refreshed",str_time).commit();

                    Load_Data();
                    dialog.dismiss();
                    menu_displayed=false;
                }catch(Exception ex){}
            }
        });

        TextView txtShowAll = dialog.findViewById(R.id.txtShowAll);
        if (!Show_all_roster)
            txtShowAll.setText("Show All");
            //txtShowAll.setBackgroundColor(Color.parseColor("#70B4B4B4"));
        else
            txtShowAll.setText("Not Show All");
          //  txtShowAll.setBackgroundColor(Color.parseColor("#00000000"));

        txtShowAll.refreshDrawableState();

        txtShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    Show_all_roster=!Show_all_roster;
                    doService2();
                    dialog.dismiss();
                    menu_displayed=false;
                }catch(Exception ex){}
            }
        });

        TextView txtAddLeave = dialog.findViewById(R.id.txtAddLeave);
       String test=settings.getString("AllowLeaveEntry","false");
        if (settings.getString("AllowLeaveEntry","false").equalsIgnoreCase("false")){
            txtAddLeave.setVisibility(View.GONE);
        }
        txtAddLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Set_Leave(v.getContext());
                    dialog.dismiss();
                    menu_displayed=false;
                }catch(Exception ex){}
            }
        });
        TextView txtAddAvailability = dialog.findViewById(R.id.txtAddAvailability);
        if (settings.getString("EnableRosterAvailability","false").equalsIgnoreCase("false")){
            txtAddAvailability.setVisibility(View.GONE);
        }
        txtAddAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    show_Roster_Availability();
                    dialog.dismiss();
                    menu_displayed=false;
                }catch(Exception ex){}
            }
        });

        //irfan

        TextView txtAcceptRosters = dialog.findViewById(R.id.txtAcceptRosters);
        if (settings.getString("ApplyAcceptShift","false").equalsIgnoreCase("false")){
            txtAcceptRosters.setVisibility(View.GONE);
        }

        //txtAcceptRosters.setVisibility(View.GONE);


        txtAcceptRosters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    try {



                        froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                        String state = Environment.getExternalStorageState();
                        File fileDir=null;
                        fileDir = new File(froot.getAbsolutePath()+"/.server/");
                        File fXmlFile = null;

                        fXmlFile= new File(fileDir, "traccs.xml");

                        if (fXmlFile.exists()){


                            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                            org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                            doc.getDocumentElement().normalize();
                            NodeList nList = doc.getElementsByTagName("Roster_Info");

                            // Toast.makeText(getApplicationContext(),"node list = " + nList.getLength() , Toast.LENGTH_LONG).show();

                            if (nList==null) return;
                            //textMsg.setText("\nCurrent Element :" + nList.item(0).getNodeName());

                            for ( int tmp = 0; tmp< nList.getLength(); tmp++) {
                                try{

                                    Node  nNode = nList.item(tmp);
                                    //  textMsg.setText(nNode.getTextContent());
                                    //  textMsg.setText("\nCurrent Element :" + nNode.getNodeName());

                                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                        Element eElement = (Element) nNode;

                                        String rosterDate = eElement.getElementsByTagName("Roster_Date").item(0).getTextContent();
                                        String currentDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());

                                        if(rosterDate.equals (currentDate)){

                                            String acceptedByStaff = eElement.getElementsByTagName("acceptedbyStaff").item(0).getTextContent();
                                            String newAcceptDate = eElement.getElementsByTagName("NewAcceptDate").item(0).getTextContent();

                                            boolean result = isDateGreaterOrEqual(newAcceptDate);

                                            if (result){//acceptedByStaff == null || acceptedByStaff.equalsIgnoreCase("False")) {
                                                    accept_Rosters();
                                            }else{
                                                   showAlertMessage("You have already accepted rosters. You can start shifts");



                                            }

                                            break;
                                        }


                                    }else { continue; }
                                }  catch (Exception aE) {  }

                            }


                        } else { }//textMsg.setText("Xml file not found"); }
                    } catch (Exception e) {
                        Tost_Message(e.toString());
                    }

                    dialog.dismiss();
                    menu_displayed=false;
                }catch(Exception ex){}
            }
        });
        //irfan

        TextView txtExit = dialog.findViewById(R.id.txtExit);
        txtExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.rl_exit_confirmation, viewGroup, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                Button dialogButtonNo =  dialogView.findViewById(R.id.btnNo);
                // if button is clicked, close the custom dialog
                dialogButtonNo.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        alertDialog.dismiss();

                    }
                });

                Button dialogButtonYes =  dialogView.findViewById(R.id.btnYes);
                // if button is clicked, close the custom dialog
                dialogButtonYes.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        alertDialog.dismiss();
                        if (settings.getBoolean("mslogin", false)) {
                            Intent it = new Intent(getApplicationContext(), MainActivity2Factor.class);
                            startActivity(it);
                        }
                        try{

                            new MyAsyncClass_RemoveSession().execute(UserName);
                        }catch(Exception ex){}
                    }
                });
                alertDialog.show();
                menu_displayed=false;
            }
        });
        TextView txtStaff_menu = dialog.findViewById(R.id.txtStaff_menu);
        txtStaff_menu.setOnClickListener(new View.OnClickListener() {
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

    public boolean isDateGreaterOrEqual(String newAcceptDate) {
        String dateFormat = "dd/MM/yyyy";  // Adjust this format based on your actual NewAcceptDate string format

        SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);

        try {
            Date newAcceptDateObj = dateFormatter.parse(newAcceptDate);
            Date currentDate = new Date();

            // Compare the dates
            return currentDate.compareTo(newAcceptDateObj) >= 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

void kill_all_processes(){

    List<ActivityManager.RunningAppProcessInfo> processes;
    Context context;
    ActivityManager amg;
    // using Activity service to list all process
    amg = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
// list all running process
    try {
       processes = amg.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : processes) {

            Log.d(info.pid + " : ",info.processName.toString());
            if (info.processName.equalsIgnoreCase("adamas.traccs.mta_20_06.Waiter")) {
                android.os.Process.killProcess(info.pid);
                android.os.Process.sendSignal(info.pid, android.os.Process.SIGNAL_KILL);
               // amg.killBackgroundProcesses(info.processName);

            }
        }
    }catch(Exception ex){Log.d(  " Kill : ",ex.toString());}
}
    public void set_Dash_board_detail(Context view) {
        final Dialog dialog = new Dialog(view,R.style.CustomDialog);
        dialog.setContentView(R.layout.dashboard);
        dialog.setTitle("My Dashboard");
        dialog.setCanceledOnTouchOutside(false);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity= Gravity.CENTER;

       /* dialog.getWindow().setAttributes(lp);
        ((ViewGroup)dialog.getWindow().getDecorView())
                .getChildAt(0).startAnimation(AnimationUtils.loadAnimation(
                view,android.R.anim.slide_in_left));*/
        // Setting the title and layout for the dialog
      //  dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

        getPaid_Hours(RosterDate);

      Window window = dialog.getWindow();
        if (window != null) {
            window.getAttributes().x= 0;//(int) TextDate.getTop();
            window.getAttributes().y =(int) TextDate.getTop()-200;
          //  window.setBackgroundDrawableResource(R.color.transparent);
            //Tost_Message("Top = " + txtRecipient.getTop());

            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
           // params.height = 550;//WindowManager.LayoutParams.WRAP_CONTENT;

            WindowManager wm = (WindowManager) view.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
           // Tost_Message(height + ", " + width);
            if (height>1500)
             params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            else
                params.height = 750;

            params.horizontalMargin = 0;
            params.verticalMargin=0;
            params.gravity =   Gravity.CENTER;
            params.dimAmount = 0;
            params.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;


            window.setAttributes(params);
        }

        TextView txtTotalKM=dialog.findViewById(R.id.txtTotalKM);
        TextView txtTotalHours=dialog.findViewById(R.id.txtTotalHours);
        TextView txtDailyHours=dialog.findViewById(R.id.txtDailyHours);
        TextView txtDailyKM=dialog.findViewById(R.id.txtDailyKM);
        TextView txtTotalBookings=dialog.findViewById(R.id.txtTotalBookings);


     //   dialog.setContentView(txtRecipient,lp);
        //  Spinner spinnerIncidentLocation = (Spinner) dialog.findViewById(R.id.spinnerIncidentLocation);
        try {
            txtTotalBookings.setText( "Total Bookings " + Total_Rosters);
            txtDailyHours.setText( ConvertToTime(Daily_Paid_Hours));
            txtTotalHours.setText( ConvertToTime(Monthly_Paid_Hours));
            txtDailyKM.setText(""+ String.format("%.2f", Daily_Paid_KM));
            txtTotalKM.setText(""+ String.format("%.2f", Monthly_Paid_KM));

          /*  txtTotalBookings.setText("Total Bookings" + spaces(8) + Total_Rosters);
            txtDailyHours.setText("Daily Hours" + spaces(8) +  ConvertToTime(Daily_Paid_Hours));
            txtTotalHours.setText("Total Hours " +spaces(8)+ ConvertToTime(Monthly_Paid_Hours));
            txtDailyKM.setText("Daily KM " + spaces(16) + Daily_Paid_KM);
            txtTotalKM.setText("Total KM " + spaces(16) + Monthly_Paid_KM);
*/
        } catch (Exception ex) {
            Tost_Message(ex.toString());
        }

        ImageButton dialogButton =  dialog.findViewById(R.id.btnExit);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialog.dismiss();
                dashboard_displayed=false;
            }
        });
        Button btnPolicyLink =  dialog.findViewById(R.id.btnPolicyLink);
        // if button is clicked, close the custom dialog
        btnPolicyLink.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String url = user_settings.getPolicyLink();
                Intent i = new Intent(MainActivity.this,WebService.class);
                i.putExtra("web_link",url);
                //Intent i = new Intent(Intent.ACTION_VIEW);
                //i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        Button btnWebsite =  dialog.findViewById(R.id.btnWebsite);
        // if button is clicked, close the custom dialog
        btnWebsite.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String url = user_settings.getAgencyPortal();//"http://adamas.net.au";
                Intent i = new Intent(MainActivity.this,WebService.class);
                i.putExtra("web_link",url);
                //Intent i = new Intent(Intent.ACTION_VIEW);
                //i.setData(Uri.parse(url));
                startActivity(i);
            }
        });



        Button btnCompetencies =  dialog.findViewById(R.id.btnCompetencies);
        // if button is clicked, close the custom dialog
        btnCompetencies.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String url = user_settings.getAgencyPortal();//"http://adamas.net.au";
                Intent i = new Intent(MainActivity.this,WebService.class);
                i.putExtra("web_link",url);
                //Intent i = new Intent(Intent.ACTION_VIEW);
                //i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        dialog.show();

    }
String spaces(int sps){
        String spss="";
        for (int i=0;i<sps; i++)
            spss=spss+" ";
        return  spss;
}
    public void Add_Incident2(String Incident_Type, String Service, String Incident_Severity, String Location, String Note, boolean No_Recipient, String StaffCode, String Personid, String OperatorID, String AccountNo, String Program,String IncidentSummary) {
        try {

            String command;
            // command= "\n update roster set notes='" + Note.replace("'", "''") + "' WHERE recordno = " + RecordNo;
            //command= command+ "\n update Recipients set notes='" + Note.replace("'", "''") + "' WHERE AccountNo in (select [client code] from roster where recordno = " + RecordNo + ")";

            Note = SQLSafe(Note);
            Note=Note.replace("\n","~");
            IncidentSummary = SQLSafe(IncidentSummary);
            Note=Note.replace("\n","~");
            command = "\nN`" + Personid + "`" + Incident_Type + "`" + Service + "`" + Incident_Severity + "`" + Location + "`" + Note + "`" + No_Recipient + "`" + StaffCode + "`" + OperatorID + "`" + AccountNo + "`" + Program + "`" + IncidentSummary;


            set_Updates(command);
            try {
                String messgas = "Client Incident added by  \"" + StaffCode + "\" :\n\n" + "Incident Type : " + Incident_Type + "\nServity : " + Incident_Severity + " \nLocation : " + Location + " \nService : " + Service + "\n" + IncidentSummary + "\n" + Note;
                String title = "Client Incident for \"" + AccountNo + "\"\n";

                send_email_alert(messgas, title);

            } catch (Exception ex) {

            }
            Tost_Message("Cleint Incident added Successfully");
        } catch (Exception ex) {
            Tost_Message("Operation not done due to some error\n" + ex.toString());
        }
    }

    public void Add_Incident(String Incident_Type, String Service, String Incident_Severity, String Location, String Note, boolean No_Recipient,String OperatorID,String Personid, String AccountNo, String Program, String IncidentSummary) {




        if (Server_Available || !Server_Available) { // call in both cases
            Add_Incident2(Incident_Type, Service, Incident_Severity, Location, Note, No_Recipient, StaffCode,Personid,OperatorID, AccountNo, Program,IncidentSummary);
            return;
        }

        String URL41 = root + "/TimeSheet.asmx?op=Add_Incident";
        String SOAP_ACTION41 = "https://tempuri.org/Add_Incident";
        String METHOD_NAME41 = "Add_Incident";

        // TextView txtmsg = ((TextView) findViewById(R.id.textMsg));
        //txtmsg.setVisibility(View.VISIBLE);
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
                    String messgas = "Client Incident added by  \"" + StaffCode + "\" :\n\n" + "Incident Type : " + Incident_Type + "\nServity : " + Incident_Severity + " \nLocation : " + Location + " \nService : " + Service +  "\n" + IncidentSummary + "\n" + Note;
                    String title = "Client Incident for \"" + AccountNo + "\"\n";


                    if (No_Recipient || Cordinator_Email.equals("")) {
                        // tmp_cor_email=Cordinator_Email;
                        Cordinator_Email=Staff_Coordinator_Email;
                    }

                    send_email_alert(messgas, title);



                } catch (Exception ex) {
                    Tost_Message("Operation not done due to some server error\n" + ex.toString());
                }


            } else
                Tost_Message("Client Incident not added due to some problem - " + AccountNo + " Result=" + result.toString());



        } catch (Exception ex) {
            Tost_Message("Operation not done due to some server error\n" + ex.toString());
        }


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
                        //  textMsg.setText(nNode.getTextContent());
                        //  textMsg.setText("\nCurrent Element :" + nNode.getNodeName());

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            String code = eElement.getElementsByTagName("Description").item(0).getTextContent();

                            lst_inscdt.add(code);


                        }

                    } catch (Exception aE) {
                        Tost_Message( "Error in Incident getting Locations ");
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
                        //  textMsg.setText(nNode.getTextContent());
                        //  textMsg.setText("\nCurrent Element :" + nNode.getNodeName());

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



    private class SimpleArrayAdapter extends ArrayAdapter<String> {
        public SimpleArrayAdapter(Context context, List<String> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);

        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    private class ListArrayAdapter extends BaseAdapter {
        List<Group_Recipient> lst_grps;
        Group_Recipient grp;
        ViewHolder viewHolder;
        Context context;

        public ListArrayAdapter(Context contxt, List<Group_Recipient> objects) {
            // super(context, android.R.layout.simple_list_item_1, objects);
            this.lst_grps=objects;
            this.context=contxt;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // View card = null;
            View gridView = null;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                viewHolder = new ViewHolder();
                gridView = inflater.inflate(R.layout.grid_row_recipient_group, null);

            } else {
                gridView = (LinearLayout) convertView;
                viewHolder = (ViewHolder) gridView.getTag();
            }

            viewHolder.index = position;
            viewHolder.txtReceipientName = (TextView) gridView.findViewById(R.id.txtRecipientDetail);
            viewHolder.txtReceipientName.setTag(position);
            viewHolder.chkSelect = (CheckBox) gridView.findViewById(R.id.checkBox);
            viewHolder.txtClientNote = (TextView) gridView.findViewById(R.id.txtClientNote);
            //   viewHolder.txtShiftNote = (TextView) gridView.findViewById(R.id.txtShiftNote);

            try{


                grp = lst_grps.get(position);

                viewHolder.txtReceipientName.setText(grp.getName() + "\n" + grp.getPickUpAddress1() );
                viewHolder.txtClientNote.setText("Client Notes");
                viewHolder.txtShiftNote.setText("Record Incident");

                if (user_settings.getAllowIncidentEntry().equalsIgnoreCase("true") || user_settings.getAllowIncidentEntry().equalsIgnoreCase("1"))
                    viewHolder.txtShiftNote.setVisibility(View.VISIBLE);
                else
                    viewHolder.txtShiftNote.setVisibility(View.INVISIBLE);

                viewHolder.chkSelect.setChecked(grp.getStatus().equals("2"));


                viewHolder.txtShiftNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {

                            Current_Group_recipient = lst_grps.get(position);
                            //Set_Incident(listView.getContext());


                        } catch (Exception e) {
                        }
                    }

                });

                viewHolder.txtClientNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        try {
                            Group_Recipient grp = null;

                            Current_Group_recipient = lst_grps.get(position);
                            grp = Current_Group_recipient;
                            //  grp=lst_grps.get(position);
                            Roster_Info r = getRoster(grp.getRecordNo());

                            if (r != null) {

                                Current_roster = r;
                            } else {
                                Current_roster.setClient_code(grp.getAccountNo());
                                Current_roster.setActual_Client_Code(grp.getAccountNo());
                            }


                            String Params = grp.getAccountNo();// + "," + grp.getRecordNo()+"," +grp.getCoordinator_Email()+","+ position;

                            if (Server_Available)
                                new MainActivity.MyAsyncClass_load_Op_Case_Note().execute(Params);

                            show_OP_Case_Note(v, grp.getRecordNo(), grp.getCoordinator_Email(), position);

                        } catch (Exception e) {
                        }
                    }


                });
                viewHolder.chkSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean sts = ((CheckBox) v).isChecked();
                        // grp=(Group_Recipient) chkSelect.getTag();
                        grp = lst_grps.get(position);
                        Group_Recipient_Rocord_Update = true;
                        if (sts) {
                            try {
                                // viewHolder.chkSelect.setChecked(true);

                                grp.setStatus("2");
                                //  Tost_Message("Update Roster status = 2 where RecordNo="+grp.getRecordNo());
                                set_Updates("Update Roster set status = 2 where RecordNo=" + grp.getRecordNo() + "\n");
                                if (!Server_Available)  xml.Update_Roster_Node(grp.getRecordNo(), "Status", "2");
                            } catch (Exception e) {
                            }
                        } else {
                            try {
                                //viewHolder.chkSelect.setChecked(false);
                                //grp.setStatus("1");
                                grp.setStatus("1");
                                set_Updates("Update Roster set status = 1 where RecordNo=" + grp.getRecordNo() + "\n");
                                if (!Server_Available)  xml.Update_Roster_Node(grp.getRecordNo(), "Status", "1");
                            } catch (Exception e) {
                            }

                        }
                    }

                });

                gridView.setTag(viewHolder);
            }catch (Exception ex){}

            return gridView;
        }
        @Override
        public int getCount() {
            return this.lst_grps.size();
        }

        @Override
        public Object getItem(int position) {
            return lst_grps.get(position) ;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder {

            TextView txtShiftNote;
            TextView txtReceipientName;
            TextView txtClientNote;
            CheckBox chkSelect;

            int index;

        }

    }

    Roster_Info getRoster(String RecordNo2) {
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

                String s_RecordNo = "";

                for (int tmp = 0; tmp < nList.getLength(); tmp++) {
                    try {

                        Node nNode = nList.item(tmp);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            s_RecordNo = eElement.getElementsByTagName("RecordNo").item(0).getTextContent();


                            if (s_RecordNo.equals(RecordNo2)) {
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
    boolean show_MultiShift_Alarm(Roster_Info job, int Start_End) {
        Roster_Info rstr=null;
        Roster_Info first_job=null;
        Roster_Info last_job=null;

        boolean status=true;
        try {
            for (int i = 0; i < rosters.size(); i++) {
                rstr = rosters.get(i);
                if (rstr.getMinorGroup().equalsIgnoreCase("BREAK")) return true;
                if (rstr.getRecordNo().equalsIgnoreCase(job.getRecordNo())) {
                    if (first_job == null) first_job = rstr;

                    if (Long.parseLong(first_job.getStarted()) > 0 && Start_End == 1) {

                        return false;
                    }


                }

                if (rstr.getActual_Client_Code().equalsIgnoreCase(job.getActual_Client_Code())) {
                    if (first_job == null) first_job = rstr;
                    //else if (last_job==null)

                    last_job = rstr;
                }

            }

            if (first_job.getRecordNo().equalsIgnoreCase(last_job.getRecordNo())) {

                status = true;
            }
            if (Long.parseLong(first_job.getStarted()) > 0 && last_job.getRecordNo().equalsIgnoreCase(job.getRecordNo())) {
                if (Start_End == 2)
                    status = true;
            }
            if (Long.parseLong(first_job.getStarted()) > 0 && !first_job.getRecordNo().equalsIgnoreCase(job.getRecordNo())) {

                status = false;
            }
        }catch(Exception ex){}
        return status;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        if (menu != null) {
            menu.clear();
        }
        return false;
//        MenuItem btnTravel2 = menu.findItem(R.id.btnbtnLoad);
//        //MenuItem btnPic2 = menu.findItem(R.id.btnShowAll);
//        MenuItem btnDeliver = menu.findItem(R.id.btnDeliver);
//        MenuItem btnLeave = menu.findItem(R.id.btnLeave);
//        MenuItem btnBooking = menu.findItem(R.id.btnBooking);
//        MenuItem btnRosterAvailability = menu.findItem(R.id.btnRosterAvailability );
//
//        btnTravel2.setVisible(true);
//        // btnPic2.setVisible(true);
//        btnBooking.setVisible(false);
//        // btnRosterAvailability.setVisible(false);
//
//        if (!user_settings.getAllowLeaveEntry().equalsIgnoreCase("true") && !user_settings.getAllowLeaveEntry().equalsIgnoreCase("1")){
//            btnLeave.setVisible(false);
//        }
//
//        if (!user_settings.getEnableRosterDelivery().equalsIgnoreCase("true") && !user_settings.getEnableRosterDelivery().equalsIgnoreCase("1")){
//            btnDeliver.setVisible(false);
//        }
//
//        if (!user_settings.getEnableRosterAvailability().equalsIgnoreCase("true") && !user_settings.getEnableRosterAvailability().equalsIgnoreCase("1")){
//            btnRosterAvailability.setVisible(false);
//        }
//        return true;
    }


    void show_Roster_Availability(){

        Intent intent= new Intent(this,Roster_Availability_Main.class);

        Bundle b= new Bundle();
        b.putString("root", root);

        String format="yyyy/MM/dd";

        SimpleDateFormat    sdf = new SimpleDateFormat(format, Locale.getDefault());
        Calendar c = Calendar.getInstance();
        Date dt=c.getTime();

        String rosterDate = sdf.format(dt);


        b.putString("StaffCode", StaffCode);
        b.putString("Roster_Date",  rosterDate);
        b.putBoolean("Server_Available", Server_Available);
        b.putString("OperatorId", OperatorId);
        b.putString("Security_Token", Security_Token);
        b.putString("Allow_OverWrite_Existing_Availability", Allow_OverWrite_Existing_Availability);
        b.putString("Coordinator_Email", Staff_Coordinator_Email);
        b.putString("EmailUnavailabilityNotification", EmailUnavailabilityNotification);


        intent.putExtras(b);


        startActivityForResult(intent, r_code);
    }

    private void show_OP_Case_Note(View v, String Roster_RecordNo, String Coord_Email, int indx){
        try{
            Bundle bundle= new Bundle();
            bundle.putString("root",root);
            bundle.putString("StaffCode",StaffCode);
            bundle.putString("EnableViewNoteCases",EnableViewNoteCases);

            bundle.putString("OperatorId",OperatorId);
            bundle.putString("Security_Token",getSecurityToken());

            bundle.putBoolean("Server_Available",Server_Available);
            bundle.putString("RecordNo",Roster_RecordNo);
            bundle.putString("Coordinator_Email",Coord_Email);
            bundle.putString("UseOPNoteAsShiftReport",  UseOPNoteAsShiftReport);
            bundle.putString("UseServiceNoteAsShiftReport",UseServiceNoteAsShiftReport);
            bundle.putString("Roster_Date",Current_roster.getRoster_Date() + " " + Current_roster.getStart_Time() + "-" + Current_roster.get_End_Time());

            String PersonId="0";

            List<Group_Recipient> grp =Current_roster.get_group_Recipients();

            if (grp != null) {
                if (grp.size() > 0) {
                    PersonId = grp.get(indx).get_Personid();
                    bundle.putString("Recipient",grp.get(indx).getAccountNo());
                    bundle.putString("AccountNo",grp.get(indx).getAccountNo());
                }else {
                    bundle.putString("Recipient", Current_roster.getActual_Client_Code());
                    bundle.putString("AccountNo",Current_roster.getClient_code());
                }
            }else{
                bundle.putString("Recipient",Current_roster.getActual_Client_Code());
                bundle.putString("AccountNo",Current_roster.getClient_code());
            }
            bundle.putString("PersonId",PersonId);
            bundle.putString("AllowRosterNoteEntry",user_settings.getAllowRosterNoteEntry());

            bundle.putString("AllowCaseNoteEntry",user_settings.getAllowCaseNote());
            bundle.putString("AllowOPNoteEntry",user_settings.getAllowOPNote());
            bundle.putString("AllowClinicalNoteEntry",user_settings.getAllowClinicalNoteEntry());
            bundle.putString("PersonId",Current_roster.getPersonId());


            //  bundle.putBoolean("PersonId",PersonId);


            //  Toast.makeText(v.getContext(),  EnableViewNoteCases , Toast.LENGTH_LONG).show();

            Intent intent2= new Intent(v.getContext(),OP_Case_Note_Activity.class);
            intent2.putExtras(bundle);
            startActivity(intent2);
            // Toast.makeText(v.getContext(),  "No Issue", Toast.LENGTH_LONG).show();
        }catch(Exception ex){Tost_Message( ex.toString() );}
    }


    public void Load_Data(){

        if (Server_Available==false){
            Tost_Message(  "Internet is not available to refresh data" );
            return;
        }
        try {
            spt.bindListeners();
            double spd = spt.speedInfo.kilobits;
            spd = spt.speedInfo.kilobits;
            for(int i=0; i<5;i++ ){
                spd = spt.speedInfo.kilobits;
            }
            if (spt.speedInfo==null) {
                Tost_Message("Tap again to refresh");


                return;
            }

        }catch(Exception ex){
            Tost_Message(ex.toString());
        }
        double speed=0;
        try{
            spt.bindListeners();
            speed = spt.speedInfo.kilobits;
            if(speed<speed_limit){
                Tost_Message( "Internet is slow, refresh may not be completed \n please retry in a few minutes if data is not fully refreshed" );
                // Tost_Message("Internet  Speed = " + speed + " KB");
                // return;
            }
        } catch(Exception ex){
            //  Tost_Message(ex.toString());
            Tost_Message("Re-Try to refresh");
        }

        // Tost_Message("Internet  Speed = " + speed + " KB");
        try{
            // new MyAsyncClass3().execute();
        } catch(Exception ex){}


        try{
            String format="yyyy/MM/dd";

            SimpleDateFormat    sdf = new SimpleDateFormat(format,Locale.getDefault());
            Calendar c = Calendar.getInstance();
            Date dt=c.getTime();

            String rosterDate = sdf.format(dt);

            //  Toast.makeText(getApplicationContext(), "Loading data from server for " + StaffCode, Toast.LENGTH_LONG).show();

            bulk_data= new Bulk_Data(root,StaffCode,OperatorId,Security_Token,Server_Available,rosterDate,context);
            new MainActivity.MyAsyncClass_bulk_data().execute();

            //AsyncTaskTools.execute(new MyAsyncClass_buld_data().execute());


        } catch(Exception ex){}



    }

    void Update_Deliver_Status(boolean deliver){

        try{
            if (deliver)
            {
                btnDeliver.setTag("1");
                btnDeliver.setBackgroundResource(R.drawable.checkbox2);

                String command="update UserInfo set RosterRequested=1 where name='" + OperatorId + "'" ;

                Make_update (command );
            }else{
                btnDeliver.setTag("0");
                btnDeliver.setBackgroundResource(R.drawable.checkbox);

                String command="update UserInfo set RosterRequested=0 where name='" + OperatorId + "'" ;

                Make_update (command);
            }

            Tost_Message("Status Updated Successfully");

        }catch(Exception ex){ Tost_Message( "Status not updated");}
    }



    public void show_Shift_View(Context view)
    {
        try{

        }catch(Exception ex){}

        final Dialog dialog = new Dialog(view);
        dialog.setContentView(R.layout.shift_view);
        dialog.setTitle("Shift View");
        dialog.setCanceledOnTouchOutside(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        // View rl_main_Headning=(View)dialog.findViewById(R.id.rl_main_Headning);

        TextView txt= (TextView)dialog.findViewById(R.id.txtMain);
        txt.setText(Current_Address);


        Button dialogButton = (Button) dialog.findViewById(R.id.btnCancell);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        dialog.show();

    }
    void start_service(){

        try{

            // Intent intnt= new Intent(this, Alert_Service.class)	;
            Intent intnt= new Intent(this, load_data_service.class)	;
            // Intent intnt2= new Intent(this, Load_Data_Service2.class)	;

            Bundle b= new Bundle();
            b.putString("root", root);

            String format="yyyy/MM/dd";

            SimpleDateFormat    sdf = new SimpleDateFormat(format,Locale.getDefault());
            Calendar c = Calendar.getInstance();
            Date dt=c.getTime();

            String rosterDate = sdf.format(dt);

            //  Toast.makeText(getApplicationContext(), "Loading data from server for " + StaffCode, Toast.LENGTH_LONG).show();

            b.putString("StaffCode", StaffCode);
            b.putString("Roster_Date",  rosterDate);
            b.putBoolean("Server_Available", Server_Available);
            b.putString("OperatorId", OperatorId);
            b.putString("Security_Token", Security_Token);
            intnt.putExtras(b);
            //  intnt2.putExtras(b);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            if (Server_Available==true) {
                startService(intnt);
                //  startService(intnt2);
            }


        }catch(Exception ex){}
    }
    void start_service2(){

        try{

            // Intent intnt= new Intent(this, Alert_Service.class)	;
            Intent intnt= new Intent(this, Load_Data_Service2.class)	;
            // Intent intnt2= new Intent(this, Load_Data_Service2.class)	;

            Bundle b= new Bundle();
            b.putString("root", root);

            String format="yyyy/MM/dd";

            SimpleDateFormat    sdf = new SimpleDateFormat(format,Locale.getDefault());
            Calendar c = Calendar.getInstance();
            Date dt=c.getTime();

            String rosterDate = sdf.format(dt);

            //  Toast.makeText(getApplicationContext(), "Loading data from server for " + StaffCode, Toast.LENGTH_LONG).show();

            b.putString("StaffCode", StaffCode);
            b.putString("Roster_Date",  rosterDate);
            b.putBoolean("Server_Available", Server_Available);
            b.putString("OperatorId", OperatorId);
            b.putString("Security_Token", Security_Token);
            intnt.putExtras(b);
            //  intnt2.putExtras(b);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            if (Server_Available==true) {
                startService(intnt);
                //  startService(intnt2);
            }


        }catch(Exception ex){}
    }
    void start_service3(){

        try{

            // Intent intnt= new Intent(this, Alert_Service.class)	;
            Intent intnt= new Intent(this, Load_Data_Service3.class)	;
            // Intent intnt2= new Intent(this, Load_Data_Service2.class)	;

            Bundle b= new Bundle();
            b.putString("root", root);

            String format="yyyy/MM/dd";

            SimpleDateFormat    sdf = new SimpleDateFormat(format,Locale.getDefault());
            Calendar c = Calendar.getInstance();
            Date dt=c.getTime();

            String rosterDate = sdf.format(dt);

            //  Toast.makeText(getApplicationContext(), "Loading data from server for " + StaffCode, Toast.LENGTH_LONG).show();

            b.putString("StaffCode", StaffCode);
            b.putString("Roster_Date",  rosterDate);
            b.putBoolean("Server_Available", Server_Available);
            b.putString("OperatorId", OperatorId);
            b.putString("Security_Token", Security_Token);
            intnt.putExtras(b);
            //  intnt2.putExtras(b);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            if (Server_Available==true) {
                startService(intnt);
                //  startService(intnt2);
            }


        }catch(Exception ex){}
    }
    void start_service_for_alert(){

        try{

            Intent intnt= new Intent(this, Alert_Service.class)	;

            intnt.putExtra("root", root);

            // Alert_Service="TA100003999";

            String Roster_Date=TextDate.getTag().toString();
            intnt.putExtra("StaffCode", StaffCode);
            intnt.putExtra("Roster_Date",  Roster_Date);
            intnt.putExtra("Server_Available", Server_Available);
            intnt.putExtra("OperatorId", OperatorId);
            intnt.putExtra("Security_Token", Security_Token);

            //if (Server_Available==true)
            //  startService(intnt);


        }catch(Exception ex){}
    }
    void show_shift_detail_With_auto_login(){
        autologin="false";
        set_first_Item_InGrid();
        if (rosters.size()>0 && Current_roster!=null){

            try{

                TextView tv;
                tv= ((TextView)findViewById(R.id.TextDate));
                tv.setText(Current_roster.getRecordNo() + " " + Current_roster.getClient_code()  );
                tv.setTag(Current_roster.getActual_Client_Code());

                String msg="";

                int ind=TextDate.getText().toString().indexOf(",");
                String str_dated=TextDate.getText().toString().substring(ind);

                msg= "You have logged on to your shift " + str_dated
                        + " from " + Current_roster.getStart_Time() + " to " + Current_roster.get_End_Time();

                listView =  findViewById(R.id.listViewMain);
                msg= msg + "\nRecord No. " + Current_roster.getRecordNo() +  "\nRoster Date: " + Current_roster.getRoster_Date() +
                        "\nClient Code : " + Current_roster.getClient_code() + "\nStart Time : " + Current_roster.getStart_Time() + ", Duration :" + Current_roster.get_Calculated_Duration() + "\nNotes : " + Current_roster.getNotes()  ;

                // ShowDialog(listView,msg);
                //Shift_Detail();

            }catch(Exception e){ }
        }
    }


    String getSecurityToken(){

        String Val= OperatorId  + "$" + Security_Token + "$";
        return Val;
    }

    public void Make_update(String Command)
    {

        final Context context=getApplicationContext();
        final String command=Command;
        Handler handler =  new Handler(context.getMainLooper());
        handler.post( new Runnable(){
            public void run(){

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
                    // textMsg.setText("Operation not done due to some server error\n" + ex.toString());
                }




            }
        });
    }

    void set_server_Ip()
    {

        Bundle bundle = getIntent().getExtras();

        try{
            UserName=bundle.get("User").toString();
            OperatorId=UserName;

            Server_Available= bundle.get("Server").equals("True");


            root=bundle.get("root").toString();
            urlString=root + "/timesheet.asmx";

            spt=  new SpeedTest(root);
            spt.bindListeners();


            if (bundle.get("StaffCode")==null)
                StaffCode="Nothing";
            else
                StaffCode=bundle.get("StaffCode").toString();

            if (bundle.get("Timesheet")==null)
                Timesheet="0";
            else
                Timesheet=bundle.get("Timesheet").toString();

            if (bundle.get("TMMode")==null)
                TMMode="0";
            else
                TMMode=bundle.get("TMMode").toString();
            if (bundle.get("TMMode")==null)
                TMMode="0";
            else
                TAMode=bundle.get("TAMode").toString();

            if (bundle.get("AllowSetTime")==null)
                AllowSetTime="0";
            else
                AllowSetTime=bundle.get("AllowSetTime").toString();

            if (bundle.get("MobileFutureLimit")==null)
                MobileFutureLimit="10";
            else
                MobileFutureLimit=bundle.get("MobileFutureLimit").toString();

            if (bundle.get("AllowPicUpload")==null)
                AllowPicUpload="0";
            else
                AllowPicUpload=bundle.get("AllowPicUpload").toString();

            if (bundle.get("RecipientDocFolder")==null)
                RecipientDocFolder="D:\\";
            else
                RecipientDocFolder=bundle.get("RecipientDocFolder").toString();

            if (bundle.get("UserId")==null)
                UserId="0";
            else
                UserId=bundle.get("UserId").toString();

            if (bundle.get("StaffLocationUpdateInterval")==null)
                StaffLocationUpdateInterval="5";
            else
                StaffLocationUpdateInterval=bundle.get("StaffLocationUpdateInterval").toString();

            if (bundle.get("MobileIncident")==null)
                MobileIncident="false";
            else
                MobileIncident=bundle.get("MobileIncident").toString();


            if (bundle.get("mobilegeocodelimit")==null)
                mobilegeocodelimit="1";
            else
                mobilegeocodelimit=bundle.get("mobilegeocodelimit").toString();

            if (bundle.get("Security_Token")==null)
                Security_Token="";
            else
                Security_Token=bundle.get("Security_Token").toString();

            if (bundle.get("Apply_Goe_Location_Setting")==null)
                Apply_Goe_Location_Setting="False";
            else
                Apply_Goe_Location_Setting=bundle.get("Apply_Goe_Location_Setting").toString();

            if (bundle.get("autologin")==null)
                autologin="False";
            else
                autologin=bundle.get("autologin").toString();

            if (bundle.get("UserSessionLimit")==null)
                UserSessionLimit="10";
            else
                UserSessionLimit=bundle.get("UserSessionLimit").toString();

            if (bundle.get("Enable_Shift_End_Alarm")==null)
                Enable_Shift_End_Alarm="0";
            else
                Enable_Shift_End_Alarm=bundle.get("Enable_Shift_End_Alarm").toString();

            try{
                if (bundle.get("CheckAlertInterval")==null)
                    CheckAlertInterval="10";
                else
                    CheckAlertInterval=bundle.get("CheckAlertInterval").toString();

            }catch(Exception ex){}

            try{
                if (bundle.get("RosterRequested")==null)
                    RosterRequested="0";
                else
                    RosterRequested=bundle.get("RosterRequested").toString();

            }catch(Exception ex){}

            try{
                if (bundle.get("EnableRosterDelivery")==null)
                    EnableRosterDelivery="0";
                else
                    EnableRosterDelivery=bundle.get("EnableRosterDelivery").toString();

            }catch(Exception ex){}
            try{
                if (bundle.get("MinimumInternetSpeedForOnline")==null)
                    MinimumInternetSpeedForOnline=10;
                else
                    MinimumInternetSpeedForOnline=bundle.getInt("MinimumInternetSpeedForOnline");

            }catch(Exception ex){}

            try{
                if (bundle.get("AllowViewBookings")==null)
                    AllowViewBookings="false";
                else
                    AllowViewBookings=bundle.getString("AllowViewBookings");

            }catch(Exception ex){}


            try{
                if (bundle.get("EnableViewNoteCases")==null)
                    EnableViewNoteCases= "00000";
                else
                    EnableViewNoteCases=bundle.getString("EnableViewNoteCases");

            }catch(Exception ex){}


            try{
                if (bundle.get("Enable_Shift_Start_Alarm")==null)
                    Enable_Shift_Start_Alarm="0";
                else
                    Enable_Shift_Start_Alarm=bundle.getString("Enable_Shift_Start_Alarm");

            }catch(Exception ex){}


            // Toast.makeText(getApplicationContext(), "AllowViewBookings=" + AllowViewBookings, Toast.LENGTH_LONG).show();

            URL = root  + "/TimeSheet.asmx?op=getRoster_Datewise";
            URL2 = root  + "/TimeSheet.asmx?op=getRecipients";
            URL3 = root  + "/TimeSheet.asmx?op=getRecipient_Detail";
            URL4 = root  + "/TimeSheet.asmx?op=getDevice_Active_Reminders";

        }catch(Exception ex) {rosterview.setText("Some data values are not loaded properly\n" + ex.toString());}
    }

    public void Shift_Detail2(){



        try{

            loc=guessCurrentPlace();


        } catch(Exception ex){
            Tost_Message( ex.toString());
        }


        final TextView tv;
        tv= ((TextView)findViewById(R.id.TextDate));

        if ( Booking_view==true && Accept_Booking==true ){

            Handler handler =  new Handler(getApplicationContext().getMainLooper());
            handler.post( new Runnable(){
                public void run(){
                }
            });
            try{
                Accept_Roster_Booking(listView);

            }catch(Exception ex){ }





            buton_clicked=true;
            //  doService(tv);
            return;
        }

        try{
            if ( Booking_view==true && Accept_Booking==false ){
                Tost_Message("You do not have permission to Accept/Reject Bookings");
                return;
            }
            try {
                //Save_Current_Location();
            }catch( Exception ex){
                Tost_Message("Location Error\n" + ex.toString());
            }

            if (tv.getText()=="" || Current_roster==null )
            {
                textMsg.setText("Current Roster not set");
                return;
            }
            TextView tdate=   ((TextView) findViewById(R.id.TextDate));



            //Intent intent = new Intent(this, Shift_Detail.class);
            Intent intent = new Intent(this, Shift_Detail.class);
            Bundle bundle = getShift_Bundle();


            //Toast.makeText(getApplicationContext(), "Sending AllowPicUpload " + AllowPicUpload, Toast.LENGTH_LONG).show();
            form_resumed=false;
            intent.putExtras(bundle);
            startActivity(intent);
            //startActivityForResult(intent, STATIC_INTEGER_VALUE);

        }catch(Exception ex){}


    }
    public Bundle getShift_Bundle(){



        TextView tdate=   ((TextView) findViewById(R.id.TextDate));
        Bundle bundle = new Bundle();
        try {


            Exclude_Goe_Location_Setting= Current_roster.getTA_EXCLUDEGEOLOCATION();

            TMMode = Current_roster.getTA_LOGINMODE();


        }catch(Exception ex){}

        try {

            bundle.putString("RecordNo", Current_roster.getRecordNo());
            bundle.putString("acceptedbyStaff",Current_roster.getAcceptedbyStaff());
            bundle.putString("Server", "" + Server_Available);
            bundle.putString("StartTime", Current_roster.getStart_Time());
            bundle.putString("EndTime", Current_roster.get_End_Time());
        }catch(Exception ex){}

        try{
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();
            File fileDir=null;
            fileDir = new File(froot.getAbsolutePath()+"/.server/");
            File fXmlFile = new File(fileDir, "Recipient.xml");
            //  rosters=new ArrayList<Roster_Info>();
            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Recipient");
                if (nList == null) return null;

                String AccountNo = "";
                String current_element = "Total elements :" + nList.getLength();
                //  textMsg.setText("RecordNo : " + Current_roster.getRecordNo());




                String Address = "";
                for (int temp = 0; temp < nList.getLength(); temp++) {

                    try {
                        Node nNode = nList.item(temp);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            current_element = eElement.getNodeValue();
                            AccountNo = eElement.getElementsByTagName("AccountNo").item(0).getTextContent();


                            if (AccountNo.equals(Current_roster.getActual_Client_Code())) {
                                if (AccountNo.equals("!MULTIPLE") && Current_roster.getRoster_type().equals("11") && !Current_roster.getAddress().equals("-")) {
                                    Address = Current_roster.getAddress();
                                    bundle.putString("Addresses", Address);
                                    bundle.putString("Simple_Address", Address);
                                } else {
                                    bundle.putString("Addresses", eElement.getElementsByTagName("Addresses").item(0).getTextContent());
                                    bundle.putString("Simple_Address", eElement.getElementsByTagName("Simple_Address").item(0).getTextContent());
                                }
                                bundle.putString("AccountNo", AccountNo);
                                bundle.putString("Personid", eElement.getElementsByTagName("Personid").item(0).getTextContent());
                                bundle.putString("Phone_Numbers", eElement.getElementsByTagName("Phone_Numbers").item(0).getTextContent());
                                bundle.putString("Roster_Alerts", eElement.getElementsByTagName("Roster_Alerts").item(0).getTextContent());
                                bundle.putString("Runsheet_Alerts", eElement.getElementsByTagName("Runsheet_Alerts").item(0).getTextContent());
                                // bundle.putString("servicesetting",eElement.getElementsByTagName("servicesetting").item(0).getTextContent());
                                bundle.putString("Notes", eElement.getElementsByTagName("Notes").item(0).getTextContent());
                                bundle.putString("PinCode", eElement.getElementsByTagName("PinCode").item(0).getTextContent());


                                break;
                            }

                        }
                    } catch (Exception aE) {
                        //  textMsg.setText("Error 2" + current_element + " " +  aE.toString());
                    }

                }
            }

            if (Current_roster.getActual_Client_Code().equalsIgnoreCase("!MULTIPLE") || Current_roster.getActual_Client_Code().equalsIgnoreCase("!INTERNAL"))
                bundle.putString("Personid",Current_roster.getServiceSetting());

            bundle.putString("Actual_Client_Code",Current_roster.getActual_Client_Code());

            if (Current_roster!=null)
                bundle.putString("Time_String", Current_roster.getStart_Time() + "," +  Current_roster.get_Calculated_Duration());

            bundle.putString("root",root);
            bundle.putString("User",UserName);
            bundle.putString("TMMode",TMMode);

            bundle.putString("roster_type",Current_roster.getRoster_type());



            bundle.putString("KMAgainstTravelOnly",KMAgainstTravelOnly);
            bundle.putString("TAMode",TAMode);
            bundle.putString("AllowSetTime",AllowSetTime);
            bundle.putString("AllowPicUpload",AllowPicUpload);
            bundle.putString("RecipientDocFolder",RecipientDocFolder);

            bundle.putString("RosterDate",Current_roster.getRoster_Date());
            bundle.putString("UserId",UserId);
            bundle.putString("MobileIncident",MobileIncident);
            bundle.putString("mobilegeocodelimit",mobilegeocodelimit);
            bundle.putString("StaffCode",StaffCode);
            bundle.putString("ServiceType",Current_roster.getServiceType());
            bundle.putString("OperatorId",OperatorId);
            bundle.putString("Security_Token",Security_Token);
            bundle.putString("Apply_Goe_Location_Setting",Apply_Goe_Location_Setting);
            bundle.putString("Program",Current_roster.getProgram());
            bundle.putString("RosterDate",tdate.getTag().toString());
            bundle.putString("Exclude_Goe_Location_Setting",Exclude_Goe_Location_Setting);
            bundle.putBoolean("Process_Sleep_Over",Process_Sleep_Over);
            bundle.putString("MinorGroup",Current_roster.getMinorGroup());



            if (Current_roster.getMTAServiceType().equalsIgnoreCase(""))
                bundle.putString("MTAServiceType",Current_roster.getServiceType());
            else
                bundle.putString("MTAServiceType",Current_roster.getMTAServiceType());

            bundle.putBoolean("View_only",View_only);

            bundle.putString("RECIPIENT_COORDINATOR",RECIPIENT_COORDINATOR);
            bundle.putString("MobileFutureLimit",MobileFutureLimit);
            bundle.putString("Staff_Coordinator_Email",Staff_Coordinator_Email);
            try {
                bundle.putString("MyOwnCarStatus", Current_roster.getMyOwnCarStatus());
            }catch (Exception ex){}

            try {
                bundle.putString("Service_Setting", Current_roster.getServiceSetting());
            }catch (Exception ex){}

            try {
                bundle.putString("RunsheetAlerts", Current_roster.getRunsheetAlerts());
            }catch (Exception ex){}




        } catch (Exception aE) {
            ((TextView) findViewById(R.id.TextDate)).setText("Error 3 : " + aE.getMessage());
        }

        return bundle;

    }
    public String getRecipient_Address(String ClientCode){

        String Address = "";
        String Simple_Address="";

        try{
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();
            File fileDir=null;
            fileDir = new File(froot.getAbsolutePath()+"/.server/");
            File fXmlFile = new File(fileDir, "Recipient.xml");
            //  rosters=new ArrayList<Roster_Info>();
            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Recipient");
                if (nList == null) return null;

                String AccountNo = "";
                String current_element = "Total elements :" + nList.getLength();
                //  textMsg.setText("RecordNo : " + Current_roster.getRecordNo());

                for (int temp = 0; temp < nList.getLength(); temp++) {

                    try {
                        Node nNode = nList.item(temp);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            current_element = eElement.getNodeValue();
                            AccountNo = eElement.getElementsByTagName("AccountNo").item(0).getTextContent();


                            if (AccountNo.equalsIgnoreCase(ClientCode)) {

                                Address= eElement.getElementsByTagName("Addresses").item(0).getTextContent();
                                Simple_Address=eElement.getElementsByTagName("Simple_Address").item(0).getTextContent();
                            }


                            break;
                        }


                    } catch (Exception aE) {
                        //  textMsg.setText("Error 2" + current_element + " " +  aE.toString());
                    }

                }
            }





        } catch (Exception aE) {
            ((TextView) findViewById(R.id.TextDate)).setText("Error 3 : " + aE.getMessage());
        }

        return Address;

    }

    public void Shift_Detail(){

        try {


            Exclude_Goe_Location_Setting= Current_roster.getTA_EXCLUDEGEOLOCATION();

            TMMode = Current_roster.getTA_LOGINMODE();

        }catch(Exception ex){}


        if (Server_Available==false )
        {
            Shift_Detail2();
            return;
        }



        // Toast.makeText(getApplicationContext(),"Starting Activity"  , Toast.LENGTH_SHORT).show();
      /* if(Current_roster!=null){
    	   Toast.makeText(getApplicationContext(),"Current Roster not set properly " + METHOD_NAME3  , Toast.LENGTH_SHORT).show();
    	  return;
       }*/
        TextView tdate=   ((TextView) findViewById(R.id.TextDate));

        try {

            TextView tv;

            tv= ((TextView)findViewById(R.id.TextDate));

            if (tv.getText()=="" )
                return;
            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME3);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL3);
            androidHttpTransport.debug =true;

            PropertyInfo pi3=new PropertyInfo();
            pi3.setName("AccountNo");

            String message = Current_roster.getActual_Client_Code(); //tv.getTag().toString();
            pi3.setValue(getSecurityToken() + message);
            request.addProperty(pi3);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION3, envelope);

            SoapObject  result =(SoapObject)envelope.getResponse();

            SoapPrimitive obj;
            Intent intent = new Intent(this, Shift_Detail.class);
            Bundle bundle = new Bundle();
            bundle.putString("RecordNo",Current_roster.getRecordNo());
            bundle.putString("Server","True");
            bundle.putString("StartTime",Current_roster.getStart_Time());
            bundle.putString("EndTime",Current_roster.get_End_Time());

            // Toast.makeText(getApplicationContext(), "Result = " + result.getPropertyCount() + " Attr = " + result.getAttributeCount(), Toast.LENGTH_LONG).show();

            if (result!=null && result.getPropertyCount()>0){
                //for (int j=0; j<result.getPropertyCount(); j++)       {
                for (int j=0; j<1; j++)       {
                    SoapObject   result2 =(SoapObject)result.getProperty(j);
                    for(int i=0 ; i<result2.getPropertyCount(); i++)
                    {
                        try{
                            obj=(SoapPrimitive)result2.getProperty("AccountNo");
                            bundle.putString("AccountNo",obj.toString());

                            obj=(SoapPrimitive)result2.getProperty("Personid");
                            bundle.putString("Personid",obj.toString());

                            //   Toast.makeText(getApplicationContext(), "Personid: " + obj.toString() , Toast.LENGTH_LONG).show();

                            obj=(SoapPrimitive)result2.getProperty("Addresses");
                            if (obj.toString()==null)
                                bundle.putString("Addresses","-");
                            else
                                bundle.putString("Addresses",obj.toString());

                            obj=(SoapPrimitive)result2.getProperty("Phone_Numbers");
                            if (obj.toString()==null)
                                bundle.putString("Phone_Numbers","-");
                            else
                                bundle.putString("Phone_Numbers",obj.toString());

                            obj=(SoapPrimitive)result2.getProperty("Roster_Alerts");
                            if (obj.toString()==null)
                                bundle.putString("Roster_Alerts","-");
                            else
                                bundle.putString("Roster_Alerts",obj.toString());


                            obj=(SoapPrimitive)result2.getProperty("Runsheet_Alerts");
                            if (obj.toString()==null)
                                bundle.putString("Runsheet_Alerts","-");
                            else
                                bundle.putString("Runsheet_Alerts",obj.toString());

                            obj=(SoapPrimitive)result2.getProperty("Notes");
                            if (obj.toString()==null)
                                bundle.putString("Notes","-");
                            else
                                bundle.putString("Notes",obj.toString());

                            obj=(SoapPrimitive)result2.getProperty("PinCode");
                            if (obj.toString()==null)
                                bundle.putString("PinCode","-");
                            else
                                bundle.putString("PinCode",obj.toString());

                            try{
                                if (result2.getProperty("Simple_Address")!=null){
                                    obj=(SoapPrimitive)result2.getProperty("Simple_Address");
                                    if (obj.toString()==null)
                                        bundle.putString("Simple_Address","-");
                                    else
                                        bundle.putString("Simple_Address",obj.toString());
                                }
                            }catch(Exception ex){}



                        }catch(Exception ex){}
                    }
                }
            }


            if (result==null|| result.getPropertyCount()==0){
                bundle.putString("AccountNo",Current_roster.getActual_Client_Code());
                bundle.putString("Personid","0");
                bundle.putString("Addresses","-");
                bundle.putString("Phone_Numbers","-");
                bundle.putString("Roster_Alerts","-");
                bundle.putString("Runsheet_Alerts","-");
                bundle.putString("PinCode","0");
                bundle.putString("Simple_Address","-");

            }

            if (Current_roster.getActual_Client_Code().equalsIgnoreCase("!MULTIPLE") || Current_roster.getActual_Client_Code().equalsIgnoreCase("!INTERNAL"))
                bundle.putString("Personid",Current_roster.getServiceSetting());

            bundle.putString("Actual_Client_Code",Current_roster.getActual_Client_Code());

            if (Current_roster!=null){
                bundle.putString("Time_String", Current_roster.getStart_Time() + "," +  Current_roster.get_Calculated_Duration());
                if (Current_roster.getNotes()==null)
                    bundle.putString("Notes","-");
                else
                    bundle.putString("Notes",Current_roster.getNotes());
            }
            bundle.putString("root",root);
            bundle.putString("User",UserName);
            bundle.putString("TMMode",TMMode);
            if (RecipientDocFolder==null)
                bundle.putString("RecipientDocFolder","-");
            else
                bundle.putString("RecipientDocFolder",RecipientDocFolder);


            bundle.putString("roster_type",Current_roster.getRoster_type());
            bundle.putString("KMAgainstTravelOnly",KMAgainstTravelOnly);
            bundle.putString("AllowSetTime",AllowSetTime);
            bundle.putString("TAMode",TAMode);
            bundle.putString("AllowPicUpload",AllowPicUpload);
            bundle.putString("UserId",UserId);
            bundle.putString("MobileIncident",MobileIncident);
            bundle.putString("mobilegeocodelimit",mobilegeocodelimit);
            bundle.putString("StaffCode",StaffCode);
            bundle.putString("ServiceType",  Current_roster.getServiceType());
            bundle.putString("OperatorId",OperatorId);
            bundle.putString("Security_Token",Security_Token);
            bundle.putString("Apply_Goe_Location_Setting",Apply_Goe_Location_Setting);
            bundle.putString("Program",Current_roster.getProgram());
            bundle.putString("RosterDate",tdate.getTag().toString());
            bundle.putString("Exclude_Goe_Location_Setting",Exclude_Goe_Location_Setting);
            bundle.putBoolean("Process_Sleep_Over",Process_Sleep_Over);

            // Toast.makeText(getApplicationContext(), "Date Posted: " + tdate.getTag().toString() , Toast.LENGTH_LONG).show();


            intent.putExtras(bundle);


            startActivityForResult(intent, STATIC_INTEGER_VALUE);
            // progressBar.dismiss();

        } catch (Exception ex) {

            Tost_Message("No Name and Address Detail exists for this Receipient ");
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == STATIC_INTEGER_VALUE) {
            if (resultCode == Activity.RESULT_OK) {
                // A contact was picked.  Here we will just display it
                // to the user.
                //Tost_Message("onActivityResult last_indx= " + last_indx );
            }
        }


        if (requestCode == r_code) {

            try {
                if (Server_Available && Roster_Availability_Main.data_changed) {
                    Roster_Availability_Main.data_changed = false;
                    new MyAsyncClass3().execute();
                }

            } catch (Exception ex) {
            }

            return;
        }

        if (requestCode == Attendee_code) {
            Attendee_code = 0;
            try {

                doService2();
                Current_roster = rosters.get(Current_index);
                Group_shift = Current_roster.getActual_Client_Code().equalsIgnoreCase("!MULTIPLE");

                show_Group_Recipients(listView);
            } catch (Exception ex) {
            }

            return;
        }


    }
    public void fill_Receipients2(){
        recipients= new ArrayList<String>();
        recipients.add(StaffCode);
        recipients.add("No More");
    }
    public void fill_Receipients(){

        if ((Server_Available==false) || (!Timesheet.equals("999")))
        // if ((Server_Available==false) )
        {
            fill_Receipients2();
            return;
        }

        try {

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME2);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL2);
            androidHttpTransport.debug =true;

            PropertyInfo pi2=new PropertyInfo();
            pi2.setName("Fontra");
            pi2.setValue(getSecurityToken() + "99");
            request.addProperty(pi2);

            PropertyInfo pi3=new PropertyInfo();
            pi3.setName("client");
            pi3.setValue(false);
            request.addProperty(pi3);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

           /*

            Credentials credentials = new Credentials() {

				@Override
				public Principal getUserPrincipal() {
					// TODO Auto-generated method stub
					return  null;
				}


				public String getUser() {
					// TODO Auto-generated method stub
					return "arshad";
				}
				@Override
				public String getPassword() {
					// TODO Auto-generated method stub
					return "arshad786";
				}
			} ;


            envelope.addMapping(NAMESPACE, "UserCredentials", credentials.getClass());

            */

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION2, envelope);

            SoapObject  result =(SoapObject)envelope.getResponse();
            int n=result.getPropertyCount();

            SoapPrimitive obj;
            recipients= new ArrayList<String>();
            //  rosters=new ArrayList<Roster_Info>();
            for (int j=0; j<result.getPropertyCount(); j++)       {
                obj=(SoapPrimitive)result.getProperty(j);
                recipients.add( obj.toString());
            }
            //    ((TextView) findViewById(R.id.TextDate)).setText(n);

        } catch (Exception aE) {
            ((TextView) findViewById(R.id.TextDate)).setText(aE.getMessage());
        }
    }
    public void fill_time() {
        try {
            int indx;
            int tim_count = 0;
            for (indx = 0; indx < total_item; indx++) {
                durationImages[indx] = 0;
                Accounts[indx] = -1;
                started[indx] = 0;
                completed[indx] = 0;

                //-Hour part of arry
                if ((tim_count * cell_size / 60) < 10)
                    items[indx] = "0" + (tim_count * cell_size / 60);
                else
                    items[indx] = "" + (tim_count * cell_size / 60);

                //Minutes part
                if ((tim_count * cell_size % 60) < 10)
                    items[indx] = items[indx] + ":0" + (tim_count * cell_size % 60);
                else
                    items[indx] = items[indx] + ":" + (tim_count * cell_size % 60);

                tim_count = tim_count + 1;

            }
        } catch (Exception ex) {
        }
    }



    public void doService2(){
        fill_time();
        clicked=0;
        clicked_indx=0;
        int indx=0;
        int color=0;
        int j=0;
        int totalblock=0;
        int item_count=0;
        Daily_Paid_Hours=0;
        Daily_Paid_KM=0.0;

        updateLabel();


        Roster_Info rst=null;
        try {

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();
            File fileDir=null;
            fileDir = new File(froot.getAbsolutePath()+"/.server/");
            File fXmlFile = null;
            if( Booking_view)
                fXmlFile= new File(fileDir, "booking.xml");
            else
                fXmlFile= new File(fileDir, "traccs.xml");

            rosters=null;
            rosters=new ArrayList<Roster_Info>();



            if (fXmlFile.exists()){


                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Roster_Info");

                // Toast.makeText(getApplicationContext(),"node list = " + nList.getLength() , Toast.LENGTH_LONG).show();

                if (nList==null) return;
                //textMsg.setText("\nCurrent Element :" + nList.item(0).getNodeName());

                String str_date="";
                //TextView dtp=(TextView)findViewById(R.id.TextDate);
                //String sel_date[]=dtp.getTag().toString().split("/");
                //String selected_date= RosterDate ;// Integer.parseInt( sel_date[2]) + "/" + Integer.parseInt( sel_date[1]) + "/" + sel_date[0];

                //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                //Date date = new Date(dtp.getTag().toString());
                //selected_date = dateFormat.format(date);
                String Roster_Date="";
                int index=0;
                Total_Rosters=0;

                settings.edit().putString("Started_Job_No", "0").commit();
                for ( int tmp = 0; tmp< nList.getLength(); tmp++) {
                    try{

                        Node  nNode = nList.item(tmp);
                        //  textMsg.setText(nNode.getTextContent());
                        //  textMsg.setText("\nCurrent Element :" + nNode.getNodeName());

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            str_date= eElement.getElementsByTagName("Roster_Date").item(0).getTextContent();
                            String code=eElement.getElementsByTagName("Carer_code").item(0).getTextContent();
                            //code=objText.decryptText(code);

                            if (str_date == "2026/04/07"){
                                rst.setStart_Time(eElement.getElementsByTagName("Start_Time").item(0).getTextContent());
                            }

                            DateFormat dateFormatN = new SimpleDateFormat("dd/MM/yyyy");

                            DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");

                            Date date2 = new Date(str_date);
                            str_date = dateFormat2.format(date2);

                            Roster_Date=dateFormatN.format(date2);
//                            RosterDate=Roster_Date;
                            String Roster_Type="";
                            String InfoOnly="0";
                            try {
                                // textMsg.setText(textMsg.getText() + "\n" + str_date + " = " + selected_date + " " +  selection.getText().equals(code));
                                Roster_Type = eElement.getElementsByTagName("Roster_Type").item(0).getTextContent();
                                InfoOnly = eElement.getElementsByTagName("InfoOnly").item(0).getTextContent();
                            }catch(Exception ex){}
                            //Integer.parseInt(Roster_Type)>2

                            if ( Booking_view==true && !code.trim().equals("BOOKED") ){
                                continue;
                            }

                            if (Booking_view==false && code.trim().equals("BOOKED") ){
                                continue;
                            }

                            if (Show_all_roster==false && (Roster_Type.equals("9") || Roster_Type.equals("13") || InfoOnly.equalsIgnoreCase("true") ) ){
                                continue;
                            }


                            if ( (str_date.equals(RosterDate) || Booking_view ) || code.trim().equals("BOOKED"))
                            {
                                rst= new Roster_Info();
                                rst.setRecordNo(eElement.getElementsByTagName("RecordNo").item(0).getTextContent());
                                //  btnLoadMoreBooking= btnLoadMoreBooking=(Button)findViewById(R.id.btnLoadMoreBooking);
                                rst.setIndex(index);
                                index=index+1;
                                if(Booking_view) {
                                    Max_RecordNo = rst.getRecordNo();
                                }else {
                                    Max_RecordNo = "0";
                                }

                                if(code.trim().equals("BOOKED")) {
                                    rst.setBook_date("Book Date : " + Roster_Date + " Time: ");
                                }else {
                                    rst.setBook_date("");
                                }


                                rst.setRoster_Date(str_date);
                                rst.setCarer_code(code);
                                rst.setServiceType(eElement.getElementsByTagName("ServiceType").item(0).getTextContent());

                                if ( eElement.getElementsByTagName("Service_Detail").item(0).getTextContent()!=null)
                                    rst.setService_Detail(eElement.getElementsByTagName("Service_Detail").item(0).getTextContent());
                                else
                                    rst.setService_Detail("-");


                                rst.setStart_Time(eElement.getElementsByTagName("Start_Time").item(0).getTextContent());
                                rst.setDuration(eElement.getElementsByTagName("Duration").item(0).getTextContent());
                                rst.setCarer_code(code); // eElement.getElementsByTagName("Carer_code").item(0).getTextContent());

                                rst.setClient_code(eElement.getElementsByTagName("Client_code").item(0).getTextContent());
                                rst.setProgram(eElement.getElementsByTagName("Program").item(0).getTextContent());

                                rst.setDayNo(Integer.parseInt(eElement.getElementsByTagName("DayNo").item(0).getTextContent()));
                                rst.setMonthNo(Integer.parseInt(eElement.getElementsByTagName("Monthno").item(0).getTextContent()));
                                rst.setYearNo(Integer.parseInt(eElement.getElementsByTagName("YearNo").item(0).getTextContent()));
                                rst.setBlockNo(Integer.parseInt(eElement.getElementsByTagName("blockNo").item(0).getTextContent()));

                                if (eElement.getElementsByTagName("Notes").item(0).getTextContent()!=null) {
                                    rst.setNotes(eElement.getElementsByTagName("Notes").item(0).getTextContent());
                                } else{
                                    rst.setNotes("-");}

                                rst.setRoster_type(eElement.getElementsByTagName("Roster_Type").item(0).getTextContent());
                                rst.setStarted(eElement.getElementsByTagName("Started").item(0).getTextContent());

                                if (eElement.getElementsByTagName("Completed").item(0).getTextContent()!=null) {
                                    rst.setCompleted(eElement.getElementsByTagName("Completed").item(0).getTextContent());
                                }else {
                                    rst.setCompleted("0");
                                }

                                if (rst.getStarted().equals("1") && rst.getCompleted().equals("0") ){
                                    settings.edit().putString("Started_Job_No", rst.getRecordNo()).commit();

                                }

                                rst.setActual_Client_Code(eElement.getElementsByTagName("Actual_Client_Code").item(0).getTextContent());

                                try {
                                    rst.setServiceSetting(eElement.getElementsByTagName("servicesetting").item(0).getTextContent());

                                }catch(Exception ex){}



                                try {
                                    rst.setTA_LOGINMODE(eElement.getElementsByTagName("TA_LOGINMODE").item(0).getTextContent());
                                    rst.setTA_EXCLUDEGEOLOCATION(eElement.getElementsByTagName("TA_EXCLUDEGEOLOCATION").item(0).getTextContent());
                                    rst.setGroup_Alerts(eElement.getElementsByTagName("Group_Alerts").item(0).getTextContent());

                                    rst.setKM(eElement.getElementsByTagName("KM").item(0).getTextContent());
                                }catch(Exception ex){}

                                try{
                                    rst.setInfoOnly(eElement.getElementsByTagName("InfoOnly").item(0).getTextContent());
                                }catch(Exception ex){}

                                try{
                                    rst.setAddress(eElement.getElementsByTagName("Address").item(0).getTextContent());
                                }catch(Exception ex){}

                                try{
                                    rst.setDebtor(eElement.getElementsByTagName("Debtor").item(0).getTextContent());
                                }catch(Exception ex){}

                                try{
                                    rst.setDebtor(eElement.getElementsByTagName("Debtor").item(0).getTextContent());
                                }catch(Exception ex){}

                                try{
                                    rst.setFee(eElement.getElementsByTagName("Fee").item(0).getTextContent());
                                }catch(Exception ex){}

                                try{
                                    rst.setDisplayFeeInApp(eElement.getElementsByTagName("DisplayFeeInApp").item(0).getTextContent());
                                }catch(Exception ex){}

                                try{
                                    rst.setDisplayDebtorInApp(eElement.getElementsByTagName("DisplayDebtorInApp").item(0).getTextContent());
                                }catch(Exception ex){}

                                try {

                                    rst.set_ACCOUNTINGIDENTIFIER(eElement.getElementsByTagName("ACCOUNTINGIDENTIFIER").item(0).getTextContent());
                                } catch (Exception ex) {
                                }

                                try{

                                    int type= Integer.parseInt(rst.getRoster_type());

                                    if ((type==13 || type==14 || type==15) || (rst.getInfoOnly().equalsIgnoreCase("true") || rst.getInfoOnly().equalsIgnoreCase("1"))) {
                                    }else{
                                        Daily_Paid_Hours=Daily_Paid_Hours + Integer.parseInt(rst.getDuration());

                                    }

                                    if (type == 9){
                                        Daily_Paid_KM=Daily_Paid_KM+Double.parseDouble(rst.getKM());
                                    }

                                }catch(Exception ex){}

                                try{

                                    rst.setMinorGroup(eElement.getElementsByTagName("MinorGroup").item(0).getTextContent());
                                }catch(Exception ex){}

                                try{

                                    rst.setMTAServiceType(eElement.getElementsByTagName("MTAServiceType").item(0).getTextContent());
                                }catch(Exception ex){}

                                try{

                                    rst.setMyOwnCarStatus(eElement.getElementsByTagName("MyOwnCarStatus").item(0).getTextContent());
                                }catch(Exception ex){}

                                try{

                                    rst.setDisable_Shift_Start_Alarm(eElement.getElementsByTagName("Disable_Shift_Start_Alarm").item(0).getTextContent());
                                }catch(Exception ex){}

                                try{

                                    rst.setDisable_Shift_End_Alarm(eElement.getElementsByTagName("Disable_Shift_End_Alarm").item(0).getTextContent());
                                }catch(Exception ex){}

                                try{

                                    rst.setTA_MultiShift(eElement.getElementsByTagName("TA_MultiShift").item(0).getTextContent());
                                }catch(Exception ex){}

                                try{

                                    rst.setRunsheetAlerts(eElement.getElementsByTagName("RunsheetAlerts").item(0).getTextContent());
                                }catch(Exception ex){}
                                try{

                                    rst.setPersonId(eElement.getElementsByTagName("PersonId").item(0).getTextContent());
                                }catch(Exception ex){}
                                try{

                                    rst.setExcludeFromAppLogging(eElement.getElementsByTagName("ExcludeFromAppLogging").item(0).getTextContent());
                                }catch(Exception ex){}

                                try{
                                    rst.setForceShiftReport(eElement.getElementsByTagName("ForceShiftReport").item(0).getTextContent());
                                }catch(Exception ex){}

                                try{
                                    rst.setHasServiceNotes(eElement.getElementsByTagName("HasServiceNotes").item(0).getTextContent());
                                }catch(Exception ex){}

                                try{
                                    rst.setAcceptedbyStaff(eElement.getElementsByTagName("acceptedbyStaff").item(0).getTextContent());
                                }catch(Exception ex){}


                                try{
                                    rst.setNewAcceptDate(eElement.getElementsByTagName("NewAcceptDate").item(0).getTextContent());
                                }catch(Exception ex){}


                                try {
                                    String val2="";
                                    rst.setActual_Start_Time("");
                                    rst.setActual_End_Time("");

                                    val2 = eElement.getElementsByTagName("Actual_Start").item(0).getTextContent();
                                    rst.setActual_Start_Time(val2);

                                    val2 = eElement.getElementsByTagName("Actual_End").item(0).getTextContent();
                                    rst.setActual_End_Time(val2);

                                }catch(Exception ex){ }


                                try{
                                    if(eElement.getElementsByTagName("Group_Recipients")==null) continue;
                                    List<Group_Recipient> lst= new ArrayList<Group_Recipient>() ;
                                    Group_Recipient rg=null;
                                    NodeList nlst=eElement.getElementsByTagName("Group_Recipient");
                                    for(int ind=0; ind<nlst.getLength(); ind++) {
                                        Node  nNod = nlst.item(ind);
                                        Element eElm = (Element) nNod;
                                        rg= new Group_Recipient();

                                        String val="";
                                        val = eElm.getElementsByTagName("Name").item(0).getTextContent();
                                        rg.setName(val);
                                        val = eElm.getElementsByTagName("AccountNo").item(0).getTextContent();
                                        rg.setAccountNo(val);
                                        val = eElm.getElementsByTagName("RecordNo").item(0).getTextContent();
                                        rg.setRecordNo(val);
                                        val = eElm.getElementsByTagName("RosterType").item(0).getTextContent();
                                        rg.setRosterType(val);
                                        val = eElm.getElementsByTagName("PickUpAddress1").item(0).getTextContent();
                                        rg.setPickUpAddress1(val);

                                        try{
                                            val = eElm.getElementsByTagName("DropOffAddress1").item(0).getTextContent();
                                            rg.setDropOffAddress1(val);
                                        }catch(Exception ex){ rg.setDropOffAddress1("-");}
                                        try {
                                            val = eElm.getElementsByTagName("Status").item(0).getTextContent();
                                            rg.setStatus(val);
                                        }catch(Exception ex){ rg.setStatus("1");}

                                        val = eElm.getElementsByTagName("Coordinator_Email").item(0).getTextContent();
                                        rg.setCoordinator_Email(val);

                                        val = eElm.getElementsByTagName("RECIPIENT_CoOrdinator").item(0).getTextContent();
                                        rg.setRECIPIENT_CoOrdinator(val);

                                        try {
                                            val = eElm.getElementsByTagName("Personid").item(0).getTextContent();
                                            rg.set_Personid(val);
                                        }catch(Exception ex){ rg.set_Personid("0");}
                                        try {
                                            val = eElm.getElementsByTagName("Mobility").item(0).getTextContent();
                                            rg.setMobility(val);
                                        }catch(Exception ex){ rg.setMobility("-");}
                                        try {
                                            val = eElm.getElementsByTagName("Start_Time").item(0).getTextContent();
                                            rg.setStart_Time(val);
                                        }catch(Exception ex){ rg.setStart_Time("-");}

                                        try {
                                            val = eElm.getElementsByTagName("ServiceSettings").item(0).getTextContent();
                                            rg.setServiceSetting(val);
                                        }catch(Exception ex){ rg.setServiceSetting("-");}

                                        try {
                                            val = eElm.getElementsByTagName("roster_date").item(0).getTextContent();
                                            rg.setRoster_Date(val);
                                        }catch(Exception ex){ rg.setRoster_Date("-");}

                                        lst.add(rg);

                                    }
                                    rst.set_group_Recipients(lst);

                                }catch(Exception ex){}

                            }
                            else{
                                continue;
                            }
                        }else { continue; }
                    }  catch (Exception aE) {


                    }

                    item_count=item_count+1;
                    rosters.add(rst);

                    if( Check_Start_Time(rst.getStart_Time(), rst.get_End_Time())==true)
                        Current_roster=rst;
                }

                if ( Booking_view==true && rosters.size()<1){
                    Max_RecordNo = "0";
                }

                if (rosters!=null) {
                    // listView.setAdapter(new Main_Dairy_Adapter(rosters));
                    Total_Rosters=item_count;
                    Main_Dairy_Adapter mAdapter = new Main_Dairy_Adapter(rosters, this );
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    listView.setLayoutManager(mLayoutManager);
                    if(flagDecoration) {
                        listView.setItemAnimator(new DefaultItemAnimator());
                        listView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
                        flagDecoration=false;
                    }

                    listView.setAdapter(mAdapter);

                }

            } else { }//textMsg.setText("Xml file not found"); }
        } catch (Exception e) {
            Tost_Message(e.toString());

        }
    }


    public int calculate_end_time_Position(String duration)
    {
        String[] str1 =duration.split(",");
        String[] str2 =str1[0].split(":");
        int tim=(Integer.parseInt(str2[0])*60)/15;
        tim=tim+Integer.parseInt(str2[1])/15;
        return tim;
    }

    private String getStart_Actual_Time(String stime){
        String StrTime="";
        String[] sta =stime.split(":");
        int tim=Integer.parseInt(sta[1]);
        // tim=tim-tim%15;

        int rem_time=tim%cell_size;

        if (rem_time==0)
            StrTime=stime;
        else
            StrTime= sta[0] + ":" + set_leading_zero( (tim-rem_time),2);

        return StrTime;
    }

    public void ShowProgressBar(View v ) {
        Tost_Message("Loading Detail"  );
        Shift_Detail2();

        if (true) return;

        // prepare for a progress bar dialog
        progressBar = new ProgressDialog(v.getContext());
        progressBar.setCancelable(true);
        progressBar.setMessage("Processing please wait ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        //reset progress bar status
        progressBarStatus = 0;
        progressBar.setProgress(10);


        new Thread(new Runnable() {
            public void run() {
                while (progressBarStatus < 100) {

                    // process some tasks

                    progressBar.setProgress(50);

                    Shift_Detail2();
                    progressBarStatus=100;
                    // your computer is too fast, sleep 1 second
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Update the progress bar
                    progressBarHandler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressBarStatus);
                        }
                    });
                }

                // ok, file is downloaded,
                if (progressBarStatus >= 100) {

                    // sleep 2 seconds, so that you can see the 100%
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // close the progress bar dialog
                    progressBar.dismiss();
                }
            }
        }).start();

    }
    Roster_Info getStarted_Job(){
        int indx=0;

        total_item= rosters.size();
        Roster_Info rst=null;
        while(indx<total_item)
        {
            rst=rosters.get(indx);
            //Integer.parseInt(rst.getStarted())<=0

            // if (rst.getStarted().equalsIgnoreCase("true") && rst.getCompleted().equalsIgnoreCase("false") )
            if (Integer.parseInt(rst.getStarted())>0 && Integer.parseInt(rst.getCompleted())<=0 )
            {

                Current_roster=rst;

                break;
            }
            indx=indx+1;

        }
        return rst;
    }
    void set_first_Item_InGrid()
    {
        try{
            int indx=0;
            listView =  findViewById(R.id.listViewMain);
            total_item= rosters.size();
            Roster_Info rst=null;
            while(indx<total_item)
            {
                rst=rosters.get(indx);
                //Integer.parseInt(rst.getStarted())<=0

                if (Integer.parseInt(rst.getCompleted())<=0
                        && Check_valid_Duration(rst.getStart_Time(), rst.get_End_Time())==true   )
                {
                    //listView.setSelection(indx);
                    Current_roster=rst;
                    break;
                }
                indx=indx+1;

            }
        } catch (Exception ex) {}
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

            // Toast.makeText(getApplicationContext(),"curr_time " + curr_time + " start=" + start + " end=" + end , Toast.LENGTH_LONG).show();


            if ( date1.compareTo(start)>=0 && date1.compareTo(end)<0)
                sts=true;

        }catch (Exception ex){
            // Toast.makeText(getApplicationContext(),ex.toString() , Toast.LENGTH_LONG).show();
        }

        return sts;
    }
    void Check_End_of_shift() {

        try {
            int indx = 0;
            listView = findViewById(R.id.listViewMain);
            total_item = rosters.size();
            Roster_Info rst = null;
            while (indx < total_item) {
                rst = rosters.get(indx);
                //Integer.parseInt(rst.getStarted())<=0

                if (Integer.parseInt(rst.getStarted()) > 0 && Integer.parseInt(rst.getCompleted()) <= 0
                        && Check_End_Duration(rst.getStart_Time(), rst.get_End_Time()) == true) {
                    if (Alarm_RosterNo == 0) {

                        //   Toast.makeText(getApplicationContext(), rst.getRecordNo() + " Current Shift time is ended, you have to end job", Toast.LENGTH_LONG).show();
                        //  Alarm_RosterNo= Long.parseLong(rst.getRecordNo());
                        String msg = "Time of Current Shift " + rst.getStart_Time() + " - " + rst.get_End_Time() + " has been ended, you have to end job";
                        if (Current_roster.getDisable_Shift_End_Alarm().equalsIgnoreCase("0") || Current_roster.getDisable_Shift_End_Alarm().equalsIgnoreCase("false"))
                            if (Alarm_RosterNo == 0) {
                                Alarm_RosterNo = 1;
                                // already_alarm_shown = true;
                                // if (show_MultiShift_Alarm(rst, 2))
                                show_alarm(msg);
                            }
                    }
                    break;
                }
                indx = indx + 1;

            }
        } catch (Exception ex) {
        }
    }

    void Check_Start_of_shift()
    {
        try{
            int indx=0;
            listView = findViewById(R.id.listViewMain);
            total_item= rosters.size();
            Roster_Info rst=null;
            while(indx<total_item)
            {
                rst=rosters.get(indx);
                //Integer.parseInt(rst.getStarted())<=0

                if (Integer.parseInt(rst.getStarted())<=0 && Integer.parseInt(rst.getCompleted())<=0
                        && Check_Start_Time(rst.getStart_Time(), rst.get_End_Time())==true   )
                {
                    if (Alarm_RosterNo==0){
                        //   Toast.makeText(getApplicationContext(), rst.getRecordNo() + " Current Shift time is ended, you have to end job", Toast.LENGTH_LONG).show();
                        //  Alarm_RosterNo= Long.parseLong(rst.getRecordNo());
                        String msg ="Time of Current Shift " + rst.getStart_Time()+" - " + rst.get_End_Time() + " has been started, you have to start the job";
                        if (Current_roster.getDisable_Shift_Start_Alarm().equalsIgnoreCase("0") || Current_roster.getDisable_Shift_Start_Alarm().equalsIgnoreCase("false"))
                            if (Alarm_RosterNo==0){
                                Alarm_RosterNo=1;
                                //  already_alarm_shown=true;

                                // if(show_MultiShift_Alarm(rst,1))
                                show_alarm(msg);
                            }
                    }
                    break;
                }
                indx=indx+1;

            }
        } catch (Exception ex) {}
    }
    void show_alarm(String msg){

        try{

            if(already_alarm_shown) return;

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder.setTitle("Current Roster Alarm!");
            alertDialogBuilder
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            already_alarm_shown=true;
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

            already_alarm_shown=true;
            try{

                View_only = false;
                Intent intent = new Intent(this, Shift_Detail.class);
                Bundle bundle = getShift_Bundle();
                intent.putExtras(bundle);
                PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);



                Notification noti = new Notification.Builder(this)
                        .setContentTitle("Current Roster Alarm!" )
                        .setContentText(msg).setSmallIcon(R.drawable.ic_set_time2)
                        .setContentIntent(pIntent)
                        .addAction(R.drawable.ic_time_24dp, "View", pIntent)
                        .addAction(R.drawable.gback, "Diary", pIntent)
                        .addAction(R.drawable.ic_exit, "Cancel", pIntent).build();
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // hide the notification after its selected
                noti.flags |= Notification.FLAG_AUTO_CANCEL;

                notificationManager.notify(0, noti);


                  /*  //Define Notification Manager
                    NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                    //Define sound URI
                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.end_job )
                            .setContentTitle("Current Roster Alarm!")
                            .setContentText(msg)
                            .setSound(soundUri); //This sets the sound to play




                    //Display notification
                    notificationManager.notify(1, mBuilder.build());*/



            } catch (Exception e) {}


        }catch(Exception ex){}

        //
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

            // if  (Alarm_RosterNo==0)
            //   Toast.makeText(getApplicationContext(),"curr_time " + curr_time + " output = " + date1.compareTo(end)  , Toast.LENGTH_SHORT).show();


            if ( date1.compareTo(start)>=0 && date1.compareTo(end)>=0)
                sts=true;
            // else
            //  Alarm_RosterNo=0;

        }catch (Exception ex){
            // Toast.makeText(getApplicationContext(),ex.toString() , Toast.LENGTH_LONG).show();
        }

        return sts;
    }
    boolean Check_Start_Time(String start_time, String end_time){
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

            // if  (Alarm_RosterNo==0)



            if ( date1.compareTo(start)>=0 && date1.compareTo(end)<0 )
                sts=true;
            //  Toast.makeText(getApplicationContext(),"start " + start + "," + end + " output = " + (date1.compareTo(start)>=0 && date1.compareTo(end)<0)  , Toast.LENGTH_SHORT).show();
            // else
            //  Alarm_RosterNo=0;

        }catch (Exception ex){
            // Toast.makeText(getApplicationContext(),ex.toString() , Toast.LENGTH_LONG).show();
        }

        return sts;
    }
    public void getKMAgainstTravelOnly(String AccountNo)
    {
        try
        {
            String URL5 = root + "/TimeSheet.asmx?op=KMAgainstTravelOnly";
            String SOAP_ACTION5 =  "https://tempuri.org/KMAgainstTravelOnly";
            String METHOD_NAME5 = "KMAgainstTravelOnly";

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME5);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
            androidHttpTransport.debug =true;

            PropertyInfo pi=new PropertyInfo();
            pi.setName("Staff_AccountNo");

            pi.setValue(getSecurityToken() +  AccountNo);
            request.addProperty(pi);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION5, envelope);
            SoapPrimitive  result =(SoapPrimitive)envelope.getResponse();
            if (result==null)
                KMAgainstTravelOnly="false";
            else
                KMAgainstTravelOnly=result.toString();

        }catch(Exception ex){
            //textMsg.setText("Operation not done due to some server error\n" + ex.toString());
        }
    }
    public void showReminder2(Context v)
    {
        try{

            // get_Active_Device_Reminder2();
            // if (Reminders.length<=0) return;
            Intent intent = new Intent(v, Reminders_Activity.class);
            Bundle bundle = new Bundle();
            bundle.putString("root",root);
            bundle.putString("UserId",UserId);
            bundle.putString("Security_Token",Security_Token);
            bundle.putString("OperatorId",OperatorId);
            bundle.putString("StaffCode",StaffCode);
            bundle.putBoolean("Server_Available", Server_Available);
            bundle.putString("rosterDate",RosterDate);

            intent.putExtras(bundle);
            startActivity(intent);
            // timer2.cancel();
        } catch(Exception ex){}
    }

    void showReminder(Context context){


        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.device_reminders);
        dialog.setTitle("Reminders");
        dialog.setCanceledOnTouchOutside(false);
        display_alert=false;

        Button dialogButton = (Button) dialog.findViewById(R.id.btnExit);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                show_again=true;
                display_alert=true;
                try{
                    //new MainActivity.MyAsyncClass3().execute();

                } catch(Exception ex){}
                dialog.dismiss();

            }
        });

        Button dialogSave = (Button) dialog.findViewById(R.id.btnOk);
        // if button is clicked, close the custom dialog
        dialogSave.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                show_again=false;
                display_alert=true;
                try{
                    new MainActivity.MyAsyncClass3().execute();

                } catch(Exception ex){}

                dialog.dismiss();
            }
        });

        ListView	listView1 = (ListView) dialog.findViewById(R.id.lst_device_reminders);

        try{
            if (Reminders!=null){
                listView1.setAdapter(new Reminder_Adapter(this, Reminders, OperatorId, Security_Token, root));
            }
        }catch(Exception ex){}



		/*listView1.setOnItemClickListener(new OnItemClickListener() {
	       		@Override public void onItemClick(AdapterView<?> parent, View v,int position, long id)
	       	        {
	       			;

	        }});*/
        dialog.show();

    }

    private void setNeverSleepPolicy(){
        try{
            ContentResolver cr = getApplicationContext().getContentResolver();
            int set = android.provider.Settings.System.WIFI_SLEEP_POLICY_NEVER;
            android.provider.Settings.System.putInt(cr, android.provider.Settings.System.WIFI_SLEEP_POLICY, set);


            WifiApManager wifi= new WifiApManager(getApplicationContext());

            WifiConfiguration config=wifi.getWifiApConfiguration();

            wifi.setWifiApState(config, true);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
/*
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "MyWakelockTag");
            wakeLock.acquire();
*/


        }catch(Exception ex){}
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


    public  boolean isGPSEnabled() {
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
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


    public void get_Active_Device_Reminder(){


        try {


            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME4);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL4);
            androidHttpTransport.debug =true;

            PropertyInfo pi=new PropertyInfo();
            pi.setName("UserID");
            pi.setValue(getSecurityToken() + StaffCode);
            request.addProperty(pi);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;

            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION4, envelope);
            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath() + "/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "Reminder.xml");
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


         /*     SoapObject   result =(SoapObject)envelope.getResponse();
            SoapObject   result2=null;
            DeviceReminders dev=null;
            int n=result.getPropertyCount();
          Reminders=new DeviceReminders[n];
            for (int j=0; j<result.getPropertyCount(); j++)
            {

                try{

                    dev=new DeviceReminders();
                    result2=(SoapObject)result.getProperty(j);
                    dev.setRecordnumber(result2.getProperty("Recordnumber").toString()); // Recordnumber
                    dev.setUserID(result2.getProperty("UserID").toString()); // UserID
                    dev.setActivationDateTime(result2.getProperty("ActivationDateTime").toString()); // ActivationDateTime
                    dev.setDetail(result2.getProperty("Detail").toString()); // Detail
                    dev.setStatus(result2.getProperty("Status").toString()); // Status
                    try {

                        dev.setMessageGroup(result2.getProperty("MessageGroup").toString()); // MessageGroup
                        dev.setExternalID(result2.getProperty("ExternalID").toString()); // ExternalID
                        dev.setStaff(result2.getProperty("Staff").toString()); // Staff
                    }catch(Exception ex){}

                }catch(Exception ex){}

                Reminders[j]=dev;
            }*/
        } catch(Exception ex){
            // Toast.makeText(getApplicationContext(), "Device Reminder - "+ ex.toString(), Toast.LENGTH_LONG).show();
        }


    }
    public void get_Active_Device_Reminder2(){

        try {

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            File fileDir = null;
            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = new File(fileDir, "Reminder.xml");

            //  Button textMsg = ((Button) findViewById(R.id.txtAddress2));

            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("DeviceReminders");

                if (nList == null) return;
                DeviceReminders dev=null;
                int n=nList.getLength();
                if (n<=0) return;

                Reminders=new ArrayList<DeviceReminders>();
                for (int temp = 0; temp < n; temp++) {

                    try {
                        Node nNode = nList.item(temp);

                        dev = new DeviceReminders();
                        String value = "";
                        //  textMsg.setText("\nCurrent Element :" + nNode.getNodeName());
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            value = eElement.getElementsByTagName("Recordnumber").item(0).getTextContent();
                            dev.setRecordnumber(value);
                            value = eElement.getElementsByTagName("UserID").item(0).getTextContent();
                            dev.setUserID(value);
                            value = eElement.getElementsByTagName("ActivationDateTime").item(0).getTextContent();
                            dev.setActivationDateTime(value);
                            value = eElement.getElementsByTagName("Detail").item(0).getTextContent();
                            dev.setDetail(value);
                            value = eElement.getElementsByTagName("Status").item(0).getTextContent();
                            dev.setStatus(value);

                            try {

                                value = eElement.getElementsByTagName("MessageGroup").item(0).getTextContent();
                                dev.setMessageGroup(value);
                                value = eElement.getElementsByTagName("ExternalID").item(0).getTextContent();
                                dev.setExternalID(value);
                                value = eElement.getElementsByTagName("Staff").item(0).getTextContent();
                                dev.setStaff(value);


                            }catch(Exception ex){}
                            Reminders.add(dev);
                        }


                    } catch (Exception aE) {
                        // ((Button) findViewById(R.id.txtAddress2)).setText(aE.toString());
                    }
                }
            } else {
                //  textMsg.setText("Xml for Task List file not found");
            }
        } catch (Exception aE) {
            //  ((Button) findViewById(R.id.txtAddress2)).setText(aE.toString());
        }



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
            if(Server_Available==false && connected ){
                // timer.cancel();

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
    void Remove_session(){
        try {

            if (Server_Available == false) return;


            String path = root + "/index.aspx?logout=1&user=" + UserName;

            Volly_Post_Request(path);

        }catch(Exception ex){}
    }
    void Volly_Post_Request(String url_String){

        try {


            new Thread(() -> {
                try {
                    java.net.URL url = new URL(url_String);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        // Print result
                        System.out.println(response.toString());
                    } else {
                        System.out.println("GET request failed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        }catch(Exception ex){ System.out.println(ex.toString());}
    }
    public void load_Roster_data(){

        String URL = root  + "/TimeSheet.asmx?op=getMonth_Rosters";
        String SOAP_ACTION =  "https://tempuri.org/getMonth_Rosters";
        String METHOD_NAME = "getMonth_Rosters";

        String buff="";
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        Calendar myCalendar = Calendar.getInstance();
        Date date = myCalendar.getTime();
        String strDate=dateFormat.format(date);
        String[] strDate2 =strDate.split("/");

        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        try{
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug =true;

            PropertyInfo pi=new PropertyInfo();
            pi.setName("client_code");
            pi.setValue( getSecurityToken() + StaffCode);
            request.addProperty(pi);

            PropertyInfo pi2=new PropertyInfo();
            pi2.setName("MonthNo");
            pi2.setValue(strDate2[1]);
            request.addProperty(pi2);

            PropertyInfo pi3=new PropertyInfo();
            pi3.setName("YearNo");
            pi3.setValue(strDate2[0]);
            request.addProperty(pi3);


            PropertyInfo pi4=new PropertyInfo();
            pi4.setName("client");
            pi4.setValue(false);
            request.addProperty(pi4);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;

            envelope.setOutputSoapObject(request);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            // SoapObject obj=(SoapObject) envelope.getResponse();
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

            }
        }catch (Exception ex){ }//textMsg.setText("cc: " + ex.toString());}
        //    textMsg.setText("Done successfully");


    }


    public void load_Booking_data(){

        String URL = root  + "/TimeSheet.asmx?op=getBookingRosters";
        String SOAP_ACTION =  "https://tempuri.org/getBookingRosters";
        String METHOD_NAME = "getBookingRosters";

        String buff="";
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        Calendar myCalendar = Calendar.getInstance();
        Date date = myCalendar.getTime();
        String strDate=dateFormat.format(date);
        String[] strDate2 =strDate.split("/");

        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        try{
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug =true;

            PropertyInfo pi=new PropertyInfo();
            pi.setName("AccountNo");
            pi.setValue( getSecurityToken() + StaffCode);
            request.addProperty(pi);

            PropertyInfo pi2=new PropertyInfo();
            pi2.setName("username");
            pi2.setValue(OperatorId);
            request.addProperty(pi2);

            PropertyInfo pi3=new PropertyInfo();
            pi3.setName("RecordNo");
            pi3.setValue(Max_RecordNo);
            request.addProperty(pi3);



            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;

            envelope.setOutputSoapObject(request);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            // SoapObject obj=(SoapObject) envelope.getResponse();
            String xml= androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir=null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            fileDir = new File(froot.getAbsolutePath()+"/.server/");
            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir,"booking.xml");
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

            }
        }catch (Exception ex){ }//textMsg.setText("cc: " + ex.toString());}
        //    textMsg.setText("Done successfully");

    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    class DownReceiver extends ResultReceiver {

        private Receiver mReceiver;

        public DownReceiver(Handler handler) {
            super(handler);
        }

        public void setReceiver(Receiver receiver) {
            mReceiver = receiver;
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (mReceiver != null) {
                mReceiver.onReceiveResult(resultCode, resultData);
            }
        }

    }
    class MyAsyncClass extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(MainActivity.this);
            pDialog.setMessage("Please wait while processing  ....");
            pDialog.show();
            if(listView==null){
                listView=findViewById(R.id.listViewMain);
            }
        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                // Add subject, Body, your mail Id, and receiver mail Id.
                // sender.sendMail("Subject", " body", "from Mail", "to mail");

                doService2();
            }

            catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();
            //  Toast.makeText(getApplicationContext(), "Email send", 100).show();
        }
    }
    class MyAsyncClass_bulk_data extends AsyncTask<Void, Void, Void> {


        //LoadingDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog_refresh==null)
                pDialog_refresh = new LoadingDialog(MainActivity.this);
            pDialog_refresh.setMessage("Please wait while loading Recipient detail from server  ....");
            pDialog_refresh.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {

                bulk_data.load_Receipient_Detail(MobileFutureLimit);

                // bulk_data.get_Active_Device_Reminder();
            }

            catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //pDialog.cancel();

            try{

                new MainActivity.MyAsyncClass_bulk_data3().execute();
            } catch(Exception ex){}

        }
    }
    //--- It is shifted to Add Attendee file
    class MyAsyncClass_bulk_data2 extends AsyncTask<Void, Void, Void> {

        //LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // pDialog = new LoadingDialog(MainActivity.this);
            //pDialog.setMessage("Please wait while loading Program Recipients   ....");
            //  pDialog.show();



        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {

                bulk_data.getProgramRecipients(MobileFutureLimit);

            }

            catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //pDialog.cancel();
            try{

                new MainActivity.MyAsyncClass_bulk_data3().execute();
            } catch(Exception ex){}


        }
    }
    class MyAsyncClass_bulk_data3 extends AsyncTask<Void, Void, Void> {

        //LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //pDialog = new LoadingDialog(MainActivity.this);
//            pDialog.setMessage("Please wait while loading Transport Detail  ....");
//            pDialog.show();
            if (pDialog_refresh==null)
                pDialog_refresh = new LoadingDialog(MainActivity.this);
            pDialog_refresh.setMessage("Please wait while loading Transport Detail  ....");
          //  pDialog_refresh.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                bulk_data.load_Transport_Detail();
            }

            catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //pDialog.cancel();
            try{

                new MainActivity.MyAsyncClass_bulk_data4().execute();
            } catch(Exception ex){}


        }
    }
    class MyAsyncClass_bulk_data4 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            pDialog = new LoadingDialog(MainActivity.this);
//            pDialog.setMessage("Please wait while loading Shift Goals  ....");
//            pDialog.show();

            if (pDialog_refresh==null)
                pDialog_refresh = new LoadingDialog(MainActivity.this);
            pDialog_refresh.setMessage("Please wait while loading Shift Goals  ....");
          //  pDialog_refresh.show();
        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {

                bulk_data.load_Shift_Goals();
               // bulk_data.get_Strategies();

            }

            catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //pDialog.cancel();
            try{

                new MainActivity.MyAsyncClass_bulk_data5().execute();
            } catch(Exception ex){}


        }
    }
    class MyAsyncClass_bulk_data5 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            pDialog = new LoadingDialog(MainActivity.this);
//            pDialog.setMessage("Please wait while loading Alerts Detail  ....");
//            pDialog.show();
            if (pDialog_refresh==null)
                pDialog_refresh = new LoadingDialog(MainActivity.this);
            pDialog_refresh.setMessage("Please wait while loading Alerts Detail  ....");
          //  pDialog_refresh.show();
        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                bulk_data.load_Group_Alerts_Detail();

            }

            catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //pDialog.cancel();
            try{

                new MainActivity.MyAsyncClass_bulk_data6().execute();
            } catch(Exception ex){}


        }
    }
    class MyAsyncClass_bulk_data6 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//
//            pDialog = new LoadingDialog(MainActivity.this);
//            pDialog.setMessage("Please wait while loading Incident Detail  ....");
//            pDialog.show();
            if (pDialog_refresh==null)
                pDialog_refresh = new LoadingDialog(MainActivity.this);

            pDialog_refresh.setMessage("Please wait while loading Incident Detail  ....");
          //  pDialog_refresh.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {

                bulk_data.load_incident_Locations();
                bulk_data.load_incident_Types();
            }

            catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //pDialog.cancel();
            try{
                new MainActivity.MyAsyncClass_bulk_data7().execute();
            } catch(Exception ex){}


        }
    }
    class MyAsyncClass_bulk_data7 extends AsyncTask<Void, Void, Void> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            pDialog = new LoadingDialog(MainActivity.this);
//            pDialog.setMessage("Please wait while loading paid hours rosters  ....");
//            pDialog.show();
            if (pDialog_refresh==null)
                pDialog_refresh = new LoadingDialog(MainActivity.this);

            pDialog_refresh.setMessage("Please wait while loading paid hours rosters ....");
           // pDialog_refresh.show();
        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {


                bulk_data.load_paid_hours();

            }

            catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //pDialog.cancel();
            try{

                new MainActivity.MyAsyncClass_bulk_data8().execute();
            } catch(Exception ex){}


        }
    }
    class MyAsyncClass_bulk_data8 extends AsyncTask<Void, Void, Void> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            pDialog = new LoadingDialog(MainActivity.this);
//            pDialog.setMessage("Please wait while loading Leave types  ....");
//            pDialog.show();
            if (pDialog_refresh==null)
                pDialog_refresh = new LoadingDialog(MainActivity.this);

            pDialog_refresh.setMessage("Please wait while loading Leave types ....");
           // pDialog_refresh.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {

                bulk_data.load_LeaveTypes();

            }

            catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //pDialog.cancel();
            try{

                new MainActivity.MyAsyncClass_bulk_data9().execute();
            } catch(Exception ex){}


        }
    }
    class MyAsyncClass_bulk_data9 extends AsyncTask<Void, Void, Void> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            pDialog = new LoadingDialog(MainActivity.this);
//            pDialog.setMessage("Please wait while loading Task Detail  ....");
//            pDialog.show();
            if (pDialog_refresh==null)
                pDialog_refresh = new LoadingDialog(MainActivity.this);

            pDialog_refresh.setMessage("Please wait while loading Task Detail ....");
           // pDialog_refresh.show();
        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {


                bulk_data.load_Task_Detail();

            }

            catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //pDialog.cancel();
            try{

                new MainActivity.MyAsyncClass_bulk_data10().execute();
            } catch(Exception ex){}


        }
    }
    class MyAsyncClass_bulk_data10 extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//
//            pDialog = new LoadingDialog(MainActivity.this);
//            pDialog.setMessage("Please wait while loading Roster Recipient  ....");
//            pDialog.show();
            if (pDialog_refresh==null)
                pDialog_refresh = new LoadingDialog(MainActivity.this);

            pDialog_refresh.setMessage("Please wait while loading Roster Recipient ....");
           // pDialog_refresh.show();
        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {

                bulk_data.getRoster_Recipient();
            }

            catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            pDialog_refresh.cancel();
            try{
                //Call for loading Shift Gaols
                new MainActivity.MyAsyncClass_bulk_data11().execute();
            } catch(Exception ex){}


        }
    }
    class MyAsyncClass_bulk_data11 extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//
//            pDialog = new LoadingDialog(MainActivity.this);
//            pDialog.setMessage("Please wait while loading Roster Recipient  ....");
//            pDialog.show();
            if (pDialog_refresh==null)
                pDialog_refresh = new LoadingDialog(MainActivity.this);

            pDialog_refresh.setMessage("Please wait while loading Service Shift Goals ....");
           // pDialog_refresh.show();
        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {

                bulk_data.load_Service_Wise_Shift_Goals();
            }

            catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog_refresh.cancel();
            try{
                //Call for loading Roster Data
                new MainActivity.MyAsyncClass3().execute();
            } catch(Exception ex){}


        }
    }
    class MyAsyncClass_load_Op_Case_Note extends AsyncTask<String, Void, Void> {

        LoadingDialog pDialog;
        String[] parms;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(MainActivity.this);
            pDialog.setMessage("Please wait while loading Op/Case Nots from server  ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... mApi) {
            try {
                parms=mApi;

                String format="yyyy/MM/dd";

                SimpleDateFormat    sdf = new SimpleDateFormat(format,Locale.getDefault());
                Calendar c = Calendar.getInstance();
                Date dt=c.getTime();

                String rosterDate = sdf.format(dt);
                bulk_data= new Bulk_Data(root,StaffCode,OperatorId,Security_Token,Server_Available,rosterDate,context);
                // Tost_Message(parms[0]);

                //irfan 12feb 2025
//                if (parms[0].equals(""))
//                    bulk_data.get_OP_Case_Notes();
//                else
//                    bulk_data.get_OP_Case_Note(parms[0]);

            }

            catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();

            /*LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View gridView = inflater.inflate(R.layout.grid_row_recipient_group, null);
            View rl_View=gridView.findViewById(R.id.rl_View);
            TextView  txtClientNote = (TextView)  rl_View.findViewById(R.id.txtClientNote);*/

            if(listView==null){
                listView=findViewById(R.id.listViewMain);
            }
//            show_OP_Case_Note(listView,parms[0],parms[1],Integer.parseInt(parms[2]));


        }
    }
    class MyAsyncClass2 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(MainActivity.this);
            pDialog.setMessage("Please wait while updating server updates  ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                // Add subject, Body, your mail Id, and receiver mail Id.
                // sender.sendMail("Subject", " body", "from Mail", "to mail");
                Upload_Updates up= new Upload_Updates(root,OperatorId,Security_Token,context);
                up.Upload_Updates_on_server();
            }

            catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();
            try{
                new MainActivity.MyAsyncClass3().execute();

            } catch(Exception ex){}
            //doService2(listView);
            //  Toast.makeText(getApplicationContext(), "Email send", 100).show();
        }
    }
    class MyAsyncClass3 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new LoadingDialog(MainActivity.this);
            pDialog.setMessage("Please wait while loading roster data from server  ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                final Thread t2 = new Thread() {
                    public void run() {
                        try {

                            sleep(1000);
                            //   progress.setProgress(30);
                        }
                        catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    } };
                t2.start();

                load_Roster_data();
                //  load_Booking_data();

                // if (AllowViewBookings.equalsIgnoreCase("true"))
                //  load_Booking_data();

            }

            catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            pDialog.cancel();
            buton_clicked=true;
            listView = findViewById(R.id.listViewMain);

            try{
                doService2();
                //   new MyAsyncClass4().execute();
            }catch (Exception ex) {}



            //  Toast.makeText(getApplicationContext(), "Email send", 100).show();
        }
    }



        class MyAsyncClass4_bandwidth extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(MainActivity.this);
            pDialog.setMessage("Please wait while loading Recipient data ....");
            //  pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                // Add subject, Body, your mail Id, and receiver mail Id.

                // getRoster_Recipient();
                try{

                    //Download your image
                   /*     long startTime = System.currentTimeMillis();
                        HttpGet httpRequest = new HttpGet( URI.create(urlString) );
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpResponse response = (HttpResponse) httpClient.execute(httpRequest);
                        long endTime = System.currentTimeMillis();

                        HttpEntity entity = response.getEntity();
                        BufferedHttpEntity bufHttpEntity;
                        bufHttpEntity = new BufferedHttpEntity(entity);

                        //You can re-check the size of your file
                        final long contentLength = bufHttpEntity.getContentLength();

                        // Log
                        // Log.d(TAG, "[BENCHMARK] Dowload time :"+(endTime-startTime)+" ms");

                        // Bandwidth : size(KB)/time(s)
                        bandwidth = (contentLength /(endTime-startTime)) ;*/
                    // ErroString="contentLength=" + contentLength + "\nTime " + (endTime - startTime);
                }catch(Exception ex){ErroString=ex.toString();}
            }

            catch (Exception ex) {
                // ErroString=ex.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //  pDialog.cancel();
            try{
                // new MyAsyncClass5().execute();
            } catch (Exception ex) {}


            //  Toast.makeText(getApplicationContext(), "bandwidth="+bandwidth +"\n" + ErroString, Toast.LENGTH_LONG).show();
        }
    }
    class MyAsyncClass5 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(MainActivity.this);
            pDialog.setMessage("Getting Current Location....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {


                loc = guessCurrentPlace();



            } catch (Exception ex) {
                //  Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //getRoster_Recipient();
            pDialog.cancel();

            // Toast.makeText(getApplicationContext(),Current_Address, Toast.LENGTH_SHORT).show();
            try{

                //  new MyAsyncClass6().execute();
            }catch(Exception ex){}
            //
        }
    }

    double get_internet_Speed(){
        // Add subject, Body, your mail Id, and receiver mail Id.
        double speed=0;
        try {
            // SpeedTest spt = new SpeedTest(root);
            spt.bindListeners();
            speed = spt.speedInfo.kilobits;
            speed = spt.speedInfo.kilobits;
            for(int i=0; i<5;i++ ){
                speed = spt.speedInfo.kilobits;
            }


        }catch(Exception ex){
        }
        return speed;
    }

    class MyAsyncClass6 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(MainActivity.this);
           // pDialog.setMessage("Please wait while checking alerts  ....");
          //  pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {


                get_Active_Device_Reminder();

            }catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            pDialog.cancel();
            showReminder2(MainActivity.this);
            //  Toast.makeText(getApplicationContext(), "Email send", 100).show();
        }
    }

    public void load_Receipient_Detail(){

        String URL2 = root  + "/TimeSheet.asmx?op=getStaff_Recipient_Detail";
        final String SOAP_ACTION2 =  "https://tempuri.org/getStaff_Recipient_Detail";
        final String METHOD_NAME2 = "getStaff_Recipient_Detail";


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
                }catch (Exception ex){ }//textMsg.setText("cc: " + ex.toString());}
                finally{   fileos.close();}
            }
        }catch (Exception ex){ //textMsg.setText("cc: " + ex.toString());
        }

        //    textMsg.setText("Done successfully");

    }
    public void ShowDialog_for_Roseter_Deliver(View v, String commandText, boolean deliver ) {
        try{

            final boolean deliver2=deliver;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setTitle("Roster Deliver Status");
            alertDialogBuilder
                    .setMessage(commandText)
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing

                            Update_Deliver_Status(deliver2);
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

    public void getPaid_Hours(String Roster_Date)
    {
        try{
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();
            File fileDir=null;
            fileDir = new File(froot.getAbsolutePath()+"/.server/");
            File fXmlFile = new File(fileDir, "Paid_Hours.xml");
            //  rosters=new ArrayList<Roster_Info>();
            if (fXmlFile.exists()){
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Paid_Hours");
                if (nList==null) return;

                String Roster_Date2="";
                String Value="";
                // Daily_Paid_Hours=0;
                // Daily_Paid_KM=0;
                 Monthly_Paid_Hours=0;
                 Monthly_Paid_KM=0.0;



                //   TextView txtPayRate=(TextView)findViewById(R.id.txtPayRate);


                String script="";
                for (int temp = 0; temp< nList.getLength(); temp++) {

                    try{
                        Node  nNode = nList.item(temp);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;

                            Value=eElement.getElementsByTagName("Daily_Paid_Hours").item(0).getTextContent();
                            Monthly_Paid_Hours=Monthly_Paid_Hours + Integer.parseInt(Value);

                            Value=eElement.getElementsByTagName("Daily_Paid_KM").item(0).getTextContent();
                            Monthly_Paid_KM=Monthly_Paid_KM + Double.parseDouble(Value);

                            Roster_Date2=eElement.getElementsByTagName("Roster_Date").item(0).getTextContent();

                            if( Roster_Date2.equals(Roster_Date) )
                            {
                                try{

                                    Value=eElement.getElementsByTagName("Daily_Paid_Hours").item(0).getTextContent();
                                  //  Daily_Paid_Hours=Daily_Paid_Hours + Integer.parseInt(Value);

                                    Value=eElement.getElementsByTagName("Daily_Paid_KM").item(0).getTextContent();
                                   // Daily_Paid_KM=Daily_Paid_KM + Integer.parseInt(Value);

                                    //txtPayRate.setText(Html.fromHtml(script));

                                }catch(Exception ex){}


                            }
                        }
                    }catch(Exception ex){}

                }

                // script="<b><font>Daily Rostered Hours = </font></b>" + ConvertToTime(Daily_Paid_Hours) + ", <b><font> KM = </font></b>" + Daily_Paid_KM  +  "<br />" +
                //		 "<b><font>Total Rostered Hours = </font></b>" + ConvertToTime(Monthly_Paid_Hours) + ", <b><font>KM = </font></b>" + Monthly_Paid_KM  ;

                script= "<b><font>Total Rostered Hours = </font></b>" + ConvertToTime(Monthly_Paid_Hours) + ", <b><font>KM = </font></b>" + Monthly_Paid_KM  ;

                // txtPayRate.setText(Html.fromHtml(script));
            }
        }catch(Exception ex){}
    }

    String ConvertToTime(int blocks){
        String str_duration="";
        int hours = blocks*5/60;
        int min = blocks*5%60;
        str_duration=set_leading_zero(hours,2) + ":" + set_leading_zero(min, 2) + "" ;
        return str_duration;

    }

    String ConvertToTime_formated(int blocks){
        String str_duration="";
        int hours = blocks*5/60;
        int min = blocks*5%60;
        str_duration=set_leading_zero(hours,2)  + "h " + set_leading_zero(min, 2) + "m" ;
        return str_duration;

    }

    String ConvertToTime_Decimal(int blocks){
        String str_duration="";
        int hours = blocks*5/60;
        int min=blocks*5%60;

        min = (int)Math.round((((double)min*100)/60));
        str_duration=hours + "." + set_leading_zero(min, 2) ;
        return str_duration;

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("MainActivity--dispatchTouchEvent:"
                        + "---MotionEvent.ACTION_DOWN---");
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("MainActivity--dispatchTouchEvent:"
                        + "---MotionEvent.ACTION_MOVE---");
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("MainActivity--dispatchTouchEvent:"
                        + "---MotionEvent.ACTION_UP---");
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
    String selected_val="";
    public void Set_Leave(Context view)
    {

        Intent in= new Intent(view, Add_Leave.class);
        Bundle b= new Bundle();
        b.putString("root",root);
        b.putString("StaffCode",StaffCode);
        b.putString("Staff_Coordinator_Email",Staff_Coordinator_Email);
        b.putBoolean("Server_Available",Server_Available);
        b.putBoolean("Server_Available",Server_Available);
        b.putString("OperatorId",OperatorId);
        b.putString("Security_Token",Security_Token);
        b.putString("RosterDate",RosterDate);

        in.putExtras(b);

        view.startActivity(in);

        // dialog_leave.show();

    }
    public  Date getDateFromDatePicket(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
    public long Add_Leave2(String Leave_Type,String Start_Date, String End_Date,String Note, String Address1)
    {
        try
        {
            String command;
            // command= "\n update roster set notes='" + Note.replace("'", "''") + "' WHERE recordno = " + RecordNo;
            //command= command+ "\n update Recipients set notes='" + Note.replace("'", "''") + "' WHERE AccountNo in (select [client code] from roster where recordno = " + RecordNo + ")";

            Note=SQLSafe(Note);

            command="\nL`"  + StaffCode +"`" + Leave_Type + "`" +  Start_Date + "`"  + End_Date + "`" +  Note.replace("\n","~") +"`" + Address1  ;


            set_Updates(command);
            //  Toast.makeText(getApplicationContext(), "Leave Application added Successfully", Toast.LENGTH_LONG).show();
            return 1;

        }catch(Exception ex){
            // Toast.makeText(getApplicationContext(), "Operation not done due to some error\n" +ex.toString(), Toast.LENGTH_LONG).show();
            return 0;
        }
    }
    long Add_Leave(String Leave_Type,String Start_Date, String End_Date,String Note, String Address1 )
    {

        long r_val=0;

        if (Server_Available==false)
        {
            r_val= Add_Leave2(Leave_Type,Start_Date,End_Date,Note, Address1 );
            return 1;
        }

        String URL41 = root + "/TimeSheet.asmx?op=Add_LeaveEntry";
        String SOAP_ACTION41 =  "https://tempuri.org/Add_LeaveEntry";
        String METHOD_NAME41 = "Add_LeaveEntry";

        //  TextView txtmsg=  ((TextView) findViewById(R.id.textMsg));
        //txtmsg.setVisibility(View.VISIBLE);
        // txtmsg.setText("Calling add note  " + URL4);



        try
        {
            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME41);
            HttpTransportSE androidHttpTransport2 = new HttpTransportSE(URL41);
            androidHttpTransport2.debug =true;

            PropertyInfo pi=new PropertyInfo();
            pi.setName("StaffCode");
            pi.setValue( getSecurityToken() + StaffCode);
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
            pi5.setValue(Note);
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
            r_val=Integer.parseInt(result.toString());
            if (r_val>0){

                // Toast.makeText(getApplicationContext(), "Leave Application added Successfully", Toast.LENGTH_LONG).show();
                  /*  try
                    {
                        String messgas="Leave Application submitted by  \"" + StaffCode + "\" :\n\n" + "Leave_Type : " + Leave_Type + "\nFrom : " + Start_Date + " to "+ End_Date + "\n" + Note;
                        String title="Leave Application for \"" + StaffCode+ "\"\n" ;
                        send_email_alert(messgas,title);
                    }catch(Exception ex){}*/


            }else
                // Toast.makeText(getApplicationContext(), "Leave Application not added due to some problem - " + StaffCode + " Result=" + result.toString(), Toast.LENGTH_LONG).show();

                try{
                    //    sendEmail("Client Note",Note,"arshadblouch81@yahoo.com");
                }catch(Exception ex){

                    // Toast.makeText(getApplicationContext(), "Operation not done due to some server error\n" + ex.toString(), Toast.LENGTH_LONG).show();

                }


        }catch(Exception ex){
            //  Toast.makeText(getApplicationContext(), "Operation not done due to some server error\n" + ex.toString(), Toast.LENGTH_LONG).show();

        }

        return r_val;

    }

    public void getLeaves() throws ParserConfigurationException, SAXException {

        // Toast.makeText(getApplicationContext(), "Filling Task", Toast.LENGTH_LONG).show();

        lst_leave = new ArrayList<String>();


        try {

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            File fileDir = null;
            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = new File(fileDir, "LeaveTypes.xml");

            TextView textMsg = ((TextView) findViewById(R.id.txtMsg));

            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("DataDomain2");

                if (nList == null) return;

                for (int temp = 0; temp < nList.getLength(); temp++) {

                    try {
                        Node nNode = nList.item(temp);


                        String value = "";
                        //  textMsg.setText("\nCurrent Element :" + nNode.getNodeName());
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;

                            value = eElement.getElementsByTagName("Description").item(0).getTextContent();


                            lst_leave.add(value);
                        }


                    } catch (Exception aE) {

                    }
                }
            } else {
            }//textMsg.setText("Xml for Task List file not found"); }
        } catch (Exception aE) {}


    }

    public String SQLSafe(String sValue)
    {

        String sAns = null;
        sAns = sValue.replace("'", "''");
        sAns = sAns.replace("\"", "''");
        return (sAns);

    }

    public void set_Updates(String command ) throws IOException{
        // File froot = null;
        try{
            // check for SDcard
            //((TextView) findViewById(R.id.textMsg)).setText("making update");
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File filein=null;
            File fileDir=null;
            String state = Environment.getExternalStorageState();
//  textMsg.setText("No Server found - " + state);
            if (Environment.MEDIA_MOUNTED.equals(state)) {

                //check sdcard permission
                fileDir = new File(froot.getAbsolutePath()+"/.server/");

                filein= new File(fileDir, "Updates.txt");

                if (filein.exists())
                {
                    try{
                        froot.setWritable(true);
                        //if (froot.canWrite()) {

                        File file = new File(fileDir, "Updates.txt");
                        FileWriter filewriter = new FileWriter(file,true);
                        BufferedWriter out = new BufferedWriter(filewriter);
                        out.write(command);
                        out.close();

                    } catch (Exception e) {
                        // textMsg.setText(e.toString());
                        //Log.e("Exception","error occurred while creating xml file");
                    }
                }else{
                    froot.setWritable(true);

                    //if (froot.canWrite()) {

                    if (!fileDir.exists())
                        fileDir.mkdirs();

                    File file = new File(fileDir, "Updates.txt");
                    FileWriter filewriter = new FileWriter(file,true);
                    BufferedWriter out = new BufferedWriter(filewriter);
                    out.write(command);
                    out.close();
                }
            }
        } catch (Exception e) {}


        //  ((TextView) findViewById(R.id.textMsg)).setText("Operation done successfully");
    }

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

    class MyAsyncClass33 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(MainActivity.this);
            pDialog.setMessage("Please wait while Processing  Email Alert....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {


            try {



                if ( !Staff_Coordinator_Email.equals("") && user_settings.getAppUsesSMTP().equalsIgnoreCase("true"))     {

                    try{

                        //  email.sendMail(email_seting.getFromDisplayName(), email_msg, email_seting.getFromEmail(),To_Emails);
                        email.sendMail(Email_Subject, email_msg, email_seting.getFromEmail(),Staff_Coordinator_Email);
                        //   send_local_email();
                    } catch (Exception e) {
                        //Toast.makeText(getApplicationContext(),"SMTP Server not working" , Toast.LENGTH_LONG).show();
                        send_local_email();
                    }
                }else{
                    send_local_email();
                }




            }catch (Exception ex) {
                send_local_email();
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();
            //  Toast.makeText(getApplicationContext(), "Email send", 100).show();
        }
    }
    class MyAsyncClass_Leave extends AsyncTask<String, String, Long> {

        LoadingDialog pDialog;
        String messgas="";
        String title="";
        long r_val=0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(MainActivity.this);
            pDialog.setMessage("Please wait while Processing Leave....");
            pDialog.show();

        }

        @Override
        protected Long doInBackground(String... mApi) {



            try {

                String parm1=mApi[0];
                String parm2=mApi[1];

                String[] vals1 =parm1.split("`");
                String[] vals2 =parm2.split("`");

                title="Leave Application for \"" + StaffCode+ "\"\n" ;
                messgas="Leave Application submitted by  \"" + StaffCode + "\" :\n\n" + "Leave_Type : " + vals1[0] + "\nFrom : " + vals2[0] + " to "+ vals2[1] + "\n" + vals1[1];
                r_val=  Add_Leave(vals1[0],vals2[0],vals2[1],vals1[1],"-");


                return r_val;


            }catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
            return r_val;
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            pDialog.cancel();


            if (result>0) {
                Tost_Message( "Leave Application added Successfully" );
                try
                {

                    //  sendAutoEmail("arshadblouch81@gmail.com","dahsra`123","arshadblouch81@gmail.com",title,messgas);

                    //if(Server_Available)
                    send_email_alert(messgas,title);

                }catch(Exception ex){ Tost_Message(ex.toString());}
            }else
                Tost_Message("Unable to add Leave Application ");
        }
    }
    private void sendAutoEmail(String User,String Password, String To_Email,String Title, String Body){
        BackgroundMail.newBuilder(this)
                .withUsername(User)
                .withPassword(Password)
                .withMailto(To_Email)
                .withSubject(Title)
                .withBody(Body)
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        // Tost_Message("Email sent successfully");
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        //Tost_Message("Email Sending Failed");
                    }
                })
                .send();
    }
    class MyAsyncClass_RemoveSession extends AsyncTask<String, String, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(MainActivity.this);
            pDialog.setMessage("Logging Off User ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... mApi) {


            String usr = mApi[0];
            String urlString = root + "/index.aspx?logout=1&user=" + usr;
            String data = "";

            try {
                Volly_Post_Request(urlString);
            } catch (Exception ex) {
                Tost_Message(ex.toString());
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();

            if (!settings.getBoolean("mslogin", false)) {
                finish();
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                Toast.makeText(getApplicationContext(), "bandwidth=" + bandwidth + "\n" + ErroString, Toast.LENGTH_LONG).show();
            }else {
                settings.edit().putBoolean("mslogin", false).commit();
                finish();
            }
        }
    }
    public void getRoster_Recipient2(String RecordNo)
    {
        try{
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();
            File fileDir=null;
            fileDir = new File(froot.getAbsolutePath()+"/.server/");
            File fXmlFile = new File(fileDir, "Roster_Recipient.xml");
            //  rosters=new ArrayList<Roster_Info>();
            if (fXmlFile.exists()){
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Roster_Recipient");
                if (nList==null) return;

                String RecordNo2="";

                for (int temp = 0; temp< nList.getLength(); temp++) {

                    try{
                        Node  nNode = nList.item(temp);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;

                            RecordNo2= eElement.getElementsByTagName("RecordNo").item(0).getTextContent();

                            if( RecordNo2.equals(RecordNo) )
                            {
                                try{


                                    Cordinator_Email="";
                                    if (eElement.getElementsByTagName("Cordinator_Email").item(0).getTextContent()!=null){

                                        Cordinator_Email=eElement.getElementsByTagName("Cordinator_Email").item(0).getTextContent();
                                    }
                                }catch(Exception ex){}

                                try{

                                    Receipient_Email="";
                                    if (eElement.getElementsByTagName("Email").item(0).getTextContent()!=null){

                                        Receipient_Email=eElement.getElementsByTagName("Email").item(0).getTextContent();
                                    }

                                }catch(Exception ex){}

                                break;
                            }
                        }
                    }catch(Exception ex){}

                }
            }
        }catch(Exception ex){}
    }
    void send_local_email(){

        String subject = Email_Subject;


        try{
            if (possibleEmail!=null && !possibleEmail.equals("")){
                //        		packageName!=null && !packageName.equals("")){
                try{

                   /*  Intent emailIntent = new Intent(Intent.ACTION_SEND);


                    //emailIntent.setType("text/plain");
                   emailIntent.setType("message/rfc822");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, possibleEmail);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, email_msg);
                    emailIntent.setData(Uri.parse("mailto:"+Staff_Coordinator_Email)); // or just "mailto:" for blank
                    emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    emailIntent.setAction(Intent.ACTION_DEFAULT);
                    startActivity(emailIntent);*/
                    // MainActivity.this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));

                    String[] TO = {Cordinator_Email};
                    Uri uri = Uri.parse("mailto:" + Staff_Coordinator_Email)
                            .buildUpon()
                            .appendQueryParameter("subject", subject)
                            .appendQueryParameter("body", email_msg)
                            .build();
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));

                }catch(Exception ex){}

            }else if (!Staff_Coordinator_Email.equals("") || Staff_Coordinator_Email!=null){

               /* Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, email_msg);
                intent.setData(Uri.parse("mailto:"+Staff_Coordinator_Email)); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                // MainActivity.this.startActivity(Intent.createChooser(intent, "Send mail..."));
                startActivity(intent);*/
                String[] TO = {Staff_Coordinator_Email};
                Uri uri = Uri.parse("mailto:" + Staff_Coordinator_Email)
                        .buildUpon()
                        .appendQueryParameter("subject", subject)
                        .appendQueryParameter("body", email_msg)
                        .build();
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        }catch (Exception ex) {
            // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void check_SMTP_Server_Setting(){



        froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File filein=null;
        File fileDir=null;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {

            //check sdcard permission
            fileDir = new File(froot.getAbsolutePath()+"/.server/");

            filein= new File(fileDir, "server_setting.txt");
            if (!filein.exists())
            {
                Intent intnt= new Intent(this, SMTP_Settings.class)	;

                intnt.putExtra("root", root);
                intnt.putExtra("Server_Available", Server_Available);
                intnt.putExtra("OperatorId", OperatorId);
                intnt.putExtra("Security_Token", Security_Token);
                startActivity(intnt);
            }
        }
    }

    public boolean Get_SMTP_Server_Setting(){

        boolean status = false;

        try{
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File filein=null;
            File fileDir=null;
            String state = Environment.getExternalStorageState();


            if (Environment.MEDIA_MOUNTED.equals(state)) {

                //check sdcard permission
                fileDir = new File(froot.getAbsolutePath()+"/.server/");

                filein= new File(fileDir, "server_setting.txt");
                if (filein.exists())
                {
                    try{
                        if (filein.length()<=0) return false;

                        email_seting = new Email_Settings();

                        FileReader fileReader = new FileReader(filein);
                        BufferedReader buf= new BufferedReader(fileReader);

                        String value=buf.readLine();
                        email_seting.setSMTPServer(value);

                        value=buf.readLine();
                        email_seting.setSMTPUser(value);

                        value=buf.readLine();
                        email_seting.setSMTPPassword(value);

                        value=buf.readLine();
                        email_seting.setSMTP_Port(value);

                        value=buf.readLine();
                        email_seting.setFromEmail(value);

                        value=buf.readLine();
                        email_seting.setFromDisplayName("  TRACCS Client Note Added for : " + StaffCode);

                        status=true;

                    } catch (Exception e) { }



                }else{
                    try{
                        filein.createNewFile();
                    }catch(Exception ex){}
                    status=false;
                }
            }

        } catch (Exception e) { status=false;}

        return status;
    }


    boolean Get_User_Settings2(){
        boolean status=true;
        user_settings= new User_Settings();
        try{
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();
            File fileDir=null;
            fileDir = new File(froot.getAbsolutePath()+"/.server/");
            File fXmlFile = new File(fileDir, "User_Settings.xml");


            //  rosters=new ArrayList<Roster_Info>();
            if (fXmlFile.exists()){
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("getUser_SettingsResult");

                String current_element="";

                if (nList==null) return false;

                try{
                    Node  nNode = nList.item(0);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        try{


                            current_element=eElement.getElementsByTagName("AllowSetTime").item(0).getTextContent();
                            user_settings.setAllowSetTime(current_element);
                            settings.edit().putString( "AllowSetTime",user_settings.getAllowSetTime()).commit();
                        }catch (Exception e) {}
                        try{
                            user_settings.setTMMode(eElement.getElementsByTagName("TMMode").item(0).getTextContent());
                            settings.edit().putString( "TMMode",user_settings.getTMMode()).commit();
                        }catch (Exception e) {}

                        try{
                            user_settings.setMobileFutureLimit(eElement.getElementsByTagName("MobileFutureLimit").item(0).getTextContent());
                            settings.edit().putString( "MobileFutureLimit",user_settings.getMobileFutureLimit()).commit();

                        }catch (Exception e) {}

                        try {
                            user_settings.setAllowPicUpload(eElement.getElementsByTagName("AllowPicUpload").item(0).getTextContent());
                            settings.edit().putString( "AllowPicUpload",user_settings.getAllowPicUpload()).commit();

                        } catch (Exception e) {
                        }
                        try{
                            current_element=eElement.getElementsByTagName("Apply_Goe_Location_Setting").item(0).getTextContent();
                            user_settings.setApply_Goe_Location_Setting(current_element);
                            settings.edit().putString( "Apply_Goe_Location_Setting",current_element).commit();

                        }catch (Exception e) {}

                        try{
                            Apply_Goe_Location_Setting=user_settings.getApply_Goe_Location_Setting();
                            settings.edit().putString( "Apply_Goe_Location_Setting",Apply_Goe_Location_Setting).commit();
                        }catch (Exception e) {}

                        try{
                            current_element=eElement.getElementsByTagName("KMAgainstTravelOnly").item(0).getTextContent();
                            KMAgainstTravelOnly=current_element;
                            user_settings.setKMAgainstTravelOnly(current_element);
                            settings.edit().putString( "KMAgainstTravelOnly",KMAgainstTravelOnly).commit();

                        }catch (Exception e) {}

                        try{
                            current_element=eElement.getElementsByTagName("mobilegeocodelimit").item(0).getTextContent();
                            user_settings.setMobilegeocodelimit(current_element);
                            settings.edit().putString( "mobilegeocodelimit",current_element).commit();

                        }catch (Exception e) {}

                        try{
                            current_element=eElement.getElementsByTagName("StaffLocationUpdateInterval").item(0).getTextContent();
                            user_settings.setStaffLocationUpdateInterval(current_element);
                            settings.edit().putString( "StaffLocationUpdateInterval",current_element).commit();

                        }catch (Exception e) {}

                        try{
                            current_element=eElement.getElementsByTagName("AllowIncidentEntry").item(0).getTextContent();
                            user_settings.setAllowIncidentEntry(current_element);
                            settings.edit().putString( "AllowIncidentEntry",current_element).commit();
                        }catch (Exception e) {}

                        try{
                            current_element=eElement.getElementsByTagName("AllowTravelEntry").item(0).getTextContent();
                            user_settings.setAllowTravelEntry(current_element);
                            settings.edit().putString( "AllowTravelEntry",current_element).commit();
                        }catch (Exception e) {}



                        try{
                            current_element=eElement.getElementsByTagName("AllowCaseNote").item(0).getTextContent();
                            user_settings.setAllowOPNote(current_element);
                            settings.edit().putString( "AllowCaseNote",current_element).commit();
                        }catch (Exception e) {}

                        try{
                            current_element=eElement.getElementsByTagName("AllowOPNote").item(0).getTextContent();
                            user_settings.setAllowCaseNote(current_element);
                            settings.edit().putString( "AllowOPNote",current_element).commit();
                        }catch (Exception e) {}
                        try{
                            current_element=eElement.getElementsByTagName("AllowClientNoteEntry").item(0).getTextContent();
                            user_settings.setAllowClientNoteEntry(current_element);
                            settings.edit().putString( "AllowClientNoteEntry",current_element).commit();
                        }catch (Exception e) {}

                        try{
                            current_element=eElement.getElementsByTagName("AllowRosterNoteEntry").item(0).getTextContent();
                            user_settings.setAllowRosterNoteEntry(current_element);
                            settings.edit().putString( "AllowRosterNoteEntry",current_element).commit();
                        }catch (Exception e) {}


                        try{
                            current_element=eElement.getElementsByTagName("StaffCode").item(0).getTextContent();
                            user_settings.setStaffCode(current_element);
                            settings.edit().putString( "StaffCode",current_element).commit();
                        }catch (Exception e) {}

                        try{
                            current_element=eElement.getElementsByTagName("AllowRegisterSign").item(0).getTextContent();
                            user_settings.setAllowRegisterSign(current_element);
                            settings.edit().putString( "AllowRegisterSign",current_element).commit();
                        }catch (Exception e) {}

                        try{
                            current_element=eElement.getElementsByTagName("ToDate").item(0).getTextContent();
                            user_settings.setToDate(current_element);
                            settings.edit().putString( "ToDate",current_element).commit();
                        }catch (Exception e) {}

                        try{
                            current_element=eElement.getElementsByTagName("Time").item(0).getTextContent();
                            user_settings.setTime(current_element);
                            settings.edit().putString( "Time",current_element).commit();
                        }catch (Exception e) {}

                        try{
                            current_element=eElement.getElementsByTagName("UserSessionLimit").item(0).getTextContent();
                            UserSessionLimit=(current_element);
                            settings.edit().putString( "UserSessionLimit",current_element).commit();
                        }catch (Exception e) {}

                        try{
                            user_settings.setUserSessionLimit(UserSessionLimit);

                        }catch (Exception e) {}

                        try{
                            current_element=eElement.getElementsByTagName("ShowClientPhoneInApp").item(0).getTextContent();
                            user_settings.setShowClientPhoneInApp(current_element);
                            settings.edit().putString( "ShowClientPhoneInApp",current_element).commit();
                        }catch (Exception e) {}

                        try{
                            current_element=eElement.getElementsByTagName("TA_TRAVELDEFAULT").item(0).getTextContent();
                            user_settings.setTA_TRAVELDEFAULT(current_element);
                            settings.edit().putString( "TA_TRAVELDEFAULT",current_element).commit();
                        }catch (Exception e) {}

                        try{
                            current_element=eElement.getElementsByTagName("CheckAlertInterval").item(0).getTextContent();
                            user_settings.setCheckAlertInterval(current_element);
                            settings.edit().putString( "CheckAlertInterval",current_element).commit();
                        }catch (Exception e) {}

                        try{
                            current_element=eElement.getElementsByTagName("AllowOPNote").item(0).getTextContent();
                            user_settings.setAllowOPNote(current_element);
                            settings.edit().putString( "AllowOPNote",current_element).commit();

                        }catch (Exception e) {}


                        try{
                            current_element=eElement.getElementsByTagName("AllowCaseNote").item(0).getTextContent();
                            user_settings.setAllowCaseNote(current_element);
                            settings.edit().putString( "AllowCaseNote",current_element).commit();

                        }catch (Exception e) {}


                        try{
                            current_element=eElement.getElementsByTagName("EnableViewNoteCases").item(0).getTextContent();
                            user_settings.set_EnableViewNoteCases(current_element);
                            EnableViewNoteCases=current_element;
                            settings.edit().putString( "EnableViewNoteCases",current_element).commit();
                        }catch (Exception e) {}


                        try{
                            current_element=eElement.getElementsByTagName("AllowIncidentNote").item(0).getTextContent();
                            user_settings.setAllowIncidentNote(current_element);
                            settings.edit().putString( "AllowIncidentNote",current_element).commit();
                        }catch (Exception e) {}

                        try{

                            current_element=eElement.getElementsByTagName("AppUsesSMTP").item(0).getTextContent();
                            user_settings.setAppUsesSMTP(current_element);
                            settings.edit().putString( "AppUsesSMTP",current_element).commit();
                        }catch (Exception e) {}

                        try{

                            current_element=eElement.getElementsByTagName("AllowLeaveEntry").item(0).getTextContent();
                            user_settings.setAllowLeaveEntry(current_element);

                            settings.edit().putString( "AllowLeaveEntry",current_element).commit();
                        }catch (Exception e) {}

                        try{

                            current_element=eElement.getElementsByTagName("EnableRosterDelivery").item(0).getTextContent();
                            user_settings.setEnableRosterDelivery(current_element);
                            settings.edit().putString( "EnableRosterDelivery",current_element).commit();
                        }catch (Exception e) {}

                        try{

                            current_element=eElement.getElementsByTagName("MinimumInternetSpeedForOnline").item(0).getTextContent();
                            user_settings.setMinimumInternetSpeedForOnline(current_element);
                            MinimumInternetSpeedForOnline=Integer.parseInt(current_element);
                            settings.edit().putString( "MinimumInternetSpeedForOnline",current_element).commit();
                        }catch (Exception e) {}


                        try {

                            current_element = eElement.getElementsByTagName("GeolocateEnabled").item(0).getTextContent();
                            user_settings.setGeolocateEnabled(current_element);
                            settings.edit().putString( "GeolocateEnabled",current_element).commit();
                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("HideGeolocation").item(0).getTextContent();
                            user_settings.setHideGeolocation(current_element);
                            settings.edit().putString( "HideGeolocation",current_element).commit();
                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("EnablShift_End_Alarm").item(0).getTextContent();
                            user_settings.setEnable_Shift_End_Alarm(current_element);
                            Enable_Shift_End_Alarm=current_element;
                            settings.edit().putString( "EnablShift_End_Alarm",current_element).commit();
                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("LeaveLeadTime").item(0).getTextContent();
                            user_settings.setLeaveLeadTim(current_element);
                            settings.edit().putString( "LeaveLeadTime",current_element).commit();

                        } catch (Exception e) { }

                        try {

                            current_element = eElement.getElementsByTagName("AcceptBookings").item(0).getTextContent();
                            user_settings.setAcceptBookings(current_element);
                            settings.edit().putString( "AcceptBookings",current_element).commit();
                        } catch (Exception e) { }
                        try {

                            current_element = eElement.getElementsByTagName("Enable_Shift_Start_Alarm").item(0).getTextContent();
                            user_settings.setEnable_Shift_Start_Alarm(current_element);
                            Enable_Shift_Start_Alarm=current_element;
                            settings.edit().putString( "Enable_Shift_Start_Alarm",current_element).commit();

                        } catch (Exception e) { }

                        try {

                            current_element = eElement.getElementsByTagName("AcceptBookings").item(0).getTextContent();
                            user_settings.setAcceptBookings(current_element);
                            Accept_Booking=Boolean.parseBoolean(current_element);
                            settings.edit().putString( "AcceptBookings",current_element).commit();

                        } catch (Exception e) { }
                        try {

                            current_element = eElement.getElementsByTagName("Coordinator_Email").item(0).getTextContent();
                            user_settings.setCoordinator_Email(current_element);
                            Staff_Coordinator_Email=current_element;
                            settings.edit().putString( "Staff_Coordinator_Email",current_element).commit();

                        } catch (Exception e) { }

                        try {

                            current_element = eElement.getElementsByTagName("EnableRosterAvailability").item(0).getTextContent();
                            user_settings.setEnableRosterAvailability(current_element);
                            settings.edit().putString( "EnableRosterAvailability",current_element).commit();

                        } catch (Exception e) { }

                        try{
                            current_element=eElement.getElementsByTagName("AllowClinicalNoteEntry").item(0).getTextContent();
                            user_settings.setAllowClinicalNoteEntry(current_element);
                            settings.edit().putString( "AllowClinicalNoteEntry",current_element).commit();
                        }catch (Exception e) {}

                        try {

                            current_element = eElement.getElementsByTagName("GoogleAPI_Key").item(0).getTextContent();
                            user_settings.set_GoogleAPI_Key(current_element);
                            settings.edit().putString( "GoogleAPI_Key",current_element).commit();

                        } catch (Exception e) { }

                        try {

                            current_element = eElement.getElementsByTagName("Allow_OverWrite_Existing_Availability").item(0).getTextContent();
                            user_settings.setAllow_OverWrite_Existing_Availability(current_element);
                            Allow_OverWrite_Existing_Availability=current_element;
                            settings.edit().putString( "Allow_OverWrite_Existing_Availability",current_element).commit();
                        } catch (Exception e) { }

                        try{
                            user_settings.setAllowPicUpload(eElement.getElementsByTagName("AllowPicUpload").item(0).getTextContent());
                            settings.edit().putString( "AllowPicUpload",user_settings.getAllowPicUpload()).commit();

                        }catch (Exception e) {}

                        try{
                            user_settings.setAllowViewGoalPlans(eElement.getElementsByTagName("AllowViewGoalPlans").item(0).getTextContent());
                            settings.edit().putString( "AllowViewGoalPlans",user_settings.getAllowViewGoalPlans()).commit();

                        }catch (Exception e) {}

                        try{
                            user_settings.set_ViewClientDocuments(eElement.getElementsByTagName("ViewClientDocuments").item(0).getTextContent());
                            settings.edit().putString( "ViewClientDocuments",user_settings.get_ViewClientDocuments()).commit();

                        }catch (Exception e) {}

                        try {

                            current_element = eElement.getElementsByTagName("RestrictTravelSameDay").item(0).getTextContent();
                            user_settings.set_RestrictTravelSameDay(current_element);
                            settings.edit().putString("RestrictTravelSameDay",current_element);


                        } catch (Exception e) {
                        }

                    try {
                        current_element = eElement.getElementsByTagName("HIdeCancelButton").item(0).getTextContent();
                        user_settings.setHIdeCancelButton(current_element);
                        settings.edit().putString("HIdeCancelButton",current_element);
                    }catch(Exception ex){}

                        try {

                            current_element = eElement.getElementsByTagName("EnableRosterAvailability").item(0).getTextContent();
                            user_settings.setEnableRosterAvailability(current_element);
                            settings.edit().putString("EnableRosterAvailability",current_element);
                        } catch (Exception e) {
                        }





                        try {

                            current_element = eElement.getElementsByTagName("SuppressEmailOnRosterNote").item(0).getTextContent();
                            user_settings.setSuppressEmailOnRosterNote(current_element);
                            settings.edit().putString( "SuppressEmailOnRosterNote",current_element).commit();


                        } catch (Exception e) { }


                        try {

                            current_element = eElement.getElementsByTagName("GoogleAPI_Key").item(0).getTextContent();
                            user_settings.set_GoogleAPI_Key(current_element);
                            settings.edit().putString( "GoogleAPI_Key",current_element).commit();


                        } catch (Exception e) {
                        }


                        try {

                            current_element = eElement.getElementsByTagName("EmailUnavailabilityNotification").item(0).getTextContent();
                            user_settings.setEmailUnavailabilityNotification(current_element);
                            EmailUnavailabilityNotification=current_element;
                            settings.edit().putString( "EmailUnavailabilityNotification",current_element).commit();
                        } catch (Exception e) { }


                        try {

                            current_element = eElement.getElementsByTagName("UseOPNoteAsShiftReport").item(0).getTextContent();
                            user_settings.setUseOPNoteAsShiftReport(current_element);
                            UseOPNoteAsShiftReport=current_element;
                            settings.edit().putString( "UseOPNoteAsShiftReport",current_element).commit();
                        } catch (Exception e) { }

                        try {

                            current_element = eElement.getElementsByTagName("UseServiceNoteAsShiftReport").item(0).getTextContent();
                            user_settings.setUseServiceNoteAsShiftReport(current_element);
                            UseServiceNoteAsShiftReport=current_element;
                            settings.edit().putString( "UseServiceNoteAsShiftReport",current_element).commit();
                        } catch (Exception e) { }

                        try {
                            current_element = eElement.getElementsByTagName("EmailUnavailabilityNotification").item(0).getTextContent();
                            user_settings.setEmailUnavailabilityNotification(current_element);
                            settings.edit().putString( "EmailUnavailabilityNotification",current_element).commit();

                        } catch (Exception e) {
                        }
                        try {

                            current_element = eElement.getElementsByTagName("HideAddress").item(0).getTextContent();
                            user_settings.setHideAddress(current_element);
                            settings.edit().putString( "HideAddress",current_element).commit();


                        } catch (Exception e) {
                        }
                        try {

                            current_element = eElement.getElementsByTagName("AgencyPortalText").item(0).getTextContent();
                            user_settings.setAgencyPortalText(current_element);
                            settings.edit().putString( "AgencyPortalText",current_element).commit();
                        } catch (Exception e) { }
                        try {

                            current_element = eElement.getElementsByTagName("AgencyPortal").item(0).getTextContent();
                            user_settings.setAgencyPortal(current_element);
                            settings.edit().putString( "AgencyPortal",current_element).commit();
                        } catch (Exception e) { }
                        try {

                            current_element = eElement.getElementsByTagName("PolicyLink").item(0).getTextContent();
                            user_settings.setPolicyLink(current_element);
                            settings.edit().putString( "PolicyLink",current_element).commit();
                        } catch (Exception e) { }
                        try {

                            current_element = eElement.getElementsByTagName("applyAcceptShift").item(0).getTextContent();
                            user_settings.setApplyAcceptShift(current_element);
                            settings.edit().putString( "ApplyAcceptShift",current_element).commit();
                        } catch (Exception e) { }

                        try {

                            current_element = eElement.getElementsByTagName("EnableDocExteranlView").item(0).getTextContent();
                            user_settings.setEnableDocExteranlView(current_element);
                            settings.edit().putString( "EnableDocExteranlView",current_element).commit();
                        } catch (Exception e) { }

                        try {

                            current_element = eElement.getElementsByTagName("HideDOB").item(0).getTextContent();
                            user_settings.setHideDOB(current_element);
                            settings.edit().putString( "HideDOB",current_element).commit();
                        } catch (Exception e) { }
                        try {

                            current_element = eElement.getElementsByTagName("StaffLeaveEmailOverrides").item(0).getTextContent();
                            user_settings.setStaffLeaveEmailOverrides(current_element);
                            settings.edit().putString( "StaffLeaveEmailOverrides",current_element).commit();

                            current_element = eElement.getElementsByTagName("StaffLeaveEmail").item(0).getTextContent();
                            user_settings.setStaffLeaveEmail(current_element);
                            settings.edit().putString( "StaffLeaveEmail",current_element).commit();

                            current_element = eElement.getElementsByTagName("ClientNoteEmail").item(0).getTextContent();
                            user_settings.setClientNoteEmail(current_element);
                            settings.edit().putString( "ClientNoteEmail",current_element).commit();

                            current_element = eElement.getElementsByTagName("ClientNoteEmailOverrides").item(0).getTextContent();
                            user_settings.setClientNoteEmailOverrides(current_element);
                            settings.edit().putString( "ClientNoteEmailOverrides",current_element).commit();

                            current_element = eElement.getElementsByTagName("EnableEmailNotification").item(0).getTextContent();
                            user_settings.setEnableEmailNotification(current_element);
                            settings.edit().putString( "EnableEmailNotification",current_element).commit();

                            current_element = eElement.getElementsByTagName("DefaultAppNoteCategory").item(0).getTextContent();
                            user_settings.setEnableEmailNotification(current_element);
                            settings.edit().putString( "DefaultAppNoteCategory",current_element).commit();




                        } catch (Exception e) { }

                        try {
                            current_element = eElement.getElementsByTagName("UserOverrideIncidentEmail").item(0).getTextContent();
                            user_settings.setUserOverrideIncidentEmail(current_element);
                            settings.edit().putString( "UserOverrideIncidentEmail",current_element).commit();

                        } catch (Exception e) {
                        }
                        try {
                            current_element = eElement.getElementsByTagName("OverrideIncientEmail").item(0).getTextContent();
                            user_settings.setOverrideIncientEmail(current_element);
                            settings.edit().putString( "OverrideIncientEmail",current_element).commit();

                        } catch (Exception e) {
                        }

                    }
                }  catch (Exception aE) {}
                finally{  }

            }

        }catch (Exception e) {status=false;}

        return status;
    }


    public void Accept_Roster_Booking(View v ) {
        try{



            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

            alertDialogBuilder.setTitle("Take/Accept Booking");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder
                    .setMessage("Are you sure, you want to Accept/Take Booking for " + Current_roster.getClient_code())
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing

                            //Accept_Booking(Current_roster.getRecordNo());
                            try{
                                //     new MainActivity.MyAsyncClass_Accept_booking().execute();
                            }catch (Exception ex){}
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

    void Accept_Booking(String RecordNo){


        String URL = root  + "/TimeSheet.asmx?op=Accept_Booking";
        String SOAP_ACTION =  "https://tempuri.org/Accept_Booking";
        String METHOD_NAME = "Accept_Booking";

        try{
            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug =true;



            PropertyInfo pi2=new PropertyInfo();
            pi2.setName("StaffCode");
            pi2.setValue(getSecurityToken() + this.StaffCode);
            request.addProperty(pi2);

            PropertyInfo pi=new PropertyInfo();
            pi.setName("RecordNo");
            pi.setValue(RecordNo);
            request.addProperty(pi);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive  result =(SoapPrimitive)envelope.getResponse();

            //Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();

            if (result.toString().equalsIgnoreCase("true"))
            {
                try{
                    XmlData xml= new XmlData(context);
                    xml.Update_Roster_Node(RecordNo,"Carer_code",this.StaffCode);
                    xml.Update_Roster_Node(RecordNo,"Roster_Type","2");
                    xml=null;
                }catch(Exception ex){}

                Tost_Message("Booking Accepted Successfully");
                doService2();
            }else{

                Tost_Message( "No Status Updated Successfully");
            }

        }catch(Exception ex){ Tost_Message( ex.toString());}
    }

    //irfan

    public void accept_Rosters() {

        // Create the AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the title and message
        builder.setTitle("Notification");
        builder.setMessage("Your proposed roster has been published in the MTA app. Please review and accept the proposed rosters or discuss any changes you require with your supervisor. Your acceptance of the proposed roster is your agreement to vary your pattern of work in accordance with the SCHADS award. Failure to accept the proposed roster will mean that the shifts have not been confirmed.");

        // Add positive button (OK)
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle "OK" button click
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Perform SOAP request in background thread
                        //String result = makeSoapRequest();

                        try {
                            acceptRosters();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Update UI with result on the main thread
                            }
                        });
                    }
                }).start();

            }
        });

        // Add negative button (Cancel)
        builder.setNegativeButton("Changes Required", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle "Cancel" button click
                rejectRosters();
            }
        });

        // Optional: Add a neutral button (can be used for a third option)
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle "Neutral" button click



            }
        });

        // Show the dialog
        builder.show();
    }




    void acceptRosters() throws IOException {


        if (Server_Available==false)
        {

            XmlData xml= new XmlData(context);
            xml.updateAllRosters();
            xml=null;

//            String command;
//            command = "UPDATE roster SET acceptedbyStaff='True' where [date] between '2025/03/21' and '2025/03/22'  and [Carer Code]='ali Arsalan 2'";
//            try {
//                set_Updates(command);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }

            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("SyncAcceptedRosters", true);
            editor.apply();

            runOnUiThread(new Runnable() {


                @Override
                public void run() {
                    // Your UI update code here
                    showAlertMessage("Rosters accepted successfully");
                    doService2();

                }
            });
            return;
        }


        String URL = root  + "/TimeSheet.asmx?op=AcceptRosters";
        String SOAP_ACTION =  "https://tempuri.org/AcceptRosters";
        String METHOD_NAME = "AcceptRosters";

        try{
            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug =true;



            PropertyInfo pi2=new PropertyInfo();
            pi2.setName("StaffCode");
            pi2.setValue(getSecurityToken() + this.StaffCode);
            request.addProperty(pi2);

            PropertyInfo pi=new PropertyInfo();
            pi.setName("start_date");
            pi.setValue("2025/03/21");
            request.addProperty(pi);

            PropertyInfo pi3=new PropertyInfo();
            pi3.setName("end_date");
            pi3.setValue("2025/04/04");
            request.addProperty(pi3);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive  result =(SoapPrimitive)envelope.getResponse();

            //Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();

            if (result.toString().equalsIgnoreCase("true"))
            {

                load_Roster_data();

                runOnUiThread(new Runnable() {


                    @Override
                    public void run() {
                        // Your UI update code here
                        showAlertMessage("Rosters accepted successfully");
                        doService2();

                    }
                });


            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Your UI update code here
                        showAlertMessage("Rosters not accepted");
                    }
                });

            }

        }catch(Exception ex){ Tost_Message( ex.toString());}
    }

    public void syncAcceptRostersWebService(String staffCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String URL = root  + "/TimeSheet.asmx?op=AcceptRosters";
                String SOAP_ACTION =  "https://tempuri.org/AcceptRosters";
                String METHOD_NAME = "AcceptRosters";

                try{
                    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                    androidHttpTransport.debug =true;



                    PropertyInfo pi2=new PropertyInfo();
                    pi2.setName("StaffCode");
                    pi2.setValue(getSecurityToken() + StaffCode);
                    request.addProperty(pi2);

                    PropertyInfo pi=new PropertyInfo();
                    pi.setName("start_date");
                    pi.setValue("2025/03/21");
                    request.addProperty(pi);

                    PropertyInfo pi3=new PropertyInfo();
                    pi3.setName("end_date");
                    pi3.setValue("2025/04/04");
                    request.addProperty(pi3);


                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                    envelope.dotNet=true;
                    envelope.setOutputSoapObject(request);
                    // Make the soap call.
                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    SoapPrimitive  result =(SoapPrimitive)envelope.getResponse();

                    //Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();

                    if (result.toString().equalsIgnoreCase("true")) {


                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("SyncAcceptedRosters", false);
                        editor.apply();

                        load_Roster_data();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            doService2();
                        }
                    });

                } catch (final Exception ex) {
                    ex.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Tost_Message(ex.toString());
                        }
                    });
                }
            }
        }).start();
    }

    void rejectRosters(){

        // Create the AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the title and message
        builder.setTitle("Notification");
        builder.setMessage("Please urgently speak to your supervisor about changes to your proposed roster before the upcoming week. Any changes to your roster are your agreement to vary your pattern of work in accordance with the SCHADS award. Failure to accept the proposed roster will mean that the shifts have not been confirmed. You will continue to receive notifications to accept the rosters until your required changes have been made.");



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

    void showAlertMessage(String message){

        // Create the AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the title and message
        builder.setTitle("Notification");
        builder.setMessage(message);



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

    //irfan

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {


      /*  Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPlaceDetectionApi();
                }
                break;
        }
    }
    String resultStr="";
    String best_result="";
    private void callPlaceDetectionApi() throws SecurityException {
        // Toast.makeText(Shift_Detail.this,"Checking Current Location", Toast.LENGTH_SHORT).show();
      /*  final PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
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

                 *//*   Toast.makeText(MainActivity.this, String.format("Place '%s' with " +
                                    "likelihood: %g",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()), Toast.LENGTH_SHORT).show();*//*


                }
                likelyPlaces.release();
            }
        });
*/

    }

    protected boolean shouldAskPermissions() {
        try{
            return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
        }catch (Exception ex){}

        return false;
    }

    protected void askPermissions()  {
        int requestCode = 200;
        try {
            //  requestPermissions(permissions, requestCode);
        }catch (Exception ex){}
    }

    private Location_Address guessCurrentPlace() {
        loc= new Location_Address();
        loc.Address="";
        loc.Latitude=0;
        loc.Longitude=0;
        //    shouldAskPermissions();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //boolean status=  selfPermissionGranted(android.Manifest.permission.ACCESS_FINE_LOCATION);
            //  Toast.makeText(getApplicationContext(), "Getting Location permission denied " , Toast.LENGTH_SHORT).show();
            //if(!status)
            shouldAskPermissions();
            //return loc;
        }
        /*
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                try {

                    PlaceLikelihood placeLikelihood = likelyPlaces.get(0);
                    String content = "";
                    if (placeLikelihood != null && placeLikelihood.getPlace() != null && !TextUtils.isEmpty(placeLikelihood.getPlace().getName()))
                        content = "Most likely place: " + placeLikelihood.getPlace().getName() + "\n" + placeLikelihood.getPlace().getAddress() + "\n";
                    if (placeLikelihood != null)
                        content += "Percent change of being there: " + (int) (placeLikelihood.getLikelihood() * 100) + "%";


                    Current_Address = placeLikelihood.getPlace().getName().toString() + ", " + placeLikelihood.getPlace().getAddress().toString();

                    //loc.Latitude = placeLikelihood.getPlace().getLatLng().latitude;
                    //loc.Longitude = placeLikelihood.getPlace().getLatLng().longitude;
                    //loc.Address = Current_Address;


                    //Save_Current_Location(loc);

                    //likelyPlaces.release();

                }catch (Exception ex) {}
            }
        });
        // Toast.makeText(getApplicationContext(), Current_Address, Toast.LENGTH_SHORT).show();
*/

        return loc;


    }

    void Save_Current_Location(Location_Address loc){

        try {

            settings = getSharedPreferences(PREFS_NAME, 0);
            settings.edit().putString("Address", loc.Address).commit();
            settings.edit().putString("Latitude", ""+loc.Latitude).commit();
            settings.edit().putString("Longitude", ""+loc.Longitude).commit();

            //  Toast.makeText(getApplicationContext(), loc.Latitude +"\n" +  loc.Longitude, Toast.LENGTH_SHORT).show();


        }catch(Exception ex){Tost_Message( ex.toString());}
    }
    void Save_Server_Settings(){

        try {

            settings = getSharedPreferences(PREFS_NAME, 0);
            settings.edit().putString("root", root).commit();
            settings.edit().putString("StaffCode", StaffCode).commit();
            settings.edit().putString("OperatorId", OperatorId).commit();
            settings.edit().putString("Security_Token", Security_Token).commit();
            settings.edit().putBoolean("Server_Available", Server_Available).commit();
            settings.edit().putString("rosterDate", RosterDate).commit();


            //  Toast.makeText(getApplicationContext(), Server_Available +"\n" +  RosterDate, Toast.LENGTH_SHORT).show();


        }catch(Exception ex){Tost_Message( ex.toString());}
    }
    public boolean selfPermissionGranted(String permission) {
        // For Android < Android M, self permissions are always granted.
        boolean result = true;
        int targetSdkVersion=23;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = this.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(this, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }

        return result;
    }


    class MyAsyncClass4_Recipient extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(MainActivity.this);
            pDialog.setMessage("Please wait while loading Recipient data ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
// Add subject, Body, your mail Id, and receiver mail Id.
                try{

                    getRoster_Recipient();

                }catch(Exception ex){ErroString=ex.toString();}
            }

            catch (Exception ex) {
                // ErroString=ex.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();


            //  Toast.makeText(getApplicationContext(), "bandwidth="+bandwidth +"\n" + ErroString, Toast.LENGTH_LONG).show();
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

            PropertyInfo pi4=new PropertyInfo();
            pi4.setName("MobileFutureLimit");
            pi4.setValue(MobileFutureLimit);
            request.addProperty(pi4);

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

    }

} // End of Class
