package dev.arch3rtemp.contactexchange.presentation.ui.create;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.domain.usecase.SaveCardUseCase;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.domain.usecase.ValidateCardUseCase;
import dev.arch3rtemp.contactexchange.domain.util.SchedulerProvider;
import dev.arch3rtemp.ui.base.BasePresenter;
import dev.arch3rtemp.ui.util.StringResourceManager;

public class CreateCardPresenter extends BasePresenter<CreateCardContract.CreateCardEvent, CreateCardContract.CreateCardEffect, CreateCardContract.CreateCardState> {
    private final SaveCardUseCase saveCard;
    private final ValidateCardUseCase validateCard;
    private final SchedulerProvider schedulerProvider;
    private final StringResourceManager stringResourceManager;

    @Inject
    public CreateCardPresenter(SaveCardUseCase saveCard, ValidateCardUseCase validateCard, SchedulerProvider schedulerProvider, StringResourceManager stringResourceManager) {
        this.saveCard = saveCard;
        this.validateCard = validateCard;
        this.schedulerProvider = schedulerProvider;
        this.stringResourceManager = stringResourceManager;
    }

    @Override
    protected CreateCardContract.CreateCardState createInitialState() {
        return new CreateCardContract.CreateCardState();
    }

    @Override
    protected void handleEvent(CreateCardContract.CreateCardEvent createCardEvent) {
        if (createCardEvent instanceof CreateCardContract.CreateCardEvent.OnCreateButtonPressed onCreateButtonPressed) {
            saveCard(onCreateButtonPressed.card());
        }
    }

    private void saveCard(Card card) {
        if (validateCard.invoke(card)) {
            var disposable = saveCard.invoke(card)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.main())
                    .subscribe(
                            () -> setEffect(CreateCardContract.CreateCardEffect.NavigateUp::new),
                            throwable -> setEffect(() -> new CreateCardContract.CreateCardEffect.ShowMessage(throwable.getLocalizedMessage()))
                    );
            disposables.add(disposable);
        } else {
            setEffect(() -> new CreateCardContract.CreateCardEffect.ShowMessage(stringResourceManager.string(R.string.msg_all_fields_required)));
        }
    }
}
