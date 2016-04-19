package com.example.pgorbach.yandexmusicschool;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.example.pgorbach.yandexmusicschool.api.content.Artist;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class ArtistTest {

    public static final int TEST_ARTIST_ID = 632412;
    public static final String TEST_ARTIST_NAME = "Emery";
    public static final String[] TEST_ARTIST_GENRES = {
            "post-hardcore",
            "melodic hardcore",
            "hard rock",
            "alternative rock"
    };
    public static final int TEST_ARTIST_ALBUMS = 6;
    public static final int TEST_ARTIST_TRACKS = 53;


    private Artist mArtist;

    @Before
    public void createArtist() {
        mArtist = new Artist();
        // Set up the Parcelable object to send and receive.
        mArtist.id = TEST_ARTIST_ID;
        mArtist.name = TEST_ARTIST_NAME;
        mArtist.genres = TEST_ARTIST_GENRES;
        mArtist.albums = TEST_ARTIST_ALBUMS;
        mArtist.tracks = TEST_ARTIST_TRACKS;
    }

    @Test
    public void logHistory_ParcelableWriteRead() {

        // Write the data.
        Parcel parcel = Parcel.obtain();
        mArtist.writeToParcel(parcel, 0);

        // After you're done with writing, you need to reset the parcel for reading.
        parcel.setDataPosition(0);

        // Read the data.
        Artist createdFromParcel = Artist.CREATOR.createFromParcel(parcel);

        // Verify that the received data is correct.
        Assert.assertEquals(createdFromParcel.id, TEST_ARTIST_ID);
        Assert.assertEquals(createdFromParcel.name, TEST_ARTIST_NAME);
        Assert.assertArrayEquals(createdFromParcel.genres, TEST_ARTIST_GENRES);
        Assert.assertEquals(createdFromParcel.albums, TEST_ARTIST_ALBUMS);
        Assert.assertEquals(createdFromParcel.tracks, TEST_ARTIST_TRACKS);
    }
}
