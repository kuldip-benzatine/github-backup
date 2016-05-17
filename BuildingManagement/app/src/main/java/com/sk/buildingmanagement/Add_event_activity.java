package com.sk.buildingmanagement;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Add_event_activity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    EditText add_event_activity_venus;
    String building_id,
            eventname,
            starttime,
            endtime,
            chief_guest,
            event_description,
            venus,
            event_id,
            latitude,
            longitude,
            getstatus;
    LatLng a;

    EditText add_event_activity_event_name,
            add_event_activity_chiefguest, add_event_activity_description;
    TextView add_event_activity_end_time, add_event_activity_start_time;
    Button add_event_activity_create_btn;
    Context mcontext;
    MarkerOptions markerOptions = new MarkerOptions();
    GoogleMap mMap;
    String id, device_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mcontext = getApplicationContext();

        SharedPreferences prefs = getSharedPreferences("Login_detail_pref", MODE_PRIVATE);
        id = prefs.getString("id", null);
        device_id = prefs.getString("device_id", null);

        initialize_controll();

        initialize_controll_action();

        if (getIntent().getStringExtra("page_type").equals("create")) {
            add_event_activity_create_btn.setText("Create");
        } else {
            add_event_activity_create_btn.setText("Update");
            // get edit values for event page

            event_id = getIntent().getStringExtra("event_id");
            building_id = getIntent().getStringExtra("building_id");
            eventname = getIntent().getStringExtra("eventname");
            starttime = getIntent().getStringExtra("starttime");
            endtime = getIntent().getStringExtra("endtime");
            chief_guest = getIntent().getStringExtra("chief_guest");
            event_description = getIntent().getStringExtra("event_description");
            venus = getIntent().getStringExtra("venus");
            latitude = getIntent().getStringExtra("latitude");
            longitude = getIntent().getStringExtra("longitude");
            getstatus = getIntent().getStringExtra("status");
            // display all edit values for user
            a = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
            //put_marker(a);
            get_edit_values();

        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void initialize_controll() {
        add_event_activity_venus = (EditText) findViewById(R.id.add_event_activity_venus);
        add_event_activity_event_name = (EditText) findViewById(R.id.add_event_activity_event_name);
        add_event_activity_end_time = (TextView) findViewById(R.id.add_event_activity_end_time);
        add_event_activity_start_time = (TextView) findViewById(R.id.add_event_activity_start_time);
        add_event_activity_chiefguest = (EditText) findViewById(R.id.add_event_activity_chiefguest);
        add_event_activity_description = (EditText) findViewById(R.id.add_event_activity_description);
        add_event_activity_create_btn = (Button) findViewById(R.id.add_event_activity_create_btn);

    }

    public void get_edit_values() {
        add_event_activity_venus.setText(venus);
        add_event_activity_event_name.setText(eventname);
        add_event_activity_start_time.setText(starttime);
        add_event_activity_end_time.setText(endtime);
        add_event_activity_chiefguest.setText(chief_guest);
        add_event_activity_description.setText(event_description);
    }

    public void put_marker(LatLng p1) {
        //LatLng p1=getLocationFromAddress(mcontext,add_event_activity_venus.getText().toString());
        markerOptions.position(p1);
        markerOptions.position(p1);
        // Setting the title for the marker.
        // This will be displayed on taping the marker
        markerOptions.title(p1.latitude + " : " + p1.longitude);
        // Clears the previously touched position
//        mMap.clear();
        // Animating to the touched position
        mMap.animateCamera(CameraUpdateFactory.newLatLng(p1));
        // Placing a marker on the touched position
        mMap.addMarker(markerOptions);
        //latitude= String.valueOf(p1.latitude);
        //longitude= String.valueOf(p1.longitude);
    }

    public void initialize_controll_action() {
        add_event_activity_create_btn.setOnClickListener(this);
        add_event_activity_start_time.setOnClickListener(this);
        add_event_activity_end_time.setOnClickListener(this);
        add_event_activity_venus.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        LatLng p1 = getLocationFromAddress(mcontext, add_event_activity_venus.getText().toString());
                        markerOptions.position(p1);
                        markerOptions.position(p1);
                        // Setting the title for the marker.
                        // This will be displayed on taping the marker
                        markerOptions.title(p1.latitude + " : " + p1.longitude);
                        // Clears the previously touched position
                        mMap.clear();
                        // Animating to the touched position
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(p1));
                        // Placing a marker on the touched position
                        mMap.addMarker(markerOptions);
                        latitude = String.valueOf(p1.latitude);
                        longitude = String.valueOf(p1.longitude);
                    } catch (Exception e) {
                        Log.d("Address not found", e.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, Event_activity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        if(getIntent().getStringExtra("page_type").equals("update")) {
            put_marker(a);
        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {


                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);
                latitude= String.valueOf(latLng.latitude);
                longitude= String.valueOf(latLng.longitude);
                //add_event_activity_venus.setText(getAddress(latLng.latitude,latLng.longitude));
                add_event_activity_venus.setText(util.full_address(mcontext,latLng.latitude,latLng.longitude));
            }
        });
    }



