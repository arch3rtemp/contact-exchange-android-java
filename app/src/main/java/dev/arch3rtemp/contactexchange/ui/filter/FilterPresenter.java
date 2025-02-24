package dev.arch3rtemp.contactexchange.ui.filter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.db.ContactDao;
import dev.arch3rtemp.contactexchange.ui.mapper.ContactToUiMapper;
import dev.arch3rtemp.contactexchange.ui.model.ContactUi;
import dev.arch3rtemp.contactexchange.util.SchedulerProvider;
import dev.arch3rtemp.ui.util.StringResourceManager;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class FilterPresenter implements FilterContract.Presenter {
    private final ContactDao contactDao;
    private final SchedulerProvider schedulers;
    private final ContactToUiMapper mapper;
    private final StringResourceManager stringManager;
    private FilterContract.View view;
    private CompositeDisposable compositeDisposable;
    private List<ContactUi> unfiltered;

    @Inject
    public FilterPresenter(ContactDao contactDao, SchedulerProvider schedulers, ContactToUiMapper mapper, StringResourceManager stringManager) {
        this.contactDao = contactDao;
        this.schedulers = schedulers;
        this.mapper = mapper;
        this.stringManager = stringManager;
    }

    @Override
    public void onCreate(FilterContract.View view) {
        this.view = view;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getContacts() {
        var disposable = contactDao.selectScannedContacts()
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe(contacts -> {
                            unfiltered = mapper.toUiList(contacts);
                            view.onGetContacts(unfiltered);
                        },
                        throwable -> view.showMessage(throwable.getLocalizedMessage()));
        compositeDisposable.add(disposable);
    }

    @Override
    public void deleteContact(int id) {
        var disposable = contactDao.delete(id)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe(() -> view.showMessage(stringManager.string(R.string.msg_contact_deleted)),
                        throwable -> view.showMessage(throwable.getLocalizedMessage()));
        compositeDisposable.add(disposable);
    }

    @Override
    public List<ContactUi> filterContacts(String query) {
        List<ContactUi> filteredContacts = new ArrayList<>();
        String filterPattern = query.toLowerCase().trim();
        for (ContactUi row : unfiltered) {
            if (row.name().toLowerCase().contains(filterPattern)) {
                filteredContacts.add(row);
            }
        }
        return filteredContacts;
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        this.view = null;
    }
}
