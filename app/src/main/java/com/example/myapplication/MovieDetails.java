package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetails extends AppCompatActivity {
    ImageView movieBackDrop,movieDetailPoster;
    TextView movieDetailsTitle,movieDate,movieRating,movieDesc,movieGenre,castHead,simHead;
    String back,title,date,desc,poster;
    StringBuilder genre;
    RecyclerView movieCast,movieRec;
    MovieCastAdapter movieCastAdapter;
    ArrayList<CastModel> castList=new ArrayList<>();
    MovieDashboardAdapter adapter;
    ArrayList<MovieModel> recList=new ArrayList<>();
    ProgressBar castLoading,recLoading;
    int[] genreList;
    int movieId;
    double votes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_details);
        movieBackDrop=findViewById(R.id.movieBackDrop);
        movieDetailsTitle=findViewById(R.id.movieDetailsTitle);
        movieDate=findViewById(R.id.movieDate);
        movieRating=findViewById(R.id.movieRating);
        movieDesc=findViewById(R.id.movieDesc);
        movieDetailPoster=findViewById(R.id.movieDetailPoster);
        movieGenre=findViewById(R.id.movieGenre);
        movieCast=findViewById(R.id.movieCast);
        movieRec=findViewById(R.id.movieRec);
        castLoading=findViewById(R.id.castLoading);
        castHead=findViewById(R.id.castHead);
        simHead=findViewById(R.id.simHead);
        recLoading=findViewById(R.id.recLoading);
        movieCastAdapter=new MovieCastAdapter(this,castList);
        movieCast.setAdapter(movieCastAdapter);
        adapter=new MovieDashboardAdapter(recList,this);
        movieRec.setAdapter(adapter);

        movieId=getIntent().getIntExtra("id",0);
        getCast(movieId);
        getRec(movieId);
        back=getIntent().getStringExtra("back");
        title=getIntent().getStringExtra("title");
        date=getIntent().getStringExtra("dates");
        desc=getIntent().getStringExtra("desc");
        poster=getIntent().getStringExtra("poster");
        votes=getIntent().getDoubleExtra("votes",0.0);
        genreList=getIntent().getIntArrayExtra("genre");
        simHead.setVisibility(View.GONE);
        castHead.setVisibility(View.GONE);
        DecimalFormat df = new DecimalFormat("#.#");
        String vote=df.format(votes);

        try {
            // Parse the input string into a Date object
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date inputDate = inputFormat.parse(date);
            SimpleDateFormat outputFormat = new SimpleDateFormat("d MMM yyyy");
            String outputDateString= outputFormat.format(inputDate);
            movieDate.setText(outputDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        genre=new StringBuilder();
        for(int i=0;i<genreList.length-1;i++){
            genre.append(getGenre(genreList[i]));
            genre.append(", ");
        }
        genre.append(getGenre(genreList[genreList.length-1]));

        movieGenre.setText(genre.toString());
        movieDetailsTitle.setText(title);
        movieRating.setText(vote+"/10");
        movieDesc.setText(desc);
        if(poster!=null){
            Picasso.get().load("https://image.tmdb.org/t/p/original/"+back).into(movieBackDrop);
        }
        else{
            Picasso.get().load("https://www.lifecarehospital.co.in/images/no-image-available.jpg").into(movieBackDrop);

        }

        if(poster!=null){
            Picasso.get().load("https://image.tmdb.org/t/p/original/"+poster).into(movieDetailPoster);
        }
        else{
            Picasso.get().load("https://www.lifecarehospital.co.in/images/no-image-available.jpg").into(movieDetailPoster);

        }




    }

    private void getRec(int movieId) {
        recLoading.setVisibility(View.VISIBLE);
        String Url="https://api.themoviedb.org/3/movie/"+movieId+"/similar?api_key=0c27af00fb961de464f689b1fff284b1";
        String BASE_URL="https://api.themoviedb.org/3/";
        recList.clear();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI=retrofit.create(RetrofitAPI.class);
        Call<MovieData> call;
        call=retrofitAPI.getALLMovies(Url);
        call.enqueue(new Callback<MovieData>() {
            @Override
            public void onResponse(Call<MovieData> call, Response<MovieData> response) {
                MovieData movieData=response.body();
                recLoading.setVisibility(View.GONE);
                ArrayList<MovieModel> movieList=movieData.getResults();
                if(movieList.size()!=0) {simHead.setVisibility(View.VISIBLE);}
                int minSize=Math.min(movieList.size(),11);
                for(int i=0;i<minSize;i++){
                    recList.add(new MovieModel(movieList.get(i).getTitle(),movieList.get(i).getOverview(),"https://image.tmdb.org/t/p/original/"+movieList.get(i).getPoster_path(),movieList.get(i).getRelease_date(),movieList.get(i).getBackdrop_path(),movieList.get(i).getVote_average(),movieList.get(i).getGenre_ids(),movieList.get(i).getId()));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MovieData> call, Throwable t) {
                Toast.makeText(MovieDetails.this, "No internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCast(int id) {
        castLoading.setVisibility(View.VISIBLE);
        String Url="https://api.themoviedb.org/3/movie/"+id+"/credits?api_key=0c27af00fb961de464f689b1fff284b1&language=en-US";//https://api.themoviedb.org/3/search/movie?api_key=###&query=the+avengers
        String BASE_URL="https://api.themoviedb.org/3/";
        castList.clear();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI=retrofit.create(RetrofitAPI.class);
        Call<CastData> call = retrofitAPI.getALLCast(Url);
        call.enqueue(new Callback<CastData>() {
            @Override
            public void onResponse(Call<CastData> call, Response<CastData> response) {
                CastData castData=response.body();
                castLoading.setVisibility(View.GONE);
                ArrayList<CastModel> castModels=castData.getCastArrayList();
                if(castModels.size()!=0){castHead.setVisibility(View.VISIBLE);
                }
                int size=Math.min(castModels.size(),11);
                for(int i=0;i<size;i++){
                    castList.add(new CastModel(castModels.get(i).getProfile_path(),castModels.get(i).getCharacter(),castModels.get(i).getName()));
                    movieCastAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<CastData> call, Throwable t) {
                Toast.makeText(MovieDetails.this, "No internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getGenre(int genreId) {
        HashMap<Integer,String> genreList=new HashMap<>();
        genreList.put(28,"Action");
        genreList.put(12,"Adventure");
        genreList.put(16,"Animation");
        genreList.put(35,"Comedy");
        genreList.put(80,"Crime");
        genreList.put(99,"Documentary");
        genreList.put(18,"Drama");
        genreList.put(10751,"Family");
        genreList.put(14,"Fantasy");
        genreList.put(36,"History");
        genreList.put(27,"Horror");
        genreList.put(10402,"Music");
        genreList.put(9648,"Mystery");
        genreList.put(10749,"Romance");
        genreList.put(878,"Science Fiction");
        genreList.put(10770,"TV Movie");
        genreList.put(53,"Thriller");
        genreList.put(10752,"War");
        genreList.put(37,"Western");
        return genreList.get(genreId);

    }
}