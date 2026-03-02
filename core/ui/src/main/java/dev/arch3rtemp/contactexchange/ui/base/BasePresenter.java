package dev.arch3rtemp.contactexchange.ui.base;

public interface BasePresenter<V extends BaseView> {
    void onCreate(V view);
    void onDestroy();
}