//    private String getAddress(double latitude, double longitude)
//    {
//        StringBuilder result = new StringBuilder();
//        try
//        {
//            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
//            if (addresses.size() > 0)
//            {
//                Address address = addresses.get(0);
//                result.append(address.getLocality()).append("\n");
//                result.append(address.getCountryName());
//            }
//        }
//        catch (IOException e)
//        {
//            Log.e("tag", e.getMessage());
//        }
//
//        return result.toString();
//    }
//    public String full_address(double latitude, double longitude)
//    {
//        Geocoder geocoder;
//        List<Address> addresses=null;
//        geocoder = new Geocoder(this, Locale.getDefault());
//        String address="",city="",state="",country="",postalCode="";
//        StringBuilder result = new StringBuilder();
//        try {
//            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//            city = addresses.get(0).getLocality();
//            state = addresses.get(0).getAdminArea();
//            country = addresses.get(0).getCountryName();
//            postalCode = addresses.get(0).getPostalCode();
//            //String knownName = addresses.get(0).getFeatureName(); //
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//            Toast.makeText(this,"location address is not found",Toast.LENGTH_LONG).show();
//        }
//        // check address is null and add string
//        if (address == null)
//        {
//            postalCode="";
//
//        }
//        else
//        {
//            result.append(address);
//        }
//        // check city is empty and add city name
//        if (city == null)
//        {
//            city="";
//
//        }
//        else
//        {
//            result.append(","+city);
//        }
//        // check state is null and add state
//        if (state == null)
//        {
//            state="";
//
//        }
//        else
//        {
//            result.append(","+state);
//        }
//        // check country is null and add country
//        if (country == null)
//        {
//            country="";
//
//        }
//        else
//        {
//            result.append(","+country);
//        }
//        // check postalcode null and add postal code
//        if (postalCode == null)
//        {
//            postalCode="";
//
//        }
//        else
//        {
//            result.append(","+postalCode);
//        }
//
//        return result.toString();
//        //return address+","+city+","+state+","+country+","+postalCode;
//        //return addresses.get(0).getAddressLine(0)+","+addresses.get(0).getLocality()+","+addresses.get(0).getAdminArea()+","+addresses.get(0).getCountryName()+","+addresses.get(0).getPostalCode();
//    }

    @Override
    public void onClick(View v)
    {
        // create button press that time add event
        if(v.getId()==R.id.add_event_activity_create_btn)
        {
            if(util.isOnline(this))
            {
                SharedPreferences prefs = getSharedPreferences("Login_detail_pref", MODE_PRIVATE);
                building_id = prefs.getString("Building_id", null);
                eventname = add_event_activity_event_name.getText().toString();
                starttime = add_event_activity_start_time.getText().toString();
                endtime = add_event_activity_end_time.getText().toString();
                chief_guest = add_event_activity_chiefguest.getText().toString();
                event_description = add_event_activity_description.getText().toString();
                venus = add_event_activity_venus.getText().toString();
                if (building_id.isEmpty() || eventname.isEmpty() || starttime.isEmpty() || endtime.isEmpty() || event_description.isEmpty() || venus.isEmpty() || latitude.isEmpty() || longitude.isEmpty() || building_id==null ||  eventname==null ||  starttime==null || endtime==null || event_description==null ||venus==null || latitude==null || longitude==null) {
                    Toast.makeText(this, R.string.add_event_check_data_isempty, Toast.LENGTH_LONG).show();
                }
                else
                {
                    new add_event().execute();
                }
            }
            else
            {
                Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show();
            }
        }

        if(v.getId()==R.id.add_event_activity_start_time)
        {

            dialog_class.date_time_picker_dialog(this,add_event_activity_start_time);
        }
        if(v.getId()==R.id.add_event_activity_end_time)
        {
            dialog_class.date_time_picker_dialog(this,add_event_activity_end_time);
        }
    }

    String add_event_url="http://benzatineinfotech.com/webservice/build_mgnt/admin_add_event.php";
    String Update_event_url="http://benzatineinfotech.com/webservice/build_mgnt/admin_edit_event.php";

    class add_event extends AsyncTask<String, String, String>
    {
        ProgressDialog pDialog;

        String response;
        JSONObject json;
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(Add_event_activity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        /**
         * Getting Wing list on back ground
         * */
        protected String doInBackground(String... params)
        {
            // Check for success tag
            try
            {
                // Building Parameters
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("id",id));
                params1.add(new BasicNameValuePair("device_id",device_id));
                params1.add(new BasicNameValuePair("building_id",building_id));
                params1.add(new BasicNameValuePair("eventname",eventname));
                params1.add(new BasicNameValuePair("starttime",starttime));
                params1.add(new BasicNameValuePair("endtime",endtime));
            //    params1.add(new BasicNameValuePair("chief_guest",chief_guest));
                params1.add(new BasicNameValuePair("event_description",event_description));
                params1.add(new BasicNameValuePair("venus",venus));
                params1.add(new BasicNameValuePair("latitude",latitude));
                params1.add(new BasicNameValuePair("longitude",longitude));

                HttpLoader loader=new HttpLoader();
                String url;
                if(getIntent().getStringExtra("page_type").equals("create"))
                {
                    url=add_event_url;
                }
                else
                {
                    url=Update_event_url;
                    params1.add(new BasicNameValuePair("event_id",event_id));
                    params1.add(new BasicNameValuePair("status",getstatus));
                }
                if(chief_guest.equals("")||chief_guest==null)
                {
                    response = loader.loadDataByPost(url,params1);
                }
                else
                {
                   // List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                    //params2.add(new BasicNameValuePair("chief_guest",chief_guest));
                    params1.add(new BasicNameValuePair("chief_guest",chief_guest));
                    response= loader.loadDataByPost(url,params1);
                }

               // response = loader.loadDataByPost(add_event_url,params1);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url)
        {
            // dismiss the dialog once got all details
            pDialog.dismiss();
            int success;
            //SharedPreferences.Editor editor = getSharedPreferences("get_wing_list", Context.MODE_PRIVATE).edit();
            try
            {
                JSONObject json = new JSONObject(response);
                success = json.getInt("status_code");
                if (success == 200)
                {
                    Toast.makeText(Add_event_activity.this, json.optString("msg"),Toast.LENGTH_LONG).show();
                    finish();
                }
                if (success == 611)
                {
                    Toast.makeText(Add_event_activity.this, json.optString("msg"),Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {

            }
        }
    }
}
