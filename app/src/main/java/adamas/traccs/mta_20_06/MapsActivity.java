package adamas.traccs.mta_20_06;

import android.annotation.TargetApi;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;

import android.location.Geocoder;

import android.os.Build;

import android.os.Bundle;


import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import androidx.fragment.app.FragmentActivity;
import timesheet.NetworkService;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String Current_addrress="";
    String Dest_addrress="";
    Location_Address loc_dest=null;
    Location_Address Current_loc=null;
    Address dest_loc=null;
    List<Address> address=null;
    final String PREFS_NAME = "MTAPrefs";
    String[] permissions = {
            "com.google.android.providers.gsf.permission.READ_GSERVICES",
            "android.permission.MAPS_RECEIVE",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_NETWORK_STATE"
    };

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {

        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
      /*  if (shouldAskPermissions()) {
            askPermissions();
        }*/
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }catch(Exception ex){Toast.makeText(this, "Unable to load Map\n" + ex.toString(), Toast.LENGTH_SHORT).show();}
        Bundle bundle = getIntent().getExtras();


        Current_loc= get_Current_Location();
        //  Toast.makeText(this, Current_loc.Latitude + "\n" + Current_loc.Longitude, Toast.LENGTH_SHORT).show();

        Geocoder  coder = new Geocoder(this, Locale.getDefault());

        try{
            if (!bundle.get("Dest_Address").toString().equalsIgnoreCase("") &&
                    !bundle.get("Dest_Address").toString().equalsIgnoreCase("-"))
            {
                Dest_addrress=bundle.get("Dest_Address").toString();
            }
            get_location(Dest_addrress);
            if(true) return;

            try {
                address=null;
                int cnt=0;
                try{
                    address = coder.getFromLocationName(Dest_addrress, 5);
                }catch(Exception ex){}

                while(address.size()==0 && cnt++<3) {
                    try{
                        address = coder.getFromLocationName(Dest_addrress, 5);
                    }catch(Exception ex){}

                }

                if (address.size()==0) {
                    get_location(Dest_addrress);
                }

                if (address.size()>0) {
                    dest_loc = address.get(0);
                    loc_dest.Address=Dest_addrress;
                    loc_dest.Latitude=dest_loc.getLatitude();
                    loc_dest.Longitude=dest_loc.getLongitude();
                }
            } catch (Exception ex) {  Toast.makeText(this, "Destination:\n"+ ex.toString(), Toast.LENGTH_SHORT).show();   }

            //   Toast.makeText(this, "Destination " + "\n" + dest_loc.getAddressLine(0) +" " +
            //           dest_loc.getLatitude() + "," + dest_loc.getLongitude(), Toast.LENGTH_SHORT).show();

        }catch(Exception ex){
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();

        }


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap2) {
        mMap = googleMap2;



        LatLng Current=null;
        LatLng dest=null;
        //  Toast.makeText(this,  "Drawing Path between \n" + Current_addrress + "\n\n" + Dest_addrress, Toast.LENGTH_SHORT).show();
        try{

            Current=new LatLng(Current_loc.Latitude,Current_loc.Longitude);
            dest=new LatLng(loc_dest.Latitude,loc_dest.Longitude);
            List<Marker> markers= new ArrayList<Marker>();



          /*  mMap.addMarker(new MarkerOptions().position(Current).title(Current_addrress));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(Current));

            mMap.addMarker(new MarkerOptions().position(dest).title(Dest_addrress));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(dest));
*/

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dest, 13));

            Marker m =  mMap.addMarker(new MarkerOptions().title("Destination").snippet(Dest_addrress).position(dest));
            m.showInfoWindow();
            markers.add(m);


            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Current, 14));
            Marker m2= mMap.addMarker(new MarkerOptions().title("Current").snippet(Current_loc.Address).position(Current));
            m2.showInfoWindow();
            markers.add(m2);




        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),"Problem in Adding Marker"  + ex , Toast.LENGTH_LONG).show(); }


        // if(1==1) return;


        //--------calculating distance and finding path

        try{
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
            Polyline line = mMap.addPolyline(new PolylineOptions().add(Current, dest).width(5).color(Color.RED));

            double d2=gps2m(Current.latitude, Current.longitude, dest.latitude, dest.longitude);
            d2=Math.round(d2)/1000;

            //-------------code find distance between source and destination---------------------------------

            GMapV2Direction md = new GMapV2Direction();
            Document doc = md.getDocument(Current, dest, GMapV2Direction.MODE_DRIVING);

            int duration = md.getDurationValue(doc);
            String distance = md.getDistanceText(doc);
            String start_address = md.getStartAddress(doc);
            String copy_right = md.getCopyRights(doc);

            ArrayList<LatLng> directionPoint = md.getDirection(doc);
            PolylineOptions rectLine = new PolylineOptions().width(6).color(Color.BLUE);

            for(int i = 0 ; i < directionPoint.size() ; i++) {
                rectLine.add(directionPoint.get(i));
            }

            mMap.addPolyline(rectLine);


            int min=duration/60;
            int hours=min/60;
            min=min%60;

            String msg="Distance between these loction : " + d2 + " KM \n" +
                    "Road distance =" + distance + " Duration: " + hours + ":" + (min<10 ? "0"+ min : min) ;

            Toast.makeText(getApplicationContext(), msg , Toast.LENGTH_LONG).show();

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Current, 8));

            mMap.addMarker(new MarkerOptions().title("Detail").snippet(msg).position(Current));

            // PolygonOptions options= new PolygonOptions();
            // options.add(Current);
            // googleMap.addPolygon(options);

            //ShowDialog(getApplicationContext(),msg);


        }catch (Exception ex) {
            Toast.makeText(getApplicationContext(),  "By Road path not found between locations" , Toast.LENGTH_LONG).show();
        }
        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    private double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
        double pk = (double) (180/3.14169);

        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;

        double t1 = Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2);
        double t2 = Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2);
        double t3 = Math.sin(a1)*Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000*tt;
    }
    Location_Address get_Current_Location(){
        Location_Address loc = new Location_Address();
        try {
            // callPlaceDetectionApi();
            SharedPreferences settings = null;
            settings = getSharedPreferences(PREFS_NAME, 0);
            loc.Address=  settings.getString("Address","");
            loc.Latitude= Double.parseDouble(settings.getString("Latitude","0"));
            loc.Longitude=  Double.parseDouble(settings.getString("Longitude","0"));

            //  Toast.makeText(getApplicationContext(),  "Getting Current Location\n" +loc.Address , Toast.LENGTH_LONG).show();

        }catch(Exception ex){}
        return loc;
    }

    void get_location(String address){

//        link="https://www.google.com/maps/dir/"+Current_Address+"/" + Dest_Address+"&sensor=true";

        String link = "http://maps.google.com/maps/api/geocode/json?address=" + address.replace(" ", "%20") + "";

        try {

            // String Json_response = NetworkService.INSTANCE.search(link);


            String Json_response = "";
            URL url;
            HttpURLConnection urlConnection;

            url = new URL(link);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream is = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(is);

            int data = inputStreamReader.read();
            while (data != -1) {
                char curr = (char) data;
                Json_response += curr;
                data = inputStreamReader.read();
            }

            // Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            if (Json_response != null) {
                try {
                    JSONObject locationObject = new JSONObject(Json_response);
                    JSONObject locationGeo = locationObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                    loc_dest= new Location_Address();
                    //   Toast.makeText(getApplicationContext(),locationGeo.toString(),Toast.LENGTH_LONG).show();

                    loc_dest.Address=address;
                    loc_dest.Latitude=locationGeo.getDouble("lat");
                    loc_dest.Longitude=locationGeo.getDouble("lng");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    Address getLocation(String address){

        Address add=null;
        try {

          /* String link = "http://maps.google.com/maps/api/geocode/json?address=" + Dest_addrress.replace(" ", "%20") + "";
           JSONObject json;

           GetLocationDownloadTask getLocation = new GetLocationDownloadTask();

           getLocation.execute(link);

           json = getLocation.getLocation();

         Toast.makeText(getApplicationContext(),json.toString(),Toast.LENGTH_LONG).show();*/


        }catch(Exception ex){}

        return add;
    }
}
