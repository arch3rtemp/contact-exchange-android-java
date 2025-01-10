package dev.arch3rtemp.contactexchange.presentation.ui.edit;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.domain.usecase.GetCardByIdUseCase;
import dev.arch3rtemp.contactexchange.domain.usecase.UpdateCardUseCase;
import dev.arch3rtemp.contactexchange.domain.usecase.ValidateCardUseCase;
import dev.arch3rtemp.contactexchange.domain.util.SchedulerProvider;
import dev.arch3rtemp.contactexchange.presentation.mapper.CardUiMapper;

import javax.inject.Inject;

import dev.arch3rtemp.ui.base.BasePresenter;
import dev.arch3rtemp.ui.util.StringResourceManager;

public class EditCardPresenter extends BasePresenter<EditCardContract.EditCardEvent, EditCardContract.EditCardEffect, EditCardContract.EditCardState> {

    private final GetCardByIdUseCase getCardById;
    private final UpdateCardUseCase updateCard;
    private final ValidateCardUseCase validateCard;
    private final StringResourceManager resourceManager;
    private final CardUiMapper mapper;
    private final SchedulerProvider schedulerProvider;

    @Inject
    public EditCardPresenter(GetCardByIdUseCase getCardById, UpdateCardUseCase updateCard, ValidateCardUseCase validateCard, StringResourceManager resourceManager, CardUiMapper mapper, SchedulerProvider schedulerProvider) {
        this.getCardById = getCardById;
        this.updateCard = updateCard;
        this.validateCard = validateCard;
        this.resourceManager = resourceManager;
        this.mapper = mapper;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    protected EditCardContract.EditCardState createInitialState() {
        return new EditCardContract.EditCardState.Idle();
    }

    @Override
    protected void handleEvent(EditCardContract.EditCardEvent event) {
        if (event instanceof EditCardContract.EditCardEvent.OnCardLoad onCardLoad) {
            getCard(onCardLoad.id());
        } else if (event instanceof EditCardContract.EditCardEvent.OnUpdateButtonPress onSaveButtonPress) {
            updateCard(onSaveButtonPress.card());
        }
    }

    private void getCard(int id) {
        var disposable = getCardById.invoke(id)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .doOnError(throwable -> setEffect(() -> new EditCardContract.EditCardEffect.ShowError(throwable.getLocalizedMessage())))
                .doOnSubscribe(subscription -> setState(current -> new EditCardContract.EditCardState.Loading()))
                .subscribe(
                        card -> setState(current -> new EditCardContract.EditCardState.Success(mapper.toUiModel(card))),
                        throwable -> setState(current -> new EditCardContract.EditCardState.Error())
                );
        disposables.add(disposable);
    }

    private void updateCard(Card newCard) {
        if (validateCard.invoke(newCard)) {
            if (getCurrentState() instanceof EditCardContract.EditCardState.Success current) {

                var disposable = updateCard.invoke(mapper.fromUiModel(current.card()), newCard)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.main())
                        .subscribe(
                                () -> setEffect(EditCardContract.EditCardEffect.NavigateUp::new),
                                throwable -> setEffect(() -> new EditCardContract.EditCardEffect.ShowError(throwable.getLocalizedMessage()))
                        );
                disposables.add(disposable);

            } else {
                setEffect(() -> new EditCardContract.EditCardEffect.ShowError(resourceManager.string(R.string.msg_could_not_save)));
            }
        } else {
            setEffect(() -> new EditCardContract.EditCardEffect.ShowError(resourceManager.string(R.string.msg_all_fields_required)));
        }
    }

}
