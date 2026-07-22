package adamas.traccs.mta_20_06;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import androidx.core.text.HtmlCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Add_Service_Note extends AppCompatActivity {

    String root="";

    private final String NAMESPACE = "https://tempuri.org/";

    private final String SOAP_ACTION4 = "https://tempuri.org/Add_client_Note";
    private final String METHOD_NAME4 = "Add_client_Note";

    String AccountNo;
    String EnableViewNoteCases;
    String AllowRosterNoteEntry,AllowOPNoteEntry,AllowClinicalNoteEntry,AllowCaseNoteEntry;
    String RecordNo,Recipient,Security_Token,OperatorId,Personid,StaffCode;
    String Cordinator_Email="";
    Boolean Server_Available=false;
    String Job_Time;
    String Main_Op_Note;
    String UseOPNoteAsShiftReport;
    String Roster_Date;
    List<Recipient_Goals> lst_Recipient_goals=null;
    List<String> lst_Recipient_Strategies=null;
    List<String> list_gen;

    final String PREFS_NAME = "MTAPrefs";
    SharedPreferences settings = null;
    String UseServiceNoteAsShiftReport;
    ArrayList<OP_Case_Note> lst_notes=null;
    File froot;
    Email_Settings email_seting = null;
    String email_msg,Email_Subject;
    Email email;
    String ServiceType="";
    String Shift_Goal="";
    String selectedItem="";
    Bulk_Data bulk_data;
    Context context;
    Recipient_Goals Selected_Goal=null;
    ImageView imgGoals ;
    ImageView imgStrategy ;
    View img_view_Strategy;
    String DefaultAppNoteCategory="#MTANOTE";

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
                if(settings.getString( "UseServiceNoteAsShiftReport","1").equalsIgnoreCase("false")
                        || settings.getString( "UseServiceNoteAsShiftReport","0").equalsIgnoreCase("0"))
                {
                    textviewTitle.setText("Service Note");
                }
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
    void setMultiLineCapSentencesAndDoneAction(EditText txt) {
        txt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        txt.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        txt.setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES  );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__service__note);

        settings = getSharedPreferences(PREFS_NAME, 0);
        setupActionBar();
        context=this.getApplicationContext();
        set_server_Ip();
        Server_Available= isOnline(getApplicationContext());
        TextView txtRecipient = (TextView) findViewById(R.id.txt1);
        //txt1.setText("Recipient \n" + AccountNo);
        txtRecipient.setText(HtmlCompat.fromHtml("<b>Recipient</b><br>" + AccountNo,0));

        boolean shift_note=false;
        
        // Toast.makeText(getApplicationContext(), "Add Mobile Note of maximum 500 characters", Toast.LENGTH_LONG).show();
        final EditText txtRosterNote = (EditText) findViewById(R.id.txtRosterNote);
        final TextView txtShiftDate = findViewById(R.id.txtShiftDate);
        final EditText spShiftGoals =  findViewById(R.id.txtShiftgoals);
        final EditText txtShiftPurpose =  findViewById(R.id.txtShiftPurpose);
        final EditText txtAction = findViewById(R.id.txtAction);
        final EditText txtOutCome = findViewById(R.id.txtOutCome);
        final EditText txtFeedback= findViewById(R.id.txtFeedBack);


        img_view_Strategy=findViewById(R.id.img_view_Strategy);

        imgGoals =  findViewById(R.id.imgGoals);
        imgStrategy =  findViewById(R.id.imgStrategy);

        imgGoals.setVisibility(View.GONE);
        img_view_Strategy.setVisibility(View.GONE);
       // setPadding(txtShiftPurpose,15,0,10,0);


