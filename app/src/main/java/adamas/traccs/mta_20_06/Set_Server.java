package adamas.traccs.mta_20_06;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.protobuf.NullValue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;

import androidx.appcompat.app.AppCompatActivity;


public class Set_Server extends AppCompatActivity {

    final String PREFS_NAME = "MTAPrefs";
    SharedPreferences settings = null;
    ListView lstSevers;
    File froot;
    Context context;
    String root;
    List<Server_Link>lst_servers;
    EditText txtserver;
    EditText txtLinkName;
    View vHelp;
    int current_index=-1;
    boolean modify=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        context=this.getApplicationContext();
        setContentView(R.layout.activity_set__server);
        settings = getSharedPreferences(PREFS_NAME, 0);
        txtLinkName = findViewById(R.id.txtLinkName);
        txtserver = findViewById(R.id.txtServer);
        final Button btnSave = findViewById(R.id.btnSave);
        final Button btnAdd = findViewById(R.id.btnAdd);



        root= settings.getString("root","");
        if (!root.equals("")) {
            txtserver.setText(root);
            txtLinkName.setText(settings.getString("LinkName",""));
            txtserver.setEnabled(false);
            txtLinkName.setEnabled(false);

        }
        lstSevers= findViewById(R.id.lstServerLinks);
        Load_ServerList();
        if (lst_servers.size()>0){
            btnSave.setEnabled(false);
        }

        lstSevers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                btnAdd.setText("Modify Link");

                current_index=position;
                Server_Link link= lst_servers.get(position);
                if (!root.equals(link.getServerLink()))
                    settings.edit().putInt("loginAttempt", 0).commit();

                root=link.getServerLink();
                txtserver.setText(root);
                txtLinkName.setText(link.getCompanyName());

                settings.edit().putString("root",root).commit();
                settings.edit().putString("LinkName",link.getCompanyName()).commit();


