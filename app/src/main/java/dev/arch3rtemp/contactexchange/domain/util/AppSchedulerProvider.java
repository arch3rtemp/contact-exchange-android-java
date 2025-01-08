package dev.arch3rtemp.contactexchange.domain.util;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AppSchedulerProvider implements SchedulerProvider {
    @Inject
    public AppSchedulerProvider() {}

    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @Override
    public Scheduler main() {
        return AndroidSchedulers.mainThread();
    }
}
