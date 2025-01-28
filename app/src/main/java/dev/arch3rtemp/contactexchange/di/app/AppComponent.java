package dev.arch3rtemp.contactexchange.di.app;

import dagger.BindsInstance;
import dagger.Component;
import dev.arch3rtemp.contactexchange.App;
import dev.arch3rtemp.contactexchange.di.AppScope;
import dev.arch3rtemp.contactexchange.di.activity.ActivityComponent;

@AppScope
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(App app);

    ActivityComponent.Factory activityComponent();

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(App app);
        AppComponent build();
    }
}
