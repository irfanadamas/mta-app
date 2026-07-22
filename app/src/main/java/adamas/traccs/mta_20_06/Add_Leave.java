package adamas.traccs.mta_20_06;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.os.Bundle;
import android.text.InputType;
import android.text.format.Time;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Add_Leave extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private final String NAMESPACE = "https://tempuri.org/";
    ArrayList<String> lst_leave = null;
    String selected_val;
    File froot;
    Context context;

    Email_Settings email_seting = null;
    Email email;
    String StaffCode="",Staff_Coordinator_Email="";
    String email_msg="",Email_Subject="";
    SharedPreferences settings = null;
    final String PREFS_NAME = "MTAPrefs";
    String root;
    Boolean Server_Available=false;
    String OperatorId, Security_Token;
    String RosterDate="";
    String selectedItem="";
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
                imageBack.setVisibility(View.GONE);
                actionBar.setDisplayHomeAsUpEnabled(true);

                // imageMenu.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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

            int gravity =   spinner.getGravity();

            params.horizontalMargin = 0;
            params.verticalMargin=0;
           // params.gravity = gravity;// Gravity.getAbsoluteGravity(gravity,Gravity.TOP);
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

    public void set_Spinner_old(Context view, final TextView spinner, final List<String> lst) {
        final Dialog dialog = new Dialog(view,R.style.CustomDialog);
        dialog.setContentView(R.layout.spinner_list);
        //dialog.setTitle("My Dashboard");
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.x = 0;   //x position
        lp.y = 0;   //y position
        dialog.getWindow().setAttributes(lp);

        Window window = dialog.getWindow();
        if (window != null) {
            window.getAttributes().x=0;//(int) TextDate.getTop();
            window.getAttributes().y =0;

            WindowManager.LayoutParams params = window.getAttributes();
          //  params.width =  500;
            params.height = lst.size()*110;

         //   params.horizontalMargin = 0;
         //   params.verticalMargin=0;
            int gravity = spinner.getGravity();

            params.gravity = gravity; //Gravity.RIGHT | Gravity.CENTER_HORIZONTAL;
           // params.gravity = Gravity.RIGHT;
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

       /* EditText txtLeaveNote=findViewById(R.id.txtLeaveNote);
        txtLeaveNote.clearFocus();
        txtLeaveNote.setSelected(false);
       // txtLeaveNote.setFocusable(false);

        Spinner cmbLeaveType=findViewById(R.id.cmbLeaveType);
        cmbLeaveType.requestFocus();*/

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
    void setMultiLineCapSentencesAndDoneAction(EditText txt) {
        txt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        txt.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        txt.setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES  );
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__leave);
        setupActionBar();
        context=this.getApplicationContext();
        set_serverIp();
          final EditText txtRosterNote= (EditText)findViewById(R.id.txtLeaveNote);
        final TextView spinner1=  findViewById(R.id.cmbLeaveType);
       // setMultiLineCapSentencesAndDoneAction(txtRosterNote);

        try {
            settings = getSharedPreferences(PREFS_NAME, 0);
            // setNeverSleepPolicy();
        } catch (Exception ex) {
        }


        try{

            //lst_incident= Get_Incident_Locations();

            getLeaves();

           // lst_leave.add(0,"Select an option");
            if (lst_leave.size ()>0 ){
                // Toast.makeText(getApplicationContext(), "lst_incident=" + lst_incident.size(), Toast.LENGTH_LONG).show();

               // ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lst_leave);
               // adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
              //  spinner1.setAdapter(adapter1);

              /*  spinner1.setOnItemSelectedListener(this);
                CustomAdapter customAdapter=new CustomAdapter(this,lst_leave);
                spinner1.setAdapter(customAdapter);*/

            }

        }catch(Exception ex){}

        spinner1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lst_leave.size()>0)
               //     set_Spinner_old(v.getContext(),spinner1,lst_leave);
                set_Spinner(v.getContext(),spinner1,lst_leave,"Leave Type");


            }


        });
        Button dialogButton = (Button) findViewById(R.id.btnSettings);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
               finish();
            }
        });