//        setMultiLineCapSentencesAndDoneAction(txtShiftPurpose);
//        setMultiLineCapSentencesAndDoneAction(txtAction);
//        setMultiLineCapSentencesAndDoneAction(txtOutCome);
//        setMultiLineCapSentencesAndDoneAction(txtRosterNote);
//        setMultiLineCapSentencesAndDoneAction(txtFeedback);

        try {

          //  set_control_Visibility(false);
            txtShiftDate.setText(Roster_Date.substring(8,10) + "/" + Roster_Date.substring(5,7) + "/" + Roster_Date.substring(0,4) + " " + Job_Time);

            set_control_Visibility(false);


           if ( UseServiceNoteAsShiftReport.equalsIgnoreCase("true") || UseServiceNoteAsShiftReport.equals("1"))
            {
              set_control_Visibility(true);
                get_Recipient_goals();

            }
        }catch(Exception ex){Tost_Message(ex.toString());}


        imgGoals.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lst_Recipient_goals==null) return;
                if (lst_Recipient_goals.size()>1) {
                    set_Spinner_Gaols(v.getContext(), spShiftGoals, list_gen,"Goals");
                    //set_Spinner(v.getContext(), spShiftGoals, list_gen,"Goals");
                } else   if (lst_Recipient_goals.size()>0)
                    spShiftGoals.setText(lst_Recipient_goals.get(0).getGoal());


        }


        });

        imgStrategy.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lst_Recipient_Strategies==null) return;
                if (lst_Recipient_Strategies.size()>1)
                    set_Spinner(v.getContext(),txtShiftPurpose,lst_Recipient_Strategies,"Strategies");
                else   if (lst_Recipient_Strategies.size()>0)
                    txtShiftPurpose.setText(lst_Recipient_Strategies.get(0));
            }


        });

        Button dialogButton = (Button) findViewById(R.id.btnExit);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });
        // if button is clicked, close the custom dialog



        Button dialogSave = (Button) findViewById(R.id.btnOK);

        dialogSave.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                String notes="";

               // if (UseServiceNoteAsShiftReport.equalsIgnoreCase("true") || UseServiceNoteAsShiftReport.equals("1"))
              //  {
                    String shift_goals="";
                //    String shift_goals= lst_shiftgoals.get(spShiftGoals.getSelectedItemPosition());
                if ( UseServiceNoteAsShiftReport.equalsIgnoreCase("true") || UseServiceNoteAsShiftReport.equals("1")){

                    if (spShiftGoals.getText().toString().isEmpty()){
                        Tost_Message("Please enter valid shift goals");
                        spShiftGoals.setError("Please fill field");
                        return;
                    }

                      if (Shift_Goal.equalsIgnoreCase(""))
                         shift_goals=spShiftGoals.getText().toString(); //lst_shiftgoals.get(spShiftGoals.getSelectedItemPosition());
                       else
                        shift_goals=Shift_Goal;

                    if (txtRosterNote.getText().toString().isEmpty()){
                        Tost_Message("Please enter valid note");
                        txtRosterNote.setError("Please fill field");
                        return;
                    }

                    if (shift_goals.isEmpty()){
                        Tost_Message("Please select valid shift goals");
                        return;
                    }
                    if (txtShiftPurpose.getText().toString().isEmpty()){
                        Tost_Message("Please fill in all fields");
                        txtShiftPurpose.setError("Please fill field");
                        return;
                    }
                    if (txtAction.getText().toString().isEmpty()){
                        Tost_Message("Please fill in all fields");
                        txtAction.setError("Please fill field");

                        return;
                    }
                    if (txtOutCome.getText().toString().isEmpty()){
                        //Tost_Message("Please fill in all fields");
                       // txtOutCome.setError("Please fill field");
                       // return;
                    }
                    if (txtFeedback.getText().toString().isEmpty()){
                        Tost_Message("Please fill in all fields");
                        txtFeedback.setError("Please fill field");
                        return;
                    }
                        notes = "SHIFT REPORT FOR : " +  Roster_Date + " " + Job_Time + "\n\n"
                                + "SHIFT GOAL/S: \n" + shift_goals + "\n\n"
                                + "SHIFT PURPOSE: \n" + txtShiftPurpose.getText() + "\n\n"
                                + "ACTIONS: \n" + txtAction.getText() + "\n\n"
                                + "Total KM: \n" + txtOutCome.getText() + "\n\n"
                                + "FEEDBACK: \n" + txtFeedback.getText() + "\n\n"
                                + txtRosterNote.getText().toString();


                }else {

                    notes = txtRosterNote.getText().toString();
                }


