package dev.arch3rtemp.contactexchange;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RxSchedulerWatcher extends TestWatcher {

    @Override
    protected void starting(Description description) {
        // Override RxJava schedulers with trampoline (synchronous)
        RxJavaPlugins.setInitIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setInitComputationSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setInitSingleSchedulerHandler(scheduler -> Schedulers.trampoline());
    }

    @Override
    protected void finished(Description description) {
        // Reset RxJava schedulers after the test
        RxJavaPlugins.reset();
    }
}
