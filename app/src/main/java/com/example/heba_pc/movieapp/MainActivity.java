package com.example.heba_pc.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MovieListner{

    Boolean isTablet = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MoviePostersFragment postersViewFrag = new MoviePostersFragment();
        postersViewFrag.setMovieListner(this);
        if(savedInstanceState == null){

            getSupportFragmentManager().beginTransaction().add(R.id.main , postersViewFrag).commit();
        }

        if(findViewById(R.id.detail_activity) !=null)
            isTablet = true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }


    @Override
    public void setSelectedMovie(Movie movie, Boolean isFavourite) {
        if(!isTablet)
        {
            Intent detailIntent = new Intent(this , DetailActivity.class);
            detailIntent.putExtra("id" , movie.getId());
            detailIntent.putExtra("image" , movie.getImageUrl());
            detailIntent.putExtra("plot" , movie.getPlot());
            detailIntent.putExtra("title", movie.getTitle());
            detailIntent.putExtra("date", movie.getReleaseDate());
            detailIntent.putExtra("rate" , movie.getRating());
            if(isFavourite){
                detailIntent.putExtra("Favourite" , "true");
            }
            else
            {
                detailIntent.putExtra("Favourite" , "false");
            }
            startActivity(detailIntent);
        }
        else if (isTablet)
        {
            DetailFragment detailes = new DetailFragment();
            Bundle movie_data = new Bundle();

            movie_data.putInt("id" , movie.getId());
            movie_data.putString("image" , movie.getImageUrl());
            movie_data.putString("plot" , movie.getPlot());
            movie_data.putString("title", movie.getTitle());
            movie_data.putString("date", movie.getReleaseDate());
            movie_data.putDouble("rate" , movie.getRating());
            if(isFavourite){
                movie_data.putString("Favourite" , "true");
            }
            else
            {
                movie_data.putString("Favourite" , "false");
            }
            detailes.setArguments(movie_data);
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_activity , detailes, "").commit();
        }
    }
}
