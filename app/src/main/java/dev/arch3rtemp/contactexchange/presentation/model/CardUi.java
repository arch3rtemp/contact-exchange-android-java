package dev.arch3rtemp.contactexchange.presentation.model;

import android.graphics.PorterDuffColorFilter;

import androidx.annotation.ColorInt;

import dev.arch3rtemp.ui.util.ColorUtils;

public record CardUi(
        int id,
        String name,
        String job,
        String position,
        String email,
        String phoneMobile,
        String phoneOffice,
        long createdAt,
        String formattedCreatedAt,
        @ColorInt int color,
        boolean isMy
) {
    public PorterDuffColorFilter getSrcInColorFilter() {
        return ColorUtils.createSrcInColorFilter(color);
    }

    public String formatInitials() {
        if (name.contains(" ")) {
            var spaceIndex = name.indexOf(" ") + 1;
            var firstLetter = name.substring(0, 1);
            var secondLetter = name.substring(spaceIndex, spaceIndex + 1);
            return firstLetter + secondLetter;
        } else {
            return name.substring(0, 1);
        }
    }
}
