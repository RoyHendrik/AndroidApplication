package com.pkg.to_day.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pkg.to_day.models.Label;

import java.util.List;

@Dao
public interface LabelDao {

    @Query("SELECT * FROM labels WHERE userID=(:userID)")
    List<Label> getAll(int userID);

    @Query("SELECT * FROM labels WHERE id=(:id)")
    Label getLabel(long id);

    @Insert
    long create(Label label);

    @Update
    void update(Label label);

    @Delete
    void remove(Label label);
}