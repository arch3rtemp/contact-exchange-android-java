package dev.arch3rtemp.ui.base;

public class TestPresenter extends BasePresenter<String, String, String> {
    @Override
    protected String createInitialState() {
        return INITIAL_STATE;
    }

    @Override
    protected void handleEvent(String event) {
        setState(current -> HANDLED_EVENT + event);
    }

    void triggerEffect(String effect) {
        setEffect(() -> effect);
    }

    final static String INITIAL_STATE = "Initial State";
    final static String HANDLED_EVENT = "Handled Event: ";
}
