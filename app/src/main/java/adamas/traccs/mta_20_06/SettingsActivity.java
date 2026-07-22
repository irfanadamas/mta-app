package adamas.traccs.mta_20_06;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.provider.ContactsContract;

import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatActivity {

    TextView txtServerLabel;
    TextView txtServer;
    TextView txtServerLink_Label;
    TextView txtServerLink;
    TextView txtLoginMode_Label;
    TextView txtLoginMode;
    TextView txtOnlineStatus_Label;
    TextView txtOnlineStatus;
    TextView txtSMTP_Label;
    TextView txtSMTP;
    TextView txtDataRefresh_label;
    TextView txtDatarefresh;
    TextView txtLogout;
    TextView txtCompanyLink;
    TextView txtCompanyLink2;
    TextView txtMTAGuide;
    TextView txtMTAGuide_link;
    ImageView imgfoward_set_server;
    ImageView imgfoward_server_link;
    ImageView imgfoward_Change_Login_mode;
    ImageView imgfoward_SMTP;
    ImageView imgfoward_Data_refresh;
    ImageView imgGuide;
    ImageView imgCompanyLink;
    Context context;
    Timer timer;
    String root = "";
    String user = "";
    final String PREFS_NAME = "MTAPrefs";
    SharedPreferences settings = null;
    File froot;
    Boolean PinCode_Mode;
    Bulk_Data bulk_data;
    boolean Server_Available = true;
    SpeedTest spt;
    int speed_limit = 512;
    String StaffCode = "";
    String Security_Token="";
    String OperatorId="";
    ProgressDialog pDialog_refresh;
    String MobileFutureLimit="10";
    String CompanyLink="";
    String CompanyText="Our Site";
    String App_Web_ServiceVersionNo="";
    String Server_Web_ServiceVersionNo="";
    @Override
    protected void onResume() {

        super.onResume();
        make_settings();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);


        View root = findViewById(R.id.scroll); // your top-level container
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return WindowInsetsCompat.CONSUMED; // stop it propagating further and double-applying
        });


        setupActionBar();

        settings = getSharedPreferences(PREFS_NAME, 0);
        context=this.getApplicationContext();
        txtServerLabel = findViewById(R.id.txtServerLabel);
        txtServerLabel.setOnTouchListener(onTouchListener);
        txtServer = findViewById(R.id.txtServer);
        txtServer.setOnTouchListener(onTouchListener);

        txtServerLink_Label = findViewById(R.id.txtServerLink_Label);
        txtServerLink_Label.setOnTouchListener(onTouchListener);
        txtServerLink = findViewById(R.id.txtServerLink);
        txtServerLink.setOnTouchListener(onTouchListener);

        txtLoginMode_Label = findViewById(R.id.txtLoginMode_Label);
        txtLoginMode_Label.setOnTouchListener(onTouchListener);
        txtLoginMode = findViewById(R.id.txtLoginMode);
        txtLoginMode.setOnTouchListener(onTouchListener);

        txtOnlineStatus_Label = findViewById(R.id.txtOnlineStatus_Label);
        txtOnlineStatus_Label.setOnTouchListener(onTouchListener);

        txtOnlineStatus = findViewById(R.id.txtOnlineStatus);
        txtOnlineStatus.setOnTouchListener(onTouchListener);

        txtSMTP_Label = findViewById(R.id.txtSMTP_Label);
        txtSMTP_Label.setOnTouchListener(onTouchListener);

        txtSMTP = findViewById(R.id.txtSMTP);
        txtSMTP.setOnTouchListener(onTouchListener);


        txtDatarefresh = findViewById(R.id.txtData_refresh);
        txtDatarefresh.setOnTouchListener(onTouchListener);

        txtDataRefresh_label = findViewById(R.id.txtDataRefresh_Label);
        txtDataRefresh_label.setOnTouchListener(onTouchListener);

        txtLogout = findViewById(R.id.txtLogout);
        txtLogout.setOnTouchListener(onTouchListener);

        txtCompanyLink= findViewById(R.id.txtCompanyLink);
        txtCompanyLink.setOnTouchListener(onTouchListener);


        txtCompanyLink2= findViewById(R.id.txtCompanyLink2);
        txtCompanyLink2.setOnTouchListener(onTouchListener);

        txtMTAGuide= findViewById(R.id.txtMTAGuide);
        txtMTAGuide.setOnTouchListener(onTouchListener);

        txtMTAGuide_link= findViewById(R.id.txtMTAGuide_link);
        txtMTAGuide_link.setOnTouchListener(onTouchListener);

        imgfoward_set_server =findViewById(R.id.imgfoward_set_server);
        imgfoward_server_link=findViewById(R.id.imgfoward_server_link);
        imgfoward_Change_Login_mode=findViewById(R.id.imgfoward_Change_Login_mode);
        imgfoward_SMTP=findViewById(R.id.imgfoward_SMTP);
        imgfoward_Data_refresh=findViewById(R.id.imgfoward_Data_refresh);
        imgGuide=findViewById(R.id.imgGuide);
        imgCompanyLink=findViewById(R.id.imgCompanyLink);

        imgfoward_set_server.setOnTouchListener(onTouchListener);
        imgfoward_server_link.setOnTouchListener(onTouchListener);
        imgfoward_Change_Login_mode.setOnTouchListener(onTouchListener);
        imgfoward_SMTP.setOnTouchListener(onTouchListener);
        imgfoward_Data_refresh.setOnTouchListener(onTouchListener);
        imgGuide.setOnTouchListener(onTouchListener);
        imgCompanyLink.setOnTouchListener(onTouchListener);
    }


    void make_settings() {
      try {
          root = settings.getString("root","");
          user = getIntent().getExtras().getString("user");
          StaffCode = getIntent().getExtras().getString("StaffCode");
          Security_Token = getIntent().getExtras().getString("Security_Token");
          OperatorId = getIntent().getExtras().getString("user");
          MobileFutureLimit = getIntent().getExtras().getString("MobileFutureLimit");
          CompanyText = settings.getString("AgencyPortalText","Our Site");
          CompanyLink = settings.getString("AgencyPortal","");
          txtCompanyLink.setText(CompanyText);
          txtCompanyLink2.setText(CompanyLink);

          App_Web_ServiceVersionNo=  getIntent().getExtras().getString("App_Web_ServiceVersionNo","");
          Server_Web_ServiceVersionNo= getIntent().getExtras().getString("Server_Web_ServiceVersionNo","");

          TextView txtWebServiceVersion= findViewById(R.id.txtWebServiceVersion);
          TextView txtServerVersion= findViewById(R.id.txtServerVersion);

          txtWebServiceVersion.setText("Version No. " + App_Web_ServiceVersionNo);
          txtServerVersion.setText("Version No. " +Server_Web_ServiceVersionNo);


          TextView txtServerVersion_error= findViewById(R.id.txtServerVersion_error);
           // Tost_Message(App_Web_ServiceVersionNo + "\n" + Server_Web_ServiceVersionNo);

          if (!App_Web_ServiceVersionNo.equalsIgnoreCase(Server_Web_ServiceVersionNo)){
              txtServerVersion_error.setVisibility(View.VISIBLE);
          }else{
              txtServerVersion_error.setVisibility(View.GONE);
          }



      }catch(Exception ex){}
         txtServer = findViewById(R.id.txtServer);
         txtServerLink = findViewById(R.id.txtServerLink);
        txtServer.setText("No Server Setting");

        if (!root.equals("")) {
            txtServer.setText("Server Set");
            txtServerLink.setText(root);
            settings.edit().putString("root", root).commit();
        }

        TextView txtLoginMode = findViewById(R.id.txtLoginMode);
        final TextView txtOnlineStatus = findViewById(R.id.txtOnlineStatus);
        if (settings.getBoolean("pin_code_mode", false)) {
            txtLoginMode.setText("Pin Code Mode Set");
        } else {
            txtLoginMode.setText("Manual Mode Set");
        }

        if (isOnline(getApplicationContext())) {
            txtOnlineStatus.setText("Online");
            Server_Available = true;
        } else {
            txtOnlineStatus.setText("Offline");
            Server_Available = false;
        }
        TextView txtDataRefresh_Label = findViewById(R.id.txtDataRefresh_Label);
        TextView txtData_refresh = findViewById(R.id.txtData_refresh);
        //Tost_Message(settings.getString("Data_Refreshed","10:00"));
        txtData_refresh.setText("Data refreshed at " + settings.getString("Data_Refreshed","10:00"));

        if (!StaffCode.equals("") && !Security_Token.equals("") && Server_Available) {
            txtData_refresh.setEnabled(true);
            txtDataRefresh_Label.setEnabled(true);
        } else {
            txtData_refresh.setEnabled(false);
            txtDataRefresh_Label.setEnabled(false);


        }

        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                runOnUiThread(new Runnable() {


                    public void run() {


                        if (isOnline(getApplicationContext())) {
                            txtOnlineStatus.setText("Online");
                            Server_Available = true;
                        } else {
                            txtOnlineStatus.setText("Offline");
                            Server_Available = false;
                        }


                    }
                });

            }
        }, 1000, 1000);


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
            Server_Available = connected;
            return connected;


        } catch (Exception e) {
            ///System.out.println("CheckConnectivity Exception: " + e.getMessage());
            // Log.v("connectivity", e.toString());

        }
        return connected;
    }

    void smtp_setting() {
        Intent intnt = new Intent(SettingsActivity.this, SMTP_Settings.class);
        startActivity(intnt);
    }

    void set_server() {
        // Toast.makeText(SettingsActivity.this, "Server", Toast.LENGTH_SHORT).show();
        Intent intnt = new Intent(SettingsActivity.this, Set_Server.class);
        startActivity(intnt);
    }
