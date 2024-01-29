package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieCastAdapter extends RecyclerView.Adapter<MovieCastAdapter.ViewHolder> {
    Context context;
    ArrayList<CastModel> castModelArrayList;

    public MovieCastAdapter(Context context, ArrayList<CastModel> castModelArrayList) {
        this.context = context;
        this.castModelArrayList = castModelArrayList;
    }

    @NonNull
    @Override
    public MovieCastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_cast_item,parent,false);
        return new MovieCastAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MovieCastAdapter.ViewHolder holder, int position) {
        CastModel model=castModelArrayList.get(position);
        holder.castCharacter.setText(model.getCharacter());
        holder.castName.setText(model.getName());
        if(model.getProfile_path()!=null)
            Picasso.get().load("https://image.tmdb.org/t/p/original/"+model.getProfile_path()).into(holder.castImg);
        else
            Picasso.get().load("https://media.istockphoto.com/id/1223671392/vector/default-profile-picture-avatar-photo-placeholder-vector-illustration.jpg?s=612x612&w=0&k=20&c=s0aTdmT5aU6b8ot7VKm11DeID6NctRCpB755rA1BIP0=").into(holder.castImg);


    }

    @Override
    public int getItemCount() {
        return castModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView castImg;
        TextView castName,castCharacter;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            castCharacter=itemView.findViewById(R.id.castCharacter);
            castImg=itemView.findViewById(R.id.castImg);
            castName=itemView.findViewById(R.id.castName);
        }
    }
}
