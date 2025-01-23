package dev.arch3rtemp.contactexchange.db;

import android.content.Context;

import dev.arch3rtemp.contactexchange.db.models.Contact;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class DataManager implements DataCallBack{

    private Context context;
    private AppDatabase appDatabase;

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
