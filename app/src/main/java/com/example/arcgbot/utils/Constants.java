package com.example.arcgbot.utils;

public class Constants {
    public static final String BASE_URL   = "https://3d28fa537a50.ngrok.io/";
    public static final String SUCCESS = "success";
    public static final String  GENERIC_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String  DATE_FORMAT = "yyyy-MM-dd";

    public static final String MPESA_DEPOSIT_REGEX = "^(\\w+\\s+\\d+\\.\\d+)(\\s+\\w+\\s\\w+\\s+\\w+\\s+\\w+\\s+\\w+\\s+\\d+\\s+\\w+\\s+\\w+\\s+\\d+\\s+\\w+\\s+\\w+\\s+\\w+\\s+\\w+\\s+)(\\d+\\/\\d+\\/+\\d+)\\s\\w+\\s+(\\d+\\:\\d+\\s+\\w+)\\.\\s+\\w+\\-\\w+\\s+\\w+\\s+(\\w+)";


    public static final class Events{
        public static final String GAME_ITEM_CLICK = "game_item";
        public static final String SCREEN_ITEM_CLICK = "game_item";
        public static final String BOTTOM_SHEET = "close_sheet";
        public static final String MINUS_GAME_COUNT = "minus";
        public static final String ADD_GAME_COUNT = "add";
        public static final String CLOSE_ = "add";
        public static final String SYNC_GAME_DATA = "game_data_sync";
        public static final String START_GAME = "START GAME";
        public static final String END_GAME =  "END GAME";
        public static final String LOGIN = "login";
        public static final String CLOSE_ERROR_SHEET = "close_error_sheet" ;
    }

    public static final class RoomConfigs {
        public static final String DATABASE_NAME = "arcbot";
        public static final int NEW_DB_VERSION = 7;
        public static final int START_DB_VERSION = 2;
    }

    public static final class InputError {
        public static final String USERNAME_ERROR = "Username Required";
        public static final String PASSWORD_ERROR = "Password Required ";
        public static final String VALID_LOGIN = "Correct Login ";
    }

    public class PrefsKeys {
        public static final String LOGIN_SUCCESS = "login_success";
        public static final String PASSWORD_ERROR = "Password Required ";
        public static final String ACCESS_TOKEN = "access_token";
        public static final String CURRENT_DATE = "date" ;
    }
}
