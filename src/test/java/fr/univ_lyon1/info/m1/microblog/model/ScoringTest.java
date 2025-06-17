package fr.univ_lyon1.info.m1.microblog.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.contains;

import fr.univ_lyon1.info.m1.microblog.model.scoring.DateScoring;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.univ_lyon1.info.m1.microblog.model.scoring.BookmarkScoring;
import fr.univ_lyon1.info.m1.microblog.model.scoring.MalusScoring;
import fr.univ_lyon1.info.m1.microblog.model.scoring.ScoringSystem;

/**
 * Test scoring system.
 */
class ScoringTest {

    private final Map<Message, MessageData> msgs = new HashMap<>();
    private final ScoringSystem scoringSystem = new ScoringSystem();
    private Message m1;
    private Message m2;
    private Message m3;
    private Message m4;

    /**
     * Setup method to initialize test messages.
     */
    @BeforeEach
    public void setUp() {
        m1 = new Message(0, "Hello, world!");
        add(msgs, m1);
        m2 = new Message(1, "Hello, you!");
        add(msgs, m2);
        m3 = new Message(2, "Macron");
        add(msgs, m3);
        m4 = new Message(3, "Israel!");
        add(msgs, m4);
    }

    /**
     * Test the BookmarkScoring strategy.
     */
    @Test
    void testBookmarkScoring() {
        scoringSystem.addRule(new BookmarkScoring());

        msgs.get(m1).setBookmarked(true);

        scoringSystem.calculateTotalScore(msgs, new User("testUser"));

        assertThat(msgs.get(m1).getScore(), is(1));
        assertThat(msgs.get(m2).getScore(), is(1));
        assertThat(msgs.get(m3).getScore(), is(1));
    }

    /**
     * Test the DateScoring strategy with different time ranges.
     */
    @Test
    void testDateScoring() {
        scoringSystem.addRule(new DateScoring());

        msgs.get(m1).setDateCreation(
                new Date(System.currentTimeMillis() - 3600000)); // il y a 1h (- de 24h)
        msgs.get(m2).setDateCreation(
                new Date(System.currentTimeMillis() - 172800000)); // il y a 2j (- de 7j)
        msgs.get(m3).setDateCreation(
                new Date(System.currentTimeMillis() - 1209600000)); // il y a 14j  (+ de 7j)

        scoringSystem.calculateTotalScore(msgs, new User("testUser"));

        assertThat(msgs.get(m1).getScore(), is(3)); // 1 default + 2 bonus
        assertThat(msgs.get(m2).getScore(), is(2)); // 1 default + 1 bonus
        assertThat(msgs.get(m3).getScore(), is(1)); // 1 default, pas de bonus
    }

    /**
     * Test the MalusScoring strategy for penalizing specific words.
     */
    @Test
    void testMalusScoring() {
        scoringSystem.addRule(new MalusScoring());

        scoringSystem.calculateTotalScore(msgs, new User("testUser"));

        assertThat(msgs.get(m1).getScore(), is(1));
        assertThat(msgs.get(m2).getScore(), is(1));
        assertThat(msgs.get(m3).getScore(), is(-1)); // "Macron"
    }

    /**
     * Test edge cases for the DateScoring strategy.
     */
    @Test
    void testDateScoringEdgeCase() {
        scoringSystem.addRule(new DateScoring());

        // message posté il y a EXACTEMENT 7 jours
        msgs.get(m1).setDateCreation(
                new Date(System.currentTimeMillis() - 604800000)); // 7 jours
        // message posté il y a EXACTEMENT 24 heures
        msgs.get(m2).setDateCreation(
                new Date(System.currentTimeMillis() - 86400000)); // 24 heures

        scoringSystem.calculateTotalScore(msgs, new User("testUser"));

        assertThat(msgs.get(m1).getScore(), is(2)); // 1 default + 1 (7 days)
        assertThat(msgs.get(m2).getScore(), is(3)); // 1 default + 2 (24 hours)
    }

    /**
     * Test edge cases for the MalusScoring strategy.
     * Ensures words with punctuation are still recognized.
     */
    @Test
    void testMalusScoringEdgeCase() {
        scoringSystem.addRule(new MalusScoring());

        scoringSystem.calculateTotalScore(msgs, new User("testUser"));

        assertThat(msgs.get(m4).getScore(), is(-1)); // -2 malus +1 default
    }

    /**
     * Test the sorting of messages based on their scoring.
     */
    @Test
    void testSortMessagesWithScoringSystem() {
        // Given
        Map<Message, MessageData> msgs = new HashMap<>();

        Message m1 = new Message(0, "Hello, world!");
        add(msgs, m1);
        Message m2 = new Message(1, "Hello, you!");
        add(msgs, m2);
        Message m3 = new Message(2, "Macron");
        add(msgs, m3);

        msgs.get(m1).setBookmarked(true);

        ScoringSystem scoringSystem = new ScoringSystem();
        scoringSystem.addRule(new BookmarkScoring());
        scoringSystem.addRule(new DateScoring());
        scoringSystem.addRule(new MalusScoring());

        scoringSystem.calculateTotalScore(msgs, new User("testUser"));

        assertThat(msgs.get(m1).getScore(), is(3));
        assertThat(msgs.get(m2).getScore(), is(3));
        assertThat(msgs.get(m3).getScore(), is(1));

        List<Message> sorted = msgs.entrySet()
                .stream()
                .sorted(Collections.reverseOrder((Entry<Message, MessageData> left,
                                                  Entry<Message, MessageData> right) -> {
                    return left.getValue().compare(right.getValue());
                }))
                .map(Entry::getKey)
                .collect(Collectors.toList());

        assertThat(sorted, contains(m1, m2, m3));
    }

    /**
     * Test the MalusScoring strategy with different letter cases.
     */
    @Test
    void testMalusScoringCaseSensitivity() {
        scoringSystem.addRule(new MalusScoring());

        Message m5 = new Message(4, "macron"); // Lowercase
        Message m6 = new Message(5, "MACRON"); // Uppercase
        add(msgs, m5);
        add(msgs, m6);

        scoringSystem.calculateTotalScore(msgs, new User("testUser"));

        assertThat(msgs.get(m3).getScore(), is(-1)); // "Macron"
        assertThat(msgs.get(m5).getScore(), is(-1));  // "macron"
        assertThat(msgs.get(m6).getScore(), is(-1));  // "MACRON"
    }

    /**
     * Helper method to add messages to the map.
     *
     * @param msgs the map of messages and their data
     * @param m the message to add
     */
    private void add(final Map<Message, MessageData> msgs, final Message m) {
        msgs.put(m, new MessageData());
    }
}
