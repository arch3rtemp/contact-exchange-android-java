package com.example.contactsexchangejava.ui.card;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.contactsexchangejava.db.models.Contact;
import com.example.contactsexchangejava.ui.IBasePresenter;
import com.example.contactsexchangejava.ui.IBaseView;

public interface ICreateOrEditCardContract {

    interface Presenter extends IBasePresenter {
        void setBackgroundColorWithAnimationAndRetainShape(final int currentColor, final int finalColor, final Drawable background);
        void createContact(Contact contact);
        void editContact(Contact contact);
        void getContactById(int id);
    }

    interface View extends IBaseView<Presenter> {
        void onGetContactById(Contact contact);
    }
}
