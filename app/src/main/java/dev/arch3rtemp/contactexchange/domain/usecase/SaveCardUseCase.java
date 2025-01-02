package dev.arch3rtemp.contactexchange.domain.usecase;

import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.domain.repository.CardRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public final class SaveCardUseCase {

    private final CardRepository repository;

    @Inject
    public SaveCardUseCase(CardRepository repository) {
        this.repository = repository;
    }

    public Completable invoke(Card card) {
        return repository.addCard(card);
    }
}
