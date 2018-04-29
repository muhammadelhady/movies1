package com.example.muham.movies;



import android.graphics.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by muham on 4/28/2018.
 */

public class JsonParser {
   public static ArrayList<movie> parseJson(String jsonString) throws JSONException {
      ArrayList<movie> listOfMovies=new ArrayList<>();
      JSONObject jsonData= new JSONObject(jsonString);
      JSONArray movies=jsonData.getJSONArray("results");
      ArrayList<JSONObject> jsonObjects=new ArrayList<>();
      for (int i = 0;i<movies.length();i++)
      {
         jsonObjects.add(movies.getJSONObject(i));
      }
      for (int i =0;i<jsonObjects.size();i++)
      {
         movie movie= new movie();
         JSONObject currenObject=jsonObjects.get(i);
         movie.setTitle(currenObject.getString("title"));
         movie.setOrinalTitle(currenObject.getString("original_title"));
         movie.setMovieRate(currenObject.getString("vote_average"));
         movie.setOverView(currenObject.getString("overview"));
         movie.setReleseDate(currenObject.getString("release_date"));
         movie.setImageUrl(currenObject.getString("backdrop_path"));
         listOfMovies.add(movie);
      }
      return listOfMovies;
   }

}
