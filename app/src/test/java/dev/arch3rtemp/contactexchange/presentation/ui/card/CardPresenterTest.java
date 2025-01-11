package dev.arch3rtemp.contactexchange.presentation.ui.card;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

import dev.arch3rtemp.contactexchange.TestData;
import dev.arch3rtemp.contactexchange.domain.usecase.DeleteCardUseCase;
import dev.arch3rtemp.contactexchange.domain.usecase.GetCardByIdUseCase;
import dev.arch3rtemp.contactexchange.domain.util.SchedulerProvider;
import dev.arch3rtemp.contactexchange.presentation.mapper.CardUiMapper;
import dev.arch3rtemp.contactexchange.rx.TestSchedulerProvider;
import dev.arch3rtemp.ui.util.TimeConverter;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;

@RunWith(RobolectricTestRunner.class)
public class CardPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    public GetCardByIdUseCase mockGetCardById;

    @Mock
    public DeleteCardUseCase mockDeleteCard;

    private CardPresenter presenter;

    private TestObserver<CardContract.CardState> testStateObserver;
    private TestObserver<CardContract.CardEffect> testEffectObserver;

    @Before
    public void setUp() {
        SchedulerProvider schedulerProvider = new TestSchedulerProvider();
        CardUiMapper mapper = new CardUiMapper(new TimeConverter());
        presenter = new CardPresenter(mockGetCardById, mockDeleteCard, mapper, schedulerProvider);
        testStateObserver = presenter.stateStream().test();
        testEffectObserver = presenter.effectStream().test();
    }

    @After
    public void tearDown() {
        presenter.destroy();
    }

    @Test
    public void loadCard_success_emitsSuccessState() {
        when(mockGetCardById.invoke(TestData.testMyCard.id()))
                .thenReturn(Observable.just(TestData.testMyCard));

        presenter.setEvent(new CardContract.CardEvent.OnCardLoad(TestData.testMyCard.id()));

        verify(mockGetCardById).invoke(TestData.testMyCard.id());

        testStateObserver.assertNoErrors()
                .assertValueCount(3)
                .assertValueAt(0, new CardContract.CardState.Idle())
                .assertValueAt(1, new CardContract.CardState.Loading())
                .assertValueAt(2, new CardContract.CardState.Success(TestData.testMyCardUi));
    }

    @Test
    public void loadCard_error_emitsErrorState() {
        when(mockGetCardById.invoke(TestData.testMyCard.id()))
                .thenReturn(Observable.error(TestData.sqlException));

        presenter.setEvent(new CardContract.CardEvent.OnCardLoad(TestData.testMyCard.id()));

        verify(mockGetCardById).invoke(TestData.testMyCard.id());

        testStateObserver.assertValueCount(3)
                .assertValueAt(0, new CardContract.CardState.Idle())
                .assertValueAt(1, new CardContract.CardState.Loading())
                .assertValueAt(2, new CardContract.CardState.Error());
    }

    @Test
    public void deleteCard_success_emitsAnimationEffect() {
        when(mockDeleteCard.invoke(TestData.testMyCard.id()))
                .thenReturn(Completable.complete());

        presenter.setEvent(new CardContract.CardEvent.OnCardDelete(TestData.testMyCard.id()));

        verify(mockDeleteCard).invoke(TestData.testMyCard.id());

        testEffectObserver.assertValueCount(1)
                .assertValue(new CardContract.CardEffect.AnimateDeletion());
    }

    @Test
    public void deleteCard_error_emitsShowErrorEffect() {
        when(mockDeleteCard.invoke(TestData.testMyCard.id()))
                .thenReturn(Completable.error(TestData.sqlException));

        presenter.setEvent(new CardContract.CardEvent.OnCardDelete(TestData.testMyCard.id()));

        verify(mockDeleteCard).invoke(TestData.testMyCard.id());

        testEffectObserver.assertValueCount(1)
                .assertValue(new CardContract.CardEffect.ShowError(TestData.sqlException.getLocalizedMessage()));
    }
}
