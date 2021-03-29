package com.example.contactsexchangejava.ui.card;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.contactsexchangejava.R;
import com.example.contactsexchangejava.db.AppDatabase;
import com.example.contactsexchangejava.db.DataManager;
import com.example.contactsexchangejava.db.models.Contact;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.MaterialShapeDrawable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
