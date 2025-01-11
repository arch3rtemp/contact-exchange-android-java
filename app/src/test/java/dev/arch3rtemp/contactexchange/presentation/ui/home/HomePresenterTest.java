package dev.arch3rtemp.contactexchange.presentation.ui.home;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;
import java.util.concurrent.TimeUnit;

import dev.arch3rtemp.contactexchange.TestData;
import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.domain.usecase.DeleteCardUseCase;
import dev.arch3rtemp.contactexchange.domain.usecase.FilterCardsUseCase;
import dev.arch3rtemp.contactexchange.domain.usecase.GetMyCardsUseCase;
import dev.arch3rtemp.contactexchange.domain.usecase.GetScannedCardsUseCase;
import dev.arch3rtemp.contactexchange.domain.usecase.SaveCardUseCase;
import dev.arch3rtemp.contactexchange.domain.util.ErrorMsgConstants;
import dev.arch3rtemp.contactexchange.domain.util.SchedulerProvider;
import dev.arch3rtemp.contactexchange.presentation.mapper.CardUiMapper;
import dev.arch3rtemp.contactexchange.rx.RxTestSchedulerRule;
import dev.arch3rtemp.contactexchange.rx.TestSchedulerProvider;
import dev.arch3rtemp.ui.util.StringResourceManager;
import dev.arch3rtemp.ui.util.TimeConverter;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.schedulers.TestScheduler;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class HomePresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public final RxTestSchedulerRule rxTestSchedulerRule = new RxTestSchedulerRule();

    @Mock
    public GetMyCardsUseCase mockGetMyCards;

    @Mock
    public GetScannedCardsUseCase mockGetScannedCards;

    @Mock
    public DeleteCardUseCase mockDeleteCard;

    @Mock
    public SaveCardUseCase mockSaveCard;

    @Mock
    public FilterCardsUseCase mockFilterCards;

    @Mock
    public StringResourceManager resourceManager;

    private HomePresenter presenter;

    private TestObserver<HomeContract.HomeState> testStateObserver;

    private TestObserver<HomeContract.HomeEffect> testEffectObserver;
    private TestScheduler testScheduler;

    @Before
    public void setUp() {
        SchedulerProvider testSchedulerProvider = new TestSchedulerProvider();
        var mapper = new CardUiMapper(new TimeConverter());
        presenter = new HomePresenter(mockGetMyCards, mockGetScannedCards, mockDeleteCard, mockSaveCard, mockFilterCards, resourceManager, mapper, testSchedulerProvider);
        testStateObserver = presenter.stateStream().test();
        testEffectObserver = presenter.effectStream().test();
    }

    @After
    public void tearDown() {
        presenter.destroy();
    }

    @Test
    public void loadMyCards_emitsSuccessState() {
        when(mockGetMyCards.invoke()).thenReturn(Observable.just(TestData.testCards));

        presenter.setEvent(new HomeContract.HomeEvent.OnCardsLoad());

        verify(mockGetMyCards).invoke();

        testStateObserver.assertValueCount(3)
                .assertValueAt(0, new HomeContract.HomeState(new HomeContract.ViewState.Idle(), presenter.getCurrentState().scannedCards()))
                .assertValueAt(1, new HomeContract.HomeState(new HomeContract.ViewState.Loading(), presenter.getCurrentState().scannedCards()))
                .assertValueAt(2, new HomeContract.HomeState(new HomeContract.ViewState.Success(TestData.testCardsUi), presenter.getCurrentState().scannedCards()));
    }

    @Test
    public void loadMyCards_emitsEmptyState() {
        when(mockGetMyCards.invoke()).thenReturn(Observable.just(List.of()));

        presenter.setEvent(new HomeContract.HomeEvent.OnCardsLoad());

        verify(mockGetMyCards).invoke();

        testStateObserver.assertValueCount(3)
                .assertValueAt(0, new HomeContract.HomeState(new HomeContract.ViewState.Idle(), presenter.getCurrentState().scannedCards()))
                .assertValueAt(1, new HomeContract.HomeState(new HomeContract.ViewState.Loading(), presenter.getCurrentState().scannedCards()))
                .assertValueAt(2, new HomeContract.HomeState(new HomeContract.ViewState.Empty(), presenter.getCurrentState().scannedCards()));
    }

    @Test
    public void loadMyCards_dbFailure_emitsErrorState() {
        when(mockGetMyCards.invoke()).thenReturn(Observable.error(TestData.sqlException));

        presenter.setEvent(new HomeContract.HomeEvent.OnCardsLoad());

        verify(mockGetMyCards).invoke();

        testStateObserver.assertValueCount(3)
                .assertValueAt(0, new HomeContract.HomeState(new HomeContract.ViewState.Idle(), presenter.getCurrentState().scannedCards()))
                .assertValueAt(1, new HomeContract.HomeState(new HomeContract.ViewState.Loading(), presenter.getCurrentState().scannedCards()))
                .assertValueAt(2, new HomeContract.HomeState(new HomeContract.ViewState.Error(TestData.sqlException.getLocalizedMessage()), presenter.getCurrentState().scannedCards()));
    }

    @Test
    public void loadScannedCards_emitsSuccessState() {
        when(mockGetScannedCards.invoke()).thenReturn(Observable.just(TestData.testCards));

        presenter.setEvent(new HomeContract.HomeEvent.OnContactsLoad());

        verify(mockGetScannedCards).invoke();

        testStateObserver.assertValueCount(3)
                .assertValueAt(0, new HomeContract.HomeState(presenter.getCurrentState().myCards(), new HomeContract.ViewState.Idle()))
                .assertValueAt(1, new HomeContract.HomeState(presenter.getCurrentState().myCards(), new HomeContract.ViewState.Loading()))
                .assertValueAt(2, new HomeContract.HomeState(presenter.getCurrentState().myCards(), new HomeContract.ViewState.Success(TestData.testCardsUi)));
    }

    @Test
    public void loadScannedCards_emitsEmptyState() {
        when(mockGetScannedCards.invoke()).thenReturn(Observable.just(List.of()));

        presenter.setEvent(new HomeContract.HomeEvent.OnContactsLoad());

        verify(mockGetScannedCards).invoke();

        testStateObserver.assertValueCount(3)
                .assertValueAt(0, new HomeContract.HomeState(presenter.getCurrentState().myCards(), new HomeContract.ViewState.Idle()))
                .assertValueAt(1, new HomeContract.HomeState(presenter.getCurrentState().myCards(), new HomeContract.ViewState.Loading()))
                .assertValueAt(2, new HomeContract.HomeState(presenter.getCurrentState().myCards(), new HomeContract.ViewState.Empty()));
    }

    @Test
    public void loadScannedCards_dbFailure__emitsErrorState() {
        when(mockGetScannedCards.invoke()).thenReturn(Observable.error(TestData.sqlException));

        presenter.setEvent(new HomeContract.HomeEvent.OnContactsLoad());

        verify(mockGetScannedCards).invoke();

        testStateObserver.assertValueCount(3)
                .assertValueAt(0, new HomeContract.HomeState(presenter.getCurrentState().myCards(), new HomeContract.ViewState.Idle()))
                .assertValueAt(1, new HomeContract.HomeState(presenter.getCurrentState().myCards(), new HomeContract.ViewState.Loading()))
                .assertValueAt(2, new HomeContract.HomeState(presenter.getCurrentState().myCards(), new HomeContract.ViewState.Error(TestData.sqlException.getLocalizedMessage())));
    }

    @Test
    public void deleteCard_withValidId_emitsUndoEffect() {
        when(mockDeleteCard.invoke(TestData.testMyCard.id()))
                .thenReturn(Completable.complete());

        presenter.setEvent(new HomeContract.HomeEvent.OnContactDeleted(TestData.testMyCardUi));

        verify(mockDeleteCard).invoke(TestData.testMyCard.id());

        testEffectObserver.assertValueCount(1)
                .assertValue(new HomeContract.HomeEffect.ShowUndo(TestData.testMyCardUi));
    }

    @Test
    public void deleteCard_withInvalidId_emitsShowErrorEffect() {
        when(mockDeleteCard.invoke(TestData.NEGATIVE_CARD_ID))
                .thenReturn(Completable.error(new IllegalArgumentException(ErrorMsgConstants.MSG_ID_MUST_BE_POSITIVE)));

        presenter.setEvent(new HomeContract.HomeEvent.OnContactDeleted(TestData.testInvalidCardUi));

        verify(mockDeleteCard).invoke(TestData.NEGATIVE_CARD_ID);

        testEffectObserver
                .assertValue(new HomeContract.HomeEffect.ShowError(ErrorMsgConstants.MSG_ID_MUST_BE_POSITIVE));
    }

    @Test
    public void saveCard_withValidData_success() {
        when(mockSaveCard.invoke(TestData.testScannedCard)).thenReturn(Completable.complete());

        presenter.setEvent(new HomeContract.HomeEvent.OnContactSaved(TestData.testScannedCardUi));

        verify(mockSaveCard).invoke(TestData.testScannedCard);
    }

    @Test
    public void saveCard_dbFailure_emitsShowErrorEffect() {
        when(mockSaveCard.invoke(TestData.testScannedCard)).thenReturn(Completable.error(TestData.sqlException));

        presenter.setEvent(new HomeContract.HomeEvent.OnContactSaved(TestData.testScannedCardUi));

        verify(mockSaveCard).invoke(TestData.testScannedCard);

        testEffectObserver
                .assertValue(new HomeContract.HomeEffect.ShowError(TestData.sqlException.getLocalizedMessage()));
    }

    @Test
    public void filterCards_withValidQuery_emitsSuccessState() {
        // Prepare presenter for filtering
        when(mockGetScannedCards.invoke()).thenReturn(Observable.just(TestData.testCards));
        PublishSubject<List<Card>> filterSubject = PublishSubject.create();
        when(mockFilterCards.getFilteredCardsStream()).thenReturn(filterSubject.observeOn(rxTestSchedulerRule.getTestScheduler()));

        presenter.setEvent(new HomeContract.HomeEvent.OnContactsLoad());
        verify(mockGetScannedCards).invoke();

        // Simulate the filtering response
        var simulatedResponse = List.of(TestData.testScannedCard);
        var expectedCard = List.of(TestData.testScannedCardUi);
        var query = "jane";

        // Trigger the search query event
        presenter.setEvent(new HomeContract.HomeEvent.OnSearchQuery(query));
        filterSubject.onNext(simulatedResponse);
        verify(mockFilterCards).invoke(query, TestData.testCards);

        // Advance time to trigger debounce
        rxTestSchedulerRule.getTestScheduler().advanceTimeBy(300, TimeUnit.MILLISECONDS);

        // Assert that the presenter emits the correct filtered state
        testStateObserver
                .assertValueCount(4) // Idle, Loading, Success (unfiltered), Success (filtered)
                .assertValueAt(3, new HomeContract.HomeState(presenter.getCurrentState().myCards(), new HomeContract.ViewState.Success(expectedCard)));
    }

    @Test
    public void filterCards_withEmptyQuery_emitsAllCards() {
        // Prepare presenter for filtering
        when(mockGetScannedCards.invoke()).thenReturn(Observable.just(TestData.testCards));
        PublishSubject<List<Card>> filterSubject = PublishSubject.create();
        when(mockFilterCards.getFilteredCardsStream()).thenReturn(filterSubject.observeOn(rxTestSchedulerRule.getTestScheduler()));

        presenter.setEvent(new HomeContract.HomeEvent.OnContactsLoad());
        verify(mockGetScannedCards).invoke();

        // Simulate the filtering response
        var simulatedResponse = List.of(TestData.testScannedCard);
        var query = "jane";

        // Trigger the search query event
        presenter.setEvent(new HomeContract.HomeEvent.OnSearchQuery(query));
        filterSubject.onNext(simulatedResponse);
        verify(mockFilterCards).invoke(query, TestData.testCards);

        // Trigger the search query event
        presenter.setEvent(new HomeContract.HomeEvent.OnSearchQuery(""));
        filterSubject.onNext(TestData.testCards);
        verify(mockFilterCards).invoke("", TestData.testCards);

        // Advance time to trigger debounce
        rxTestSchedulerRule.getTestScheduler().advanceTimeBy(300, TimeUnit.MILLISECONDS);

        // Assert that the presenter emits the correct filtered state
        testStateObserver
                .assertValueCount(5)
                .assertValueAt(4, new HomeContract.HomeState(presenter.getCurrentState().myCards(), new HomeContract.ViewState.Success(TestData.testCardsUi)));
    }

    @Test
    public void filterCards_withNoMatches_emitsEmptyState() {
        // Prepare presenter for filtering
        when(mockGetScannedCards.invoke()).thenReturn(Observable.just(TestData.testCards));
        PublishSubject<List<Card>> filterSubject = PublishSubject.create();
        when(mockFilterCards.getFilteredCardsStream()).thenReturn(filterSubject.observeOn(rxTestSchedulerRule.getTestScheduler()));

        presenter.setEvent(new HomeContract.HomeEvent.OnContactsLoad());
        verify(mockGetScannedCards).invoke();

        var invalidQuery = "invalid";

        // Trigger the search query event
        presenter.setEvent(new HomeContract.HomeEvent.OnSearchQuery(invalidQuery));
        filterSubject.onNext(List.of());
        verify(mockFilterCards).invoke(invalidQuery, TestData.testCards);

        // Advance time to trigger debounce
        rxTestSchedulerRule.getTestScheduler().advanceTimeBy(300, TimeUnit.MILLISECONDS);

        // Assert that the presenter emits the correct filtered state
        testStateObserver
                .assertValueCount(4)
                .assertValueAt(3, new HomeContract.HomeState(presenter.getCurrentState().myCards(), new HomeContract.ViewState.Empty()));
    }

    @Test
    public void filterCards_withError_emitsErrorEffect() {
        // Prepare presenter for filtering
        when(mockGetScannedCards.invoke()).thenReturn(Observable.just(TestData.testCards));
        PublishSubject<List<Card>> filterSubject = PublishSubject.create();
        when(mockFilterCards.getFilteredCardsStream()).thenReturn(filterSubject.observeOn(rxTestSchedulerRule.getTestScheduler()));

        presenter.setEvent(new HomeContract.HomeEvent.OnContactsLoad());
        verify(mockGetScannedCards).invoke();

        // When a query causes an error in the filter use case
        var query = "jane";
        var errorMsg = "Unforeseen error";
        presenter.setEvent(new HomeContract.HomeEvent.OnSearchQuery(query));
        filterSubject.onError(new RuntimeException(errorMsg));
        verify(mockFilterCards).invoke(query, TestData.testCards);

        // Advance time to trigger debounce
        rxTestSchedulerRule.getTestScheduler().advanceTimeBy(300, TimeUnit.MILLISECONDS);

        // Assert that the presenter emits the correct filtered state
        testStateObserver
                .assertValueCount(4)
                .assertValueAt(3, new HomeContract.HomeState(presenter.getCurrentState().myCards(), new HomeContract.ViewState.Error(errorMsg)));
    }
}
