package dev.arch3rtemp.contactexchange.presentation.card;

import dev.arch3rtemp.contactexchange.domain.usecase.DeleteCardUseCase;
import dev.arch3rtemp.contactexchange.domain.usecase.GetCardByIdUseCase;

import javax.inject.Inject;

import dev.arch3rtemp.ui.base.BasePresenter;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CardPresenter extends BasePresenter<CardContract.CardEvent, CardContract.CardEffect, CardContract.CardState> {

    private final DeleteCardUseCase deleteCard;
    private final GetCardByIdUseCase getCardById;

    @Inject
    public CardPresenter(DeleteCardUseCase deleteCard, GetCardByIdUseCase getCardById) {
        this.deleteCard = deleteCard;
        this.getCardById = getCardById;
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((card) -> {
                    setState((Card) -> new CardContract.CardState.Success(card));
                }, throwable -> {
                    setState((String) -> new CardContract.CardState.Error());
                    setEffect(() -> new CardContract.CardEffect.ShowError(throwable.getLocalizedMessage()));
                });
        disposables.add(disposable);
    }

    private void deleteCard(int id) {
        var disposable = deleteCard.invoke(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> setEffect(CardContract.CardEffect.AnimateDeletion::new), throwable ->
                        new CardContract.CardEffect.ShowError(throwable.getLocalizedMessage()));
        disposables.add(disposable);
    }
}
