package dev.arch3rtemp.contactexchange.ui.card;

import android.graphics.drawable.Drawable;

import dev.arch3rtemp.contactexchange.db.models.Contact;
import dev.arch3rtemp.ui.base.IBasePresenter;
import dev.arch3rtemp.ui.base.IBaseView;

public interface ICardContract {

    interface Presenter extends IBasePresenter {
        void getContactById(int id);
        void deleteContact(int id);
        void setBackgroundColorAndRetainShape(final int color, final Drawable background);
    }

    interface View extends IBaseView<Presenter> {
        void onCardLoaded(Contact contact);
    }
}
