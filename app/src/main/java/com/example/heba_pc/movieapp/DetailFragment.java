package com.example.heba_pc.movieapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

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
import java.util.List;

public class DetailFragment extends Fragment
{
    private final String logTag = "DetailFragment";
    private Movie movie;
    private ExpandableListView listView;
    private DetailsAdapter trailer_review;
    private List<Trailer> trailers;
    private List<Review> reviews;
    private HashMap<String, List<?>> childs;
    private List<String> headers;
    private DatabaseHelper fav_database;
    String reviewResultString = null;
    String trailerResultString = null;


    public DetailFragment()
    {
        movie = new Movie();
        headers = new ArrayList<>();
        trailers=null;
        reviews = null;
    }


    @Override
    public void onStart() {
        super.onStart();




    }

    public void getTrailersReviews(String movieID)
    {
        FetchMovieReviews reviews = new FetchMovieReviews();
        reviews.execute(movieID);
        FetchMovieTrailers trailers = new FetchMovieTrailers();
        trailers.execute(movieID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail , container , false);
        ImageView poster = (ImageView) view.findViewById(R.id.movie_image);
        TextView title = (TextView) view.findViewById(R.id.original_title);
        TextView plot = (TextView) view.findViewById(R.id.plot);
        TextView date = (TextView) view.findViewById(R.id.release_date);
        TextView rate = (TextView) view.findViewById(R.id.rating);
        Intent details = getActivity().getIntent();
        String imageUrl = details.getStringExtra("image");


        movie.setImageUrl(imageUrl);
        movie.setId(details.getIntExtra("id" , 0));
        movie.setTitle(details.getStringExtra("title"));
        movie.setPlot(details.getStringExtra("plot"));
        movie.setReleaseDate(details.getStringExtra("date"));
        movie.setRating(details.getDoubleExtra("rate" , 0.0));
        Picasso.with(getActivity()).load(imageUrl).placeholder(R.drawable.placeholder_img).into(poster);
        SpannableStringBuilder ssb = new SpannableStringBuilder("");
        SpannableString ss = new SpannableString("Original Title");
        ss.setSpan(new StyleSpan(Typeface.BOLD) , 0, ss.length() , 0);
        ssb.append(ss);
        ssb.append("\n" + movie.getTitle());
        title.setText(ssb);
        ss = new SpannableString("Plot");
        ss.setSpan(new StyleSpan(Typeface.BOLD) , 0, ss.length() , 0);
        ssb = new SpannableStringBuilder("");
        ssb.append(ss);
        ssb.append("\n" + movie.getPlot());
        plot.setText(ssb);
        ss = new SpannableString("Date");
        ss.setSpan(new StyleSpan(Typeface.BOLD) , 0, ss.length() , 0);
        ssb = new SpannableStringBuilder("");
        ssb.append(ss);
        ssb.append("\n" +movie.getReleaseDate());
        date.setText(ssb);
        double rating = movie.getRating();
        ss = new SpannableString("Rating");
        ss.setSpan(new StyleSpan(Typeface.BOLD) , 0, ss.length() , 0);
        ssb = new SpannableStringBuilder("");
        ssb.append(ss);
        ssb.append("\n" + rating);
        rate.setText(ssb);
        ToggleButton favouriteButton = (ToggleButton) view.findViewById(R.id.fav_button);
        String favour = details.getStringExtra("Favourite");
        if(favour.equals("true")) {
            favouriteButton.setChecked(true);
        }
        else
        {
            favouriteButton.setChecked(false);
            getTrailersReviews(Integer.toString(movie.getId()));
        }
        listView = (ExpandableListView) view.findViewById(R.id.expandable_list);


        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if(groupPosition==1)
                {
                    return false;
                }
                else if (groupPosition ==0)
                {
                    Trailer trailer = (Trailer)  trailer_review.getChild(groupPosition, childPosition);
                    Intent playAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+trailer.getUrl()));
                    String baseURL = "https://www.youtube.com/watch?v=";
                    Intent playWebIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(baseURL+trailer.getUrl()));
                    try{
                        startActivity(playAppIntent);
                    }
                    catch (ActivityNotFoundException ex)
                    {
                        startActivity(playWebIntent);
                    }

                }
                return false;
            }
        });
        fav_database = new DatabaseHelper(getActivity());


        favouriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {


                    Boolean inserted = fav_database.insertMovie(movie.getTitle(),movie.getImageUrl() , movie.getReleaseDate() , Double.toString(movie.getRating()) , movie.getPlot() , trailerResultString
                    , reviewResultString);
                    if(inserted)
                    {
                        Toast.makeText(getActivity() , "Added to favourites" , Toast.LENGTH_SHORT);
                    }
                    else
                    {
                        Toast.makeText(getActivity() , "Problem in adding to favourites" , Toast.LENGTH_SHORT);
                    }
                }
                else
                {
                    Integer deleted = fav_database.deleteMovie(movie.getTitle());
                    if(deleted == 0 )
                    {
                        Toast.makeText(getActivity() , "Couldn't Remove From Favourite" , Toast.LENGTH_SHORT);
                    }
                    else
                    {
                        Toast.makeText(getActivity() , "Removed From Favourites" , Toast.LENGTH_SHORT);
                    }
                }
            }
        });



        return view;
    }

    public class FetchMovieReviews extends AsyncTask<String,Void,ArrayList<Review>>
    {
        private URL reviewURL;
        @Override
        protected void onPostExecute(ArrayList<Review> r) {
            reviews = new ArrayList<>();
            reviews.addAll(r);

            if(trailers !=null && reviews!=null )
            {
                headers.add(0,"Trailers");
                headers.add(1,"Reviews");
                childs = new HashMap<>();
                childs.put(headers.get(0) , trailers);
                childs.put(headers.get(1) , reviews);
                trailer_review = new DetailsAdapter(headers , childs , getActivity());
                listView.setAdapter(trailer_review);
            }
        }

        @Override
        protected ArrayList<Review> doInBackground(String... params) {
            final String apiKey = "23386b0753dd348bcb87ab9f516da5d5";
            HttpURLConnection movieDbConnection = null;
            BufferedReader reader = null;
            reviewResultString= null;

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http").authority("api.themoviedb.org").appendPath("3").appendPath("movie").appendPath(params[0])
                    .appendPath("reviews").appendQueryParameter("api_key" , apiKey);
            try {
                reviewURL = new URL(builder.build().toString());
                movieDbConnection = (HttpURLConnection) reviewURL.openConnection();
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
                reviewResultString = bufferData.toString();
                return getMovieReviews(reviewResultString);


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

        private ArrayList<Review> getMovieReviews(String jsonData){

            try {
                JSONObject object = new JSONObject(jsonData);
                JSONArray results = object.getJSONArray("results");
                ArrayList<Review> moviesReviews = new ArrayList<>();
                for (int i=0; i<results.length(); i++) {
                    JSONObject reviewObj = results.getJSONObject(i);

                    String content = reviewObj.getString("content");
                    String id = reviewObj.getString("id");
                    String author = reviewObj.getString("author");
                    Review review = new Review(id,author,content);
                    moviesReviews.add(review);
                }


                return moviesReviews;
            } catch (JSONException e) {
                Toast.makeText(getActivity(), "Loading Problem. Please check again later" , Toast.LENGTH_SHORT).show();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }


    public class FetchMovieTrailers extends AsyncTask<String,Void,ArrayList<Trailer>>
    {
        private URL trailersURL;
        @Override
        protected void onPostExecute(ArrayList<Trailer> t) {
            trailers = new ArrayList<>();
            trailers.addAll(t);
            if(trailers !=null && reviews!=null )
            {
                headers.add(0,"Trailers");
                headers.add(1,"Reviews");
                childs = new HashMap<>();
                childs.put(headers.get(0) , trailers);
                childs.put(headers.get(1) , reviews);
                trailer_review = new DetailsAdapter(headers , childs , getActivity());
                listView.setAdapter(trailer_review);
            }
        }

        @Override
        protected ArrayList<Trailer> doInBackground(String... params) {
            final String apiKey = "23386b0753dd348bcb87ab9f516da5d5";
            HttpURLConnection movieDbConnection = null;
            BufferedReader reader = null;
            trailerResultString = null;

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http").authority("api.themoviedb.org").appendPath("3").appendPath("movie").appendPath(params[0])
                    .appendPath("videos").appendQueryParameter("api_key" , apiKey);
            try {
                trailersURL = new URL(builder.build().toString());
                movieDbConnection = (HttpURLConnection) trailersURL.openConnection();
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
                trailerResultString = bufferData.toString();
                return getMovieTrailers(trailerResultString);


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
        protected void onPreExecute() {
            super.onPreExecute();
        }


        private ArrayList<Trailer> getMovieTrailers(String jsonData){

            try {

                JSONObject object = new JSONObject(jsonData);
                JSONArray results = object.getJSONArray("results");
                ArrayList<Trailer> moviestrailers = new ArrayList<>();
                for (int i=0; i<results.length(); i++) {
                    JSONObject trailerObj = results.getJSONObject(i);
                    String vidKey =  trailerObj.getString("key");
                    String id = trailerObj.getString("id");
                    String name = trailerObj.getString("name");
                    String imageURL ="https://img.youtube.com/vi/" + vidKey + "/0.jpg";
                    Trailer trailer = new Trailer(id,vidKey,name , imageURL);
                    moviestrailers.add(trailer);
                }

                return moviestrailers;
            } catch (JSONException e) {
                Toast.makeText(getActivity(), "Loading Problem. Please check again later" , Toast.LENGTH_SHORT).show();
            }

            return null;
        }
    }
}