package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MovieDashboardAdapter extends RecyclerView.Adapter<MovieDashboardAdapter.ViewHolder> {
    ArrayList<MovieModel> arrayList;
    Context context;

    public MovieDashboardAdapter(ArrayList<MovieModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieDashboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_view,parent,false);
        return new MovieDashboardAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieModel model=arrayList.get(position);

        try {
            // Parse the input string into a Date object
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date inputDate = inputFormat.parse(model.getRelease_date());
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy");
            String outputDateString= outputFormat.format(inputDate);
            holder.movieTitle.setText(model.getTitle()+" ("+outputDateString+")");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(model.getPoster_path()!=null){
            Picasso.get().load("https://image.tmdb.org/t/p/original/"+model.getPoster_path()).into(holder.moviePoster);
        }
        else{
            Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/6/65/No-Image-Placeholder.svg/1665px-No-Image-Placeholder.svg.png").into(holder.moviePoster);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MovieDetails.class);
                intent.putExtra("title",model.getTitle());
                intent.putExtra("back",model.getBackdrop_path());
                intent.putExtra("votes",model.getVote_average());
                intent.putExtra("dates",model.getRelease_date());
                intent.putExtra("desc",model.getOverview());
                intent.putExtra("genre",model.getGenre_ids());
                intent.putExtra("poster",model.getPoster_path());
                intent.putExtra("id",model.getId());
                context.startActivity(intent);

            }
        });
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView movieTitle,movieYear;
        ImageView moviePoster;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster=itemView.findViewById(R.id.moviePoster);
            movieTitle=itemView.findViewById(R.id.movieTitle);
        }
    }
}
