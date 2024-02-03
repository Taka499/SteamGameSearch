/**
 * Author: George Saito (takatoms)
 * Last Modified: November 28th, 2022
 *
 * This is Game class that will be used to parse the JSON string response from
 * the web application
 */
package edu.cmu.steamgamesearch.response;

public class Game {
    String title;
    String steamAppID;
    String cheapest;
    String thumb;

    public Game (String title, String steamAppID, String cheapest, String thumb) {
        this.title = title;
        this.steamAppID = steamAppID;
        this.cheapest = cheapest;
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public String getSteamAppID() {
        return steamAppID;
    }

    public String getCheapest() {
        return cheapest;
    }

    public String getThumb() {
        return thumb;
    }
}
