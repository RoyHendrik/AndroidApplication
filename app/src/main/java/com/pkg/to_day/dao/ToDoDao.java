package com.pkg.to_day.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pkg.to_day.models.ToDo;

import java.util.List;

@Dao
public interface ToDoDao {

    @Query("SELECT * FROM todos WHERE userID=(:userID)")
    List<ToDo> getAll(int userID);

    @Query("SELECT * FROM todos WHERE userID=(:userID) AND doneDate IS NOT NULL")
    List<ToDo> getCompleted(int userID);

    @Query("SELECT * FROM todos WHERE userID=(:userID) AND doneDate IS NULL")
    List<ToDo> getUncompleted(int userID);

    @Query("SELECT * FROM todos WHERE id=(:id)")
    ToDo getToDo(long id);

    @Insert
    long create(ToDo todo);

    @Update
    void update(ToDo todo);

    @Delete
    void remove(ToDo todo);
}
