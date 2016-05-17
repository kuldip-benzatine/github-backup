package com.sk.buildingmanagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;

public class Complain_details_activity extends AppCompatActivity
{
    TextView complain_detail_title,complain_detail_from_owner,complain_detail_date,complain_detail_description;
    GridView compalin_detail_img_gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complain_details_activity);
        getSupportActionBar().setSubtitle("Complain Deatils");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void initialize_controll()
    {
        complain_detail_title=(TextView) findViewById(R.id.complain_detail_title);
        complain_detail_from_owner=(TextView)findViewById(R.id.complain_detail_from_owner);
        complain_detail_date=(TextView)findViewById(R.id.complain_detail_date);
        complain_detail_description=(TextView)findViewById(R.id.complain_detail_description);
        compalin_detail_img_gridview=(GridView)findViewById(R.id.complain_detail_img_gridview);
    }
    public void initialize_controll_action()
    {

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
