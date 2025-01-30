package dev.arch3rtemp.contactexchange.ui.createoredit;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.db.ContactDao;
import dev.arch3rtemp.contactexchange.db.model.Contact;
import dev.arch3rtemp.contactexchange.util.SchedulerProvider;
import dev.arch3rtemp.ui.util.StringResourceManager;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class CreateOrEditCardPresenter implements CreateOrEditCardContract.Presenter {

    private final ContactDao contactDao;
    private final SchedulerProvider schedulers;
    private final StringResourceManager stringManager;
    private CreateOrEditCardContract.View view;
    private CompositeDisposable compositeDisposable;

    @Inject
    public CreateOrEditCardPresenter(ContactDao contactDao, SchedulerProvider schedulers, StringResourceManager stringManager) {
        this.contactDao = contactDao;
        this.schedulers = schedulers;
        this.stringManager = stringManager;
    }

    @Override
    public void onCreate(CreateOrEditCardContract.View view) {
        this.view = view;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void createContact(Contact contact) {
        var disposable = contactDao.insert(contact)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe(() -> view.showToastMessage(stringManager.string(R.string.msg_contact_created)),
                        throwable -> view.showToastMessage(throwable.getLocalizedMessage()));

        compositeDisposable.add(disposable);
    }

    @Override
    public void editContact(Contact contact) {
        var disposable = contactDao.update(contact)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe(() -> view.showToastMessage(stringManager.string(R.string.msg_contact_updated)),
                        throwable -> view.showToastMessage(throwable.getLocalizedMessage()));

        compositeDisposable.add(disposable);
    }

    @Override
    public void getContactById(int id) {
        var disposable = contactDao.selectContactById(id)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe(contact -> view.onGetContactById(contact),
                        throwable -> view.showToastMessage(throwable.getLocalizedMessage()));

        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        this.view = null;
    }
}
