package dev.arch3rtemp.contactexchange.presentation.ui.home;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.domain.usecase.DeleteCardUseCase;
import dev.arch3rtemp.contactexchange.domain.usecase.FilterCardsUseCase;
import dev.arch3rtemp.contactexchange.domain.usecase.GetMyCardsUseCase;
import dev.arch3rtemp.contactexchange.domain.usecase.GetScannedCardsUseCase;
import dev.arch3rtemp.contactexchange.domain.usecase.SaveCardUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.presentation.mapper.CardUiMapper;
import dev.arch3rtemp.contactexchange.presentation.model.CardUi;
import dev.arch3rtemp.ui.base.BasePresenter;
import dev.arch3rtemp.ui.util.StringResourceManager;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenter extends BasePresenter<HomeContract.HomeEvent, HomeContract.HomeEffect, HomeContract.HomeState> {

    private final GetMyCardsUseCase getMyCards;
    private final GetScannedCardsUseCase getScannedCards;
    private final DeleteCardUseCase deleteCard;
    private final SaveCardUseCase saveCard;
    private final FilterCardsUseCase filterCards;
    private final StringResourceManager resourceManager;
    private final CardUiMapper mapper;
    private List<Card> unfilteredContacts = new ArrayList<>();

    @Inject
    public HomePresenter(GetMyCardsUseCase getMyCards, GetScannedCardsUseCase getScannedCards, DeleteCardUseCase deleteCard, SaveCardUseCase saveCard, FilterCardsUseCase filterCards, StringResourceManager resourceManager, CardUiMapper mapper) {
        this.getMyCards = getMyCards;
        this.getScannedCards = getScannedCards;
        this.deleteCard = deleteCard;
        this.saveCard = saveCard;
        this.filterCards = filterCards;
        this.resourceManager = resourceManager;
        this.mapper = mapper;
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
                .doOnError(throwable -> setEffect(() -> new HomeContract.HomeEffect.ShowError(throwable.getLocalizedMessage())))
                .subscribe(cards -> {
                    if (cards.isEmpty()) {
                        setState(currentState -> new HomeContract.HomeState(
                                new HomeContract.ViewState.Empty(),
                                getCurrentState().scannedCards(),
                                getCurrentState().query()
                        ));
                    } else {
                        setState(currentState -> new HomeContract.HomeState(
                                new HomeContract.ViewState.Success(mapper.toUiModelList(cards)),
                                getCurrentState().scannedCards(),
                                getCurrentState().query()
                        ));
                    }
                }, throwable -> {
                    setState(currentState -> new HomeContract.HomeState(
                            new HomeContract.ViewState.Error(resourceManager.string(R.string.msg_could_not_load_data)),
                            getCurrentState().scannedCards(),
                            getCurrentState().query()
                    ));
                });

        disposables.add(disposable);
    }

    private void getScannedCards() {
        var disposable = getScannedCards.invoke()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> setEffect(() -> new HomeContract.HomeEffect.ShowError(throwable.getLocalizedMessage())))
                .subscribe(contacts -> {
                    unfilteredContacts = contacts;
                    var uiModels = mapper.toUiModelList(contacts);
                    if (contacts.isEmpty()) {
                        setState(currentState -> new HomeContract.HomeState(
                                getCurrentState().myCards(),
                                new HomeContract.ViewState.Empty(),
                                getCurrentState().query()
                        ));
                    } else {
                        setState(currentState -> new HomeContract.HomeState(
                                getCurrentState().myCards(),
                                new HomeContract.ViewState.Success(uiModels),
                                getCurrentState().query()
                        ));
                    }
                }, throwable -> {
                    setState(currentState -> new HomeContract.HomeState(
                            getCurrentState().myCards(),
                            new HomeContract.ViewState.Error(resourceManager.string(R.string.msg_could_not_load_data)),
                            getCurrentState().query()
                    ));
                });

        disposables.add(disposable);
    }

    private void saveCard(CardUi card) {
        var disposable = saveCard.invoke(mapper.fromUiModel(card))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> setEffect(() -> new HomeContract.HomeEffect.ShowError(throwable.getLocalizedMessage())))
                .subscribe();

        disposables.add(disposable);
    }

    private void deleteCard(CardUi card) {
        var disposable = deleteCard.invoke(card.id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> setEffect(() -> new HomeContract.HomeEffect.ShowError(throwable.getLocalizedMessage())))
                .doOnComplete(() -> setEffect(() -> new HomeContract.HomeEffect.ShowUndo(card)))
                .subscribe();

        disposables.add(disposable);
    }

    private void subscribeToFilter() {
        var disposable = this.filterCards.getFilteredCardsStream()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> setEffect(() -> new HomeContract.HomeEffect.ShowError(throwable.getLocalizedMessage())))
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
                                new HomeContract.ViewState.Success(mapper.toUiModelList(cards)),
                                getCurrentState().query()
                        ));
                    }
                });

        disposables.add(disposable);
    }

    private void filterCards(String query, List<Card> unfiltered) {
        filterCards.invoke(query, unfiltered);
    }
}
