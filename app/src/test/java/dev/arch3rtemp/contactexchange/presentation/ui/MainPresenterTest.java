package dev.arch3rtemp.contactexchange.presentation.ui;

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
import dev.arch3rtemp.contactexchange.domain.usecase.SaveCardUseCase;
import dev.arch3rtemp.contactexchange.domain.util.SchedulerProvider;
import dev.arch3rtemp.contactexchange.rx.TestSchedulerProvider;
import dev.arch3rtemp.ui.util.StringResourceManager;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.observers.TestObserver;

public class MainPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    public SaveCardUseCase mockSaveCard;

    @Mock
    public StringResourceManager mockStringManager;

    private MainPresenter presenter;

    private TestObserver<MainContract.MainEffect> testEffectObserver;

    @Before
    public void setUp() {
        SchedulerProvider schedulerProvider = new TestSchedulerProvider();
        presenter = new MainPresenter(mockSaveCard, schedulerProvider, mockStringManager);
        testEffectObserver = presenter.effectStream().test();
    }

    @After
    public void tearDown() {
        presenter.destroy();
    }

    @Test
    public void createCard_withValidData_emitsSuccess() {
        when(mockSaveCard.invoke(TestData.testScannedCard)).thenReturn(Completable.complete());
        when(mockStringManager.string(R.string.msg_contact_added)).thenReturn("Contact Added");

        presenter.setEvent(new MainContract.MainEvent.OnQrScanComplete(TestData.testScannedCard));

        verify(mockSaveCard).invoke(TestData.testScannedCard);

        testEffectObserver.assertValue(new MainContract.MainEffect.ShowMessage("Contact Added"));
    }

    @Test
    public void createCard_dbFailure_emitsError() {
        when(mockSaveCard.invoke(TestData.testScannedCard)).thenReturn(Completable.error(TestData.sqlException));

        presenter.setEvent(new MainContract.MainEvent.OnQrScanComplete(TestData.testScannedCard));

        verify(mockSaveCard).invoke(TestData.testScannedCard);

        testEffectObserver.assertValue(new MainContract.MainEffect.ShowMessage(TestData.sqlException.getLocalizedMessage()));
    }
}
