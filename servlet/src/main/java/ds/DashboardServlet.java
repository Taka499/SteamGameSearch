/**
 * Author: George Saito (takatoms)
 * Last Modified: November 28th, 2022
 *
 * This program is the dashboard servlet of Project 4 Task2
 */
package ds;

import com.mongodb.client.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.Document;

import java.io.IOException;
import java.util.*;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {
    // Define the interesting analytics
    private int totalRequest;
    private Map<String, Integer> countSearched;
    private Map<String, Integer> countPhones;
    private long totalLatency;
    private List<UserLog> userLogs;

    public void init() {
        this.totalRequest = 0;
        this.countSearched = new HashMap<>();
        this.countPhones = new HashMap<>();
        this.totalLatency = 0;
        this.userLogs = new ArrayList<>();
    }

    /**
     * This function will treat the request to GET analytics dashboard
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        // access MongoDB for the data
        init();
        accessMongoDB();

        // sort the map to get the top searchWord and most common device of users
        Map.Entry<String, Integer> maxCountSearchedEntry = Collections.max(countSearched.entrySet(), Map.Entry.comparingByValue());
        String topSearchedWord = maxCountSearchedEntry.getKey();
        Map.Entry<String, Integer> maxCountPhonesEntry = Collections.max(countPhones.entrySet(), Map.Entry.comparingByValue());
        String mostCommonDevice = maxCountPhonesEntry.getKey();

        // set attributes for jsp
        request.setAttribute("totalRequest", this.totalRequest);
        request.setAttribute("topSearchedWord", topSearchedWord);
        request.setAttribute("mostCommonDevice", mostCommonDevice);
        request.setAttribute("averageLatency", String.valueOf(this.totalLatency / this.totalRequest));
        request.setAttribute("userLogs", this.userLogs);

        // apply the changes to the request
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }

    /**
     * This function will access MongoDB and store the information from user to the database
     */
    private void accessMongoDB() {
        // access MongoDB
        String mongoURI = "mongodb+srv://" + System.getenv("MONGODB_ACCESS_ID") + ":" + System.getenv("MONGODB_ACCESS_PWD") + "@cluster0.bkh0lsv.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(mongoURI)) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("SteamGameSearchDB");
            MongoCollection<Document> collection = mongoDatabase.getCollection("requestInfo");

            // get the analytics from the database
            // total number of requests received
            this.totalRequest = (int) collection.countDocuments();

            MongoCursor<Document> cursor = collection.find().iterator();

            try {
                while (cursor.hasNext()) {
                    // get the searchWord, mobileModel, and searchTimeSpent from database
                    Document doc = cursor.next();
                    UserLog userLog = toUserLog(doc);
                    String searchWord = userLog.getSearchWord();
                    String mobileModel = userLog.getMobileModel();
                    long searchTime = userLog.getSearchTimeSpent();

                    // add them to the class fields
                    updateCountSearched(searchWord);
                    updateCountPhones(mobileModel);
                    this.totalLatency += searchTime;
                    userLogs.add(userLog);
                }
            } finally {
                cursor.close();
            }
        }
    }

    /**
     * This function will convert Document class to UserLog class
     * @param doc
     * @return
     */
    private UserLog toUserLog(Document doc) {
        UserLog userLog = new UserLog(
                doc.getObjectId("_id").toString(),
                doc.getString("searchWord"),
                doc.getString("timeStamp"),
                doc.getString("userMobileModel"),
                doc.getInteger("numResultFound"),
                doc.getLong("searchTimeSpent"),
                doc.getString("firstApiRequest"),
                doc.getString("firstCheapSharkResponse"),
                doc.getString("secondApiRequest"),
                doc.getString("secondCheapSharkResponse"),
                doc.getString("reply2User")
        );
        return userLog;
    }

    /**
     * This function will add the searchWord to the HashMap countSearched
     * @param searchWord
     */
    private void updateCountSearched(String searchWord) {
        if (countSearched.containsKey(searchWord)) {
            countSearched.put(searchWord, countSearched.get(searchWord) + 1);
        }
        else {
            countSearched.put(searchWord, 0);
        }
    }

    /**
     * This function will add the mobile device to HashMap countPhones
     * @param mobileModel
     */
    private void updateCountPhones(String mobileModel) {
        if (countPhones.containsKey(mobileModel)) {
            countPhones.put(mobileModel, countPhones.get(mobileModel) + 1);
        }
        else {
            countPhones.put(mobileModel, 0);
        }
    }

    public void destroy() {
    }
}
