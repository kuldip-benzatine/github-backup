package com.sk.buildingmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.sk.buildingmanagement.adapter.Complain_box_list_adapter;

public class Complain_Box_Activity extends AppCompatActivity
{
    ListView complain_box_activity_listview;
    Complain_box_list_adapter complain_box_list_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complain__box_activity);
        getSupportActionBar().setSubtitle("Complain Box");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        complain_box_list_adapter=new Complain_box_list_adapter(this);
    }

    public void initialize_controll()
    {
        complain_box_activity_listview= (ListView) findViewById(R.id.complain_box_activity_listview);
    }
    public void initialize_controll_action()
    {}

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        if(item.getItemId()==R.id.add_complain)
        {

            Intent intent = new Intent(Complain_Box_Activity.this,add_complain_box_activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.complain_box_menu, menu);
        return true;
    }

}
