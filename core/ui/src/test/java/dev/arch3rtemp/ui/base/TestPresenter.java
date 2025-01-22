package dev.arch3rtemp.ui.base;

public class TestPresenter extends BasePresenter<TestEvent, TestEffect, TestState> {
    @Override
    protected TestState createInitialState() {
        return new TestState(INITIAL_STATE);
    }

    @Override
    protected void handleEvent(TestEvent event) {
        setState(current -> new TestState(HANDLED_EVENT + event.event()));
    }

    void triggerEffect(TestEffect effect) {
        setEffect(() -> effect);
    }

    final static String INITIAL_STATE = "Initial State";
    final static String HANDLED_EVENT = "Handled Event: ";


}
