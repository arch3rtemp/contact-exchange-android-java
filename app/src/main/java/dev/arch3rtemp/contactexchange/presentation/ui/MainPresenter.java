package dev.arch3rtemp.contactexchange.presentation.ui;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.domain.usecase.SaveCardUseCase;

import javax.inject.Inject;

import dev.arch3rtemp.ui.base.BasePresenter;
import dev.arch3rtemp.ui.util.StringResourceManager;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainPresenter extends BasePresenter<MainContract.MainEvent, MainContract.MainEffect, MainContract.MainState> {

    private final SaveCardUseCase saveCardUseCase;
    private final StringResourceManager resourceManager;

    @Inject
    public MainPresenter(SaveCardUseCase saveCardUseCase, StringResourceManager resourceManager) {
        this.saveCardUseCase = saveCardUseCase;
        this.resourceManager = resourceManager;
    }

    @Override
    protected MainContract.MainState createInitialState() {
        return new MainContract.MainState();
    }

    @Override
    protected void handleEvent(MainContract.MainEvent mainEvent) {
        if (mainEvent instanceof MainContract.MainEvent.OnQrScanCanceled onQrScanCanceled) {
            showMessage(onQrScanCanceled.message());
        } else if (mainEvent instanceof MainContract.MainEvent.OnQrScanComplete onQrScanComplete) {
            createCard(onQrScanComplete.card());
        } else if (mainEvent instanceof MainContract.MainEvent.OnQrScanFail onQrScanFail) {
            showMessage(onQrScanFail.message());
        } else if (mainEvent instanceof MainContract.MainEvent.OnJsonParseFail onJsonParseFail) {
            showMessage(onJsonParseFail.message());
        }
    }

    private void createCard(Card card) {
        var disposable = saveCardUseCase.invoke(card)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> showMessage(throwable.getLocalizedMessage()))
                .doOnComplete(() -> showMessage(resourceManager.string(R.string.msg_contact_added)))
                .subscribe();
        disposables.add(disposable);
    }

    private void showMessage(String message) {
        setEffect(() -> new MainContract.MainEffect.ShowMessage(message));
    }
}