/*
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?>arg0, View view, int arg2, long arg3) {

                selected_val=spinner1.getSelectedItem().toString();

                //Toast.makeText(getApplicationContext(), selected_val , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
*/

        final Button  dtpStart=(Button)findViewById(R.id.dtpStart);
        final Button  dtpEnd=(Button)findViewById(R.id.dtpEnd);

        final   DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {


                int day2 = view.getDayOfMonth();
                int month2 = view.getMonth() + 1;
                int year2 = view.getYear();

                dtpStart.setTag(  year2 + "/" + set_leading_zero( month2,2) + "/"+ set_leading_zero(day2,2));
                dtpStart.setText( set_leading_zero(day2,2)   + "/" + set_leading_zero( month2,2) + "/" + year2);



            }
        };

        final   DatePickerDialog.OnDateSetListener d2 = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                int day2 = view.getDayOfMonth();
                int month2 = view.getMonth() + 1;
                int year2 = view.getYear();

                dtpEnd.setTag(  year2 + "/" + set_leading_zero( month2,2) + "/"+ set_leading_zero(day2,2));
                dtpEnd.setText( set_leading_zero(day2,2)   + "/" + set_leading_zero( month2,2) + "/" + year2);



            }
        };

        final Calendar myCalendar = Calendar.getInstance();
        dtpStart.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                /*new DatePickerDialog(Add_Leave.this, d, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
*/
                int y,m,d;
                y=Integer.parseInt(RosterDate.substring(0,4));
                m=Integer.parseInt(RosterDate.substring(5,7));
                d=Integer.parseInt(RosterDate.substring(8,10));

                DatePickerDialog dpt=  new DatePickerDialog(Add_Leave.this,R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //  makeWrapContent(view);
                        int day2 = view.getDayOfMonth();
                        int month2 = view.getMonth() +1;
                        int year2 = view.getYear();
                        dtpStart.setTag(  year2 + "/" + set_leading_zero( month2,2) + "/"+ set_leading_zero(day2,2));
                        dtpStart.setText( set_leading_zero(day2,2)   + "/" + set_leading_zero( month2,2) + "/" + year2);

                    }
                }, y, m-1, d+1);

                dpt.show();
            }
        });

        dtpEnd.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                /*new DatePickerDialog(Add_Leave.this, d2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
*/
                int y,m,d;
                y=Integer.parseInt(RosterDate.substring(0,4));
                m=Integer.parseInt(RosterDate.substring(5,7));
                d=Integer.parseInt(RosterDate.substring(8,10));

                DatePickerDialog dpt=  new DatePickerDialog(Add_Leave.this,R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //  makeWrapContent(view);
                        int day2 = view.getDayOfMonth();
                        int month2 = view.getMonth() +1;
                        int year2 = view.getYear();
                        dtpEnd.setTag(  year2 + "/" + set_leading_zero( month2,2) + "/"+ set_leading_zero(day2,2));
                        dtpEnd.setText( set_leading_zero(day2,2)   + "/" + set_leading_zero( month2,2) + "/" + year2);

                    }
                }, y, m-1, d+1);

                dpt.show();
            }

        });

        Button dialogSave = (Button) findViewById(R.id.btnSave);
        // if button is clicked, close the custom dialog
        dialogSave.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // String Note_Type ="RECIMNOTE";



                String notes=txtRosterNote.getText().toString();

               // Spinner   cmbLeaveType= (Spinner)findViewById(R.id.cmbLeaveType);
                Button   dtpStart= (Button)findViewById(R.id.dtpStart);
                Button   dtpEnd= (Button)findViewById(R.id.dtpEnd);

                if (dtpStart.getText().toString().equalsIgnoreCase("-")){
                    Toast.makeText(Add_Leave.this, "Invalid Start Date", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dtpEnd.getText().toString().equalsIgnoreCase("-")){
                    Toast.makeText(Add_Leave.this, "Invalid End Date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (txtRosterNote.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(Add_Leave.this, "Please enter valid leave note", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedItem.equalsIgnoreCase("Select an option") || selectedItem.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Please select valid leave type", Toast.LENGTH_LONG).show();
                    return;
                }
                String format="yyyy/MM/dd";

                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());

                int leadTime= Integer.parseInt(settings.getString("LeaveLeadTime","1"));
                Calendar cal= Calendar.getInstance();

                cal.setTime(new Date()); // Now use today date.
                cal.add(Calendar.DATE, leadTime); // Adding 5 days
                Date todate= cal.getTime();
                // Toast.makeText(getApplicationContext(), selected_val + "\n" + spinnerIncidentType.getSelectedItem().toString() , Toast.LENGTH_LONG).show();



                Date dt=null, dt2=null;
                String Start_Date="";
                String End_Date="";

                try {
                    Start_Date = dtpStart.getTag().toString();

                    dt = sdf.parse(Start_Date);


                }catch(Exception ex){}


                if (dt.before(todate)){
                    Toast.makeText(getApplicationContext(), "Invalid Leave Dates, Only Leave can be added after " + todate, Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    Start_Date = dtpStart.getTag().toString();
                    End_Date = dtpEnd.getTag().toString();

                    dt = sdf.parse(Start_Date);
                    // Start_Date=sdf.format(dt.getDate());

                    // End_Date = dtpEnd.getTag().toString();// sdf.format();;
                    dt2 = sdf.parse(End_Date);
                    //  End_Date=sdf.format(dt2.getDate());

                }catch(Exception ex){}


                if (dt2.before(dt)){
                    Toast.makeText(getApplicationContext(), "Invalid Leave Dates, please enter valid Dates", Toast.LENGTH_LONG).show();
                    return;
                }

                //  notes.replace("\'", "\'\'");
                notes=SQLSafe(notes);

                try{
                    //  Add_Leave(selected_val,Start_Date,End_Date, notes,"");
                    //  Toast.makeText(getApplicationContext(), "Staff: " + StaffCode, Toast.LENGTH_LONG).show();
                   // int i=spinner1.getSelectedItemPosition();
                    String Leave_type= selectedItem ;//lst_leave.get(i);

                    new Add_Leave.MyAsyncClass_Leave().execute(Leave_type + "`" + notes,Start_Date + "`" + End_Date);


                    try
                    {
                        String messgas="Leave Application submitted by  \"" + StaffCode + "\" :\n\n" + "Leave_Type : " + Leave_type + "\nFrom : " + Start_Date + " to "+ End_Date + "\n" + notes;
                        String title="Leave Application for \"" + StaffCode+ "\"\n" ;

                        // send_email_alert(messgas,title);

                    }catch(Exception ex){}


                    // Toast.makeText(getApplicationContext(), "Leave Application added Successfully", Toast.LENGTH_LONG).show();

                }catch(Exception ex){	Toast.makeText(getApplicationContext(), "Issue in Adding Leave", Toast.LENGTH_LONG).show(); }

               

            }
        });


    }
    void set_serverIp(){
        Bundle b= getIntent().getExtras();
        root=b.getString("root");
        StaffCode=b.getString("StaffCode");
        Staff_Coordinator_Email=b.getString("Staff_Coordinator_Email");
        Server_Available=b.getBoolean("Server_Available");

        OperatorId=b.getString("OperatorId");
        Security_Token=b.getString("Security_Token");
        RosterDate=b.getString("RosterDate");



    }
    String getSecurityToken(){

        String Val= OperatorId  + "$" + Security_Token + "$";
        return Val;
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
                Document doc = dBuilder.parse(fXmlFile);
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
    private String set_leading_zero(int val, int size) {
        String new_val = String.valueOf(val);
        size = size - (new_val.length());
        for (int i = 0; i < size; i++) {
            new_val = "0" + new_val;
        }
        return new_val;
    }

    public void send_email_alert(String msg, String subject) {

        if (settings.getString("StaffLeaveEmailOverrides","false").equalsIgnoreCase("true")){
            if(!settings.getString("StaffLeaveEmail","").equals(""))
                Staff_Coordinator_Email=settings.getString("StaffLeaveEmail","");
        }

        email_msg = subject + msg;
        Email_Subject = subject;
        //Toast.makeText(getApplicationContext(), email_seting.getSMTPServer() + "\n" + email_seting.getSMTPUser() + "\nPassword :" + email_seting.getSMTPPassword(), Toast.LENGTH_LONG).show();

        //Toast.makeText(getApplicationContext(), "Sending Email from SMTP Server :" + email_seting.getSMTPServer(), Toast.LENGTH_LONG).show();


        //Toast.makeText(getApplicationContext(),"Cordinator_Email : " + Cordinator_Email, Toast.LENGTH_LONG).show();
        //  Toast.makeText(getApplicationContext(),Cordinator_Email + "\n" + email_msg , Toast.LENGTH_LONG).show();

        try {
            if ( !Staff_Coordinator_Email.equals("") && settings.getString("AppUsesSMTP","false").equalsIgnoreCase("true"))
            {
                try{
                    //Cordinator_Email="arshadblouch81@yahoo.com";
                    //  email.sendMail(email_seting.getFromDisplayName(), email_msg, email_seting.getFromEmail(),To_Emails);
                    email.sendMail(Email_Subject, email_msg, email_seting.getFromEmail(),Staff_Coordinator_Email);

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
            composeEmail(Staff_Coordinator_Email,subject,email_msg);
            if(1==1) return;
            String[] TO = {Staff_Coordinator_Email};


            Uri uri = Uri.parse("mailto:" )
                    .buildUpon()
                    .appendQueryParameter("subject", subject)
                    .appendQueryParameter("body", email_msg)
                    .build();
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
           // emailIntent.setType("text/plain");
            //emailIntent.setData( Uri.parse(Staff_Coordinator_Email));
            try{
             //   startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }catch (Exception ex) {}
     //       composeEmail(TO,subject);
            Toast.makeText(getApplicationContext(), "Staff_Coordinator_Email: " +Staff_Coordinator_Email , Toast.LENGTH_LONG).show();

            Intent Email = new Intent(Intent.ACTION_SEND);
            Email.setType("text/email");
            Email.putExtra(Intent.EXTRA_EMAIL, new String[] { Staff_Coordinator_Email });
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class MyAsyncClass33 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Add_Leave.this);
            pDialog.setMessage("Please wait while Processing  Email Alert....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {


            try {

                if (settings.getString("StaffLeaveEmailOverrides","false").equalsIgnoreCase("true")){
                    if(!settings.getString("StaffLeaveEmail","").equals(""))
                        Staff_Coordinator_Email=settings.getString("StaffLeaveEmail","");
                }

                if ( !Staff_Coordinator_Email.equals("") && settings.getString("AppUsesSMTP","false").equalsIgnoreCase("true"))     {

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
           // pDialog.cancel();
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

            pDialog = new LoadingDialog(Add_Leave.this);
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
                Toast.makeText(getApplicationContext(), "Leave Application added Successfully" , Toast.LENGTH_LONG).show();
                try
                {

                    //  sendAutoEmail("arshadblouch81@gmail.com","dahsra`123","arshadblouch81@gmail.com",title,messgas);

                    //if(Server_Available)
                    if (settings.getString( "EnableEmailNotification","true").equalsIgnoreCase("true")
                            || settings.getString( "EnableEmailNotification","true").equalsIgnoreCase("1"))

                        send_email_alert(messgas,title);

                    finish();

                }catch(Exception ex){ Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();}
            }else
                Toast.makeText(getApplicationContext(), "Unable to add Leave Application ", Toast.LENGTH_LONG).show();
        }
    }
    void send_local_email(){

        String subject = Email_Subject;

        if (settings.getString("StaffLeaveEmailOverrides","false").equalsIgnoreCase("true")){
            if(!settings.getString("StaffLeaveEmail","").equals(""))
                Staff_Coordinator_Email=settings.getString("StaffLeaveEmail","");
        }


        try{
            if (!Staff_Coordinator_Email.equals("") || Staff_Coordinator_Email!=null){

               /* Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, email_msg);
                intent.setData(Uri.parse("mailto:"+Staff_Coordinator_Email)); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                // MainActivity_Navigation.this.startActivity(Intent.createChooser(intent, "Send mail..."));
                startActivity(intent);*/
               // subject=subject+Staff_Coordinator_Email;
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

            SoapPrimitive result=null;

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

}
