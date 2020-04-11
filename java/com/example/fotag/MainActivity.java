package com.example.fotag;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;


import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {

    Model mModel;
    RatingBar mRatingBar;
    GridView gridView;
    ImageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().hide();
        mRatingBar = findViewById(R.id.rating_bar);

        mModel = Model.getInstance();
        mModel.addObserver(this);

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mModel.setFilter(rating);
            }
        });

        gridView = findViewById(R.id.grid_view);
        adapter = new ImageAdapter(this, mModel);
        gridView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        mRatingBar.setRating(mModel.getFilter());
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    @Override
    public void update(Observable o, Object arg) {
        Log.d("update method", "get called");
        gridView.removeAllViewsInLayout();
        adapter.notifyDataSetChanged();
    }


    public void switchScene(int id) {
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void load(View view) {
        final String[] mText = new String[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter URL");
        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mText[0] = input.getText().toString();
                Log.d("entered", mText[0]);

                new DownLoadImageTask().execute(mText[0]);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("click", "cancel");
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void loadImageSet(View view) {
        String[] sample_urls =
                {
                        "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/bunny.jpg",
                        "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/chinchilla.jpg",
                        "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/deer.jpg",
                        "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/doggo.jpg",
                        "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/ducks.jpg",
                        "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/fox.jpg",
                        "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/hamster.jpg",
                        "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/hedgehog.jpg",
                        "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/husky.jpg",
                        "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/kitten.png",
                        "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/loris.jpg",
                        "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/puppy.jpg",
                        "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/running.jpg",
                        "https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/sleepy.png"
                };
        new DownLoadImageTask().execute(sample_urls);
    }

    public void clear(View view) {
        mModel.clear();
    }



    private class DownLoadImageTask extends AsyncTask<String, String, ArrayList<Bitmap> > {

        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = new ProgressBar(MainActivity.this);
            progressBar.setIndeterminate(false);
            progressBar.setVisibility(View.VISIBLE);
            LinearLayout root = findViewById(R.id.main_portrait_layout);
            root.addView(progressBar);
        }


        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
        */
        protected ArrayList<Bitmap> doInBackground(String...urls) {
            ArrayList<Bitmap> result = new ArrayList<>();
            for (String urlOfImage : urls) {
                Bitmap image;
                try {
                    InputStream is = new URL(urlOfImage).openStream();
                    image = BitmapFactory.decodeStream(is);
                    result.add(image);
                    Log.d("url", String.valueOf(image));
                }
                catch(Exception e) { // Catch the download exception
                    Log.d("do in background failed", " failed ");
                    return result;
                }
            }
            return result;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(ArrayList<Bitmap> result) {
            Log.d("bitmap valid", (result.size() == 0) ? "null bitmap" : "valid");
            progressBar.setVisibility(View.GONE);
            if (result.size() == 0) {
                showFailDialog();
            }
            else {
                for (Bitmap image : result) {
                    Log.d("image size", image.getWidth() + " " + image.getHeight());
                    //image = Bitmap.createScaledBitmap(image, 500, 500, false);
                    mModel.loadImage(image);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void showFailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Failed to load image!");
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

}

