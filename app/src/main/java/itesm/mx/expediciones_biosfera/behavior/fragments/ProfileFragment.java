package itesm.mx.expediciones_biosfera.behavior.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import itesm.mx.expediciones_biosfera.R;
import itesm.mx.expediciones_biosfera.database.operations.User;
import itesm.mx.expediciones_biosfera.database.operations.UserOperations;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private static final int CAMERA_REQUEST = 1888;

    UserOperations dao;

    FirebaseAuth firebaseAuth;
    FirebaseUser fbuser;

    Button btn_save;
    Button btn_take;
    EditText edit_occupation;
    EditText edit_interests;
    EditText edit_phone;
    TextView tv_name;
    ImageView iv_picture;

    ArrayList<User> users = new ArrayList<>();

    OnSaveProfileListener mCallBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        findViews(view);

        getFirebaseUser();

        configureDatabase();

        fillUser(); //fills editTexts if firebaseID is already in SQLite database

        setClickListeners();

        return view;
    }

    public void fillUser() {
        String str = fbuser.getDisplayName();
        tv_name.setText(str);
        str = fbuser.getUid();

        for(int i = 0; i < users.size(); i++){
            if(users.get(i).getFbid().equals(str)){
                edit_occupation.setText( users.get(i).getOccupations() );
                edit_interests.setText( users.get(i).getInterests() );
                edit_phone.setText( users.get(i).getPhone() );

                // Convert bytes data into a Bitmap
                byte[] bytes = users.get(i).getPicture();
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                // Set the Bitmap data to the ImageView
                iv_picture.setImageBitmap(bmp);
            }
        }
    }

    public boolean userExists() {
        String str = fbuser.getUid();
        for(int i = 0; i < users.size(); i++){
            if(users.get(i).getFbid().equals(str)){
                return true;
            }
        }
        return false;
    }

    public boolean validateInput() {

        String occupation = edit_occupation.getText().toString();
        String interests = edit_interests.getText().toString();
        String phone = edit_phone.getText().toString();

        return !(occupation.trim().isEmpty() || interests.trim().isEmpty() || phone.trim().isEmpty() || iv_picture.getDrawable() == null);
    }

    public void getFirebaseUser() {
        firebaseAuth = FirebaseAuth.getInstance();
        fbuser = firebaseAuth.getCurrentUser();
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_profile_take:
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                break;
            case R.id.button_profile_save:

                if(validateInput()){
                    String fbid = fbuser.getUid();
                    String occupations = edit_occupation.getText().toString();
                    String interests = edit_interests.getText().toString();
                    String phone = edit_phone.getText().toString();

                    //Convert ImageView to Byte Array
                    Bitmap bitmap = ((BitmapDrawable) iv_picture.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageInByte = baos.toByteArray();

                    User user = new User(fbid, occupations, interests, phone, imageInByte);

                    if(userExists()){ //se updatea
                        dao.deleteUser(user.getFbid());
                        dao.addUser(user);
                        Toast.makeText(getActivity(), getResources().getString(R.string.information_updated), Toast.LENGTH_LONG).show();
                    }else{ //se agrega
                        dao.addUser(user);
                        Toast.makeText(getActivity(), getResources().getString(R.string.information_saved), Toast.LENGTH_LONG).show();
                    }

                    users = dao.getAllUsers();

                    mCallBack.closeProfile();
                } else{
                    Toast.makeText(getActivity(), getResources().getString(R.string.missing_information), Toast.LENGTH_LONG).show();
                }

                break;
            default:
                break;
        }
    }

    private void findViews(View view) {
        btn_save = view.findViewById(R.id.button_profile_save);
        btn_take = view.findViewById(R.id.button_profile_take);
        edit_occupation = view.findViewById(R.id.edit_profile_occupation);
        edit_interests = view.findViewById(R.id.edit_profile_interests);
        edit_phone = view.findViewById(R.id.edit_profile_phone);
        iv_picture = view.findViewById(R.id.iv_profile_image);
        tv_name = view.findViewById(R.id.text_profile_name);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            iv_picture.setImageBitmap(photo);
        }
    }

    private void configureDatabase() {
        dao = new UserOperations(getActivity());
        dao.open();
        users = dao.getAllUsers();
    }

    private void setClickListeners() {
        btn_take.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    public interface OnSaveProfileListener {
        void closeProfile();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;

        if (context instanceof Activity) {
            activity = (Activity) context;
            try {
                mCallBack = (OnSaveProfileListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnSaveProfileListener");
            }
        }
    }



}
