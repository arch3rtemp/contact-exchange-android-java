package dev.arch3rtemp.contactexchange.presentation.ui.create;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.domain.usecase.SaveCardUseCase;

import javax.inject.Inject;

import dev.arch3rtemp.ui.base.BasePresenter;
import dev.arch3rtemp.ui.util.StringResourceManager;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CreateCardPresenter extends BasePresenter<CreateCardContract.CreateCardEvent, CreateCardContract.CreateCardEffect, CreateCardContract.CreateCardState> {
    private final SaveCardUseCase saveCard;
    private final StringResourceManager stringResourceManager;

    @Inject
    public CreateCardPresenter(SaveCardUseCase saveCard, StringResourceManager stringResourceManager) {
        this.saveCard = saveCard;
        this.stringResourceManager = stringResourceManager;
    }

    @Override
    protected CreateCardContract.CreateCardState createInitialState() {
        return new CreateCardContract.CreateCardState.Idle();
    }

    @Override
    protected void handleEvent(CreateCardContract.CreateCardEvent createCardEvent) {
        if (createCardEvent instanceof CreateCardContract.CreateCardEvent.OnCreateButtonPressed onCreateButtonPressed) {
            saveCard(onCreateButtonPressed.card());
        }
    }

    private void saveCard(Card card) {
        if (card.isNotBlank()) {
            var disposable = saveCard.invoke(card)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> setEffect(() -> new CreateCardContract.CreateCardEffect.ShowMessage(throwable.getLocalizedMessage())))
                    .doOnComplete(() -> setEffect(CreateCardContract.CreateCardEffect.NavigateOnSuccess::new))
                    .subscribe(
                            () -> setState(current -> new CreateCardContract.CreateCardState.Success()),
                            throwable -> setState(current -> new CreateCardContract.CreateCardState.Error())
                    );
            disposables.add(disposable);
        } else {
            setEffect(() -> new CreateCardContract.CreateCardEffect.ShowMessage(stringResourceManager.string(R.string.msg_all_fields_required)));
        }
    }
}
