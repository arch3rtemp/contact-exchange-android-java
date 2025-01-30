package dev.arch3rtemp.contactexchange.ui.createoredit;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.db.ContactDao;
import dev.arch3rtemp.contactexchange.db.model.Contact;
import dev.arch3rtemp.contactexchange.ui.mapper.ContactToUiMapper;
import dev.arch3rtemp.contactexchange.util.SchedulerProvider;
import dev.arch3rtemp.ui.util.StringResourceManager;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class CreateOrEditCardPresenter implements CreateOrEditCardContract.Presenter {

    private final ContactDao contactDao;
    private final SchedulerProvider schedulers;
    private final ContactToUiMapper mapper;
    private final StringResourceManager stringManager;
    private CreateOrEditCardContract.View view;
    private CompositeDisposable compositeDisposable;
    private Contact card;

    @Inject
    public CreateOrEditCardPresenter(ContactDao contactDao, SchedulerProvider schedulers, ContactToUiMapper mapper, StringResourceManager stringManager) {
        this.contactDao = contactDao;
        this.schedulers = schedulers;
        this.mapper = mapper;
        this.stringManager = stringManager;
    }

    @Override
    public void onCreate(CreateOrEditCardContract.View view) {
        this.view = view;
        compositeDisposable = new CompositeDisposable();
    }

    private boolean containsBlankField(Contact contact) {
        return contact.getName().isEmpty() || contact.getJob().isEmpty() || contact.getPosition().isEmpty() || contact.getEmail().isEmpty() || contact.getPhoneMobile().isEmpty() || contact.getPhoneOffice().isEmpty();
    }

    @Override
    public void createContact(Contact contact) {
        if (containsBlankField(contact)) {
            view.showMessage(stringManager.string(R.string.msg_all_fields_required));
            return;
        }

        var disposable = contactDao.insert(contact)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe(() -> {
                            view.showMessage(stringManager.string(R.string.msg_contact_created));
                            view.navigateUp();
                        },
                        throwable -> view.showMessage(throwable.getLocalizedMessage())
                );

        compositeDisposable.add(disposable);
    }

    @Override
    public void editContact(Contact contact) {
        if (containsBlankField(contact)) {
            view.showMessage(stringManager.string(R.string.msg_all_fields_required));
            return;
        }

        var disposable = contactDao.update(contact)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe(() -> {
                            view.showMessage(stringManager.string(R.string.msg_contact_updated));
                            view.navigateUp();
                        }, throwable -> view.showMessage(throwable.getLocalizedMessage())
                );

        compositeDisposable.add(disposable);
    }

    @Override
    public void getContactById(int id) {
        var disposable = contactDao.selectContactById(id)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe(contact -> {
                            card = contact;
                            view.onGetContactById(mapper.toUi(contact));
                        },
                        throwable -> view.showMessage(throwable.getLocalizedMessage()));

        compositeDisposable.add(disposable);
    }

    @Override
    public Contact getCurrentCard() {
        return card;
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        this.view = null;
    }
}
