/**
 * Author: George Saito (takatoms)
 * Last Modified: November 28th, 2022
 *
 * This program will send the API request to the web application
 * in the background thread
 * The main structure is adjusted from Lab 7 InterestingPicture
 */
package edu.cmu.steamgamesearch;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import edu.cmu.steamgamesearch.response.Game;

public class GetGames {
    SteamGameSearch sgs = null;
    String searchWord = null;
    Game[] games = null;
    Bitmap[] thumbs = null;

    public void search(String searchWord, Activity activity, SteamGameSearch sgs) {
        this.sgs = sgs;
        this.searchWord = searchWord;
        new BackgroundTask(activity).execute();
    }

    private class BackgroundTask {
        private Activity activity; // This is the UI thread

        public BackgroundTask (Activity activity) {
            this.activity = activity;
        }

        private void startBackground() {
            new Thread(() -> {
                doInBackground();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onPostExecute();
                    }
                });
            }).start();
        }

        private void execute() {
            startBackground();
        }

        private void doInBackground() {
            Gson gson = new Gson();
            String responseJson = search(searchWord);
            // Convert the responseJson to Game class object and Bitmap image
            // Check if the response is empty
            if (!"".equals(responseJson) && !"[]".equals(responseJson)) {
                games = gson.fromJson(responseJson, Game[].class);
                thumbs = new Bitmap[games.length];
                getThumbnails();
            }
        }

        public void onPostExecute() {
            sgs.searchResultReady(games, thumbs);
        }

        /**
         * This function will get the thumbnail image from its url and store
         * it as BitMap in the local field
         */
        private void getThumbnails() {
            for (int i=0; i<games.length; i++) {
                String thumbURL = games[i].getThumb();
                try {
                    URL url = new URL(thumbURL);
                    thumbs[i] = getRemoteImage(url);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    thumbs[i] = null;
                }
            }
        }

        /**
         * This will make api request to the web application
         * @param searchWord
         * @return
         */
        private String search(String searchWord) {
            String responseJson = "";
            try {
                String apiRequest = String.format("%s/games?searchWord=%s", BuildConfig.SERVLET_API, URLEncoder.encode(searchWord, "UTF-8"));
                responseJson = sendRequest(apiRequest);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return responseJson;
        }

        /**
         * The function for sending Http request
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
                else if (status == 400) {
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

        /*
         * Given a URL referring to an image, return a bitmap of that image
         * This code comes from Lab 7 GetPicture.java
         */
        @RequiresApi(api = Build.VERSION_CODES.P)
        private Bitmap getRemoteImage(final URL url) {
            try {
                final URLConnection conn = url.openConnection();
                conn.connect();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                Bitmap bm = BitmapFactory.decodeStream(bis);
                return bm;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
