package layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sk.buildingmanagement.HttpLoader;
import com.sk.buildingmanagement.R;
import com.sk.buildingmanagement.json_parser;
import com.sk.buildingmanagement.util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Update_profile_fragment extends Fragment implements Button.OnClickListener,AdapterView.OnItemSelectedListener {

    View root_view;

    Context mcontext;

    TextView update_profile_date_txt;
    EditText update_profile_first_name , update_profile_last_name ,
            update_profile_contact_no , update_profile_address ;
    Spinner update_profile_gender;
    Button update_profile_update_btn,update_profile_birth_date,update_profile_birth_time;
    String update_url="http://benzatineinfotech.com/webservice/build_mgnt/admin_update_profile.php";
    private OnFragmentInteractionListener mListener;
    String id, firstname, lastname, mobileno, address, gender, birthdate, profileimage;
    String update_gender;
    ImageView cover_image;
    CircleImageView profile_img;
    String profile_img_url;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String profile_image_path="";
    String device_id;
    public Update_profile_fragment()
    {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Update_profile_fragment newInstance(String param1, String param2)
    {
        Update_profile_fragment fragment = new Update_profile_fragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        root_view = inflater.inflate(R.layout.fragment_update_profile, container, false);
        mcontext=root_view.getContext();
        initialize_controll();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Update Profile");



        // get prefrence value
        SharedPreferences prefs = getActivity().getSharedPreferences("Login_detail_pref",Context.MODE_PRIVATE);
        id=prefs.getString("id",null);
        device_id=prefs.getString("device_id",device_id);
        update_profile_first_name.setText(prefs.getString("first_name",null));
        update_profile_last_name.setText(prefs.getString("last_name",null));
        update_profile_contact_no.setText(prefs.getString("mobile_no",null));
        update_profile_address.setText(prefs.getString("address",null));
        update_profile_date_txt.setText(prefs.getString("birth_date",null));
        gender=prefs.getString("gender",null);
        profile_img_url=prefs.getString("profile_img",null);
        List<String> categories = new ArrayList<String>();
        categories.add("MALE");
        categories.add("FEMALE");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        update_profile_gender.setAdapter(dataAdapter);
        if(gender.equals("1"))
        {
            update_profile_gender.setSelection(0);
        }
        else
        {
            update_profile_gender.setSelection(1);
        }
        initialize_controll_action();
       // Check_internet_connection();
        util.image_loader(profile_img_url,cover_image,getActivity());
        util.image_loader(profile_img_url,profile_img,getActivity());
        return root_view;
    }

    Calendar date;
    public void showDateTimePicker()
    {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(mcontext, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(mcontext, new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
    public void initialize_controll()
    {
        update_profile_first_name = (EditText) root_view.findViewById(R.id.update_profile_first_name);
        update_profile_last_name = (EditText) root_view.findViewById(R.id.update_profile_last_name);
        cover_image = (ImageView) root_view.findViewById(R.id.display_profile_cover_img);
        profile_img= (CircleImageView) root_view.findViewById(R.id.display_profile_circle_img);
        update_profile_contact_no = (EditText) root_view.findViewById(R.id.update_profile_contact_no);
        update_profile_address = (EditText) root_view.findViewById(R.id.update_profile_address);
        update_profile_birth_date = (Button) root_view.findViewById(R.id.update_profile_birth_date);
        update_profile_gender = (Spinner) root_view.findViewById(R.id.update_profile_gender);
        update_profile_update_btn = (Button) root_view.findViewById(R.id.update_profile_update_btn);
        update_profile_date_txt = (TextView) root_view.findViewById(R.id.update_profile_birth_date_txt);
    }
    public void initialize_controll_action()
    {
        update_profile_update_btn.setOnClickListener(this);
        update_profile_gender.setOnItemSelectedListener(this);
        update_profile_birth_date.setOnClickListener(this);

        cover_image.setOnClickListener(this);


    }
    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.update_profile_update_btn)
        {
            if(util.isOnline(getContext()))
            {
                firstname = update_profile_first_name.getText().toString();
                lastname = update_profile_last_name.getText().toString();
                mobileno = update_profile_contact_no.getText().toString();
                address = update_profile_address.getText().toString();

                if (gender.equals("1")) {
                    update_gender = "1";
                    gender = "1";
                } else {
                    update_gender = "2";
                    gender = "2";
                }

                birthdate = update_profile_date_txt.getText().toString();

                new update_profile().execute();
            }
            else
            {
                Toast.makeText(getContext(),R.string.no_internet,Toast.LENGTH_LONG).show();
            }
        }
        if(v.getId()==R.id.update_profile_birth_date)
        {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(mcontext,new DatePickerDialog.OnDateSetListener()
                    {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth)
                        {
                            update_profile_date_txt.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            birthdate=update_profile_date_txt.getText().toString();
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if(v.getId()==R.id.display_profile_cover_img)
        {
            selectImage();
        }

    }

    private void selectImage()
    {
        final CharSequence[] items = { "Take Photo", "Choose from Library","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {
                if (items[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
                else if (items[item].equals("Choose from Library"))
                {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                }
                else if (items[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }
    private void onCaptureImageResult(Intent data)
    {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        profile_image_path=Environment.getExternalStorageDirectory()+"/"+System.currentTimeMillis()+".jpg";
        FileOutputStream fo;
        try
        {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        cover_image.setImageBitmap(thumbnail);
        profile_img.setImageBitmap(thumbnail);
    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data)
    {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getActivity().managedQuery(selectedImageUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);
        profile_image_path=selectedImagePath;
        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        cover_image.setImageBitmap(bm);
        profile_img.setImageBitmap(bm);
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
    json_parser jsonParser = new json_parser();

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        String if_gender = (String) update_profile_gender.getSelectedItem();
        if(if_gender.equals("MALE"))
        {
            gender="1";
        }
        else
        {
            gender="2";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    class update_profile extends AsyncTask<String, String, String>
    {
        ProgressDialog pDialog;

        String response;
        JSONObject json;
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params)
        {
            // Check for success tag
            try
            {
                // Building Parameters
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();

                params1.add(new BasicNameValuePair("id",id));
                params1.add(new BasicNameValuePair("device_id",device_id));
                params1.add(new BasicNameValuePair("firstname",firstname));
                params1.add(new BasicNameValuePair("lastname",lastname));
                params1.add(new BasicNameValuePair("mobileno",mobileno));
                params1.add(new BasicNameValuePair("address",address));
                params1.add(new BasicNameValuePair("gender",update_gender));
                params1.add(new BasicNameValuePair("birthdate",birthdate));
                HttpLoader loader=new HttpLoader();
                if(profile_image_path.equals(""))
                {
                    response = loader.loadDataByPost(update_url,params1);
                }
                else
                {
                    List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                    params2.add(new BasicNameValuePair("profileimage",profile_image_path));
                    response= loader.loadDataByPost(update_url,params1,params2);
                }

               // params1.add(new BasicNameValuePair("profileimage",profileimage));
                // params1.add(new BasicNameValuePair("password",login_activity_password_txt.getText().toString()));
                //jsonParser.makeHttpRequest(update_url, "POST", );
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url)
        {
            // dismiss the dialog once got all details
            pDialog.dismiss();
            int success;
            SharedPreferences.Editor editor = getActivity().getSharedPreferences("Login_detail_pref",Context.MODE_PRIVATE).edit();
            //SharedPreferences.Editor editor = getSharedPreferences("Login_detail_pref", MODE_PRIVATE).edit();
            try
            {

                    JSONObject json = new JSONObject(response);
                    success = json.getInt("status_code");
                if (success == 200)
                {
                    JSONObject jData=json.optJSONObject("data");
                    editor.putString("user_name",jData.optString("username"));
                    editor.putString("first_name",jData.optString("firstname"));
                    editor.putString("last_name",jData.optString("lastname"));
//                    editor.putString("email",jData.optString("email"));
                    editor.putString("mobile_no",jData.optString("mobileno"));
                    editor.putString("profile_img",jData.optString("profileimage"));
                    editor.putString("address",jData.optString("address"));
                    editor.putString("gender",jData.optString("gender"));
                    editor.putString("birth_date",util.date_format(jData.optString("birthdate")));
                    //editor.putString("is_first_time",jData.optString("is_firsttime"));
                   // util.image_loader(jData.optString("profileimage"),(ImageView)((NavigationView) getActivity().findViewById(R.id.nav_view)).getHeaderView(0).findViewById(R.id.imageView),getActivity());
                    util.image_loader(jData.optString("profileimage"),(ImageView)((NavigationView) getActivity().findViewById(R.id.nav_view1)).getHeaderView(0).findViewById(R.id.imageView2),getActivity());
                    editor.commit();
                }
                if(success == 1011)
                {
                    Toast.makeText(getActivity(), json.optString("msg"),Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {

            }
        }
    }

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
