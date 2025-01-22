package dev.arch3rtemp.ui.base;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import io.reactivex.rxjava3.observers.TestObserver;

public class BasePresenterTest {

    private TestPresenter presenter;
    private TestObserver<TestState> testStateObserver;
    private TestObserver<TestEffect> testEffectObserver;

    @Before
    public void setUp() {
        presenter = new TestPresenter();
        testStateObserver = presenter.stateStream().test();
        testEffectObserver = presenter.effectStream().test();
    }

    @After
    public void tearDown() {
        presenter.destroy();
    }

    @Test
    public void testInitialState() {
        testStateObserver.assertValue(new TestState(TestPresenter.INITIAL_STATE));
    }

    @Test
    public void testSetEventAndHandleEvent() {
        presenter.setEvent(new TestEvent(TEST_EVENT));
        testStateObserver.assertValueCount(2)
                .assertValueAt(1, new TestState(TestPresenter.HANDLED_EVENT + TEST_EVENT));
    }

    @Test
    public void testSetEffect() {
        presenter.triggerEffect(new TestEffect(TEST_EFFECT));
        testEffectObserver.assertValue(new TestEffect(TEST_EFFECT));
    }

    @Test
    public void testDestroyClearsDisposables() {

        presenter.destroy();

        // Try to set new data
        presenter.setEvent(new TestEvent(TEST_EVENT));
        presenter.triggerEffect(new TestEffect(TEST_EFFECT));

        // Check if presenter is cleared
        testStateObserver.assertValueCount(1)
                .assertValueAt(0, state -> !Objects.equals(state, new TestState(TEST_EVENT)));
        testEffectObserver.assertNoValues();
    }

    private final static String TEST_EVENT = "Test Event";
    private final static String TEST_EFFECT = "Test Effect";
}
