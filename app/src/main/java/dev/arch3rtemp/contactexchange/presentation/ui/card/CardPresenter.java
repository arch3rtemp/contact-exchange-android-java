package dev.arch3rtemp.contactexchange.presentation.ui.card;

import dev.arch3rtemp.contactexchange.domain.usecase.DeleteCardUseCase;
import dev.arch3rtemp.contactexchange.domain.usecase.GetCardByIdUseCase;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.domain.util.SchedulerProvider;
import dev.arch3rtemp.contactexchange.presentation.mapper.CardUiMapper;
import dev.arch3rtemp.ui.base.BasePresenter;

public class CardPresenter extends BasePresenter<CardContract.CardEvent, CardContract.CardEffect, CardContract.CardState> {

    private final GetCardByIdUseCase getCardById;
    private final DeleteCardUseCase deleteCard;
    private final CardUiMapper mapper;
    private final SchedulerProvider schedulerProvider;

    @Inject
    public CardPresenter(GetCardByIdUseCase getCardById, DeleteCardUseCase deleteCard, CardUiMapper mapper, SchedulerProvider schedulerProvider) {
        this.getCardById = getCardById;
        this.deleteCard = deleteCard;
        this.mapper = mapper;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    protected CardContract.CardState createInitialState() {
        return new CardContract.CardState.Idle();
    }

    @Override
    protected void handleEvent(CardContract.CardEvent cardEvent) {
        if (cardEvent instanceof CardContract.CardEvent.OnCardDelete onCardDelete) {
            deleteCard(onCardDelete.id());
        } else if (cardEvent instanceof CardContract.CardEvent.OnCardLoad onCardLoad) {
            getCard(onCardLoad.id());
        }
    }

    private void getCard(int id) {
        var disposable = getCardById.invoke(id)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .doOnError(throwable -> {
                    setEffect(() -> new CardContract.CardEffect.ShowError(throwable.getLocalizedMessage()));
                })
                .doOnSubscribe(subscription -> setState(current -> new CardContract.CardState.Loading()))
                .subscribe((card) -> {
                    setState(current -> new CardContract.CardState.Success(mapper.toUiModel(card)));
                }, throwable -> {
                    setState(current -> new CardContract.CardState.Error());
                });
        disposables.add(disposable);
    }

    private void deleteCard(int id) {
        var disposable = deleteCard.invoke(id)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .subscribe(
                        () -> setEffect(CardContract.CardEffect.AnimateDeletion::new),
                        throwable -> setEffect(() -> new CardContract.CardEffect.ShowError(throwable.getLocalizedMessage()))
                );
        disposables.add(disposable);
    }
}
