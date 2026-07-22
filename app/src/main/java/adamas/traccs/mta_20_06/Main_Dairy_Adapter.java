package adamas.traccs.mta_20_06;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

public class Main_Dairy_Adapter extends RecyclerView.Adapter<Main_Dairy_Adapter.MyViewHolder>  {

    private final List<Roster_Info> Roster_List;
    //private RecyclerViewClickListener mListener;
    File froot;
    String Existing_Attendees="";
    View itemView;
    Context context;
    TextView txtTime_Actual;
    final String PREFS_NAME = "MTAPrefs";
    SharedPreferences settings = null;

    public Main_Dairy_Adapter(List<Roster_Info> RosterList,Context c) {
        settings = c.getSharedPreferences(PREFS_NAME, 0);
        this.Roster_List = RosterList;
        this.context=c;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView txtTime,txtClient, txtServiceType, txtNote,txtTime_Actual;
        Button btnAlert, btnOPNote,btnAttendee,btnAttendee2,btnMap;
        int index;
        View card;
        Context context;

        public MyViewHolder(View view) {
            super(view);



            txtTime = (TextView) view.findViewById(R.id.txtTime);
            txtClient = (TextView) view.findViewById(R.id.txtClient);
            txtServiceType = (TextView) view.findViewById(R.id.txtBooking);
            txtNote = (TextView) view.findViewById(R.id.txtNote);
            txtTime_Actual = (TextView) view.findViewById(R.id.txtTime_Actual);
            txtNote = (TextView) view.findViewById(R.id.txtNote);
            
            btnAlert = (Button) view.findViewById(R.id.btnAlert);
            btnOPNote = (Button) view.findViewById(R.id.btnOPNote);
            btnAttendee = (Button) view.findViewById(R.id.btnAttendee);
            btnAttendee2 = (Button) view.findViewById(R.id.btnAttendee2);
            btnMap = (Button) view.findViewById(R.id.btnMap);
            context = view.getContext();
            card= view.findViewById(R.id.cardView);

            /*btnAlert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(v.getContext(), "Alert Clicked = " , Toast.LENGTH_SHORT).show();
                }
            });
*/

        }



    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String show_client="",show_Note="";
        String show_Service="", show_Service2="";
        int type=0;
        String show_val="", InfoOnly="0", Actual_Time="";
        String show_Group_Roster="";
        final int pos=position;
        
        Roster_Info rst = Roster_List.get(position);

         //irfan 18 Sep
         //old
          // show_val=rst.getStart_Time() + "-" + rst.get_End_Time();

        //new
             show_val=rst.getStart_Time() + "-" + rst.get_End_Time() + " -- " + rst.get_Calculated_Duration();

        //irfan 18 Sep

        if (Integer.parseInt(rst.getCompleted())>0 ) {
            Actual_Time = rst.getActual_Start_Time() + "-" + rst.getActual_End_Time();
        }
        if (Integer.parseInt(rst.getCompleted())>0 && rst.getActual_Start_Time().equalsIgnoreCase("00:00") ) {
            Actual_Time = rst.getStart_Time() + "-" + rst.get_End_Time();
        }


        type = Integer.parseInt(rst.getRoster_type());
        InfoOnly= rst.getInfoOnly();
        if (rst.getActual_Client_Code().equalsIgnoreCase("!MULTIPLE") || rst.getActual_Client_Code().equalsIgnoreCase("!INTERNAL")){
            //viewHolder.txtReceipientName.setText("Type : " + rst.getClient_code());
            show_client="<b>Type : </b>" + rst.getClient_code();

        }else if ( rst.getAddress().equalsIgnoreCase("-") ||  rst.getAddress().equalsIgnoreCase("") || settings.getString("HideAddress","0").equalsIgnoreCase("true")) {
            show_client=rst.getClient_code();
        }else{
            if (rst.getAddress().equalsIgnoreCase("") || rst.getAddress().equalsIgnoreCase("-"))
                show_client=rst.getClient_code()  ;
            else
                show_client=rst.getClient_code() + "<br>" + rst.getAddress().replace(",","<br>");
        }

        //viewHolder.txtServiceType.setText("Service Type : " + rst.getServiceType() );
        show_Service2="";
        show_Service= ("<b>Service Type : </b>" +  rst.getServiceType() );
        if (!rst.getServiceType().equalsIgnoreCase(rst.getMTAServiceType()) && !rst.getMTAServiceType().equalsIgnoreCase("")) {
            show_Service2 = "<b>MTA Service : </b>" + rst.getMTAServiceType();
        }
        if ( rst.getNotes().equalsIgnoreCase("-") ||  rst.getNotes().equalsIgnoreCase("") ){
            //	viewHolder.txtNote.setText("" );          
            //	viewHolder.txtNote.setVisibility(View.GONE);
            show_Note="";
          //  show_Note="<b>Notes : </b>" + "(No Notes)";
           holder.txtNote.setVisibility(View.GONE);
        }else{
            //viewHolder.txtNote.setText("Notes : " + rst.getNotes());    
            show_Note= rst.getNotes();
            holder.txtNote.setVisibility(View.VISIBLE);
        }

        
        int color=0;
        if (type==9)
        {
           // show_val= show_val + " (Info Only)";
           // show_Service= ("Activity : " + rst.getServiceType()  + "<br><br>Qty : " +rst.getKM() );
            show_Service= "Qty : " +rst.getKM() ;
            show_Service2="";
            color=3;

        } else if ((type==13 || type==14 || type==15) && (InfoOnly.equals("1") || InfoOnly.equalsIgnoreCase("true")))
        {
            show_val= show_val + "(Info Only)";
            color=3;

        } else if (Integer.parseInt(rst.getCompleted())>0 && (!rst.getCarer_code().trim().equals("BOOKED"))){
            //show_val =show_val + "(Completed)";

            color=1;

        } else if (Integer.parseInt(rst.getStarted())>0 && (!rst.getCarer_code().trim().equals("BOOKED")) ){
            // holder.txtServiceType.setText(show_val + "     (Started)");
            // holder.txtServiceType.setTextColor(Color.GREEN);
            //show_val= show_val + " (Started)";
            color=2;
        }else if (!rst.getCarer_code().trim().equals("BOOKED")){
            // holder.txtServiceType.setText(show_val + "     (Not Started)");
            // holder.txtServiceType.setTextColor(Color.BLACK);
            show_val =show_val + "";
            color=0;
        }

        String Group_alerts="";
        holder.btnAlert.setVisibility(View.GONE);
        if (!rst.getGroup_Alerts().equalsIgnoreCase("-"))
        {
            holder.btnAlert.setVisibility(View.VISIBLE);
            Group_alerts = "";// + rst.getGroup_Alerts() + "";
        }
        String Show_Debtor_Fee = "";
        if (rst.getDisplayDebtorInApp().equalsIgnoreCase("true"))
            Show_Debtor_Fee = " Debtor: " + rst.getDebtor() + " (" + rst.get_ACCOUNTINGIDENTIFIER() + ")" + "";

        if (rst.getDisplayFeeInApp().equalsIgnoreCase("true"))
            Show_Debtor_Fee = Show_Debtor_Fee + "Fee: " + rst.getFee() + "";
        show_Group_Roster="";
        if (rst.getActual_Client_Code().equalsIgnoreCase("!MULTIPLE")){
            show_Group_Roster = "MULTIPLE ATTENDEES";
            holder.btnAttendee.setVisibility(View.VISIBLE);
            holder.btnOPNote.setVisibility(View.GONE);
            holder.btnAttendee2.setVisibility(View.GONE);
        }else{
            holder.btnAttendee.setVisibility(View.GONE);
            holder.btnOPNote.setVisibility(View.VISIBLE);
            holder.btnAttendee2.setVisibility(View.GONE);
        }
        if (rst.getActual_Client_Code().equalsIgnoreCase("!INTERNAL")){
            holder.btnOPNote.setVisibility(View.GONE);
        }
        if (rst.getActual_Client_Code().equalsIgnoreCase("!INTERNAL") || rst.getActual_Client_Code().equalsIgnoreCase("!MULTIPLE")
        || type==13 || type==14 || type==15 || rst.getAddress().equalsIgnoreCase("") ) {
            holder.btnMap.setVisibility(View.GONE);
        }else{

            if (settings.getString("HideAddress","0").equalsIgnoreCase("true")) {
                holder.btnMap.setVisibility(View.GONE);
            }else{
                holder.btnMap.setVisibility(View.VISIBLE);
            }
        }


        holder.index=position;
        holder.txtTime.setText(show_val);
        holder.txtTime_Actual.setText(Actual_Time);
        holder.txtClient.setText( HtmlCompat.fromHtml(show_client,0));

        holder.txtServiceType.setText(HtmlCompat.fromHtml(show_Service +
                        (show_Service2.equals("")? "" : "<br><br>" + show_Service2) +
                        (Show_Debtor_Fee.equals("")? "":Show_Debtor_Fee) +
                        (Group_alerts.equals("")?"":Group_alerts) +
                        (show_Group_Roster.equals("")?"": show_Group_Roster),0)
                );

            holder.txtNote.setText( HtmlCompat.fromHtml(show_Note,0));

//&& rst.getIndex()==position
        if (Long.parseLong(rst.getCompleted())>=1) {
            holder.card.setBackgroundColor(Color.parseColor("#F7EFEA"));
        }else if (rst.getStarted().equals("1") ){
            holder.card.setBackgroundColor(Color.parseColor("#EBF7EA"));

        }else{
            holder.card.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        if (type==9 ) {
            holder.btnAlert.setVisibility(View.GONE);
            holder.btnOPNote.setVisibility(View.GONE);
            holder.btnAttendee.setVisibility(View.GONE);
            holder.btnMap.setVisibility(View.GONE);
        }

        holder.txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Roster_Info r= Roster_List.get(pos);
                Shift_Detail2(v.getContext(), r);
            }
        });
        holder.txtClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Roster_Info r= Roster_List.get(pos);
                Shift_Detail2(v.getContext(), r);
            }
        });
        holder.txtServiceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Roster_Info r= Roster_List.get(pos);
                Shift_Detail2(v.getContext(), r);
              //  v.setEnabled(false);

            }
        });
        holder.txtNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Roster_Info r= Roster_List.get(pos);
                Shift_Detail2(v.getContext(), r);
                //v.setEnabled(false);

            }
        });
        holder.btnAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Roster_Info r= Roster_List.get(pos);
                Intent alrt = new Intent(v.getContext(), Group_Alert_Activity.class);
                Bundle b= new Bundle();
                b.putString("Alerts",r.getRunsheetAlerts() + "\n" + r.getGroup_Alerts());
                b.putString("PersonId",r.getPersonId());

                b.putBoolean("load_group_alerts",false);


                alrt.putExtras(b);
               context.startActivity(alrt);
                //v.setEnabled(false);

            }
        });
        holder.btnOPNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Roster_Info r= Roster_List.get(pos);
                show_OP_Case_Note(v.getContext(),r);
                //v.setEnabled(false);
              /*  Intent op = new Intent(v.getContext(), OP_Case_Note_Activity.class);
                v.getContext().startActivity(op);*/
              //  Toast.makeText(v.getContext(), "Op Note Clicked = " + pos , Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnAttendee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PopupMenu pm = new PopupMenu(context, v);
                pm.getMenuInflater().inflate(R.menu.pm_accounts_item, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Roster_Info r;
                        //Toast.makeText(getApplicationContext(), String.valueOf(item.getTitle()), Toast.LENGTH_SHORT).show();
                        switch (item.getItemId()) {

                            case R.id.mnu_tiemwise:
                                 r = Roster_List.get(pos);
                                show_Group_Recipients(v.getContext(), r, "timewise");
                                break;

                            case R.id.mnu_daywise:
                                 r = Roster_List.get(pos);
                                show_Group_Recipients(v.getContext(), r, "datewise");
                                break;

                            case R.id.mnu_cancel:
                                break;

                            default:
                                break;
                        }
                        return true;
                    }
                });
                pm.show();


            }
        });
        holder.btnAttendee2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Roster_Info r= Roster_List.get(pos);
                show_Group_Recipients(v.getContext(),r,"timewise");
                //v.setEnabled(false);
                // Toast.makeText(v.getContext(), "Attendee Clicked = " + pos , Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Roster_Info r= Roster_List.get(pos);
                show_Map(v.getContext(),r.getAddress());
                //v.setEnabled(false);
                // Toast.makeText(v.getContext(), "Attendee Clicked = " + pos , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return Roster_List.size();
    }

    public void Shift_Detail2(Context c, Roster_Info rst){

    try{

            //Intent intent = new Intent(this, Shift_Detail.class);
            Intent intent = new Intent(c, Shift_Detail.class);
            Bundle bundle = getShift_Bundle(rst);
            intent.putExtras(bundle);

        context.startActivity(intent);
            //startActivityForResult(intent, STATIC_INTEGER_VALUE);

        }catch(Exception ex){}


    }
    public Bundle getShift_Bundle( Roster_Info Current_roster){

        String Shift_Status="";

        Bundle bundle= new Bundle();

        try {
            if (Current_roster==null){

                return null;
            }

            bundle.putString("RecordNo", Current_roster.getRecordNo());
            bundle.putString("RosterDate", Current_roster.getRoster_Date());
            bundle.putString("StartTime", Current_roster.getStart_Time());
            bundle.putString("EndTime", Current_roster.get_End_Time());
            bundle.putString("Actual_Client_Code",Current_roster.getActual_Client_Code());
            bundle.putString("AccountNo",Current_roster.getClient_code());
            bundle.putString("Roster_type",Current_roster.getRoster_type());
            bundle.putString("Program",Current_roster.getProgram());
            bundle.putString("ServiceType",Current_roster.getServiceType());
            bundle.putString("TMMode",Current_roster.getTA_LOGINMODE());


            bundle.putString("acceptedbyStaff",Current_roster.getAcceptedbyStaff());
            bundle.putString("NewAcceptDate",Current_roster.getNewAcceptDate());



            bundle.putString("Time_String", Current_roster.getStart_Time() + "," +  Current_roster.get_Calculated_Duration());

            bundle.putString("MinorGroup",Current_roster.getMinorGroup());

            if (Current_roster.getMTAServiceType().equalsIgnoreCase(""))
                bundle.putString("MTAServiceType",Current_roster.getServiceType());
            else
                bundle.putString("MTAServiceType",Current_roster.getMTAServiceType());
            try {
                bundle.putString("MyOwnCarStatus", Current_roster.getMyOwnCarStatus());
            }catch (Exception ex){}

            try {
                bundle.putString("Service_Setting", Current_roster.getServiceSetting());
            }catch (Exception ex){}

            try {
                bundle.putString("RunsheetAlerts", Current_roster.getRunsheetAlerts());
            }catch (Exception ex){}
            try {
                bundle.putString("Address", Current_roster.getAddress());
            }catch (Exception ex){}

            try {
                bundle.putString("notes", Current_roster.getNotes());
            }catch (Exception ex){}

            try {
                bundle.putString("ExcludeFromAppLogging", Current_roster.getExcludeFromAppLogging());
            }catch (Exception ex){}

            try {
                bundle.putString("ForceShiftReport", Current_roster.getForceShiftReport());
            }catch (Exception ex){}
            try {

                settings.edit().putBoolean("HasServiceNotes",Boolean.parseBoolean(Current_roster.getHasServiceNotes())).commit();

            }catch (Exception ex){}
            boolean View_only=false;
            int type=Integer.parseInt(Current_roster.getRoster_type());
            String InfoOnly=Current_roster.getInfoOnly();

//            if ( type==9 || type==13 || type==14 ||type==15 || (InfoOnly.equals("1") || InfoOnly.equalsIgnoreCase("true"))) {
//                View_only=true;
//            }

            if (type==1 || type==9 || type==13 || type==14 ||type==15) {
                View_only=true;
            }
            //if ((type==13 || type==14 || type==15) && (InfoOnly.equals("1") || InfoOnly.equalsIgnoreCase("true"))) {


            try {

                bundle.putBoolean("View_only", View_only);
            }catch (Exception ex){}

            try {
               // bundle.putString("Started_Job_No", "0");
                if (Long.parseLong(Current_roster.getCompleted())>=1)
                    Shift_Status="Completed";
                else if (Current_roster.getStarted().equalsIgnoreCase("1")) {
                    Shift_Status = "Started";
                   // settings.edit().putString("Started_Job_No", Current_roster.getRecordNo()).commit();
                   // bundle.putString("Started_Job_No", Current_roster.getRecordNo());

                } else
                    Shift_Status="Not Started";

                bundle.putString("Shift_Status", Shift_Status);

            }catch (Exception ex){}


        }catch(Exception ex){}
    //    bundle.putBoolean("View_only", false);

            /*bundle.putBoolean("Process_Sleep_Over",Process_Sleep_Over);

            bundle.putBoolean("View_only",View_only);

            bundle.putString("RECIPIENT_COORDINATOR",RECIPIENT_COORDINATOR);
            bundle.putString("Staff_Coordinator_Email",Staff_Coordinator_Email);

        */

        return bundle;

    }
    private void show_OP_Case_Note( Context context,Roster_Info rst){
        try{
            Bundle bundle= new Bundle();

            bundle.putString("RecordNo",rst.getRecordNo());
            bundle.putString("Roster_Date",rst.getRoster_Date() + " " + rst.getStart_Time() + "-" + rst.get_End_Time());
            bundle.putString("Recipient",rst.getClient_code());
            bundle.putString("AccountNo",rst.getActual_Client_Code());
            bundle.putString("PersonId",rst.getPersonId());
            bundle.putString("Job_Time",rst.getStart_Time() + "-" + rst.get_End_Time());

           //OP_Case_Note_Activity.Refresh_OP_Note_data_single=false;
            Intent intent2= new Intent(context,OP_Case_Note_Activity.class);
            intent2.putExtras(bundle);
            context.startActivity(intent2);
            // Toast.makeText(v.getContext(),  "No Issue", Toast.LENGTH_LONG).show();
        }catch(Exception ex){Toast.makeText(context,  ex.toString() , Toast.LENGTH_LONG).show();}
    }


    void show_Group_Recipients(Context context,  Roster_Info rst, String mode){
        try{
            Bundle bundle= new Bundle();

            bundle.putString("RecordNo",rst.getRecordNo());
            bundle.putString("Roster_Date",rst.getRoster_Date() + " " + rst.getStart_Time() + "-" + rst.get_End_Time());
            bundle.putString("Recipient",rst.getClient_code());
            bundle.putString("AccountNo",rst.getActual_Client_Code());
            bundle.putString("PersonId",rst.getPersonId());
            bundle.putString("mode",mode);

           // bundle.putStringArray("attendees", (ArrayList<? extends Parcelable>) rst.get_group_Recipients());

            Intent intent2= new Intent(context,Group_Recipient_Activity.class);
            intent2.putExtras(bundle);
            context.startActivity(intent2);
            // Toast.makeText(v.getContext(),  "No Issue", Toast.LENGTH_LONG).show();
        }catch(Exception ex){Toast.makeText(context,  ex.toString() , Toast.LENGTH_LONG).show();}
    }

    void show_Map(Context context,String Simple_Address) {
        try {
           // if (!Server_Available) return;

            String lat_long = "";
            String lat_long_current = "";


            String link = "http://maps.google.com/maps?f=q&hl=en&geocode=&time=&date=&ttype=&saddr=Your Location&daddr=" + Simple_Address;
            String uri = String.format(Locale.ENGLISH, link);
            Intent intent2 = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse(uri));
            intent2.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

            context.startActivity(intent2);


        } catch (Exception ex) {
            // ((TextView) findViewById(R.id.txtAcknowledge)).setText("Operation not done due to some server error\n" + ex.toString());
        }
    }
}//End of Adapter Class