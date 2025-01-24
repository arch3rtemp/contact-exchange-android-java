package dev.arch3rtemp.contactexchange.ui.home;

import android.content.Context;

import dev.arch3rtemp.contactexchange.db.AppDatabase;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenter implements IHomeContract.Presenter {

    private IHomeContract.View view;
    private AppDatabase appDatabase;
    private CompositeDisposable compositeDisposable;

    public HomePresenter(IHomeContract.View view) {
        this.view = view;
    }

    @Override
    public void onViewCreated(Context context) {
        appDatabase = AppDatabase.getDBInstance(context.getApplicationContext());
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getMyCards() {
        var disposable = appDatabase.contactDao().getMyCards()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cards -> view.onGetMyCards(cards),
                        throwable -> view.showMessage(throwable.getLocalizedMessage()));
        compositeDisposable.add(disposable);
    }

    @Override
    public void getContacts() {
         var disposable = appDatabase.contactDao().getScannedContacts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts -> view.onGetContacts(contacts),
                        throwable -> view.showMessage(throwable.getLocalizedMessage()));
        compositeDisposable.add(disposable);
    }

    @Override
    public void deleteContact(int id) {
        var disposable = appDatabase.contactDao().delete(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {},
                        throwable -> view.showMessage(throwable.getLocalizedMessage()));
        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        this.view = null;
    }
}
