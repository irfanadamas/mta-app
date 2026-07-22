package adamas.traccs.mta_20_06;

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

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;



/**
 * Test speed of our network connection
 * @author Greg Bugaj http://www.gregbugaj.com
 * @version 1.0
 *
 */
public class SpeedTest {
	private String root_address="";
	public  SpeedInfo speedInfo;

	public SpeedTest(String root){
		this.root_address=root;
		speedInfo= new SpeedInfo();
		bindListeners();
	}
	public  void bindListeners() {


		//new Thread(mWorker).start();
		try {
			new MyAsyncClass2().execute();
		}catch(Exception ex){}
	}



	/**
	 * Our Slave worker that does actually all the work
	 */
	public void  Check_Speed(){

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
			//Log.e(TAG, e.getMessage());
		} catch (IOException e) {
			//Log.e(TAG, e.getMessage());
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


	class MyAsyncClass2 extends AsyncTask<Void, Void, Void> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			//	pDialog = new ProgressDialog(Login.this);
			//pDialog.setMessage("Please wait while loading data ....");
			// pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			//	pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... mApi) {
			try {

				Check_Speed();
			}

			catch (Exception ex) {
				// Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			//pDialog.cancel();



			//  Toast.makeText(getApplicationContext(), "Email send", 100).show();
		}
	}

}
