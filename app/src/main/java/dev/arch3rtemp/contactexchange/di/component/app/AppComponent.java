package dev.arch3rtemp.contactexchange.di.component.app;

import dagger.BindsInstance;
import dagger.Component;
import dev.arch3rtemp.contactexchange.App;
import dev.arch3rtemp.contactexchange.di.scope.AppScope;
import dev.arch3rtemp.contactexchange.di.component.activity.ActivityComponent;

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
