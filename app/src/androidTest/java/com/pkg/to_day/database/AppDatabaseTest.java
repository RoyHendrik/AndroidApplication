package com.pkg.to_day.database;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.pkg.to_day.dao.LabelDao;
import com.pkg.to_day.dao.ToDoDao;
import com.pkg.to_day.dao.UserDao;
import com.pkg.to_day.models.Label;
import com.pkg.to_day.models.ToDo;
import com.pkg.to_day.models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class AppDatabaseTest {

    private UserDao userDao;
    private ToDoDao toDoDao;
    private LabelDao labelDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        userDao = db.userDao();
        toDoDao = db.toDoDao();
        labelDao = db.labelDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void test_user_register() {
        String email = "lorem@ipsum.com", password = "password";

        userDao.register(new User(email, password));

        assertNotNull(userDao.find(email));
    }

    @Test()
    public void test_user_register_duplicate() {
        String email = "lorem@ipsum.com", password = "password";
        boolean thrown = false;

        userDao.register(new User(email, password));

        try {
            userDao.register(new User(email, password));
        } catch (Exception e) {
            thrown = true;
        }

        assertTrue(thrown);
    }

    @Test
    public void test_user_find_does_not_exist() {
        userDao.register(new User("_lorem@ipsum.com", "password"));
        userDao.register(new User("lorem_@ipsum.com", "password"));
        userDao.register(new User("lorem@ipsum_.com", "password"));
        userDao.register(new User("lorem@ipsum_.com_", "password"));

        assertNull(userDao.find("lorem@ipsum.com"));
    }

    @Test
    public void test_user_login_incorrect_info() {
        String email = "lorem@ipsum.com", password = "password";

        userDao.register(new User(email, password));

        assertNull(userDao.login("_lorem@ipsum.com", password));
    }

    @Test
    public void test_user_login_correct_info() {
        String email = "lorem@ipsum.com", password = "password";

        userDao.register(new User(email, password));

        assertNotNull(userDao.login(email, password));
    }

    @Test
    public void test_todo_add_single_todo() {
        String email = "lorem@ipsum.com", password = "password";
        User user = new User(email, password);

        userDao.register(user);

        User dbUser = userDao.find(email);

        int userId = dbUser.getId();
        String title = "Lorem", context = "Ipsum", dueDate = "1624042294";

        ToDo toDo = new ToDo(userId, null, title, context, dueDate);
        toDoDao.create(toDo);

        assertEquals(1, toDoDao.getAll(userId).size());
    }

    @Test
    public void test_todo_get_all_todos_from_user() {
        String email = "lorem@ipsum.com", password = "password";
        User user = new User(email, password);

        userDao.register(user);

        User dbUser = userDao.find(email);

        int userId = dbUser.getId();

        List<ToDo> toDosToBeAdded = new ArrayList<>();
        toDosToBeAdded.add(new ToDo(userId, null, "title 1", "context", "1624042294"));
        toDosToBeAdded.add(new ToDo(userId, null, "title 2", "context", "1624042294"));
        toDosToBeAdded.add(new ToDo(userId, null, "title 3", "context", "1624042294"));

        toDoDao.create(toDosToBeAdded.get(0));
        toDoDao.create(toDosToBeAdded.get(1));
        toDoDao.create(toDosToBeAdded.get(2));

        List<ToDo> toDosFromDB = toDoDao.getAll(userId);

        assertEquals(toDosFromDB.size(), toDosToBeAdded.size());
    }

    @Test
    public void test_todo_get_all_completed_todos_from_user() {
        String email = "lorem@ipsum.com", password = "password";
        User user = new User(email, password);

        userDao.register(user);

        User dbUser = userDao.find(email);

        int userId = dbUser.getId();

        ToDo toDo1 = new ToDo(userId, null, "title 1", "context", "1624042294");
        toDo1.setDoneDate("1624042294");
        toDoDao.create(toDo1);

        ToDo toDo2 = new ToDo(userId, null, "title 2", "context", "1624042294");
        toDo2.setDoneDate("1624040294");
        toDoDao.create(toDo2);

        ToDo toDo3 = new ToDo(userId, null, "title 3", "context", "1624042294");
        toDoDao.create(toDo3);


        List<ToDo> toDosFromDB = toDoDao.getCompleted(userId);

        assertEquals(2, toDosFromDB.size());
    }

    @Test
    public void test_todo_get_all_uncompleted_todos_from_user() {
        String email = "lorem@ipsum.com", password = "password";
        User user = new User(email, password);

        userDao.register(user);

        User dbUser = userDao.find(email);

        int userId = dbUser.getId();

        ToDo toDo1 = new ToDo(userId, null, "title 1", "context", "1624042294");
        toDo1.setDoneDate("1624041294");
        toDoDao.create(toDo1);

        ToDo toDo2 = new ToDo(userId, null, "title 2", "context", "1624042294");
        toDo2.setDoneDate("1624040294");
        toDoDao.create(toDo2);

        ToDo toDo3 = new ToDo(userId, null, "title 3", "context", "1624042294");
        toDoDao.create(toDo3);


        List<ToDo> toDosFromDB = toDoDao.getUncompleted(userId);

        assertEquals(1, toDosFromDB.size());
    }

    @Test
    public void test_todo_get_single_todo_from_user() {
        String email = "lorem@ipsum.com", password = "password";
        User user = new User(email, password);

        userDao.register(user);

        User dbUser = userDao.find(email);

        int userId = dbUser.getId();

        ToDo toDo = new ToDo(userId, null, "title 1", "context", "1624042294");
        toDoDao.create(toDo);

        List<ToDo> toDosFromDB = toDoDao.getAll(userId);
        int todoId = toDosFromDB.get(0).getId();

        ToDo toDoFromDBFromToDoId = toDoDao.getToDo(todoId);

        assertNotNull(toDoFromDBFromToDoId);
    }

    @Test
    public void test_todo_edit() {
        String email = "lorem@ipsum.com", password = "password";
        User user = new User(email, password);

        userDao.register(user);

        User dbUser = userDao.find(email);

        int userId = dbUser.getId();

        ToDo toDo = new ToDo(userId, null, "title 1", "context", "1624042294");
        toDoDao.create(toDo);

        List<ToDo> toDosFromDB = toDoDao.getAll(userId);
        ToDo toDoFromDb = toDosFromDB.get(0);
        int todoId = toDoFromDb.getId();

        String originalTitle = toDoFromDb.getTitle();
        String originalContext = toDoFromDb.getContext();
        String originalDueDate = toDoFromDb.getDueDate();
        String originalDoneDate = toDoFromDb.getDoneDate();

        toDoFromDb.setTitle("New title");
        toDoFromDb.setContext("New context");
        toDoFromDb.setDueDate("123456789");
        toDoFromDb.setDoneDate("987654321");

        toDoDao.update(toDoFromDb);

        ToDo todoUpdatedFromDb = toDoDao.getToDo(todoId);

        assertNotEquals(originalTitle, todoUpdatedFromDb.getTitle());
        assertNotEquals(originalContext, todoUpdatedFromDb.getContext());
        assertNotEquals(originalDueDate, todoUpdatedFromDb.getDueDate());
        assertNotEquals(originalDoneDate, todoUpdatedFromDb.getDoneDate());
    }

    @Test
    public void test_todo_remove() {
        String email = "lorem@ipsum.com", password = "password";
        User user = new User(email, password);

        userDao.register(user);

        User dbUser = userDao.find(email);

        int userId = dbUser.getId();

        ToDo toDo = new ToDo(userId, null, "title 1", "context", "1624042294");
        toDoDao.create(toDo);

        assertEquals(1, toDoDao.getAll(userId).size());

        List<ToDo> toDosFromDB = toDoDao.getAll(userId);
        ToDo toDoFromDb = toDosFromDB.get(0);

        toDoDao.remove(toDoFromDb);

        assertEquals(0, toDoDao.getAll(userId).size());
    }

    @Test
    public void test_label_add_single_label() {
        String email = "lorem@ipsum.com", password = "password";
        User user = new User(email, password);

        userDao.register(user);

        User dbUser = userDao.find(email);

        int userId = dbUser.getId();
        String title = "Lorem ipsum";

        labelDao.create(new Label(userId, title));

        assertEquals(1, labelDao.getAll(userId).size());
    }

    @Test
    public void test_label_get_all_labels_from_user() {
        String email = "lorem@ipsum.com", password = "password";
        User user = new User(email, password);

        userDao.register(user);

        User dbUser = userDao.find(email);

        int userId = dbUser.getId();

        List<Label> labelsToBeAdded = new ArrayList<>();
        labelsToBeAdded.add(new Label(userId, "Label 1"));
        labelsToBeAdded.add(new Label(userId, "Label 2"));
        labelsToBeAdded.add(new Label(userId, "Label 3"));

        labelDao.create(labelsToBeAdded.get(0));
        labelDao.create(labelsToBeAdded.get(1));
        labelDao.create(labelsToBeAdded.get(2));

        List<Label> labelsFromDB = labelDao.getAll(userId);

        assertEquals(labelsFromDB.size(), labelsToBeAdded.size());
    }

    @Test
    public void test_label_get_single_label_from_user() {
        String email = "lorem@ipsum.com", password = "password";
        User user = new User(email, password);

        userDao.register(user);

        User dbUser = userDao.find(email);

        int userId = dbUser.getId();

        Label label = new Label(userId, "Label 1");
        labelDao.create(label);

        List<Label> labelsFromDB = labelDao.getAll(userId);
        int labelId = labelsFromDB.get(0).getId();

        Label labelFromDBFromLabelId = labelDao.getLabel(labelId);

        assertNotNull(labelFromDBFromLabelId);
    }

    @Test
    public void test_label_edit() {
        String email = "lorem@ipsum.com", password = "password";
        User user = new User(email, password);

        userDao.register(user);

        User dbUser = userDao.find(email);

        int userId = dbUser.getId();

        Label label = new Label(userId, "Label 1");
        labelDao.create(label);

        List<Label> labelsFromDB = labelDao.getAll(userId);
        Label labelFromDb = labelsFromDB.get(0);
        int labelId = labelFromDb.getId();

        String originalTitle = labelFromDb.getTitle();

        labelFromDb.setTitle("New title");

        labelDao.update(labelFromDb);

        Label labelUpdatedFromDb = labelDao.getLabel(labelId);

        assertNotEquals(originalTitle, labelUpdatedFromDb.getTitle());
    }

    @Test
    public void test_label_remove() {
        String email = "lorem@ipsum.com", password = "password";
        User user = new User(email, password);

        userDao.register(user);

        User dbUser = userDao.find(email);

        int userId = dbUser.getId();

        Label label = new Label(userId, "Label 1");
        labelDao.create(label);

        assertEquals(1, labelDao.getAll(userId).size());

        List<Label> labelsFromDB = labelDao.getAll(userId);
        Label labelFromDb = labelsFromDB.get(0);

        labelDao.remove(labelFromDb);

        assertEquals(0, labelDao.getAll(userId).size());
    }
}
