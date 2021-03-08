package com.example.contactsexchangejava.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.contactsexchangejava.R;
import com.example.contactsexchangejava.ui.fragments.CardFragment;
import com.example.contactsexchangejava.ui.fragments.CreateOrEditCardFragment;

public class CardActivity extends AppCompatActivity {

    FragmentManager manager;
    Intent intent;
    LinearLayout back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        initUI();
    }

    private void initUI() {
        back = findViewById(R.id.ll_back);
        manager = getSupportFragmentManager();
        intent = getIntent();
        setListeners();
        boolean isCreate = intent.getBooleanExtra("isCreate", false);
        if (isCreate)
            initCreateCardFragment();
        else
            initCardFragment();
    }

    private void setListeners() {
        back.setOnClickListener(v -> onBackPressed());
    }

    private void initCreateCardFragment() {
        CreateOrEditCardFragment createCardFragment = new CreateOrEditCardFragment(true);

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_main_frame_container, createCardFragment);
//        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initCardFragment() {
        Boolean isMe = intent.getBooleanExtra("isMe", false);
        CardFragment cardFragment = new CardFragment(isMe);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_main_frame_container, cardFragment);
//        transaction.addToBackStack(null);
        transaction.commit();
    }


}