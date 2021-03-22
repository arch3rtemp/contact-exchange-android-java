package com.example.contactsexchangejava.ui.card;

import com.example.contactsexchangejava.db.models.Contact;
import com.example.contactsexchangejava.ui.IBasePresenter;
import com.example.contactsexchangejava.ui.IBaseView;

public interface ICardContract {

    interface Presenter extends IBasePresenter {
        void getContactById(int id);
    }

    interface View extends IBaseView<Presenter> {
        void onCardLoaded(Contact contact);
    }
}
