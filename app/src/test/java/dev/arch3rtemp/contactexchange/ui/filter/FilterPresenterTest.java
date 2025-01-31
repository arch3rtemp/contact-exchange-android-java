package dev.arch3rtemp.contactexchange.ui.filter;

import static org.junit.Assert.assertEquals;
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
import dev.arch3rtemp.contactexchange.db.ContactDao;
import dev.arch3rtemp.contactexchange.rx.TestSchedulerProvider;
import dev.arch3rtemp.contactexchange.ui.mapper.ContactToUiMapper;
import dev.arch3rtemp.ui.util.StringResourceManager;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public class FilterPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ContactDao mockContactDao;
    @Mock
    private ContactToUiMapper mockMapper;
    @Mock
    private StringResourceManager mockStringManager;
    @Mock
    private FilterContract.View mockView;

    private FilterPresenter presenter;

    @Before
    public void setUp() {
        var schedulers = new TestSchedulerProvider();
        presenter = new FilterPresenter(mockContactDao, schedulers, mockMapper, mockStringManager);
        presenter.onCreate(mockView);
    }

    @After
    public void tearDown() {
        presenter.onDestroy();
    }

    @Test
    public void loadScannedCards_success() {
        when(mockContactDao.selectScannedContacts()).thenReturn(Observable.just(TestData.testContacts));
        when(mockMapper.toUiList(TestData.testContacts)).thenReturn(TestData.testContactsUi);

        presenter.getContacts();

        verify(mockContactDao).selectScannedContacts();
        verify(mockMapper).toUiList(TestData.testContacts);
        verify(mockView).onGetContacts(TestData.testContactsUi);
    }

    @Test
    public void loadScannedCards_error() {
        when(mockContactDao.selectScannedContacts()).thenReturn(Observable.error(TestData.sqlException));

        presenter.getContacts();

        verify(mockContactDao).selectScannedContacts();
        verify(mockView).showMessage(TestData.sqlException.getLocalizedMessage());
    }

    @Test
    public void deleteContact_success() {
        when(mockContactDao.delete(TestData.testMyContact.getId())).thenReturn(Completable.complete());
        when(mockStringManager.string(R.string.msg_contact_deleted)).thenReturn("Contact deleted");

        presenter.deleteContact(TestData.testMyContact.getId());

        verify(mockContactDao).delete(TestData.testMyContact.getId());
        verify(mockView).showMessage("Contact deleted");
    }

    @Test
    public void deleteContact_error() {
        when(mockContactDao.delete(TestData.testMyContact.getId())).thenReturn(Completable.error(TestData.sqlException));

        presenter.deleteContact(TestData.testMyContact.getId());

        verify(mockContactDao).delete(TestData.testMyContact.getId());
        verify(mockView).showMessage(TestData.sqlException.getLocalizedMessage());
    }

    @Test
    public void filterContacts_success() {
        when(mockContactDao.selectScannedContacts()).thenReturn(Observable.just(TestData.testContacts));
        when(mockMapper.toUiList(TestData.testContacts)).thenReturn(TestData.testContactsUi);

        var query = "Jo";

        presenter.getContacts();
        var filtered = presenter.filterContacts(query);

        assertEquals(1, filtered.size());
        assertEquals(TestData.testContactsUi.get(0), filtered.get(0));
    }

    @Test
    public void filterContacts_empty() {
        when(mockContactDao.selectScannedContacts()).thenReturn(Observable.just(TestData.testContacts));
        when(mockMapper.toUiList(TestData.testContacts)).thenReturn(TestData.testContactsUi);

        var query = "Justin";

        presenter.getContacts();
        var filtered = presenter.filterContacts(query);

        assertEquals(0, filtered.size());
    }
}
