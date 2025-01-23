package dev.arch3rtemp.contactexchange.ui.card;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;

import dev.arch3rtemp.contactexchange.db.AppDatabase;
import dev.arch3rtemp.contactexchange.db.models.Contact;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CardPresenter implements ICardContract.Presenter {

    private ICardContract.View view;
    private AppDatabase appDatabase;
    private CompositeDisposable compositeDisposable;

    public CardPresenter(ICardContract.View view) {
        this.view = view;
    }

    @Override
    public void onViewCreated(Context context) {
        appDatabase = AppDatabase.getDBInstance(context.getApplicationContext());
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getContactById(int id) {
        appDatabase.contactDao().getContactById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Contact>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Contact contact) {
                        view.onCardLoaded(contact);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void setBackgroundColorAndRetainShape(final int color, final Drawable background) {
        if (background instanceof ShapeDrawable)
            ((ShapeDrawable) background.mutate()).getPaint().setColor(color);
        else if (background instanceof GradientDrawable)
            ((GradientDrawable) background.mutate()).setColor(color);
        else if (background instanceof ColorDrawable)
            ((ColorDrawable) background).setColor(color);
        else
            Log.w("TAG", "Not a valid background type");
    }

    @Override
    public void deleteContact(int id) {
        new Thread(() -> appDatabase.contactDao().delete(id)).start();
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        this.view = null;
    }
}
