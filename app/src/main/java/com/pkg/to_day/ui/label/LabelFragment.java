package com.pkg.to_day.ui.label;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.pkg.to_day.R;
import com.pkg.to_day.dao.LabelDao;
import com.pkg.to_day.dao.UserDao;
import com.pkg.to_day.database.AppDatabase;
import com.pkg.to_day.models.Label;
import com.pkg.to_day.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LabelFragment extends Fragment implements LabelValidator {

    private static final ArrayList<Label> labelList = new ArrayList<>();
    private static LabelAdapter adapter;
    private RecyclerView recyclerView;
    private Label deletedLabel = null;
    private View view;

    public LabelFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_label, container, false);
        adapter = new LabelAdapter(labelList);
        recyclerView = view.findViewById(R.id.fg_label_recyclerview);

        initRecyclerView();
        loadLabelList();

        return view;
    }

    private void initRecyclerView() {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void loadLabelList() {
        AppDatabase appDatabase = AppDatabase.getAppDatabase(view.getContext());
        UserDao userDao = appDatabase.userDao();
        LabelDao labelDao = appDatabase.labelDao();

        labelList.clear();

        User user = userDao.find(getActivity().getIntent().getStringExtra("email"));

        if (user != null) {
            List<Label> labels = labelDao.getAll(user.getId());
            labelList.addAll(labels);
        }

        recyclerView.invalidate();
    }

    public void showPopUp(Activity activity) {
        View mView = LayoutInflater.from(activity).inflate(R.layout.activity_label, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
        alertDialogBuilderUserInput.setView(mView);

        EditText userInput = mView.findViewById(R.id.userInputDialog);
        AppDatabase appDatabase = AppDatabase.getAppDatabase(activity.getApplicationContext());
        UserDao userDao = appDatabase.userDao();

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Add", (dialogBox, id) -> {
                    User user = userDao.find(activity.getIntent().getStringExtra("email"));

                    if (user != null) {
                        Label label = new Label(user.getId(), userInput.getText().toString());
                        ArrayList<String> validationErrors = validateLabel(label.getTitle());

                        if (validationErrors.size() == 0) {
                            addLabel(activity, label, -1);
                        } else {
                            validationErrors.forEach(error ->
                                    activity.runOnUiThread(() -> Toast.makeText(activity.getApplicationContext(), error, Toast.LENGTH_LONG).show()));

                        }
                    }
                })
                .setNegativeButton("Cancel", (dialogBox, id) -> {
                    adapter.notifyDataSetChanged();
                    dialogBox.cancel();
                });

        alertDialogBuilderUserInput.create().show();
    }

    public void showPopUp(Activity activity, Label label, int position) {
        View mView = LayoutInflater.from(activity).inflate(R.layout.activity_label, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
        alertDialogBuilderUserInput.setView(mView);

        EditText userInput = mView.findViewById(R.id.userInputDialog);
        userInput.setText(label.getTitle());

        TextView dialogTitle = mView.findViewById(R.id.dialogTitle);
        dialogTitle.setText(R.string.edit_label);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Save", (dialogBox, id) -> {
                    label.setTitle(userInput.getText().toString());

                    ArrayList<String> validationErrors = validateLabel(label.getTitle());

                    if (validationErrors.size() == 0) {
                        editLabel(activity, label, position);
                    } else {
                        validationErrors.forEach(error ->
                                activity.runOnUiThread(() -> Toast.makeText(activity.getApplicationContext(), error, Toast.LENGTH_LONG).show()));
                    }
                })
                .setNegativeButton("Cancel", (dialogBox, id) -> {
                    adapter.notifyDataSetChanged();
                    dialogBox.cancel();
                });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    deletedLabel = labelList.get(position);
                    removeLabel(getActivity(), deletedLabel, position);

                    Snackbar.make(recyclerView, deletedLabel.getTitle() + ", Deleted !", Snackbar.LENGTH_LONG)
                            .setAction("Undo", view -> {
                                addLabel(getActivity(), deletedLabel, position);
                                deletedLabel = null;
                            }).show();
                    break;
                case ItemTouchHelper.RIGHT:
                    showPopUp(getActivity(), labelList.get(position), position);
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

    private void addLabel(Activity activity, Label label, final int position) {
        AppDatabase appDatabase = AppDatabase.getAppDatabase(activity.getApplicationContext());
        LabelDao labelDao = appDatabase.labelDao();

        long labelID = labelDao.create(label);

        Label createdLabel = labelDao.getLabel(labelID);

        if (position == -1)
            labelList.add(createdLabel);
        else
            labelList.add(position, createdLabel);

        adapter.notifyDataSetChanged();
    }

    private void removeLabel(Activity activity, Label label, int position) {
        AppDatabase appDatabase = AppDatabase.getAppDatabase(activity.getApplicationContext());
        LabelDao labelDao = appDatabase.labelDao();

        labelDao.remove(label);
        labelList.remove(label);

        adapter.notifyItemRemoved(position);
    }

    private void editLabel(Activity activity, Label label, int position) {
        AppDatabase appDatabase = AppDatabase.getAppDatabase(activity.getApplicationContext());
        LabelDao labelDao = appDatabase.labelDao();

        labelDao.update(label);

        labelList.set(position, label);
        adapter.notifyItemChanged(position);
    }
}