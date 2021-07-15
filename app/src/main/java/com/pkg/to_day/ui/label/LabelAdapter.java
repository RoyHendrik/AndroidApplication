package com.pkg.to_day.ui.label;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pkg.to_day.R;
import com.pkg.to_day.models.Label;

import java.util.ArrayList;

public class LabelAdapter extends RecyclerView.Adapter<LabelViewHolder> {

    private final ArrayList<Label> labelList;

    public LabelAdapter(ArrayList<Label> labelList) {
        this.labelList = labelList;
    }

    @NonNull
    @Override
    public LabelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_card_label, parent, false);

        return new LabelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LabelViewHolder holder, int position) {
        Label label = labelList.get(position);

        holder.title.setText(label.getTitle());
    }

    @Override
    public int getItemCount() {
        return labelList.size();
    }
}