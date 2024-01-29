package com.example.myapplication;

import java.util.ArrayList;

public class CastData {
    int id;
    ArrayList<CastModel> cast;

    public CastData(int id, ArrayList<CastModel> castArrayList) {
        this.id = id;
        this.cast = castArrayList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<CastModel> getCastArrayList() {
        return cast;
    }

    public void setCastArrayList(ArrayList<CastModel> castArrayList) {
        this.cast = castArrayList;
    }
}
