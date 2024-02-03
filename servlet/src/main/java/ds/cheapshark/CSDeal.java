/**
 * Author: George Saito (takatoms)
 * Last Modified: November 28th, 2022
 *
 * One of the class to parse the JSON response from Cheap Shark
 */
package ds.cheapshark;

public class CSDeal {
    String storeID;
    String dealID;
    String price;
    String retailPrice;
    String savings;

    public String getPrice() {
        return price;
    }

    public String getDealID() {
        return dealID;
    }

    public String getStoreID() {
        return storeID;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public String getSavings() {
        return savings;
    }
}
