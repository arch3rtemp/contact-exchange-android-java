package dev.arch3rtemp.contactexchange.di.component.activity;

import dagger.Binds;
import dagger.Module;
import dev.arch3rtemp.contactexchange.ui.MainContract;
import dev.arch3rtemp.contactexchange.ui.MainPresenter;

@Module
public interface PresenterModule {

    @Binds
    MainContract.Presenter bindMainPresenter(MainPresenter impl);
}
