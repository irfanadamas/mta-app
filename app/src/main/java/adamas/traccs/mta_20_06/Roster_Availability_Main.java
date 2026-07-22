package adamas.traccs.mta_20_06;

import android.animation.TimeAnimator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTabHost;

public class Roster_Availability_Main extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    public String root = "https://58.162.142.150/timesheet"; //https://10.0.2.2:49884
    private final String NAMESPACE = "https://tempuri.org/";

    String StaffCode = "";
    String OperatorId = "";
    String Security_Token = "";
    boolean server_available = false;
    String Roster_Date;
    File froot = null;
    Context context;
    String RecordNo = "0";
    String Allow_OverWrite_Existing_Availability="1";
    String EmailUnavailabilityNotification="0";
    int original_margin=0;
    String r_Date = "";
    String E_Date="";
    String Start_Time = "";
    String End_Time = "";
    String Note = "";
    String Cycle = "";
    String Week_Days = "";
    String Weeks="";
    String RosterDate;

    Button btnSave;
    Button btnCancel;
    View Current_View;
    RadioButton chkDateWise;
    RadioButton chkPermanent;
    CheckBox chkSpecifyDays;
    String selected_val="";
    boolean SleepOver=false;
    boolean OverWrite_Existing_Availability=true;
    String Coordinator_Email="";
    String Email_Message="";
    static boolean data_changed=false;
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

                        //onBackPressed();
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster__availability__main);
        setupActionBar();
        this.context=this.getApplicationContext();
        get_Server();
        set_default_date();

        btnCancel = (Button) findViewById(R.id.btnSettings);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Current_View=fragmentTabHost.getCurrentTabView();
                server_available = isOnline(btnSave.getContext());
                data_changed=true;
                CheckBox  chkWeek1=(CheckBox) findViewById(R.id.chkWeek1);
                CheckBox  chkWeek2=(CheckBox) findViewById(R.id.chkWeek2);
                CheckBox  chkWeek3=(CheckBox) findViewById(R.id.chkWeek3);
                CheckBox  chkWeek4=(CheckBox) findViewById(R.id.chkWeek4);
                RadioButton  chkPermanent=(RadioButton) findViewById(R.id.chkPermanent);
                Spinner spinnerCycle= findViewById(R.id.spinnerCycle);

                TextView dtpStart = findViewById(R.id.dtpStartDate);
                TextView dtpEnd = findViewById(R.id.dtpEndDate);
                TextView dtpStartTime = findViewById(R.id.dtpStartTime);
                TextView dtpEndTime = findViewById(R.id.dtpEndTime);
                
                if (chkPermanent.isChecked()) {
                    if (spinnerCycle.getSelectedItem().equals("Select an option")) {
                        Tost_Message("Please select valid Cycle");
                        return;
                    }
                    if (spinnerCycle.getSelectedItem().equals("WEEKLY")) {
                        chkWeek1.setChecked(true);
                    }

                    if (!chkWeek1.isChecked() && !chkWeek2.isChecked() && !chkWeek3.isChecked() && !chkWeek4.isChecked()) {
                        Tost_Message("Please select some week");
                        return;
                    }
                }else{
                    if (dtpStart.getText().equals("")) {
                        Tost_Message("Please enter valid start date");
                        return;
                    }
                    if (dtpEnd.getText().equals("")) {
                        Tost_Message("Please enter valid end date");
                        return;
                    }
                }

                if (dtpStartTime.getText().equals("-")){
                    Tost_Message("Please enter valid start time");
                    return;
                }
                if (dtpEndTime.getText().equals("-")){
                    Tost_Message("Please enter valid end time");
                    return;
                }
                if (Cycle.equalsIgnoreCase("MONTHLY"))
                {
                    if (chkWeek1.isChecked() || chkWeek2.isChecked()) {
                        Cycle = "FORTNIGHTLY";
                        Call_Add_Roster();
                        try {
                            Thread.sleep(1000);
                        }catch (Exception ex){}
                    }

                    new Thread(new Runnable() {
                        public void run() {

                            btnSave.post(new Runnable() {
                                public void run() {
                                   // Thread.sleep(1000);
                                    Cycle="MONTHLY";
                                    Call_Add_Roster();

                                }
                            });
                        }
                    }).start();


                }else
                    Call_Add_Roster();
            }
        });

      /*final  FragmentTabHost fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        fragmentTabHost.addTab(getTabSpec1(fragmentTabHost), Roster_Availability.class, null);
        fragmentTabHost.addTab(getTabSpec2(fragmentTabHost), Roster_Availability2.class, null);
*/

        CheckBox chkOverWrite_Existing_Availability=(CheckBox)findViewById(R.id.chkOverWrite_Existing_Availability);


        if (Allow_OverWrite_Existing_Availability.equalsIgnoreCase("0") || Allow_OverWrite_Existing_Availability.equalsIgnoreCase("false")){

            chkOverWrite_Existing_Availability.setVisibility(View.GONE);

      }

       final Spinner spinnerCycle=(Spinner) findViewById(R.id.spinnerCycle);
         chkDateWise= findViewById(R.id.chkDateWise);
         chkPermanent= findViewById(R.id.chkPermanent);
         chkSpecifyDays= findViewById(R.id.chkSpecifyDays);

        final  CheckBox  chkDay1=(CheckBox) findViewById(R.id.chkDay1);
        final  CheckBox  chkDay2=(CheckBox) findViewById(R.id.chkDay2);
        final  CheckBox  chkDay3=(CheckBox) findViewById(R.id.chkDay3);
        final  CheckBox  chkDay4=(CheckBox) findViewById(R.id.chkDay4);
        final  CheckBox  chkDay5=(CheckBox) findViewById(R.id.chkDay5);
        final  CheckBox  chkDay6=(CheckBox) findViewById(R.id.chkDay6);
        final  CheckBox  chkDay7=(CheckBox) findViewById(R.id.chkDay7);


        final CheckBox  chkWeek3=(CheckBox) findViewById(R.id.chkWeek3);
        final CheckBox  chkWeek4=(CheckBox) findViewById(R.id.chkWeek4);

        final View rl_body_dates=(View) findViewById(R.id.rl_body_dates);
        final  View rlPermanent=(View) findViewById(R.id.rl_body_permanant);

        final  View rl_body_permanant4=(View) findViewById(R.id.rl_body_w2);
        final  View rl_body_permanant5=(View) findViewById(R.id.rl_body_w3);

        rl_body_permanant4.setEnabled(false);
        rl_body_permanant5.setEnabled(false);

        chkDateWise.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {
                    rl_body_dates.setVisibility(View.VISIBLE);
                    chkPermanent.setChecked(false);
                    rlPermanent.setVisibility(View.GONE);
                    View rl_body_w1=findViewById(R.id.rl_body_w1);
                    ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) rl_body_w1.getLayoutParams();
                    if (original_margin==0) original_margin=lp.topMargin;
                    lp.setMargins(lp.getMarginStart(),original_margin ,lp.getMarginEnd(),8);
                    // Apply the updated layout parameters to TextView
                    rl_body_w1.setLayoutParams(lp);
                }else{
                }


            }
        });

        chkPermanent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {
                    Cycle="WEEKLY";
                    rlPermanent.setVisibility(View.VISIBLE);
                    chkDateWise.setChecked(false);
                    rl_body_dates.setVisibility(View.GONE);

                    String cc=spinnerCycle.getSelectedItem().toString();

                    if (cc.equalsIgnoreCase("fortnightly") ||cc.equalsIgnoreCase("monthly"))
                    {
                        View rl_body_w1=findViewById(R.id.rl_body_w1);

                        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) rl_body_w1.getLayoutParams();
                        if (original_margin==0) original_margin=lp.topMargin;
                        lp.setMargins(lp.getMarginStart(),original_margin +160,lp.getMarginEnd(),8);
                        // Apply the updated layout parameters to TextView
                        rl_body_w1.setLayoutParams(lp);
                    }

                }else{
                   /* Cycle="";
                    rlPermanent.setVisibility(View.GONE);
                    chkDateWise.setChecked(true);
                    rl_body_dates.setVisibility(View.VISIBLE);*/
                }


            }
        });


        chkSpecifyDays.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {

                    chkSpecifyDays.setButtonDrawable(R.drawable.checkbox2);
                    rl_body_permanant4.setEnabled(true);
                    rl_body_permanant5.setEnabled(true);

                    chkDay1.setEnabled(true);
                    chkDay2.setEnabled(true);
                    chkDay3.setEnabled(true);
                    chkDay4.setEnabled(true);
                    chkDay5.setEnabled(true);
                    chkDay6.setEnabled(true);
                    chkDay7.setEnabled(true);
                   // rl_body_permanant4.setBackgroundResource(R.drawable.background2);
                   // rl_body_permanant5.setBackgroundResource(R.drawable.background2);

                }else{
                    chkSpecifyDays.setButtonDrawable(R.drawable.checkbox);
                    rl_body_permanant4.setEnabled(false);
                    rl_body_permanant5.setEnabled(false);

                    chkDay1.setEnabled(false);
                    chkDay2.setEnabled(false);
                    chkDay3.setEnabled(false);
                    chkDay4.setEnabled(false);
                    chkDay5.setEnabled(false);
                    chkDay6.setEnabled(false);
                    chkDay7.setEnabled(false);

                    chkDay1.setChecked(false);
                    chkDay2.setChecked(false);
                    chkDay3.setChecked(false);
                    chkDay4.setChecked(false);
                    chkDay5.setChecked(false);
                    chkDay6.setChecked(false);
                    chkDay7.setChecked(false);

                    //rl_body_permanant4.setBackgroundResource(R.drawable.bg2);
                   // rl_body_permanant5.setBackgroundResource(R.drawable.bg2);

                }


            }
        });




        spinnerCycle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?>arg0, View view, int arg2, long arg3) {
                View rl_body_permanant2= findViewById(R.id.rl_body_permanant2);
                selected_val=spinnerCycle.getSelectedItem().toString();
                Cycle=selected_val;


                if (selected_val.equalsIgnoreCase("FORTNIGHTLY")
                || selected_val.equalsIgnoreCase("MONTHLY")){

                    rl_body_permanant2.setVisibility(View.VISIBLE);
                        if(selected_val.equalsIgnoreCase("MONTHLY")) {

                            chkWeek3.setVisibility(View.VISIBLE);
                            chkWeek4.setVisibility(View.VISIBLE);
                        }else{
                            chkWeek3.setVisibility(View.GONE);
                            chkWeek4.setVisibility(View.GONE);
                        }


                    View rl_body_w1=findViewById(R.id.rl_body_w1);
                    ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) rl_body_w1.getLayoutParams();
                    if (original_margin==0) original_margin=lp.topMargin;
                    lp.setMargins(lp.getMarginStart(),original_margin +100,lp.getMarginEnd(),8);
                    // Apply the updated layout parameters to TextView
                    rl_body_w1.setLayoutParams(lp);

                }else{


                    chkWeek3.setVisibility(View.GONE);
                    chkWeek4.setVisibility(View.GONE);
                    rl_body_permanant2.setVisibility(View.GONE);
                    View rl_body_w1=findViewById(R.id.rl_body_w1);
                    ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) rl_body_w1.getLayoutParams();
                    if (original_margin==0) original_margin=lp.topMargin;
                    lp.setMargins(lp.getMarginStart(),original_margin ,lp.getMarginEnd(),8);
                    // Apply the updated layout parameters to TextView
                    rl_body_w1.setLayoutParams(lp);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        final Button dtpStartTime = (Button) findViewById(R.id.dtpStartTime);
        final Button dtpEndTime = (Button) findViewById(R.id.dtpEndTime);
      //  final EditText txtNote2 = (EditText) findViewById(R.id.txtNote);
        final Calendar myCalendar = Calendar.getInstance();


        final Button dtpStartDate = (Button) findViewById(R.id.dtpStartDate);
        final Button dtpEndDate = (Button) findViewById(R.id.dtpEndDate);

        final DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                int day2 = view.getDayOfMonth();
                int month2 = view.getMonth() + 1;
                int year2 = view.getYear();

                dtpStartDate.setTag(year2 + "/" + set_leading_zero(month2, 2) + "/" + set_leading_zero(day2, 2));
                dtpStartDate.setText(set_leading_zero(day2, 2) + "/" + set_leading_zero(month2, 2) + "/" + year2);


            }
        };

        dtpStartDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /*new DatePickerDialog(Roster_Availability_Main.this, d, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();*/

                int y,m,d;
                y=Integer.parseInt(RosterDate.substring(0,4));
                m=Integer.parseInt(RosterDate.substring(5,7));
                d=Integer.parseInt(RosterDate.substring(8,10));
                // if (m>1) m--;
                DatePickerDialog dpt=  new DatePickerDialog(Roster_Availability_Main.this,R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //  makeWrapContent(view);
                        int day2 = view.getDayOfMonth();
                        int month2 = view.getMonth() +1;
                        int year2 = view.getYear();
                        //RosterDate =(year2 + "/" + set_leading_zero(month2, 2) + "/" + set_leading_zero(day2, 2));
                        dtpStartDate.setTag(year2 + "/" + set_leading_zero(month2, 2) + "/" + set_leading_zero(day2, 2));
                        dtpStartDate.setText(set_leading_zero(day2, 2) + "/" + set_leading_zero(month2, 2) + "/" + year2);


                    }
                }, y, m-1, d);
                dpt.show();

            }
        });

        final   DatePickerDialog.OnDateSetListener d2 = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                int day2 = view.getDayOfMonth();
                int month2 = view.getMonth() + 1;
                int year2 = view.getYear();

                dtpEndDate.setTag(  year2 + "/" + set_leading_zero( month2,2) + "/"+ set_leading_zero(day2,2));
                dtpEndDate.setText( set_leading_zero(day2,2)   + "/" + set_leading_zero( month2,2) + "/" + year2);



            }
        };

        dtpEndDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

               /* new DatePickerDialog(Roster_Availability_Main.this, d2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
*/
                int y,m,d;
                y=Integer.parseInt(RosterDate.substring(0,4));
                m=Integer.parseInt(RosterDate.substring(5,7));
                d=Integer.parseInt(RosterDate.substring(8,10));
                // if (m>1) m--;
                DatePickerDialog dpt=  new DatePickerDialog(Roster_Availability_Main.this,R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //  makeWrapContent(view);
                        int day2 = view.getDayOfMonth();
                        int month2 = view.getMonth() +1;
                        int year2 = view.getYear();
                        //RosterDate =(year2 + "/" + set_leading_zero(month2, 2) + "/" + set_leading_zero(day2, 2));
                        dtpEndDate.setTag(year2 + "/" + set_leading_zero(month2, 2) + "/" + set_leading_zero(day2, 2));
                        dtpEndDate.setText(set_leading_zero(day2, 2) + "/" + set_leading_zero(month2, 2) + "/" + year2);


                    }
                }, y, m-1, d);
                dpt.show();

            }
        });


