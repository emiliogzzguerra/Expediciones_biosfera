package itesm.mx.expediciones_biosfera.behavior.fragments;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import itesm.mx.expediciones_biosfera.R;
import itesm.mx.expediciones_biosfera.entities.adapters.AdminReservationRecyclerViewAdapter;
import itesm.mx.expediciones_biosfera.entities.adapters.CustomerReservationRecyclerViewAdapter;
import itesm.mx.expediciones_biosfera.entities.models.Destination;
import itesm.mx.expediciones_biosfera.entities.models.Reservation;

/**
 * Created by avillarreal on 5/4/18.
 */

public class ReservationsListFragment extends Fragment {
    private RecyclerView recyclerView;
    private AdminReservationRecyclerViewAdapter mAdminAdapter;
    private CustomerReservationRecyclerViewAdapter mCustomerAdapter;

    private FirebaseFirestore firestoreDB;
    private ListenerRegistration firestoreListener;

    private boolean isAdmin = false;//TODO: CHECK IF USER IS ADMIN
    private FirebaseAuth firebaseAuth;
    FirebaseUser fbuser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservation_list, container, false);
        recyclerView = view.findViewById(R.id.rv_reservation_list);
        firestoreDB = FirebaseFirestore.getInstance();

        loadReservationList();

        firestoreListener = firestoreDB.collection("reservations")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null){
                            Log.e("Reservations", "Failed", e);
                            return;
                        }

                        List<Reservation> reservationList = new ArrayList<>();

                        firebaseAuth = FirebaseAuth.getInstance();
                        fbuser = firebaseAuth.getCurrentUser();
                        String fbid = fbuser.getUid();

                        for(DocumentSnapshot doc : documentSnapshots){
                            Reservation reservation = doc.toObject(Reservation.class);
                            if(isAdmin){
                                reservationList.add(reservation);
                            }else{
                                if(reservation.getCustomerReference().equals(fbid)){
                                    reservationList.add(reservation);
                                }
                            }

                        }

                        if(isAdmin){
                            mAdminAdapter = new AdminReservationRecyclerViewAdapter(reservationList, getActivity().getApplicationContext(), firestoreDB);
                            recyclerView.setAdapter(mAdminAdapter);
                        }else{
                            mCustomerAdapter = new CustomerReservationRecyclerViewAdapter(reservationList, getActivity().getApplicationContext(), firestoreDB);
                            recyclerView.setAdapter(mCustomerAdapter);
                        }

                    }
                });
        return view;
    }

    private void loadReservationList(){
        firestoreDB.collection("reservations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<Reservation> reservationList = new ArrayList<>();

                            firebaseAuth = FirebaseAuth.getInstance();
                            fbuser = firebaseAuth.getCurrentUser();
                            String fbid = fbuser.getUid();

                            for(DocumentSnapshot doc : task.getResult()){
                                Reservation reservation = doc.toObject(Reservation.class);
                                if(isAdmin){
                                    reservationList.add(reservation);
                                }else{
                                    if(reservation.getCustomerReference().equals(fbid)){
                                        reservationList.add(reservation);
                                    }
                                }
                            }

                            if(isAdmin){
                                mAdminAdapter = new AdminReservationRecyclerViewAdapter(reservationList, getActivity().getApplicationContext(), firestoreDB);
                            }else{
                                mCustomerAdapter = new CustomerReservationRecyclerViewAdapter(reservationList, getActivity().getApplicationContext(), firestoreDB);
                            }

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            if(isAdmin){
                                recyclerView.setAdapter(mAdminAdapter);
                            }else{
                                recyclerView.setAdapter(mCustomerAdapter);
                            }

                        }
                    }
                });
    }

}
