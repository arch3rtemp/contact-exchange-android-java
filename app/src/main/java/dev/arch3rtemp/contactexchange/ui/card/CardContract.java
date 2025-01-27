package dev.arch3rtemp.contactexchange.ui.card;

import dev.arch3rtemp.contactexchange.db.models.Contact;
import dev.arch3rtemp.ui.base.IBasePresenter;
import dev.arch3rtemp.ui.base.IBaseView;

public interface CardContract {

    interface Presenter extends IBasePresenter {
        void getContactById(int id);
        void deleteContact(int id);
    }

    interface View extends IBaseView<Presenter> {
        void onCardLoaded(Contact contact);
        void showMessage(String message);
    }
}