                lstSevers.setAdapter( new ServerListAdpater(Set_Server.this, lst_servers));



            }
        });
      final  AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure, you want to remove this setting?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                Server_Link l= lst_servers.get(current_index);
                // lstSevers.removeViewAt(current_index);
                try {
                    Remove_Server(l.getCompanyName(), l.getServerLink());
                    Load_ServerList();
                    if (current_index>0){
                        current_index--;
                        Server_Link link= lst_servers.get(current_index);
                        root=link.getServerLink();
                        txtserver.setText(root);
                        txtLinkName.setText(link.getCompanyName());

                    }
                    btnAdd.setText("Add Link");

                }catch(Exception ex){}
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        lstSevers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                txtserver.setEnabled(false);
                txtLinkName.setEnabled(false);
                current_index=position;
                AlertDialog alert = builder.create();
                alert.show();

                return false;
            }


        });
        Button btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtserver.setEnabled(false);
                txtLinkName.setEnabled(false);
                Server_Link link= lst_servers.get(current_index);
                root=link.getServerLink();
                txtserver.setText(root);
                txtLinkName.setText(link.getCompanyName());

                settings.edit().putString("root",root).commit();
                settings.edit().putString("LinkName",link.getCompanyName()).commit();

                btnAdd.setText("Add Link");
            }
            });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              if(! URLUtil.isValidUrl(txtserver.getText().toString())){
                  Tost_Message("Can’t find link check details\nPlease enter valid server link");
                  return;
              }

                if (txtserver.getText().toString().equals("")){

                 Tost_Message("Please enter valid server link");
                }else if (!modify && Search_Server_record(txtLinkName.getText().toString(),txtserver.getText().toString())) {
                    Tost_Message("This Server Settings alreay exists");
                }else{

                    try {
                        settings.edit().putString("root", txtserver.getText().toString()).commit();
                        settings.edit().putString("LinkName", txtLinkName.getText().toString()).commit();
                        settings.edit().putInt("loginAttempt", 0).commit();

                       if(modify){
                           Server_Link l= lst_servers.get(current_index);
                           Remove_Server(l.getServerLink(),l.getCompanyName());
                           modify=false;
                           btnAdd.setText("Add Link");
                           lst_servers.remove(current_index);
                           current_index--;
                       }
                        set_Server(txtserver.getText().toString(),txtLinkName.getText().toString());
                        Tost_Message("Server Setting saved successfully");
                        Load_ServerList();
                        txtserver.setEnabled(false);
                        txtLinkName.setEnabled(false);
                       // finish();
                    }catch(Exception ex){}
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnSave.setEnabled(true);
                if (btnAdd.getText().toString().equalsIgnoreCase ("Modify Link")) {
                    txtserver.setEnabled(true);
                    txtLinkName.setEnabled(true);
                    txtLinkName.requestFocus();
                    modify=true;

                }else {

                    txtserver.setText("");
                    txtLinkName.setText("");
                    txtserver.setEnabled(true);
                    txtLinkName.setEnabled(true);
                    txtLinkName.requestFocus();
                }
            }
        });
        vHelp = findViewById(R.id.view);
        vHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  set_help(v.getContext());
                showCustomDialog();
            }
        });

       /* TextView txtHelp = findViewById(R.id.txtHelp);
        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_help(v.getContext());
            }
        });
        TextView txtHelp2 = findViewById(R.id.txtHelp2);
        txtHelp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_help(v.getContext());
            }
        });*/

    }


    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
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
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
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
    void Load_ServerList(){
        // File froot = null;
        try {
            // check for SDcard
            lst_servers=new ArrayList<Server_Link>();
            Server_Link link;
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File filein = null;
            File fileDir = null;
            String state = Environment.getExternalStorageState();
            //  textMsg.setText("No Server found - " + state);
            if (Environment.MEDIA_MOUNTED.equals(state)) {

                //check sdcard permission
                fileDir = new File(froot.getAbsolutePath() + "/.server/");

                filein = new File(fileDir, "serverIp.txt");

                if (filein.exists()) {
                    try {
                        FileReader fileReader = new FileReader(filein);
                        BufferedReader buf= new BufferedReader(fileReader);
                        String line="";
                        int indx=0;
                        while ((line = buf.readLine()) != null){
                            link= new Server_Link();
                            link.setCompanyName(line);

                            line = buf.readLine();
                            link.setServerLink(line);

                            lst_servers.add(link);

                            if (link.getServerLink().equalsIgnoreCase(root)){
                                current_index=indx;
                            }
                            indx++;
                        }
                        fileReader.close();
                        buf.close();
                    } catch (Exception e) {}
                    finally {

                    }
                }
            }
            if (lst_servers.size()>1);
            lstSevers.setAdapter( new ServerListAdpater(Set_Server.this, lst_servers));
        } catch (Exception e) {

        }
    }
    public void set_Server(String root, String LinKName) throws IOException {
        // File froot = null;
        try {
            // check for SDcard

            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File filein = null;
            File fileDir = null;
            String state = Environment.getExternalStorageState();
            //  textMsg.setText("No Server found - " + state);
            if (Environment.MEDIA_MOUNTED.equals(state)) {

                //check sdcard permission
                fileDir = new File(froot.getAbsolutePath() + "/.server/");

                filein = new File(fileDir, "serverIp.txt");
                String existing_record="";
                if (filein.exists()) {
                    try {

                        String line="";

                        for ( int i=0; i<lst_servers.size(); i++){

                            line= lst_servers.get(i).getCompanyName() + "\n" + lst_servers.get(i).getServerLink();
                            existing_record = existing_record + line + "\n";

                            }


                        existing_record=existing_record + LinKName + "\n" +root +"\n";

                        froot.setWritable(true);
                        //if (froot.canWrite()) {

                       File file = new File(fileDir, "serverIp.txt");
                       FileWriter filewriter = new FileWriter(file);
                       BufferedWriter out = new BufferedWriter(filewriter);

                        out.write(existing_record);

                       out.close();
                        filewriter.close();

                    } catch (Exception e) {

                    }
                } else {
                    froot.setWritable(true);

                    //if (froot.canWrite()) {

                    if (!fileDir.exists())
                        fileDir.mkdirs();

                    File file = new File(fileDir, "serverIp.txt");
                    FileWriter filewriter = new FileWriter(file);
                    BufferedWriter out = new BufferedWriter(filewriter);
                    out.write(LinKName + "\n" +root + "\n");
                    out.close();
                }
            }
        } catch (Exception e) {

        }

    }
     boolean Search_Server_record(String root, String LinKName)  {
        // File froot = null;
            boolean record_exists=false;
        try {
            // check for SDcard

            String Remaining_Text="";
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File filein = null;
            File fileDir = null;
            String state = Environment.getExternalStorageState();
            //  textMsg.setText("No Server found - " + state);
            if (Environment.MEDIA_MOUNTED.equals(state)) {

                //check sdcard permission
                fileDir = new File(froot.getAbsolutePath() + "/.server/");

                filein = new File(fileDir, "serverIp.txt");

                if (filein.exists()) {
                    try {
                        FileReader fileReader = new FileReader(filein);
                        BufferedReader buf= new BufferedReader(fileReader);
                        String line="";

                        while ((line = buf.readLine()) != null){
                            if(line.equalsIgnoreCase(root) ||line.equalsIgnoreCase(LinKName) ){
                                record_exists=true;
                                break;
                            }
                        }
                        buf.close();
                        fileReader.close();

                    } catch (Exception e) {}
                    finally {

                    }


                    try {
                        froot.setWritable(true);
                        //if (froot.canWrite()) {

                        File file = new File(fileDir, "serverIp.txt");
                        FileWriter filewriter = new FileWriter(file);
                        BufferedWriter out = new BufferedWriter(filewriter);
                        out.write(Remaining_Text);

                        out.close();

                    } catch (Exception e) {

                    }

                }
            }
        } catch (Exception e) {

        }
        return  record_exists;
    }
    public void Remove_Server(String root, String LinKName) throws IOException {
        // File froot = null;

        try {
            // check for SDcard

            String Remaining_Text="";
            froot= (context.getExternalFilesDir(null));//froot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File filein = null;
            File fileDir = null;
            String state = Environment.getExternalStorageState();
            //  textMsg.setText("No Server found - " + state);
            if (Environment.MEDIA_MOUNTED.equals(state)) {

                //check sdcard permission
                fileDir = new File(froot.getAbsolutePath() + "/.server/");

                filein = new File(fileDir, "serverIp.txt");

                if (filein.exists()) {
                    try {
                        FileReader fileReader = new FileReader(filein);
                        BufferedReader buf= new BufferedReader(fileReader);
                        String line="";

                        while ((line = buf.readLine()) != null){
                          if(line.equalsIgnoreCase(root) ||line.equalsIgnoreCase(LinKName) );

                           else{
                              Remaining_Text=Remaining_Text + line + "\n";
                          }
                        }
                        buf.close();
                        fileReader.close();

                    } catch (Exception e) {}
                    finally {

                    }


                    try {
                        froot.setWritable(true);
                        //if (froot.canWrite()) {

                        File file = new File(fileDir, "serverIp.txt");
                        FileWriter filewriter = new FileWriter(file);
                        BufferedWriter out = new BufferedWriter(filewriter);
                        out.write(Remaining_Text);

                        out.close();

                    } catch (Exception e) {

                    }



                }
            }
        } catch (Exception e) {

        }

    }
    class ServerListAdpater extends BaseAdapter {
        List<Server_Link> lst_servers;
        Context context;
        String Note_Type="";
        public  ServerListAdpater(Context con,List<Server_Link> lst_svers){
            this.lst_servers=lst_svers;
            this.context=con;
        }
        @Override
        public int getCount() {
            return lst_servers.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Server_Link link;
            final TextView txtCompnayName ;
            final TextView txtLink;

            View gridView=null;


            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            if (convertView == null) {

                gridView = inflater.inflate(R.layout.server_link_row, null);
            }else{
                gridView=convertView;
            }

            if (current_index==position){
                //#E0E0E0
              //  gridView.setBackgroundColor(Color.parseColor("#70B4B4B4"));
                gridView.setBackgroundResource(R.drawable.border_grey_background);
            }else{
                gridView.setBackgroundResource(R.drawable.border);
            }


            try {


                txtCompnayName =  (TextView) gridView.findViewById(R.id.txtCompnayName);
                txtLink =  (TextView) gridView.findViewById(R.id.txtLink);
                link= lst_servers.get(position);
                txtCompnayName.setText(link.getCompanyName());
                txtLink.setText(link.getServerLink());

            }catch(Exception ex){}

            return gridView;

        }
    }


    private void showCustomDialog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.rl_help, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
       final AlertDialog alertDialog = builder.create();
        ImageView dialogButton =  dialogView.findViewById(R.id.imgCancel);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
