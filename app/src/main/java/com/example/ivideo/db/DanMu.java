package com.example.ivideo.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "danmu")
public class DanMu {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo
    private String  neirong;

    public DanMu(String neirong) {
        this.neirong = neirong;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNeirong() {
        return neirong;
    }

    public void setNeirong(String neirong) {
        this.neirong = neirong;
    }

    @Override
    public String toString() {
        return "DanMu{" +
                "id=" + id +
                ", neirong='" + neirong + '\'' +
                '}';
    }
}
