package com.example.thewizard.cjuliaol.catalog.parsers;

import com.example.thewizard.cjuliaol.catalog.model.Flower;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjuliaol on 18-Nov-15.
 */
public class FlowerJSONParser {

    public static List<Flower> parseFeed(String content) {

        try {
            JSONArray jsonArray = new JSONArray(content);
            List<Flower> flowerList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Flower flower = new Flower();

                flower.setProductId(jsonObject.getInt("productId"));
                flower.setName(jsonObject.getString("name"));
                flower.setCategory(jsonObject.getString("category"));
                flower.setInstructions(jsonObject.getString("instructions"));
                flower.setPrice(jsonObject.getDouble("price"));
                flower.setPhoto(jsonObject.getString("photo"));

                flowerList.add(flower);

            }
            return flowerList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
