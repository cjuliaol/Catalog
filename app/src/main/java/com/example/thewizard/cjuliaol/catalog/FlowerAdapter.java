package com.example.thewizard.cjuliaol.catalog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.thewizard.cjuliaol.catalog.model.Flower;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by cjuliaol on 19-Nov-15.
 */
public class FlowerAdapter extends ArrayAdapter<Flower> {

    private static final String TAG = "FlowerAdapterLog";
    private Context mContext;
    private List<Flower> mFlowerList;
    private LruCache<Integer, Bitmap> imageCache;
    private RequestQueue mQueue;

    public FlowerAdapter(Context context, int resource, List<Flower> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mFlowerList = objects;

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        imageCache = new LruCache<>(cacheSize);

        mQueue = Volley.newRequestQueue(context);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_flower, parent, false);

        // Display flower name in the textview widget
        final Flower flower = mFlowerList.get(position);

        TextView flowerName = (TextView) view.findViewById(R.id.flower_name);
        flowerName.setText(flower.getName());

        // Display the flower photo in the imageview widget
        Bitmap bitmap = imageCache.get(flower.getProductId());
        final ImageView flowerImage = (ImageView) view.findViewById(R.id.flower_image);

        if (bitmap != null) {
            flowerImage.setImageBitmap(bitmap);
        } else {
            String imageUrl = MainActivity.PHOTO_BASE_URL + flower.getPhoto();
            ImageRequest request = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    flowerImage.setImageBitmap(response);
                    imageCache.put(flower.getProductId(), response);

                }
            }, 80
                    , 80
                    , ImageView.ScaleType.CENTER_CROP
                    , Bitmap.Config.ARGB_8888
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, error.getMessage());
                }
            }
            );

            mQueue.add(request);

        }


        return view;


    }


}
