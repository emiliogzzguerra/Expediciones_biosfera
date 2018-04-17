package itesm.mx.expediciones_biosfera.database.operations;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import itesm.mx.expediciones_biosfera.entities.models.Customer;
import itesm.mx.expediciones_biosfera.entities.models.Destination;
import itesm.mx.expediciones_biosfera.entities.models.Trip;

/**
 * Created by avillarreal on 4/16/18.
 */

public class FirestoreDestinationHelper {
    static Destination destination;
    public static Destination buildDestination(Map<String, Object> map) {
        return new Destination(
                map.get("name").toString(),
                map.get("state").toString(),
                map.get("city").toString(),
                (Double) map.get("lat"),
                (Double) map.get("lon"),
                map.get("description").toString(),
                new ArrayList< Bitmap>());
    }
}
