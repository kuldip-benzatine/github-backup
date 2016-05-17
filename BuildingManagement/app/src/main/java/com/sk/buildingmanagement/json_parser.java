package com.sk.buildingmanagement;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;

public class json_parser
{

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public json_parser()
    {

    }
    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String url, String method,
                                      List<NameValuePair> params)
    {
        try
        {
            HttpLoader loader=new HttpLoader();
            json=loader.loadDataByPost(url,params);
            jObj = new JSONObject(json);
        }
        catch (JSONException e)
        {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        // return JSON String
        return jObj;

    }
}