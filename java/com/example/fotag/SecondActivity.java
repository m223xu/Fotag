package com.example.fotag;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Observable;
import java.util.Observer;

public class SecondActivity extends AppCompatActivity implements Observer {

    Model mModel;
    int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        mModel = Model.getInstance();
        mModel.addObserver(this);

        Intent intent = getIntent();
        mId = intent.getIntExtra("id", -1);

        int index;
        for (index = 0; index < mModel.getDisplayImages().size(); index++) {
            if (mModel.getDisplayImages().get(index).id == mId) {
                break;
            }
        }

        ImageView image = findViewById(R.id.image);
        image.setImageBitmap(mModel.getDisplayImages().get(index).image);

        RatingBar rating = findViewById(R.id.rating);
        rating.setRating(mModel.getDisplayImages().get(index).rating);
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mModel.setRatingById(mId, (int) rating);
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
