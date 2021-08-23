package com.example.arcgbot.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.arcgbot.database.entity.GameType;
import com.example.arcgbot.database.entity.Promotion;

import java.util.List;

@Dao
public abstract class PromotionDao extends BaseDao<Promotion> {

    @Query("SELECT * FROM game_promotion")
    public abstract List<Promotion> getAllPromotions();

    @Query("UPDATE game_promotion SET happy_hour_discount= 0 WHERE promo_game_id=:gameId")
    public abstract int updatePromotion(long gameId);

    @Query("SELECT happy_hour_discount FROM game_promotion WHERE promo_game_id=:id")
    public abstract double getHappyHourAmount(long id);
}
