package com.pkg.to_day.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.SET_NULL;

@Entity(tableName = "todos", foreignKeys = {
            @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userID", onDelete = CASCADE),
            @ForeignKey(entity = Label.class, parentColumns = "id", childColumns = "labelID", onDelete = SET_NULL)
})
public class ToDo {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "userID")
    private final int userID;

    @ColumnInfo(name = "labelID")
    private Integer labelID;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String context;

    @ColumnInfo(name = "dueDate")
    private String dueDate;

    @ColumnInfo(name = "doneDate")
    private String doneDate;

    public ToDo(Integer userID, Integer labelID, String title, String context, String dueDate) {
        this.userID = userID;
        this.labelID = labelID;
        this.title = title;
        this.context = context;
        this.dueDate = dueDate;
    }

    @Ignore
    public ToDo(Integer userID, String title, String context, String dueDate) {
        this.userID = userID;
        this.title = title;
        this.context = context;
        this.dueDate = dueDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLabelID(Integer labelID) {
        this.labelID = labelID;
    }

    public int getUserID() {
        return userID;
    }

    public Integer getLabelID() {
        return labelID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(String doneDate) {
        this.doneDate = doneDate;
    }

}
