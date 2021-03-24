package com.example.contactsexchangejava.ui.card;

import android.graphics.drawable.Drawable;

import com.example.contactsexchangejava.db.models.Contact;
import com.example.contactsexchangejava.ui.IBasePresenter;
import com.example.contactsexchangejava.ui.IBaseView;

public interface ICardContract {

    interface Presenter extends IBasePresenter {
        void getContactById(int id);
        void deleteContact(int id);
        void createContact(Contact contact);
        void setBackgroundColorAndRetainShape(final int color, final Drawable background);
    }

    interface View extends IBaseView<Presenter> {
        void onCardLoaded(Contact contact);
    }
}
