package com.example.myapplication;

public class MovieModel {
 String title, overview, poster_path, release_date, backdrop_path;
 int[] genre_ids;
 int id;
 double vote_average;

 public MovieModel(String title, String overview, String poster_path, String release_date, String backdrop_path, double vote_average, int[] genre_ids, int id) {
  this.title = title;
  this.overview = overview;
  this.poster_path = poster_path;
  this.release_date = release_date;
  this.backdrop_path = backdrop_path;
  this.vote_average = vote_average;
  this.genre_ids = genre_ids;
  this.id = id;
 }

 public String getTitle() {
  return title;
 }

 public void setTitle(String title) {
  this.title = title;
 }

 public String getOverview() {
  return overview;
 }

 public void setOverview(String overview) {
  this.overview = overview;
 }

 public String getPoster_path() {
  return poster_path;
 }

 public void setPoster_path(String poster_path) {
  this.poster_path = poster_path;
 }

 public String getRelease_date() {
  return release_date;
 }

 public void setRelease_date(String release_date) {
  this.release_date = release_date;
 }

 public String getBackdrop_path() {
  return backdrop_path;
 }

 public void setBackdrop_path(String backdrop_path) {
  this.backdrop_path = backdrop_path;
 }

 public double getVote_average() {
  return vote_average;
 }

 public void setVote_average(double vote_average) {
  this.vote_average = vote_average;
 }

 public int[] getGenre_ids() {
  return genre_ids;
 }

 public void setGenre_ids(int[] genre_ids) {
  this.genre_ids = genre_ids;
 }

 public int getId() {
  return id;
 }

 public void setId(int id) {
  this.id = id;
 }
}