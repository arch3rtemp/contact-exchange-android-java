package com.example.contactsexchangejava.ui.card;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;

import com.example.contactsexchangejava.db.AppDatabase;
import com.example.contactsexchangejava.db.DataManager;
import com.example.contactsexchangejava.db.models.Contact;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CreateOrEditCardPresenter implements ICreateOrEditCardContract.Presenter {

    private ICreateOrEditCardContract.View view;
    private CompositeDisposable compositeDisposable;
    private AppDatabase appDatabase;


    public CreateOrEditCardPresenter(ICreateOrEditCardContract.View view) {
        this.view = view;
    }

    @Override
    public void onViewCreated(Context context) {
        appDatabase = AppDatabase.getDBInstance(context.getApplicationContext());
        compositeDisposable = new CompositeDisposable();
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
    public void createContact(Contact contact) {
        new Thread(() -> appDatabase.contactDao().insert(contact)).start();
    }

    @Override
    public void editContact(Contact contact) {
        new Thread(() -> appDatabase.contactDao().update(contact)).start();
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
                        view.onGetContactById(contact);
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
    public void onDestroy() {
        compositeDisposable.clear();
        this.view = null;
    }
}
