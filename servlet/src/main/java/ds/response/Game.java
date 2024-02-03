/**
 * Author: George Saito (takatoms)
 * Last Modified: November 28th, 2022
 *
 * This is the class for the response to the Android user. It will be passed to GSON
 * to form a JSON format string
 */
package ds.response;

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
