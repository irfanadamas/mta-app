package adamas.traccs.mta_20_06;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

public class ControlApplication extends Application
{
    private static final String TAG=ControlApplication.class.getName();
    private Waiter waiter;  //Thread which controls idle time
    final String PREFS_NAME = "MTAPrefs";
    SharedPreferences settings = null;
    String root="";
    String User="";
    String UserSessionLimit="5";
    // only lazy initializations here!
    @Override
    public void onCreate()
    {
        super.onCreate();
        settings = getSharedPreferences(PREFS_NAME, 0);
        root= settings.getString("root","");
        User= settings.getString("User","");
        UserSessionLimit=settings.getString("UserSessionLimit",UserSessionLimit);
        Log.d(TAG, "Starting application"+this.toString());
        waiter=new Waiter(Long.parseLong(UserSessionLimit)*60*1000,getApplicationContext(),root,User); //15 mins
        waiter.start();
    }

    public void touch()
    {
        waiter.touch();
    }
}