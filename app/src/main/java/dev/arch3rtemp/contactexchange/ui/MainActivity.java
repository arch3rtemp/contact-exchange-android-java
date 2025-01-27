package dev.arch3rtemp.contactexchange.ui;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.ui.home.HomeFragment;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private LinearLayout llScan;

    private MainContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        setPresenter(new MainPresenter(this));
        presenter.onCreate(this);
        setListeners();
    }

    private void initUI() {
        llScan = findViewById(R.id.ll_qr_home);
        initHomeFragment();
    }

    private void setListeners() {
        llScan.setOnClickListener(v -> {
            presenter.scanContact();
        });
    }

    private void initHomeFragment() {
        var homeFragment = HomeFragment.getInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_main_frame_container, homeFragment);
        transaction.commit();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
