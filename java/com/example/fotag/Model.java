package com.example.fotag;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Observable;

class MyImage {
    Bitmap image;
    Integer rating;
    Integer id;
    MyImage(Bitmap image, Integer rating, Integer id) {
        this.image = image;
        this.rating = rating;
        this.id = id;
    }
}

class Model extends Observable {
    private static Model ourInstance = new Model();
    static Model getInstance() {
        return ourInstance;
    }

    private ArrayList<MyImage> images;
    private ArrayList<MyImage> displayImages;
    private int idCounter;
    private int filter;

    private Model() {
        images = new ArrayList<>();
        displayImages = new ArrayList<>();
        filter = 0;
        idCounter = 0;
    }

    int getFilter() {
        return filter;
    }

    void setFilter(float filter) {
        this.filter = (int) filter;
        displayImages.clear();
        for (int i = 0; i < images.size(); i++) {
            if (images.get(i).rating >= this.filter) {
                displayImages.add(images.get(i));
            }
        }
        Log.d("no. of display images", displayImages.size()+"");
        setChanged();
        notifyObservers();
    }


    void setRatingById(int id, int rating) {
        MyImage remove = null;
        for (MyImage image : displayImages) {
            if (image.id == id) {
                //found
                image.rating = rating;
                if (rating < filter) {
                    //remove it
                    remove = image;
                }
                break;
            }
        }
        if (remove != null) {
            displayImages.remove(remove);
            setChanged();
            notifyObservers();
        }
    }

    void clear() {
        images.clear();
        displayImages.clear();
        setChanged();
        notifyObservers();
    }

    ArrayList<MyImage> getDisplayImages() {
        return displayImages;
    }


    void loadImage(Bitmap bitmap) {
        MyImage myImage = new MyImage(bitmap, 0, idCounter);
        images.add(myImage);
        if (filter == 0) {
            displayImages.add(myImage);
        }
        idCounter++;
    }
}
