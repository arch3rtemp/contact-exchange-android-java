package dev.arch3rtemp.contactexchange.data.db;

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
public class CardDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AppDatabase testDb;
    private CardDao cardDao;

    @Before
    public void setup() {
        testDb = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().getContext(),
                AppDatabase.class
        ).allowMainThreadQueries().build();

        cardDao = testDb.cardDao();
        initDb();
    }

    @After
    public void tearDown() {
        testDb.close();
    }

    @Test
    public void testSelectScannedCards_returnsValidData() {
        cardDao.selectScannedCards()
                .test()
                .assertValueCount(1)
                .assertValue(cardEntities ->
                        cardEntities.get(0).equals(TestData.testScannedCardEntity)
                );
    }

    @Test
    public void testSelectMyCards_returnsValidData() {
        cardDao.selectMyCards()
                .test()
                .assertValueCount(1)
                .assertValue(cardEntities ->
                        cardEntities.get(0).equals(TestData.testMyCardEntity)
                );
    }

    @Test
    public void testSelectCardById_returnsValidData() {
        cardDao.selectCardById(TestData.testMyCard.id())
                .test()
                .assertValue(TestData.testMyCardEntity);
    }

    @Test
    public void testUpdateAndRetrieve_returnsComplete() {
        cardDao.update(TestData.mergedCardEntity).blockingAwait();
        cardDao.selectCardById(TestData.mergedCardEntity.getId())
                .test()
                .assertValue(TestData.mergedCardEntity);
    }

    @Test
    public void testDeleteAndCheck_returnsComplete() {
        cardDao.delete(TestData.testMyCardEntity.getId()).blockingAwait();
        cardDao.selectCardById(TestData.testMyCardEntity.getId())
                .test()
                .assertNoValues();
    }

    private void initDb() {
        cardDao.insert(TestData.testScannedCardEntity).blockingAwait();
        cardDao.insert(TestData.testMyCardEntity).blockingAwait();
    }
}
