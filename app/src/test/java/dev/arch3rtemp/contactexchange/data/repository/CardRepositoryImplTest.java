package dev.arch3rtemp.contactexchange.data.repository;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import dev.arch3rtemp.contactexchange.rx.RxTrampolineRule;
import dev.arch3rtemp.contactexchange.TestData;
import dev.arch3rtemp.contactexchange.data.db.CardDao;
import dev.arch3rtemp.contactexchange.data.mapper.CardEntityMapper;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public class CardRepositoryImplTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public RxTrampolineRule trampolineRule = new RxTrampolineRule();

    @Mock
    public CardDao mockCardDao;

    @Mock
    public CardEntityMapper mockMapper;

    @InjectMocks
    private CardRepositoryImpl repository;

    @Test
    public void invokeGetMyCards_mapsAndReturnsValidData() {
        when(mockCardDao.selectMyCards()).thenReturn(Observable.just(TestData.testCardsEntity));
        when(mockMapper.fromEntityList(TestData.testCardsEntity)).thenReturn(TestData.testCards);

        repository.getMyCards()
                .test()
                .assertComplete()
                .assertValue(TestData.testCards);

        verify(mockCardDao).selectMyCards();
    }

    @Test
    public void invokeGetMyCards_failsDb() {
        when(mockCardDao.selectMyCards()).thenReturn(Observable.error(TestData.sqlException));

        repository.getMyCards()
                .test()
                .assertError(TestData.sqlException);

        verify(mockCardDao).selectMyCards();
    }

    @Test
    public void invokeGetScannedCards_mapsAndReturnsValidData() {
        when(mockCardDao.selectScannedCards()).thenReturn(Observable.just(TestData.testCardsEntity));
        when(mockMapper.fromEntityList(TestData.testCardsEntity)).thenReturn(TestData.testCards);

        repository.getScannedCards()
                .test()
                .assertComplete()
                .assertValue(TestData.testCards);

        verify(mockCardDao).selectScannedCards();
    }

    @Test
    public void invokeGetScannedCards_failsDb() {
        when(mockCardDao.selectScannedCards()).thenReturn(Observable.error(TestData.sqlException));

        repository.getScannedCards()
                .test()
                .assertError(TestData.sqlException);

        verify(mockCardDao).selectScannedCards();
    }

    @Test
    public void invokeGetCardById_mapsAndReturnsValidData() {
        when(mockCardDao.selectCardById(TestData.testMyCardEntity.getId()))
                .thenReturn(Observable.just(TestData.testMyCardEntity));
        when(mockMapper.fromEntity(TestData.testMyCardEntity))
                .thenReturn(TestData.testMyCard);

        repository.getCardById(TestData.testMyCardEntity.getId())
                .test()
                .assertComplete()
                .assertValue(TestData.testMyCard);

        verify(mockCardDao).selectCardById(TestData.testMyCardEntity.getId());
    }

    @Test
    public void invokeGetCardById_failsDb() {
        when(mockCardDao.selectCardById(TestData.testMyCardEntity.getId()))
                .thenReturn(Observable.error(TestData.sqlException));

        repository.getCardById(TestData.testMyCardEntity.getId())
                .test()
                .assertError(TestData.sqlException);

        verify(mockCardDao).selectCardById(TestData.testMyCardEntity.getId());
    }

    @Test
    public void invokeAddCard_withValidCard_completesSuccessfully() {
        when(mockCardDao.insert(TestData.testScannedCardEntity))
                .thenReturn(Completable.complete());
        when(mockMapper.toEntity(TestData.testScannedCard))
                .thenReturn(TestData.testScannedCardEntity);

        repository.addCard(TestData.testScannedCard)
                .test()
                .assertComplete();

        verify(mockCardDao).insert(TestData.testScannedCardEntity);
    }

    @Test
    public void invokeAddCard_failsDb() {
        when(mockCardDao.insert(TestData.testScannedCardEntity))
                .thenReturn(Completable.error(TestData.sqlException));
        when(mockMapper.toEntity(TestData.testScannedCard))
                .thenReturn(TestData.testScannedCardEntity);

        repository.addCard(TestData.testScannedCard)
                .test()
                .assertError(TestData.sqlException);

        verify(mockCardDao).insert(TestData.testScannedCardEntity);
    }

    @Test
    public void invokeUpdateCard_withValidCard_completesSuccessfully() {
        when(mockCardDao.update(TestData.testScannedCardEntity))
                .thenReturn(Completable.complete());
        when(mockMapper.toEntity(TestData.testScannedCard))
                .thenReturn(TestData.testScannedCardEntity);

        repository.updateCard(TestData.testScannedCard)
                .test()
                .assertComplete();

        verify(mockCardDao).update(TestData.testScannedCardEntity);
    }

    @Test
    public void invokeUpdateCard_withValidCard_throwsErrorDueToDbFailure() {
        when(mockCardDao.update(TestData.testScannedCardEntity))
                .thenReturn(Completable.error(TestData.sqlException));
        when(mockMapper.toEntity(TestData.testScannedCard))
                .thenReturn(TestData.testScannedCardEntity);

        repository.updateCard(TestData.testScannedCard)
                .test()
                .assertError(TestData.sqlException);

        verify(mockCardDao).update(TestData.testScannedCardEntity);
    }

    @Test
    public void invokeDeleteCard_withValidId_completesSuccessfully() {
        when(mockCardDao.delete(TestData.testMyCardEntity.getId())).thenReturn(Completable.complete());

        repository.deleteCard(TestData.testMyCard.id())
                .test()
                .assertComplete();

        verify(mockCardDao).delete(TestData.testMyCardEntity.getId());
    }

    @Test
    public void invokeDeleteCard_withValidId_throwsErrorDueToDbFailure() {
        when(mockCardDao.delete(TestData.testMyCardEntity.getId()))
                .thenReturn(Completable.error(TestData.sqlException));

        repository.deleteCard(TestData.testMyCard.id())
                .test()
                .assertError(TestData.sqlException);

        verify(mockCardDao).delete(TestData.testMyCardEntity.getId());
    }
}
