package dev.arch3rtemp.contactexchange.di.activity;

import dagger.Binds;
import dagger.Module;
import dev.arch3rtemp.contactexchange.di.ActivityScope;
import dev.arch3rtemp.contactexchange.ui.MainContract;
import dev.arch3rtemp.contactexchange.ui.MainPresenter;

@Module
public interface PresenterModule {

    @ActivityScope
    @Binds
    MainContract.Presenter provideMainPresenter(MainPresenter impl);
}
