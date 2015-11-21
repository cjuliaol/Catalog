package com.example.thewizard.cjuliaol.catalog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thewizard.cjuliaol.catalog.model.Flower;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by cjuliaol on 19-Nov-15.
 */
public class FlowerAdapter extends ArrayAdapter<Flower> {

    private Context mContext;
    private List<Flower> mFlowerList;

    public FlowerAdapter(Context context, int resource, List<Flower> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mFlowerList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_flower, parent, false);

        // Display flower name in the textview widget

        Flower flower = mFlowerList.get(position);

        TextView flowerName = (TextView) view.findViewById(R.id.flower_name);
        flowerName.setText(flower.getName());

        // Display the flower photo in the imageview widget

        if (flower.getBitmap() != null) {
            ImageView flowerImage = (ImageView) view.findViewById(R.id.flower_image);
            flowerImage.setImageBitmap(flower.getBitmap());
        }
        else {
            FlowerAndView container = new FlowerAndView();
            container.flower = flower;
            container.view = view;

            ImageLoader loader = new ImageLoader();
            loader.execute(container);

        }


        return view;


    }

    class FlowerAndView {
        Flower flower;
        View view;
        Bitmap bitmap;

    }

    private class ImageLoader extends AsyncTask<FlowerAndView, Void, FlowerAndView> {
        @Override
        protected FlowerAndView doInBackground(FlowerAndView... params) {

            FlowerAndView container = params[0];
            Flower flower = container.flower;

            try {
                String imageUrl = MainActivity.PHOTO_BASE_URL + flower.getPhoto();

                InputStream inputStream = (InputStream) new URL(imageUrl).getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                flower.setBitmap(bitmap);

                inputStream.close();

                container.bitmap = bitmap;
                return  container;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(FlowerAndView flowerAndView) {
            ImageView imageView = (ImageView) flowerAndView.view.findViewById(R.id.flower_image);
            imageView.setImageBitmap(flowerAndView.bitmap);
            flowerAndView.flower.setBitmap(flowerAndView.bitmap);

        }
    }
}
