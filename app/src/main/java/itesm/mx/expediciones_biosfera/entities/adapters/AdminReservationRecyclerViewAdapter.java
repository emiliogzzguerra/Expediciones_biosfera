package itesm.mx.expediciones_biosfera.entities.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.List;

import itesm.mx.expediciones_biosfera.R;
import itesm.mx.expediciones_biosfera.behavior.activities.ReservationDetailActivity;
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
    private AdapterView.OnItemClickListener clickListener;
    private boolean isAdmin;
    private FirebaseAuth firebaseAuth;
    FirebaseUser fbuser;

    public AdminReservationRecyclerViewAdapter(List<Reservation> reservationList, Context context, FirebaseFirestore firestoreDB){
        this.reservationList = reservationList;
        this.context = context;
        this.firestoreDB = firestoreDB;
    }

    @Override
    public AdminReservationRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view;

        firebaseAuth = FirebaseAuth.getInstance();
        fbuser = firebaseAuth.getCurrentUser();
        String fbid = fbuser.getUid();

        isAdmin = true;//TODO: CHECK IF USER IS ADMIN

        if(isAdmin){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_reservation, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_reservation, parent, false);
        }

        return new AdminReservationRecyclerViewAdapter.ViewHolder(view);
    }

    public interface OnItemClickListener {
        public void onCLick(View view, int position);
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
                    if(isAdmin) {
                        holder.tvCustomer.setText(customer.getName());
                    }
                }
                else{
                    if(isAdmin) {
                        holder.tvCustomer.setText("User not found");
                    }
                }
            }
        });

        holder.tvPrice.setText("$"+String.valueOf(reservation.getPrice()));
        holder.tvDate.setText(reservation.getInitialDate().toString());
        if(reservation.getIsPaid() != null) {
            if (!reservation.getIsConfirmed().equals("Aprobado")) {
                holder.tvStatus.setText("Confirmacion: " + reservation.getIsConfirmed());
            } else if (reservation.getIsPaid() != null) {
                holder.tvStatus.setText("Pago: " + reservation.getIsPaid());
            }
        }
    }

    @Override
    public int getItemCount(){
        return reservationList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView tvDate, tvPrice, tvCustomer, tvDestination, tvStatus;
        ViewHolder(View view){
            super(view);
            tvDate = view.findViewById(R.id.text_date);
            if(isAdmin){
                tvCustomer = view.findViewById(R.id.text_customer);
            }

            tvDestination = view.findViewById(R.id.text_destination);
            tvPrice = view.findViewById(R.id.text_price);
            tvStatus = view.findViewById(R.id.text_status);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int position = getAdapterPosition();
                    Intent i	=	new	Intent(context, ReservationDetailActivity.class);
                    Reservation reservation = reservationList.get(position);
                    i.putExtra("destination", tvDestination.getText().toString());
                    i.putExtra("customer", tvCustomer.getText().toString());
                    i.putExtra("reservation", reservation);


                    context.startActivity(i);
                }
            });

        }
    }
}
