package com.sk.buildingmanagement;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.sk.buildingmanagement.adapter.get_wing_list_adapter;
import com.sk.buildingmanagement.lisners.wing_activity_interface;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class wing_activity extends AppCompatActivity implements wing_activity_interface
{

    ListView get_wing_listview;
    String building_id=null;
    com.sk.buildingmanagement.adapter.get_wing_list_adapter get_wing_list_adapter;
    String add_wing_url="http://benzatineinfotech.com/webservice/build_mgnt/admin_add_wing.php";
    String get_wing_list_url="http://benzatineinfotech.com/webservice/build_mgnt/admin_wing_list.php";
    ArrayList<HashMap<String,String>>wingList=new ArrayList<HashMap<String,String>>();
    String id,device_id;
    ImageButton add_wing_activity_add_wing_img_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_wing_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setSubtitle("Wing List");
        initialize_controll();
        SharedPreferences prefs = getSharedPreferences("Login_detail_pref",MODE_PRIVATE);
        building_id=prefs.getString("Building_id",null);
        id=prefs.getString("id",null);
        device_id=prefs.getString("device_id",null);

        new get_wing_list().execute();
        initialize_controll_action();
    }
    public void initialize_controll()
    {

        get_wing_listview = (ListView) findViewById(R.id.get_wing_listview);
        add_wing_activity_add_wing_img_btn=(ImageButton)findViewById(R.id.add_wing_activity_add_wing_img_btn);
    }
    public void initialize_controll_action()
    {

        get_wing_listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(wing_activity.this,wing_detail_activity.class);
                intent.putExtra("wing_id",wingList.get(position).get("wing_id"));
                intent.putExtra("building_id",wingList.get(position).get("building_id"));
                intent.putExtra("wingname",wingList.get(position).get("wingname"));
                intent.putExtra("total_floor",wingList.get(position).get("total_floor"));
                intent.putExtra("ground_floor",wingList.get(position).get("ground_floor"));
                intent.putExtra("basement",wingList.get(position).get("basement"));
                wing_activity.this.startActivity(intent);
            }
        });
        add_wing_activity_add_wing_img_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
