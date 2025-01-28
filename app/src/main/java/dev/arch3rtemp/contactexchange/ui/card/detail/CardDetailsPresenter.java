package dev.arch3rtemp.contactexchange.ui.card.detail;

import dev.arch3rtemp.contactexchange.db.ContactDao;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CardDetailsPresenter implements CardDetailsContract.Presenter {

    private CardDetailsContract.View view;
    private final ContactDao contactDao;
    private CompositeDisposable compositeDisposable;

    public CardDetailsPresenter(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    @Override
    public void onCreate(CardDetailsContract.View view) {
        this.view = view;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getContactById(int id) {
        var disposable = contactDao.selectContactById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contact -> view.onCardLoaded(contact),
                        throwable -> view.showMessage(throwable.getLocalizedMessage()));

        compositeDisposable.add(disposable);
    }

    @Override
    public void deleteContact(int id) {
        var disposable = contactDao.delete(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        this.view = null;
    }
}
