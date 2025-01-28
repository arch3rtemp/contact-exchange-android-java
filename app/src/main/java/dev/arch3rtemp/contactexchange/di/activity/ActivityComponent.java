package dev.arch3rtemp.contactexchange.di.activity;

import androidx.fragment.app.FragmentActivity;

import dagger.BindsInstance;
import dagger.Subcomponent;
import dev.arch3rtemp.contactexchange.di.ActivityScope;
import dev.arch3rtemp.contactexchange.di.fragment.FragmentComponent;
import dev.arch3rtemp.contactexchange.ui.MainActivity;
import dev.arch3rtemp.contactexchange.ui.card.CardActivity;

@ActivityScope
@Subcomponent(modules = {RouterModule.class, PresenterModule.class, FragmentManagerModule.class})
public interface ActivityComponent {

    void inject(MainActivity mainActivity);
    void inject(CardActivity cardActivity);

    FragmentComponent.Factory fragmentComponent();

    @Subcomponent.Factory
    interface Factory {

        ActivityComponent create(@BindsInstance FragmentActivity activity);
    }
}
