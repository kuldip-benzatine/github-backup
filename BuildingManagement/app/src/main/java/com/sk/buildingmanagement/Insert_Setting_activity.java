package com.sk.buildingmanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Insert_Setting_activity extends AppCompatActivity implements View.OnClickListener
{
    RadioGroup setting_buil_type,maintanance_info_type;
    RadioButton setting_sociaty_chb,setting_bussiness_chb;
    RadioButton setting_per_sq_flat,setting_per_sq_ft;
    EditText maintanance_amount;
    TextView setting_maintanance_amount;
    Button setting_save_btn;
    Date date;
    String build_type=null,maintanance_type=null,amount,Id,building_id,device_id;
    String Update_date;
    String insert_setting="http://benzatineinfotech.com/webservice/build_mgnt/admin_insert_setting.php";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert__setting_activity);
        getSupportActionBar().setSubtitle("Setting");

        SharedPreferences prefs = getSharedPreferences("Login_detail_pref",MODE_PRIVATE);
        Id=prefs.getString("id",null);
        building_id=prefs.getString("Building_id",null);
        device_id=prefs.getString("device_id",null);
        initialize_controll();
        date = new Date();

        initialize_controll_action();
    }
    public void initialize_controll()
    {
        setting_buil_type = (RadioGroup) findViewById(R.id.setting_buil_type_radio_group);
        maintanance_info_type=(RadioGroup)findViewById(R.id.setting_mantanance_info_radio_group);
        maintanance_amount = (EditText)findViewById(R.id.setting_maintanance_amount);
        setting_save_btn = (Button) findViewById(R.id.setting_save_btn);
        setting_sociaty_chb= (RadioButton)findViewById(R.id.setting_society_radio);
        setting_bussiness_chb= (RadioButton)findViewById(R.id.setting_radio_bussiness_redio);
        setting_per_sq_flat= (RadioButton) findViewById(R.id.setting_per_flat_radio);
        setting_per_sq_ft= (RadioButton) findViewById(R.id.setting_per_sq_fit_radio);
        setting_maintanance_amount = (TextView) findViewById(R.id.setting_maintanance_amount_txt);
    }
    public void initialize_controll_action()
    {
        setting_save_btn.setOnClickListener(this);
        maintanance_info_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // checkedId is the RadioButton selected
                RadioButton rb=(RadioButton)findViewById(checkedId);
                setting_maintanance_amount.setText("Enter maintanance Amount " + rb.getText());
                //Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.setting_save_btn)
        {
            int a =setting_buil_type.getCheckedRadioButtonId();
            int b =maintanance_info_type.getCheckedRadioButtonId();
            amount = maintanance_amount.getText().toString();
            if(a==R.id.setting_society_radio)
            {
                build_type="1";
            }
            else
            {
                build_type="2";
            }
            if(b==R.id.setting_per_flat_radio)
            {
                maintanance_type="1";
            }
            else
            {
                maintanance_type="2";
            }
            Update_date=date.getDate()+""+date.getTime();
            new insert_setting().execute();

        }

    }
    json_parser jsonParser = new json_parser();
    class insert_setting extends AsyncTask<String, String, String>
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
            pDialog = new ProgressDialog(Insert_Setting_activity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params)
        {
            // Check for success tag
            try
            {
                // Building Parameters
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("building_id",building_id));
                params1.add(new BasicNameValuePair("device_id",device_id));
                params1.add(new BasicNameValuePair("building_type",build_type));
                params1.add(new BasicNameValuePair("maintain_type",maintanance_type));
                params1.add(new BasicNameValuePair("amount",amount));
                params1.add(new BasicNameValuePair("id",Id));

                // params1.add(new BasicNameValuePair("password",login_activity_password_txt.getText().toString()));
                json= jsonParser.makeHttpRequest(insert_setting,"POST", params1);
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
            //SharedPreferences.Editor editor = getSharedPreferences("Login_detail_pref", MODE_PRIVATE).edit();
            try
            {
                success = json.getInt("status_code");
                if (success == 200)
                {
                    Toast.makeText(Insert_Setting_activity.this,json.optString("msg"),Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Insert_Setting_activity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(success==503)
                {
                    Toast.makeText(Insert_Setting_activity.this,json.optString("msg"),Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Insert_Setting_activity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            catch (Exception e)
            {
                Log.e("Error","Exception : "+e.getMessage());
            }
        }
    }
}
