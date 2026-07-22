package adamas.traccs.mta_20_06;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class Waiter extends Thread {
    private static final String TAG=Waiter.class.getName();
    private long lastUsed;
    private long period;
    private boolean stop;
    private String root;
    private String user;
    private  WeakReference<Context> mContextRef;

    public Waiter(final long period, final Context context, String root, String user) {
        this.period = period;
        stop = false;
        this.root=root;
        this.user=user;
        mContextRef = new WeakReference<Context>(context);
    }

    public void setContext(Context context){
        this.mContextRef=new WeakReference<Context>(context);;
    }
    public void run() {
        long idle = 0;
        this.touch();

        do {
            idle = System.currentTimeMillis()-lastUsed;
            Log.d(TAG, "Application is idle for " + idle + " ms");
            try {
                Thread.sleep(5000); //check every 5 seconds

                if(idle > period) {
                    final Context context = mContextRef.get();

                    if (context != null) {
                        // start activity
                        String urlString = root + "/index.aspx?logout=1&user=" + this.user;
                        String data = "";

                        try {
                            Volly_Post_Request(urlString);
                        } catch (Exception ex) {
                            //Tost_Message(ex.toString());
                        }
                       // new MyAsyncClass_RemoveSession().execute(this.user,"");
                        startActivity(context);
                    }

                    stop = true;
                }
            }
            catch (InterruptedException e) {
                Log.d(TAG, "Waiter interrupted!");
                // set stop, because smb has called interrupt() on the thread
                stop = true;
            }
        }
        while(!stop);
        Log.d(TAG, "Finishing Waiter thread");
    }

    private void startActivity(final Context context) {
        final Intent intent = new Intent(context, Login.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // If there is nothing that can send a text/html MIME type
            e.printStackTrace();
        }
    }

    public synchronized void touch() {
        lastUsed=System.currentTimeMillis();
    }

    public synchronized void setPeriod(long period) {
        this.period=period;
    }

    void Volly_Post_Request(String url_String){

        try {


            new Thread(() -> {
                try {
                    URL url = new URL(url_String);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        // Print result
                        System.out.println(response.toString());
                    } else {
                        System.out.println("GET request failed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        }catch(Exception ex){ System.out.println(ex.toString());}
    }

}