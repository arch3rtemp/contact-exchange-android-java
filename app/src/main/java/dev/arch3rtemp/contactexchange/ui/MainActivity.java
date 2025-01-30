package dev.arch3rtemp.contactexchange.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.App;
import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.router.Router;
import dev.arch3rtemp.contactexchange.ui.filter.FilterFragment;
import dev.arch3rtemp.contactexchange.ui.home.HomeFragment;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    @Inject
    MainContract.Presenter presenter;
    @Inject
    Router router;
    private TextView tvTitle;
    private LinearLayout llBack;
    private LinearLayout llScan;

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
        llBack = findViewById(R.id.ll_back);
        tvTitle = findViewById(R.id.tv_title);
        llScan = findViewById(R.id.ll_qr_home);
        initHomeFragment();
    }

    private void setListeners() {
        llScan.setOnClickListener(v -> {
            presenter.scanContact();
        });

        llBack.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                updateToolbar(getCurrentFragment());
            }
        });

    }

    private void initHomeFragment() {
        router.navigate(HomeFragment.newInstance(), false, false);
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    private void updateToolbar(Fragment fragment) {
        if (fragment instanceof HomeFragment || fragment instanceof FilterFragment) {
            llBack.setVisibility(View.INVISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
        } else {
            llBack.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.GONE);
        }
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
