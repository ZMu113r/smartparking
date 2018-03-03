package com.example.zacharymuller.smartparking.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zacharymuller.smartparking.Activities.NavigationActivity;
import com.example.zacharymuller.smartparking.Entities.GarageEntity;
import com.example.zacharymuller.smartparking.Entities.User;
import com.example.zacharymuller.smartparking.Models.CardModel;
import com.example.zacharymuller.smartparking.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zach on 1/13/2018.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.SwipeableCardViewHolder> {
    private User currentUser;

    private ArrayList<GarageEntity> closestGarages;

    private List<CardModel> modelList;

    private Context context;


    public CardAdapter(User currentUser, ArrayList<GarageEntity> closestGarages, List<CardModel> modelList, Context context) {
        this.currentUser = currentUser;
        this.closestGarages = closestGarages;
        this.modelList = modelList;
        this.context = context;
    }

    @Override
    public CardAdapter.SwipeableCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.card_item, parent, false);

        SwipeableCardViewHolder holder = new SwipeableCardViewHolder(view);

        return holder;
    }

    // Getters
    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @Override
    public void onBindViewHolder(SwipeableCardViewHolder holder, int position) {
        if(modelList == null || modelList.size() == 0) {
            return;
        }

        // Grab view element references
        CardModel cardmodel = modelList.get(position);

        String spots = "Open spots: " + String.valueOf(cardmodel.getNumSpotsLeft());
        String arrival_time = "Will arrive at: " + String.valueOf(cardmodel.getTimeToArrive());
        String walking_time = "Walk distance in: " + String.valueOf(cardmodel.getWalkingTime());

        holder.garage.setText(String.valueOf(cardmodel.getgarage()));
        holder.spots_left.setText(spots);
        holder.arrival_time.setText(arrival_time);
        holder.walking_time.setText(walking_time);
    }

    public class SwipeableCardViewHolder extends RecyclerView.ViewHolder {
        protected TextView garage;
        protected TextView spots_left;
        protected TextView arrival_time;
        protected TextView walking_time;

        public SwipeableCardViewHolder(View itemView) {
            super(itemView);

            garage = itemView.findViewById(R.id.garage);
            spots_left = itemView.findViewById(R.id.spots_left);
            arrival_time = itemView.findViewById(R.id.arrival_time);
            walking_time = itemView.findViewById(R.id.walking_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gs = new Gson();

                    String currentUserJSON = gs.toJson(currentUser);
                    String closestGaragesJSON = gs.toJson(closestGarages);
                    String chosenGarageJSON = gs.toJson(garage.getText().toString());

                    Intent intent = new Intent(v.getContext(), NavigationActivity.class);

                    // re-pack
                    intent.putExtra("current user", currentUserJSON);
                    intent.putExtra("closest garages", closestGaragesJSON);
                    intent.putExtra("chosen garage", chosenGarageJSON);

                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
