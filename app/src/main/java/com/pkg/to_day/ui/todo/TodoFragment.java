package com.pkg.to_day.ui.todo;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.pkg.to_day.R;
import com.pkg.to_day.dao.LabelDao;
import com.pkg.to_day.dao.UserDao;
import com.pkg.to_day.database.AppDatabase;
import com.pkg.to_day.databinding.FragmentTodoBinding;
import com.pkg.to_day.models.Label;
import com.pkg.to_day.models.ToDo;
import com.pkg.to_day.models.User;
import com.pkg.to_day.ui.todo.uncompleted.UncompletedFragment;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class TodoFragment extends Fragment implements TodoValidator {

    private static TodoAdapter adapter;
    private static TabLayout tabLayout;
    private FragmentTodoBinding binding;
    private ViewPager2 viewpager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTodoBinding.inflate(inflater, container, false);
        adapter = new TodoAdapter(getChildFragmentManager(), getLifecycle());

        initTabLayout();
        initViewpager();
        initListeners();

        return binding.getRoot();
    }

    private void initTabLayout() {
        tabLayout = binding.fgTodoTabLayout;
        tabLayout.addTab(tabLayout.newTab().setText("Uncompleted"));
        tabLayout.addTab(tabLayout.newTab().setText("completed"));
    }

    private void initViewpager() {
        viewpager = binding.fgTodoViewpager;
        viewpager.setAdapter(adapter);
        viewpager.setUserInputEnabled(false);
        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    private void initListeners() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void addNewTodoPopup(Activity activity) {
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
        AtomicReference<Label> selectedLabel = new AtomicReference<>();
        ArrayAdapter<Label> labelAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, labelDao.getAll(user.getId()));
        MaterialAutoCompleteTextView labels = mView.findViewById(R.id.todo_set_label);
        TextView todoHeader = mView.findViewById(R.id.todo_header);

        todoHeader.setText(R.string.create_todo_task);

        labels.setAdapter(labelAdapter);
        labels.setOnItemClickListener((parent, view, position, id) -> {
            Object item = parent.getItemAtPosition(position);
            if (item instanceof Label) {
                Label label = (Label) item;
                selectedLabel.set(label);
            }
        });
        dueDate.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus)
                UncompletedFragment.showDateTimeDialog(activity, dueDate);
        });

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Add", (dialogBox, id) -> {
                    Label label = selectedLabel.get();
                    ToDo toDo;

                    if (label != null) {
                        toDo = new ToDo(user.getId(), label.getId(), title.getText().toString(),
                                context.getText().toString(), dueDate.getText().toString());
                    } else {
                        toDo = new ToDo(user.getId(), title.getText().toString(),
                                context.getText().toString(), dueDate.getText().toString());
                    }

                    ArrayList<String> validationErrors = validateTodo(toDo.getTitle(), toDo.getContext(), toDo.getDueDate());

                    if (validationErrors.size() == 0) {
                        UncompletedFragment.addToDo(activity, toDo, -1);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}