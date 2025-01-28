package dev.arch3rtemp.contactexchange.ui;

import dev.arch3rtemp.ui.base.BasePresenter;
import dev.arch3rtemp.ui.base.BaseView;

public interface MainContract {

    interface Presenter extends BasePresenter<View> {
        void scanContact();
    }

    interface View extends BaseView {
        void showMessage(String message);
    }
}