/*
        final TimePickerDialog.OnTimeSetListener td = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                dtpStartTime.setText(set_leading_zero(hour, 2) + ":" + set_leading_zero(minute, 2));

            }
        };*/




        dtpStartTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {



                TimePickerDialog tdp = new TimePickerDialog(Roster_Availability_Main.this, R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        dtpStartTime.setText(set_leading_zero(hourOfDay, 2) + ":" + set_leading_zero(minute, 2));
                    }
                }, 12, 00, false);


               //TimePickerDialog tdp = new TimePickerDialog(Roster_Availability_Main.this, td, 12, 00, true);

                tdp.setTitle("Start Time");
                tdp.setCancelable(true);
                tdp.setCanceledOnTouchOutside(false);
                tdp.setIcon(R.drawable.ic_time_24dp);

                tdp.getActionBar();
                tdp.show();


            }
        });

      /*  final TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                dtpEndTime.setText(set_leading_zero(hour, 2) + ":" + set_leading_zero(minute, 2));
            //    txtNote2.setEnabled(true);
            }
        };*/

        dtpEndTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                TimePickerDialog tdp = new TimePickerDialog(Roster_Availability_Main.this, R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        dtpEndTime.setText(set_leading_zero(hourOfDay, 2) + ":" + set_leading_zero(minute, 2));
                    }
                }, 12, 00, false);


              //  TimePickerDialog tdp = new TimePickerDialog(Roster_Availability_Main.this, t, 12, 00, true);

                tdp.setTitle("End Time");
                tdp.setCancelable(false);
                tdp.setCanceledOnTouchOutside(false);
                tdp.setIcon(R.drawable.ic_time_24dp);
                tdp.getActionBar();
                tdp.show();


            }
        });

    }

    Activity getActivity(){

        return getActivity();
    }
    private TabHost.TabSpec getTabSpec1(FragmentTabHost tabHost) {

        return tabHost.newTabSpec("DateWise")
                .setIndicator("Date Wise");
    }

    private TabHost.TabSpec getTabSpec2(FragmentTabHost tabHost) {

        return tabHost.newTabSpec("Permanenet")
                .setIndicator("Permanenet");
    }

    void Call_Add_Roster() {


        Weeks="";
        Week_Days="";
          RadioButton  chkPermanent= findViewById(R.id.chkPermanent);
          CheckBox  chkDay1=(CheckBox) findViewById(R.id.chkDay1);
          CheckBox  chkDay2=(CheckBox) findViewById(R.id.chkDay2);
          CheckBox  chkDay3=(CheckBox) findViewById(R.id.chkDay3);
          CheckBox  chkDay4=(CheckBox) findViewById(R.id.chkDay4);
          CheckBox  chkDay5=(CheckBox) findViewById(R.id.chkDay5);
          CheckBox  chkDay6=(CheckBox) findViewById(R.id.chkDay6);
          CheckBox  chkDay7=(CheckBox) findViewById(R.id.chkDay7);

          CheckBox  chkWeek1=(CheckBox) findViewById(R.id.chkWeek1);
          CheckBox  chkWeek2=(CheckBox) findViewById(R.id.chkWeek2);
          CheckBox  chkWeek3=(CheckBox) findViewById(R.id.chkWeek3);
          CheckBox  chkWeek4=(CheckBox) findViewById(R.id.chkWeek4);

        CheckBox  chkSleepOver=(CheckBox) findViewById(R.id.chkSleepOver);
          CheckBox chkOverWrite_Existing_Availability=(CheckBox) findViewById(R.id.chkOverWrite_Existing_Availability);
        SleepOver= chkSleepOver.isChecked();
        OverWrite_Existing_Availability= chkOverWrite_Existing_Availability.isChecked();

        if (chkDay1.isChecked()) Week_Days = Week_Days + "1";
        if (chkDay2.isChecked()) Week_Days = Week_Days + "2";
        if (chkDay3.isChecked()) Week_Days = Week_Days + "3";
        if (chkDay4.isChecked()) Week_Days = Week_Days + "4";
        if (chkDay5.isChecked()) Week_Days = Week_Days + "5";
        if (chkDay6.isChecked()) Week_Days = Week_Days + "6";
        if (chkDay7.isChecked()) Week_Days = Week_Days + "7";


        if (chkPermanent.isChecked()) {

            if (Cycle.equalsIgnoreCase("FORTNIGHTLY") ){

                if (chkWeek1.isChecked() && !chkWeek2.isChecked()){
                    Weeks="-W1";
                }else if (!chkWeek1.isChecked() && chkWeek2.isChecked()){
                    Weeks="-W2";
                }
            }

            else if (Cycle.equalsIgnoreCase("MONTHLY") ){

                if (chkWeek3.isChecked() && !chkWeek4.isChecked()){
                    Weeks="-W3";
                }else if (!chkWeek3.isChecked() && chkWeek4.isChecked()){
                    Weeks="-W4";
                }


            }

        }

        Button dtpStart = (Button) findViewById(R.id.dtpStartTime);
        Button dtpEnd = (Button) findViewById(R.id.dtpEndTime);
        TextView txtNote = (TextView) findViewById(R.id.txtNote);

        String format = "yyyy/MM/dd HH:mm";


        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        //  SimpleDateFormat sdf = new SimpleDateFormat(format);



        Start_Time = dtpStart.getText().toString();
        End_Time = dtpEnd.getText().toString();

        Note = "-"; //txtNote.getText().toString();
/*
        if (check_valid_note(Note)) {
            Note = SQLSafe(Note);
        }
*/
    if (chkPermanent.isChecked()) {

        r_Date="1900/01/01";


    }else{

        Button dtpStartDate = (Button) findViewById(R.id.dtpStartDate);
        Button dtpEndDate = (Button) findViewById(R.id.dtpEndDate);
        r_Date = dtpStartDate.getTag().toString();
        E_Date = dtpEndDate.getTag().toString();

        Calendar cal = Calendar.getInstance();

            cal.setTime(new Date()); // Now use today date.
            Date todate = cal.getTime();

            String curr_time = "";
            curr_time = sdf.format(todate);
            Date dt = null, dt2 = null;

            try {

                dt = sdf.parse(r_Date + " " + Start_Time);
                dt2 = sdf.parse(E_Date + " " + Start_Time);

                if (dt2.before(dt) ) {
                    Toast.makeText(getApplicationContext(), "Invalid  Dates, Please Enter valid Dates", Toast.LENGTH_LONG).show();
                    return;
                }


                if (  SleepOver && !dt2.after(dt)) {
                    Toast.makeText(getApplicationContext(), "Invalid Dates, please enter valid Start and End Dates for sleepover shift", Toast.LENGTH_LONG).show();
                    return;
                }

            } catch (Exception ex) {
            }

            if (dt.before(todate) ) {
                Toast.makeText(getApplicationContext(), "Invalid  Dates, Availability can only be added after " + todate, Toast.LENGTH_LONG).show();
                return;
            }

            try {
                Date date1 = sdf.parse(curr_time);
                dt = sdf.parse(r_Date + " " + Start_Time);

                if (dt.before(date1)) {
                    Toast.makeText(getApplicationContext(), "Invalid Roster Date, Request must be date after " + todate, Toast.LENGTH_LONG).show();
                    return;
                }
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "Invalid Roster Date, Request must be date after " + todate, Toast.LENGTH_LONG).show();
                return;
            }

            try {

                dt = sdf.parse(r_Date + " " + Start_Time);
                dt2 = sdf.parse(r_Date + " " + End_Time);



                if (dt2.before(dt) &&  !SleepOver ) {
                    Toast.makeText(getApplicationContext(), "Invalid Date/Time, please enter valid Start and End Times\n Please tick sleepover for sleepover shift", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!dt2.before(dt) &&  SleepOver ) {
                    Toast.makeText(getApplicationContext(), "Invalid Time, please enter valid Start and End Times for sleepover shift", Toast.LENGTH_LONG).show();
                    return;
                }

            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "Invalid Time, please enter valid Start and End Times", Toast.LENGTH_LONG).show();
                return;
            }

        r_Date=r_Date + "," + E_Date;
    }

    String Availability_Type="";

    if (chkPermanent.isChecked()){
        Availability_Type="Availabilty Type : Permanent \n" + "Cycle : " + Cycle;
    }else{
        Availability_Type="Availabilty Type : Date Wise \n From : " + r_Date.replace(","," To ");
    }

        Email_Message =" An Availability is added by Staff " + StaffCode + "\n" +
                Availability_Type+ "\n" + "Start Time : " + Start_Time + "\n" +
                " End Time : " + End_Time + "\n Note : " + Note;


        try {

        new MyAsyncClass_Add_Roster().execute();


        } catch (Exception ex) {
        }
    }

    void add_Roster2() {
        try {
            String command;
            String Mobile_Device= Build.MANUFACTURER + "-" + android.os.Build.MODEL  ;
            Note="-";

            command = "\nR" + "`" + StaffCode + "`" + r_Date + "`" + Start_Time + "`" + End_Time + "`" + Note + "`" + Cycle+Weeks + "`" + Week_Days + "`" + Mobile_Device;
            set_Updates(command);
            RecordNo = "111";

        } catch (Exception ex) {
            Tost_Message("Operation not done due to some error\n" + ex.toString());
        }
    }

    void add_Roster() {

        String URL6 = root + "/TimeSheet.asmx?op=Add_Roster_Availability";
        String SOAP_ACTION6 = "https://tempuri.org/Add_Roster_Availability";
        String METHOD_NAME6 = "Add_Roster_Availability";

        String Mobile_Device= Build.MANUFACTURER + "-" + android.os.Build.MODEL  ;

        try {


            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME6);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug = true;

            PropertyInfo pi1 = new PropertyInfo();
            pi1.setName("Carer_Code");
            pi1.setValue(getSecurityToken() + StaffCode);
            request.addProperty(pi1);

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("Roster_Date");
            pi2.setValue(r_Date);
            request.addProperty(pi2);

            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("Start_Time");
            pi3.setValue(Start_Time);
            request.addProperty(pi3);

            PropertyInfo pi4 = new PropertyInfo();
            pi4.setName("End_Time");
            pi4.setValue(End_Time);
            request.addProperty(pi4);

            PropertyInfo pi5 = new PropertyInfo();
            pi5.setName("Note");
            pi5.setValue(Note);
            request.addProperty(pi5);

            PropertyInfo pi6 = new PropertyInfo();
            pi6.setName("Cycle");
            pi6.setValue(Cycle+Weeks);
            request.addProperty(pi6);

            PropertyInfo pi7 = new PropertyInfo();
            pi7.setName("Week_Days");
            pi7.setValue(Week_Days);
            request.addProperty(pi7);

            PropertyInfo pi8 = new PropertyInfo();
            pi8.setName("Mobile_Device");
            pi8.setValue(Mobile_Device);
            request.addProperty(pi8);

            PropertyInfo pi9 = new PropertyInfo();
            pi9.setName("SleepOver");
            pi9.setValue(SleepOver);
            pi9.setType(Boolean.TYPE);
            request.addProperty(pi9);


            PropertyInfo pi10 = new PropertyInfo();
            pi10.setName("OverWrite_Existing_Availability");
            pi10.setValue(OverWrite_Existing_Availability);
            pi10.setType(Boolean.TYPE);
            request.addProperty(pi10);



            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION6, envelope);

            SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
            RecordNo = result.toString();

        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
        }


    }

    public void set_Updates(String command) throws IOException {
        // File froot = null;
        try {
            // check for SDcard

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File filein = null;
            File fileDir = null;
            String Command2 = "";

            if (command.indexOf("\n", 1) > 0) {
                Command2 = command.replace("\n", "%%");
                Command2 = "\n" + Command2.substring(2);
            } else
                Command2 = command;


            String state = Environment.getExternalStorageState();

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
            Tost_Message("Operation not done due to some error");
        }

    }

    private String set_leading_zero(int val, int size) {
        String new_val = String.valueOf(val);
        size = size - (new_val.length());
        for (int i = 0; i < size; i++) {
            new_val = "0" + new_val;
        }
        return new_val;
    }

    void get_Server() {

        Bundle b = getIntent().getExtras();
        root = b.getString("root");
        OperatorId = b.getString("OperatorId");
        Security_Token = b.getString("Security_Token");
        StaffCode = b.getString("StaffCode");
        server_available = b.getBoolean("Server_Available");
        Roster_Date = b.getString("Roster_Date");

        Allow_OverWrite_Existing_Availability= b.getString("Allow_OverWrite_Existing_Availability");

        Coordinator_Email = b.getString("Coordinator_Email");
        EmailUnavailabilityNotification = b.getString("EmailUnavailabilityNotification");

    }

    String getSecurityToken() {

        String Val = OperatorId + "$" + Security_Token + "$";
        return Val;
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

    public boolean isOnline(Context context) {

        ConnectivityManager connectivityManager;

        boolean connected = false;
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            if (server_available == false && connected) {

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

    public String SQLSafe(String sValue) {

        String sAns = null;
        sAns = sValue.replace("'", "''");
        sAns = sAns.replace("\"", "''");
        return (sAns);

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

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }


    class MyAsyncClass_Add_Roster extends AsyncTask<Void, Void, Void> {


        LoadingDialog pDialog;
        boolean local = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Roster_Availability_Main.this);
            pDialog.setMessage("Please wait while processing....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {

            try {

                if (server_available)
                    add_Roster();
                else
                    add_Roster2();

            } catch (Exception ex) {

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();

            if (RecordNo.equalsIgnoreCase("0") || RecordNo.equalsIgnoreCase(""))
                Tost_Message("Due to some issue, Request is not processed successfully");
            if (RecordNo.equalsIgnoreCase("-1") )
                Tost_Message("Due to Shift Conflicts, Roster Availability Request is not processed successfully");
            else {
                Tost_Message("Availability Time request has been processed successfully");
                //if (EmailUnavailabilityNotification.equals("0") || EmailUnavailabilityNotification.equalsIgnoreCase("true"))
                 send_local_email(Roster_Availability_Main.this, "Staff Availability Added",Email_Message, Coordinator_Email);

                finish();

            }

           // if(!user_settings.getSuppressEmailOnRosterNote().equalsIgnoreCase("True"))
             //   send_email_alert(messgas, title);
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
    public void composeEmail(String addresses, String subject, String body) {
        String uriText = "mailto:" + addresses +
                "?subject=" + subject +
                "&body=" + body;
        Uri uri = Uri.parse(uriText);
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(uri);
        startActivity(Intent.createChooser(sendIntent, "Send Email").addFlags( Intent.FLAG_ACTIVITY_NEW_TASK));
    }
    void send_local_email(Context mActivity, String Email_Subject, String email_msg, String To_Email){

        composeEmail(To_Email,Email_Subject,email_msg);
        if(1==1) return;

        String subject = Email_Subject;
        try{



          /*  Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, email_msg);
            intent.setData(Uri.parse("mailto:"+To_Email)); // or just "mailto:" for blank
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
            startActivity(intent);*/

            String[] TO = {To_Email};
            Uri uri = Uri.parse("mailto:" + To_Email)
                    .buildUpon()
                    .appendQueryParameter("subject", subject)
                    .appendQueryParameter("body", email_msg)
                    .build();
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
           // startActivity(Intent.createChooser(emailIntent, "Send mail..."));

            Intent Email = new Intent(Intent.ACTION_SEND);
            Email.setType("text/email");
            Email.putExtra(Intent.EXTRA_EMAIL, new String[] { To_Email });
            Email.putExtra(Intent.EXTRA_SUBJECT, subject);
            Email.putExtra(Intent.EXTRA_TEXT, email_msg);
            startActivity(Intent.createChooser(Email, "SEND EMAIL TO" ));

        }catch (Exception ex) {
            // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }

    }


}
