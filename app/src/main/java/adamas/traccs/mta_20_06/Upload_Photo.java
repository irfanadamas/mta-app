package adamas.traccs.mta_20_06;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


//implements Response.ErrorListener, Response.Listener<String>

public class Upload_Photo extends AppCompatActivity {

    public String root="https://58.162.142.150/timesheet" ; //https://10.0.2.2:49884
    private final String NAMESPACE = "https://tempuri.org/";
    private  String URL = root  + "/TimeSheet.asmx?op=UploadFile";
    private final String SOAP_ACTION =  "https://tempuri.org/UploadFile";
    private final String METHOD_NAME = "UploadFile";

    private  String URL2 = root  + "/TimeSheet.asmx?op=DownloadFile";
    private final String SOAP_ACTION2 =  "https://tempuri.org/DownloadFile";
    private final String METHOD_NAME2 = "DownloadFile";

    int image_size=100;
    int new_height=250,new_width=250;

    final String PREFS_NAME = "MTAPrefs";
    SharedPreferences settings = null;
    Button btnBrowse;
    Button btnUpload;
    Button btnCancel;
    File froot = null;
    int bytesRead, bytesAvailable, bufferSize;
    byte[] buffer;
    int maxBufferSize = 1*1024*1024;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private boolean Server_Available;
    private String Personid;
    private TextView txtMsg;
    String RecipientDocFolder ="";
    ProgressDialog progressBar;
    private final int progressBarStatus = 0;
    private final Handler progressBarHandler = new Handler();
    String image_name="";
    String AccountNo="";
    String recordNo;
    String UserId="";
    String OperatorId;
    String Security_Token;
    String TAG="100";
    Uri filePath;

    String[] permissions = {
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.READ_INTERNAL_STORAGE",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_NETWORK_STATE",
            "android.permission.ACCESS_WIFI_STATE",
            "android.permission.CAMERA"

    };

    protected boolean shouldAskPermissions() {
        try{
            return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
        }catch (Exception ex){}

        return false;
    }

    protected void askPermissions()  {
        int requestCode = 200;
        try {
            requestPermissions(permissions, requestCode);
        }catch (Exception ex){}
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__photo);

        setupActionBar();

        if (shouldAskPermissions()) {
            askPermissions();
        }

        set_server_Ip();
        EditText   txtRosterNote= (EditText)findViewById(R.id.txtRosterNote);
      //  setMultiLineCapSentencesAndDoneAction(txtRosterNote);
        imageView=(ImageView)findViewById(R.id.imageView1);
        txtMsg=  (TextView)findViewById(R.id.txtMsg);
        if (Server_Available==false){
            Toast.makeText(getApplicationContext(), "This Service is not available in offline mode", Toast.LENGTH_LONG).show();
            txtMsg.setText("This Service is not available in offline mode");
        }
        // Read_Image();

