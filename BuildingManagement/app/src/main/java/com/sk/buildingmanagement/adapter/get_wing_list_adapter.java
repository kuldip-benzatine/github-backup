package com.sk.buildingmanagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sk.buildingmanagement.R;
import com.sk.buildingmanagement.lisners.wing_activity_interface;
import com.sk.buildingmanagement.wing_detail_activity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ISKM on 05/06/2016.
 */
public class get_wing_list_adapter extends BaseAdapter
{

    String building_id;
    String wing_id;
    Context context;
    ArrayList<HashMap<String,String>> wingList;
    String wingname, total_floor, ground_floor, basement;

    wing_activity_interface wing_interface;

        private static LayoutInflater inflater=null;
        public get_wing_list_adapter(Context mainActivity,ArrayList<HashMap<String,String>> list)
        {
            wing_interface=(wing_activity_interface)mainActivity;
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
            TextView add_wing_name_txt,add_wing_floor;
            ImageButton add_wing_delete_image_btn;
            ImageButton add_wing_edit_image_btn;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.wing_list_adapter_layout, null);

            holder.add_wing_name_txt=(TextView) rowView.findViewById(R.id.add_wing_name_txt);
            holder.add_wing_floor=(TextView) rowView.findViewById(R.id.add_wing_floor);
            holder.add_wing_delete_image_btn=(ImageButton) rowView.findViewById(R.id.add_wing_delete_image_btn);
            holder.add_wing_edit_image_btn=(ImageButton)rowView.findViewById(R.id.add_wing_edit_image_btn);


            holder.add_wing_name_txt.setText(wingList.get(position).get("wingname"));
            holder.add_wing_floor.setText(wingList.get(position).get("total_floor"));
            holder.add_wing_delete_image_btn.setImageResource(R.drawable.ic_user_addwing_delete);

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,wing_detail_activity.class);
                    intent.putExtra("wing_id",wingList.get(position).get("wing_id"));
                    intent.putExtra("building_id",wingList.get(position).get("building_id"));
                    intent.putExtra("wingname",wingList.get(position).get("wingname"));
                    intent.putExtra("total_floor",wingList.get(position).get("total_floor"));
                    intent.putExtra("ground_floor",wingList.get(position).get("ground_floor"));
                    intent.putExtra("basement",wingList.get(position).get("basement"));
                    context.startActivity(intent);
                }
            });
           final HashMap<String,String> selectedWing=wingList.get(position);
            holder.add_wing_delete_image_btn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    wing_id=selectedWing.get("wing_id");
                    building_id=selectedWing.get("building_id");

                    wing_interface.delete_wing_call_back(building_id,wing_id);


                }
            });
        holder.add_wing_edit_image_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                building_id=selectedWing.get("building_id");
                wing_id=selectedWing.get("wing_id");
                wingname=selectedWing.get("wingname");
                total_floor=selectedWing.get("total_floor");
                ground_floor=selectedWing.get("ground_floor");
                basement=selectedWing.get("basement");
                wing_interface.edit_wing_call_back(building_id,wing_id,wingname,total_floor,ground_floor,basement);
            }
        });
            return rowView;
        }


}