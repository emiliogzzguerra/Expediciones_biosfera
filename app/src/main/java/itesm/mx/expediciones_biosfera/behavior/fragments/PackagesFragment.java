package itesm.mx.expediciones_biosfera.behavior.fragments;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import itesm.mx.expediciones_biosfera.R;
import itesm.mx.expediciones_biosfera.entities.adapters.DestinationRecyclerViewAdapter;
import itesm.mx.expediciones_biosfera.entities.models.Destination;

public class PackagesFragment extends Fragment {

    private RecyclerView recyclerView;
    private DestinationRecyclerViewAdapter mAdapter;

    private FirebaseFirestore firestoreDB;
    private ListenerRegistration firestoreListener;

    public PackagesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_packages, container, false);

        recyclerView = view.findViewById(R.id.rvDestinationList);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firestoreDB = FirebaseFirestore.getInstance();

        loadDestinationList();

        firestoreListener = firestoreDB.collection("destinations")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("Destinations", "Failed", e);
                            return;
                        }

                        List<Destination> destinationList = new ArrayList<>();

                        for (DocumentSnapshot doc : documentSnapshots) {
                            Destination destination = doc.toObject(Destination.class);
                            destination.setReference(doc.getId());
                            destinationList.add(destination);
                        }

                        mAdapter = new DestinationRecyclerViewAdapter(destinationList, getContext(), firestoreDB);
                        recyclerView.setAdapter(mAdapter);
                    }
                });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        firestoreListener.remove();
    }

    private void loadDestinationList() {
        firestoreDB.collection("destinations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Destination> destinationList = new ArrayList<>();

                            for (DocumentSnapshot doc : task.getResult()) {
                                Destination destination = doc.toObject(Destination.class);
                                destination.setReference(doc.getId());
                                destinationList.add(destination);
                            }

                            mAdapter = new DestinationRecyclerViewAdapter(destinationList, getContext(), firestoreDB);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);
                        } else {
                            Log.d("ERROR", "error getting docs", task.getException());
                        }
                    }
                });
    }

}
