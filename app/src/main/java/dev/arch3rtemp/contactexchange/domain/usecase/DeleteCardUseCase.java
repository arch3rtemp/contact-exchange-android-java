package dev.arch3rtemp.contactexchange.domain.usecase;

import dev.arch3rtemp.contactexchange.domain.repository.CardRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public final class DeleteCardUseCase {

    private final CardRepository repository;

    @Inject
    public DeleteCardUseCase(CardRepository repository) {
        this.repository = repository;
    }

    public Completable invoke(int id) {
        return repository.deleteCard(id);
    }
}
