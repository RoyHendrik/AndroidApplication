package com.pkg.to_day.ui.todo.uncompleted;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.pkg.to_day.R;
import com.pkg.to_day.dao.LabelDao;
import com.pkg.to_day.dao.ToDoDao;
import com.pkg.to_day.dao.UserDao;
import com.pkg.to_day.database.AppDatabase;
import com.pkg.to_day.models.Label;
import com.pkg.to_day.models.ToDo;
import com.pkg.to_day.models.User;
import com.pkg.to_day.ui.todo.TodoValidator;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class UncompletedFragment extends Fragment implements TodoValidator {

    private static final ArrayList<ToDo> uncompletedTodoList = new ArrayList<>();
    private static UncompletedAdapter adapter;
    private RecyclerView recyclerView;
    private ToDo deletedTodo = null;
    private View view;

    public UncompletedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_uncompleted, container, false);
        adapter = new UncompletedAdapter(this.getContext(), uncompletedTodoList);
        recyclerView = view.findViewById(R.id.fg_todo_recyclerview_uncompleted);

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
        AppDatabase appDatabase = AppDatabase.getAppDatabase(getActivity().getApplicationContext());
        UserDao userDao = appDatabase.userDao();
        ToDoDao toDoDao = appDatabase.toDoDao();
        User user = userDao.find(getActivity().getIntent().getStringExtra("email"));

        uncompletedTodoList.clear();

        if (user != null) {
            List<ToDo> todos = toDoDao.getUncompleted(user.getId());
            uncompletedTodoList.addAll(todos);
        }

        adapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    deletedTodo = uncompletedTodoList.get(position);
                    removeToDo( deletedTodo, position);

                    Snackbar.make(recyclerView, deletedTodo.getTitle() + ", Deleted !", Snackbar.LENGTH_LONG)
                            .setAction("Undo", view -> {
                                addToDo(getActivity(), deletedTodo, position);
                                deletedTodo = null;
                            }).show();
                    break;
                case ItemTouchHelper.RIGHT:
                    showEditPopUp(getActivity(), uncompletedTodoList.get(position), position);
                    break;
            }
        }

        @Override
        public void onChildDraw(@NotNull Canvas c, @NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                View itemView = viewHolder.itemView;
                Paint paint = new Paint();
                int color = ContextCompat.getColor(view.getContext(), R.color.statusEdit);

                if (dX > 0) {
                    paint.setColor(color);
                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                            (float) itemView.getBottom(), paint);

                } else {
                    color = ContextCompat.getColor(view.getContext(), R.color.statusDelete);
                    paint.setColor(color);

                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                            (float) itemView.getRight(), (float) itemView.getBottom(), paint);
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
    };

    public void showEditPopUp(Activity activity, ToDo toDo, int position) {
        View mView = LayoutInflater.from(activity).inflate(R.layout.fragment_add_todo, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
        alertDialogBuilderUserInput.setView(mView);

        AppDatabase appDatabase = AppDatabase.getAppDatabase(activity.getApplicationContext());
        LabelDao labelDao = appDatabase.labelDao();
        UserDao userDao = appDatabase.userDao();
        User user = userDao.find(activity.getIntent().getStringExtra("email"));

        EditText title = mView.findViewById(R.id.todo_title);
        EditText dueDate = mView.findViewById(R.id.todo_due_date);
        EditText context = mView.findViewById(R.id.todo_context);
        MaterialAutoCompleteTextView labels = mView.findViewById(R.id.todo_set_label);

        AtomicReference<Label> selectedLabel = new AtomicReference<>();
        ArrayAdapter<Label> labelAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, labelDao.getAll(user.getId()));

        title.setText(toDo.getTitle());
        context.setText(toDo.getContext());
        dueDate.setText(toDo.getDueDate());
        labels.setAdapter(labelAdapter);
        labels.setOnItemClickListener((parent, view, iPosition, id) -> {
            Object item = parent.getItemAtPosition(iPosition);
            if (item instanceof Label) {
                Label label = (Label) item;
                selectedLabel.set(label);
            }
        });
        dueDate.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus)
                showDateTimeDialog(activity, dueDate);
        });

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Save", (dialogBox, id) -> {
                    Label label = selectedLabel.get();

                    toDo.setTitle(title.getText().toString());
                    toDo.setContext(context.getText().toString());
                    toDo.setDueDate(dueDate.getText().toString());

                    if (label != null)
                        toDo.setLabelID(label.getId());

                    ArrayList<String> validationErrors = validateTodo(toDo.getTitle(), toDo.getContext(), toDo.getDueDate());

                    if (validationErrors.size() == 0) {
                        editToDo(toDo, position);
                    } else {
                        validationErrors.forEach(error -> activity.runOnUiThread(() -> Toast.makeText(activity.getApplicationContext(), error, Toast.LENGTH_LONG).show()));
                    }

                }).setNegativeButton("Cancel",
                (dialogBox, id) -> {
                    adapter.notifyDataSetChanged();
                    dialogBox.cancel();
                });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    public static void showDateTimeDialog(Activity activity, final EditText dueDate) {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            TimePickerDialog.OnTimeSetListener timeSetListener = (view1, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY HH:mm");

                dueDate.setText(simpleDateFormat.format(calendar.getTime()));
            };

            new TimePickerDialog(activity, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        };

        new DatePickerDialog(activity, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    public static void addToDo(Activity activity, ToDo toDo, int position) {
        AppDatabase appDatabase = AppDatabase.getAppDatabase(activity.getApplicationContext());
        ToDoDao toDoDao = appDatabase.toDoDao();

        toDoDao.create(toDo);

        if (position > -1) {
            uncompletedTodoList.add(position, toDo);
            adapter.notifyItemInserted(position);
        } else {
            uncompletedTodoList.add(toDo);
            adapter.notifyDataSetChanged();
        }
    }

    private void removeToDo(ToDo toDo, int position) {
        AppDatabase appDatabase = AppDatabase.getAppDatabase(getContext());
        ToDoDao toDoDao = appDatabase.toDoDao();

        toDoDao.remove(toDo);

        uncompletedTodoList.remove(toDo);
        adapter.notifyItemRemoved(position);
    }

    private void editToDo(ToDo toDo, int position) {
        AppDatabase appDatabase = AppDatabase.getAppDatabase(getContext());
        ToDoDao toDoDao = appDatabase.toDoDao();

        toDoDao.update(toDo);

        uncompletedTodoList.set(position, toDo);
        adapter.notifyItemChanged(position);
    }
}