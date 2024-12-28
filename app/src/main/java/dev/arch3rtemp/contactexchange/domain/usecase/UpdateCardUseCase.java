package dev.arch3rtemp.contactexchange.domain.usecase;

import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.domain.repository.Repository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public final class UpdateCardUseCase {

    private final Repository repository;

    @Inject
    public UpdateCardUseCase(Repository repository) {
        this.repository = repository;
    }

    public Completable invoke(Card card) {
        return repository.updateCard(card);
    }
}
