package adamas.traccs.mta_20_06;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import androidx.core.text.HtmlCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import android.widget.CheckBox;
import android.util.Log;


public class Add_Note_Activity extends AppCompatActivity implements SpellCheckerSession.SpellCheckerSessionListener {

    private final String NAMESPACE = "https://tempuri.org/";

    String root="";
    String AccountNo;
    String EnableViewNoteCases;
    String AllowRosterNoteEntry,AllowOPNoteEntry,AllowClinicalNoteEntry,AllowCaseNoteEntry;
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
    String Job_Time="";
     String selectedItem="";
    String Enforce_Note="false";
    Email email;
    Email_Settings email_seting = null;
    String email_msg, Email_Subject;
    String DefaultAppNoteCategory ="#MTANOTE";
    ImageView imgGoals ;
    ImageView imgStrategy ;
    View img_view_Strategy;
    Context context;
    List<String> lst_Recipient_Strategies=null;
    List<String> list_gen;
    Bulk_Data bulk_data;
    View scrollView;
    private CheckBox sampleCheckBox;
    private boolean isCheckBoxChecked = false;

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
                textviewTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     //   onBackPressed();
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
    public void onGetSuggestions(SuggestionsInfo[] results) {
    }

    @Override
    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] results) {
        final StringBuffer sb = new StringBuffer();
        for(SentenceSuggestionsInfo result:results){
            int n = result.getSuggestionsCount();
            for(int i=0; i < n; i++){
                int m = result.getSuggestionsInfoAt(i).getSuggestionsCount();
                if((result.getSuggestionsInfoAt(i).getSuggestionsAttributes() &
                        SuggestionsInfo.RESULT_ATTR_LOOKS_LIKE_TYPO) != SuggestionsInfo.RESULT_ATTR_LOOKS_LIKE_TYPO )
                  ;//  continue;
                for(int k=0; k < m; k++) {
                    sb.append(result.getSuggestionsInfoAt(i).getSuggestionAt(k))
                            .append("\n");
                }
                sb.append("\n");
            }
        }
    }
    private void fetchSuggestionsFor(String input){
        TextServicesManager tsm =
                (TextServicesManager) getSystemService(TEXT_SERVICES_MANAGER_SERVICE);

        SpellCheckerSession session =
                tsm.newSpellCheckerSession(null, Locale.ENGLISH, this, true);
        session.getSentenceSuggestions(
                new TextInfo[]{ new TextInfo(input) },
                5
        );


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__note);
        setupActionBar();
        this.context=this.getApplicationContext();
               settings = getSharedPreferences(PREFS_NAME, 0);

        set_server_Ip();

        get_SMTP_Settings();
        TextView txtRecipient = (TextView) findViewById(R.id.txt1);
        txtRecipient.setText(HtmlCompat.fromHtml("<b>Recipient</b><br>" + AccountNo,0));

        boolean shift_note=false;

        final TextView txtShiftDate = findViewById(R.id.txtShiftDate);
        final TextView textTime_Label = findViewById(R.id.textView7);

        final TextView spShiftGoals = (TextView) findViewById(R.id.txtShiftgoals);
        final EditText txtShiftPurpose =  findViewById(R.id.txtShiftPurpose);
        final EditText txtAction = findViewById(R.id.txtAction);
        final EditText txtOutCome = findViewById(R.id.txtOutCome);
        final EditText txtFeedBack = findViewById(R.id.txtFeedBack);

        img_view_Strategy=findViewById(R.id.img_view_Strategy);
        imgGoals =  findViewById(R.id.imgGoals);
        imgStrategy =  findViewById(R.id.imgStrategy);

        imgGoals.setVisibility(View.GONE);
        img_view_Strategy.setVisibility(View.GONE);

        final EditText txtNote = findViewById(R.id.txtRosterNote);


        if ( (AllowOPNoteEntry.equals("0")|| AllowOPNoteEntry.equalsIgnoreCase("false"))
                &&  (AllowCaseNoteEntry.equals("0")|| AllowCaseNoteEntry.equalsIgnoreCase("false"))
                &&  (AllowClinicalNoteEntry.equals("0")|| AllowClinicalNoteEntry.equalsIgnoreCase("false"))
        ) {

            Tost_Message("Blocked from adding notes see your TRACCS administrator");
        }


