package com.sk.buildingmanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

public class add_complain_box_activity extends AppCompatActivity implements View.OnClickListener {
    EditText add_complain_title,add_complain_owner_name,add_complain_description;
    CheckBox add_complain_all_owner_chb;
    GridView add_complain_upload_image_gridview;
    Button add_complain_send_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_complain_box_activity);
        getSupportActionBar().setSubtitle("Add Complain");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialize_controll();

        initialize_cotroll_action();
    }
    public void initialize_controll()
    {
        add_complain_title=(EditText)findViewById(R.id.add_complain_title);
        add_complain_owner_name=(EditText)findViewById(R.id.add_complain_owner_name);
        add_complain_description=(EditText)findViewById(R.id.add_complain_description);
        add_complain_all_owner_chb=(CheckBox)findViewById(R.id.add_complain_all_owner_chb);
        add_complain_upload_image_gridview=(GridView)findViewById(R.id.add_complain_upload_image_gridview);
        add_complain_send_btn=(Button)findViewById(R.id.add_complain_send_btn);
    }

    public void initialize_cotroll_action()
    {
        add_complain_send_btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.add_complain_send_btn)
        {
            Toast.makeText(getBaseContext(),"Your complain Send successfully msg box",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
