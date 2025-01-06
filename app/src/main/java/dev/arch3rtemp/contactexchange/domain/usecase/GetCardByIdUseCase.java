package dev.arch3rtemp.contactexchange.domain.usecase;

import static dev.arch3rtemp.contactexchange.domain.util.ErrorMsgConstants.MSG_ID_MUST_BE_POSITIVE;

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
        if (id <= 0) return Observable.error(new IllegalArgumentException(MSG_ID_MUST_BE_POSITIVE));
        return repository.getCardById(id);
    }
}
