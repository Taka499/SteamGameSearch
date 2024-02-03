/**
 * Author: George Saito (takatoms)
 * Last Modified: November 28th, 2022
 *
 * This program is the web app model of Project 4 Task2
 */
package ds;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ds.cheapshark.CSGameLookupResponse;
import ds.cheapshark.CSListOfGamesItem;
import ds.response.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SteamGamesModel {
    private String firstApiRequest = null;
    private String firstCSResponse = null;
    private String secondApiRequest = null;
    private String secondCSResponse = null;
    private int numResult = 0;

    /**
     * This function will reset the private local fields
     */
    private void resetFields() {
        this.firstApiRequest = null;
        this.firstCSResponse = null;
        this.secondApiRequest = null;
        this.secondCSResponse = null;
        this.numResult = 0;
    }

    /**
     * This function will be called by the servlet to make API request
     * @param searchWord
     * @return
     * @throws EmptyResponseException
     * @throws UnsupportedEncodingException
     */
    public String doGameSearch(String searchWord) throws EmptyResponseException, UnsupportedEncodingException {
        // reset the private fields for this search
        resetFields();

        Gson gson = new Gson();
        // GET List of Games using searchWord
        String listOfGamesJson = getListOfGames(searchWord);

        // Deserialize from Json to CSGameListItem[] array
        CSListOfGamesItem[] listOfGames = gson.fromJson(listOfGamesJson, CSListOfGamesItem[].class);

        // Pass the list to the method getMultipleLookup()
        // From Json String, get the map of lookup response
        String multipleLookupResponseJson = getMultipleLookup(listOfGames);
        Type multipleLookupResponseType = new TypeToken<Map<String, CSGameLookupResponse>>() {}.getType();
        Map<String, CSGameLookupResponse> multipleLookupResponse = gson.fromJson(multipleLookupResponseJson, multipleLookupResponseType);

        // Iterate over the map and Construct Game class from CSGameLookupResponse value in the map
        // and append it to the list
        List<Game> gameList = new ArrayList<>();
        CSGameLookupResponse gameLookupResponse;
        for (Map.Entry<String, CSGameLookupResponse> entry : multipleLookupResponse.entrySet()) {
            gameLookupResponse = entry.getValue();
            // Construct Game class object
            Game game = new Game(
                    gameLookupResponse.getInfo().getTitle(),
                    gameLookupResponse.getInfo().getSteamAppID(),
                    gameLookupResponse.getCheapestPriceEver().getPrice(),
                    gameLookupResponse.getInfo().getThumb()
            );
            // append it to the list
            gameList.add(game);
        }

        // record the number of results found
        this.numResult = gameList.size();

        // Convert the list to json gameListJson
        String gameListJson = gson.toJson(gameList);

        // return the json
        return gameListJson;
    }

    /**
     * This function will make API request to retrieve the details of games in the list
     * @param listOfGames
     * @return
     */
    private String getMultipleLookup(CSListOfGamesItem[] listOfGames) {
        // create the API response for multiple lookup
        // The API can only look up for 25 games as maximum
        String apiRequest = String.format("https://www.cheapshark.com/api/1.0/games?ids=%s", listOfGames[0].getGameID());
        for (int i=1; i<Math.min(25, listOfGames.length); i++) {
            apiRequest += "," + listOfGames[i].getGameID();
        }

        // record the api request
        this.secondApiRequest = apiRequest;

        String responseJson = sendRequest(apiRequest);

        // record the response
        this.secondCSResponse = responseJson;

        return responseJson;
    }

    /**
     * This function will make the API request to get the list of games using the search words
     * @param searchWord
     * @return
     * @throws EmptyResponseException
     * @throws UnsupportedEncodingException
     */
    private String getListOfGames(String searchWord) throws EmptyResponseException, UnsupportedEncodingException {
        String apiRequest = String.format("https://www.cheapshark.com/api/1.0/games?title=%s&exact=0", URLEncoder.encode(searchWord, "UTF-8"));

        // record the api request
        this.firstApiRequest = apiRequest;

        String responseJson = sendRequest(apiRequest);

        // record the response
        this.firstCSResponse = responseJson;

        // throw Exception when the response is empty
        if ("[]".equals(responseJson) || "".equals(responseJson)) {
            throw new EmptyResponseException("Empty Response");
        }

        return responseJson;
    }

    /**
     * This function will send the API request
     * @param apiRequest
     * @return
     */
    private String sendRequest(String apiRequest){
        HttpURLConnection connection;
        int status = 0;

        String response = "";
        try {
            URL url = new URL(apiRequest);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            status = connection.getResponseCode();

            if (status == 200) {
                response = getResponseBody(connection);
            }

            connection.disconnect();
        } catch (MalformedURLException e) {
            System.out.println("URL Exception thrown" + e);
        } catch (IOException e) {
            System.out.println("IO Exception thrown" + e);
        } catch (Exception e) {
            System.out.println("IO Exception thrown" + e);
        }

        return response;
    }

    // Gather up a response body from the connection
    // and close the connection.
    // This code comes from Lab 8 code
    private String getResponseBody(HttpURLConnection conn) {
        String responseText = "";
        try {
            String output = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            while ((output = br.readLine()) != null) {
                responseText += output;
            }
            conn.disconnect();
        } catch (IOException e) {
            System.out.println("Exception caught " + e);
        }
        return responseText;
    }

    public String getFirstApiRequest() {
        return this.firstApiRequest;
    }

    public String getFirstCSResponse() {
        return firstCSResponse;
    }

    public String getSecondApiRequest() {
        return secondApiRequest;
    }

    public String getSecondCSResponse() {
        return secondCSResponse;
    }

    public int getNumResult() {
        return numResult;
    }
}
