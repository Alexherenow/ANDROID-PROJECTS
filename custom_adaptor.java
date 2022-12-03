package com.example.ar_wallpaper;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_wallpaper.listeners.OnRecyclerclickListener;
import com.example.ar_wallpaper.modell.CuratedApiResponse;
import com.example.ar_wallpaper.modell.Photo;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class custom_adaptor extends RecyclerView.Adapter<custom_adaptor.MyViewHolder> {
    private List<Photo> imagelist;
    private Context context;
    private OnRecyclerclickListener listener;

    public custom_adaptor(Context context, List<Photo> imagelist, OnRecyclerclickListener listener) {
        this.imagelist = imagelist;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.listview_customl_layout2,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
              Picasso.with(context)
                .load(imagelist.get(position).getSrc().getTiny())
                      .resize(200,200)
                .centerCrop()
                .into(holder.imageView);

              holder.imageView.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      listener.onclick(imagelist.get(holder.getAdapterPosition()));
                  }
              });
    }

    @Override
    public int getItemCount() {
        return imagelist.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView2);
        }
    }
}
