package com.sk.buildingmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import layout.Add_sub_admin;
import layout.Change_Password;
import layout.Home_fragment;
import layout.Other_Functions;
import layout.Setting_fragment;
import layout.Sub_admin_detail_fragment;
import layout.Update_profile_fragment;
import layout.display_Profile_fragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , display_Profile_fragment.OnFragmentInteractionListener
        , Change_Password.OnFragmentInteractionListener
        , Add_sub_admin.OnFragmentInteractionListener
        , Update_profile_fragment.OnFragmentInteractionListener
        , Sub_admin_detail_fragment.OnFragmentInteractionListener
        , Setting_fragment.OnFragmentInteractionListener
{
    DrawerLayout drawer;
    DrawerLayout drawer_right;
    Toolbar toolbar;
    public NavigationView navigationView;
    public NavigationView navigationView1;
    Context mcontext;
    String profile_img_url,user_name;
    ImageView profile_imgview,profile_imgview1;
    TextView left_drawer_profile_name,right_drawer_profile_name;
    private Other_Functions other_functions = new Other_Functions();
    String is_first_time=null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize_controll();

        mcontext=this;
        SharedPreferences prefs = getSharedPreferences("Login_detail_pref", 0);
        profile_img_url=prefs.getString("profile_img",null);
        user_name=prefs.getString("user_name",null);
    //    is_first_time=prefs.getString("is_first_time",null);

        String profile_img_str=profile_img_url;

        image_loader(profile_img_str,profile_imgview);
        image_loader(profile_img_str,profile_imgview1);
        left_drawer_profile_name.setText(user_name);
        right_drawer_profile_name.setText(user_name);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Building Management");
        // right side action drawer
        ActionBarDrawerToggle toggle1 = new ActionBarDrawerToggle(
                this, drawer_right, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // left side action drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        // Change top window
        other_functions.top_window_color(this);

        drawer.setDrawerListener(toggle);
        drawer.setDrawerListener(toggle1);
        toggle.syncState();
        toggle1.syncState();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new Home_fragment()).commit();
            //toolbar.setBackgroundResource(R.color.colorPrimary);
        initialize_controll_action();
    }
    public void image_loader(String uri,ImageView img)
    {
        //we are connected to a network
        String url = uri;
        ImageView loader_image_view=img;
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .showImageOnLoading(R.mipmap.ic_launcher).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this)
                .defaultDisplayImageOptions(options)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();
        ImageLoader.getInstance().init(config);
        //initialize image view
        //download and display image from url
        imageLoader.getInstance().displayImage(url, loader_image_view, options);
    }
    @Override
    public void onBackPressed()
    {
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
        if(drawer_right.isDrawerOpen(GravityCompat.END))
        {
            drawer_right.closeDrawer(GravityCompat.END);
        }
        else
        {
            super.onBackPressed();
        }
    }

    public void initialize_controll()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer_right = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView1 = (NavigationView) findViewById(R.id.nav_view1);
        profile_imgview = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        left_drawer_profile_name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.left_drawer_profile_name);
        profile_imgview1 = (ImageView) navigationView1.getHeaderView(0).findViewById(R.id.imageView2);
        right_drawer_profile_name = (TextView)navigationView1.getHeaderView(0).findViewById(R.id.right_drawer_profile_name);
    }
    public void initialize_controll_action()
    {
        navigationView.setNavigationItemSelectedListener(this);
        navigationView1.setNavigationItemSelectedListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            drawer.openDrawer(GravityCompat.END);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
    // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.HOME)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new Home_fragment()).commit();
        }
        else if (id == R.id.ADD_WINGS)
        {
            Intent intent = new Intent(this,wing_activity.class);
            startActivity(intent);
        }
        else if (id == R.id.MAINTANANCE_SHEET)
        {

        }
        else if (id == R.id.BUILDING_INFO)
        {

        }
        else if (id == R.id.EVENTS)
        {
            Intent intent = new Intent(this,Event_activity.class);
            startActivity(intent);
        }
        else if (id == R.id.SHOP_INFO)
        {

        }
        else if(id == R.id.OWNER_INFO)
        {

        }
        else if(id == R.id.COMPLAIN_BOX)
        {
            Intent intent = new Intent(this,Complain_Box_Activity.class);
            startActivity(intent);
        }
        else if(id == R.id.SERVICE_INFO)
        {

        }
        else if( id == R.id.RULES)
        {
            Intent intent = new Intent(this, Rules_Activity.class);
            startActivity(intent);
        }
        else if(id == R.id.MEMBER_INFO)
        {

        }
        else if(id == R.id.GENRAL_INFO)
        {

        }
        else if(id == R.id.HELP_SUPPORT)
        {

        }
        else if(id == R.id.SETTING)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new Setting_fragment()).commit();
        }
        else if(id == R.id.LOGOUT)
        {
            // clear  prefrences
            SharedPreferences.Editor editor = getSharedPreferences("Login_detail_pref",Context.MODE_PRIVATE).edit();
            editor.clear();
            editor.commit();

            Intent intent = new Intent(this,Login_Activity.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.PROFILE)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new display_Profile_fragment()).commit();
        }
        else if(id == R.id.ADD_SUBADMIN)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new Sub_admin_detail_fragment()).commit();
        }
        else if(id == R.id.CHANGE_PASSWORD)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new Change_Password()).commit();
        }
        else if(id == R.id.lOG_OUT)
        {
            // clear prefrences
            SharedPreferences.Editor editor = getSharedPreferences("Login_detail_pref",Context.MODE_PRIVATE).edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(this,Login_Activity.class);
            startActivity(intent);
            finish();
        }
        drawer.closeDrawer(GravityCompat.START);
        drawer_right.closeDrawer(GravityCompat.END);
        return true;
    }
    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }
}
