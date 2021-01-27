package com.example.arcgbot.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.arcgbot.database.converters.DateConverter;
import com.example.arcgbot.database.entity.Customer;
import com.example.arcgbot.database.entity.GameCount;
import com.example.arcgbot.database.entity.GameTypes;
import com.example.arcgbot.database.entity.Screen;

import static com.example.arcgbot.utils.Constants.RoomConfigs.NEW_DB_VERSION;

@Database(entities = {Customer.class, Screen.class, GameCount.class, GameTypes.class},
        version = NEW_DB_VERSION)
@TypeConverters(DateConverter.class)
public abstract class AppDataBase extends RoomDatabase {

  /*  public abstract UserDao userDao();*/

}
