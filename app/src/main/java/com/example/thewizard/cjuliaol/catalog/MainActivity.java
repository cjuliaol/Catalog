package com.example.thewizard.cjuliaol.catalog;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thewizard.cjuliaol.catalog.model.Flower;
import com.example.thewizard.cjuliaol.catalog.parsers.FlowerJSONParser;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {

    TextView output;
    ProgressBar progressBar;

    List<Flower> mFlowerList;
    public static final String PHOTO_BASE_URL = "http://services.hanselandpetal.com/photos/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //		Initialize the TextView for vertical scrolling
        //  output = (TextView) findViewById(R.id.textView);
        //   output.setMovementMethod(new ScrollingMovementMethod());

        progressBar = (ProgressBar) findViewById(R.id.progressBar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_do_task) {

            if (isOnline()) {
                requestData("http://services.hanselandpetal.com/feeds/flowers.json");

            } else {
                Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
            }

        }
        return false;
    }

    private void requestData(String uri) {
//        RequestPackage requestPackage = new RequestPackage();
//        requestPackage.setUri(uri);

        StringRequest request = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mFlowerList = FlowerJSONParser.parseFeed(response);
                        updateDisplay();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT);
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    protected boolean isOnline() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }

    }

    protected void updateDisplay() {

        /*for (Flower flower: mFlowerList ) {
            output.append(flower.getName() + "\n");
        }*/
        FlowerAdapter adapter = new FlowerAdapter(this, R.layout.item_flower, mFlowerList);
        setListAdapter(adapter);
    }


}
