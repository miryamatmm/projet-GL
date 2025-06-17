package fr.univ_lyon1.info.m1.microblog.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Message class.
 */
public class MessageTest {

    private Message message1;
    private Message message2;
    /**
     * Set up method to initialize the messages before each test.
     */
    @BeforeEach
    public void setUp() {
        message1 = new Message(0, "Hello, Microblog!");
        message2 = new Message(1, "Hello, you!");
    }

    /**
     * Tests that a Message object is correctly initialized with content.
     */
    @Test
    public void testMessageInitialization() {
        assertThat(message1, is(notNullValue()));
        assertThat(message1.getContent(), is("Hello, Microblog!"));
    }

    /**
     * Tests that a Message object is correctly initialized with an ID and content.
     */
    @Test
    public void testMessageInitializationWithId() {
        assertThat(message2, is(notNullValue()));
        assertThat(message2.getIdMessage(), is(1));
        assertThat(message2.getContent(), is("Hello, you!"));
    }
}
