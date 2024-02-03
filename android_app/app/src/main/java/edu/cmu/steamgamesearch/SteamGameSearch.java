/**
 * Author: George Saito (takatoms)
 * Last Modified: November 28th, 2022
 *
 * This program is the activity class of Android app of Project 4 Task2
 */
package edu.cmu.steamgamesearch;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.cmu.steamgamesearch.response.Game;

public class SteamGameSearch extends AppCompatActivity {
    SteamGameSearch me = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SteamGameSearch ma = this;

        /*
         * Find the "submit" button, and add a listener to it
         */
        Button submitButton = (Button)findViewById(R.id.submit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tell the user that app is searching
                RecyclerView searchResult = (RecyclerView) findViewById(R.id.search_result_recyclerview);
                searchResult.setVisibility(View.INVISIBLE);
                TextView searchResultNotice = (TextView) findViewById(R.id.search_result_notice);
                searchResultNotice.setText("Searching ...");
                searchResultNotice.setVisibility(View.VISIBLE);

                // fetch the search word and run the script for api request
                String searchWord = ((EditText)findViewById(R.id.searchWord)).getText().toString();
                GetGames gg = new GetGames();
                gg.search(searchWord, me, ma);
            }
        });
    }

    /**
     * This will be called by the GetGames.java
     * to display the response to the app
     * @param games
     * @param thumbs
     */
    public void searchResultReady(Game[] games, Bitmap[] thumbs) {
        TextView searchResultNotice = (TextView) findViewById(R.id.search_result_notice);
        RecyclerView searchResult = (RecyclerView) findViewById(R.id.search_result_recyclerview);
        TextView searchView = (EditText) findViewById(R.id.searchWord);

        if (games != null) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            searchResult.setLayoutManager(layoutManager);
            searchResult.setItemAnimator(new DefaultItemAnimator());

            SearchResultAdapter adapter = new SearchResultAdapter(games, thumbs);
            searchResult.setAdapter(adapter);

            searchResultNotice.setText(String.format("Search Result of %s (%d found)", searchView.getText(), adapter.getItemCount()));
            searchResultNotice.setVisibility(View.VISIBLE);
            searchResult.setVisibility(View.VISIBLE);
        }
        else {
            searchResultNotice.setText("Games Not Found");
            searchResultNotice.setVisibility(View.VISIBLE);
        }

    }
}
