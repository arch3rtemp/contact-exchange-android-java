package dev.arch3rtemp.ui.base;

public interface BasePresenter<V extends BaseView> {
    void onCreate(V view);
    void onDestroy();
}
