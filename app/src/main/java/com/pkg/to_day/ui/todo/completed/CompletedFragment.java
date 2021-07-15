package com.pkg.to_day.ui.todo.completed;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.pkg.to_day.R;
import com.pkg.to_day.dao.ToDoDao;
import com.pkg.to_day.dao.UserDao;
import com.pkg.to_day.database.AppDatabase;
import com.pkg.to_day.models.ToDo;
import com.pkg.to_day.models.User;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CompletedFragment extends Fragment {

    private static final ArrayList<ToDo> completedTodoList = new ArrayList<>();
    private CompletedAdapter adapter;
    private RecyclerView recyclerView;
    private ToDo deletedTodo = null;
    private View view;

    public CompletedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_completed, container, false);
        adapter = new CompletedAdapter(this.getContext(), completedTodoList);
        recyclerView = view.findViewById(R.id.fg_todo_recyclerview_completed);

        fillTodoList();
        initRecyclerView();

        return view;
    }

    @Override
    public void onResume() {
        fillTodoList();
        super.onResume();
    }

    private void fillTodoList() {
        AppDatabase appDatabase = AppDatabase.getAppDatabase(getContext());
        UserDao userDao = appDatabase.userDao();
        ToDoDao toDoDao = appDatabase.toDoDao();
        User user = userDao.find(getActivity().getIntent().getStringExtra("email"));

        completedTodoList.clear();

        if (user != null) {
            ArrayList<ToDo> todos = new ArrayList<>(toDoDao.getCompleted(user.getId()));

            todos.sort((toDo1, toDo2) -> {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY HH:mm");
                long timeToDo1 = 0;
                long timeToDo2 = 0;
                try {
                    timeToDo1 = sdf.parse(toDo1.getDoneDate()).getTime();
                    timeToDo2 = sdf.parse(toDo2.getDoneDate()).getTime();
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
                return Long.compare(timeToDo2, timeToDo1);
            });

            completedTodoList.addAll(todos);
        }

        adapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();

            if (direction == ItemTouchHelper.LEFT) {
                deletedTodo = completedTodoList.get(position);
                removeToDo(deletedTodo, position);

                Snackbar.make(recyclerView, deletedTodo.getTitle() + ", Deleted !", Snackbar.LENGTH_LONG)
                        .setAction("Undo", view -> {
                            addToDo(deletedTodo, position);
                            deletedTodo = null;
                            adapter.notifyDataSetChanged();
                        }).show();
            }
        }

        @Override
        public void onChildDraw(@NotNull Canvas c, @NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                View itemView = viewHolder.itemView;
                Paint paint = new Paint();

                if (dX < 0) {
                    int color = ContextCompat.getColor(view.getContext(), R.color.statusDelete);
                    paint.setColor(color);

                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                            (float) itemView.getRight(), (float) itemView.getBottom(), paint);
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
    };

    private void removeToDo(ToDo toDo, int position) {
        AppDatabase appDatabase = AppDatabase.getAppDatabase(getContext());
        ToDoDao toDoDao = appDatabase.toDoDao();

        toDoDao.remove(toDo);

        completedTodoList.remove(toDo);
        adapter.notifyItemRemoved(position);
    }

    private void addToDo(ToDo toDo, int position) {
        AppDatabase appDatabase = AppDatabase.getAppDatabase(getContext());
        ToDoDao toDoDao = appDatabase.toDoDao();

        toDoDao.create(toDo);

        if (position > -1) {
            completedTodoList.add(position, toDo);
            adapter.notifyItemInserted(position);
        } else {
            completedTodoList.add(toDo);
            adapter.notifyDataSetChanged();
        }
    }
}