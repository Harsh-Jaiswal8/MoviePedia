package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
 RecyclerView movieDashboard;
 LinearLayoutManager layoutManager;
 ArrayList<MovieModel> movieArrayList;
 MovieDashboardAdapter movieDashboardAdapter;
 ImageView search;
 ProgressBar dashLoading;
 Boolean isScrolling=false;
 int currentPage=1,totalPages;
 int currentItems,scrolledOut,totalItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieDashboard=findViewById(R.id.movieDashboard);
        search=findViewById(R.id.search);
        dashLoading=findViewById(R.id.dashLoading);
        layoutManager=new GridLayoutManager(this,2);
        movieDashboard.setLayoutManager(layoutManager);
        movieArrayList=new ArrayList<>();
        movieDashboardAdapter=new MovieDashboardAdapter(movieArrayList,this);
        movieDashboard.setAdapter(movieDashboardAdapter);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MovieSearch.class);
                startActivity(intent);
            }
        });

        getMovies();
        movieDashboard.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling=true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems=layoutManager.getChildCount();
                totalItems=layoutManager.getItemCount();
                scrolledOut=layoutManager.findFirstVisibleItemPosition();
                if(isScrolling&&(scrolledOut+currentItems==totalItems)){
                    currentPage++;
                    getMoreMovies(currentPage);
                }


            }
        });


    }

    private void getMoreMovies(int currentPage) {
        dashLoading.setVisibility(View.VISIBLE);
        String Url="https://api.themoviedb.org/3/movie/popular?api_key=0c27af00fb961de464f689b1fff284b1&page="+(currentPage);//https://api.themoviedb.org/3/search/movie?api_key=###&query=the+avengers
        String BASE_URL="https://api.themoviedb.org/3/";
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
                dashLoading.setVisibility(View.GONE);

                totalPages=movieData.getTotal_pages();
                if(currentPage>totalPages) return;
                ArrayList<MovieModel> movieList=movieData.getResults();
                for(int i=0;i<movieList.size();i++){
                    movieArrayList.add(new MovieModel(movieList.get(i).getTitle(),movieList.get(i).getOverview(),"https://image.tmdb.org/t/p/original/"+movieList.get(i).getPoster_path(),movieList.get(i).getRelease_date(),movieList.get(i).getBackdrop_path(),movieList.get(i).getVote_average(),movieList.get(i).getGenre_ids(),movieList.get(i).getId()));
                    movieDashboardAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MovieData> call, Throwable t) {
                Toast.makeText(MainActivity.this, "No internet", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getMovies() {
        dashLoading.setVisibility(View.VISIBLE);
        String Url="https://api.themoviedb.org/3/movie/popular?api_key=0c27af00fb961de464f689b1fff284b1&page=1";//https://api.themoviedb.org/3/search/movie?api_key=###&query=the+avengers
        String BASE_URL="https://api.themoviedb.org/3/";
        movieArrayList.clear();
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
                dashLoading.setVisibility(View.GONE);
                ArrayList<MovieModel> movieList=movieData.getResults();
                for(int i=0;i<movieList.size();i++){
                    movieArrayList.add(new MovieModel(movieList.get(i).getTitle(),movieList.get(i).getOverview(),"https://image.tmdb.org/t/p/original/"+movieList.get(i).getPoster_path(),movieList.get(i).getRelease_date(),movieList.get(i).getBackdrop_path(),movieList.get(i).getVote_average(),movieList.get(i).getGenre_ids(),movieList.get(i).getId()));
                    movieDashboardAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MovieData> call, Throwable t) {
                Toast.makeText(MainActivity.this, "No internet", Toast.LENGTH_SHORT).show();
            }
        });


    }
}