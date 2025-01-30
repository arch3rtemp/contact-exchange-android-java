package dev.arch3rtemp.contactexchange.ui.createoredit;

import dev.arch3rtemp.contactexchange.db.model.Contact;
import dev.arch3rtemp.contactexchange.ui.model.ContactUi;
import dev.arch3rtemp.ui.base.BasePresenter;
import dev.arch3rtemp.ui.base.BaseView;

public interface CreateOrEditCardContract {

    interface Presenter extends BasePresenter<View> {
        void createContact(Contact contact);
        void editContact(Contact contact);
        void getContactById(int id);
        Contact getCurrentCard();
    }

    interface View extends BaseView {
        void onGetContactById(ContactUi contact);
        void showMessage(String message);
        void navigateUp();
    }
}
