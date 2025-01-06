package dev.arch3rtemp.contactexchange.domain.usecase;

import static dev.arch3rtemp.contactexchange.domain.util.ErrorMsgConstants.MSG_CARD_CANNOT_BE_NULL;

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
        if (card == null) {
            return Completable.error(new IllegalArgumentException(MSG_CARD_CANNOT_BE_NULL));
        }
        return repository.addCard(card);
    }
}
