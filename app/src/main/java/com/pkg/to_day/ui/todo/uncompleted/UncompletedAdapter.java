package com.pkg.to_day.ui.todo.uncompleted;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.pkg.to_day.R;
import com.pkg.to_day.dao.LabelDao;
import com.pkg.to_day.dao.ToDoDao;
import com.pkg.to_day.database.AppDatabase;
import com.pkg.to_day.models.Label;
import com.pkg.to_day.models.ToDo;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class UncompletedAdapter extends RecyclerView.Adapter<UncompletedViewHolder> {

    private final ArrayList<ToDo> todoList;
    private final Context context;

    public UncompletedAdapter(Context context, ArrayList<ToDo> todoList) {
        this.todoList = todoList;
        this.context = context;
    }

    @NotNull
    @Override
    public UncompletedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_card_todo, parent, false);

        return new UncompletedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UncompletedViewHolder holder, int position) {
        AppDatabase appDatabase = AppDatabase.getAppDatabase(holder.card.getContext());
        ToDoDao toDoDao = appDatabase.toDoDao();
        LabelDao labelDao = appDatabase.labelDao();
        ToDo todo = todoList.get(position);
        Label label;

        if (todo.getLabelID() != null) {
            label = labelDao.getLabel(todo.getLabelID());

            if (label != null) {
                holder.label.setText(label.getTitle());
            } else {
                holder.label.setText("");
            }

        } else {
            holder.label.setText("");
        }

        holder.date.setText(todo.getDueDate());
        holder.title.setText(todo.getTitle());
        holder.check.setChecked(false);
        holder.card.setOnClickListener(v -> showInfoPopup(todo));
        holder.check.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY HH:mm");

            todo.setDoneDate(simpleDateFormat.format(calendar.getTime()));
            toDoDao.update(todo);

            holder.check.setChecked(true);
            this.todoList.remove(todo);
            this.notifyDataSetChanged();
        });
    }


    public void showInfoPopup(ToDo toDo) {
        View infoFragment = LayoutInflater.from(this.context).inflate(R.layout.fragment_info_todo, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.context);
        alertDialog.setView(infoFragment);
        alertDialog.setCancelable(false).setPositiveButton("Got it!", null);

        TextView title = infoFragment.findViewById(R.id.fg_todo_popup_info_title_value);
        TextView dueDate = infoFragment.findViewById(R.id.fg_todo_popup_info_due_date_value);
        TextView context = infoFragment.findViewById(R.id.fg_todo_popup_info_context_value);
        TextView labels = infoFragment.findViewById(R.id.fg_todo_popup_info_label_value);

        title.setText(toDo.getTitle());
        dueDate.setText(toDo.getDueDate());
        context.setText(toDo.getContext());

        try {
            AppDatabase appDatabase = AppDatabase.getAppDatabase(this.context);
            LabelDao labelDao = appDatabase.labelDao();
            Label label = labelDao.getLabel(toDo.getLabelID());

            labels.setText(label.getTitle());
        } catch (Exception e) {
            labels.setText(R.string.empty_label);
        }

        alertDialog.create().show();
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }
}