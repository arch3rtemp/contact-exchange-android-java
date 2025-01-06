package dev.arch3rtemp.contactexchange.domain.usecase;

import static dev.arch3rtemp.contactexchange.domain.util.ErrorMsgConstants.MSG_CARD_CANNOT_BE_NULL;

import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.domain.repository.CardRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public final class UpdateCardUseCase {

    private final CardRepository repository;

    @Inject
    public UpdateCardUseCase(CardRepository repository) {
        this.repository = repository;
    }

    public Completable invoke(Card current, Card newCard) {
        if (newCard == null) return Completable.error(new IllegalArgumentException(MSG_CARD_CANNOT_BE_NULL));
        var mergedCard = mergeCard(current, newCard);
        return repository.updateCard(mergedCard);
    }

    private Card mergeCard(Card current, Card newCard) {
        return new Card(
                current.id(),
                newCard.name(),
                newCard.job(),
                newCard.position(),
                newCard.email(),
                newCard.phoneMobile(),
                newCard.phoneOffice(),
                current.createdAt(),
                current.color(),
                current.isMy()
        );
    }
}
