package com.example.pgorbach.yandexmusicschool.api.content;


import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

public class Artist {

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

    @SerializedName("description")
    public HashMap<String, String> cover;

}
