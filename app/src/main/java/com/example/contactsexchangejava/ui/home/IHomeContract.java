package com.example.contactsexchangejava.ui.home;

import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.contactsexchangejava.db.models.Contact;
import com.example.contactsexchangejava.ui.IBasePresenter;
import com.example.contactsexchangejava.ui.IBaseView;

import java.util.List;

public interface IHomeContract {

    interface Presenter extends IBasePresenter {
        void getMyCards();
        void getContacts();
        void deleteContact(int id);
    }

    interface View extends IBaseView<Presenter> {
        void onGetMyCards(List<Contact> cards);
        void onGetContacts(List<Contact> contacts);
    }
}
