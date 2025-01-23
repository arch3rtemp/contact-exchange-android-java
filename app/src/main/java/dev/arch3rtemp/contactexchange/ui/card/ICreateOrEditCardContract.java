package dev.arch3rtemp.contactexchange.ui.card;

import dev.arch3rtemp.contactexchange.db.models.Contact;
import dev.arch3rtemp.ui.base.IBasePresenter;
import dev.arch3rtemp.ui.base.IBaseView;

public interface ICreateOrEditCardContract {

    interface Presenter extends IBasePresenter {
        void createContact(Contact contact);
        void editContact(Contact contact);
        void getContactById(int id);
    }

    interface View extends IBaseView<Presenter> {
        void onGetContactById(Contact contact);
        void showToastMessage(String message);
    }
}
