package com.example.contactsexchangejava.ui.card;

import android.graphics.drawable.Drawable;

import com.example.contactsexchangejava.db.models.Contact;
import com.example.contactsexchangejava.ui.IBasePresenter;
import com.example.contactsexchangejava.ui.IBaseView;

public interface ICreateOrEditCardContract {

    interface Presenter extends IBasePresenter {
        void setBackgroundColorAndRetainShape(final int color, final Drawable background);
        void createContact(Contact contact);
        void editContact(Contact contact);
    }

    interface View extends IBaseView<Presenter> {

    }
}
