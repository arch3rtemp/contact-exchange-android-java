package dev.arch3rtemp.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

/**
 * A base fragment class using RxJava for reactive state/effect.
 *
 * @param <Event>  Type representing UI events
 * @param <Effect> Type representing one-time UI effects (like showing a message)
 * @param <State>  Type representing the UI state
 * @param <VB>     Type of the ViewBinding class for this fragment's layout
 * @param <P>     Type of the Presenter providing state & effect streams
 */
public abstract class BaseFragment<Event extends UiEvent, Effect extends UiEffect, State extends UiState, VB extends ViewBinding, P extends BasePresenter<Event, Effect, State>>
        extends Fragment {

    private VB binding;
    private final CompositeDisposable disposables = new CompositeDisposable();

    /**
     * A function that inflates the view binding.
     */
    protected abstract VB bindLayout(LayoutInflater inflater, ViewGroup container, boolean attachToRoot);

    /**
     * Provides the Presenter instance.
     */
    protected abstract P getPresenter();

    /**
     * Prepares the UI after the view is created. Similar to onViewCreated in Kotlin version.
     */
    protected abstract void prepareView(@Nullable Bundle savedInstanceState);

    /**
     * Renders the UI state. Called whenever the state changes.
     */
    protected abstract void renderState(State state);

    /**
     * Handles one-time effects emitted by the Presenter. Called whenever a new effect is emitted.
     */
    protected abstract void renderEffect(Effect effect);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = bindLayout(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareView(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Subscribe to state changes
        // Handle error if needed
        var stateDisposable = getPresenter().stateStream()
                .subscribe(
                        this::renderState,
                        Throwable::printStackTrace
                );
        disposables.add(stateDisposable);

        // Subscribe to effects
        // Handle error if needed
        var effectDisposable = getPresenter().effectStream()
                .subscribe(
                        this::renderEffect,
                        Throwable::printStackTrace
                );
        disposables.add(effectDisposable);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Clear all subscriptions to avoid memory leaks and stop listening when not visible
        disposables.clear();
    }

    protected VB getBinding() {
        return binding;
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        getPresenter().destroy();
        super.onDetach();
    }
}
