package com.example.myapplication;

public class CastModel {
    String profile_path,character,name;

    public CastModel(String profile_path, String character, String name) {
        this.profile_path = profile_path;
        this.character = character;
        this.name = name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
