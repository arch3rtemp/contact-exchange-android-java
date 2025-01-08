package dev.arch3rtemp.contactexchange.domain.util;

import io.reactivex.rxjava3.core.Scheduler;

public interface SchedulerProvider {
    Scheduler io();
    Scheduler computation();
    Scheduler main();
}
