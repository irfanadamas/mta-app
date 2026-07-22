package adamas.traccs.mta_20_06;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;

import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Xml;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.text.HtmlCompat;

public class Record_Incident extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private final String NAMESPACE = "https://tempuri.org/";
    String root;
    String  RecordNo;
     String  ServiceType2  ;
     String  Service_Setting2  ;
     String Program2;
     String AccountNo2;
     File froot;
    Context context;
     Roster_Info Current_roster;
     Boolean Server_Available;
     String StaffCode;
    String Security_Token;
    String OperatorId;
    String Cordinator_Email;

    String Staff_Coordinator_Email;
    final String PREFS_NAME = "MTAPrefs";
    SharedPreferences settings = null;
    Email   email;
    Email_Settings email_seting = null;
    String email_msg;
    String Email_Subject;
    String PersonId;
    int index=0;
    Group_Recipient grp;
    List<String> lst_incident = null;
    List<String> lst_Recipients = null;
    List<String> lst_inccident_Types = null;
    List<String> lst_severity=null;
    String IncidentType,IncidentSeverity,IncidentLocation;
    String notes="";
    String IncidentSummary="";
    boolean No_Recipient = false;
    TextView spinnerIncidentType ;
    TextView spinnerIncidentSeverity ;
    TextView spinnerIncidentLocation ;
    String selectedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record__incident);
        setupActionBar();
        context=this.getApplicationContext();
       settings = getSharedPreferences(PREFS_NAME, 0);

       Set_Incident();
        EditText txtAction= findViewById(R.id.txtAction);
        EditText txtOutCome= findViewById(R.id.txtOutCome);

       EditText txtRosterNote= findViewById(R.id.txtRosterNote);
       EditText txtIncidentSummary = findViewById(R.id.txtIncidentSummary);

