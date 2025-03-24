package dev.arch3rtemp.contactexchange.di.component.fragment;

import dagger.Subcomponent;
import dev.arch3rtemp.contactexchange.di.scope.FragmentScope;
import dev.arch3rtemp.contactexchange.ui.createoredit.CreateOrEditCardFragment;
import dev.arch3rtemp.contactexchange.ui.detail.CardDetailsFragment;
import dev.arch3rtemp.contactexchange.ui.filter.FilterFragment;
import dev.arch3rtemp.contactexchange.ui.home.HomeFragment;

@FragmentScope
@Subcomponent(modules = {FragmentPresenterModule.class})
public interface FragmentComponent {

    void inject(CardDetailsFragment cardDetailsFragment);
    void inject(CreateOrEditCardFragment createOrEditCardFragment);
    void inject(HomeFragment homeFragment);
    void inject(FilterFragment filterFragment);

    @Subcomponent.Factory
    interface Factory {
        FragmentComponent create();
    }
}
