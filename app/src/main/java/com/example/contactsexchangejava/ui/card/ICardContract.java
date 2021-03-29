package com.example.contactsexchangejava.ui.card;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.contactsexchangejava.db.models.Contact;
import com.example.contactsexchangejava.ui.IBasePresenter;
import com.example.contactsexchangejava.ui.IBaseView;
import com.google.android.material.shape.MaterialShapeDrawable;

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
