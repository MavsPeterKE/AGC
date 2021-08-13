package com.example.arcgbot.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.arcgbot.database.converters.DateConverter;
import com.example.arcgbot.database.dao.CompleteGameDao;
import com.example.arcgbot.database.dao.CustomerDao;
import com.example.arcgbot.database.dao.GameCountDao;
import com.example.arcgbot.database.dao.GameDao;
import com.example.arcgbot.database.dao.ScreenDao;
import com.example.arcgbot.database.entity.CompletedGame;
import com.example.arcgbot.database.entity.Customer;
import com.example.arcgbot.database.entity.GameCount;
import com.example.arcgbot.database.entity.GameType;
import com.example.arcgbot.database.entity.Screen;
import com.example.arcgbot.database.views.GameView;

import static com.example.arcgbot.utils.Constants.RoomConfigs.NEW_DB_VERSION;

@Database(entities = {Customer.class, Screen.class, GameCount.class, CompletedGame.class, GameType.class},views = {GameView.class},
        version = NEW_DB_VERSION)
@TypeConverters(DateConverter.class)
public abstract class AppDataBase extends RoomDatabase {

    public abstract ScreenDao screenDao();

    public abstract CustomerDao customerDao();

    public abstract GameDao gameTypeDao();

    public abstract GameCountDao gameCountDao();

    public abstract CompleteGameDao completeGameDao();

}
