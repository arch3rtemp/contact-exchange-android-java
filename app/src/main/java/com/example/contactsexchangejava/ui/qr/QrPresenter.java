package com.example.contactsexchangejava.ui.qr;

import android.content.Context;

import com.example.contactsexchangejava.db.DataManager;
import com.example.contactsexchangejava.db.models.Contact;

public class QrPresenter implements IQrContract.Presenter {

    IQrContract.View view;
    DataManager dataManager;

    public QrPresenter(IQrContract.View view) {
        this.view = view;
    }

    @Override
    public void onViewCreated(Context context) {
        dataManager = new DataManager(context.getApplicationContext());
    }

    @Override
    public void createContact(Contact contact) {
        new Thread(() -> dataManager.createContact(contact)).start();
    }

    @Override
    public void onDestroy() {
        this.view = null;
        dataManager = null;
    }
}
