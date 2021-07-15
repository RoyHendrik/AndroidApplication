package com.pkg.to_day.ui.label;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pkg.to_day.R;

public class LabelViewHolder extends RecyclerView.ViewHolder {
    TextView title;

    public LabelViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.fg_label_card_title);
    }
}