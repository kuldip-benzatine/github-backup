package com.sk.buildingmanagement.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sk.buildingmanagement.R;
import com.sk.buildingmanagement.Shop_info_activity;
import com.sk.buildingmanagement.lisners.ButtonClick;
import com.sk.buildingmanagement.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class wing_detail_adapter extends BaseAdapter
{
    Context context;
    ArrayList<HashMap<String,String>> floor_list;
    ArrayList<HashMap<String,String>> wingList;
    String building_id,wing_id;
    private static LayoutInflater inflater = null;
    Holder holder=new Holder();
    ButtonClick mButtonclick;
    int location;
    String new_floor_end_series="";
    ArrayList<Integer> old_floor_end_series1;

    public wing_detail_adapter(ButtonClick buttonClick,Context mainActivity,List<String> list,String building_id,String wing_id,ArrayList<HashMap<String,String>> list1)
    {
        mButtonclick=buttonClick;
        floor_list=list1;
        wingList = list1;

        context = mainActivity;
        this.building_id=building_id;
        this.wing_id=wing_id;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        TextView wing_detail_floor_name;
        ImageButton wing_detail_img_btn;
        TextView wing_detail_adapter_layout_floor_series;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        //final Holder holder = new Holder();
        final View rowView;

        rowView = inflater.inflate(R.layout.wing_detail_adapter_layout, null);
        // initialize controll
        holder.wing_detail_floor_name = (TextView) rowView.findViewById(R.id.wing_detail_floor_name);
        holder.wing_detail_img_btn = (ImageButton) rowView.findViewById(R.id.wing_detail_img_btn);

        holder.wing_detail_adapter_layout_floor_series=(TextView)rowView.findViewById(R.id.wing_detail_adapter_layout_floor_series);
        // set floor name
        holder.wing_detail_floor_name.setText(wingList.get(position).get("floor_name"));
        holder.wing_detail_adapter_layout_floor_series.setText(floor_list.get(position).get("series_start")+"to"+floor_list.get(position).get("series_end"));
        holder.wing_detail_img_btn.setImageResource(R.drawable.edit_button);

        rowView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context,Shop_info_activity.class);
                intent.putExtra("building_id",wingList.get(position).get("building_id"));
                intent.putExtra("wing_id",wingList.get(position).get("wing_id"));
                intent.putExtra("floor_id",wingList.get(position).get("floor_id"));
                context.startActivity(intent);
            }
        });
        // add floor onclick event
        holder.wing_detail_img_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                location=position;
                //old_floor_end_series=wingList.get(position).get("total_flat");
                add_floor_name=wingList.get(position).get("floor_name");
                showDialog();
            }
        });
        return rowView;
    }

    // display dialog and get a value
    AlertDialog myDialog=null;
    String add_floor_series_start,add_floor_series_end,add_floor_total_sq_feet,add_floor_name,add_floor_total_flat;
    String floor_id,add_floor_floor_name;
    public void  showDialog()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.edit_floor_layout, null);

        final EditText add_floor_layout_series_start_edit_txt,add_floor_series_end_edit_txt,add_floor_layout_total_sq_feet_edit_txt;
        final Button add_floor_layout_add_floor_btn,add_floor_layout_cancel_btn;
        final  EditText add_floor_layout_floor_name_edit_text;

        add_floor_layout_floor_name_edit_text=(EditText)view.findViewById(R.id.add_floor_layout_floor_name_edit_text);
        add_floor_layout_series_start_edit_txt = (EditText)view.findViewById(R.id.add_floor_layout_series_start_edit_txt);
        add_floor_series_end_edit_txt=(EditText)view.findViewById(R.id.add_floor_layout_series_end_edit_txt);
        add_floor_layout_total_sq_feet_edit_txt=(EditText)view.findViewById(R.id.add_floor_layout_total_sq_feet_edit_txt);
        add_floor_layout_add_floor_btn = (Button) view.findViewById(R.id.add_floor_layout_add_btn);

        add_floor_layout_cancel_btn= (Button) view.findViewById(R.id.add_floor_layout_cancel_btn);
        add_floor_layout_cancel_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDialog.dismiss();
            }
        });

        add_floor_layout_floor_name_edit_text.setText(wingList.get(location).get("floor_name"));
        add_floor_layout_series_start_edit_txt.setText(wingList.get(location).get("series_start"));
        add_floor_series_end_edit_txt.setText(wingList.get(location).get("series_end"));
        add_floor_layout_total_sq_feet_edit_txt.setText(wingList.get(location).get("total_sq_feet"));
        add_floor_layout_add_floor_btn.setText("Update");

        //floor_id=wingList.get(location).get("floor_id");

        add_floor_layout_add_floor_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(add_floor_layout_add_floor_btn.getText().toString().equals("Update"))
                {
                    // update floor detail
                    if (util.isOnline(context))
                    {
                        add_floor_series_start = add_floor_layout_series_start_edit_txt.getText().toString();
                        add_floor_series_end = add_floor_series_end_edit_txt.getText().toString();
                        add_floor_total_sq_feet = add_floor_layout_total_sq_feet_edit_txt.getText().toString();
                        add_floor_total_flat = String.valueOf((Integer.parseInt(add_floor_series_end) - Integer.parseInt(add_floor_series_start)) + 1);

                        add_floor_floor_name=add_floor_layout_floor_name_edit_text.getText().toString();
                        // check data is null or not and display msg
                        if (add_floor_floor_name.equals("") || add_floor_series_start.equals("") || add_floor_series_end.equals("") || add_floor_series_end.equals(""))
                        {
                            Toast.makeText(context, R.string.add_event_check_data_isempty, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            mButtonclick.update_onButtonClick(Integer.parseInt(wingList.get(location).get("series_end")),wingList.get(location).get("floor_id"),building_id, wing_id,add_floor_floor_name,add_floor_total_flat, add_floor_series_start, add_floor_series_end, add_floor_total_sq_feet);
                            myDialog.dismiss();
                        }
                    }
                    else
                    {
                        Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
                    }
                }
                // add floor code
//                else {
//                    if (util.isOnline(context))
//                    {
//                        add_floor_series_start = add_floor_layout_series_start_edit_txt.getText().toString();
//                        add_floor_series_end = add_floor_series_end_edit_txt.getText().toString();
//                        add_floor_total_sq_feet = add_floor_layout_total_sq_feet_edit_txt.getText().toString();
//                        add_floor_total_flat = String.valueOf((Integer.parseInt(add_floor_series_end) - Integer.parseInt(add_floor_series_start)) + 1);
//
//                        // check data is null or not and display msg
//                        if (add_floor_series_start.equals("") || add_floor_series_end.equals("") || add_floor_series_end.equals(""))
//                        {
//                            Toast.makeText(context, R.string.add_event_check_data_isempty, Toast.LENGTH_SHORT).show();
//                        }
//                        else
//                        {
//                            holder.wing_detail_img_btn.setImageResource(R.drawable.edit_button);
//                            mButtonclick.onButtonClick(building_id, wing_id, add_floor_name, add_floor_total_flat, add_floor_series_start, add_floor_series_end, add_floor_total_sq_feet);
//                            myDialog.dismiss();
//                        }
//                    } else {
//                        Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
//                    }
//                }
            }
        });

        builder.setView(view);

        builder.setCancelable(false);
        myDialog=builder.create();
        myDialog.show();
    }
}
