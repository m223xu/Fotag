package com.example.fotag;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;

import java.util.HashMap;

class ImageAdapter extends BaseAdapter {
    private MainActivity mMainActivity;
    private Model mModel;

    private HashMap<ImageView, Integer> imageViewIntegerHashMap = new HashMap<>();
    private HashMap<RatingBar, Integer> ratingBarIntegerHashMap = new HashMap<>();

    public ImageAdapter(MainActivity mMainActivity, Model mModel) {
        this.mMainActivity = mMainActivity;
        this.mModel = mModel;
    }

    @Override
    public int getCount() {
        return mModel.getDisplayImages().size();
    }

    @Override
    public Object getItem(int position) {
        return mModel.getDisplayImages().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyImage image = mModel.getDisplayImages().get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mMainActivity);
            convertView = layoutInflater.inflate(R.layout.image_layout, null);
        }

        ImageView imageView = convertView.findViewById(R.id.image);
        float width = imageView.getWidth();
        float height = imageView.getHeight();
        Log.d("image view size", " " + width + " " + height);
        Bitmap bitmap = image.image;
        /*if (width != 0 && height != 0) {
            Log.d("good news","width and height are non zero");
            double scale = Math.min(width/bitmap.getWidth(), height/bitmap.getHeight());
            bitmap = Bitmap.createScaledBitmap(
                    bitmap, (int)(width*scale), (int)(height*scale), false
            );
            Log.d("bitmap size", ""+bitmap.getWidth()+ " "+ bitmap.getHeight());
        }*/

        imageView.setImageBitmap(bitmap);
        imageViewIntegerHashMap.put(imageView, image.id);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainActivity.switchScene(imageViewIntegerHashMap.get(v));
            }
        });

        RatingBar ratingBar = convertView.findViewById(R.id.rating);
        ratingBar.setRating(image.rating);
        ratingBarIntegerHashMap.put(ratingBar, image.id);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.d("id to be changed", ""+ ratingBarIntegerHashMap.get(ratingBar));
                if (fromUser) {
                    Log.d("from user", "change it!");
                    mModel.setRatingById(ratingBarIntegerHashMap.get(ratingBar), (int) rating);
                }
            }
        });

        return convertView;
    }

}
