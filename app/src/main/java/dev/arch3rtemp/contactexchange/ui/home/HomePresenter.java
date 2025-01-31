package dev.arch3rtemp.contactexchange.ui.home;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.db.ContactDao;
import dev.arch3rtemp.contactexchange.ui.mapper.ContactToUiMapper;
import dev.arch3rtemp.contactexchange.ui.model.ContactUi;
import dev.arch3rtemp.contactexchange.util.SchedulerProvider;
import dev.arch3rtemp.ui.util.StringResourceManager;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class HomePresenter implements HomeContract.Presenter {

    private final ContactDao contactDao;
    private final SchedulerProvider schedulers;
    private final ContactToUiMapper mapper;
    private final StringResourceManager stringManager;
    private HomeContract.View view;
    private CompositeDisposable compositeDisposable;

    @Inject
    public HomePresenter(ContactDao contactDao, SchedulerProvider schedulers, ContactToUiMapper mapper, StringResourceManager stringManager) {
        this.contactDao = contactDao;
        this.schedulers = schedulers;
        this.mapper = mapper;
        this.stringManager = stringManager;
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
                .subscribe(cards -> {
                            if (cards.isEmpty()) view.onCardsResult(new HomeContract.ViewState.Empty());
                            else view.onCardsResult(new HomeContract.ViewState.Success(mapper.toUiList(cards)));
                        },
                        throwable -> {
                            view.onCardsResult(new HomeContract.ViewState.Error(stringManager.string(R.string.msg_could_not_load_data)));
                            view.showMessage(throwable.getLocalizedMessage());
                        });
        compositeDisposable.add(disposable);
    }

    @Override
    public void getContacts() {
        var disposable = contactDao.selectScannedContacts()
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe(contacts -> {
                            if (contacts.isEmpty()) view.onContactsResult(new HomeContract.ViewState.Empty());
                            else view.onContactsResult(new HomeContract.ViewState.Success(mapper.toUiList(contacts)));
                        },
                        throwable -> {
                            view.onContactsResult(new HomeContract.ViewState.Error(stringManager.string(R.string.msg_could_not_load_data)));
                            view.showMessage(throwable.getLocalizedMessage());
                        });
        compositeDisposable.add(disposable);
    }

    @Override
    public void deleteContact(ContactUi contact) {
        var disposable = contactDao.delete(contact.id())
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe(() -> view.onContactDeleted(contact, stringManager.string(R.string.msg_contact_deleted)),
                        throwable -> view.showMessage(throwable.getLocalizedMessage()));
        compositeDisposable.add(disposable);
    }

    @Override
    public void saveContact(ContactUi contact) {
        var disposable = contactDao.insert(mapper.fromUi(contact))
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
