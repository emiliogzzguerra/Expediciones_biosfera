package itesm.mx.expediciones_biosfera.behavior.fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.w3c.dom.Text;

import java.sql.SQLOutput;
import java.util.ArrayList;

import itesm.mx.expediciones_biosfera.R;
import itesm.mx.expediciones_biosfera.database.operations.User;
import itesm.mx.expediciones_biosfera.database.operations.UserOperations;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    UserOperations dao;
    private static final int CAMERA_REQUEST = 1888;

    FirebaseAuth firebaseAuth;
    FirebaseUser fbuser;

    Button btn_save;
    Button btn_take;
    EditText edt_occupation;
    EditText edt_interests;
    EditText edt_phone;
    TextView tv_name;
    ImageView iv_picture;

    ArrayList<User> lista = new ArrayList<>();

    public void validateUser(){
        String str = fbuser.getUid();
        for(int i = 0; i<lista.size(); i++){
            if(lista.get(i).getFbid().equals(str)){
                edt_occupation.setText( lista.get(i).getOccupations() );
                edt_interests.setText( lista.get(i).getInterests() );
                edt_phone.setText( lista.get(i).getPhone() );
            }
            /*
            System.out.println(i);
            System.out.println(lista.get(i).getFbid() + " " + lista.get(i).getOccupations() + " " +
                    lista.get(i).getInterests() + " " + lista.get(i).getPhone()
            );
            */
        }
    }

    public boolean validateInput(){

        if(edt_occupation.getText().toString().trim().length() == 0 ||
                edt_phone.getText().toString().trim().length() == 0   ||
                edt_interests.getText().toString().trim().length() == 0 ) {
                return false;
        }

        return true;
    }


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        

        btn_save = view.findViewById(R.id.btn_profile_save);
        btn_take = view.findViewById(R.id.btn_profile_take);
        edt_occupation = view.findViewById(R.id.edt_profile_occupation);
        edt_interests = view.findViewById(R.id.edt_profile_interests);
        edt_phone = view.findViewById(R.id.edt_profile_phone);
        iv_picture = view.findViewById(R.id.iv_profile_image);
        tv_name = view.findViewById(R.id.tv_profile_name);

        firebaseAuth = FirebaseAuth.getInstance();
        fbuser = firebaseAuth.getCurrentUser();

        ///
            String str = fbuser.getDisplayName();
            tv_name.setText(str);
            Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
            str = fbuser.getEmail();
            Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
            str = fbuser.getUid();
            Toast.makeText(getActivity(), str , Toast.LENGTH_SHORT).show();
        ///

        dao = new UserOperations(getActivity());
        dao.open();
        lista = dao.getAllUsers();
        //dao.deleteAll();

        validateUser();

        Toast.makeText(getActivity(), "la bd se abrio" , Toast.LENGTH_SHORT).show();

        View.OnClickListener action = new View.OnClickListener(){
            @Override
            public void onClick(View view){
                switch (view.getId()){
                    case R.id.btn_profile_take:
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        break;
                    case R.id.btn_profile_save:

                        if(validateInput()){
                            String fbid = fbuser.getUid();
                            String occupations = edt_occupation.getText().toString();
                            String interests = edt_interests.getText().toString();
                            String phone = edt_phone.getText().toString();
                            User user = new User(fbid, occupations, interests, phone, null);

                            //dao.addUser(user);
                            Toast.makeText(getActivity(), "se agrego un usuario" , Toast.LENGTH_LONG).show();
                            lista = dao.getAllUsers();
                        }else{
                            Toast.makeText(getActivity(), "Faltan datos" , Toast.LENGTH_LONG).show();
                        }

                        break;
                    default:
                        break;
                }
            }

        };

        btn_take.setOnClickListener(action);
        btn_save.setOnClickListener(action);

        Toast.makeText(getActivity(), String.valueOf(lista.size()), Toast.LENGTH_SHORT).show();

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            iv_picture.setImageBitmap(photo);
        }
    }

}
