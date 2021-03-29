package com.example.contactsexchangejava.ui.card;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.contactsexchangejava.R;
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
    public void setBackgroundColorWithAnimationAndRetainShape(final int currentColor, final int finalColor, final Drawable background) {

        ValueAnimator valueAnimator = ValueAnimator.ofArgb(currentColor, finalColor);
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (background instanceof ShapeDrawable)
                    ((ShapeDrawable) background.mutate()).getPaint().setColor((Integer) valueAnimator.getAnimatedValue());
                else if (background instanceof GradientDrawable)
                    ((GradientDrawable) background.mutate()).setColor((Integer) valueAnimator.getAnimatedValue());
                else if (background instanceof ColorDrawable)
                    ((ColorDrawable) background).setColor((Integer) valueAnimator.getAnimatedValue());
                else
                    Log.w("TAG", "Not a valid background type");
            }
        });
        valueAnimator.start();

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
