package dev.arch3rtemp.contactexchange.domain.usecase;

import static dev.arch3rtemp.contactexchange.domain.util.ErrorMsgConstants.MSG_ID_MUST_BE_POSITIVE;

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
        if (id <= 0) return Completable.error(new IllegalArgumentException(MSG_ID_MUST_BE_POSITIVE));
        return repository.deleteCard(id);
    }
}
