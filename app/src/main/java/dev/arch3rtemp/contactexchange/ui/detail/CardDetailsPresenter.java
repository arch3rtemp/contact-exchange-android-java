package dev.arch3rtemp.contactexchange.ui.detail;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.db.ContactDao;
import dev.arch3rtemp.contactexchange.util.SchedulerProvider;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class CardDetailsPresenter implements CardDetailsContract.Presenter {

    private final ContactDao contactDao;
    private final SchedulerProvider schedulers;
    private CardDetailsContract.View view;
    private CompositeDisposable compositeDisposable;

    @Inject
    public CardDetailsPresenter(ContactDao contactDao, SchedulerProvider schedulers) {
        this.contactDao = contactDao;
        this.schedulers = schedulers;
    }

    @Override
    public void onCreate(CardDetailsContract.View view) {
        this.view = view;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getContactById(int id) {
        var disposable = contactDao.selectContactById(id)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe(contact -> view.onCardLoaded(contact),
                        throwable -> view.showMessage(throwable.getLocalizedMessage()));

        compositeDisposable.add(disposable);
    }

    @Override
    public void deleteContact(int id) {
        var disposable = contactDao.delete(id)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe();

        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        this.view = null;
    }
}
