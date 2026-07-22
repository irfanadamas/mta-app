package adamas.traccs.mta_20_06;


import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


public class DocumetView extends AppCompatActivity {

    final int TIMEOUT_CONNECTION=20;
    final int TIMEOUT_SOCKET=20;
    String link="";
    String root="";
    String FileName;
    String DocPath;
    boolean Server_Available=false;
    String OperatorId;
    String Security_Token="";
    File froot;
    Context context;
    String output_path="";
    private static final String TAG = "Download Task";
    ///========================This class is never uses===================
    // It is being done in Client_Document Activity
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            String Title ="Client Document View"; //actionBar.getTitle().toString();
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
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        //  getMenuInflater().inflate(R.menu.menu_file, menu);

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documet_view);
        setupActionBar();
        context=this.getApplicationContext();
        Bundle b= getIntent().getExtras();

        root=b.getString("root");
        link= b.getString("link");
        Server_Available= b.getBoolean("Server_Available");
        OperatorId= b.getString("OperatorId");
        Security_Token= b.getString("Security_Token");
        DocPath= b.getString("DocPath");
        FileName= b.getString("FileName");

        // Toast.makeText(this, link, Toast.LENGTH_SHORT).show();

        try{
            load_file();
            // new MyAsyncDownload().execute(link);
            //  new MyAsyncClass_Doc().execute();
        }catch(Exception ex){}

        //DownloadFile(link);

     /*  // Toast.makeText(this, link , Toast.LENGTH_SHORT).show();
        try {
            WebView webView = (WebView) findViewById(R.id.webview);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(link);
        }catch (Exception ex) {}*/



    }

    public void DownloadFile(String link) {
        try {

            Intent install = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(install);


        }catch(Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();


        }

    }
    String getSecurityToken() {

        String Val = OperatorId + "$" + Security_Token + "$";
        return Val;
    }


    class MyAsyncDownload extends AsyncTask<String, Void, Void>

    {
        boolean flag = true;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(DocumetView.this);
            pDialog.setMessage("Please wait while downloading....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... mApi) {


            boolean downloading =true;
            String dlink=mApi[0];


            try{
                begin_download(dlink);

            }catch (Exception e) {
                flag = false;

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();
            load_file();

        }
    }

    void load_file(){
        WebView  web_view = (WebView)findViewById(R.id.web_view);

        //   web_view.loadUrl(link);
        WebSettings webSettings = web_view.getSettings();

        //    web_view.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

       webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setLoadsImagesAutomatically(true);


        web_view.setWebViewClient(new WebViewClient() {
            // Override page so it's load on my view only
            @Override
            public boolean shouldOverrideUrlLoading(WebView  view, String  url)
            {
                // Return true to override url loading (In this case do nothing).
                return false;
            }
            //  ProgressDialog progressDialog = new ProgressDialog(DocumetView.this);

            public void onPageFinished(WebView view, String url) {
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }
            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);

            }
        });


        web_view.loadUrl("https://docs.google.com/gview?embedded=true&url=" + link);

        //web_view.loadUrl("file:///android_asset/web/viewer.html?file=" +link);


        File droot =  (context.getExternalFilesDir(null));//Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String path=droot.getPath() + "/" + FileName;
        final File fileDir = new File(path);
        try {
            //web_view.loadUrl("file://" + fileDir.getPath());
         //  web_view.loadUrl(" file:///android_asset/web/viewer.html?file=" + fileDir.getPath());
          //  web_view.loadUrl(" file:///android_asset/web/viewer.html?file=" +link);

        }catch(Exception ex){
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    void begin_download(String dlink) {
        File apkStorage = null;
        File outputFile = null;
        try {


            URL url = new URL(dlink);//Create Download URl
            HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
            c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
            c.connect();//connect the URL Connection

            //If Connection response is not OK then show Logs
            if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                        + " " + c.getResponseMessage());

            }


            apkStorage = new File(context.getExternalFilesDir(null).getAbsolutePath() + "/mta");

            if (!apkStorage.exists())
                apkStorage.mkdirs();

            outputFile = new File(apkStorage, FileName);//Create Output file in Main File

            //Create New File if not present
            if (!outputFile.exists()) {
                outputFile.createNewFile();
                Log.e(TAG, "File Created");
            }
            output_path=outputFile.getAbsolutePath();
            FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

            InputStream is = c.getInputStream();//Get InputStream for connection

            byte[] buffer = new byte[1024];//Set buffer type
            int len1 = 0;//init length
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);//Write new file
            }

            //Close all connection after doing task
            fos.close();
            is.close();

        } catch (Exception e) {

            //Read exception if something went wrong
            e.printStackTrace();
            outputFile = null;
            Log.e(TAG, "Download Error Exception " + e.getMessage());
        }

    }
}