//                if (!check_valid_note(notes)) {
//                    Toast.makeText(getApplicationContext(), "Please enter valid Note, You have used some invalid characters", Toast.LENGTH_LONG).show();
//                    return;
//                }

                //notes=notes.replace("\'", "\'\'");
                notes = SQLSafe(notes);
                if (!isOnline(getApplicationContext()))
                    notes=notes.replace("\n","~");
                //String command="Update Roster set " +
                //            " notes=CONVERT(VARCHAR(MAX),isnull(notes,'')) + CONVERT(VARCHAR,' -- " + notes  + "')" +
                //               " where RecordNo=" + RecordNo ;

               /* String comman"\nUpdate Roster set " +
                        " notes=CONVERT(VARCHAR(MAX),isnull(notes,'')) + CONVERT(VARCHAR(MAx),', " + notes + "')" +
                        " where RecordNo=" + RecordNo;
*/
                String command="";
                if ( UseServiceNoteAsShiftReport.equalsIgnoreCase("true") || UseServiceNoteAsShiftReport.equals("1")) {
                     command = "insert into history(detaildate,PersonId,creator,Detail,PrivateFlag,ExtraDetail1,ExtraDetail2,whocode,PublishToApp) " +
                            "values(getDate(),'" + RecordNo + "','" + OperatorId + "','" + notes + "',0,'SVCNOTE','#SHIFTREPORT'," + RecordNo + ",1)";

                }else{
                     command = "insert into history(detaildate,PersonId,creator,Detail,PrivateFlag,ExtraDetail1,ExtraDetail2,whocode,PublishToApp) " +
                            "values(getDate(),'" + RecordNo + "','" + OperatorId + "','" + notes + "',0,'SVCNOTE','" + DefaultAppNoteCategory +"'," + RecordNo + ",1)";

                }


                //  	Toast.makeText(getApplicationContext(),command, Toast.LENGTH_LONG).show ();
                try {
                    //  ((TextView)findViewById(R.id.lblErrMsg)).setText(command);
                    //  Make_update(command);
                    //  new MyAsyncClass_Roster_Note().execute(command, notes);
                    if (Server_Available == false) {

                        set_Updates("\n" + command);
                        Tost_Message("Service Note added Successfully");
                        if ( UseServiceNoteAsShiftReport.equalsIgnoreCase("true") || UseServiceNoteAsShiftReport.equals("1")) {

                            if (!txtOutCome.getText().toString().equalsIgnoreCase("")) {
                                String distance = txtOutCome.getText().toString();

                                String Travel_Type = "TRAVEL WITHIN";
                                String Charge_Type = "Chargeable";
                                String Mobile_Device = Build.MANUFACTURER + "-" + android.os.Build.MODEL;
                                String startKM = "0";
                                String EndKM = distance;
                                String MyCar = "1";

                                command = "\nV" + "`" + RecordNo + "`" + distance + "`" + Travel_Type + "`" + Charge_Type + "`" + startKM + "`" + EndKM + "`" + notes.replace("\n", "~") + "`" + MyCar + "`" + Mobile_Device;
                               // set_Updates(command);
                            }
                        }
                        String messgas = "The following op/case note has been added for client \"" + AccountNo + "\" by \"" + StaffCode + "\" :\n\n" + notes;
                        String title = "  TRACCS Service Note Added for : " + AccountNo + "\n";

                        if(settings.getString("SuppressEmailOnRosterNote","false").equalsIgnoreCase("false") || settings.getString("SuppressEmailOnRosterNote","false").equalsIgnoreCase("0")  ) {
                            if (settings.getString( "EnableEmailNotification","true").equalsIgnoreCase("true")
                                    || settings.getString( "EnableEmailNotification","true").equalsIgnoreCase("1"))

                                send_email_alert(messgas, title);

                        }

                        finish();
                        return;
                    }

                    new Add_Service_Note.MyAsyncClass_Roster_Note().execute(command, notes);
                    // Add_Service_Note(notes,"SVCNOTE");

                    //Tost_Message( "Roster Note added Successfully");

                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                }

                
            }
        });

    }

    void set_control_Visibility(boolean status){

        TextView spinner1 = (TextView) findViewById(R.id.txtShiftgoals);
        TextView txtShiftPurpose = findViewById(R.id.txtShiftPurpose);
        TextView txtAction = findViewById(R.id.txtAction);
        TextView txtOutCome = findViewById(R.id.txtOutCome);
        TextView txtFeedBack = findViewById(R.id.txtFeedBack);

        TextView lblShiftGoals = findViewById(R.id.lblShiftGoals);
        TextView lblShiftPurpose = findViewById(R.id.lblShiftPurpose);
        TextView lblAction = findViewById(R.id.lblAction);
        TextView lbloutCome = findViewById(R.id.lbloutCome);
        TextView lblFeedBack = findViewById(R.id.lblFeedBack);
        View img_Shift_Purpose = findViewById(R.id.img_Shift_Purpose);
        TextView lbl1RosterNote = findViewById(R.id.lbl1RosterNote);

        if (status){

            spinner1.setVisibility(View.VISIBLE);
            txtShiftPurpose.setVisibility(View.VISIBLE);
            txtAction.setVisibility(View.VISIBLE);
            txtOutCome.setVisibility(View.VISIBLE);
            txtFeedBack.setVisibility(View.VISIBLE);

            lblShiftGoals.setVisibility(View.VISIBLE);
            lblShiftPurpose.setVisibility(View.VISIBLE);
            lblAction.setVisibility(View.VISIBLE);
            lbloutCome.setVisibility(View.VISIBLE);
            lblFeedBack.setVisibility(View.VISIBLE);
            img_Shift_Purpose.setVisibility(View.VISIBLE);
            lbl1RosterNote.setText("What ongoing or follow-up activities could be done in the next support to continue to work towards the goals?");

        }else {
            spinner1.setVisibility(View.GONE);
            txtShiftPurpose.setVisibility(View.GONE);
            txtAction.setVisibility(View.GONE);
            txtOutCome.setVisibility(View.GONE);
            txtFeedBack.setVisibility(View.GONE);

            lblShiftGoals.setVisibility(View.GONE);
            lblShiftPurpose.setVisibility(View.GONE);
            lblAction.setVisibility(View.GONE);
            lbloutCome.setVisibility(View.GONE);
            lblFeedBack.setVisibility(View.GONE);
            img_Shift_Purpose.setVisibility(View.GONE);
            lbl1RosterNote.setText("Service Note");
        }
    }

    public List<String> get_ShiftGoals() {


        File froot = null;
        File fileDir = null;
        List<String> lst_goals=null;

        try {

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();

            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = new File(fileDir, "ShiftGoals.xml");

            lst_goals = new ArrayList<String>();


            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("DataDomain");

                //   Toast.makeText(getApplicationContext(),"node list = " + nList.getLength() , Toast.LENGTH_LONG).show();


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
                        //  Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }


                }//loop
            }
        }catch (Exception e) {}

        return lst_goals;
    }

    void set_server_Ip()
    {

        try{
            Bundle bundle = getIntent().getExtras();
            settings = getSharedPreferences(PREFS_NAME, 0);

            try{

                this.RecordNo= bundle.getString("RecordNo");
                Job_Time= bundle.getString("Job_Time");
                root=settings.getString("root","");
                Recipient=bundle.get("Recipient").toString();
                Security_Token=settings.getString("Security_Token","");
                OperatorId=settings.getString("OperatorId","");
                this.AccountNo= bundle.getString("AccountNo");
                this.Roster_Date= bundle.getString("Roster_Date");
                Personid= bundle.getString("PersonId","0");
                Main_Op_Note= bundle.getString("Main_Op_Note","");

                ServiceType= bundle.getString("ServiceType","");

            }catch(Exception ex){}

            try{
                // Cordinator_Email=settings.getString("Staff_Coordinator_Email","-");
                EnableViewNoteCases=settings.getString("EnableViewNoteCases","00000");
                StaffCode=settings.getString("StaffCode","ABC");
                this.Server_Available= settings.getBoolean("Server_Available",false);
                //  Cordinator_Email= settings.getString("Coordinator_Email","");
                DefaultAppNoteCategory=  settings.getString( "DefaultAppNoteCategory","#MTANOTE");

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
            try {
                if (settings.getString("ShiftNoteEmailOverrides", "false").equalsIgnoreCase("true")) {
                    if (!settings.getString("ShiftNoteEmail", "").equals(""))
                        Cordinator_Email = settings.getString("ShiftNoteEmail", "");
                }

            }catch(Exception ex){}

            Load_Data();

        }catch(Exception ex){}
    }
    void Load_Data(){

        if (Server_Available) {
            bulk_data = new Bulk_Data(root, StaffCode, OperatorId, Security_Token, Server_Available, Roster_Date,context);

            bulk_data.get_Recipient_ShiftGoals(Personid);
            //bulk_data.get_Recipient_Goals_Strategies(Personid);
        }
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
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
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
    class MyAsyncClass_Roster_Note extends AsyncTask<String, String, Void> {

        LoadingDialog pDialog;
        String Command = "";
        String notes = "";
        boolean done=true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Add_Service_Note.this);
            pDialog.setMessage("Please wait while saving Note ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... mApi) {
            try {
// Add subject, Body, your mail Id, and receiver mail Id.
                Command = mApi[0];
                notes = mApi[1];
                try{

                    Add_Service_Note_on_server(notes,"SVCNOTE");



                }catch(Exception ex){}
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
            String messgas = "The following op/case note has been added for client \"" + AccountNo + "\" by \"" + StaffCode + "\" :\n\n" + notes;
            String title = "  TRACCS Service Note Added for : " + AccountNo + "\n";

            if(settings.getString("SuppressEmailOnRosterNote","false").equalsIgnoreCase("false") || settings.getString("SuppressEmailOnRosterNote","false").equalsIgnoreCase("0")  ) {
                if (settings.getString( "EnableEmailNotification","true").equalsIgnoreCase("true")
                        || settings.getString( "EnableEmailNotification","true").equalsIgnoreCase("1"))

                    send_email_alert(messgas, title);

            }
            finish();
        }
    }
    String getSecurityToken() {

        String Val = OperatorId + "$" + Security_Token + "$";
        return Val;
    }

    public void Add_Service_Note_on_server(String Note, String Note_Type) {

        Server_Available= isOnline(getApplicationContext());
        if (Server_Available == false) {

                Note=Note.replace("\n","~");
            Update_client_Note2(Note, Note_Type);

            return;
        }


        try {
            String URL4 = root + "/TimeSheet.asmx?op=Add_client_Note";

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
            pi2.setValue(RecordNo);
            request.addProperty(pi2);

            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("OperatorID");
            pi3.setValue(OperatorId);
            request.addProperty(pi3);

//            if (!check_valid_note(Note)) {
//                Toast.makeText(getApplicationContext(), "Only enter numbers, letters, ambersands, and symbols. No emojis or graphics", Toast.LENGTH_LONG).show();
//                return;
//            }
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
            pi6.setValue(RecordNo);
            request.addProperty(pi6);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            SoapPrimitive result = null;

            // Make the soap call.
            androidHttpTransport2.call(SOAP_ACTION4, envelope);
            result = (SoapPrimitive) envelope.getResponse();

            if (Boolean.valueOf(result.toString()) == true) {
                Tost_Message( "Service Note added Successfully");

                settings.edit().putBoolean("HasServiceNotes",true).commit();

                TextView txtOutCome= findViewById(R.id.txtOutCome);
                if (!txtOutCome.getText().toString().equalsIgnoreCase(""))
                {
                   // Add_Travel_claim(txtOutCome.getText().toString(),Note);
                }


            } else
                Tost_Message( "Operation not done - " + AccountNo + " Result=" + result.toString());



        } catch (Exception ex) {

            Tost_Message(ex.toString());
        }


    }
    void Add_Travel_claim(String distance,String notes){
          String URL = root  + "/TimeSheet.asmx?op=Add_Travel_Roster";
          String SOAP_ACTION =  "https://tempuri.org/Add_Travel_Roster";
          String METHOD_NAME = "Add_Travel_Roster";

          String Travel_Type="TRAVEL WITHIN";
          String  Charge_Type="Chargeable";

        try{
            String Mobile_Device= Build.MANUFACTURER + "-" + android.os.Build.MODEL  ;

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug =true;

            PropertyInfo pi=new PropertyInfo();
            pi.setName("RecordNo");

            pi.setValue( getSecurityToken() + RecordNo);
            request.addProperty(pi);

            PropertyInfo pi2=new PropertyInfo();
            pi2.setName("distance");
            pi2.setValue(""+distance);
            request.addProperty(pi2);

            PropertyInfo pi3=new PropertyInfo();
            pi3.setName("Travel_Type");
            pi3.setValue(Travel_Type);
            request.addProperty(pi3);

            PropertyInfo pi4=new PropertyInfo();
            pi4.setName("Charge_Type");
            pi4.setValue(Charge_Type);
            request.addProperty(pi4);

            PropertyInfo pi5=new PropertyInfo();
            pi5.setName("StartKm");
            pi5.setValue("0");
            request.addProperty(pi5);

            PropertyInfo pi6=new PropertyInfo();
            pi6.setName("EndKM");
            pi6.setValue(""+distance);
            request.addProperty(pi6);


           TextView txtRosterNote= findViewById(R.id.txtRosterNote);
            PropertyInfo pi7=new PropertyInfo();
            pi7.setName("Notes");
           // pi7.setValue(txtRosterNote.getText().toString());
            pi7.setValue(notes);
            request.addProperty(pi7);

            PropertyInfo pi8=new PropertyInfo();
            pi8.setName("OwnVehicle");
            pi8.setValue("1");
            request.addProperty(pi8);

            PropertyInfo pi9=new PropertyInfo();
            pi9.setName("Mobile_Device");
            pi9.setValue(Mobile_Device);
            request.addProperty(pi9);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive  result =(SoapPrimitive)envelope.getResponse();
            String result_value= result.toString();
            if (!result_value.equalsIgnoreCase("0")) {
                Tost_Message("Travel Claim added Successfully");
            }else{
                Tost_Message("Travel Claim may not be added due to missing Travel default settings");
            }

        }catch(Exception ex){

        }
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
    public void composeEmail(String addresses, String subject, String body) {
        String uriText = "mailto:" + addresses +
                "?subject=" + subject +
                "&body=" + body;
        Uri uri = Uri.parse(uriText);
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(uri);
        startActivity(Intent.createChooser(sendIntent, "Send Email").addFlags( Intent.FLAG_ACTIVITY_NEW_TASK));
    }
    void send_local_email(String subject, String email_msg){

            composeEmail(Cordinator_Email,subject,email_msg);
            if(1==1) return;

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
               // startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }catch (Exception ex) {}
            Intent Email = new Intent(Intent.ACTION_SEND);
            Email.setType("text/email");
            Email.putExtra(Intent.EXTRA_EMAIL, new String[] { Cordinator_Email });
            Email.putExtra(Intent.EXTRA_SUBJECT, subject);
            Email.putExtra(Intent.EXTRA_TEXT, email_msg);
            startActivity(Intent.createChooser(Email, "SEND EMAIL TO" ));

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
    void send_local_email2(Context mActivity){

        String subject = Email_Subject;
        try{



         //   Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND

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
           
        }catch (Exception ex) {
            // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
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



    public String SQLSafe(String sValue) {

        String sAns = null;
        sAns = sValue.replace("'", "''");
        sAns = sAns.replace("\"", "''");
        return (sAns);

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
                if (((("%*!,-_()/\\=0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz:@#&;?'$.\n\t\" " + s_ExtraChar).indexOf(s_Char) + 1)
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

    boolean check_valid_note(String value) {
        boolean valid = false;
        int i = 0;

        for (i = 0; i < value.length(); i++) {
            if (AllowableChar(value.charAt(i), 1, "!@#%^&*()_+=?/><}{[];\\'$.\\n\\t>,")) {
                valid = true;
            } else {
                valid = false;
                break;
            }
        }


        return valid;

    }

    public void set_Updates(String command) throws IOException {
        // File froot = null;
        try {
            // check for SDcard
           // ((TextView) findViewById(R.id.txtAcknowledge)).setText("making update");
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File filein = null;
            File fileDir = null;
            String Command2="";
            // Toast.makeText(this, command.indexOf("\n",1) + ", " + command.length()+"\n"+ command, Toast.LENGTH_SHORT).show();
            if (command.indexOf("\n",1)>0) {
                Command2 = command.replace("\n", "%%");
                Command2 = "\n" + Command2.substring(2);
            }else
                Command2=command;


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
       // ((TextView) findViewById(R.id.txtAcknowledge)).setText("Operation done successfully");
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
                Toast.makeText(context, "Online Connection becomes available\nRe-login the App in online mode", Toast.LENGTH_SHORT).show();

                finish();
            }

            return connected;


        } catch (Exception e) {
            ///System.out.println("CheckConnectivity Exception: " + e.getMessage());
            // Log.v("connectivity", e.toString());
        }
        return connected;
    }
    public void Update_client_Note2(String Note, String Note_Type) {
        try {
            String command;
            // command= "\n update roster set notes='" + Note.replace("'", "''") + "' WHERE recordno = " + RecordNo;
            //command= command+ "\n update Recipients set notes='" + Note.replace("'", "''") + "' WHERE AccountNo in (select [client code] from roster where recordno = " + RecordNo + ")";

            Note = SQLSafe(Note);
            Note=Note.replace("\n","~");


//            command = "\n" + "insert into history(detaildate,PersonId,creator,Detail,PrivateFlag,ExtraDetail1,ExtraDetail2,whocode,PublishToApp) " +
//                    "values(getDate(),'" + RecordNo + "','" + OperatorId + "','" + SQLSafe(Note) + "',0,'" + Note_Type + "','" + Note_Type + "','" + AccountNo + "',1)";

            if ( UseServiceNoteAsShiftReport.equalsIgnoreCase("true") || UseServiceNoteAsShiftReport.equals("1")) {
                command = "insert into history(detaildate,PersonId,creator,Detail,PrivateFlag,ExtraDetail1,ExtraDetail2,whocode,PublishToApp) " +
                        "values(getDate(),'" + RecordNo + "','" + OperatorId + "','" + SQLSafe(Note) + "',0,'SVCNOTE','#SHIFTREPORT'," + RecordNo + ",1)";

            }else{
                command = "insert into history(detaildate,PersonId,creator,Detail,PrivateFlag,ExtraDetail1,ExtraDetail2,whocode,PublishToApp) " +
                        "values(getDate(),'" + RecordNo + "','" + OperatorId + "','" + SQLSafe(Note) + "',0,'SVCNOTE','"+DefaultAppNoteCategory+"'," + RecordNo + ",1)";

            }
            set_Updates(command);

            try {
                String messgas = "The following op/case note has been added for client \"" + AccountNo + "\" by \"" + StaffCode + "\" :\n\n" + Note;
                String title = "  TRACCS Client Note Added for : " + AccountNo + "\n";

               // send_email_alert(messgas, title);

            } catch (Exception ex) {

            }
            ((TextView) findViewById(R.id.txtAcknowledge)).setText("Client Note added Successfully");
        } catch (Exception ex) {
            ((TextView) findViewById(R.id.txtAcknowledge)).setText("Operation not done due to some server error\n" + ex.toString());
        }

    }
    public void set_Spinner(Context view, final TextView spinner, final List<String> lst, String Title) {
        final Dialog dialog = new Dialog(view);
        dialog.setContentView(R.layout.spinner_list_with_title);
        //dialog.setTitle("My Dashboard");
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        Window window = dialog.getWindow();

        if (window != null) {
            window.setBackgroundDrawableResource(R.color.shidt_detail_color);
            window.getAttributes().x=0;//(int) TextDate.getTop();
            window.getAttributes().y =0;

            WindowManager.LayoutParams params = window.getAttributes();
            params.width =  WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;

            params.horizontalMargin = 0;
            params.verticalMargin=0;
            params.gravity = Gravity.CENTER;
            params.dimAmount = 0;
            params.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(params);
        }
        TextView txtSpinnerTitle= dialog.findViewById(R.id.txtSpinnerTitle);
        txtSpinnerTitle.setText(Title);
        ListView lstSpinner= dialog.findViewById(R.id.lstSpinner);

        if (lst.size()<=0) return;
        lstSpinner.setAdapter(new Spinner_Adpater(view,lst));
        lstSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                selectedItem = lst.get(position);
                spinner.setText(selectedItem);
                dialog.dismiss();

            }});

        //spinner.setText(selectedItem);
        dialog.show();

    }

    public void set_Spinner_Gaols(Context view, final TextView spinner, final List<String> lst, String Title) {
        final Dialog dialog = new Dialog(view);
        dialog.setContentView(R.layout.spinner_list_with_title);
        //dialog.setTitle("My Dashboard");
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);


        final EditText txtShiftPurpose =  findViewById(R.id.txtShiftPurpose);
        ListView lstSpinner= dialog.findViewById(R.id.lstSpinner);
        TextView txtSpinnerTitle= dialog.findViewById(R.id.txtSpinnerTitle);
        txtSpinnerTitle.setText(Title);

        if (lst.size()<=0) return;
        lstSpinner.setAdapter(new Spinner_Adpater(view,lst));
        lstSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                selectedItem = lst.get(position);
                spinner.setText(selectedItem);
                Selected_Goal=lst_Recipient_goals.get(position);
                get_Recipient_Strategies(Selected_Goal.getGoal());
                if (lst_Recipient_Strategies.size()==1)
                    txtShiftPurpose.setText(lst_Recipient_Strategies.get(0));
                dialog.dismiss();

            }});

        //spinner.setText(selectedItem);
        dialog.show();

    }
    public void set_Spinner(Context view, final TextView spinner, final List<String> lst) {
        final Dialog dialog = new Dialog(view,R.style.DialogTheme);
        dialog.setContentView(R.layout.spinner_list);
        //dialog.setTitle("My Dashboard");
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        Window window = dialog.getWindow();
        if (window != null) {
            window.getAttributes().x=0;//(int) TextDate.getTop();
            window.getAttributes().y =0;

            WindowManager.LayoutParams params = window.getAttributes();
            params.width =  WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;

            params.horizontalMargin = 0;
            params.verticalMargin=0;
            params.gravity = Gravity.RIGHT;
            params.dimAmount = 0;
            params.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(params);
        }

        ListView lstSpinner= dialog.findViewById(R.id.lstSpinner);
        if (lst.size()<=0) return;
        lstSpinner.setAdapter(new Spinner_Adpater(view,lst));
        lstSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                selectedItem = lst.get(position);
                spinner.setText(selectedItem);


                dialog.dismiss();

            }});

        //spinner.setText(selectedItem);
        dialog.show();

    }
