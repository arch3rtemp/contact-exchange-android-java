package dev.arch3rtemp.contactexchange.ui.home;

import dev.arch3rtemp.ui.base.IBasePresenter;
import dev.arch3rtemp.ui.base.IBaseView;

public interface IMainContract {

    interface Presenter extends IBasePresenter {
        void scanContact();
    }

    interface View extends IBaseView<Presenter> {
        void showMessage(String message);
    }
}
