/**
 * Author: George Saito (takatoms)
 * Last Modified: November 28th, 2022
 *
 * One of the class to parse the JSON response from Cheap Shark
 */
package ds.cheapshark;

import java.util.List;

public class CSGameLookupResponse {
    CSGameInfo info;
    CSCheapestPriceEver cheapestPriceEver;
    List<CSDeal> deals;

    public CSGameInfo getInfo() {
        return info;
    }

    public CSCheapestPriceEver getCheapestPriceEver() {
        return cheapestPriceEver;
    }

    public List<CSDeal> getDeals() {
        return deals;
    }
}
