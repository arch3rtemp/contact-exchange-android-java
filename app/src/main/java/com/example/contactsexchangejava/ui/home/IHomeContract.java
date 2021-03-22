package com.example.contactsexchangejava.ui.home;

import android.content.Context;

import com.example.contactsexchangejava.db.models.Contact;
import com.example.contactsexchangejava.ui.IBasePresenter;
import com.example.contactsexchangejava.ui.IBaseView;

import java.util.List;

public interface IHomeContract {

    interface PresenterI extends IBasePresenter {
        void getMyCards();
        void getContacts();
    }

    interface View extends IBaseView<PresenterI> {
        void onGetMyCards(List<Contact> cards);
        void onGetContacts(List<Contact> contacts);
    }
}
