package dev.arch3rtemp.contactexchange.presentation.ui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.os.OperationCanceledException;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import org.json.JSONException;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.databinding.ActivityMainBinding;
import dev.arch3rtemp.contactexchange.domain.usecase.ScanQrUseCase;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Inject
    MainPresenter presenter;
    @Inject
    ScanQrUseCase scanner;
    private NavController navController;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(binding.root, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getWindow().setNavigationBarContrastEnforced(false);
        }
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

        binding.llQrScanner.setOnClickListener(v -> startScanner());

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
        var disposable = presenter
                .effectStream()
                .subscribe(this::renderEffect);
        disposables.add(disposable);
    }

    private void renderEffect(MainContract.MainEffect effect) {
        if (effect instanceof MainContract.MainEffect.ShowMessage showMessage) {
            Toast.makeText(this, showMessage.message(), Toast.LENGTH_SHORT).show();
        }
    }

    private void startScanner() {
        var disposable = scanner.invoke().subscribe((card) -> {
            presenter.setEvent(new MainContract.MainEvent.OnQrScanComplete(card));
        }, throwable -> {
            if (throwable instanceof OperationCanceledException error) {
                presenter.setEvent(new MainContract.MainEvent.OnQrScanCanceled(error.getLocalizedMessage()));
            } else if (throwable instanceof JSONException error) {
                presenter.setEvent(new MainContract.MainEvent.OnJsonParseFail(error.getLocalizedMessage()));
            } else {
                presenter.setEvent(new MainContract.MainEvent.OnQrScanFail(throwable.getLocalizedMessage()));
            }
        });
        disposables.add(disposable);
    }

    @Override
    protected void onDestroy() {
        disposables.clear();
        presenter.destroy();
        super.onDestroy();
    }
}
