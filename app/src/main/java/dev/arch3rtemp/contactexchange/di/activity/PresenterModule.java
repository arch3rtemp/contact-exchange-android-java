package dev.arch3rtemp.contactexchange.di.activity;

import dagger.Module;
import dagger.Provides;
import dev.arch3rtemp.contactexchange.db.ContactDao;
import dev.arch3rtemp.contactexchange.di.ActivityScope;
import dev.arch3rtemp.contactexchange.ui.MainContract;
import dev.arch3rtemp.contactexchange.ui.MainPresenter;

@Module
public class PresenterModule {

    @ActivityScope
    @Provides
    MainContract.Presenter provideMainPresenter(ContactDao contactDao) {
        return new MainPresenter(contactDao);
    }
}
