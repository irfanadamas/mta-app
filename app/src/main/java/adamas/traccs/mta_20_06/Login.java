package adamas.traccs.mta_20_06;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.crashlytics.CrashlyticsRegistrar;
import com.google.firebase.crashlytics.*;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.text.HtmlCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;

import android.text.InputType;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.HashMap;
import java.util.List;

import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlSerializer;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager.WakeLock;
import android.provider.MediaStore.Images;


import adamas.traccs.mta_20_06.msalandroidapp.MainActivity2Factor;
import timesheet.NetworkStateReceiver;


import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.SignInParameters;
import com.microsoft.identity.client.exception.MsalClientException;
import com.microsoft.identity.client.exception.MsalException;
import com.microsoft.identity.client.exception.MsalServiceException;

/**
 *
 * @author arshad
 */

public class Login extends AppCompatActivity  implements NetworkStateReceiver.NetworkStateReceiverListener {

    SharedPreferences settingstwo      = null;
    public String root = ""; //https://10.0.2.2:49884
    private final String NAMESPACE = "https://tempuri.org/";
    String TAG="Permission";
    private String URL4 = root + "/TimeSheet.asmx?op=Login_User";
    private final String SOAP_ACTION4 = "https://tempuri.org/Login_User";
    private final String METHOD_NAME4 = "Login_User";

    private String URL = root + "/TimeSheet.asmx?op=getMonth_Rosters";
    private final String SOAP_ACTION = "https://tempuri.org/getMonth_Rosters";
    private final String METHOD_NAME = "getMonth_Rosters";

    private String URL2 = root + "/TimeSheet.asmx?op=getStaff_Recipient_Detail";
    private final String SOAP_ACTION2 = "https://tempuri.org/getStaff_Recipient_Detail";
    private final String METHOD_NAME2 = "getStaff_Recipient_Detail";

    private String URL3 = root + "/TimeSheet.asmx?op=getAllTaskList";
    private final String SOAP_ACTION3 = "https://tempuri.org/getAllTaskList";
    private final String METHOD_NAME3 = "getAllTaskList";

    private String URL6 = root + "/TimeSheet.asmx?op=getAllAlertGroups";
    private final String SOAP_ACTION6 = "https://tempuri.org/getAllAlertGroups";
    private final String METHOD_NAME6 = "getAllAlertGroups";

    private String URL7 = root + "/TimeSheet.asmx?op=getRoster_Datewise";
    private final String SOAP_ACTION7 = "https://tempuri.org/getRoster_Datewise";
    private final String METHOD_NAME7 = "getRoster_Datewise";

    private static final String USERNAME = "arshad";
    private static final String PASSWORD = "arshad786";
    static boolean start_Job = true;
    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private final Handler progressBarHandler = new Handler();
    private String prog_command = "";
    private boolean autologin;
    private final String UTF8_Encoding = "UTF-8";
    SharedPreferences settings=null;
    private ArrayList<Roster_Info> rosters = null;
    private Roster_Info Current_roster = null;

    String urlString = root + "/traccs_error_log.txt";
    String ErroString = "";
    float bandwidth = 0;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser fbUser;
    String DeviceToken;
    String CompanyLink="";
    String CompanyText="Our Site";
    int MaxFailedLogins=3;
    int loginAttempt=0;
    Email_Settings email_seting = null;

    String KEY="MTASamada2002";
    byte[] myIV = KEY.getBytes(); //{(byte)50,(byte)51,(byte)52,(byte)53,(byte)54,(byte)55,(byte)56,(byte)57};
    byte[] tdesKeyData = KEY.getBytes(); /*{
            (byte)0xA2, (byte)0x15, (byte)0x37, (byte)0x07, (byte)0xCB, (byte)0x62,
            (byte)0xC1, (byte)0xD3, (byte)0xF8, (byte)0xF1, (byte)0x97, (byte)0xDF,
            (byte)0xD0, (byte)0x13, (byte)0x4F, (byte)0x79, (byte)0x01, (byte)0x67,
            (byte)0x7A, (byte)0x85, (byte)0x94, (byte)0x16, (byte)0x31, (byte)0x92 }*/


    Boolean login = false;
    Button btnSettings;
    Button btnOK;
    Button btnMs;

    FirebaseCrashlytics crashlytics;

    TextView textMsg;
    private ISingleAccountPublicClientApplication mSingleAccountApp;
    private IAccount mAccount;

    Boolean AllowMTASaveUserPass=true;
    String DefaultAppNoteCategory="";

    NetworkStateReceiver networkStateReceiver;
    File froot = null;
    Context context;
    String OperatorId = "";
    String UserId = "0";
    String TimesheetReights = "0";
    String StaffCode = "ABC";
    String TMMode = "0";
    String RecipientDocFolder = "";
    String AllowSetTime = "0";
    String TAMode = "0";
    String MobileFutureLimit = "0";
    String AllowPicUpload = "0";
    String MobileIncident = "false";
    String mobilegeocodelimit = "1";
    String Security_Token = "";
    String UserSessionLimit = "10";
    String PinCode = "";
    String CheckAlertInterval = "5";
    String RosterRequested = "0";
    String EnableRosterDelivery = "0";
    String AllowViewBookings = "false";
    String EnableViewNoteCases = "00000";
    String MTAAutRefreshOnLogin = "0";
    String Enable_Shift_Start_Alarm = "0";
    String Server_Link="";

    final String PREFS_NAME = "MTAPrefs";

    static String App_Web_ServiceVersionNo = "26.07.001";
    static String Server_Web_ServiceVersionNo = "";
    //private HttpClient httpsClient;
    int total_allowed_connections = -1;
    int connected_users = 0;
    boolean server_available = true;
    private static final int STATIC_INTEGER_VALUE = 1;
    String StaffLocationUpdateInterval = "5";
    public static final int SIGNATURE_ACTIVITY = 1;

    boolean very_first_time = false;
    LinearLayout mContent;
    Signature mSignature = null;
    View mView;
    Button mClear, mGetSign, mCancel, getsign_login;
    byte[] buffer = null;
    String picturePath = "";
    File mypath;
    public static String tempDir;
    String Apply_Goe_Location_Setting = "false";
    String Enable_Shift_End_Alarm = "0";
    static boolean PinCode_Mode = false;
    boolean PinCode_Mode_login = false;
    int idle_time = 0;
    XmlData xml ;
    String Latitude = "0";
    String Longitude = "0";
    String Location_Address = "-";
    String msg = "";
    View rl_server;
    View rl_sep11;
    SpeedTest spt;
    Bulk_Data bulk_data;
    TextView txtCompanyLink;

    static int MinimumInternetSpeedForOnline = 10;
    private static Context mContext;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    String[] permissions = {
            "android.permission.CAMERA",
            "android.permission.EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.READ_INTERNAL_STORAGE",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_NETWORK_STATE",
            "android.permission.ACCESS_WIFI_STATE",
            "android.permission.GET_ACCOUNTS",
            "com.google.android.providers.gsf.permission.READ_GSERVICES",
            "adamas.traccs.mta.permission.MAPS_RECEIVE",
            "android.permission.CALL_PHONE"

    };

    boolean checkSingleInstance() {
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        // get the info from the currently running task
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(20);
        boolean alreadyTask = false;
        for (ActivityManager.RunningTaskInfo info : taskInfo) {
            ComponentName componentInfo = info.topActivity;
            String value = componentInfo.getPackageName();
            if (value.contains(getPackageName()) && !info.topActivity.getClassName().contains(getPackageName() + ".login")) {
                //if(value.contains(getPackageName()) ){
                alreadyTask = true;
                //Toast.makeText(getApplicationContext(), "You are already logged in to TRACCS MTA!\n" + info.topActivity.getClassName(),Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "You are already logged into TRACCS MTA!", Toast.LENGTH_LONG).show();

                break;
            }
        }

        if (alreadyTask) {
            // finish();
        }

        return alreadyTask;
    }

    protected boolean shouldAskPermissions() {

        try {
            return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
        } catch (Exception ex) {
        }

        return false;
    }

    @TargetApi(23)
    protected void askPermissions() {


        int requestCode = 200;
        try {
            requestPermissions(permissions, requestCode);


        } catch (Exception ex) {
        }
    }

    boolean  check_valid_Time_Form_9_To_5(){

        boolean status=false;
        try {
            SimpleDateFormat tFormat = new SimpleDateFormat("HH:mm");
            Date date = new Date();
            // String strDate = dateFormat.format(date);
            String temp_time = tFormat.format(date);

            Date Curr_Time = tFormat.parse(temp_time);
            Date start_time = tFormat.parse("09:00");
            Date end_time = tFormat.parse("17:00");

            if (Curr_Time.after(start_time) && Curr_Time.before(end_time))
                status= true;


        }catch(Exception ex){ }

        return status;

    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            if (mAuthListener != null) {
                //mAuth.addAuthStateListener(mAuthListener);
            }
        }catch(Exception ex){}
    }

    @Override
    public void onStop() {
        super.onStop();
        try{
            if (mAuthListener != null) {
                // mAuth.removeAuthStateListener(mAuthListener);
            }
        }catch(Exception ex){}
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);

                return false;


            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
    public static void restartActivity(Activity activity){
        if (Build.VERSION.SDK_INT >= 11) {
            activity.recreate();
        } else {
            activity.finish();
            activity.startActivity(activity.getIntent());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_login);
        context=this.getApplicationContext();
        xml = new XmlData(context);

        View rootView = findViewById(R.id.scroll); // your top-level container
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return WindowInsetsCompat.CONSUMED; // stop it propagating further and double-applying
        });

        //        if (!isStoragePermissionGranted()){
//            restartActivity(Login.this);
//        }


        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        EditText txtuser= findViewById(R.id.txtUser);
        TextView txtPinCode= findViewById(R.id.txtPinLabel);

        EditText txtpass= findViewById(R.id.txtPassword);
        Button btnOk= findViewById(R.id.btnOk);
        Button btnSettings= findViewById(R.id.btnSettings);
        btnMs= findViewById(R.id.btnMs);

        ImageView imageView = findViewById(R.id.imageView);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        // Tost_Message(height + ", " + width);

        if (height>1500) {
            imageView.setBackgroundResource(R.drawable.path275_t);


            ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) txtPinCode.getLayoutParams();
            params2.topMargin = params2.topMargin+height/20;

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) txtuser.getLayoutParams();
            params.topMargin = params.topMargin + height / 20;
        }else {
            imageView.setBackgroundResource(R.drawable.path275);
        }
        if (shouldAskPermissions()) {
            askPermissions();
        }

        try {
            FirebaseApp.initializeApp(this);
            mAuth = FirebaseAuth.getInstance();
            //  FirebaseUser currentUser = mAuth.getCurrentUser();
           /*
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (fbUser == null){
                      //  Tost_Message("Fbuser not connected");
                    }else{
                        //Tost_Message("Fbuser  connected " + fbUser);
                    }
                }
            };*/

            // Operations on FirebaseCrashlytics.
            crashlytics = FirebaseCrashlytics.getInstance();
            crashlytics.setCrashlyticsCollectionEnabled(true);

            //  Crashlytics.getInstance().crash();

        }catch(Exception ex){ Tost_Message(ex.toString());}

        textMsg = ((TextView) findViewById(R.id.textMsg));
        btnOK = (Button) findViewById(R.id.btnOk);

        set_Server_Ip();

        try {
            new MyAsyncClass3().execute();
        }catch(Exception ex){}


        if (settings.getBoolean("mslogin",false)){
            try{
                String email =settings.getString("msloginEmail","");
                new MyAsyncClass_LoginUser_With_MS().execute( email);
            }catch(Exception ex){}
        }



        try {
            Check_Login_Mode(btnOK.getContext());
            if (PinCode_Mode) {

                try {

                    Intent i = new Intent(Login.this, PinCode_Login.class);
                    i.putExtra("root",root);
                    startActivity(i);

                }catch(Exception ex){
                }
            }

        } catch (Exception e) {}

        try {

            if (settings.getBoolean("AllowMTASaveUserPass",true)) {
                AutoLogin();
            }else {
            }


        }catch(Exception ex){}


        // txtCompanyLink=findViewById(R.id.txtCompanyLink);
       /* txtCompanyLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Company_Link();
            }
        });*/

        btnOK.setFocusable(true);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );




        btnSettings = (Button) findViewById(R.id.btnSettings);
//        progressBar = new LoadingDialog(btnOK.getContext());
        //   ShowProgressBar_Step(btnOK,100) ;

        try {
            //   get_Server(); //temporarily commented
            //set_Server_Ip();

            autologin = false;
// textMsg.setText("Offline Mode");

            if (isOnline(getApplicationContext()) == true) {
                try {

                    try {
                        spt=  new SpeedTest(root);
                        spt.bindListeners();

                    }catch(Exception ex){}

                    //new MyAsyncClass3().execute();
                } catch (Exception e) {
                }


            }else{

                txtCompanyLink= findViewById(R.id.txtCompanyLink);
                txtuser.setVisibility(View.VISIBLE);
                txtpass.setVisibility(View.VISIBLE);
                btnOk.setVisibility(View.VISIBLE);
                btnSettings.setVisibility(View.VISIBLE);
                txtCompanyLink.setVisibility(View.VISIBLE);

            }

        } catch (Exception e) {
            textMsg.setText(e.toString());
        }


       /* getsign_login = (Button) findViewById(R.id.getsign_login);

        getsign_login.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    get_sign_login(v.getContext());


                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });*/


        btnSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {

                    // crashlytics.getInstance().didCrashOnPreviousExecution(); // Force a crash

                    EditText txtuser= findViewById(R.id.txtUser);
                    Intent intent = new Intent(Login.this, SettingsActivity.class);
                    Bundle b= new Bundle();
                    b.putString("root",root);
                    b.putString("user",txtuser.getText().toString());
                    b.putString("StaffCode",StaffCode);
                    b.putString("Security_Token",Security_Token);
                    b.putString("OperatorId",OperatorId);
                    b.putString("MobileFutureLimit",MobileFutureLimit);
                    b.putString("App_Web_ServiceVersionNo",App_Web_ServiceVersionNo);
                    b.putString("Server_Web_ServiceVersionNo",Server_Web_ServiceVersionNo);


                    intent.putExtras(b);
                    startActivity(intent);

                } catch (Exception e) {

                }
            }
        });

     /*   final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(1000); // duration - 1000 for 1 second and 500 for half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in

        bntPincode.startAnimation(animation);


        if (autologin) {
            btnOK.startAnimation(animation);
            btnOK.requestFocus();
        }
*/
        btnOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            String login_time="",last_login="",msg="";
                try{

                    SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd kk:mm:ss z yyyy", Locale.US);
//MM-dd-yyyy HH:mm:ss
                    login_time= Calendar.getInstance().getTime().toString();// sdf.format(getTodaysDate() +"-" + getCurrentTime());
                    last_login=settings.getString("lastLogin", "2021/01/01/ 20:20:00");
                   if (loginAttempt==0)
                    loginAttempt=settings.getInt("loginAttempt", 0);
                    Date d1,d2;
                     d1 = sdf.parse(login_time);
                    try {
                         d2 = sdf.parse(last_login);
                    }catch (Exception ex){ d2 = sdf.parse(login_time);}

                    long time_difference=d1.getTime()- d2.getTime();
                    long minutes_difference = (time_difference / (1000*60)) % 60;


                    settings.edit().putString("lastLogin", login_time).commit();
                    settings.edit().putInt("loginAttempt", loginAttempt).commit();

                    msg="Only " +loginAttempt+" login attempts allowed - wait 10 mins before trying again";

                    if (minutes_difference>10  )
                        loginAttempt=0;

                     if (loginAttempt>=MaxFailedLogins){
                                Tost_Message(msg);
                                textMsg.setText(msg);
                                return;
                            }
                        }catch(Exception ex){}


                if (PinCode_Mode) {
                    try {
                        new MyAsyncClass_LoginUser_With_PinCode().execute();

                    } catch (Exception ex) {
                    }
                    return;
                }



                try {
                    textMsg.setText("Authenticating login");
                    PinCode_Mode_login = false;
                    goto_login();

                } catch (Exception e) {
                    textMsg.setText(e.toString());
                }
            }
        });

        btnMs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Login_With_Microsoft_Account();
            }
        });

       /* btnServer = (Button) findViewById(R.id.btnServer);
        btnServer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {

                    try {
                        //  new MyAsyncClass_check().execute();
                    } catch (Exception ex) {
                    }

                    //   if(true) return;

                    if (btnServer.getText().toString().trim().equalsIgnoreCase("Set Server")) {
                        btnServer.setText("Save Server");
                        rl_server.setVisibility(View.VISIBLE);
                        rl_sep11.setVisibility(View.VISIBLE);

                    } else {

                        set_Server(txtServer.getText().toString());
                        tv_server_link.setText(txtServer.getText().toString());

                        set_Server_Ip();
                        btnServer.setText("Set Server");
                        rl_server.setVisibility(View.GONE);
                        rl_sep11.setVisibility(View.GONE);
                    }


                } catch (Exception e) {
                    textMsg.setText(e.toString());
                }
            }
        });
        */
