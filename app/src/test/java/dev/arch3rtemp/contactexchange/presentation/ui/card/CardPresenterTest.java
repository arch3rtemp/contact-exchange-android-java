package dev.arch3rtemp.contactexchange.presentation.ui.card;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    public GetCardByIdUseCase getCardById;

    @Mock
    public DeleteCardUseCase deleteCard;

    private CardPresenter presenter;

    private TestObserver<CardContract.CardState> stateObserver;
    private TestObserver<CardContract.CardEffect> effectObserver;

    @Before
    public void setup() {
        SchedulerProvider schedulerProvider = new TestSchedulerProvider();
        CardUiMapper mapper = new CardUiMapper(new TimeConverter());
        presenter = new CardPresenter(getCardById, deleteCard, mapper, schedulerProvider);
        stateObserver = presenter.stateStream().test();
        effectObserver = presenter.effectStream().test();
    }

    @Test
    public void testOnCardLoad_success_emitsSuccessState() {
        when(getCardById.invoke(TestData.testMyCard.id()))
                .thenReturn(Observable.just(TestData.testMyCard));

        presenter.setEvent(new CardContract.CardEvent.OnCardLoad(TestData.testMyCard.id()));

        verify(getCardById).invoke(TestData.testMyCard.id());

        stateObserver.assertNoErrors()
                .assertValueCount(3)
                .assertValueAt(0, new CardContract.CardState.Idle())
                .assertValueAt(1, new CardContract.CardState.Loading())
                .assertValueAt(2, new CardContract.CardState.Success(TestData.testMyCardUi));
    }

    @Test
    public void testOnCardLoad_error_emitsErrorState() {
        when(getCardById.invoke(TestData.testMyCard.id()))
                .thenReturn(Observable.error(TestData.sqlException));

        presenter.setEvent(new CardContract.CardEvent.OnCardLoad(TestData.testMyCard.id()));

        verify(getCardById).invoke(TestData.testMyCard.id());

        stateObserver.assertValueCount(3)
                .assertValueAt(0, new CardContract.CardState.Idle())
                .assertValueAt(1, new CardContract.CardState.Loading())
                .assertValueAt(2, new CardContract.CardState.Error());
    }

    @Test
    public void testOnCardDelete_success_emitsAnimationEffect() {
        when(deleteCard.invoke(TestData.testMyCard.id()))
                .thenReturn(Completable.complete());

        presenter.setEvent(new CardContract.CardEvent.OnCardDelete(TestData.testMyCard.id()));

        verify(deleteCard).invoke(TestData.testMyCard.id());

        effectObserver.assertValueCount(1)
                .assertValue(new CardContract.CardEffect.AnimateDeletion());
    }

    @Test
    public void testOnCardDelete_error_emitsShowErrorEffect() {
        when(deleteCard.invoke(TestData.testMyCard.id()))
                .thenReturn(Completable.error(TestData.sqlException));

        presenter.setEvent(new CardContract.CardEvent.OnCardDelete(TestData.testMyCard.id()));

        verify(deleteCard).invoke(TestData.testMyCard.id());

        effectObserver.assertValueCount(1)
                .assertValue(new CardContract.CardEffect.ShowError(TestData.sqlException.getLocalizedMessage()));
    }
}
