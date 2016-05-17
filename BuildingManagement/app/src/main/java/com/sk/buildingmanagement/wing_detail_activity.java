package com.sk.buildingmanagement;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.sk.buildingmanagement.adapter.shop_delete_adapter;
import com.sk.buildingmanagement.adapter.wing_detail_adapter;
import com.sk.buildingmanagement.lisners.ButtonClick;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class wing_detail_activity extends AppCompatActivity implements ButtonClick
{
    ArrayList<HashMap<String,String>>wingList=new ArrayList<HashMap<String,String>>();

    String wing_name,ground_floor,basement,total_floor;
    List<String> list;
    ListView wing_detail_list_view;
    wing_detail_adapter wing_detail_adapter;
    String id,device_id;

    int old_floor_end_series=0;
    int new_floor_end_series=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wing_detail_activity);
        getSupportActionBar().setSubtitle("wing details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = getSharedPreferences("Login_detail_pref",MODE_PRIVATE);
        id=prefs.getString("id",null);
        device_id=prefs.getString("device_id",null);


        list=new ArrayList<String>();

        wing_detail_list_view=(ListView)findViewById(R.id.wing_detail_list_view);

        wingList=new ArrayList<HashMap<String,String>>();
        wing_id=getIntent().getStringExtra("wing_id");
        building_id=getIntent().getStringExtra("building_id");
        wing_name=getIntent().getStringExtra("wingname");
        ground_floor=getIntent().getStringExtra("ground_floor");
        basement=getIntent().getStringExtra("basement");
        total_floor=getIntent().getStringExtra("total_floor");
        if(util.isOnline(this))
        {
            create_total_floor();
            new get_floor_list().execute();
        }
        else
        {
            Toast.makeText(this,R.string.no_internet,Toast.LENGTH_SHORT).show();
        }
    }

    public void create_total_floor()
    {
        if(basement.equals("1"))
        {
            list.add("Base Floor");
        }
        if(ground_floor.equals("1"))
        {
            list.add("Ground Floor");
        }
        for(int a = 1; a<=Integer.parseInt(total_floor);a++)
        {
            list.add("Floor " + a);
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


    // get floor list ...

    String get_floor_list_url="http://benzatineinfotech.com/webservice/build_mgnt/admin_floor_list.php";
    String building_id,wing_id,add_floor_name,add_floor_total_flat,add_floor_series_start,add_floor_series_end,add_floor_total_sq_feet;
    String floor_id;

    @Override
    public void onButtonClick(String building_id, String wing_id, String add_floor_name, String add_floor_total_flat, String add_floor_series_start, String add_floor_series_end, String add_floor_total_sq_feet)
    {
        this.building_id=building_id;
        this.wing_id=wing_id;
        this.add_floor_name=add_floor_name;
        this.add_floor_total_flat=add_floor_total_flat;
        this.add_floor_series_start=add_floor_series_start;
        this.add_floor_series_end=add_floor_series_end;
        this.add_floor_total_sq_feet=add_floor_total_sq_feet;

            new add_floor().execute();
    }

    // edit shop and delete shop is pospond
    @Override
    public void update_onButtonClick(Integer old_series_end,String floor_id, String building_id, String wing_id, String add_floor_name, String add_floor_total_flat, String add_floor_series_start, String add_floor_series_end, String add_floor_total_sq_feet)
    {
        this.floor_id=floor_id;
        this.building_id=building_id;
        this.wing_id=wing_id;
        this.add_floor_name=add_floor_name;
        this.add_floor_total_flat=add_floor_total_flat;
        this.add_floor_series_start=add_floor_series_start;
        this.add_floor_series_end=add_floor_series_end;
        this.add_floor_total_sq_feet=add_floor_total_sq_feet;
        old_floor_end_series=old_series_end;
        new_floor_end_series= Integer.parseInt(add_floor_series_end);
        if(old_floor_end_series > new_floor_end_series)
        {
//            deleted_floor=old_floor_end_series-new_floor_end_series;
//            new get_shop_list().execute();
            //shop_delete_dialog();
            Toast.makeText(getBaseContext(),R.string.deleted_shop_is_not_available,Toast.LENGTH_LONG).show();
        }
        else
        {
            new edit_floor().execute();
        }

    }
    int deleted_floor;
    String add_floor_url="http://benzatineinfotech.com/webservice/build_mgnt/admin_add_floor.php";
    class add_floor extends AsyncTask<String, String, String>
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
            pDialog = new ProgressDialog(wing_detail_activity.this);
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
                params1.add(new BasicNameValuePair("wing_id",wing_id));
                params1.add(new BasicNameValuePair("floor_name",add_floor_name));
                params1.add(new BasicNameValuePair("total_flat",add_floor_total_flat));
                params1.add(new BasicNameValuePair("series_start",add_floor_series_start));
                params1.add(new BasicNameValuePair("series_end",add_floor_series_end));
                if(add_floor_total_sq_feet.equals("") || add_floor_total_sq_feet==null)
                {

                }
                else
                {
                    params1.add(new BasicNameValuePair("total_sq_feet", add_floor_total_sq_feet));
                }
                HttpLoader loader=new HttpLoader();
                response = loader.loadDataByPost(add_floor_url,params1);
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
                    Toast.makeText(wing_detail_activity.this, json.optString("msg"),Toast.LENGTH_LONG).show();
                    new get_floor_list().execute();
                }
            }
            catch (Exception e)
            {

            }
        }
    }
        // get floor list
    class get_floor_list extends AsyncTask<String, String, String>
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
            pDialog = new ProgressDialog(wing_detail_activity.this);
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
                params1.add(new BasicNameValuePair("wing_id",wing_id));
                HttpLoader loader=new HttpLoader();
                response = loader.loadDataByPost(get_floor_list_url,params1);
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
                    wingList=new ArrayList<HashMap<String,String>>();
                    for(int i=0;i<JArray.length();i++)
                    {
                        HashMap<String,String> wing=new HashMap<String,String>();
                        JSONObject obj=JArray.optJSONObject(i);

                        wing.put("floor_id",obj.optString("floor_id"));
                        wing.put("building_id",obj.optString("building_id"));
                        wing.put("wing_id",obj.optString("wing_id"));
                        wing.put("floor_name",obj.optString("floor_name"));
                        wing.put("series_start",obj.optString("series_start"));
                        wing.put("series_end",obj.optString("series_end"));
                        wing.put("total_flat",obj.optString("total_flat"));
                        wing.put("total_sq_feet",obj.optString("total_sq_feet"));
                        wing.put("date_create",obj.optString("date_create"));
                        wing.put("date_update",obj.optString("date_update"));
                        wing.put("status",obj.optString("status"));
                        wingList.add(wing);
                    }
                }
                if(success == 610)
                {
                    Toast.makeText(wing_detail_activity.this, json.optString("msg"),Toast.LENGTH_LONG).show();
                }
                wing_detail_adapter=new wing_detail_adapter(wing_detail_activity.this,wing_detail_activity.this,list,building_id,wing_id,wingList);
                wing_detail_list_view.setAdapter(wing_detail_adapter);
            }
            catch (Exception e)
            {
                Log.e("ERROR",e.toString());
            }
        }
    }

    String edit_floor_url="http://benzatineinfotech.com/webservice/build_mgnt/admin_edit_floor.php";

    class edit_floor extends AsyncTask<String, String, String>
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
            pDialog = new ProgressDialog(wing_detail_activity.this);
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
                params1.add(new BasicNameValuePair("floor_id",floor_id));
                params1.add(new BasicNameValuePair("building_id",building_id));
                params1.add(new BasicNameValuePair("wing_id",wing_id));
                params1.add(new BasicNameValuePair("floor_name",add_floor_name));
                params1.add(new BasicNameValuePair("total_flat",add_floor_total_flat));
                params1.add(new BasicNameValuePair("series_start",add_floor_series_start));
                params1.add(new BasicNameValuePair("series_end",add_floor_series_end));
                old_floor_end_series= Integer.parseInt(add_floor_series_end);
                if(add_floor_total_sq_feet.equals("") || add_floor_total_sq_feet==null)
                {

                }
                else
                {
                    params1.add(new BasicNameValuePair("total_sq_feet", add_floor_total_sq_feet));
                }
                HttpLoader loader=new HttpLoader();
                response = loader.loadDataByPost(edit_floor_url,params1);
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
                    Toast.makeText(wing_detail_activity.this, json.optString("msg"),Toast.LENGTH_LONG).show();

                    // Add delete shop code

                    new get_floor_list().execute();
                }
            }
            catch (Exception e)
            {

            }
        }
    }

    AlertDialog myDialog=null;
    shop_delete_adapter shop_delete_adapter;

    public void  shop_delete_dialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.shop_delete_dialog_layout, null);

        ListView shop_delete_dialog_layout_list_view;
        final Button shop_delete_dialog_layout_shop_delete_btn,shop_delete_dialog_layout_shop_cancel_btn;



        shop_delete_dialog_layout_list_view=(ListView)view.findViewById(R.id.shop_delete_dialog_layout_list_view);
        shop_delete_dialog_layout_shop_delete_btn=(Button)view.findViewById(R.id.shop_delete_dialog_layout_shop_delete_btn);
        // cancel button id and that click event
        shop_delete_dialog_layout_shop_cancel_btn= (Button) view.findViewById(R.id.shop_delete_dialog_layout_shop_cancel_btn);

        final shop_delete_adapter shop_delete_adapter=new shop_delete_adapter(wing_detail_activity.this,shoplist,deleted_floor);
        shop_delete_dialog_layout_list_view.setAdapter(shop_delete_adapter);

        shop_delete_dialog_layout_shop_cancel_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDialog.dismiss();
            }
        });
        // add button click event
        shop_delete_dialog_layout_shop_delete_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // update floor detail
                if (util.isOnline(wing_detail_activity.this))
                {
                    deleted_shop_list=shop_delete_adapter.get_deleted_list();

                        new edit_floor().execute();
                        new delete_shop().execute();
                }
                else
                {
                    Toast.makeText(wing_detail_activity.this, R.string.no_internet, Toast.LENGTH_SHORT).show();

                }
                myDialog.dismiss();
            }
        });

        builder.setView(view);

        builder.setCancelable(false);
        myDialog=builder.create();
        myDialog.show();
    }
    int location;
    ArrayList<HashMap<String, String>> deleted_shop_list;
    ArrayList<HashMap<String, String>> shoplist;
    String shop_list_url="http://benzatineinfotech.com/webservice/build_mgnt/admin_flat_list.php";
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
            pDialog = new ProgressDialog(wing_detail_activity.this);
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
                params1.add(new BasicNameValuePair("building_id",building_id));
                params1.add(new BasicNameValuePair("wing_id",wing_id));
                params1.add(new BasicNameValuePair("floor_id",floor_id));
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
                    shop_delete_dialog();
                }
                if(success == 629)
                {
                    Toast.makeText(wing_detail_activity.this,json.optString("msg"),Toast.LENGTH_LONG).show();
                    shop_delete_dialog();
                }
            }
            catch (Exception e)
            {
                Log.e("ERROR",e.toString());
            }
        }
    }

    String delete_shop_url="http://benzatineinfotech.com/webservice/build_mgnt/admin_delete_flat.php";
    class delete_shop extends AsyncTask<String, String, String>
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
            pDialog = new ProgressDialog(wing_detail_activity.this);
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
            for(int i=0;i<deleted_shop_list.size();i++)
            {
                for(int a=0;a<shoplist.size();a++)
                {
                    if(deleted_shop_list.get(i).get("flat_no").equals(shoplist.get(a).get("flat_no")))
                    {
                        location=i;
                        try
                        {
                            SharedPreferences prefs = getSharedPreferences("Login_detail_pref", MODE_PRIVATE);
                            // Building Parameters
                            List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                            params1.add(new BasicNameValuePair("id",prefs.getString("id",null)));
                            params1.add(new BasicNameValuePair("device_id",prefs.getString("device_id",null)));
                            params1.add(new BasicNameValuePair("flat_id", deleted_shop_list.get(location).get("flat_id")));
                            params1.add(new BasicNameValuePair("building_id", getIntent().getStringExtra("building_id")));
                            params1.add(new BasicNameValuePair("wing_id", getIntent().getStringExtra("wing_id")));
                            params1.add(new BasicNameValuePair("floor_id",floor_id));
                            HttpLoader loader = new HttpLoader();
                            response = loader.loadDataByPost(delete_shop_url, params1);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
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
                    Toast.makeText(wing_detail_activity.this,json.optString("SHOP IS DELETED"),Toast.LENGTH_LONG).show();
                }
                if(success == 629)
                {
                    Toast.makeText(wing_detail_activity.this,json.optString("msg"),Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
                Log.e("ERROR",e.toString());
            }
        }
    }

}
