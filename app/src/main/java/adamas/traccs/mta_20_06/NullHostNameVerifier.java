package adamas.traccs.mta_20_06;

import android.os.Build;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created by arshad on 21/09/2017.
 */

public class NullHostNameVerifier implements HostnameVerifier {
    public boolean verify(String hostname, SSLSession session) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE_1_1;
    }
}