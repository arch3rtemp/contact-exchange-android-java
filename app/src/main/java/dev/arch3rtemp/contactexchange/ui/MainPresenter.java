package dev.arch3rtemp.contactexchange.ui;

import android.content.Context;

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
import dev.arch3rtemp.ui.util.StringResourceManager;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainPresenter implements IMainContract.Presenter {

    private IMainContract.View view;
    private AppDatabase appDatabase;
    private GmsBarcodeScanner scanner;
    private StringResourceManager stringManager;
    private CompositeDisposable compositeDisposable;

    public MainPresenter(IMainContract.View view) {
        this.view = view;
    }

    @Override
    public void onCreate(Context context) {
        scanner = initScanner(context);
        stringManager = new StringResourceManager(context);
        appDatabase = AppDatabase.getDBInstance(context.getApplicationContext());
        compositeDisposable = new CompositeDisposable();
    }

    private GmsBarcodeScanner initScanner(Context context) {
        var options = new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .enableAutoZoom()
                .build();

        return GmsBarcodeScanning.getClient(context, options);
    }

    @Override
    public void scanContact() {
        scanner.startScan()
                .addOnSuccessListener(new OnSuccessListener<Barcode>() {
                    @Override
                    public void onSuccess(Barcode barcode) {
                        try {
                            var contact = new Contact(new JSONObject(barcode.getRawValue()));
                            saveContact(contact);
                        } catch (JSONException e) {
                            view.showMessage(e.getLocalizedMessage());
                        }
                    }
                });
    }

    private void saveContact(Contact contact) {
        var disposable = appDatabase.contactDao().insert(contact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.showMessage(stringManager.string(R.string.msg_contact_added)),
                        throwable -> view.showMessage(throwable.getLocalizedMessage()));
        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        this.view = null;
    }
}
