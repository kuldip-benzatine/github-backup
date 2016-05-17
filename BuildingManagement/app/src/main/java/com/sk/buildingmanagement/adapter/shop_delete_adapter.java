package com.sk.buildingmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.sk.buildingmanagement.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ISKM on 05/14/2016.
 */
public class shop_delete_adapter extends BaseAdapter
{
    Context context;
    ArrayList<HashMap<String, String>> shop_list;// =new ArrayList<HashMap<String, String>>();

    //Shop_info_interface shop_info_interface;
    private static LayoutInflater inflater = null;
    Holder holder = new Holder();
    ArrayList<HashMap<String,String>> delete_shop_list = new ArrayList<HashMap<String,String>>();
    ArrayList<Boolean> boolean_chb_list=new ArrayList<Boolean>();
    int deleted_shop_count;
    public shop_delete_adapter(Context mainActivity, ArrayList<HashMap<String, String>> list1 , Integer deletedshop)
    {
        shop_list = list1;
        for(int i=0;i<shop_list.size();i++)
        {
            boolean_chb_list.add(false);
        }
        deleted_shop_count=deletedshop;
        //shop_info_interface = (Shop_info_interface) mainActivity;
        context = mainActivity;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return shop_list.size();
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
        TextView delete_shop_layout_shopno_edit_txt;
        TextView delete_shop_layout_sq_foot_edit_txt;
        CheckBox delete_shop_layout_chb;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        //final Holder holder = new Holder();
        final View rowView;

        rowView = inflater.inflate(R.layout.delete_shop_layout, null);
        // initialize controll
        holder.delete_shop_layout_shopno_edit_txt=(TextView)  rowView.findViewById(R.id.delete_shop_layout_shopno_edit_txt);
        holder.delete_shop_layout_sq_foot_edit_txt=(TextView)rowView.findViewById(R.id.delete_shop_layout_sq_foot_edit_txt);
        holder.delete_shop_layout_chb = (CheckBox) rowView.findViewById(R.id.delete_shop_layout_chb);
        holder.delete_shop_layout_shopno_edit_txt.setText(shop_list.get(position).get("flat_no"));
        holder.delete_shop_layout_sq_foot_edit_txt.setText(shop_list.get(position).get("sq_feet"));
        holder.delete_shop_layout_chb.setChecked(boolean_chb_list.get(position));

//        if (shop_list.get(position).get("status").equals("1"))
//        {
//            holder.delete_shop_layout_chb.setChecked(true);
//        }
//        else
//        {
//            holder.delete_shop_layout_chb.setChecked(false);
//        }

        holder.delete_shop_layout_chb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(deleted_shop_count>delete_shop_list.size()) {
                    if (isChecked) {
                        delete_shop_list.add(shop_list.get(position));
                        boolean_chb_list.set(position, true);
                    } else {
                        delete_shop_list.remove(shop_list.get(position));
                        boolean_chb_list.set(position, false);
                    }
                }
                else
                {
                    buttonView.setChecked(false);
                }
            }
        });

        return rowView;
    }
    public ArrayList<HashMap<String,String>> get_deleted_list()
    {
        return delete_shop_list;
    }
}