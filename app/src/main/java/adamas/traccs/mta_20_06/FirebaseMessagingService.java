package adamas.traccs.mta_20_06;


import android.app.ActivityManager;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.RemoteInput;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.core.app.NotificationCompat;
import android.app.PendingIntent;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.Toast;


import com.google.firebase.messaging.RemoteMessage;


import java.util.List;

import androidx.core.app.NotificationCompat;
import timesheet.NetworkStateReceiver;

/**
 * Created by User on 2/20/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService
implements  NetworkStateReceiver.NetworkStateReceiverListener {
    private static final String TAG = "FirebaseMessagingService";
    boolean Server_Available=true;
    boolean AllowViewBookings=false;
    final String PREFS_NAME = "MTAPrefs";
     String root = "MTAPrefs";
     String StaffCode="";

    String rosterDate="";
    String Security_Token="";
    String OperatorId="";
    SharedPreferences settings=null;
    String values="";
    Boolean Shift_Change=false;
    Context context;
    public FirebaseMessagingService() {
      // this.context=getApplicationContext();
      //  settings = context.getSharedPreferences(PREFS_NAME, 0);
    }
    public FirebaseMessagingService(Context cntx) {

        this.context = cntx;
        settings = context.getSharedPreferences(PREFS_NAME, 0);

    }
   
    void get_Server_Settings(){

        try {
            if (this.context==null)
                this.context = getApplicationContext();
            settings = context.getSharedPreferences(PREFS_NAME, 0);
           
            root= settings.getString("root", root);
            StaffCode= settings.getString("StaffCode", StaffCode);
            OperatorId=settings.getString("OperatorId",OperatorId);
            Security_Token=settings.getString("Security_Token",Security_Token);
            Server_Available= settings.getBoolean("Server_Available",Server_Available  );
            rosterDate= settings.getString("rosterDate", rosterDate);

            values = root + ", " + StaffCode + "," + OperatorId + ", " + Security_Token + ", " + rosterDate;


              //Toast.makeText(context, root +"\n" +  StaffCode, Toast.LENGTH_SHORT).show();


        }catch(Exception ex){
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();}
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        get_Server_Settings();

        String title = remoteMessage.getNotification().getTag() + " : " + remoteMessage.getNotification().getTitle();
        String message =  remoteMessage.getNotification().getTag() +":"  + remoteMessage.getNotification().getBody() ;
        Log.d(TAG, remoteMessage.getNotification().getTag() +": onMessageReceived: Message Received: \n" +
                "Title: " + title + "\n" +
                "Message: " + message );




        if (remoteMessage.getNotification().getTag().contains("Shift Change") &&
                Server_Available == true ) {
            Shift_Change=true;
            sendNotification_Shift(title,message);

            try {
               // RunBakckgrounThread();
             //   Bulk_Data bulk= new Bulk_Data(root,StaffCode,OperatorId,Security_Token,Server_Available,rosterDate);
               // bulk.get_Active_Device_Reminder();
            } catch (Exception ex) {
            }


            try {
                if (AllowViewBookings) {
                    //    new MainActivity_Navigation.MyAsyncClass_booking().execute();
                }} catch (Exception ex) {
            }

        }else {
            sendNotification(title,message);
        }


    }

    @Override
    public void onDeletedMessages() {
        /*if (Shift_Change==true &&  Server_Available == true ) {
        try {
            Shift_Change=false;
            Intent intent;

            if (isRunning(context)) {
                intent = new Intent(this, MainActivity_Navigation.class);
            }else{
                intent = new Intent(this, Login.class);
            }
            startActivity (intent);
            RunBakckgrounThread();


        } catch (Exception ex) {
        }
    }*/
    }

    private void sendNotification(String title,String messageBody) {

        final int NOTIFICATION_ID = 30002;
       Intent intent;
      //  intent = new Intent(this, MainActivity_Navigation.class);
        if (isRunning(context)) {

            intent =  new Intent(context, MainActivity.class);
        }else{
            intent = new Intent(context, Login.class);
        }

        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
       // Toast.makeText(context, cn.getShortClassName(), Toast.LENGTH_SHORT).show();
        if (cn.getClassName().contains("MainActivity_Navigation") )
            intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        if (cn.getClassName().contains("Login")) {
            intent = new Intent(context, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        } else
            intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);


      /*  // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
// Get the PendingIntent containing the entire back stack
        PendingIntent pendingIntent =
               stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
*/
        String channelId="MTA_20_06";
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,channelId)
                .setSmallIcon(R.drawable.ic_set_time2)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);




        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID /* ID of notification */, notificationBuilder.build());
    }
    private void sendNotification_Shift(String title,String messageBody) {

        final int NOTIFICATION_ID = 30002;
        Intent intent = new Intent(context, Reminders_Activity.class);

        try{

            Bundle bundle = new Bundle();
            bundle.putString("root",root);
            bundle.putString("UserId",OperatorId);
            bundle.putString("Security_Token",Security_Token);
            bundle.putString("OperatorId",OperatorId);
            bundle.putString("StaffCode",StaffCode);
            bundle.putBoolean("Server_Available",Server_Available);
            bundle.putString("rosterDate",rosterDate);

            intent.putExtras(bundle);
           // startActivity(intent);
            // timer2.cancel();
        } catch(Exception ex){}

        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,  PendingIntent.FLAG_ONE_SHOT);
/*
// Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
// Get the PendingIntent containing the entire back stack
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
*/
        String channelId="MTA_20_06";

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,channelId)
                .setSmallIcon(R.drawable.ic_alarm_add)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);




        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID /* ID of notification */, notificationBuilder.build());
    }

    NetworkStateReceiver networkStateReceiver;
    @Override
    public void networkAvailable() {

        Server_Available = true;
        /* TODO: Your connection-oriented stuff here */
    }

    @Override
    public void networkUnavailable() {
        Server_Available = false;

    }

    public static boolean isRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }
        return false;
    }
    private void RunBakckgrounThread() {
        final ProgressDialog pDialog = new ProgressDialog(context);
        // Database operations should not be done on the main thread
        AsyncTask<Void, Void, Void> runBakckgrounThread = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();


                pDialog.setMessage("Please wait while processing  ....");
                pDialog.show();

            }
            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    Bulk_Data bulk= new Bulk_Data(root,StaffCode,OperatorId,Security_Token,Server_Available,rosterDate,context);

                    bulk.get_Active_Device_Reminder();
                    Thread.sleep(1000);
                } catch (Exception ex) {
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                pDialog.cancel();



            }
        };

        runBakckgrounThread.execute();
    }

}
