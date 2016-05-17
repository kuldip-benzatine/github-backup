package com.sk.buildingmanagement.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sk.buildingmanagement.R;
import com.sk.buildingmanagement.lisners.Rules_interface;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ISKM on 05/16/2016.
 */
public class Rule_list_adapter extends BaseAdapter
{
    View root_view;
    Context mcontext;
    Holder holder=new Holder();
    ArrayList<HashMap<String,String>> rule_list;
    private static LayoutInflater inflater;
    Rules_interface rules_interface;
    int location;
    public Rule_list_adapter(Context context, ArrayList<HashMap<String,String>> list)
    {
        mcontext=context;
        rules_interface=(Rules_interface)context;
        rule_list=list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount()
    {
        return rule_list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }
    public class Holder
    {
        TextView rules_list_layout_rules_title;
        ImageButton rules_list_layout_delete_img_btn;
        ImageButton rules_list_layout_edit_img_btn;
        TextView rules_list_layout_rules_description;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        root_view=convertView;
        root_view = inflater.inflate(R.layout.rules_list_layout, null);

        holder.rules_list_layout_rules_title=(TextView)root_view.findViewById(R.id.rules_list_layout_rules_title);
        holder.rules_list_layout_rules_description=(TextView)root_view.findViewById(R.id.rules_list_layout_rules_description);
        holder.rules_list_layout_delete_img_btn=(ImageButton)root_view.findViewById(R.id.rules_list_layout_delete_img_btn);
        holder.rules_list_layout_edit_img_btn=(ImageButton)root_view.findViewById(R.id.rules_list_layout_edit_img_btn);

        holder.rules_list_layout_rules_title.setText(rule_list.get(position).get("rule_title"));
        holder.rules_list_layout_rules_description.setText(rule_list.get(position).get("rule_description"));

        holder.rules_list_layout_edit_img_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                location=position;
                edit_rules_dialog();
            }
        });

        holder.rules_list_layout_delete_img_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                location=position;
                delete_rule_dialog();
            }
        });

        return root_view;
    }
    AlertDialog mdialog=null;
    public void edit_rules_dialog()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle("Add Rules");
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View view = inflater.inflate(R.layout.rules_add_dialog_layout, null);
        final EditText rules_title=(EditText)view.findViewById(R.id.rules_add_dialog_layout_rules_title_edit_txt);
        final EditText rules_description=(EditText)view.findViewById(R.id.rules_add_dialog_layout_rules_description_edit_txt);

        rules_title.setText(rule_list.get(location).get("rule_title"));
        rules_description.setText(rule_list.get(location).get("rule_description"));

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(rules_title.equals("")||rules_description.equals(""))
                {
                    Toast.makeText(mcontext,R.string.add_event_check_data_isempty,Toast.LENGTH_LONG).show();
                }
                else
                {
                    rules_interface.edit_rule(rule_list.get(location).get("rule_id"), rules_title.getText().toString(), rules_description.getText().toString());
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                mdialog.dismiss();
            }
        });
        builder.setView(view);
        builder.setCancelable(false);
        mdialog=builder.create();
        mdialog.show();
    }
    public void delete_rule_dialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle("Are Delete this Rule");


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                rules_interface.delete_rule(rule_list.get(location).get("rule_id"));
                mdialog.dismiss();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                mdialog.dismiss();
            }
        });
        builder.setCancelable(false);
        mdialog=builder.create();
        mdialog.show();
    }
}
