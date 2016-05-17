package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sk.buildingmanagement.R;


public class Sub_admin_detail_fragment extends Fragment implements Button.OnClickListener
{

    View root_view;
    Context mcontext;
    Button sub_admin_detail_btn;
    private OnFragmentInteractionListener mListener;

    public Sub_admin_detail_fragment()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Sub_admin_detail_fragment newInstance(String param1, String param2)
    {
        Sub_admin_detail_fragment fragment = new Sub_admin_detail_fragment();
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
        root_view=inflater.inflate(R.layout.sub_admin_detail_fragment, container, false);
        mcontext=root_view.getContext();
        initialize_controll();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Sub Admin Detail");
       // getActivity().findViewById(R.id.toolbar).setBackgroundColor(R.color.window_color);
        initialize_controll_action();
        // Inflate the layout for this fragment
        return root_view;
    }
    public void initialize_controll()
    {
        sub_admin_detail_btn = (Button) root_view.findViewById(R.id.sub_detail_btn);
    }
    public void initialize_controll_action()
    {
        sub_admin_detail_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.sub_detail_btn)
        {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment, new Add_sub_admin(), "Sub Admin Detail");
            ft.commit();
        }
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
