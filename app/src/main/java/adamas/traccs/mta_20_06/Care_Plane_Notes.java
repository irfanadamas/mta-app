package adamas.traccs.mta_20_06;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Care_Plane_Notes extends AppCompatActivity {

    String root="";
    String AccountNo;
    String EnableViewNoteCases;
     String RecordNo,Recipient,Security_Token,OperatorId,Personid,StaffCode;
    String Cordinator_Email="";
    Boolean Server_Available;
    String Main_Op_Note;
    String UseOPNoteAsShiftReport;
    String Roster_Date;
    List<String> lst_shiftgoals=null;
    final String PREFS_NAME = "MTAPrefs";
    SharedPreferences settings = null;
    String UseServiceNoteAsShiftReport;
    ArrayList<OP_Case_Note> lst_notes=null;
    File froot;
    String DATE_FORMAT_1="EEE, MMM d, yyyy";
    Context context;
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            try {
                String Title = actionBar.getTitle().toString();
                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                actionBar.setCustomView(getLayoutInflater().inflate(R.layout.action_bar_home, null),
                        new ActionBar.LayoutParams(
                                ActionBar.LayoutParams.WRAP_CONTENT,
                                ActionBar.LayoutParams.MATCH_PARENT,
                                Gravity.CENTER
                        )
                );
                TextView textviewTitle = (TextView) actionBar.getCustomView().findViewById(R.id.actionbar_textview);
                textviewTitle.setGravity(Gravity.CENTER);
                textviewTitle.setText(Title);
                actionBar.setDisplayHomeAsUpEnabled(true);
            } catch (Exception ex) { }
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

    void setMultiLineCapSentencesAndDoneAction(EditText txt) {
        txt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        txt.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        txt.setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES  );
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care__plane__notes);
        setupActionBar();
        context=this.getApplicationContext();
        settings = getSharedPreferences(PREFS_NAME, 0);

        set_server_Ip();

        TextView txt1 = (TextView) findViewById(R.id.txt1);
        txt1.setText("Recipient \n" + AccountNo);
        boolean shift_note=false;
        final TextView txtShiftDate = findViewById(R.id.txtShiftDate);

        final EditText txtShiftPurpose =  findViewById(R.id.txtShiftPurpose);
        final EditText txtAction = findViewById(R.id.txtAction);

        final EditText txtNote = findViewById(R.id.txtRosterNote);
        final EditText txtFeedBack = findViewById(R.id.txtFeedBack);

        setMultiLineCapSentencesAndDoneAction(txtShiftPurpose);
        setMultiLineCapSentencesAndDoneAction(txtAction);

        setMultiLineCapSentencesAndDoneAction(txtNote);
        setMultiLineCapSentencesAndDoneAction(txtFeedBack);

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


            }catch(Exception ex){}

            try{
                // Cordinator_Email=settings.getString("Staff_Coordinator_Email","-");
              //  EnableViewNoteCases=settings.getString("EnableViewNoteCases","00000");
                StaffCode=settings.getString("StaffCode","ABC");
                this.Server_Available= settings.getBoolean("Server_Available",false);
                //  Cordinator_Email= settings.getString("Coordinator_Email","");

            }catch(Exception ex){}


            try{

           UseOPNoteAsShiftReport= settings.getString("UseOPNoteAsShiftReport","0");
                UseServiceNoteAsShiftReport= settings.getString("UseServiceNoteAsShiftReport","0");

            }catch(Exception ex){}

         TextView   txtShiftDate = findViewById(R.id.txtShiftDate);
              Spinner spShiftGoals = findViewById(R.id.txtShiftgoals);
            try {
              //  if ((UseOPNoteAsShiftReport.equalsIgnoreCase("true") || UseOPNoteAsShiftReport.equals("1")))
                {

                    set_control_Visibility(true);
                    android.text.format.DateFormat df = new android.text.format.DateFormat();
                    DateFormat.format(DATE_FORMAT_1, new Date(Roster_Date));
                    txtShiftDate.setText(DateFormat.format(DATE_FORMAT_1, new Date(Roster_Date)).toString());

                   // txtShiftDate.setText(Roster_Date.substring(8,10) + "/" + Roster_Date.substring(5,7) + "/" + Roster_Date.substring(0,4) + " " + Roster_Date.substring(11));

                    lst_shiftgoals = get_ShiftGoals();
                    if (lst_shiftgoals.size() > 0) {
                        // Toast.makeText(getApplicationContext(), "lst_incident=" + lst_incident.size(), Toast.LENGTH_LONG).show();

                      //  ArrayAdapter adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lst_shiftgoals);
                      //  adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                       // spShiftGoals.setAdapter(adapter1);
                        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),lst_shiftgoals);
                        spShiftGoals.setAdapter(customAdapter);


                    }
                }
            }catch(Exception ex){}


        }catch(Exception ex){}
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
                        //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }


                }//loop
            }
        }catch (Exception e) {}

        return lst_goals;
    }

    void set_control_Visibility(boolean status){

        final TextView txtShiftDate = findViewById(R.id.txtShiftDate);

        Spinner spinner1 = (Spinner) findViewById(R.id.txtShiftgoals);
        TextView txtShiftPurpose = findViewById(R.id.txtShiftPurpose);
        TextView txtAction = findViewById(R.id.txtAction);


        TextView lblShiftGoals = findViewById(R.id.lblShiftGoals);


        if (status){

            txtShiftDate.setVisibility(View.VISIBLE);

            spinner1.setVisibility(View.VISIBLE);
            txtShiftPurpose.setVisibility(View.VISIBLE);
            txtAction.setVisibility(View.VISIBLE);

            lblShiftGoals.setVisibility(View.VISIBLE);

        }else {
            txtShiftDate.setVisibility(View.GONE);

            spinner1.setVisibility(View.GONE);
            txtShiftPurpose.setVisibility(View.GONE);
            txtAction.setVisibility(View.GONE);

            lblShiftGoals.setVisibility(View.GONE);
        }
    }

}
