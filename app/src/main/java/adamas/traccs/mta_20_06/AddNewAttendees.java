package adamas.traccs.mta_20_06;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.Xml;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import android.widget.*;

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import timesheet.NetworkStateReceiver;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


public class AddNewAttendees extends AppCompatActivity implements Button.OnClickListener, NetworkStateReceiver.NetworkStateReceiverListener  {

    private ListView listView;
    File froot                                                               = null;
    ArrayList<ProgrameRecipientDC> arrProgrameRecipients                     = null;
    boolean Server_Available                                                 = true;
    public String root                                                       = "https://58.162.142.150/timesheet";
    private final String NAMESPACE                                           = "https://tempuri.org/";
    private final String METHOD_NAME                                         = "Add_Attendee";
    private String URL                                                       = root + "/TimeSheet.asmx?op=Add_Attendee";
    String OperatorId                                                        = "";
    String Security_Token                                                    = "";
    String rosterRecordNo                                                    = "";
    private final String SOAP_ACTION                                         = "https://tempuri.org/Add_Attendee";
    String accountNo                                                         = "";
    String StaffCode;
    String Existing_Attendees="";
    String ServiceType="";
    Bulk_Data bulk_data;
    Context context;
    String MobileFutureLimit="10";
    String rosterDate="";
    public static boolean data_loaded=false;
    final String PREFS_NAME = "MTAPrefs";
    SharedPreferences settings = null;
    ArrayList<ProgrameRecipientDC> arrSelectedRecipients;
    @Override
    public void networkAvailable() {

        if (Server_Available==false) {
            Toast.makeText(getApplicationContext(), "Online Connection becomes available\nRe-login the App in online mode", Toast.LENGTH_SHORT).show();
            finish();
        }

        Server_Available = true;
        /* TODO: Your connection-oriented stuff here */
    }

