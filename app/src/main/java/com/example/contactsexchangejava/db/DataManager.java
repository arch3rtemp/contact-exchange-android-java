package com.example.contactsexchangejava.db;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.contactsexchangejava.db.models.Contact;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DataManager implements DataCallBack{

    private Context context;
    private AppDatabase appDatabase;
    private static DataManager dataManager;

    public DataManager(Context context) {
        this.context = context;
        appDatabase = AppDatabase.getDBInstance(context);
    }

    @Override
    public Observable<List<Contact>> loadContacts() {
        Observable<List<Contact>> contacts = appDatabase.contactDao().getAllContacts();
        if (contacts != null)
            return contacts;

        return Observable.empty();
    }

    @Override
    public Observable<List<Contact>> loadMyCards() {
        Observable<List<Contact>> cards = appDatabase.contactDao().getAllMyCards();
        if (cards != null)
            return cards;

        return Observable.empty();
    }

    @Override
    public Observable<Contact> loadContactById(int id) {
        Observable<Contact> contact = appDatabase.contactDao().getContactById(id);
        if (contact != null)
            return contact;

        return Observable.empty();
    }

    @Override
    public void createContact(Contact contact) {
        appDatabase.contactDao().insert(contact);
    }

    @Override
    public void editContact(Contact contact) {
        appDatabase.contactDao().update(contact);
    }

    @Override
    public void deleteContact(int id) {
        appDatabase.contactDao().delete(id);
    }
}
