package adamas.traccs.mta_20_06;

    import android.content.Context;
        import android.os.AsyncTask;
        import android.os.Environment;
        import android.os.Handler;
        import android.util.Log;
        import android.widget.Button;
        import android.widget.Toast;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.InputStream;
        import java.net.HttpURLConnection;
        import java.net.URL;



/**
 * Created by SONU on 29/10/15.
 */
public class DownloadTask {

    private static final String TAG = "Download Task";
    private final Context context;

    private String downloadUrl = "", downloadFileName = "";
    private String output_path="";
    public DownloadTask(Context context,  String downloadUrl, String FileName) {
        this.context = context;

        this.downloadUrl = downloadUrl;

        downloadFileName = FileName;//downloadUrl.replace(Utils.mainUrl, "");//Create file name by picking download file name from URL
      //  Log.e(TAG, downloadFileName);

        //Start Downloading Task
        new DownloadingTask().execute();
    }

    public String getOutput_Path(){
        return output_path;
    }
    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    //If Download completed then change button text
                } else {
                   /* buttonText.setText(R.string.downloadFailed);//If download failed change button text
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            buttonText.setEnabled(true);
                            buttonText.setText(R.string.downloadAgain);//Change button text again after 3sec
                        }
                    }, 3000);

                    Log.e(TAG, "Download Failed");
*/
                }
            } catch (Exception e) {
                e.printStackTrace();


            }


            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUrl);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());

                }


                    //apkStorage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/mta");
                apkStorage = new File(context.getExternalFilesDir(null).getAbsolutePath() + "/mta");

                if (!apkStorage.exists())
                    apkStorage.mkdirs();

                outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                //Create New File if not present
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                    Log.e(TAG, "File Created");
                }
                output_path=outputFile.getAbsolutePath();
                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                InputStream is = c.getInputStream();//Get InputStream for connection

                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);//Write new file
                }

                //Close all connection after doing task
                fos.close();
                is.close();

            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + e.getMessage());
            }

            return null;
        }
    }
}