//        setMultiLineCapSentencesAndDoneAction(txtShiftPurpose);
//            setMultiLineCapSentencesAndDoneAction(txtAction);
//            setMultiLineCapSentencesAndDoneAction(txtOutCome);
//            setMultiLineCapSentencesAndDoneAction(txtNote);
           // txtShiftPurpose.setImeOptions(EditorInfo.IME_ACTION_DONE);
           // txtShiftPurpose.setRawInputType(InputType.TYPE_CLASS_TEXT);

            set_control_Visibility(false);

           /* final RadioButton chkOPNote2 =  findViewById(R.id.chkOPNote);
            final RadioButton chkCaseNote2 =  findViewById(R.id.chkCaseNote);
            final RadioButton chkIncidentNote2 =  findViewById(R.id.chkIncidentNote);
            final RadioButton chkClinicalNotes2 =  findViewById(R.id.chkClinicalNotes);
            final RadioButton chkServiceNote2 =  findViewById(R.id.chkServiceNote);

            chkOPNote2.setVisibility(View.GONE) ;
            chkCaseNote2.setVisibility(View.GONE) ;
            chkClinicalNotes2.setVisibility(View.GONE) ;
            chkServiceNote2.setVisibility(View.GONE) ;
            chkIncidentNote2.setVisibility(View.GONE) ;

            try{
                if ( Integer.parseInt( EnableViewNoteCases.substring(0, 1))==1 && AllowOPNoteEntry.equalsIgnoreCase("true")) {
                  //  chkOPNote2.setVisibility(View.VISIBLE) ;
                }
                if ( Integer.parseInt( EnableViewNoteCases.substring(1, 2))==1 && AllowCaseNoteEntry.equalsIgnoreCase("true")) {
                  //  chkCaseNote2.setVisibility(View.VISIBLE) ;
                }
                if ( Integer.parseInt( EnableViewNoteCases.substring(2, 3))==1) {
                    chkIncidentNote2.setVisibility(View.GONE) ;
                }
                if (  AllowRosterNoteEntry.equalsIgnoreCase("true")) {
                 //   chkServiceNote2.setVisibility(View.GONE) ;
                }
                if ( Integer.parseInt( EnableViewNoteCases.substring(4, 5))==1 && AllowClinicalNoteEntry.equalsIgnoreCase("true")) {
                //    chkClinicalNotes2.setVisibility(View.VISIBLE) ;
                }
            }catch(Exception ex){}*/

        set_control_Visibility(false);

        txtShiftDate.setText(Roster_Date.substring(8,10) + "/" + Roster_Date.substring(5,7) + "/" + Roster_Date.substring(0,4) + " " + Job_Time);



         spShiftGoals.setOnClickListener( new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                   //  set_Spinner(v.getContext(),spShiftGoals,lst_shiftgoals,"Shift Goals");


             }


        });
        imgGoals.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lst_shiftgoals.size()>1) {
                    set_Spinner(v.getContext(),spShiftGoals,lst_shiftgoals,"Shift Goals");
                    //set_Spinner(v.getContext(), spShiftGoals, list_gen,"Goals");
                } else   if (lst_shiftgoals.size()>0)
                    spShiftGoals.setText(lst_shiftgoals.get(0));


            }


        });

        txtShiftPurpose.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasfocus) {
                if (hasfocus) {
                    // Tost_Message( "e1 focused");
                    // if (lst_Recipient_Strategies.size()>0) txtShiftPurpose.setText("");
                   /* if (lst_Recipient_Strategies.size()>1)
                        set_Spinner(v.getContext(),txtShiftPurpose,lst_Recipient_Strategies);
                    else   if (lst_Recipient_Strategies.size()>0)
                        txtShiftPurpose.setText(lst_Recipient_Strategies.get(0));*/

                } else {
                    //Tost_Message("e1 not focused");
                }
            }
        });

        String Note_Type = Main_Op_Note;

        /*
        chkOPNote2.setChecked(false);
        if (Main_Op_Note.equalsIgnoreCase("OPNOTE") ){
            chkOPNote2.setChecked(true);
        }
        if (Main_Op_Note.equalsIgnoreCase("CASENOTE")) {
            chkCaseNote2.setChecked(true);
        }
        if (Main_Op_Note.equalsIgnoreCase("RECIMNOTE")){
            chkIncidentNote2.setChecked(false);
        }
        if (Main_Op_Note.equalsIgnoreCase("SVCNOTE")) chkServiceNote2.setChecked(true);
        if (Main_Op_Note.equalsIgnoreCase("CLINNOTE")) {            chkClinicalNotes2.setChecked(true);

        }*/

        final TextView spNoteType=findViewById(R.id.spNoteType);
        final List<String> lstNoteType= new ArrayList<>();


        if (Enforce_Note.equalsIgnoreCase("true")){
            lstNoteType.add(Main_Op_Note);
            selectedItem=Main_Op_Note;

        }else {


            if (AllowOPNoteEntry.equalsIgnoreCase("true") || AllowOPNoteEntry.equalsIgnoreCase("1")) {

                lstNoteType.add("Op Note");
               // Main_Op_Note = "OPNOTE";
            }
            if (AllowCaseNoteEntry.equalsIgnoreCase("true") || AllowCaseNoteEntry.equalsIgnoreCase("1")) {

                lstNoteType.add("Case Note");
              //  Main_Op_Note = "CASENOTE";
            }

            if (AllowClinicalNoteEntry.equalsIgnoreCase("true") || AllowClinicalNoteEntry.equalsIgnoreCase("1")) {

                lstNoteType.add("Clinical Note");
              //  Main_Op_Note = "CLINNOTE";
            }
        }
        spNoteType.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                set_Spinner_OP_Note(v.getContext(), spNoteType, lstNoteType, "Note Type");
                try {
                    if ((UseOPNoteAsShiftReport.equalsIgnoreCase("true") || UseOPNoteAsShiftReport.equals("1"))
                            && Main_Op_Note.equalsIgnoreCase("OPNOTE") )
                    {

                        lst_shiftgoals = get_ShiftGoals();
                        //  lst_shiftgoals.add(0,"Select an option");
                        if (lst_shiftgoals.size() > 0) {


                        }
                    }
                }catch(Exception ex){}


            }


        });


        selectedItem = Main_Op_Note;
        spNoteType.setText(selectedItem);
        set_control_Visibility(selectedItem.equalsIgnoreCase("OP NOTE") && (UseOPNoteAsShiftReport.equalsIgnoreCase("true") || UseOPNoteAsShiftReport.equals("1")));

            Button dialogButton = (Button) findViewById(R.id.btnExit);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    finish();
                }
            });
            Button dialogSave = (Button) findViewById(R.id.btnOK);
            // if button is clicked, close the custom dialog
            dialogSave.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    String Note_Type = Main_Op_Note;

                    EditText txtRosterNote = (EditText) findViewById(R.id.txtRosterNote);
                    TextView spNoteType =  findViewById(R.id.spNoteType);

                    String spnote= spNoteType.getText().toString();
                    if (spnote.equalsIgnoreCase("Op Note")) Note_Type = "OPNOTE";
                    else if (spnote.equalsIgnoreCase("Case Note")) Note_Type = "CASENOTE";
                    else if (spnote.equalsIgnoreCase("Clinical Note")) Note_Type = "CLINNOTE";
                    else if (spnote.equalsIgnoreCase("Incident Note")) Note_Type = "RECIMNOTE";

                    /*if (chkOPNote2.isChecked()) Note_Type = "OPNOTE";
                    if (chkCaseNote2.isChecked()) Note_Type = "CASENOTE";
                    if (chkIncidentNote2.isChecked()) Note_Type = "RECIMNOTE";
                    if (chkServiceNote2.isChecked()) Note_Type = "SVCNOTE";
                    if (chkClinicalNotes2.isChecked()) Note_Type = "CLINNOTE";
*/
                    if (txtRosterNote.getText().toString().isEmpty()){
                        Tost_Message("Please enter valid note");
                        txtRosterNote.setError("Please fill field");
                        return;
                    }
                    String notes="";
                    if ((UseOPNoteAsShiftReport.equalsIgnoreCase("true") || UseOPNoteAsShiftReport.equals("1"))
                            && Note_Type.equalsIgnoreCase ("OPNOTE")  )
                    {
                        String shift_goals=spShiftGoals.getText().toString(); //lst_shiftgoals.get(spShiftGoals.getSelectedItemPosition());
                        if (shift_goals.isEmpty()){
                            Tost_Message("Please select valid shift goals");
                            return;
                        }
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

                        if (txtFeedBack.getText().toString().isEmpty()){
                            Tost_Message("Please fill in all fields");
                            txtFeedBack.setError("Please fill field");
                            return;
                        }
                      notes = "SHIFT REPORT FOR : " +  Roster_Date + "\n\n"
                                + "SHIFT GOAL/S: \n" +shift_goals + "\n\n"
                                + "SHIFT PURPOSE: \n" + txtShiftPurpose.getText()  + "\n\n"
                                + "ACTIONS: \n" + txtAction.getText()  + "\n\n"
                                + "OUTCOME: \n" + txtOutCome.getText()  + "\n\n"
                                + "FEEDBACK: \n" + txtFeedBack.getText().toString() + "\n\n"
                                + txtRosterNote.getText().toString() ;


                      /*  notes = "SHIFT REPORT FOR : " +  Roster_Date + "\n\n"

                                + "SHIFT PURPOSE: \n" + txtShiftPurpose.getText()  + "\n\n"
                                + "ACTIONS: \n" + txtAction.getText()  + "\n\n"
                                + "OUTCOME: \n" + txtOutCome.getText()  + "\n\n"
                                + txtRosterNote.getText().toString();
*/


                    }else {

                        notes = txtRosterNote.getText().toString();
                    }


                    notes = SQLSafe(notes); //notes=notes.replace("\'", "\'\'");
                    Server_Available = isOnline(getApplicationContext());


                    try {
                        new MyAsyncClass5().execute(notes, Note_Type);
                    } catch (Exception ex) {
                    }


                }
            });
          /*  chkOPNote2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked) {

                        chkCaseNote2.setChecked(false);
                        chkIncidentNote2.setChecked(false);
                        chkServiceNote2.setChecked(false);
                        chkClinicalNotes2.setChecked(false);
                        if (UseOPNoteAsShiftReport.equalsIgnoreCase("true") || UseOPNoteAsShiftReport.equals("1"))
                            set_control_Visibility(true);
                    }


                }
            });
            chkCaseNote2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked) {
                        chkOPNote2.setChecked(false);
                        chkIncidentNote2.setChecked(false);
                        chkServiceNote2.setChecked(false);
                        chkClinicalNotes2.setChecked(false);
                        set_control_Visibility(false);

                    }


                }
            });

            chkIncidentNote2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked) {
                        chkOPNote2.setChecked(false);
                        chkCaseNote2.setChecked(false);
                        chkServiceNote2.setChecked(false);
                        chkClinicalNotes2.setChecked(false);
                        set_control_Visibility(false);

                    }


                }
            });

            chkServiceNote2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked) {
                        chkOPNote2.setChecked(false);
                        chkCaseNote2.setChecked(false);
                        chkIncidentNote2.setChecked(false);
                        chkClinicalNotes2.setChecked(false);
                        set_control_Visibility(false);
                    }


                }
            });

            chkClinicalNotes2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked) {
                        chkOPNote2.setChecked(false);
                        chkCaseNote2.setChecked(false);
                        chkIncidentNote2.setChecked(false);
                        chkServiceNote2.setChecked(false);

                        set_control_Visibility(false);
                    }


                }
            });*/


        sampleCheckBox = findViewById(R.id.sampleCheckBox);

        sampleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckBoxChecked = isChecked;

                if (isChecked) {
                    Log.d("CheckBox", "Checked - value is TRUE");
                } else {
                    Log.d("CheckBox", "Unchecked - value is FALSE");
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
        ImageView imgGoals = findViewById(R.id.imgGoals);

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
            imgGoals.setVisibility(View.VISIBLE);
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
            imgGoals.setVisibility(View.GONE);
            lbl1RosterNote.setText("Client Note");
        }
    }
        void set_control_Visibility2(boolean status){

            final TextView txtShiftDate = findViewById(R.id.txtShiftDate);
            final TextView textTime_Label = findViewById(R.id.textView7);

            TextView spinner1 = findViewById(R.id.txtShiftgoals);
            TextView txtShiftPurpose = findViewById(R.id.txtShiftPurpose);
            TextView txtAction = findViewById(R.id.txtAction);
            TextView txtOutCome = findViewById(R.id.txtOutCome);

            TextView lblShiftGoals = findViewById(R.id.lblShiftGoals);
            TextView lblShiftPurpose = findViewById(R.id.lblShiftPurpose);
            TextView lblAction = findViewById(R.id.lblAction);
            TextView lbloutCome = findViewById(R.id.lbloutCome);

            if (status){

                txtShiftDate.setVisibility(View.VISIBLE);
                textTime_Label.setVisibility(View.VISIBLE);

                spinner1.setVisibility(View.VISIBLE);
                txtShiftPurpose.setVisibility(View.VISIBLE);
                txtAction.setVisibility(View.VISIBLE);
                txtOutCome.setVisibility(View.VISIBLE);

                lblShiftGoals.setVisibility(View.VISIBLE);
                lblShiftPurpose.setVisibility(View.VISIBLE);
                lblAction.setVisibility(View.VISIBLE);
                lbloutCome.setVisibility(View.VISIBLE);

            }else {
              /*  txtShiftDate.setVisibility(View.GONE);
                textTime_Label.setVisibility(View.GONE);
*/
                spinner1.setVisibility(View.GONE);
                txtShiftPurpose.setVisibility(View.GONE);
                txtAction.setVisibility(View.GONE);
                txtOutCome.setVisibility(View.GONE);

                lblShiftGoals.setVisibility(View.GONE);
                lblShiftPurpose.setVisibility(View.GONE);
                lblAction.setVisibility(View.GONE);
                lbloutCome.setVisibility(View.GONE);
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
                        //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }


                }//loop
            }
            if (lst_goals.size()>0)
                imgGoals.setVisibility(View.VISIBLE);
            else
                imgGoals.setVisibility(View.GONE);
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
                root=settings.getString("root","");
                Recipient=bundle.get("Recipient").toString();
                Security_Token=settings.getString("Security_Token","");
                OperatorId=settings.getString("OperatorId","");
                this.AccountNo= bundle.getString("AccountNo");
                this.Roster_Date= bundle.getString("Roster_Date");
                Personid= bundle.getString("PersonId","0");
                Main_Op_Note= bundle.getString("Main_Op_Note","");
                Job_Time= bundle.getString("Job_Time","-");
                Enforce_Note=bundle.getString("Enforce_Note","false");

            }catch(Exception ex){}

            try{
               // Cordinator_Email=settings.getString("Staff_Coordinator_Email","-");
                EnableViewNoteCases=settings.getString("EnableViewNoteCases","00000");
                StaffCode=settings.getString("StaffCode","ABC");
                this.Server_Available= settings.getBoolean("Server_Available",false);
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



        }catch(Exception ex){}
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

            pDialog = new LoadingDialog(Add_Note_Activity.this);
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
                OP_Case_Note_Activity.Refresh_OP_Note_data=true;
                String messgas = "The following "+ Note_Typee+" note has been added to client \"" + AccountNo + "\" by \"" + StaffCode + "\" :\n\n" + Notee;
                String title = "  TRACCS Client Note Added for : " + AccountNo + "\n";

                if (settings.getString("ClientNoteEmailOverrides","false").equalsIgnoreCase("true")){
                    if(!settings.getString("ClientNoteEmail","").equals(""))
                        Cordinator_Email=settings.getString("ClientNoteEmail","");
                }
                if (email_seting != null) {
                    //email= new Email ("traccs.sprt.mta@gmail.com","arshadabbas","smtp.gmail.com","465");
                    email = new Email(email_seting.getFromEmail(), email_seting.getSMTPPassword(), email_seting.getSMTPServer(), email_seting.getSMTP_Port());
                    //Toast.makeText(getApplicationContext(), email_seting.getPOP3User() + "\n" + email_seting.getPOP3Password() + "\nSending Email from SMTP Server :" + email_seting.getPOP3Server(), Toast.LENGTH_LONG).show();
                    if (settings.getString( "EnableEmailNotification","true").equalsIgnoreCase("true")
                            || settings.getString( "EnableEmailNotification","true").equalsIgnoreCase("1"))

                                  if(isCheckBoxChecked)
                                  {
                                      send_email_alert(messgas, title);
                                  }


                }else
                if (settings.getString( "EnableEmailNotification","true").equalsIgnoreCase("true")
                        || settings.getString( "EnableEmailNotification","true").equalsIgnoreCase("1"))
                    send_local_email(title,messgas);
                //
                finish();

            }catch (Exception ex) {}

            try{
              //  new MyAsyncClass4().execute(result,Main_Op_Note);

            }catch (Exception ex) {}
        }
    }


    String getSecurityToken(){

        String Val= OperatorId  + "$" + Security_Token + "$";
        return Val;
    }

    void get_SMTP_Settings(){

            email_seting= new Email_Settings();

            email_seting.setSMTPServer(settings.getString("SMTP_Server", "mail.adamas.net.au"));
            email_seting.setSMTP_Port(settings.getString("SMTP_Port", "567"));
            email_seting.setSMTPUser(settings.getString("SMTP_User", "timwatts@adamas.net.au"));
            email_seting.setSMTPPassword(settings.getString("SMTP_Password", "samada2002"));
            email_seting.setFromDisplayName(settings.getString("Email_Subject", " TRACCS Client Note Added for : " + AccountNo));
            email_seting.setFromEmail(settings.getString("From_Address", "support@adamas.net.au"));



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
            pi.setValue(getSecurityToken() + values);
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
                Tost_Message("Operation not done - " + AccountNo +  "");
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

        try{
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
                if (((("%*!,-_()/\\+=0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz:@#&;?'$.\n\t\" " + s_ExtraChar).indexOf(s_Char) + 1)
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

            if (UseOPNoteAsShiftReport.equalsIgnoreCase("true") || UseOPNoteAsShiftReport.equalsIgnoreCase("1")) {
                command = "\n" + "insert into history(detaildate,PersonId,creator,Detail,PrivateFlag,ExtraDetail1,ExtraDetail2,whocode,PublishToApp) " +
                        "values(getDate(),'" + Personid + "','" + OperatorId + "','" + SQLSafe(Note) + "',0,'" + Note_Type + "','#SHIFTREPORT','" + AccountNo + "',1)";

            }else {
                command = "\n" + "insert into history(detaildate,PersonId,creator,Detail,PrivateFlag,ExtraDetail1,ExtraDetail2,whocode,PublishToApp) " +
                        "values(getDate(),'" + Personid + "','" + OperatorId + "','" + SQLSafe(Note) + "',0,'" + Note_Type + "','" + DefaultAppNoteCategory + "','" + AccountNo + "',1)";
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
    public void set_Spinner_OP_Note(Context view, final TextView spinner, final List<String> lst, String Title) {
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
                set_control_Visibility(selectedItem.equalsIgnoreCase("OP NOTE") && (UseOPNoteAsShiftReport.equalsIgnoreCase("true") || UseOPNoteAsShiftReport.equals("1")));
                dialog.dismiss();

            }});

        //spinner.setText(selectedItem);
        dialog.show();

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
        final EditText txtShiftPurpose =  findViewById(R.id.txtShiftPurpose);

        if (lst.size()<=0) return;
        lstSpinner.setAdapter(new Spinner_Adpater(view,lst));
        lstSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                selectedItem = lst.get(position);
                spinner.setText(selectedItem);
                //Selected_Goal=lst_Recipient_goals.get(position);

                dialog.dismiss();

            }});

        //spinner.setText(selectedItem);
        dialog.show();

    }
    public void set_Spinner(Context view, final TextView spinner, final List<String> lst) {
        final Dialog dialog = new Dialog(view);
        dialog.setContentView(R.layout.spinner_list_with_title);
        //dialog.setTitle("My Dashboard");
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        Window window = dialog.getWindow();
        if (window != null) {
            window.getAttributes().x=0;//(int) TextDate.getTop();
            window.getAttributes().y =-2;

            WindowManager.LayoutParams params = window.getAttributes();
            params.width =  500;
            params.height = lst.size()*110;

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
    public void set_Spinner2(Context view, final TextView spinner, final List<String> lst) {
        //final Dialog dialog = new Dialog(view,R.style.CustomDialog);
       // dialog.setContentView(R.layout.spinner_list);
        //dialog.setTitle("My Dashboard");
        final Dialog dialog = new Dialog(view);
        dialog.setContentView(R.layout.spinner_list_with_title);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        Window window = dialog.getWindow();
        if (window != null) {
            window.getAttributes().x=0;//(int) TextDate.getTop();
            window.getAttributes().y =0;

            WindowManager.LayoutParams params = window.getAttributes();
            params.width =  WindowManager.LayoutParams.MATCH_PARENT;
            params.height = 400;

            params.horizontalMargin = 0;
            params.verticalMargin=0;
            params.gravity = Gravity.RIGHT | Gravity.CENTER;
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
       // final Dialog dialog = new Dialog(view,R.style.CustomDialog);
       // dialog.setContentView(R.layout.spinner_list);
        //dialog.setTitle("My Dashboard");
        final Dialog dialog = new Dialog(view);
        dialog.setContentView(R.layout.spinner_list_with_title);
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
            ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) spinner.getLayoutParams();

            window.getAttributes().x=0;//(int) spinner.getTop();
            window.getAttributes().y =params2.topMargin+70;

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
