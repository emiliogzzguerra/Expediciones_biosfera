package itesm.mx.expediciones_biosfera.entities.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import itesm.mx.expediciones_biosfera.R;
import itesm.mx.expediciones_biosfera.behavior.activities.ReservationAdminDetailActivity;
import itesm.mx.expediciones_biosfera.behavior.activities.ReservationCustomerDetailActivity;
import itesm.mx.expediciones_biosfera.entities.models.Customer;
import itesm.mx.expediciones_biosfera.entities.models.Destination;
import itesm.mx.expediciones_biosfera.entities.models.Reservation;
import itesm.mx.expediciones_biosfera.utilities.StringFormatHelper;

/**
 * Created by avillarreal on 5/4/18.
 */

public class CustomerReservationRecyclerViewAdapter extends RecyclerView.Adapter<CustomerReservationRecyclerViewAdapter.ViewHolder> {
    private List<Reservation> reservationList;
    private Context context;
    private FirebaseFirestore firestoreDB;

    public CustomerReservationRecyclerViewAdapter(List<Reservation> reservationList, Context context, FirebaseFirestore firestoreDB){
        this.reservationList = reservationList;
        this.context = context;
        this.firestoreDB = firestoreDB;
    }

    @Override
    public CustomerReservationRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_reservation, parent, false);
        return new CustomerReservationRecyclerViewAdapter.ViewHolder(view);
    }

    public String getStatusMessage(String status) {
        Resources resources = this.context.getResources();
        int resourceId;
        switch (status) {
            case "approved":
                resourceId = R.string.approved;
                break;
            case "denied":
                resourceId = R.string.denied;
                break;
            default:
                resourceId = R.string.pending;
                break;
        }
        return resources.getString(resourceId);
    }

    @Override
    public void onBindViewHolder(final CustomerReservationRecyclerViewAdapter.ViewHolder holder, int position){
        final int itemPosition = position;
        final Reservation reservation = reservationList.get(itemPosition);
        firestoreDB.collection("destinations").document(reservation.getTripReference())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if(snapshot.exists()) {
                    Destination destination = snapshot.toObject(Destination.class);
                    holder.tvDestination.setText(destination.getName());
                }
                else{
                    holder.tvDestination.setText("Destination not found");
                }
            }
        });
        firestoreDB.collection("users").document(reservation.getCustomerReference())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if(snapshot.exists()) {
                    Customer customer = snapshot.toObject(Customer.class);
                }
            }
        });

        holder.tvPrice.setText("$"+String.valueOf(reservation.getPrice()));
        holder.tvDate.setText(StringFormatHelper
                .getDateAsString(reservation.getInitialDate(),true));
        if(reservation.getIsPaid() != null) {
            String status = "";
            if (!reservation.getIsConfirmed().equals("approved")) {
                status = "Confirmación: " + getStatusMessage(reservation.getIsConfirmed());
            } else if (reservation.getIsPaid() != null) {
                status = "Pago: " + getStatusMessage(reservation.getIsPaid());
            }
            holder.tvStatus.setText(status);
        }
    }

    @Override
    public int getItemCount(){
        return reservationList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView tvDate, tvPrice, tvDestination, tvStatus;
        ViewHolder(View view){
            super(view);
            tvDate = view.findViewById(R.id.text_date);

            tvDestination = view.findViewById(R.id.text_destination);
            tvPrice = view.findViewById(R.id.text_price);
            tvStatus = view.findViewById(R.id.text_status);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int selectedReservation = getAdapterPosition();
                    Intent i	=	new	Intent(context, ReservationCustomerDetailActivity.class);
                    Reservation reservation = reservationList.get(selectedReservation);
                    if(!reservation.getIsConfirmed().equals("pending")){
                        i.putExtra(ReservationCustomerDetailActivity.DESTINATION_TITLE,
                                tvDestination.getText().toString());
                        i.putExtra(ReservationCustomerDetailActivity.RESERVATION_OBJECT, reservation);
                        i.putExtra(ReservationCustomerDetailActivity.RESERVATION_REFERENCE,
                                reservation.getReference());
                        context.startActivity(i);
                    } else {
                        Toast statusToast = Toast.makeText(v.getContext(),
                                "Aún no hemos podido evaluar tu solicitud, regresa pronto", Toast.LENGTH_SHORT);
                        statusToast.show();
                    }
                }
            });

        }
    }
}
