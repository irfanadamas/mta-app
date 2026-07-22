package adamas.traccs.mta_20_06;


import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;

public class OnBootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent("adamas.traccs.mta_20_06.FirebaseMessagingService");
        i.setClass(context, FirebaseMessagingService.class);
        context.startService(i);
    }
}