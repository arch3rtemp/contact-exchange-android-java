package dev.arch3rtemp.contactexchange.ui.home;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import dev.arch3rtemp.contactexchange.db.AppDatabase;
import dev.arch3rtemp.contactexchange.db.models.Contact;

public class HomeActivity extends AppCompatActivity {

    FragmentManager manager = getSupportFragmentManager();
    HomeFragment homeFragment;
    LinearLayout llScan;
    TextView tvMyCards;
    GmsBarcodeScanner scanner;
    AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        appDatabase = AppDatabase.getDBInstance(this);
        initUI();
        initScanner();
        setListeners();
    }

    private void initUI() {
        llScan = findViewById(R.id.ll_qr_home);
        tvMyCards = findViewById(R.id.tv_my_cards);
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
                                appDatabase.contactDao().insert(contact);
                                Toast.makeText(HomeActivity.this, "Contact added", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        });
    }

    private void initHomeFragment() {
        homeFragment = HomeFragment.getInstance();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fl_main_frame_container, homeFragment);
        transaction.commit();
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
