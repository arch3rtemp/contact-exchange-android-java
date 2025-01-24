package dev.arch3rtemp.contactexchange.ui.card;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.ui.home.IMainContract;
import dev.arch3rtemp.contactexchange.ui.home.MainPresenter;

public class CardActivity extends AppCompatActivity implements IMainContract.View {

    private LinearLayout back;
    private LinearLayout llScan;
    private IMainContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        initUI();
        setListeners();
        setPresenter(new MainPresenter(this));
        presenter.onCreate(this);
    }

    private void initUI() {
        back = findViewById(R.id.ll_back);
        llScan = findViewById(R.id.ll_qr_card);
        Intent intent = getIntent();

        FragmentType fragmentType;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            fragmentType = intent.getSerializableExtra("type", FragmentType.class);
        } else {
            fragmentType = (FragmentType) intent.getSerializableExtra("type");
        }
        switch (fragmentType) {
            case CREATE:
                initCreateCardFragment();
                break;
            case CARD:
                Bundle bundle = new Bundle();
                bundle.putBoolean("isMy", intent.getBooleanExtra("isMy", false));
                bundle.putInt("id", intent.getIntExtra("id", -1));
                initCardFragment(bundle);
                break;
            case DELETED:
                createDeletedFragment();
                break;
            default:
                break;
        }
    }

    private void setListeners() {
        back.setOnClickListener(v -> onBackPressed());

        llScan.setOnClickListener(v -> {
            presenter.scanContact();
        });
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(CardActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void initCreateCardFragment() {
        CreateOrEditCardFragment createCardFragment = new CreateOrEditCardFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isCreate", true);
        createCardFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_main_frame_container, createCardFragment);
//        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initCardFragment(Bundle bundle) {
        CardDetailsFragment cardDetailsFragment = CardDetailsFragment.getInstance();
        cardDetailsFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_main_frame_container, cardDetailsFragment);
//        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void createDeletedFragment() {
        DeletedFragment deletedFragment = new DeletedFragment();
        this.getSupportFragmentManager()
                .beginTransaction()
//                .setCustomAnimations(R.anim.slide_in, 0)
                .replace(R.id.fl_main_frame_container, deletedFragment)
                .commit();
    }

    @Override
    public void setPresenter(IMainContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
