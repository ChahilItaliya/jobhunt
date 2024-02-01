package com.example.jobhunt;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<Card> cardList;
    private List<Card> filteredList;
    public CardAdapter.onCardClicked listener;


    public CardAdapter(List<Card> cardList) {
        this.cardList = cardList;
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
        this.filteredList = new ArrayList<>(cardList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Card card = cardList.get(position);
        holder.titleTextView.setText(card.getTitle());
        holder.descriptionTextView.setText(card.getDescription());
        // Set photo (you might use a library like Picasso or Glide to load images)

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCardClicked(card); // Pass the clicked card to the listener
                }
            }
        });
    }
    public interface onCardClicked {
        void onCardClicked(Card card);
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;

        public CardViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            descriptionTextView = itemView.findViewById(R.id.description_text_view);
            // Photo view initialization
        }
    }
}
