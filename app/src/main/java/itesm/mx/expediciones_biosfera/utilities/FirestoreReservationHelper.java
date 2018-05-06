package itesm.mx.expediciones_biosfera.utilities;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import itesm.mx.expediciones_biosfera.entities.models.Reservation;
import itesm.mx.expediciones_biosfera.entities.models.Destination;
import itesm.mx.expediciones_biosfera.entities.models.Trip;

public class FirestoreReservationHelper {
    final static ArrayList<Reservation> list = new ArrayList<>();
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static Destination destination;
    static Reservation reservation;

    public static void getAllReservations(){
        db.collection("reservations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                list.add(documentSnapshot.toObject(Reservation.class));
                            }
                        } else {
                            System.out.println("ERROR");
                        }
                    }
                });
    }

    public static void addReservation(Reservation reservation){
        CollectionReference reservations = db.collection("reservations");
        reservations.add(reservation);
    }
}