package com.example.pgorbach.yandexmusicschool.api.services;

import com.example.pgorbach.yandexmusicschool.api.content.Artist;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ArtistService {
    @GET("artists.json")
    Call<List<Artist>> listArtists();
}
