package dev.arch3rtemp.contactexchange.router;

import android.os.Bundle;

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
    public void navigate(Class<? extends Fragment> fragmentClass, Bundle bundle, boolean addToBackStack) {
        if (addToBackStack) {
            navigateAndAddToBackStack(fragmentClass, bundle);
        } else {
            navigateWithoutAddToBackStack(fragmentClass, bundle);
        }
    }

    private void navigateAndAddToBackStack(Class<? extends Fragment> fragmentClass, Bundle bundle) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.fl_main_frame_container, fragmentClass, bundle, fragmentClass.getSimpleName())
                .addToBackStack(fragmentClass.getSimpleName())
                .commit();
    }

    private void navigateWithoutAddToBackStack(Class<? extends Fragment> fragmentClass, Bundle bundle) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.fl_main_frame_container, fragmentClass, bundle, fragmentClass.getSimpleName())
                .commit();
    }
}
