package dev.arch3rtemp.contactexchange.ui.card;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.ui.MainContract;
import dev.arch3rtemp.contactexchange.ui.MainPresenter;

public class CardActivity extends AppCompatActivity implements MainContract.View {

    private LinearLayout back;
    private LinearLayout llScan;
    private MainContract.Presenter presenter;

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
            fragmentType = intent.getSerializableExtra(TYPE, FragmentType.class);
        } else {
            fragmentType = (FragmentType) intent.getSerializableExtra(TYPE);
        }
        switch (fragmentType) {
            case CREATE:
                initCreateCardFragment();
                break;
            case CARD:
                initCardFragment(intent.getIntExtra(ID, -1), intent.getBooleanExtra("isMy", false));
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
        var bundle = new Bundle();
        bundle.putBoolean(IS_CREATE, true);
        var transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_main_frame_container, CreateOrEditCardFragment.class, bundle, CreateOrEditCardFragment.class.getSimpleName());
        transaction.commit();
    }

    private void initCardFragment(int id, boolean isMy) {
        var bundle = new Bundle();
        bundle.putInt(ID, id);
        bundle.putBoolean(IS_MY, isMy);
        var transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_main_frame_container, CardDetailsFragment.class, bundle, CardDetailsFragment.class.getSimpleName());
        transaction.commit();
    }

    private void createDeletedFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_main_frame_container, DeletedFragment.class, null, DeletedFragment.class.getSimpleName())
                .commit();
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

    public static void start(Context context, int id, boolean isMy, FragmentType fragmentType) {
        Intent intent = new Intent(context, CardActivity.class);
        intent.putExtra(TYPE, fragmentType);
        intent.putExtra(IS_MY, isMy);
        intent.putExtra(ID, id);
        context.startActivity(intent);
    }

    public static final String TYPE = "type";
    public static final String IS_MY = "isMy";
    public static final String ID = "id";
    public static final String IS_CREATE = "isCreate";
}
