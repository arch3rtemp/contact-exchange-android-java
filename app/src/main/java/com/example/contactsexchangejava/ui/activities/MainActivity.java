package com.example.contactsexchangejava.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.contactsexchangejava.R;
import com.example.contactsexchangejava.ui.fragments.HomeFragment;


public class MainActivity extends AppCompatActivity {

    FragmentManager manager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI() {
        initHomeFragment();
    }

    private void initHomeFragment() {
        HomeFragment homeFragment = HomeFragment.getInstance();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fl_main_frame_container, homeFragment);
        transaction.commit();
    }

    void startCardActivity() {
        Intent intent = new Intent(this, CardActivity.class);
        startActivity(intent);
    }
}