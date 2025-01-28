package dev.arch3rtemp.contactexchange.ui.home;

import java.util.List;

import dev.arch3rtemp.contactexchange.ui.model.ContactUi;
import dev.arch3rtemp.ui.base.BasePresenter;
import dev.arch3rtemp.ui.base.BaseView;

public interface HomeContract {

    interface Presenter extends BasePresenter<View> {
        void getMyCards();
        void getContacts();
        void deleteContact(int id);
    }

    interface View extends BaseView {
        void onGetMyCards(List<ContactUi> cards);
        void onGetContacts(List<ContactUi> contacts);
        void showMessage(String message);
    }
}
