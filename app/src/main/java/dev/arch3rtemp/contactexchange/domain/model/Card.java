package dev.arch3rtemp.contactexchange.domain.model;

public record Card(
        int id,
        String name,
        String job,
        String position,
        String email,
        String phoneMobile,
        String phoneOffice,
        long createdAt,
        int color,
        boolean isMy
) {}
