package com.example.search.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryBean(var title: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}