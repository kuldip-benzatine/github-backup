package com.sk.buildingmanagement.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sk.buildingmanagement.Add_event_activity;
import com.sk.buildingmanagement.HttpLoader;
import com.sk.buildingmanagement.R;
import com.sk.buildingmanagement.util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class get_event_list_adapter extends BaseAdapter
{
    Context context;
    ArrayList<HashMap<String,String>> wingList;
    String building_id,event_id;
    private static LayoutInflater inflater=null;

    public get_event_list_adapter(Context mainActivity, ArrayList<HashMap<String,String>> list)
    {

        wingList=list;
        context=mainActivity;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return wingList.size();
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position)
    {

        return position;
    }

    public class Holder
    {
        TextView event_list_layout_event_name,event_list_layout_event_venus,event_list_layout_time,event_list_layout_date;

        ImageButton event_list_layout_delete_btn;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        View rowView;




        rowView = inflater.inflate(R.layout.event_list_layout, null);

        holder.event_list_layout_event_name=(TextView) rowView.findViewById(R.id.event_list_layout_event_name);
        holder.event_list_layout_event_venus=(TextView) rowView.findViewById(R.id.event_list_layout_event_venus);
        holder.event_list_layout_time=(TextView) rowView.findViewById(R.id.event_list_layout_time);
        holder.event_list_layout_date=(TextView) rowView.findViewById(R.id.event_list_layout_date);

        holder.event_list_layout_delete_btn=(ImageButton)rowView.findViewById(R.id.event_list_layout_detele_btn);


        //holder.add_wing_detail_image_btn=(ImageButton) rowView.findViewById(R.id.add_wing_detail_image_btn);

        holder.event_list_layout_event_name.setText(wingList.get(position).get("eventname"));
        holder.event_list_layout_event_venus.setText(wingList.get(position).get("venus"));
        holder.event_list_layout_time.setText(util.time_format(wingList.get(position).get("starttime")));
        holder.event_list_layout_date.setText(util.date_format(wingList.get(position).get("starttime")));

        rowView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context,Add_event_activity.class);
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
                context.startActivity(intent);
            }
        });

        holder.event_list_layout_delete_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new delete_event().execute();
                building_id=wingList.get(position).get("building_id");
                event_id=wingList.get(position).get("event_id");
                wingList.remove(position);
                notifyDataSetChanged();
            }
        });
        return rowView;
    }
    String admin_delete_event="http://benzatineinfotech.com/webservice/build_mgnt/admin_delete_event.php";
    class delete_event extends AsyncTask<String, String, String>
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
                pDialog = new ProgressDialog(context);
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
                    SharedPreferences prefs=context.getSharedPreferences("Login_detail_pref",Context.MODE_PRIVATE);
                    List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                    params1.add(new BasicNameValuePair("id",prefs.getString("id",null)));
                    params1.add(new BasicNameValuePair("device_id",prefs.getString("device_id",null)));
                    params1.add(new BasicNameValuePair("building_id",building_id));
                    params1.add(new BasicNameValuePair("event_id",event_id));

                    HttpLoader loader=new HttpLoader();
                    response = loader.loadDataByPost(admin_delete_event,params1);

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
                        Toast.makeText(context, json.optString("msg"),Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e)
                {

                }
            }
        }

}