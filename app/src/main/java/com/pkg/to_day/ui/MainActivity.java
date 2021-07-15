package com.pkg.to_day.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pkg.to_day.R;
import com.pkg.to_day.databinding.ActivityMainBinding;
import com.pkg.to_day.ui.label.LabelFragment;
import com.pkg.to_day.ui.todo.TodoFragment;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.pkg.to_day.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.fg_todo, R.id.fg_label).build();
        navController = Navigation.findNavController(this, R.id.act_main_navigation_host);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.actMainBottomNavigationView, navController);

        fab = findViewById(R.id.act_main_fab_add);

        initListeners();
    }

    private void initListeners() {
        fab.setOnClickListener(view -> {
            try {
                Fragment hostFragment = getSupportFragmentManager().findFragmentById(R.id.act_main_navigation_host).getChildFragmentManager().getFragments().get(0);

                if (navController.getCurrentDestination().getId() == R.id.fg_label) {
                    LabelFragment labelFragment = (LabelFragment) hostFragment;
                    labelFragment.showPopUp(this);
                }

                if (navController.getCurrentDestination().getId() == R.id.fg_todo) {
                    TodoFragment todoFragment = (TodoFragment) hostFragment;
                    todoFragment.addNewTodoPopup(this);
                }
            } catch (NullPointerException e) {
                Log.d("[ERROR]", e.toString());
            }
        });
    }
}