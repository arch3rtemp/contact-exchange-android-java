package dev.arch3rtemp.contactexchange.data.repository;

import dev.arch3rtemp.contactexchange.data.db.CardDao;
import dev.arch3rtemp.contactexchange.data.mapper.CardEntityMapper;
import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.domain.repository.Repository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public class RepositoryImpl implements Repository {

    private final CardDao cardDao;
    private final CardEntityMapper mapper;

    @Inject
    public RepositoryImpl(CardDao cardDao, CardEntityMapper mapper) {
        this.cardDao = cardDao;
        this.mapper = mapper;
    }

    @Override
    public Observable<List<Card>> getMyCards() {
        return cardDao.selectMyCards().map(mapper::fromEntityList);
    }

    @Override
    public Observable<List<Card>> getScannedCards() {
        return cardDao.selectScannedCards().map(mapper::fromEntityList);
    }

    @Override
    public Observable<Card> getCardById(int id) {
        return cardDao.selectCardById(id).map(mapper::fromEntity);
    }

    @Override
    public Completable addCard(Card card) {
        return cardDao.insert(mapper.toEntity(card));
    }

    @Override
    public Completable updateCard(Card card) {
        return cardDao.update(mapper.toEntity(card));
    }

    @Override
    public Completable deleteCard(int id) {
        return cardDao.delete(id);
    }
}
