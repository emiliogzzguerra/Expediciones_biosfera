package itesm.mx.expediciones_biosfera.entities.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import itesm.mx.expediciones_biosfera.R;
import itesm.mx.expediciones_biosfera.behavior.activities.DestinationActivity;
import itesm.mx.expediciones_biosfera.entities.models.Destination;

/**
 * Created by avillarreal on 4/26/18.
 */


public class DestinationRecyclerViewAdapter extends RecyclerView.Adapter<DestinationRecyclerViewAdapter.ViewHolder> {
    private List<Destination> destinationList;
    private Context context;
    private FirebaseFirestore firestoreDB;

    public DestinationRecyclerViewAdapter(List<Destination> destinationList, Context context, FirebaseFirestore firestoreDB){
        this.destinationList = destinationList;
        this.context = context;
        this.firestoreDB = firestoreDB;
    }

    @Override
    public DestinationRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_destination, parent, false);
        return new DestinationRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DestinationRecyclerViewAdapter.ViewHolder holder, int position){
        final int itemPosition = position;
        final Destination destination = destinationList.get(itemPosition);

        String destinationName = destination.getState() + ", " + destination.getCity();
        holder.tvDestinationName.setText(destinationName);
        String imageUrl;
        if (destination.getImageUrls().size() > 0) {
            imageUrl = destination.getImageUrls().get(0);
        } else {
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/expedicionesbiosfera.appspot.com/o/Cuatro%20cienegas%2Fcuatro-cienegas-coahuila-pueblo-magico.1600.jpg?alt=media&token=59c33c6d-ccbc-46a4-b6d1-16a0ea4c08ad";
        }



        Glide.with(holder.ivDestinationMain.getContext())
                .load(imageUrl)
                .dontAnimate()
                .into(holder.ivDestinationMain);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                sendToDetailedView(destination);
            }
        });
    }

    @Override
    public int getItemCount(){
        return destinationList.size();
    }

    private void sendToDetailedView(Destination destination){
        Intent intent = new Intent(context, DestinationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DestinationActivity.DESTINATION_OBJECT, destination);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDestinationName;
        ImageView ivDestinationMain;

        ViewHolder(View view){
            super(view);
            tvDestinationName = view.findViewById(R.id.text_destination_name);
            ivDestinationMain = view.findViewById(R.id.image_destination_main);
        }
    }
}
