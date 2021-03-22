package com.example.contactsexchangejava.ui;

import android.content.Context;

public interface IBasePresenter {
    void onViewCreated(Context context);
    void onDestroy();
}
