package itesm.mx.expediciones_biosfera.behavior.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import itesm.mx.expediciones_biosfera.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    Button btn_change;
    TextView tv_occupation;
    TextView tv_interests;
    TextView tv_phone;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        btn_change = view.findViewById(R.id.btn_change);
        tv_occupation = view.findViewById(R.id.tv_occupation);
        tv_interests = view.findViewById(R.id.tv_interests);
        tv_phone = view.findViewById(R.id.tv_phone);

        //los textview se deben de ver así
        //Ocupacion: #####
        //Intereses: #####
        //Telefono: #####

        //si quieren modificar los contenidos de los textviews usen este ejemplo
        tv_occupation.setText(String.format(getResources().getString(R.string.occupation), "Estudiante"));

        return view;
    }

}
