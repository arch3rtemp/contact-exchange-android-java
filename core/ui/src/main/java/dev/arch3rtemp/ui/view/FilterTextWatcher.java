package dev.arch3rtemp.ui.view;

import android.text.Editable;
import android.text.TextWatcher;

public interface FilterTextWatcher extends TextWatcher {
    @Override
    default void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    default void afterTextChanged(Editable s) {}
}
