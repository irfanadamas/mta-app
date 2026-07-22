/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adamas.traccs.mta_20_06;



import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.*;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlSerializer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileReader;
import java.io.BufferedReader;

/**
 *
 * @author arshad
 */
public  class Travel extends AppCompatActivity {

    String root="";
    private final String NAMESPACE = "https://tempuri.org/";
    private  String URL = root  + "/TimeSheet.asmx?op=Add_Travel_Roster";
    private final String SOAP_ACTION =  "https://tempuri.org/Add_Travel_Roster";
    private final String METHOD_NAME = "Add_Travel_Roster";

    private boolean Server_Available;
    private String Personid;
    File froot = null;
    Context context;
    String RecipientDocFolder ="";
    String recordNo;
    String RestrictTravelSameDay="false";
    String Roster_Date="";


    Timer timer;
    String OperatorId;
    String Security_Token;
    String AccountNo="";
    String TA_TRAVELDEFAULT="-";
    String MyOwnCarStatus="0";
    TextView txtMsg;
    TextView  tvStartKm;
    TextView  txtEndKm;
    TextView  txtKM;
    String EndTime="";
    Button btnSave;
    String StaffCode="";
    /**
     * Called when the activity is first created.
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
                imageBack.setVisibility(View.INVISIBLE);
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
                      //  onBackPressed();
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
    public static String convertToTitleCaseIteratingChars(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ToDo add your GUI initialization code here
        setContentView(R.layout.travel);
        setupActionBar();
        this.context=this.getApplicationContext();
        set_server_Ip();
        txtMsg=(TextView)findViewById(R.id.lblMsg);
        tvStartKm=(EditText)findViewById(R.id.txtStartKm);
        txtEndKm=(EditText)findViewById(R.id.txtEndKm);
        txtKM=(EditText)findViewById(R.id.txtKM);
        //  txtEndKm.setEnabled(false);
        final  CheckBox chkCharge = (CheckBox)findViewById(R.id.chkCharge);
        final  RadioButton chkCar = (RadioButton)findViewById(R.id.chkMyCar);

        //  TextView tvCharge=(TextView)findViewById(R.id.tvCharge);
        TextView tvTravel=(TextView)findViewById(R.id.tvTravel);
        tvTravel.setText("Travel Claim (" + convertToTitleCaseIteratingChars(TA_TRAVELDEFAULT) + ")");

        EditText   txtRosterNote= (EditText)findViewById(R.id.txtRosterNote);
        //setMultiLineCapSentencesAndDoneAction(txtRosterNote);

        Default_Setting(TA_TRAVELDEFAULT);
      //  View rl_view6=(View)findViewById(R.id.rl_view6);
     //   View rl_view7=(View)findViewById(R.id.rl_view7);
        //final   ToggleButton  toggleButtonCharge=(ToggleButton)findViewById(R.id.toggleButtonCharge);

        if (AccountNo.equalsIgnoreCase("!MULTIPLE") || AccountNo.equalsIgnoreCase("!INTERNAL")){
            //  rl_view6.setVisibility(View.GONE);
           // rl_view7.setVisibility(View.GONE);
            chkCharge.setChecked(false);
            tvTravel.setText("TRAVEL CLAIM (TRAVEL BETWEEN)");
            RadioButton radioTravelBetween = (RadioButton)findViewById(R.id.radioTravelBetween);
            radioTravelBetween.setChecked(true);
            RadioButton radioWithin = (RadioButton)findViewById(R.id.radioWithin);
            //  radioWithin.setChecked(false);
            //  radioWithin.setEnabled(false);\

           // toggleButtonCharge.setEnabled(false);
            chkCharge.setChecked(false);
           // toggleButtonCharge.setChecked(false);
            // toggleButtonCharge.setBackgroundResource(R.drawable.background_red);
        }
        //  set_Record(recordNo);
        // Resolution_Setting();



         btnSave=(Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                boolean EntryExists=false;
                if (Server_Available==false){
                    EntryExists= checkOfflineEntry();
                    // return;
                }
                if (XmlData.check_TravelClaim(EndTime,Roster_Date, AccountNo) || EntryExists){

                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(Travel.this).inflate(R.layout.rl_travel_confirmation, viewGroup, false);
                    AlertDialog.Builder builder = new AlertDialog.Builder(Travel.this);
                    builder.setView(dialogView);
                    final AlertDialog alertDialog = builder.create();
                    Button dialogButtonNo =  dialogView.findViewById(R.id.btnNo);
                    // if button is clicked, close the custom dialog
                    dialogButtonNo.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    Button dialogButtonYes =  dialogView.findViewById(R.id.btnYes);
                    // if button is clicked, close the custom dialog
                    dialogButtonYes.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            adding_travel();
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();


                       /* String commandText="Travel claim already saved.  Do you want to continue?";
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(txtMsg.getContext());
                        alertDialogBuilder.setTitle("");
                        alertDialogBuilder
                                .setMessage(commandText)
                                .setCancelable(false)
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        return;

                                    }
                                })
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        adding_travel();

                                    }
                                });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.getWindow().setGravity(10);
                        // show it
                        alertDialog.show();*/
                }else{
                    adding_travel();
                }

            }});

        final TextView txtStartKm= (TextView)findViewById(R.id.txtStartKm);
        txtStartKm.setFocusable(true);
        txtStartKm.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (tvStartKm.getText().toString().equals(""))
                    txtEndKm.setEnabled(true);

                txtMsg.setText("");
                if (txtEndKm.getText().toString().equals("")|| tvStartKm.getText().toString().equals("")){

                    txtKM.setEnabled(true);
                    return;
                }
                txtKM.setEnabled(false);
                double dist=0;
                dist=Double.parseDouble(txtEndKm.getText().toString().trim())-Double.parseDouble(tvStartKm.getText().toString().trim())  ;
                txtKM.setText(""+dist);
              //  txtKM.requestFocus();
            }
        });
        txtStartKm.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        // Identifier of the action. This will be either the identifier you supplied,
                        // or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                      try{
                        if (actionId == EditorInfo.IME_ACTION_SEARCH
                                || actionId == EditorInfo.IME_ACTION_DONE
                                || event.getAction() == KeyEvent.ACTION_DOWN
                               ) {
                            if (tvStartKm.getText().toString().equals(""))
                                txtEndKm.setEnabled(true);

                            txtMsg.setText("");
                            if (txtEndKm.getText().toString().equals("")|| tvStartKm.getText().toString().equals("")){

                                txtKM.setEnabled(true);
                                return false;
                            }
                            txtKM.setEnabled(false);
                            double dist=0;
                            dist=Double.parseDouble(txtEndKm.getText().toString().trim())-Double.parseDouble(tvStartKm.getText().toString().trim())  ;
                            txtKM.setText(""+dist);
                           // txtKM.requestFocus();
                            return false;
                        }
                      }catch(Exception ex){}
                        // Return true if you have consumed the action, else false.
                        return false;
                    }
                });

        final TextView txtEndKm2= (TextView)findViewById(R.id.txtEndKm);
        txtEndKm2.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                txtMsg.setText("");
                if (txtEndKm.getText().toString().equals("")|| tvStartKm.getText().toString().equals("")){

                    txtKM.setEnabled(true);
                    return;
                }
                txtKM.setEnabled(false);
                double dist=0;
                dist=Double.parseDouble(txtEndKm.getText().toString().trim())-Double.parseDouble(tvStartKm.getText().toString().trim())  ;
                txtKM.setText(""+dist);
              //  txtKM.requestFocus();
            }
        });
        txtEndKm2.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        try {
                            // Identifier of the action. This will be either the identifier you supplied,
                            // or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                            if (actionId == EditorInfo.IME_ACTION_SEARCH
                                    || actionId == EditorInfo.IME_ACTION_DONE
                                    || event.getAction() == KeyEvent.ACTION_DOWN
                                    ) {

                                txtMsg.setText("");
                                if (txtEndKm.getText().toString().equals("") || tvStartKm.getText().toString().equals("")) {

                                    txtKM.setEnabled(true);
                                    return false;
                                }
                                txtKM.setEnabled(false);
                                double dist = 0;
                                dist = Double.parseDouble(txtEndKm.getText().toString().trim()) - Double.parseDouble(tvStartKm.getText().toString().trim());
                                txtKM.setText("" + dist);
                              //  txtKM.requestFocus();
                                return false;
                            }
                        }catch(Exception ex){}
                        // Return true if you have consumed the action, else false.
                        return false;
                    }
                });
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                runOnUiThread(new Runnable() {
                    public void run() {

                        // textMsg.setText("Working with Online Mode");
                        // textMsg.setText("Working with Offline Mode");
                        Server_Available= isNetworkAvailable(getApplicationContext());
                    }
                });

            }
        }, 1000, 1000);
    }
    @Override
    public void onUserInteraction() {

        super.onUserInteraction();
        MainActivity.idle_time = 0;
        // Toast.makeText(getApplicationContext(), "User has intracted idle_time=" + MainActivity.idle_time , Toast.LENGTH_LONG).show();

    }
    void setMultiLineCapSentencesAndDoneAction(EditText txt) {
        txt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        txt.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        txt.setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES  );
    }
    private void Resolution_Setting(){
        try{
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int wwidth = displaymetrics.widthPixels;
            //double percent=heightPixels/widthPixels
            int final_width=0;
            if (wwidth<400)
                final_width=(int)wwidth*40/100;
            else
                final_width=(int)wwidth*50/100;

            ((TextView) findViewById(R.id.tvTravel)).setWidth(wwidth);



        }catch(Exception ex){}
    }

    boolean checkOfflineEntry(){
        boolean exist=false;
        // EditText   txtRosterNote= (EditText)findViewById(R.id.txtRosterNote);

        froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

        File filein = null;
        File fileDir = null;
        String state = Environment.getExternalStorageState();
        fileDir = new File(froot.getAbsolutePath() + "/.server/");
        filein = new File(fileDir, "Updates.txt");
        if (filein.exists()) {
            try {
                FileReader fileReader = new FileReader(filein);
                BufferedReader buf = new BufferedReader(fileReader);
                String line = buf.readLine();
                while(line!=null && line!="\n" ) {
                    if (line.length()>0) {
                        //  txtRosterNote.setText(txtRosterNote.getText() + "\n" + line + " -> " + line.charAt(0) + " , " + line.contains(recordNo));
                        if ((line.charAt(0) == 'V') && line.contains(recordNo)) {
                            //"V" + "`" +  RecordNo
                            exist = true;
                            break;
                        }
                    }
                    line = buf.readLine();
                }
            } catch (Exception ex) {
            } finally {

            }
        }
        return exist;
    }


    String getSecurityToken(){

        String Val= OperatorId  + "$" + Security_Token + "$";
        return Val;
    }
    private String getTodaysDate() {

        final Calendar c = Calendar.getInstance();
        int todaysDate = (c.get(Calendar.YEAR) * 10000) +
                ((c.get(Calendar.MONTH) + 1) * 100) +
                (c.get(Calendar.DAY_OF_MONTH));
        Log.w("DATE:", String.valueOf(todaysDate));
        return (String.valueOf(todaysDate));

    }
    public String SQLSafe(String sValue)
    {

        String sAns = null;
        sAns = sValue.replace("'", "''");
        sAns = sAns.replace("\"", "''");
        return (sAns);

    }



    class MyAsyncClass extends AsyncTask<String, Void, String> {

        LoadingDialog pDialog;
        String r_value="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Travel.this);
            pDialog.setMessage("Please wait while processing....");
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... mApi) {

            try {

                r_value=Add_Roster(mApi[0]);

            } catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
            return r_value;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.cancel();
            if(result.equalsIgnoreCase("")) result="0";
            if (Double.parseDouble(result.trim())>0)
            {
                Toast.makeText(getApplicationContext(), "Travel Added Successfully", Toast.LENGTH_LONG).show();
                txtMsg.setText("Travel Added Successfully");
                try{
                    //IRFAN 25/06/2025
                    new MyAsyncClass2().execute();
                }catch(Exception ex){}
                finish();
            }else
            {
                Toast.makeText(getApplicationContext(), "Roster Record is not added properly due to some missing travel default settings or some server issue", Toast.LENGTH_LONG).show();
                txtMsg.setText("Roster Record is not added properly due to some missing travel default settings or some server issue");
            }
        }
    }

    class MyAsyncClass3 extends AsyncTask<String, Void, String> {

        LoadingDialog pDialog;
        String return_value="true";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Travel.this);
            pDialog.setMessage("Please wait while processing....");
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... mApi) {

            try {

                Make_update(mApi[0]);

            }catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
            return return_value;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.cancel();
            Toast.makeText(getApplicationContext(), "Travel Added Successfully", Toast.LENGTH_LONG).show();
            txtMsg.setText("Travel Added Successfully");
            MainActivity.form_resumed=false;
        }
    }
    public void Make_update(String command)
    {
        try
        {
            if (Server_Available==false){
                try{
                    set_Updates(command);
                    return;
                }catch(Exception ex){
                    txtMsg.setText(ex.toString());
                }

            }
            String URL5 = root + "/TimeSheet.asmx?op=Make_update";
            String SOAP_ACTION5 =  "https://tempuri.org/Make_update";
            String METHOD_NAME5 = "Make_update";
            if ((!command.equals("")) && command!=null ){
                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME5);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
                androidHttpTransport.debug =true;

                PropertyInfo pi=new PropertyInfo();
                pi.setName("command");

                pi.setValue(command);
                request.addProperty(pi);

                PropertyInfo pi2=new PropertyInfo();
                pi2.setName("Fontra");
                pi2.setValue(getSecurityToken() + "99");
                request.addProperty(pi2);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.dotNet=true;
                envelope.setOutputSoapObject(request);
                // Make the soap call.
                androidHttpTransport.call(SOAP_ACTION5, envelope);
                SoapPrimitive  result =(SoapPrimitive)envelope.getResponse();

            }
        }catch(Exception ex){
            txtMsg.setText(ex.toString());
        }

    }
    public void ShowDialog_for_KM(View v, String commandText ) {
        try{

            final View final_view=v;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

            alertDialogBuilder.setTitle("--------- KM Alert ----------");
            alertDialogBuilder
                    .setMessage(commandText)
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            // Add_Roster(recordNo);
                            try {
                                new MyAsyncClass().execute(recordNo);
                            }catch(Exception ex){}
                            dialog.cancel();

                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
            alertDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            // show it
            alertDialog.show();

        }catch(Exception ex) {}
    }

    public String  Add_Roster(String RecordNo)
    {
        EditText tvStartKm2=(EditText)findViewById(R.id.txtStartKm);
        EditText  txtEndKm2=(EditText)findViewById(R.id.txtEndKm);
        EditText  txtKM2=(EditText)findViewById(R.id.txtKM);
        txtMsg=(TextView)findViewById(R.id.lblMsg);
        //  txtEndKm.setEnabled(false);
        String result_value="0";
        String Mobile_Device= Build.MANUFACTURER + "-" + android.os.Build.MODEL  ;

        String Charge_Type="";
        String Travel_Type="";
        CheckBox chk = (CheckBox)findViewById(R.id.chkCharge);
        if (chk.isChecked())
            Charge_Type="Chargeable";
        else
            Charge_Type="Non-Chargeable";

        RadioButton radioWithin = (RadioButton)findViewById(R.id.radioWithin);
        if (radioWithin.isChecked())
            Travel_Type="TRAVEL WITHIN";

        RadioButton radioTravelBetween = (RadioButton)findViewById(R.id.radioTravelBetween);
        if (radioTravelBetween.isChecked())
            Travel_Type="TRAVEL BETWEEN";

        boolean MyCar=false;
        RadioButton chkMyCar = (RadioButton)findViewById(R.id.chkMyCar);


        if (chkMyCar.isChecked()) MyCar=true;

        double startKM=0,EndKM=0, distance=0;
        try{
            if (tvStartKm2.getText().toString().trim().equals(""))
                startKM=0;
            else
                startKM= Double.parseDouble(tvStartKm2.getText().toString().trim());

            if (txtEndKm2.getText().toString().trim().equals("") )
                EndKM=Double.parseDouble(txtKM2.getText().toString().trim());
            else
                EndKM=Double.parseDouble(txtEndKm2.getText().toString().trim());
        }catch(Exception ex){   txtMsg.setText("Value Conversion : " + ex.toString());
            return "false";}

        distance= EndKM-startKM;

        EditText   txtRosterNote= (EditText)findViewById(R.id.txtRosterNote);
        //setMultiLineCapSentencesAndDoneAction(txtRosterNote);
        //  String notes=txtRosterNote.getText().toString().replace("\'","\'\'");
        String notes=txtRosterNote.getText().toString();
        notes= SQLSafe(notes);
        if (notes.equalsIgnoreCase("")) notes="-";

        if (Server_Available==false){

            try
            {

                String command;

                command= "V" + "`" +  RecordNo + "`" + distance + "`" + Travel_Type + "`" + Charge_Type + "`" + startKM + "`" + EndKM + "`" + notes.replace("\n","~") + "`" + MyCar + "`" +Mobile_Device ;
                set_Updates(command);

                result_value="11";

            }catch(Exception ex){
                ((TextView) findViewById(R.id.textMsg)).setText("Operation not done due to some server error\n" + ex.toString());
            }



        }else{


            try{
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
                pi5.setValue(""+startKM);
                request.addProperty(pi5);

                PropertyInfo pi6=new PropertyInfo();
                pi6.setName("EndKM");
                pi6.setValue(""+EndKM);
                request.addProperty(pi6);

                PropertyInfo pi7=new PropertyInfo();
                pi7.setName("Notes");
                pi7.setValue(notes);
                request.addProperty(pi7);

                PropertyInfo pi8=new PropertyInfo();
                pi8.setName("OwnVehicle");
                pi8.setValue(MyCar);
                request.addProperty(pi8);

                PropertyInfo pi9=new PropertyInfo();
                pi9.setName("Mobile_Device");
                pi9.setValue(Mobile_Device);
                request.addProperty(pi9);

                PropertyInfo pi10=new PropertyInfo();
                pi10.setName("StaffCode");
                pi10.setValue(StaffCode);
                request.addProperty(pi10);



                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.dotNet=true;
                envelope.setOutputSoapObject(request);
                // Make the soap call.
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapPrimitive  result =(SoapPrimitive)envelope.getResponse();
                result_value= result.toString();
                //Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();



            }catch(Exception ex){
                txtMsg.setText(ex.toString());
            }
        }

        return result_value;

    }
    void set_server_Ip()
    {
        try{


            Bundle bundle = getIntent().getExtras();
            root=bundle.get("root").toString();
            Personid =bundle.get("Personid").toString();
            recordNo=bundle.get("recordNo").toString();
            AccountNo=bundle.get("AccountNo").toString();
            Roster_Date=bundle.get("RosterDate").toString();
            EndTime=bundle.get("EndTime").toString();
            StaffCode = bundle.get("StaffCode").toString();
            URL = root  + "/TimeSheet.asmx?op=Add_Travel_Roster";

            Server_Available= bundle.get("Server").equals("True");


            if (bundle.get("Security_Token")==null)
                Security_Token="";
            else
                Security_Token=bundle.get("Security_Token").toString();


            if (bundle.get("OperatorId")==null)
                OperatorId="";
            else
                OperatorId=bundle.get("OperatorId").toString();

            if (bundle.get("TA_TRAVELDEFAULT")==null)
                TA_TRAVELDEFAULT="-";
            else
                TA_TRAVELDEFAULT=bundle.get("TA_TRAVELDEFAULT").toString();

            if (bundle.get("MyOwnCarStatus")==null)
                MyOwnCarStatus="0";
            else
                MyOwnCarStatus=bundle.get("MyOwnCarStatus").toString();

            RestrictTravelSameDay=bundle.get("RestrictTravelSameDay").toString();



        }catch(Exception ex){}
    }
    public void set_Record(String RecordNo)
    {
        try
        {
            if (Server_Available==false){
                return;
            }

            String URL5 = root + "/TimeSheet.asmx?op=getTravelInfo";
            String SOAP_ACTION5 =  "https://tempuri.org/getTravelInfo";
            String METHOD_NAME5 = "getTravelInfo";

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME5);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
            androidHttpTransport.debug =true;

            PropertyInfo pi=new PropertyInfo();
            pi.setName("RecordNo");

            pi.setValue(getSecurityToken() + RecordNo);
            request.addProperty(pi);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION5, envelope);
            SoapObject  result =(SoapObject)envelope.getResponse();
            Object obj;
            EditText  tvStartKm=(EditText)findViewById(R.id.txtStartKm);
            EditText txtEndKm=(EditText)findViewById(R.id.txtEndKm);
            EditText txtKM=(EditText)findViewById(R.id.txtKM);
            CheckBox chk = (CheckBox)findViewById(R.id.chkCharge);
            EditText   txtRosterNote= (EditText)findViewById(R.id.txtRosterNote);

            if ( result.getPropertyCount()>0)
            {
                //  obj=(SoapPrimitive)result.getProperty(i);

                obj =(SoapPrimitive) result.getProperty("StartKm");
                if (Double.parseDouble(obj.toString().trim())>0)
                    tvStartKm.setText(obj.toString());

                obj =(SoapPrimitive) result.getProperty("EndKm");
                if (Double.parseDouble(obj.toString().trim())>0)
                    txtEndKm.setText(obj.toString());

                obj =(SoapPrimitive) result.getProperty("EndKm");
                if (Double.parseDouble(obj.toString().trim())>0)
                    txtKM.setText(obj.toString());

                obj =(SoapPrimitive) result.getProperty("Charge");
                if (Double.parseDouble(obj.toString().trim())>0)
                    chk.setChecked(true);

                obj =(SoapPrimitive) result.getProperty("Travel_Note");
                txtRosterNote.setText(obj.toString().trim());
                
          	  /*    obj =(SoapPrimitive) result.getProperty("Client_Code");  
          	  
          	   TextView tvCharge=(TextView)findViewById(R.id.tvCharge);
          	   if (AccountNo.equalsIgnoreCase("!MULTIPLE") || AccountNo.equalsIgnoreCase("!INTERNAL") ){
          		  chk.setVisibility(View.INVISIBLE);
          		  tvCharge.setVisibility(View.INVISIBLE);
          	   }
          	   
          	  */

            }



        }catch(Exception ex){
            txtMsg.setText(ex.toString());
        }

    }
    public void set_Updates(String command ) throws IOException{

        try{
            // check for SDcard   

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
                        out.write("\n" + command);
                        out.close();

                    } catch (Exception e) {

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
                    out.write("\n" + command);
                    out.close();
                }
            }
        } catch (Exception e) {

        }

    }
    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            if (info != null)
            {
                for (int i = 0; i < info.length; i++)
                {
                    //  Log.i("Class", info[i].getState().toString());
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    boolean check_valid_note(String value){
        boolean valid=false;
        int i=0;

        for (i=0; i<value.length(); i++){
            if (AllowableChar(value.charAt(i), 1, ";'$.\n\t ")) {
                valid=true;
            }else{
                valid=false;
                break;
            }
        }


        return valid;

    }

    public boolean AllowableChar(char s_Char, int i_BaseFilter, String s_ExtraChar) {
        boolean AllowableChar2=false;
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
        if ( (int)s_Char == 8) {
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

    void adding_travel(){

        try{


            RadioButton radioWithin = (RadioButton)findViewById(R.id.radioWithin);
            RadioButton radioTravelBetween = (RadioButton)findViewById(R.id.radioTravelBetween);
            RadioButton chkMyCar = (RadioButton)findViewById(R.id.chkMyCar);
            RadioButton chkFleetVehicle = (RadioButton)findViewById(R.id.chkFleetVehicle);

            if (!chkMyCar.isChecked() && !chkFleetVehicle.isChecked())
            {
                //tvStartKm.getText().toString().equals("0");
                Toast.makeText(Travel.this, "Please Select Vehicle Type", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!radioWithin.isChecked() && !radioTravelBetween.isChecked())
            {
                //tvStartKm.getText().toString().equals("0");
                Toast.makeText(Travel.this, "Please Select Travel Type", Toast.LENGTH_SHORT).show();
                return;
            }
            if (tvStartKm.getText().toString().equals("") && !txtEndKm.getText().toString().equals("")  )
            {
                //tvStartKm.getText().toString().equals("0");
                txtMsg.setText("Please Enter start KM");
                return;
            }

            if (txtEndKm.getText().toString().equals("") && !tvStartKm.getText().toString().equals(""))
            // if (txtEndKm.getText().toString().equals("") && !tvStartKm.getText().toString().equals(""))
            {
                txtMsg.setText("Please Enter End KM");
                return;
            }
            double dist=0;


            try{
                if (!tvStartKm.getText().toString().equals("") && !txtEndKm.getText().toString().equals("")){

                    dist=Double.parseDouble(txtEndKm.getText().toString().trim())-Double.parseDouble(tvStartKm.getText().toString().trim())  ;
                    txtKM.setText(""+dist);

                }else  if (!txtKM.getText().toString().equals("") ){

                    tvStartKm.setText("0");
                    txtEndKm.setText(txtKM.getText().toString());
                    dist=Double.parseDouble(txtKM.getText().toString().trim())  ;

                }

            }catch(Exception ex){}


            if (Double.parseDouble(txtKM.getText().toString().trim())<=0)
            {
                txtMsg.setText("Invalid distance");
                return;
            }

            EditText   txtRosterNote= (EditText)findViewById(R.id.txtRosterNote);
            String notes=txtRosterNote.getText().toString();

            if (notes.equals("") || notes==null){
                Toast.makeText(Travel.this, "Please enter some valid note of travel claim", Toast.LENGTH_SHORT).show();

                return;
            }

            java.text.DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
            String today=getTodaysDate();

            if (RestrictTravelSameDay.equalsIgnoreCase("true") &&  !today.equalsIgnoreCase(Roster_Date.replace("/","")))
            {
                txtMsg.setText("You cannot add travel claim of other than today ");
                Toast.makeText(Travel.this, "You cannot add travel claim of other than today ", Toast.LENGTH_SHORT).show();
                return;
            }


            String status="0";

            CheckBox chk = (CheckBox)findViewById(R.id.chkCharge);
            if (chk.isChecked())
                status="1";


            double startKM=0,EndKM=0;

            if (tvStartKm.getText().toString().trim().equals(""))
                startKM=0;
            else
                startKM= Double.parseDouble(tvStartKm.getText().toString().trim());

            if (txtEndKm.getText().toString().trim().equals("") )
                EndKM=Double.parseDouble(txtKM.getText().toString().trim());
            else
                EndKM=Double.parseDouble(txtEndKm.getText().toString().trim());


           // EditText   txtRosterNote= (EditText)findViewById(R.id.txtRosterNote);
            // String notes=txtRosterNote.getText().toString().replace("\'","\'\'");
             notes=txtRosterNote.getText().toString();
            // RadioButton radioTravelBetween = (RadioButton)findViewById(R.id.radioTravelBetween);

            if ( notes.equals("") && !( status.equals("0") && radioTravelBetween.isChecked() )){
                Toast.makeText(getApplicationContext(), "Please enter valid Note, It is compulsory", Toast.LENGTH_LONG).show();
                return;
            }

            if (notes.equals("")){
                Toast.makeText(getApplicationContext(), "Please enter valid Note", Toast.LENGTH_LONG).show();
                return;
            }else{

                if (Double.parseDouble(txtKM.getText().toString().trim())>=200) {
                    ShowDialog_for_KM(btnSave, "You have Entered KM greater than 200\n Are you sure, you want to continue?");
                    return;
                }  else{
                    String command ="update roster set StartKm=" + startKM  + ",EndKm=" + EndKM +
                            ",KM=" + txtKM.getText() + ",Charge="+ status +", notes=CONVERT(VARCHAR(MAX),isnull(notes,'')) + CONVERT(VARCHAR,' -- " + notes  + "')" +
                            " where recordno=" + recordNo;

                    // Add_Roster(recordNo);
                    try {
                        new MyAsyncClass().execute(recordNo);
                    }catch(Exception ex){}
                }
                //-----------------below code is disaled and will do nothing

                // Make_update(command);
                // txtMsg.setText("Record updated successfully");
                //  Toast.makeText(getApplicationContext(), "Record updated successfully", Toast.LENGTH_LONG).show();

            }

        }catch(Exception ex){txtMsg.setText("Please enter valid KM");}
    }
    void Default_Setting(String Charge_Type){

        //Toast.makeText(getApplicationContext(), Charge_Type, Toast.LENGTH_LONG).show();

        CheckBox chk = (CheckBox)findViewById(R.id.chkCharge);
        RadioButton radioWithin = (RadioButton)findViewById(R.id.radioWithin);
        RadioButton radioTravelBetween = (RadioButton)findViewById(R.id.radioTravelBetween);
        RadioButton chkCar = (RadioButton)findViewById(R.id.chkMyCar);
        //  TextView tvMyCar = (TextView)findViewById(R.id.tvMyCar);
        chk.setChecked(false);

        radioWithin.setChecked(false);
        radioTravelBetween.setChecked(false);

        if (Charge_Type.equalsIgnoreCase("CHARGEABLE BETWEEN")) {
            chk.setChecked(true);
            radioTravelBetween.setChecked(true);
        }

        if (Charge_Type.equalsIgnoreCase("CHARGEABLE WITHIN")) {
            chk.setChecked(true);
            radioWithin.setChecked(true);
        }

        if (Charge_Type.equalsIgnoreCase("NON CHARGEABLE BETWEEN")) {
            chk.setChecked(false);
            radioTravelBetween.setChecked(true);
        }
        if (Charge_Type.equalsIgnoreCase("NON CHARGEABLE WITHIN")) {
            chk.setChecked(false);
            radioWithin.setChecked(true);
        }
        //chkCar.setVisibility(View.VISIBLE);
        //tvMyCar.setVisibility(View.VISIBLE);
        chkCar.setChecked(MyOwnCarStatus.equalsIgnoreCase("true"));


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
            // byte[] byte_Data =null;
            //  char [] key ={'M','T','A','S','A','M','A','D','A','2','0','0','2'};

            //   byte_Data= EncryptData.encodeFile(EncryptData.generateKey(key,xml.getBytes()),xml.getBytes());

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
                fileos.close();


               /*  String key = "MTASamada2002";
                File inputFile =  new File(fileDir,"traccs.xml");
                File encryptedFile = new File("traccs.encrypted");
               // File decryptedFile = new File("traccs.decrypted");

               try {
                    CryptoUtils.encrypt(key, inputFile, encryptedFile);
                  //  CryptoUtils.decrypt(key, encryptedFile, decryptedFile);
                } catch (CryptoException ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }*/

            }
        }catch (Exception ex){ }//textMsg.setText("cc: " + ex.toString());}
        //    textMsg.setText("Done successfully");




    }
    class MyAsyncClass2 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Travel.this,true);
            pDialog.setMessage("Please wait while loading data ....");
          //  pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {

                load_Roster_data();
            }

            catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
           // pDialog.cancel();
            MainActivity.form_resumed=false;
           // load_main_form();


            //  Toast.makeText(getApplicationContext(), "Email send", 100).show();
        }
    }
}