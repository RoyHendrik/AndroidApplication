package com.pkg.to_day.ui.todo.completed;

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

import java.util.ArrayList;

public class CompletedAdapter extends RecyclerView.Adapter<CompletedViewHolder> {

    private final ArrayList<ToDo> todoList;
    private final Context context;

    public CompletedAdapter(Context context, ArrayList<ToDo> todoList) {
        this.todoList = todoList;
        this.context = context;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.fragment_card_todo;
    }

    @NonNull
    @Override
    public CompletedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_card_todo, parent, false);

        return new CompletedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedViewHolder holder, int position) {
        AppDatabase appDatabase = AppDatabase.getAppDatabase(holder.card.getContext());
        ToDoDao toDoDao = appDatabase.toDoDao();
        LabelDao labelDao = appDatabase.labelDao();
        ToDo todo = todoList.get(position);
        Label label;

        if (todo.getLabelID() != null) {
            label = labelDao.getLabel(todo.getLabelID());

            if (label != null) {
                holder.label.setText(label.getTitle());
            }
        }

        holder.date.setText(todo.getDoneDate());
        holder.title.setText(todo.getTitle());
        holder.card.setAlpha((float) 0.75);
        holder.check.setChecked(true);
        holder.card.setOnClickListener(v -> showInfoPopup(todo));
        holder.check.setOnClickListener(v -> {
            todo.setDoneDate(null);
            toDoDao.update(todo);

            holder.check.setChecked(false);
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
        TextView doneDate = infoFragment.findViewById(R.id.fg_todo_popup_info_done_date_value);

        title.setText(toDo.getTitle());
        dueDate.setText(toDo.getDueDate());
        context.setText(toDo.getContext());
        doneDate.setText(toDo.getDoneDate());

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