package com.example.contactsexchangejava.ui.card;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;

import com.example.contactsexchangejava.R;
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
