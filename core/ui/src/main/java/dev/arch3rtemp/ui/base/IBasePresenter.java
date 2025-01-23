package dev.arch3rtemp.ui.base;

import android.content.Context;

public interface IBasePresenter {
    void onViewCreated(Context context);
    void onDestroy();
}
