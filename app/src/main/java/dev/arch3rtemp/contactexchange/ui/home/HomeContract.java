package dev.arch3rtemp.contactexchange.ui.home;

import java.util.List;

import dev.arch3rtemp.contactexchange.db.models.Contact;
import dev.arch3rtemp.ui.base.BasePresenter;
import dev.arch3rtemp.ui.base.BaseView;

public interface HomeContract {

    interface Presenter extends BasePresenter<View> {
        void getMyCards();
        void getContacts();
        void deleteContact(int id, int position);
    }

    interface View extends BaseView {
        void onGetMyCards(List<Contact> cards);
        void onGetContacts(List<Contact> contacts);
        void onContactDelete(int position);
        void showMessage(String message);
    }
}
