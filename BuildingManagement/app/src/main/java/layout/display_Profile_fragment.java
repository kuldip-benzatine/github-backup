package layout;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.sk.buildingmanagement.R;
import com.sk.buildingmanagement.util;

import de.hdodenhof.circleimageview.CircleImageView;


public class display_Profile_fragment extends Fragment implements Button.OnClickListener, Update_profile_fragment.OnFragmentInteractionListener
{
    private OnFragmentInteractionListener mListener;
    Button dispay_profile_update_btn;
    View root_view;
    Context mcontext;
    Toolbar toolbar;
    Activity activity;
    //String restoredText = prefs.getString("text", null);
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageView cover_image;
    CircleImageView profile_img;
    String profile_img_url;
    SharedPreferences pref;
    TextView display_profile_name_text , display_profile_birth_text ,display_profile_email_text,dislay_profile_contact_no
            ,display_profile_gender,display_profile_address;
    public display_Profile_fragment()
    {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static display_Profile_fragment newInstance(String param1, String param2)
    {
        display_Profile_fragment fragment = new display_Profile_fragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        root_view=inflater.inflate(R.layout.fragment_display_profile, container, false);
        mcontext = root_view.getContext();
        activity=getActivity();
        initialize_controll();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Profile");
        //toolbar.setSubtitle("Profile");
        SharedPreferences prefs = getActivity().getSharedPreferences("Login_detail_pref", Context.MODE_PRIVATE);
        profile_img_url=prefs.getString("profile_img",null);

        initialize_controll_action();

        display_profile_name_text.setText(prefs.getString("first_name",null)+prefs.getString("last_name",null));

        display_profile_birth_text.setText(prefs.getString("birth_date",null));
        display_profile_email_text.setText(prefs.getString("email",null));
        dislay_profile_contact_no.setText(prefs.getString("mobile_no",null));
        display_profile_address.setText(prefs.getString("address",null));

        String a = prefs.getString("gender",null);
        if(a.equals("1"))
        {
            display_profile_gender.setText("MALE");
        }
        else
        {
            display_profile_gender.setText("FEMALE");
        }

        // load cover and profile image
        util.image_loader(profile_img_url,cover_image,getContext());
        util.image_loader(profile_img_url,profile_img,getContext());
        return  root_view;
    }
    public void initialize_controll()
    {
        toolbar = (Toolbar) this.root_view.findViewById(R.id.toolbar);
        cover_image = (ImageView) root_view.findViewById(R.id.display_profile_cover_img);
        profile_img= (CircleImageView) root_view.findViewById(R.id.display_profile_circle_img);
        dispay_profile_update_btn = (Button) root_view.findViewById(R.id.display_profile_update_btn);

        display_profile_name_text = (TextView) root_view.findViewById(R.id.display_profile_name_text);
        display_profile_birth_text = (TextView) root_view.findViewById(R.id.display_profile_birth_text);
        display_profile_email_text = (TextView) root_view.findViewById(R.id.display_profile_email_text);
        dislay_profile_contact_no = (TextView) root_view.findViewById(R.id.dislay_profile_contact_no);
        display_profile_gender = (TextView)root_view.findViewById(R.id.display_profile_gender);
        display_profile_address = (TextView)root_view.findViewById(R.id.display_profile_address);

    }
    public void initialize_controll_action()
    {
        dispay_profile_update_btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.display_profile_update_btn)
        {

            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment, new Update_profile_fragment(), "Update profile Fragment");
            ft.commit();
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new Home_fragment()).commit();
        }
    }
//    public void Check_internet_connection()
//    {
//        boolean connected = false;
//        String cover_img_str="http://www.online-image-editor.com//styles/2014/images/example_image.png";
//        String profile_img_str=profile_img_url;
//        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
//                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
//        {
//            image_loader(profile_img_str,cover_image);
//            image_loader(profile_img_str,profile_img);
//
//        }
//        else
//        {
//            image_loader(profile_img_str,cover_image);
//            image_loader(profile_img_str,profile_img);
//            Toast.makeText(getContext(),R.string.no_internet, Toast.LENGTH_LONG).show();
//        }
//    }
//    public void image_loader(String uri,ImageView img)
//    {
//        //we are connected to a network
//        String url = uri;
//        ImageView loader_image_view=img;
//        ImageLoader imageLoader = ImageLoader.getInstance();
//        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
//                .cacheOnDisc(true).resetViewBeforeLoading(true)
//                .showImageForEmptyUri(R.mipmap.ic_launcher)
//                .showImageOnFail(R.mipmap.ic_launcher)
//                .showImageOnLoading(R.mipmap.ic_launcher).build();
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//                getContext())
//                .defaultDisplayImageOptions(options)
//                .memoryCache(new WeakMemoryCache())
//                .discCacheSize(100 * 1024 * 1024).build();
//        ImageLoader.getInstance().init(config);
//        //initialize image view
//        //download and display image from url
//        imageLoader.getInstance().displayImage(url, loader_image_view, options);
//    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
