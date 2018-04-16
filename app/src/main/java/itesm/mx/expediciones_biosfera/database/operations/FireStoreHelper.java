package itesm.mx.expediciones_biosfera.database.operations;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by avillarreal on 4/16/18.
 */

public class FireStoreHelper {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference destination = db.collection("destinations").document("destinations");

}
