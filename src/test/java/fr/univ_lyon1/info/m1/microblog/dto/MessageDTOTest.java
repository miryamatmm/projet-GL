package fr.univ_lyon1.info.m1.microblog.dto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the MessageDTO class.
 */
public class MessageDTOTest {

    private MessageDTO messageDTO;

    /**
     * Setup method to initialize a MessageDTO object before each test.
     */
    @BeforeEach
    public void setUp() {
        messageDTO = new MessageDTO();
    }

    /**
     * Test the default constructor initializes fields correctly.
     */
    @Test
    public void testDefaultConstructor() {
        assertThat(messageDTO.getId(), is(0));
        assertThat(messageDTO.getUsertag(), is(nullValue()));
        assertThat(messageDTO.getContent(), is(nullValue()));
        assertThat(messageDTO.getBookmarkedByUsers(), is(empty()));
        assertThat(messageDTO.getCreationTimestamp(), is(0L));
    }

    /**
     * Test the parameterized constructor initializes fields correctly.
     */
    @Test
    public void testParameterizedConstructor() {
        int id = 1;
        String usertag = "@testuser";
        String content = "Hello, world!";
        long timestamp = System.currentTimeMillis();
        List<String> bookmarks = List.of("user1", "user2");

        messageDTO = new MessageDTO(id, usertag, content, timestamp, bookmarks);

        assertThat(messageDTO.getId(), is(id));
        assertThat(messageDTO.getUsertag(), is(usertag));
        assertThat(messageDTO.getContent(), is(content));
        assertThat(messageDTO.getCreationTimestamp(), is(timestamp));
        assertThat(messageDTO.getBookmarkedByUsers(), contains("user1", "user2"));
    }

    /**
     * Test setters and getters for all fields.
     */
    @Test
    public void testSettersAndGetters() {
        int id = 42;
        String usertag = "@newuser";
        String content = "New content";
        long timestamp = System.currentTimeMillis();
        List<String> bookmarks = new ArrayList<>();
        bookmarks.add("userA");

        messageDTO.setId(id);
        messageDTO.setUsertag(usertag);
        messageDTO.setContent(content);
        messageDTO.setCreationTimestamp(timestamp);
        messageDTO.setBookmarkedByUsers(bookmarks);

        assertThat(messageDTO.getId(), is(id));
        assertThat(messageDTO.getUsertag(), is(usertag));
        assertThat(messageDTO.getContent(), is(content));
        assertThat(messageDTO.getCreationTimestamp(), is(timestamp));
        assertThat(messageDTO.getBookmarkedByUsers(), contains("userA"));
    }

    /**
     * Test setting the bookmarkedByUsers field to null.
     */
    @Test
    public void testSetBookmarkedByUsersWithNull() {
        messageDTO.setBookmarkedByUsers(null);
        assertThat(messageDTO.getBookmarkedByUsers(), is(nullValue()));
    }
}
