package com.example.pgorbach.yandexmusicschool;

import com.example.pgorbach.yandexmusicschool.api.ApiFactory;
import com.example.pgorbach.yandexmusicschool.api.content.Artist;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class ApiTest {


    @Test
    public void api() {
        Call<List<Artist>> listArtists = ApiFactory.getArtistService(null).listArtists();
        try {
            Response<List<Artist>> response = listArtists.execute();
            Assert.assertTrue(response.isSuccessful());
            Assert.assertNotEquals(response.body().size(), 0);

            for (Artist artist : response.body()) {
                Assert.assertNotNull(artist.name);
                Assert.assertNotNull(artist.name + " has empty description", artist.description);
                Assert.assertNotNull(artist.name + " hasn't cover", artist.cover);
                Assert.assertNotNull(artist.name + " hasn't small cover", artist.cover.get(Artist.COVER_SMALL));
                Assert.assertNotNull(artist.name + " hasn't big cover", artist.cover.get(Artist.COVER_BIG));
                Assert.assertNotEquals(artist.name + " has empty genres", artist.genres.length, 0);

                if (artist.albums == 0) {
                    Assert.assertNotEquals(artist.name + " has empty albums and tracks", artist.tracks, 0);
                }
            }
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }

    }
}
