package dev.arch3rtemp.contactexchange.ui.search;

import android.content.Context;

import dev.arch3rtemp.contactexchange.db.AppDatabase;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchPresenter implements ISearchContract.Presenter {
    private ISearchContract.View view;
    private AppDatabase appDatabase;
    private CompositeDisposable compositeDisposable;

    public SearchPresenter(ISearchContract.View view) {
        this.view = view;
    }

    @Override
    public void getContacts() {
        var disposable = appDatabase.contactDao().selectScannedContacts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts -> view.onGetContacts(contacts),
                        throwable -> view.showMessage(throwable.getLocalizedMessage()));
        compositeDisposable.add(disposable);
    }

    @Override
    public void onCreate(Context context) {
        appDatabase = AppDatabase.getDBInstance(context.getApplicationContext());
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        this.view = null;
    }
}
