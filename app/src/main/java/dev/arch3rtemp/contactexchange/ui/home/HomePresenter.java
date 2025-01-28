package dev.arch3rtemp.contactexchange.ui.home;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.db.ContactDao;
import dev.arch3rtemp.contactexchange.ui.mapper.ContactToUiMapper;
import dev.arch3rtemp.contactexchange.util.SchedulerProvider;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class HomePresenter implements HomeContract.Presenter {

    private final ContactDao contactDao;
    private final SchedulerProvider schedulers;
    private final ContactToUiMapper mapper;
    private HomeContract.View view;
    private CompositeDisposable compositeDisposable;

    @Inject
    public HomePresenter(ContactDao contactDao, SchedulerProvider schedulers, ContactToUiMapper mapper) {
        this.contactDao = contactDao;
        this.schedulers = schedulers;
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
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe(cards -> view.onGetMyCards(mapper.toUiList(cards)),
                        throwable -> view.showMessage(throwable.getLocalizedMessage()));
        compositeDisposable.add(disposable);
    }

    @Override
    public void getContacts() {
        var disposable = contactDao.selectScannedContacts()
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe(contacts -> view.onGetContacts(mapper.toUiList(contacts)),
                        throwable -> view.showMessage(throwable.getLocalizedMessage()));
        compositeDisposable.add(disposable);
    }

    @Override
    public void deleteContact(int id) {
        var disposable = contactDao.delete(id)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
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
