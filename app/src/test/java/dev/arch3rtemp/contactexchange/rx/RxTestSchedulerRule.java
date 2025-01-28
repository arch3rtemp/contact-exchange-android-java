package dev.arch3rtemp.contactexchange.rx;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.TestScheduler;

public class RxTestSchedulerRule extends TestWatcher {

    private final TestScheduler testScheduler = new TestScheduler();

    @Override
    protected void starting(Description description) {
        RxJavaPlugins.setInitComputationSchedulerHandler(schedulerSupplier -> testScheduler);
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> testScheduler);
    }

    @Override
    protected void finished(Description description) {
        RxJavaPlugins.reset();
    }

    public TestScheduler getTestScheduler() {
        return testScheduler;
    }
}
