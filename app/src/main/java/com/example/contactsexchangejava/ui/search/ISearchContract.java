package com.example.contactsexchangejava.ui.search;

import com.example.contactsexchangejava.db.models.Contact;
import com.example.contactsexchangejava.ui.IBasePresenter;
import com.example.contactsexchangejava.ui.IBaseView;

import java.util.List;

public interface ISearchContract {
    interface Presenter extends IBasePresenter {
        void getContacts();
    }
    interface View extends IBaseView<Presenter> {
        void onGetContacts(List<Contact> contacts);
    }
}