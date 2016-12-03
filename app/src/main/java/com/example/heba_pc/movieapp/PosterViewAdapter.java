package com.example.heba_pc.movieapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by HEBA-PC on 11/1/2016.
 */

public class PosterViewAdapter extends ArrayAdapter<Movie> {

    private final String logTag = PosterViewAdapter.class.getSimpleName();

    public PosterViewAdapter(Activity context, List<Movie> movies){
        super(context, 0, movies);
        Log.v(logTag , "size = " + movies.size());

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item,parent, false);

        }

        ImageView poster = (ImageView) convertView.findViewById(R.id.poster_item);
        Picasso.with(getContext()).load(movie.getImageUrl()).placeholder(R.drawable.placeholder_img).into(poster);

        return convertView;
    }
}