//        if(item.getItemId()==R.id.add_wing)
//        {
//            //showDialog();
//        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        getMenuInflater().inflate(R.menu.add_wing, menu);
//        return true;
//    }

    String delete_wing_url="http://benzatineinfotech.com/webservice/build_mgnt/admin_delete_wing.php";
    String wing_id;
    @Override
    public void delete_wing_call_back(String building_id, String wing_id)
    {
        this.building_id=building_id;
        this.wing_id=wing_id;

        delete_wing_dialog();
    }
    AlertDialog mdialog=null;
    public void delete_wing_dialog()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(wing_activity.this);
        builder.setTitle("You Delete This Wing");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                new delete_wing().execute();
                mdialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                   mdialog.dismiss();
            }
        });
        builder.setCancelable(false);
        mdialog=builder.create();
        mdialog.show();
    }

    Boolean flag=false;
    @Override
    public void edit_wing_call_back(String building_id, String wing_id, String wingname, String total_floor, String ground_floor, String basement) {
        this.building_id=building_id;
        this.wing_id=wing_id;
        this.wingname=wingname;
        this.total_floor = total_floor;
        this.ground_floor=ground_floor;
        this.basement=basement;
        flag=true;
        showDialog();
    }
    // edit wing call back interface method

    // Delete wing AsyncTask
    class delete_wing extends AsyncTask<String, String, String>
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
            pDialog = new ProgressDialog(wing_activity.this);
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
                response = loader.loadDataByPost(delete_wing_url ,params1);
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
                    Toast.makeText(wing_activity.this, json.optString("msg"),Toast.LENGTH_LONG).show();
                    new get_wing_list().execute();
                }
            }
            catch (Exception e)
            {

            }
        }
    }

    // get wing list
    class get_wing_list extends AsyncTask<String, String, String>
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
            pDialog = new ProgressDialog(wing_activity.this);
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
                response = loader.loadDataByPost(get_wing_list_url,params1);
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
                    wingList=new ArrayList<HashMap<String, String>>();
                    for(int i=0;i<JArray.length();i++)
                    {
                        HashMap<String,String> wing=new HashMap<String,String>();
                        JSONObject obj=JArray.optJSONObject(i);

                        wing.put("wing_id",obj.optString("wing_id"));
                        wing.put("building_id",obj.optString("building_id"));
                        wing.put("wingname",obj.optString("wingname"));
                        wing.put("total_floor",obj.optString("total_floor"));
                        wing.put("ground_floor",obj.optString("ground_floor"));
                        wing.put("basement",obj.optString("basement"));
                        wing.put("status",obj.optString("status"));
                        wingList.add(wing);
                    }

                    get_wing_list_adapter getwinglist_adapter = new get_wing_list_adapter(wing_activity.this,wingList);
                    get_wing_listview.setAdapter(getwinglist_adapter);
                }
                if(success == 610)
                {
                    Toast.makeText(wing_activity.this,json.optString("msg"),Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
                Log.e("ERROR",e.toString());
            }
        }
    }

    // add wing Alert Dialog
    String wingname, total_floor, ground_floor, basement;
    AlertDialog myDialog=null;
    public void  showDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(wing_activity.this);
        //  builder.setTitle("Add Wing");

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View view = inflater.inflate(R.layout.add_wing_dialog_layout, null);
        final EditText add_wing_name = (EditText) view.findViewById(R.id.add_wing_name);
        final EditText add_wing_floor = (EditText) view.findViewById(R.id.add_wing_floor);
        final CheckBox add_wing_ground_floor_chb = (CheckBox) view.findViewById(R.id.add_wing_ground_floor_chb);
        final CheckBox add_wing_basement_chb = (CheckBox) view.findViewById(R.id.add_wing_basement_chb);


        Button btn_close_popup= (Button) view.findViewById(R.id.btn_close_popup);
        btn_close_popup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDialog.dismiss();
            }
        });

        Button btn_add_popup = (Button) view.findViewById(R.id.btn_add_popup);

        if(flag)
        {
            add_wing_name.setText(wingname);
            add_wing_floor.setText(total_floor);
            if(ground_floor.equals("1"))
            {
                add_wing_ground_floor_chb.setChecked(true);
            }
            else
            {
                add_wing_ground_floor_chb.setChecked(false);
            }
            if(basement.equals("1"))
            {
                add_wing_basement_chb.setChecked(true);
            }
            else
            {
                add_wing_basement_chb.setChecked(false);
            }
            btn_add_popup.setText("Update");
        }

        btn_add_popup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                wingname=add_wing_name.getText().toString();
                total_floor=add_wing_floor.getText().toString();
                if(add_wing_ground_floor_chb.isChecked())
                {
                    ground_floor="1";
                }
                else
                {
                    ground_floor="0";
                }
                if(add_wing_basement_chb.isChecked())
                {
                    basement="1";
                }
                else
                {
                    basement="0";
                }
                if(flag)
                {
                    new edit_wing().execute();
                }
                else
                {
                    new add_wing().execute();
                }

                myDialog.dismiss();

            }
        });

        builder.setView(view);

        builder.setCancelable(false);
        myDialog=builder.create();
        myDialog.show();
    }


    // add wing detail
    class add_wing extends AsyncTask<String, String, String>
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
            pDialog = new ProgressDialog(wing_activity.this);
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
                params1.add(new BasicNameValuePair("wingname",wingname));
                params1.add(new BasicNameValuePair("total_floor",total_floor));
                params1.add(new BasicNameValuePair("ground_floor",ground_floor));
                params1.add(new BasicNameValuePair("basement",basement));

                HttpLoader loader=new HttpLoader();
                response = loader.loadDataByPost(add_wing_url,params1);
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
                    Toast.makeText(wing_activity.this, json.optString("msg"),Toast.LENGTH_LONG).show();
                    new get_wing_list().execute();
                }
            }
            catch (Exception e)
            {

            }
        }
    }

    // add wing detail
    String edit_wing_url="http://benzatineinfotech.com/webservice/build_mgnt/admin_edit_wing.php";
    class edit_wing extends AsyncTask<String, String, String>
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
            pDialog = new ProgressDialog(wing_activity.this);
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
                params1.add(new BasicNameValuePair("wing_id",wing_id));
                params1.add(new BasicNameValuePair("building_id",building_id));
                params1.add(new BasicNameValuePair("wingname",wingname));
                params1.add(new BasicNameValuePair("total_floor",total_floor));
                params1.add(new BasicNameValuePair("ground_floor",ground_floor));
                params1.add(new BasicNameValuePair("basement",basement));
                HttpLoader loader=new HttpLoader();
                response = loader.loadDataByPost(edit_wing_url,params1);
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
                    Toast.makeText(wing_activity.this, json.optString("msg"),Toast.LENGTH_LONG).show();
                    new get_wing_list().execute();
                }
                if(success == 609)
                {
                    Toast.makeText(wing_activity.this, json.optString("msg"),Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {

            }
        }
    }
}
