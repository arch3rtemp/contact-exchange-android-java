package com.example.contactsexchangejava.ui.home;

import android.content.Context;

import com.example.contactsexchangejava.db.AppDatabase;
import com.example.contactsexchangejava.db.DataManager;
import com.example.contactsexchangejava.db.models.Contact;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
        appDatabase.contactDao().getAllMyCards()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Contact>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull List<Contact> myCards) {
                        view.onGetMyCards(myCards);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getContacts() {
        appDatabase.contactDao().getAllContacts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Contact>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull List<Contact> contacts) {
                        view.onGetContacts(contacts);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        this.view = null;
    }
}
