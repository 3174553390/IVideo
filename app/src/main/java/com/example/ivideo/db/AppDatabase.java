package com.example.ivideo.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DanMu.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DanMuDao danMuDao();
}
