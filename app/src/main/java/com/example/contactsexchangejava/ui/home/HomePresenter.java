package com.example.contactsexchangejava.ui.home;

import android.content.Context;

import com.example.contactsexchangejava.db.DataManager;
import com.example.contactsexchangejava.db.models.Contact;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter implements IHomeContract.PresenterI {

    private IHomeContract.View view;
    private DataManager dataManager;
    private CompositeDisposable compositeDisposable;

    public HomePresenter(IHomeContract.View view) {
        this.view = view;
    }

    @Override
    public void onViewCreated(Context context) {
        dataManager = new DataManager(context.getApplicationContext());
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getMyCards() {
        dataManager.loadMyCards()
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
//        cards.add(new Contact("Archil", "Asanishvili","Georgian-American University", "Android Developer", "a.asanishvili@gau.ge", "+995 571 85 98 85", "+995 032 254 84 78", R.color.light_navy, true));
//        cards.add(new Contact("Archil", "Asanishvili","Terabank", "Android Developer", "a.asanishvili@gau.ge", "+995 571 85 98 85", "+995 032 254 84 78", R.color.purple, true));
//        cards.add(new Contact("Archil", "Asanishvili","Terabank", "Android Developer", "a.asanishvili@gau.ge", "+995 571 85 98 85", "+995 032 254 84 78", R.color.purple, true));
    }

    @Override
    public void getContacts() {
        dataManager.loadContacts()
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
//        contacts.add(new Contact("John", "Parker", "GAU", "Project Manager", "parker@gau.ge", "+995 577 783189", "+995 598 48 84 34", R.color.clay_warm, false));
//        contacts.add(new Contact("Alice", "Worth", "GAU", "Office Manager", "worth@gau.ge", "+995 577 783189", "+995 598 48 84 34", R.color.clay, false));
//        contacts.add(new Contact("Brad", "Wilson", "GAU", "Graphic Designer", "wilson@gau.ge", "+995 577 783189", "+995 598 48 84 34", R.color.dusky_rose, false));
//        contacts.add(new Contact("Jack", "Jackson", "GAU", "Art Director", "jackson@gau.ge", "+995 577 783189", "+995 598 48 84 34", R.color.soft_green,false));
    }

    @Override
    public void onDestroy() {
        this.view = null;
        compositeDisposable.clear();
    }
}
