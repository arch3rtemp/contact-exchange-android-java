package dev.arch3rtemp.contactexchange.ui.model;

import androidx.annotation.ColorInt;

public record ContactUi(
        int id,
        String name,
        String job,
        String position,
        String email,
        String phoneMobile,
        String phoneOffice,
        long createdAt,
        String dateString,
        @ColorInt int color,
        boolean isMy
) {
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
