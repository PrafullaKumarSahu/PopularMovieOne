package com.android.serverwarrior.popularmoviesone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends ArrayAdapter<String> {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final int layoutId;
    private final int imageViewID;

    public ImageAdapter(Context context, int layoutId, int imageViewID, ArrayList<String> urls){
        super(context, 0, urls);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.layoutId = layoutId;
        this.imageViewID = imageViewID;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {//not called

        View v = convertView;
        
        String url;
        if(v == null){
            v = this.mLayoutInflater.inflate(this.layoutId, parent, false);
        }

        ImageView imageView = (ImageView) v.findViewById(this.imageViewID);
        url = getItem(position);
        Picasso.with(this.mContext).load(url).into(imageView);
        return v;

    }

}