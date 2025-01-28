package dev.arch3rtemp.contactexchange.ui.mapper;

import java.time.ZoneId;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.db.model.Contact;
import dev.arch3rtemp.contactexchange.ui.model.ContactUi;
import dev.arch3rtemp.ui.util.TimeConverter;

public class ContactToUiMapper {
    private final TimeConverter timeConverter;

    @Inject
    public ContactToUiMapper(TimeConverter timeConverter) {
        this.timeConverter = timeConverter;
    }

    public ContactUi toUi(Contact contact) {
        return new ContactUi(
                contact.getId(),
                contact.getName(),
                contact.getJob(),
                contact.getPosition(),
                contact.getEmail(),
                contact.getPhoneMobile(),
                contact.getPhoneOffice(),
                contact.getCreatedAt(),
                timeConverter.convertLongToDateString(contact.getCreatedAt(), "dd MMM yy", Locale.getDefault(), ZoneId.systemDefault()),
                contact.getColor(),
                contact.getIsMy()
        );
    }

    public Contact fromUi(ContactUi contactUi) {
        return new Contact(
                contactUi.id(),
                contactUi.name(),
                contactUi.job(),
                contactUi.position(),
                contactUi.email(),
                contactUi.phoneMobile(),
                contactUi.phoneOffice(),
                contactUi.createdAt(),
                contactUi.color(),
                contactUi.isMy()
        );
    }

    public List<ContactUi> toUiList(List<Contact> contacts) {
        return contacts.stream().map(this::toUi).toList();
    }
    
    public List<Contact> fromUiList(List<ContactUi> contactUis) {
        return contactUis.stream().map(this::fromUi).toList();
    }
}
