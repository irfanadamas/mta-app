package adamas.traccs.mta_20_06;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class LoginMode extends AppCompatActivity {

    File froot;
    final String PREFS_NAME = "MTAPrefs";
    SharedPreferences settings = null;
    boolean PinCode_Mode;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_mode);

        setupActionBar();








        settings = getSharedPreferences(PREFS_NAME, 0);

            Button   normal = (Button)findViewById(R.id.normal);
            Button   pincode_mode = (Button)findViewById(R.id.pincode_mode);


            normal.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    settings.edit().putBoolean("pin_code_mode", false).commit();
                    //set_Login_Mode_status_In_DB("Normal");
                    PinCode_Mode=false;
                    Login.PinCode_Mode=false;

                    finish();
                }
            });

            pincode_mode.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    try{
                        PinCode_Mode=true;

                        Intent i= new Intent(LoginMode.this, PinCode_Login.class);

                        startActivity(i);
                        finish();


                    }catch(Exception ex){
                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();}
                }
            });

            // yourName = (EditText) findViewById(R.id.yourName);

    }



}
