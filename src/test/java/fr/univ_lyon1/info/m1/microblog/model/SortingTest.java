package fr.univ_lyon1.info.m1.microblog.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import fr.univ_lyon1.info.m1.microblog.model.sorting.DateSortingStrategy;
import fr.univ_lyon1.info.m1.microblog.model.sorting.ScoreSortingStrategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * Unit tests for the sorting strategies.
 */
public class SortingTest {

    private User user;
    private DateSortingStrategy dateSortingStrategy;
    private ScoreSortingStrategy scoreSortingStrategy;
    private Message message1;
    private Message message2;
    private MessageData messageData1;
    private MessageData messageData2;

    /**
     * Setup method to initialize objects before each test.
     */
    @BeforeEach
    public void setUp() {
        user = new User("mariam la queen");

        dateSortingStrategy = new DateSortingStrategy();
        scoreSortingStrategy = new ScoreSortingStrategy();

        message1 = new Message(0, "oui");
        message2 = new Message(1, "non");

        messageData1 = new MessageData();
        messageData2 = new MessageData();

        messageData1.setDateCreation(new Date()); // mtn
        messageData2.setDateCreation(new Date(System.currentTimeMillis() - 86400000)); // hier

        messageData1.setScore(10);
        messageData2.setScore(20);
    }

    /**
     * Test DateSortingStrategy with MessageData.
     */
    @Test
    public void testDateSortingStrategy() {
        Map<Message, MessageData> messagesMap = new HashMap<>();
        messagesMap.put(message1, messageData1);
        messagesMap.put(message2, messageData2);

        List<Map.Entry<Message, MessageData>> sortedMessages =
                new ArrayList<>(messagesMap.entrySet());
        sortedMessages.sort(dateSortingStrategy.getComparator(user));

        assertThat(sortedMessages.get(0).getValue().getDate(), is(messageData1.getDate()));
        assertThat(sortedMessages.get(1).getValue().getDate(), is(messageData2.getDate()));
    }

    /**
     * Test ScoreSortingStrategy with MessageData.
     */
    @Test
    public void testScoreSortingStrategy() {
        Map<Message, MessageData> messagesMap = new HashMap<>();
        messagesMap.put(message1, messageData1);
        messagesMap.put(message2, messageData2);

        List<Map.Entry<Message, MessageData>> sortedMessages =
                new ArrayList<>(messagesMap.entrySet());
        sortedMessages.sort(scoreSortingStrategy.getComparator(user));

        assertThat(sortedMessages.get(0).getValue().getScore(), is(messageData2.getScore()));
        assertThat(sortedMessages.get(1).getValue().getScore(), is(messageData1.getScore()));
    }

    /**
     * Test DateSortingStrategy when the dates are the same.
     */
    @Test
    public void testDateSortingWithSameDate() {
        Date sameDate = new Date(); // Utilisation de la date actuelle pour l'exemple
        messageData1.setDateCreation(sameDate);
        messageData2.setDateCreation(sameDate);

        Map<Message, MessageData> messagesMap = new HashMap<>();
        messagesMap.put(message1, messageData1);
        messagesMap.put(message2, messageData2);

        List<Map.Entry<Message, MessageData>> sortedMessages =
                new ArrayList<>(messagesMap.entrySet());
        sortedMessages.sort(dateSortingStrategy.getComparator(user));

        assertThat(sortedMessages.get(0).getValue().getDate(), is(sameDate));
        assertThat(sortedMessages.get(1).getValue().getDate(), is(sameDate));
    }

    /**
     * Test ScoreSortingStrategy when the scores are the same.
     */
    @Test
    public void testScoreSortingWithSameScore() {
        messageData1.setScore(15);
        messageData2.setScore(15);

        Map<Message, MessageData> messagesMap = new HashMap<>();
        messagesMap.put(message1, messageData1);
        messagesMap.put(message2, messageData2);

        List<Map.Entry<Message, MessageData>> sortedMessages =
                new ArrayList<>(messagesMap.entrySet());
        sortedMessages.sort(scoreSortingStrategy.getComparator(user));

        assertThat(sortedMessages.get(0).getValue().getScore(), is(15));
        assertThat(sortedMessages.get(1).getValue().getScore(), is(15));
    }

    /**
     * Test if an empty map is handled correctly.
     */
    @Test
    public void testEmptyMap() {
        Map<Message, MessageData> messagesMap = new HashMap<>();

        List<Map.Entry<Message, MessageData>> sortedMessages =
                new ArrayList<>(messagesMap.entrySet());
        sortedMessages.sort(dateSortingStrategy.getComparator(user));

        assertThat(sortedMessages, is(empty()));
    }
}