void call_link(){

    String url = txtServerLink.getText().toString();
    Intent i = new Intent(SettingsActivity.this,WebService.class);
    i.putExtra("web_link",url);
    //Intent i = new Intent(Intent.ACTION_VIEW);
    //i.setData(Uri.parse(url));
    startActivity(i);
}
    void load_MTA_Guide(){


        Intent i = new Intent(SettingsActivity.this,MTA_Guide.class);

        startActivity(i);
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
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
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

    class MyAsyncClass_RemoveSession extends AsyncTask<String, String, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SettingsActivity.this);
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
            finish();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            //Toast.makeText(getApplicationContext(), "bandwidth="+bandwidth +"\n" + ErroString, Toast.LENGTH_LONG).show();
        }
    }

    void Volly_Post_Request(String url_String) {

        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = url_String; //"http://www.google.com";

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
            Tost_Message(ex.toString());
        }
    }

    public void Set_Login_Mode(Context context) {

        Intent intt = new Intent(SettingsActivity.this, LoginMode.class);
        startActivity(intt);

    }


    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {


            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    //Here if you want to know from which touch this event has accured you can do following code
                    switch (v.getId()) {
                        case R.id.txtServerLabel:
                            set_server();
                            break;
                        case R.id.txtServer:
                            set_server();
                            break;
                        case R.id.imgfoward_set_server:
                            set_server();
                            break;
                        case R.id.txtServerLink_Label:
                            call_link();
                            break;
                        case R.id.txtServerLink:
                            call_link();
                            break;
                        case R.id.imgfoward_server_link:
                            call_link();
                            break;
                        case R.id.txtSMTP:
                            smtp_setting();
                            break;
                        case R.id.txtSMTP_Label:
                            smtp_setting();
                            break;
                        case R.id.imgfoward_SMTP:
                            smtp_setting();
                            break;
                        case R.id.txtLoginMode_Label:
                            Set_Login_Mode(SettingsActivity.this);
                            break;
                        case R.id.txtLoginMode:
                            Set_Login_Mode(SettingsActivity.this);
                            break;
                        case R.id.imgfoward_Change_Login_mode:
                            Set_Login_Mode(SettingsActivity.this);
                            break;
                        case R.id.txtDataRefresh_Label:
                            Load_Data();
                            break;
                        case R.id.txtData_refresh:
                            Load_Data();
                            break;
                        case R.id.imgfoward_Data_refresh:
                            Load_Data();
                            break;
                        case R.id.txtLogout:
                            try {
                                new MyAsyncClass_RemoveSession().execute(user);
                            } catch (Exception ex) {
                            }
                            break;
                        case R.id.txtCompanyLink:
                            try {
                                Company_Link();
                            } catch (Exception ex) {
                            }
                            break;
                        case R.id.txtCompanyLink2:
                            try {
                                Company_Link();
                            } catch (Exception ex) {
                            }
                            break;
                        case R.id.imgCompanyLink:
                            try {
                                Company_Link();
                            } catch (Exception ex) {
                            }
                            break;
                        case R.id.txtMTAGuide:
                            try {
                                load_MTA_Guide();
                            } catch (Exception ex) {
                            }
                            break;
                        case R.id.txtMTAGuide_link:
                            try {
                                load_MTA_Guide();
                            } catch (Exception ex) {
                            }
                            break;

                        case R.id.imgGuide:
                            try {
                                load_MTA_Guide();
                            } catch (Exception ex) {
                            }
                            break;
                    }


                    break;
                case MotionEvent.ACTION_UP:
                    // Action you you want on finger up


                    switch (v.getId()) {
                        case R.id.textView1:
                            break;

                    }


                    break;
            }

            return true;
        }
    };
