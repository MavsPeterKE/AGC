package com.example.arcgbot.retrofit.responseStructures;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScreenStructure {
    @SerializedName("id")
    public Integer id;
    @SerializedName("sessions")
    public List<Session> sessions = null;
    @SerializedName("label")
    public String label;
    @SerializedName("active")
    public Boolean active;


    public class Session {

        @SerializedName("id")
        public Integer id;
        @SerializedName("players")
        public List<Player> players = null;
        @SerializedName("game_details")
        public GameDetails gameDetails;
        @SerializedName("ref")
        public String ref;
        @SerializedName("start_time")
        public String startTime;
        @SerializedName("end_time")
        public String endTime;
        @SerializedName("rounds")
        public Integer rounds;
        @SerializedName("screen")
        public Integer screen;
        @SerializedName("business_day")
        public Integer businessDay;
        @SerializedName("game")
        public Integer game;

    }

    public class Player {
        @SerializedName("id")
        public Integer id;
        @SerializedName("username")
        public String username;

    }


    public class GameDetails {

        @SerializedName("id")
        public Integer id;
        @SerializedName("name")
        public String name;
        @SerializedName("pricing_mode")
        public String pricingMode;
        @SerializedName("unit_price")
        public Double unitPrice;
        @SerializedName("active")
        public Boolean active;

    }
}
