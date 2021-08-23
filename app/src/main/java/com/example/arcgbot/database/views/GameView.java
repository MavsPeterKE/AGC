package com.example.arcgbot.database.views;

import androidx.room.DatabaseView;
import androidx.room.Embedded;

import com.example.arcgbot.database.entity.GameCount;
import com.example.arcgbot.database.entity.GameType;
import com.example.arcgbot.database.entity.Promotion;
import com.example.arcgbot.database.entity.Screen;

import java.util.List;

@DatabaseView(
        "SELECT " +
                "st.*," +
                "gct.happy_hour_amount+gct.normal_game_rate_amount AS payableAmount ," +
                "gct.happy_hour_bonus_amount+normal_game_rate_bonus_amount AS bonusAmount ," +
                "gct.happy_hour_game_count+gct.games_count AS totalGameCount ," +
                "gct.games_bonus_count*gtt.charges-(gp.happy_hour_discount*gct.games_bonus_count) AS bonusAmount, gct.*, gtt.* ,gp.* " +
                "FROM screen_table st " +
                "LEFT JOIN game_count_table gct ON gct.screen_id=st.id " +
                "LEFT JOIN game_types_table gtt ON gtt.type_id= gct.game_type_id " +
                "LEFT JOIN game_promotion gp ON gp.promo_game_id=gtt.type_id")


public class GameView {
    @Embedded
    public Screen screen;

    @Embedded()
    public GameCount gameCount;
    public double payableAmount;
    public double bonusAmount;
    public int totalGameCount;
    @Embedded()
    public GameType gameType;
    @Embedded
    public Promotion promotion;
}
