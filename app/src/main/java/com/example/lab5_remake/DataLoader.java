package com.example.lab5_remake;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataLoader {

    public interface DataRetrievalListener {
        void onDataRetrieved(String result);
    }

    public void retrieveData(String apiUrl, DataRetrievalListener listener) {
        new Thread(() -> {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    InputStream in = urlConnection.getInputStream();
                    String result = readStream(in);
                    listener.onDataRetrieved(result);
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                listener.onDataRetrieved(null);
            }
        }).start();
    }

    private String readStream(InputStream is) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(is);
        char[] buffer = new char[1024];
        int bytesRead;

        while ((bytesRead = reader.read(buffer)) != -1) {
            stringBuilder.append(buffer, 0, bytesRead);
        }

        return stringBuilder.toString();
    }
}
