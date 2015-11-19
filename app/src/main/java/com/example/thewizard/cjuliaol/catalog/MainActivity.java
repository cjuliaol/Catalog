package com.example.thewizard.cjuliaol.catalog;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thewizard.cjuliaol.catalog.model.Flower;
import com.example.thewizard.cjuliaol.catalog.parsers.FlowerJSONParser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView output;
    ProgressBar progressBar;
    List<MyTask> mTasks;
    List<Flower> mFlowerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//		Initialize the TextView for vertical scrolling
        output = (TextView) findViewById(R.id.textView);
        output.setMovementMethod(new ScrollingMovementMethod());

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        mTasks = new ArrayList<>();

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
        MyTask task = new MyTask();
        // With default executor: serial processing
         task.execute(uri);

        //With executor for parallel processing
        // task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "param1", "param2", "param3");
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

        for (Flower flower: mFlowerList ) {
            output.append(flower.getName() + "\n");
        }


    }

    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            //updateDisplay("Starting task");

            if (mTasks.size() == 0) {
                progressBar.setVisibility(View.VISIBLE);
            }
            mTasks.add(this);

        }

        @Override
        protected String doInBackground(String... params) {

            /* example
            for (int i = 0; i < params.length; i++) {
                publishProgress("Working with " + params[i]);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/

           String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {

            mFlowerList = FlowerJSONParser.parseFeed(result);
            updateDisplay();

            mTasks.remove(this);
            if (mTasks.size() == 0) {
                progressBar.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        protected void onProgressUpdate(String... values) {

            //updateDisplay(values[0]);
        }
    }
}
