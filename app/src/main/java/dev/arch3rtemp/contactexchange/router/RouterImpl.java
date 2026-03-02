package dev.arch3rtemp.contactexchange.router;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.R;

public class RouterImpl implements Router {
    private final WeakReference<FragmentManager> fragmentManager;

    @Inject
    public RouterImpl(FragmentManager fragmentManager) {
        this.fragmentManager = new WeakReference<>(fragmentManager);
    }

    @Override
    public void navigate(Fragment fragment, boolean animation, boolean addToBackstack) {
        var transaction = fragmentManager.get().beginTransaction();
        if (animation) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        }
        transaction.replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName());
        if (addToBackstack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        transaction.commit();
    }
}
