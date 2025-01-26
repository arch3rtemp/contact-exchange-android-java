package dev.arch3rtemp.contactexchange.ui.filter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.db.AppDatabase;
import dev.arch3rtemp.contactexchange.db.models.Contact;
import dev.arch3rtemp.ui.util.StringResourceManager;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FilterPresenter implements IFilterContract.Presenter {
    private IFilterContract.View view;
    private StringResourceManager stringResourceManager;
    private AppDatabase appDatabase;
    private CompositeDisposable compositeDisposable;
    private List<Contact> unfiltered;

    public FilterPresenter(IFilterContract.View view) {
        this.view = view;
    }

    @Override
    public void getContacts() {
        var disposable = appDatabase.contactDao().selectScannedContacts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts -> {
                            unfiltered = contacts;
                            view.onGetContacts(contacts);
                        },
                        throwable -> view.showMessage(throwable.getLocalizedMessage()));
        compositeDisposable.add(disposable);
    }

    @Override
    public void deleteContact(int id) {
        var disposable = appDatabase.contactDao().delete(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.showMessage(stringResourceManager.string(R.string.msg_contact_deleted)),
                        throwable -> view.showMessage(throwable.getLocalizedMessage()));
        compositeDisposable.add(disposable);
    }

    @Override
    public List<Contact> filterContacts(String query) {
        List<Contact> filteredContacts = new ArrayList<>();
        String filterPattern = query.toLowerCase().trim();
        for (Contact row : unfiltered) {
            if (row.getName().toLowerCase().contains(filterPattern) || row.getName().toLowerCase().contains(filterPattern)) {
                filteredContacts.add(row);
            }
        }
        return filteredContacts;
    }

    @Override
    public void onCreate(Context context) {
        stringResourceManager = new StringResourceManager(context);
        appDatabase = AppDatabase.getDBInstance(context.getApplicationContext());
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        this.view = null;
    }
}
