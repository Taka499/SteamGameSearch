/**
 * Author: George Saito (takatoms)
 * Last Modified: November 28th, 2022
 *
 * One of the class to parse the JSON response from Cheap Shark
 */
package ds.cheapshark;

public class CSCheapestPriceEver {
    String price;
    int date;

    public String getPrice() {
        return price;
    }

    public int getDate() {
        return date;
    }
}
