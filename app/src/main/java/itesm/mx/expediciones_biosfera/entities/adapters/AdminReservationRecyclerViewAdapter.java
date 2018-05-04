package itesm.mx.expediciones_biosfera.entities.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import itesm.mx.expediciones_biosfera.R;
import itesm.mx.expediciones_biosfera.entities.models.Customer;
import itesm.mx.expediciones_biosfera.entities.models.Destination;
import itesm.mx.expediciones_biosfera.entities.models.Reservation;

/**
 * Created by avillarreal on 5/4/18.
 */

public class AdminReservationRecyclerViewAdapter extends RecyclerView.Adapter<AdminReservationRecyclerViewAdapter.ViewHolder> {
    private List<Reservation> reservationList;
    private Context context;
    private FirebaseFirestore firestoreDB;
    private AdminReservationRecyclerViewAdapter.ViewHolder tempHolder;
    public AdminReservationRecyclerViewAdapter(List<Reservation> reservationList, Context context, FirebaseFirestore firestoreDB){
        this.reservationList = reservationList;
        this.context = context;
        this.firestoreDB = firestoreDB;
    }

    @Override
    public AdminReservationRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_reservation, parent, false);
        return new AdminReservationRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdminReservationRecyclerViewAdapter.ViewHolder holder, int position){
        final int itemPosition = position;
        final Reservation reservation = reservationList.get(itemPosition);
        firestoreDB.collection("destinations").document(reservation.getTripReference())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if(snapshot.exists()) {
                    Destination destination = snapshot.toObject(Destination.class);
                    holder.tvDestination.setText(destination.getCity());
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
                    holder.tvCustomer.setText(customer.getName());
                    System.out.println("");
                }
                else{
                    holder.tvCustomer.setText("User not found");
                }
            }
        });

        holder.tvPrice.setText("$"+String.valueOf(reservation.getPrice()));
        holder.tvDate.setText(reservation.getInitialDate().toString());


    }

    @Override
    public int getItemCount(){
        return reservationList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView tvDate, tvPrice, tvCustomer, tvDestination, tvStatus;
        ViewHolder(View view){
            super(view);
            tvDate = view.findViewById(R.id.tv_date);
            tvCustomer = view.findViewById(R.id.tv_customer);
            tvDestination = view.findViewById(R.id.tv_destination);
            tvPrice = view.findViewById(R.id.tv_price);
            tvStatus = view.findViewById(R.id.tv_status);
        }
    }
}
