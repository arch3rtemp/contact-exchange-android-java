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

    public Completable invoke(Card current, Card newCard) {
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
