package dev.arch3rtemp.contactexchange.ui.card;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.App;
import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.router.Router;
import dev.arch3rtemp.contactexchange.ui.MainContract;

public class CardActivity extends AppCompatActivity implements MainContract.View {

    private LinearLayout back;
    private LinearLayout llScan;
    @Inject
    MainContract.Presenter presenter;
    @Inject
    Router router;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        ((App) getApplication()).getAppComponent().activityComponent().create(this).inject(this);
        initUI();
        setListeners();
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
        router.navigate(CreateOrEditCardFragment.class, bundle, false);
    }

    private void initCardFragment(int id, boolean isMy) {
        var bundle = new Bundle();
        bundle.putInt(ID, id);
        bundle.putBoolean(IS_MY, isMy);
        router.navigate(CardDetailsFragment.class, bundle, false);
    }

    private void createDeletedFragment() {
        router.navigate(DeletedFragment.class, null, false);
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

    @Override
    public Context getContext() {
        return this;
    }
}
