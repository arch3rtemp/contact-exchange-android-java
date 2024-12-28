package dev.arch3rtemp.contactexchange.domain.model;

import java.util.Date;

public record Card(
        int id,
        String name,
        String job,
        String position,
        String email,
        String phoneMobile,
        String phoneOffice,
        Date createDate,
        int color,
        boolean isMy
) {
}
