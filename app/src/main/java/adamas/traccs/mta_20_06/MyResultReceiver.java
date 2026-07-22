package adamas.traccs.mta_20_06;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;


/**
 * Created by arshad on 11/04/2018.
 */


public class MyResultReceiver extends ResultReceiver {

    int RESULT_CODE=1000;

public MyResultReceiver(Handler handler) {
        super(handler);
    }

    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if(resultCode==RESULT_CODE){
            String message=resultData.getString("progress");
           // textView.setText(message+"%");
            //progressbar.setProgress(Integer.parseInt(message));
     }
    }
}

