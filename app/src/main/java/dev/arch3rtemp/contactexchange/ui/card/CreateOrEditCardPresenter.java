package dev.arch3rtemp.contactexchange.ui.card;

import android.content.Context;

import dev.arch3rtemp.contactexchange.db.AppDatabase;
import dev.arch3rtemp.contactexchange.db.models.Contact;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CreateOrEditCardPresenter implements ICreateOrEditCardContract.Presenter {

    private ICreateOrEditCardContract.View view;
    private CompositeDisposable compositeDisposable;
    private AppDatabase appDatabase;

    public CreateOrEditCardPresenter(ICreateOrEditCardContract.View view) {
        this.view = view;
    }

    @Override
    public void onCreate(Context context) {
        appDatabase = AppDatabase.getDBInstance(context.getApplicationContext());
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void createContact(Contact contact) {
        var disposable = appDatabase.contactDao().insert(contact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.showToastMessage("Contact created"),
                        throwable -> view.showToastMessage(throwable.getLocalizedMessage()));

        compositeDisposable.add(disposable);
    }

    @Override
    public void editContact(Contact contact) {
        var disposable = appDatabase.contactDao().update(contact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.showToastMessage("Contact edited"),
                        throwable -> view.showToastMessage(throwable.getLocalizedMessage()));

        compositeDisposable.add(disposable);
    }

    @Override
    public void getContactById(int id) {
        var disposable = appDatabase.contactDao().getContactById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
