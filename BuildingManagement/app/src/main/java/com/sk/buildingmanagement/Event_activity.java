package com.sk.buildingmanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.sk.buildingmanagement.adapter.get_event_list_adapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Event_activity extends AppCompatActivity implements View.OnClickListener
{
    ImageButton event_activity_create_img_btn;
    String get_event_list_url="http://benzatineinfotech.com/webservice/build_mgnt/admin_list_event.php";
    ListView event_activity_event_listview;
    String building_id;
    get_event_list_adapter get_event_list_adapter;
    String device_id,id;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);
        getSupportActionBar().setSubtitle("Event List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = getSharedPreferences("Login_detail_pref",MODE_PRIVATE);
        building_id=prefs.getString("Building_id",null);
        id=prefs.getString("id",null);
        device_id=prefs.getString("device_id",null);

        initialize_controll();


        initialize_controll_action();
        if(util.isOnline(this))
        {
            new get_event_list().execute();
        }
        else
        {
            Toast.makeText(this,R.string.no_internet,Toast.LENGTH_LONG).show();
        }
    }

    public void initialize_controll()
    {
        event_activity_create_img_btn=(ImageButton) findViewById(R.id.event_activity_create_img_btn);
        event_activity_event_listview=(ListView)findViewById(R.id.event_activity_event_listview);
    }
    public void initialize_controll_action()
    {
        event_activity_create_img_btn.setOnClickListener(this);
        event_activity_event_listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                Intent intent = new Intent(Event_activity.this,Add_event_activity.class);
                intent.putExtra("page_type","update");
                intent.putExtra("event_id",wingList.get(position).get("event_id"));
                intent.putExtra("building_id",wingList.get(position).get("building_id"));
                intent.putExtra("eventname",wingList.get(position).get("eventname"));
                intent.putExtra("starttime",wingList.get(position).get("starttime"));
                intent.putExtra("endtime",wingList.get(position).get("endtime"));
                intent.putExtra("chief_guest",wingList.get(position).get("chief_guest"));
                intent.putExtra("event_description",wingList.get(position).get("event_description"));
                intent.putExtra("venus",wingList.get(position).get("venus"));
                intent.putExtra("latitude",wingList.get(position).get("latitude"));
                intent.putExtra("longitude",wingList.get(position).get("longitude"));
                intent.putExtra("date_create",wingList.get(position).get("date_create"));
                intent.putExtra("date_update",wingList.get(position).get("date_update"));
                intent.putExtra("status",wingList.get(position).get("status"));

                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.event_activity_create_img_btn)
        {
            Intent intent=new Intent(this,Add_event_activity.class);
            intent.putExtra("page_type","create");
            startActivity(intent);
            finish();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    ArrayList<HashMap<String,String>>wingList=new ArrayList<HashMap<String,String>>();
    class get_event_list extends AsyncTask<String, String, String>
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
            pDialog = new ProgressDialog(Event_activity.this);
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
                HttpLoader loader=new HttpLoader();
                response = loader.loadDataByPost(get_event_list_url,params1);
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
                    JSONArray JArray=json.optJSONArray("data");
                    wingList=new ArrayList<HashMap<String,String>>();
                    for(int i=0;i<JArray.length();i++)
                    {
                        HashMap<String,String> wing=new HashMap<String,String>();
                        JSONObject obj=JArray.optJSONObject(i);

                        wing.put("event_id",obj.optString("event_id"));
                        wing.put("building_id",obj.optString("building_id"));
                        wing.put("eventname",obj.optString("eventname"));
                        wing.put("starttime",obj.optString("starttime"));
                        wing.put("endtime",obj.optString("endtime"));
                        wing.put("chief_guest",obj.optString("chief_guest"));
                        wing.put("event_description",obj.optString("event_description"));
                        wing.put("venus",obj.optString("venus"));
                        wing.put("latitude",obj.optString("latitude"));
                        wing.put("longitude",obj.optString("longitude"));
                        wing.put("date_create",obj.optString("date_create"));
                        wing.put("date_update",obj.optString("date_update"));
                        wing.put("status",obj.optString("status"));
                        wingList.add(wing);
                    }
                    get_event_list_adapter = new get_event_list_adapter(Event_activity.this,wingList);
                    event_activity_event_listview.setAdapter(get_event_list_adapter);
                }
                if(success == 613)
                {
                    Toast.makeText(Event_activity.this, json.optString("msg"),Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
                Log.e("ERROR",e.toString());
            }
        }
    }

}
