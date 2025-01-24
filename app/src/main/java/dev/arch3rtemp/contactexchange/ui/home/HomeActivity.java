package dev.arch3rtemp.contactexchange.ui.home;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import org.json.JSONException;
import org.json.JSONObject;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.db.AppDatabase;
import dev.arch3rtemp.contactexchange.db.models.Contact;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout llScan;
    private GmsBarcodeScanner scanner;
    private AppDatabase appDatabase;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        compositeDisposable = new CompositeDisposable();
        appDatabase = AppDatabase.getDBInstance(this);
        initUI();
        initScanner();
        setListeners();
    }

    private void initUI() {
        llScan = findViewById(R.id.ll_qr_home);
        initHomeFragment();
    }

    private void initScanner() {
        var options = new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .enableAutoZoom()
                .build();

        scanner = GmsBarcodeScanning.getClient(this, options);
    }

    private void setListeners() {
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
                                        .subscribe(() -> showMessage(getString(R.string.msg_contact_added)),
                                                throwable -> showMessage(throwable.getLocalizedMessage()));
                                compositeDisposable.add(disposable);
                            } catch (JSONException e) {
                                showMessage(e.getLocalizedMessage());
                            }
                        }
                    });
        });
    }

    private void initHomeFragment() {
        var homeFragment = HomeFragment.getInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_main_frame_container, homeFragment);
        transaction.commit();
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
