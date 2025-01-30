package dev.arch3rtemp.contactexchange.di.activity;

import androidx.fragment.app.FragmentActivity;

import dagger.BindsInstance;
import dagger.Subcomponent;
import dev.arch3rtemp.contactexchange.di.ActivityScope;
import dev.arch3rtemp.contactexchange.di.fragment.FragmentComponent;
import dev.arch3rtemp.contactexchange.ui.MainActivity;

@ActivityScope
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    FragmentComponent.Factory fragmentComponent();

    @Subcomponent.Factory
    interface Factory {

        ActivityComponent create(@BindsInstance FragmentActivity activity);
    }
}