void setPadding(View view, int left, int top, int right, int bottom ) {
   // int paddingDp = 25;
    float density = view.getContext().getResources().getDisplayMetrics().density;
        right = (int) (right * density);
        left = (int) (left * density);
        top = (int) (top * density);
        bottom = (int) (bottom * density);
    view.setPadding(left, top, right, bottom);
}
    public void get_Recipient_goals(){

        try {

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            File fileDir = null;
            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = new File(fileDir, "Recipient_Goals.xml");

            lst_Recipient_goals= new ArrayList<Recipient_Goals>();
            list_gen= new ArrayList<String>();
            Recipient_Goals rpg;

            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Recipient_Goals");
                TextView txtShiftgoals = findViewById(R.id.txtShiftgoals);

                if (nList == null) return;

                int n=nList.getLength();
                if (n<=0) return;


                for (int temp = 0; temp < n; temp++) {

                    try {
                        Node nNode = nList.item(temp);


                        String value = "";
                        rpg= new Recipient_Goals();
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;

                            try {
                                value= eElement.getElementsByTagName("RecordNumber").item(0).getTextContent();
                                rpg.setRecordNumber(value);

                                value= eElement.getElementsByTagName("Name").item(0).getTextContent();
                                rpg.setName(value);
                                rpg.setGoal(value);
                                list_gen.add(value);
                                value= eElement.getElementsByTagName("Activity").item(0).getTextContent();
                                rpg.setActivity(value);
                                if (value.equals(ServiceType)){
                                    txtShiftgoals.setText(rpg.getGoal());
                                    get_Recipient_Strategies(rpg.getGoal());
                                }


                            }catch(Exception ex){}
                            lst_Recipient_goals.add(rpg);
                        }
                        if (lst_Recipient_goals.size()>0)
                            imgGoals.setVisibility(View.VISIBLE);
                        else
                            imgGoals.setVisibility(View.GONE);

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

    public void get_Recipient_Strategies(String Goal_Name){

        try {

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            File fileDir = null;
            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = new File(fileDir, "Recipient_Strategies.xml");
            lst_Recipient_Strategies=null;
            lst_Recipient_Strategies= new ArrayList<String>();
            Recipient_Strategy rps=null;
            TextView txtShiftPurpose = findViewById(R.id.txtShiftPurpose);

            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Recipient_Strategy");

                if (nList == null) return;

                int n=nList.getLength();
                if (n<=0) return;


                for (int temp = 0; temp < n; temp++) {

                    try {
                        Node nNode = nList.item(temp);


                        String value = "";
                        String goal="";
                        String Activity="";
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;

                            goal = eElement.getElementsByTagName("Name").item(0).getTextContent();

                            if (goal.equalsIgnoreCase(Goal_Name)) {
                                rps= new Recipient_Strategy();
                                try {

                                    value = eElement.getElementsByTagName("Strategy").item(0).getTextContent();
                                    rps.setStrategy(value);
                                    lst_Recipient_Strategies.add(value);

                                    Activity = eElement.getElementsByTagName("Activity").item(0).getTextContent();

                                    if (Activity.equals(ServiceType)){
                                        txtShiftPurpose.setText(rps.getStrategy());
                                    }

                                } catch (Exception ex) {
                                }

                            }
                            }

                    } catch (Exception aE) {
                        // ((Button) findViewById(R.id.txtAddress2)).setText(aE.toString());
                    }
                } // loop end
                if (lst_Recipient_Strategies.size()>1) {
                    img_view_Strategy.setVisibility(View.VISIBLE);

                    //  setPadding(txtShiftPurpose,15,0,170,0);
                }else {
                    img_view_Strategy.setVisibility(View.GONE);
                    // setPadding(txtShiftPurpose,15,0,10,0);
                }

            }
        } catch (Exception aE) {
            //  ((Button) findViewById(R.id.txtAddress2)).setText(aE.toString());
        }



    }
}
