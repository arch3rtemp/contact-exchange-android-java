package dev.arch3rtemp.contactexchange.ui.filter;

import java.util.List;

import dev.arch3rtemp.contactexchange.db.models.Contact;
import dev.arch3rtemp.ui.base.IBasePresenter;
import dev.arch3rtemp.ui.base.IBaseView;

public interface FilterContract {
    interface Presenter extends IBasePresenter {
        void getContacts();
        void deleteContact(int id);
        List<Contact> filterContacts(String query);
    }
    interface View extends IBaseView<Presenter> {
        void onGetContacts(List<Contact> contacts);
        void showMessage(String message);
    }
}
