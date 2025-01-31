package dev.arch3rtemp.contactexchange.ui.home;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static java.util.Collections.emptyList;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.TestData;
import dev.arch3rtemp.contactexchange.db.ContactDao;
import dev.arch3rtemp.contactexchange.rx.TestSchedulerProvider;
import dev.arch3rtemp.contactexchange.ui.mapper.ContactToUiMapper;
import dev.arch3rtemp.ui.util.StringResourceManager;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public class HomePresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ContactDao mockContactDao;
    @Mock
    private ContactToUiMapper mockMapper;
    @Mock
    private StringResourceManager mockStringManager;
    @Mock
    private HomeContract.View mockView;
    private HomePresenter presenter;

    @Before
    public void setUp() {
        var schedulers = new TestSchedulerProvider();
        presenter = new HomePresenter(mockContactDao, schedulers, mockMapper, mockStringManager);
        presenter.onCreate(mockView);
    }

    @After
    public void tearDown() {
        presenter.onDestroy();
    }

    @Test
    public void loadMyCards_success() {
        when(mockContactDao.selectMyCards()).thenReturn(Observable.just(TestData.testContacts));
        when(mockMapper.toUiList(TestData.testContacts)).thenReturn(TestData.testContactsUi);

        presenter.getMyCards();

        verify(mockContactDao).selectMyCards();
        verify(mockMapper).toUiList(TestData.testContacts);
        verify(mockView).onCardsResult(new HomeContract.ViewState.Success(TestData.testContactsUi));
    }

    @Test
    public void loadMyCards_error() {
        when(mockContactDao.selectMyCards()).thenReturn(Observable.error(TestData.sqlException));
        when(mockStringManager.string(R.string.msg_could_not_load_data)).thenReturn("Could not load data");

        presenter.getMyCards();

        verify(mockContactDao).selectMyCards();
        verify(mockStringManager).string(R.string.msg_could_not_load_data);
        verify(mockView).onCardsResult(new HomeContract.ViewState.Error("Could not load data"));
        verify(mockView).showMessage(TestData.sqlException.getLocalizedMessage());
    }

    @Test
    public void loadMyCards_empty() {
        when(mockContactDao.selectMyCards()).thenReturn(Observable.just(emptyList()));

        presenter.getMyCards();

        verify(mockContactDao).selectMyCards();
        verify(mockView).onCardsResult(new HomeContract.ViewState.Empty());
    }

    @Test
    public void loadScannedContacts_success() {
        when(mockContactDao.selectScannedContacts()).thenReturn(Observable.just(TestData.testContacts));
        when(mockMapper.toUiList(TestData.testContacts)).thenReturn(TestData.testContactsUi);

        presenter.getContacts();

        verify(mockContactDao).selectScannedContacts();
        verify(mockMapper).toUiList(TestData.testContacts);
        verify(mockView).onContactsResult(new HomeContract.ViewState.Success(TestData.testContactsUi));
    }

    @Test
    public void loadScannedContacts_error() {
        when(mockContactDao.selectScannedContacts()).thenReturn(Observable.error(TestData.sqlException));
        when(mockStringManager.string(R.string.msg_could_not_load_data)).thenReturn("Could not load data");

        presenter.getContacts();

        verify(mockContactDao).selectScannedContacts();
        verify(mockStringManager).string(R.string.msg_could_not_load_data);
        verify(mockView).onContactsResult(new HomeContract.ViewState.Error("Could not load data"));
    }

    @Test
    public void loadScannedContacts_empty() {
        when(mockContactDao.selectScannedContacts()).thenReturn(Observable.just(emptyList()));

        presenter.getContacts();

        verify(mockContactDao).selectScannedContacts();
        verify(mockView).onContactsResult(new HomeContract.ViewState.Empty());
    }

    @Test
    public void deleteContact_success() {
        when(mockContactDao.delete(TestData.testMyContact.getId())).thenReturn(Completable.complete());
        when(mockStringManager.string(R.string.msg_contact_deleted)).thenReturn("Contact deleted");

        presenter.deleteContact(TestData.testMyContactUi);

        verify(mockContactDao).delete(TestData.testMyContact.getId());
        verify(mockView).onContactDeleted(TestData.testMyContactUi, "Contact deleted");
    }

    @Test
    public void deleteContact_error() {
        when(mockContactDao.delete(TestData.testMyContact.getId())).thenReturn(Completable.error(TestData.sqlException));

        presenter.deleteContact(TestData.testMyContactUi);

        verify(mockContactDao).delete(TestData.testMyContact.getId());
        verify(mockView).showMessage(TestData.sqlException.getLocalizedMessage());
    }

    @Test
    public void saveContact_success() {
        when(mockMapper.fromUi(TestData.testMyContactUi)).thenReturn(TestData.testMyContact);
        when(mockContactDao.insert(TestData.testMyContact)).thenReturn(Completable.complete());

        presenter.saveContact(TestData.testMyContactUi);

        verify(mockContactDao).insert(TestData.testMyContact);
    }

    @Test
    public void saveContact_error() {
        when(mockMapper.fromUi(TestData.testMyContactUi)).thenReturn(TestData.testMyContact);
        when(mockContactDao.insert(TestData.testMyContact)).thenReturn(Completable.error(TestData.sqlException));

        presenter.saveContact(TestData.testMyContactUi);

        verify(mockContactDao).insert(TestData.testMyContact);
        verify(mockView).showMessage(TestData.sqlException.getLocalizedMessage());
    }
}
