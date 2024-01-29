package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieSearch extends AppCompatActivity {
    RecyclerView searchDashBoard;
    EditText searchTitle;
    TextView noResult,resultTV;
    ImageView searchButton;
    LinearLayoutManager layoutManager;
    ArrayList<MovieModel> searchArrayList;
    MovieDashboardAdapter movieDashboardAdapter;
    ProgressBar searchLoading;
    private int currentPage=1,totalPages;
    private String find="";
    private int currentItems,scrolledOut,totalItems;
    private Boolean isScrolling=false,isSearched=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_search);
        searchDashBoard=findViewById(R.id.searchDashboard);
        searchButton=findViewById(R.id.searchButton);
        searchTitle=findViewById(R.id.searchTitle);
        searchLoading=findViewById(R.id.searchLoading);
        searchTitle.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchTitle.setSingleLine(true);
        searchTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Close the keyboard
                    hideKeyboard();
                    noResult.setVisibility(View.GONE);
                    String find=searchTitle.getText().toString();
                    if(find!=null)

                        getSearchedMovies(find);

                    else
                        Toast.makeText(MovieSearch.this, "Enter Movie Name", Toast.LENGTH_SHORT).show();

                    return true; // Consume the event to prevent default behavior
                }
                return false;
            }


        });



        noResult=findViewById(R.id.noResult);
        resultTV=findViewById(R.id.resultTV);
        layoutManager=new GridLayoutManager(this,2);
        searchDashBoard.setLayoutManager(layoutManager);
        searchArrayList=new ArrayList<>();
        movieDashboardAdapter=new MovieDashboardAdapter(searchArrayList,this);
        searchDashBoard.setAdapter(movieDashboardAdapter);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find=searchTitle.getText().toString();
                isSearched=true;

                    noResult.setVisibility(View.GONE);
                if(find.length()!=0)
                    getSearchedMovies(find);
                else
                    Toast.makeText(MovieSearch.this, "Enter Movie Title", Toast.LENGTH_SHORT).show();
            }
        });
        searchDashBoard.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    getMoreMovies(currentPage,find);
                }
            }
        });


    }
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(searchTitle.getWindowToken(), 0);
        }
    }

    private void getMoreMovies(int currentPage,String find) {
        searchLoading.setVisibility(View.VISIBLE);
        String Url="https://api.themoviedb.org/3/search/movie?api_key=0c27af00fb961de464f689b1fff284b1&query="+find+"&page="+currentPage;//https://api.themoviedb.org/3/search/movie?api_key=###&query=the+avengers
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
                resultTV.setVisibility(View.VISIBLE);
                searchLoading.setVisibility(View.GONE);
                totalPages=movieData.getTotal_pages();


                ArrayList<MovieModel> movieList=movieData.getResults();


                if(currentPage>totalPages) return;


                for(int i=0;i<movieList.size();i++){
                    searchArrayList.add(new MovieModel(movieList.get(i).getTitle(),movieList.get(i).getOverview(),"https://image.tmdb.org/t/p/original/"+movieList.get(i).getPoster_path(),movieList.get(i).getRelease_date(),movieList.get(i).getBackdrop_path(),movieList.get(i).getVote_average(),movieList.get(i).getGenre_ids(),movieList.get(i).getId()));
                    movieDashboardAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MovieData> call, Throwable t) {

            }
        });
    }

    private void getSearchedMovies(String find) {
        searchLoading.setVisibility(View.VISIBLE);
        String Url="https://api.themoviedb.org/3/search/movie?api_key=0c27af00fb961de464f689b1fff284b1&query="+find;//https://api.themoviedb.org/3/search/movie?api_key=###&query=the+avengers
        String BASE_URL="https://api.themoviedb.org/3/";
        searchArrayList.clear();
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
                searchLoading.setVisibility(View.GONE);
                resultTV.setVisibility(View.VISIBLE);
                ArrayList<MovieModel> movieList=movieData.getResults();
                if(movieList.size()==0) noResult.setVisibility(View.VISIBLE);
                for(int i=0;i<movieList.size();i++){
                    searchArrayList.add(new MovieModel(movieList.get(i).getTitle(),movieList.get(i).getOverview(),"https://image.tmdb.org/t/p/original/"+movieList.get(i).getPoster_path(),movieList.get(i).getRelease_date(),movieList.get(i).getBackdrop_path(),movieList.get(i).getVote_average(),movieList.get(i).getGenre_ids(),movieList.get(i).getId()));
                    movieDashboardAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MovieData> call, Throwable t) {
                Toast.makeText(MovieSearch.this, "No internet", Toast.LENGTH_SHORT).show();
            }
        });


    }
}