package com.example.myapplication;

import java.util.ArrayList;

public class MovieData {
    int page, total_pages;
    ArrayList<MovieModel> results;

    public MovieData(int page, int total_pages, ArrayList<MovieModel> results) {
        this.page = page;
        this.total_pages = total_pages;
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public ArrayList<MovieModel> getResults() {
        return results;
    }

    public void setResults(ArrayList<MovieModel> results) {
        this.results = results;
    }
}