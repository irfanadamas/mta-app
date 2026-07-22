package adamas.traccs.mta_20_06;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;


import org.kobjects.base64.Base64;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpLoadService extends AsyncTask<Void, Void, String> {
    private File file;
    private String serverUrl;
    private static final String BOUNDARY = "----WebKitFormBoundary7MA4YWxkTrZu0gW";
    private static final String LINE_END = "\r\n";
    private static final String TWO_HYPHENS = "--";
    public UpLoadService(File file, String serverUrl) {
        this.file = file;
        this.serverUrl = serverUrl;
    }
//    private RequestQueue mRequestQueue;
//    private static UpLoadService apiRequests = null;
//
//    public static UpLoadService getInstance() {
//        if (apiRequests == null) {
//            apiRequests = new UpLoadService();
//            return apiRequests;
//        }
//        return apiRequests;
//    }
    public void uploadFile(Context context,String url, String doc_name, String AccountNo, String RecipientDocFolder, File file) {

//        SimpleMultiPartRequest request = new Request<Task> (Request.Method.POST, url, listener, errorListener);
////        request.setParams(data);
//        mRequestQueue = RequestManager.getnstance(context);
//        request.addFile("buff", file.getPath());
//        request.addMultipartParam("fileName", "text", doc_name);
//        request.addMultipartParam("RecipientDocFolder", "text", RecipientDocFolder);
//        request.addMultipartParam("AccountNo", "text", AccountNo);
//
//        request.setFixedStreamingMode(true);
//        mRequestQueue.add(request);
    }
    @Override
    protected String doInBackground(Void... voids) {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        FileInputStream fileInputStream = null;

        try {
            URL url = new URL(serverUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            outputStream = new DataOutputStream(connection.getOutputStream());

            // Write text parameters
            writeFormField(outputStream, "param1", "value1");

            // Write file part
            writeFilePart(outputStream, "file", file);

            // End of multipart/form-data.
            outputStream.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + LINE_END);
            outputStream.flush();

            // Get response
            int responseCode = connection.getResponseCode();
            return "Server Response Code: " + responseCode;

        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } finally {
            try {
                if (fileInputStream != null) fileInputStream.close();
                if (outputStream != null) outputStream.close();
                if (connection != null) connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeFormField(DataOutputStream outputStream, String name, String value) throws IOException {
        outputStream.writeBytes(TWO_HYPHENS + BOUNDARY + LINE_END);
        outputStream.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + LINE_END);
        outputStream.writeBytes(LINE_END);
        outputStream.writeBytes(value + LINE_END);
    }

    private void writeFilePart(DataOutputStream outputStream, String fieldName, File file) throws IOException {
        outputStream.writeBytes(TWO_HYPHENS + BOUNDARY + LINE_END);
        outputStream.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + file.getName() + "\"" + LINE_END);
        outputStream.writeBytes("Content-Type: application/octet-stream" + LINE_END);
        outputStream.writeBytes(LINE_END);

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.writeBytes(LINE_END);
    }
}

