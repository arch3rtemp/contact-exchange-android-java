package dev.arch3rtemp.ui.util;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import androidx.annotation.ColorInt;

public class ColorUtils {

    public static PorterDuffColorFilter createSrcInColorFilter(@ColorInt int color) {
        return new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
    }
}
