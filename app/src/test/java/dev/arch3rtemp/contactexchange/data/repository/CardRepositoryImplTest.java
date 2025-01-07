package dev.arch3rtemp.contactexchange.data.repository;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import dev.arch3rtemp.contactexchange.RxTrampolineRule;
import dev.arch3rtemp.contactexchange.TestData;
import dev.arch3rtemp.contactexchange.data.db.CardDao;
import dev.arch3rtemp.contactexchange.data.mapper.CardEntityMapper;
import dev.arch3rtemp.contactexchange.domain.repository.CardRepository;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public class CardRepositoryImplTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public RxTrampolineRule trampolineRule = new RxTrampolineRule();

    @Mock
    public CardDao cardDao;

    private CardRepository repository;

    @Before
    public void setup() {
        CardEntityMapper mapper = new CardEntityMapper();
        repository = new CardRepositoryImpl(cardDao, mapper);
    }

    @Test
    public void invokeGetMyCards_mapsAndReturnsValidData() {
        when(cardDao.selectMyCards()).thenReturn(Observable.just(TestData.testCardsEntity));

        repository.getMyCards()
                .test()
                .assertComplete()
                .assertValue(TestData.testCards);

        verify(cardDao).selectMyCards();
    }

    @Test
    public void invokeGetMyCards_failsDb() {
        when(cardDao.selectMyCards()).thenReturn(Observable.error(TestData.sqlException));

        repository.getMyCards()
                .test()
                .assertError(TestData.sqlException);

        verify(cardDao).selectMyCards();
    }

    @Test
    public void invokeGetScannedCards_mapsAndReturnsValidData() {
        when(cardDao.selectScannedCards()).thenReturn(Observable.just(TestData.testCardsEntity));

        repository.getScannedCards()
                .test()
                .assertComplete()
                .assertValue(TestData.testCards);

        verify(cardDao).selectScannedCards();
    }

    @Test
    public void invokeGetScannedCards_failsDb() {
        when(cardDao.selectScannedCards()).thenReturn(Observable.error(TestData.sqlException));

        repository.getScannedCards()
                .test()
                .assertError(TestData.sqlException);

        verify(cardDao).selectScannedCards();
    }

    @Test
    public void invokeGetCardById_mapsAndReturnsValidData() {
        when(cardDao.selectCardById(TestData.testMyCardEntity.getId()))
                .thenReturn(Observable.just(TestData.testMyCardEntity));

        repository.getCardById(TestData.testMyCardEntity.getId())
                .test()
                .assertComplete()
                .assertValue(TestData.testMyCard);

        verify(cardDao).selectCardById(TestData.testMyCardEntity.getId());
    }

    @Test
    public void invokeGetCardById_failsDb() {
        when(cardDao.selectCardById(TestData.testMyCardEntity.getId()))
                .thenReturn(Observable.error(TestData.sqlException));

        repository.getCardById(TestData.testMyCardEntity.getId())
                .test()
                .assertError(TestData.sqlException);

        verify(cardDao).selectCardById(TestData.testMyCardEntity.getId());
    }

    @Test
    public void invokeAddCard_withValidCard_completesSuccessfully() {
        when(cardDao.insert(TestData.testScannedCardEntity))
                .thenReturn(Completable.complete());

        repository.addCard(TestData.testScannedCard)
                .test()
                .assertComplete();

        verify(cardDao).insert(TestData.testScannedCardEntity);
    }

    @Test
    public void invokeAddCard_withValidCard_throwsErrorDueToDbFailure() {
        when(cardDao.insert(TestData.testScannedCardEntity))
                .thenReturn(Completable.error(TestData.sqlException));

        repository.addCard(TestData.testScannedCard)
                .test()
                .assertError(TestData.sqlException);

        verify(cardDao).insert(TestData.testScannedCardEntity);
    }

    @Test
    public void invokeUpdateCard_withValidCard_completesSuccessfully() {
        when(cardDao.update(TestData.testScannedCardEntity))
                .thenReturn(Completable.complete());

        repository.updateCard(TestData.testScannedCard)
                .test()
                .assertComplete();

        verify(cardDao).update(TestData.testScannedCardEntity);
    }

    @Test
    public void invokeUpdateCard_withValidCard_throwsErrorDueToDbFailure() {
        when(cardDao.update(TestData.testScannedCardEntity))
                .thenReturn(Completable.error(TestData.sqlException));

        repository.updateCard(TestData.testScannedCard)
                .test()
                .assertError(TestData.sqlException);

        verify(cardDao).update(TestData.testScannedCardEntity);
    }

    @Test
    public void invokeDeleteCard_withValidId_completesSuccessfully() {
        when(cardDao.delete(TestData.testMyCardEntity.getId())).thenReturn(Completable.complete());

        repository.deleteCard(TestData.testMyCard.id())
                .test()
                .assertComplete();

        verify(cardDao).delete(TestData.testMyCardEntity.getId());
    }

    @Test
    public void invokeDeleteCard_withValidId_throwsErrorDueToDbFailure() {
        when(cardDao.delete(TestData.testMyCardEntity.getId()))
                .thenReturn(Completable.error(TestData.sqlException));

        repository.deleteCard(TestData.testMyCard.id())
                .test()
                .assertError(TestData.sqlException);

        verify(cardDao).delete(TestData.testMyCardEntity.getId());
    }
}
