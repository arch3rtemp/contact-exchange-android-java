package dev.arch3rtemp.contactexchange.di.fragment;

import dagger.Module;
import dagger.Provides;
import dev.arch3rtemp.contactexchange.db.ContactDao;
import dev.arch3rtemp.contactexchange.di.FragmentScope;
import dev.arch3rtemp.contactexchange.ui.card.detail.CardDetailsContract;
import dev.arch3rtemp.contactexchange.ui.card.detail.CardDetailsPresenter;
import dev.arch3rtemp.contactexchange.ui.card.createoredit.CreateOrEditCardContract;
import dev.arch3rtemp.contactexchange.ui.card.createoredit.CreateOrEditCardPresenter;
import dev.arch3rtemp.contactexchange.ui.filter.FilterContract;
import dev.arch3rtemp.contactexchange.ui.filter.FilterPresenter;
import dev.arch3rtemp.contactexchange.ui.home.HomeContract;
import dev.arch3rtemp.contactexchange.ui.home.HomePresenter;

@Module
public class FragmentPresenterModule {

    @FragmentScope
    @Provides
    CardDetailsContract.Presenter provideCardDetailsPresenter(ContactDao contactDao) {
        return new CardDetailsPresenter(contactDao);
    }

    @FragmentScope
    @Provides
    CreateOrEditCardContract.Presenter provideCreateOrEditCardPresenter(ContactDao contactDao) {
        return new CreateOrEditCardPresenter(contactDao);
    }

    @FragmentScope
    @Provides
    HomeContract.Presenter provideHomePresenter(ContactDao contactDao) {
        return new HomePresenter(contactDao);
    }

    @FragmentScope
    @Provides
    FilterContract.Presenter providerFilterPresenter(ContactDao contactDao) {
        return new FilterPresenter(contactDao);
    }
}
