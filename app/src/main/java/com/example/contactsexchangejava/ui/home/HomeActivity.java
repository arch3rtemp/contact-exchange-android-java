package com.example.contactsexchangejava.ui.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactsexchangejava.R;
import com.example.contactsexchangejava.ui.qr.QrActivity;


public class HomeActivity extends AppCompatActivity {

    FragmentManager manager = getSupportFragmentManager();
    HomeFragment homeFragment;
    LinearLayout llScan;
    TextView tvMyCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initUI();
        setListeners();
    }

    private void initUI() {
        llScan = findViewById(R.id.ll_qr_home);
        tvMyCards = findViewById(R.id.tv_my_cards);
        initHomeFragment();
    }

    private void setListeners() {
        llScan.setOnClickListener(v -> {
            Intent intent = new Intent(this, QrActivity.class);
            startActivityForResult(intent, 121);
        });
    }

    private void initHomeFragment() {
        homeFragment = HomeFragment.getInstance();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fl_main_frame_container, homeFragment);
        transaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 121) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Contact added", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}