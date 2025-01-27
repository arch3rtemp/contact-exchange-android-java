package dev.arch3rtemp.contactexchange.ui;

import dev.arch3rtemp.ui.base.IBasePresenter;
import dev.arch3rtemp.ui.base.IBaseView;

public interface MainContract {

    interface Presenter extends IBasePresenter {
        void scanContact();
    }

    interface View extends IBaseView<Presenter> {
        void showMessage(String message);
    }
}
