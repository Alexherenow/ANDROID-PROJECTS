package com.example.ar_wallpaper.listeners;

import com.example.ar_wallpaper.modell.CuratedApiResponse;

public interface CuratedResponseListener {
    void onFetch(CuratedApiResponse response, String message);
    void onError(String message);
}
