package dev.arch3rtemp.contactexchange.ui.card;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import org.json.JSONException;
import org.json.JSONObject;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.constants.FragmentType;
import dev.arch3rtemp.contactexchange.db.AppDatabase;
import dev.arch3rtemp.contactexchange.db.models.Contact;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CardActivity extends AppCompatActivity {

    FragmentManager manager;
    Intent intent;
    LinearLayout back;
    LinearLayout llScan;
    GmsBarcodeScanner scanner;
    AppDatabase appDatabase;
    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        compositeDisposable = new CompositeDisposable();
        appDatabase = AppDatabase.getDBInstance(this);
        initUI();
        initScanner();
    }

    private void initUI() {
        back = findViewById(R.id.ll_back);
        llScan = findViewById(R.id.ll_qr_card);
        manager = getSupportFragmentManager();
        intent = getIntent();
        setListeners();
        int fragmentType = intent.getIntExtra("type", 0);
        switch (fragmentType) {
            case FragmentType.CREATE:
                initCreateCardFragment();
                break;
            case FragmentType.CARD:
                Bundle bundle = new Bundle();
                bundle.putInt("isMe", intent.getIntExtra("isMe", 0));
                bundle.putInt("id", intent.getIntExtra("id", -1));
                initCardFragment(bundle);
                break;
            case FragmentType.DELETED:
                createDeletedFragment();
                break;
            default:
                break;
        }
    }

    private void initScanner() {
        var options = new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .enableAutoZoom()
                .build();

        scanner = GmsBarcodeScanning.getClient(this, options);
    }

    private void setListeners() {
        back.setOnClickListener(v -> onBackPressed());

        llScan.setOnClickListener(v -> {
            scanner.startScan()
                    .addOnSuccessListener(new OnSuccessListener<Barcode>() {
                        @Override
                        public void onSuccess(Barcode barcode) {
                            try {
                                var contact = new Contact(new JSONObject(barcode.getRawValue()));
                                var disposable = appDatabase.contactDao().insert(contact)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> showMessage("Contact added"),
                                                throwable -> showMessage(throwable.getLocalizedMessage()));
                                compositeDisposable.add(disposable);
                            } catch (JSONException e) {
                                showMessage(e.getLocalizedMessage());
                            }
                        }
                    });
        });
    }

    private void showMessage(String message) {
        Toast.makeText(CardActivity.this, message, Toast.LENGTH_SHORT).show();
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

    private void initCardFragment(Bundle bundle) {
        CardFragment cardFragment = CardFragment.getInstance();
        cardFragment.setArguments(bundle);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_main_frame_container, cardFragment);
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
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
