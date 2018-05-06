package itesm.mx.expediciones_biosfera.behavior.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

/**
 * Created by avillarreal on 4/26/18.
 */

public class DestinationListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DestinationRecyclerViewAdapter mAdapter;

    private FirebaseFirestore firestoreDB;
    private ListenerRegistration firestoreListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_list);

        recyclerView = findViewById(R.id.rvDestinationList);
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
                            destinationList.add(destination);
                        }

                        mAdapter = new DestinationRecyclerViewAdapter(destinationList, getApplicationContext(), firestoreDB);
                        recyclerView.setAdapter(mAdapter);
                    }
                });
    }

    @Override
    protected void onDestroy(){
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
                                destinationList.add(destination);
                            }

                            mAdapter = new DestinationRecyclerViewAdapter(destinationList, getApplicationContext(), firestoreDB);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
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