public void Company_Link()
{
  // CompanyLink= settings.getString("CompanyLink","");
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(CompanyLink));
    startActivity(browserIntent);
}
    public void Load_Data() {

        if (Server_Available == false) {
            Toast.makeText(getApplicationContext(), "Internet is not available to refresh data", Toast.LENGTH_LONG).show();

            return;
        }
        try {
            spt = new SpeedTest(root);
            spt.bindListeners();
            double spd = spt.speedInfo.kilobits;
            spd = spt.speedInfo.kilobits;
            for (int i = 0; i < 5; i++) {
                spd = spt.speedInfo.kilobits;
            }
            if (spt.speedInfo == null) {
                Tost_Message("Tap again to refresh");


                return;
            }

        } catch (Exception ex) {
            Tost_Message(ex.toString());
        }
        double speed = 0;
        try {
            spt.bindListeners();
            speed = spt.speedInfo.kilobits;
            if (speed < speed_limit) {
              //  Toast.makeText(getApplicationContext(), "Internet is slow, refresh may not be completed \n please retry in a few minutes if data is not fully refreshed", Toast.LENGTH_LONG).show();
                // Tost_Message("Internet  Speed = " + speed + " KB");
                // return;
            }
        } catch (Exception ex) {
            //  Tost_Message(ex.toString());
            Tost_Message("Re-Try to refresh");
        }

        // Tost_Message("Internet  Speed = " + speed + " KB");
        try {
            // new MyAsyncClass3().execute();
        } catch (Exception ex) {
        }


        try {
            String format = "yyyy/MM/dd";
            String format2 = "MMM dd, yyyy hh:mm:ss a"; //"dd/MM/yyyy hh:mm";
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            SimpleDateFormat sdf2 = new SimpleDateFormat(format2, Locale.getDefault());
            Calendar c = Calendar.getInstance();
            Date dt = c.getTime();
            String rosterDate = sdf.format(dt);

            String str_time=sdf2.format(dt.getTime());
            settings.edit().putString("Data_Refreshed",str_time).commit();
            TextView txtData_refresh= findViewById(R.id.txtData_refresh);
            txtData_refresh.setText("Data refreshed at " + settings.getString("Data_Refreshed","10:00"));


            //  Toast.makeText(getApplicationContext(), "Loading data from server for " + StaffCode, Toast.LENGTH_LONG).show();

            try {

                bulk_data = new Bulk_Data(root, StaffCode, OperatorId, Security_Token, Server_Available, rosterDate,context);
                new SettingsActivity.MyAsyncClass_bulk_data().execute();


            } catch (Exception ex) {
            }


        } catch (Exception ex) {}


    }
    class MyAsyncClass_bulk_data extends AsyncTask<Void, Void, Void> {

       
        //ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog_refresh==null)
                pDialog_refresh = new ProgressDialog(SettingsActivity.this);
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

                new SettingsActivity.MyAsyncClass_bulk_data3().execute();
            } catch(Exception ex){}

        }
    }
    class MyAsyncClass_bulk_data3 extends AsyncTask<Void, Void, Void> {

        //ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //pDialog = new ProgressDialog(SettingsActivity.this);
//            pDialog.setMessage("Please wait while loading Transport Detail  ....");
//            pDialog.show();
            if (pDialog_refresh==null)
                pDialog_refresh = new ProgressDialog(SettingsActivity.this);
            pDialog_refresh.setMessage("Please wait while loading Transport Detail  ....");

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

                new SettingsActivity.MyAsyncClass_bulk_data4().execute();
            } catch(Exception ex){}


        }
    }
    class MyAsyncClass_bulk_data4 extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            pDialog = new ProgressDialog(SettingsActivity.this);
