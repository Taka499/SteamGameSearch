/**
 * Author: George Saito (takatoms)
 * Last Modified: November 28th, 2022
 *
 * One of the class to parse the JSON response from Cheap Shark
 */
package ds.cheapshark;

public class CSListOfGamesItem {
    String gameID;
    String steamAppID;
    String cheapest;
    String cheapestDealID;
    String external;
    String thumb;

    public String getGameID() {
        return gameID;
    }

    public String getSteamAppID() {
        return steamAppID;
    }

    public String getCheapest() {
        return cheapest;
    }

    public String getCheapestDealID() {
        return cheapestDealID;
    }

    public String getExternal() {
        return external;
    }

    public String getThumb() {
        return thumb;
    }
}
