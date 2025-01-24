package dev.arch3rtemp.contactexchange.db;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import dev.arch3rtemp.contactexchange.TestData;

@RunWith(RobolectricTestRunner.class)
public class ContactDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AppDatabase testDb;
    private ContactDao contactDao;

    @Before
    public void setUp() {
        testDb = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().getContext(),
                AppDatabase.class
        ).allowMainThreadQueries().build();

        contactDao = testDb.contactDao();
        initDb();
    }

    private void initDb() {
        contactDao.insert(TestData.testScannedContact).blockingAwait();
        contactDao.insert(TestData.testMyContact).blockingAwait();
    }

    @After
    public void tearDown() {
        testDb.close();
    }

    @Test
    public void selectScannedContacts_returnsValidData() {
        contactDao.selectScannedContacts()
                .test()
                .assertValueCount(1)
                .assertValue(cardEntities ->
                        cardEntities.get(0).equals(TestData.testScannedContact)
                );
    }

    @Test
    public void selectMyContacts_returnsValidData() {
        contactDao.selectMyCards()
                .test()
                .assertValueCount(1)
                .assertValue(cardEntities ->
                        cardEntities.get(0).equals(TestData.testMyContact)
                );
    }

    @Test
    public void selectContactById_returnsValidData() {
        contactDao.selectContactById(TestData.testMyContact.getId())
                .test()
                .assertValue(TestData.testMyContact);
    }

    @Test
    public void updateAndRetrieve_returnsComplete() {
        contactDao.update(TestData.mergedContact).blockingAwait();
        contactDao.selectContactById(TestData.mergedContact.getId())
                .test()
                .assertValue(TestData.mergedContact);
    }

    @Test
    public void deleteAndCheck_returnsComplete() {
        contactDao.delete(TestData.testMyContact.getId()).blockingAwait();
        contactDao.selectContactById(TestData.testMyContact.getId())
                .test()
                .assertNoValues();
    }
}
