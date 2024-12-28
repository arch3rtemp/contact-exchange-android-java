package dev.arch3rtemp.contactexchange.presentation.home;

import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.domain.usecase.DeleteCardUseCase;
import dev.arch3rtemp.contactexchange.domain.usecase.FilterCardsUseCase;
import dev.arch3rtemp.contactexchange.domain.usecase.GetMyCardsUseCase;
import dev.arch3rtemp.contactexchange.domain.usecase.GetScannedCardsUseCase;
import dev.arch3rtemp.contactexchange.domain.usecase.SaveCardUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dev.arch3rtemp.ui.base.BasePresenter;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenter extends BasePresenter<HomeContract.HomeEvent, HomeContract.HomeEffect, HomeContract.HomeState> {

    private final GetMyCardsUseCase getMyCards;
    private final GetScannedCardsUseCase getScannedCards;
    private final DeleteCardUseCase deleteCard;
    private final SaveCardUseCase saveCard;
    private final FilterCardsUseCase filterCards;
    private List<Card> unfilteredContacts = new ArrayList<>();

    @Inject
    public HomePresenter(GetMyCardsUseCase getMyCards, GetScannedCardsUseCase getScannedCards, DeleteCardUseCase deleteCard, SaveCardUseCase saveCard, FilterCardsUseCase filterCards) {
        this.getMyCards = getMyCards;
        this.getScannedCards = getScannedCards;
        this.deleteCard = deleteCard;
        this.saveCard = saveCard;
        this.filterCards = filterCards;
    }

    @Override
    protected HomeContract.HomeState createInitialState() {
        return new HomeContract.HomeState(new HomeContract.ViewState.Idle(), new HomeContract.ViewState.Idle(), "");
    }

    @Override
    protected void handleEvent(HomeContract.HomeEvent homeEvent) {
        if (homeEvent instanceof HomeContract.HomeEvent.OnCardsLoad) {
            getMyCards();
        } else if (homeEvent instanceof HomeContract.HomeEvent.OnContactsLoad) {
            getScannedCards();
            subscribeToFilter();
        } else if (homeEvent instanceof HomeContract.HomeEvent.OnContactDeleted onContactDeleted) {
            deleteCard(onContactDeleted.card());
        } else if (homeEvent instanceof HomeContract.HomeEvent.OnContactSaved onContactSaved) {
            saveCard(onContactSaved.card());
        } else if (homeEvent instanceof HomeContract.HomeEvent.OnSearchTyped onSearchTyped) {
            filterCards(onSearchTyped.query(), unfilteredContacts);
        }
    }

    private void getMyCards() {
        var disposable = getMyCards.invoke()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cards -> {
                    if (cards.isEmpty()) {
                        setState(currentState -> new HomeContract.HomeState(
                                new HomeContract.ViewState.Empty(),
                                getCurrentState().scannedCards(),
                                getCurrentState().query()
                        ));
                    } else {
                        setState(currentState -> new HomeContract.HomeState(
                                new HomeContract.ViewState.Success(cards),
                                getCurrentState().scannedCards(),
                                getCurrentState().query()
                        ));
                    }
                }, throwable -> {
                    setState(currentState -> new HomeContract.HomeState(
                            new HomeContract.ViewState.Error(throwable.getLocalizedMessage()),
                            getCurrentState().scannedCards(),
                            getCurrentState().query()
                    ));
                    setEffect(() -> new HomeContract.HomeEffect.ShowError(throwable.getLocalizedMessage()));
                });

        disposables.add(disposable);
    }

    private void getScannedCards() {
        var disposable = getScannedCards.invoke()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts -> {
                    unfilteredContacts = contacts;
                    if (contacts.isEmpty()) {
                        setState(currentState -> new HomeContract.HomeState(
                                getCurrentState().myCards(),
                                new HomeContract.ViewState.Empty(),
                                getCurrentState().query()
                        ));
                    } else {
                        setState(currentState -> new HomeContract.HomeState(
                                getCurrentState().myCards(),
                                new HomeContract.ViewState.Success(contacts),
                                getCurrentState().query()
                        ));
                    }
                }, throwable -> {
                    setState(currentState -> new HomeContract.HomeState(
                            getCurrentState().myCards(),
                            new HomeContract.ViewState.Error(throwable.getLocalizedMessage()),
                            getCurrentState().query()
                    ));
                    setEffect(() -> new HomeContract.HomeEffect.ShowError(throwable.getLocalizedMessage()));
                });

        disposables.add(disposable);
    }

    private void saveCard(Card card) {
        var disposable = saveCard.invoke(card)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    // TODO
                }, throwable -> {
                    setEffect(() -> new HomeContract.HomeEffect.ShowError(throwable.getLocalizedMessage()));
                });

        disposables.add(disposable);
    }

    private void deleteCard(Card card) {
        var disposable = deleteCard.invoke(card.id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    setEffect(() -> new HomeContract.HomeEffect.ShowUndo(card));
                }, throwable -> {
                    setEffect(() -> new HomeContract.HomeEffect.ShowError(throwable.getLocalizedMessage()));
                });

        disposables.add(disposable);
    }

    private void subscribeToFilter() {
        var disposable = this.filterCards.init()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cards -> {
                    if (cards.isEmpty()) {
                        setState(currentState -> new HomeContract.HomeState(
                                getCurrentState().myCards(),
                                new HomeContract.ViewState.Empty(),
                                getCurrentState().query()
                        ));
                    } else {
                        setState(currentState -> new HomeContract.HomeState(
                                getCurrentState().myCards(),
                                new HomeContract.ViewState.Success(cards),
                                getCurrentState().query()
                        ));
                    }
                }, throwable -> {
                    setEffect(() -> new HomeContract.HomeEffect.ShowError(throwable.getLocalizedMessage()));
                });

        disposables.add(disposable);
    }

    private void filterCards(String query, List<Card> unfiltered) {
        filterCards.invoke(query, unfiltered);
    }
}
