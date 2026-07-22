package adamas.traccs.mta_20_06;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

//import static com.google.android.gms.wearable.DataMap.TAG;

public class SimpleScannerActivity extends Activity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause}
    }
    @Override
    public void handleResult (Result rawResult){
        // Do something with the result here
        Log.v("QRCode", rawResult.getText()); // Prints scan results
       // Toast.makeText(this, rawResult.getText(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
        //Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        String displayContents=rawResult.getText();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("barcode", displayContents);
        setResult(RESULT_OK, resultIntent);
        this.finish();
        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }



/*
    // Toggle flash:
    void setFlash(boolean);

    // Toogle autofocus:
    void setAutoFocus(boolean);

    // Specify interested barcode formats:
    void setFormats(List<BarcodeFormat> formats);

    // Specify the cameraId to start with:
    void startCamera(int cameraId);

*/
}