package dev.arch3rtemp.contactexchange.ui.card;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;

import dev.arch3rtemp.contactexchange.db.AppDatabase;
import dev.arch3rtemp.contactexchange.db.models.Contact;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
