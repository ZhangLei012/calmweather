package com.example.calmweather.gson;

import com.google.gson.annotations.SerializedName;

public class Suggestion {
    public String type;

    @SerializedName("brf")
    public String feeling;

    @SerializedName("txt")
    public String info;
}
