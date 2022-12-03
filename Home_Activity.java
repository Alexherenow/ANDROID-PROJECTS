package com.example.ar_wallpaper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ar_wallpaper.listeners.CuratedResponseListener;
import com.example.ar_wallpaper.listeners.OnRecyclerclickListener;
import com.example.ar_wallpaper.modell.CuratedApiResponse;
import com.example.ar_wallpaper.modell.Photo;

import java.util.ArrayList;
import java.util.List;


public class Home_Activity extends AppCompatActivity implements OnRecyclerclickListener {
    private SearchView searchView;
    private TextView textView;
    private RecyclerView recyclerView,recyclerView2;
    private RequestManager manager,manager2;
    private custom_adaptor custom_adaptor;
    private custom_adaptor2 custom_adaptor2;
    SharedPreferences suggest;
    private Button Nature_btn,tech_btn,devotional_btn,background_btn,car_btn,Animal_btn,others_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        searchView=findViewById(R.id.sr);
        Nature_btn=findViewById(R.id.Nature_btn);
        tech_btn=findViewById(R.id.tech_btn);
        devotional_btn=findViewById(R.id.devotional_btn);
        car_btn=findViewById(R.id.car_btn);
        background_btn=findViewById(R.id.background_btn);
        others_btn=findViewById(R.id.others_btn);
        Animal_btn=findViewById(R.id.Animal_btn);

        suggest=getSharedPreferences("nature",MODE_PRIVATE);
        suggest=getSharedPreferences("technology",MODE_PRIVATE);
    SharedPreferences.Editor editor= suggest.edit();



      Nature_btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              categories(Nature_btn.getText().toString());
                int a=suggest.getInt("nature",0)+1;
              editor.putInt("nature",a).apply();
              Toast.makeText(getApplicationContext(), suggest.getInt("nature",0)+"", Toast.LENGTH_SHORT).show();
          }
      });
        tech_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categories("technology");
                int a=suggest.getInt("technology",0)+1;
                editor.putInt("technology",a).apply();
                Toast.makeText(getApplicationContext(), suggest.getInt("technology",0)+"", Toast.LENGTH_SHORT).show();

            }
        });
        devotional_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categories("god");
                suggest=getSharedPreferences("god",MODE_PRIVATE);
            }
        });
        background_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categories(background_btn.getText().toString());
            }
        });
        car_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categories(car_btn.getText().toString());
            }
        });
        Animal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categories(Animal_btn.getText().toString());
            }
        });

      searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
          @Override
          public boolean onQueryTextSubmit(String query) {
             categories(query);
              return true;
          }

          @Override
          public boolean onQueryTextChange(String newText) {
              return false;
          }
      });


      int n,t,a;

      n=suggest.getInt("nature",0);
      t=suggest.getInt("technology",0);
        manager=new RequestManager(this);
      if(n>t){
          manager.getCuratedWallpapers(listener,"1","5","nature");
      }
      else{
          manager.getCuratedWallpapers(listener,"1","5","technology");
      }

        manager.getCuratedWallpapers(listener2,"1","50","girl");





}
    private final CuratedResponseListener listener=new CuratedResponseListener() {
        @Override
        public void onFetch(CuratedApiResponse response, String message) {
            if(response.getPhotos().isEmpty()){
                Toast.makeText(getApplicationContext(), "No Image Found!!", Toast.LENGTH_SHORT).show();
            }
            showdata_suggested(response.getPhotos());
        }

        @Override
        public void onError(String message) {

        }
    };
    private final CuratedResponseListener listener2=new CuratedResponseListener() {
        @Override
        public void onFetch(CuratedApiResponse response, String message) {
            if(response.getPhotos().isEmpty()){
                Toast.makeText(getApplicationContext(), "No Image Found!!", Toast.LENGTH_SHORT).show();
            }
            showdata_quickwall(response.getPhotos());
        }

        @Override
        public void onError(String message) {

        }
    };

    private void showdata_suggested(List<Photo> photo) {

        recyclerView=findViewById(R.id.suggested_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        custom_adaptor=new custom_adaptor(Home_Activity.this, photo,this);
        recyclerView.setAdapter(custom_adaptor);

    }

    private void showdata_quickwall(List<Photo> photo) {

        recyclerView2=findViewById(R.id.recycle_view_quick);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new GridLayoutManager(this,3));
        custom_adaptor2=new custom_adaptor2(Home_Activity.this,photo,this);
        recyclerView2.setAdapter(custom_adaptor2);

    }

    public void categories(String name){
        startActivity(new Intent(Home_Activity.this,Catagories_wallpapers.class)
                .putExtra("search",name));
    }

    @Override
    public void onclick(Photo photo) {
        startActivity(new Intent(Home_Activity.this,fullimage.class)
        .putExtra("photo",photo));


    }
}