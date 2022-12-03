package com.example.ar_wallpaper;


import static android.app.WallpaperManager.FLAG_LOCK;
import static android.app.WallpaperManager.FLAG_SYSTEM;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ar_wallpaper.modell.Photo;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class fullimage extends AppCompatActivity {
    private ImageView fullimage;
    private Button Apply;
    private ImageButton downloadbtn,sharebtn;
    Photo url;
    boolean state=false;
         private boolean checkPermission=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullimage);



        fullimage=findViewById(R.id.imageView3);
        Apply=findViewById(R.id.apply_wall);
        downloadbtn=findViewById(R.id.download);
        sharebtn=findViewById(R.id.share);
        url= (Photo) getIntent().getSerializableExtra("photo");

       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               Picasso.with(getApplicationContext())
                       .load(url.getSrc().getPortrait())
                       .into(fullimage);
           }
       });

        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fullimage.getDrawable()!=null){
                BitmapDrawable share_bitmap_Drawable = ((BitmapDrawable) fullimage.getDrawable());
                Bitmap share_bitmap = share_bitmap_Drawable.getBitmap();
                shareimage(share_bitmap);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please wait while image is loading", Toast.LENGTH_SHORT).show();
                }

            }
        });


       downloadbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

             runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     if(fullimage.getDrawable()!=null){
                         try {
                             download();
                             if(state){
                                 Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
                             }
                             else {
                                 Toast.makeText(getApplicationContext(), "Download Unsuccessful", Toast.LENGTH_SHORT).show();
                             }
                         } catch (IOException e) {
                             e.printStackTrace();
                         }
                     }
                     else {
                         Toast.makeText(getApplicationContext(), "Please wait while image is loading", Toast.LENGTH_SHORT).show();
                     }

                 }
             });
       }
    });

        Apply.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)

            @Override
            public void onClick(View view) {


              runOnUiThread(new Runnable() {
                  @Override
                  public void run() {



                if(fullimage.getDrawable()!=null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(com.example.ar_wallpaper.fullimage.this);
                    builder.setTitle("Apply on..")
                         .setItems(new CharSequence[]{"HOME", "LOCK", "BOTH"}, new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {
                                 switch (i){
                                     case 0:
                                         try {
                                             setasWallpaper();
                                             Toast.makeText(getApplicationContext(), "Set Successful", Toast.LENGTH_SHORT).show();
                                         } catch (IOException e) {
                                             e.printStackTrace();
                                         }
                                         break;

                                     case 1:
                                         try {
                                             setaslockscreen();
                                             Toast.makeText(getApplicationContext(), "Set Successful", Toast.LENGTH_SHORT).show();
                                         } catch (IOException e) {
                                             e.printStackTrace();
                                         }
                                         break;
                                     case 2:
                                         try {
                                             setaslockscreen();
                                         } catch (IOException e) {
                                             e.printStackTrace();
                                         }
                                         try {
                                             setasWallpaper();
                                             Toast.makeText(getApplicationContext(), "Set Successful", Toast.LENGTH_SHORT).show();
                                         } catch (IOException e) {
                                             e.printStackTrace();
                                         }
                                         break;


                                 }
                             }
                         });
                  AlertDialog alertDialog=builder.create();
                  alertDialog.show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please wait while image is loading", Toast.LENGTH_SHORT).show();
                }


            }
        });
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void shareimage(Bitmap share_bitmap) {
        Drawable drawable=fullimage.getDrawable();
        Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();

        try {
            File file = new File(getApplicationContext().getExternalCacheDir(), File.separator +"share_image.jpg");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID +".provider", file);

            intent.putExtra(Intent.EXTRA_STREAM, photoURI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/jpg");

            startActivity(Intent.createChooser(intent, "Share image via"));
        } catch (Exception e) {
            e.printStackTrace();
        }

            }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setasWallpaper() throws IOException {

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
       Bitmap bitmap=((BitmapDrawable)fullimage.getDrawable()).getBitmap();
       Bitmap final_bitmap=Bitmap.createScaledBitmap(bitmap,width,height,true);
       WallpaperManager wallpaperManager2=WallpaperManager.getInstance(getApplicationContext());
       wallpaperManager2.suggestDesiredDimensions(width,height);
       wallpaperManager2.setWallpaperOffsetSteps(1,1);
        wallpaperManager2.setBitmap(final_bitmap);
    }


    int A= new Rect().centerX();
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setaslockscreen() throws IOException {

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Bitmap bitmap=((BitmapDrawable)fullimage.getDrawable()).getBitmap();
        Bitmap final_bitmap=Bitmap.createScaledBitmap(bitmap,width,height,true);
        WallpaperManager wallpaperManager= WallpaperManager.getInstance(getApplicationContext());

        wallpaperManager.suggestDesiredDimensions(width,height);
        wallpaperManager.setWallpaperOffsetSteps(1,1);

        wallpaperManager.setBitmap(final_bitmap,null,false,FLAG_LOCK);
    }

        private boolean download() throws IOException {

            String filename = "Wallpaper.jpg";
            if(chechforpermission()) {
                createdir();
                DownloadManager dm = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
                Uri downloadUri = Uri.parse(url.getSrc().getLarge());
                DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle(filename)
                        .setMimeType("image/jpeg")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                                File.separator + "AR_Wallpaper" + File.separator + filename);
                dm.enqueue(request);

                state=true;
            }
            return state;
            };

    private boolean chechforpermission(){

        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        checkPermission=true;

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    checkPermission=false;
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                    }
                }).check();


        return checkPermission;
    }


    private void createdir(){
         File direct =
                    new File(Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            .getAbsolutePath() + "/" + "AR_Wallpaper"+ "/");


            if (!direct.exists()) {
                direct.mkdir();
                Log.d("created", "dir created for first time");
            }
        }







}