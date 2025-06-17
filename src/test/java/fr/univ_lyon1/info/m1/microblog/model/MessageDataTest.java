package fr.univ_lyon1.info.m1.microblog.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the MessageData class.
 */
public class MessageDataTest {

    /**
     * Tests if a user can be added to the bookmarked users set.
     */
    @Test
    public void testAddBookmarkedUser() {
        MessageData messageData = new MessageData();
        User user = new User("testUser");

        messageData.addBookmarkedUser(user);

        assertTrue(messageData.getBookmarkedUsers().contains(user),
                "User should be added to the bookmarked users set.");
    }

    /**
     * Tests if a user can be removed from the bookmarked users set.
     */
    @Test
    public void testRemoveBookmarkedUser() {
        MessageData messageData = new MessageData();
        User user = new User("testUser");

        messageData.addBookmarkedUser(user);
        messageData.removeBookmarkedUser(user);

        assertFalse(messageData.isBookmarked(),
                "Message should no longer be bookmarked after all users are removed.");
        assertFalse(messageData.getBookmarkedUsers().contains(user),
                "User should be removed from the bookmarked users set.");
    }

    /**
     * Tests bookmarking with multiple users and removing one of them.
     */
    @Test
    public void testMultipleBookmarkedUsers() {
        MessageData messageData = new MessageData();
        User user1 = new User("user1");
        User user2 = new User("user2");

        messageData.addBookmarkedUser(user1);
        messageData.addBookmarkedUser(user2);
        messageData.removeBookmarkedUser(user1);

        assertTrue(messageData.isBookmarked(),
                "Message should remain bookmarked as long as there is at least one user.");
        assertFalse(messageData.getBookmarkedUsers().contains(user1),
                "Removed user should not be in the bookmarked users set.");
        assertTrue(messageData.getBookmarkedUsers().contains(user2),
                "Remaining user should still be in the bookmarked users set.");
    }

    /**
     * Tests setting and getting words for a message.
     */
    @Test
    public void testSetAndGetWords() {
        MessageData messageData = new MessageData();
        Set<String> words = new HashSet<>();
        words.add("test");
        words.add("microblog");

        messageData.setWords(words);

        assertEquals(2, messageData.getWords().size(),
                "The set of words should contain exactly 2 elements.");
        assertTrue(messageData.getWords().contains("test"),
                "The word 'test' should be present in the words set.");
        assertTrue(messageData.getWords().contains("microblog"),
                "The word 'microblog' should be present in the words set.");
    }

    /**
     * Tests the compare method prioritizing bookmarked messages.
     */
    @Test
    public void testCompareWithBookmark() {
        MessageData message1 = new MessageData();
        MessageData message2 = new MessageData();

        User user = new User("testUser");
        message1.setBookmarked(true);

        int result = message1.compare(message2);

        assertTrue(result > 0,
                "A bookmarked message should be considered greater than a non-bookmarked one.");
    }

    /**
     * Tests the compare method with different scores.
     */
    @Test
    public void testCompareByScore() {
        MessageData message1 = new MessageData();
        MessageData message2 = new MessageData();

        message1.setScore(10);
        message2.setScore(5);

        int result = message1.compare(message2);

        assertTrue(result > 0, "The message with the higher score should be considered greater.");
    }

    /**
     * Tests the compare method with equal scores and bookmarks.
     */
    @Test
    public void testCompareWithEqualScoresAndBookmarks() {
        MessageData message1 = new MessageData();
        MessageData message2 = new MessageData();

        message1.setScore(10);
        message2.setScore(10);

        int result = message1.compare(message2);

        assertEquals(0, result,
                "Messages with equal scores and bookmark statuses should be considered equal.");
    }

    /**
     * Tests the date initialization of a message.
     */
    @Test
    public void testDateInitialization() {
        MessageData messageData = new MessageData();
        Date now = new Date();

        assertNotNull(messageData.getDate(), "Date of creation should not be null.");
        assertTrue(messageData.getDate().before(now) || messageData.getDate().equals(now),
                "Date of creation should be initialized to the current date or earlier.");
    }
}
