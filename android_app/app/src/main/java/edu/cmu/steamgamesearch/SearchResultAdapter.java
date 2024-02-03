/**
 * Author: George Saito (takatoms)
 * Last Modified: November 28th, 2022
 *
 * This program is the Adapter class that is used for defining RecyclerView
 */
package edu.cmu.steamgamesearch;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import edu.cmu.steamgamesearch.response.Game;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private Game[] games;
    private Bitmap[] thumbs;

    /**
     * define the ViewHolder that holds the CardView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;

        public ViewHolder (View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }

        public CardView getCardView() {
            return cardView;
        }
    }

    // Initialize
    public SearchResultAdapter(Game[] games, Bitmap[] thumbs) {
        this.games = games;
        this.thumbs = thumbs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // create a new view
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    /**
     * Apply the data to the corresponding views in Android app
     * @param viewHolder
     * @param idx
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int idx) {
        // retrieve the views in the card
        ImageView thumb = (ImageView) viewHolder.getCardView().findViewById(R.id.thumb);
        TextView gameTitle = (TextView) viewHolder.getCardView().findViewById(R.id.game_title);
        TextView steamAppID = (TextView) viewHolder.getCardView().findViewById(R.id.steamAppID);
        TextView cheapest = (TextView) viewHolder.getCardView().findViewById(R.id.cheapest);
        // assign the values in games and thumbs
        if (thumbs[idx] != null) {
            thumb.setImageBitmap(thumbs[idx]);
        }
        if (games[idx] != null) {
            gameTitle.setText(games[idx].getTitle());
            steamAppID.setText(games[idx].getSteamAppID());
            cheapest.setText(games[idx].getCheapest());
        }

    }

    @Override
    public int getItemCount() {
        return games.length;
    }
}
