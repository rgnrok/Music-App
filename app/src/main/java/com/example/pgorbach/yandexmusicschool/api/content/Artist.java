package com.example.pgorbach.yandexmusicschool.api.content;


import android.os.Parcel;
import android.os.Parcelable;

import com.example.pgorbach.yandexmusicschool.R;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

public class Artist implements Parcelable {

    public static final String COVER_SMALL = "small";
    public static final String COVER_BIG = "big";

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("genres")
    public String[] genres;

    @SerializedName("tracks")
    public int tracks;

    @SerializedName("albums")
    public int albums;

    @SerializedName("link")
    public String link;

    @SerializedName("description")
    public String description;

    @SerializedName("cover")
    public HashMap<String, String> cover;

    public String getGenresAsString() {
        StringBuilder mBuilder = new StringBuilder();
        if (genres.length > 0) {
            mBuilder.append(genres[0]);
            for (int i = 1; i < genres.length; i++) {
                mBuilder.append(", ").append(genres[i]);
            }
        }
        return mBuilder.toString();
    }

    public Artist() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(genres.length);
        dest.writeStringArray(genres);
        dest.writeInt(tracks);
        dest.writeInt(albums);
        dest.writeString(link);
        dest.writeString(description);
        dest.writeString(cover != null ? cover.get(COVER_SMALL) : null);
        dest.writeString(cover != null ? cover.get(COVER_BIG) : null);
    }

    protected Artist(Parcel in) {
        id = in.readInt();
        name = in.readString();
        genres = new String[in.readInt()];
        in.readStringArray(genres);
        tracks = in.readInt();
        albums = in.readInt();
        link = in.readString();
        description = in.readString();
        cover = new HashMap<>();
        cover.put(COVER_SMALL, in.readString());
        cover.put(COVER_BIG, in.readString());
    }

    public static final Parcelable.Creator<Artist> CREATOR = new Parcelable.Creator<Artist>() {
        public Artist createFromParcel(Parcel source) {
            return new Artist(source);
        }

        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
}
