package dev.arch3rtemp.contactexchange.presentation.ui;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.domain.usecase.SaveCardUseCase;
import dev.arch3rtemp.contactexchange.presentation.mapper.JsonToCardMapper;

import org.json.JSONException;

import javax.inject.Inject;

import dev.arch3rtemp.ui.base.BasePresenter;
import dev.arch3rtemp.ui.util.StringResourceManager;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainPresenter extends BasePresenter<MainContract.MainEvent, MainContract.MainEffect, MainContract.MainState> {

    private final SaveCardUseCase saveCardUseCase;
    private final JsonToCardMapper mapper;
    private final StringResourceManager resourceManager;

    @Inject
    public MainPresenter(SaveCardUseCase saveCardUseCase, JsonToCardMapper mapper, StringResourceManager resourceManager) {
        this.saveCardUseCase = saveCardUseCase;
        this.mapper = mapper;
        this.resourceManager = resourceManager;
    }

    @Override
    protected MainContract.MainState createInitialState() {
        return new MainContract.MainState();
    }

    @Override
    protected void handleEvent(MainContract.MainEvent mainEvent) {
        if (mainEvent instanceof MainContract.MainEvent.OnQrScanCanceled) {
            showMessage(resourceManager.string(R.string.msg_scan_cancelled));
        } else if (mainEvent instanceof MainContract.MainEvent.OnQrScanComplete onQrScanComplete) {
            try {
                createCard(mapper.fromJson(onQrScanComplete.cardJson()));
            } catch (JSONException e) {
                setEvent(new MainContract.MainEvent.OnJsonParseFail(e.getLocalizedMessage()));
            }
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
                .subscribe(() -> {
                    showMessage(resourceManager.string(R.string.msg_contact_added));
                }, throwable -> {
                    showMessage(throwable.getLocalizedMessage());
                });
        disposables.add(disposable);
    }

    private void showMessage(String message) {
        setEffect(() -> new MainContract.MainEffect.ShowMessage(message));
    }
}
