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
import io.reactivex.rxjava3.core.Completable;

public class DeleteCardUseCaseTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public RxTrampolineRule rxTrampolineRule = new RxTrampolineRule();

    @Mock
    private CardRepository repository;

    @InjectMocks
    private DeleteCardUseCase deleteCard;

    @Test
    public void invokeWithValidId_emitsComplete() {
        when(repository.deleteCard(TestData.testMyCard.id()))
                .thenReturn(Completable.complete());

        deleteCard.invoke(TestData.testMyCard.id())
                .test()
                .assertComplete();

        verify(repository).deleteCard(TestData.testMyCard.id());
    }

    @Test
    public void invokeWithInvalidId_emitsError() {
        deleteCard.invoke(TestData.NEGATIVE_CARD_ID)
                .test()
                .assertFailure(IllegalArgumentException.class);
    }

    @Test
    public void invokeWithZeroId_emitsError() {
        deleteCard.invoke(TestData.ZERO_CARD_ID)
                .test()
                .assertFailure(IllegalArgumentException.class);
    }

    @Test
    public void invokeWhenRepositoryFails_emitsError() {
        when(repository.deleteCard(TestData.testMyCard.id()))
                .thenReturn(Completable.error(TestData.sqlException));

        deleteCard.invoke(TestData.testMyCard.id())
                .test()
                .assertError(TestData.sqlException);
    }

}