        btnBrowse=(Button)findViewById(R.id.btnBrowse);
        btnBrowse.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try{
                    if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                    {
                        Toast.makeText(getApplicationContext(),"No Media exists to browse image/document", Toast.LENGTH_LONG).show ();
                        return;
                    }
                    Intent i = new Intent(
                    Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);

                    // Intent.ACTION_PICK, android.provider.MediaStore.Files.getContentUri(MediaStore.EXTRA_MEDIA_ALBUM) );
                    // Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    // startActivityForResult(i, RESULT_LOAD_IMAGE);

                } catch (Exception e) {
                    txtMsg.setText(e.toString());
                }

            } });

        btnUpload=(Button)findViewById(R.id.btnUpload);

        btnUpload.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                txtMsg.setVisibility(View.INVISIBLE);


                if (buffer==null)
                {
                   Tost_Message("No Image loaded");
                    return;
                }
                TextView txtnote= findViewById(R.id.txtRosterNote);
                if(txtnote.getText().toString().equals("")){
                    Tost_Message("Please enter valid photo description note");
                    txtnote.setError("Please fill field");
                    return;
                }
                Upload_Photo(v);
               // upload_Image(currentImagePath);
               // uploadData();
            }});

        Button btnSave=(Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                TextView txtnote= findViewById(R.id.txtRosterNote);
                if(txtnote.getText().toString().equals("")){
                    Tost_Message("Please enter valid photo description note");
                    txtnote.setError("Please fill field");
                    return;
                }

                Save_Document_Note();
            }});


        Button photoButton = (Button) this.findViewById(R.id.button1);
        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                  if (cameraIntent.resolveActivity(getPackageManager())!=null){
                      File imageFile =  null;
                      try {
                          imageFile = getImageFile();

                      }catch (IOException e){e.printStackTrace();}
                      if (imageFile!=null){

                          Uri imageUri = FileProvider.getUriForFile(v.getContext(),"adamas.traccs.mta_20_06.provider",imageFile);
                          cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                          startActivityForResult(cameraIntent, CAMERA_REQUEST);
                      }
                  }

                   // selectImage();

                }catch(Exception ex){
                    Toast.makeText(Upload_Photo.this, ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button btnSetSize=(Button)findViewById(R.id.btnSetSize);
        btnSetSize.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                get_image_size(v);
            }});



    } // Main method end

    String getSecurityToken(){

        String Val= OperatorId  + "$" + Security_Token + "$";
        return Val;
    }


    void UploadImage(){
        SoapPrimitive result=null;
        try{


            if (buffer==null)
            {
                txtMsg.setText("No Image not loaded correctly");
                return;
            }
            settings = getSharedPreferences(PREFS_NAME, 0);

            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug =true;
            try{
                PropertyInfo pi=new PropertyInfo();
                pi.type = MarshalBase64.BYTE_ARRAY_CLASS;
                pi.setName("buff");
                String  base64String = Base64.encode(buffer);
                pi.setValue(base64String);
                request.addProperty(pi);

                PropertyInfo pi2=new PropertyInfo();
                pi2.setName("fileName");
                pi2.setValue(getSecurityToken() + image_name );
                request.addProperty(pi2);

                PropertyInfo pi3=new PropertyInfo();
                pi3.setName("RecipientDocFolder");
                pi3.setValue(RecipientDocFolder);
                request.addProperty(pi3);

                PropertyInfo pi4=new PropertyInfo();
                pi4.setName("AccountNo");
                pi4.setValue(AccountNo);
                request.addProperty(pi4);

                try {
                    settings.edit().putString(Personid, base64String).commit();
                }catch(Exception ex){ Tost_Message( ex.toString());}



            } catch (Exception e) {txtMsg.setText( e.toString());}
            try{
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet=true;
                envelope.encodingStyle = SoapEnvelope.ENC;
                envelope.setOutputSoapObject(request);
                // Make the soap call.
                androidHttpTransport.call(SOAP_ACTION, envelope);
                result =(SoapPrimitive)envelope.getResponse();


            } catch (Exception e) {txtMsg.setText( e.toString());}


            if (result.toString().equals("OK")) {
                Tost_Message("Image/File saved successfully");
                txtMsg.setText(" Image/File saved successfully");
            }else
                Tost_Message("Image not saved\n" + result.toString());


        } catch (Exception e) {txtMsg.setText( e.toString());}
    }
    void set_server_Ip()
    {

        Bundle bundle = getIntent().getExtras();
        root=bundle.get("root").toString();
        Personid =bundle.get("Personid").toString();
        RecipientDocFolder=bundle.get("RecipientDocFolder").toString();
        AccountNo=  bundle.getString("AccountNo");
        recordNo=  bundle.getString("RecordNo");
        UserId=  bundle.getString("UserId");
        OperatorId=  bundle.getString("OperatorId");
        Security_Token=  bundle.getString("Security_Token");

        Server_Available= bundle.get("Server").equals("True");

        URL = root  + "/TimeSheet.asmx?op=UploadFile";
        URL2 = root  + "/TimeSheet.asmx?op=DownloadFile";
        //  Toast.makeText(this, "Personid="+Personid, Toast.LENGTH_SHORT).show();
    }
    void Read_Image()
    {

        if (Server_Available==true)
        {
            byte[] buffer2=null;
            SoapPrimitive  result=null;
            try{

                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME2);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL2);
                androidHttpTransport.debug =true;

                PropertyInfo pi2=new PropertyInfo();
                pi2.setName("fileName");
                pi2.setValue(getSecurityToken() + Personid +".png");
                request.addProperty(pi2);


                PropertyInfo pi3=new PropertyInfo();
                pi3.setName("RecipientDocFolder");
                pi3.setValue(RecipientDocFolder);
                request.addProperty(pi3);
                try{
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet=true;
                    envelope.encodingStyle = SoapEnvelope.ENC;
                    envelope.setOutputSoapObject(request);
                    // Make the soap call.
                    androidHttpTransport.call(SOAP_ACTION2, envelope);
                    result =(SoapPrimitive)envelope.getResponse();
                    String text = result.toString();
                    // buffer2 = text.getBytes("UTF-8");
                    buffer2=Base64.decode(text);
                    Bitmap photo = BitmapFactory.decodeByteArray(buffer2 , 0, buffer2.length);
                    imageView.setImageBitmap(photo);

                    //   scaleImage(imageView, 100); // in dp

                } catch (Exception e) {txtMsg.setText( "No Picture Exists");}

            } catch (Exception e) {txtMsg.setText( e.toString());}
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RESULT_LOAD_IMAGE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }


    String currentImagePath="";
    private File getImageFile()throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "jpg_"+timestamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(imageName, ".jpg",storageDir);
        currentImagePath =  imageFile.getAbsolutePath();
        return imageFile;
    }
    private void displayImage(){
        ImageView imageView2 = (ImageView) findViewById(R.id.imageView1);
        Bitmap photo=BitmapFactory.decodeFile(currentImagePath);

        //new_Width=800; new_Height=600;
        float ratio=0.5f;
       // Bitmap resized = Bitmap.createScaledBitmap(photo, newWidth, newHeight, true);
       // Bitmap resized = Bitmap.createScaledBitmap(photo,(int)(photo.getWidth()*ratio), (int)(photo.getHeight()*ratio), true);

      //  Bitmap resized = scaleDown(photo, newWidth, true);
        try {
            if (new_width > 0 && new_height > 0) {
                Bitmap resized = scaleBitmap(photo, new_width, new_height);
                imageView.setImageBitmap(resized);
                buffer = bitmapToByteArray(resized);
            } else {

                imageView.setImageBitmap(photo);
               // Bitmap resized = scaleBitmap(photo, 500, 500);
               // imageView.setImageBitmap(resized);

                buffer = bitmapToByteArray(photo);
            }
            // Bitmap photo = (Bitmap) data.getExtras().get("data");
        }catch(Exception ex){
            Log.d(TAG, "displayImage: " + "\nloading error:\n" + ex.toString());
           // Tost_Message("loading error:\n" + ex.toString());
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView2 = (ImageView) findViewById(R.id.imageView1);
            imageView2.setImageBitmap(BitmapFactory.decodeFile(picturePath));


            buffer= bitmapToByteArray(BitmapFactory.decodeFile(picturePath));
        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(photo);
//            buffer= bitmapToByteArray(photo);
//            // imageView.setMaxHeight(250);
//            //   imageView.setMaxWidth(185);
            //displayImage();
            try {
                new MyAsyncClass2().execute();
            }catch(Exception ex){}
        }

    }
    public static byte[] bitmapToByteArray(Bitmap bm) {
        // Create the buffer with the correct size
        int iBytes = bm.getWidth() * bm.getHeight() * 4;
        ByteBuffer buffer = ByteBuffer.allocate(iBytes);

        // Log.e("DBG", buffer.remaining()+""); -- Returns a correct number based on dimensions
        // Copy to buffer and then into byte array
        bm.copyPixelsToBuffer(buffer);
        // Log.e("DBG", buffer.remaining()+""); -- Returns 0

        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image=stream.toByteArray();

        return image;


    }
    String get_File_Name(String pId){
        DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        DateFormat tFormat = new SimpleDateFormat("HHmm");
        String file_Name="";

        Date date = new Date();
        String strDate = dateFormat.format(date);
        String strTime = tFormat.format(date);
        strDate = strDate + strTime;


        file_Name=pId + "_" + strDate + ".png";

        return  file_Name;
    }
    public void Upload_Photo(View view)
    {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat tFormat = new SimpleDateFormat("HH:mm");


        Date date = new Date();
        String strDate = dateFormat.format(date);
        String strTime = tFormat.format(date);
        strDate = strDate + " " + strTime;


        image_name=get_File_Name(Personid);

        try{

            new Upload_Photo.MyAsyncClass().execute();

        }catch(Exception ex){}

/*


        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.upload_prompt, null);
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("File Name");

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView );



        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text

                                String Note=userInput.getText().toString();
                                image_name=Note;

                                try{

                                    new Upload_Photo.MyAsyncClass().execute();

                                }catch(Exception ex){}

                                txtMsg.setVisibility(View.VISIBLE);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
*/

    }
    public void get_image_size(View view)
    {

        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.image_size_promt);
        dialog.setTitle("Image size");
        dialog.setCanceledOnTouchOutside(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        final EditText txtImageHeight = (EditText) dialog.findViewById(R.id.txtImageHeight);
        final EditText txtImagewidth = (EditText) dialog.findViewById(R.id.txtImagewidth);
        final RadioButton rdSmall =  dialog.findViewById(R.id.rdSmall);
        final RadioButton rdMedium =  dialog.findViewById(R.id.rdMedium);
        final RadioButton rdLarge =  dialog.findViewById(R.id.rdLarge);
        final RadioButton rdOriginal =  dialog.findViewById(R.id.rdOriginal);

        if (new_height==250){
            rdSmall.setChecked(true);
        }
        if (new_height==500){
            rdMedium.setChecked(true);
        }
        if (new_height==750){
            rdLarge.setChecked(true);
        }
        if (new_height==0){
            rdOriginal.setChecked(true);
        }

        rdSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtImageHeight.setText("250");
                txtImagewidth.setText("250");
                new_width=250;
                new_height=250;
            }
        });
        rdMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtImageHeight.setText("500");
                txtImagewidth.setText("500");
                new_width=500;
                new_height=500;
            }
        });
        rdLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtImageHeight.setText("750");
                txtImagewidth.setText("750");
                new_width=750;
                new_height=750;
            }
        });
        rdOriginal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtImageHeight.setText("0");
                txtImagewidth.setText("0");
                new_height=0;
                new_width=0;


            }
        });
        //txtImage_size.setText(imageView.getHeight());
        Button dialogButton = (Button) dialog.findViewById(R.id.btnOK);
        // button is clicked, close the custom dialog

        dialogButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {
                    // edit text
                    if (new_width==0){
                        new MyAsyncClass2().execute();
                        dialog.dismiss();
                        return;
                    }
                    new_height=Integer.parseInt(txtImageHeight.getText().toString());
                    new_width=Integer.parseInt(txtImagewidth.getText().toString());
                    imageView.getLayoutParams().height = new_height;

                    imageView.getLayoutParams().width = new_width;

                   // imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    //imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                   // scaleImage2(imageView, new_height,new_width); // in dp
                    //displayImage();
                    new MyAsyncClass2().execute();



                    dialog.dismiss();

                } catch (Exception ex) {
                }
            }
        });
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialog.dismiss();
                try {

                    dialog.dismiss();

                } catch (Exception ex) {
                }
            }
        });

        // show it
        dialog.show();

    }
    public  Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());

        return output;
    }
    public  Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
    @SuppressWarnings("deprecation")
    private void scaleImage(ImageView view, int x, int y)
    {
        // Get the ImageView and its bitmap
        Drawable drawing = view.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();

        // Get current dimensions
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) x) / width;
        float yScale = ((float) y) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        width = scaledBitmap.getWidth();
        height = scaledBitmap.getHeight();

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);

        //--------------compress image----------------------------
        try{
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, width, height, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int quality = 90;
            resized.compress(Bitmap.CompressFormat.PNG, quality, baos);

            int maxSize= height;
     /*int cnt=0;
       do {
    	   if (cnt==5) break;
    	   resized.compress(CompressFormat.PNG, quality, baos);
    	     if (baos.size() > maxSize)
    	    	 quality = quality * maxSize / baos.size();
    	     cnt=cnt+1;
    	   } while (baos.size() > maxSize);
     */
            byte[] bytesImage;
            bytesImage = baos.toByteArray();
            baos.close();
            baos.flush();
            buffer=bytesImage;
            Bitmap bmp = BitmapFactory.decodeByteArray(bytesImage, 0, bytesImage.length);

            BitmapDrawable result2 = new BitmapDrawable(bmp);
            view.setImageDrawable(result2);
            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.width = width;
            params.height = height;
            view.setLayoutParams(params2);

        }catch(Exception ex){}

    }
    private void scaleImage2(ImageView view, int x, int y)
    {
        // Get the ImageView and its bitmap
        Drawable drawing = view.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();

        // Get current dimensions
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();


        //--------------compress image----------------------------
        try{
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, width, height, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int quality = 90;
            resized.compress(Bitmap.CompressFormat.PNG, quality, baos);

            int maxSize= height;

            byte[] bytesImage;
            bytesImage = baos.toByteArray();
            baos.close();
            baos.flush();
            buffer=bytesImage;
            Bitmap bmp = BitmapFactory.decodeByteArray(bytesImage, 0, bytesImage.length);

            BitmapDrawable result2 = new BitmapDrawable(bmp);
            view.setImageDrawable(result2);
           /* LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.width = width;
            params.height = height;
            view.setLayoutParams(params2);*/

        }catch(Exception ex){}

    }
    void setMultiLineCapSentencesAndDoneAction(EditText txt) {
        txt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        txt.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        txt.setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES  );
    }
    private int dpToPx(int dp)
    {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }

    public void Make_update(String command)
    {
        try
        {

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

    void Save_Document_Note(){


        String command="";

        try{



            EditText   txtRosterNote= findViewById(R.id.txtRosterNote);
            String notes=txtRosterNote.getText().toString().replace("'", "''");

            UserId= settings.getString("UserId","0");


            command ="Insert into documents(DOCUMENTGROUP,Title ,Status,Created,Modified,FileName,OriginalLocation,Author,PersonID,DocumentType,Classification,Category,RTFText)" +
                    "Values('DOCUMENT','" + AccountNo + " Photo ' + CONVERT(varchar,getdate(),103)" + ",'O',getDate(),getDate(),'" + image_name + "','" + RecipientDocFolder +
                    "'," + UserId + ",'" + Personid + "','.png','PHOTO','PHOTO',N'" + notes  + "')";

            Make_update(command);
           // Tost_Message("Note saved successfully");




        }catch(Exception ex){
            // txtMsg.setText("Problem in data saving\n" + command + "\n" +  ex.toString());
        }

    }

    class MyAsyncClass extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Upload_Photo.this);
            pDialog.setMessage("Please wait while uploading photo  ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                //Save_Document_Note();
                UploadImage();

            } catch (Exception ex) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();
            try {
                Upload_Photo.this.setResult(2, null);

                Save_Document_Note();
                     if (currentImagePath != null) {
                        File f = new File(currentImagePath);
                        f.delete();
                    }

                btnUpload.setEnabled(false);
                finish();
            }catch (Exception ex){}

        }
    }
    class MyAsyncClass2 extends AsyncTask<Void, Void, Void> {

        LoadingDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new LoadingDialog(Upload_Photo.this);
            pDialog.setMessage("Please wait while loading photo  ....");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                //Save_Document_Note();
                displayImage();

            } catch (Exception ex) {
                Tost_Message(ex.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (buffer==null){
                displayImage();
            }
            pDialog.cancel();

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

    private void uploadData() {
        try {
            File mChoosenFile = new File(currentImagePath);
            Bitmap bmp= BitmapFactory.decodeFile(currentImagePath);
            //uploadBitmap(bmp);
            image_name = getSecurityToken() + get_File_Name(Personid);
           // UpLoadService.uploadFile(this.getApplicationContext(),URL, image_name, AccountNo, RecipientDocFolder, mChoosenFile, this, this);

            new UpLoadService(mChoosenFile, URL).execute();

        }catch (Exception ex){
            Tost_Message("ex:" + ex.toString());
        }
    }

//    @Override
//    public void onErrorResponse(VolleyError error) {
//        Tost_Message("respnse error:" + error.toString());
//    }
//
//    @Override
//    public void onResponse(String response) {
//        //Your response here
//        Tost_Message("response:" + response);
//    }
//    private void uploadBitmap(final Bitmap bitmap) {
//
//        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL,
//                new Response.Listener<NetworkResponse>() {
//                    @Override
//                    public void onResponse(NetworkResponse response) {
//                        try {
//                            JSONObject obj = new JSONObject(new String(response.data));
//                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
//                        Log.e("GotError",""+error.getMessage());
//                    }
//                }) {
//
//
//            @Override
//            protected Map<String, DataPart> getByteData() {
//                Map<String, DataPart> params = new HashMap<>();
//                //long imagename = System.currentTimeMillis();
//                image_name =  get_File_Name(Personid);
//                params.put("buff", new DataPart(image_name + ".png", getFileDataFromDrawable(bitmap)));
//
//                return params;
//            }
//
//            protected Map<String, String> getStringData() {
//                Map<String, String> params = new HashMap<>();
//                //long imagename = System.currentTimeMillis();
//                image_name = getSecurityToken() + get_File_Name(Personid);
//                params.put ("fileName", image_name);
//                params.put("RecipientDocFolder", RecipientDocFolder);
//                params.put("AccountNo", AccountNo);
//
//                return params;
//            }
//        };
//
//        //adding the request to volley
//        Volley.newRequestQueue(this).add(volleyMultipartRequest);
//    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
