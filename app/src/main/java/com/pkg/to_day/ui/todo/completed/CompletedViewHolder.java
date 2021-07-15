package com.pkg.to_day.ui.todo.completed;

import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pkg.to_day.R;

public class CompletedViewHolder extends RecyclerView.ViewHolder {
    TextView title, label, date;
    RadioButton check;
    CardView card;

    public CompletedViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.fg_todo_card_title);
        label = itemView.findViewById(R.id.fg_todo_card_label);
        date = itemView.findViewById(R.id.fg_todo_card_date);
        check = itemView.findViewById(R.id.fg_todo_card_check);
        card = itemView.findViewById(R.id.fg_todo_card);
    }
}