/**
 * Author: George Saito (takatoms)
 * Last Modified: November 28th, 2022
 *
 * One of the class to parse the JSON response from Cheap Shark
 */
package ds.cheapshark;

public class CSGameInfo {
    String title;
    String steamAppID;
    String thumb;

    public String getSteamAppID() {
        return steamAppID;
    }

    public String getThumb() {
        return thumb;
    }

    public String getTitle() {
        return title;
    }
}
