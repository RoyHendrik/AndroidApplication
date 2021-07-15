package com.pkg.to_day.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "labels", foreignKeys = {
        @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userID")
})
public class Label {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "userID")
    private final int userID;

    @ColumnInfo(name = "title")
    private String title;

    public Label(int userID, String title) {
        this.userID = userID;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public int getUserID() {
        return userID;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
