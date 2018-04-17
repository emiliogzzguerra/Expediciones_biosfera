package itesm.mx.expediciones_biosfera.database.operations;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import itesm.mx.expediciones_biosfera.entities.models.Customer;
import itesm.mx.expediciones_biosfera.entities.models.Destination;
import itesm.mx.expediciones_biosfera.entities.models.Trip;

/**
 * Created by avillarreal on 4/16/18.
 */

public class FirestoreTripHelper {
    final static ArrayList<Trip> list = new ArrayList<>();
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static Destination destination;
    public static void getAllTrips(){
        db.collection("trips")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                System.out.println("OOOOOOOOOOOOOOOOOOOOO");
                                System.out.println(documentSnapshot.getData());
                                list.add(documentSnapshot.toObject(Trip.class));
                            }
                        } else {
                            System.out.println("ERROR");
                        }
                    }
                });
    }

    public static Trip builTrip(Map<String, Object> map) {
      /*  DocumentReference reference = (DocumentReference) map.get("destination");
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                System.out.println("SNAP" + snapshot.getData());
                destination = FirestoreDestinationHelper.buildDestination(snapshot.getData());
                System.out.println(destination);
            }
        }); */
        return new Trip((String) map.get("title"),
                (Date) map.get("date"),
                Integer.valueOf((map.get("capacity")).toString()),
                (Double) map.get("price"),
                Integer.valueOf(map.get("duration").toString()),
                null,
                new ArrayList<Customer>());
    }
}
