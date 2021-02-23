package com.example.arcgbot.database.views;

import androidx.room.DatabaseView;
import androidx.room.Embedded;

import com.example.arcgbot.database.entity.GameCount;
import com.example.arcgbot.database.entity.GameType;
import com.example.arcgbot.database.entity.Screen;

import java.util.List;

@DatabaseView(
        "SELECT st.*,((gct.games_count*gtt.charges)-(gct.games_bonus_count*gtt.charges)) AS payableAmount,gct.games_bonus_count*gtt.charges AS bonusAmount, gct.*, gtt.*  FROM screen_table st LEFT JOIN game_count_table gct ON gct.screen_id=st.id LEFT JOIN game_types_table gtt ON gtt.type_id= gct.game_type_id")

public class GameView {
    @Embedded
    public Screen screen;

    @Embedded()
    public GameCount gameCount;

    public double payableAmount;

    public double bonusAmount;

    @Embedded()
    public GameType gameType;
}
