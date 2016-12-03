package com.example.heba_pc.movieapp;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * A placeholder fragment containing a simple view.
 */
public class MoviePostersFragment extends Fragment {

    private PosterViewAdapter posterAdapter;
    GridView postersGrid;
    private final String logTag = MoviePostersFragment.class.getSimpleName();
    private DatabaseHelper fav_database;
    private HashMap<String , Movie> favourites;
    ArrayList<Movie> fav_movies;
    private MovieListner listner;




    public MoviePostersFragment() {
        posterAdapter = null;
        favourites = new HashMap<>();
        fav_movies = new ArrayList<>();

    }

    void setMovieListner(MovieListner movie_listener)
    {
        this.listner = movie_listener;
    }

    @Override
    public void onStart() {
        super.onStart();

        updateMovies();

    }

    private void getFavourites(){

        Cursor fav_data = fav_database.getAllFavourites();

        if(fav_data.getCount() ==0) {
            Toast.makeText(getActivity(), "No Favourites Yet", Toast.LENGTH_LONG);
        }
        else
        {
            fav_movies = new ArrayList<>();
            while (fav_data.moveToNext())
            {

                Movie mov = new Movie();
                mov.setId(fav_data.getInt(0));
                mov.setTitle(fav_data.getString(1));
                mov.setImageUrl(fav_data.getString(2));
                mov.setReleaseDate(fav_data.getString(3));
                mov.setRating(Double.parseDouble(fav_data.getString(4)));
                mov.setPlot(fav_data.getString(5));
                mov.setTrailerJSON(fav_data.getString(6));
                mov.setReviewJSON(fav_data.getString(7));

                favourites.put(mov.getTitle() , mov);
                fav_movies.add(mov);
            }
        }

    }

    private void updateMovies() {

        FetchMovieData movieData = new FetchMovieData();
        String order_val = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(getString(R.string.order_pref_key) , getString(R.string.order_pref_default));
        fav_database = new DatabaseHelper(getActivity());
        if(order_val.equals("pop")) {
            movieData.execute("popular");
            getFavourites();
        }
        else if(order_val.equals("top")) {
            movieData.execute("top_rated");
            getFavourites();
        }
        else if(order_val.equals("fav"))
        {
            getFavourites();
            posterAdapter = new PosterViewAdapter(getActivity(), fav_movies);
            postersGrid.setAdapter(posterAdapter);
        }



    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_main, container, false);
        postersGrid = (GridView) root.findViewById(R.id.posters_view);
        postersGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = posterAdapter.getItem(position);


                if(favourites.containsKey(movie.getTitle())){
                    listner.setSelectedMovie(movie , true);
                }
                else
                {
                    listner.setSelectedMovie(movie , false);
                }
            }
        });



        return root;
    }

    public class FetchMovieData extends AsyncTask<String,Void,ArrayList<Movie>>
    {
        private URL movieDBUrl;
        private final String logTag = FetchMovieData.class.getSimpleName();

        @Override
        protected  ArrayList<Movie> doInBackground(String... params) {
            final String apiKey = "23386b0753dd348bcb87ab9f516da5d5";
            HttpURLConnection movieDbConnection = null;
            BufferedReader reader = null;
            String resultString = null;

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http").authority("api.themoviedb.org").appendPath("3").appendPath("movie").appendPath(params[0])
                    .appendQueryParameter("api_key" , apiKey);
            try {
                movieDBUrl = new URL(builder.build().toString());
                movieDbConnection = (HttpURLConnection) movieDBUrl.openConnection();
                movieDbConnection.setRequestMethod("GET");
                movieDbConnection.connect();
                InputStream incomingData = movieDbConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(incomingData));
                StringBuffer bufferData= new StringBuffer();
                String line;
                while ((line = reader.readLine())!=null)
                {
                    bufferData.append(line+"\n");
                }
                resultString = bufferData.toString();
                return getMovieData(resultString);


            } catch (MalformedURLException e) {
                Log.e(logTag, e.getMessage());
            } catch (ProtocolException e) {
                Log.e(logTag, e.getMessage());
            } catch (IOException e) {
            }finally {
                if(movieDbConnection!=null)
                {
                    movieDbConnection.disconnect();
                }
                if(reader != null)
                {
                    try{
                        reader.close();
                    }
                    catch (final IOException e)
                    {
                        Log.e(logTag, "Error cloasing input stream: ", e);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movie) {

            posterAdapter = new PosterViewAdapter(getActivity(), movie);
            postersGrid.setAdapter(posterAdapter);
        }


        private ArrayList<Movie> getMovieData(String jsonData){

            try {
                String baseURL = "http://image.tmdb.org/t/p/w342/";
                JSONObject object = new JSONObject(jsonData);
                JSONArray results = object.getJSONArray("results");
                ArrayList<Movie> moviesPosters = new ArrayList<>();
                for (int i=0; i<results.length(); i++) {
                    JSONObject movieObj = results.getJSONObject(i);
                    Movie movie = new Movie();
                    String image = baseURL +  movieObj.getString("poster_path");
                    int id = movieObj.getInt("id");
                    String originalTitle = movieObj.getString("original_title");
                    String plot = movieObj.getString("overview");
                    double rating = movieObj.getDouble("vote_average");
                    String releaseDate = movieObj.getString("release_date");
                    movie.setId(id);
                    movie.setPlot(plot);
                    movie.setTitle(originalTitle);
                    movie.setRating(rating);
                    movie.setImageUrl(image);

                    movie.setReleaseDate(releaseDate);
                    moviesPosters.add(movie);
                }

                return moviesPosters;
            } catch (JSONException e) {
                Toast.makeText(getActivity(), "Loading Problem. Please check again later" , Toast.LENGTH_SHORT).show();
            }

            return null;
        }


    }



}
