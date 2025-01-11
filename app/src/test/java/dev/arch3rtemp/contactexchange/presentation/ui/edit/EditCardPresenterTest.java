package dev.arch3rtemp.contactexchange.presentation.ui.edit;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.TestData;
import dev.arch3rtemp.contactexchange.domain.usecase.GetCardByIdUseCase;
import dev.arch3rtemp.contactexchange.domain.usecase.UpdateCardUseCase;
import dev.arch3rtemp.contactexchange.domain.usecase.ValidateCardUseCase;
import dev.arch3rtemp.contactexchange.domain.util.SchedulerProvider;
import dev.arch3rtemp.contactexchange.presentation.mapper.CardUiMapper;
import dev.arch3rtemp.contactexchange.rx.TestSchedulerProvider;
import dev.arch3rtemp.ui.util.StringResourceManager;
import dev.arch3rtemp.ui.util.TimeConverter;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;

public class EditCardPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    public GetCardByIdUseCase mockGetCardById;

    @Mock
    public UpdateCardUseCase mockUpdateCard;

    @Mock
    public StringResourceManager mockStringManager;

    private EditCardPresenter presenter;

    private TestObserver<EditCardContract.EditCardState> testStateObserver;

    private TestObserver<EditCardContract.EditCardEffect> testEffectObserver;

    @Before
    public void setUp() {
        var mapper = new CardUiMapper(new TimeConverter());
        SchedulerProvider testSchedulerProvider = new TestSchedulerProvider();
        var validateCard = new ValidateCardUseCase();
        presenter = new EditCardPresenter(
                mockGetCardById, mockUpdateCard, validateCard, mockStringManager, mapper, testSchedulerProvider
        );
        testStateObserver = presenter.stateStream().test();
        testEffectObserver = presenter.effectStream().test();
    }

    @After
    public void tearDown() {
        presenter.destroy();
    }

    @Test
    public void loadCard_withValidId_emitsSuccessState() {
        when(mockGetCardById.invoke(TestData.testMyCard.id()))
                .thenReturn(Observable.just(TestData.testMyCard));

        presenter.setEvent(new EditCardContract.EditCardEvent.OnCardLoad(TestData.testMyCard.id()));

        verify(mockGetCardById).invoke(TestData.testMyCard.id());

        testStateObserver.assertValueCount(3)
                .assertValueAt(0, new EditCardContract.EditCardState.Idle())
                .assertValueAt(1, new EditCardContract.EditCardState.Loading())
                .assertValueAt(2, new EditCardContract.EditCardState.Success(TestData.testMyCardUi));

    }

    @Test
    public void loadCard_withInvalidId_emitsErrorStateAndShowErrorEffect() {
        when(mockGetCardById.invoke(TestData.NEGATIVE_CARD_ID))
                .thenReturn(Observable.error(TestData.sqlException));

        presenter.setEvent(new EditCardContract.EditCardEvent.OnCardLoad(TestData.NEGATIVE_CARD_ID));

        verify(mockGetCardById).invoke(TestData.NEGATIVE_CARD_ID);

        testStateObserver.assertValueCount(3)
                .assertValueAt(0, new EditCardContract.EditCardState.Idle())
                .assertValueAt(1, new EditCardContract.EditCardState.Loading())
                .assertValueAt(2, new EditCardContract.EditCardState.Error());

        testEffectObserver.assertValue(new EditCardContract.EditCardEffect.ShowError(TestData.sqlException.getLocalizedMessage()));
    }

    @Test
    public void updateCard_withValidData_emitsNavigateUpEffect() {
        when(mockGetCardById.invoke(TestData.testMyCard.id()))
                .thenReturn(Observable.just(TestData.testMyCard));
        when(mockUpdateCard.invoke(TestData.testMyCard, TestData.testNewCard))
                .thenReturn(Completable.complete());

        presenter.setEvent(new EditCardContract.EditCardEvent.OnCardLoad(TestData.testMyCard.id()));
        presenter.setEvent(new EditCardContract.EditCardEvent.OnUpdateButtonPress(TestData.testNewCard));

        testEffectObserver.assertValue(new EditCardContract.EditCardEffect.NavigateUp());
    }

    @Test
    public void updateCard_withInvalidData_emitsShowErrorEffect() {
        when(mockGetCardById.invoke(TestData.testMyCard.id()))
                .thenReturn(Observable.just(TestData.testMyCard));
        when(mockStringManager.string(R.string.msg_all_fields_required))
                .thenReturn("Please fill in all fields");

        presenter.setEvent(new EditCardContract.EditCardEvent.OnCardLoad(TestData.testMyCard.id()));
        presenter.setEvent(new EditCardContract.EditCardEvent.OnUpdateButtonPress(TestData.blankCard));

        testEffectObserver.assertValue(new EditCardContract.EditCardEffect.ShowError("Please fill in all fields"));
    }

    @Test
    public void updateCard_withValidData_dbFailure_emitsShowErrorEffect() {
        when(mockGetCardById.invoke(TestData.testMyCard.id()))
                .thenReturn(Observable.just(TestData.testMyCard));
        when(mockUpdateCard.invoke(TestData.testMyCard, TestData.testNewCard))
                .thenReturn(Completable.error(TestData.sqlException));

        presenter.setEvent(new EditCardContract.EditCardEvent.OnCardLoad(TestData.testMyCard.id()));
        presenter.setEvent(new EditCardContract.EditCardEvent.OnUpdateButtonPress(TestData.testNewCard));

        testEffectObserver.assertValue(new EditCardContract.EditCardEffect.ShowError(TestData.sqlException.getLocalizedMessage()));
    }
}