/*
        Button getSignature = (Button) findViewById(R.id.signature);
        getSignature.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, CaptureSignature.class);
                Bundle bundle = new Bundle();
                bundle.putString("root", root);
                bundle.putString("OperatorId", OperatorId);
                bundle.putString("Security_Token", Security_Token);

                intent.putExtras(bundle);
                startActivityForResult(intent, SIGNATURE_ACTIVITY);
            }
        });
        Button btnSMTP = (Button) findViewById(R.id.btnSMTP);
        btnSMTP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {

                    check_SMTP_Server_Setting();

                } catch (Exception e) {
                    textMsg.setText(e.toString());

                }
            }
        });
*/
  /*      Button btnModeSetting = (Button) findViewById(R.id.btnModeSetting);
        btnModeSetting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {

                    Set_Login_Mode(view.getContext());

                } catch (Exception e) {
                    textMsg.setText(e.toString());

                }
            }
        });
        Button btnLocation = (Button) findViewById(R.id.btnLocation);

        btnLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {

                    Intent i = new Intent(view.getContext(), LocationActivity.class);
                    startActivity(i);

                } catch (Exception e) {
                    textMsg.setText(e.toString());

                }
            }
        });*/

       /* timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                runOnUiThread(new Runnable() {


                    public void run() {



                        if (isOnline(getApplicationContext())) {
                           // textMsg.setText("Online Mode");
                           // textMsg.setText("");
                            server_available = true;

                        } else {
                          //  textMsg.setText("");
                           //  textMsg.setText("Offline Mode");
                            server_available = false;
                        }


                    }
                });

            }
        }, 1000, 1000);*/

        //Link fixed in App by irfan 23 Jun 21
//SB Care

//        settingstwo         = getSharedPreferences("MTAPrefs", 0);
//        settingstwo.edit().putString("LinkName","SB Care").commit();
//        settingstwo.edit().putString("root","http://ta.sbcare.org.au/timesheet").commit();



//Integrated Living


