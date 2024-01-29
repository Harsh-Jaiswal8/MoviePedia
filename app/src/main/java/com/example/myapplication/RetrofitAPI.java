package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitAPI {
    @GET
    Call<MovieData> getALLMovies(@Url String url);
    @GET
    Call<CastData> getALLCast(@Url String url);
    @GET
    Call<MovieData> getALLRec(@Url String url);
}
