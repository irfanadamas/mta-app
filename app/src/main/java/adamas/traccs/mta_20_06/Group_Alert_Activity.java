
package adamas.traccs.mta_20_06;

        import android.annotation.SuppressLint;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.SharedPreferences;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.os.Environment;
        import android.os.Handler;

        import android.text.Html;
        import android.util.Xml;
        import android.view.Gravity;
        import android.view.KeyEvent;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
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
        import org.xmlpull.v1.XmlSerializer;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.util.ArrayList;

        import javax.xml.parsers.DocumentBuilder;
        import javax.xml.parsers.DocumentBuilderFactory;

        import androidx.appcompat.app.ActionBar;
        import androidx.appcompat.app.AppCompatActivity;

public class Group_Alert_Activity extends AppCompatActivity {
    String root="";
    final String PREFS_NAME = "MTAPrefs";
    SharedPreferences settings = null;
    boolean Server_Available;
    private final String NAMESPACE = "https://tempuri.org/";
    String PersonId="";
    String Security_Token="";
    String OperatorId="1";
    boolean  load_group_alerts=false;
    public static boolean load_group_alerts_all=true;
    File froot;
    ArrayList<GroupAlerts> lst_groups = null;
    String SpecialConsiderations="-";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_alert);
        //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //   setSupportActionBar(toolbar);
        setupActionBar();

        TextView txtAlert = findViewById(R.id.txtAlerts);
        settings = getSharedPreferences(PREFS_NAME, 0);
        PersonId = getIntent().getExtras().getString("PersonId","0");
        OperatorId = settings.getString("OperatorId", "0"); //bundle.getString("User");

//        OperatorId= getIntent().getExtras().getString("OperatorId","0");
        root=settings.getString("root","");
        Server_Available=settings.getBoolean("Server_Available",false);
        Security_Token=settings.getString("Security_Token","");

        try{
            load_group_alerts= getIntent().getExtras().getBoolean("load_group_alerts",false);

        }catch(Exception ex){}

        txtAlert.setText("Alerts Group");
        try{

            if (load_group_alerts && Server_Available) {
                load_group_alerts_all=true;
                new MyAsyncClass6().execute();
            }else if (load_group_alerts_all && Server_Available) {
                load_group_alerts_all = false;
                new MyAsyncClass5().execute();
            }else
                get_GroupAlerts2();

        }catch(Exception ex){}
