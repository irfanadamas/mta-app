package adamas.traccs.mta_20_06;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;

import android.os.Bundle;
import android.util.Xml;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SMTP_Settings extends AppCompatActivity {


    File froot = null;
    Context context;
    final String PREFS_NAME = "MTAPrefs";
    SharedPreferences settings=null;
    private final String NAMESPACE = "https://tempuri.org/";
    String root;
    boolean Server_Available=false;
    String OperatorId = "";
    String Security_Token = "";
    Email_Settings   email_seting=null;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setupActionBar();
        setContentView(R.layout.activity_s_m_t_p__settings);
        setupActionBar();
        this.context=this.getApplicationContext();
        settings = getSharedPreferences(PREFS_NAME, 0);

        set_server_Ip();


        EditText txtServer=findViewById(R.id.txtServer);
        txtServer.setFocusable(true);
        String smtp= settings.getString("SMTP","Nothing");
        if (smtp.equalsIgnoreCase("True")) {
            show_SMTP_Server_Setting();
        }else{
            try{
                new MyAsyncClass4_Server().execute();
            }catch (Exception ex){}

        }
        Button btn_exit = (Button) findViewById(R.id.btnExit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                finish();
            }
        });

        Button btnOk = (Button) findViewById(R.id.btnSave);
        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                save_record();
                finish();
            }
        });

    }
  /* private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
*/
  void set_server_Ip()
  {

      try{
          Bundle bundle = getIntent().getExtras();

           root=settings.getString("root","");
              Security_Token=settings.getString("Security_Token","");
              OperatorId=settings.getString("OperatorId","");
          Server_Available= settings.getBoolean("Server_Available",false);



          }catch(Exception ex){}


  }
    void save_record() {



        settings.edit().putString("SMTP", "True").commit();

        froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File filein = null;
        File fileDir = null;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {

            //check sdcard permission
            fileDir = new File(froot.getAbsolutePath() + "/.server/");
            if (!fileDir.exists())
                fileDir.mkdirs();

            filein = new File(fileDir, "server_setting.txt");
            if (!filein.exists()) {
                try {
                    if (!fileDir.exists())
                        fileDir.createNewFile();

                } catch (Exception e) {
                }
            }


            try {
                froot.setWritable(true);
                //if (froot.canWrite()) {

                File file = new File(fileDir, "server_setting.txt");
                FileWriter filewriter = new FileWriter(file);
                BufferedWriter out = new BufferedWriter(filewriter);
                EditText txtServer = (EditText) findViewById(R.id.txtServer);
                out.write(txtServer.getText().toString() + "\n");

                EditText txtUser = (EditText) findViewById(R.id.txtUser);
                out.write(txtUser.getText().toString() + "\n");

                EditText txtpassword = (EditText) findViewById(R.id.txtpassword);
                out.write(txtpassword.getText().toString() + "\n");

                EditText txtPort = (EditText) findViewById(R.id.txtPort);
                out.write(txtPort.getText().toString() + "\n");

                EditText txtFormEmail = (EditText) findViewById(R.id.txtFormEmail);
                out.write(txtFormEmail.getText().toString() + "\n");

                EditText txtEmail_subject = (EditText) findViewById(R.id.txtEmail_subject);
                out.write(txtEmail_subject.getText().toString() + "\n");

                out.close();

                try {

                    settings.edit().putString("SMTP_Server", txtServer.getText().toString()).commit();
                    settings.edit().putString("SMTP_Port", txtPort.getText().toString()).commit();
                    settings.edit().putString("SMTP_User", txtUser.getText().toString()).commit();
                    settings.edit().putString("SMTP_Password", txtpassword.getText().toString()).commit();
                    settings.edit().putString("Email_Subject", txtEmail_subject.getText().toString() ).commit();
                    settings.edit().putString("From_Address", txtFormEmail.getText().toString()).commit();


                } catch (Exception ex) {
                }

            } catch (Exception e) {
            }


        }

        Toast.makeText(getApplicationContext(), " Server Setting Saved successfully", Toast.LENGTH_LONG).show();
    }
    public void show_SMTP_Server_Setting() {


        EditText txtServer = (EditText) findViewById(R.id.txtServer);
        txtServer.setText(settings.getString("SMTP_Server", ""));

        EditText txtUser = (EditText) findViewById(R.id.txtUser);
        txtUser.setText(settings.getString("SMTP_User", ""));


        EditText txtpassword = (EditText) findViewById(R.id.txtpassword);
        txtpassword.setText(settings.getString("SMTP_Password", ""));

        EditText txtPort = (EditText) findViewById(R.id.txtPort);
        txtPort.setText(settings.getString("SMTP_Port", ""));

        EditText txtFormEmail = (EditText) findViewById(R.id.txtFormEmail);
        txtFormEmail.setText(settings.getString("From_Address", ""));

        EditText txtEmail_subject = (EditText) findViewById(R.id.txtEmail_subject);
        txtEmail_subject.setText(settings.getString("Email_Subject", "" ));

  }
    public void show_SMTP_Server_Setting2() {

        try {
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File filein = null;
            File fileDir = null;
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {

                //check sdcard permission
                fileDir = new File(froot.getAbsolutePath() + "/.server/");

                filein = new File(fileDir, "server_setting.txt");
                if (filein.exists()) {
                    try {
                        if (filein.length() <= 0) return;

                        FileReader fileReader = new FileReader(filein);
                        BufferedReader buf = new BufferedReader(fileReader);
                        String add = buf.readLine();

                        EditText txtServer = (EditText) findViewById(R.id.txtServer);
                        txtServer.setText(add);

                        EditText txtUser = (EditText) findViewById(R.id.txtUser);
                        add = buf.readLine();
                        txtUser.setText(add);


                        EditText txtpassword = (EditText) findViewById(R.id.txtpassword);
                        add = buf.readLine();
                        txtpassword.setText(add);

                        EditText txtPort = (EditText) findViewById(R.id.txtPort);
                        add = buf.readLine();
                        txtPort.setText(add);

                        EditText txtFormEmail = (EditText) findViewById(R.id.txtFormEmail);
                        add = buf.readLine();
                        txtFormEmail.setText(add);

                        EditText txtEmail_subject = (EditText) findViewById(R.id.txtEmail_subject);
                        add = buf.readLine();
                        txtEmail_subject.setText(add);

                        buf.close();

                        try {

                            settings.edit().putString("SMTP_Server", txtServer.getText().toString()).commit();
                            settings.edit().putString("SMTP_Port", txtPort.getText().toString()).commit();
                            settings.edit().putString("SMTP_User", txtUser.getText().toString()).commit();
                            settings.edit().putString("SMTP_Password", txtpassword.getText().toString()).commit();
                            settings.edit().putString("Email_Subject", txtEmail_subject.getText().toString() ).commit();
                            settings.edit().putString("From_Address", txtFormEmail.getText().toString()).commit();



                        } catch (Exception ex) {
                        }

                    } catch (Exception ex) {
                    } finally {

                    }

                }else{
                    try{
                        filein.createNewFile();
                    }catch(Exception ex){}
                }
            }
        } catch (Exception ex) {
        }
    }
    String getSecurityToken() {

        String Val = OperatorId + "$" + Security_Token + "$";
        return Val;
    }
    class MyAsyncClass4_Server extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(SMTP_Settings.this);
            pDialog.setMessage("Please wait while loading data ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
// Add subject, Body, your mail Id, and receiver mail Id.
                try{

                    get_Email_Settings();

                }catch(Exception ex){}
            }

            catch (Exception ex) {
                // ErroString=ex.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();


            //  Toast.makeText(getApplicationContext(), "bandwidth="+bandwidth +"\n" + ErroString, Toast.LENGTH_LONG).show();
        }
    }
    public void get_Email_Settings() {

        if (Server_Available == false) {
            return;
        }
        try {

        String URL5 = root + "/TimeSheet.asmx?op=GetEmailSettings";
        String SOAP_ACTION5 =  "https://tempuri.org/GetEmailSettings";
        String METHOD_NAME5 = "GetEmailSettings";

        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME5);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL5);
        androidHttpTransport.debug =true;

        PropertyInfo pi=new PropertyInfo();
        pi.setName("Fontra");
        pi.setValue(getSecurityToken() + "99");
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        // Make the soap call.
        androidHttpTransport.call(SOAP_ACTION5, envelope);

            SoapObject result = null;
            SoapPrimitive obj;
            // Make the soap call.

            result = (SoapObject) envelope.getResponse();

            email_seting = new Email_Settings();
            for (int i = 0; i < result.getPropertyCount(); i++) {
                try {
                    obj = (SoapPrimitive) result.getProperty("POP3Server");
                    email_seting.setPOP3Server(obj.toString());

                    obj = (SoapPrimitive) result.getProperty("POP3User");
                    email_seting.setPOP3User(obj.toString());

                    obj = (SoapPrimitive) result.getProperty("POP3Password");
                    email_seting.setPOP3Password(obj.toString());

                    obj = (SoapPrimitive) result.getProperty("SMTPServer");
                    email_seting.setSMTPServer(obj.toString());

                    obj = (SoapPrimitive) result.getProperty("SMTPUser");
                    email_seting.setSMTPUser(obj.toString());

                    obj = (SoapPrimitive) result.getProperty("SMTPPassword");
                    email_seting.setSMTPPassword(obj.toString());

                    obj = (SoapPrimitive) result.getProperty("FromEmail");
                    email_seting.setFromEmail(obj.toString());

                    obj = (SoapPrimitive) result.getProperty("FromDisplayName");
                    email_seting.setFromDisplayName(obj.toString() + " TRACCS Client Note Added for : " );

                    obj = (SoapPrimitive) result.getProperty("SMTP_Port");
                    email_seting.setSMTP_Port(obj.toString());


                } catch (Exception ex) {
                }

            }
        } catch (Exception ex) {
            email_seting=null;

        }
        try {
            if (email_seting==null) {
                return;
            }

            if (email_seting.getSMTPServer() != null && !email_seting.getSMTPServer().equals("")) {
                settings.edit().putString("SMTP_Server", email_seting.getSMTPServer()).commit();
                settings.edit().putString("SMTP_Port", email_seting.getSMTP_Port()).commit();
                settings.edit().putString("SMTP_User", email_seting.getSMTPUser()).commit();
                settings.edit().putString("SMTP_Password", email_seting.getSMTPPassword()).commit();
                settings.edit().putString("From_Address", email_seting.getFromEmail()).commit();
                settings.edit().putString("Email_Subject", email_seting.getFromDisplayName()).commit();
                show_SMTP_Server_Setting();
             }
        } catch (Exception ex) {
        }


    }
    public void get_Email_Settings2() {



        String SOAP_ACTION55 = "https://tempuri.org/GetEmailSettings";
        String METHOD_NAME55 = "GetEmailSettings";
        String URL55 = root + "/TimeSheet.asmx?op=GetEmailSettings";

        if (Server_Available == false) {
            return;
        }

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME55);
            HttpTransportSE androidHttpTransport2 = new HttpTransportSE(URL55);
            androidHttpTransport2.debug = true;

            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("Fontra");
            pi2.setValue(getSecurityToken() + "99");
            request.addProperty(pi2);



            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            SoapObject result = null;
            SoapPrimitive obj;
            // Make the soap call.
            androidHttpTransport2.call(SOAP_ACTION55, envelope);
           // result = (SoapObject) envelope.getResponse();
            // Make the soap call.
           // androidHttpTransport.call(SOAP_ACTION6, envelope);

            String xml = androidHttpTransport2.responseDump;
            // txtServer.setText(androidHttpTransport.requestDump);

            File fileDir = null;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            fileDir = new File(froot.getAbsolutePath() + "/.server/");

            XmlSerializer serializer = Xml.newSerializer();
            File newxmlfile = new File(fileDir, "server_setting.xml");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
                    newxmlfile.createNewFile();
                } catch (IOException e) {

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
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(),e.toString() , Toast.LENGTH_LONG).show();
        }
    }
}
