package layout;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.sk.buildingmanagement.adapter.Custom_pager_adapter;
import com.sk.buildingmanagement.adapter.GridView_Adapter;
import com.sk.buildingmanagement.R;

import java.util.Timer;
import java.util.TimerTask;


public class Home_fragment extends Fragment implements View.OnClickListener
{
    View rootview;
    Context mcontext;
    int currentpager=0;
    Custom_pager_adapter mcustom_pager_adapter;
    GridView_Adapter gridView_adapter;
    GridView home_fragment_gridview;
    ViewPager mpager;

    int[] grid_image_button_image =
            {
                    R.drawable.ic_add_wing,
                    R.drawable.ic_maintenace_sheet,
                    R.drawable.ic_building_info,
                    R.drawable.ic_event,
                    R.drawable.ic_shop_info,
                    R.drawable.ic_owner_info,
                    R.drawable.ic_complaint_box,
                    R.drawable.ic_service_info,
                    R.drawable.ic_ruls,
                    R.drawable.ic_member_info,
                    R.drawable.ic_add_sub_admin,
                    R.drawable.ic_timing_info,
                    R.drawable.ic_help_support,
                    R.drawable.ic_setting
            };
    String[] grid_image_button_name =
            {
                    "ADD WINGS",
                    "MAINTANCE SHEET",
                    "BUILDING INFO",
                    "EVENTS",
                    "SHOP INFO",
                    "OWNER INFO",
                    "COMPLAIN BOX",
                    "SERVICE INFO",
                    "RULES",
                    "MEMBER INFO",
                    "ADD_SUBADMIN",
                    "TIMING INFO",
                    "HELP AND SUPPORT",
                    "SETTING"
            };
    public Home_fragment()
    {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }
    // initialize all controlls

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        rootview=inflater.inflate(R.layout.fragment_home, container, false);
        mcontext=rootview.getContext();
        ImageView profile_img= (ImageView) rootview.findViewById(R.id.imageView);
        SharedPreferences pref = mcontext.getSharedPreferences("Login_detail_pref",0);
        String profile_img_url = pref.getString("profile_img",null);
        initialize_controll();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Home");

        initialize_controll_action();

        mcustom_pager_adapter = new Custom_pager_adapter(mcontext);
        mpager.setAdapter(mcustom_pager_adapter);

        gridView_adapter=new GridView_Adapter(mcontext,grid_image_button_name,grid_image_button_image);
        home_fragment_gridview.setAdapter(gridView_adapter);
        sliding_pager();

        // Inflate the layout for this fragment
        return rootview;
    }
    public void initialize_controll()
    {
        mpager = (ViewPager) rootview.findViewById(R.id.home_fragment_pager);
        home_fragment_gridview = (GridView) rootview.findViewById(R.id.home_fragment_gridview);

    }
    public void initialize_controll_action()
    {

    }
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        toolbar.setSubtitle("Home");
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
    }
    @Override
    public void onClick(View v)
    {

    }
    public void sliding_pager()
    {
        final Handler mHandler = new Handler();
        // Create runnable for posting
        final Runnable mUpdateResults = new Runnable()
        {
            public void run()
            {
                if(currentpager<4)
                    currentpager = mpager.getCurrentItem()+1;
                else
                    currentpager=0;
                mpager.setCurrentItem(currentpager);
            }
        };
        int delay = 1000; // delay for 1 sec.
        int period = 8000; // repeat every 4 sec.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            public void run()
            {
                mHandler.post(mUpdateResults);
            }
        }, delay, period);
    }
}