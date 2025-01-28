package dev.arch3rtemp.contactexchange.ui;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.App;
import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.router.Router;
import dev.arch3rtemp.contactexchange.ui.home.HomeFragment;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private LinearLayout llScan;
    @Inject
    MainContract.Presenter presenter;
    @Inject
    Router router;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((App) getApplication()).getAppComponent().activityComponent().create(this).inject(this);
        initUI();
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
        router.navigate(HomeFragment.class, null, false);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
