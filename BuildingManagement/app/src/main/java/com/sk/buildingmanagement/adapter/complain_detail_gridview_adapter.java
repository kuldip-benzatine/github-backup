package com.sk.buildingmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sk.buildingmanagement.R;

public class complain_detail_gridview_adapter extends BaseAdapter
{

    Context context;
    int [] imageId;
    private static LayoutInflater inflater=null;
    public complain_detail_gridview_adapter(Context mainActivity,int[] prgmImages)
    {
        // TODO Auto-generated constructor stub

        context=mainActivity;
        imageId=prgmImages;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return 1;
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
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageButton img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.complain_detail_gridview_adapter, null);
      /*  holder.tv=(TextView) rowView.findViewById(R.id.home_fragment_gridview_textview);
        holder.img=(ImageButton) rowView.findViewById(R.id.home_fragment_gridview_imagebutton);


        holder.img.setImageResource(imageId[position]);

        holder.img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
//        rowView.setOnClickListener(new OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                // TODO Auto-generated method stub
//                Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
//            }
//        });
*/
        return rowView;
    }

}