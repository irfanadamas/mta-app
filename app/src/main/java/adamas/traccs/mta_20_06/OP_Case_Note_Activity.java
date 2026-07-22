package adamas.traccs.mta_20_06;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
import org.xmlpull.v1.XmlSerializer;


import android.os.Environment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Xml;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class OP_Case_Note_Activity  extends AppCompatActivity {

    private final String NAMESPACE = "https://tempuri.org/";

    String root="";

    RecyclerView listView;
    ArrayList<OP_Case_Note> lst_notes=null;
    OP_Case_Note note=null;
    boolean flagDecoration=true;
    String Recipient="";
    String OperatorId;
    String Security_Token;
    String EnableViewNoteCases="00000";
    String AccountNo="";
    String Personid="";
    String Job_Time="";
    boolean Server_Available=false;
    String StaffCode="";
    String Cordinator_Email="";
    String RecordNo="";
    String Roster_Date;
    File froot = null;
    Context context;
    public static String Main_Op_Note="";
    String AllowRosterNoteEntry="false";
    String AllowCaseNoteEntry="false",AllowOPNoteEntry="false",AllowClinicalNoteEntry="false";
    int reqCode=100;
    TextView txtFilter;
    Bulk_Data bulk_data;

    boolean ViewAll=false;
    private  String UseOPNoteAsShiftReport="0";
    private String UseServiceNoteAsShiftReport="0";
    final String PREFS_NAME = "MTAPrefs";
    SharedPreferences settings = null;
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

    static boolean Refresh_OP_Note_data=false;
    static boolean Refresh_OP_Note_data_single=false;
    protected boolean shouldAskPermissions() {
        try{
            return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
        }catch (Exception ex){}

        return false;
    }


    protected void askPermissions()  {
        int requestCode = 200;
        try {
            requestPermissions(permissions, requestCode);
        }catch (Exception ex){}
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
               // actionBar.setDisplayHomeAsUpEnabled(true);
                ImageView imageMenu=(ImageView) actionBar.getCustomView().findViewById(R.id.imageMenu);
                imageMenu.setVisibility(View.INVISIBLE);
                ImageView imageBack=(ImageView) actionBar.getCustomView().findViewById(R.id.imageBack);
             //   imageBack.setVisibility(View.GONE);
                imageBack.setVisibility(View.GONE);
                actionBar.setDisplayHomeAsUpEnabled(true);
                imageBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

            } catch (Exception ex) { }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
      //  if(1==1) return false;

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
        try{
            //  new MyAsyncClass4().execute(Main_Op_Note);
            //it is set true on each login and Multi-Attendee screen

            //irfan
         /*   if (!Main_Op_Note.equals("")) {
                DisplayNote(Main_Op_Note);
                if (Refresh_OP_Note_data_single && Server_Available) {
                    new MyAsyncClass_load_Op_Case_Note().execute();
                    //  Refresh_OP_Note_data=true;
                    // Refresh_OP_Note_data_single=false;
                } else if (Refresh_OP_Note_data && Server_Available) {
                    settings.edit().putBoolean("Refresh_OP_Note_data",false).commit();
                    Refresh_OP_Note_data = false;
                    new MyAsyncClass_load_Op_Case_Note().execute();
                } else
                    get_Op_Case_Notes(Main_Op_Note);
            }*/

            if (!Main_Op_Note.equals("")) {
                if (Refresh_OP_Note_data && Server_Available) {
                    DisplayNote(Main_Op_Note);
                    Refresh_OP_Note_data = false;
                    new MyAsyncClass_load_Op_Case_Note().execute();
                }
            }

        }catch (Exception ex) {}
    }
    @SuppressLint("GestureBackNavigation")
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            menu.clear();
        }
        return false;
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.op_case_note);
        setupActionBar();
        context=this.getApplicationContext();
        flagDecoration=true;
        set_server_Ip();
        listView = (RecyclerView) findViewById(R.id.listViewOP);
        TextView txtLabel  = (TextView) findViewById(R.id.txtLabel);
        txtLabel.setText(AccountNo);
        Button btnAddNote = (Button) findViewById(R.id.btnAddNote);
          txtFilter  = (TextView) findViewById(R.id.txtFilter);
          Button  btnFilter=(Button)findViewById(R.id.btnFilter);

        if(Personid.length()<=0 || Personid.equals("0")){
            btnAddNote.setVisibility(View.GONE);
        }

        if (Integer.parseInt( EnableViewNoteCases.substring(0,1))==1){
            if (Main_Op_Note.equals("")) DisplayNote(Main_Op_Note); Main_Op_Note="OPNOTE";
        }else if (Integer.parseInt( EnableViewNoteCases.substring(1,2))==1){
            if (Main_Op_Note.equals("")) DisplayNote(Main_Op_Note); Main_Op_Note="CASENOTE";

        } else if (Integer.parseInt( EnableViewNoteCases.substring(2,3))==1){
            if (Main_Op_Note.equals("")) DisplayNote(Main_Op_Note); Main_Op_Note="SVCNOTE";

        }else if (Integer.parseInt( EnableViewNoteCases.substring(3,4))==1){
            if (Main_Op_Note.equals("")) DisplayNote(Main_Op_Note); Main_Op_Note="CLINNOTE";

        }else{
            if (Main_Op_Note.equals("")) DisplayNote(Main_Op_Note); Main_Op_Note="OPNOTE";
        }

        if ( (AllowOPNoteEntry.equals("0")|| AllowOPNoteEntry.equalsIgnoreCase("false"))
            &&  (AllowCaseNoteEntry.equals("0")|| AllowCaseNoteEntry.equalsIgnoreCase("false"))
            &&  (AllowClinicalNoteEntry.equals("0")|| AllowClinicalNoteEntry.equalsIgnoreCase("false"))
            &&  (AllowRosterNoteEntry.equals("0")|| AllowRosterNoteEntry.equalsIgnoreCase("false"))
            &&    (Integer.parseInt( EnableViewNoteCases.substring(0, 1))==0 || Integer.parseInt( EnableViewNoteCases.substring(1, 2))==0
                || Integer.parseInt( EnableViewNoteCases.substring(2, 3))==0 || Integer.parseInt( EnableViewNoteCases.substring(3, 4))==0 || Integer.parseInt( EnableViewNoteCases.substring(4, 5))==0)) {
                Tost_Message("Blocked from notes see your TRACCS administrator");
                Main_Op_Note="";

        }


        //&&  (AllowRosterNoteEntry.equals("0")|| AllowRosterNoteEntry.equalsIgnoreCase("false"))

        if ( (AllowOPNoteEntry.equals("0")|| AllowOPNoteEntry.equalsIgnoreCase("false"))
                &&  (AllowCaseNoteEntry.equals("0")|| AllowCaseNoteEntry.equalsIgnoreCase("false"))
                &&  (AllowClinicalNoteEntry.equals("0")|| AllowClinicalNoteEntry.equalsIgnoreCase("false"))
                )
                 {

                btnAddNote.setVisibility(View.INVISIBLE);

        }

      //  Refresh_OP_Note_data=settings.getBoolean("Refresh_OP_Note_data",false);

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (Main_Op_Note.equalsIgnoreCase ("OPNOTE") && (AllowOPNoteEntry.equals("0")|| AllowOPNoteEntry.equalsIgnoreCase("false")))
                    Toast.makeText(OP_Case_Note_Activity.this, "No Permission to add OP Note", Toast.LENGTH_SHORT).show();
                else if (Main_Op_Note.equalsIgnoreCase ("CASENOTE") &&  (AllowCaseNoteEntry.equals("0")|| AllowCaseNoteEntry.equalsIgnoreCase("false")))
                    Toast.makeText(OP_Case_Note_Activity.this, "No Permission to add Case Note", Toast.LENGTH_SHORT).show();
                else if (Main_Op_Note.equalsIgnoreCase ("CLINNOTE") &&  (AllowClinicalNoteEntry.equals("0")|| AllowClinicalNoteEntry.equalsIgnoreCase("false")))
                    Toast.makeText(OP_Case_Note_Activity.this, "No Permission to add Clinical Note " + AllowClinicalNoteEntry, Toast.LENGTH_SHORT).show();
                else*/

                    Set_Client_Note(view);
            }
        });




        if (   (Integer.parseInt( EnableViewNoteCases.substring(0, 1))==0 && Integer.parseInt( EnableViewNoteCases.substring(1, 2))==0
                && Integer.parseInt( EnableViewNoteCases.substring(2, 3))==0 && Integer.parseInt( EnableViewNoteCases.substring(4, 5))==0)) {

           // Tost_Message("No Permissions");
            //return;
            btnFilter.setVisibility(View.INVISIBLE);

        }
        btnFilter.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try{

                    Intent it= new Intent(v.getContext(), Note_Filter.class);
                    it.putExtra("Main_Op_Note",Main_Op_Note);
                    startActivityForResult(it,reqCode);



                } catch(Exception ex){ }
                finally{ }
            }
        });

        Refresh_OP_Note_data                                            =  false;
        if (!Main_Op_Note.equals("")) {
            DisplayNote(Main_Op_Note);
            new MyAsyncClass_load_Op_Case_Note().execute();

        }
    }


    void DisplayNote(String noteType){
        ViewAll=false;
        if (noteType.equalsIgnoreCase("OPNOTE")) txtFilter.setText("OP Note");
        else if (noteType.equalsIgnoreCase("CASENOTE")) txtFilter.setText("Case Note");
        else if (noteType.equalsIgnoreCase("CLINNOTE")) txtFilter.setText("Clinical Note");
        else if (noteType.equalsIgnoreCase("SVCNOTE")) txtFilter.setText("Svc Notes/Shift Reports");
        else if (noteType.equalsIgnoreCase("VIEW ALL")){
            txtFilter.setText("View All");
            ViewAll=true;
        }


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

      // if (resultCode==reqCode){

        //   Main_Op_Note= data.getStringExtra("Note_Type");

            DisplayNote(Main_Op_Note);
            get_Op_Case_Notes(Main_Op_Note);

       // }

        }

    void set_server_Ip()
    {

        try{
            Bundle bundle = getIntent().getExtras();
            settings = getSharedPreferences(PREFS_NAME, 0);

            try{
                this.RecordNo= bundle.getString("RecordNo");
                root=settings.getString("root","");
                Recipient=bundle.get("Recipient").toString();
                Security_Token=settings.getString("Security_Token","");
                OperatorId=settings.getString("OperatorId","");
                this.AccountNo= bundle.getString("AccountNo");
                this.Roster_Date= bundle.getString("Roster_Date");
                Personid= bundle.getString("PersonId","0");
                Job_Time= bundle.getString("Job_Time","-");
            }catch(Exception ex){}

            try{
                Cordinator_Email=settings.getString("Staff_Coordinator_Email","-");
                EnableViewNoteCases=settings.getString("EnableViewNoteCases","00000");
                StaffCode=settings.getString("StaffCode","ABC");
                this.Server_Available= settings.getBoolean("Server_Available",false);
                Cordinator_Email= settings.getString("Coordinator_Email","");
                if (Personid==null)
                    Personid= settings.getString("PersonId","0");
                if (Personid.equalsIgnoreCase("0"))
                    Personid= settings.getString("PersonId","0");


            }catch(Exception ex){}


            try{


                AllowRosterNoteEntry= settings.getString("AllowRosterNoteEntry","0");
                AllowCaseNoteEntry= settings.getString("AllowCaseNote","0");
                AllowOPNoteEntry= settings.getString("AllowOPNote","0");
                AllowClinicalNoteEntry= settings.getString("AllowClinicalNoteEntry","0");
                UseOPNoteAsShiftReport= settings.getString("UseOPNoteAsShiftReport","0");
                UseServiceNoteAsShiftReport= settings.getString("UseServiceNoteAsShiftReport","0");

            }catch(Exception ex){}


           if (Personid.equalsIgnoreCase("0"))
               Personid= get_Recipient(Recipient);

            if (Cordinator_Email.equalsIgnoreCase("") || Cordinator_Email.equalsIgnoreCase("-"))
             getRoster_Recipient2(Recipient);



        }catch(Exception ex){}
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
    public void Set_Client_Note(View view) {

        Intent in= new Intent(OP_Case_Note_Activity.this,Add_Note_Activity.class);

        Bundle b= new Bundle();

            b.putString("RecordNo",RecordNo);
            b.putString("Recipient",Recipient);
            b.putString("PersonId", Personid);
            b.putString("AccountNo", AccountNo);
            b.putString("Roster_Date", Roster_Date);
            b.putString("Main_Op_Note", Main_Op_Note);
            b.putString("Job_Time", Job_Time);
            b.putString("Enforce_Note", "false");

            in.putExtras(b);
            startActivity(in);


    }

    public void Update_client_Note(String Note, String Note_Type) {

         String URL4 = root + "/TimeSheet.asmx?op=Add_client_Note";
         String SOAP_ACTION4 = "https://tempuri.org/Add_client_Note";
         final String METHOD_NAME4 = "Add_client_Note";
        String S_PersonId ="";

       // TextView txtmsg = ((TextView) findViewById(R.id.textMsg));
       // txtmsg.setVisibility(View.VISIBLE);
        // txtmsg.setText("Calling add note  " + URL4);

        if (Server_Available == false) {
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
            pi.setValue(Security_Token + values);
            request.addProperty(pi);


            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("PersonId");
            if (Note_Type.equalsIgnoreCase("SVCNOTE"))
                pi2.setValue(RecordNo);
            else
                pi2.setValue(Personid);

            request.addProperty(pi2);

            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("OperatorID");
            pi3.setValue(OperatorId);
            request.addProperty(pi3);

            if (!check_valid_note(Note)) {
               // Toast.makeText(getApplicationContext(), "Please enter valid Note, You have used some invalid characters", Toast.LENGTH_LONG).show();
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
            if (Note_Type.equalsIgnoreCase("SVCNOTE"))
                pi6.setValue(RecordNo);
            else
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
                Tost_Message("Client Note added Successfully");


            } else
                Tost_Message("Operation not done - " + AccountNo + " Result=" + result.toString());
               // Toast.makeText(getApplicationContext(), "Operation not done - " + AccountNo + " Result=" + result.toString(), Toast.LENGTH_LONG).show();

            try {
                String messgas = "The following "+ Note_Type + " note has been added to client \"" + AccountNo + "\" by \"" + StaffCode + "\" :\n\n" + Note;
                String title = "  TRACCS Client Note Added for : " + AccountNo + "\n";

                //send_local_email(title,messgas);
                //send_email_alert(messgas, title);

            } catch (Exception ex) {

               // Toast.makeText(getApplicationContext(), "Operation not done due to some server error\n" + ex.toString(), Toast.LENGTH_LONG).show();

            }


        } catch (Exception ex) {
           // Toast.makeText(getApplicationContext(), "Operation not done due to some server error\n" + ex.toString(), Toast.LENGTH_LONG).show();

        }


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
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
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

    public String SQLSafe(String sValue) {

        String sAns = null;
        sAns = sValue.replace("'", "''");
        sAns = sAns.replace("\"", "''");
        return (sAns);

    }

    public void Update_client_Note2(String Note, String Note_Type) {
        try {
            String command;
            // command= "\n update roster set notes='" + Note.replace("'", "''") + "' WHERE recordno = " + RecordNo;
            //command= command+ "\n update Recipients set notes='" + Note.replace("'", "''") + "' WHERE AccountNo in (select [client code] from roster where recordno = " + RecordNo + ")";

            Note = SQLSafe(Note);
            Note=Note.replace("\n","~");

            if (Note_Type.equalsIgnoreCase("SVCNOTE")) {
                command = "\n" + "insert into history(detaildate,PersonId,creator,Detail,PrivateFlag,ExtraDetail1,ExtraDetail2,whocode,PublishToApp) " +
                        "values(getDate(),'" + RecordNo + "','" + OperatorId + "','" + SQLSafe(Note) + "',0,'" + Note_Type + "','" + Note_Type + "','" + RecordNo + "',1)";

            }else {
                command = "\n" + "insert into history(detaildate,PersonId,creator,Detail,PrivateFlag,ExtraDetail1,ExtraDetail2,whocode,PublishToApp) " +
                        "values(getDate(),'" + Personid + "','" + OperatorId + "','" + SQLSafe(Note) + "',0,'" + Note_Type + "','" + Note_Type + "','" + AccountNo + "',1)";
            }

            set_Updates(command);
            Tost_Message("Client Note Added Successfully");

        } catch (Exception ex) {
            //Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();

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

    public void set_Updates(String command) throws IOException {
        // File froot = null;
        try {
            // check for SDcard
          //  ((TextView) findViewById(R.id.textMsg)).setText("making update");
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File filein = null;
            File fileDir = null;
            String Command2="";
            // Toast.makeText(this, command.indexOf("\n",1) + ", " + command.length()+"\n"+ command, Toast.LENGTH_SHORT).show();
           /* if (command.indexOf("\n",1)>0) {
                Command2 = command.replace("\n", "%%");
                Command2 = "\n" + Command2.substring(2);
            }else
               */

            Command2=command;

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
                        out.write(Command2);
                        out.close();

                    } catch (Exception e) {
                       // textMsg.setText(e.toString());
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
           // Button ts = (Button) findViewById(R.id.txtAddress2);
           // ts.setText(e.toString());
        }
       // ((TextView) findViewById(R.id.textMsg)).setText("Operation done successfully");
    }


    public void get_Op_Case_Notes(String Note_Type){


        File froot = null;
        File fileDir=null;


        try {

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();

            fileDir = new File(froot.getAbsolutePath()+"/.server/");
            File fXmlFile = new File(fileDir, "op_case_note.xml");

            lst_notes=new ArrayList<OP_Case_Note>();

           // Tost_Message(Recipient);

            if (fXmlFile.exists()){
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("OP_Case_Note");

                //   Toast.makeText(getApplicationContext(),"node list = " + nList.getLength() , Toast.LENGTH_LONG).show();

                listView = (RecyclerView) findViewById(R.id.listViewOP);
                if (nList==null) {

                    try{
                        Op_case_note_adapter mAdapter = new Op_case_note_adapter(lst_notes, this );
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        listView.setLayoutManager(mLayoutManager);
                        if(flagDecoration) {
                            listView.setItemAnimator(new DefaultItemAnimator());
                            listView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
                            flagDecoration=false;
                        }

                        listView.setAdapter(mAdapter);

                    }catch(Exception ex){Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();}

                    return;
                }

                for ( int tmp = 0; tmp< nList.getLength(); tmp++) {
                    try{

                        Node  nNode = nList.item(tmp);


                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;

                            String val=eElement.getElementsByTagName("Note_Type").item(0).getTextContent();

                            String element=eElement.getElementsByTagName("Receipient_Code").item(0).getTextContent();


                            if (( ViewAll|| val.equalsIgnoreCase(Note_Type)) && (element.equalsIgnoreCase(Recipient) || element.equalsIgnoreCase(RecordNo) ) ) {

                                //  OPNOTE CASENOTE CLINNOTE
                                if (val.equalsIgnoreCase("OPNOTE") && Integer.parseInt( EnableViewNoteCases.substring(0, 1))==0){
                                    continue;
                                }
                                if (val.equalsIgnoreCase("CASENOTE") && Integer.parseInt( EnableViewNoteCases.substring(1, 2))==0){
                                    continue;
                                }
                                if (val.equalsIgnoreCase("SVCNOTE") && Integer.parseInt( EnableViewNoteCases.substring(2, 3))==0){
                                    continue;
                                }
                                if (val.equalsIgnoreCase("RECIMNOTE") && Integer.parseInt( EnableViewNoteCases.substring(3, 4))==0){
                                    continue;
                                }
                                if (val.equalsIgnoreCase("CLINNOTE") && Integer.parseInt( EnableViewNoteCases.substring(4, 5))==0){
                                    continue;
                                }
                                note = new OP_Case_Note();

                                note.setNoteType(val);
                                note.setRecipient(element);
                                element = eElement.getElementsByTagName("Note_Date").item(0).getTextContent();
                                note.setNote_Date(element);

                                element = eElement.getElementsByTagName("Detail").item(0).getTextContent();
                                note.setDetail(element);

                                element = eElement.getElementsByTagName("Creator").item(0).getTextContent();
                                note.setCreator(element);

                                note.setRecordNo(RecordNo);
                                note.setAccountNo(AccountNo);
                                note.setPersonId(Personid);
                                note.setRoster_Date(Roster_Date);
                                note.setJob_Time(Job_Time);


                                lst_notes.add(note);

                            }




                        } else {  }

                    }catch(Exception e){
                        //Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
                        Tost_Message(e.toString());
                    }


                }//loop

               /* note.setRecordNo("");
                note.setJob_Time("");
                note.setNote_Date("");
                note.setAccountNo("");;
                note.setNoteType("");
                note.setCreator("");
                note.setDetail("");
                lst_notes.add(note);
*/

                listView = (RecyclerView) findViewById(R.id.listViewOP);
                try{

                    if (lst_notes.size()<=0){

                       Tost_Message("NO PUBLISHED NOTES");
                      // return;
                    }

                    Op_case_note_adapter mAdapter = new Op_case_note_adapter(lst_notes, this );
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    listView.setLayoutManager(mLayoutManager);
                    if(flagDecoration) {
                        listView.setItemAnimator(new DefaultItemAnimator());
                        listView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
                        flagDecoration=false;
                    }

                    listView.setAdapter(mAdapter);



                }catch(Exception ex){Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();}


            }
        }catch(Exception e){}
    }


    public String get_Recipient(String AccountNo) {


        File froot = null;
        File fileDir = null;


        try {

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();

            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = new File(fileDir, "Recipient.xml");

            lst_notes = new ArrayList<OP_Case_Note>();


            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Recipient");

                //   Toast.makeText(getApplicationContext(),"node list = " + nList.getLength() , Toast.LENGTH_LONG).show();


                if (nList == null) return "0";


                for (int tmp = 0; tmp < nList.getLength(); tmp++) {
                    try {

                        Node nNode = nList.item(tmp);


                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;

                            String val = eElement.getElementsByTagName("AccountNo").item(0).getTextContent();


                            if (Recipient.equalsIgnoreCase(val)) {


                                Personid = eElement.getElementsByTagName("Personid").item(0).getTextContent();
                              return Personid;

                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }


                }//loop
            }
        }catch (Exception e) {}

        return Personid;
    }

    public void getRoster_Recipient2(String Recipient_AccountNo)
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
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Roster_Recipient");
                if (nList==null) return;

                String Recipient_AccountNo2="";

                for (int temp = 0; temp< nList.getLength(); temp++) {

                    try{
                        Node  nNode = nList.item(temp);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;

                            Recipient_AccountNo2= eElement.getElementsByTagName("AccountNo").item(0).getTextContent();

                            if( Recipient_AccountNo2.equals(Recipient_AccountNo) )
                            {
                                try{


                                    Cordinator_Email="";
                                    if (eElement.getElementsByTagName("Cordinator_Email").item(0).getTextContent()!=null){

                                        Cordinator_Email=eElement.getElementsByTagName("Cordinator_Email").item(0).getTextContent();

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


    class MyAsyncClass5 extends AsyncTask<String, Void, String> {

        LoadingDialog pDialog;
        String Notee="";
        String Note_Typee="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(OP_Case_Note_Activity.this);
            pDialog.setMessage("Please wait while Processing  ....");
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... mApi) {
            try {
// Add subject, Body, your mail Id, and receiver mail Id.
                // sender.sendMail("Subject", " body", "from Mail", "to mail");
                Notee=mApi[0];
                Note_Typee=mApi[1];
                Update_client_Note(Notee, Note_Typee);

            }catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
            return Note_Typee;

        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pDialog.cancel();
            try {
                String messgas = "The following "+ Note_Typee+" note has been added to client \"" + AccountNo + "\" by \"" + StaffCode + "\" :\n\n" + Notee;
                String title = "  TRACCS Client Note Added for : " + AccountNo + "\n";

               // if(Server_Available)
                send_local_email(title,messgas);
                //send_email_alert(messgas, title);


            } catch (Exception ex) {}

            try{
                new MyAsyncClass4().execute(result,Main_Op_Note);

            }catch (Exception ex) {}
        }
    }


    class MyAsyncClass4 extends AsyncTask<String, Void, Void> {

        LoadingDialog pDialog;

        String Note_Type2="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(OP_Case_Note_Activity.this);
            pDialog.setMessage("Please wait while Processing  ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... mApi) {
            try {
// Add subject, Body, your mail Id, and receiver mail Id.
                // sender.sendMail("Subject", " body", "from Mail", "to mail");

                    Note_Type2 = mApi[0];
                    if(Server_Available)
                  ; // load_OP_Case_Notes();


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

                get_Op_Case_Notes(Main_Op_Note);

            } catch (Exception ex) {}
            //  Toast.makeText(getApplicationContext(), "Email send", 100).show();
        }
    }


    public void load_OP_Case_Notes(){


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

            pi.setValue( Security_Token + StaffCode);
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

        String client_codes = AccountNo;

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
    class MyAsyncClass_load_Op_Case_Note extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(OP_Case_Note_Activity.this);
            pDialog.setMessage("Please wait while loading notes from server  ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {


                String format="yyyy/MM/dd";

                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
                Calendar c = Calendar.getInstance();
                Date dt=c.getTime();

                String rosterDate = sdf.format(dt);
                bulk_data= new Bulk_Data(root,StaffCode,OperatorId,Security_Token,Server_Available,rosterDate,context);
                // Tost_Message(parms[0]);
//                if (Refresh_OP_Note_data_single){
//                  //  Refresh_OP_Note_data_single=false;
//                    bulk_data.get_OP_Case_Note(AccountNo);
//                }else
                  bulk_data.get_OP_Case_Notes(AccountNo);

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
            get_Op_Case_Notes(Main_Op_Note);

        }
    }
    public void Set_Filters(Context view) {
        final Dialog dialog_filter = new Dialog(view);
        dialog_filter.setContentView(R.layout.filters);
        dialog_filter.setTitle("Add Leave Application");


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog_filter.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = 400; //WindowManager.LayoutParams.MATCH_PARENT;


        final Spinner spinner1 = (Spinner) dialog_filter.findViewById(R.id.cmbFilters);


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {

                // selected_val=spinner1.getSelectedItem().toString();
               // dialog_filter.dismiss();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        dialog_filter.show();


    }

}