//            pDialog.setMessage("Please wait while loading Shift Goals  ....");
//            pDialog.show();

            if (pDialog_refresh==null)
                pDialog_refresh = new ProgressDialog(SettingsActivity.this);
            pDialog_refresh.setMessage("Please wait while loading Shift Goals  ....");
        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {

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
            //pDialog.cancel();
            try{

                new SettingsActivity.MyAsyncClass_bulk_data5().execute();
            } catch(Exception ex){}


        }
    }
    class MyAsyncClass_bulk_data5 extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            pDialog = new ProgressDialog(SettingsActivity.this);
//            pDialog.setMessage("Please wait while loading Alerts Detail  ....");
//            pDialog.show();
            if (pDialog_refresh==null)
                pDialog_refresh = new ProgressDialog(SettingsActivity.this);

            pDialog_refresh.setMessage("Please wait while loading Alerts Detail  ....");
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

                new SettingsActivity.MyAsyncClass_bulk_data6().execute();
            } catch(Exception ex){}


        }
    }
    class MyAsyncClass_bulk_data6 extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//
//            pDialog = new ProgressDialog(SettingsActivity.this);
//            pDialog.setMessage("Please wait while loading Incident Detail  ....");
//            pDialog.show();
            if (pDialog_refresh==null)
                pDialog_refresh = new ProgressDialog(SettingsActivity.this);

            pDialog_refresh.setMessage("Please wait while loading Incident Detail  ....");

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

                new SettingsActivity.MyAsyncClass_bulk_data7().execute();
            } catch(Exception ex){}


        }
    }
    class MyAsyncClass_bulk_data7 extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            pDialog = new ProgressDialog(SettingsActivity.this);
