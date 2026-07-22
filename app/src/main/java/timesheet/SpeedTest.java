package timesheet;

/*
This file is part of SpeedTest.

SpeedTest is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

SpeedTest is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with SpeedTest.  If not, see <http://www.gnu.org/licenses/>.

*/


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import adamas.traccs.mta_20_06.*;


/**
 * Test speed of our network connection
 * @author Greg Bugaj http://www.gregbugaj.com
 * @version 1.0
 *
 */
public class SpeedTest {
	private String root_address="";

	public SpeedTest(String root){
		this.root_address=root;
	}
	public  void bindListeners() {


		new Thread(mWorker).start();

	}

	public  SpeedInfo speedInfo;

	/**
	 * Our Slave worker that does actually all the work
	 */
	private final Runnable mWorker=new Runnable(){

		@Override
		public void run() {
			InputStream stream=null;
			try {
				int bytesIn=0;
				String downloadFileUrl=root_address +"/index.aspx";
				long startCon=System.currentTimeMillis();
				URL url=new URL(downloadFileUrl);
				URLConnection con=url.openConnection();
				con.setUseCaches(false);
				long connectionLatency=System.currentTimeMillis()- startCon;
				stream=con.getInputStream();


				long start=System.currentTimeMillis();
				int currentByte=0;
				long updateStart=System.currentTimeMillis();
				long updateDelta=0;
				int  bytesInThreshold=0;

				while((currentByte=stream.read())!=-1){
					bytesIn++;
					bytesInThreshold++;
					if(updateDelta>=UPDATE_THRESHOLD){
						int progress=(int)((bytesIn/(double)EXPECTED_SIZE_IN_BYTES)*100);

						updateStart=System.currentTimeMillis();
						bytesInThreshold=0;
					}
					updateDelta = System.currentTimeMillis()- updateStart;
				}

				long downloadTime=(System.currentTimeMillis()-start);
				//Prevent AritchmeticException
				if(downloadTime==0){
					downloadTime=1;
				}
				speedInfo=calculate(updateDelta, bytesInThreshold);

			}
			catch (MalformedURLException e) {
				Log.e(TAG, e.getMessage());
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}finally{
				try {
					if(stream!=null){
						stream.close();
					}
				} catch (IOException e) {
					//Suppressed
				}
			}

		}
	};

	/**
	 * Get Network type from download rate
	 * @return 0 for Edge and 1 for 3G
	 */
	private  int networkType(final double kbps){
		int type=1;//3G
		//Check if its EDGE
		if(kbps<EDGE_THRESHOLD){
			type=0;
		}
		return type;
	}

	/**
	 *
	 * 1 byte = 0.0078125 kilobits
	 * 1 kilobits = 0.0009765625 megabit
	 *
	 * @param downloadTime in miliseconds
	 * @param bytesIn number of bytes downloaded
	 * @return SpeedInfo containing current speed
	 */
	private  SpeedInfo calculate(final long downloadTime, final long bytesIn){
		SpeedInfo info=new SpeedInfo();
		//from mil to sec
		double bytespersecond   = (double)(bytesIn / downloadTime) * 1000;
		double kilobits=bytespersecond * BYTE_TO_KILOBIT;
		double megabits=kilobits  * KILOBIT_TO_MEGABIT;
		info.downspeed=bytespersecond;
		info.kilobits=kilobits;
		info.megabits=megabits;

		return info;
	}

	/**
	 * Transfer Object
	 * @author devil
	 *
	 */



//Private fields
	private static final String TAG = SpeedTest.class.getSimpleName();
	private static final int EXPECTED_SIZE_IN_BYTES = 1048576;//1MB 1024*1024

	private static final double EDGE_THRESHOLD = 176.0;
	private static final double BYTE_TO_KILOBIT = 0.0078125;
	private static final double KILOBIT_TO_MEGABIT = 0.0009765625;




	private final static int UPDATE_THRESHOLD=300;


	public void check_Internet_Capacity(Context context){
		/*NetworkInfo info = adamas.traccs.mta.Connectivity.getNetworkInfo(this);
		if(info.getType() == ConnectivityManager.TYPE_WIFI){
			// do something
		} else if(info.getType() == ConnectivityManager.TYPE_MOBILE){
			// check NetworkInfo subtype
			if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS){
				// Bandwidth between 100 kbps and below
			} else if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE){
				// Bandwidth between 50-100 kbps
			} else if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_0){
				// Bandwidth between 400-1000 kbps
			} else if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_A){
				// Bandwidth between 600-1400 kbps
			}

			// Other list of various subtypes you can check for and their bandwidth limits
			// TelephonyManager.NETWORK_TYPE_1xRTT       ~ 50-100 kbps
			// TelephonyManager.NETWORK_TYPE_CDMA        ~ 14-64 kbps
			// TelephonyManager.NETWORK_TYPE_HSDPA       ~ 2-14 Mbps
			// TelephonyManager.NETWORK_TYPE_HSPA        ~ 700-1700 kbps
			// TelephonyManager.NETWORK_TYPE_HSUPA       ~ 1-23 Mbps
			// TelephonyManager.NETWORK_TYPE_UMTS        ~ 400-7000 kbps
			// TelephonyManager.NETWORK_TYPE_UNKNOWN     ~ Unknown

		}*/
	}

	void Check_Internet_Speed(){
       /* long startTime;
        long endTime;
        long fileSize;
        OkHttpClient client = new OkHttpClient();

// bandwidth in kbps
        private int POOR_BANDWIDTH = 150;
        private int AVERAGE_BANDWIDTH = 550;
        private int GOOD_BANDWIDTH = 2000;

        Request request = new Request.Builder()
                .url("IMAGE_URL_HERE")
                .build();

        startTime = System.currentTimeMillis();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    Log.d(TAG, responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                InputStream input = response.body().byteStream();

                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];

                    while (input.read(buffer) != -1) {
                        bos.write(buffer);
                    }
                    byte[] docBuffer = bos.toByteArray();
                    fileSize = bos.size();

                } finally {
                    input.close();
                }

                endTime = System.currentTimeMillis();


                // calculate how long it took by subtracting endtime from starttime

                double timeTakenMills = Math.floor(endTime - startTime);  // time taken in milliseconds
                double timeTakenSecs = timeTakenMills / 1000;  // divide by 1000 to get time in seconds
                final int kilobytePerSec = (int) Math.round(1024 / timeTakenInSecs);

                if(kilobytePerSec <= POOR_BANDWIDTH){
                    // slow connection
                }

                // get the download speed by dividing the file size by time taken to download
                double speed = fileSize / timeTakenMills;

                Log.d(TAG, "Time taken in secs: " + timeTakenSecs);
                Log.d(TAG, "kilobyte per sec: " + kilobytePerSec);
                Log.d(TAG, "Download Speed: " + speed);
                Log.d(TAG, "File size: " + fileSize);


            }
        });*/
	}

}
