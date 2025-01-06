package dev.arch3rtemp.contactexchange;

import android.database.SQLException;

import java.util.List;

import dev.arch3rtemp.contactexchange.domain.model.Card;

public class TestData {

    public final static int NEGATIVE_CARD_ID = -1;
    public final static int ZERO_CARD_ID = 0;
    private final static long SIMULATED_CREATED_TIME_1 = 1736190485327L;
    private final static long SIMULATED_CREATED_TIME_2 = 1736190485364L;

    public static Card testMyCard = new Card(
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

    public static Card testScannedCard = new Card(
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

    public static Card testNewCard = new Card(
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

    public static Card mergedCard = new Card(
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

    public static List<Card> testCards = List.of(testMyCard, testScannedCard);

    public static SQLException sqlException = new SQLException("Database error");

}
