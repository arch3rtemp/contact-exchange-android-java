package dev.arch3rtemp.ui.util;

import android.content.Context;

import androidx.annotation.StringRes;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class StringResourceManager {

    private final Context context;

    @Inject
    public StringResourceManager(@ApplicationContext Context context) {
        this.context = context;
    }

    public String string(@StringRes int resId) {
        return context.getString(resId);
    }
}