//            settingstwo         = getSharedPreferences("MTAPrefs", 0);
//            settingstwo.edit().putString("LinkName","Integrated Living").commit();
//            settingstwo.edit().putString("root","https://mta.integratedliving.org.au").commit();

    }


    @Override
    public void onUserInteraction() {
        idle_time = 0;
        //Toast.makeText(getApplicationContext(), "User has intracted "  , Toast.LENGTH_LONG).show();

    }
    @SuppressLint("GestureBackNavigation")
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);

    }
    @Override
    public void onResume() {
        super.onResume();
        try {
            // Check_Login_Mode(btnOK.getContext());
            if (isOnline(this)) {server_available=true;}
            Switching_Mode();
            EditText usr = (EditText) findViewById(R.id.txtUser);
            // settings = getSharedPreferences(PREFS_NAME, 0);

            if (settings.getBoolean("my_first_time", true) || usr.getText().toString().equals("")) {

                settings.edit().putBoolean("my_first_time", false).commit();
                //    TextView lblVersion=  ((TextView) findViewById(R.id.lblVersionNo));
                //  lblVersion.setText("Version No. " + App_Web_ServiceVersionNo);

            }
            set_Server_Ip();

            txtCompanyLink= findViewById(R.id.txtCompanyLink);
           // CompanyText= findViewById(R.id.txtCompanyName);

            CompanyText = settings.getString("AgencyPortalText","Our Site");
            CompanyLink = settings.getString("AgencyPortal","");
            //   txtCompanyLink.setText(CompanyText);
            txtCompanyLink.setVisibility(View.VISIBLE);

        } catch (Exception e) {
        }

    }

    void Login_With_Microsoft_Account(){

        if (settings.getBoolean("mslogin",false)){
           try{
               btnMs.setText("Logout Microsoft");
               String email =settings.getString("msloginEmail","");
               new MyAsyncClass_LoginUser_With_MS().execute( email);
           }catch(Exception ex){}
        }else {
            btnMs.setText("Login with Microsoft");
            try {
                Intent it = new Intent(getApplicationContext(), MainActivity2Factor.class);
                //  startActivity(it);
                startActivityForResult(it, 2);


            }catch(Exception ex){}
        }
//        PublicClientApplication.createSingleAccountPublicClientApplication(this.context,
//                R.raw.auth_config_single_account,
//                new IPublicClientApplication.ISingleAccountApplicationCreatedListener() {
//                    @Override
//                    public void onCreated(ISingleAccountPublicClientApplication application) {
//                        /**
//                         * This test app assumes that the app is only going to support one account.
//                         * This requires "account_mode" : "SINGLE" in the config json file.
//                         **/
//                        mSingleAccountApp = application;
//                        loadAccount();
//                       String accessScope []= {"user.read"};
//
//                        mSingleAccountApp.signIn (
//                                SignInParameters.builder()
//                                        .withActivity(this.clone())
//                                        .withScopes(accessScope)
//                                         .withCallback(
//                                                 getAuthInteractiveCallback()).build()
//                                );
//
//                       // mSingleAccountApp.signIn (this,null,accessScope, getAuthInteractiveCallback());
//
//                    }
//
//                    @Override
//                    public void onError(MsalException exception) {
//                        displayError(exception);
//                    }
//                });
    }
    private void loadAccount() {
        if (mSingleAccountApp == null) {
            return;
        }

        mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback() {
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount) {
                // You can use the account data to update your UI or your app database.
                mAccount = activeAccount;
               // updateUI();
            }

            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount, @Nullable IAccount currentAccount) {
                if (currentAccount == null) {
                    // Perform a cleanup task as the signed-in account changed.
                   // showToastOnSignOut();
                }
            }

            @Override
            public void onError(@NonNull MsalException exception) {
                displayError(exception);
            }
        });
    }
    private AuthenticationCallback getAuthInteractiveCallback() {
        return new AuthenticationCallback() {

            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                /* Successfully got a token, use it to call a protected resource - MSGraph */
                Log.d(TAG, "Successfully authenticated");
                Log.d(TAG, "ID Token: " + authenticationResult.getAccount().getClaims().get("id_token"));

                /* Update account */
                mAccount = authenticationResult.getAccount();
               // updateUI();

                /* call graph */
               // callGraphAPI(authenticationResult);
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());
                displayError(exception);

                if (exception instanceof MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception instanceof MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                }
            }

            @Override
            public void onCancel() {
                /* User canceled the authentication */
                Log.d(TAG, "User cancelled login.");
            }
        };
    }
    private void displayError(@NonNull final Exception exception) {
        textMsg.setText(exception.toString());
    }
    public void Company_Link()
    {
        // CompanyLink= settings.getString("CompanyLink","");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(CompanyLink));
        startActivity(browserIntent);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SIGNATURE_ACTIVITY:
                if (resultCode == RESULT_OK) {

                    Bundle bundle = data.getExtras();
                    String status = bundle.getString("status");
                    if (status.equalsIgnoreCase("done")) {
                        Toast toast = Toast.makeText(this, "Signature capture successful!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 105, 50);
                        toast.show();
                    }
                }
                break;
            case 2:
                if (settings.getBoolean("mslogin",false)){
                    try{
                        String email =settings.getString("msloginEmail","");
                        new MyAsyncClass_LoginUser_With_MS().execute( email);
                    }catch(Exception ex){}
                }
                break;
        }

    }

    void Make_notification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        /*mBuilder.setSmallIcon(R.drawable.action);
        mBuilder.setContentTitle("Notification Alert, Click Me!");
        mBuilder.setContentText("Hi, This is Android Notification Detail!");

        Intent resultIntent = new Intent(this, ResultActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ResultActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(9999, mBuilder.build());
*/
    }

    /*private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }*/
    public  void Switching_Mode() {
        try {

            //  Control_Settings_on_Mode();

            EditText usr = (EditText) findViewById(R.id.txtUser);
            EditText pass = (EditText) findViewById(R.id.txtPassword);
            TextView txtPinLabel =  findViewById(R.id.txtPinLabel);
            EditText txtPincode = (EditText) findViewById(R.id.txtPassword);

            if (PinCode_Mode) {
                usr.setText("");
                pass.setText("");
                pass.setHint("Enter 4-7 digits Pin");
                //  txtPincode.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);

                usr.setVisibility(View.GONE);
                pass.setVisibility(View.VISIBLE);

                txtPincode.setVisibility(View.VISIBLE);
                txtPinLabel.setVisibility(View.VISIBLE);

            } else {
                usr.setVisibility(View.VISIBLE);
                pass.setVisibility(View.VISIBLE);
                //   txtPincode.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

                //   setMargins(btnOK,0,600,0,0);
                // txtPincode.setVisibility(View.INVISIBLE);
                txtPinLabel.setVisibility(View.INVISIBLE);

                PinCode_Mode = false;
                //  btnPincode.setText("Switch to Pin Code Mode");
                //bntPincode.setTag("0");


            }

        } catch (Exception e) {
            textMsg.setText(e.toString());

        }
    }

    void Login_User_With_Signature() {

        String URL5 = root + "/TimeSheet.asmx?op=Login_User_Signature";
        String SOAP_ACTION5 = "https://tempuri.org/Login_User_Signature";
        String METHOD_NAME5 = "Login_User_Signature";

        SoapPrimitive result = null;
        SoapObject obj = null;
        TextView lblMsg = (TextView) findViewById(R.id.textMsg);

        try {
            Toast.makeText(getApplicationContext(), "Logging with Signature", Toast.LENGTH_LONG).show();

            if (buffer == null) {
                Toast.makeText(getApplicationContext(), "Invalid Signature", Toast.LENGTH_LONG).show();
                return;
            }

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME5);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
            androidHttpTransport.debug = true;

            try {

                PropertyInfo pi = new PropertyInfo();
                pi.type = MarshalBase64.BYTE_ARRAY_CLASS;
                pi.setName("Signature");
                String base64String = Base64.encode(buffer);
                pi.setValue(base64String);
                request.addProperty(pi);

                PropertyInfo pi2 = new PropertyInfo();
                pi2.setName("Fontra");
                pi2.setValue("99");
                request.addProperty(pi2);


            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }

            try {
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.encodingStyle = SoapEnvelope.ENC;
                envelope.setOutputSoapObject(request);
                // Make the soap call.
                androidHttpTransport.call(SOAP_ACTION5, envelope);
                obj = (SoapObject) envelope.getResponse();

                // textMsg.setText( obj.getName() + " " + obj.getPropertyCount());
                result = null;
                if (obj != null) {
                    if (obj.getPropertyCount() > 0)
                        result = (SoapPrimitive) obj.getProperty("Login");
                }

                if (result == null) {
                    textMsg.setText("Invalid User");
                    Toast.makeText(getApplicationContext(), "No User found with this Signature, Invalid User", Toast.LENGTH_LONG).show();
                } else if (result.toString().equalsIgnoreCase("false")) {
                    textMsg.setText("Invalid User");
                    Toast.makeText(getApplicationContext(), "No User found with this Signature, Invalid User", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Login successfully", Toast.LENGTH_LONG).show();
                    Prepare_Main_Screen_After_Login(obj);
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    SoapObject Login_User_With_PinCode() {

        String URL5 = root + "/TimeSheet.asmx?op=Login_Staff_PinCode";
        String SOAP_ACTION5 = "https://tempuri.org/Login_Staff_PinCode";
        String METHOD_NAME5 = "Login_Staff_PinCode";

        EditText txtPincode = (EditText) findViewById(R.id.txtPassword);

        if (txtPincode.getText().toString().equals("")) {

            // Toast.makeText(getApplicationContext(),"Please Enter Pin Code" , Toast.LENGTH_LONG).show();
            Tost_Message("Please Enter Pin Code");
            return null;
        }

        SoapPrimitive result = null;
        SoapObject obj = null;
        TextView lblMsg = (TextView) findViewById(R.id.textMsg);

        try {
            //Toast.makeText(getApplicationContext(),"Logging with Pin Code" , Toast.LENGTH_LONG).show();
            // Tost_Message("Logging with Pin Code") ;
            if (txtPincode.getText().toString() == null || txtPincode.getText().toString().equals("")) {
                //   Toast.makeText(getApplicationContext(), "Invalid Pin Code", Toast.LENGTH_LONG).show();
                Tost_Message("Invalid Pin Code");
                return null;
            }

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME5);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
            androidHttpTransport.debug = true;

            try {


                PropertyInfo pi = new PropertyInfo();
                pi.setName("PinCode");
                String PinCode = txtPincode.getText().toString();
                pi.setValue(PinCode);
                request.addProperty(pi);


                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                // Make the soap call.
                androidHttpTransport.call(SOAP_ACTION5, envelope);
                obj = (SoapObject) envelope.getResponse();


            } catch (Exception e) {
                Tost_Message(e.toString());
            }
        } catch (Exception e) {
            Tost_Message(e.toString());
        }

        return obj;

    }
    SoapObject Login_User_With_MS(String email) {

        String URL5 = root + "/TimeSheet.asmx?op=Login_user_with_Microsoft";
        String SOAP_ACTION5 = "https://tempuri.org/Login_user_with_Microsoft";
        String METHOD_NAME5 = "Login_user_with_Microsoft";



        SoapPrimitive result = null;
        SoapObject obj = null;
        TextView lblMsg = (TextView) findViewById(R.id.textMsg);

        try {
            //Toast.makeText(getApplicationContext(),"Logging with Pin Code" , Toast.LENGTH_LONG).show();

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME5);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
            androidHttpTransport.debug = true;

            try {


                PropertyInfo pi = new PropertyInfo();
                pi.setName("email");
                pi.setValue(email);
                request.addProperty(pi);


                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                // Make the soap call.
                androidHttpTransport.call(SOAP_ACTION5, envelope);
                obj = (SoapObject) envelope.getResponse();


            } catch (Exception e) {
                Tost_Message(e.toString());
            }
        } catch (Exception e) {
            Tost_Message(e.toString());
        }

        return obj;

    }

    @Override
    public void networkAvailable() {

        if (server_available==false) {

        }

        server_available = true;
        /* TODO: Your connection-oriented stuff here */
    }

    @Override
    public void networkUnavailable() {
        server_available = false;


        // textMsg.setText(output + "\t Offline Mode \n Location settings disabled");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }

    String Login_User_With_PinCode_Job_Processing() {

        String URL5 = root + "/TimeSheet.asmx?op=Login_PinCode_With_Processing_Job";
        String SOAP_ACTION5 = "https://tempuri.org/Login_PinCode_With_Processing_Job";
        String METHOD_NAME5 = "Login_PinCode_With_Processing_Job";

        EditText txtPincode = (EditText) findViewById(R.id.txtPassword);


        SoapPrimitive result = null;
        SoapObject obj = null;
        TextView lblMsg = (TextView) findViewById(R.id.textMsg);


        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME5);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
        androidHttpTransport.debug = true;

        try {

            PropertyInfo pi = new PropertyInfo();
            pi.setName("PinCode");
            String PinCode = txtPincode.getText().toString();
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

    public void Get_Job_Processing() {

        // doService2();
        if (rosters != null)
            Current_roster = get_current_Item();

        if (Current_roster == null) {
            Tost_Message("No current Job found to process");
            // Toast.makeText(getApplicationContext(),"No current Job found to process", Toast.LENGTH_LONG).show();
            // textMsg.setText("No current Job found to process");
            return;
        }
        Button btnok = (Button) findViewById(R.id.btnOk);

        if (rosters.size() > 0) {

            try {
                if (start_Job) {

                    msg = "Do you want to start Job : " + Current_roster.getServiceType()
                            + "\n From " + Current_roster.getStart_Time() + " to " + Current_roster.get_End_Time();
                    msg = msg + "\nRecord No. " + Current_roster.getRecordNo() + "\nRoster Date: " + Current_roster.getRoster_Date() +
                            "\nClient Code : " + Current_roster.getClient_code() + "\nStart Time : " + Current_roster.getStart_Time() + ", Duration :" + Current_roster.get_Calculated_Duration() + "\nNotes : " + Current_roster.getNotes();

                    ShowDialog(btnok, msg);
                } else {
                    msg = "Job Stared\nDo you want to stop Job : " + Current_roster.getServiceType()
                            + "\n From " + Current_roster.getStart_Time() + " to " + Current_roster.get_End_Time();

                    msg = msg + "\nRecord No. " + Current_roster.getRecordNo() + "\nRoster Date: " + Current_roster.getRoster_Date() +
                            "\nClient Code : " + Current_roster.getClient_code() + "\nStart Time : " + Current_roster.getStart_Time() + ", Duration :" + Current_roster.get_Calculated_Duration() + "\nNotes : " + Current_roster.getNotes();

                    ShowDialog(btnok, msg);
                }


                //  TextView txtPinMsg= (TextView)findViewById(R.id.txtPinMsg);
                //  Toast.makeText(getApplicationContext(),msg  , Toast.LENGTH_LONG).show();

                //  txtPinMsg.setText(msg);

                //  ShowDialog(getApplicationContext(),msg);
                //Shift_Detail();

            } catch (Exception e) {  //Toast.makeText(getApplicationContext(),e.toString()  , Toast.LENGTH_LONG).show();
            }
        } else {
            msg = "No Job Exists to process at this time";
            //TextView txtPinMsg = (TextView) findViewById(R.id.txtPinMsg);
            //  Toast.makeText(getApplicationContext(),msg  , Toast.LENGTH_LONG).show();
            textMsg.setText(msg);
        }
    }

    public void ShowDialog(View v, String commandText) {
        try {


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

          //  alertDialogBuilder.setIcon(R.drawable.action);
            //alertDialogBuilder.setCustomTitle(customTitleView)

            alertDialogBuilder.setTitle("Currently Selected Roster");
            alertDialogBuilder
                    .setMessage(commandText)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing

                            try {
                                AsyncTaskTools.execute(new MyAsyncClass_start().execute());
                            } catch (Exception ex) {
                            }

                           /* if (start_Job){
                                Set_Job(Current_roster.getRecordNo());

                                msg= "Your Job has been started successfull\n"
                                        + " From " + Current_roster.getStart_Time() + " to " + Current_roster.get_End_Time();
                            }else{
                                End_Job(Current_roster.getRecordNo());

                                msg= "Your Job has been stopped successfully \n"
                                        + " From " + Current_roster.getStart_Time() + " to " + Current_roster.get_End_Time();*/


                            // Toast.makeText(getApplicationContext(),msg  , Toast.LENGTH_LONG).show();

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

    public void get_sign_login(Context context) {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.signature2);
            dialog.setTitle("Sign to Login");

            try {
                tempDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + getResources().getString(R.string.external_dir) + "/";
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File directory = cw.getDir(getResources().getString(R.string.external_dir), Context.MODE_PRIVATE);

                prepareDirectory();
                String uniqueId = getTodaysDate() + "_" + getCurrentTime() + "_" + Math.random();
                String current = uniqueId + ".png";
                mypath = new File(directory, current);

            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }


            mContent = (LinearLayout) dialog.findViewById(R.id.linearLayout_sign2);
            mSignature = new Signature(Login.this, null);
            mSignature.setBackgroundColor(Color.WHITE);
            mContent.addView(mSignature, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);


            mCancel = (Button) dialog.findViewById(R.id.cancel);
            mClear = (Button) dialog.findViewById(R.id.clear);
            mGetSign = (Button) dialog.findViewById(R.id.getsign);


            mGetSign.setEnabled(false);
            mView = mContent;

            mClear.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Log.v("log_tag", "Panel Cleared");
                    mSignature.clear();
                    mGetSign.setEnabled(false);
                }
            });

            mGetSign.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    try {
                        //.v("log_tag", "Panel Saved");
                        boolean error = false; //captureSignature();
                        if (!error) {
                            mView.setDrawingCacheEnabled(true);
                            mSignature.save(mView);
                            Login_User_With_Signature();
                            Bundle b = new Bundle();
                            b.putString("status", "done");
                            Intent intent = new Intent();
                            intent.putExtras(b);
                            setResult(RESULT_OK, intent);
                            //finish();
                            dialog.dismiss();
                        }

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            mCancel.setOnClickListener(new OnClickListener() {
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
                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                    }

                }

            });

            dialog.show();
            // yourName = (EditText) findViewById(R.id.yourName);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }

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
        return (String.valueOf(currentTime));

    }


    private boolean prepareDirectory() {
        try {
            return makedirs();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Could not initiate File System.. Is Sdcard mounted properly?", Toast.LENGTH_LONG).show();
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

    public void goto_login() {

        String user, password;

        EditText usr = (EditText) findViewById(R.id.txtUser);
        EditText pass = (EditText) findViewById(R.id.txtPassword);
        user=usr.getText().toString();
        password=pass.getText().toString();

        //EditText txtServer = (EditText) findViewById(R.id.txtPort);
        textMsg = ((TextView) findViewById(R.id.textMsg));
        if (user.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter valid user", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        //  if (txtServer.getText().toString().equalsIgnoreCase("")) {
        if (Server_Link.equals("")){
            Toast.makeText(this, "Please enter valid server address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isOnline(Login.this)) {

            try {
                new MyAsyncClass_login().execute(user, password, "");
                // new MyAsyncClass_login().execute(usr.getText().toString(),pass.getText().toString(),"");
                // login_user();
            } catch (Exception ex) {  textMsg.setText(ex.toString());
                Log.d("login", ex.toString());
            }

        } else {
            //Offline working
            server_available=false;

            Boolean res = false;
            try {
                res = login_user_Local(user, password);
            } catch (Exception ex) {
                textMsg.setText(ex.toString());
            }

            if (res == false)
                textMsg.setText("Invalid User Information");
            // Read_File("/.server/task.xml");
            //  textMsg.setText("Uploading Previous Updates");
        }
        //  dialog.dismiss();

    }

    public void set_Server_Ip() {

       // settings.edit().putString("root","http://mta.sjccl.org.au/2102006/").commit();

        root = settings.getString("root","") ;// txtServer.getText().toString();

        if (root==null || root.equalsIgnoreCase("") || root.equalsIgnoreCase("null"))
        {
            try {
                get_Server();
            }catch(Exception ex){}
        }
        Server_Link=root;
        URL = root + "/TimeSheet.asmx?op=getRoster_Datewise";
        URL4 = root + "/TimeSheet.asmx?op=Login_User";
        URL2 = root + "/TimeSheet.asmx?op=getStaff_Recipient_Detail";
        URL3 = root + "/TimeSheet.asmx?op=getAllTaskList";
        URL6 = root + "/TimeSheet.asmx?op=getAllAlertGroups";
        URL7 = root + "/TimeSheet.asmx?op=getRoster_Datewise";
        urlString = root + "/timesheet.asmx";

    }


    void start_service() {

        try {

            // Intent intnt= new Intent(this, Alert_Service.class)	;
            Intent intnt = new Intent(this, load_data_service.class);
            Bundle b = new Bundle();
            b.putString("root", root);

            String format = "yyyy/MM/dd";

            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Calendar c = Calendar.getInstance();
            Date dt = c.getTime();

            String rosterDate = sdf.format(dt);

            //  Toast.makeText(getApplicationContext(), "Loading data from server for " + StaffCode, Toast.LENGTH_LONG).show();

            b.putString("StaffCode", StaffCode);
            b.putString("Roster_Date", rosterDate);
            b.putBoolean("Server_Available", server_available);
            b.putString("OperatorId", OperatorId);
            b.putString("Security_Token", Security_Token);
            intnt.putExtras(b);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            if (server_available == true)
                startService(intnt);


        } catch (Exception ex) {
        }
    }

    void start_service2() {

        try {

            // Intent intnt= new Intent(this, Alert_Service.class)	;
            Intent intnt = new Intent(this, Load_Data_Service2.class);
            Bundle b = new Bundle();
            b.putString("root", root);

            String format = "yyyy/MM/dd";

            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Calendar c = Calendar.getInstance();
            Date dt = c.getTime();

            String rosterDate = sdf.format(dt);

            //  Toast.makeText(getApplicationContext(), "Loading data from server for " + StaffCode, Toast.LENGTH_LONG).show();

            b.putString("StaffCode", StaffCode);
            b.putString("Roster_Date", rosterDate);
            b.putBoolean("Server_Available", server_available);
            b.putString("OperatorId", OperatorId);
            b.putString("Security_Token", Security_Token);
            intnt.putExtras(b);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            if (server_available == true)
                startService(intnt);


        } catch (Exception ex) {
        }
    }

    private final Runnable myThread = new Runnable() {
        private int myProgress = 0;

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (myProgress < 10) {
                try {
                    myHandle.sendMessage(myHandle.obtainMessage());
                    Thread.sleep(1000);
                } catch (Throwable t) {
                }
            }
            //---hides the progress bar---
            myHandle.post(new Runnable() {
                public void run() {
                    //---0 - VISIBLE; 4 - INVISIBLE; 8 - GONE---
                    //progressBar.setVisibility(8);
                    textMsg.setText("Please wait while processing ....");
                }
            });
        }

        final Handler myHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                myProgress++;
                progressBar.setProgress(myProgress);
            }
        };
    };


    /* public void onReceive(WifiManager wifiManager) {
         int numberOfLevels=5;
         WifiInfo wifiInfo = wifiManager.getConnectionInfo();
         int level=WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
         textMsg=  ((TextView) findViewById(R.id.textMsg));
         textMsg.setText("Bars =" +level);
         Toast.makeText(getApplicationContext(), "Wifi Received" , Toast.LENGTH_LONG).show();

     }*/
    public void login_user() {
        SoapObject obj = null;
        String dumpresponse = "";
        try {

            set_Server_Ip();

            if (PinCode_Mode) {
                try {
                    new MyAsyncClass_LoginUser_With_PinCode().execute();
                    //Login_User_With_PinCode();

                } catch (Exception ex) {
                }
                return;
            }

            EditText usr = (EditText) findViewById(R.id.txtUser);
            EditText pass = (EditText) findViewById(R.id.txtPassword);
            textMsg = ((TextView) findViewById(R.id.textMsg));


            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME4);

            //  AuthTransportSE androidHttpTransport = new AuthTransportSE(URL, USERNAME, PASSWORD);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL4);

            androidHttpTransport.debug = true;


            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("User");
            String message = usr.getText().toString();
            pi3.setValue(message);
            pi3.setType(PropertyInfo.STRING_CLASS);
            request.addProperty(pi3);

            PropertyInfo pi4 = new PropertyInfo();
            pi4.setName("Password");
            pi3.setType(PropertyInfo.STRING_CLASS);
            message = pass.getText().toString();
            pi4.setValue(message);
            request.addProperty(pi4);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.bodyOut = request;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            envelope.implicitTypes=true;
            // Make the soap call.

            androidHttpTransport.call(SOAP_ACTION4, envelope);
            obj = (SoapObject) envelope.getResponse();
            // obj = (SoapObject) envelope.bodyIn;

            if (obj == null) {
                Object object = getResponse(envelope.bodyIn);

                if (object != null)
                    obj = (SoapObject) envelope.bodyIn;
            }
            // textMsg.setText( obj.getName() + " " + obj.getPropertyCount());
            SoapPrimitive result = null;

            Tost_Message(obj.getPropertyCount() + "\n" + obj.toString());

            if (obj.getPropertyCount() > 0)
                result = (SoapPrimitive) obj.getProperty("Login");

            if (obj == null || result == null)
                textMsg.setText("Invaid User Information");

            // Toast.makeText(getApplicationContext(), "Getting User Login " + result  , Toast.LENGTH_LONG).show();


            if (result == null)
                textMsg.setText("Invalid User");
            else {

                Prepare_Main_Screen_After_Login(obj);
            }

        } catch (Exception ex) {
            textMsg.setText(textMsg.getText() + "\n" + ex.getMessage());
            // Toast.makeText(getApplicationContext(), ex.toString() , Toast.LENGTH_LONG).show();
            // textMsg.setText( "SERVER SETTINGS ARE INCORRECT: PLEASE ENTER A VALID SERVER ID");
            Toast.makeText(getApplicationContext(), "SERVER SETTINGS ARE INCORRECT: PLEASE ENTER A VALID SERVER ID", Toast.LENGTH_LONG).show();
        }

    }


    public Object getResponse(Object bodyIn) throws SoapFault {
        if (bodyIn instanceof SoapFault) {
            throw (SoapFault) bodyIn;
        }
        KvmSerializable ks = (KvmSerializable) bodyIn;

        if (ks.getPropertyCount() == 0) {
            return null;
        } else if (ks.getPropertyCount() == 1) {
            return ks.getProperty(0);
        } else {
            Vector ret = new Vector();
            for (int i = 0; i < ks.getPropertyCount(); i++) {
                ret.add(ks.getProperty(i));
            }
            return ret;
        }
    }


    void send_email(String Note) {


        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra("loginName", "arshadblouch81@gmail.com");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"arshadblouch81@yahoo.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        i.putExtra(Intent.EXTRA_TEXT, "body of email\n" + Note);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            textMsg.setText(ex.toString());
        }
    }

    public void get_Server() throws IOException {
        // File froot = null;
        try {
            // check for SDcard

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            File filein = null;

            File fileDir = null;
            File fileDir2 = null;
            File fileDir3 = null;

            String state = Environment.getExternalStorageState();
            // textMsg.setText("SD Card Status - " + state);
            if (Environment.MEDIA_MOUNTED.equals(state)) {

                try {
                    //   fileDir2 = new File(froot.getAbsolutePath() + "/server/");
                    //    fileDir3 = new File(froot.getAbsolutePath() + "/.server/");
                   /* if (fileDir2.exists() && !fileDir3.exists()) {
                        File fnew = new File(froot.getAbsolutePath() + "/.server/");
                        fileDir2.renameTo(fnew);
                    }*/
                } catch (Exception ex) {
                }
                finally {

                }

                //check sdcard permission

                fileDir = new File(froot.getAbsolutePath() + "/.server/");
                filein = new File(fileDir, "serverIp.txt");
                if (filein.exists()) {
                    try {
                        FileReader fileReader = new FileReader(filein);
                        BufferedReader buf = new BufferedReader(fileReader);
                        String name = buf.readLine();
                        String add = buf.readLine();
                        if (add==null ||add.equalsIgnoreCase("") || add.equalsIgnoreCase("null")){
                            fileReader.close();
                            buf.close();
                            settings.edit().putString("LinkName","MTA").commit();
                            settings.edit().putString("root",name).commit();
                            set_Server_first_time(name,"MTA");
                            return;
                        }
                        Server_Link=add;
                        root=add;
                        settings.edit().putString("LinkName",name).commit();
                        settings.edit().putString("root",root).commit();
                        //tv_server_link.setText(add);
                        //  Button btnSMTP = (Button) findViewById(R.id.btnSMTP);
                        //btnSMTP.setVisibility(View.VISIBLE);

                    } catch (Exception e) {
                        textMsg.setText(e.toString());
                        //Log.e("Exception","error occurred while creating xml file");
                    }

                }

            }
        } catch (Exception e) {
            textMsg.setText(e.toString());
            //              Log.e("Exception","error occurred while creating xml file");
        }
        // tv_server_link.setText("");
        // txtServer.setText("https://remote.jccagedcare.org.au/timesheet/");
    }
    public void set_Server_first_time(String root, String LinKName) throws IOException {
        // File froot = null;
        try {
            // check for SDcard

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File filein = null;
            File fileDir = null;
            String state = Environment.getExternalStorageState();
            //  textMsg.setText("No Server found - " + state);
            if (Environment.MEDIA_MOUNTED.equals(state)) {

                //check sdcard permission
                fileDir = new File(froot.getAbsolutePath() + "/.server/");

                filein = new File(fileDir, "serverIp.txt");

                if (filein.exists()) {
                    try {
                        froot.setWritable(true);
                        //if (froot.canWrite()) {

                        File file = new File(fileDir, "serverIp.txt");
                        FileWriter filewriter = new FileWriter(file);
                        BufferedWriter out = new BufferedWriter(filewriter);
                        out.write(LinKName + "\n" + root + "\n");

                        out.close();
                        filewriter.close();

                    } catch (Exception e) {

                    }
                } else {
                    froot.setWritable(true);

                    //if (froot.canWrite()) {

                    if (!fileDir.exists())
                        fileDir.mkdirs();

                    File file = new File(fileDir, "serverIp.txt");
                    FileWriter filewriter = new FileWriter(file);
                    BufferedWriter out = new BufferedWriter(filewriter);
                    out.write(root);
                    out.close();
                    filewriter.close();
                }
            }
        } catch (Exception e) {

        }

    }
    void Prepare_Main_Screen_After_Login(SoapObject obj) {
        String user = "";
        String Password = "";
        SoapPrimitive result = null;


        result = (SoapPrimitive) obj.getProperty("Login");



        //   Toast.makeText(getApplicationContext(), "Preparing User Settings " + result.toString()  , Toast.LENGTH_LONG).show();
        settings.edit().putInt("loginAttempt", 0).commit();

        if (Boolean.parseBoolean(result.toString())) {
            try {

                loginAttempt=0;
                settings.edit().putInt("loginAttempt", loginAttempt).commit();

                result = (SoapPrimitive) obj.getProperty("UserName");
                user =result.toString();
                OperatorId = user;
                result = (SoapPrimitive) obj.getProperty("Password");
                Password = result.toString().substring(4);

                //  Toast.makeText(getApplicationContext(), "Password "  + Password , Toast.LENGTH_LONG).show();

                result = (SoapPrimitive) obj.getProperty("TimeSheet");
                TimesheetReights = result.toString();
                result = (SoapPrimitive) obj.getProperty("Staffcode");
                StaffCode = result.toString();
                result = (SoapPrimitive) obj.getProperty("TMMode");
                TMMode = result.toString();

                result = (SoapPrimitive) obj.getProperty("TAMode");
                TAMode = result.toString();
                result = (SoapPrimitive) obj.getProperty("AllowSetTime");
                AllowSetTime = result.toString();
                result = (SoapPrimitive) obj.getProperty("MobileFutureLimit");
                MobileFutureLimit = result.toString();
                result = (SoapPrimitive) obj.getProperty("AllowPicUpload");
                AllowPicUpload = result.toString();

                result = (SoapPrimitive) obj.getProperty("Total_No_of_Usrs");
                total_allowed_connections = Integer.parseInt(result.toString());

                result = (SoapPrimitive) obj.getProperty("Connected_Users");
                connected_users = Integer.parseInt(result.toString());


                UserId = obj.getProperty("UserId").toString();

                if (obj.getProperty("RecipientDocFolder") == null) {
                    RecipientDocFolder = "";
                } else {
                    result = (SoapPrimitive) obj.getProperty("RecipientDocFolder");
                    RecipientDocFolder = result.toString();
                }

                if (RecipientDocFolder.equals("")) RecipientDocFolder = "P:\\TRACCS";


            } catch (Exception ex) {
                textMsg.setText("Setting Gloabl Values " + ex.toString());
            }

            if (obj.getProperty("StaffLocationUpdateInterval") != null) {
                result = (SoapPrimitive) obj.getProperty("StaffLocationUpdateInterval");
                StaffLocationUpdateInterval = result.toString();
            }

            if (Integer.parseInt(StaffLocationUpdateInterval) <= 0) {
                // Toast.makeText(getApplicationContext(), "Staff Location Update Interval set 0, App will not proceed further" , Toast.LENGTH_LONG).show();
                StaffLocationUpdateInterval = "60";
                //return;
            }
            settings.edit().putString("StaffLocationUpdateInterval",StaffLocationUpdateInterval);
            if (obj.getProperty("MobileIncident") != null) {
                result = (SoapPrimitive) obj.getProperty("MobileIncident");
                MobileIncident = result.toString();
            }
            settings.edit().putString("MobileIncident",MobileIncident);
            //  Toast.makeText(getApplicationContext(), "MobileIncident "  + MobileIncident , Toast.LENGTH_LONG).show();

            if (obj.getProperty("mobilegeocodelimit") != null) {
                result = (SoapPrimitive) obj.getProperty("mobilegeocodelimit");
                mobilegeocodelimit = result.toString();

            }
            settings.edit().putString("mobilegeocodelimit",mobilegeocodelimit);
            if (obj.getProperty("Security_Token") != null) {
                result = (SoapPrimitive) obj.getProperty("Security_Token");
                Security_Token = result.toString().substring(3);

            }
            if (obj.getProperty("Apply_Goe_Location_Setting") != null) {
                result = (SoapPrimitive) obj.getProperty("Apply_Goe_Location_Setting");
                Apply_Goe_Location_Setting = result.toString();

            }
            settings.edit().putString("Apply_Goe_Location_Setting",Apply_Goe_Location_Setting);
            UserSessionLimit = "20";
            if (obj.getProperty("UserSessionLimit") != null) {
                result = (SoapPrimitive) obj.getProperty("UserSessionLimit");
                UserSessionLimit = result.toString();

            }
            settings.edit().putString("UserSessionLimit",UserSessionLimit);
            Enable_Shift_End_Alarm = "0";
            try {
                if (obj.getProperty("Enable_Shift_End_Alarm") != null) {
                    result = (SoapPrimitive) obj.getProperty("Enable_Shift_End_Alarm");
                    Enable_Shift_End_Alarm = result.toString();

                }
            } catch (Exception ex) {
            }
            settings.edit().putString("Enable_Shift_End_Alarm",Enable_Shift_End_Alarm);

            Enable_Shift_Start_Alarm = "0";
            try {
                if (obj.getProperty("Enable_Shift_Start_Alarm") != null) {
                    result = (SoapPrimitive) obj.getProperty("Enable_Shift_Start_Alarm");
                    Enable_Shift_Start_Alarm = result.toString();

                }
            } catch (Exception ex) {
            }
            settings.edit().putString("Enable_Shift_Start_Alarm",Enable_Shift_Start_Alarm);


            CheckAlertInterval = "5";
            try {
                if (obj.getProperty("CheckAlertInterval") != null) {
                    result = (SoapPrimitive) obj.getProperty("CheckAlertInterval");
                    CheckAlertInterval = result.toString();

                }
            } catch (Exception ex) {
            }
            settings.edit().putString("CheckAlertInterval",CheckAlertInterval);

            RosterRequested = "5";
            try {
                if (obj.getProperty("RosterRequested") != null) {
                    result = (SoapPrimitive) obj.getProperty("RosterRequested");
                    RosterRequested = result.toString();

                }
            } catch (Exception ex) {
            }
            settings.edit().putString("RosterRequested",RosterRequested);
            AllowViewBookings = "false";
            try {
                if (obj.getProperty("AllowViewBookings") != null) {
                    result = (SoapPrimitive) obj.getProperty("AllowViewBookings");
                    AllowViewBookings = result.toString();

                }
            } catch (Exception ex) {
            }
            settings.edit().putString("AllowViewBookings",AllowViewBookings);
            // Toast.makeText(getApplicationContext(), "AllowViewBookings===" + AllowViewBookings, Toast.LENGTH_SHORT).show();
            EnableViewNoteCases = "00000";
            try {
                if (obj.getProperty("EnableViewNoteCases") != null) {
                    result = (SoapPrimitive) obj.getProperty("EnableViewNoteCases");
                    EnableViewNoteCases = result.toString();

                }
            } catch (Exception ex) {
            }
            settings.edit().putString("EnableViewNoteCases",EnableViewNoteCases);
            MTAAutRefreshOnLogin = "0";
            try {
                if (obj.getProperty("MTAAutRefreshOnLogin") != null) {
                    result = (SoapPrimitive) obj.getProperty("MTAAutRefreshOnLogin");
                    MTAAutRefreshOnLogin = result.toString();

                }
            } catch (Exception ex) {
            }
            settings.edit().putString("MTAAutRefreshOnLogin",MTAAutRefreshOnLogin);
            EnableRosterDelivery = "0";
            try {
                if (obj.getProperty("EnableRosterDelivery") != null) {
                    result = (SoapPrimitive) obj.getProperty("EnableRosterDelivery");
                    EnableRosterDelivery = result.toString();

                }
            } catch (Exception ex) {
            }
            settings.edit().putString("EnableRosterDelivery",EnableRosterDelivery);
            try {
                if (obj.getProperty("MinimumInternetSpeedForOnline") != null) {
                    result = (SoapPrimitive) obj.getProperty("MinimumInternetSpeedForOnline");
                    MinimumInternetSpeedForOnline = Integer.parseInt(result.toString());

                    // final  settings = getSharedPreferences(PREFS_NAME, 0);
                    settings.edit().putInt("MinimumInternetSpeedForOnline", MinimumInternetSpeedForOnline).commit();
                }
            } catch (Exception ex) {
            }
            settings.edit().putInt("MinimumInternetSpeedForOnline",MinimumInternetSpeedForOnline);

            try {
                if (obj.getProperty("AllowMTASaveUserPass") != null) {
                    result = (SoapPrimitive) obj.getProperty("AllowMTASaveUserPass");
                    AllowMTASaveUserPass = Boolean.parseBoolean(result.toString());
                    settings.edit().putBoolean("AllowMTASaveUserPass", AllowMTASaveUserPass).commit();
                }
            } catch (Exception ex) {
            }
            try {
                if (obj.getProperty("DefaultAppNoteCategory") != null) {
                    result = (SoapPrimitive) obj.getProperty("DefaultAppNoteCategory");
                    DefaultAppNoteCategory = (result.toString());
                    settings.edit().putString("DefaultAppNoteCategory", DefaultAppNoteCategory).commit();
                }
            } catch (Exception ex) {
            }

            //  Toast.makeText(getApplicationContext(), "Security_Token "  + Security_Token , Toast.LENGTH_LONG).show();
            //  Add_session();
            if (connected_users > total_allowed_connections) {
                textMsg.setText("User limit " + connected_users + " exceeds from licensed Limit of allowed Users :" + total_allowed_connections);
                Toast.makeText(getApplicationContext(), "User limit " + connected_users + " exceeds from licensed Limit of allowed Users :" + total_allowed_connections, Toast.LENGTH_LONG).show();
                return;
            }

            if (PinCode_Mode_login) {

                // getLocation(getApplicationContext());
                //getLocation_and_do_Job_Setting(getApplicationContext());
                //  Get_Job_Processing();
                // return;
            }

            //  if (autologin==false)
            //  {
            Save_User_Info(user, Password, StaffCode, UserId);

            try{
                new MyAsyncClass_DeviceToken().execute();
            }catch (Exception ex){}

            //  }
            // Toast.makeText(getApplicationContext(), "Loading data "  + Security_Token , Toast.LENGTH_LONG).show();
            textMsg.setText("Loading Data ");
            try {
                //  new MyAsyncClass().execute();

                MainActivity.form_resumed=false;
               // settings.edit().putBoolean("Refresh_OP_Note_data",true).commit();
                if (UpdateExists()) {
                    try {
                        // updated=true;
                        new MyAsyncClassUpload().execute();
                        // load_data();
                    } catch (Exception ex) {
                    }
                } else {
                    load_data();
                }

            } catch (Exception e) {
            }


        } else {
            textMsg.setText("Invalid User Information");
            Tost_Message("Invalid User Information");
        }
    }

    void load_main_form() {

        Intent intent = new Intent(this, MainActivity.class);

        try {
            Bundle bundle = new Bundle();
            bundle.putString("User", OperatorId);
            bundle.putString("root", Server_Link);
            bundle.putString("StaffCode", StaffCode);
            bundle.putString("Server", "True");
            bundle.putString("Timesheet", TimesheetReights);
            bundle.putString("TMMode", TMMode);
            bundle.putString("TAMode", TAMode);
            bundle.putString("AllowSetTime", AllowSetTime);
            bundle.putString("MobileFutureLimit", MobileFutureLimit);
            bundle.putString("AllowPicUpload", AllowPicUpload);
            bundle.putString("RecipientDocFolder", RecipientDocFolder);
            bundle.putString("UserId", UserId);
            bundle.putString("StaffLocationUpdateInterval", StaffLocationUpdateInterval);
            bundle.putString("MobileIncident", MobileIncident);
            bundle.putString("mobilegeocodelimit", mobilegeocodelimit);
            bundle.putString("Security_Token", Security_Token);
            bundle.putString("Apply_Goe_Location_Setting", Apply_Goe_Location_Setting);
            bundle.putString("autologin", "" + autologin);
            bundle.putString("UserSessionLimit", UserSessionLimit);
            bundle.putString("Enable_Shift_End_Alarm", Enable_Shift_End_Alarm);
            bundle.putString("CheckAlertInterval", CheckAlertInterval);
            bundle.putString("RosterRequested", RosterRequested);
            bundle.putString("EnableRosterDelivery", EnableRosterDelivery);
            bundle.putInt("MinimumInternetSpeedForOnline", MinimumInternetSpeedForOnline);
            bundle.putString("AllowViewBookings", AllowViewBookings);
            bundle.putString("EnableViewNoteCases", EnableViewNoteCases);
            bundle.putString("Enable_Shift_Start_Alarm", Enable_Shift_Start_Alarm);
            bundle.putString("MTAAutRefreshOnLogin", MTAAutRefreshOnLogin);

            // Toast.makeText(getApplicationContext(), "AllowViewBookings=" + AllowViewBookings , Toast.LENGTH_LONG).show();

            EditText txtPincode = (EditText) findViewById(R.id.txtPassword);
            if (PinCode_Mode)
                txtPincode.setText("");

            // Toast.makeText(getApplicationContext(), "PinCode_Mode_login=" + PinCode_Mode_login , Toast.LENGTH_LONG).show();

            intent.putExtras(bundle);
            startActivity(intent);
            // Read_File("task.xml");
            textMsg.setText("");

           // Remove_session();
        } catch (Exception ex) {
            textMsg.setText("\n" + ex.toString());
            //Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();

        }
    }

    public void AutoLogin() throws IOException {
        // File froot = null;
        try {
            // check for SDcard

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);


            File filein = null;
            File fileDir = null;
            String state = Environment.getExternalStorageState();
            // textMsg.setText("SD Card Status - " + state);

            if (Environment.MEDIA_MOUNTED.equals(state)) {

                //check sdcard permission
                fileDir = new File(froot.getAbsolutePath() + "/.server/");
                try{
                    if (!fileDir.exists())
                        fileDir.mkdirs();
                }catch (Exception e) {}

                filein = new File(fileDir, "User.txt");
                if (!filein.exists()){
                    filein.createNewFile();
                }
                if (filein.exists())
                    if (filein.exists()) {
                    try {
                        EditText usr = (EditText) findViewById(R.id.txtUser);
                        EditText pass = (EditText) findViewById(R.id.txtPassword);
                        FileReader fileReader = new FileReader(filein);
                        BufferedReader buf = new BufferedReader(fileReader);
                        String add = buf.readLine();
                        usr.setText("");
                        usr.setText(add);
                        add = buf.readLine();
                        pass.setText("");
                        if (!PinCode_Mode)
                            pass.setText(add);

                        fileReader.close();

                        if (!usr.getText().toString().equals("") && !pass.getText().toString().equals("")) {
                            autologin = true;
                            //   Login_User_With_PinCode();
                            //login_user();
                            //Login_User_With_PinCode_Job_Processing();
                        }
                    } catch (Exception e) {
                        textMsg.setText(e.toString());
                        //Log.e("Exception","error occurred while creating xml file");
                    }

                }
            }

        } catch (Exception e) {
            textMsg.setText(e.toString());
            //              Log.e("Exception","error occurred while creating xml file");
        }

    }

    public void Check_Login_Mode(Context context) throws IOException {

        try {

            //   btnServer = (Button) findViewById(R.id.btnServer);
            // settings = getSharedPreferences(PREFS_NAME, 0);
            boolean FileExists=false;
            //-Checking File
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File filein = null;
            File fileDir = null;
            String state = Environment.getExternalStorageState();
            //  textMsg.setText("No Server found - " + state);
            if (Environment.MEDIA_MOUNTED.equals(state)) {

                //check sdcard permission
                fileDir = new File(froot.getAbsolutePath() + "/.server/");

                filein = new File(fileDir, "serverIp.txt");

                if (filein.exists()) {
                    FileExists=true;
                }
            }



            if (settings.getBoolean("my_first_time", true) && FileExists==false) {
                try{

                    Handler handler3 = new Handler(getMainLooper());
                    handler3.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Create_files();
                            } catch (Exception ex) {
                            }
                        }
                    });
                } catch (Exception e) {
                }

                Toast.makeText(getApplicationContext(), "App is being launched for first time, please set server link and login mode   ", Toast.LENGTH_LONG).show();

                settings.edit().putBoolean("my_first_time", false).commit();
            }else{
                if (settings.getBoolean("my_first_time", true)) {
                    get_Server();
                    AutoLogin();
                }
                settings.edit().putBoolean("my_first_time", false).commit();
            }

            PinCode_Mode = settings.getBoolean("pin_code_mode", false) == true;

            Switching_Mode();

        }catch (Exception e) {
            textMsg.setText(e.toString());
            //Log.e("Exception","error occurred while creating xml file");
        }

    }

    public void check_Login_Mode_status_In_DB() {

        try {
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);


            File filein = null;
            File fileDir = null;
            PinCode_Mode = false;
            String state = Environment.getExternalStorageState();
            textMsg.setText("SD Card Status - " + state);
            if (Environment.MEDIA_MOUNTED.equals(state)) {

                //check sdcard permission
                fileDir = new File(froot.getAbsolutePath() + "/.server/");
                filein = new File(fileDir, "PP.txt");
                if (filein.exists()) {
                    try {
                        FileReader fileReader = new FileReader(filein);
                        BufferedReader buf = new BufferedReader(fileReader);
                        String mode = buf.readLine();
                        //Toast.makeText(this, mode + "Checking from file DB", Toast.LENGTH_LONG).show();
                        // textMsg.setText(mode);
                        PinCode_Mode = mode.equalsIgnoreCase("Pincode");

                        buf.close();
                        fileReader.close();


                    } catch (Exception e) {
                    }

                }
                Switching_Mode();

            }

        } catch (Exception e) {
            textMsg.setText(e.toString());
            //              Log.e("Exception","error occurred while creating xml file");
        }
    }

    public void set_Login_Mode_status_In_DB(String mode) {

        try {
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);


            File filein = null;
            File fileDir = null;
            PinCode_Mode = false;
            String state = Environment.getExternalStorageState();
            textMsg.setText("SD Card Status - " + state);
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
            textMsg.setText(e.toString());
            //              Log.e("Exception","error occurred while creating xml file");
        }
    }

    File createExternalStoragePublicFile(String FileName) {
        // Create a path where we will place our picture in the user's
        // public pictures directory.  Note that you should be careful about
        // what you place here, since the user often manages these files.  For
        // pictures and other media owned by the application, consider
        // Context.getExternalMediaDir().
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File file = new File(path+"./server/",  FileName);

        try {
            // Make sure the Pictures directory exists.
            path.mkdirs();

            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(this,
                    new String[] { file.toString() }, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });
            
        } catch (Exception e) {
            // Unable to create file, likely because external storage is
            // not currently mounted.
            Log.w("ExternalStorage", "Error writing " + file, e);
        }
        
        return file;
    }

    public void Save_User_Info(String User, String Password, String StaffCode, String UserId) {
        try {
            // check for SDcard
            try {

                settings.edit().putString("User", User).commit();
                settings.edit().putString("UserId", UserId).commit();
                settings.edit().putString("OperatorId", User).commit();
                settings.edit().putString("StaffCode", StaffCode).commit();
                settings.edit().putString("Security_Token", Security_Token).commit();
                settings.edit().putBoolean("Update", false).commit();
                settings.edit().putBoolean("AllowMTASaveUserPass", AllowMTASaveUserPass).commit();
                settings.edit().putString("RecipientDocFolder", RecipientDocFolder).commit();
                settings.edit().putString("MobileFutureLimit", MobileFutureLimit).commit();
                settings.edit().putString("TAMode", TAMode).commit();
                settings.edit().putString("TMMode", TMMode).commit();
                settings.edit().putString("AllowPicUpload", AllowPicUpload).commit();

            } catch (Exception ex) {
            }

            try{

                FirebaseDatabase.getInstance().setPersistenceEnabled(true);


                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("server/saving-data/fireblog");
                DatabaseReference usersRef = ref.child("Users");

                Map<String, Users> users = new HashMap<>();
                users.put("Users", new Users(User, "1913", Security_Token));

                usersRef.setValue (users);

            }catch(Exception ex){}

            try {
                froot = (context.getExternalFilesDir(null));
                //froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                //froot=Environment.getExternalStorageDirectory();
                //Tost_Message(froot.getAbsolutePath());
            }catch (Exception ex){
                Tost_Message(ex.toString());
            }
            File filein = null;
            File fileDir = null;
            String state = Environment.getExternalStorageState();
            //check sdcard permission
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                froot.setWritable(true);

                fileDir = new File(froot.getAbsolutePath() + "/.server/");

                if (!fileDir.exists())
                    fileDir.mkdirs();

                filein = new File(fileDir, "User.txt");
                //filein= createExternalStoragePublicFile("User.txt");
                if (filein.exists()) {
                    try {
                        //if (froot.canWrite()) {

                        File file = new File(fileDir, "User.txt");
                        FileWriter filewriter = new FileWriter(file);
                        BufferedWriter out = new BufferedWriter(filewriter);

                        if (PinCode_Mode) {
                            EditText txtPincode = (EditText) findViewById(R.id.txtPassword);

                            out.write(User + "\n" + Password + "\n" + StaffCode + "\n" + TimesheetReights + "\n" + TMMode +
                                    "\n" + TAMode + "\n" + AllowSetTime + "\n" + MobileFutureLimit + "\n" + AllowPicUpload +
                                    "\n" + RecipientDocFolder + "\n" + UserId + "\n" + txtPincode.getText().toString());
                        } else {
                            out.write(User + "\n" + Password + "\n" + StaffCode + "\n" + TimesheetReights + "\n" + TMMode +
                                    "\n" + TAMode + "\n" + AllowSetTime + "\n" + MobileFutureLimit + "\n" + AllowPicUpload +
                                    "\n" + RecipientDocFolder + "\n" + UserId);
                        }
                        out.close();

                    } catch (Exception e) {
                        textMsg.setText(e.toString());
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        //Log.e("Exception","error occurred while creating xml file");
                    }
                } else {
                    froot.setWritable(true);

                    //if (froot.canWrite()) {

                    if (!filein.exists()){
                        filein.createNewFile();
                        if (!filein.canWrite()) {
                            Tost_Message("No permission to write file");
                           //return;

                        }
                    }

                    File file = new File(fileDir, "User.txt");
                    //File file = createExternalStoragePublicFile("User.txt");
                    FileWriter filewriter = new FileWriter(file);
                    BufferedWriter out = new BufferedWriter(filewriter);
                    out.write(User + "\n" + Password + "\n" + StaffCode + "\n" + TimesheetReights + "\n" + TMMode +
                            "\n" + TAMode + "\n" + AllowSetTime + "\n" + MobileFutureLimit + "\n" + AllowPicUpload +
                            "\n" + RecipientDocFolder + "\n" + UserId);


                    out.close();


                }
            }
        } catch (Exception e) {
            textMsg.setText(e.toString());
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            //              Log.e("Exception","error occurred while creating xml file");
        }
    }

    public boolean get_Term_condition() throws IOException {
        // File froot = null;
        try {
            // check for SDcard

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            File filein = null;

            File fileDir = null;
            String state = Environment.getExternalStorageState();
            textMsg.setText("SD Card Status - " + state);
            if (Environment.MEDIA_MOUNTED.equals(state)) {

                //check sdcard permission
                fileDir = new File(froot.getAbsolutePath() + "/.server/");
                filein = new File(fileDir, "term.txt");
                return filein.exists();
            }
        } catch (Exception e) {
            textMsg.setText(e.toString());
            //              Log.e("Exception","error occurred while creating xml file");
        }
        return false;
    }

    public void set_Term_condition(String root) throws IOException {
        // File froot = null;
        try {
            // check for SDcard

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File filein = null;
            File fileDir = null;
            String state = Environment.getExternalStorageState();
            //  textMsg.setText("No Server found - " + state);
            if (Environment.MEDIA_MOUNTED.equals(state)) {

                //check sdcard permission
                fileDir = new File(froot.getAbsolutePath() + "/.server/");

                filein = new File(fileDir, "term.txt");

                if (filein.exists()) {
                    try {
                        froot.setWritable(true);
                        //if (froot.canWrite()) {

                        File file = new File(fileDir, "term.txt");
                        FileWriter filewriter = new FileWriter(file);
                        BufferedWriter out = new BufferedWriter(filewriter);
                        out.write(root);
                        out.close();
                        textMsg.setText("Server has been set successfully");
                    } catch (Exception e) {
                        textMsg.setText(e.toString());
                        //Log.e("Exception","error occurred while creating xml file");
                    }
                } else {
                    froot.setWritable(true);

                    //if (froot.canWrite()) {

                    if (!fileDir.exists())
                        fileDir.mkdirs();

                    File file = new File(fileDir, "term.txt");
                    FileWriter filewriter = new FileWriter(file);
                    BufferedWriter out = new BufferedWriter(filewriter);
                    out.write(root);
                    out.close();
                }
            }
        } catch (Exception e) {
            textMsg.setText(e.toString());
            //              Log.e("Exception","error occurred while creating xml file");
        }

    }

    public boolean login_user_Local(String User, String Password) throws IOException {
        // File froot = null;
        textMsg = (TextView) findViewById(R.id.textMsg);

        try {
            EditText txtPincode = (EditText) findViewById(R.id.txtPassword);

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File filein = null;
            File fileDir = null;
            String state = Environment.getExternalStorageState();
            Intent intent = new Intent(this, MainActivity.class);


            if (Environment.MEDIA_MOUNTED.equals(state)) {

                //check sdcard permission
                fileDir = new File(froot.getAbsolutePath() + "/.server/");
                filein = new File(fileDir, "User.txt");
                if (filein.exists()) {
                    try {
                        FileReader fileReader = new FileReader(filein);
                        BufferedReader buf = new BufferedReader(fileReader);
                        String usr2 = buf.readLine();
                        String pass2 = buf.readLine();
                        try {

                            StaffCode = buf.readLine();

                            TimesheetReights = buf.readLine();
                            TMMode = buf.readLine();
                            TAMode = buf.readLine();
                            AllowSetTime = buf.readLine();
                            MobileFutureLimit = buf.readLine();
                            AllowPicUpload = buf.readLine();
                            RecipientDocFolder = buf.readLine();
                            UserId = buf.readLine();
                            PinCode = buf.readLine();

                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                        }

                        if ((User.equalsIgnoreCase(usr2) && Password.equalsIgnoreCase(pass2)) ||
                                (PinCode_Mode == true && PinCode.equals(txtPincode.getText().toString()))) {
                            OperatorId = User;

                            // final  settings = getSharedPreferences(PREFS_NAME, 0);
                            try {
                                Security_Token = settings.getString("Security_Token", "");
                            } catch (Exception ex) {
                            }

                            Bundle bundle = new Bundle();
                            bundle.putString("User", User);
                            bundle.putString("root", Server_Link);
                            bundle.putString("StaffCode", StaffCode);
                            bundle.putString("Server", "False");
                            bundle.putString("Timesheet", TimesheetReights);
                            bundle.putString("TMMode", TMMode);
                            bundle.putString("TAMode", TAMode);
                            bundle.putString("AllowSetTime", AllowSetTime);
                            bundle.putString("MobileFutureLimit", MobileFutureLimit);
                            bundle.putString("AllowPicUpload", AllowPicUpload);
                            bundle.putString("RecipientDocFolder", RecipientDocFolder);
                            bundle.putString("UserId", UserId);
                            bundle.putString("Security_Token", Security_Token);


                            EditText usr = (EditText) findViewById(R.id.txtUser);
                            EditText pass = (EditText) findViewById(R.id.txtPassword);

                            fileReader.close();

                            //  usr.setText("");
                            // pass.setText("");
                            textMsg.setText("");
                            Tost_Message("Starting App offline");
                            // Toast.makeText(getApplicationContext(), "Starting App offline", Toast.LENGTH_LONG).show();

                            intent.putExtras(bundle);
                            MainActivity.form_resumed=false;

                            startActivity(intent);
                            return true;
                        } else
                            return false;

                    } catch (Exception e) {
                        textMsg.setText(e.toString());
                        //Log.e("Exception","error occurred while creating xml file");
                        return false;
                    }

                }

            }
        } catch (Exception e) {
            textMsg.setText(e.toString());
            //              Log.e("Exception","error occurred while creating xml file");
        }
        return false;
    }

    private ProgressDialog progress;

    boolean UpdateExists() {

        try {
            // check for SDcard


            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File filein = null;
            File fileDir = null;
            String state = Environment.getExternalStorageState();

            // txtServer.setText(state);
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                //check sdcard permission

                fileDir = new File(froot.getAbsolutePath() + "/.server/");
                filein = new File(fileDir, "updates.txt");

                if (filein.exists()) {
                    if (filein.length() > 0)
                        return true;
                }
            }
        } catch (Exception ex) {
        }
        return false;
    }

    public void load_data() {

        if (server_available == false) return;

        //  settings = getSharedPreferences(PREFS_NAME, 0);

        Calendar c = Calendar.getInstance();
        Date dt = c.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

        String curr_Date = sdf.format(dt);
        String toDate = settings.getString("toDate", "2016/03/22");
        String Prev_User = settings.getString("Prev_User", "Testt");

        double spd=0;
        try {

            spt.bindListeners();
            spd = spt.speedInfo.kilobits;

            for(int i=0; i<5;i++ ){
                spd = spt.speedInfo.kilobits;
            }
        }catch(Exception ex){}
        if (!toDate.equals(curr_Date) || !Prev_User.equalsIgnoreCase(OperatorId)) {
            try {
                very_first_time = true;

                // spt.bindListeners();

                //  Tost_Message("Internet Speed "+spt.speedInfo.kilobits + " KB");

                if (spt.speedInfo.kilobits > 300) {
                    // Tost_Message(getRecipientCodes());
                    try {

                        String rosterDate = sdf.format(dt);
                        bulk_data= new Bulk_Data(root,StaffCode,OperatorId,Security_Token,server_available,rosterDate,context);
                        new MyAsyncClass_bulk_data().execute();

                    } catch (Exception ex) {
                    }

                } else {
                    Tost_Message("Complete data will not be  loaded due to low internet speed\nPlease Refresh to load data");
                }
            }catch(Exception ex){}
        }



        settings.edit().putString("toDate", curr_Date).commit();
        settings.edit().putString("Prev_User", OperatorId).commit();

        try {
            new MyAsyncClass().execute(); //load user settings
            //  AsyncTaskTools.execute(new MyAsyncClass());
        } catch (Exception e) {}

        // if(updated) return;



    }

    int jumpTime = 0;
    public void download(View view){
        progress=new ProgressDialog(this);
        progress.setMessage("Please wait while loading data.....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        final int totalProgressTime = 100;
        final Thread t = new Thread() {
            @Override
            public void run() {
                int jumpTime = 0;

                while(jumpTime < totalProgressTime) {
                    try {


                        sleep(1000);
                        jumpTime += 5;
                        progress.setProgress(jumpTime);
                    }
                    catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();


        progress.dismiss();

    }

    String getSecurityToken(){

        String Val= OperatorId  + "$" + Security_Token + "$";
        return Val;
    }

    public String  getRecipientCodes() {

        String client_codes = "";

        try {
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();
            File fileDir = null;
            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = new File(fileDir, "traccs.xml");
            //  rosters=new ArrayList<Roster_Info>();
            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Roster_Info");
                if (nList == null) return "";

                String client_code = "";


                for (int temp = 0; temp < nList.getLength(); temp++) {

                    try {
                        Node nNode = nList.item(temp);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            client_code= eElement.getElementsByTagName("Actual_Client_Code").item(0).getTextContent();

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
            // byte[] byte_Data =null;
            //  char [] key ={'M','T','A','S','A','M','A','D','A','2','0','0','2'};

            //   byte_Data= EncryptData.encodeFile(EncryptData.generateKey(key,xml.getBytes()),xml.getBytes());

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
                fileos.close();


               /*  String key = "MTASamada2002";
                File inputFile =  new File(fileDir,"traccs.xml");
                File encryptedFile = new File("traccs.encrypted");
               // File decryptedFile = new File("traccs.decrypted");

               try {
                    CryptoUtils.encrypt(key, inputFile, encryptedFile);
                  //  CryptoUtils.decrypt(key, encryptedFile, decryptedFile);
                } catch (CryptoException ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }*/

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
            pi.setName("client_code");
            pi.setValue( getSecurityToken() + StaffCode);
            request.addProperty(pi);

            PropertyInfo pi2=new PropertyInfo();
            pi2.setName("username");
            pi2.setValue(OperatorId);
            request.addProperty(pi2);

            PropertyInfo pi3=new PropertyInfo();
            pi3.setName("RecordNo");
            pi3.setValue("0");
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

    public void load_Roster_data_old(){

        textMsg.setText("Load Roster Data");

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
                    textMsg.setText("bb "+ e.toString());
                }
                FileOutputStream fileos = null;

                try{
                    fileos = new FileOutputStream(newxmlfile );
                    fileos.write(xml.getBytes());

                    serializer.setOutput(fileos, "UTF-8");
                }catch (Exception ex){ textMsg.setText("cc: " + ex.toString());}
                finally{   fileos.close();}
            }
        }catch (Exception ex){ textMsg.setText("cc: " + ex.toString());}
        //    textMsg.setText("Done successfully");

    }
    public void load_Receipient_Detail(){
        textMsg.setText("Loading Recepient Data");
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
                    textMsg.setText("bb "+ e.toString());
                }
                FileOutputStream fileos = null;




                try{
                    fileos = new FileOutputStream(newxmlfile );
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                }catch (Exception ex){ textMsg.setText("cc: " + ex.toString());}
                finally{   fileos.close();}
            }
        }catch (Exception ex){ textMsg.setText("cc: " + ex.toString());}

        //    textMsg.setText("Done successfully");

    }
    public void load_Task_Detail(){
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
                    textMsg.setText("bb "+ e.toString());
                }
                FileOutputStream fileos = null;

                try{
                    fileos = new FileOutputStream(newxmlfile );
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                }catch (Exception ex){ textMsg.setText("cc: " + ex.toString());}
                finally{   fileos.close();}
            }
        }catch (Exception ex){ textMsg.setText("cc: " + ex.toString());}
        //    textMsg.setText("Done successfully");

    }
    public void load_Group_Alerts_Detail(){
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
                    textMsg.setText("bb "+ e.toString());
                }
                FileOutputStream fileos = null;

                try{
                    fileos = new FileOutputStream(newxmlfile );
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                }catch (Exception ex){ textMsg.setText("cc: " + ex.toString());}
                finally{   fileos.close();}
            }
        }catch (Exception ex){ textMsg.setText("cc: " + ex.toString());}
        //    textMsg.setText("Done successfully");

    }
    public void  Read_File(String fileName  ) throws IOException{

        List<String> records = new ArrayList<String>();
        try{
            // check for SDcard
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File filein=null;
            File fileDir=null;
            String state = Environment.getExternalStorageState();
            //txtServer.setText(state);
            if (Environment.MEDIA_MOUNTED.equals(state)) {

                //check sdcard permission
                fileDir = new File(froot.getAbsolutePath());
                filein= new File(fileDir, fileName);
                if (filein.exists())
                {
                    try{
                        FileReader fileReader = new FileReader(filein);
                        BufferedReader  buf= new BufferedReader(fileReader);
                        String line="";

                        while ((line = buf.readLine()) != null){
                            records.add(line);
                            textMsg.setText( textMsg.getText().toString() + "\n" + line);

                        }
                    } catch (Exception e) {
                        textMsg.setText(e.toString());
                        //Log.e("Exception","error occurred while creating xml file");
                    }
                }
            }
        } catch (Exception e) {
            textMsg.setText(e.toString());
            //              Log.e("Exception","error occurred while creating xml file");
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
            // textMsg.setText(ex.toString());
            return 0;
        }
        return 5;
    }
    public  String getSha256Hash(String password) {
        try {
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            }

            digest.reset();
            return bin2hex(digest.digest(password.getBytes()));
        } catch (Exception ignored) {
            return null;
        }
    }

    private static String bin2hex(byte[] data) {
        StringBuilder hex = new StringBuilder(data.length * 2);
        for (byte b : data)
            hex.append(String.format("%02x", b & 0xFF));
        return hex.toString();
    }
    public int Check_Web_ServiceVersionNo(Context context)
    {
        try
        {
            String URL5 = root + "/TimeSheet.asmx?op=Web_service_VersionNo";
            String SOAP_ACTION5 =  "https://tempuri.org/Web_service_VersionNo";
            String METHOD_NAME5 = "Web_service_VersionNo";

            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(URL5));
            //startActivity(intent);

           // String text= getSha256Hash("samada2002");
// Instantiate the custom HttpClient

            Handler handler3 = new Handler(getMainLooper());
            handler3.post(new Runnable() {
                @Override
                public void run() {
                    try {

                        String URL = root + "/TimeSheet.asmx?op=Web_service_VersionNo";

                        DefaultHttpClient client = new DefaultHttpClient();
                        HttpGet get = new HttpGet(URL);
// Execute the GET call and obtain the response
                        HttpResponse getResponse = client.execute(get);
                        HttpEntity responseEntity = getResponse.getEntity();

                    } catch (Exception ex) {
                    }
                }
            });
          /*

            SoapObject request = new SoapObject( NAMESPACE,METHOD_NAME5);
            HttpTransportSE androidHttpTransport = new HttpTransportSE( URL5);
            androidHttpTransport.debug =true;

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.bodyOut = request;
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION5, envelope);
            //SoapPrimitive  result =(SoapPrimitive)envelope.getResponse();
            SoapPrimitive  result =(SoapPrimitive)envelope.getResponse();

            Server_Web_ServiceVersionNo=result.toString();*/
            // textMsg.setText(Server_Web_ServiceVersionNo);
            //Tost_Message(Server_Web_ServiceVersionNo);

        }catch(Exception ex){
            Tost_Message("Server is not accessible, please check server settings");
            textMsg.setText(ex.toString());
            return 0;
        }
        return 1;
    }
    public void ShowProgressBar(View v, String command ) {

        // prepare for a progress bar dialog

        progressBar.setCancelable(true);
        progressBar.setMessage("Processing please wait ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        //reset progress bar status
        progressBarStatus = 0;
        prog_command=command;

        new Thread(new Runnable() {
            public void run() {
                while (progressBarStatus < 100) {

                    // process some tasks
                    progressBarStatus = Make_update(prog_command);
                    if (prog_command.equals(""))
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
    public void ShowProgressBar(View v ) {

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
    public void ShowProgressBar_Step(View v, int value  ) {
        // prepare for a progress bar dialog
        if (progressBar!=null) progressBar.dismiss();
        progressBar = new ProgressDialog(v.getContext());
        progressBar.setCancelable(true);
        progressBar.setMessage("Processing please wait ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        //reset progress bar status
        progressBarStatus = value;
        progressBar.setProgress(value);
        new Thread(new Runnable() {
            public void run() {
                while (progressBarStatus < 100) {
                    // process some tasks
                    //your computer is too fast, sleep 1 second
                    try {
                        Thread.sleep(2000);
                        progressBar.setProgress(50);
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
                progressBar.setProgress(90);
                progressBarStatus=100;
                // ok, file is downloaded,
                if (progressBarStatus >= 100) {
                    // sleep 2 seconds, so that you can see the 100%
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // close the progress bar dialog
                    progressBar.dismiss();
                }
            }
        }).start();

    }
    public void ShowDialog2(View v) {
        try{
            String commandText;
            commandText="1.	You will not, nor allow third parties on your behalf to " +
                    "\n\t i. Make and distribute copies of the Application" +
                    "\n\tAttempt to copy, reproduce, alter, modify, reverse engineer," +
                    "\n\tdisassemble, decompile, transfer, exchange or translate the Application." +
                    "\n\tii. Create derivative works of the Application of any kind whatsoever." +
                    "\n2. You acknowledge that the terms of agreement with Adamas  Corporate solution.";

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
            alertDialogBuilder.setTitle("Terms and Condition of Use");

            alertDialogBuilder
                    .setMessage(commandText)
                    .setCancelable(false)
                    .setPositiveButton("Accept",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();

                        }
                    })
                    .setNegativeButton("Reject",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                            finish();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.getWindow().setGravity(10);
            // show it
            alertDialog.show();

        }catch(Exception ex) {}
    }
    public void ShowDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.terms);
        dialog.setTitle("Term and Conditions");
        TextView   Description= (TextView)dialog.findViewById(R.id.txtDescription);
        Description.setText("1.	You will not, nor allow third parties on your behalf to " +
                "\n\t i. Make and distribute copies of the Application" +
                "\n\tAttempt to copy, reproduce, alter, modify, reverse engineer," +
                "\n\tdisassemble, decompile, transfer, exchange or translate the Application." +
                "\n\tii. Create derivative works of the Application of any kind whatsoever." +
                "\n2. You acknowledge that the terms of agreement with Adamas  Corporate solution.");

        Button dialogButton = (Button) dialog.findViewById(R.id.btnAccept);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialog.dismiss();
                try{
                    set_Term_condition("Accept");
                }catch(Exception e){}
            }
        });
        Button btnReject = (Button) dialog.findViewById(R.id.btnReject);
        // if button is clicked, close the custom dialog
        btnReject.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialog.dismiss();
                finish();

            }
        });
        dialog.show();

    }

    void Add_session(){
        Remove_session();
        try{

            if (server_available==false) return;

            EditText usr  = (EditText) findViewById(R.id.txtUser);

            /*try {
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(final String hostname, final SSLSession session) {
                        HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                        return hv.verify(hostname, session);
                    }
                });
            }catch(Exception ex){}
*/
            String urlToHit=root + "/index.aspx?logout=0&user=" + usr.getText().toString();

            Volly_Post_Request(urlToHit);
            /*
            if ( this.httpsClient==null){
                this.httpsClient = new DefaultHttpClient();
                HttpPost httpspost = new HttpPost(urlToHit);
                HttpResponse response = this.httpsClient.execute(httpspost);
            }*/

        } catch(Exception  ex){

        }
    }


    void remove_SSL_Session(){


    }

    void Remove_session(){
        try{
            EditText usr  = (EditText) findViewById(R.id.txtUser);


          //  if (server_available==false) return;

            // Remove_session2();



            try{
                new MyAsyncClass_RemoveSession().execute(usr.getText().toString(),"");
            }catch(Exception ex){}


        } catch(Exception ex){
            Toast.makeText(getApplicationContext(), ex.toString() , Toast.LENGTH_LONG).show();
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    public String getLinkRate()
    {
        int speed=0;
        ConnectivityManager cManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cManager.getActiveNetworkInfo();
        int type = info.getType();
        int subType = info.getSubtype();
       /* if (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI)
        {
            WifiManager wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
            WifiInfo wi = wm.getConnectionInfo();
            // return String.format("%d Mbps", wi.getLinkSpeed());
            Toast.makeText(getApplicationContext(), wi.getLinkSpeed(), Toast.LENGTH_LONG).show();
            speed= wi.getLinkSpeed();
        }else if (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE){

        }*/
        return "Speed " + speed + "MB";

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

    Intent intent;
    String  fileName;
    static Bitmap bitmap; private static final int CAPTURE_REQUEST_CODE = 0;

    private void startCaptureActivity() {

        try{

            intent = new Intent("biz.binarysolutions.signature.CAPTURE");

            String keyFileName    = "biz.binarysolutions.signature.FileName";
            String keyTitle       = "biz.binarysolutions.signature.Title";
            String keyStrokeWidth = "biz.binarysolutions.signature.StrokeWidth";
            String keyCrop        = "biz.binarysolutions.signature.Crop";
            String keyWidth       = "biz.binarysolutions.signature.Width";
            String keyHeight      = "biz.binarysolutions.signature.Height";

            fileName    = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+ "/imdad.png";    // set the file name (global write permissions)
            //String  title       = "Imdad app";    // optional, default is set in AndroidManifest.xml
            int     strokeWidth = 10;    // optional, default is 12
            boolean crop        = false; // optional, default is true

            // allowed units: px, dp, dip, sp, pt, mm, in
            String width  = "600dip"; // optional, default is max
            String height = "300dip"; // same as above

            intent.putExtra(keyFileName, fileName);
            //intent.putExtra(keyTitle, title);
            intent.putExtra(keyStrokeWidth, strokeWidth);
            intent.putExtra(keyCrop, crop);
            intent.putExtra(keyWidth, width);
            intent.putExtra(keyHeight, height);
            // intent.putExtra("Security_Token", Security_Token);
            // intent.putExtra("OperatorId", OperatorId);

            //Log.v("FILE SAVE ",fileName);

            startActivityForResult(intent, CAPTURE_REQUEST_CODE);

            Button done=new Button(this);
            done.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    startActivityForResult(intent, CAPTURE_REQUEST_CODE);

                }
            });
            //  startActivityForResult(intent, CAPTURE_REQUEST_CODE);

            Toast.makeText(getApplicationContext(), "signature saved at " + fileName , Toast.LENGTH_LONG).show();

        } catch(Exception ex){
            Toast.makeText(getApplicationContext(), ex.toString() , Toast.LENGTH_LONG).show();
        }
    }


    public class Signature extends View
    {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private final Paint paint = new Paint();
        private final Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public Signature(Context context, AttributeSet attrs)
        {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }
        Bitmap mBitmap=null;

        public void save(View v)
        {
            Log.v("log_tag", "Width: " + v.getWidth());
            Log.v("log_tag", "Height: " + v.getHeight());
            if(mBitmap == null)
            {
                mBitmap =  Bitmap.createBitmap (mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(mBitmap);
            //  buffer= bitmapToByteArray(mBitmap);
            try
            {


                final FileOutputStream mFileOutStream = new FileOutputStream(mypath);
                picturePath= mypath.getPath();
                v.draw(canvas);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mBitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                    }
                });

                mFileOutStream.flush();
                mFileOutStream.close();
                String url = Images.Media.insertImage(getContentResolver(), mBitmap, "title", null);
                Log.v("log_tag","url: " + url);
                buffer= bitmapToByteArray(BitmapFactory.decodeFile(picturePath));

                //In case you want to delete the file
                //boolean deleted = mypath.delete();
                //Log.v("log_tag","deleted: " + mypath.toString() + deleted);
                //If you want to convert the image to string use base64 converter


            }
            catch(Exception e)
            {
                Log.v("log_tag", e.toString());
            }
        }
        public  byte[] bitmapToByteArray(Bitmap bm) {
            // Create the buffer with the correct size
            final Bitmap bmp =bm;
            int iBytes = bm.getWidth() * bm.getHeight() * 4;
            ByteBuffer buffer = ByteBuffer.allocate(iBytes);

            // Log.e("DBG", buffer.remaining()+""); -- Returns a correct number based on dimensions
            // Copy to buffer and then into byte array
            bm.copyPixelsToBuffer(buffer);
            // Log.e("DBG", buffer.remaining()+""); -- Returns 0
            final ByteArrayOutputStream stream=new ByteArrayOutputStream();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    bmp.compress(Bitmap.CompressFormat.PNG, 90, stream);
                }
            });


            byte[] image=stream.toByteArray();

            return image;


        }
        public void clear()
        {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign.setEnabled(true);

            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++)
                    {
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

        private void debug(String string){
        }

        private void expandDirtyRect(float historicalX, float historicalY)
        {
            if (historicalX < dirtyRect.left)
            {
                dirtyRect.left = historicalX;
            }
            else if (historicalX > dirtyRect.right)
            {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top)
            {
                dirtyRect.top = historicalY;
            }
            else if (historicalY > dirtyRect.bottom)
            {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY)
        {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }

    final Dialog dialog=null;

    public void get_Authenticate_Pin(Context context ){

        try{

            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.pin_staff);
            dialog.setTitle("Please Enter Pin Code");

            mCancel = (Button)dialog.findViewById(R.id.cancel);
            mClear = (Button)dialog.findViewById(R.id.clear);
            mGetSign = (Button)dialog.findViewById(R.id.getsign);
            Button  btnLogoutPin= (Button)dialog.findViewById(R.id.btnLogoutPin);

            // mGetSign.setEnabled(false);
            final  EditText  txtStaff_pin = (EditText)dialog.findViewById(R.id.txtStaff_pin);
            final  EditText  txtPincode = (EditText)findViewById(R.id.txtPassword);
            //   final  TextView  txtPinMsg = (TextView)findViewById(R.id.txtPinMsg);

            btnLogoutPin.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    finish();
                    // mGetSign.setEnabled(false);
                }
            });

            mClear.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Log.v("log_tag", "Panel Cleared");
                    txtStaff_pin.setText("");
                    // mGetSign.setEnabled(false);
                }
            });

            mGetSign.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    try{

                        txtPincode.setText(txtStaff_pin.getText());
                        //dialog.dismiss();
                        PinCode_Mode_login=true;
                        //	login_user();
                        //  Login_User_With_PinCode_Job_Processing();

                        try{
                            new MyAsyncClass_PinCodeLogin_Job_Processing().execute();
                        }catch(Exception ex){}


                    }catch(Exception ex){Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();}
                }
            });

            mCancel.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {

                    dialog.dismiss();

                }

            });

            dialog.show();
            // yourName = (EditText) findViewById(R.id.yourName);
        }catch(Exception ex){Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();}

    }

    void Control_Settings_on_Mode(){


        EditText txtuser = findViewById(R.id.txtUser);
        EditText txtpass = findViewById(R.id.txtPassword);
        Button btnOk= findViewById(R.id.btnOk);
        Button btnSettings= findViewById(R.id.btnSettings);
        txtCompanyLink= findViewById(R.id.txtCompanyLink);
        EditText txtPincode = findViewById(R.id.txtPassword);
        TextView txtPinLabel = findViewById(R.id.txtPinLabel);

        if (PinCode_Mode) {
            //  txtPincode.setVisibility(View.VISIBLE);
            txtPinLabel.setVisibility(View.VISIBLE);
            txtuser.setVisibility(View.GONE);
            txtpass.setVisibility(View.GONE);

        }else{

            txtuser.setVisibility(View.VISIBLE);
            txtpass.setVisibility(View.VISIBLE);
            //   txtPincode.setVisibility(View.INVISIBLE);
            txtPinLabel.setVisibility(View.INVISIBLE);
        }
        btnOk.setVisibility(View.VISIBLE);
        btnSettings.setVisibility(View.VISIBLE);
        txtCompanyLink.setVisibility(View.VISIBLE);
        //  Toast.makeText(getApplicationContext(), "Email send", 100).show();
    }
    public void Set_Login_Mode(Context context ){

        try{

            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.login_mode);
            dialog.setTitle("Please Select Login Mode");

            Button   mCancel = (Button)dialog.findViewById(R.id.cancel);
            Button   normal = (Button)dialog.findViewById(R.id.normal);
            Button   pincode_mode = (Button)dialog.findViewById(R.id.pincode_mode);
            // mGetSign.setEnabled(false);

            //  final  settings = getSharedPreferences(PREFS_NAME, 0);



            // record the fact that the app has been started at least once




            normal.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    settings.edit().putBoolean("pin_code_mode", false).commit();
                    //set_Login_Mode_status_In_DB("Normal");
                    PinCode_Mode=false;
                    Switching_Mode();
                    dialog.dismiss();
                }
            });

            pincode_mode.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    try{

                        settings.edit().putBoolean("pin_code_mode", true).commit();
                        set_Login_Mode_status_In_DB("Pincode");
                        PinCode_Mode=true;
                        Switching_Mode();
                        dialog.dismiss();


                    }catch(Exception ex){Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();}
                }
            });

            mCancel.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {

                    dialog.dismiss();

                }

            });

            dialog.show();
            // yourName = (EditText) findViewById(R.id.yourName);
        }catch(Exception ex){Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();}

    }


    public void doService(){
        if (server_available==false)
        {
            // doService2(view);
            return;
        }
        int prop=0;

        String client_code="";


        try {


            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME7);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL7);
            androidHttpTransport.debug =true;


            PropertyInfo pi=new PropertyInfo();

            pi.setName("Client_code");
            client_code=StaffCode;
            pi.setValue( getSecurityToken() +  client_code);
            request.addProperty(pi);

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

            MobileFutureLimit="10";
            // if (MobileFutureLimit.equals("0") || MobileFutureLimit.equals("") || MobileFutureLimit==null) MobileFutureLimit="10";
            PropertyInfo pi4=new PropertyInfo();
            pi4.setName("MobileFutureLimit");
            pi4.setValue(MobileFutureLimit);
            request.addProperty(pi4);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;

            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION7, envelope);


            SoapObject   result =(SoapObject)envelope.getResponse();

            rosters=new ArrayList<Roster_Info>();


            Roster_Info rst;

            if (result==null){
                Toast.makeText(getApplicationContext(), " No Job Exists for this Date", Toast.LENGTH_LONG).show();
                return;
            }

            if (result.getPropertyCount()<=0){
                Toast.makeText(getApplicationContext(), " No Job exists in this Date", Toast.LENGTH_LONG).show();
                return;
            }

            SoapObject   result2=null;

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


            // start_service();

            //set_first_Item_InGrid();

        } catch (Exception ex) {
            Toast.makeText(mContext, " No rosters fetched due to some error in data on server ", Toast.LENGTH_SHORT).show();

        }
    }


    public void doService2(){

        int indx=0;
        int color=0;
        int j=0;
        int totalblock=0;
        int item_count=0;
        int Daily_Paid_Hours=0;
        int Daily_Paid_KM=0;


        Roster_Info rst=null;
        try {

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();
            File fileDir=null;
            fileDir = new File(froot.getAbsolutePath()+"/.server/");
            File fXmlFile = null;

            fXmlFile= new File(fileDir, "traccs.xml");

            rosters=null;
            rosters=new ArrayList<Roster_Info>();

            if (fXmlFile.exists()){
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Roster_Info");

                // Toast.makeText(getApplicationContext(),"node list = " + nList.getLength() , Toast.LENGTH_LONG).show();

                if (nList==null) return;
                //textMsg.setText("\nCurrent Element :" + nList.item(0).getNodeName());

                String str_date="";
                TextView dtp=(TextView)findViewById(R.id.TextDate);
                String[] sel_date =dtp.getTag().toString().split("/");
                String selected_date= Integer.parseInt( sel_date[2]) + "/" + Integer.parseInt( sel_date[1]) + "/" + sel_date[0];

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date date = new Date(dtp.getTag().toString());
                selected_date = dateFormat.format(date);
                String Roster_Date="";

                for ( int tmp = 0; tmp< nList.getLength(); tmp++) {
                    try{

                        Node  nNode = nList.item(tmp);
                        //  textMsg.setText(nNode.getTextContent());
                        //  textMsg.setText("\nCurrent Element :" + nNode.getNodeName());

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            str_date= eElement.getElementsByTagName("Roster_Date").item(0).getTextContent();
                            String code=eElement.getElementsByTagName("Carer_code").item(0).getTextContent();

                            DateFormat dateFormatN = new SimpleDateFormat("dd/MM/yyyy");
                            DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");

                            Date date2 = new Date(str_date);
                            str_date = dateFormat2.format(date2);

                            Roster_Date=dateFormatN.format(date2);
                            // textMsg.setText(textMsg.getText() + "\n" + str_date + " = " + selected_date + " " +  selection.getText().equals(code));
                            String Roster_Type=eElement.getElementsByTagName("Roster_Type").item(0).getTextContent();
                            String InfoOnly=eElement.getElementsByTagName("InfoOnly").item(0).getTextContent();




                            if ( (Roster_Type.equals("9") || Roster_Type.equals("13") || InfoOnly.equalsIgnoreCase("true") ) ){
                                continue;
                            }


                            if ( (str_date.equals(selected_date)  )   )
                            {
                                rst= new Roster_Info();
                                rst.setRecordNo(eElement.getElementsByTagName("RecordNo").item(0).getTextContent());

                                rst.setBook_date("");

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

                                if (eElement.getElementsByTagName("Notes").item(0).getTextContent()!=null)
                                    rst.setNotes(eElement.getElementsByTagName("Notes").item(0).getTextContent());
                                else
                                    rst.setNotes("-");

                                rst.setRoster_type(eElement.getElementsByTagName("Roster_Type").item(0).getTextContent());
                                rst.setStarted(eElement.getElementsByTagName("Started").item(0).getTextContent());

                                if (eElement.getElementsByTagName("Completed").item(0).getTextContent()!=null)
                                    rst.setCompleted(eElement.getElementsByTagName("Completed").item(0).getTextContent());
                                else
                                    rst.setCompleted("0");

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
                                    rst.setFee(eElement.getElementsByTagName("Fee").item(0).getTextContent());
                                }catch(Exception ex){}

                                try{
                                    rst.setDisplayFeeInApp(eElement.getElementsByTagName("DisplayFeeInApp").item(0).getTextContent());
                                }catch(Exception ex){}

                                try{
                                    rst.setDisplayDebtorInApp(eElement.getElementsByTagName("DisplayDebtorInApp").item(0).getTextContent());
                                }catch(Exception ex){}

                                try{
                                    Daily_Paid_Hours=Daily_Paid_Hours + Integer.parseInt(rst.getDuration());
                                    Daily_Paid_KM=Daily_Paid_KM+Integer.parseInt(rst.getKM());
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
                            }
                            else{
                                continue;
                            }
                        }else { textMsg.setText("doService2 - File does not exist"); return;}
                    }  catch (Exception aE) {
                        // textMsg.setText("Error in doService2 " +  aE.toString());
                    }

                    item_count=item_count+1;
                    rosters.add(rst);
                }




            } else { }//textMsg.setText("Xml file not found"); }
        } catch (Exception aE) {

        }
    }

    Roster_Info get_current_Item()
    {
        int indx=0;
        if (rosters==null){
            TextView txtPinMsg= (TextView)findViewById(R.id.txtPassword);
            Toast.makeText(getApplicationContext(),"No Job exists to start "  , Toast.LENGTH_LONG).show();
            txtPinMsg.setText("No Job exists to start ");
        }
        int total_item= rosters.size();
        Roster_Info rst=null;
        Current_roster=null;
        while(indx<total_item)
        {
            rst=rosters.get(indx);
            //Integer.parseInt(rst.getStarted())<=0


            if (Integer.parseInt(rst.getCompleted())<=0  && Check_valid_Duration(rst.getStart_Time(), rst.get_End_Time())==true   )
            {

                Current_roster=rst;
                if (Integer.parseInt(rst.getStarted())>0){
                    start_Job=false;
                }

                break;
            }
            indx=indx+1;

        }
        return Current_roster;
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


            // Toast.makeText(getApplicationContext(),"Current Time : " + curr.getTime() , Toast.LENGTH_LONG).show();


		 /* long diffSeconds = diff / 1000 % 60;
		  long diffMinutes = diff / (60 * 1000) % 60;
		  long diffMinutes2 = diff2 / (60 * 1000) % 60;
		  long durationMinutes = diff2 / (60 * 1000) % 60;
		  */

            if (curr.getTime()>=start.getTime() && curr.getTime()<=end.getTime())
            {
                sts=true;
                // Toast.makeText(getApplicationContext(),"Condition True\nCurrent " + date1.getTime() + "\n Start=" + start.getTime() + "\n end=" + end.getTime() , Toast.LENGTH_LONG).show();

            }
            //  Toast.makeText(getApplicationContext(),"Current " + date1.getTime() + "\n Start=" + start.getTime() + "\n end=" + end.getTime() , Toast.LENGTH_LONG).show();
		  	 /*   // long diffHours = diff / (60 * 60 * 1000);

		 // Toast.makeText(getApplicationContext(),"diff " + diff + " duration=" + duration + " end=" + end , Toast.LENGTH_LONG).show();
		  if (date1.after(start) && date1.before(end))
		   sts=true;

		if ( diffMinutes<=durationMinutes && diffMinutes>=0   ){
			  sts=true;
		  }

		  if ( diffMinutes2<=5 && diffMinutes2>=0){
			  sts=true;
		  }

	      if ( date1.compareTo(start)>=0 && date1.compareTo(end)<0)
			 sts=true;
		*/

        }catch (Exception ex){
            // Toast.makeText(getApplicationContext(),ex.toString() , Toast.LENGTH_LONG).show();
        }

        return sts;
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


            if ( date1.compareTo(start)>=0 && date1.compareTo(end)<=0)
                sts=true;
            // else
            //  Alarm_RosterNo=0;

        }catch (Exception ex){
            // Toast.makeText(getApplicationContext(),ex.toString() , Toast.LENGTH_LONG).show();
        }

        return sts;
    }
    void Check_End_of_shift()
    {
        int indx=0;
        Roster_Info rst;

        while(indx<rosters.size())
        {
            rst=rosters.get(indx);
            //Integer.parseInt(rst.getStarted())<=0

            if (Integer.parseInt(rst.getStarted())>0 && Integer.parseInt(rst.getCompleted())<=0
                    && Check_End_Duration(rst.getStart_Time(), rst.get_End_Time())==true   )
            {
                String msg ="Time of Current Shift " + rst.getStart_Time()+" - " + rst.get_End_Time() + " has been ended, you have to end job";

  		       /*  if (Enable_Shift_End_Alarm.equalsIgnoreCase("1") || Enable_Shift_End_Alarm.equalsIgnoreCase("true"))
	        	   if (Alarm_RosterNo==0){
	        		   Alarm_RosterNo=1;
	        		   show_alarm(msg);
	        	*/
            }
            break;
        }
        indx=indx+1;

    }

    //====================================================================================================
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

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION2, envelope);

            SoapPrimitive  result =(SoapPrimitive)envelope.getResponse();

            Date date= new Date();
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
            String currentDateString = dateFormat2.format(date);
            if (Boolean.valueOf(result.toString())==true){


                try{
                    xml.Update_Roster_Node(RecordNo,"Started","0");
                    //	   Toast.makeText(getApplicationContext(),"Job stopped successfully", Toast.LENGTH_LONG).show();

                }catch (Exception ex){}

            }
            else
                Toast.makeText(getApplicationContext(),"Operation not done due to some server error", Toast.LENGTH_LONG).show();

        }catch(Exception ex){

            Toast.makeText(getApplicationContext(),"Operation not done due to some server error", Toast.LENGTH_LONG).show();
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
            }catch (Exception ex){}
            ((TextView) findViewById(R.id.textMsg)).setText("Job stopped successfully");




        }catch(Exception ex){
            // ((TextView) findViewById(R.id.textMsg)).setText("Operation not done due to some server error \n" + ex.toString());
            Toast.makeText(getApplicationContext(),"Operation not done due to some server error", Toast.LENGTH_LONG).show();
        }
    }

    String getDeviceName (){

        String D_Name= Settings.Global.getString(this.getContentResolver(), "device_name");

        return D_Name;

    }
    public void set_Updates(String command ) throws IOException {
        // File froot = null;
        try {
            // check for SDcard
            ((TextView) findViewById(R.id.textMsg)).setText("making update");
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
                        textMsg.setText(e.toString());
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

            ((TextView) findViewById(R.id.textMsg)).setText("Operation done successfully");
        }
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
            @SuppressLint("InvalidWakeLockTag") WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "MyWakelockTag");
            wakeLock.acquire();


        }catch(Exception ex){}
    }


    class MyAsyncClass extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog==null)
                pDialog = new LoadingDialog(Login.this,false);
            pDialog.setMessage("Please wait while loading user settings ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                Load_User_Settings();

            }

            catch (Exception ex) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();

            if (MTAAutRefreshOnLogin.equalsIgnoreCase("1") || MTAAutRefreshOnLogin.equalsIgnoreCase("true") || very_first_time) {
                try {
                    new MyAsyncClass2().execute();
                } catch (Exception ex) {
                }
            }else {
                load_main_form();
            }

            //  Toast.makeText(getApplicationContext(), "Email send", 100).show();
        }
    }
    class MyAsyncClass2 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Login.this,true);
            pDialog.setMessage("Please wait while loading data ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {

                load_Roster_data();
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

            load_main_form();


            //  Toast.makeText(getApplicationContext(), "Email send", 100).show();
        }
    }
    class MyAsyncClass3 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Login.this,true);
            pDialog.setMessage("Please wait while loading app ....");
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... mApi) {
            if (root.equals("")){
                Tost_Message("Please set server settings correctly");
                return null;
            }
            try
            {
                String URL5 = root + "/TimeSheet.asmx?op=Web_service_VersionNo";
                String SOAP_ACTION5 =  "https://tempuri.org/Web_service_VersionNo";
                String METHOD_NAME5 = "Web_service_VersionNo";

                SoapObject request = new SoapObject( NAMESPACE,METHOD_NAME5);
                HttpTransportSE androidHttpTransport = new HttpTransportSE( URL5);
                androidHttpTransport.debug =true;

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.bodyOut = request;
                envelope.dotNet=true;
                envelope.setOutputSoapObject(request);

                // Make the soap call.
                androidHttpTransport.call(SOAP_ACTION5, envelope);
                //SoapPrimitive  result =(SoapPrimitive)envelope.getResponse();
                SoapPrimitive  result =(SoapPrimitive)envelope.getResponse();

                Server_Web_ServiceVersionNo=result.toString();


            }catch(Exception ex){


            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (!App_Web_ServiceVersionNo.equals(Server_Web_ServiceVersionNo))
            {
                // textMsg.setText("App is not compatible with Web service" );
                //  Control_Settings_on_Mode();
                //  TextView lblVersion=  ((TextView) findViewById(R.id.lblVersionNo));
                // lblVersion.setText(lblVersion.getText()+ ", Web Service No : App=" + App_Web_ServiceVersionNo + ", Server=" + Server_Web_ServiceVersionNo );
            }

        }

    }
    class MyAsyncClass_DeviceToken extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try
            {
                /*

//irfan       This code temporary commented at 28/09/2025 because firebase not working need to fix this
            //    fbUser = FirebaseAuth.getInstance().getCurrentUser();
                //DeviceToken= FirebaseInstanceId.getInstance().getToken("947962252237", "FCM");
//irfan      This code temporary commented at 28/09/2025 because firebase not working need to fix this

                */
                DeviceToken = "947962252237";
                String URL5 = root + "/TimeSheet.asmx?op=Register_Device";
                String SOAP_ACTION5 =  "https://tempuri.org/Register_Device";
                String METHOD_NAME5 = "Register_Device";

                SoapObject request = new SoapObject( NAMESPACE,METHOD_NAME5);
                HttpTransportSE androidHttpTransport = new HttpTransportSE( URL5);
                androidHttpTransport.debug =true;

                PropertyInfo pi=new PropertyInfo();
                pi.setName("StaffCode");
                pi.setValue(getSecurityToken () + StaffCode);
                request.addProperty(pi);

                PropertyInfo pi2=new PropertyInfo();
                pi2.setName("DeviceToken");
                pi2.setValue(DeviceToken);
                request.addProperty(pi2);

                PropertyInfo pi3=new PropertyInfo();
                pi3.setName("DeviceName");
                pi3.setValue(getDeviceName ());
                request.addProperty(pi3);

                PropertyInfo pi4=new PropertyInfo();
                pi4.setName("DeviceType");
                pi4.setValue("NewAppAndroid");
                request.addProperty(pi4);


                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.bodyOut = request;
                envelope.dotNet=true;
                envelope.setOutputSoapObject(request);

                // Make the soap call.
                androidHttpTransport.call(SOAP_ACTION5, envelope);
                //SoapPrimitive  result =(SoapPrimitive)envelope.getResponse();
                SoapPrimitive  result =(SoapPrimitive)envelope.getResponse();

                 //Server_Web_ServiceVersionNo=result.toString();


            }catch(Exception ex){
               //Tost_Message("DeviceToken " + ex.toString());

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
           // Tost_Message("Server_Web_ServiceVersionNo " + Server_Web_ServiceVersionNo);
        }
    }
    private  class MyAsyncClass_login extends AsyncTask<String , String, String> {

        LoadingDialog pDialog;
        SoapObject  obj=null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new  LoadingDialog(Login.this,false);

            // pDialog = new  LoadingDialog(Login.this,false);
            pDialog.setMessage("Please wait while authenticating ....");
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {


            String val="Fail";
            try {

                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME4);

            /*
             Code for ILA to check SSL
             TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                } };
                SSLContext sc = null;
                try {
                    sc = SSLContext.getInstance("SSL");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                try {
                    sc.init(null, trustAllCerts, new java.security.SecureRandom());
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                // Create all-trusting host name verifier
                HostnameVerifier allHostsValid = new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                };
                // Install the all-trusting host verifier
                HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
*/

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL4 );

                androidHttpTransport.debug =true;


                PropertyInfo pi3=new PropertyInfo();
                pi3.setName("User");
                String message = params[0];
                pi3.setValue(message);
                pi3.setType(PropertyInfo.STRING_CLASS);
                request.addProperty(pi3);

                PropertyInfo pi4=new PropertyInfo();
                pi4.setName("Password");
                pi3.setType(PropertyInfo.STRING_CLASS);
                message = params[1];
                pi4.setValue(message);
                request.addProperty(pi4);


                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.bodyOut = request;
                envelope.dotNet=true;
                envelope.setOutputSoapObject(request);



                // Make the soap call.
                androidHttpTransport.call(SOAP_ACTION4, envelope);

                // if(1==1) return "true";
                obj=(SoapObject)envelope.getResponse();
                // obj = (SoapObject) envelope.bodyIn;

                if (obj==null) {
                    Object object = getResponse(envelope.bodyIn);

                    if (object != null)
                        obj = (SoapObject) envelope.bodyIn;
                }
                // textMsg.setText( obj.getName() + " " + obj.getPropertyCount());
                SoapPrimitive  result=null;
                if (obj.getPropertyCount()>0)
                    result =(SoapPrimitive) obj.getProperty("Login");


                if (result==null)
                    val= "Fail";
                else {
                    val = "Valid";
                    loginAttempt++;
                }
            }        catch (Exception ex) {
                //Tost_Message("Server is not accessible, please check server settings");
                val=ex.toString();
                val="Invalid Server";
                //finish();
            }

            return val;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.cancel();
            if (result.equalsIgnoreCase("Invalid Server"))
                Tost_Message("Server is not accessible, please check server settings");
            else if (result.equals("Fail")) {
                textMsg.setText("Invalid User");
               // Tost_Message("Invalid User Information");
                //  Toast.makeText(getApplicationContext(), "Invalid User " + result, Toast.LENGTH_LONG).show();
            }else {
                 Prepare_Main_Screen_After_Login(obj);
            }
        }
    }

    private  class MyAsyncClass_start extends AsyncTask<Void , Void, Void> {

        LoadingDialog pDialog;
        SoapObject  obj=null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            pDialog = new LoadingDialog(Login.this,true);
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

            if (start_Job){
                // Set_Job(Current_roster.getRecordNo());

                msg= "Your Job has been started successfull\n"
                        + " From " + Current_roster.getStart_Time() + " to " + Current_roster.get_End_Time();
            }else{
                //  End_Job(Current_roster.getRecordNo());

                msg= "Your Job has been stopped successfully \n"
                        + " From " + Current_roster.getStart_Time() + " to " + Current_roster.get_End_Time();

            }

            Tost_Message(msg);
        }
    }

    void Create_files(){
        File fileDir=null;

        try{
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath()+"/.server/");

            try{
                if (!fileDir.exists())
                    fileDir.mkdirs();
            }catch (Exception e) {}
        }catch(Exception e){}

        File newxmlfile = new File(fileDir,"traccs.xml");
        File newxmlfile2 = new File(fileDir,"Recipient.xml");
        File newxmlfile3 = new File(fileDir,"task.xml");
        File newxmlfile4 = new File(fileDir,"group_alerts.xml");
        File newxmlfile5 = new File(fileDir,"User_Settings.xml");
        File newxmlfile6 = new File(fileDir,"Incident_Locations.xml");

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            try{
                newxmlfile.createNewFile();
            }catch(IOException e){}

            try{
                newxmlfile2.createNewFile();
            }catch(IOException e){}

            try{
                newxmlfile3.createNewFile();
            }catch(IOException e){}
            try{
                newxmlfile4.createNewFile();
            }catch(IOException e){}
            try{
                newxmlfile5.createNewFile();
            }catch(IOException e){}

            try{
                newxmlfile6.createNewFile();
            }catch(IOException e){}

            try{
                File file = new File(fileDir, "Updates.txt");
                FileWriter filewriter = new FileWriter(file,true);
                BufferedWriter out = new BufferedWriter(filewriter);
                out.close();
            }catch(IOException e){}


        }
    }
    boolean Load_User_Settings(){

        boolean flag=false;
        String URL5 = root  + "/TimeSheet.asmx?op=getUser_Settings";
        String SOAP_ACTION5 =  "https://tempuri.org/getUser_Settings";
        String METHOD_NAME5 = "getUser_Settings";



        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME5);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
        androidHttpTransport.debug =true;


        try{

            PropertyInfo pi2=new PropertyInfo();
            pi2.setName("UserName");
            pi2.setValue( getSecurityToken() + OperatorId);
            request.addProperty(pi2);


        }catch (Exception e) { textMsg.setText("ff: " + e.toString());}

        try{
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            // envelope.encodingStyle = SoapEnvelope.ENC;
            envelope.setOutputSoapObject(request);
            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION5, envelope);


            String xml= androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir=null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath()+"/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir,"User_Settings.xml");
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
                    flag=true;
                }catch (Exception ex){ }
                finally{   fileos.close();}
            }

        }catch (Exception e) {
            // Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
            textMsg.setText("cc: " + e.toString());
        }


        return flag;
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
            Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
        }
    }
    public void load_paid_hours(){

        String URL6 = root  + "/TimeSheet.asmx?op=get_Paid_Hours";
        String SOAP_ACTION6 =  "https://tempuri.org/get_Paid_Hours";
        String METHOD_NAME6 = "get_Paid_Hours";


        String format="yyyy/MM/dd";

        //SimpleDateFormat    sdf = new SimpleDateFormat(format, Locale.getDefault());
        SimpleDateFormat    sdf = new SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        Date dt=c.getTime();

        String curr_Date =sdf.format(dt);


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
    class MyAsyncClassUpload extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Login.this,false);
            pDialog.setMessage("Please wait while updating server updates  ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                // Add subject, Body, your mail Id, and receiver mail Id.
                // sender.sendMail("Subject", " body", "from Mail", "to mail");
                Upload_Updates up= new Upload_Updates(root,OperatorId,Security_Token, context);
                up.Upload_Updates_on_server();

            }catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            pDialog.cancel();

            try {
                load_data();
            } catch (Exception ex) {}
            //  Toast.makeText(getApplicationContext(), "Email send", 100).show();
        }
    }

    public void get_Email_Settings_from_server( )
    {


        String SOAP_ACTION55 =  "https://tempuri.org/GetEmailSettings";
        String METHOD_NAME55 = "GetEmailSettings";
        String URL55 = root + "/TimeSheet.asmx?op=GetEmailSettings";

        if (server_available==false)
        {
            return;
        }

        try
        {
            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME55);
            HttpTransportSE androidHttpTransport2 = new HttpTransportSE(URL55);
            androidHttpTransport2.debug =true;

            PropertyInfo pi2=new PropertyInfo();
            pi2.setName("Fontra");
            pi2.setValue(getSecurityToken() + "99");
            request.addProperty(pi2);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

            SoapObject  result=null;
            SoapPrimitive obj;
            // Make the soap call.
            androidHttpTransport2.call(SOAP_ACTION55, envelope);
            result= (SoapObject) envelope.getResponse();


            email_seting = new Email_Settings();

            for(int i=0 ; i<result.getPropertyCount(); i++)
            {
                try{

                    obj=(SoapPrimitive)result.getProperty("POP3Server");
                    email_seting.setPOP3Server(obj.toString());

                    obj=(SoapPrimitive)result.getProperty("POP3User");
                    email_seting.setPOP3User(obj.toString());

                    obj=(SoapPrimitive)result.getProperty("POP3Password");
                    email_seting.setPOP3Password(obj.toString());

                    obj=(SoapPrimitive)result.getProperty("SMTPServer");
                    email_seting.setSMTPServer(obj.toString());

                    obj=(SoapPrimitive)result.getProperty("SMTPUser");
                    email_seting.setSMTPUser(obj.toString());

                    obj=(SoapPrimitive)result.getProperty("SMTPPassword");
                    email_seting.setSMTPPassword(obj.toString());

                    obj=(SoapPrimitive)result.getProperty("FromEmail");
                    email_seting.setFromEmail(obj.toString());

                    obj=(SoapPrimitive)result.getProperty("FromDisplayName");
                    email_seting.setFromDisplayName( " TRACCS Client Note Added for : " );

                    obj=(SoapPrimitive)result.getProperty("SMTP_Port");
                    email_seting.setSMTP_Port(obj.toString());


                }catch(Exception ex){}

            }
        }catch(Exception ex){
            Toast.makeText(getApplicationContext(), "Operation not done due to some server error\n" + ex.toString(), Toast.LENGTH_LONG).show();

        }

        froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File filein=null;
        File fileDir=null;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {

            //check sdcard permission
            fileDir = new File(froot.getAbsolutePath()+"/.server/");
            if (!fileDir.exists())
                fileDir.mkdirs();

            filein= new File(fileDir, "server_setting.txt");

            try{
                if (!filein.exists()){

                    filein= new File(filein, "server_setting.txt");
                }
            }catch (Exception e) {}

            if (filein.exists()){


                try{


                /*	 FileReader fileReader = new FileReader(filein);
                     BufferedReader buf = new BufferedReader(fileReader);
                     String add = buf.readLine();
*/


                    FileWriter filewriter = new FileWriter(filein);
                    BufferedWriter out = new BufferedWriter(filewriter);


                    out.write( email_seting.getSMTPServer() + "\n");

                    out.write(email_seting.getSMTPUser()+ "\n");

                    out.write(email_seting.getSMTPPassword()+ "\n");

                    out.write(email_seting.getSMTP_Port()+ "\n");

                    out.write(email_seting.getFromEmail()+ "\n");

                    out.write(email_seting.getFromDisplayName()+ "\n");


                    out.close();


                }catch(Exception ex){}

            }

        }
                /*  try{
    	    settings = getSharedPreferences(PREFS_NAME, 0);
    	   if (email_seting.getSMTPServer()==null || email_seting.getSMTPServer().equals("")){

    		   email_seting.setSMTPServer(settings.getString("SMTP_Server","mail.adamas.net.au"));
    		   email_seting.setSMTP_Port(settings.getString("SMTP_Port","567"));
    		   email_seting.setSMTPUser(settings.getString("SMTP_User","timwatts@adamas.net.au"));
    		   email_seting.setSMTPPassword(settings.getString("SMTP_Password","samada2002"));
    		 //  email_seting.setFromDisplayName(settings.getString("Email_Subject",  " TRACCS Client Note Added for : " + AccountNo));
    		   email_seting.setFromEmail(settings.getString("From_Address","support@adamas.net.au"));


    	   }
       }catch(Exception ex){}


        settings = getSharedPreferences(PREFS_NAME, 0);
       settings.edit().putString("SMTP_Server", email_seting.getSMTPServer()).commit();
       settings.edit().putString("SMTP_Port", email_seting.getSMTP_Port()).commit();
       settings.edit().putString("SMTP_User", email_seting.getSMTPUser()).commit();
       settings.edit().putString("SMTP_Password", email_seting.getSMTPPassword()).commit();
       settings.edit().putString("From_Address", email_seting.getFromEmail()).commit();
       settings.edit().putString("Email_Subject", email_seting.getFromDisplayName()).commit();
       */
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

    class MyAsyncClass4_bandwidth extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Login.this);
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


                  /*  long startTime = System.currentTimeMillis();
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



            //Toast.makeText(getApplicationContext(), "bandwidth="+bandwidth +"\n" + ErroString, Toast.LENGTH_LONG).show();
        }
    }

    void TestWebService() {
        String result = "";
        String url = "http://10.0.2.2:8080/WebService/ServiceServlet";
        // String  url ="http://maps.googleapis.com/maps/api/directions/json?origin=Lahore%20Pakistan&destination=Sargodha%20Pakistan&sensor=false";

        TextView txtAddress2 = (TextView) findViewById(R.id.txtAddress);
        //  txtAddress2.setText(url);

        String[] tag = {"text"};
        JSONObject jsonObj = null;
        //HttpResponse response = null;
        try {

            /*HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
            response = httpClient.execute(httpPost, localContext);
            StringBuilder sb;
            try {
                InputStream resp_body = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resp_body, "iso-8859-1"), 8);
                sb = new StringBuilder();
                sb.append(reader.readLine() + "\n");
                String line = "0";
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                resp_body.close();
                String rrr = sb.toString();
               Tost_Message(rrr);

            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }*/
        }catch(Exception ex){  Tost_Message(ex.toString());}
    }
    class MyAsyncClass_RemoveSession extends AsyncTask<String, String, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            pDialog = new LoadingDialog(Login.this);
