package com.example.contactsexchangejava.ui.card;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.contactsexchangejava.R;
import com.example.contactsexchangejava.ui.qr.QrActivity;

public class CardActivity extends AppCompatActivity {

    FragmentManager manager;
    Intent intent;
    LinearLayout back;
    LinearLayout llScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        initUI();
    }

    private void initUI() {
        back = findViewById(R.id.ll_back);
        llScan = findViewById(R.id.ll_qr_card);
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

        llScan.setOnClickListener(v -> {
            Intent intent = new Intent(this, QrActivity.class);
            startActivityForResult(intent, 122);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 122) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Contact added", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initCreateCardFragment() {
        CreateOrEditCardFragment createCardFragment = new CreateOrEditCardFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isCreate", true);
        createCardFragment.setArguments(bundle);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_main_frame_container, createCardFragment);
//        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initCardFragment() {
        int isMe = intent.getIntExtra("isMe", 0);
        CardFragment cardFragment = CardFragment.getInstance();
        Bundle bundle = new Bundle();
        bundle.putInt("isMe", isMe);
        bundle.putInt("id", intent.getIntExtra("id", -1));
        cardFragment.setArguments(bundle);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_main_frame_container, cardFragment);
//        transaction.addToBackStack(null);
        transaction.commit();
    }


}