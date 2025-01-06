package dev.arch3rtemp.contactexchange.domain.usecase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static dev.arch3rtemp.contactexchange.domain.util.ErrorMsgConstants.MSG_CARD_CANNOT_BE_NULL;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Objects;

import dev.arch3rtemp.contactexchange.RxTrampolineRule;
import dev.arch3rtemp.contactexchange.TestData;
import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.domain.repository.CardRepository;
import io.reactivex.rxjava3.core.Completable;

public class UpdateCardUseCaseTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public RxTrampolineRule rxTrampolineRule = new RxTrampolineRule();

    @Mock
    private CardRepository repository;

    @InjectMocks
    private UpdateCardUseCase updateCard;

    @Test
    public void invokeWithValidNewCard_emitsComplete() {

        when(repository.updateCard(any(Card.class)))
                .thenReturn(Completable.complete());

        updateCard.invoke(TestData.testMyCard, TestData.testNewCard)
                .test()
                .assertComplete();

        verify(repository).updateCard(TestData.mergedCard);
    }

    @Test
    public void invokeWithNullNewCard_emitsError() {
        updateCard.invoke(TestData.testMyCard, null)
                .test()
                .assertError(error ->
                        error instanceof IllegalArgumentException
                                && Objects.equals(error.getMessage(), MSG_CARD_CANNOT_BE_NULL)
                );
    }

    @Test
    public void invokeWhenRepositoryFails_emitsError() {
        when(repository.updateCard(TestData.testNewCard))
                .thenReturn(Completable.error(TestData.sqlException));

        updateCard.invoke(TestData.testNewCard, TestData.testNewCard)
                .test()
                .assertError(TestData.sqlException);
    }

}
