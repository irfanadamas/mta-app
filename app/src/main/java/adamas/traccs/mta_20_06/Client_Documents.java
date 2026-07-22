package adamas.traccs.mta_20_06;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.os.Handler;
import android.util.Xml;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.text.HtmlCompat;

public class Client_Documents extends AppCompatActivity {


    public String root="https://58.162.142.150/timesheet" ; //https://10.0.2.2:49884
    private final String NAMESPACE = "https://tempuri.org/";
    String StaffCode="";
    String OperatorId="";
    String Security_Token="";

    String PersonId="";
    String Recipient="";
    ListView listViewDocument=null;
    ArrayList<Document> lst_documents=null;
    Boolean Server_Available=true;
    List<String> openedFiles=null;
    File froot = null;
    DownloadManager mManager;
    String DocPath;
    String FileName;
    String Extension;
    String link;
 //   InputStreamVolleyRequest vRequest=null;
    int Activity_Code=0;
    String all_flies="";
    long downloadID=0;
    ProgressDialog pDialog=null;
    final String PREFS_NAME = "MTAPrefs";
    SharedPreferences settings = null;

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

    @SuppressLint("GestureBackNavigation")
    @Override
    public void onBackPressed() {


        try {

            Document doc=null;
            String File_names="";

            for (int i=0; i<lst_documents.size(); i++)
            {
                if (i==0)
                    File_names= lst_documents.get(i).getFileName();
                else
                    File_names= File_names + "," + lst_documents.get(i).getFileName();

            }
            all_flies=File_names;
            try{
                new MyAsyncClass_Remove_Doc().execute();
            }catch(Exception ex){}
        } catch (Exception ex) {

        }
        
        super.onBackPressed();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_file, menu);

        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            menu.clear();
        }
        return false;
    }
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
    public void onDestroy() {
        super.onDestroy();

        try {
            File droot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String path;

            path = droot.getPath();
            File file_root = new File(path);
            File[] Files = file_root.listFiles();
            try {
                for(File file: Files) {

                    if (check_File(file.getName())){
                        file.delete();
                        if (file.exists()) {
                            file.getCanonicalFile().delete();
                            if (file.exists()) {
                                getApplicationContext().deleteFile(file.getName());
                            }
                        }
                    }
                }
                Thread.sleep(100);
                file_root = new File(path + "/Adobe Acrobat");
                Files = file_root.listFiles();
                for(File file: Files) {

                    if (check_File(file.getName())){
                        file.delete();
                        if (file.exists()) {
                            file.getCanonicalFile().delete();
                            if (file.exists()) {
                                getApplicationContext().deleteFile(file.getName());
                            }
                        }
                    }
                }

                Thread.sleep(500);
            } catch (Exception ex) {
            }

        } catch (Exception ex) {

        }
    }
    boolean check_File(String fileName){

        for(String f:openedFiles){
            if (fileName.startsWith(f))
                return  true;
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client__documents);


        setupActionBar();
        settings = getSharedPreferences(PREFS_NAME, 0);

        set_server_Ip();
        openedFiles= new ArrayList<String>();
        TextView txtTitle=findViewById(R.id.txtTitle);

        txtTitle.setText(HtmlCompat.fromHtml("<b>Recipient</b><br>" + Recipient,0));

       // txtTitle.setText("Recipient\n" + Recipient);

        lst_documents = new ArrayList<Document>();
        listViewDocument = (ListView) findViewById(R.id.listViewDocuments);

       // registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        // if(lst_documents.size()>0)
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
               //         .setAction("Action", null).show();
            }
        });*/
      //  final CheckBox chkDocument = (CheckBox) findViewById(R.id.chkDocument);
      //  final  CheckBox chkCareplans = (CheckBox) findViewById(R.id.chkCareplans);

        final  Button btnExit = (Button) findViewById(R.id.btnExit);

        try{
            new MyAsyncClass().execute("DOCUMENT");
        }catch (Exception ex){}

        btnExit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {

                    Document doc=null;
                    String File_names="";

                    for (int i=0; i<lst_documents.size(); i++)
                    {
                        if (i==0)
                            File_names= lst_documents.get(i).getFileName();
                        else
                            File_names= File_names + "," + lst_documents.get(i).getFileName();

                    }
                    all_flies=File_names;
                    try{
                        new MyAsyncClass_Remove_Doc().execute();
                    }catch(Exception ex){}
                } catch (Exception ex) {
                    finish();
                }
            }
        });
