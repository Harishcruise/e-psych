package com.example.azure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GetSentiment {

// ***********************************************
// *** Update or verify the following values. ***
// **********************************************

    // Replace the accessKey string value with your valid access key.
    static String accessKey = "7e61f11c688540858ca0f08e2f4d9c1c";

// Replace or verify the region.

// You must use the same region in your REST API call as you used to obtain your access keys.
// For example, if you obtained your access keys from the westus region, replace
// "westcentralus" in the URI below with "westus".

    // NOTE: Free trial access keys are generated in the westcentralus region, so if you are using
// a free trial access key, you should not need to change this region.
    static String host = "https://eastus.api.cognitive.microsoft.com";

    static String path = "/text/analytics/v2.0/sentiment";

    public static String getTheSentiment (Documents documents) throws Exception {
        String text = new Gson().toJson(documents);
        byte[] encoded_text = text.getBytes("UTF-8");

        URL url = new URL(host+path);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/json");
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", accessKey);
        connection.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.write(encoded_text, 0, encoded_text.length);
        wr.flush();
        wr.close();

        StringBuilder response = new StringBuilder ();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        return response.toString();
    }

    public static String prettify(String json_text) {
        JsonParser parser = new JsonParser();
        //JsonElement tree = parser.parse(json_text);
        JsonObject json = parser.parse(json_text).getAsJsonObject();
        JsonElement documents = json.get("documents");
        boolean isarray = documents.isJsonArray();
       // boolean isobject = documents.isJsonObject();
        JsonArray array = (JsonArray) documents;
        //String title = ((JsonObject)array.get(0)).get("title");
        JsonObject document = array.get(0).getAsJsonObject();
        JsonElement scorejson =  document.get("score");
        String stringscore = scorejson.toString();
        //Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return stringscore;
    }

    public static String makeCall (String feeling) {
        String score = "Failed";
        try {
            Documents documents = new Documents ();
            documents.add ("1", "en", feeling);
            //documents.add ("2", "es", "Este ha sido un dia terrible, llegué tarde al trabajo debido a un accidente automobilistico.");

            String response = getTheSentiment (documents);
            score = (prettify (response));
        }
        catch (Exception e) {
            System.out.println (e);
        }
        return score;
    }

}
