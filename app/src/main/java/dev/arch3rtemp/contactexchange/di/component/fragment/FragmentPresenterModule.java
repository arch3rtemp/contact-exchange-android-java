package dev.arch3rtemp.contactexchange.di.component.fragment;

import dagger.Binds;
import dagger.Module;
import dev.arch3rtemp.contactexchange.ui.createoredit.CreateOrEditCardContract;
import dev.arch3rtemp.contactexchange.ui.createoredit.CreateOrEditCardPresenter;
import dev.arch3rtemp.contactexchange.ui.detail.CardDetailsContract;
import dev.arch3rtemp.contactexchange.ui.detail.CardDetailsPresenter;
import dev.arch3rtemp.contactexchange.ui.filter.FilterContract;
import dev.arch3rtemp.contactexchange.ui.filter.FilterPresenter;
import dev.arch3rtemp.contactexchange.ui.home.HomeContract;
import dev.arch3rtemp.contactexchange.ui.home.HomePresenter;

@Module
public interface FragmentPresenterModule {

    @Binds
    CardDetailsContract.Presenter bindCardDetailsPresenter(CardDetailsPresenter impl);

    @Binds
    CreateOrEditCardContract.Presenter bindCreateOrEditCardPresenter(CreateOrEditCardPresenter impl);

    @Binds
    HomeContract.Presenter bindHomePresenter(HomePresenter impl);

    @Binds
    FilterContract.Presenter bindFilterPresenter(FilterPresenter impl);
}
