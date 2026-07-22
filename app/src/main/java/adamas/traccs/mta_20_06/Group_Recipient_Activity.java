package adamas.traccs.mta_20_06;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Group_Recipient_Activity extends AppCompatActivity {

    ArrayList<? extends Parcelable> lst_grps;
    private ArrayList<Roster_Info> rosters = null;
    final String PREFS_NAME = "MTAPrefs";
    SharedPreferences settings = null;
    Group_Recipient Current_Group_recipient;
    File froot;
    Context context;
    List<Group_Recipient> lst;
    String RecordNo;
    Roster_Info Current_roster;
    String Existing_Attendees="";
    int Attendee_code=300;
    String Recipient="";
    String PersonId="";
    Boolean Server_Available=false;
    String Roster_Date="";
    String mode="datewise";
    int Current_Index=0;
    String OperatorId;
    String root="";
    String Security_Token="";
    XmlData xml ;
    String curr_Date = "";
    boolean view_only=false;
    boolean flagDecoration=true;
    String AllowCaseNoteEntry="false",AllowOPNoteEntry="false",AllowClinicalNoteEntry="false", AllowRosterNoteEntry="false";
    String EnableViewNoteCases="";
    User_Settings user_settings = null;
    String TA_TRAVELDEFAULT = "-";

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
      //  if(1==1) return false;

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        super.onOptionsItemSelected(item);

        switch(item.getItemId())
        {
            case android.R.id.home:
                // API 5+ solution
               // settings.edit().putBoolean("Refresh_OP_Note_data",true).commit();
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
        try{
          //  new MyAsyncClass2().execute();
        }catch(Exception ex){}




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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group__recipient_);
        setupActionBar();
        context=this.getApplicationContext();
        settings = getSharedPreferences(PREFS_NAME, 0);
        Bundle bundle= getIntent().getExtras();
        xml = new XmlData(context);
        RecordNo= bundle.getString("RecordNo");
        Roster_Date= bundle.getString("Roster_Date");
        root= settings.getString("root","");
        OperatorId= settings.getString("OperatorId","");
        Security_Token= settings.getString("Security_Token","");
        mode= bundle.getString("mode");
        Server_Available=settings.getBoolean("Server_Available",false);
        Roster_Date=Roster_Date.substring(0,10);


      //
       // Current_roster=this.rosters.get(0);
        Current_roster= getRoster(RecordNo);
        if (Current_roster == null) {
            Tost_Message("Roster not found");
            finish();
            return;
        }
        if (mode.equalsIgnoreCase("datewise")) {
            lst = Current_roster.get_group_Recipients();
        }else {
           // load_roster(Roster_Date);
           // Current_roster = (Roster_Info) this.rosters.stream().filter(item -> item.getRecordNo().toString().equals(RecordNo)).findAny().orElse(null);

            lst = getTimeWiseGroupShifts(Current_roster.get_group_Recipients(),Current_roster.getStart_Time(),Current_roster.getRoster_Date(), Current_roster.getServiceSetting());
            Current_roster.set_group_Recipients2(lst);
        }
        if (lst!=null) {
            for (int i = 0; i < lst.size(); i++) {
                Existing_Attendees = Existing_Attendees + "," + lst.get(i).getAccountNo();
            }
        }
        try{



            AllowCaseNoteEntry= settings.getString("AllowCaseNote","0");
            AllowOPNoteEntry= settings.getString("AllowOPNote","0");
            AllowRosterNoteEntry= settings.getString("AllowRosterNoteEntry","0");
            AllowClinicalNoteEntry= settings.getString("AllowClinicalNoteEntry","0");
            AllowClinicalNoteEntry= settings.getString("AllowClinicalNoteEntry","0");
            EnableViewNoteCases=settings.getString("EnableViewNoteCases","00000");


        }catch(Exception ex){}

        try {
            Calendar c = Calendar.getInstance();
            Date dt = c.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            curr_Date = sdf.format(dt);




        } catch (Exception ex) {
        }
        long days =    calculateDays(Roster_Date, curr_Date);

        //view_only=true;

        // if( days==1 || days==0 )

        //view_only=false; //!curr_Date.equals(Roster_Date);

        view_only=false;
        flagDecoration=true;
        RecyclerView listview= findViewById(R.id.lstRecipients);
        if (lst!=null) {
            ListArrayAdapter mAdapter = new ListArrayAdapter(this, lst, view_only);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            listview.setLayoutManager(mLayoutManager);
            if (flagDecoration) {
                listview.setItemAnimator(new DefaultItemAnimator());
                listview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
                flagDecoration = false;
            }

            listview.setAdapter(mAdapter);
        }else{
            Tost_Message("No Recipients found in this booking");
        }
       /* ListArrayAdapter adapter;
        if (lst!=null) {
             adapter = new ListArrayAdapter(this, lst,view_only);
            listview.setAdapter(adapter);
        }
*/

        Get_User_Settings2();

    Button btnAddNew= findViewById(R.id.btnAddNew);
    btnAddNew.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {


                //   if (!user_settings.getToDate().equals(RosterDate) ) {
                if (!curr_Date.equals(Roster_Date)) {
                    Tost_Message("Cannot perform action on past or future booking");
                    return;
                }
                Intent addNewAttendees = new Intent(Group_Recipient_Activity.this, AddNewAttendees.class);
                Bundle b= new Bundle();
                b.putString("RecordNo", Current_roster.getRecordNo());
                b.putString("Existing_Attendees", Existing_Attendees);
                b.putString("ServiceType", Current_roster.getServiceType());

                addNewAttendees.putExtras(b);

                startActivityForResult(addNewAttendees,Attendee_code);


            } catch (Exception e) {

            }

        }
    });

    }

    boolean Get_User_Settings2() {
        boolean status = true;
        user_settings = new User_Settings();
        try {
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();
            File fileDir = null;
            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = new File(fileDir, "User_Settings.xml");


            //  rosters=new ArrayList<Roster_Info>();
            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("getUser_SettingsResult");

                String current_element = "";

                if (nList == null) return false;

                try {
                    Node nNode = nList.item(0);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        try {
                            current_element = eElement.getElementsByTagName("AllowSetTime").item(0).getTextContent();

                            user_settings.setAllowSetTime(current_element);

                        } catch (Exception e) {
                        }
                        try {
                            user_settings.setTMMode(eElement.getElementsByTagName("TMMode").item(0).getTextContent());
                        } catch (Exception e) {
                        }

                        try {
                            user_settings.setMobileFutureLimit(eElement.getElementsByTagName("MobileFutureLimit").item(0).getTextContent());
                        } catch (Exception e) {
                        }

                        try {
                            user_settings.setAllowPicUpload(eElement.getElementsByTagName("AllowPicUpload").item(0).getTextContent());
                        } catch (Exception e) {
                        }
                        try {
                            current_element = eElement.getElementsByTagName("Apply_Goe_Location_Setting").item(0).getTextContent();
                            user_settings.setApply_Goe_Location_Setting(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("KMAgainstTravelOnly").item(0).getTextContent();
                            user_settings.setKMAgainstTravelOnly(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("mobilegeocodelimit").item(0).getTextContent();
                            user_settings.setMobilegeocodelimit(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("StaffLocationUpdateInterval").item(0).getTextContent();
                            user_settings.setStaffLocationUpdateInterval(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("AllowIncidentEntry").item(0).getTextContent();
                            user_settings.setAllowIncidentEntry(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("AllowTravelEntry").item(0).getTextContent();
                            user_settings.setAllowTravelEntry(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("AllowClientNoteEntry").item(0).getTextContent();
                            user_settings.setAllowClientNoteEntry(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("AllowRosterNoteEntry").item(0).getTextContent();
                            user_settings.setAllowRosterNoteEntry(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("StaffCode").item(0).getTextContent();
                            user_settings.setStaffCode(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("AllowRegisterSign").item(0).getTextContent();
                            user_settings.setAllowRegisterSign(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("ToDate").item(0).getTextContent();
                            user_settings.setToDate(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("Time").item(0).getTextContent();
                            user_settings.setTime(current_element);
                        } catch (Exception e) {
                        }




                        try {
                            current_element = eElement.getElementsByTagName("ShowClientPhoneInApp").item(0).getTextContent();
                            user_settings.setShowClientPhoneInApp(current_element);

                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("TA_TRAVELDEFAULT").item(0).getTextContent();
                            TA_TRAVELDEFAULT = current_element;
                            user_settings.setTA_TRAVELDEFAULT(current_element);
                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("CheckAlertInterval").item(0).getTextContent();
                            user_settings.setCheckAlertInterval(current_element);

                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("AllowOPNote").item(0).getTextContent();
                            user_settings.setAllowOPNote(current_element);

                        } catch (Exception e) {
                        }


                        try {
                            current_element = eElement.getElementsByTagName("AllowClinicalNoteEntry").item(0).getTextContent();
                            user_settings.setAllowClinicalNoteEntry(current_element);

                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("AllowCaseNote").item(0).getTextContent();
                            user_settings.setAllowCaseNote(current_element);

                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("EnableViewNoteCases").item(0).getTextContent();
                            user_settings.set_EnableViewNoteCases(current_element);

                        } catch (Exception e) {
                        }

                        try {
                            current_element = eElement.getElementsByTagName("AllowIncidentNote").item(0).getTextContent();
                            user_settings.setAllowIncidentNote(current_element);

                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("AppUsesSMTP").item(0).getTextContent();
                            user_settings.setAppUsesSMTP(current_element);

                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("AllowLeaveEntry").item(0).getTextContent();
                            user_settings.setAllowLeaveEntry(current_element);

                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("EnableRosterDelivery").item(0).getTextContent();
                            user_settings.setEnableRosterDelivery(current_element);

                        } catch (Exception e) {
                        }


                        try {

                            current_element = eElement.getElementsByTagName("GeolocateEnabled").item(0).getTextContent();
                            user_settings.setGeolocateEnabled(current_element);

                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("HideGeolocation").item(0).getTextContent();
                            user_settings.setHideGeolocation(current_element);

                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("Enable_Shift_End_Alarm").item(0).getTextContent();
                            user_settings.setEnable_Shift_End_Alarm(current_element);

                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("LeaveLeadTime").item(0).getTextContent();
                            user_settings.setLeaveLeadTim(current_element);

                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("AllowViewGoalPlans").item(0).getTextContent();
                            user_settings.setAllowViewGoalPlans(current_element);

                        } catch (Exception e) {
                        }



                        try {

                            current_element = eElement.getElementsByTagName("AllowViewGoalPlans").item(0).getTextContent();
                            user_settings.setAllowViewGoalPlans(current_element);


                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("ViewClientCareplans").item(0).getTextContent();
                            user_settings.set_ViewClientCareplans(current_element);


                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("ViewClientDocuments").item(0).getTextContent();
                            user_settings.set_ViewClientDocuments(current_element);


                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("RestrictTravelSameDay").item(0).getTextContent();
                            user_settings.set_RestrictTravelSameDay(current_element);


                        } catch (Exception e) {
                        }

                        try {

                            current_element = eElement.getElementsByTagName("HIdeCancelButton").item(0).getTextContent();
                            user_settings.setHIdeCancelButton(current_element);

                            try {

                                current_element = eElement.getElementsByTagName("EnableRosterAvailability").item(0).getTextContent();
                                user_settings.setEnableRosterAvailability(current_element);


                            } catch (Exception e) {
                            }

                            try {

                                current_element = eElement.getElementsByTagName("SuppressEmailOnRosterNote").item(0).getTextContent();
                                user_settings.setSuppressEmailOnRosterNote(current_element);


                            } catch (Exception e) {
                            }

                            try {

                                current_element = eElement.getElementsByTagName("GoogleAPI_Key").item(0).getTextContent();
                                user_settings.set_GoogleAPI_Key(current_element);


                            } catch (Exception e) {
                            }

                            try {

                                current_element = eElement.getElementsByTagName("AllowMTASaveUserPass").item(0).getTextContent();
                                user_settings.setAllowMTASaveUserPass(current_element);


                            } catch (Exception e) {
                            }


                            try {
                                current_element = eElement.getElementsByTagName("EmailUnavailabilityNotification").item(0).getTextContent();
                                user_settings.setEmailUnavailabilityNotification(current_element);
                            } catch (Exception e) {
                            }

                            try {

                                current_element = eElement.getElementsByTagName("HideAddress").item(0).getTextContent();
                                user_settings.setHideAddress(current_element);
                                // HideAddress=current_element;

                            } catch (Exception e) {
                            }

                            try {
                                current_element = eElement.getElementsByTagName("UserOverrideIncidentEmail").item(0).getTextContent();
                                user_settings.setUserOverrideIncidentEmail(current_element);
                            } catch (Exception e) {
                            }
                            try {
                                current_element = eElement.getElementsByTagName("OverrideIncientEmail").item(0).getTextContent();
                                user_settings.setOverrideIncientEmail(current_element);
                            } catch (Exception e) {
                            }



                            try {
                                current_element = eElement.getElementsByTagName("UserOverrideIncidentEmail").item(0).getTextContent();
                                user_settings.setUserOverrideIncidentEmail(current_element);

                            } catch (Exception e) {
                            }
                            try {
                                current_element = eElement.getElementsByTagName("OverrideIncientEmail").item(0).getTextContent();
                                user_settings.setOverrideIncientEmail(current_element);

                            } catch (Exception e) {
                            }


                        } catch (Exception e) {
                        }

                    }
                } catch (Exception aE) {
                } finally {
                }

            }

        } catch (Exception e) {
            status = false;
        }


        return status;
    }


    public long calculateDays(String startDate, String endDate) {
        Date sDate = new Date(startDate);
        Date eDate = new Date(endDate);
        Calendar cal3 = Calendar.getInstance();
        cal3.setTime(sDate);
        Calendar cal4 = Calendar.getInstance();
        cal4.setTime(eDate);
        return daysBetween(cal3, cal4);
    }
    public long daysBetween(Calendar startDate, Calendar endDate) {
        Calendar date = (Calendar) startDate.clone();
        long daysBetween = 0;
        while (date.before(endDate)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Attendee_code){
            Current_roster= getRoster(RecordNo);

            lst=Current_roster.get_group_Recipients();

            //ListView listview= findViewById(R.id.lstRecipients);
            RecyclerView listview= findViewById(R.id.lstRecipients);
            ListArrayAdapter adapter;
            if (lst!=null) {
               // adapter = new ListArrayAdapter(this, lst,view_only);
                //listview.setAdapter(adapter);
                flagDecoration=true;

                ListArrayAdapter mAdapter = new ListArrayAdapter(this, lst,view_only);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                listview.setLayoutManager(mLayoutManager);
                if(flagDecoration) {
                    listview.setItemAnimator(new DefaultItemAnimator());
                    listview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
                    flagDecoration=false;
                }

                listview.setAdapter(mAdapter);
            }
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


    private class ListArrayAdapter extends RecyclerView.Adapter<ListArrayAdapter.ViewHolder> {
        List<Group_Recipient> lst_grps;
        Group_Recipient grp;
        ListArrayAdapter.ViewHolder viewHolder;
        Context context;
        boolean view_only;
        View itemView;
        public ListArrayAdapter(Context contxt, List<Group_Recipient> objects, boolean viewOnly) {
         //   super();
            // super(context, android.R.layout.simple_list_item_1, objects);
            this.lst_grps=objects;
            this.context=contxt;
            this.view_only=viewOnly;
        }
        public class ViewHolder extends RecyclerView.ViewHolder{


            TextView txtReceipientName;
            Button btnOptions;
            CheckBox chkSelect;
            Button btnAlert, btnOPNote;
            Button btnMap;


             ViewHolder(View view) {
                super(view);
               // viewHolder.index = position;
                txtReceipientName = (TextView) view.findViewById(R.id.txtRecipientDetail);
                chkSelect = (CheckBox) view.findViewById(R.id.checkBox);
                btnOptions =  view.findViewById(R.id.btnMenu);
                btnAlert = (Button) view.findViewById(R.id.btnAlert);
                btnOPNote = (Button) view.findViewById(R.id.btnOPNote);

                btnMap = (Button) view.findViewById(R.id.btnMap);
            }
        }




        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_row_recipient_group2, parent, false);
            return new ListArrayAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {

                try{


                    grp = lst_grps.get(position);
                   final int Index=position;
                    Current_Index=position;

                    String detail_Text="";

                    detail_Text=grp.getStart_Time() + " : " + grp.getName() + "<br/><b>" + "Appointment: </b>" + grp.getAppointment();

                    if (!grp.getPickUpAddress1().equalsIgnoreCase("-") && !grp.getPickUpAddress1().equalsIgnoreCase(""))
                        detail_Text=detail_Text + "<br><b>Pick Up:</b><br>" +grp.getPickUpAddress1();
                    if (!grp.getDropOffAddress1().equalsIgnoreCase("-") && !grp.getDropOffAddress1().equalsIgnoreCase(""))
                        detail_Text=detail_Text + "<br><b>Drop Off:</b><br>" +grp.getDropOffAddress1();
                    if (!grp.getMobility().equalsIgnoreCase("-") && !grp.getMobility().equalsIgnoreCase(""))
                        detail_Text=detail_Text + "<br><b>Mobility: </b>" +grp.getMobility();

                    detail_Text=detail_Text.replace("AUSTRALIA","");
                    detail_Text=detail_Text.replace("NSW","");
                    viewHolder.txtReceipientName.setText( HtmlCompat.fromHtml(detail_Text,0));


                    viewHolder.chkSelect.setChecked(grp.isSelected());
                    if ( (AllowOPNoteEntry.equals("0")|| AllowOPNoteEntry.equalsIgnoreCase("false"))
                            &&  (AllowCaseNoteEntry.equals("0")|| AllowCaseNoteEntry.equalsIgnoreCase("false"))
                            &&  (AllowRosterNoteEntry.equals("0")|| AllowRosterNoteEntry.equalsIgnoreCase("false"))
                            &&  (AllowClinicalNoteEntry.equals("0")|| AllowClinicalNoteEntry.equalsIgnoreCase("false"))
                            &&    (Integer.parseInt( EnableViewNoteCases.substring(0, 1))==0 || Integer.parseInt( EnableViewNoteCases.substring(1, 2))==0
                            || Integer.parseInt( EnableViewNoteCases.substring(4, 5))==0)) {

                        viewHolder.btnOPNote.setVisibility(View.GONE);

                    }
                    if (view_only){
                        viewHolder.btnOPNote.setVisibility(View.GONE);
                        viewHolder.btnAlert.setVisibility(View.GONE);
                    }
                    if (grp.getPickUpAddress1().length()<3 && grp.getDropOffAddress1().length()<3){
                        viewHolder.btnMap.setVisibility(View.GONE);
                    }
                    viewHolder.btnOPNote.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            try {
                              //  Group_Recipient grp = null;

                                Current_Group_recipient = lst_grps.get(Index);

                                grp = Current_Group_recipient;
                                Current_roster.setClient_code(grp.getAccountNo());
                                Current_roster.setActual_Client_Code(grp.getAccountNo());

                                //  grp=lst_grps.get(position);
                                //  Roster_Info r = getRoster(grp.getRecordNo());

                               /* if (r != null) {

                                    Current_roster = r;
                                } else {
                                    Current_roster.setClient_code(grp.getAccountNo());
                                    Current_roster.setActual_Client_Code(grp.getAccountNo());
                                }*/


                                String Params = grp.getAccountNo();// + "," + grp.getRecordNo()+"," +grp.getCoordinator_Email()+","+ position;

                                if (Server_Available) {
                                    //MyAsyncClass_load_Op_Case_Note().execute(Params);
                                }
                                show_OP_Case_Note(v);

                            } catch (Exception e) {
                            }

                        }
                    });
                    viewHolder.btnAlert.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            grp = lst_grps.get(Index);
                            Intent alrt = new Intent(v.getContext(), Group_Alert_Activity.class);
                            Bundle b= new Bundle();
                            //b.putString("Alerts",r.getRunsheetAlerts() + "\n" + r.getGroup_Alerts());
                            b.putString("PersonId",grp.get_Personid());
                            b.putString("OperatorId",OperatorId);
                            b.putBoolean("load_group_alerts",true);


                            alrt.putExtras(b);
                            context.startActivity(alrt);
                            //v.setEnabled(false);

                        }
                    });
                    viewHolder.btnMap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            grp = lst_grps.get(Index);
                            show_map_Options(v,grp);

                        }
                    });

                    viewHolder.btnOptions.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try{

                                Current_Group_recipient = lst_grps.get(Index);
                                PersonId=Current_Group_recipient.get_Personid();
                                Recipient=Current_Group_recipient.getAccountNo();
                                if (!curr_Date.equals(Roster_Date)) {
                                    //  Tost_Message("Cannot perform action on inactive booking");
                                    //  Tost_Message("Cannot perform action on inactive booking\n" + curr_Date + "\n" +Roster_Date);
                                    // return;
                                }
                                show_Group_Recipients(v);
                            } catch (Exception e) { }
                        }

                    });



                    viewHolder.chkSelect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            boolean sts = ((CheckBox) v).isChecked();

                            if (view_only){
                                Tost_Message("cannot perform action on past or future booking");
                                ((CheckBox) v).setChecked(!sts);
                                return;
                            }

                            // grp=(Group_Recipient) chkSelect.getTag();

                            String Audit_Query="";
                            lst_grps.get(Index).setSelected(sts);
                            lst_grps.get(Index).setStatus(""+sts);
                            grp = lst_grps.get(Index);
                            MainActivity.Group_Recipient_Rocord_Update=true;
                            if (sts) {
                                try {
                                    Audit_Query = ";Insert into [Audit] (Operator,ActionDate,ActionOn,WhoWhatCode,TraccsUser,AuditDescription)" +
                                            " values('" + OperatorId +"',getDate(),'MTA-ROSTER','"+ grp.getRecordNo() +"','"+OperatorId+"','APPROVED IN APP') ";


                                    set_Updates("Update Roster set status = 2 where status = 1 and RecordNo=" + grp.getRecordNo() + Audit_Query + "\n");
                                    if (!Server_Available)
                                        xml.Update_Roster_Node(grp.getRecordNo(), "Status", "2");
                                }catch(Exception ex){}
                            }else{
                                try {
                                    Audit_Query = ";Insert into [Audit] (Operator,ActionDate,ActionOn,WhoWhatCode,TraccsUser,AuditDescription)" +
                                            " values('" + OperatorId +"',getDate(),'MTA-ROSTER','"+ grp.getRecordNo() +"','"+OperatorId+"','Un-APPROVED IN APP') ";

                                    set_Updates("Update Roster set status = 1 where status = 2 and RecordNo=" + grp.getRecordNo() + Audit_Query + "\n");
                                    if (!Server_Available)
                                        xml.Update_Roster_Node(grp.getRecordNo(), "Status", "1");
                                }catch(Exception ex){}
                            }

                        }

                    });


                }catch (Exception ex){}

            }

        @Override
        public int getItemCount() {
            return lst_grps.size();
        }

    }




    Roster_Info getRoster(String RecordNo2) {
        Roster_Info rst = null;

        try {

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            String filePath = froot.getAbsolutePath()+"/.server/traccs.xml";

            File fXmlFile = null;

            fXmlFile = new File( filePath);

            if (fXmlFile.exists()) {

                DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
                fac.setNamespaceAware(false);
                fac.setValidating(false);
                fac.setFeature("http://xml.org/sax/features/namespaces", false);
                fac.setFeature("http://xml.org/sax/features/validation", false);
                //  fac.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
                //fac.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
                DocumentBuilder dBuilder = fac.newDocumentBuilder();
                // DocumentBuilder dBuilder;

                //dBuilder = dbFactory.newDocumentBuilder();


                //DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                //DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
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

                                       // if (lst.size()>0)
                                         //   if (lst.get(lst.size()-1).getAccountNo().equals(val) ) continue;

                                        rg.setAccountNo(val);
                                        val = eElm.getElementsByTagName("RecordNo").item(0).getTextContent();
                                        rg.setRecordNo(val);
                                        val = eElm.getElementsByTagName("PickUpAddress1").item(0).getTextContent();
                                        rg.setPickUpAddress1(val);

                                        try{
                                        val = eElm.getElementsByTagName("DropOffAddress1").item(0).getTextContent();
                                        rg.setDropOffAddress1(val);
                                        }catch(Exception ex){ rg.setDropOffAddress1("-");}

                                        try {
                                            val = eElm.getElementsByTagName("Status").item(0).getTextContent();
                                            rg.setStatus(val);
                                            rg.setSelected(val.equalsIgnoreCase("2"));
                                        }catch(Exception ex){
                                            rg.setStatus("1");
                                            rg.setSelected(false);}

                                       // lst.add(rg);
                                        val = eElm.getElementsByTagName("Coordinator_Email").item(0).getTextContent();
                                        rg.setCoordinator_Email(val);

                                        val = eElm.getElementsByTagName("RECIPIENT_CoOrdinator").item(0).getTextContent();
                                        rg.setRECIPIENT_CoOrdinator(val);

                                        try {
                                            val = eElm.getElementsByTagName("Personid").item(0).getTextContent();
                                            rg.set_Personid(val);
                                        }catch(Exception ex){ rg.set_Personid("0");}

                                        try {
                                            val = eElm.getElementsByTagName("Mobility").item(0).getTextContent();
                                            rg.setMobility(val);
                                        }catch(Exception ex){ rg.setMobility("-");}
                                        try {
                                            val = eElm.getElementsByTagName("Start_Time").item(0).getTextContent();
                                            rg.setStart_Time(val);
                                        }catch(Exception ex){ rg.setStart_Time("-");}

                                        try {
                                            val = eElm.getElementsByTagName("AppointmentTime").item(0).getTextContent();
                                            rg.setAppointment(val);
                                        }catch(Exception ex){ rg.setAppointment("-");}

                                        try {
                                            val = eElm.getElementsByTagName("ServiceSettings").item(0).getTextContent();
                                            rg.setServiceSetting(val);
                                        }catch(Exception ex){ rg.setServiceSetting("-");}

                                        try {
                                            val = eElm.getElementsByTagName("roster_date").item(0).getTextContent();
                                            rg.setRoster_Date(val);
                                        }catch(Exception ex){ rg.setRoster_Date("-");}


                                        if (!lst.contains(rg))
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



    public void load_roster(String RosterDate){

        int indx=0;
        int color=0;
        int j=0;
        int totalblock=0;
        int item_count=0;

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
                org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Roster_Info");

                // Toast.makeText(getApplicationContext(),"node list = " + nList.getLength() , Toast.LENGTH_LONG).show();

                if (nList==null) return;
                //textMsg.setText("\nCurrent Element :" + nList.item(0).getNodeName());

                String str_date="";
                //TextView dtp=(TextView)findViewById(R.id.TextDate);
                //String sel_date[]=dtp.getTag().toString().split("/");
                //String selected_date= RosterDate ;// Integer.parseInt( sel_date[2]) + "/" + Integer.parseInt( sel_date[1]) + "/" + sel_date[0];

                //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                //Date date = new Date(dtp.getTag().toString());
                //selected_date = dateFormat.format(date);

                int index=0;

                settings.edit().putString("Started_Job_No", "0").commit();
                for ( int tmp = 0; tmp< nList.getLength(); tmp++) {
                    try{

                        Node  nNode = nList.item(tmp);
                        //  textMsg.setText(nNode.getTextContent());
                        //  textMsg.setText("\nCurrent Element :" + nNode.getNodeName());

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            str_date= eElement.getElementsByTagName("Roster_Date").item(0).getTextContent();


                            String code=eElement.getElementsByTagName("Carer_code").item(0).getTextContent();
                            //code=objText.decryptText(code);

                            DateFormat dateFormatN = new SimpleDateFormat("dd/MM/yyyy");

                            DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");

                            Date date2 = new Date(str_date);
                            str_date = dateFormat2.format(date2);

                            //r_Date=dateFormatN.format(date2);
//                         //   RosterDate=r_Date;
                            String Roster_Type="";
                            String InfoOnly="0";
                            try {
                                // textMsg.setText(textMsg.getText() + "\n" + str_date + " = " + selected_date + " " +  selection.getText().equals(code));
                                Roster_Type = eElement.getElementsByTagName("Roster_Type").item(0).getTextContent();
                                InfoOnly = eElement.getElementsByTagName("InfoOnly").item(0).getTextContent();
                            }catch(Exception ex){}
                            //Integer.parseInt(Roster_Type)>2




                            if ( str_date.equals(RosterDate))
                            {
                                rst= new Roster_Info();
                                rst.setRecordNo(eElement.getElementsByTagName("RecordNo").item(0).getTextContent());
                                //  btnLoadMoreBooking= btnLoadMoreBooking=(Button)findViewById(R.id.btnLoadMoreBooking);
                                rst.setIndex(index);
                                index=index+1;

                                if(code.trim().equals("BOOKED")) {
                                    rst.setBook_date("Book Date : " + Roster_Date + " Time: ");
                                }else {
                                    rst.setBook_date("");
                                }


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

                                if (eElement.getElementsByTagName("Notes").item(0).getTextContent()!=null) {
                                    rst.setNotes(eElement.getElementsByTagName("Notes").item(0).getTextContent());
                                } else{
                                    rst.setNotes("-");}

                                rst.setRoster_type(eElement.getElementsByTagName("Roster_Type").item(0).getTextContent());
                                rst.setStarted(eElement.getElementsByTagName("Started").item(0).getTextContent());

                                if (eElement.getElementsByTagName("Completed").item(0).getTextContent()!=null) {
                                    rst.setCompleted(eElement.getElementsByTagName("Completed").item(0).getTextContent());
                                }else {
                                    rst.setCompleted("0");
                                }

                                if (rst.getStarted().equals("1") && rst.getCompleted().equals("0") ){
                                    settings.edit().putString("Started_Job_No", rst.getRecordNo()).commit();

                                }

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

                                try {

                                    rst.set_ACCOUNTINGIDENTIFIER(eElement.getElementsByTagName("ACCOUNTINGIDENTIFIER").item(0).getTextContent());
                                } catch (Exception ex) {
                                }

                                try{

                                    int type= Integer.parseInt(rst.getRoster_type());


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

                                try{

                                    rst.setDisable_Shift_Start_Alarm(eElement.getElementsByTagName("Disable_Shift_Start_Alarm").item(0).getTextContent());
                                }catch(Exception ex){}

                                try{

                                    rst.setDisable_Shift_End_Alarm(eElement.getElementsByTagName("Disable_Shift_End_Alarm").item(0).getTextContent());
                                }catch(Exception ex){}

                                try{

                                    rst.setTA_MultiShift(eElement.getElementsByTagName("TA_MultiShift").item(0).getTextContent());
                                }catch(Exception ex){}

                                try{

                                    rst.setRunsheetAlerts(eElement.getElementsByTagName("RunsheetAlerts").item(0).getTextContent());
                                }catch(Exception ex){}
                                try{

                                    rst.setPersonId(eElement.getElementsByTagName("PersonId").item(0).getTextContent());
                                }catch(Exception ex){}
                                try{

                                    rst.setExcludeFromAppLogging(eElement.getElementsByTagName("ExcludeFromAppLogging").item(0).getTextContent());
                                }catch(Exception ex){}


                                try {
                                    String val2="";
                                    rst.setActual_Start_Time("");
                                    rst.setActual_End_Time("");

                                    val2 = eElement.getElementsByTagName("Actual_Start").item(0).getTextContent();
                                    rst.setActual_Start_Time(val2);

                                    val2 = eElement.getElementsByTagName("Actual_End").item(0).getTextContent();
                                    rst.setActual_End_Time(val2);

                                }catch(Exception ex){ }


                                try{
                                    if(eElement.getElementsByTagName("Group_Recipients")==null) continue;
                                    List<Group_Recipient> lst= new ArrayList<Group_Recipient>() ;
                                    Group_Recipient rg=null;
                                    NodeList nlst=eElement.getElementsByTagName("Group_Recipient");
                                    for(int ind=0; ind<nlst.getLength(); ind++) {
                                        Node  nNod = nlst.item(ind);
                                        Element eElm = (Element) nNod;
                                        rg= new Group_Recipient();

                                        String val="";
                                        val = eElm.getElementsByTagName("Name").item(0).getTextContent();
                                        rg.setName(val);
                                        val = eElm.getElementsByTagName("AccountNo").item(0).getTextContent();
                                        rg.setAccountNo(val);
                                        val = eElm.getElementsByTagName("RecordNo").item(0).getTextContent();
                                        rg.setRecordNo(val);
                                        val = eElm.getElementsByTagName("RosterType").item(0).getTextContent();
                                        rg.setRosterType(val);
                                        val = eElm.getElementsByTagName("PickUpAddress1").item(0).getTextContent();
                                        rg.setPickUpAddress1(val);

                                        try{
                                            val = eElm.getElementsByTagName("DropOffAddress1").item(0).getTextContent();
                                            rg.setDropOffAddress1(val);
                                        }catch(Exception ex){ rg.setDropOffAddress1("-");}
                                        try {
                                            val = eElm.getElementsByTagName("Status").item(0).getTextContent();
                                            rg.setStatus(val);
                                        }catch(Exception ex){ rg.setStatus("1");}

                                        val = eElm.getElementsByTagName("Coordinator_Email").item(0).getTextContent();
                                        rg.setCoordinator_Email(val);

                                        val = eElm.getElementsByTagName("RECIPIENT_CoOrdinator").item(0).getTextContent();
                                        rg.setRECIPIENT_CoOrdinator(val);

                                        try {
                                            val = eElm.getElementsByTagName("Personid").item(0).getTextContent();
                                            rg.set_Personid(val);
                                        }catch(Exception ex){ rg.set_Personid("0");}
                                        try {
                                            val = eElm.getElementsByTagName("Mobility").item(0).getTextContent();
                                            rg.setMobility(val);
                                        }catch(Exception ex){ rg.setMobility("-");}
                                        try {
                                            val = eElm.getElementsByTagName("Start_Time").item(0).getTextContent();
                                            rg.setStart_Time(val);
                                        }catch(Exception ex){ rg.setStart_Time("-");}

                                        try {
                                            val = eElm.getElementsByTagName("ServiceSettings").item(0).getTextContent();
                                            rg.setServiceSetting(val);
                                        }catch(Exception ex){ rg.setServiceSetting("-");}

                                        try {
                                            val = eElm.getElementsByTagName("roster_date").item(0).getTextContent();
                                            rg.setRoster_Date(val);
                                        }catch(Exception ex){ rg.setRoster_Date("-");}
                                        lst.add(rg);

                                    }
                                    rst.set_group_Recipients(lst);

                                }catch(Exception ex){}

                            }



                        }else { continue; }
                    }  catch (Exception aE) {  }

                    if (rosters.contains(rst)) continue;
                    if (rst!=null) {
                        item_count = item_count + 1;
                        rosters.add(rst);
                    }

                }

                setDayWiseGroupShift();



            } else { }//textMsg.setText("Xml file not found"); }
        } catch (Exception e) {
            Tost_Message(e.toString());
        }
    }

    void setDayWiseGroupShift(){
        Roster_Info rost;

        List<Group_Recipient> lst= new ArrayList<Group_Recipient>() ;
        for(int r=0; r< this.rosters.size(); r++) {
            rost= this.rosters.get(r);
            if (rost.get_group_Recipients()==null) continue;

           // if (lst.size()<=0)
            lst=getSettingWiseGroupShifts(rost.getRoster_Date(), rost.getServiceSetting());
            rost.set_group_Recipients2(lst);
        }

    }

    List<Group_Recipient>  getSettingWiseGroupShifts(String date, String settings){
        Roster_Info rst;
        List<Group_Recipient> lst= new ArrayList<Group_Recipient>() ;
        List<Group_Recipient> flst=null ;

        flst=new ArrayList<Group_Recipient>() ;

        for(int i=0; i< this.rosters.size(); i++) {
            rst= this.rosters.get(i);
            if (rst.get_group_Recipients()==null) continue;


            lst=rst.get_group_Recipients();
            for (int j=0; j<lst.size(); j++){
                if (flst.contains(lst.get(j))) continue;
                if (rst.getRoster_Date().equals(date)  && rst.getServiceSetting().equalsIgnoreCase(settings))
                    flst.add(lst.get(j));
            }

        }

        return flst;
    }
    List<Group_Recipient>  getTimeWiseGroupShifts(List<Group_Recipient> lst,String time, String date,String serviceSettings){
        Group_Recipient rst;

        List<Group_Recipient> flst=null ;

        flst=new ArrayList<Group_Recipient>() ;
        if (lst == null) return flst;
    for (int j=0; j<lst.size(); j++){
        rst=lst.get(j);
        if (flst.contains(lst.get(j))) continue;
        if (rst.getStart_Time().equals(time) && rst.getRoster_Date().equals(date) && rst.getServiceSetting().equalsIgnoreCase(serviceSettings))
            flst.add(lst.get(j));

    }



        return flst;
    }
    void show_Map(Context context,String Simple_Address) {
        try {
            // if (!Server_Available) return;

            String lat_long = "";
            String lat_long_current = "";


            String link = "http://maps.google.com/maps?f=q&hl=en&geocode=&time=&date=&ttype=&saddr=Your Location&daddr=" + Simple_Address;
            String uri = String.format(Locale.ENGLISH, link);
            Intent intent2 = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse(uri));
            intent2.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

            context.startActivity(intent2);


        } catch (Exception ex) {
            // ((TextView) findViewById(R.id.txtAcknowledge)).setText("Operation not done due to some server error\n" + ex.toString());
        }
    }
    void show_Map(Context context,String Source_Address, String Destination_Address) {
        try {
            // if (!Server_Available) return;

            String lat_long = "";
            String lat_long_current = "";


            String link = "http://maps.google.com/maps?f=q&hl=en&geocode=&time=&date=&ttype=&saddr=" + Source_Address +"&daddr=" + Destination_Address;
            String uri = String.format(Locale.ENGLISH, link);
            Intent intent2 = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse(uri));
            intent2.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

            context.startActivity(intent2);


        } catch (Exception ex) {
            // ((TextView) findViewById(R.id.txtAcknowledge)).setText("Operation not done due to some server error\n" + ex.toString());
        }
    }
    void show_Map_with_muliple_points(Context context,String Source_Address, String Destination_Address) {
        try {
            // if (!Server_Available) return;

            String link ="http://maps.google.com/maps?f=q&hl=en&geocode=&time=&date=&ttype=&saddr=Your Location&daddr="+Source_Address+"+to:"+Destination_Address+"&zoom=14&views=traffic";

            String uri = String.format(Locale.ENGLISH, link);
            Intent intent2 = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse(uri));
            intent2.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

            context.startActivity(intent2);

        } catch (Exception ex) {
            // ((TextView) findViewById(R.id.txtAcknowledge)).setText("Operation not done due to some server error\n" + ex.toString());
        }
    }
    void show_Group_Recipients(View view){

        int NoPermission=0;
        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.recipient_group_options);
        dialog.setTitle("Group Recipeints");
        dialog.setCanceledOnTouchOutside(false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        dialog.getWindow().setAttributes(lp);



        Button btnViewDocument = (Button) dialog.findViewById(R.id.btnViewDocument);
        if (settings.getString( "ViewClientDocuments","0").equalsIgnoreCase("0") ||
                settings.getString( "ViewClientDocuments","0").equalsIgnoreCase("false"))
        {
          // Tost_Message("Blocked from view client documents see your TRACCS administrator");
            btnViewDocument.setVisibility(View.GONE);
            NoPermission++;
        }else{
            NoPermission=0;
        }
        // if button is clicked, close the custom dialog
        btnViewDocument.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                try{


                        View_Documents(v.getContext());


                }catch(Exception ex){}

                dialog.dismiss();
            }

        });
        Button btnClientNote = (Button) dialog.findViewById(R.id.btnClientNote);
        //btnClientNote.setVisibility(View.GONE);
        if (AllowRosterNoteEntry.equalsIgnoreCase("false") || AllowRosterNoteEntry.equalsIgnoreCase("0")) btnClientNote.setVisibility(View.GONE);

        if ( (AllowOPNoteEntry.equals("0")|| AllowOPNoteEntry.equalsIgnoreCase("false"))
                &&  (AllowCaseNoteEntry.equals("0")|| AllowCaseNoteEntry.equalsIgnoreCase("false"))
                &&  (AllowRosterNoteEntry.equals("0")|| AllowRosterNoteEntry.equalsIgnoreCase("false"))
                &&  (AllowClinicalNoteEntry.equals("0")|| AllowClinicalNoteEntry.equalsIgnoreCase("false"))
                &&    (Integer.parseInt( EnableViewNoteCases.substring(0, 1))==0 || Integer.parseInt( EnableViewNoteCases.substring(1, 2))==0
                || Integer.parseInt( EnableViewNoteCases.substring(4, 5))==0)) {

                 btnClientNote.setVisibility(View.GONE);
                NoPermission++;
        }else{
            NoPermission--;
        }

        btnClientNote.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                try {
                    Group_Recipient grp = null;


                    grp = Current_Group_recipient;
                    //  grp=lst_grps.get(position);
                    Roster_Info r = getRoster(grp.getRecordNo());

                    if (r != null) {

                        Current_roster = r;
                    } else {
                        Current_roster.setClient_code(grp.getAccountNo());
                        Current_roster.setActual_Client_Code(grp.getAccountNo());
                    }


                    String Params = grp.getAccountNo();// + "," + grp.getRecordNo()+"," +grp.getCoordinator_Email()+","+ position;

                    if (Server_Available) {
                        //MyAsyncClass_load_Op_Case_Note().execute(Params);
                    }
                    show_Client_Note(v);

                } catch (Exception e) {
                }
            }
        });

        Button btnRecordIncident = (Button) dialog.findViewById(R.id.btnRecordIncident);
        if (settings.getString( "AllowIncidentEntry","0").equalsIgnoreCase("0")||
                settings.getString( "AllowIncidentEntry","0").equalsIgnoreCase("false"))
        {
           // Tost_Message("Blocked from Add Incident see your TRACCS administrator");
           // return;

            btnRecordIncident.setVisibility(View.GONE);
            NoPermission++;
        }else{
            NoPermission--;
        }
        btnRecordIncident.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {
                       //Current_Group_recipient = lst_grps.get(position);
                         Set_Incident(v.getContext());


                    dialog.dismiss();

                } catch (Exception e) {

                }

            }
        });

        //test
        Button btnTravelClaim = (Button) dialog.findViewById(R.id.btnTravelClaim);
        if (settings.getString( "AllowTravelEntry","0").equalsIgnoreCase("0")||
                settings.getString( "AllowTravelEntry","0").equalsIgnoreCase("false"))
        {
            btnTravelClaim.setVisibility(View.GONE);
            NoPermission++;
        }else{
            NoPermission--;
        }
        btnTravelClaim.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {
                    //Current_Group_recipient = lst_grps.get(position);
                    Call_Travel(v.getContext());
                    dialog.dismiss();

                } catch (Exception e) {

                }

            }
        });
        //test


        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {


                    dialog.dismiss();

                } catch (Exception e) {

                }

            }
        });

        if (NoPermission>=3)
            Tost_Message("No permission");
        else
            dialog.show();
    }
    void show_map_Options(View view, Group_Recipient g){

        int NoPermission=0;
        final Group_Recipient grp=g;
        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.map_options);
        dialog.setTitle("Map Options");
        dialog.setCanceledOnTouchOutside(false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        dialog.getWindow().setAttributes(lp);

        TextView txtPickUp =  dialog.findViewById(R.id.txtPickUp);
        // if button is clicked, close the custom dialog
        txtPickUp.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try{
                    show_Map(v.getContext(),grp.getPickUpAddress1());

                }catch(Exception ex){}

                dialog.dismiss();
            }

        });
        TextView txtDropOff =  dialog.findViewById(R.id.txtDropOff);
        txtDropOff.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {
                    show_Map(v.getContext(),grp.getDropOffAddress1());
                } catch (Exception e) {
                }
                dialog.dismiss();
            }
        });
        TextView txtPickUpDropOff =  dialog.findViewById(R.id.txtPickUpDropOff);
        txtPickUpDropOff.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {
                    show_Map_with_muliple_points(v.getContext(),grp.getPickUpAddress1(),grp.getDropOffAddress1());
                   // show_Map_with_muliple_points(v.getContext(),"Lahore","Multan");
                } catch (Exception e) {
                }
                dialog.dismiss();
            }
        });
        TextView txtCancel =  dialog.findViewById(R.id.txtCancel);
        txtCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialog.dismiss();
            }
        });

            dialog.show();
    }
    public void View_Documents(Context context){
        try{
            Intent i= new Intent(context,Client_Documents.class);
            Bundle b= new Bundle();
            b.putString("PersonId",PersonId);
            b.putString("Recipient",Recipient);
            i.putExtras(b);
            startActivity(i);

        }catch(Exception ex){ }

    }

    private void show_Client_Note(View v){
        try{
            Bundle bundle= new Bundle();


            bundle.putBoolean("Server_Available",Server_Available);
            bundle.putString("RecordNo",Current_Group_recipient.getRecordNo());
            bundle.putString("Roster_Date",Roster_Date);
            bundle.putString("AccountNo",Current_Group_recipient.getAccountNo());
            bundle.putString("PersonId",  Current_Group_recipient.get_Personid());
            bundle.putString("Recipient",Current_Group_recipient.getAccountNo());
            bundle.putString("Job_Time","");
            bundle.putString("Main_Op_Note", "SVCNOTE");
            bundle.putString("ServiceType", Current_roster.getServiceType());


            //OP_Case_Note_Activity.Refresh_OP_Note_data_single=true;
            Intent intent2= new Intent(v.getContext(),Add_Service_Note.class);
            intent2.putExtras(bundle);
            startActivity(intent2);
            // Toast.makeText(v.getContext(),  "No Issue", Toast.LENGTH_LONG).show();
        }catch(Exception ex){Toast.makeText(v.getContext(),  ex.toString() , Toast.LENGTH_LONG).show();}
    }
    private void show_OP_Case_Note(View v){
        try{
            Bundle bundle= new Bundle();


            bundle.putBoolean("Server_Available",Server_Available);
            bundle.putString("RecordNo",Current_Group_recipient.getRecordNo());
            bundle.putString("Roster_Date",Roster_Date);
            bundle.putString("AccountNo",Current_Group_recipient.getAccountNo());
            bundle.putString("PersonId",  Current_Group_recipient.get_Personid());
            bundle.putString("Recipient",Current_Group_recipient.getAccountNo());
            bundle.putString("Job_Time","");
            bundle.putString("Main_Op_Note", "OPNOTE");
           // OP_Case_Note_Activity.Refresh_OP_Note_data_single=true;
            Intent intent2= new Intent(v.getContext(),OP_Case_Note_Activity.class);
            intent2.putExtras(bundle);
            startActivity(intent2);
            // Toast.makeText(v.getContext(),  "No Issue", Toast.LENGTH_LONG).show();
        }catch(Exception ex){Toast.makeText(v.getContext(),  ex.toString() , Toast.LENGTH_LONG).show();}
    }
    public void Set_Incident(Context view) {

        Intent i= new Intent(Group_Recipient_Activity.this,Record_Incident.class);
        Bundle b= new Bundle();
        b.putString("RecordNo", RecordNo);
        b.putInt("Index", Current_Index);
        b.putString("PersonId", Current_Group_recipient.get_Personid());
        b.putString("AccountNo", Current_Group_recipient.getAccountNo());
        b.putString("Service_Setting", Current_roster.getServiceSetting());
        b.putString("ServiceType",Current_roster.getServiceType());
        b.putString("Program", Current_roster.getProgram());
        i.putExtras(b);

        view.startActivity(i);
    }

   public void Call_Travel(Context v) {
        try {

            Intent intent2 = new Intent(v, Travel.class);

            Bundle bundle = new Bundle();
            bundle.putString("root", root);
            bundle.putString("Personid",  Current_Group_recipient.get_Personid());
            bundle.putString("recordNo", Current_Group_recipient.getRecordNo());
            bundle.putString("AccountNo", Current_Group_recipient.getAccountNo());
            bundle.putString("RosterDate",Current_Group_recipient.getRoster_Date());
            bundle.putString("EndTime", "");
            bundle.putString("StaffCode", settings.getString("StaffCode","ABC"));

            if (Server_Available == true)
                bundle.putString("Server", "True");
            else
                bundle.putString("Server", "False");
            //  bundle.putString("Timesheet",TimesheetReights);

            bundle.putString("OperatorId", OperatorId);
            bundle.putString("Security_Token", Security_Token);

            bundle.putString("TA_TRAVELDEFAULT",TA_TRAVELDEFAULT);
            bundle.putString("MyOwnCarStatus","1");
            bundle.putString("RestrictTravelSameDay",user_settings.get_RestrictTravelSameDay());

            intent2.putExtras(bundle);
            startActivity(intent2);
        } catch (Exception ex) {
            //  lblTime.setText("Operation not done due to some server error\n" + ex.toString());
        }
    }

    class MyAsyncClass2 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Group_Recipient_Activity.this);
            pDialog.setMessage("Please wait while updating server updates  ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                // Add subject, Body, your mail Id, and receiver mail Id.
                // sender.sendMail("Subject", " body", "from Mail", "to mail");
                Upload_Updates up= new Upload_Updates(root,OperatorId,Security_Token,context);
                up.Upload_Updates_on_server();
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

        }
    }


}
