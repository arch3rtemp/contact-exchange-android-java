package dev.arch3rtemp.contactexchange.di.fragment;

import dagger.Binds;
import dagger.Module;
import dev.arch3rtemp.contactexchange.di.FragmentScope;
import dev.arch3rtemp.contactexchange.ui.card.createoredit.CreateOrEditCardContract;
import dev.arch3rtemp.contactexchange.ui.card.createoredit.CreateOrEditCardPresenter;
import dev.arch3rtemp.contactexchange.ui.card.detail.CardDetailsContract;
import dev.arch3rtemp.contactexchange.ui.card.detail.CardDetailsPresenter;
import dev.arch3rtemp.contactexchange.ui.filter.FilterContract;
import dev.arch3rtemp.contactexchange.ui.filter.FilterPresenter;
import dev.arch3rtemp.contactexchange.ui.home.HomeContract;
import dev.arch3rtemp.contactexchange.ui.home.HomePresenter;

@Module
public interface FragmentPresenterModule {

    @FragmentScope
    @Binds
    CardDetailsContract.Presenter provideCardDetailsPresenter(CardDetailsPresenter impl);

    @FragmentScope
    @Binds
    CreateOrEditCardContract.Presenter provideCreateOrEditCardPresenter(CreateOrEditCardPresenter impl);

    @FragmentScope
    @Binds
    HomeContract.Presenter provideHomePresenter(HomePresenter impl);

    @FragmentScope
    @Binds
    FilterContract.Presenter providerFilterPresenter(FilterPresenter impl);
}
