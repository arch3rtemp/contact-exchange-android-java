package com.example.contactsexchangejava.ui.qr;

import android.content.Context;

import com.example.contactsexchangejava.db.AppDatabase;
import com.example.contactsexchangejava.db.DataManager;
import com.example.contactsexchangejava.db.models.Contact;

public class QrPresenter implements IQrContract.Presenter {

    IQrContract.View view;
    AppDatabase appDatabase;

    public QrPresenter(IQrContract.View view) {
        this.view = view;
    }

    @Override
    public void onViewCreated(Context context) {
        appDatabase = AppDatabase.getDBInstance(context.getApplicationContext());
    }

    @Override
    public void createContact(Contact contact) {
        new Thread(() -> appDatabase.contactDao().insert(contact)).start();
    }

    @Override
    public void onDestroy() {
        this.view = null;
    }
}
