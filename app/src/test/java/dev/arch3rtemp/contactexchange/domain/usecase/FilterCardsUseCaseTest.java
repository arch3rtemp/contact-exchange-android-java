package dev.arch3rtemp.contactexchange.domain.usecase;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dev.arch3rtemp.contactexchange.rx.RxTestSchedulerRule;
import dev.arch3rtemp.contactexchange.TestData;
import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.rx.TestSchedulerProvider;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.schedulers.TestScheduler;

public class FilterCardsUseCaseTest {

    @Rule
    public final RxTestSchedulerRule rxTestSchedulerRule = new RxTestSchedulerRule();

    private FilterCardsUseCase filterCards;
    private TestScheduler testScheduler;
    private TestObserver<List<Card>> testObserver;

    @Before
    public void setUp() {
        testScheduler = rxTestSchedulerRule.getTestScheduler();
        filterCards = new FilterCardsUseCase(new TestSchedulerProvider());
        testObserver = filterCards.getFilteredCardsStream().test();
    }

    @Test
    public void checkWithDebounce_emitsOnTime() {

        // 1) First emission
        filterCards.invoke("First", Collections.emptyList());

        // Advance virtual time by only 299ms, just under the 300ms debounce
        testScheduler.advanceTimeBy(299, TimeUnit.MILLISECONDS);

        // At this point, we expect NO emissions yet, because we haven't hit 300ms
        testObserver.assertNoValues();

        // 2) Advance 1 more millisecond to reach the 300ms debounce threshold
        testScheduler.advanceTimeBy(1, TimeUnit.MILLISECONDS);

        // Now we expect 1 emission
        testObserver.assertValueCount(1);
        testObserver.assertValue(Collections.emptyList());
    }

    @Test
    public void distinctUntilChanged_preventsDuplicateEmissions() {

        // 1) Emit "query" + list
        filterCards.invoke("jane", TestData.testCards);
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS);
        testObserver.assertValueCount(1);

        // 2) Emit the same "query" + same list -> no new emission
        filterCards.invoke("jane", TestData.testCards);
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS);

        // Still only 1 emission because distinctUntilChanged() sees same query & list
        testObserver.assertValueCount(1);
        testObserver.assertValue(List.of(TestData.testScannedCard));
    }

    @Test
    public void filtersCards_correctly() {
        // 1) Query = "al" -> Should match "Alice", "Alana", and "alex"
        filterCards.invoke("ja", TestData.testCards);

        // Advance time so the debounced emission goes through
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS);

        // We should have 1 emission
        testObserver.assertValueCount(1);

        // Check that it actually filtered as expected
        var filteredResult = testObserver.values().get(0);
        assertEquals(1, filteredResult.size());
        assertEquals(TestData.testScannedCard, filteredResult.get(0));
    }
}
