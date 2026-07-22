package adamas.traccs.mta_20_06;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;

import android.os.Bundle;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Reminders_Activity extends AppCompatActivity {




    public String root="https://58.162.142.150/timesheet" ; //https://10.0.2.2:49884
    private final String NAMESPACE = "https://tempuri.org/";

    private  String URL4 = root  + "/TimeSheet.asmx?op=getDevice_Active_Reminders";
    private final String SOAP_ACTION4 =  "https://tempuri.org/getDevice_Active_Reminders";
    private final String METHOD_NAME4 = "getDevice_Active_Reminders";


    ListView listView;
    private List<DeviceReminders> Reminders=null;
    DeviceReminders deviceReminder;
    final String PREFS_NAME = "MTAPrefs";
    SharedPreferences settings=null;
    String UserId="";
    String OperatorId;
    String Security_Token;
    String StaffCode;
    String rosterDate="";
    File froot = null;
    Context context;
    boolean Server_Available=true;
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            try {
                String Title = actionBar.getTitle().toString();
                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                actionBar.setCustomView(getLayoutInflater().inflate(R.layout.action_bar_home3, null),
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
                imageMenu.setVisibility(View.GONE);
                ImageView imageBack=(ImageView) actionBar.getCustomView().findViewById(R.id.imageBack);
                // imageMenu.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
               // imageBack.setVisibility(View.GONE);
              //  actionBar.setDisplayHomeAsUpEnabled(true);
                imageBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       finish();
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.device_reminders);
        setupActionBar();
        context=this.getApplicationContext();
        set_server_Ip();
        //get_Server_Settings();

        Button  btnOk=(Button)findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                finish();
            }
        });

        Button  btnRemind=(Button)findViewById(R.id.btnRemind);

        btnRemind.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                finish();
            }
        });
        Button  btnAcknowledge=(Button)findViewById(R.id.btnAcknowledge);

        btnAcknowledge.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                Set_Acknowledge();
            }
        });

    }
    @Override
    public void onResume(){
        super.onResume();

        try{

              //  new MyAsyncClass6().execute();
            get_Active_Device_Reminder2
                ();
        }catch(Exception ex){}
    }
    void Set_Acknowledge(){
        String command="Update DeviceReminders set status=0 where status=1 and StaffCode='" + StaffCode + "'";
        // Make_update(command);
        try{
            new MyAsyncClass().execute(command,"","");
        }catch(Exception ex){}
    }
   String getSecurityToken(){

        String Val= OperatorId  + "$" + Security_Token + "$";
        return Val;
    }

    void set_server_Ip()
    {


        try {

            Bundle b=getIntent().getExtras();

            settings = getSharedPreferences(PREFS_NAME, 0);
            root= settings.getString("root", root);
            StaffCode= settings.getString("StaffCode", StaffCode);
            OperatorId=settings.getString("OperatorId",OperatorId);
            Security_Token=settings.getString("Security_Token",Security_Token);
            Server_Available= settings.getBoolean("Server_Available",Server_Available  );
            rosterDate= settings.getString("rosterDate", rosterDate);

            URL4 = root + "/TimeSheet.asmx?op=getDevice_Active_Reminders";

            //Toast.makeText(getApplicationContext(), root +"\n" +  StaffCode, Toast.LENGTH_SHORT).show();


        }catch(Exception ex){
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();}





    }
    public void get_Active_Device_Reminder(){


        try {

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME4);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL4);
            androidHttpTransport.debug =true;

            PropertyInfo pi=new PropertyInfo();
            pi.setName("UserID");
            pi.setValue(getSecurityToken() + UserId);
            request.addProperty(pi);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet=true;

            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION4, envelope);
            String output="" ;

            SoapObject   result =(SoapObject)envelope.getResponse();
            SoapObject   result2=null;
            DeviceReminders dev=null;
            int n=result.getPropertyCount();
            Reminders=new ArrayList<DeviceReminders>() ;
            for (int j=0; j<result.getPropertyCount(); j++)
            {

                try{

                    dev=new DeviceReminders();
                    result2=(SoapObject)result.getProperty(j);
                    dev.setRecordnumber(result2.getProperty("Recordnumber").toString()); // Recordnumber
                    dev.setUserID(result2.getProperty("UserID").toString()); // UserID
                    dev.setActivationDateTime(result2.getProperty("ActivationDateTime").toString()); // ActivationDateTime
                    dev.setDetail(result2.getProperty("Detail").toString()); // Detail
                    dev.setStatus(result2.getProperty("Status").toString()); // Status

                    try {

                        dev.setMessageGroup(result2.getProperty("MessageGroup").toString()); // MessageGroup
                        dev.setExternalID(result2.getProperty("ExternalID").toString()); // ExternalID
                        dev.setStaff(result2.getProperty("Staff").toString()); // Staff
                    }catch(Exception ex){Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();}

                }catch(Exception ex){Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();}

                Reminders.add(dev);
            }
        } catch(Exception ex){Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();}


    }

    public void get_Active_Device_Reminder2(){

        try {

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            File fileDir = null;
            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = new File(fileDir, "Reminder.xml");

//            Button textMsg = ((Button) findViewById(R.id.txtAddress2));

            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("DeviceReminders");

                if (nList == null) {

                    finish();
                    return;
                }
                DeviceReminders dev=null;
                int n=nList.getLength();
                if (n<=0) {
                    finish();
                    return;
                }

                Reminders=new ArrayList<DeviceReminders>() ;
                for (int temp = 0; temp < n; temp++) {

                    try {
                        Node nNode = nList.item(temp);

                        dev = new DeviceReminders();
                        String value = "";
                        //  textMsg.setText("\nCurrent Element :" + nNode.getNodeName());
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            value = eElement.getElementsByTagName("Recordnumber").item(0).getTextContent();
                            dev.setRecordnumber(value);
                            value = eElement.getElementsByTagName("UserID").item(0).getTextContent();
                            dev.setUserID(value);
                            value = eElement.getElementsByTagName("ActivationDateTime").item(0).getTextContent();
                            dev.setActivationDateTime(value);
                            value = eElement.getElementsByTagName("Detail").item(0).getTextContent();
                            dev.setDetail(value);
                            value = eElement.getElementsByTagName("Status").item(0).getTextContent();
                            dev.setStatus(value);

                            try {

                                value = eElement.getElementsByTagName("MessageGroup").item(0).getTextContent();
                                dev.setMessageGroup(value);
                                value = eElement.getElementsByTagName("ExternalID").item(0).getTextContent();
                                dev.setExternalID(value);
                                value = eElement.getElementsByTagName("Staff").item(0).getTextContent();
                                dev.setStaff(value);


                            }catch(Exception ex){}
                            Reminders.add(dev);
                        }


                    } catch (Exception aE) {
                        // ((Button) findViewById(R.id.txtAddress2)).setText(aE.toString());
                    }
                }
            }
            if (Reminders==null )  finish();
            if (Reminders.size()<=0) finish();

        } catch (Exception aE) {
            //  ((Button) findViewById(R.id.txtAddress2)).setText(aE.toString());
        }
        try{
            //  Button dialogButton = (Button) findViewById(R.id.btnExit_main);
            if (Reminders!=null ) {
                try{

                    ListView	listView = (ListView) findViewById(R.id.lst_device_reminders);

                    if (Reminders.size() >0) {

                        listView.setAdapter(new Reminder_Adapter(Reminders_Activity.this, Reminders, OperatorId, Security_Token, root));
                       // setListViewHeightBasedOnChildren(listView);
                    }
                }catch(Exception ex){}
            }else{
                finish();
            }
        } catch (Exception ex) {}



    }
    class MyAsyncClass6 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Reminders_Activity.this);
            pDialog.setMessage("Please wait while loading data  ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {


                try {
                    Bulk_Data bulk= new Bulk_Data(root,StaffCode,OperatorId,Security_Token,Server_Available,rosterDate,context);
                    bulk.get_Active_Device_Reminder();

                } catch (Exception ex) {
                }

            }catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try{
              //  Button dialogButton = (Button) findViewById(R.id.btnExit_main);
                if (Reminders!=null ) {
                    try{
                       //
                        ListView	listView = (ListView) findViewById(R.id.lst_device_reminders);

                        if (Reminders.size() >0)

                        listView.setAdapter(new Reminder_Adapter(Reminders_Activity.this, Reminders,OperatorId,Security_Token,root));


                    }catch(Exception ex){}
                }
            } catch (Exception ex) {}
            pDialog.cancel();

            //  Toast.makeText(getApplicationContext(), "Email send", 100).show();
        }
    }
    private void RunBakckgrounThread() {
        final ProgressDialog pDialog = new ProgressDialog(getApplicationContext());
        // Database operations should not be done on the main thread
        AsyncTask<Void, Void, Void> runBakckgrounThread = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();


                pDialog.setMessage("Please wait while processing  ....");
                pDialog.show();

            }
            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    Bulk_Data bulk= new Bulk_Data(root,StaffCode,OperatorId,Security_Token,Server_Available,rosterDate,context);
                    bulk.get_Active_Device_Reminder();
                    get_Active_Device_Reminder2();
                } catch (Exception ex) {
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                pDialog.cancel();

                //  Toast.makeText(getApplicationContext()," total_item= " + Reminders.length , Toast.LENGTH_LONG).show();

                ListView	listView1 = (ListView) findViewById(R.id.lst_device_reminders);

                try{
                    if (Reminders!=null){
                        listView1.setAdapter(new Reminder_Adapter(getApplicationContext(), Reminders, OperatorId, Security_Token, root));
                    }
                }catch(Exception ex){}
               // listView.setAdapter(new Reminder_Adapter(getApplicationContext(), Reminders,OperatorId,Security_Token,root));



            }
        };

        runBakckgrounThread.execute();
    }
    class MyAsyncClass extends AsyncTask<String, String, String> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new LoadingDialog(Reminders_Activity.this);
            pDialog.setMessage("Please wait while processing  ....");
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... mApi) {
            try {

                Make_update(mApi[0]);
            }
            catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
            return "True";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.cancel();
            finish();
        }
    }

    public void Make_update(String command)
    {
        try{

            String URL5 = this.root + "/TimeSheet.asmx?op=Make_update";
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
                SoapPrimitive result =(SoapPrimitive)envelope.getResponse();

            }
        }catch(Exception ex){

        }

    }

    public  void setListViewHeightBasedOnChildren(ListView listView) {
       int totalHeight=0;
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height =  + (150 * (Reminders.size()+1));
        listView.setLayoutParams(params);
        listView.requestLayout();
       /* ListAdapter listAdapter = (ListAdapter) listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);

        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()));
        listView.setLayoutParams(params);
        listView.requestLayout();*/
    }
}
