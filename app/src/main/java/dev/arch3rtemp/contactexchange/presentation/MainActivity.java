package dev.arch3rtemp.contactexchange.presentation;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.databinding.ActivityMainBinding;
import io.reactivex.rxjava3.disposables.Disposable;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Inject
    MainPresenter presenter;
    private NavController navController;
    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initNavigationWithToolbar();
        setListeners();
        setObservables();
    }

    private void initNavigationWithToolbar() {
        setSupportActionBar(binding.toolbar);

        var navHost = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container_view);

        navController = Objects.requireNonNull(navHost).getNavController();

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            String title;

            if (destination.getId() == R.id.homeFragment) {
                binding.llBack.setVisibility(View.INVISIBLE);
                title = getString(R.string.home_cards);
            } else {
                binding.llBack.setVisibility(View.VISIBLE);
                title = "";
            }

            Objects.requireNonNull(getSupportActionBar()).setTitle(title);
        });
    }

    private void setListeners() {

        binding.llQrScanner.setOnClickListener(v -> initScanner());

        binding.llBack.setOnClickListener(v -> {
            navController.navigateUp();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
            }
        });
    }

    private void setObservables() {
        disposable = presenter
                .effectStream()
                .subscribe(this::renderEffect);
    }

    private void renderEffect(MainContract.MainEffect effect) {
        if (effect instanceof MainContract.MainEffect.ShowMessage showMessage) {
            Toast.makeText(this, showMessage.message(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initScanner() {
        var options = new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .enableAutoZoom()
                .build();

        var scanner = GmsBarcodeScanning.getClient(this, options);

        scanner
                .startScan()
                .addOnSuccessListener(barcode -> {
                    String value = barcode.getRawValue();
                    presenter.setEvent(new MainContract.MainEvent.OnQrScanComplete(value));
                }).addOnCanceledListener(() -> {
                    presenter.setEvent(new MainContract.MainEvent.OnQrScanCanceled());
                }).addOnFailureListener(e -> {
                    presenter.setEvent(new MainContract.MainEvent.OnQrScanFail(e.getLocalizedMessage()));
                });
    }

    @Override
    protected void onDestroy() {
        disposable.dispose();
        presenter.destroy();
        super.onDestroy();
    }
}
