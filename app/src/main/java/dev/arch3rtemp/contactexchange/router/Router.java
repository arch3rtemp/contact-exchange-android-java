package dev.arch3rtemp.contactexchange.router;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public interface Router {
    void navigate(Class<? extends Fragment> fragmentClass, Bundle bundle, boolean addToBackStack);
}
