package com.example.lab5_remake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Parser {

    public interface XMLParseListener {
        void onXMLParsed(ArrayList<String> result);
    }

    public interface JSONParseListener {
        void onJSONParsed(ArrayList<String> result);
    }

    public static void parseJSONData(String jsonData, JSONParseListener listener) {
        new Thread(() -> {
            ArrayList<String> currencyList = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray dataArray = jsonObject.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject currencyObject = dataArray.getJSONObject(i);
                    String currencyName = currencyObject.getString("name");
                    currencyList.add(currencyName);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            listener.onJSONParsed(currencyList);
        }).start();
    }
}
