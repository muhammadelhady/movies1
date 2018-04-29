package com.example.muham.movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    TextView title,orignalTitle,userRating,relaseDate,overView;
    ImageView movieImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        movieImage=(ImageView)findViewById(R.id.movie_image);
        title=(TextView)findViewById(R.id.title_txt);
        orignalTitle=(TextView)findViewById(R.id.original_title_txt);
        userRating=(TextView)findViewById(R.id.user_rating_txt);
        relaseDate=(TextView)findViewById(R.id.relase_date_txt);
        overView=(TextView)findViewById(R.id.overviewe_txt);

FillData();
    }


    public void FillData()
    {
        Intent intent=getIntent();
        Picasso.get().load(getResources().getString(R.string.image_base_url)+intent.getStringExtra("image_url")).into(movieImage);
        String titlel=intent.getStringExtra("title");
        title.setText(intent.getStringExtra("title"));
        orignalTitle.setText(intent.getStringExtra("original_title"));
        userRating.setText(intent.getStringExtra("user_rate"));
        relaseDate.setText(intent.getStringExtra("relase_date"));
        overView.setText(intent.getStringExtra("overview"));
    }
}
