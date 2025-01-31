package dev.arch3rtemp.contactexchange.ui.detail;

import dev.arch3rtemp.contactexchange.ui.model.ContactUi;
import dev.arch3rtemp.ui.base.BasePresenter;
import dev.arch3rtemp.ui.base.BaseView;

public interface CardDetailsContract {

    interface Presenter extends BasePresenter<View> {
        void getContactById(int id);
        void deleteContact(int id);
    }

    interface View extends BaseView {
        void onCardLoaded(ContactUi contact);
        void showMessage(String message);
        void animateDeletion();
    }
}
