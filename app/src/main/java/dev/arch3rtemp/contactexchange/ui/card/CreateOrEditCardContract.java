package dev.arch3rtemp.contactexchange.ui.card;

import dev.arch3rtemp.contactexchange.db.models.Contact;
import dev.arch3rtemp.ui.base.BasePresenter;
import dev.arch3rtemp.ui.base.BaseView;

public interface CreateOrEditCardContract {

    interface Presenter extends BasePresenter<View> {
        void createContact(Contact contact);
        void editContact(Contact contact);
        void getContactById(int id);
    }

    interface View extends BaseView {
        void onGetContactById(Contact contact);
        void showToastMessage(String message);
    }
}
