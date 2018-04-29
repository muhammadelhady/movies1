package com.example.muham.movies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.muham.movies.R.string.api_pouplar_url;
import static com.example.muham.movies.R.string.image_base_url;

public class MainActivity extends AppCompatActivity {

Spinner spinner;
GridView movieList;
TextView networkStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
movieList=(GridView) findViewById(R.id.moives_grid);
networkStatus=(TextView)findViewById(R.id.network_status);
spinnerr();

}
public void spinnerr()
{
    spinner=(Spinner)findViewById(R.id.spinner);
    ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.SortBy,R.layout.support_simple_spinner_dropdown_item);
    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);


    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            if (spinner.getSelectedItemPosition()==0&&isOnline())
            {
                networkStatus.setText("");
                new FetchData().execute("Popular");

            }
            else if(spinner.getSelectedItemPosition()==1&&isOnline())
            {
                networkStatus.setText("");
                new FetchData().execute("TopRated");

            }
            else
            {
                networkStatus.setText("NO NETWORK");
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parentView) {
            // your code here
        }

    });
}

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected() ;
    }





 class FetchData extends AsyncTask<String , String,String>
{
String Data;
    ArrayList<movie>movies=new ArrayList<>();
    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url;
            String apiKey=getResources().getString(R.string.api_key);
            if (strings[0]=="Popular")
            {
                url =new URL(getResources().getString(R.string.api_pouplar_url)+apiKey);
            }
            else{
                url =new URL(getResources().getString(R.string.api_topRated_url)+apiKey);
            }


            HttpURLConnection urlConnection =(HttpURLConnection)url.openConnection();
            InputStream inputStream=new BufferedInputStream(urlConnection.getInputStream());
               Data=streamtostring(inputStream);
               inputStream.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {


            try {
                movies=JsonParser.parseJson(Data);
                listVeiewAdapter listVeiewAdapter=new listVeiewAdapter(movies);
                movieList.setAdapter(null);
                movieList.setAdapter(listVeiewAdapter);
                movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent =new Intent(MainActivity.this,DetailsActivity.class);
                        intent.putExtra("title",movies.get(position).getTitle());
                        intent.putExtra("original_title",movies.get(position).getOrinalTitle());
                        intent.putExtra("image_url",movies.get(position).getImageUrl());
                        intent.putExtra("overview",movies.get(position).getOverView());
                        intent.putExtra("user_rate",movies.get(position).getMovieRate());
                        intent.putExtra("relase_date",movies.get(position).getReleseDate());
                        startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        super.onPostExecute(s);
    }

    String streamtostring(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String text="";
        while ((line=bufferedReader.readLine())!=null)
        {
            text+=line;
        }
        bufferedReader.close();
        return text;


    }
}

    public class listVeiewAdapter extends BaseAdapter {
        ArrayList<movie> movies=new ArrayList<>();
        TextView textView;

        public listVeiewAdapter(ArrayList<movie> movies)
        {
            this.movies=movies;

        }
        @Override
        public int getCount() {
            return movies.size();
        }

        @Override
        public Object getItem(int i) {
            return movies.get(i).getTitle();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater layoutInflater=getLayoutInflater();

            View view1 =  layoutInflater.inflate(R.layout.row_layout,viewGroup,false);
       textView=(TextView)view1.findViewById(R.id.movie_title);
            textView.setText(movies.get(i).getTitle());

            ImageView imageView=(ImageView)view1.findViewById(R.id.movie_thumbnail);

            Picasso.get().load(getResources().getString(R.string.image_base_url)+movies.get(i).getImageUrl()).into(imageView);

            return view1;
        }
    }

}
