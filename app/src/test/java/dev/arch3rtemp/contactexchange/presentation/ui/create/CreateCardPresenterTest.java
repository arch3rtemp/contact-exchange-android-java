package dev.arch3rtemp.contactexchange.presentation.ui.create;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.TestData;
import dev.arch3rtemp.contactexchange.domain.model.Card;
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
    public SaveCardUseCase saveCard;

    public ValidateCardUseCase validateCard;

    @Mock
    public StringResourceManager stringResourceManager;

    private CreateCardPresenter presenter;

    private TestObserver<CreateCardContract.CreateCardEffect> effectObserver;

    @Before
    public void setup() {
        validateCard = new ValidateCardUseCase();
        SchedulerProvider schedulerProvider = new TestSchedulerProvider();
        presenter = new CreateCardPresenter(saveCard, validateCard, schedulerProvider, stringResourceManager);
        effectObserver = presenter.effectStream().test();
    }

    @Test
    public void testOnCreateButtonPressed_withValidData_success_emitsNavigateUpEffect() {
        when(saveCard.invoke(TestData.testMyCard)).thenReturn(Completable.complete());

        presenter.setEvent(new CreateCardContract.CreateCardEvent.OnCreateButtonPressed(TestData.testMyCard));

        verify(saveCard).invoke(TestData.testMyCard);

        effectObserver.assertValueCount(1)
                .assertValue(new CreateCardContract.CreateCardEffect.NavigateUp());
    }

    @Test
    public void testOnCreateButtonPressed_withValidData_error_emitsShowErrorEffect() {
        when(saveCard.invoke(TestData.testMyCard)).thenReturn(Completable.error(TestData.sqlException));

        presenter.setEvent(new CreateCardContract.CreateCardEvent.OnCreateButtonPressed(TestData.testMyCard));

        verify(saveCard).invoke(TestData.testMyCard);

        effectObserver.assertValueCount(1)
                .assertValue(new CreateCardContract.CreateCardEffect.ShowMessage(TestData.sqlException.getLocalizedMessage()));
    }

    @Test
    public void testOnCreateButtonPressed_withInvalidData_error_emitsShowErrorEffect() {
        when(stringResourceManager.string(R.string.msg_all_fields_required)).thenReturn("Please fill in all fields");
        var blankCard = new Card(-1, "", "", "", "", "", "", -1, -1, false);

        presenter.setEvent(new CreateCardContract.CreateCardEvent.OnCreateButtonPressed(blankCard));

        verifyNoInteractions(saveCard);

        effectObserver.assertValueCount(1)
                .assertValue(new CreateCardContract.CreateCardEffect.ShowMessage("Please fill in all fields"));
    }
}