    @Override
    public void networkUnavailable() {
        Server_Available = false;

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       // if(1==1) return false;

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
        setContentView(R.layout.activity_add_new_attendees);

        setupActionBar();
        context=this.getApplicationContext();
        settings = getSharedPreferences(PREFS_NAME, 0);

        Bundle bundle = getIntent().getExtras();

            root = settings.getString("root","");
            set_server_Ip(root);
            OperatorId =  settings.getString("User","");
            Security_Token = settings.getString("Security_Token","");
            MobileFutureLimit= settings.getString("MobileFutureLimit","10");
            StaffCode=settings.getString("StaffCode","");

            Existing_Attendees= bundle.getString("Existing_Attendees");
            ServiceType= bundle.getString("ServiceType");
            rosterRecordNo  = bundle.getString("RecordNo");

            listView = (ListView) findViewById(R.id.listView);

        if (!data_loaded) {

            try {
                new MyAsyncClass_bulk_data2().execute();
            } catch (Exception ex) {}

        }else{

            try {
                getProgrameRecipientsFromLocalFiles();
            } catch (Exception ex) {
            }


            if (arrProgrameRecipients.size() > 0) {
                AddNewwAttendeesCellAdapter adapter = new AddNewwAttendeesCellAdapter(this, arrProgrameRecipients);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listView.setAdapter(adapter);
            }
        }
        final Button btnCancel = (Button) findViewById(R.id.btnSettings);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                finish();
            }
        });

        final Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
    }

    void set_server_Ip(String str_root) {

        URL = str_root + "/TimeSheet.asmx?op=Add_Attendee";

    }

    public void getProgrameRecipientsFromLocalFiles() throws ParserConfigurationException, SAXException {

        arrProgrameRecipients = new ArrayList();


        try {

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            File fileDir = null;
            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = new File(fileDir, "ProgramRecipients.xml");

            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Program_Recipient");


                if (nList == null) return;

                for (int temp = 0; temp < nList.getLength(); temp++) {

                    ProgrameRecipientDC   objProgrameRecipient = new ProgrameRecipientDC();
                    try {
                        Node nNode = nList.item(temp);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            objProgrameRecipient.setAccountNo(eElement.getElementsByTagName("AccountNo").item(0).getTextContent());
                            objProgrameRecipient.setClientName(eElement.getElementsByTagName("ClientName").item(0).getTextContent());
                            objProgrameRecipient.setPersonId(eElement.getElementsByTagName("PersonId").item(0).getTextContent());
                            objProgrameRecipient.setProgram(eElement.getElementsByTagName("Program").item(0).getTextContent());
                            objProgrameRecipient.setServiceType(eElement.getElementsByTagName("ServiceType").item(0).getTextContent());
                            objProgrameRecipient.setCoordinator_Email(eElement.getElementsByTagName("Coordinator_Email").item(0).getTextContent());
                            objProgrameRecipient.setAddress(eElement.getElementsByTagName("Address").item(0).getTextContent());

                            if (!Existing_Attendees.contains(objProgrameRecipient.getAccountNo()) && ServiceType.equalsIgnoreCase(objProgrameRecipient.getServiceType())){
                            //if (!Existing_Attendees.contains(objProgrameRecipient.getAccountNo()) ) {
                                arrProgrameRecipients.add(objProgrameRecipient);
                                Existing_Attendees=Existing_Attendees + "," + objProgrameRecipient.getAccountNo();
                            }
                        }


                    } catch (Exception aE) {
                        //((Button) findViewById(R.id.txtAddress2)).setText(aE.toString());
                    }
                }
            } else {

            }
        } catch (Exception aE) {

        }
    }


    @Override
    public void onClick(View view) {
        int id= (view.getId()) ;
            if (id== R.id.btnSave){
                 arrSelectedRecipients = getSelectedItems();

                if (arrSelectedRecipients==null){
                    Toast.makeText(this, "Please select some Attndeee", Toast.LENGTH_SHORT).show();
                    return;
                }
                String accountNo ="";
                for (int i = 0; i < arrSelectedRecipients.size(); ++i) {
                    ProgrameRecipientDC obj =  arrSelectedRecipients.get(i);


                   // if (i != arrSelectedRecipients.size() -1){
                    if (accountNo.equals("")){
                        accountNo=obj.getAccountNo();
                    }else{
                        accountNo=accountNo + "," + obj.getAccountNo();
                    }



                }

                if (accountNo.equals("")){

                    Toast.makeText(this, "Please select some Attndeee", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    try {

                        this.accountNo = accountNo;
                        if (!Server_Available)
                            Add_Attendee2(rosterRecordNo, this.accountNo);
                        else
                            new AddRecipientsTask().execute();
                    } catch (Exception ex) {
                    }
                }
        }
    }


    private ArrayList<ProgrameRecipientDC> getSelectedItems() {
        ArrayList<ProgrameRecipientDC> result = new ArrayList<>();
        SparseBooleanArray checkedItems = listView.getCheckedItemPositions();

        if (checkedItems==null) return result;

        for (int i = 0; i < checkedItems.size(); i++) {

            if (checkedItems.valueAt(i)) {
                result.add((ProgrameRecipientDC) listView.getItemAtPosition(checkedItems.keyAt(i)));
            }else{
                break;
            }
        }

        return result;
    }


    String getSecurityToken() {

        String Val = OperatorId + "$" + Security_Token + "$";
        return Val;
    }

    void Add_Attendee2(String RecordNo,String AccountNo){
        try {

            String command;
            // command= "\n update roster set notes='" + Note.replace("'", "''") + "' WHERE recordno = " + RecordNo;
            //command= command+ "\n update Recipients set notes='" + Note.replace("'", "''") + "' WHERE AccountNo in (select [client code] from roster where recordno = " + RecordNo + ")";

            command = "\nG`" + RecordNo + "`" + AccountNo ;


            set_Updates(command);
            try {
               // String messgas = "Client Incident added by  \"" + StaffCode + "\" :\n\n" + "Incident Type : " + Incident_Type + "\nServity : " + Incident_Severity + " \nLocation : " + Location + " \nService : " + Service + "\n" + IncidentSummary + "\n" + Note;
              //  String title = "Client Incident for \"" + AccountNo + "\"\n";

              //  send_email_alert(messgas, title);

            } catch (Exception ex) {

            }
            Toast.makeText(getApplicationContext(), "Recipients added successfully", Toast.LENGTH_LONG).show();
            finish();

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Recipients not added due to some problem", Toast.LENGTH_LONG).show();

        }
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
   public class AddRecipientsTask extends AsyncTask<Void, Void, Void> {

        Boolean isResponseSuccess = false;
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
       }

       @Override
       protected void onPostExecute(Void aVoid) {
           super.onPostExecute(aVoid);

           if (isResponseSuccess){
               Toast.makeText(getApplicationContext(), "Recipients added successfully", Toast.LENGTH_LONG).show();
              if (Server_Available)
               new MyAsyncClass3().execute();

           }else{
               Toast.makeText(getApplicationContext(), "Recipients not added due to some problem", Toast.LENGTH_LONG).show();
           }
       }

       @Override
       protected Void doInBackground(Void... voids) {


           try
           {
               String URL5 = root + "/TimeSheet.asmx?op=Add_Attendee";
               String SOAP_ACTION5 =  "https://tempuri.org/Add_Attendee";
               String METHOD_NAME5 = "Add_Attendee";

               SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME5);
               HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
               androidHttpTransport.debug =true;

               PropertyInfo pi=new PropertyInfo();
               pi.setName("RecordNo");

               pi.setValue( getSecurityToken() + rosterRecordNo);
               request.addProperty(pi);

               PropertyInfo pi2=new PropertyInfo();
               pi2.setName("AccountNo");

               pi2.setValue(accountNo);
               request.addProperty(pi2);


               SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

               envelope.dotNet = true;
               envelope.setOutputSoapObject(request);

               SoapPrimitive result = null;
               // Make the soap call.
               androidHttpTransport.call(SOAP_ACTION5, envelope);
                result = (SoapPrimitive) envelope.getResponse();

               isResponseSuccess = Integer.parseInt(result.toString()) > 0;
               // txtServer.setText(androidHttpTransport.requestDump);
           }catch(Exception ex){
               isResponseSuccess = false;
           }

           return null;
       }
   }

    class MyAsyncClass3 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new LoadingDialog(AddNewAttendees.this);
            pDialog.setMessage("Please wait while updating roster data from server  ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {


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

            finish();
            //  Toast.makeText(getApplicationContext(), "Email send", 100).show();
        }
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

    class MyAsyncClass_bulk_data2 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(AddNewAttendees.this);
            pDialog.setMessage("Please wait while loading Program Recipients ....");
            pDialog.show();
            bulk_data= new Bulk_Data(root,StaffCode,OperatorId,Security_Token,Server_Available,rosterDate,context);
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
            pDialog.cancel();
            data_loaded=true;

            listView = (ListView) findViewById(R.id.listView);
            try {
                getProgrameRecipientsFromLocalFiles();
            } catch (Exception ex) {
            }


            if (arrProgrameRecipients.size() > 0) {
                AddNewwAttendeesCellAdapter adapter = new AddNewwAttendeesCellAdapter(AddNewAttendees.this, arrProgrameRecipients);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listView.setAdapter(adapter);
            }

        }
    }
}