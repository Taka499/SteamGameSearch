/**
 * Author: George Saito (takatoms)
 * Last Modified: November 28th, 2022
 *
 * This program is the web app servlet of Project 4 Task2
 */
package ds;

import java.io.*;
import java.net.URLDecoder;
import java.sql.Timestamp;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.bson.Document;
import org.bson.types.ObjectId;

@WebServlet(name = "SteamGamesServlet", urlPatterns = {"/api/*", "/getSteamGames/*"})
public class SteamGamesServlet extends HttpServlet {

    private SteamGamesModel sgm = null; // The "business model" for this app

    // Initiate this servlet by instantiating the model that it will use.
    @Override
    public void init() { sgm = new SteamGamesModel(); }

    /**
     * This is doGet function that treat user's GET request
     * Only the API request will invoke this function
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String searchWord = null;
        String responseJson = "";
        long timeStart = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(timeStart);
        try {
            // parse searchWord from request URL
            searchWord = getQuery(request);

            // doGameSearch using the model
            responseJson = sgm.doGameSearch(searchWord);

            // prepare the response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            PrintWriter out = response.getWriter();
            out.println(responseJson);
        }
        catch (InvalidRequestException e) {
            // if the API request is invalid, return 400
            response.setStatus(400);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            responseJson = "Invalid Request";

            PrintWriter out = response.getWriter();
            out.println(responseJson);
        }
        catch (InvalidQueryPathException e) {
            // 404 not found
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        catch (EmptyResponseException e) {
            // if the response from Cheap Shark is empty, return 400
            response.setStatus(400);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            responseJson = "[]";

            PrintWriter out = response.getWriter();
            out.println(responseJson);
        }
        long timeEnd = System.currentTimeMillis();

        // log the information to MongoDB
        logMongoDB(searchWord, responseJson, timestamp, request, timeEnd-timeStart);
    }

    /**
     * This function will get the searchWord sent through API request from HTTP URI
     * It will also check the validness of API Request
     * @param request
     * @return
     * @throws IOException
     * @throws InvalidQueryPathException
     * @throws InvalidRequestException
     */
    private String getQuery(HttpServletRequest request) throws IOException, InvalidQueryPathException, InvalidRequestException {
        String context = request.getContextPath();
        String uri = request.getRequestURI();
        String servletPath = request.getServletPath();
        // get the queryPath (mmm/nnn/ooo/ppp/aaa?bbb=x&ccc=y...)
        if (uri.length() <= servletPath.length() + context.length()) {
            throw new InvalidRequestException("Invalid Request");
        }
        String queryPath = uri.substring(servletPath.length() + context.length() + 1);
        // and the query (bbb=x&ccc=y...)
        String query = request.getQueryString();

        // Check if the query is valid
        // The motto is to be robust to the input: ignore any irrelevant path
        // parse the queryPath with '/' -> (["mmm", "nnn", "ooo", "ppp", "aaa?bbb=x&ccc=y..."])
        String[] parsedWithSlash = queryPath.split("/");
        // parse the last element in parsedWithSlash with '?' -> (["aaa", "bbb=x&ccc=y..."])
        String[] parsedWithQuestionMark = parsedWithSlash[parsedWithSlash.length-1].split("\\?");
        // if the parameter is not 'games', then throw an error telling that it's 404
        String path = URLDecoder.decode(parsedWithQuestionMark[0], "UTF-8");
        if (!"games".equalsIgnoreCase(path)) {
            throw new InvalidQueryPathException("Invalid Path"); // will return 404
        }

        // Then check the query. We will still ignore those irrelevant name-value pairs
        // we will only look for the name of 'searchWord', and the value following it
        if (query != null) {
            for (String q : query.split("&")) {
                String[] nameValuePair = q.split("=");
                String name = URLDecoder.decode(nameValuePair[0], "UTF-8");
                if ("searchWord".equalsIgnoreCase(name)) {
                    // ignore all the other name
                    if (nameValuePair.length > 1) {
                        String value = URLDecoder.decode(nameValuePair[1], "UTF-8");
                        return value;
                    }
                    // if nameValuePair has length of 1, the query is invalid
                }
            }
        }
        throw new InvalidRequestException("Invalid Request"); // will return 400
    }

    /**
     * This function will connect MongoDB and store the user's request to the log database
     * @param searchWord
     * @param responseJson
     * @param timestamp
     * @param request
     * @param timeLapsed
     */
    private void logMongoDB(String searchWord, String responseJson, Timestamp timestamp, HttpServletRequest request, long timeLapsed) {
        String mongoURI = "mongodb+srv://" + System.getenv("MONGODB_ACCESS_ID") + ":" + System.getenv("MONGODB_ACCESS_PWD") + "@cluster0.bkh0lsv.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(mongoURI)) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("SteamGameSearchDB");

            // insert the log to MongoDB
            Document doc = new Document()
                    .append("_id", new ObjectId())
                    .append("searchWord", searchWord)
                    .append("timeStamp", timestamp.toString())
                    .append("userMobileModel", request.getHeader("User-Agent").split("\\s")[0])
                    .append("firstApiRequest", sgm.getFirstApiRequest())
                    .append("firstCheapSharkResponse", sgm.getFirstCSResponse())
                    .append("secondApiRequest", sgm.getSecondApiRequest())
                    .append("secondCheapSharkResponse", sgm.getSecondCSResponse())
                    .append("numResultFound", sgm.getNumResult())
                    .append("reply2User", responseJson)
                    .append("searchTimeSpent", timeLapsed);
            mongoDatabase.getCollection("requestInfo").insertOne(doc);
        }
        catch (MongoException e) {
            System.err.println("Unable to insert: " + e);
        }
    }

    public void destroy() {
    }
}
