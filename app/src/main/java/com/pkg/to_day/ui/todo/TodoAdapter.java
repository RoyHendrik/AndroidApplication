package com.pkg.to_day.ui.todo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.pkg.to_day.ui.todo.completed.CompletedFragment;
import com.pkg.to_day.ui.todo.uncompleted.UncompletedFragment;

public class TodoAdapter extends FragmentStateAdapter {
    public TodoAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) return new UncompletedFragment();
        return new CompletedFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
