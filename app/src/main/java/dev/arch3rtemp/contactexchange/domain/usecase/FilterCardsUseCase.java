package dev.arch3rtemp.contactexchange.domain.usecase;

import dev.arch3rtemp.contactexchange.domain.model.Card;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.domain.util.SchedulerProvider;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public final class FilterCardsUseCase {

    private final SchedulerProvider schedulerProvider;
    private final BehaviorSubject<Map.Entry<String, List<Card>>> querySubject;

    @Inject
    public FilterCardsUseCase(SchedulerProvider schedulerProvider) {
        this.schedulerProvider = schedulerProvider;
        querySubject = BehaviorSubject.create();
    }

    /**
     * Initializes the use case, returning an observable that emits filtered card lists
     * whenever the query or unfiltered list changes.
     */
    public Observable<List<Card>> getFilteredCardsStream() {
        return querySubject
                .debounce(DEBOUNCE_PERIOD_MS, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMap(entry ->
                        Observable.fromCallable(() -> filterCards(entry.getKey(), entry.getValue()))
                        .subscribeOn(schedulerProvider.computation()));
    }

    /**
     * Triggers the filtering operation with the given query and unfiltered list of cards.
     */
    public void invoke(@NotNull String query, List<Card> unfiltered) {
        querySubject.onNext(Map.entry(query, unfiltered));
    }

    /**
     * Filters the list of cards based on the query string.
     *
     * @param query       The search query.
     * @param unfiltered The list of unfiltered cards.
     * @return A filtered list of cards matching the query.
     */
    private List<Card> filterCards(@NotNull String query, List<Card> unfiltered) {
        if (query.isBlank()) return unfiltered;

        var filterPattern = query.toLowerCase().trim();
        return unfiltered.stream()
                .filter(card -> card.name().toLowerCase().contains(filterPattern))
                .collect(Collectors.toList());
    }

    private static final long DEBOUNCE_PERIOD_MS = 300;
}