//            pDialog.setMessage("Logging Off User ....");
//            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... mApi) {



            String usr = mApi[0];
            String urlString = root + "/index.aspx?logout=1&user=" + usr;
            String data = "";

            try {
              // Volly_Post_Request(urlString);
               // new Waiter(Long.parseLong(UserSessionLimit)*60000, Login.this,root, usr).run();


            } catch (Exception ex) {
                Tost_Message(ex.toString());
            }

            return null;

        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            pDialog.cancel();
//            finish();
//            moveTaskToBack(true);
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(1);
            //Toast.makeText(getApplicationContext(), "bandwidth="+bandwidth +"\n" + ErroString, Toast.LENGTH_LONG).show();
        }
    }

    class MyAsyncClass_check extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Login.this);
            pDialog.setMessage("checking Web service ....");
            // pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {


            try {
                TestWebService();

            } catch (Exception ex) {//Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }

            return null;

        }
        @Override
        protected void onPostExecute(Void result) {

            //Toast.makeText(getApplicationContext(), "bandwidth="+bandwidth +"\n" + ErroString, Toast.LENGTH_LONG).show();
        }
    }
    class MyAsyncClass_PinCodeLogin_Job_Processing extends AsyncTask<Void, Void, String> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Login.this);
            pDialog.setMessage("Logging User ....");

            pDialog.show();

        }

        @Override
        protected String doInBackground(Void... mApi) {


            String res="0";
            try {

                res= Login_User_With_PinCode_Job_Processing();
                doService();
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

                // Tost_Message("Pin code authenticated, Login successfully");
                //  Toast.makeText(getApplicationContext(), "Login successfully", Toast.LENGTH_LONG).show();
                // Prepare_Main_Screen_After_Login(obj);
                Get_Job_Processing();

            }else{
                //  Toast.makeText(getApplicationContext(), "No User found with this Pin Code, Invalid User", Toast.LENGTH_LONG).show();
                //textMsg.setText("No User found with this Pin Code, Invalid User");
                Tost_Message("No User found with this Pin Code, Invalid User");
                return;
            }
        }
    }
    class MyAsyncClass_bulk_data extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // pDialog = new LoadingDialog(Login.this);
            //  pDialog.setMessage("Please wait while loading data from server  ....");


            pDialog = new  LoadingDialog(Login.this);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {

                bulk_data.load_Receipient_Detail(MobileFutureLimit);
                bulk_data.load_Transport_Detail();
                bulk_data.getProgramRecipients(MobileFutureLimit);
                bulk_data.load_Shift_Goals();
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

                new MyAsyncClass_bulk_data2().execute();
            } catch(Exception ex){}

        }
    }
    class MyAsyncClass_bulk_data2 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Login.this);
            pDialog.setMessage("Please wait while loading data from server  ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {


                //bulk_data.get_OP_Case_Notes();
                bulk_data.load_Group_Alerts_Detail();
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
            pDialog.cancel();
            try{

                new Login.MyAsyncClass_bulk_data3().execute();
            } catch(Exception ex){}


        }
    }
    class MyAsyncClass_bulk_data3 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Login.this);
            pDialog.setMessage("Please wait while loading data from server  ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                bulk_data.load_paid_hours();
                bulk_data.load_LeaveTypes();
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
            pDialog.cancel();
            try{

                new MyAsyncClass_bulk_data4().execute();
            } catch(Exception ex){}


        }
    }
    class MyAsyncClass_bulk_data4 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Login.this);
            pDialog.setMessage("Please wait while loading data from server  ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                bulk_data.load_Service_Wise_Shift_Goals();

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
            pDialog.cancel();
            try{

                //new MyAsyncClass_bulk_data4().execute();
            } catch(Exception ex){}


        }
    }

    class MyAsyncClass_LoginUser_With_PinCode extends AsyncTask<Void, Void, String> {

        LoadingDialog pDialog;
        SoapObject  obj2=null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Login.this);
            pDialog.setMessage("Logging User with Pin Code....");

            pDialog.show();

        }

        @Override
        protected String doInBackground(Void... mApi) {


            String res="False";
            try {

                obj2= Login_User_With_PinCode();
                res="True";
            } catch (Exception ex) {//Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }

            return res;

        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();

            if (obj2!=null ){
                if (obj2.getPropertyCount()>6)
                    Prepare_Main_Screen_After_Login(obj2);
                else {
                    loginAttempt++;
                    Tost_Message("No User found with this Pin Code, Invalid User");
                }
            }else{
                loginAttempt++;
                Tost_Message( "No User found with this Pin Code, Invalid User");
                textMsg.setText("No User found with this Pin Code, Invalid User");
            }

        }
    }

    class MyAsyncClass_LoginUser_With_MS extends AsyncTask<String, String, String> {

        LoadingDialog pDialog;
        SoapObject  obj2=null;
        String email="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Login.this);
            pDialog.setMessage("Logging User with Microsoft Account....");

            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {


            String res="False";
             email = params[0];
            try {

                obj2= Login_User_With_MS(email);
                if (obj2!=null)
                    res="True";
            } catch (Exception ex) {//Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }

            return res;

        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();

            if (obj2!=null ){
                if (obj2.getPropertyCount()>6)
                    Prepare_Main_Screen_After_Login(obj2);
                else {
                    loginAttempt++;
                    Tost_Message("No User found with MS Account " + email + ", Invalid User");
                }
            }else{
                loginAttempt++;
                Tost_Message( "No User found with  MS Account " + email + ", Invalid User");
               // textMsg.setText("No User found with MS Account, Invalid User");
            }

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

    void delay(int second){
        final Context context=getApplicationContext();
        final long min=(second*360000);
        for(long i=0; i<min; i++)
            i=i;
        Handler handler =  new Handler(context.getMainLooper());
        handler.post( new Runnable(){
            public void run(){
                //  for(long i=0; i<min; i++)
                //  i=i;
            }
        });
    }


    void Volly_Post_Request(String url_String){

        try {


            new Thread(() -> {
                try {
                    URL url = new URL(url_String);
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

        }catch(Exception ex){Tost_Message(ex.toString());}
    }

}//main activity class

