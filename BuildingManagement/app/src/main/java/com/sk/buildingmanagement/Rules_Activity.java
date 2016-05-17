package com.sk.buildingmanagement;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.sk.buildingmanagement.adapter.Rule_list_adapter;
import com.sk.buildingmanagement.lisners.Rules_interface;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Rules_Activity extends AppCompatActivity implements View.OnClickListener,Rules_interface {
    ListView rules_activity_listview;
    ImageButton rules_activity_add_rules_img_btn;
    String id,device_id,building_id;
    Rule_list_adapter rule_list_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules_activity);
        getSupportActionBar().setSubtitle("Rules");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences prefs=getSharedPreferences("Login_detail_pref",MODE_PRIVATE);
        id=prefs.getString("id",null);
        device_id=prefs.getString("device_id",null);
        building_id=prefs.getString("Building_id",null);
        // initialize and action listenears
        initialize_controll();
        initialize_controll_action();

        new get_rule_list().execute();


    }
    public void initialize_controll()
    {
        rules_activity_listview=(ListView)findViewById(R.id.rules_activity_listview);
        rules_activity_add_rules_img_btn=(ImageButton)findViewById(R.id.rules_activity_add_rules_img_btn);
    }
    public void initialize_controll_action()
    {
        rules_activity_add_rules_img_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.rules_activity_add_rules_img_btn)
        {
            Add_rules_dialog();
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        getMenuInflater().inflate(R.menu.rules_menu,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
    AlertDialog mdialog=null;
    String rule_title,rule_description;
    public void Add_rules_dialog()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(Rules_Activity.this);
        builder.setTitle("Add Rules");
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View view = inflater.inflate(R.layout.rules_add_dialog_layout, null);
        final EditText rules_title=(EditText)view.findViewById(R.id.rules_add_dialog_layout_rules_title_edit_txt);
        final EditText rules_description=(EditText)view.findViewById(R.id.rules_add_dialog_layout_rules_description_edit_txt);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                rule_title=rules_title.getText().toString();
                rule_description=rules_description.getText().toString();
                if(rule_title.equals("")||rules_description.equals(""))
                {
                    Toast.makeText(getBaseContext(),R.string.add_event_check_data_isempty,Toast.LENGTH_LONG).show();
                }
                else {
                    new add_rule().execute();
                    mdialog.dismiss();
                }

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
        builder.setView(view);
        builder.setCancelable(false);
        mdialog=builder.create();
        mdialog.show();
    }

    // edit rule
    String edit_rule_url="http://benzatineinfotech.com/webservice/build_mgnt/admin_edit_rule.php";
    String rule_id;
    @Override
    public void edit_rule(String rule_id, String rule_title, String rule_description)
    {
        this.rule_id=rule_id;
        this.rule_title=rule_title;
        this.rule_description=rule_description;
        new edit_rule().execute();
    }
    // delete rule
    String delete_rule_url="http://benzatineinfotech.com/webservice/build_mgnt/admin_delete_rule.php";
    @Override
    public void delete_rule(String rule_id)
    {
        this.rule_id=rule_id;
        new delete_rule().execute();
    }
    String Add_rules_url="http://benzatineinfotech.com/webservice/build_mgnt/admin_add_rule.php";
    class add_rule extends AsyncTask<String, String, String>
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
            pDialog = new ProgressDialog(Rules_Activity.this);
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

                params1.add(new BasicNameValuePair("rule_title",rule_title));
                params1.add(new BasicNameValuePair("rule_description",rule_description));

                HttpLoader loader=new HttpLoader();
                response = loader.loadDataByPost(Add_rules_url,params1);
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
                    Toast.makeText(Rules_Activity.this, json.optString("msg"),Toast.LENGTH_LONG).show();
                    new get_rule_list().execute();
                }

            }
            catch (Exception e)
            {

            }
        }
    }
    class edit_rule extends AsyncTask<String, String, String>
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
            pDialog = new ProgressDialog(Rules_Activity.this);
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
                params1.add(new BasicNameValuePair("rule_id",rule_id));
                params1.add(new BasicNameValuePair("rule_title",rule_title));
                params1.add(new BasicNameValuePair("rule_description",rule_description));

                HttpLoader loader=new HttpLoader();
                response = loader.loadDataByPost(edit_rule_url,params1);
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
                    Toast.makeText(Rules_Activity.this, json.optString("msg"),Toast.LENGTH_LONG).show();
                    new get_rule_list().execute();
                }

            }
            catch (Exception e)
            {

            }
        }
    }
    String get_rule_list_url="http://benzatineinfotech.com/webservice/build_mgnt/admin_rule_list.php";
    ArrayList<HashMap<String,String>> rule_list;
    class get_rule_list extends AsyncTask<String, String, String>
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
            pDialog = new ProgressDialog(Rules_Activity.this);
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
                response = loader.loadDataByPost(get_rule_list_url,params1);
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
                    rule_list=new ArrayList<HashMap<String, String>>();
                    for(int i=0;i<JArray.length();i++)
                    {
                        HashMap<String,String> rule=new HashMap<String,String>();
                        JSONObject obj=JArray.optJSONObject(i);

                        rule.put("rule_id",obj.optString("rule_id"));
                        rule.put("building_id",obj.optString("building_id"));
                        rule.put("rule_title",obj.optString("rule_title"));
                        rule.put("rule_description",obj.optString("rule_description"));
                        rule.put("date_create",obj.optString("date_create"));
                        rule.put("date_update",obj.optString("date_update"));
                        rule.put("status",obj.optString("status"));
                        rule_list.add(rule);
                    }
                    rule_list_adapter=new Rule_list_adapter(Rules_Activity.this,rule_list);
                    rules_activity_listview.setAdapter(rule_list_adapter);
                    Toast.makeText(Rules_Activity.this, json.optString("msg"),Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {

            }
        }
    }

    class delete_rule extends AsyncTask<String, String, String>
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
            pDialog = new ProgressDialog(Rules_Activity.this);
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
                params1.add(new BasicNameValuePair("rule_id",rule_id));
                HttpLoader loader=new HttpLoader();
                response = loader.loadDataByPost(delete_rule_url,params1);
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
                    Toast.makeText(Rules_Activity.this, json.optString("msg"),Toast.LENGTH_LONG).show();
                    new get_rule_list().execute();
                }
            }
            catch (Exception e)
            {

            }
        }
    }

}
