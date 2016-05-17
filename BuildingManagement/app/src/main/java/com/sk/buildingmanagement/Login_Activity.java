package com.sk.buildingmanagement;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.sk.buildingmanagement.gcm.GCMClientManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import layout.Other_Functions;

public class Login_Activity extends AppCompatActivity implements Button.OnClickListener
{
    private Other_Functions other_functions=new Other_Functions();
    AutoCompleteTextView login_activity_email_txt,login_activity_password_txt;
    Button login_activity_login_btn,forget_password;

    String login_url="http://benzatineinfotech.com/webservice/build_mgnt/admin_login.php";
    String Forget_password_url="http://benzatineinfotech.com/webservice/build_mgnt/forgot_password.php";
    String id;
    String Building_id;
    String Building_name;
    String user_name;
    String first_name;
    String last_name;
    String email="";
    String mobile_no;
    String profile_img;
    String address;
    String gender;
    String birth_date;
    String is_first_time="0";
    String password="";
    boolean flag=false;
    String PROJECT_NUMBER="469619362993";
    String device_token="";
    String device_type="2";
    String deviceId;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        other_functions.top_window_color(this);
        initialize_controll();
        getSupportActionBar().setTitle("Building Management");
        getSupportActionBar().setSubtitle("Login");
        final SharedPreferences prefs = getSharedPreferences("Login_detail_pref", Context.MODE_PRIVATE);

        GCMClientManager pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler()
        {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration)
            {

                Log.d("Registration id", registrationId);
                if(isNewRegistration)
                {
                    prefs.edit().putString("gcm_id",registrationId);
                }
                device_token=registrationId;
            }

            @Override
            public void onFailure(String ex)
            {
                super.onFailure(ex);
            }
        });

//        TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//        deviceId = tManager.getDeviceId();

        // GET DEVICE ID
        deviceId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

       // is_first_time = prefs.getString("kuldeep","1");
        if(!prefs.getAll().isEmpty())
        {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        initialize_controll_action();
    }
    public void initialize_controll()
    {
        login_activity_email_txt=(AutoCompleteTextView)findViewById(R.id.login_activity_email_txt);
        login_activity_password_txt=(AutoCompleteTextView)findViewById(R.id.login_activity_password_txt);
        login_activity_login_btn=(Button)findViewById(R.id.login_activity_btn);
        forget_password=(Button) findViewById(R.id.Forget_password);

    }
    public void initialize_controll_action()
    {
        login_activity_login_btn.setOnClickListener(this);
        forget_password.setOnClickListener(this);
    }
    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.login_activity_btn)
        {
            if(util.isOnline(this))
            {

                    email = login_activity_email_txt.getText().toString();
                    password = login_activity_password_txt.getText().toString();
                if(email.equals("")||password.equals(""))
                {
                    Toast.makeText(this,R.string.email_password_is_null,Toast.LENGTH_LONG).show();
                }
                else {
                    new Login_task().execute();
                }
            }
            else
            {
                Toast.makeText(this,R.string.no_internet,Toast.LENGTH_LONG).show();
            }
        }
        if(v.getId()==R.id.Forget_password)
        {

                if (util.isOnline(this))
                {
                    email = login_activity_email_txt.getText().toString();
                    if(email.equals(""))
                    {
                        Toast.makeText(this,R.string.error_invalid_email,Toast.LENGTH_LONG).show();
                    }
                    else {
                        new Forget_password().execute();
                    }
                }
                else
                {
                    Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show();
                }

        }
    }

    json_parser jsonParser = new json_parser();
    class Login_task extends AsyncTask<String, String, String>
    {
        ProgressDialog pDialog;
        JSONObject json;
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login_Activity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        /**
         * Getting details in background thread
         * */
        protected String doInBackground(String... params)
        {
            // Check for success tag
            try
            {
                // Building Parameters
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("email",email));
                params1.add(new BasicNameValuePair("password",password));
                params1.add(new BasicNameValuePair("device_id",deviceId));
                params1.add(new BasicNameValuePair("device_token",device_token));
                params1.add(new BasicNameValuePair("device_type",device_type));

                json= jsonParser.makeHttpRequest(login_url, "POST", params1);
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
            SharedPreferences.Editor editor = getSharedPreferences("Login_detail_pref", MODE_PRIVATE).edit();
            try
            {
                success = json.getInt("status_code");
                if (success == 200)
                {
                    JSONObject jData=json.optJSONObject("data");
                    editor.putString("id",jData.optString("id"));
                    editor.putString("Building_id",jData.optString("building_id"));
                    editor.putString("device_id",jData.optString("device_id"));
                    editor.putString("device_token",jData.optString("device_token"));

                    editor.putString("Building_name",jData.optString("building_name"));
                    editor.putString("user_name",jData.optString("username"));
                    editor.putString("first_name",jData.optString("firstname"));
                    editor.putString("last_name",jData.optString("lastname"));
                    editor.putString("email",jData.optString("email"));
                    editor.putString("mobile_no",jData.optString("mobileno"));
                    editor.putString("profile_img",jData.optString("profileimage"));
                    editor.putString("address",jData.optString("address"));
                    editor.putString("gender",jData.optString("gender"));
                    editor.putString("birth_date",util.date_format(jData.optString("birthdate")));
                    editor.putString("is_first_time",jData.optString("is_firsttime"));


                    if(jData.optString("is_firsttime").equals("1"))
                    {
                     //   if( is_first_time.equals("1")){
                      //      editor.putString("kuldeep",jData.optString("is_firsttime")).apply();

                      //  }
                        Intent intent = new Intent(Login_Activity.this,Insert_Setting_activity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    editor.commit();
                    finish();
                }
                else if(success==201)
                {
                    Toast.makeText(Login_Activity.this,json.optString("msg"),Toast.LENGTH_LONG).show();
                }
                else if(success==204)
                {
                    Toast.makeText(Login_Activity.this,json.optString("msg"),Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
                Log.e("ERROR",e.getMessage());
            }
        }
    }
    class Forget_password extends AsyncTask<String, String, String>
    {
        ProgressDialog pDialog;

        JSONObject json;
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login_Activity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params)
        {
            // Check for success tag

            try
            {
                // Building Parameters
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("email",email));
                //params1.add(new BasicNameValuePair("password",password));
                json= jsonParser.makeHttpRequest(Forget_password_url, "POST", params1);
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
                success = json.getInt("status_code");
                if (success == 200)
                {
                    JSONObject jData=json.optJSONObject("data");
                    //String Id=jData.optString("id");
                    Toast.makeText(Login_Activity.this,json.optString("msg"),Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {

            }
        }
    }

}
