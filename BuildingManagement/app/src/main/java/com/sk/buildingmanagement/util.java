package com.sk.buildingmanagement;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ISKM on 05/05/2016.
 */
public class util
{
    public static String date_format(String date)
    {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        DateFormat resultformat=new SimpleDateFormat("yyyy-MM-dd");
        Date dateform=new Date();

        try {
            dateform= format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultformat.format(dateform);
    }

    public static String time_format(String date)
    {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        DateFormat resultformat=new SimpleDateFormat("h:mm a");
        Date timeform=null;
        try
        {
            timeform= format.parse(date);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return resultformat.format(timeform);
    }
    public static String time_format_only(String date)
    {
        DateFormat format = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        DateFormat resultformat=new SimpleDateFormat("h:mm a");
        Date timeform=null;
        try
        {
            timeform= format.parse(date);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return resultformat.format(timeform);
    }
    public static void image_loader(String uri,ImageView img, Context context)
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
                context)
                .defaultDisplayImageOptions(options)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();
        ImageLoader.getInstance().init(config);
        //initialize image view
        //download and display image from url
        imageLoader.getInstance().displayImage(url, loader_image_view, options);
    }
    public static String full_address(Context mcontext,double latitude, double longitude)
    {
        Geocoder geocoder;
        List<Address> addresses=null;
        geocoder = new Geocoder(mcontext, Locale.getDefault());
        String address="",city="",state="",country="",postalCode="";
        StringBuilder result = new StringBuilder();
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            //String knownName = addresses.get(0).getFeatureName(); //
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        // check address is null and add string
        if (address == null)
        {
            postalCode="";

        }
        else
        {
            result.append(address);
        }
        // check city is empty and add city name
        if (city == null)
        {
            city="";

        }
        else
        {
            result.append(","+city);
        }
        // check state is null and add state
        if (state == null)
        {
            state="";

        }
        else
        {
            result.append(","+state);
        }
        // check country is null and add country
        if (country == null)
        {
            country="";

        }
        else
        {
            result.append(","+country);
        }
        // check postalcode null and add postal code
        if (postalCode == null)
        {
            postalCode="";

        }
        else
        {
            result.append(","+postalCode);
        }

        return result.toString();
        //return address+","+city+","+state+","+country+","+postalCode;
        //return addresses.get(0).getAddressLine(0)+","+addresses.get(0).getLocality()+","+addresses.get(0).getAdminArea()+","+addresses.get(0).getCountryName()+","+addresses.get(0).getPostalCode();
    }
    public static Boolean isOnline(Context context)
    {
        boolean connected = false;
        final ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            connected = true;
        } else if (netInfo != null && netInfo.isConnected()
                && cm.getActiveNetworkInfo().isAvailable())
        {
            connected = true;
        } else if (netInfo != null && netInfo.isConnected())
        {
            try {
                URL url = new URL("http://www.google.com");
                HttpURLConnection urlc = (HttpURLConnection) url
                        .openConnection();
                urlc.setConnectTimeout(3000);
                urlc.connect();
                if (urlc.getResponseCode() == 200)
                {
                    connected = true;
                }
            } catch (MalformedURLException e1)
            {
                e1.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else if (cm != null)
        {
            final NetworkInfo[] netInfoAll = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfoAll)
            {
                System.out.println("get network type :::" + ni.getTypeName());
                if ((ni.getTypeName().equalsIgnoreCase("WIFI") || ni
                        .getTypeName().equalsIgnoreCase("MOBILE"))
                        && ni.isConnected() && ni.isAvailable())
                {
                    connected = true;
                    if (connected)
                    {
                        break;
                    }
                }
            }
        }
        return connected;
    }
}
