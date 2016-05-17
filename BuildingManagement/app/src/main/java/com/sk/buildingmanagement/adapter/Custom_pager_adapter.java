package com.sk.buildingmanagement.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sk.buildingmanagement.R;

public class Custom_pager_adapter extends PagerAdapter
{

    Context mContext;
    LayoutInflater mLayoutInflater;

    int[] mResources =
            {
                    R.drawable.building1,
                    R.drawable.building2,
                    R.drawable.building3,
                    R.drawable.building4
            };

    public Custom_pager_adapter(Context context)
    {
        mContext = context;



            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount()
    {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        View itemView = mLayoutInflater.inflate(R.layout.home_fragment_pager_layout, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageResource(mResources[position]);
        container.addView(itemView);
        return itemView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((LinearLayout) object);
    }

}