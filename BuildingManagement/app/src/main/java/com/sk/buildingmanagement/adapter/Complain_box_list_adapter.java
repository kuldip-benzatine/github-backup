package com.sk.buildingmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sk.buildingmanagement.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Complain_box_list_adapter extends BaseAdapter
{


    Context context;
    ArrayList<HashMap<String,String>> wingList;

    private static LayoutInflater inflater=null;
    public Complain_box_list_adapter(Context mainActivity)
    {

        //wingList=list;
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
        //TextView event_list_layout_event_name,event_list_layout_event_venus,event_list_layout_time,event_list_layout_date;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.wing_list_adapter_layout, null);

//        holder.event_list_layout_event_name=(TextView) rowView.findViewById(R.id.event_list_layout_event_name);
//        holder.event_list_layout_event_venus=(TextView) rowView.findViewById(R.id.event_list_layout_event_venus);
//        holder.event_list_layout_time=(TextView) rowView.findViewById(R.id.event_list_layout_time);
//        holder.event_list_layout_date=(TextView) rowView.findViewById(R.id.event_list_layout_date);


        //holder.add_wing_detail_image_btn=(ImageButton) rowView.findViewById(R.id.add_wing_detail_image_btn);

//        holder.event_list_layout_event_name.setText(wingList.get(position).get("eventname"));
//        holder.event_list_layout_event_venus.setText(wingList.get(position).get("venus"));
//        holder.event_list_layout_time.setText(wingList.get(position).get("starttime"));
//        holder.event_list_layout_date.setText(wingList.get(position).get("endtime"));

        // HashMap<String,String> selectedWing=wingList.get(position);
//        holder.add_wing_detail_image_btn.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//
//            }
//        });

        return rowView;
    }

}