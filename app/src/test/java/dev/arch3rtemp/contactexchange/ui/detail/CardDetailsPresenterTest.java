package dev.arch3rtemp.contactexchange.ui.detail;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import dev.arch3rtemp.contactexchange.TestData;
import dev.arch3rtemp.contactexchange.db.ContactDao;
import dev.arch3rtemp.contactexchange.rx.TestSchedulerProvider;
import dev.arch3rtemp.contactexchange.ui.mapper.ContactToUiMapper;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public class CardDetailsPresenterTest {

    @Rule
    public MockitoRule mockRule = MockitoJUnit.rule();

    @Mock
    private ContactDao mockContactDao;
    @Mock
    private ContactToUiMapper mockMapper;
    @Mock
    private CardDetailsContract.View mockView;
    private CardDetailsPresenter presenter;

    @Before
    public void setUp() {
        var schedulers = new TestSchedulerProvider();
        presenter = new CardDetailsPresenter(mockContactDao, schedulers, mockMapper);
        presenter.onCreate(mockView);
    }

    @After
    public void tearDown() {
        presenter.onDestroy();
    }

    @Test
    public void loadCard_success() {
        when(mockContactDao.selectContactById(TestData.testMyContact.getId())).thenReturn(Observable.just(TestData.testMyContact));
        when(mockMapper.toUi(TestData.testMyContact)).thenReturn(TestData.testMyContactUi);

        presenter.getContactById(TestData.testMyContact.getId());

        verify(mockView).onCardLoaded(TestData.testMyContactUi);
        verify(mockMapper).toUi(TestData.testMyContact);
        verify(mockView).onCardLoaded(TestData.testMyContactUi);
    }

    @Test
    public void loadCard_dbFailure_error() {
        when(mockContactDao.selectContactById(TestData.testMyContact.getId())).thenReturn(Observable.error(TestData.sqlException));

        presenter.getContactById(TestData.testMyContact.getId());

        verify(mockContactDao).selectContactById(TestData.testMyContact.getId());
        verify(mockView).showMessage(TestData.sqlException.getLocalizedMessage());
    }

    @Test
    public void deleteCard_success() {
        when(mockContactDao.delete(TestData.testMyContact.getId())).thenReturn(Completable.complete());

        presenter.deleteContact(TestData.testMyContact.getId());

        verify(mockContactDao).delete(TestData.testMyContact.getId());
        verify(mockView).animateDeletion();
    }

    @Test
    public void deleteCard_dbFailure_error() {
        when(mockContactDao.delete(TestData.testMyContact.getId())).thenReturn(Completable.error(TestData.sqlException));

        presenter.deleteContact(TestData.testMyContact.getId());

        verify(mockContactDao).delete(TestData.testMyContact.getId());
        verify(mockView).showMessage(TestData.sqlException.getLocalizedMessage());
    }
}
