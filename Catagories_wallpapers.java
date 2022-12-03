package com.example.ar_wallpaper;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ar_wallpaper.listeners.CuratedResponseListener;
import com.example.ar_wallpaper.listeners.OnRecyclerclickListener;
import com.example.ar_wallpaper.modell.CuratedApiResponse;
import com.example.ar_wallpaper.modell.Photo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class Catagories_wallpapers extends AppCompatActivity implements OnRecyclerclickListener {
    private RequestManager manager;
    String url;
    private RecyclerView recyclerView;
    private custom_adaptor2 custom_adaptor;
    private ProgressBar progressBar;
    String page="1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagories_wallpapers);
        Intent intent=getIntent();

        url= intent.getStringExtra("search");
        progressBar=findViewById(R.id.progressBar2);
        manager=new RequestManager(this);
        manager.getCuratedWallpapers(listener,"1","80",url);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              int p=Integer.parseInt(page)+1;
              page=String.valueOf(p);
                Toast.makeText(getApplicationContext(), page+"", Toast.LENGTH_SHORT).show();
                manager.getCuratedWallpapers(listener,page,"80",url);



                progressBar.setVisibility(View.VISIBLE);
            }
        });


    }
    private final CuratedResponseListener listener=new CuratedResponseListener() {
        @Override
        public void onFetch(CuratedApiResponse response, String message) {
            if(response.getPhotos().isEmpty()){
                Toast.makeText(getApplicationContext(), "No Image Found!!", Toast.LENGTH_SHORT).show();
            }
            showdata(response.getPhotos());
            progressBar.setVisibility(View.GONE);

        }

        @Override
        public void onError(String message) {

        }
    };
        private void showdata(List<Photo> photo) {
            recyclerView=findViewById(R.id.recycle_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(this,3));
            custom_adaptor=new custom_adaptor2(Catagories_wallpapers.this,photo,this);
            recyclerView.setAdapter(custom_adaptor);

        }

    @Override
    public void onclick(Photo photo) {
        startActivity(new Intent(Catagories_wallpapers.this,fullimage.class)
                .putExtra("photo",photo));
    }
};