package dev.arch3rtemp.contactexchange.ui.home;

import java.util.List;

import dev.arch3rtemp.contactexchange.db.models.Contact;
import dev.arch3rtemp.ui.base.IBasePresenter;
import dev.arch3rtemp.ui.base.IBaseView;

public interface IHomeContract {

    interface Presenter extends IBasePresenter {
        void getMyCards();
        void getContacts();
        void deleteContact(int id);
    }

    interface View extends IBaseView<Presenter> {
        void onGetMyCards(List<Contact> cards);
        void onGetContacts(List<Contact> contacts);
        void showMessage(String message);
    }
}
