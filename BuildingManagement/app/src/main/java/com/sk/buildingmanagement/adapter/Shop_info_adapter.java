package com.sk.buildingmanagement.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.sk.buildingmanagement.R;
import com.sk.buildingmanagement.lisners.Shop_info_interface;
import com.sk.buildingmanagement.util;

import java.util.ArrayList;
import java.util.HashMap;


public class Shop_info_adapter extends BaseAdapter
{
    Context context;
    ArrayList<HashMap<String,String>> shop_list;// =new ArrayList<HashMap<String, String>>();

    Shop_info_interface shop_info_interface;
    private static LayoutInflater inflater = null;
    String shop_no="",sq_foot="";
    Boolean flag;
    Holder holder=new Holder();
    String status;
    String flat_id;

    public Shop_info_adapter(Context mainActivity,ArrayList<HashMap<String,String>> list1)
    {

        shop_list=list1;
        shop_info_interface=(Shop_info_interface) mainActivity;
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
        TextView shop_info_adapter_layout_sq_foot_edit_txt;
        Button shop_info_adapter_layout_edit_btn;
        Switch shop_info_adapter_layout_switch;
        TextView shop_info_adapter_layout_shop_no;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        //final Holder holder = new Holder();
        final View rowView;

        rowView = inflater.inflate(R.layout.shop_info_adapter_layout, null);
        // initialize controll
        holder.shop_info_adapter_layout_sq_foot_edit_txt = (TextView) rowView.findViewById(R.id.shop_info_adapter_layout_sq_foot_edit_txt);
        //holder.shop_info_adapter_layout_edit_btn = (Button) rowView.findViewById(R.id.shop_info_adapter_layout_edit_btn);
        holder.shop_info_adapter_layout_switch = (Switch) rowView.findViewById(R.id.shop_info_adapter_layout_switch);
        holder.shop_info_adapter_layout_shop_no=(TextView)rowView.findViewById(R.id.shop_info_adapter_layout_shopno_edit_txt);

        holder.shop_info_adapter_layout_shop_no.setText(shop_list.get(position).get("flat_no"));
        holder.shop_info_adapter_layout_sq_foot_edit_txt.setText(shop_list.get(position).get("sq_feet"));

        if(shop_list.get(position).get("status").equals("1"))
        {
            holder.shop_info_adapter_layout_switch.setChecked(true);
        }
        else
        {
            holder.shop_info_adapter_layout_switch.setChecked(false);
        }
        // shop no text view click event
        holder.shop_info_adapter_layout_shop_no.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                flag=true;
                shop_no=shop_list.get(position).get("flat_no");
                sq_foot=shop_list.get(position).get("sq_feet");
                status=shop_list.get(position).get("status");
                flat_id=shop_list.get(position).get("flat_id");
                showDialog();
            }
        });
        holder.shop_info_adapter_layout_sq_foot_edit_txt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                flag=false;
                shop_no=shop_list.get(position).get("flat_no");
                sq_foot=shop_list.get(position).get("sq_feet");
                status=shop_list.get(position).get("status");
                flat_id=shop_list.get(position).get("flat_id");
                showDialog();
            }
        });

        holder.shop_info_adapter_layout_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    shop_info_interface.edit_shop(shop_list.get(position).get("flat_id"),shop_list.get(position).get("flat_no"),shop_list.get(position).get("sq_feet"),"1");
                }
                else
                {
                    shop_info_interface.edit_shop(shop_list.get(position).get("flat_id"),shop_list.get(position).get("flat_no"),shop_list.get(position).get("sq_feet"),"0");
                }

            }
        });

        // set floor name
        return rowView;
    }

    // display dialog and get a value
    AlertDialog myDialog=null;

    public void  showDialog()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.shop_info_add_sq_foot_dialog_layout, null);

        final EditText shop_info_add_sq_foot_dialog_edit_txt;
        final EditText shop_info_dialog_shop_no;

        final Button shop_info_add_sq_foot_dialog_add_btn,shop_info_dialog_layout_cancel_btn;

        // shop no edit text id
        shop_info_dialog_shop_no=(EditText)view.findViewById(R.id.shop_info_dialog_shop_no);
        // sq foot edit text id
        shop_info_add_sq_foot_dialog_edit_txt=(EditText)view.findViewById(R.id.shop_info_add_sq_foot_dialog_edit_txt);
        // add button id
        shop_info_add_sq_foot_dialog_add_btn = (Button) view.findViewById(R.id.shop_info_add_sq_foot_dialog_add_btn);
        shop_info_dialog_shop_no.setText(shop_no);
        shop_info_add_sq_foot_dialog_edit_txt.setText(sq_foot);

        if(flag)
        {
            shop_info_add_sq_foot_dialog_edit_txt.setVisibility(View.INVISIBLE);
        }
        else
        {
            shop_info_dialog_shop_no.setVisibility(View.INVISIBLE);
        }
        // cancel button id and that click event
        shop_info_dialog_layout_cancel_btn= (Button) view.findViewById(R.id.shop_info_dialog_layout_cancel_btn);
        shop_info_dialog_layout_cancel_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDialog.dismiss();
            }
        });
        // add button click event
        shop_info_add_sq_foot_dialog_add_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                    // update floor detail
                    if (util.isOnline(context))
                    {
                        shop_info_interface.edit_shop(flat_id,shop_info_dialog_shop_no.getText().toString(),shop_info_add_sq_foot_dialog_edit_txt.getText().toString(),status);
                    }
                    else
                    {
                        Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
                    }
                myDialog.dismiss();
            }
        });

        builder.setView(view);

        builder.setCancelable(false);
        myDialog=builder.create();
        myDialog.show();
    }
}
