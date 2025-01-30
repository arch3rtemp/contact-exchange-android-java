package dev.arch3rtemp.contactexchange.router;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.R;

public class RouterImpl implements Router {
    private final FragmentManager fragmentManager;

    @Inject
    public RouterImpl(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void navigate(Fragment fragment, boolean animation, boolean addToBackstack) {
        var transaction = fragmentManager.beginTransaction();
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
