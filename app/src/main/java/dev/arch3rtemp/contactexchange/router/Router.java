package dev.arch3rtemp.contactexchange.router;

import androidx.fragment.app.Fragment;

public interface Router {
    void navigate(Fragment fragment, boolean animation, boolean addToBackstack);
}
