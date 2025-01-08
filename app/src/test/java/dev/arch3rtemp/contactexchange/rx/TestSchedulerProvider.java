package dev.arch3rtemp.contactexchange.rx;

import dev.arch3rtemp.contactexchange.domain.util.SchedulerProvider;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TestSchedulerProvider implements SchedulerProvider {

    @Override
    public Scheduler io() {
        return Schedulers.trampoline();
    }

    @Override
    public Scheduler computation() {
        return Schedulers.trampoline();
    }

    @Override
    public Scheduler main() {
        return Schedulers.trampoline();
    }
}
