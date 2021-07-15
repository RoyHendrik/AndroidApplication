package com.pkg.to_day.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.pkg.to_day.dao.LabelDao;
import com.pkg.to_day.dao.ToDoDao;
import com.pkg.to_day.dao.UserDao;
import com.pkg.to_day.models.Label;
import com.pkg.to_day.models.ToDo;
import com.pkg.to_day.models.User;

@Database(entities = {User.class, ToDo.class, Label.class}, version = 10)
public abstract class AppDatabase extends RoomDatabase {

    private static final String dbName = "to-day-db";
    private static AppDatabase appDatabase;

    public static synchronized AppDatabase getAppDatabase(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context, AppDatabase.class, dbName)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return appDatabase;
    }

    public abstract UserDao userDao();
    public abstract ToDoDao toDoDao();
    public abstract LabelDao labelDao();
}
