package dev.arch3rtemp.contactexchange.domain.usecase;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import dev.arch3rtemp.contactexchange.rx.RxTrampolineRule;
import dev.arch3rtemp.contactexchange.TestData;
import dev.arch3rtemp.contactexchange.domain.repository.CardRepository;
import io.reactivex.rxjava3.core.Observable;

public class GetCardByIdUseCaseTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public RxTrampolineRule rxTrampolineRule = new RxTrampolineRule();

    @Mock
    public CardRepository mockRepository;

    @InjectMocks
    private GetCardByIdUseCase getCardById;

    @Test
    public void invokeWithValidId_emitsData() {
        when(mockRepository.getCardById(TestData.testScannedCard.id()))
                .thenReturn(Observable.just(TestData.testScannedCard));

        getCardById.invoke(TestData.testScannedCard.id())
                .test()
                .assertComplete()
                .assertValue(TestData.testScannedCard);

        verify(mockRepository).getCardById(TestData.testScannedCard.id());
    }

    @Test
    public void invokeWithInvalidId_emitsError() {
        getCardById.invoke(TestData.NEGATIVE_CARD_ID)
                .test()
                .assertFailure(IllegalArgumentException.class);

        verifyNoInteractions(mockRepository);
    }

    @Test
    public void invokeWithZeroId_emitsError() {
        getCardById.invoke(TestData.ZERO_CARD_ID)
                .test()
                .assertFailure(IllegalArgumentException.class);

        verifyNoInteractions(mockRepository);
    }

    @Test
    public void invokeWhenRepositoryFails_emitsError() {
        when(mockRepository.getCardById(TestData.testScannedCard.id()))
                .thenReturn(Observable.error(TestData.sqlException));

        getCardById.invoke(TestData.testScannedCard.id())
                .test()
                .assertError(TestData.sqlException);

        verify(mockRepository).getCardById(TestData.testScannedCard.id());
    }

}
