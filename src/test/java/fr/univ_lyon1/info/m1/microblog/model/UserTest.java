package fr.univ_lyon1.info.m1.microblog.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the User class.
 */
public class UserTest {

    private User user;

    /**
     * Setup method to initialize a User object before each test.
     */
    @BeforeEach
    public void setUp() {
        user = new User("testUser");
    }

    /**
     * Test the initialization of the User object with its user tag.
     */
    @Test
    public void testUserInitialization() {
        assertThat(user.getUserTag(), is("testUser"));
    }

    /**
     * Test adding a word to the user's bookmarked words.
     */
    @Test
    public void testAddBookmarkedWord() {
        user.addBookmarkedWord("microblog");

        assertThat(user.getBookmarkedWords(), contains("microblog"));
    }

    /**
     * Test removing a word from the user's bookmarked words.
     */
    @Test
    public void testRemoveBookmarkedWord() {
        user.addBookmarkedWord("microblog");
        user.removeBookmarkedWord("microblog");

        assertThat(user.getBookmarkedWords(), not(contains("microblog")));
    }

    /**
     * Test adding multiple words and retrieving the list of bookmarked words.
     */
    @Test
    public void testAddMultipleBookmarkedWords() {
        user.addBookmarkedWord("microblog");
        user.addBookmarkedWord("hello");
        user.addBookmarkedWord("world");

        assertThat(user.getBookmarkedWords(), contains("microblog", "hello", "world"));
    }

    /**
     * Test that the list of bookmarked words is empty initially.
     */
    @Test
    public void testInitialEmptyBookmarkedWords() {
        assertThat(user.getBookmarkedWords(), is(empty()));
    }

    /**
     * Test that removing a non-existent word does not change the list.
     */
    @Test
    public void testRemoveNonExistentBookmarkedWord() {
        user.addBookmarkedWord("microblog");
        user.removeBookmarkedWord("nonexistent");

        assertThat(user.getBookmarkedWords(), contains("microblog"));
    }
}
