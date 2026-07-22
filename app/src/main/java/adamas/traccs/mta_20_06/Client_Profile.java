package adamas.traccs.mta_20_06;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Client_Profile extends AppCompatActivity {
    String FirstName = "";
    String PreferredName = "";
    String lastName = "";
    String dateOfBirth = "";
    String age = "";
    String Cordinator = "";
    String Address = "";
    String Phone = "030100";
    String Actual_Client_Code = "";
    final String PREFS_NAME = "MTAPrefs";
    SharedPreferences settings = null;
    String Personid = "";
    String ShowClientPhoneInApp = "false";
    byte[] buffer2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client__profile);
        setupActionBar();

        Bundle b = getIntent().getExtras();
        settings = getSharedPreferences(PREFS_NAME, 0);
        try {

            FirstName = b.getString("FirstName");
            PreferredName = b.getString("PreferredName");
            lastName = b.getString("lastName");
            dateOfBirth = b.getString("dateOfBirth");
            age = b.getString("age");
            Cordinator = b.getString("Cordinator");
            Address = b.getString("address");
            Personid = b.getString("Personid");
            Phone = b.getString("Phone");
            Actual_Client_Code = b.getString("Actual_Client_Code");
            ShowClientPhoneInApp = b.getString("ShowClientPhoneInApp");
        } catch (Exception ex) {
        }

        TextView txtAddress = findViewById(R.id.txtAddress);
        TextView txtPhone = findViewById(R.id.txtPhone);
        txtAddress.setText(Address);
        String msg = "";
        View vphone= findViewById(R.id.cardView6);
        vphone.setVisibility(View.GONE);
        if (Actual_Client_Code.equalsIgnoreCase("!MULTIPLE") || Actual_Client_Code.equalsIgnoreCase("!INTERNAL")) {

        } else if (ShowClientPhoneInApp.equalsIgnoreCase("true") ||ShowClientPhoneInApp.equalsIgnoreCase("1")) {
            //msg =  (Address == null ? "-" : Address);
            //   msg = msg + "\nPhone : " + Phone + "\n";
            txtPhone.setText(Phone);
            vphone.setVisibility(View.VISIBLE);

        }

        vphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Dial_Phone();
            }
        });
        View cardView5 = findViewById(R.id.cardView5);
        if (settings.getString("HideAddress","0").equalsIgnoreCase("true")) {
            cardView5.setVisibility(View.INVISIBLE);
        }
    try{
            TextView txtClient = findViewById(R.id.txtClient);
            txtClient.setText(FirstName  + " " + lastName );
    }catch(Exception ex){}

        TextView txtDOB = findViewById(R.id.txtDOB);
    try {
        String DATE_FORMAT_1 = "dd MMMM yyyy";
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        DateFormat.format(DATE_FORMAT_1, new Date(dateOfBirth));
       // String dob =df.format(DATE_FORMAT_1, new Date(dateOfBirth)).toString();
       txtDOB.setText(dateOfBirth);

    }catch(Exception ex){  txtDOB.setText(dateOfBirth);}

    try{
        View cardView3 = findViewById(R.id.cardView3);
        if (settings.getString("HideDOB","0").equalsIgnoreCase("true")) {
            cardView3.setVisibility(View.INVISIBLE);
        }else{
            cardView3.setVisibility(View.VISIBLE);
        }
    }catch(Exception ex){}
    try{
        TextView txtAge = findViewById(R.id.txtAge);
        txtAge.setText(age);
    }catch(Exception ex){}
    try{
        TextView txtCoordinator = findViewById(R.id.txtCoordinator);
        txtCoordinator.setText(Cordinator);
    }catch(Exception ex){}
        ImageView imageView2 =(ImageView)findViewById(R.id.profile_image);


        try {
            String text = settings.getString(Personid, "Nothing");

            if (!text.equalsIgnoreCase("Nothing")) {
                buffer2 = Base64.decode(text, 0);
                Bitmap photo = BitmapFactory.decodeByteArray(buffer2, 0, buffer2.length);
                imageView2.setImageBitmap(photo);

            }
        }catch(Exception ex){}
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

    void Dial_Phone(){
        try {
            //Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(Phone));
            Intent intent = new Intent(Intent.ACTION_CALL);

            intent.setData(Uri.parse("tel:" + parsePhone(Phone)));
            if (ActivityCompat.checkSelfPermission(Client_Profile.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Client_Profile.this.startActivity(intent);

        }catch(Exception ex){
            Toast.makeText(Client_Profile.this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    String parsePhone(String Phone){
        String ph   =Phone.replace("HOME","");
        ph          =ph.replace("MOBILE","");
        ph          =ph.replace("(","");
        ph          =ph.replace(")","");
        ph          =ph.replace("-","");
        ph          =ph.replace(" ","");
        return ph;
    }
}
