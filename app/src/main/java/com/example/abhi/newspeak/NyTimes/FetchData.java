package com.example.abhi.newspeak.NyTimes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.abhi.newspeak.*;
import com.example.abhi.newspeak.NyTimes.TopStories.Technology.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FetchData extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    List<News> NYTnews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fetch_data);
        NYTnews = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        new GetJSON().execute("https://api.nytimes.com/svc/topstories/v2/technology.json?api-key=ccde25f273d54d74be7269f71376890c");

    }

    private class GetJSON extends AsyncTask<String, String, String> {


        HttpURLConnection connection = null;
        BufferedReader reader = null;

        @Override
        protected String doInBackground(String... strings) {
            StringBuffer buffer = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            return buffer.toString(); // JSON data from the link

        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);

            // calling adapter after json is fetched
            adapter = new RecyclerAdapter(FetchData.this, json);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(FetchData.this));


        }
    }

   /* private class LoadImage extends AsyncTask<String, String, Bitmap> {


        protected Bitmap doInBackground(String... args) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {

            if (bitmap != null) {
                // calling adapter after json is fetched
                adapter = new RecyclerAdapter(FetchData.this, resultObject, bitmap);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(FetchData.this));
            }
        }
    }*/


}
