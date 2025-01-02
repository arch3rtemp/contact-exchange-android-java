package dev.arch3rtemp.contactexchange.domain.usecase;

import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.domain.repository.CardRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public final class GetCardByIdUseCase {

    private final CardRepository repository;

    @Inject
    public GetCardByIdUseCase(CardRepository repository) {
        this.repository = repository;
    }

    public Observable<Card> invoke(int id) {
        return repository.getCardById(id);
    }
}
