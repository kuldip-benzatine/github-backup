package com.sk.buildingmanagement;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.sk.buildingmanagement.adapter.Shop_info_adapter;
import com.sk.buildingmanagement.lisners.Shop_info_interface;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Shop_info_activity extends AppCompatActivity implements Shop_info_interface
{
    ListView shop_info_activity_list_view;
    Shop_info_adapter shop_info_adapter;
    ArrayList<HashMap<String, String>> shoplist;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_info_activity);
        initialize_controll();
        getSupportActionBar().setSubtitle("Shop Info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        initialize_controll_action();
        if(util.isOnline(this)) {
            new get_shop_list().execute();
        }
        else
        {
            Toast.makeText(this,R.string.no_internet,Toast.LENGTH_LONG).show();
        }
    }
    public void initialize_controll()
    {
        shop_info_activity_list_view=(ListView)findViewById(R.id.shop_info_activity_list_view);
    }
    public void initialize_controll_action()
    {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        if(item.getItemId()==R.id.add_wing)
        {

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //getMenuInflater().inflate(R.menu.add_wing, menu);
        return true;
    }

    // get shop list
    String shop_list_url="http://benzatineinfotech.com/webservice/build_mgnt/admin_flat_list.php";


    String shop_no,sq_foot,status,flat_id;
    @Override
    public void edit_shop(String flat_id,String shop_no, String sq_feet,String status)
    {
        this.flat_id=flat_id;
        this.shop_no=shop_no;
        this.sq_foot=sq_feet;
        this.status=status;
        new edit_shop().execute();
    }
    String edit_flat_url="http://benzatineinfotech.com/webservice/build_mgnt/admin_edit_flat.php";
    class edit_shop extends AsyncTask<String, String, String>
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
            pDialog = new ProgressDialog(Shop_info_activity.this);
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
                SharedPreferences prefs=getSharedPreferences("Login_detail_pref",MODE_PRIVATE);
                // Building Parameters
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("id",prefs.getString("id",null)));
                params1.add(new BasicNameValuePair("device_id",prefs.getString("device_id",null)));
                params1.add(new BasicNameValuePair("flat_id",flat_id));
                params1.add(new BasicNameValuePair("status",status));
                params1.add(new BasicNameValuePair("sq_feet",sq_foot));
                params1.add(new BasicNameValuePair("flat_no",shop_no));
                HttpLoader loader=new HttpLoader();
                response = loader.loadDataByPost(edit_flat_url,params1);
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

            try
            {
                JSONObject json = new JSONObject(response);
                success = json.getInt("status_code");
                if (success == 200)
                {
                    Toast.makeText(Shop_info_activity.this,json.optString("msg"),Toast.LENGTH_LONG).show();
                    new get_shop_list().execute();
                }
                if(success == 629)
                {
                    Toast.makeText(Shop_info_activity.this,json.optString("msg"),Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
                Log.e("ERROR",e.toString());
            }
        }
    }

    class get_shop_list extends AsyncTask<String, String, String>
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
            pDialog = new ProgressDialog(Shop_info_activity.this);
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
                SharedPreferences prefs=getSharedPreferences("Login_detail_pref",MODE_PRIVATE);
                // Building Parameters
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("id",prefs.getString("id",null)));
                params1.add(new BasicNameValuePair("device_id",prefs.getString("device_id",null)));
                params1.add(new BasicNameValuePair("building_id",getIntent().getStringExtra("building_id")));
                params1.add(new BasicNameValuePair("wing_id",getIntent().getStringExtra("wing_id")));
                params1.add(new BasicNameValuePair("floor_id",getIntent().getStringExtra("floor_id")));
                HttpLoader loader=new HttpLoader();
                response = loader.loadDataByPost(shop_list_url,params1);
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

            try
            {
                JSONObject json = new JSONObject(response);
                success = json.getInt("status_code");
                if (success == 200)
                {
                    JSONArray JArray=json.optJSONArray("data");
                    shoplist=new ArrayList<HashMap<String, String>>();
                    for(int i=0;i<JArray.length();i++)
                    {
                        HashMap<String,String> shop=new HashMap<String,String>();
                        JSONObject obj=JArray.optJSONObject(i);

                        shop.put("flat_id",obj.optString("flat_id"));
                        shop.put("wing_id",obj.optString("wing_id"));
                        shop.put("building_id",obj.optString("building_id"));
                        shop.put("floor_id",obj.optString("floor_id"));
                        shop.put("flat_no",obj.optString("flat_no"));
                        shop.put("sq_feet",obj.optString("sq_feet"));
                        shop.put("assign_id",obj.optString("assign_id"));
                        shop.put("status",obj.optString("status"));
                        shop.put("date_create",obj.optString("date_create"));
                        shop.put("date_update",obj.optString("date_update"));
                       shoplist.add(shop);
                    }
                    Shop_info_adapter shop_info_adapter = new Shop_info_adapter(Shop_info_activity.this,shoplist);
                    shop_info_activity_list_view.setAdapter(shop_info_adapter);
                }
                if(success == 629)
                {
                    Toast.makeText(Shop_info_activity.this,json.optString("msg"),Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
                Log.e("ERROR",e.toString());
            }
        }
    }
}
