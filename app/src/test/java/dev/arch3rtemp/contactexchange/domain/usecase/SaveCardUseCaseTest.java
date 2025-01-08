package dev.arch3rtemp.contactexchange.domain.usecase;

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
import dev.arch3rtemp.contactexchange.domain.repository.CardRepository;
import io.reactivex.rxjava3.core.Completable;

public class SaveCardUseCaseTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public RxTrampolineRule rxTrampolineRule = new RxTrampolineRule();

    @Mock
    private CardRepository repository;

    @InjectMocks
    private SaveCardUseCase saveCard;

    @Test
    public void invokeWithValidCard_completesSuccessfully() {
        when(repository.addCard(TestData.testScannedCard))
                .thenReturn(Completable.complete());

        saveCard.invoke(TestData.testScannedCard)
                .test()
                .assertComplete();

        verify(repository).addCard(TestData.testScannedCard);
    }

    @Test
    public void invokeWhenRepositoryFails_emitsError() {
        when(repository.addCard(TestData.testScannedCard))
                .thenReturn(Completable.error(TestData.sqlException));

        saveCard.invoke(TestData.testScannedCard)
                .test()
                .assertError(TestData.sqlException);
    }

    @Test
    public void invokeWithNullCard_emitsError() {
        saveCard.invoke(null)
                .test()
                .assertFailure(IllegalArgumentException.class);
    }
}
