package adamas.traccs.mta_20_06;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class Note_Filter extends AppCompatActivity {
    TextView txtOPNote;
    TextView txtCaseNote;
    TextView txtClinicalNote;
    TextView txtSVCNote;
    TextView txtViewAll;

    ImageView tickClinicalNote;
    ImageView tickCaseNote;
    ImageView tickOPNote;
    ImageView tickSVCNote;
    ImageView tickViewAllTick;
    String EnableViewNoteCases;
    SharedPreferences settings = null;
    String Note_Type="OPNOTE";
    final String PREFS_NAME = "MTAPrefs";
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
                // actionBar.setDisplayHomeAsUpEnabled(true);
                ImageView imageMenu = (ImageView) actionBar.getCustomView().findViewById(R.id.imageMenu);
                imageMenu.setVisibility(View.INVISIBLE);
                ImageView imageBack = (ImageView) actionBar.getCustomView().findViewById(R.id.imageBack);
                //   imageBack.setVisibility(View.GONE);
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

        switch (item.getItemId()) {
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
        setContentView(R.layout.activity_note__filter);
        setupActionBar();
        Note_Type= getIntent().getStringExtra("Main_Op_Note");

        settings = getSharedPreferences(PREFS_NAME, 0);
        EnableViewNoteCases=settings.getString("EnableViewNoteCases","00000");

         txtOPNote = findViewById(R.id.txtOPNote);
         txtCaseNote= findViewById(R.id.txtCaseNote);
         txtClinicalNote= findViewById(R.id.txtClinicalNote);
         txtSVCNote= findViewById(R.id.txtSVCNote);
         txtViewAll= findViewById(R.id.txtViewAll);

        tickOPNote= findViewById(R.id.tickOPNote);
        tickCaseNote= findViewById(R.id.tickCaseNote);
        tickClinicalNote= findViewById(R.id.tickClinicalNote);
        tickSVCNote= findViewById(R.id.tickSVCNote);

         tickViewAllTick= findViewById(R.id.tickViewAllTick);

        tickOPNote.setVisibility(View.GONE);
        tickCaseNote.setVisibility(View.GONE);
        tickClinicalNote.setVisibility(View.GONE);
        tickSVCNote.setVisibility(View.GONE);
        tickViewAllTick.setVisibility(View.GONE);


        txtOPNote.setVisibility(View.GONE);
        txtCaseNote.setVisibility(View.GONE);
        txtClinicalNote.setVisibility(View.GONE);
        txtSVCNote.setVisibility(View.GONE);
        txtViewAll.setVisibility(View.GONE);

        try{

            if (Integer.parseInt( EnableViewNoteCases.substring(0, 1))==1) {
                // chkOPNote.setVisibility(View.VISIBLE) ;
                txtOPNote.setVisibility(View.VISIBLE);
                txtViewAll.setVisibility(View.VISIBLE);
            }
            if ( Integer.parseInt( EnableViewNoteCases.substring(1, 2))==1) {
                txtCaseNote.setVisibility(View.VISIBLE);
                txtViewAll.setVisibility(View.VISIBLE);
            }
            if ( Integer.parseInt( EnableViewNoteCases.substring(2, 3))==1) {
                //    chkIncidentNote.setVisibility(View.GONE) ; //Hide note all the time
                //  Main_Op_Note="IMNOTE";
                //chkIncidentNote.setChecked(true);
                txtSVCNote.setVisibility(View.VISIBLE);
                txtViewAll.setVisibility(View.VISIBLE);
            }
            if ( Integer.parseInt( EnableViewNoteCases.substring(3, 4))==1 ) {
                // chkServiceNote.setVisibility(View.GONE) ; //Hide note all the time

            }
            if (Integer.parseInt( EnableViewNoteCases.substring(4, 5))==1) {
                txtClinicalNote.setVisibility(View.VISIBLE);
                txtViewAll.setVisibility(View.VISIBLE);
            }

        }catch(Exception ex){}



        if (Note_Type.equalsIgnoreCase("OPNOTE"))
            tickOPNote.setVisibility(View.VISIBLE);
        else if (Note_Type.equalsIgnoreCase("CASENOTE"))
            tickCaseNote.setVisibility(View.VISIBLE);
        else if (Note_Type.equalsIgnoreCase("CLINNOTE"))
            tickClinicalNote.setVisibility(View.VISIBLE);
        else if (Note_Type.equalsIgnoreCase("SVCNOTE"))
            tickSVCNote.setVisibility(View.VISIBLE);
        else if (Note_Type.equalsIgnoreCase("VIEW ALL"))
            tickViewAllTick.setVisibility(View.VISIBLE);

        txtOPNote.setOnTouchListener(onTouchListener);
        txtCaseNote.setOnTouchListener(onTouchListener);
        txtClinicalNote.setOnTouchListener(onTouchListener);
        txtSVCNote.setOnTouchListener(onTouchListener);
        txtViewAll.setOnTouchListener(onTouchListener);

       /* ListView lstfilters = findViewById(R.id.lstFilter);
        String lst[] = getResources().getStringArray(R.array.filters);
        lstfilters.setAdapter(new FilterAdapter(this,lst));*/


    }
    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {


            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    tickOPNote.setVisibility(View.GONE);
                    tickCaseNote.setVisibility(View.GONE);
                    tickClinicalNote.setVisibility(View.GONE);
                    tickSVCNote.setVisibility(View.GONE);
                    tickViewAllTick.setVisibility(View.GONE);

                    //Here if you want to know from wich touch this event has accured you can do following code
                    switch (v.getId()) {
                        case R.id.txtOPNote:
                            Note_Type="OPNOTE";
                            tickOPNote.setVisibility(View.VISIBLE);

                            break;
                        case R.id.txtCaseNote:
                            Note_Type="CASENOTE";
                            tickCaseNote.setVisibility(View.VISIBLE);
                            break;
                        case R.id.txtClinicalNote:
                            Note_Type="CLINNOTE";
                            tickClinicalNote.setVisibility(View.VISIBLE);
                            break;
                        case R.id.txtSVCNote:
                            Note_Type="SVCNOTE";
                            tickSVCNote.setVisibility(View.VISIBLE);
                            break;
                        case R.id.txtViewAll:
                            Note_Type="VIEW ALL";
                            tickViewAllTick.setVisibility(View.VISIBLE);
                            break;

                    }

                    break;
                case MotionEvent.ACTION_UP:
                    // Action you you want on finger up

                    break;

            }
            OP_Case_Note_Activity.Main_Op_Note=Note_Type;
            //finishActivity(100);
            finish();
            return true;
        }
    };

}