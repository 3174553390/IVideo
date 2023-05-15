package com.example.ivideo.db;

import androidx.room.Room;

import com.example.ivideo.App;

public class DBInstant {
    private static final String TAB_NAME = "IVideo";
    private static AppDatabase appDatabase;
    public static AppDatabase getAppDatabase(){
        if (appDatabase == null){
            synchronized (DBInstant.class){
                if (appDatabase == null){
                    appDatabase = Room.databaseBuilder(App.context,AppDatabase.class,TAB_NAME)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return appDatabase;
    }
}
