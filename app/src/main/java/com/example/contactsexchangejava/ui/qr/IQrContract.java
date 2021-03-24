package com.example.contactsexchangejava.ui.qr;

import com.example.contactsexchangejava.db.models.Contact;
import com.example.contactsexchangejava.ui.IBasePresenter;
import com.example.contactsexchangejava.ui.IBaseView;

public interface IQrContract {
    interface Presenter extends IBasePresenter {
        void createContact(Contact contact);
    }
    interface View extends IBaseView<Presenter> {

    }
}
