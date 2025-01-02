package dev.arch3rtemp.contactexchange.domain.repository;

import dev.arch3rtemp.contactexchange.domain.model.Card;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public interface CardRepository {
    Observable<List<Card>> getMyCards();
    Observable<List<Card>> getScannedCards();
    Observable<Card> getCardById(int id);
    Completable addCard(Card card);
    Completable updateCard(Card card);
    Completable deleteCard(int id);
}
