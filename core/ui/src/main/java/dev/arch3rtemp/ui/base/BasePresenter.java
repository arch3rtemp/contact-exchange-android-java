package dev.arch3rtemp.ui.base;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.core.Observable;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class BasePresenter<Event, Effect, State> {

    private final BehaviorSubject<State> stateSubject;
    private final PublishSubject<Event> eventSubject = PublishSubject.create();
    private final PublishSubject<Effect> effectSubject = PublishSubject.create();
    protected final CompositeDisposable disposables = new CompositeDisposable();

    public BasePresenter() {
        stateSubject = BehaviorSubject.createDefault(createInitialState());
        subscribeToEvents();
    }

    /**
     * Create the initial state for the presenter.
     */
    protected abstract State createInitialState();

    /**
     * Handle incoming Events and update State or produce Effects.
     */
    protected abstract void handleEvent(Event event);

    /**
     * Subscribe to incoming events from the eventSubject.
     */
    private void subscribeToEvents() {
        disposables.add(
                eventSubject.subscribe(
                        this::handleEvent,
                        throwable -> {
                            // Error handling if needed
                        }
                )
        );
    }

    /**
     * Observable for the current state. The view should subscribe to this to render UI.
     * It will immediately emit the last known state to new subscribers.
     */
    public Observable<State> stateStream() {
        return stateSubject.distinctUntilChanged();
    }

    /**
     * Observable for one-time effects. The view should subscribe to this for transient actions.
     */
    public Observable<Effect> effectStream() {
        return effectSubject.hide();
    }

    /**
     * Access the current state synchronously (if needed).
     */
    public State getCurrentState() {
        return stateSubject.getValue();
    }

    /**
     * Called by the View (or Controller) to send user events (UiEvents) to the Presenter.
     */
    public void setEvent(Event event) {
        eventSubject.onNext(event);
    }

    /**
     * Update the current state using a reducer function.
     * The reducer receives the current state and returns a new, modified state.
     */
    protected void setState(Function<State, State> reducer) {
        State current = getCurrentState();
        if (current != null) {
            State newState = reducer.apply(current);
            stateSubject.onNext(newState);
        }
    }

    /**
     * Emit a one-time effect. The View should subscribe and handle these effects as they arrive.
     */
    protected void setEffect(Supplier<Effect> effectSupplier) {
        Effect effect = effectSupplier.get();
        effectSubject.onNext(effect);
    }

    /**
     * Clean up resources when the Presenter is no longer needed.
     * Call this from the View's onDestroy or similar lifecycle method.
     */
    public void destroy() {
        disposables.clear();
    }
}
