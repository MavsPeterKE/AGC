package com.example.arcgbot.utils;

public class Constants {

    public static final String BASE_URL   = "https://b83b4eb04d48.ngrok.io";
    public static final String SUCCESS = "success";

    public static final class Events{
        public static final String GAME_ITEM_CLICK = "game_item";
        public static final String BOTTOM_SHEET = "close_sheet";
        public static final String MINUS_GAME_COUNT = "minus";
        public static final String ADD_GAME_COUNT = "add";
        public static final String SYNC_GAME_DATA = "game_data_sync";
        public static final String START_GAME = "START GAME";
        public static final String END_GAME =  "END GAME";
        public static final String LOGIN = "login";
    }

    public static final class RoomConfigs {
        public static final String DATABASE_NAME = "arcGbot";
        public static final int NEW_DB_VERSION = 3;
        public static final int START_DB_VERSION = 2;
    }

    public static final class InputError {
        public static final String USERNAME_ERROR = "Username Required";
        public static final String PASSWORD_ERROR = "Password Required ";
        public static final String VALID_LOGIN = "Correct Login ";
    }

}