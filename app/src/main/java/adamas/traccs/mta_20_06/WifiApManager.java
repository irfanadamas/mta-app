package adamas.traccs.mta_20_06;


import java.lang.reflect.Method;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

public final class WifiApManager {
      private static final int WIFI_AP_STATE_FAILED = 4;
      private final WifiManager mWifiManager;
      private final String TAG = "Wifi Access Manager";
      private final Method wifiControlMethod;
      private final Method wifiApConfigurationMethod;
      private final Method wifiApState;

      public WifiApManager(Context context) throws SecurityException, NoSuchMethodException {
      // context =  ; //Preconditions.checkNotNull(context);

              mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
              wifiControlMethod = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
              wifiApConfigurationMethod = (Method) mWifiManager.getClass().getMethod("getWifiApConfiguration", this.getClass());

       wifiApState = mWifiManager.getClass().getMethod("getWifiApState");

      }   
      public boolean setWifiApState(WifiConfiguration config, boolean enabled) {
     //  config = Preconditions.checkNotNull(config);
       try {
        if (enabled) {
            mWifiManager.setWifiEnabled(!enabled);
        }
        return (Boolean) wifiControlMethod.invoke(mWifiManager, config, enabled);
       } catch (Exception e) {
        Log.e(TAG, "", e);
        return false;
       }
      }
      public WifiConfiguration getWifiApConfiguration()
      {
          Object[] arg ={"Wifi"};
          try{
              return (WifiConfiguration)wifiApConfigurationMethod.invoke(mWifiManager, arg);
          }
          catch(Exception e)
          {
              return null;
          }
      }
      public int getWifiApState() {
       try {
            return (Integer)wifiApState.invoke(mWifiManager);
       } catch (Exception e) {
        Log.e(TAG, "", e);
            return WIFI_AP_STATE_FAILED;
       }
      }
}