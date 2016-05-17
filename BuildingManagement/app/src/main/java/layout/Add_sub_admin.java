package layout;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.sk.buildingmanagement.R;

import java.util.ArrayList;
import java.util.List;



public class Add_sub_admin extends Fragment
{

    View rootview;
    Context mcontext;
    Spinner member_type;
    Spinner gender;
    List<String> list;
    List<String> gender_list;
    ArrayAdapter<String> dataAdapter;
    ArrayAdapter<String> gender_data_adapter;
    Button sub_admin_submit_btn;

    private OnFragmentInteractionListener mListener;

    public Add_sub_admin() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Add_sub_admin newInstance(String param1, String param2)
    {
        Add_sub_admin fragment = new Add_sub_admin();
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
        rootview=inflater.inflate(R.layout.fragment_add_sub_admin, container, false);
        mcontext=rootview.getContext();

        initialize_controll();
        list.add("member 1");
        list.add("member 2");
        list.add("member 3");
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        member_type.setAdapter(dataAdapter);

        gender_list.add("Male");
        gender_list.add("Female");

        gender_data_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        gender.setAdapter(gender_data_adapter);

        initialize_controll_action();

        // Inflate the layout for this fragment
        return rootview;
    }

    public void initialize_controll()
    {
        member_type = (Spinner) rootview.findViewById(R.id.add_sub_admin_member_type);
        list = new ArrayList<String>();
        dataAdapter = new ArrayAdapter<String>(mcontext,android.R.layout.simple_spinner_item, list);
        gender_list=new ArrayList<String>();
        gender_data_adapter = new ArrayAdapter<String>(mcontext,android.R.layout.simple_spinner_item, gender_list);
        gender = (Spinner) rootview.findViewById(R.id.add_sub_admin_gender);
        sub_admin_submit_btn = (Button) rootview.findViewById(R.id.sub_admin_submit_btn);
    }
    public void initialize_controll_action()
    {

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        toolbar.setSubtitle("Add Sub Admin");
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


    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
