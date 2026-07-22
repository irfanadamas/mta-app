package adamas.traccs.mta_20_06;

import android.app.Application;
import android.content.Context;

/**
 * This custom class is used to Application level things.
 *
 * @author Chintan Rathod (http://www.chintanrathod.com)
 */

public class YourApplication extends Application {
 
    private static Context mContext;
     
    public static YourApplication instace;
 
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        instace = this;
    }
     
    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }
 
    public static YourApplication getInstance() {
        return instace;
    }
}

