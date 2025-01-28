package dev.arch3rtemp.contactexchange.ui.filter;

import java.util.List;

import dev.arch3rtemp.contactexchange.db.models.Contact;
import dev.arch3rtemp.ui.base.BasePresenter;
import dev.arch3rtemp.ui.base.BaseView;

public interface FilterContract {
    interface Presenter extends BasePresenter<View> {
        void getContacts();
        void deleteContact(int id);
        List<Contact> filterContacts(String query);
    }
    interface View extends BaseView {
        void onGetContacts(List<Contact> contacts);
        void showMessage(String message);
    }
}
