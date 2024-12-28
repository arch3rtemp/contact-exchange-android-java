package dev.arch3rtemp.contactexchange.domain.usecase;

import dev.arch3rtemp.contactexchange.domain.model.Card;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class FilterCardsUseCase {

    private final BehaviorSubject<Map.Entry<String, List<Card>>> querySubject;

    @Inject
    public FilterCardsUseCase() {
        querySubject = BehaviorSubject.create();
    }

    public Observable<List<Card>> init() {
        return querySubject
                .debounce(DEFAULT_DEBOUNCE_PERIOD, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMap(entry -> Observable.fromCallable(() -> filterCards(entry.getKey(), entry.getValue())).subscribeOn(Schedulers.computation()));
    }

    public void invoke(String query, List<Card> unfiltered) {
        querySubject.onNext(Map.entry(query, unfiltered));
    }

    private List<Card> filterCards(@NotNull String query, List<Card> unfiltered) {
        if (query.isBlank()) return unfiltered;

        var filterPattern = query.toLowerCase().trim();
        List<Card> filtered = new ArrayList<>();
        for (Card card : unfiltered) {
            if (card.name().toLowerCase().contains(filterPattern)) {
                filtered.add(card);
            }
        }
        return filtered;
    }

    private static final long DEFAULT_DEBOUNCE_PERIOD = 300;
}
