package dev.arch3rtemp.contactexchange;

import android.database.SQLException;

import java.util.List;

import dev.arch3rtemp.contactexchange.data.model.CardEntity;
import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.presentation.model.CardUi;

public class TestData {

    /**
     * Domain test data
     */
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

    /**
     * Data test data
     */
    public static CardEntity testMyCardEntity = new CardEntity(
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

    public static CardEntity testScannedCardEntity = new CardEntity(
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

    public static CardEntity mergedCardEntity = new CardEntity(
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

    public static List<CardEntity> testCardsEntity = List.of(testMyCardEntity, testScannedCardEntity);

    public static final String testCardJson = """
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

    /**
     * Presentation test data
     */
    public static final CardUi testMyCardUi = new CardUi(
            9,
            "John Doe",
            "Job",
            "Developer",
            "john@example.com",
            "+15559879855",
            "+15558797882",
            SIMULATED_CREATED_TIME_1,
            "06 Jan 25",
            0xFF0000,
            true
    );

    public static CardUi testScannedCardUi = new CardUi(
            10,
            "Jane Doe",
            "JP Morgan",
            "CEO",
            "jane@example.com",
            "+15559879853",
            "+15558797884",
            SIMULATED_CREATED_TIME_2,
            "06 Jan 25",
            0xFF00FF,
            false
    );

    public static List<CardUi> testCardsUi = List.of(testMyCardUi, testScannedCardUi);

    public static final String testCardJsonCompact = "{\"id\":9,\"name\":\"John Doe\",\"job\":\"Job\",\"position\":\"Developer\",\"email\":\"john@example.com\",\"phoneMobile\":\"+15559879855\",\"phoneOffice\":\"+15558797882\",\"createdAt\":1736190485327,\"formattedCreatedAt\":\"06 Jan 25\",\"color\":16711680,\"isMy\":true}";

}