//        setMultiLineCapSentencesAndDoneAction(txtRosterNote);
//        setMultiLineCapSentencesAndDoneAction(txtIncidentSummary);
//        setMultiLineCapSentencesAndDoneAction(txtAction);
//        setMultiLineCapSentencesAndDoneAction(txtOutCome);

        Button btnExit = (Button) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
         spinnerIncidentType = (TextView) findViewById(R.id.spinnerIncidentType);
         spinnerIncidentSeverity = (TextView) findViewById(R.id.spinnerIncidentSeverity);
         spinnerIncidentLocation = (TextView) findViewById(R.id.spinnerIncidentLocation);

        spinnerIncidentType.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                set_Spinner(v.getContext(),spinnerIncidentType,lst_inccident_Types,"Incident Type");

            }
        });
        spinnerIncidentSeverity.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_Spinner(v.getContext(),spinnerIncidentSeverity,lst_severity, "Incident Severity");

            }
        });
        spinnerIncidentLocation.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_Spinner(v.getContext(),spinnerIncidentLocation,lst_incident,"Incident Location");
            }
        });
        Button btnSave = (Button) findViewById(R.id.btnOK);
        btnSave.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // String Note_Type ="RECIMNOTE";
                EditText txtRosterNote = (EditText) findViewById(R.id.txtRosterNote);
                 notes = txtRosterNote.getText().toString();
                EditText txtIncidentSummary = (EditText) findViewById(R.id.txtIncidentSummary);
                 IncidentSummary= txtIncidentSummary.getText().toString();

                CheckBox chk = (CheckBox) findViewById(R.id.chkNoRecipientInvloved);



                //if (!check_valid_note(notes)){
               if (notes.length()<=0 )
                {
                    Toast.makeText(getApplicationContext(), "Please enter valid Incident Note", Toast.LENGTH_LONG).show();

                    txtRosterNote.setError("Please fill field");
                    return;
                }
                if (IncidentSummary.length()>50 || IncidentSummary.length()<=0)
                {
                    Toast.makeText(getApplicationContext(), "Please enter valid Incident Summary, It must not be greater than 50 chars", Toast.LENGTH_LONG).show();
                    txtIncidentSummary.setError("Please fill valid entry");
                    return;
                }
                if (false) {

                    // Toast.makeText(getApplicationContext(), "Please enter valid Note, You have used some invalid characters", Toast.LENGTH_LONG).show();
                    //  return;

                } else {


                    if (chk.isChecked()) {
                        No_Recipient = true;
                    }



                    //  notes.replace("\'", "\'\'");
                    notes = SQLSafe(notes);
                    IncidentSummary = SQLSafe(IncidentSummary);


                 //   Group_Recipient_Rocord_Update=true;
                try {


                    IncidentType= spinnerIncidentType.getText().toString();

                     //i=spinnerIncidentSeverity.getSelectedItemPosition();
                    IncidentSeverity=  spinnerIncidentSeverity.getText().toString();

                    //i=spinnerIncidentLocation.getSelectedItemPosition();
                    IncidentLocation=spinnerIncidentLocation.getText().toString();

                    if (IncidentType.equalsIgnoreCase("Select an option") || IncidentType.isEmpty()){
                        Tost_Message("Please Select valid Incident Type");
                        return;
                    }
                    if (IncidentSeverity.equalsIgnoreCase("Select an option") || IncidentSeverity.isEmpty()){
                        Tost_Message("Please Select valid Incident Severity");
                        return;
                    }
                    if (IncidentLocation.equalsIgnoreCase("Select an option") || IncidentLocation.isEmpty()){
                        Tost_Message("Please Select valid Incident Location");
                        return;
                    }

                    try{
                        new MyAsyncClass5().execute();
                    }catch(Exception ex){}
                    //	Toast.makeText(getApplicationContext(), "Cleint Incident added Successfully", Toast.LENGTH_LONG).show();

                }catch(Exception ex){
                    Toast.makeText(Record_Incident.this, ex.toString(), Toast.LENGTH_SHORT).show();
                }
                }
            }
        });


    }
    public void set_Updates(String command ) throws IOException {
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
                Document doc = dBuilder.parse(fXmlFile);
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
                    } catch (Exception ex) {
                    }
                }
            }

        } catch (Exception ex) {
        }

        return rst;
    }
    String getSecurityToken(){

        String Val= OperatorId  + "$" + Security_Token + "$";
        return Val;
    }

    public void Set_Incident() {


         lst_incident = new ArrayList<String>();
         lst_Recipients = new ArrayList<String>();
         lst_inccident_Types = new ArrayList<String>();

        Bundle bundle= getIntent().getExtras();
        try {
            index = bundle.getInt("Index");
        }catch(Exception ex){index=0;}

        RecordNo   = bundle.getString("RecordNo");
        PersonId= bundle.getString("PersonId");
        StaffCode=settings.getString("StaffCode","ABC");
        Server_Available=settings.getBoolean("Server_Available",false);
        Security_Token=settings.getString("Security_Token","");
        OperatorId=settings.getString("OperatorId","");
        Staff_Coordinator_Email=settings.getString("Staff_Coordinator_Email","");

        root=settings.getString("root","");
        AccountNo2= bundle.getString("AccountNo");
        Service_Setting2= bundle.getString("Service_Setting");
        ServiceType2= bundle.getString("ServiceType");
        Program2= bundle.getString("Program");
        Cordinator_Email= bundle.getString("Cordinator_Email");

        if(Cordinator_Email == null){

            Cordinator_Email = "";
        }

        /*  Current_roster= getRoster(RecordNo);
        grp=Current_roster.get_group_Recipients().get(index);
        if (Current_roster==null) return;
        if (grp!=null)
            AccountNo2=grp.getAccountNo();
        else
            AccountNo2=Current_roster.getActual_Client_Code();

        Service_Setting2=Current_roster.getServiceSetting();
        ServiceType2= Current_roster.getServiceType();
        Program2=Current_roster.getProgram();
*/
        try {
             lst_severity = new ArrayList<String>();

            lst_severity.add("LOW");
            lst_severity.add("MEDIUM");
            lst_severity.add("HIGH");
  //          lst_severity.add(0,"Select an option");

            TextView spinnerIncidentLocation = (TextView) findViewById(R.id.spinnerIncidentSeverity);

           // spinnerIncidentLocation.setOnItemSelectedListener(this);

          //  CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), lst_severity);
        //    spinnerIncidentLocation.setAdapter(customAdapter);

        } catch(Exception ex){}

        try {

            lst_incident = Get_Incident_Locations();
            lst_inccident_Types = Get_Incident_Types();

            if (lst_incident.size() > 0) {

//                lst_incident.add(0,"Select an option");
                // Toast.makeText(getApplicationContext(), "lst_incident=" + lst_incident.size(), Toast.LENGTH_LONG).show();

          //      ArrayAdapter adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lst_incident);
            //    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
              //  Spinner spinnerIncidentSeverity = (Spinner) findViewById(R.id.spinnerIncidentLocation);
              //  spinner1.setAdapter(adapter1);

      //          spinnerIncidentSeverity.setOnItemSelectedListener(this);

        //        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),lst_incident);
    //            spinnerIncidentSeverity.setAdapter(customAdapter);

            }

            if (lst_inccident_Types.size() > 0) {
                // Toast.makeText(getApplicationContext(), "lst_incident=" + lst_incident.size(), Toast.LENGTH_LONG).show();
                /*lst_inccident_Types.add(0,"Select an option");
                Spinner spinner12 = (Spinner) findViewById(R.id.spinnerIncidentType);

                //ArrayAdapter adapter12 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lst_inccident_Types);
                //adapter12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //spinner12.setAdapter(adapter12);

                spinner12.setOnItemSelectedListener(this);

                CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),lst_inccident_Types);
                spinner12.setAdapter(customAdapter);*/
            }
            TextView txtRecipient= findViewById(R.id.txtRecipient);

            txtRecipient.setText(HtmlCompat.fromHtml("<b>Recipient</b><br>" + AccountNo2,0));

           /* if ( AccountNo2.equalsIgnoreCase("!MULTIPLE")) {
                for(int i=0; i<Current_roster.get_group_Recipients().size(); i++)
                    lst_Recipients.add(Current_roster.get_group_Recipients().get(i).getAccountNo());
            }else{
                lst_Recipients.add(AccountNo2);
            }*/






        } catch (Exception ex) {
        }


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
    public String SQLSafe(String sValue) {

        String sAns = null;
        sAns = sValue.replace("'", "''");
        sAns = sAns.replace("\"", "''");
        return (sAns);

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
                String messgas = "Client Incident added by  \"" + StaffCode + "\" :\n\n" + "Incident Type : " + Incident_Type + "\nSeverity : " + Incident_Severity + " \nLocation : " + Location + " \nService : " + Service + "\nIncident Summary : " + IncidentSummary + "\nIncident Note : " + Note;

                String title = "Client Incident for \"" + AccountNo + "\"\n";
                Tost_Message("Client Incident added Successfully");

                send_local_email(title,messgas);

            } catch (Exception ex) {
            }

        } catch (Exception ex) {
            Tost_Message("Operation not done due to some error\n" + ex.toString());
        }
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
    public void Add_Incident(String Incident_Type, String Service, String Incident_Severity, String Location, String Note, boolean No_Recipient,String OperatorID,String Personid, String AccountNo, String Program, String IncidentSummary) {

        Server_Available= isOnline(getApplicationContext());
        if (!Server_Available) { // call in both cases
            Add_Incident2(Incident_Type, Service, Incident_Severity, Location, Note, No_Recipient, StaffCode,Personid,OperatorID, AccountNo, Program,IncidentSummary);
            return;
        }

        String URL41 = root + "/TimeSheet.asmx?op=Add_Incident";
        String SOAP_ACTION41 = "https://tempuri.org/Add_Incident";
        String METHOD_NAME41 = "Add_Incident";

        // TextView txtmsg = ((TextView) findViewById(R.id.textMsg));f
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
            String IncidentNo=result.toString();

            if (Integer.parseInt(result.toString()) > 0) {

                Tost_Message( "Client Incident added Successfully");

                try {

                  //  EditText txtAction= findViewById(R.id.txtAction);
                  //  EditText txtOutCome= findViewById(R.id.txtOutCome);

                  //  String messgas = "Client Incident added by  \"" + StaffCode + "\" :\n\n" + "Incident Type : " + Incident_Type + "\Severity  : " + Incident_Severity + " \nLocation : " + Location + " \nService : " + Service +  "\n" + IncidentSummary + "\n" + Note;
                    String title = "Client Incident for \"" + AccountNo + "\"\n";

                    String messgas = "Client Incident added by  \"" + StaffCode + "\" :\n\n" + "Incident Type : " + Incident_Type + "\nSeverity : " + Incident_Severity + " \nLocation : " + Location + " \nService : " + Service + "\nIncident Summary : " + IncidentSummary + "\nIncident Note : " + Note;

                    if (No_Recipient || Cordinator_Email.equals("")) {
                        // tmp_cor_email=Cordinator_Email;
                        Cordinator_Email=Staff_Coordinator_Email;
                    }

                    String Staff_Emails= get_Incident_Staff_Emails(IncidentNo);
                    if (!Staff_Emails.equals(""))
                        Staff_Coordinator_Email=Staff_Coordinator_Email + "," + Staff_Emails;


                    if (settings.getString( "EnableEmailNotification","true").equalsIgnoreCase("true")
                            || settings.getString( "EnableEmailNotification","true").equalsIgnoreCase("1"))

                        send_email_alert(messgas,title);



                } catch (Exception ex) {
                    Tost_Message("Operation not done due to some server error\n" + ex.toString());
                }


            } else
                Tost_Message("Client Incident not added due to some problem - " + AccountNo + " Result=" + result.toString());



        } catch (Exception ex) {
            Tost_Message("Operation not done due to some server error\n" + ex.toString());
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

    public void send_email_alert(String msg, String subject) {


        email_msg = subject + msg;
        Email_Subject = subject;

        if (settings.getString( "UserOverrideIncidentEmail","true").equalsIgnoreCase("true"))
            Cordinator_Email=settings.getString( "OverrideIncientEmail",Cordinator_Email);


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

        try{
            if (settings.getString( "UserOverrideIncidentEmail","true").equalsIgnoreCase("true"))
                Cordinator_Email=settings.getString( "OverrideIncientEmail",Cordinator_Email);
            //Tost_Message(Cordinator_Email + "," + settings.getString( "UserOverrideIncidentEmail","true") + ", " + settings.getString( "OverrideIncientEmail","-"));
            composeEmail(Cordinator_Email,subject,email_msg);
            if(1==1) return;


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
    void send_local_email_old(String subject, String email_msg){

        try{

         /*   String body = "<h1>"+ email_msg + "</h1";

            ShareCompat.IntentBuilder.from(Record_Incident.this)
                    .setType("message/rfc822")
                    .addEmailTo(Cordinator_Email)
                    .setSubject(subject)
                    .setText(body)
                    //.setHtmlText(body) //If you are using HTML in your body text
                    .setChooserTitle(subject)
                    .startChooser();

            if (1==1) return;*/

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
    void setMultiLineCapSentencesAndDoneAction(EditText txt) {
        txt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        txt.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        txt.setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES  );
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        /*switch (view.getId()) {


            case R.id.spinnerIncidentType:
               Spinner spinnerIncidentType = findViewById(R.id.spinnerIncidentType);
                if (!spinnerIncidentType.getSelectedItem().toString().equalsIgnoreCase("Select an option")
                && spinnerIncidentType.getItemAtPosition(0).toString().equalsIgnoreCase("Select an option"))
                spinnerIncidentType.removeViewAt(0);
                break;
            case R.id.spinnerIncidentSeverity:
                Spinner spinnerIncidentSeverity = findViewById(R.id.spinnerIncidentSeverity);
                if (!spinnerIncidentSeverity.getSelectedItem().toString().equalsIgnoreCase("Select an option")
                        && spinnerIncidentSeverity.getItemAtPosition(0).toString().equalsIgnoreCase("Select an option"))
                    spinnerIncidentSeverity.removeViewAt(0);


            break;

            default:

                break;
        }
*/

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class MyAsyncClass33 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Record_Incident.this);
            pDialog.setMessage("Please wait while Processing  Email Alert....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {


            try {



                if ( !Staff_Coordinator_Email.equals("") && settings.getString("AppUsesSMTP","").equalsIgnoreCase("true"))     {

                    try{

                        //  email.sendMail(email_seting.getFromDisplayName(), email_msg, email_seting.getFromEmail(),To_Emails);
                        email.sendMail(Email_Subject, email_msg, email_seting.getFromEmail(),Staff_Coordinator_Email);
                        //   send_local_email();
                    } catch (Exception e) {
                        //Toast.makeText(getApplicationContext(),"SMTP Server not working" , Toast.LENGTH_LONG).show();
                     //   send_local_email(sub);
                    }
                }else{
                    //send_local_email();
                }




            }catch (Exception ex) {
              //  send_local_email();
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
                Document doc = dBuilder.parse(fXmlFile);
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
                        Toast.makeText(getApplicationContext(), "Error in Incident getting Locations ", Toast.LENGTH_LONG).show();
                    }


                }
            }

        } catch (Exception aE) {
            Toast.makeText(getApplicationContext(), "Error in Incident getting Locations ", Toast.LENGTH_LONG).show();
        }

        return lst_inscdt;
    }
    public String get_Incident_Staff_Emails(String IncidentNo) {

        String URL6 = root + "/TimeSheet.asmx?op=get_Incident_Staff_Emails";
        String SOAP_ACTION6 = "https://tempuri.org/get_Incident_Staff_Emails";
        String METHOD_NAME6 = "get_Incident_Staff_Emails";


        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME6);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug = true;

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("IncidentNo");
            pi2.setValue(getSecurityToken() + IncidentNo);
            request.addProperty(pi2);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION6, envelope);

            SoapObject obj = (SoapObject) envelope.getResponse();
            SoapPrimitive result = (SoapPrimitive) obj.getProperty("Login");

            return  result.toString();

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
        }

        return "";
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
                Document doc = dBuilder.parse(fXmlFile);
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
                        Toast.makeText(getApplicationContext(), "Error in Incident getting Locations ", Toast.LENGTH_LONG).show();
                        return lst_inscdt;
                    }


                }
            }

        } catch (Exception aE) {
            Toast.makeText(getApplicationContext(), "Error in Incident getting Locations ", Toast.LENGTH_LONG).show();
        }

        return lst_inscdt;
    }
    class MyAsyncClass5 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;
        String Notee="";
        String Note_Typee="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Record_Incident.this);
            pDialog.setMessage("Please wait while Processing  ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {

                Add_Incident(IncidentType, ServiceType2, IncidentSeverity, IncidentLocation, notes, No_Recipient, OperatorId, PersonId, AccountNo2, Program2, IncidentSummary);


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
               /* String messgas = "The following "+ Note_Typee+" note has been added to client \"" + AccountNo + "\" by \"" + StaffCode + "\" :\n\n" + Notee;
                String title = "  TRACCS Client Note Added for : " + AccountNo + "\n";

                // if(Server_Available)
                send_local_email(title,messgas);
                //send_email_alert(messgas, title);*/
                finish();

            } catch (Exception ex) {}

            try{
                //  new MyAsyncClass4().execute(result,Main_Op_Note);

            }catch (Exception ex) {}
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
    public void set_Spinner_full_screen(Context view, final TextView spinner, final List<String> lst, String Title) {
        final Dialog dialog = new Dialog(view);
        dialog.setContentView(R.layout.spinner_list_with_title);
        //dialog.setTitle("My Dashboard");
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);

        Window window = dialog.getWindow();

        if (window != null) {
            window.setBackgroundDrawableResource(R.color.shidt_detail_color);
            window.getAttributes().x=0;//(int) TextDate.getTop();
            window.getAttributes().y =0;

            WindowManager.LayoutParams params = window.getAttributes();
            params.width =  WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;

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
    public void set_Spinner2(Context view, final TextView spinner, final List<String> lst) {
        final Dialog dialog = new Dialog(view,R.style.CustomDialog);
        dialog.setContentView(R.layout.spinner_list);
        //dialog.setTitle("My Dashboard");
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        WindowManager wm = (WindowManager) view.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        Window window = dialog.getWindow();

        if (window != null) {
            window.setBackgroundDrawableResource(R.color.shidt_detail_color);
            ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) spinner.getLayoutParams();

            window.getAttributes().x=0;//(int) TextDate.getTop();
            window.getAttributes().y =params2.topMargin-10;

            WindowManager.LayoutParams params = window.getAttributes();
            params.width =  width; //WindowManager.LayoutParams.MATCH_PARENT;
            params.height = height/2;

            params.horizontalMargin = 0;
            params.verticalMargin=params2.topMargin;
            params.gravity = Gravity.RIGHT ;
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
    public void set_Spinner3(Context view, final TextView spinner, final List<String> lst) {
        final Dialog dialog = new Dialog(view,R.style.CustomDialog);
        dialog.setContentView(R.layout.spinner_list);
        //dialog.setTitle("My Dashboard");
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        WindowManager wm = (WindowManager) view.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.color.shidt_detail_color);
            ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) spinner.getLayoutParams();

            window.getAttributes().x=0;//(int) spinner.getTop();
            window.getAttributes().y =params2.topMargin+50;

            WindowManager.LayoutParams params = window.getAttributes();
            params.width =  width;
            params.height =height/2;

            params.horizontalMargin = 0;
            params.verticalMargin=params2.topMargin;
            params.gravity = Gravity.RIGHT | Gravity.BOTTOM;
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
}


