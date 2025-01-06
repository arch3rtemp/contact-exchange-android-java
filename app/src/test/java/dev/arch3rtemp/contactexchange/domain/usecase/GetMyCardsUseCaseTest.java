package dev.arch3rtemp.contactexchange.domain.usecase;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import dev.arch3rtemp.contactexchange.RxTrampolineRule;
import dev.arch3rtemp.contactexchange.TestData;
import dev.arch3rtemp.contactexchange.domain.repository.CardRepository;
import io.reactivex.rxjava3.core.Observable;

public class GetMyCardsUseCaseTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public RxTrampolineRule rxTrampolineRule = new RxTrampolineRule();

    @Mock
    public CardRepository repository;

    @InjectMocks
    private GetMyCardsUseCase getMyCards;

    @Test
    public void invoke_emitsValidData() {
        when(repository.getMyCards()).thenReturn(Observable.just(TestData.testCards));

        getMyCards.invoke()
                .test()
                .assertComplete()
                .assertValue(TestData.testCards);

        verify(repository).getMyCards();
    }

    @Test
    public void invokeWhenRepositoryFails_emitsError() {
        when(repository.getMyCards()).thenReturn(Observable.error(TestData.sqlException));

        getMyCards.invoke()
                .test()
                .assertError(TestData.sqlException);

        verify(repository).getMyCards();
    }

}
