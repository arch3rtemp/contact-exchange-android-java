package dev.arch3rtemp.contactexchange.util;

import io.reactivex.rxjava3.core.Scheduler;

public interface SchedulerProvider {
    Scheduler io();
    Scheduler computation();
    Scheduler main();
}
