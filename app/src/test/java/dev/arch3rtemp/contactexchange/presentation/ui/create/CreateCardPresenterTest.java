package dev.arch3rtemp.contactexchange.presentation.ui.create;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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
import dev.arch3rtemp.contactexchange.domain.usecase.SaveCardUseCase;
import dev.arch3rtemp.contactexchange.domain.usecase.ValidateCardUseCase;
import dev.arch3rtemp.contactexchange.domain.util.SchedulerProvider;
import dev.arch3rtemp.contactexchange.rx.TestSchedulerProvider;
import dev.arch3rtemp.ui.util.StringResourceManager;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.observers.TestObserver;

public class CreateCardPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    public SaveCardUseCase mockSaveCard;

    public ValidateCardUseCase validateCard;

    @Mock
    public StringResourceManager mockStringManager;

    private CreateCardPresenter presenter;

    private TestObserver<CreateCardContract.CreateCardEffect> testEffectObserver;

    @Before
    public void setUp() {
        validateCard = new ValidateCardUseCase();
        SchedulerProvider schedulerProvider = new TestSchedulerProvider();
        presenter = new CreateCardPresenter(mockSaveCard, validateCard, schedulerProvider, mockStringManager);
        testEffectObserver = presenter.effectStream().test();
    }

    @After
    public void tearDown() {
        presenter.destroy();
    }

    @Test
    public void createCard_withValidData_success_emitsNavigateUpEffect() {
        when(mockSaveCard.invoke(TestData.testMyCard)).thenReturn(Completable.complete());

        presenter.setEvent(new CreateCardContract.CreateCardEvent.OnCreateButtonPressed(TestData.testMyCard));

        verify(mockSaveCard).invoke(TestData.testMyCard);

        testEffectObserver.assertValueCount(1)
                .assertValue(new CreateCardContract.CreateCardEffect.NavigateUp());
    }

    @Test
    public void createCard_dbFailure_error_emitsShowErrorEffect() {
        when(mockSaveCard.invoke(TestData.testMyCard)).thenReturn(Completable.error(TestData.sqlException));

        presenter.setEvent(new CreateCardContract.CreateCardEvent.OnCreateButtonPressed(TestData.testMyCard));

        verify(mockSaveCard).invoke(TestData.testMyCard);

        testEffectObserver.assertValueCount(1)
                .assertValue(new CreateCardContract.CreateCardEffect.ShowMessage(TestData.sqlException.getLocalizedMessage()));
    }

    @Test
    public void createCard_withInvalidData_error_emitsShowErrorEffect() {
        when(mockStringManager.string(R.string.msg_all_fields_required)).thenReturn("Please fill in all fields");

        presenter.setEvent(new CreateCardContract.CreateCardEvent.OnCreateButtonPressed(TestData.blankCard));

        verifyNoInteractions(mockSaveCard);

        testEffectObserver.assertValueCount(1)
                .assertValue(new CreateCardContract.CreateCardEffect.ShowMessage("Please fill in all fields"));
    }
}
