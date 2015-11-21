package com.example.thewizard.cjuliaol.catalog;

import android.util.Base64;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cjuliaol on 18-Nov-15.
 */


public class HttpManager {

    public static String getData(RequestPackage p) {

        String uri = p.getUri();
        BufferedReader reader = null;
        HttpURLConnection connection = null;

        if (p.getMethod().equals("GET") ) {

            uri += "?"+p.getEncodedParams();
        }

        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(p.getMethod());

            JSONObject jsonObject = new JSONObject(p.getParams());
            String params = "params="+jsonObject.toString();

            if( p.getMethod().equals("POST")) {
              connection.setDoOutput(true);
                OutputStreamWriter writer =new OutputStreamWriter(connection.getOutputStream());
                writer.write(params);
                writer.flush();

            }

            StringBuilder builder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
            return builder.toString();

        } catch (Exception e) {
            e.printStackTrace();

            try {
                int status = connection.getResponseCode();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }


    }


    public static String getData(String uri, String username, String password) {

        BufferedReader reader = null;
        HttpURLConnection connection = null;

        byte[] loginBytes = (username + ":" + password).getBytes();
        StringBuilder loginBuilder = new StringBuilder()
                .append("Basic ")
                .append(Base64.encodeToString(loginBytes, Base64.DEFAULT));

        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Authorization", loginBuilder.toString());

            StringBuilder builder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
            return builder.toString();

        } catch (Exception e) {
            e.printStackTrace();

            //  int status = connection.getResponseCode();

            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }


    }
}
