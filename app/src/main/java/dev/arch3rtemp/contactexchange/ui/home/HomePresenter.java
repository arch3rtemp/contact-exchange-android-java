package dev.arch3rtemp.contactexchange.ui.home;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.db.ContactDao;
import dev.arch3rtemp.contactexchange.ui.mapper.ContactToUiMapper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter {

    private final ContactDao contactDao;
    private final ContactToUiMapper mapper;
    private HomeContract.View view;
    private CompositeDisposable compositeDisposable;

    @Inject
    public HomePresenter(ContactDao contactDao, ContactToUiMapper mapper) {
        this.contactDao = contactDao;
        this.mapper = mapper;
    }

    @Override
    public void onCreate(HomeContract.View view) {
        this.view = view;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getMyCards() {
        var disposable = contactDao.selectMyCards()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cards -> view.onGetMyCards(mapper.toUiList(cards)),
                        throwable -> view.showMessage(throwable.getLocalizedMessage()));
        compositeDisposable.add(disposable);
    }

    @Override
    public void getContacts() {
        var disposable = contactDao.selectScannedContacts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts -> view.onGetContacts(mapper.toUiList(contacts)),
                        throwable -> view.showMessage(throwable.getLocalizedMessage()));
        compositeDisposable.add(disposable);
    }

    @Override
    public void deleteContact(int id) {
        var disposable = contactDao.delete(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {},
                        throwable -> view.showMessage(throwable.getLocalizedMessage()));
        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        this.view = null;
    }
}
