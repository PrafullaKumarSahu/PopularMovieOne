package com.android.serverwarrior.popularmoviesone;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int layoutId;
    private int imageViewID;

    // references to our images
//    private Integer[] mThumbIds = {
//            R.drawable.pic1,
//            R.drawable.pic2,
//            R.drawable.pic3,
//            R.drawable.pic4,
//            R.drawable.pic5,
//            R.drawable.pic6,
//            R.drawable.pic7,
//            R.drawable.pic8,
//            R.drawable.pic9,
//            R.drawable.pic10
//    };


    public ImageAdapter(Context context, int layoutId, int imageViewID, ArrayList<String> urls){
        super(context, 0, urls);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.layoutId = layoutId;
        this.imageViewID = imageViewID;

        Log.v("Image View ID", imageViewID + "");//executing correctly
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {//not called
//        ImageView imageView;
//        if (convertView == null) {
//            // if it's not recycled, initialize some attributes
//            imageView = new ImageView(mContext);
//            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setPadding(8, 8, 8, 8);
//        } else {
//            imageView = (ImageView) convertView;
//        }
//
//        Integer url = mThumbIds[position];
//        //imageView.setImageResource(mThumbIds[position]);
//        Picasso.with(mContext).load(url).into(imageView);
//
//        return imageView;
        View v = convertView;
        String url;
        if(v == null){
            v = mLayoutInflater.inflate(layoutId, parent, false);
        }

        ImageView imageView = (ImageView) v.findViewById(imageViewID);
        url = getItem(position);
        Log.v("url", url);
        Picasso.with(mContext).load(url).into(imageView);
        return v;

    }
}