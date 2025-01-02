package dev.arch3rtemp.ui.util;

import android.graphics.Point;
import android.os.Build;
import android.view.WindowManager;

import androidx.core.util.Pair;

import javax.inject.Inject;

public class DeviceSizeResolver {
    @Inject
    public DeviceSizeResolver() {}
    
    public Pair<Integer, Integer> resolve(WindowManager wManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            var x = wManager.getCurrentWindowMetrics().getBounds().width();
            var y = wManager.getCurrentWindowMetrics().getBounds().height();

            return new Pair<>(x, y);
        } else {
            @SuppressWarnings("deprecation")
            var size = new Point();
            wManager.getDefaultDisplay().getSize(size);

            return new Pair<>(size.x, size.y);
        }
    }
}
