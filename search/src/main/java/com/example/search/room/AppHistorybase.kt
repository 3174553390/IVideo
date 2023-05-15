package com.example.search.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.search.App

@Database(version = 1, entities = [HistoryBean::class],exportSchema = false)
abstract class AppHistorybase : RoomDatabase() {

    abstract fun historydao(): HistoryDao

    companion object {

        private var instance: AppHistorybase? = null

        @Synchronized
        fun getInstance(context: Context): AppHistorybase {
            instance?.let {
                return it
            }
           return Room.databaseBuilder(App.context!!, AppHistorybase::class.java, "history")
               .build()
            }

        }

    }

