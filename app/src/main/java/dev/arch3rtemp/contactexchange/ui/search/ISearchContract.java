package dev.arch3rtemp.contactexchange.ui.search;

import java.util.List;

import dev.arch3rtemp.contactexchange.db.models.Contact;
import dev.arch3rtemp.ui.base.IBasePresenter;
import dev.arch3rtemp.ui.base.IBaseView;

public interface ISearchContract {
    interface Presenter extends IBasePresenter {
        void getContacts();
    }
    interface View extends IBaseView<Presenter> {
        void onGetContacts(List<Contact> contacts);
        void showMessage(String message);
    }
}