//            pDialog.setMessage("Please wait while loading paid hours rosters  ....");
//            pDialog.show();
            if (pDialog_refresh==null)
                pDialog_refresh = new ProgressDialog(SettingsActivity.this);

            pDialog_refresh.setMessage("Please wait while loading paid hours rosters ....");
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

                new SettingsActivity.MyAsyncClass_bulk_data8().execute();
            } catch(Exception ex){}


        }
    }
    class MyAsyncClass_bulk_data8 extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            pDialog = new ProgressDialog(SettingsActivity.this);
//            pDialog.setMessage("Please wait while loading Leave types  ....");
//            pDialog.show();
            if (pDialog_refresh==null)
                pDialog_refresh = new ProgressDialog(SettingsActivity.this);

            pDialog_refresh.setMessage("Please wait while loading Leave types ....");

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

                new SettingsActivity.MyAsyncClass_bulk_data9().execute();
            } catch(Exception ex){}


        }
    }
    class MyAsyncClass_bulk_data9 extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            pDialog = new ProgressDialog(SettingsActivity.this);
//            pDialog.setMessage("Please wait while loading Task Detail  ....");
//            pDialog.show();
            if (pDialog_refresh==null)
                pDialog_refresh = new ProgressDialog(SettingsActivity.this);

            pDialog_refresh.setMessage("Please wait while loading Task Detail ....");
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

                new SettingsActivity.MyAsyncClass_bulk_data10().execute();
            } catch(Exception ex){}


        }
    }
    class MyAsyncClass_bulk_data10 extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//
//            pDialog = new ProgressDialog(SettingsActivity.this);
//            pDialog.setMessage("Please wait while loading Roster Recipient  ....");
//            pDialog.show();
            if (pDialog_refresh==null)
                pDialog_refresh = new ProgressDialog(SettingsActivity.this);

            pDialog_refresh.setMessage("Please wait while loading Roster Recipient ....");
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
            pDialog_refresh.cancel();
            try{
                //Call for loading Roster Data
                new SettingsActivity.MyAsyncClass3().execute();
            } catch(Exception ex){}


        }
    }
    class MyAsyncClass_load_Op_Case_Note extends AsyncTask<String, Void, Void> {

       // ProgressDialog pDialog;
        String[] parms;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           // pDialog_refresh = new ProgressDialog(SettingsActivity.this);
            pDialog_refresh.setMessage("Please wait while loading Op/Case Nots from server  ....");
            pDialog_refresh.show();

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
            pDialog_refresh.cancel();


        }
    }
    class MyAsyncClass3 extends AsyncTask<Void, Void, Void> {

       // ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // pDialog_refresh = new ProgressDialog(SettingsActivity.this);
            pDialog_refresh.setMessage("Please wait while loading roster data from server  ....");
            pDialog_refresh.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {

                 bulk_data.load_Roster_data();

            }

            catch (Exception ex) {

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            pDialog_refresh.cancel();




            //  Toast.makeText(getApplicationContext(), "Email send", 100).show();
        }
    }

}


