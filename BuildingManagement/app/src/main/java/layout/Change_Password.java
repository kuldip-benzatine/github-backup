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
import android.widget.Toast;

import com.sk.buildingmanagement.HttpLoader;
import com.sk.buildingmanagement.R;
import com.sk.buildingmanagement.util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Change_Password extends Fragment implements View.OnClickListener {


    View root_view;
    Context mcontext;
    EditText change_old_pass,change_new_pass,change_con_pass;
    Button change_pass_btn;
    private OnFragmentInteractionListener mListener;
    String update_url="http://benzatineinfotech.com/webservice/build_mgnt/admin_change_password.php";
    String old_pass,new_pass,con_pass;

    public Change_Password()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Change_Password newInstance(String param1, String param2) {
        Change_Password fragment = new Change_Password();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        root_view =inflater.inflate(R.layout.fragment_change__password, container, false);
        mcontext = root_view.getContext();
        initialize_controll();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Change Password");

        initialize_controll_action();
        return root_view;
    }
    public void initialize_controll()
    {
        change_old_pass = (EditText) root_view.findViewById(R.id.change_old_pass);
        change_new_pass = (EditText) root_view.findViewById(R.id.change_new_pass);
        change_con_pass = (EditText) root_view.findViewById(R.id.change_con_pass);
        change_pass_btn = (Button) root_view.findViewById(R.id.change_pass_btn);
    }
    public void initialize_controll_action()
    {
        change_pass_btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.change_pass_btn)
        {
            if(util.isOnline(getContext()))
            {
                old_pass=change_old_pass.getText().toString();
                new_pass=change_new_pass.getText().toString();
                con_pass=change_con_pass.getText().toString();
                if(new_pass.equals(con_pass))
                {
                    new change_password().execute();
                }
                else
                {
                    Toast.makeText(getContext(),R.string.check_pass_confirmation,Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(getContext(),R.string.no_internet,Toast.LENGTH_LONG).show();
            }
        }

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }
    class change_password extends AsyncTask<String, String, String>
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
            pDialog = new ProgressDialog(getActivity());
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
                SharedPreferences prefs = getActivity().getSharedPreferences("Login_detail_pref",Context.MODE_PRIVATE);
                params1.add(new BasicNameValuePair("id",prefs.getString("id",null)));
                params1.add(new BasicNameValuePair("device_id",prefs.getString("device_id",null)));
                params1.add(new BasicNameValuePair("old_password",old_pass));
                params1.add(new BasicNameValuePair("password",new_pass));
                params1.add(new BasicNameValuePair("con_password",con_pass));
                HttpLoader loader=new HttpLoader();
                    response = loader.loadDataByPost(update_url,params1);


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
            SharedPreferences.Editor editor = getActivity().getSharedPreferences("Login_detail_pref",Context.MODE_PRIVATE).edit();
            try
            {

                JSONObject json = new JSONObject(response);
                success = json.getInt("status_code");
                if (success == 200)
                {
                    Toast.makeText(getContext(),json.optString("msg"),Toast.LENGTH_LONG).show();
                }
                if(success == 605)
                {
                    Toast.makeText(getActivity(),json.optString("msg"),Toast.LENGTH_LONG).show();
                }
                if(success == 606)
                {
                    Toast.makeText(getActivity(),json.optString("msg"),Toast.LENGTH_LONG).show();
                }
                if(success == 607)
                {
                    Toast.makeText(getActivity(),json.optString("msg"),Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {

            }
        }
    }


    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
