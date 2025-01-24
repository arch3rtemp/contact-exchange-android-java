package dev.arch3rtemp.contactexchange;

import android.database.SQLException;

import java.util.List;

import dev.arch3rtemp.contactexchange.db.models.Contact;

public class TestData {

    public final static int NEGATIVE_CONTACT_ID = -1;
    public final static int ZERO_CONTACT_ID = 0;
    private final static long SIMULATED_CREATED_TIME_1 = 1736190485327L;
    private final static long SIMULATED_CREATED_TIME_2 = 1736190485364L;

    public static Contact testMyContact = new Contact(
            9,
            "John Doe",
            "Job",
            "Developer",
            "john@example.com",
            "+15559879855",
            "+15558797882",
            SIMULATED_CREATED_TIME_1,
            0xFF0000,
            true
    );

    public static Contact testScannedContact = new Contact(
            10,
            "Jane Doe",
            "JP Morgan",
            "CEO",
            "jane@example.com",
            "+15559879853",
            "+15558797884",
            SIMULATED_CREATED_TIME_2,
            0xFF00FF,
            false
    );

    public static Contact testNewContact = new Contact(
            0,
            "Justin Doe",
            "Microsoft",
            "COO",
            "justin@example.com",
            "+15559879859",
            "+15558797889",
            -1,
            -1,
            true
    );

    public static Contact mergedContact = new Contact(
            9,
            "Justin Doe",
            "Microsoft",
            "COO",
            "justin@example.com",
            "+15559879859",
            "+15558797889",
            SIMULATED_CREATED_TIME_1,
            0xFF0000,
            true
    );

    public static Contact blankContact = new Contact(
            -1, "", "", "", "", "", "", -1, -1, false
    );

    public static List<Contact> testContacts = List.of(testMyContact, testScannedContact);

    public static SQLException sqlException = new SQLException("Database error");

    public static final String testContactJson = """
            {
              "id": 9,
              "name": "John Doe",
              "job": "Job",
              "position": "Developer",
              "email": "john@example.com",
              "phoneMobile": "+15559879855",
              "phoneOffice": "+15558797882",
              "createdAt": 1736190485327,
              "formattedCreatedAt":"06 Jan 24",
              "color": 16711680,
              "isMy": true
            }
            """;

    public static final String testContactJsonCompact = "{\"id\":9,\"name\":\"John Doe\",\"job\":\"Job\",\"position\":\"Developer\",\"email\":\"john@example.com\",\"phoneMobile\":\"+15559879855\",\"phoneOffice\":\"+15558797882\",\"createdAt\":1736190485327,\"formattedCreatedAt\":\"06 Jan 25\",\"color\":16711680,\"isMy\":true}";

}