/*
        chkDocument.setVisibility(View.GONE) ;
        chkCareplans.setVisibility(View.GONE) ;

        chkDocument.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {
                    chkCareplans.setChecked(false);
                   // lst_documents= getDocument_List("DOCUMENT");
                    try{
                        new MyAsyncClass().execute("DOCUMENT");
                    }catch (Exception ex){}

                   // if(lst_documents.size()>0)
                        listViewDocument.setAdapter(new Document_Adapter(getApplicationContext(),lst_documents));

                }
            }
        });

        chkCareplans.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {
                    chkDocument.setChecked(false);
                   try{
                       new MyAsyncClass().execute("CAREPLAN");
                   }catch (Exception ex){}

                  //  if(lst_documents.size()>0)
                        listViewDocument.setAdapter(new Document_Adapter(getApplicationContext(),lst_documents));
                }
            }
        });*/

        listViewDocument.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {

                    Document doc= lst_documents.get(position);

                     link=root + "" + doc.getNewLocation();





                    DocPath=doc.getOriginalLocation();
                    FileName=doc.getFileName();
                    Extension= doc.getDocumentType();
                   // Toast.makeText(Client_Documents.this, DocPath + "\n" + FileName + "\n" + link, Toast.LENGTH_LONG).show();
                    String FilePat="";
                    int ind=FileName.indexOf('.');
                    if (ind<0) ind=FileName.length();
                    FilePat= FileName.substring(0,ind);

                    if (!openedFiles.contains(FilePat))
                        openedFiles.add(FilePat);
                    //openedFiles.add(FilePat + "*.*");

                    // Toast.makeText(Client_Documents.this, FilePat + "*.*", Toast.LENGTH_LONG).show();
                    Thread.sleep(1000);
                    try{
                        new MyAsyncClass_Doc().execute();
                    }catch(Exception ex){}





                }catch(Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void set_server_Ip()
    {

        try{
            Bundle bundle = getIntent().getExtras();
            Recipient= bundle.getString("Recipient");
            PersonId=bundle.getString("PersonId","0");

            root=settings.getString("root","");
            Server_Available=settings.getBoolean("Server_Available",false);


            Security_Token=settings.getString("Security_Token","");
            OperatorId=settings.getString("OperatorId","");



        }catch(Exception ex){}
    }
    @Override
    public void onStop(){
        super.onStop();


    }

    String getSecurityToken(){

        String Val= OperatorId  + "$" + Security_Token + "$";
        return Val;
    }
    void getDocument_List(String DocGroup){


        Document doc=null;

        String URL3 = root  + "/TimeSheet.asmx?op=getClientDocuments";
        String SOAP_ACTION3 =  "https://tempuri.org/getClientDocuments";
        String METHOD_NAME3 = "getClientDocuments";

        String buff="";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME3);

        try{
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL3);
            androidHttpTransport.debug =true;
            PropertyInfo pi=new PropertyInfo();
            pi.setName("PersonId");
            pi.setValue(getSecurityToken() +PersonId);
            request.addProperty(pi);

            PropertyInfo pi2=new PropertyInfo();
            pi2.setName("DocumentGroup");
            pi2.setValue(DocGroup);
            request.addProperty(pi2);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;

            envelope.setOutputSoapObject(request);
            androidHttpTransport.call(SOAP_ACTION3, envelope);

             SoapObject result=(SoapObject) envelope.getResponse();

            if (result.getPropertyCount() <= 0) return ;
            SoapObject obj=null;
            try {
                SoapPrimitive  result2=null;
                for (int j=0; j<result.getPropertyCount(); j++)
                {

                    doc= new Document();
                    obj =(SoapObject)result.getProperty(j);

                    result2 = (SoapPrimitive) obj.getProperty("Doc_ID");
                    doc.setDoc_ID(result2.toString());

                    result2 = (SoapPrimitive) obj.getProperty("Title");
                    doc.setTitle(result2.toString());

                    try{
                    result2 = (SoapPrimitive) obj.getProperty("Created");
                    doc.setCreated(result2.toString());

                     }catch (Exception ex){ }


                    try{
                        result2 = (SoapPrimitive) obj.getProperty("Classification");
                        doc.setClassification(result2.toString());

                     }catch (Exception ex){ }

                     try{
                         result2 = (SoapPrimitive) obj.getProperty("Status");
                         doc.setStatus(result2.toString());

                    }catch (Exception ex){ }

                    try{
                    result2 = (SoapPrimitive) obj.getProperty("Category");
                    doc.setCategory(result2.toString());
                    }catch (Exception ex){ }

                    try{
                    result2 = (SoapPrimitive) obj.getProperty("CareDomian");
                    doc.setCareDomian(result2.toString());
                    }catch (Exception ex){ }

                    try{
                     result2 = (SoapPrimitive) obj.getProperty("modified");
                     doc.setModified(result2.toString());
                  }catch (Exception ex){ }

                    try{
                        result2 = (SoapPrimitive) obj.getProperty("FileName");
                        doc.setFileName(result2.toString());
                     }catch (Exception ex){ }

                    try{
                        result2 = (SoapPrimitive) obj.getProperty("OriginalLocation");
                        doc.setOriginalLocation(result2.toString());
                    }catch (Exception ex){ }

                    try{
                    result2 = (SoapPrimitive) obj.getProperty("NewLocation");
                    doc.setNewLocation(result2.toString());
                }catch (Exception ex){ }

                try{
                    result2 = (SoapPrimitive) obj.getProperty("DocumentType");
                    doc.setDocumentType(result2.toString());
                  }catch (Exception ex){ }

                    try{
                    result2 = (SoapPrimitive) obj.getProperty("DocumentGroup");
                    doc.setDocumentGroup(result2.toString());
                 }catch (Exception ex){ }

                    lst_documents.add(doc);
            }

            }catch (Exception ex){ }
        }catch (Exception ex){ }


    }

    class MyAsyncClass extends AsyncTask<String, Void, Void>

    {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
        super.onPreExecute();

        pDialog = new ProgressDialog(Client_Documents.this);
        pDialog.setMessage("Please wait while Processing....");
        pDialog.show();

    }

        @Override
        protected Void doInBackground(String... mApi) {

    try{
        getDocument_List(mApi[0]);
        }catch(Exception ex){}
        return null;
    }

        @Override
        protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        pDialog.cancel();
        if (lst_documents==null) return;
        if (lst_documents.size()>0)
            listViewDocument.setAdapter(new Document_Adapter(listViewDocument.getContext(),lst_documents));
        //  Toast.makeText(getApplicationContext(), "Email send", 100).show();
    }
    }



    class MyAsyncClass_Doc extends AsyncTask<Void, Void, Void> {

      //  ProgressDialog pDialog;
        boolean local=false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Client_Documents.this);
            pDialog.setMessage("Please wait while loading document....");
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... mApi) {

            try {

                Copy_Document();


            }catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }



        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            DownloadFile(link);
            pDialog.dismiss();



        }
    }


    class MyAsyncClass_Remove_Doc extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;
        boolean local=false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  pDialog = new ProgressDialog(Client_Documents.this);
          //  pDialog.setMessage("Please wait while loading document....");
         //   pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... mApi) {

            try {

                Remove_Document();


            }catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }



        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            finish();
        }
    }

    void Copy_Document()
    {
        if (!Server_Available) {

            return;
        }
        String NAMESPACE = "https://tempuri.org/";
        String URL6 = root + "/Timesheet.asmx?op=Copy_Web_Document";
        String SOAP_ACTION6 = "https://tempuri.org/Copy_Web_Document";
        String METHOD_NAME6 = "Copy_Web_Document";


        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME6);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug = true;

            PropertyInfo pi1 = new PropertyInfo();
            pi1.setName("PersonID");
            pi1.setValue(getSecurityToken() + PersonId);
            request.addProperty(pi1);


            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("Extension");
            pi2.setValue( Extension);
            request.addProperty(pi2);

            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("FileName");
            pi3.setValue(FileName);
            request.addProperty(pi3);

            PropertyInfo pi4 = new PropertyInfo();
            pi4.setName("DocPath");
            pi4.setValue( DocPath);
            request.addProperty(pi4);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION6, envelope);

            SoapObject result = (SoapObject) envelope.getResponse();



        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }


    }


    void Remove_Document()
    {
        if (!Server_Available) {

            return;
        }
        String NAMESPACE = "https://tempuri.org/";
        String URL6 = root + "/Timesheet.asmx?op=Remove_Web_Document";
        String SOAP_ACTION6 = "https://tempuri.org/Remove_Web_Document";
        String METHOD_NAME6 = "Remove_Web_Document";


        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME6);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL6);
            androidHttpTransport.debug = true;

            PropertyInfo pi1 = new PropertyInfo();
            pi1.setName("PersonID");
            pi1.setValue(getSecurityToken() + PersonId);
            request.addProperty(pi1);


            PropertyInfo pi2 = new PropertyInfo();
            pi2.setName("Extension");
            pi2.setValue( Extension);
            request.addProperty(pi2);

            PropertyInfo pi3 = new PropertyInfo();
            pi3.setName("FileName");
            pi3.setValue( all_flies);
            request.addProperty(pi3);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Make the soap call.
            androidHttpTransport.call(SOAP_ACTION6, envelope);

            SoapObject result = (SoapObject) envelope.getResponse();



        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }


    }
    @Override
    protected void onResume() {
        super.onResume();



    }
    public void DownloadFile(String link) {


        try {

            //This comentted by irfan at 25 sep 24
           // This code is open documents in external pdf view app
                 openFile(link);
            //This comentted by irfan at 25 sep 24



            // This code commented by irfan 31 May 2026
            //This code is open document in WebView
        /*    Intent install = new Intent(Client_Documents.this,DocumetView.class);
            Bundle b= new Bundle();

            String fixedUrl = link.replace(" ", "%20");

            b.putString("link",fixedUrl);
            b.putString("root",root);
            b.putBoolean("Server_Available",Server_Available);
            b.putString("OperatorId",OperatorId);
            b.putString("Security_Token",Security_Token);

            install.putExtras(b);
            Client_Documents.this.startActivity(install);
*/

             // This code commented by irfan 31 May 2026



            

            if (settings.getString("EnableDocExteranlView","false").equalsIgnoreCase("true")){

                openFile(link);

            }else{

                Intent install = new Intent(Client_Documents.this,DocumetView.class);
                Bundle b= new Bundle();

                String fixedUrl = link.replace(" ", "%20");

                b.putString("link",fixedUrl);
                b.putString("root",root);
                b.putBoolean("Server_Available",Server_Available);
                b.putString("OperatorId",OperatorId);
                b.putString("Security_Token",Security_Token);

                install.putExtras(b);
                Client_Documents.this.startActivity(install);

            }




            //  startActivity(install);

            //Intent install2 = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            // startActivity(install2);

            // File droot =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            //  String path=droot.getPath() + "/" + FileName;
            //  File fileDir = new File(path);

            // openFile(fileDir);




        }catch(Exception e) {
            //Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();

            Intent install2 = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(install2);
        }

    }


    public  void openFile(File url) throws IOException {
        // Create URI
        File file=url;
        // Uri uri = Uri.fromFile(url);
        Uri uri = FileProvider.getUriForFile(Client_Documents.this, getApplicationContext().getPackageName() + ".provider", file);
      //  Uri uri =Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if(url.toString().toLowerCase().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if(url.toString().toLowerCase().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if(url.toString().toLowerCase().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if(url.toString().toLowerCase().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if(url.toString().toLowerCase().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if(url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if(url.toString().toLowerCase().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if(url.toString().toLowerCase().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if(url.toString().toLowerCase().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if(url.toString().toLowerCase().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }

       // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//        intent.putExtra(PATH, path);
//        intent.putExtra(MIMETYPE, mimeType);
//        intent.setType(mimeType);
       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
       startActivity(intent);

    }
    public  void openFile(String url) throws IOException {

        // Create URI
        // File file=url;
        Uri uri = Uri.parse(url);
        // Uri uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (url.toLowerCase().contains(".doc") || url.contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if(url.toLowerCase().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if(url.toLowerCase().contains(".ppt") || url.contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if(url.toLowerCase().contains(".xls") || url.contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if(url.toLowerCase().contains(".zip") || url.contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if(url.toLowerCase().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if(url.toLowerCase().contains(".wav") || url.contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if(url.toLowerCase().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if(url.toLowerCase().contains(".jpg") || url.contains(".jpeg") || url.contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if(url.toLowerCase().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if(url.toLowerCase().contains(".3gp") || url.contains(".mpg") || url.contains(".mpeg") || url.contains(".mpe") || url.contains(".mp4") || url.contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }


        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(intent);
    }
    private static final int REQUEST_WRITE_PERMISSION = 786;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(new String[]{ "android.permission.WRITE_EXTERNAL_STORAGE"}, REQUEST_WRITE_PERMISSION);
    }

    private boolean canReadWriteExternal() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ContextCompat.checkSelfPermission(Client_Documents.this, "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED;
    }
}
