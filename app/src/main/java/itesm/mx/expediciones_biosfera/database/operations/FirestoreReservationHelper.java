package itesm.mx.expediciones_biosfera.database.operations;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import itesm.mx.expediciones_biosfera.entities.models.Destination;
import itesm.mx.expediciones_biosfera.entities.models.Reservation;

/**
 * Created by emiliogonzalez on 5/4/18.
 */

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

    public static void setConfirmedPending(String s) {
        Map<String, Object> data = new HashMap<>();
        data.put("isConfirmed", "pending");

        db.collection("reservations").document(s)
                .set(data, SetOptions.merge());
    }

    public static void setConfirmedApproved(String s) {
        Map<String, Object> data = new HashMap<>();
        data.put("isConfirmed", "approved");

        db.collection("reservations").document(s)
                .set(data, SetOptions.merge());
    }

    public static void setConfirmedDeclined(String s) {
        Map<String, Object> data = new HashMap<>();
        data.put("isConfirmed", "declined");

        db.collection("reservations").document(s)
                .set(data, SetOptions.merge());
    }

    public static void setPaidPending(String s) {
        Map<String, Object> data = new HashMap<>();
        data.put("isPaid", "pending");

        db.collection("reservations").document(s)
                .set(data, SetOptions.merge());
    }

    public static void setPaidApproved(String s) {
        Map<String, Object> data = new HashMap<>();
        data.put("isPaid", "approved");

        db.collection("reservations").document(s)
                .set(data, SetOptions.merge());
    }

    public static void setPaidDeclined(String s) {
        Map<String, Object> data = new HashMap<>();
        data.put("isPaid", "declined");

        db.collection("reservations").document(s)
                .set(data, SetOptions.merge());
    }

}