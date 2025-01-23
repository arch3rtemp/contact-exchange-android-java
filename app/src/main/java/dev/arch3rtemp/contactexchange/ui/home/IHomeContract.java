package dev.arch3rtemp.contactexchange.ui.home;

import dev.arch3rtemp.contactexchange.db.models.Contact;
import dev.arch3rtemp.ui.base.IBasePresenter;
import dev.arch3rtemp.ui.base.IBaseView;

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
