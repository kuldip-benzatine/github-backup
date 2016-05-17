package layout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sk.buildingmanagement.R;
import com.sk.buildingmanagement.json_parser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Setting_fragment extends Fragment implements Button.OnClickListener
{

    private OnFragmentInteractionListener mListener;
    View root_view;
    Context mcontext;
    RadioGroup setting_buil_type,maintanance_info_type;
    RadioButton setting_sociaty_chb,setting_bussiness_chb;
    RadioButton setting_per_sq_flat,setting_per_sq_ft;

    EditText maintanance_amount;
    TextView setting_maintanance_amount;
    Button setting_save_btn;

    Date date;
    String build_type=null,maintanance_type=null,amount,building_id,setting_id;

    String Update_date;

    String setting_url="http://benzatineinfotech.com/webservice/build_mgnt/admin_get_setting.php";
    String update_url="http://benzatineinfotech.com/webservice/build_mgnt/admin_update_setting.php";
    String id,device_id;
    public Setting_fragment()
    {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        root_view =  inflater.inflate(R.layout.setting_fragment, container, false);
        mcontext=root_view.getContext();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Setting");

        SharedPreferences prefs = getActivity().getSharedPreferences("Login_detail_pref", Context.MODE_PRIVATE);
        building_id=prefs.getString("Building_id",null);
        id=prefs.getString("id",null);
        device_id=prefs.getString("device_id",null);

        initialize_controll();
        date = new Date();
        // Inflate the layout for this fragment

        new get_Setting().execute();


//        date.getTime();
//        date.getDate();
        initialize_controll_action();
        return root_view;
    }
    public void initialize_controll()
    {
        setting_buil_type = (RadioGroup) root_view.findViewById(R.id.setting_buil_type_radio_group);
        maintanance_info_type=(RadioGroup) root_view.findViewById(R.id.setting_mantanance_info_radio_group);
        maintanance_amount = (EditText) root_view.findViewById(R.id.setting_maintanance_amount);
        setting_save_btn = (Button) root_view.findViewById(R.id.setting_save_btn);
        setting_sociaty_chb= (RadioButton) root_view.findViewById(R.id.setting_society_radio);
        setting_bussiness_chb= (RadioButton) root_view.findViewById(R.id.setting_radio_bussiness_redio);
        setting_per_sq_flat= (RadioButton) root_view.findViewById(R.id.setting_per_flat_radio);
        setting_per_sq_ft= (RadioButton) root_view.findViewById(R.id.setting_per_sq_fit_radio);
        setting_maintanance_amount = (TextView) root_view.findViewById(R.id.setting_maintanance_amount_txt);
    }
    public void initialize_controll_action()
    {
        setting_save_btn.setOnClickListener(this);
        maintanance_info_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // checkedId is the RadioButton selected
                RadioButton rb=(RadioButton)root_view.findViewById(checkedId);
                setting_maintanance_amount.setText("Maintanance Amount "+rb.getText());
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
            //    build_type="1";
            }
            else
            {
             //   build_type="2";
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
            new Update_setting().execute();

        }

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()+ " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //get setting data
    json_parser jsonParser = new json_parser();
    class get_Setting extends AsyncTask<String, String, String>
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
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
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

                params1.add(new BasicNameValuePair("id",id));
                params1.add(new BasicNameValuePair("device_id",device_id));
                params1.add(new BasicNameValuePair("building_id",building_id));
                //params1.add(new BasicNameValuePair("setting_id",setting_id));

               // params1.add(new BasicNameValuePair("password",login_activity_password_txt.getText().toString()));
                json= jsonParser.makeHttpRequest(setting_url, "POST", params1);
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
                    JSONObject jData=json.optJSONObject("data");
                    //Id=jData.optString("id");
                    building_id=jData.optString("building_id");
                    build_type=jData.optString("building_type");
                    maintanance_type=jData.optString("maintain_type");
                    amount=jData.optString("amount");
                    setting_id=jData.optString("setting_id");
                    //date_update=jData.optString("date_updated");

                    if(build_type.equals("1"))
                    {
                        setting_sociaty_chb.setChecked(true);
                        setting_bussiness_chb.setChecked(false);
                    }
                    else
                    {
                        setting_bussiness_chb.setChecked(true);
                        setting_sociaty_chb.setChecked(false);
                    }
                    if(maintanance_type.equals("1"))
                    {
                        setting_per_sq_flat.setChecked(true);
                        setting_per_sq_ft.setChecked(false);
                        setting_maintanance_amount.setText("Maintanance Amount Per Flat");

                    }
                    else
                    {
                        setting_per_sq_ft.setChecked(true);
                        setting_per_sq_flat.setChecked(false);
                        setting_maintanance_amount.setText("Maintanance Amount per square_ft");
                    }
                    maintanance_amount.setText(amount);
                }
                else if(success==602)
                {
                    Toast.makeText(getActivity(),json.optString("msg"),Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {

            }
        }
    }
    // update setting data
    class Update_setting extends AsyncTask<String, String, String>
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
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
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
                params1.add(new BasicNameValuePair("id",id));
                params1.add(new BasicNameValuePair("building_id",building_id));
                params1.add(new BasicNameValuePair("device_id",device_id));
                params1.add(new BasicNameValuePair("setting_id",setting_id));

               // params1.add(new BasicNameValuePair("building_type",build_type));
                params1.add(new BasicNameValuePair("maintain_type",maintanance_type));
                params1.add(new BasicNameValuePair("amount",amount));


                // params1.add(new BasicNameValuePair("password",login_activity_password_txt.getText().toString()));
                json= jsonParser.makeHttpRequest(update_url, "POST", params1);
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
                    Toast.makeText(getActivity(),json.optString("msg"),Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {

            }
        }
    }
}
