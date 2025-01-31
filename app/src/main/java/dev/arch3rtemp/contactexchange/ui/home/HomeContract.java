package dev.arch3rtemp.contactexchange.ui.home;

import java.util.List;

import dev.arch3rtemp.contactexchange.ui.model.ContactUi;
import dev.arch3rtemp.ui.base.BasePresenter;
import dev.arch3rtemp.ui.base.BaseView;

public interface HomeContract {

    interface Presenter extends BasePresenter<View> {
        void getMyCards();
        void getContacts();
        void deleteContact(ContactUi contact);
        void saveContact(ContactUi contact);
    }

    interface View extends BaseView {
        void onCardsResult(ViewState state);
        void onContactsResult(ViewState state);
        void onContactDeleted(ContactUi contact, String message);
        void showMessage(String message);
    }

    sealed interface ViewState permits ViewState.Empty, ViewState.Error, ViewState.Success {
        record Empty() implements ViewState {}
        record Error(String message) implements ViewState {}
        record Success(List<ContactUi> data) implements ViewState {}
    }
}
