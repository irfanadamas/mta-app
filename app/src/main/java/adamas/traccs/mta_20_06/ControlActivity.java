package adamas.traccs.mta_20_06;

import android.app.Activity;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class ControlActivity extends AppCompatActivity
{
    private static final String TAG=ControlActivity.class.getName();

    /**
     * Gets reference to global Application
     * @return must always be type of ControlApplication! See AndroidManifest.xml
     */
    public ControlApplication getApp()
    {
        return (ControlApplication )this.getApplication();
    }

    @Override
    public void onUserInteraction()
    {
        super.onUserInteraction();
        getApp().touch();
        Log.d(TAG, "User interaction to "+this.toString());
    }

}