/*

        Button btnExit= findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
*/
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
        getMenuInflater().inflate(R.menu.menu_main2, menu);
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
    String getSecurityToken() {

        String Val = OperatorId + "$" + Security_Token + "$";
        return Val;
    }
    public void get_GroupAlerts() {

        if (!Server_Available) {
            get_GroupAlerts2();
            return;
        }

        String URL6 = root + "/Timesheet.asmx?op=getAlertGroups";
        String SOAP_ACTION6 = "https://tempuri.org/getAlertGroups";
        String METHOD_NAME6 = "getAlertGroups";

        TextView txtAlertGroup_Label = (TextView) findViewById(R.id.txtGroupAlerts);
        //  txtGroupAlerts.setTypeface(null, Typeface.BOLD);

        GroupAlerts grp = null;
        int indx = 0;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME6);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug = true;

            PropertyInfo pi1 = new PropertyInfo();
            pi1.setName("PersonID");
            pi1.setValue(getSecurityToken() + PersonId);
            request.addProperty(pi1);

            //  Toast.makeText(getApplicationContext(),Personid , Toast.LENGTH_LONG).show();

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION6, envelope);

            SoapObject result = (SoapObject) envelope.getResponse();
            SoapObject result2;
            SoapPrimitive obj;
            if(result==null) return;

            int n = result.getPropertyCount();
            DataDomain dom;
            lst_groups = new ArrayList<GroupAlerts>();
            // Toast.makeText(getApplicationContext(),"values = " + n + " Personid = " + Personid , Toast.LENGTH_LONG).show();

            for (int j = 0; j < n; j++) {
                result2 = (SoapObject) result.getProperty(j);
                grp = new GroupAlerts();

                obj = (SoapPrimitive) result2.getProperty("RecordNo");
                grp.setRecordNo(obj.toString());

                obj = (SoapPrimitive) result2.getProperty("Group");
                grp.setGroup(obj.toString());

                obj = (SoapPrimitive) result2.getProperty("Notes");
                grp.setNotes(obj.toString());

                try {
                    obj = (SoapPrimitive) result2.getProperty("SpecialConsiderations");
                    grp.setSpecialConsiderations(obj.toString());
                    SpecialConsiderations =grp.getSpecialConsiderations();
                } catch (Exception ex){
                    grp.setSpecialConsiderations("-");
                }
                lst_groups.add(grp);

            }




        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }


    }

    public void get_GroupAlerts2() {


        GroupAlerts grp = null;

        try {

            froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String state = Environment.getExternalStorageState();
            File fileDir = null;
            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            File fXmlFile = new File(fileDir, "group_alerts.xml");


            int indx = 0;

            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Alert_Group");

                // Toast.makeText(getApplicationContext(),"node list = " + nList.getLength() , Toast.LENGTH_LONG).show();


                if (nList == null) return;

                int n = nList.getLength();
                lst_groups = new ArrayList<GroupAlerts>();

                for (int tmp = 0; tmp < nList.getLength(); tmp++) {
                    try {

                        String s_PersonId = "";
                        Node nNode = nList.item(tmp);
                        //  txtAcknowledge.setText(nNode.getTextContent());
                        //  txtAcknowledge.setText("\nCurrent Element :" + nNode.getNodeName());

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            s_PersonId = eElement.getElementsByTagName("PersonId").item(0).getTextContent();

                            // txtAcknowledge.setText(txtAcknowledge.getText() + "\n" + str_date + " = " + selected_date + " " +  selection.getText().equals(code));

                            if (s_PersonId.equals(PersonId)) {
                                grp = new GroupAlerts();

                                //  Toast.makeText(getApplicationContext(),"s_PersonId" + s_PersonId + "Personid="+Personid  , Toast.LENGTH_LONG).show();

                                grp.setRecordNo(eElement.getElementsByTagName("RecordNo").item(0).getTextContent());
                                grp.setGroup(eElement.getElementsByTagName("Group").item(0).getTextContent());
                                grp.setNotes(eElement.getElementsByTagName("Notes").item(0).getTextContent());
                                grp.setPersonId(eElement.getElementsByTagName("PersonId").item(0).getTextContent());
                                grp.setPersonId(eElement.getElementsByTagName("PersonId").item(0).getTextContent());
                                try {
                                    grp.setSpecialConsiderations(eElement.getElementsByTagName("SpecialConsiderations").item(0).getTextContent());
                                    SpecialConsiderations =grp.getSpecialConsiderations();
                                } catch (Exception ex){
                                grp.setSpecialConsiderations("-");
                               }
                                lst_groups.add(grp);


                                // Toast.makeText(getApplicationContext(),"s_PersonId" + s_PersonId + "Personid="+Personid  , Toast.LENGTH_LONG).show();

                            }
                        } else {
                            // txtAcknowledge.setText("Group Alerts - File does not exist");
                        }
                    } catch (Exception aE) {
                        // txtAcknowledge.setText("Error in Group Alerts " + aE.toString());
                    }


                }


            } else {
                // txtAcknowledge.setText("Group Alerts Xml file not found");
            }
        } catch (Exception aE) {
            //  ((TextView) findViewById(R.id.TextDate)).setText("get_GroupAlerts2" + aE.toString());
        }


        try {
            if (lst_groups == null) {

                return;
            }
            if (lst_groups!=null) {
//                ListView lst_view_GroupAlerts = (ListView) findViewById(R.id.lstAlerts);
                ExpandableHeightListView listView = new ExpandableHeightListView(this);
                ExpandableHeightListView   lst_view_GroupAlerts=(ExpandableHeightListView)findViewById(R.id.lstAlerts);

                TextView txtRunsheet = findViewById(R.id.txtRunsheet);
                TextView txtRunsheet_heading = findViewById(R.id.txtRunsheet_heading);
                View sperator2 = findViewById(R.id.sperator2);
                View sperator3 = findViewById(R.id.sperator3);


                txtRunsheet.setVisibility(View.GONE);
                txtRunsheet_heading.setVisibility(View.GONE);
                sperator2.setVisibility(View.GONE);
                sperator3.setVisibility(View.GONE);

                if (lst_groups.size() > 0) {
                    lst_view_GroupAlerts.setVisibility(View.VISIBLE);

                    lst_view_GroupAlerts.setAdapter(new Group_Alert_Adapter2(Group_Alert_Activity.this, lst_groups));
                    lst_view_GroupAlerts.setExpanded(true);

                    if (!SpecialConsiderations.equalsIgnoreCase("-") && !SpecialConsiderations.equalsIgnoreCase("") ){
                        txtRunsheet.setText(Html.fromHtml("<b>Runsheet Alerts</b>" +  "<br>" +
                                "" + SpecialConsiderations + "" + "<br />" ));
                        txtRunsheet.setVisibility(View.VISIBLE);
                        txtRunsheet_heading.setVisibility(View.VISIBLE);
                        sperator2.setVisibility(View.VISIBLE);
                        sperator3.setVisibility(View.VISIBLE);
                    }
                   // setListViewHeightBasedOnChildren2(lst_view_GroupAlerts);
                }
            }
        } catch (Exception e) {
           // Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }



    }
    public static void setListViewHeightBasedOnChildren2(ListView listView) {
        Group_Alert_Adapter listAdapter = (Group_Alert_Adapter) listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
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
        listView.requestLayout();
    }
    class MyAsyncClass5 extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Group_Alert_Activity.this);
            pDialog.setMessage("Please wait while loading alerts  ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {


                load_Group_Alerts_Detail();

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
                get_GroupAlerts2();
            } catch (Exception e) {}
        }
    }
    class MyAsyncClass6 extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Group_Alert_Activity.this);
            pDialog.setMessage("Please wait while loading alerts  ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {


                get_GroupAlerts();

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
                if (lst_groups!=null) {
                    ExpandableHeightListView listView = new ExpandableHeightListView(Group_Alert_Activity.this);
                    ExpandableHeightListView   lst_view_GroupAlerts=(ExpandableHeightListView)findViewById(R.id.lstAlerts);

                    TextView txtRunsheet = findViewById(R.id.txtRunsheet);
                    TextView txtRunsheet_heading = findViewById(R.id.txtRunsheet_heading);
                    View sperator2 = findViewById(R.id.sperator2);
                    View sperator3 = findViewById(R.id.sperator3);

                    txtRunsheet.setVisibility(View.GONE);
                    txtRunsheet_heading.setVisibility(View.GONE);
                    sperator2.setVisibility(View.GONE);
                    sperator3.setVisibility(View.GONE);

                    if (lst_groups.size()<=0 &&  (SpecialConsiderations.equalsIgnoreCase("-") || SpecialConsiderations.equalsIgnoreCase(""))){
                        Tost_Message("NO ALERTS FOUND");
                    }

                    if (lst_groups.size() > 0) {
                        lst_view_GroupAlerts.setVisibility(View.VISIBLE);

                        lst_view_GroupAlerts.setAdapter(new Group_Alert_Adapter2(Group_Alert_Activity.this, lst_groups));
                        lst_view_GroupAlerts.setExpanded(true);

                        if (!SpecialConsiderations.equalsIgnoreCase("-") && !SpecialConsiderations.equalsIgnoreCase("")){
                            txtRunsheet.setText(Html.fromHtml("<b>Runsheet Alerts</b>" +  "<br />" +
                                    "" + SpecialConsiderations + "" + "<br />" ));
                            txtRunsheet.setVisibility(View.VISIBLE);
                            txtRunsheet_heading.setVisibility(View.VISIBLE);
                            sperator2.setVisibility(View.VISIBLE);
                            sperator3.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } catch (Exception e) {}
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
    public void load_Group_Alerts_Detail() {
        String URL6 = root + "/TimeSheet.asmx?op=getAllAlertGroups";
        String SOAP_ACTION6 = "https://tempuri.org/getAllAlertGroups";
        String METHOD_NAME6 = "getAllAlertGroups";

        String buff = "";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME6);
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug = true;
            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("Fontra");
            pi2.setValue(getSecurityToken() + "99");
            request.addProperty(pi2);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            androidHttpTransport.call(SOAP_ACTION6, envelope);
            // SoapObject obj=(SoapObject) envelope.getResponse();
            String xml = androidHttpTransport.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "group_alerts.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();

                } catch (IOException e) {
                    //textMsg.setText("bb "+ e.toString());
                }
                FileOutputStream fileos = null;

                try {
                    fileos = new FileOutputStream(newxmlfile);
                    fileos.write(xml.getBytes());
                    serializer.setOutput(fileos, "UTF-8");
                } catch (Exception ex) {
                } finally {
                    fileos.close();
                }
            }
        } catch (Exception ex) {
        }
        //    textMsg.setText("Done successfully");

    }
}
