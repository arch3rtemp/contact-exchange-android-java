package dev.arch3rtemp.contactexchange.ui.createoredit;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
import dev.arch3rtemp.contactexchange.util.SchedulerProvider;
import dev.arch3rtemp.ui.util.StringResourceManager;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public class CreateOrEditCardPresenterTest {

    @Rule
    public MockitoRule mockRule = MockitoJUnit.rule();
    @Mock
    private ContactDao mockContactDao;
    @Mock
    private ContactToUiMapper mockMapper;
    @Mock
    private StringResourceManager mockStringManager;
    @Mock
    CreateOrEditCardContract.View mockView;
    private CreateOrEditCardPresenter presenter;

    @Before
    public void setUp() {
        SchedulerProvider schedulers = new TestSchedulerProvider();
        presenter = new CreateOrEditCardPresenter(mockContactDao, schedulers, mockMapper, mockStringManager);
        presenter.onCreate(mockView);
    }

    @After
    public void tearDown() {
        presenter.onDestroy();
    }

    @Test
    public void createCard_withValidData_success() {
        when(mockContactDao.insert(TestData.testMyContact)).thenReturn(Completable.complete());
        when(mockStringManager.string(R.string.msg_contact_created)).thenReturn("Contact created");

        presenter.createContact(TestData.testMyContact);

        verify(mockContactDao).insert(TestData.testMyContact);
        verify(mockStringManager).string(R.string.msg_contact_created);
        verify(mockView).showMessage("Contact created");
        verify(mockView).navigateUp();
    }

    @Test
    public void createCard_withInvalidData_error() {
        when(mockStringManager.string(R.string.msg_all_fields_required)).thenReturn("All fields are required");

        presenter.createContact(TestData.blankContact);

        verify(mockStringManager).string(R.string.msg_all_fields_required);
        verify(mockView).showMessage("All fields are required");
        verifyNoMoreInteractions(mockContactDao);
    }

    @Test
    public void createCard_dbFailure_error() {
        when(mockContactDao.insert(TestData.testMyContact)).thenReturn(Completable.error(TestData.sqlException));

        presenter.createContact(TestData.testMyContact);

        verify(mockContactDao).insert(TestData.testMyContact);
        verify(mockView).showMessage(TestData.sqlException.getLocalizedMessage());
        verifyNoMoreInteractions(mockStringManager);
    }

    @Test
    public void loadCard_withValidId_success() {
        when(mockContactDao.selectContactById(TestData.testMyContact.getId())).thenReturn(Observable.just(TestData.testMyContact));
        when(mockMapper.toUi(TestData.testMyContact)).thenReturn(TestData.testMyContactUi);

        presenter.getContactById(TestData.testMyContact.getId());

        verify(mockContactDao).selectContactById(TestData.testMyContact.getId());
        verify(mockMapper).toUi(TestData.testMyContact);
        verify(mockView).onGetContactById(TestData.testMyContactUi);
    }

    @Test
    public void loadCard_dbFailure_error() {
        when(mockContactDao.selectContactById(TestData.testMyContact.getId())).thenReturn(Observable.error(TestData.sqlException));

        presenter.getContactById(TestData.testMyContact.getId());

        verify(mockContactDao).selectContactById(TestData.testMyContact.getId());
        verify(mockView).showMessage(TestData.sqlException.getLocalizedMessage());
    }

    @Test
    public void updateCard_withValidData_success() {
        when(mockContactDao.update(TestData.mergedContact)).thenReturn(Completable.complete());
        when(mockStringManager.string(R.string.msg_contact_updated)).thenReturn("Contact updated");

        presenter.editContact(TestData.mergedContact);

        verify(mockContactDao).update(TestData.mergedContact);
        verify(mockStringManager).string(R.string.msg_contact_updated);
        verify(mockView).showMessage("Contact updated");
        verify(mockView).navigateUp();
    }
}
